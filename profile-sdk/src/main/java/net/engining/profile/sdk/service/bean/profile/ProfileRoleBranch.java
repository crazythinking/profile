package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;
import net.engining.pg.support.db.querydsl.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 
 * @author liqingfeng
 *
 */
public class ProfileRoleBranch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "机构id", required = false, example="10000")
	private String branchId;

	@ApiModelProperty(value = "角色名称", required = false, example="admin")
	private String roleName;

	@ApiModelProperty(value = "应用代码", required = false, example="123456")
	private String appCd;

	private Range range;

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

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

}
