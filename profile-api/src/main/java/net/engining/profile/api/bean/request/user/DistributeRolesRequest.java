package net.engining.profile.api.bean.request.user;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 13:13
 * @since 1.0.0
 */
public class DistributeRolesRequest extends BaseOperateRequest {
    /**
     * 用户Id
     */
    @NotBlank(message = "请输入：用户ID")
    @Length(max = 40, message = "用户ID的字段长度不能超过40个字符")
    @ApiModelProperty(value = "用户ID|1-40位的字母或数字", example = "000000000", required = true)
    private String userId;
    /**
     * 角色ID集合
     */
    @ApiModelProperty(value = "角色ID集合|可为空的角色ID集合")
    private List<String> roleIdList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
