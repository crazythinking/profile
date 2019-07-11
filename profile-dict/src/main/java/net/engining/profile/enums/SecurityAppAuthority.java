package net.engining.profile.enums;

import net.engining.pg.support.enums.BaseEnum;

/**
 * 系统预设权限ID
 *
 * @author luxue
 */
public enum SecurityAppAuthority implements BaseEnum<String> {

    /**
     * 用户管理
     */
    ProfileUser("ProfileUser", "用户管理"),

    /**
     * 分支管理
     */
    ProfileBranch("ProfileBranch", "分支机构管理"),

    /**
     * 角色管理
     */
    ProfileRole("ProfileRole", "角色管理")

    ;

    private final String value;

    private final String label;

    SecurityAppAuthority(String value, String label) {
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
