package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author liqingfeng
 *
 */
public class ProfileUserRoleForm implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "请输入：PU_ID")
	@ApiModelProperty(value = "PU_ID", required = true, example="1111")
	private String puId;

	@ApiModelProperty(value = "用户id", required = false, example="1111")
	private String userId;

	@NotNull(message = "请输入：角色id集合")
	@ApiModelProperty(value = "角色id集合", required = true, example="1111")
	private List<String> roleId;

	@ApiModelProperty(value = "应用代码", required = false, example="123456")
	private String appCd;

	public String getPuId() {
		return puId;
	}

	public String getAppCd() {
		return appCd;
	}

	public void setAppCd(String appCd) {
		this.appCd = appCd;
	}

	public void setPuId(String puId) {
		this.puId = puId;
	}

	public List<String> getRoleId() {
		return roleId;
	}

	public void setRoleId(List<String> roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
