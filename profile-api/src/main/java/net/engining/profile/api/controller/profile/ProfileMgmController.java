package net.engining.profile.api.controller.profile;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.enums.OperationType;
import net.engining.profile.sdk.service.ProfileMgmService;
import net.engining.profile.sdk.service.ProfileRoleService;
import net.engining.profile.sdk.service.ProfileUserService;
import net.engining.profile.sdk.service.bean.profile.ProfileRoleBranch;
import net.engining.profile.sdk.service.bean.profile.ProfileRoleDelForm;
import net.engining.profile.sdk.service.bean.profile.ProfileRoleSaveUpdateForm;
import net.engining.profile.sdk.service.bean.profile.ProfileUserBranchForm;
import net.engining.profile.sdk.service.bean.profile.ProfileUserRoleForm;
import net.engining.profile.security.service.ProfileSecurityLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author liudongjing
 */
@RequestMapping("/profileMgm")
@RestController
public class ProfileMgmController {
    /**
     * 后台管理服务
     */
    @Autowired
    private ProfileMgmService profileMgmService;
    /**
     * 角色服务
     */
    @Autowired
    private ProfileRoleService profileRoleService;
    /**
     * 机构服务
     */
    @Autowired
    Provider4Organization provider4Organization;
    /**
     * 用户服务
     */
    @Autowired
    private ProfileUserService profileUserService;
    /**
     * 日志服务
     */
    @Autowired
    private ProfileSecurityLoggerService profileSecurityLoggerService;

    /**
     * 分页查询用户角色
     *
     * @return
     */
//    @PreAuthorize("hasAuthority('ProfileUser')")
    @RequestMapping(value = "/fetchProfileUser", method = RequestMethod.POST)
    @ApiOperation(value = "根据用户信息查询其角色", notes = "根据用户信息查询其角色")
    public @ResponseBody
    CommonWithHeaderResponse fetchProfileUser(
            @RequestBody @Validated ProfileUserBranchForm profileUserBranchForm) {

        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService.fetchProfileUser(
                profileUserBranchForm.getBranchId(), provider4Organization.getCurrentOrganizationId(),
                profileUserBranchForm.getName(), profileUserBranchForm.getRange());

        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
                .build()
                .setResponseData(fetchResponse);
    }

    /**
     * 获取所有的角色（分配角色显示）
     */
//    @PreAuthorize("hasAuthority('ProfileUser')")
    @RequestMapping(value = "/fetchAllProfileRole", method = RequestMethod.GET)
    @ApiOperation(value = " 获取所有的角色", notes = "获取所有的角色")
    public
    CommonWithHeaderResponse fetchAllProfileRole(@ApiParam(value = "应用代码", required = false)
                                                 @RequestParam(required = false)
                                                         String appCd) {
        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService.fetchAllProfileRole(appCd);

        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
                .build()
                .setResponseData(fetchResponse);
    }

    /**
     * 保存用户所对应的权限
     *
     * @return
     */
    @RequestMapping(value = "/saveProfileUserAndRole", method = RequestMethod.POST)
    @ApiOperation(value = "为用户分配角色", notes = "为用户分配角色")
    @PreAuthorize("hasAuthority('SaveProfileUserAndRole')")
    public @ResponseBody
    CommonWithHeaderResponse saveProfileUserAndRole(
            @RequestBody @Validated ProfileUserRoleForm profileUserRoleForm,
            HttpServletRequest request) {
        if (ValidateUtilExt.isNullOrEmpty(profileUserRoleForm.getRoleId())) {
            throw new ErrorMessageException(ErrorCode.Null, "角色id集合不能为空！");
        }
        Date date = new Date();
        profileMgmService.saveProfileUserAndRole(profileUserRoleForm.getPuId(), profileUserRoleForm.getRoleId());
        ProfileUser profileUserInfo = profileUserService.findProfileUserInfo(profileUserRoleForm.getPuId());
        //userNo是操作员的puid，beOper是被操作员的登录id
        profileSecurityLoggerService
                .logSecuOperation(profileUserInfo.getPuId(),
                        OperationType.GN,
                        WebCommonUtils.getIpAddress(request),
                        date,
                        profileUserInfo.getUserId());

        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .setStatusCode("0000");
    }

    /**
     * 获取用户对应的角色
     *
     * @return
     */
//    @PreAuthorize("hasAuthority('ProfileUser')")
    @RequestMapping(value = "/fetchUserRole", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户对应的角色", notes = "获取用户对应的角色")
    public @ResponseBody
    CommonWithHeaderResponse fetchUserRole(
            @RequestBody @Validated ProfileUserRoleForm profileUserRoleForm) {

        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService
                .fetchUserRoleByPuId(profileUserRoleForm.getPuId());

        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
                .build()
                .setResponseData(fetchResponse);
    }

    /**
     * 获取所有的分支下拉框
     */
    @RequestMapping(value = "/fetchAllProfileBranch", method = RequestMethod.POST)
    @ApiOperation(value = "获取所有的分支下拉框", notes = "获取所有的分支下拉框")
    public @ResponseBody
    CommonWithHeaderResponse fetchAllProfileBranch() {
        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService.fetchAllProfileBranch();

        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
                .build()
                .setResponseData(fetchResponse);
    }

    /**
     * 根据角色名称查询角色信息
     *
     * @return
     */
//    @PreAuthorize("hasAuthority('ProfileRole')")
    @RequestMapping(value = "/fetchProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有角色信息列表", notes = "查询所有角色信息列表")
    public @ResponseBody
    CommonWithHeaderResponse fetchProfileRole(
            @RequestBody @Validated ProfileRoleBranch profileRoleBranch) {
        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService
                .fetchProfileRole(profileRoleBranch.getRoleName(),
                        profileRoleBranch.getAppCd(),
                        profileRoleBranch.getRange());

        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
                .build()
                .setResponseData(fetchResponse);
    }

    /**
     * 角色新增
     *
     * @return
     */
    @PreAuthorize("hasAuthority('SaveProfileRole')")
    @RequestMapping(value = "/saveProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色新增", notes = "角色新增")
    public @ResponseBody
    CommonWithHeaderResponse saveProfileRole(
            @RequestBody @Validated ProfileRoleSaveUpdateForm profileRoleSaveForm) {
        profileMgmService.saveProfileRole(profileRoleSaveForm.getRoleId(), profileRoleSaveForm.getBranchId(),
                profileRoleSaveForm.getRoleName(), provider4Organization.getCurrentOrganizationId(),
                profileRoleSaveForm.getAppCd());

        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .setStatusCode("0000");
    }

    /**
     * 角色修改
     *
     * @return
     */
    @PreAuthorize("hasAuthority('UpdateProfileRole')")
    @RequestMapping(value = "/updateProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色修改", notes = "角色修改")
    public @ResponseBody
    CommonWithHeaderResponse updateProfileRole(
            @RequestBody @Validated ProfileRoleSaveUpdateForm profileRoleSaveUpdateForm) {

        profileMgmService.updateProfileRole(profileRoleSaveUpdateForm.getRoleId(),
                profileRoleSaveUpdateForm.getBranchId(),
                profileRoleSaveUpdateForm.getRoleName(),
                profileRoleSaveUpdateForm.getAppCd());

        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .setStatusCode("0000");
    }

    /**
     * 角色删除
     *
     * @return
     */
    @PreAuthorize("hasAuthority('DeleteProfileRole')")
    @RequestMapping(value = "/deleteProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色删除", notes = "角色删除")
    public @ResponseBody
    CommonWithHeaderResponse deleteProfileRole(
            @RequestBody @Validated ProfileRoleDelForm profileRoleDelForm) {
        profileRoleService.deleteProfileRoles(
                profileRoleDelForm.getRoleId()
        );

        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .setStatusCode("0000");
    }


}
