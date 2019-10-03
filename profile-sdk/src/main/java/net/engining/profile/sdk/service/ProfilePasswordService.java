package net.engining.profile.sdk.service;

import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.param.PasswordPattern;
import net.engining.profile.param.SecurityControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Eric Lu
 */
@Service
public class ProfilePasswordService {
    private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ParameterFacility parameterFacility;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public final String resetPwdStr = "";

	private static final String HINT_PREFIX = "密码必须";

	private int complexity = 0;
	
	/**
	 * 修改登陆密码
	 * @param puId 用户登陆表主键
	 * @param form {@link net.engining.profile.dict.view.ChangePasswordForm}
	 * @param operUser 操作人员系统登陆Id，可以为Null，为Null时，自动填入"System"; 为"Owner"时，表示用户自己;
	 * @throws ErrorMessageException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void changePassword(String puId, String oldPassword, String newPassword, String operUser) throws ErrorMessageException {
		
		SecurityControl control = parameterFacility.getUniqueParameter(SecurityControl.class).get();

        if (ValidateUtilExt.isNotNullOrEmpty(control) && control.complexPwdInd != null && control.complexPwdInd) {
            // 根据参数决定是否校验复杂度
            boolean isWellFormed = true;
            StringBuilder hint = new StringBuilder(HINT_PREFIX);
            boolean isfirstMismatch = true;
            for (PasswordPattern pattern : control.passwordPatterns) {
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

		ProfileUser user = em.find(ProfileUser.class, puId);
		checkNotNull(user);
		
		if (!passwordEncoder.matches(oldPassword, user.getPassword()))
		{
			throw new ErrorMessageException(ErrorCode.CheckError,  "原密码不正确");
		}
		
		user.setMtnTimestamp(new Date());
		user.setMtnUser(operUser);
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setPwdTries(0);
		user.setStatus(StatusDef.A);
		
		logger.info("操作员{}，修改了用户{}的密码！", operUser, user.getUserId());
	}
	
	/**
	 * 重置登陆密码
	 * @param puId
	 * @param operUser
	 */
	@Transactional(rollbackFor = Exception.class)
	public void resetPassword(String puId, String operUser){
		ProfileUser user = em.find(ProfileUser.class, puId);
		checkNotNull(user);
		
		user.setMtnTimestamp(new Date());
		user.setMtnUser(operUser);
		user.setPassword(passwordEncoder.encode(resetPwdStr));
		user.setPwdTries(0);
		user.setStatus(StatusDef.L);
		
		logger.info("操作员：{}，重置了用户：{}的密码！", operUser, user.getUserId());
	}

}
