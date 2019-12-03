package net.engining.profile.sdk.service;

import com.google.common.collect.Lists;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author
 */
@Service
public class ProfileRoleService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	private EntityManager em;



	public FetchResponse<ProfileRole> fetchRoles(Range range) {
		QProfileRole q = QProfileRole.profileRole;
		JPAQuery<ProfileRole> query = new JPAQueryFactory(em).select(q).from(q).orderBy(q.roleId.asc());

		return new JPAFetchResponseBuilder<ProfileRole>().range(range).build(query);
	}

	/**
	 * 删除角色
	 * @param roleIds
	 * @param appCd
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteProfileRoles(List<String> roleIds, String appCd) {

		QProfileRole qProfileRole = QProfileRole.profileRole;
		QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
		QProfileRoleAuth qProfileRoleAuth = QProfileRoleAuth.profileRoleAuth;
		
		long n1 = new JPAQueryFactory(em).delete(qProfileUserRole).where(qProfileUserRole.roleId.in(roleIds)).execute();
		logger.debug("删除了{}条ProfileUserRole",n1);
		long n2 = new JPAQueryFactory(em).delete(qProfileRoleAuth).where(qProfileRoleAuth.roleId.in(roleIds)).execute();
		logger.debug("删除了{}条ProfileRoleAuth",n2);
		long n3 = new JPAQueryFactory(em).delete(qProfileRole).where(qProfileRole.roleId.in(roleIds)).execute();
		logger.debug("删除了{}条ProfileRole",n3);
		
//		for (String roleId : roleIds) {
//			JPAQuery query = new JPAQuery(em).from(q);
//			for (ProfileUserRole userRole : query.where(q.roleId.eq(roleId)).list(q)) {
//				em.remove(userRole);
//			}
//			
//			query = new JPAQuery(em).from(qProfileRoleAuth);
//			for (ProfileRoleAuth roleAuth : query.where(qProfileRoleAuth.roleId.eq(roleId)).list(qProfileRoleAuth)) {
//				em.remove(roleAuth);
//			}
//
//			ProfileRole profileRole = em.find(ProfileRole.class, roleId);
//			if (profileRole != null)
//				em.remove(profileRole);
//		}
	}

	public ProfileRole getProfileRoleInfo(String roleId) {
		return em.find(ProfileRole.class, roleId);
	}


	@Transactional(rollbackFor = Exception.class)
	public void addProfileRole(ProfileRole role) throws ErrorMessageException {

		role.fillDefaultValues();

		QProfileRole q = QProfileRole.profileRole;

		if (new JPAQueryFactory(em).select(q).from(q).where(q.roleName.eq(role.getRoleName())).fetchCount() > 0) {
			throw new ErrorMessageException(ErrorCode.CheckError, "添加角色失败:角色名已存在");
		}

		em.persist(role);
	}

	public List<String> getProfileRoleAuths(String roleId) {
		List<String> auths = Lists.newLinkedList();

		QProfileRoleAuth q = QProfileRoleAuth.profileRoleAuth;
		List<ProfileRoleAuth> current = new JPAQueryFactory(em).select(q).from(q).where(q.roleId.eq(roleId)).fetch();

		for (ProfileRoleAuth ra : current) {
			auths.add(ra.getAuthority());
		}
		return auths;
	}

	/**
	 * 删除角色下的用户
	 * 
	 * @param roles
	 *            Map<key, value>; key=roleId, value=puId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteProfileUserRoles(Map<String, String> roles) {
		QProfileUserRole q = QProfileUserRole.profileUserRole;
		JPAQuery<ProfileUserRole> query = new JPAQueryFactory(em).select(q).from(q);

		for (Entry<String, String> userRole : roles.entrySet()) {
			ProfileUserRole profileUserRole = query
					.where(q.roleId.eq(userRole.getKey()).and(q.puId.eq(userRole.getValue()))).fetchOne();
			if (profileUserRole != null) {
				em.remove(profileUserRole);
			}
		}

	}

	public FetchResponse<ProfileUserRole> fetchUserRoles(String roleId, Range range) {

		QProfileUserRole q = QProfileUserRole.profileUserRole;
		JPAQuery<ProfileUserRole> query = new JPAQueryFactory(em).select(q).from(q).where(q.roleId.eq(roleId));

		return new JPAFetchResponseBuilder<ProfileUserRole>().range(range).build(query);
	}

	@Transactional(rollbackFor = Exception.class)
	public void addProfileUserRole(ProfileUserRole userRole) throws ErrorMessageException {
		userRole.fillDefaultValues();
		QProfileUserRole q = QProfileUserRole.profileUserRole;
		long n = new JPAQueryFactory(em).select(q.id).from(q).where(q.roleId.eq(userRole.getRoleId()).and(q.puId.eq(userRole.getPuId()))).fetchCount();
		if (n > 0) {
			throw new ErrorMessageException(ErrorCode.CheckError, "添加角色成员失败:角色成员已存在");
		}

		em.persist(userRole);

	}

	public Map<String, String> getUserIds(String roleId) {
		QProfileUser qProfileUser = QProfileUser.profileUser;
		QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
		List<String> puIds = new JPAQueryFactory(em)
				.select(qProfileUserRole.puId).
						from(qProfileUserRole)
				.where(qProfileUserRole.roleId.eq(roleId))
				.fetch();
		JPAQuery<Tuple> query = new JPAQueryFactory(em).select(qProfileUser.puId, qProfileUser.name).from(qProfileUser);
		if (puIds.size() != 0) {
			query.where(qProfileUser.puId.notIn(puIds));
		}
		List<Tuple> tuples = query.fetch();
		HashMap<String, String> result = Maps.newHashMapWithExpectedSize(tuples.size());
		for (Tuple t : tuples) {
			result.put(t.get(qProfileUser.puId), t.get(qProfileUser.name));
		}

		return result;
	}

	public Map<String, String> getBranchIds() {
		QProfileBranch qProfileBranch = QProfileBranch.profileBranch;
		List<Tuple> tuples = new JPAQueryFactory(em)
				.select(qProfileBranch.branchId, qProfileBranch.branchName)
				.from(qProfileBranch)
				.fetch();

		HashMap<String, String> result = Maps.newHashMapWithExpectedSize(tuples.size());

		for (Tuple t : tuples) {
			result.put(t.get(qProfileBranch.branchId), t.get(qProfileBranch.branchName));
		}
		return result;
	}

}
