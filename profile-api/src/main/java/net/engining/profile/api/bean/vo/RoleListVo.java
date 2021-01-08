package net.engining.profile.api.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.enums.SystemEnum;

import java.io.Serializable;

/**
 * 角色列表查询数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 9:47
 * @since 1.0.0
 */
public class RoleListVo implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", example = "管理员", required = true)
    private String roleName;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", example = "000000000", required = true)
    private String roleId;
    /**
     * 部门
     */
    @ApiModelProperty(value = "部门ID", example = "100001", required = true)
    private String departmentId;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", example = "科技信息部", required = true)
    private String departmentName;
    /**
     * 所属系统
     */
    @ApiModelProperty(value = "所属系统", required = true)
    private String system;

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

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "RoleListVo{" +
                "roleName='" + roleName + '\'' +
                ", roleId='" + roleId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", system='" + system + '\'' +
                '}';
    }
}
