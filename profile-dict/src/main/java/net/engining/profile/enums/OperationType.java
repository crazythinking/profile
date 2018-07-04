package net.engining.profile.enums;

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
public enum OperationType {
	/**
	 * 登陆
	 */
	LG,
	
	/**
	 * 修改密码
	 */
	CP,
	
	;

}
