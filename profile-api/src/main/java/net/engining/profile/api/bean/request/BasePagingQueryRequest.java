package net.engining.profile.api.bean.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 基础分页查询请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 9:17
 * @since 1.0.0
 */
public abstract class BasePagingQueryRequest implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 起始数
     */
    @NotNull(message = "请输入：起始数")
    @Min(value = 0L, message = "起始数必须是自然数")
    @ApiModelProperty(value = "起始数|自然数", example = "0", required = true)
    protected Long pageNum;
    /**
     * 查询笔数
     */
    @NotNull(message = "请输入：查询笔数")
    @Min(value = 0L, message = "查询笔数必须是正整数")
    @ApiModelProperty(value = "查询笔数|[10，15，20]", example = "10", required = true)
    protected Long pageSize;

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "BasePagingQueryRequest{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
