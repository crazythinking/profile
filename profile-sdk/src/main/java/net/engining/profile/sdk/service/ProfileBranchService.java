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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final static Logger log = LoggerFactory.getLogger(ProfileBranchService.class);

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
	 * 清空分支机构下的所有Profiles信息;
	 * @param branchIds
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteProfileBranch(List<String> branchIds, String orgId) {
		//密码维护历史表
		QProfilePwdHist qProfilePwdHist = QProfilePwdHist.profilePwdHist;
		//用户角色表
		QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
		//角色表
		QProfileRole qProfileRole = QProfileRole.profileRole;
		//角色权限表
		QProfileRoleAuth qProfileRoleAuth = QProfileRoleAuth.profileRoleAuth;
		//用户表
		QProfileUser qProfileUser = QProfileUser.profileUser;
		//机构表
		QProfileBranch qProfileBranch = QProfileBranch.profileBranch;

		List<String> profileUsers = new JPAQueryFactory(em)
				.select(qProfileUser.puId)
				.from(qProfileUser)
				.where(
						qProfileUser.branchId.in(branchIds),
						qProfileUser.orgId.eq(orgId)
				)
				.fetch();
		List<String> profileRoles = new JPAQueryFactory(em)
				.select(qProfileRole.roleId)
				.from(qProfileRole)
				.where(
						qProfileRole.branchId.in(branchIds),
						qProfileRole.orgId.eq(orgId)
				)
				.fetch();

		//删除密码维护历史表
		long n1 = new JPAQueryFactory(em)
				.delete(qProfilePwdHist)
				.where(qProfilePwdHist.puId.in(profileUsers))
				.execute();
		log.debug("删除了{}条ProfilePwdHist", n1);

		//删除用户表
		long n2 = new JPAQueryFactory(em)
				.delete(qProfileUser)
				.where(qProfileUser.puId.in(profileUsers))
				.execute();
		log.debug("删除了{}条ProfileUser", n2);

		//删除用户角色表
		long n3 = new JPAQueryFactory(em)
				.delete(qProfileUserRole)
				.where(qProfileUserRole.roleId.in(profileRoles))
				.execute();
		log.debug("删除了{}条ProfileUserRole", n3);

		//删除角色权限表
		long n4 = new JPAQueryFactory(em)
				.delete(qProfileRoleAuth)
				.where(qProfileRoleAuth.roleId.in(profileRoles))
				.execute();
		log.debug("删除了{}条ProfileRoleAuth", n4);

		//删除角色表
		long n5 = new JPAQueryFactory(em)
				.delete(qProfileRole)
				.where(qProfileRole.roleId.in(profileRoles))
				.execute();
		log.debug("删除了{}条ProfileRole", n5);

		//删除机构表
		long n6 = new JPAQueryFactory(em)
				.delete(qProfileBranch)
				.where(
						qProfileBranch.branchId.in(branchIds),
						qProfileBranch.orgId.eq(orgId)
				)
				.execute();
		log.debug("删除了{}条ProfileBranch", n6);

	}
}
