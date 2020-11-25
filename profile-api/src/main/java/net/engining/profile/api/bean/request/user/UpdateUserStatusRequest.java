package net.engining.profile.api.bean.request.user;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import net.engining.profile.enums.UserStatusEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改用户状态请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 16:30
 * @since 1.0.0
 */
public class UpdateUserStatusRequest extends BaseOperateRequest {
    /**
     * 用户ID
     */
    @NotBlank(message = "请输入：用户ID")
    @Length(max = 40, message = "用户ID的字段长度不能超过40个字符")
    @ApiModelProperty(value = "用户ID|1-40位的字母或数字", example = "000000000", required = true)
    private String userId;
    /**
     * 用户状态
     */
    @NotNull(message = "请输入：用户状态")
    @ApiModelProperty(value = "用户状态|枚举", example = "A", notes = "net.engining.profile.enums.UserStatusEnum", required = true)
    private UserStatusEnum userStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserStatusEnum getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }
}
