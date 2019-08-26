package net.engining.profile.sdk.service;

import net.engining.profile.entity.enums.StatusDef;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description  用户维护信息查询
 * @Author yangli
 */

public class UserManagerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String puId;

    private String orgId;

    private String branchId;

    private String userId;

    private String name;

    private StatusDef status;

    private String email;

    private Date pwdExpDate;

    private Integer pwdTries;

    private List<String> roleName;

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StatusDef getStatus() {
        return status;
    }

    public void setStatus(StatusDef status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getPwdExpDate() {
        return pwdExpDate;
    }

    public void setPwdExpDate(Date pwdExpDate) {
        this.pwdExpDate = pwdExpDate;
    }

    public Integer getPwdTries() {
        return pwdTries;
    }

    public void setPwdTries(Integer pwdTries) {
        this.pwdTries = pwdTries;
    }

    public List<String> getRoleName() {
        return roleName;
    }

    public void setRoleName(List<String> roleName) {
        this.roleName = roleName;
    }
}
