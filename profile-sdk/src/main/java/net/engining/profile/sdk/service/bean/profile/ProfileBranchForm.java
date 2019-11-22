package net.engining.profile.sdk.service.bean.profile;

import java.io.Serializable;

/**
 * @author yangxing
 */
public class ProfileBranchForm implements Serializable{

	private static final long serialVersionUID = 4603644745622122258L;
	private String superiorId;
	private String branchId;
	private String orgId;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String branchName;
	private String addrCode;
	private String address;
	private String district;
}
