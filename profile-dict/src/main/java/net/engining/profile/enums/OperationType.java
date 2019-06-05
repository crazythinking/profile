package net.engining.profile.enums;

import net.engining.pg.support.enums.BaseEnum;
import net.engining.pg.support.meta.EnumInfo;

/**
 * 操作类型
 * @author Eric Lu
 *
 */
@EnumInfo({
	"LG|登陆",
	"CP|修改密码"
})
public enum OperationType implements BaseEnum<String> {
	/**
	 * 登陆
	 */
	LG("LG","登陆"),
	
	/**
	 * 修改密码
	 */
	CP("CP","修改密码"),
	
	;

	private final String value;

	private final String label;

	OperationType(String value, String label) {
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
