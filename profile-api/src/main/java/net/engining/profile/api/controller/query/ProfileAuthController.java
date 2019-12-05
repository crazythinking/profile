package net.engining.profile.api.controller.query;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.sdk.service.bean.param.MenuQueryRequest;
import net.engining.profile.sdk.service.bean.profile.FetchRoleAuthResponse;
import net.engining.profile.sdk.service.bean.profile.ProfileRoleDelDisForm;
import net.engining.profile.sdk.service.query.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单
 *
 * @author heqingxi
 */

@Api(value = "MenuController")
@RequestMapping("/menu")
@RestController
public class ProfileAuthController {

    @Autowired
    private AuthService authService;

    //    @PreAuthorize("hasAuthority('menuQurey')")
    @RequestMapping(value = "/menuQuery", method = RequestMethod.GET)
    @ApiOperation(value = "主菜单信息查询", notes = "")
    public CommonWithHeaderResponse
    getMenu(MenuQueryRequest request) {
        String menuList = authService.getMenuData(request.getUserId(),request.getAppCd());
        return new CommonWithHeaderResponseBuilder<Void, String>()
                .build()
                .setResponseData(menuList);
    }

    //    @PreAuthorize("hasAuthority('menuQurey')")
    @RequestMapping(value = "/menuAuthorityQuery", method = RequestMethod.GET)
    @ApiOperation(value = "权限菜单信息查询", notes = "")
    public CommonWithHeaderResponse
    getAuthorityMenu(@ApiParam("应用代码") @RequestParam(required = false) String appCd) {
        //所有角色
        String menuList = authService.getAuthorityData(appCd);
        return new CommonWithHeaderResponseBuilder<Void, String>()
                .build()
                .setResponseData(menuList);
    }

    /**
     * 角色权限分配
     *
     * @return
     */
    @PreAuthorize("hasAuthority('DistributionProfileRole')")
    @RequestMapping(value = "/distributionProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色权限分配", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse distributionProfileRole(
            @RequestBody ProfileRoleDelDisForm profileRoleDelDisForm) {

        authService.distributionProfileRole(profileRoleDelDisForm.getRoleId(),
                profileRoleDelDisForm.getAuthList());

        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .setStatusCode("0000");
    }

    /**
     * 获取角色对应的权限
     *
     * @return
     */
//    @PreAuthorize("hasAuthority('ProfileRole')")
    @RequestMapping(value = "/fetchRoleAuth", method = RequestMethod.GET)
    @ApiOperation(value = "获取角色对应的权限", notes = "")
    public  CommonWithHeaderResponse fetchRoleAuth(
            @ApiParam("角色id") @RequestParam String roleId) {
        List<String> authList = authService
                .fetchRoleAuthByRoleId(roleId);
        FetchRoleAuthResponse fetchResponse = new FetchRoleAuthResponse();
        fetchResponse.setAuthList(authList);
        return new CommonWithHeaderResponseBuilder<Void, FetchRoleAuthResponse>()
                .build()
                .setResponseData(fetchResponse);
    }
}
