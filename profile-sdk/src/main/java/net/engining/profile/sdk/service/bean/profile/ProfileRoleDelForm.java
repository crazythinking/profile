package net.engining.profile.sdk.service.bean.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> roleId;

    public List<String> getRoleId() {
        return roleId;
    }

    public void setRoleId(List<String> roleId) {
        this.roleId = roleId;
    }

}
