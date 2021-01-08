package net.engining.profile.api.bean.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 基础分页查询结果
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 9:33
 * @since 1.0.0
 */
public abstract class BasePagingQueryResponse<T> implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数", example = "0", required = true)
    private Long totalNum;
    /**
     * 分页查询数据
     */
    @ApiModelProperty(value = "分页查询数据", required = true)
    private List<T> data;

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BasePagingQueryResponse{" +
                "totalNum=" + totalNum +
                ", data=" + data +
                '}';
    }
}
