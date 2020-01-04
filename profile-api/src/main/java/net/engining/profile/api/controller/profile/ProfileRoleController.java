package net.engining.profile.api.controller.profile;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileRole;
import net.engining.profile.sdk.service.ProfileRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author yangxing
 */
@RequestMapping("/profile")
@RestController
public class ProfileRoleController {

	@Autowired
	private ProfileRoleService profileRoleService;


	@PreAuthorize("hasAuthority('ProfileRole')")
	@ApiOperation(value="通过角色id查询角色信息", notes="")
	@RequestMapping(value="/getRole/{roleId}",method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse getRole(@PathVariable String roleId) {
		ProfileRole fetchResponse = profileRoleService.getProfileRoleInfo(roleId);

		return new CommonWithHeaderResponseBuilder<Void,ProfileRole>()
				.build()
				.setResponseData(fetchResponse);
	}


	@PreAuthorize("hasAuthority('ProfileRole')")
	@ApiOperation(value="删除角色下的用户", notes="")
	@RequestMapping(value="/delRoleUsers",method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse deleteRole2User(@RequestBody
											 @ApiParam(value = "key:角色id、value:用户id", required = true)
													 Map<String, String> roleUsers){
		profileRoleService.deleteProfileUserRoles(roleUsers);

		return new CommonWithHeaderResponseBuilder<Void,Void>()
				.build();

	}

}