package net.engining.profile.sdk.service.bean.profile;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description  菜单权限对象VO实体类
 * @Author heqingxi
 */
public class MenuOrAuthInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单或者接口cd
     */
    @NotBlank(message = "请输入：菜单或者接口权限标识")
    @ApiModelProperty(value = "菜单或者接口权限标识", required = true, example="menuCd")
    private String authority;
    /**
     * 菜单对应"-",接口对应所属菜单id
     */
    @NotBlank(message = "请输入：菜单或者接口权限url")
    @ApiModelProperty(value = "菜单或者接口权限url", required = true, example="1")
    private String autuUri;
    /**
     * 应用代码
     */
    @ApiModelProperty(value = "应用代码cd", required = false, example="tm-online-sv")
    private String appCd;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAutuUri() {
        return autuUri;
    }

    public void setAutuUri(String autuUri) {
        this.autuUri = autuUri;
    }

    public String getAppCd() {
        return appCd;
    }

    public void setAppCd(String appCd) {
        this.appCd = appCd;
    }
}
