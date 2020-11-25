package net.engining.profile.sdk.service.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.enums.SystemEnum;

import java.io.Serializable;

/**
 * 角色列表查询数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 13:29
 * @since 1.0.0
 */
public class RoleListDto implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 部门
     */
    private String departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 所属系统
     */
    private SystemEnum system;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public SystemEnum getSystem() {
        return system;
    }

    public void setSystem(SystemEnum system) {
        this.system = system;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
