package net.engining.profile.sdk.service.bean.param;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 用户登记薄详情bean
 *
 * @author heqingxi on 2019/8/8
 */
public class UserRegistrySearchBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**puId*/
    @ApiModelProperty("puid")
    private String  puId;

    /**所属机构*/
    @ApiModelProperty("所属机构")
    private String  affiliation;

    /**部门*/
    @ApiModelProperty("部门")
    private String  department;

    /**用户ID*/
    @ApiModelProperty("用户ID")
    private String  userId;

    /**用户名字*/
    @ApiModelProperty("用户姓名")
    private String  userName;

    /**变更日期*/
    @ApiModelProperty("变更日期")
    private Date operTime;

    /**变更类型*/
    @ApiModelProperty("变更类型")
    private String operType;

    /**被操作者puid*/
    @ApiModelProperty("被操作者puid")
    private String beoperatedId;


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

    public String getPuId() { return puId; }

    public void setPuId(String puId) { this.puId = puId; }

    public Date getOperTime() { return operTime; }

    public void setOperTime(Date operTime) { this.operTime = operTime; }

    public String getOperType() { return operType; }

    public void setOperType(String operType) { this.operType = operType; }

    public String getBeoperatedId() { return beoperatedId; }

    public void setBeoperatedId(String beoperatedId) { this.beoperatedId = beoperatedId; }
}
