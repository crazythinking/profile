package net.engining.profile.sdk.service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;

/**
 * 用来在客户端保存和传递当前登录信息的对象。
 * 为了避免与 服务端的User命名重复，加上Client前缀。
 * @author chenjun.li
 *
 */
public class ClientUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private HashSet<String> authorities;
	
	private boolean needPasswordChange = false;
	
	/**
	 * 额外的客户端信息
	 */
	private Map<String, Object> props; 
	
	/**
	 * 该用户所属的用户组，key为groupId，value为name
	 */
	private Map<Integer, String> groups;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashSet<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(HashSet<String> authorities) {
		this.authorities = authorities;
	}

	public Map<Integer, String> getGroups() {
		return groups;
	}

	public void setGroups(Map<Integer, String> groups) {
		this.groups = groups;
	}

	public Map<String, Object> getProps() {
		return props;
	}

	public void setProps(Map<String, Object> props) {
		this.props = props;
	}

	public boolean isNeedPasswordChange()
	{
		return needPasswordChange;
	}

	public void setNeedPasswordChange(boolean needPasswordChange)
	{
		this.needPasswordChange = needPasswordChange;
	}

	/**
	 * 确定是否包含所有权限
	 * @param authorities
	 * @return
	 */
	public boolean hasAllAuthorities(String ... authorities)
	{
		for (String authority : authorities)
		{
			if (!this.authorities.contains(authority.toString()))
				return false;
		}
		return true;
	}

	/**
	 * 确定是否包含所有权限，以枚举的name()为权限字符串
	 * @param authorities
	 * @return
	 */
	public boolean hasAllAuthorities(Enum<?> ... authorities)
	{
		for (Enum<?> authority : authorities)
		{
			if (!getAuthorities().contains(authority.name()))
				return false;
		}
		
		return true;
	}
	
	/**
	 * 确定包含任一权限
	 * @param authorities
	 * @return
	 */
	public boolean hasAnyAuthority(String ... authorities)
	{
		for (String authority : authorities)
		{
			if (getAuthorities().contains(authority))
				return true;
		}
		
		return false;
	}

	/**
	 * 确定包含任一权限，以枚举的name()为权限字符串
	 * @param authorities
	 * @return
	 */
	public boolean hasAnyAuthority(Enum<?> ... authorities)
	{
		for (Enum<?> authority : authorities)
		{
			if (getAuthorities().contains(authority.name()))
				return true;
		}
		return false;
	}
}
