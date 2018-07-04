package net.engining.profile.security.validator;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.param.SecurityControl;

/**
 * 用户密码有效期验证器，如{@link ProfileUser#getPwdNeverExp()}为false则执行校验，否则不校验<br\>
 * 如当前系统时间超过{@link ProfileUser#getPwdExpDate()}则抛出用
 * {@link AuthenticationResult#EXPIRED}构造的{@link AuthenticateException}
 * 
 * @author zhangkun
 * 
 */
@Service
@Configurable
public class PasswordExpireValidator implements SecurityControlValidator {

	@Override
	public void validate(ProfileUser user, String inputPassword, SecurityControl securityControl) throws ErrorMessageException {
		if (user.getPwdExpDate()!=null) {
			if (user.getPwdExpDate().compareTo(Calendar.getInstance().getTime()) < 0) {
				throw new ErrorMessageException(ErrorCode.CheckError, "密码已过期");
			}
		}
	}

}
