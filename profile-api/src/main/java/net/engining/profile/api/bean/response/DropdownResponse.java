package net.engining.profile.api.bean.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 下拉框查询返回结果
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/12 9:43
 * @since 1.0.0
 */
public class DropdownResponse<T> implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 查询结果
     */
    @ApiModelProperty(value = "查询结果")
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DropdownResponse{" +
                "data=" + data +
                '}';
    }
}
