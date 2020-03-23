package net.engining.profile.api.controller.profile;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.Range;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileBranch;
import net.engining.profile.sdk.service.ProfileBranchService;
import net.engining.profile.sdk.service.bean.profile.BranchFilter;
import net.engining.profile.sdk.service.bean.profile.ProfileBranchForm;
import net.engining.profile.sdk.service.profile.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author tuyi
 */
@Api(value = "ProfileBranchController",description = "机构服务模块")
@RequestMapping("/profile")
@RestController
public class ProfileBranchController {
	/**
	 * 机构服务
	 */
	@Autowired
	ProfileBranchService profileBranchService;
	/**
	 * 机构服务
	 */
	@Autowired
	Provider4Organization provider4Organization;
	/**
	 * 机构服务
	 */
	@Autowired
	BranchService branchService;

	/**
	 * 通过上级分支id查询机构分支信息
	 * @param branchFilter
	 * @return
	 */
	@RequestMapping(value="/branchesBySuperid",method= RequestMethod.POST)
	@ApiOperation(value="通过上级分支id查询机构分支信息", notes="通过上级分支id查询机构分支信息")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse fetchBranchesBySuperid(@RequestBody @Validated BranchFilter branchFilter) {
		FetchResponse<ProfileBranch> fetchBranch = profileBranchService.fetchBranch(
				branchFilter.getRange(),
				branchFilter.getSuperiorId(),
				provider4Organization.getCurrentOrganizationId()
		);
		FetchResponse<ProfileBranchForm> profileBranchForm = branchService.profileBranchForm(fetchBranch);
		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<ProfileBranchForm>>().build()
				.setResponseData(profileBranchForm);
	}

	@RequestMapping(value="/branches",method= RequestMethod.POST)
	@ApiOperation(value="查询机构分支信息列表", notes="查询机构分支信息列表")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse fetchBranches(@RequestBody Range range) {
		FetchResponse<ProfileBranch> fetchBranch = profileBranchService.fetchBranch(range);
		FetchResponse<ProfileBranchForm> profileBranchForm = branchService.profileBranchForm(fetchBranch);
		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<ProfileBranchForm>>().build()
				.setResponseData(profileBranchForm);
	}

	@RequestMapping(value="/branch/{branchId}",method= RequestMethod.GET)
	@ApiOperation(value="通过分支id查询机构分支信息", notes="通过分支id查询机构分支信息")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public
	CommonWithHeaderResponse getBranch(@PathVariable String branchId) {
		ProfileBranch branch = profileBranchService.getBranch(
				provider4Organization.getCurrentOrganizationId(),
				branchId
		);
		if(ValidateUtilExt.isNullOrEmpty(branch)){
			return null;
		}
		ProfileBranchForm profileBranchForm = branchService.profileBranchForm(branch);
		return new CommonWithHeaderResponseBuilder<Void,ProfileBranchForm>().build()
				.setResponseData(profileBranchForm);
	}

	@RequestMapping(value="/updateBranch",method= RequestMethod.POST)
	@ApiOperation(value="更新机构分支信息", notes="更新机构分支信息")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse updateBranch(@RequestBody ProfileBranchForm branch) {
		ProfileBranch profileBranch = branchService.profileBranch(branch);
		return new CommonWithHeaderResponseBuilder<Void,Void>()
				.build();
	}

	@RequestMapping(value="/getBranchNames",method= RequestMethod.GET)
	@ApiOperation(value="获得当前机构id下分支名", notes="获得当前机构id下分支名")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public
	CommonWithHeaderResponse getBranchNames() {
		Map<String, String> fetchBranchNamesByOrg = profileBranchService.fetchBranchNamesByOrg(
				provider4Organization.getCurrentOrganizationId()
		);
		return new CommonWithHeaderResponseBuilder<Void,Map<String, String>>().build()
				.setResponseData(fetchBranchNamesByOrg);
	}

	@RequestMapping(value="/removeBranches",method= RequestMethod.POST)
	@ApiOperation(value="删除机构分支信息", notes="删除机构分支信息")
//	@PreAuthorize("hasAuthority('ProfileBranch')")
	public @ResponseBody
	CommonWithHeaderResponse removeBranches(@RequestBody
											@ApiParam(value = "分支id", required = true)
													String[] branchIds) {
		List<String> branches= Arrays.asList(branchIds);
		profileBranchService.deleteProfileBranch(branches, provider4Organization.getCurrentOrganizationId());
		return new CommonWithHeaderResponseBuilder<Void,Void>()
				.build();
	}

}