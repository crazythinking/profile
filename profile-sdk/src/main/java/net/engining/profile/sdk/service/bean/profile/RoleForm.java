package net.engining.profile.sdk.service.bean.profile;

import net.engining.profile.entity.model.ProfileRole;

/**
 * @author Eric Lu
 *
 */
public class RoleForm {

	private ProfileRole profileRole;
	private String[] authorities;
	
	public ProfileRole getProfileRole() {
		return profileRole;
	}
	public void setProfileRole(ProfileRole profileRole) {
		this.profileRole = profileRole;
	}
	public String[] getAuthorities() {
		return authorities;
	}
	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}
	
	
	
}
