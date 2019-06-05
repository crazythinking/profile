package net.engining.profile.enums;

import net.engining.pg.support.enums.BaseEnum;

/**
 * 默认系统角色ID
 * @author Eric Lu
 *
 */
public enum DefaultRoleID implements BaseEnum<String> {
	
	/**
	 * 后端系统超级管理员
	 */
	SUPERADMIN("SUPERADMIN","后端系统超级管理员")
	;

	private final String value;

	private final String label;

	DefaultRoleID(String value, String label) {
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
