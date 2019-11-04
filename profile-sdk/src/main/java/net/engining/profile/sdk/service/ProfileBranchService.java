package net.engining.profile.sdk.service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.db.querydsl.Range;
import net.engining.profile.entity.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileBranchService{
	
	@PersistenceContext
	private EntityManager em;

	public FetchResponse<ProfileBranch> fetchBranch(Range range, String superiorId, String orgId) {
		
		QProfileBranch q = QProfileBranch.profileBranch;
		JPAQuery<ProfileBranch> query = new JPAQueryFactory(em)
				.select(q)
				.from(q);
		
		if (superiorId == null)
		{
			query.where(q.superiorId.isNull());
		}
		else
		{
			query.where(q.superiorId.eq(superiorId).and(q.orgId.eq(orgId)));
		}
		
		return new JPAFetchResponseBuilder<ProfileBranch>().range(range).build(query);
	}

	public FetchResponse<ProfileBranch> fetchBranch(Range range) {
		
		QProfileBranch q = QProfileBranch.profileBranch;
		JPAQuery<ProfileBranch> query = new JPAQueryFactory(em)
				.select(q)
				.from(q);
		
		return new JPAFetchResponseBuilder<ProfileBranch>()
			.range(range)
			.build(query);
	}

	public ProfileBranch getBranch(String orgId, String branchId) {
		ProfileBranchKey profileBranchKey = new ProfileBranchKey(orgId, branchId);
		return em.find(ProfileBranch.class, profileBranchKey);
	}

	
	@Transactional(rollbackFor = Exception.class)
	public void updateBranch(ProfileBranch branch) throws ErrorMessageException {
		
		QProfileBranch q = QProfileBranch.profileBranch;

		if (Strings.isNullOrEmpty(branch.getSuperiorId()))
		{
			long n = new JPAQueryFactory(em)
					.select(q.branchId)
					.from(q)
					.where(
							q.superiorId.isNull(), 
							q.branchId.ne(branch.getBranchId()),
							q.orgId.eq(branch.getOrgId())
							)
					.fetchCount();
			if (n > 0)
			{
				throw new ErrorMessageException(ErrorCode.CheckError, MessageFormat.format("顶级分支机构只能有一个, branchId:{}", branch.getBranchId()));
			}
			branch.setSuperiorId(null);
		}
		else
		{
			long n = new JPAQueryFactory(em)
					.select(q.branchId)
					.from(q)
					.where(
							q.branchId.eq(branch.getSuperiorId()),
							q.orgId.eq(branch.getOrgId())
							)
					.fetchCount();
			if (n == 0)
			{
				throw new ErrorMessageException(ErrorCode.CheckError, MessageFormat.format("找不到上级分支, superiorId:{}", branch.getBranchId()));
			}
		}
		
		em.merge(branch);
	}

	/**
	 * 根据OrgId查询组成分支机构Map：branchId|branchName
	 * @param orgId
	 * @return
	 */
	public Map<String, String> fetchBranchNamesByOrg(String orgId) {
		QProfileBranch q = QProfileBranch.profileBranch;
		List<Tuple> queryResults = new JPAQueryFactory(em)
				.select(q.branchId, q.branchName)
				.from(q)
				.where(q.orgId.eq(orgId))
				.fetch();

		HashMap<String, String> result = Maps.newHashMapWithExpectedSize(queryResults.size());
		
		for (Tuple t : queryResults)
		{
			result.put(t.get(q.branchId), t.get(q.branchName));
		}
		return result;
	}

	/**
	 * 新建分支机构
	 * @param branch
	 * @throws ErrorMessageException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addbranch(ProfileBranch branch) throws ErrorMessageException {
		ProfileBranchKey profileBranchKey = new ProfileBranchKey(branch.getOrgId(), branch.getBranchId());
		
		if(em.find(ProfileBranch.class, profileBranchKey) != null){
			throw new ErrorMessageException(ErrorCode.CheckError, "添加分支机构失败:分支机构已存在");
		}
		em.persist(branch);
		
	}

	/**
	 * 清空分支机构下的所有Profiles信息; //TODO 如下删除效率低，重构
	 * @param branchIds
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteProfileBranch(List<String> branchIds, String orgId) {
		QProfilePwdHist qProfilePwdHist = QProfilePwdHist.profilePwdHist;
		QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
		QProfileRole qProfileRole =QProfileRole.profileRole;
		QProfileRoleAuth qProfileRoleAuth = QProfileRoleAuth.profileRoleAuth;
		QProfileUser qProfileUser =QProfileUser.profileUser;
		
		for(String branchId : branchIds)
		{
			
			List<ProfileUser> profileUsers = new JPAQueryFactory(em)
					.select(qProfileUser)
					.from(qProfileUser)
					.where(
							qProfileUser.branchId.eq(branchId),
							qProfileUser.orgId.eq(orgId)
							)
					.fetch();
			for(ProfileUser profileUser: profileUsers){
				List<ProfilePwdHist> profilePwdHists = new JPAQueryFactory(em).select(qProfilePwdHist).from(qProfilePwdHist).where(qProfilePwdHist.puId.eq(profileUser.getPuId())).fetch();
				for(ProfilePwdHist profilePwdHist : profilePwdHists){
					em.remove(profilePwdHist);
				} 
				em.remove(profileUser);
			}
			
			List<ProfileRole> profileRoles = new JPAQueryFactory(em)
					.select(qProfileRole)
					.from(qProfileRole)
					.where(
							qProfileRole.branchId.eq(branchId),
							qProfileRole.orgId.eq(orgId)
							)
					.fetch();
			for(ProfileRole profileRole: profileRoles){
				
				List<ProfileUserRole> profileUserRoles = new JPAQueryFactory(em).select(qProfileUserRole).from(qProfileUserRole).where(qProfileUserRole.roleId.eq(profileRole.getRoleId())).fetch();
				for(ProfileUserRole profileUserRole : profileUserRoles){
					em.remove(profileUserRole);
				}
				
				List<ProfileRoleAuth> profileRoleAuths = new JPAQueryFactory(em).select(qProfileRoleAuth).from(qProfileRoleAuth).where(qProfileRoleAuth.roleId.eq(profileRole.getRoleId())).fetch();
				for(ProfileRoleAuth profileRoleAuth : profileRoleAuths){
					em.remove(profileRoleAuth);
				}
				
				em.remove(profileRole);
			}
			
			ProfileBranchKey profileBranchKey = new ProfileBranchKey(orgId, branchId);
			em.remove(em.find(ProfileBranch.class, profileBranchKey));
		}	
	}
}
