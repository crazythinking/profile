package net.engining.profile.api.bean.request.password;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 重置密码请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/10 9:28
 * @since 1.0.0
 */
public class ResetPasswordRequest extends BaseOperateRequest {
    /**
     * 用户ID
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
        return "ResetPasswordRequest{" +
                "userId='" + userId + '\'' +
                ", operatorId='" + operatorId + '\'' +
                '}';
    }
}
