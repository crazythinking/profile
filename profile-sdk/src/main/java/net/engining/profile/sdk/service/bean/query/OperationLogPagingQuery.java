package net.engining.profile.sdk.service.bean.query;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 操作日志分页查询参数
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 9:16
 * @since 1.0.0
 */
public class OperationLogPagingQuery extends BasePagingQuery {
    /**
     * 操作员ID
     */
    private String userId;
    /**
     * 操作员名称
     */
    private String userName;
    /**
     * 用户表ID
     */
    private String puId;
    /**
     * 起始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
