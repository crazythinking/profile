package net.engining.profile.sdk.service;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.db.querydsl.Range;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.model.*;
import net.engining.profile.enums.DefaultRoleID;
import net.engining.profile.sdk.service.bean.profile.MenuOrAuthInfo;
import net.engining.profile.sdk.service.query.MenuService;
import net.engining.template.config.props.ProfileParamProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author
 */
@Service
public class ProfileMgmService implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static final String AUTH_KEY = "RoleAuth";

	public static final String SPLIT = "|";
	/**
	 * A time source
	 */
	private Ticker ticker = Ticker.systemTicker();
	/**
	 * 没把全部用户的菜单树放缓存是因为，用户过多占太多内存
	 * (userId|appName)   -   Map<appcd,TreeNode<String, MenuOrAuthBean>>
	 */
	private LoadingCache<String, List<String>> roleAuthCache;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	MenuService menuService;

	@Autowired
	ProfileParamProperties profileParamProperties;

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
	 * 获取所有的角色(后端系统超级管理员权限不给予查出)
	 * @param appCd 应用代码
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchAllProfileRole(String appCd) {
		//是否为auth中心模式
		boolean isAuth = menuService.checkAppCd(appCd);
		QProfileRole qProfileRole = QProfileRole.profileRole;
		BooleanExpression roleIdCondition = qProfileRole.roleId.ne(DefaultRoleID.SUPERADMIN.toString());
		if (isAuth){
			roleIdCondition.and(qProfileRole.appCd.eq(appCd));
		}

		JPAQuery<Tuple> query = new JPAQueryFactory(em).select(qProfileRole.roleId, qProfileRole.roleName)
				.from(qProfileRole)
				.where(roleIdCondition);

		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query, qProfileRole.roleId,
				qProfileRole.roleName);
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

		logger.debug("删除了{}条ProfileUserRole", n1);

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
	 * 根据角色名称查询角色信息
	 * @param roleName
	 * @param appCd
	 * @param range
	 * @return
	 */
	public FetchResponse<Map<String, Object>> fetchProfileRole(String roleName, String appCd,Range range) {
		//是否为auth中心模式
		boolean isAuth = menuService.checkAppCd(appCd);

		QProfileRole qProfileRole = QProfileRole.profileRole;
		QProfileBranch qProfileBranch = QProfileBranch.profileBranch;
		BooleanExpression w = null;
		BooleanExpression roleIdCondition = qProfileRole.roleId.ne(DefaultRoleID.SUPERADMIN.toString());
		if (StringUtils.isNotBlank(roleName)) {
			w = qProfileRole.roleName.like("%" + roleName + "%");

		}
		if (isAuth){
			roleIdCondition.and(qProfileRole.appCd.eq(appCd));
		}
		JPAQuery<Tuple> query = new JPAQueryFactory(em)
				.select(qProfileRole.roleId, qProfileBranch.branchName,
						qProfileBranch.branchId,qProfileRole.roleName)
				.from(qProfileRole, qProfileBranch)
				.where(w,
						qProfileBranch.branchId.eq(qProfileRole.branchId),
						roleIdCondition);

		return new JPAFetchResponseBuilder<Map<String, Object>>().range(range).buildAsMap(query, qProfileRole.roleId,
				qProfileBranch.branchName,qProfileBranch.branchId, qProfileRole.roleName);
	}

	/**
	 * 新增角色
	 * @param roleId
	 * @param branchId
	 * @param roleName
	 * @param orgId
	 * @param appCd
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveProfileRole(String roleId, String branchId, String roleName, String orgId, String appCd) {
		//是否为auth中心模式
		boolean isAuth = menuService.checkAppCd(appCd);

		QProfileRole q = QProfileRole.profileRole;

		BooleanExpression w1 = q.roleName.eq(roleName);
		BooleanExpression w2 = q.roleId.eq(roleId);

		if (isAuth){
			w1.and(q.appCd.eq(appCd));
			w2.and(q.appCd.eq(appCd));
		}else {
			//本地profile模式
			appCd = profileParamProperties.getAppCd();
		}

		long nameCount = new JPAQueryFactory(em)
				.select(q)
				.from(q)
				.where()
				.fetchCount();
		long idCount = new JPAQueryFactory(em)
				.select(q)
				.from(q)
				.where()
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
		em.persist(profileRole);
	}

	/**
	 * 角色修改
	 * @param roleId
	 * @param branchId
	 * @param roleName
	 * @param appCd
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateProfileRole(String roleId, String branchId, String roleName, String appCd) {
		//是否为auth中心模式
		boolean isAuth = menuService.checkAppCd(appCd);
		//角色表
		QProfileRole q = QProfileRole.profileRole;
		BooleanExpression w1 = q.roleId.eq(roleId);
		if (isAuth){
			w1.and(q.appCd.eq(appCd));
		}
		//更新角色信息
		new JPAQueryFactory(em)
				.update(q)
				.set(q.roleName,roleName)
				.set(q.branchId,branchId)
				.where(w1)
				.execute();
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
	 * 角色权限分配
	 *
	 * @param roleId 角色id
	 * @param authInfoList 权限集合
	 */
	@Transactional(rollbackFor = Exception.class)
	public void distributionProfileRole(String roleId, List<MenuOrAuthInfo> authInfoList) {
		// 如果对应的权限存在就进行删除然后在进行添加操作
		QProfileRoleAuth qProfileRoleAuth = QProfileRoleAuth.profileRoleAuth;
		long n2 = new JPAQueryFactory(em)
				.delete(qProfileRoleAuth)
				.where(qProfileRoleAuth.roleId.eq(roleId))
				.execute();
		logger.debug("删除了{}条ProfileRoleAuth", n2);
		//添加权限
		authInfoList.forEach(authInfo -> {
			ProfileRoleAuth profileRoleAuth = new ProfileRoleAuth();
			profileRoleAuth.fillDefaultValues();
			profileRoleAuth.setRoleId(roleId);
			profileRoleAuth.setAuthority(authInfo.getAuthority());
			profileRoleAuth.setAutuUri(authInfo.getAutuUri());

			em.persist(profileRoleAuth);
		});

		//刷新角色已有权限缓存
		this.refreshUserMenuCache(StringUtils.join(AUTH_KEY,SPLIT,roleId));
		//TODO 刷新用户菜单权限缓存
//		menuService.refreshUserMenuCache();
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
	 * 获取角色对应的权限主方法
	 * @param roleId
	 * @return
	 */
	public List<String> fetchRoleAuthByRoleId(String roleId) {
		List<String> authList = null;
		try {
			//从本地缓存获取
			authList = roleAuthCache.get(StringUtils.join(AUTH_KEY,SPLIT,roleId));
		} catch (ExecutionException e) {
			authList = getRoleAuthByRoleId(roleId);
		}

		return authList;
	}
	/**
	 * 获取角色对应的权限
	 */
	public List<String> getRoleAuthByRoleId(String roleId) {
		QProfileRoleAuth qProfileBranch = QProfileRoleAuth.profileRoleAuth;
		List<String> authList = new JPAQueryFactory(em)
				.select(qProfileBranch.authority)
				.from(qProfileBranch)
				.where(qProfileBranch.roleId.eq(roleId))
				.fetch();
		return authList;
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


	/**
	 * 初始化角色已有权限cache
	 * 同时对本地模式无appcd支持
	 * RoleAuth|roleid
	 */
	public void initRoleAuthCache()
	{
		roleAuthCache = CacheBuilder.newBuilder()
				.ticker(ticker)
				//当缓存项在指定的时间段内没有被读或写就会被回收
				.expireAfterAccess(Duration.ofDays(1))
				//最大条数
				.maximumSize(100)
				.build(new CacheLoader<String, List<String>>() {

					@Override
					public List<String> load(String key) throws Exception {
						List<String> authList = null;
						String[] roleId = StringUtils.splitPreserveAllTokens(key,SPLIT);
						String roleIdKey = roleId[1];
						if (ValidateUtilExt.isNullOrEmpty(roleIdKey)) {
							return null;
						}
						authList = getRoleAuthByRoleId(roleIdKey);
						return authList;
					}

				});
	}

	/**
	 * 刷新角色已有权限cache
	 * 需要在分配角色权限后刷新缓存
	 * @param key RoleAuth|roleid
	 */
	public void refreshUserMenuCache(String key)
	{
		roleAuthCache.refresh(key);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("初始化角色已有权限cache...");
		//初始化角色已有权限cache
		initRoleAuthCache();
	}

}
