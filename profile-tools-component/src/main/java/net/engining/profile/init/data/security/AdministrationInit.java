package net.engining.profile.init.data.security;


import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.db.DbConstants;
import net.engining.pg.support.init.TableDataInitializer;
import net.engining.profile.config.props.ProfileOauthProperties;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.*;
import net.engining.profile.enums.DefaultRoleID;
import net.engining.profile.enums.InterFaceEnum;
import net.engining.profile.enums.MenuEnum;
import net.engining.profile.enums.RoleIdEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;

/**
 *
 * 文件初始化示例
 * @author 作者
 * @version 版本
 * @since
 * @date 2019/8/14 10:20
 */
public class AdministrationInit implements TableDataInitializer, InitializingBean {


    private static final Logger log = LoggerFactory.getLogger(AdministrationInit.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    Provider4Organization provider4Organization;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GmCommonProperties commonProperties;

    @Autowired
    ProfileOauthProperties cProperties;

    /**
     * 缺省角色名
     */
    private static final String ROLE_NAME = "系统超级管理员";

    private static final String ACTUATOR_ROLE_NAME = "系统监控";

    private static final String BRANCH_NAME = "财务运营管理部";
    /**
     * AppCd
     */
    private String APP_CD = "";

    /**
     * 缺省用户名
     */
    private static final String USER_NAME = "admin";

    private static final String ACTUATOR_USER = "svadmin";

    private static final String ACTUATOR_PASSWORD = "sv@dm1n";

    private static final String MTN_USER = "Initializer";
    /**
     * 超级管理员初始化菜单权限
     */
    private static final Set<ProfileMenu> SUPERADMIN_MENU_AUTH_SET = new TreeSet<>(Comparator.comparing(ProfileMenu::getMenuCd));
    /**
     * 超级管理员初始化接口权限
     */
    private static final  Set<ProfileMenuInterf> SUPERADMIN_INTER_AUTH_SET = new TreeSet<>(Comparator.comparing(ProfileMenuInterf::getInterfCd));

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws Exception {
        // 清空权限相关表
        em.createNativeQuery("delete from PROFILE_BRANCH").executeUpdate();
        em.createNativeQuery("delete from PROFILE_USER").executeUpdate();
        em.createNativeQuery("delete from PROFILE_ROLE_AUTH").executeUpdate();
        em.createNativeQuery("delete from PROFILE_ROLE").executeUpdate();
        em.createNativeQuery("delete from PROFILE_USER_ROLE").executeUpdate();

        //1.创建部门
        initBranch();

        //2.初始化菜单、接口
        initMenuAuthInterFace();

        //3.初始化用户角色权限
        initSuperAdmin();
        //initActuatorUser();
    }

    /**
     * 初始化菜单、权限、接口表
     * @throws IOException
     */
    private void initMenuAuthInterFace() throws IOException {
        //清空菜单表
        clearProfileMenu(APP_CD);
        //清空接口表表
        clearProfileMenuInterf(APP_CD);
        //初始化菜单表、管理员权限表(权限表中菜单权限的AUTU_URI为"-",接口权限的AUTU_URI为所属菜单的id)和接口表
        // (不同app_cd下的菜单cd和接口cd是可以重复的，唯一约束为菜单cd 和 appcd组合)
        //[菜单表初始化] ------------------------------------------------start
        //用户管理 0
        ProfileMenu profileUser = createMenu(MenuEnum.ProfileUser.getMname(),MenuEnum.ProfileUser.getMenuCd(),0);
        //角色管理 1
        ProfileMenu profileRole = createMenu(MenuEnum.ProfileRole.getMname(),MenuEnum.ProfileRole.getMenuCd(),profileUser.getId());
        //用户维护 1
        ProfileMenu maintenance = createMenu(MenuEnum.Maintenance.getMname(),MenuEnum.Maintenance.getMenuCd(),profileUser.getId());
        //密码规则管理 1
        ProfileMenu passwordRule = createMenu(MenuEnum.PasswordRule.getMname(),MenuEnum.PasswordRule.getMenuCd(),profileUser.getId());
        //用户登记簿查询 1
        ProfileMenu userRegistryDa = createMenu(MenuEnum.UserRegistryDa.getMname(),MenuEnum.UserRegistryDa.getMenuCd(),profileUser.getId());
        //[菜单表初始化] ------------------------------------------------end

        //[接口表初始化] ------------------------------------------------start

        //所属角色管理菜单
        //角色修改
        ProfileMenuInterf updateProfileRole = createInterFace(InterFaceEnum.UpdateProfileRole.getIname(),InterFaceEnum.UpdateProfileRole.getInterfCd(),profileRole.getId());
        //角色删除
        ProfileMenuInterf deleteProfileRole = createInterFace(InterFaceEnum.DeleteProfileRole.getIname(),InterFaceEnum.DeleteProfileRole.getInterfCd(),profileRole.getId());
        //角色权限分配
        ProfileMenuInterf distributionProfileRole = createInterFace(InterFaceEnum.DistributionProfileRole.getIname(),InterFaceEnum.DistributionProfileRole.getInterfCd(),profileRole.getId());

        //所属用户维护菜单
        //更新某个用户
        ProfileMenuInterf updateUser = createInterFace(InterFaceEnum.UpdateUser.getIname(),InterFaceEnum.UpdateUser.getInterfCd(),maintenance.getId());
        //添加用户
        ProfileMenuInterf addUser = createInterFace(InterFaceEnum.AddUser.getIname(),InterFaceEnum.AddUser.getInterfCd(),maintenance.getId());
        //删除某个用户
        ProfileMenuInterf removeUserByPuId = createInterFace(InterFaceEnum.RemoveUserByPuId.getIname(),InterFaceEnum.RemoveUserByPuId.getInterfCd(),maintenance.getId());
        //为用户分配角色
        ProfileMenuInterf saveProfileUserAndRole = createInterFace(InterFaceEnum.SaveProfileUserAndRole.getIname(),InterFaceEnum.SaveProfileUserAndRole.getInterfCd(),maintenance.getId());

        //所属密码规则管理菜单
        //密码规则确认
        ProfileMenuInterf passwordRuleManager = createInterFace(InterFaceEnum.PasswordRuleManager.getIname(),InterFaceEnum.PasswordRuleManager.getInterfCd(),passwordRule.getId());

        //所属用户登记簿查询菜单
        //用户登记薄详情查询
        ProfileMenuInterf userRegistryDetailSearch = createInterFace(InterFaceEnum.UserRegistryDetailSearch.getIname(),InterFaceEnum.UserRegistryDetailSearch.getInterfCd(),userRegistryDa.getId());
        //用户登记薄查询Excel
        ProfileMenuInterf userRegistryExport = createInterFace(InterFaceEnum.UserRegistryExport.getIname(),InterFaceEnum.UserRegistryExport.getInterfCd(),userRegistryDa.getId());
        //[接口表初始化] ------------------------------------------------end

        //[权限初始化] ------------------------------------------------start
        //超级管理员菜单权限
        SUPERADMIN_MENU_AUTH_SET.add(profileUser);
        SUPERADMIN_MENU_AUTH_SET.add(profileRole);
        SUPERADMIN_MENU_AUTH_SET.add(maintenance);
        SUPERADMIN_MENU_AUTH_SET.add(passwordRule);
        SUPERADMIN_MENU_AUTH_SET.add(userRegistryDa);
        //超级管理员接口权限
        SUPERADMIN_INTER_AUTH_SET.add(updateProfileRole);
        SUPERADMIN_INTER_AUTH_SET.add(deleteProfileRole);
        SUPERADMIN_INTER_AUTH_SET.add(distributionProfileRole);
        SUPERADMIN_INTER_AUTH_SET.add(updateUser);
        SUPERADMIN_INTER_AUTH_SET.add(addUser);
        SUPERADMIN_INTER_AUTH_SET.add(removeUserByPuId);
        SUPERADMIN_INTER_AUTH_SET.add(saveProfileUserAndRole);
        SUPERADMIN_INTER_AUTH_SET.add(passwordRuleManager);
        SUPERADMIN_INTER_AUTH_SET.add(userRegistryDetailSearch);
        SUPERADMIN_INTER_AUTH_SET.add(userRegistryExport);
        //[权限初始化] ------------------------------------------------end

    }

    private ProfileMenu createMenu(String mname, String menuCd, Integer parentId)
    {
        ProfileMenu menu= new ProfileMenu();
        menu.setMname(mname);
        menu.setMenuCd(menuCd);
        menu.setPathUrl(menuCd);
        menu.setAppCd(APP_CD);
        menu.setParentId(parentId);
        menu.setSortn(0);
        menu.setIcon("");
        menu.setOrgId(provider4Organization.getCurrentOrganizationId());
        menu.setMtnUser(MTN_USER);
        menu.setMtnTimestamp(new Date());
        em.persist(menu);
        return menu;
    }

    private ProfileMenuInterf createInterFace(String iname, String interFaceCd, Integer menuId)
    {
        ProfileMenuInterf profileMenuInterf = new ProfileMenuInterf();
        profileMenuInterf.setIname(iname);
        profileMenuInterf.setInterfCd(interFaceCd);
        profileMenuInterf.setAppCd(APP_CD);
        profileMenuInterf.setMenuId(menuId);
        profileMenuInterf.setMtnUser(MTN_USER);
        profileMenuInterf.setMtnTimestamp(new Date());
        em.persist(profileMenuInterf);
        return profileMenuInterf;
    }

    private ProfileRole initRole( String roleId, String roleName) {
        ProfileRole profileRole = new ProfileRole();
        profileRole.setRoleId(roleId);
        profileRole.setAppCd(APP_CD);
        profileRole.setOrgId(provider4Organization.getCurrentOrganizationId());
        profileRole.setBranchId(provider4Organization.getCurrentOrganizationId());
        profileRole.setRoleName(roleName);
        em.persist(profileRole);
        return profileRole;
    }

    private void createRoleAuth( String roleId, String auth, String authUrl) {
        ProfileRoleAuth profileRoleAuth = new ProfileRoleAuth();
        profileRoleAuth.setRoleId(roleId);
        profileRoleAuth.setAuthority(auth);
        profileRoleAuth.setAutuUri(authUrl);
        em.persist(profileRoleAuth);
    }

    private void initUserAndAuth(String roleId, ProfileUser user) {
        ProfileUserRole userRole = new ProfileUserRole();
        userRole.setRoleId(roleId);
        userRole.setPuId(user.getPuId());
        em.persist(userRole);
    }

    private ProfileUser createProfileUser(String userId, String userName, String defaultPassword) {
        ProfileUser user = new ProfileUser();
        user.setUserId(userId);
        user.setOrgId(provider4Organization.getCurrentOrganizationId());
        user.setBranchId(provider4Organization.getCurrentOrganizationId());
        user.setEmail("admin@admin.com");
        user.setMtnTimestamp(new Date());
        user.setMtnUser(MTN_USER);
        user.setName(userName);
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setPwdExpDate(DateUtils.addYears(new Date(), 10));
        user.setPwdTries(0);
        user.setStatus(StatusDef.A);
        em.persist(user);
        return user;
    }

    private ProfileBranch createBranch() {
        ProfileBranch branch = new ProfileBranch();
        branch.setOrgId(provider4Organization.getCurrentOrganizationId());
        branch.setBranchId(provider4Organization.getCurrentOrganizationId());
        branch.setBranchName(BRANCH_NAME);
        em.persist(branch);
        return branch;
    }

    /**
     * 清空接口表
     */
    private void clearProfileMenuInterf(String appCd) {
        int count = em
                .createNativeQuery("delete from PROFILE_MENU_INTERF where app_cd = ? ")
                .setParameter(1, appCd)
                .executeUpdate();

        log.debug("删除了{}条ProfileMenuInterf数据",count);
    }

    /**
     * 清空菜单表
     */
    private void clearProfileMenu(String appCd) {

        int count = em
                .createNativeQuery("delete from PROFILE_MENU where app_cd = ? ")
                .setParameter(1, appCd)
                .executeUpdate();

        log.debug("删除了{}条ProfileMenu数据",count);
    }

    /**
     * 初始化部门
     */
    private void initBranch() {
        log.debug("正在执行初始化器 -> AdministrationInit");
        createBranch();
    }

    /**
     * 初始化超级管理员
     */
    private void initSuperAdmin() {
        log.debug("正在执行初始化器 -> AdministrationInit");
        // Init Role
        ProfileRole profileRole = initRole(DefaultRoleID.SUPERADMIN.toString(),ROLE_NAME);

        // Init Role Auth; 角色与权限
        //预设菜单权限
        SUPERADMIN_MENU_AUTH_SET.forEach(profileMenu -> {
            createRoleAuth(profileRole.getRoleId(),profileMenu.getMenuCd(),DbConstants.NULL);
        });

        //预设接口权限
        SUPERADMIN_INTER_AUTH_SET.forEach(profileMenuInterf -> {
            createRoleAuth(profileRole.getRoleId(),profileMenuInterf.getInterfCd(),String.valueOf(profileMenuInterf.getMenuId()));
        });

        // Init User
        ProfileUser user = createProfileUser(USER_NAME,USER_NAME, commonProperties.getDefaultPassword());

        // Init User Role；用户与角色
        initUserAndAuth(DefaultRoleID.SUPERADMIN.toString(),user);
    }


    /**
     * 初始化监控用户
     * 只有监控权限
     */
    private void initActuatorUser() {
        log.debug("正在执行初始化器 -> ActuatorUserInit");
        // Init Role
        ProfileRole profileRole = initRole(RoleIdEnum.ACTUATOR.toString(),ACTUATOR_ROLE_NAME);
        // Init Role Auth; 角色与权限
        //额外增加ACTUATOR_MONITOR，用于微服务Actuator监控
        createRoleAuth(profileRole.getRoleId(),"ROLE_ACTUATOR_MONITOR",DbConstants.NULL);
        // Init User
        ProfileUser user = createProfileUser(ACTUATOR_USER,ACTUATOR_USER, ACTUATOR_PASSWORD);

        // Init User Role；用户与角色
        initUserAndAuth(RoleIdEnum.ACTUATOR.toString(),user);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化appcd
        if ( ! cProperties.isOauthed()){
            APP_CD = DbConstants.NULL;
        }
    }
}
