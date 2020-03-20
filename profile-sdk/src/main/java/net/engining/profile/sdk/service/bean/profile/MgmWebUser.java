/**
 * 
 */
package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;
import net.engining.pg.web.bean.WebLoginUser;
import net.engining.profile.entity.enums.StatusDef;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author luxue
 *
 */
public class MgmWebUser extends WebLoginUser {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "PU_ID", required = true, example="123")
	private String puId;

	@ApiModelProperty(value = "操作员id", required = true, example="123")
	private String operUserId;

	@ApiModelProperty(value = "机构号", required = false, example="123")
	private String orgId;

	@ApiModelProperty(value = "分支id", required = false, example="123")
	private String branchId;

	/**
	 * 登录ID
	 */
	@NotBlank(message = "登录ID不能为空")
	@Pattern(regexp = "^[0-9a-zA-Z]{1,38}$", message = "用户id只能输入20位以内的字母或数字")
	@ApiModelProperty(value = "登录ID", required = true, example="123")
	private String userId;

	@NotBlank(message = "用户姓名不能为空")
	@ApiModelProperty(value = "用户姓名", required = true, example="123")
	private String name;

	@NotNull(message = "用户状态不能为空")
	@ApiModelProperty(value = "用户状态", required = true, example="A")
	private StatusDef status;

	@NotBlank(message = "电子邮箱不能为空")
	@ApiModelProperty(value = "email", required = false, example="A")
	private String email;

	@ApiModelProperty(value = "密码过期日期", required = false, example="2019-03-02")
	private Date pwdExpDate;

	@ApiModelProperty(value = "密码错误次数", required = true, example="1")
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
