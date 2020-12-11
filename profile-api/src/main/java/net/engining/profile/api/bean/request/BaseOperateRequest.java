package net.engining.profile.api.bean.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 基础操作请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 15:04
 * @since 1.0.0
 */
public abstract class BaseOperateRequest implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 操作员ID
     */
    @NotBlank(message = "请输入：操作员ID")
    @Length(max = 30, min = 3, message = "用户ID的字段长度必须在3到30个字母或数字或下划线之间")
    @ApiModelProperty(value = "操作员ID|1-10位的任意字符", example = "admin", required = true)
    protected String operatorId;

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}
