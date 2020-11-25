package net.engining.profile.sdk.service.bean.query;

import net.engining.pg.support.db.querydsl.Range;

import java.io.Serializable;

/**
 * 基础分页查询参数
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 13:24
 * @since 1.0.0
 */
public abstract class BasePagingQuery implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 起始数
     */
    private Long pageNum;
    /**
     * 查询笔数
     */
    private Long pageSize;

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

    /**
     * 封装分页参数
     */
    public Range getRange() {
        return new Range(Integer.valueOf(this.pageNum.toString()),
                Integer.valueOf(this.pageSize.toString()));
    }
}
