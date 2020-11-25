package net.engining.profile.api.bean.request.role;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 10:14
 * @since 1.0.0
 */
public class DeleteRoleRequest extends BaseOperateRequest {
    /**
     * 角色ID
     */
    @NotBlank(message = "请输入：角色ID")
    @Length(max = 20, message = "角色ID的字段长度不能20个字符")
    @ApiModelProperty(value = "角色ID|1-20个字母或数字", example = "000000000", required = true)
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
