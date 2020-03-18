package net.engining.profile.security.service;

import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.ProfileRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

/**
 * 扩展Spring Security的User对象
 * @author Eric Lu
 */
public class ProfileUserDetails extends User {

	private static final long serialVersionUID = 1L;

	private String puId;
	
	private Set<ProfileRole> roles;

	private String branchId;

	private StatusDef status;

	public ProfileUserDetails(String puId, String username, String password,
			boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities,
			String branchId, StatusDef status) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.puId = puId;
		this.branchId = branchId;
		this.status = status;
	}

	public String getPuId() {
		return puId;
	}

	public void setPuId(String puId) {
		this.puId = puId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public StatusDef getStatus() {
		return status;
	}

	public void setStatus(StatusDef status) {
		this.status = status;
	}

	public Set<ProfileRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<ProfileRole> roles) {
		this.roles = roles;
	}

}
