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
    @Length(max = 30, min = 3, message = "用户ID的字段长度必须在3到30个字母或数字或下划线之间")
    @ApiModelProperty(value = "用户ID|3-30位的字母或数字或下划线", example = "000000000")
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

    @Override
    public String toString() {
        return "DistributeRolesRequest{" +
                "userId='" + userId + '\'' +
                ", roleIdList=" + roleIdList +
                ", operatorId='" + operatorId + '\'' +
                '}';
    }
}
