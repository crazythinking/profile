package net.engining.profile.api.bean.response.user;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.vo.RoleSimpleVo;

import java.io.Serializable;
import java.util.List;

/**
 * 用户拥有角色查询结果
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 11:12
 * @since 1.0.0
 */
public class ListUserRoleResponse implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户拥有角色
     */
    @ApiModelProperty(value = "用户拥有角色")
    private List<RoleSimpleVo> data;

    public List<RoleSimpleVo> getData() {
        return data;
    }

    public void setData(List<RoleSimpleVo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ListUserRoleResponse{" +
                "data=" + data +
                '}';
    }
}
