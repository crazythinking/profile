package net.engining.profile.sdk.service.bean;

import net.engining.pg.support.enums.BaseEnum;

public enum ErrorCodeDef  implements BaseEnum<String> {

    CheckError("9200","用户名密码错误"),

    CheckErrorA("9100","账户限制");
    private final String value;

    private final String label;

    ErrorCodeDef(String value, String label) {
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
