package net.engining.profile.sdk.service.bean.param;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description
 * @Author yangli
 */
public class PasswordRuleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("密码有效期")
    @NotNull(message = "密码有效期不能为空")
    public Integer pwdExpireDays;

    @ApiModelProperty("首次登陆密码更改")
    @NotNull(message = "首次登陆密码更改不能为空")
    public String pwdFirstLoginChgInd;

    @ApiModelProperty("连续输错次数")
    @NotNull(message = "连续输错次数不能为空")
    public Integer pwdTries;

    @ApiModelProperty("过期设置")
    @NotNull(message = "过期设置不能为空")
    public String expirationSettings;

    @ApiModelProperty("复杂度校验")
    @NotNull(message = "复杂度校验不能为空")
    public String complexPwdInd;

    @ApiModelProperty("最小长度")
    @NotNull(message = "最小长度不能为空")
    public Integer minimumLength;

    @ApiModelProperty("最大长度")
    @NotNull(message = "最大长度不能为空")
    public Integer maximumLength;

    public Integer getPwdExpireDays() {
        return pwdExpireDays;
    }

    public void setPwdExpireDays(Integer pwdExpireDays) {
        this.pwdExpireDays = pwdExpireDays;
    }

    public String getPwdFirstLoginChgInd() {
        return pwdFirstLoginChgInd;
    }

    public void setPwdFirstLoginChgInd(String pwdFirstLoginChgInd) {
        this.pwdFirstLoginChgInd = pwdFirstLoginChgInd;
    }

    public Integer getPwdTries() {
        return pwdTries;
    }

    public void setPwdTries(Integer pwdTries) {
        this.pwdTries = pwdTries;
    }

    public String getExpirationSettings() {
        return expirationSettings;
    }

    public void setExpirationSettings(String expirationSettings) {
        this.expirationSettings = expirationSettings;
    }

    public String getComplexPwdInd() {
        return complexPwdInd;
    }

    public void setComplexPwdInd(String complexPwdInd) {
        this.complexPwdInd = complexPwdInd;
    }

    public Integer getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }

    public Integer getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }
}
