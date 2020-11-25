package net.engining.profile.api.bean.request.user;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户拥有角色查询请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 10:52
 * @since 1.0.0
 */
public class ListUserRoleRequest implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    @NotBlank(message = "请输入：用户ID")
    @Length(max = 40, message = "用户ID的字段长度不能超过40个字符")
    @ApiModelProperty(value = "用户ID|1-40位的字母或数字", example = "000000000", required = true)
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
