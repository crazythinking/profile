package net.engining.profile.sdk.service.profile;


import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.profile.entity.model.ProfileBranch;
import net.engining.profile.entity.model.ProfileBranchKey;
import net.engining.profile.sdk.service.ProfileBranchService;
import net.engining.profile.sdk.service.bean.profile.ProfileBranchForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.MessageFormat;

/**
 * @author tuyi
 */
@Service
public class BranchService {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	ProfileBranchService profileBranchService;
	
	public ProfileBranch profileBranch(ProfileBranchForm branch){
		ProfileBranchKey key = new ProfileBranchKey();
		key.setBranchId(branch.getBranchId());
		key.setOrgId(branch.getOrgId());
		ProfileBranch ranch = em.find(ProfileBranch.class, key);
		if(null==ranch){
			throw new ErrorMessageException(ErrorCode.CheckError, MessageFormat.format("不存在branchId:{0}的机构", branch.getBranchId()));
		}
		
		ProfileBranch profileBranch= new ProfileBranch();
		BeanUtils.copyProperties(branch, profileBranch);
		profileBranch.fillDefaultValues();
		profileBranchService.updateBranch(profileBranch);
		ranch.setSuperiorId(branch.getSuperiorId());
		return ranch;
	}
	
	public FetchResponse<ProfileBranchForm> profileBranchForm(FetchResponse<ProfileBranch> fetchBranch){
		FetchResponse<ProfileBranchForm> profileBranchForm = new FetchResponse<ProfileBranchForm>();
		BeanUtils.copyProperties(fetchBranch, profileBranchForm);
		return profileBranchForm;
	}
	
	public ProfileBranchForm profileBranchForm(ProfileBranch branch){
		ProfileBranchForm profileBranchForm = new ProfileBranchForm();
		BeanUtils.copyProperties(branch, profileBranchForm);
		return profileBranchForm;
	}
}
