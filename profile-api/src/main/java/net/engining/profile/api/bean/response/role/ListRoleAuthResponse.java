package net.engining.profile.api.bean.response.role;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 角色拥有的权限查询结果
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 19:46
 * @since 1.0.0
 */
public class ListRoleAuthResponse implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 权限信息
     */
    @ApiModelProperty(value = "权限信息")
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
