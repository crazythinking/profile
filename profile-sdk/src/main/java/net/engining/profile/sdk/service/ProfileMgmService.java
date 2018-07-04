package net.engining.profile.sdk.service;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.db.querydsl.Range;
import net.engining.profile.entity.model.ProfileRole;
import net.engining.profile.entity.model.ProfileRoleAuth;
import net.engining.profile.entity.model.ProfileUserRole;
import net.engining.profile.entity.model.QProfileBranch;
import net.engining.profile.entity.model.QProfileRole;
import net.engining.profile.entity.model.QProfileRoleAuth;
import net.engining.profile.entity.model.QProfileUser;
import net.engining.profile.entity.model.QProfileUserRole;

@Service
public class ProfileMgmService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	private EntityManager em;

	/**
	 * 根据用户信息查询其角色
	 * 
	 * @param branchId
	 * @param orgId
	 * @param userName
	 * @param range
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchProfileUser(String branchId, String orgId, String userName,
			Range range) {
		QProfileUser qProfileUser = QProfileUser.profileUser;
		QProfileBranch qProfileBranch = QProfileBranch.profileBranch;

		BooleanExpression w = qProfileUser.branchId.eq(branchId).and(qProfileUser.orgId.eq(orgId));

		if (StringUtils.isNotBlank(userName)) {
			w.and(qProfileUser.name.like(userName));
		}

		JPAQuery<Tuple> query = new JPAQueryFactory(em)
				.select(qProfileUser.puId, qProfileBranch.branchName, qProfileUser.userId, qProfileUser.name,
						qProfileUser.status, qProfileUser.email, qProfileUser.pwdExpDate, qProfileUser.pwdTries,
						qProfileUser.mtnTimestamp, qProfileUser.mtnUser)
				.from(qProfileUser, qProfileBranch).where(w).orderBy(qProfileUser.mtnTimestamp.desc());
		return new JPAFetchResponseBuilder<Map<String, Object>>().range(range).buildAsMap(query, qProfileUser.puId,
				qProfileBranch.branchName, qProfileUser.userId, qProfileUser.name, qProfileUser.password,
				qProfileUser.status, qProfileUser.email, qProfileUser.pwdExpDate, qProfileUser.pwdTries,
				qProfileUser.mtnTimestamp, qProfileUser.mtnUser);
	}

	/**
	 * 获取所有的角色
	 * 
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchAllProfileRole() {
		QProfileRole qProfileRole = QProfileRole.profileRole;
		JPAQuery<Tuple> query = new JPAQueryFactory(em).select(qProfileRole.roleId, qProfileRole.roleName)
				.from(qProfileRole);
		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query, qProfileRole.roleId,
				qProfileRole.roleName);
	}

	/**
	 * 为用户分配角色 FIXME 逻辑不合理，需重构
	 * 
	 * @param roleStr
	 * @param puId
	 */
	@Transactional
	public void saveProfileUserAndRole(String roleStr, String puId) {
		// 先进行删除操作
		QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
		long n1 = new JPAQueryFactory(em).delete(qProfileUserRole).where(qProfileUserRole.puId.eq(puId)).execute();

		logger.debug("删除了{}条ProfileUserRole", n1);
		
		roleStr = roleStr + ",getAssist,tradeType,subjectList";
		// 进行相应的新增操作
		if (StringUtils.isNotBlank(roleStr) && roleStr.indexOf(",") > 0) {
			String[] roleIdArr = roleStr.split(",");
			for (int i = 0; i < roleIdArr.length; i++) {
				ProfileUserRole profileUserRole = new ProfileUserRole();
				profileUserRole.fillDefaultValues();
				profileUserRole.setPuId(puId);
				profileUserRole.setRoleId(roleIdArr[i]);
				em.persist(profileUserRole);
			}
		} else {
			ProfileUserRole profileUserRole = new ProfileUserRole();
			profileUserRole.fillDefaultValues();
			profileUserRole.setPuId(puId);
			profileUserRole.setRoleId(roleStr);
			em.persist(profileUserRole);
		}
	}

	/**
	 * 获取所有的分支机构
	 * 
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchAllProfileBranch() {
		QProfileBranch qProfileBranch = QProfileBranch.profileBranch;
		JPAQuery<Tuple> query = new JPAQueryFactory(em).select(qProfileBranch.branchId, qProfileBranch.branchName)
				.from(qProfileBranch);
		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query, qProfileBranch.branchId,
				qProfileBranch.branchName);
	}

	/**
	 * 根据角色名称查询角色信息
	 * @param branchId
	 * @param roleName
	 * @param range
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchProfileRole(String roleName, Range range) {
		QProfileRole qProfileRole = QProfileRole.profileRole;
		QProfileBranch qProfileBranch = QProfileBranch.profileBranch;
		BooleanExpression w = null;
		if (StringUtils.isNotBlank(roleName)) {
			w = qProfileRole.roleName.like("%" + roleName + "%");
			w.and(qProfileBranch.branchId.eq(qProfileRole.branchId));
		}
		
		JPAQuery<Tuple> query = new JPAQueryFactory(em)
				.select(qProfileRole.roleId, qProfileBranch.branchName, qProfileRole.roleName)
				.from(qProfileRole, qProfileBranch).where(w);

		return new JPAFetchResponseBuilder<Map<String, Object>>().range(range).buildAsMap(query, qProfileRole.roleId,
				qProfileBranch.branchName, qProfileRole.roleName);
	}

	/**
	 * 新增角色 FIXME roleId主键改为自动生成
	 * 
	 * @param profileRoleForm
	 * @param bindingResult
	 * @return
	 */
	@Transactional
	public void saveProfileRole(String roleId, String branchId, String roleName, String orgId) {
		QProfileRole q = QProfileRole.profileRole;
		if (new JPAQueryFactory(em).select(q).from(q).where(q.roleName.eq(roleName)).fetchCount() > 0) {
			throw new ErrorMessageException(ErrorCode.CheckError, "添加角色失败:角色名已存在");
		}
		ProfileRole profileRole = new ProfileRole();
		profileRole.fillDefaultValues();
		profileRole.setOrgId(orgId);
		profileRole.setRoleId(roleId);
		profileRole.setBranchId(branchId);
		profileRole.setRoleName(roleName);
		em.persist(profileRole);
	}

	/**
	 * 角色修改
	 * 
	 * @param profileRoleForm
	 * @param bindingResult
	 * @return
	 */
	@Transactional
	public void updateProfileRole(String roleId, String branchId, String roleName) {
		ProfileRole profileRole = em.find(ProfileRole.class, roleId);
		profileRole.setBranchId(branchId);
		profileRole.setRoleName(roleName);
	}

	/**
	 * 角色权限分配 FIXME 逻辑不合理，需重构
	 * 
	 * @param profileRoleForm
	 * @param bindingResult
	 * @return
	 */
	@Transactional
	public void distributionProfileRole(String roleId, String authStr) {
		// 如果对应的权限存在就进行删除然后在进行添加操作
		QProfileRoleAuth qProfileRoleAuth = QProfileRoleAuth.profileRoleAuth;
		long n2 = new JPAQueryFactory(em).delete(qProfileRoleAuth)
				.where(qProfileRoleAuth.roleId.eq(roleId)).execute();
		logger.debug("删除了{}条ProfileRoleAuth", n2);
		authStr = authStr + ",getAssist,subjectList,tradeType";
		if (StringUtils.isNotBlank(authStr) && authStr.indexOf(",") > 0) {
			String[] authArr = authStr.split(",");
			for (int i = 0; i < authArr.length; i++) {
				ProfileRoleAuth profileRoleAuth = new ProfileRoleAuth();
				profileRoleAuth.fillDefaultValues();
				profileRoleAuth.setRoleId(roleId);
				profileRoleAuth.setAuthority(authArr[i]);
				em.persist(profileRoleAuth);
			}
		} else {
			ProfileRoleAuth profileRoleAuth = new ProfileRoleAuth();
			profileRoleAuth.fillDefaultValues();
			profileRoleAuth.setRoleId(roleId);
			profileRoleAuth.setAuthority(authStr);
			em.persist(profileRoleAuth);
		}
	}

	/**
	 * 角色标识必须是唯一的
	 * 
	 * @param roleId
	 * @param num
	 * @return
	 */
	public boolean fetchRoleId(String roleId, int num) {
		QProfileRole qProfileRole = QProfileRole.profileRole;
		long n = new JPAQueryFactory(em).select(qProfileRole.roleId).from(qProfileRole)
				.where(qProfileRole.roleId.eq(roleId)).fetchCount();
		if (n > num) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取角色对应的权限
	 * 
	 * @param roleId
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchRoleAuthByRoleId(String roleId) {
		QProfileRoleAuth qProfileBranch = QProfileRoleAuth.profileRoleAuth;
		JPAQuery<Tuple> query = new JPAQueryFactory(em).select(qProfileBranch.authority, qProfileBranch.roleId)
				.from(qProfileBranch).where(qProfileBranch.roleId.eq(roleId));
		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query, qProfileBranch.authority);
	}

	/**
	 * 获取用户对应的角色
	 * 
	 * @param puId
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchUserRoleByPuId(String puId) {
		QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
		JPAQuery<Tuple> query = new JPAQueryFactory(em).select(qProfileUserRole.roleId, qProfileUserRole.puId)
				.from(qProfileUserRole).where(qProfileUserRole.puId.eq(puId));
		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query, qProfileUserRole.roleId);
	}

}
