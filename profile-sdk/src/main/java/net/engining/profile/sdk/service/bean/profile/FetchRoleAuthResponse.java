package net.engining.profile.sdk.service.bean.profile;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @Description  角色权限返回实体类
 * @Author heqingxi
 */
public class FetchRoleAuthResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限标识集合
     */
    @ApiModelProperty(value = "权限标识集合", required = false, example="profile")
    private List<String> authList;

    public List<String> getAuthList() {
        return authList;
    }

    public void setAuthList(List<String> authList) {
        this.authList = authList;
    }
}
