package net.engining.profile.api.bean.response.authority;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 可分配权限查询结果
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 17:26
 * @since 1.0.0
 */
public class ListAuthorityResponse implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 可分配权限
     */
    @ApiModelProperty(value = "可分配权限", example = "", required = true)
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ListAuthorityResponse{" +
                "data='" + data + '\'' +
                '}';
    }
}
