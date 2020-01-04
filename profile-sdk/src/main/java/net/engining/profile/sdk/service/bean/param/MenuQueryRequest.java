package net.engining.profile.sdk.service.bean.param;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : lijiahui
 * @version : 版本
 * @Description :菜单查询请求po
 * @date : 2019/12/2 21:13
 */
public class MenuQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请输入：用户id")
    @ApiModelProperty(value = "用户id", required = true, example="123456")
    private String userId;

    @ApiModelProperty(value = "应用代码", required = false, example="appCd")
    private String appCd;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppCd() {
        return appCd;
    }

    public void setAppCd(String appCd) {
        this.appCd = appCd;
    }
}
