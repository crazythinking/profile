package net.engining.profile.api.bean.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 角色数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 10:18
 * @since 1.0.0
 */
public class RoleSimpleVo implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", example = "admin", required = true)
    private String roleId;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", example = "管理员", required = true)
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
        return "RoleSimpleVo{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
