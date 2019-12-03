package net.engining.profile.sdk.controller.profile;


import io.swagger.annotations.ApiOperation;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileRole;
import net.engining.profile.sdk.service.ProfileRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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