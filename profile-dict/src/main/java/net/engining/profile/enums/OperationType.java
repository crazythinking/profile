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
	"CP|修改密码",
	"AD|新增用户",
	"DE|删除用户",
	"QU|查询用户",
	"UP|修改用户"
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
	/**
	 * 新增
	 */
	AD("AD","新增用户"),
	/**
	 * 删除
	 */
	DE("DE","删除用户"),
	/**
	 * 查询
	 */
	QU("QU","查询用户"),
	/**
	 * 分配
	 */
	GN("GN","分配角色"),
	/**
	 * 修改
	 */
	UP("UP","修改用户"),
	/**
	 * 新增角色
	 */
	AR("AD", "新增角色"),
	/**
	 * 修改角色
	 */
	UR("UR", "修改角色"),
	/**
	 * 修改用户状态
	 */
	US("US", "修改用户状态"),
    /**
     * 新增部门
     */
    AB("AB", "新增部门"),
    /**
     * 修改部门
     */
    UB("UB", "修改部门"),
    /**
     * 删除部门
     */
    DB("DB", "删除部门"),
	/**
	 * 系统调用
	 */
	SC("SC", "系统调用"),
	/**
	 * 登出
	 */
	LO("LO", "登出"),
	/**
	 * 分配权限
	 */
	DA("DA", "分配权限"),
	/**
	 * 重置密码
	 */
	RP("RP", "重置密码");

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
