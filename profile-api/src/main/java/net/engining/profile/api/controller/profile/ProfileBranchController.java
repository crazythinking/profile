package net.engining.profile.api.controller.profile;


import io.swagger.annotations.ApiOperation;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.Range;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileBranch;
import net.engining.profile.sdk.service.ProfileBranchService;
import net.engining.profile.sdk.service.bean.profile.BranchFilter;
import net.engining.profile.sdk.service.bean.profile.ProfileBranchForm;
import net.engining.profile.sdk.service.profile.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author tuyi
 */
@RequestMapping("/profile")
@RestController
public class ProfileBranchController {
	
	@Autowired
	ProfileBranchService profileBranchService;
	
	@Autowired
	Provider4Organization provider4Organization;
	
	@Autowired
	BranchService branchService;
	
	@RequestMapping(value="/branchesBySuperid",method= RequestMethod.POST)
	@ApiOperation(value="fetchBranchesBySuperid", notes="")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse fetchBranchesBySuperid(@RequestBody BranchFilter branchFilter) {
		FetchResponse<ProfileBranch> fetchBranch = profileBranchService.fetchBranch(branchFilter.getRange(), branchFilter.getSuperiorId(), provider4Organization.getCurrentOrganizationId());
		FetchResponse<ProfileBranchForm> profileBranchForm = branchService.profileBranchForm(fetchBranch);
		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<ProfileBranchForm>>().build()
				.setResponseData(profileBranchForm);
	}
	
	@RequestMapping(value="/branches",method= RequestMethod.POST)
	@ApiOperation(value="fetchBranches", notes="")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse fetchBranches(@RequestBody Range range) {
		FetchResponse<ProfileBranch> fetchBranch = profileBranchService.fetchBranch(range);
		FetchResponse<ProfileBranchForm> profileBranchForm = branchService.profileBranchForm(fetchBranch);
		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<ProfileBranchForm>>().build()
				.setResponseData(profileBranchForm);
	}
	
	@RequestMapping(value="/branch/{branchId}",method= RequestMethod.GET)
	@ApiOperation(value="getBranch", notes="")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse getBranch(@PathVariable String branchId) {
		ProfileBranch branch = profileBranchService.getBranch(provider4Organization.getCurrentOrganizationId(), branchId);
		ProfileBranchForm profileBranchForm = branchService.profileBranchForm(branch);
		return new CommonWithHeaderResponseBuilder<Void,ProfileBranchForm>().build()
				.setResponseData(profileBranchForm);
	}
	
	@RequestMapping(value="/updateBranch",method= RequestMethod.POST)
	@ApiOperation(value="updateBranch", notes="")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse updateBranch(@RequestBody ProfileBranchForm branch) {
		ProfileBranch profileBranch = branchService.profileBranch(branch);
		return new CommonWithHeaderResponseBuilder<Void,Void>()
				.build();
	}
	
	@RequestMapping(value="/getBranchNames",method= RequestMethod.GET)
	@ApiOperation(value="getBranchNames", notes="")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse getBranchNames() {
		Map<String, String> fetchBranchNamesByOrg = profileBranchService.fetchBranchNamesByOrg(provider4Organization.getCurrentOrganizationId());
		return new CommonWithHeaderResponseBuilder<Void,Map<String, String>>().build()
				.setResponseData(fetchBranchNamesByOrg);
	}
	
	@RequestMapping(value="/removeBranches",method= RequestMethod.POST)
	@ApiOperation(value="removeBranches", notes="")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse removeBranches(@RequestBody String[] branchIds) {
			List<String> branches= Arrays.asList(branchIds);
			profileBranchService.deleteProfileBranch(branches, provider4Organization.getCurrentOrganizationId());
			return new CommonWithHeaderResponseBuilder<Void,Void>()
					.build();
	}
	
}