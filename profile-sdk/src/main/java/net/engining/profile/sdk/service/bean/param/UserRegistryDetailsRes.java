package net.engining.profile.sdk.service.bean.param;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Description: 用户登记薄bean
 *
 * @author heqingxi on 2019/7/28
 */
public class UserRegistryDetailsRes implements Serializable {

  private static final long serialVersionUID = 1L;


  /**用户ID*/
  @ApiModelProperty("被操作用户ID")
  private String  userId;

  /**用户ID*/
  @ApiModelProperty("被操作用户姓名")
  private String  name;


  /**所属机构*/
  @ApiModelProperty("所属机构")
  private String  orgId;

  /**所属部门*/
  @ApiModelProperty("所属部门")
  private String  branchId;

  /**用户名字*/
  @ApiModelProperty("录入用户id")
  private String  operRecUserId;

  /**用户名字*/
  @ApiModelProperty("复核用户id")
  private String  reViewUserId;

  /**邮箱*/
  @ApiModelProperty("邮箱")
  private String  email;

  /**状态*/
  @ApiModelProperty("状态")
  private String  status;

  /**操作类型*/
  @ApiModelProperty("操作类型")
  private String  operType;

  /**操作日期*/
  @ApiModelProperty("操作日期")
  private String  operTime;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getName() { return name; }

  public void setName(String name) {
        this.name = name;
    }

  public String getOrgId() { return orgId; }

  public void setOrgId(String orgId) { this.orgId = orgId; }

  public String getOperRecUserId() {
    return operRecUserId;
  }

  public void setOperRecUserId(String operRecUserId) {
    this.operRecUserId = operRecUserId;
  }

  public String getReViewUserId() {
    return reViewUserId;
  }

  public void setReViewUserId(String reViewUserId) {
    this.reViewUserId = reViewUserId;
  }

  public String getEmail() { return email; }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getStatus() { return status; }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getOperType() { return operType; }

  public void setOperType(String operType) {
    this.operType = operType;
  }

  public String getOperTime() { return operTime; }

  public void setOperTime(String operTime) {
    this.operTime = operTime;
  }

  public String getBranchId() { return branchId; }

  public void setBranchId(String branchId) { this.branchId = branchId; }
}
