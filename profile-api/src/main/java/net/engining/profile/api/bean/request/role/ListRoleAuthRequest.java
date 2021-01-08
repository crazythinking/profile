package net.engining.profile.api.bean.request.role;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 角色拥有的权限查询请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 19:49
 * @since 1.0.0
 */
public class ListRoleAuthRequest implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @NotBlank(message = "请输入：角色ID")
    @Length(max = 20, message = "角色ID的字段长度不能20个字母或数字")
    @ApiModelProperty(value = "角色ID|1-20个字母或数字", example = "000000000", required = true)
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "ListRoleAuthRequest{" +
                "roleId='" + roleId + '\'' +
                '}';
    }
}
