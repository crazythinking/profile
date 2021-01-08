package net.engining.profile.enums;

import net.engining.pg.support.enums.BaseEnum;
import net.engining.pg.support.meta.EnumInfo;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 10:56
 * @since 1.0.0
 */
@EnumInfo(value = {
        "SCAC|账户中心",
        "SCPC|产品中心",
        "OAUTH2|授权中心",
        "SCUCCUST|客户中心",
        "SCUMP|核销管理"
})
public enum SystemEnum implements BaseEnum<String> {
    /**
     * OAUTH2
     */
    OAUTH2("OAUTH2", "授权中心"),
    /**
     * SCAC
     */
    SCAC("SCAC", "账户中心"),
    /**
     * SCPC
     */
    SCPC("SCPC", "产品中心"),
    /**
     * SCUCCUST
     */
    SCUCCUST("SCUCCUST", "客户中心"),
    /**
     * SCUMP
     */
    SCUMP("SCUMP", "核销管理");

    /**
     * 枚举值
     */
    private String value;
    /**
     * 中文描述
     */
    private String label;

    SystemEnum(String value, String label) {
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
