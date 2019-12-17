package net.engining.profile.sdk.service.bean.param;

import io.swagger.annotations.ApiModelProperty;
import net.engining.pg.support.db.querydsl.Range;

import java.io.Serializable;


/**
 * Description: 用户登记薄参数bean
 *
 * @author heqingxi on 2019/7/28
 */
public class UserRegistryDetailsReq implements Serializable {

  private static final long serialVersionUID = 1L;

  /**puId*/
  @ApiModelProperty(value = "puid", required = false, example="123")
  private String  puId;

  /**所属机构*/
  @ApiModelProperty(value = "所属机构", required = false, example="123")
  private String  affiliation;

  /**部门*/
  @ApiModelProperty(value = "部门", required = false, example="123")
  private String  department;

  /**用户ID*/
  @ApiModelProperty(value = "用户ID", required = false, example="123")
  private String  userId;

  /**用户名字*/
  @ApiModelProperty(value = "用户姓名", required = false, example="123")
  private String  userName;

  /**分页*/
  @ApiModelProperty(value = "分页", required = false, example="")
  private Range range;

  public String getAffiliation() {
    return affiliation;
  }

  public void setAffiliation(String affiliation) {
    this.affiliation = affiliation;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Range getRange() { return range; }

  public void setRange(Range range) { this.range = range; }

  public String getPuId() { return puId; }

  public void setPuId(String puId) { this.puId = puId; }
}
