package net.engining.profile.enums;

import net.engining.pg.support.enums.BaseEnum;

/**
 * 初始化角色-示例
 * @author 作者
 * @version 版本
 * @since
 * @date 2019/8/14 10:20
 */
public enum RoleIdEnum implements BaseEnum<String> {
    /**
     * 高级查询
     */
    ADVANCESEARCH("ADVANCESEARCH", "高级查询"),
    /**
     * 普通查询
     */
    NORMALSEARCH("NORMALSEARCH", "普通查询"),
    /**
     * 综合主管
     */
    GENERALMANAGER("GENERALMANAGER", "综合主管"),
    /**
     * 核算经办
     */
    ACCOUNTING("ACCOUNTING", "核算经办"),
    /**
     * 权限维护
     */
    AUTHORITYMAINTENANCE("AUTHORITYMAINTENANCE", "权限维护"),


    /**
     *
     */
    ACTUATOR("ACTUATOR","监控");
    private final String value;
    private final String label;

    RoleIdEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getLabel() {
        return this.label;
    }
}
