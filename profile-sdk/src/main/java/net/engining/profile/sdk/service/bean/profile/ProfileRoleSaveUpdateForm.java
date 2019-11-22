package net.engining.profile.sdk.service.bean.profile;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 
 * @author liqingfeng
 *
 */
public class ProfileRoleSaveUpdateForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String roleId;
	@NotBlank
	private String branchId;
	@NotBlank
	private String roleName;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
