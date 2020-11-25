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
    @Length(max = 40, message = "用户ID的字段长度不能超过40个字符")
    @ApiModelProperty(value = "用户ID|1-40位的字母或数字", example = "000000000")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
