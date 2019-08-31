package net.engining.profile.security.listener;

import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.security.ProfileUserDetails;
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
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		checkArgument(event.getAuthentication().getPrincipal() instanceof UserDetails);
		ProfileUserDetails ud = (ProfileUserDetails)event.getAuthentication().getPrincipal();
		ProfileUser user = entityManager.find(ProfileUser.class, ud.getPuId());
		checkArgument(user != null);
		user.setPwdTries(0);
	}

}
