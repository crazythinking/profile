package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

	@NotBlank(message = "请输入：角色id")
    @Pattern(regexp = "^[0-9a-zA-Z]{1,18}$", message = "角色id只能输入18位以内的字母或数字")
	@ApiModelProperty(value = "角色id", required = true, example="admin")
	private String roleId;

	@NotBlank(message = "请输入：机构id")
	@ApiModelProperty(value = "机构id", required = true, example="10000")
	private String branchId;

	@NotBlank(message = "请输入：角色名称")
	@ApiModelProperty(value = "角色名称", required = true, example="admin")
	private String roleName;

	@ApiModelProperty(value = "应用代码", required = false, example="123456")
	private String appCd;

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

	public String getAppCd() {
		return appCd;
	}

	public void setAppCd(String appCd) {
		this.appCd = appCd;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
