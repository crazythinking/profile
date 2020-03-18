package net.engining.profile.security.validator;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.param.SecurityControl;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * 用户密码有效期验证器;
 * 如当前系统时间超过{@link ProfileUser#getPwdExpDate()}则抛出{@link ErrorMessageException}
 * 
 * @author zhangkun
 * 
 */
@Service
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
