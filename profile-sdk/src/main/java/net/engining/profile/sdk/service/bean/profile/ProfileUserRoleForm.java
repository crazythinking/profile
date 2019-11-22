package net.engining.profile.sdk.service.bean.profile;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 
 * @author liqingfeng
 *
 */
public class ProfileUserRoleForm {

	@NotBlank
	private String puId;

	private String userId;
	
	private List<String> roleId;

	public String getPuId() {
		return puId;
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
