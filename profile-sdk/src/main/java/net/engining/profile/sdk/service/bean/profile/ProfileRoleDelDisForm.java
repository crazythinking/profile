package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 分配权限表单bean
 * @author yangxing
 */
public class ProfileRoleDelDisForm implements Serializable{

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请输入：角色id")
    @ApiModelProperty(value = "角色id", required = true, example="admin")
    private String roleId;

    @Valid
    private List<MenuOrAuthInfo> authList;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<MenuOrAuthInfo> getAuthList() {
        return authList;
    }

    public void setAuthList(List<MenuOrAuthInfo> authList) {
        this.authList = authList;
    }
}
