package net.engining.profile.security.validator;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.param.PasswordPattern;
import net.engining.profile.param.SecurityControl;
import org.springframework.stereotype.Service;

/**
 * 密码复杂度校验，根据参数设置的密码复杂度正则表达式校验密码，参见{@link PasswordPattern}
 * 
 * @author zhangkun
 * 
 */
@Service
public class PasswordComplexityValidator implements SecurityControlValidator {

	private static final String HINT_PREFIX = "密码必须";

	private int complexity = 0;

	@Override
	public void validate(ProfileUser user, String newPassword, SecurityControl securityControl) throws ErrorMessageException {
		// 根据参数决定是否校验复杂度
		if (securityControl.complexPwdInd) {
			boolean isWellFormed = true;
			StringBuilder hint = new StringBuilder(HINT_PREFIX);
			boolean isfirstMismatch = true;
			for (PasswordPattern pattern : securityControl.passwordPatterns) {
				if (!newPassword.matches(pattern.pattern)) {
					// 除了第一个提示短语，之后的需要先加顿号分隔
					if (isfirstMismatch) {
						isfirstMismatch = false;
					} else {
						hint.append("、");
					}
					hint.append(pattern.message);
					if (pattern.mustMatch) {
						// 密码不合规
						isWellFormed = false;
					}
				} else {
					// 累积复杂度权重
					complexity += pattern.weights;
				}
			}
			if (!isWellFormed) {
				throw new ErrorMessageException(ErrorCode.CheckError, hint.toString());
			}
		}
	}

	/**
	 * 校验完成后取得密码复杂度评分，当需要评分时，此对象必须是new出来的局部有效的对象，不能是通过spring注入的，因为它不是线程安全的，
	 * 一次构造只能为一个密码评分
	 * 
	 * @return 密码复杂度评分
	 */
	public int getComplexity() {
		return complexity;
	}

}
