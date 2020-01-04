package net.engining.profile.sdk.service.bean;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import net.engining.pg.support.meta.PropertyInfo;

public class ChangePasswordForm implements Serializable {
	private static final long serialVersionUID = 4936567826391896309L;

	@NotBlank(message = "原密码不能为空")
	@ApiModelProperty(value = "原密码", required = true, example="123")
	@PropertyInfo(name="原密码", length=100)
	private String oldPassword;
	
	@NotBlank
	@ApiModelProperty(value = "新密码", required = true, example="123")
	@PropertyInfo(name="新密码", length=100)
	private String newPassword;

	@ApiModelProperty(value = "确认密码", required = true, example="123")
	@PropertyInfo(name="确认密码", length=100)
	private String retypePassword;
	
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

	public String getRetypePassword() {
		return retypePassword;
	}

	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}
}
