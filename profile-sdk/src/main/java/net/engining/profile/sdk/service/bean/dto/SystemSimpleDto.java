package net.engining.profile.sdk.service.bean.dto;

import java.io.Serializable;

/**
 * 所属系统简单数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 11:22
 * @since 1.0.0
 */
public class SystemSimpleDto implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 系统ID
     */
    private String systemId;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 是否展示
     */
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
}
