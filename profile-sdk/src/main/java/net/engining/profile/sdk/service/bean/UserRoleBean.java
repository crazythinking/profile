package net.engining.profile.sdk.service.bean;

import java.io.Serializable;

/**
 * @Description
 * @Author yangli
 */

public class UserRoleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roleId;

    private String roleName;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "UserRoleBean{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
