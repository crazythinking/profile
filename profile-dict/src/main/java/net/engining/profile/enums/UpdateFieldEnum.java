package net.engining.profile.enums;

import net.engining.pg.support.enums.BaseEnum;
import net.engining.pg.support.meta.EnumInfo;

/**
 * 修改字段
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/10 11:03
 * @since 1.0.0
 */
@EnumInfo(value = {
        "ROLE_NAME|角色名称",
        "DEPARTMENT_NAME|部门名称",
        "ROLE_AUTH|角色名称",
        "USER_NAME|用户名称",
        "USER_STATUS|用户状态",
        "USER_ROLE|用户角色"
})
public enum UpdateFieldEnum implements BaseEnum<String> {
    /**
     * 角色名称
     */
    ROLE_NAME("ROLE_NAME", "角色名称"),
    /**
     * 部门名称
     */
    DEPARTMENT_NAME("DEPARTMENT_NAME", "部门名称"),
    /**
     * 角色权限
     */
    ROLE_AUTH("ROLE_AUTH", "角色权限"),
    /**
     * 用户名称
     */
    USER_NAME("USER_NAME", "用户名称"),
    /**
     * 用户状态
     */
    USER_STATUS("USER_STATUS", "用户状态"),
    /**
     * 用户角色
     */
    USER_ROLE("USER_ROLE", "用户角色"),;

    /**
     * 枚举值
     */
    private String value;
    /**
     * 中文描述
     */
    private String label;

    UpdateFieldEnum(String value, String label) {
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
