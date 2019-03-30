package net.engining.profile.security.listener;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import net.engining.pg.parameter.ParameterFacility;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.entity.model.QProfileUser;
import net.engining.profile.param.SecurityControl;

/**
 * 根据密码输入错误次数锁定用户
 * 
 * @author zhangkun
 *
 */
public class PasswordTriesListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private ParameterFacility parameterFacility;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private SecurityControl defaultSecurityControl = createDefaultSecurityControl();
	
	private SecurityControl createDefaultSecurityControl()
	{
		SecurityControl ctl = new SecurityControl();
		return ctl;
	}

	@Override
	@Transactional
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

		String userId = (String)event.getAuthentication().getPrincipal();
		
		QProfileUser qProfileUser = QProfileUser.profileUser;
		ProfileUser user = new JPAQueryFactory(entityManager)
				.select(qProfileUser)
				.from(qProfileUser)
				.where(qProfileUser.userId.eq(userId))
				.fetchOne();
		
		if (user != null)
		{
			//计数
			user.setPwdTries(user.getPwdTries() + 1);
			
			//处理账户销定
			SecurityControl sc = parameterFacility.getUniqueParameter(SecurityControl.class).orElse(defaultSecurityControl);
			
			if (user.getPwdTries() >= sc.getPwdTries())
			{
				user.setStatus(StatusDef.L);
				logger.info("[{}] 密码错误次数过多，账号已锁定", user.getUserId());
			}
		}
	}
}
