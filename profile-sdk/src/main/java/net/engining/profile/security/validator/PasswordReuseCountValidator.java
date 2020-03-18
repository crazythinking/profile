package net.engining.profile.security.validator;

import java.text.MessageFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.querydsl.jpa.impl.JPAQueryFactory;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.entity.model.QProfilePwdHist;
import net.engining.profile.param.SecurityControl;

/**
 * 验证同一密码使用次数是否超过限制
 * 
 * @author zhangkun
 * 
 */
@Service
public class PasswordReuseCountValidator implements SecurityControlValidator {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void validate(ProfileUser user, String rawNewPassword, SecurityControl securityControl)
			throws ErrorMessageException {
		if (user.getPuId() != null) {
			String newPassword = passwordEncoder.encode(rawNewPassword);
			QProfilePwdHist qProfilePwdHist = QProfilePwdHist.profilePwdHist;
			List<String> passwords = new JPAQueryFactory(entityManager)
					.select(qProfilePwdHist.password)
					.from(qProfilePwdHist)
					.where(qProfilePwdHist.puId.eq(user.getPuId()))
					.orderBy(qProfilePwdHist.pwdCreTime.desc())
					.limit(securityControl.pwdCycleCnt - 1)
					.fetch();
			
			for (String password : passwords) {
				if (newPassword.equals(password)) {
					throw new ErrorMessageException(ErrorCode.CheckError, MessageFormat.format("最近{0}次使用的密码不能重复", securityControl.pwdCycleCnt));
				}
			}
		}
	}

}
