package net.engining.profile.enums;

import net.engining.pg.support.enums.BaseEnum;

/**
 *
 * @author yangxin
 */
public enum ProfileErrorCode implements BaseEnum<String> {

    /**
     *
     */
    Error9200("9200","用户名或密码错误"),

    /**
     *
     */
    Error9100("9100","账户被锁");

    private final String value;

    private final String label;

    ProfileErrorCode(String value, String label) {
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
