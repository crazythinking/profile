package net.engining.profile.api.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 权限
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 19:20
 * @since 1.0.0
 */
public class RoleAuthVo implements Serializable {
    /**
     * 序列化ID
     */
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
}
