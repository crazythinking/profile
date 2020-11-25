package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.enums.UserStatusEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author heqingxi
 */
public class ProfileUserUpdateForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请输入：puId")
    @ApiModelProperty(value = "puId", required = true, example="1111")
    private String puId;

    @NotBlank(message = "请输入：operUserId")
    @ApiModelProperty(value = "操作员id", required = true, example="1111")
    private String operUserId;
    /**
     * 用户姓名
     */
    @NotBlank(message = "请输入：用户姓名")
    @ApiModelProperty(value = "用户姓名", required = true, example="xxx")
    private String name;

    @NotBlank(message = "请输入：部门id")
    @ApiModelProperty(value = "部门id", required = true, example="111")
    private String branchId;

    @NotBlank(message = "请输入：机构id")
    @ApiModelProperty(value = "机构id", required = true, example="111")
    private String orgId;

    /**
     * 登录ID
     */
    @NotBlank(message = "请输入：用户id")
    @ApiModelProperty(value = "用户id", required = true, example="111")
    private String userId;

    /**
     * 用户状态
     */
    @NotNull(message = "请输入：用户状态")
    @ApiModelProperty(value = "用户状态", required = true, example="111")
    private UserStatusEnum status;

    @NotBlank(message = "请输入：邮箱")
    @ApiModelProperty(value = "邮箱", required = true, example="@")
    private String email;

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public String getOperUserId() {
        return operUserId;
    }

    public void setOperUserId(String operUserId) {
        this.operUserId = operUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
