package net.engining.profile.security;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.querydsl.jpa.impl.JPAQueryFactory;

import net.engining.pg.parameter.ParameterFacility;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.entity.model.QProfileRole;
import net.engining.profile.entity.model.QProfileRoleAuth;
import net.engining.profile.entity.model.QProfileUser;
import net.engining.profile.entity.model.QProfileUserRole;
import net.engining.profile.param.SecurityControl;

/**
 * 基于当前Profile项目的数据结构对 {@link UserDetailsService}的一个实现
 * @author binarier
 *
 */
public class ProfileUserDetailsService implements UserDetailsService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ParameterFacility facility;

	/**
	 * @param username 用户登陆Id
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		QProfileUser qProfileUser = QProfileUser.profileUser;
		ProfileUser profileUser = new JPAQueryFactory(em)
				.select(qProfileUser)
				.from(qProfileUser)
				.where(qProfileUser.userId.eq(username))
				.fetchOne();
		
		if (profileUser == null)
			throw new UsernameNotFoundException(String.format("无法找到用户,%s", username));
		
		QProfileUserRole qUserRole = QProfileUserRole.profileUserRole;
		QProfileRoleAuth qRoleAuth = QProfileRoleAuth.profileRoleAuth;
		
		List<String> authorities = new JPAQueryFactory(em)
				.select(qRoleAuth.authority)
				.from(qUserRole, qRoleAuth)
			.where(qUserRole.roleId.eq(qRoleAuth.roleId), qUserRole.puId.eq(profileUser.getPuId()))
			.distinct()
			.fetch();
		
		//TODO 这里可能有问题，只存了权限标识
		Collection<GrantedAuthority> grantedAuthorities = Collections2.transform(authorities, new Function<String, GrantedAuthority>() {

			@Override
			@Nullable
			public GrantedAuthority apply(@Nullable String input) {
				return new SimpleGrantedAuthority(input);
			}
		});
		
		//检查安全控制项内，如果要求首次登录必须修改密码，则不给操作权限
		Optional<SecurityControl> control = facility.getUniqueParameter(SecurityControl.class);
		if(control.isPresent() && control.get().getPwdFirstLoginChgInd() && profileUser.getStatus().equals(StatusDef.N))	//新用户只能先改密码
			grantedAuthorities.clear();
		
		//这里将用户的权限存入了{@link UserDetails}
		StatusDef status = profileUser.getStatus();
		ProfileUserDetails pud = new ProfileUserDetails(
				profileUser.getPuId(),
				profileUser.getUserId(),
				profileUser.getPassword(),
				status != StatusDef.L,
				true,
				true,
				status != StatusDef.L,
				grantedAuthorities,
				profileUser.getBranchId(),
				status);
		
		//将用户角色也存入{@link UserDetails}
		QProfileRole qRole = QProfileRole.profileRole;
		pud.setRoles(Sets.newHashSet(
				new JPAQueryFactory(em)
					.select(qRole)
					.from(qUserRole, qRole)
					.where(qRole.roleId.eq(qUserRole.roleId), qUserRole.puId.eq(profileUser.getPuId()))
					.fetch()
					)
				);
		return pud;
	}

}
