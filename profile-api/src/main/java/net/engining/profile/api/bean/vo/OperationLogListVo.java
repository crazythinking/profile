package net.engining.profile.api.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作日志列表数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 20:17
 * @since 1.0.0
 */
public class OperationLogListVo implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 操作员ID
     */
    @ApiModelProperty(value = "操作员ID", example = "张三", required = true)
    private String operatorId;
    /**
     * 操作员名称
     */
    @ApiModelProperty(value = "操作员名称", example = "张三", required = true)
    private String operatorName;
    /**
     * 操作模块
     */
    @ApiModelProperty(value = "操作模块", example = "用户管理", required = true)
    private String operationModle;
    /**
     * 操作对象
     */
    @ApiModelProperty(value = "操作对象", example = "用户管理", required = true)
    private String operationTarget;
    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型", example = "用户新增", required = true)
    private String operationType;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间", example = "2020-01-01 00:00:00", required = true)
    private Date operationTimestamp;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "修改", required = true)
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

    public String getOperationModle() {
        return operationModle;
    }

    public void setOperationModle(String operationModle) {
        this.operationModle = operationModle;
    }

    public String getOperationTarget() {
        return operationTarget;
    }

    public void setOperationTarget(String operationTarget) {
        this.operationTarget = operationTarget;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
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
