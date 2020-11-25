package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author heqingxi
 */
public class LoginUserIdForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请输入：operUserId")
    @ApiModelProperty(value = "操作员id", required = true, example="1111")
    private String operUserId;

    @NotBlank(message = "请输入：用户id")
    @ApiModelProperty(value = "用户id", required = true, example="1111")
    private String userId;

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
