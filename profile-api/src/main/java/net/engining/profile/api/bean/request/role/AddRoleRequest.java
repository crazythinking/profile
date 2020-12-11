package net.engining.profile.api.bean.request.role;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 新增用户请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 10:22
 * @since 1.0.0
 */
public class AddRoleRequest extends BaseOperateRequest {
    /**
     * 角色名称
     */
    @NotBlank(message = "请输入：角色名称")
    @Length(max = 50, message = "角色名称的字段长度不能超过50个中文字符")
    @ApiModelProperty(value = "角色名称|1-50个中文字符", example = "管理员")
    private String roleName;
    /**
     * 部门ID
     */
    @NotBlank(message = "请输入：部门ID")
    @Length(max = 6, message = "部门ID的字段长度不能超过6个数字")
    @ApiModelProperty(value = "部门ID|1-6位数字", example = "100001", required = true)
    private String departmentId;
    /**
     * 所属系统
     */
    @Length(max = 10, message = "所属系统的字段长度不能超过10个字母")
    @NotBlank(message = "请输入：所属系统")
    @ApiModelProperty(value = "所属系统|1-10个字母", example = "scac", required = true)
    private String system;

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

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return "AddRoleRequest{" +
                "roleName='" + roleName + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", system='" + system + '\'' +
                '}';
    }
}
