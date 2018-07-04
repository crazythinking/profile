package net.engining.profile.sdk.service;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import net.engining.pg.parameter.ParameterFacility;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.security.ProfileUserDetails;

@Service
public class ProfileRuntimeService{
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ParameterFacility facility;

	public ClientUser loadCurrentUser()
	{
		UserDetails ud = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ClientUser cu = new ClientUser();
		cu.setAuthorities(Sets.newHashSet(
			//转换成Set<String>
			Collections2.transform(ud.getAuthorities(), new Function<GrantedAuthority, String>()
			{
				@Override
				public String apply(GrantedAuthority input)
				{
					return input.getAuthority();
				}
			})));
		
		cu.setId(ud.getUsername());
		cu.setName(ud.getUsername());
		cu.setProps(new HashMap<String, Object>());
		
		Optional<SecurityControl> control = facility.getUniqueParameter(SecurityControl.class);
		cu.setNeedPasswordChange(
				control.isPresent() &&									//存在控制参数
				control.get().pwdFirstLoginChgInd &&					//“首次登录改密码”
				ud instanceof ProfileUserDetails &&						//避免万一不是用这个用户体系
				((ProfileUserDetails)ud).getStatus() == StatusDef.N);	//新用户
		
		return cu;
	}
}
