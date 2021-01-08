package net.engining.profile.sdk.service.bean;

import net.engining.profile.enums.UpdateFieldEnum;

import java.io.Serializable;

/**
 * 修改字段记录
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/10 11:02
 * @since 1.0.0
 */
public class UpdateRecord implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 修改字段
     */
    private UpdateFieldEnum updateField;
    /**
     * 原值
     */
    private String oldValue;
    /**
     * 新值
     */
    private String newValue;

    public UpdateFieldEnum getUpdateField() {
        return updateField;
    }

    public void setUpdateField(UpdateFieldEnum updateField) {
        this.updateField = updateField;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
