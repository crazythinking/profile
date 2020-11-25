package net.engining.profile.api.bean.request.serurity;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BasePagingQueryRequest;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户操作日志分页查询请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 20:06
 * @since 1.0.0
 */
public class ListOperationLogRequest extends BasePagingQueryRequest {
    /**
     * 操作员ID
     */
    @Length(max = 10, message = "操作员ID的字符长度不能超过10个字符")
    @ApiModelProperty(value = "操作员ID|1-10个中文字符", example = "张三")
    private String operatorId;
    /**
     * 操作员名称
     */
    @Length(max = 10, message = "操作员名称的字符长度不能超过10个字符")
    @ApiModelProperty(value = "操作员名称|1-10个中文字符", example = "张三")
    private String operatorName;
    /**
     * 起始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "操作日期起始|yyyy-MM-dd", example = "2020-01-01")
    private Date startDate;
    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "操作日期截止|yyyy-MM-dd", example = "2020-01-01")
    private Date endDate;

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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
