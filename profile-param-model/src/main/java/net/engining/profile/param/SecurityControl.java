package net.engining.profile.param;

import java.io.Serializable;
import java.util.List;

import net.engining.pg.support.meta.PropertyInfo;

/**
 * 安全控制参数
 * 
 * @author Ronny<br/>zhangkun
 *
 */
@SuppressWarnings("serial")
public class SecurityControl implements Serializable {

	/**
	 * 密码有效天数
	 */
	@PropertyInfo(name="密码有效天数", length=4)
	public Integer pwdExpireDays;
	
	 /**
     * 首次登陆口令修改提示
     * true-要求修改
     * false-不要求修改
     */
	@PropertyInfo(name="首次登陆强制修改密码", length=1)
	public Boolean pwdFirstLoginChgInd;
	
	/**
	 * 密码允许尝试的次数
	 */
	@PropertyInfo(name="允许尝试的次数", length=1)
	public Integer pwdTries = 3;
	
	/**
	 * 用户名需要符合的正则表达式
	 */
	public List<UsernamePattern> usernamePattern;
	
	 /**
     * 是否强制复杂密码
     * true-强制要求复杂密码
     * false-不强制要求复杂密码
     * 如果为false则不验证正则表达式
     */
	@PropertyInfo(name="要求复杂密码", length=1)
	public Boolean complexPwdInd;
	
	/**
	 * 密码需要符合的正则表达式，对密码的格式要求都可以设置在这个参数中，如密码长度，要求有什么样的字符等，参见{@link PasswordPattern}
	 */
	public List<PasswordPattern> passwordPatterns;
	
	/**
	 * 密码历史循环数
	 */
	@PropertyInfo(name="密码历史循环数", length=2)
	public Integer pwdCycleCnt;

	public Integer getPwdExpireDays() {
		return pwdExpireDays;
	}

	public void setPwdExpireDays(Integer pwdExpireDays) {
		this.pwdExpireDays = pwdExpireDays;
	}

	public Boolean getPwdFirstLoginChgInd() {
		return pwdFirstLoginChgInd;
	}

	public void setPwdFirstLoginChgInd(Boolean pwdFirstLoginChgInd) {
		this.pwdFirstLoginChgInd = pwdFirstLoginChgInd;
	}

	public Integer getPwdTries() {
		return pwdTries;
	}

	public void setPwdTries(Integer pwdTries) {
		this.pwdTries = pwdTries;
	}

	public List<UsernamePattern> getUsernamePattern() {
		return usernamePattern;
	}

	public void setUsernamePattern(List<UsernamePattern> usernamePattern) {
		this.usernamePattern = usernamePattern;
	}

	public Boolean getComplexPwdInd() {
		return complexPwdInd;
	}

	public void setComplexPwdInd(Boolean complexPwdInd) {
		this.complexPwdInd = complexPwdInd;
	}

	public List<PasswordPattern> getPasswordPatterns() {
		return passwordPatterns;
	}

	public void setPasswordPatterns(List<PasswordPattern> passwordPatterns) {
		this.passwordPatterns = passwordPatterns;
	}

	public Integer getPwdCycleCnt() {
		return pwdCycleCnt;
	}

	public void setPwdCycleCnt(Integer pwdCycleCnt) {
		this.pwdCycleCnt = pwdCycleCnt;
	} 
	
}