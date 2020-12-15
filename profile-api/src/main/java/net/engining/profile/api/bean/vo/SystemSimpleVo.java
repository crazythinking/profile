package net.engining.profile.api.bean.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 所属系统简单数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 10:14
 * @since 1.0.0
 */
public class SystemSimpleVo implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 系统ID
     */
    @ApiModelProperty(value = "系统ID", example = "SCAC", required = true)
    private String systemId;
    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称", example = "账户中心", required = true)
    private String systemName;
    /**
     * 是否展示
     */
    @ApiModelProperty(value = "是否展示", example = "true", required = true)
    private Boolean shown;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Boolean getShown() {
        return shown;
    }

    public void setShown(Boolean shown) {
        this.shown = shown;
    }

    @Override
    public String toString() {
        return "SystemSimpleVo{" +
                "systemId='" + systemId + '\'' +
                ", systemName='" + systemName + '\'' +
                ", shown=" + shown +
                '}';
    }
}
