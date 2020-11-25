package net.engining.profile.api.bean.request.authority;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import net.engining.profile.api.bean.vo.RoleAuthVo;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 分配权限请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 18:33
 * @since 1.0.0
 */
public class DistributeAuthorityRequest extends BaseOperateRequest {
    /**
     * 角色ID
     */
    @NotBlank(message = "请输入：角色ID")
    @Length(max = 20, message = "角色ID的字段长度不能20个字符")
    @ApiModelProperty(value = "角色ID|1-20个字母或数字", example = "000000000", required = true)
    private String roleId;
    /**
     * 权限
     */
    @Valid
    @ApiModelProperty(value = "权限", example = "000000000", required = true)
    private List<RoleAuthVo> authList;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<RoleAuthVo> getAuthList() {
        return authList;
    }

    public void setAuthList(List<RoleAuthVo> authList) {
        this.authList = authList;
    }
}
