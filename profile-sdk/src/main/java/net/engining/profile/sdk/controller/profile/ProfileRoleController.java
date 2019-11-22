package net.engining.profile.sdk.controller.profile;


import io.swagger.annotations.ApiOperation;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.Range;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileRole;
import net.engining.profile.sdk.service.ProfileRoleService;
import net.engining.profile.sdk.service.bean.profile.RoleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author yangxing
 */
@RequestMapping("/profile")
@Controller
public class ProfileRoleController {

	@Autowired
	private ProfileRoleService profileRoleService;

	@PreAuthorize("hasAuthority('ProfileRole')")
	@ApiOperation(value="获取所有角色", notes="")
	@RequestMapping(value="/fetchRoles",method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse fetchRoles(@RequestBody Range range) {
		FetchResponse<ProfileRole> fetchResponse  = profileRoleService.fetchRoles(range);

		return new CommonWithHeaderResponseBuilder<Void, FetchResponse<ProfileRole>>()
				.build()
				.setResponseData(fetchResponse);
	}

	@PreAuthorize("hasAuthority('ProfileRole')")
	@ApiOperation(value="添加角色", notes="")
	@RequestMapping(value="/addRole",method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse addRole(@RequestBody ProfileRole role) {
		profileRoleService.addProfileRole(role);

		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
	}

	@PreAuthorize("hasAuthority('ProfileRole')")
	@ApiOperation(value="removeRoles", notes="")
	@RequestMapping(value="/removeRoles", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse removeRole(@RequestBody String[] roles) {
		List<String> lroles= Arrays.asList(roles);
		profileRoleService.deleteProfileRoles(lroles);

		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
	}

	@PreAuthorize("hasAuthority('ProfileRole')")
	@ApiOperation(value="updateRole", notes="")
	@RequestMapping(value="/updateRole", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse updateRole(@RequestBody RoleForm roleForm) {
		List<String> lauth= Arrays.asList(roleForm.getAuthorities());
		profileRoleService.updateProfileRole(roleForm.getProfileRole(), lauth);

		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
	}

	@PreAuthorize("hasAuthority('ProfileRole')")
	@ApiOperation(value="getRole", notes="")
	@RequestMapping(value="/getRole/{roleId}",method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse getRole(@PathVariable String roleId) {
		ProfileRole fetchResponse = profileRoleService.getProfileRoleInfo(roleId);

		return new CommonWithHeaderResponseBuilder<Void,ProfileRole>()
				.build()
				.setResponseData(fetchResponse);
	}

	@PreAuthorize("hasAuthority('ProfileRole')")
	@ApiOperation(value="getAuths4Role", notes="")
	@RequestMapping(value="/getAuths4Role/{roleId}",method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse getAuthorities4Role(@PathVariable String roleId){
		List<String> fetchResponse =  profileRoleService.getProfileRoleAuths(roleId);

		return new CommonWithHeaderResponseBuilder<Void,List<String>>()
				.build()
				.setResponseData(fetchResponse);

	}

	@PreAuthorize("hasAuthority('ProfileRole')")
	@ApiOperation(value="delRoleUsers", notes="")
	@RequestMapping(value="/delRoleUsers",method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse deleteRole2User(@RequestBody Map<String, String> roleUsers){
		profileRoleService.deleteProfileUserRoles(roleUsers);

		return new CommonWithHeaderResponseBuilder<Void,Void>()
				.build();

	}

}