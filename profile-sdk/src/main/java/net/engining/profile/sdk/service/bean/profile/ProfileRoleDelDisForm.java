package net.engining.profile.sdk.service.bean.profile;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author yangxing
 */
public class ProfileRoleDelDisForm implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String roleId;

    private String authStr;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAuthStr() {
        return authStr;
    }

    public void setAuthStr(String authStr) {
        this.authStr = authStr;
    }

}
