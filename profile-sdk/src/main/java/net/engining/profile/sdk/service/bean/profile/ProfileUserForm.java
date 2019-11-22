package net.engining.profile.sdk.service.bean.profile;

import net.engining.pg.support.db.querydsl.Range;
import net.engining.profile.entity.enums.StatusDef;

import java.io.Serializable;

/**
 * @author heqingxi
 */
public class ProfileUserForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String puId;

    private String operUserId;
    /**
     * 用户姓名
     */
    private String name;

    private String branchId;

    private String orgId;

    private Range range;

    /**
     * 登录ID
     */
    private String userId;

    private String password;

    /**
     * 用户状态
     */
    private StatusDef status;

    private String roleStr;

    private String email;

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the status
     */
    public StatusDef getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusDef status) {
        this.status = status;
    }

    /**
     * @return the branchId
     */
    public String getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the orgId
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public String getRoleStr() {
        return roleStr;
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }

    public String getOperUserId() {
        return operUserId;
    }

    public void setOperUserId(String operUserId) {
        this.operUserId = operUserId;
    }
}
