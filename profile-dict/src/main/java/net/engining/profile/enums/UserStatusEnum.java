package net.engining.profile.enums;

import net.engining.pg.support.enums.BaseEnum;
import net.engining.pg.support.meta.EnumInfo;

/**
 * 用户状态
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 16:37
 * @since 1.0.0
 */
@EnumInfo(value = {
        "A|正常",
        "L|用户锁定",
        "P|密码锁定"
})
public enum UserStatusEnum implements BaseEnum<String> {
     /**
     * 新增
     */
     N("N","新增"),
    /**
     * 活动
     */
    A("A","正常"),
    /**
     * 锁定
     */
    L("L","用户锁定"),
    /**
     * 密码锁定
     */
    P("P", "密码锁定");

    /**
     * 枚举值
     */
    private String value;
    /**
     * 中文描述
     */
    private String label;

    UserStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
