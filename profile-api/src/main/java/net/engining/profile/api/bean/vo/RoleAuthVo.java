package net.engining.profile.api.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
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
    @Length(max = 30, message = "菜单或者接口权限标识的字段长度不能超过30个数字、字母或下划线")
    @ApiModelProperty(value = "菜单或者接口权限标识|1-30位的数字、字母或下划线", required = true, example="menuCd")
    private String authority;
    /**
     * 菜单对应"-",接口对应所属菜单id
     */
    @NotBlank(message = "请输入：菜单或者接口权限url")
    @Length(max = 10, message = "菜单或者接口权限url的字段长度不能超过10个数字或短横线")
    @ApiModelProperty(value = "菜单或者接口权限url|1-10位的数字或短横线", required = true, example="1")
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
