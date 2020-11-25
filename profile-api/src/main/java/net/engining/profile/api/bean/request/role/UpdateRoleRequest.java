package net.engining.profile.api.bean.request.role;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 15:24
 * @since 1.0.0
 */
public class UpdateRoleRequest extends BaseOperateRequest {
    /**
     * 角色ID
     */
    @NotBlank(message = "请输入：角色ID")
    @Length(max = 20, message = "角色ID的字段长度不能20个字符")
    @ApiModelProperty(value = "角色ID|1-20个字母或数字", example = "000000000", required = true)
    private String roleId;
    /**
     *  角色名称
     */
    @NotBlank(message = "请输入：角色名称")
    @Length(max = 50, message = "角色名称的字段长度不能超过50个字符")
    @ApiModelProperty(value = "角色名称|1-50个中文字符", example = "管理员")
    private String roleName;
    /**
     * 部门ID
     */
    @NotBlank(message = "请输入：部门ID")
    @Length(max = 6, message = "部门ID的字段长度不能超过6个字符")
    @ApiModelProperty(value = "部门ID|1-6位数字", example = "100001", required = true)
    private String departmentId;

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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
