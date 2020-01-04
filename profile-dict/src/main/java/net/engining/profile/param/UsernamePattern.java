package net.engining.profile.param;

import java.io.Serializable;

import net.engining.pg.support.meta.PropertyInfo;

/**
 * 用户名正则表达式，包含用户名需要符合的正则表达式、不匹配时的提示信息等
 * 
 * @author zhangkun
 * 
 */
@SuppressWarnings("serial")
public class UsernamePattern implements Serializable {

	@PropertyInfo(name = "正则表达式", length = 100)
	public String pattern;

	/**
	 * 符合如下语境，如取得的值是“只能包含字母和数字”，则最后提示给用户的信息为“用户名必须只能包含字母和数字”，
	 * 当有多个提示信息时会提示用户类似这样的提示“用户名必须只能包含字母和数字、不能以下划线开头”
	 */
	@PropertyInfo(name = "提示信息", length = 100)
	public String message;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
