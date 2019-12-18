package net.engining.profile.security.listener;

import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.security.ProfileUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 登录成功重置密码错误次数
 *
 * @author zhangkun
 *
 */
public class ClearPasswordTriesListener implements ApplicationListener<AuthenticationSuccessEvent> {

	/** logger */
	private static final Logger log = LoggerFactory.getLogger(ClearPasswordTriesListener.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void onApplicationEvent(AuthenticationSuccessEvent event) {

		//为了兼容oauth2认证，去除原先【instanceof UserDetails】的强校验

		if (event.getAuthentication().getPrincipal() instanceof ProfileUserDetails) {
			ProfileUserDetails ud = (ProfileUserDetails)event.getAuthentication().getPrincipal();
			ProfileUser user = entityManager.find(ProfileUser.class, ud.getPuId());
			checkArgument(user != null);
			user.setPwdTries(0);
		}
		else {
			log.debug("Not ProfileUserDetails, do nothing");
		}

	}

}
