package net.engining.profile.security.validator;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.param.UsernamePattern;

/**
 * 用户名格式校验，根据参数设置的用户名正则表达式校验用户名，参见{@link UsernamePattern}
 * 
 * @author zhangkun
 * 
 */
@Service
@Configurable
public class UsernameFormatValidator implements SecurityControlValidator {

	@Override
	public void validate(ProfileUser user, String inputPassword, SecurityControl securityControl) throws ErrorMessageException {
		StringBuilder hint = new StringBuilder();
		boolean isWellFormed = true;
		boolean isfirstMismatch = true;
		for (UsernamePattern pattern : securityControl.usernamePattern) {
			if (!user.getUserId().matches(pattern.pattern)) {
				// 除了第一个提示短语，之后的需要先加顿号分隔
				if (isfirstMismatch) {
					isfirstMismatch = false;
				} else {
					hint.append("、");
				}
				hint.append(pattern.message);
				// 用户名不合规
				isWellFormed = false;
			}
		}
		if (!isWellFormed) {
			throw new ErrorMessageException(ErrorCode.CheckError, hint.toString());
		}
	}

}
