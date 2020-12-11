package net.engining.profile.api.bean.request.password;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 16:49
 * @since 1.0.0
 */
public class ChangePasswordRequest extends BaseOperateRequest {
    /**
     * 用户表Id
     */
    @NotBlank(message = "请输入：用户表Id")
    @Length(max = 64, message = "用户表ID最大字段长度不能超过64个字符")
    @ApiModelProperty(value = "用户表Id|UUID", example = "$10$0aa7pimPxggOnn4BlCu8sujAsLApu7gtfYlTt3cTmD/3FcKAB7in6", required = true)
    private String puId;
    /**
     * 原密码
     */
    @NotBlank(message = "请输入：原密码")
    @Length(max = 40, message = "原密码最大字段长度不能超过40个任意字符")
    @ApiModelProperty(value = "原密码|1-40位的任意字符（具体规则参照授权中心密码管理规则）", example = "A123456", required = true)
    private String oldPassword;
    /**
     * 新密码
     */
    @NotBlank(message = "请输入：新密码")
    @Length(max = 40, message = "新密码最大字段长度不能超过40个任意字符")
    @ApiModelProperty(value = "新密码|1-40位的任意字符（具体规则参照授权中心密码管理规则）", example = "A123456", required = true)
    private String newPassword;

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordRequest{" +
                "puId='" + puId + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", operatorId='" + operatorId + '\'' +
                '}';
    }
}
