package net.engining.profile.enums;


/**
 * @author : lijiahui
 * @version : 版本
 * @Description : 初始化菜单
 * @date : 2019/11/27 18:45
 */
public enum MenuEnum  {
    /**
     * 用户管理
     */
    ProfileUser("Menu_ProfileUser","用户管理"),
    /**
     * 参数管理
     */
    ParameterManage("Menu_ParameterManage","参数管理"),
    /**
     * 授权中心
     */
    AuthorizationCenter("Menu_AuthorizationCenter","授权中心"),
    /**
     * 角色管理
     */
    ProfileRole("Menu_ProfileRole","角色管理"),
    /**
     * 用户维护
     */
    Maintenance("Menu_Maintenance","用户维护"),
    /**
     * 密码规则管理
     */
    PasswordRule("Menu_PasswordRule","密码规则管理"),
    /**
     * 用户登记簿查询
     */
    UserRegistryDa("Menu_UserRegistryDa","用户登记簿查询"),
    /**
     * 系统参数复核
     */
    SystemParameterReview("Menu_SystemParameterReview","系统参数复核")
    ;

    /**
     * 菜单权限标识
     */
    private final String menuCd;
    /**
     * 菜单名称
     */
    private final String mname;

    MenuEnum(String menuCd, String mname) {
        this.menuCd = menuCd;
        this.mname = mname;
    }

    public String getMenuCd() {
        return this.menuCd;
    }

    public String getMname() {
        return this.mname;
    }
}
