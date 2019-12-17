package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yangxing
 */
public class ProfileBranchForm implements Serializable{

	private static final long serialVersionUID = 4603644745622122258L;

	@ApiModelProperty(value = "上级分支", required = false, example="123")
	private String superiorId;

	@ApiModelProperty(value = "分支id", required = true, example="123")
	@NotNull(message = "分支id不能为空")
	private String branchId;

	@ApiModelProperty(value = "机构id", required = true, example="123")
	@NotNull(message = "机构id不能为空")
	private String orgId;

	@ApiModelProperty(value = "分支名", required = true, example="xxx")
	@NotNull(message = "分支名不能为空")
	private String branchName;

	@ApiModelProperty(value = "所属地区码", required = false, example="xxx")
	private String addrCode;

	@ApiModelProperty(value = "地址", required = false, example="xxx")
	private String address;

	@ApiModelProperty(value = "区", required = false, example="xxx")
	private String district;

	public String getSuperiorId() {
		return superiorId;
	}

	public void setSuperiorId(String superiorId) {
		this.superiorId = superiorId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAddrCode() {
		return addrCode;
	}

	public void setAddrCode(String addrCode) {
		this.addrCode = addrCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
}
