package net.engining.profile.sdk.service.bean.param;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description  返回结果信息包装
 * @Author yangli
 */
public class ResultMessageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("返回码")
    private String key;

    @ApiModelProperty("返回信息")
    private String message;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
