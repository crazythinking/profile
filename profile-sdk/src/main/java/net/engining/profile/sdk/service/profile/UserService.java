package net.engining.profile.sdk.service.profile;


import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.sdk.service.ProfileUserService;
import net.engining.profile.sdk.service.bean.profile.MgmWebUser;
import net.engining.profile.sdk.service.bean.profile.ProfileUserUpdateForm;
import net.engining.profile.security.ProfileSecurityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tuyi
 */
@Service
public class UserService {

	@Autowired
	private Provider4Organization provider4Organization;
	
	@Autowired
	private ProfileSecurityService profileSecurityService;
	
	@Autowired
	private ProfileUserService profileUserService;
	public MgmWebUser mgmWebUser(ProfileUser profileUser){
		MgmWebUser mgmWebUser = new MgmWebUser();
		BeanUtils.copyProperties(profileUser, mgmWebUser, "password");
		return mgmWebUser;
	}
	
	
	public ProfileUser profileUserForm(MgmWebUser user){
		ProfileUser profileUser = new ProfileUser();
		BeanUtils.copyProperties(user, profileUser);
		profileUser.setPuId(null);
		//单机构默认赋值
		profileUser.setOrgId(provider4Organization.getCurrentOrganizationId());
		profileUser.setBranchId(provider4Organization.getCurrentOrganizationId());
		profileSecurityService.createNewUser(profileUser, user.getPassword());
		return profileUser;
	}
	
	public ProfileUser profileUser(ProfileUserUpdateForm user){
		ProfileUser profileUser = profileUserService.updateProfileUser(user);
		return profileUser;
	}
	
	public void validateUser(String userId){
		ProfileUser userInfo = profileUserService.findProfileUserInfoByUserId(userId);
		if(null!=userInfo){
			throw new ErrorMessageException(ErrorCode.CheckError, "该用户已被添加，不能再次添加");
		}
	}



}