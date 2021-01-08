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
    @Length(max = 30, min = 3, message = "用户ID的字段长度必须在3到30个字母或数字或下划线之间")
    @ApiModelProperty(value = "用户ID|3-30位的字母或数字或下划线", example = "000000000")
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

    @Override
    public String toString() {
        return "UpdateUserStatusRequest{" +
                "userId='" + userId + '\'' +
                ", userStatus=" + userStatus +
                ", operatorId='" + operatorId + '\'' +
                '}';
    }
}
