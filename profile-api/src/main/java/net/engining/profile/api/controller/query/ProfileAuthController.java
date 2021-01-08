//package net.engining.profile.api.controller.query;
//
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import net.engining.pg.web.CommonWithHeaderResponseBuilder;
//import net.engining.pg.web.bean.CommonWithHeaderResponse;
//import net.engining.profile.sdk.service.bean.profile.FetchRoleAuthResponse;
//import net.engining.profile.sdk.service.bean.profile.ProfileRoleDelDisForm;
//import net.engining.profile.sdk.service.query.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 菜单权限
// *
// * @author heqingxi
// */
//
//@Api(value = "ProfileAuthController",description = "菜单权限模块")
//@RequestMapping("/menu")
//@RestController
//public class ProfileAuthController {
//    /**
//     * 菜单权限服务
//     */
//    @Autowired
//    private AuthService authService;
//
//    //    @PreAuthorize("hasAuthority('menuQurey')")
//    @RequestMapping(value = "/menuAuthorityQuery", method = RequestMethod.GET)
//    @ApiOperation(value = "权限菜单信息查询", notes = "权限菜单信息查询")
//    public CommonWithHeaderResponse
//    getAuthorityMenu(@ApiParam(value = "应用代码", required = true)
//                     @RequestParam(required = false) String appCd) {
////        //所有角色
////        String menuList = authService.getAuthorityData(appCd);
////        return new CommonWithHeaderResponseBuilder<Void, String>()
////                .build()
////                .setResponseData(menuList);
//        return null;
//    }
//
//    /**
//     * 角色权限分配
//     *
//     * @return
//     */
//    @PreAuthorize("hasAuthority('DistributionProfileRole')")
//    @RequestMapping(value = "/distributionProfileRole", method = RequestMethod.POST)
//    @ApiOperation(value = "角色权限分配", notes = "角色权限分配")
//    public @ResponseBody
//    CommonWithHeaderResponse distributionProfileRole(
//            @RequestBody @Validated ProfileRoleDelDisForm profileRoleDelDisForm) {
//
//        authService.distributionProfileRole(profileRoleDelDisForm.getRoleId(),
//                profileRoleDelDisForm.getAuthList());
//
//        return new CommonWithHeaderResponseBuilder<Void, Void>()
//                .build()
//                .setStatusCode("0000");
//    }
//
//    /**
//     * 获取角色对应的权限
//     *
//     * @return
//     */
////    @PreAuthorize("hasAuthority('ProfileRole')")
//    @RequestMapping(value = "/fetchRoleAuth", method = RequestMethod.GET)
//    @ApiOperation(value = "获取角色对应的权限", notes = "获取角色对应的权限")
//    public  CommonWithHeaderResponse fetchRoleAuth(
//            @ApiParam(value = "角色id", required = true) @RequestParam String roleId) {
//        List<String> authList = authService
//                .fetchRoleAuthByRoleId(roleId);
//        FetchRoleAuthResponse fetchResponse = new FetchRoleAuthResponse();
//        fetchResponse.setAuthList(authList);
//        return new CommonWithHeaderResponseBuilder<Void, FetchRoleAuthResponse>()
//                .build()
//                .setResponseData(fetchResponse);
//    }
//}
