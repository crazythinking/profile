package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author heqingxi
 */
public class ProfileUserForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "PU_ID不能为空")
    @ApiModelProperty(value = "puId", required = false, example="123")
    private String puId;

    @ApiModelProperty(value = "操作员id", required = false, example="123")
    private String operUserId;

    @ApiModelProperty(value = "用户id", required = false, example="123")
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
