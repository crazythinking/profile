/**
 * 
 */
package net.engining.profile.sdk.service.bean.profile;

import net.engining.pg.web.bean.WebLoginUser;
import net.engining.profile.entity.enums.StatusDef;

import java.util.Date;

/**
 * @author luxue
 *
 */
public class MgmWebUser extends WebLoginUser {

	private static final long serialVersionUID = 1L;

	/**
	 * Profile ID，用于对接权限系统的唯一键
	 */
	private String puId;

	private String operUserId;

	private String orgId;

	private String branchId;

	/**
	 * 登录ID
	 */
	private String userId;

	private String name;

	private StatusDef status;

	private String email;

	private Date pwdExpDate;

	private Integer pwdTries;

	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId
	 *            the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the branchId
	 */
	public String getBranchId() {
		return branchId;
	}

	/**
	 * @param branchId
	 *            the branchId to set
	 */
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public StatusDef getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(StatusDef status) {
		this.status = status;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the pwdExpDate
	 */
	public Date getPwdExpDate() {
		return pwdExpDate;
	}

	/**
	 * @param pwdExpDate
	 *            the pwdExpDate to set
	 */
	public void setPwdExpDate(Date pwdExpDate) {
		this.pwdExpDate = pwdExpDate;
	}

	/**
	 * @return the pwdTries
	 */
	public Integer getPwdTries() {
		return pwdTries;
	}

	/**
	 * @param pwdTries
	 *            the pwdTries to set
	 */
	public void setPwdTries(Integer pwdTries) {
		this.pwdTries = pwdTries;
	}

	/**
	 * @return the puId
	 */
	public String getPuId() {
		return puId;
	}

	/**
	 * @param puId
	 *            the puId to set
	 */
	public void setPuId(String puId) {
		this.puId = puId;
	}

	public String getOperUserId() {
		return operUserId;
	}

	public void setOperUserId(String operUserId) {
		this.operUserId = operUserId;
	}
}
