package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 分配权限表单bean
 * @author yangxing
 */
public class ProfileRoleDelForm implements Serializable{

    private static final long serialVersionUID = 1L;

    @NotNull(message = "请输入：角色id")
    @ApiModelProperty(value = "角色id", required = true, example="admin")
    private List<String> roleId;

    @ApiModelProperty(value = "应用代码", required = false, example="123456")
    private String appCd;

    public List<String> getRoleId() {
        return roleId;
    }

    public void setRoleId(List<String> roleId) {
        this.roleId = roleId;
    }

    public String getAppCd() {
        return appCd;
    }

    public void setAppCd(String appCd) {
        this.appCd = appCd;
    }
}
