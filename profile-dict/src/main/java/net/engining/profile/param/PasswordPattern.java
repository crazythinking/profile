package net.engining.profile.param;

import java.io.Serializable;

import net.engining.pg.support.meta.PropertyInfo;

/**
 * 密码复杂度正则表达式，包含密码需要符合的正则表达式、是否必须和如果不满足要求的提示信息等
 * 
 * @author zhangkun
 * 
 */
@SuppressWarnings("serial")
public class PasswordPattern implements Serializable {

	@PropertyInfo(name =  "正则表达式", length = 100)
	public String pattern;

	@PropertyInfo(name =  "必须匹配")
	public Boolean mustMatch;

	/**
	 * 符合如下语境，如取得的值是“包含大写字母”，则最后提示给用户的信息为“密码必须包含大写字母”，当有多个提示信息时会提示用户类似这样的提示“
	 * 密码必须包含大写字母、包含小写字母、长度在6-12之间”
	 */
	@PropertyInfo(name =  "提示信息", length = 100)
	public String message;

	/**
	 * 当前匹配在密码复杂度中占的权重，总权重为100，可用于评价密码复杂度评分
	 */
	@PropertyInfo(name =  "权重", length = 9)
	public Integer weights;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Boolean getMustMatch() {
		return mustMatch;
	}

	public void setMustMatch(Boolean mustMatch) {
		this.mustMatch = mustMatch;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getWeights() {
		return weights;
	}

	public void setWeights(Integer weights) {
		this.weights = weights;
	}

}
