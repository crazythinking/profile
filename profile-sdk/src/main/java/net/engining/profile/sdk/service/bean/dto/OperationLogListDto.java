package net.engining.profile.sdk.service.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.enums.OperationType;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志分页查询结果
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 9:22
 * @since 1.0.0
 */
public class OperationLogListDto implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 操作员ID
     */
    private String operatorId;
    /**
     * 操作员名称
     */
    private String operatorName;
    /**
     * 操作对象
     */
    private String operationTarget;
    /**
     * 操作类型
     */
    private OperationType operationType;
    /**
     * 操作时间
     */
    private Date operationTimestamp;
    /**
     * 备注
     */
    private String remarks;

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

    public String getOperationTarget() {
        return operationTarget;
    }

    public void setOperationTarget(String operationTarget) {
        this.operationTarget = operationTarget;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Date getOperationTimestamp() {
        return operationTimestamp;
    }

    public void setOperationTimestamp(Date operationTimestamp) {
        this.operationTimestamp = operationTimestamp;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
