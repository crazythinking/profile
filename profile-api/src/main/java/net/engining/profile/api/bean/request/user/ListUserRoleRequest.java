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
    @Length(max = 30, min = 3, message = "用户ID的字段长度必须在3到30个字母或数字或下划线之间")
    @ApiModelProperty(value = "用户ID|3-30位的字母或数字或下划线", example = "000000000")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ListUserRoleRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
