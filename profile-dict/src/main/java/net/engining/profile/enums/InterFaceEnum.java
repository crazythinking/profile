package net.engining.profile.enums;

/**
 * @author : lijiahui
 * @version : 版本
 * @Description : 初始化接口
 * @date : 2019/11/29 17:51
 */
public enum InterFaceEnum {
    /**
     * 角色修改
     */
    UpdateProfileRole("UpdateProfileRole","角色修改","Menu_ProfileRole"),
    /**
     * 角色删除
     */
    DeleteProfileRole("DeleteProfileRole","角色删除","Menu_ProfileRole"),
    /**
     * 角色权限分配
     */
    DistributionProfileRole("DistributionProfileRole","角色权限分配","Menu_ProfileRole"),
    /**
     * 更新某个用户
     */
    UpdateUser("UpdateUser","更新某个用户","Menu_Maintenance"),
    /**
     * 添加用户
     */
    AddUser("AddUser","添加用户","Menu_Maintenance"),
    /**
     * 删除某个用户
     */
    RemoveUserByPuId("RemoveUserByPuId","删除某个用户","Menu_Maintenance"),
    /**
     * 为用户分配角色
     */
    SaveProfileUserAndRole("SaveProfileUserAndRole","为用户分配角色","Menu_Maintenance"),
    /**
     * 密码规则确认
     */
    PasswordRuleManager("PasswordRuleManager","密码规则确认","Menu_PasswordRule"),
    /**
     * 用户登记薄详情查询
     */
    UserRegistryDetailSearch("UserRegistryDetailSearch","用户登记薄详情查询","Menu_UserRegistryDa"),
    /**
     * 用户登记薄查询Excel
     */
    UserRegistryExport("UserRegistryExport","用户登记薄查询Excel","Menu_UserRegistryDa"),
    ;

    /**
     * 接口权限标识
     */
    private final String interfCd;
    /**
     * 接口名称
     */
    private final String iname;
    /**
     * 菜单标识
     */
    private final String menuCd;

    InterFaceEnum(String interfCd, String iname, String menuCd) {
        this.interfCd = interfCd;
        this.iname = iname;
        this.menuCd = menuCd;
    }

    public String getInterfCd() {
        return this.interfCd;
    }

    public String getIname() {
        return this.iname;
    }

    public String getMenuCd() {
        return this.menuCd;
    }
}
