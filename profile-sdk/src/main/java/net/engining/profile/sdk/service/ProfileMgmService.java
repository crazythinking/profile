package net.engining.profile.sdk.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.DbConstants;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.db.querydsl.Range;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.model.*;
import net.engining.profile.enums.DefaultRoleID;
import net.engining.profile.enums.RoleIdEnum;
import net.engining.profile.sdk.service.query.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

/**
 * Profile 后台管理服务
 *
 * @author
 */
@Service
public class ProfileMgmService {

	/** logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileMgmService.class);

	@PersistenceContext
	private EntityManager em;

	@Autowired
	AuthService authService;

	/**
	 * 根据用户信息查询其角色
	 *
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
	 * 获取所有的角色(后端系统超级管理员，监控权限不给予查出)
	 * 分配角色时使用
	 * @param appCd 应用代码
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchAllProfileRole(String appCd) {
		QProfileRole qProfileRole = QProfileRole.profileRole;
		BooleanExpression roleIdCondition = qProfileRole.roleId.notIn(
				DefaultRoleID.SUPERADMIN.toString(),
				RoleIdEnum.ACTUATOR.toString()
		);
		if (ValidateUtilExt.isNotNullOrEmpty(appCd)){
			roleIdCondition.and(qProfileRole.appCd.eq(appCd));
		}
		JPAQuery<Tuple> query = new JPAQueryFactory(em).select(qProfileRole.roleId, qProfileRole.roleName,
				qProfileRole.appCd)
				.from(qProfileRole)
				.where(roleIdCondition);

		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query, qProfileRole.roleId,
				qProfileRole.roleName, qProfileRole.appCd);
	}

	/**
	 * 为用户分配角色
	 *
	 * @param roleId 角色集合
	 * @param puId 用户信息表id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveProfileUserAndRole(String puId, List<String> roleId) {
		// 先进行删除操作
		QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
		long n1 = new JPAQueryFactory(em)
				.delete(qProfileUserRole)
				.where(qProfileUserRole.puId.eq(puId))
				.execute();

		LOGGER.debug("删除了{}条ProfileUserRole", n1);

		for(String s : roleId){
			ProfileUserRole profileUserRole = new ProfileUserRole();
			profileUserRole.fillDefaultValues();
			profileUserRole.setPuId(puId);
			profileUserRole.setRoleId(s);
			em.persist(profileUserRole);
		}
	}

	/**
	 * 获取所有的分支机构
	 *
	 */
	public FetchResponse<Map<String, Object>> fetchAllProfileBranch() {
		QProfileBranch qProfileBranch = QProfileBranch.profileBranch;
		JPAQuery<Tuple> query = new JPAQueryFactory(em)
				.select(qProfileBranch.branchId, qProfileBranch.branchName)
				.from(qProfileBranch);
		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query, qProfileBranch.branchId,
				qProfileBranch.branchName);
	}

	/**
	 * 查询所有角色信息列表
	 * @param roleName
	 * @param appCd
	 * @param range
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchProfileRole(String roleName, String appCd,Range range) {

		QProfileRole qProfileRole = QProfileRole.profileRole;
		QProfileBranch qProfileBranch = QProfileBranch.profileBranch;
		BooleanExpression w = null;
		BooleanExpression w1 = null;
		BooleanExpression roleIdCondition = qProfileRole.roleId.notIn(
				DefaultRoleID.SUPERADMIN.toString(),
				RoleIdEnum.ACTUATOR.toString()
		);
		if (StringUtils.isNotBlank(roleName)) {
			w = qProfileRole.roleName.like("%" + roleName + "%");

		}
		if (StringUtils.isNotBlank(appCd)) {
			w1 = qProfileRole.appCd.like("%" + appCd + "%");
		}
		JPAQuery<Tuple> query = new JPAQueryFactory(em)
				.select(qProfileRole.roleId, qProfileBranch.branchName,
						qProfileBranch.branchId,qProfileRole.roleName,
						qProfileRole.clientId,qProfileRole.appCd)
				.from(qProfileRole, qProfileBranch)
				.where(w,
						w1,
						qProfileBranch.branchId.eq(qProfileRole.branchId),
						roleIdCondition);

		return new JPAFetchResponseBuilder<Map<String, Object>>().range(range).buildAsMap(query, qProfileRole.roleId,
				qProfileBranch.branchName,qProfileBranch.branchId, qProfileRole.roleName, qProfileRole.appCd,qProfileRole.clientId);
	}

	/**
	 * 新增角色
	 * @param roleId
	 * @param branchId
	 * @param roleName
	 * @param orgId
	 * @param appCd
	 * @param clientId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveProfileRole(String roleId, String branchId, String roleName, String orgId, String appCd,String clientId) {
		//是否为auth中心模式
		boolean isAuth = authService.checkAppCd(appCd);

		QProfileRole q = QProfileRole.profileRole;
		QProfileMenu qProfileMenu = QProfileMenu.profileMenu;
		List<ProfileMenu> list = new JPAQueryFactory(em).select(qProfileMenu).from(qProfileMenu).where(qProfileMenu.appCd.eq(clientId)).fetch();
		if(ValidateUtilExt.isNullOrEmpty(list)||list.size()<=0){
			throw new ErrorMessageException(ErrorCode.CheckError,String.format("客户端：%s 没有对应的菜单",clientId));
		}
		if (!isAuth){
			//本地profile模式，如果appCd不为空，就用它的,空的话就用默认的（应对于授权中心模式）
			if (ValidateUtilExt.isNullOrEmpty(appCd)) {
				appCd = DbConstants.NULL;
			}
		}

		long nameCount = new JPAQueryFactory(em)
				.select(q)
				.from(q)
				.where(q.roleName.eq(roleName))
				.fetchCount();
		long idCount = new JPAQueryFactory(em)
				.select(q)
				.from(q)
				.where(q.roleId.eq(roleId))
				.fetchCount();
		if (nameCount > 0) {
			throw new ErrorMessageException(ErrorCode.CheckError, "添加角色失败:角色名已存在");
		}
		if (idCount > 0){
			throw new ErrorMessageException(ErrorCode.CheckError, "添加角色失败：角色ID已存在");
		}
		ProfileRole profileRole = new ProfileRole();
		profileRole.fillDefaultValues();
		profileRole.setOrgId(orgId);
		profileRole.setRoleId(roleId);
		profileRole.setAppCd(appCd);
		profileRole.setBranchId(branchId);
		profileRole.setRoleName(roleName);
		profileRole.setClientId(clientId);
		em.persist(profileRole);
	}

	/**
	 * 角色修改
	 * @param roleId
	 * @param branchId
	 * @param roleName
	 * @param appCd
	 * @param clientId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateProfileRole(String roleId, String branchId, String roleName, String appCd,String clientId) {
        ProfileRole profileRole = em.find(ProfileRole.class, roleId);
        if (ValidateUtilExt.isNotNullOrEmpty(profileRole)) {
            throw new ErrorMessageException(ErrorCode.Null, "角色不存在，请确认角色id是否正确！");
        }
        //是否为auth中心模式
//		boolean isAuth = authService.checkAppCd(appCd);
		profileRole.setBranchId(branchId);
		profileRole.setRoleName(roleName);
		profileRole.setAppCd(appCd);
		profileRole.setClientId(clientId);
	}

	/**
	 * 获取所有的权限
	 * @return 所有权限
	 */
	public FetchResponse<Map<String, Object>> fetchAllProfileRoleAuth() {
		QProfileRoleAuth qProfileRoleAuth = QProfileRoleAuth.profileRoleAuth;
		JPAQuery<Tuple> query = new JPAQueryFactory(em).select(qProfileRoleAuth.roleId, qProfileRoleAuth.authority)
				.from(qProfileRoleAuth);
		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query, qProfileRoleAuth.roleId,
				qProfileRoleAuth.authority);
	}

	/**
	 * 角色标识必须是唯一的
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
	 * 获取用户对应的角色
	 */
	public FetchResponse<Map<String, Object>> fetchUserRoleByPuId(String puId) {
		QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
		JPAQuery<Tuple> query = new JPAQueryFactory(em)
				.select(qProfileUserRole.roleId, qProfileUserRole.puId)
				.from(qProfileUserRole)
				.where(qProfileUserRole.puId.eq(puId));
		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query, qProfileUserRole.roleId);
	}

}
