package net.engining.profile.sdk.controller.profile;


import io.swagger.annotations.ApiOperation;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileSecoperLog;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.enums.OperationType;
import net.engining.profile.sdk.service.ProfileMgmService;
import net.engining.profile.sdk.service.ProfileRoleService;
import net.engining.profile.sdk.service.ProfileUserService;
import net.engining.profile.sdk.service.bean.profile.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liudongjing
 */
@RequestMapping("/profileMgm")
@Controller
public class ProfileMgmController {

    @Autowired
    private ProfileMgmService profileMgmService;

    @Autowired
    private ProfileRoleService profileRoleService;

    @Autowired
    Provider4Organization provider4Organization;

    @Autowired
    private ProfileUserService profileUserService;

    @PersistenceContext
    private EntityManager em;

    /**
     * 分页查询用户角色
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ProfileUser')")
    @RequestMapping(value = "/fetchProfileUser", method = RequestMethod.POST)
    @ApiOperation(value = "根据用户信息查询其角色", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse fetchProfileUser(
            @RequestBody ProfileUserBranchForm profileUserBranchForm) {

        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService.fetchProfileUser(
                profileUserBranchForm.getBranchId(), provider4Organization.getCurrentOrganizationId(),
                profileUserBranchForm.getName(), profileUserBranchForm.getRange());

        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
                .build()
                .setResponseData(fetchResponse);
    }

    /**
     * 获取所有的角色
     */
//    @PreAuthorize("hasAuthority('ProfileUser')")
    @RequestMapping(value = "/fetchAllProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = " 获取所有的角色", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse fetchAllProfileRole() {
        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService.fetchAllProfileRole();

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
    @ApiOperation(value = "为用户分配角色", notes = "")
//    @PreAuthorize("hasAuthority('mainSave')")
    public @ResponseBody
    CommonWithHeaderResponse saveProfileUserAndRole(
            @RequestBody ProfileUserRoleForm profileUserRoleForm, HttpServletRequest request) {
        Date date = new Date();
        profileMgmService.saveProfileUserAndRole(profileUserRoleForm.getPuId(), profileUserRoleForm.getRoleId());
        ProfileUser profileUserInfo = profileUserService.findProfileUserInfo(profileUserRoleForm.getPuId());
        String userId=profileUserInfo.getUserId();
        Object puid =getPuid(profileUserRoleForm.getUserId());
        logSecuOperation(puid.toString(), OperationType.GN, WebCommonUtils.getIpAddress(request),date,userId);

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
    @ApiOperation(value = "获取用户对应的角色", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse fetchUserRole(
            @RequestBody ProfileUserRoleForm profileUserRoleForm) {

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
    @ApiOperation(value = "获取所有的分支下拉框", notes = "")
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
    @ApiOperation(value = "根据角色名称查询角色信息", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse fetchProfileRole(
            @RequestBody ProfileRoleBranch profileRoleBranch) {
        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService
                .fetchProfileRole(profileRoleBranch.getRoleName(), profileRoleBranch.getRange());

        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
                .build()
                .setResponseData(fetchResponse);
    }

    /**
     * 角色新增
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ProfileRole')")
    @RequestMapping(value = "/saveProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色新增", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse saveProfileRole(
            @RequestBody ProfileRoleSaveUpdateForm profileRoleSaveForm) {
        profileMgmService.saveProfileRole(profileRoleSaveForm.getRoleId(), profileRoleSaveForm.getBranchId(),
                profileRoleSaveForm.getRoleName(), provider4Organization.getCurrentOrganizationId());

        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .setStatusCode("0000");
    }

    /**
     * 角色修改
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ProfileRole')")
    @RequestMapping(value = "/updateProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色修改", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse updateProfileRole(
            @RequestBody ProfileRoleSaveUpdateForm profileRoleSaveUpdateForm) {

        profileMgmService.updateProfileRole(profileRoleSaveUpdateForm.getRoleId(),
                profileRoleSaveUpdateForm.getBranchId(), profileRoleSaveUpdateForm.getRoleName());

        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .setStatusCode("0000");
    }

    /**
     * 角色删除
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ProfileRole')")
    @RequestMapping(value = "/deleteProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色删除", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse deleteProfileRole(
            @RequestBody ProfileRoleDelDisForm profileRoleDelDisForm) {
        List<String> list = new ArrayList<String>();
        list.add(profileRoleDelDisForm.getRoleId());
        profileRoleService.deleteProfileRoles(list);

        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .setStatusCode("0000");
    }

    /**
     * 角色权限分配
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ProfileRole')")
    @RequestMapping(value = "/distributionProfileRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色权限分配", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse distributionProfileRole(
            @RequestBody ProfileRoleDelDisForm profileRoleDelDisForm) {
        profileMgmService.distributionProfileRole(profileRoleDelDisForm.getRoleId(),
                profileRoleDelDisForm.getAuthStr());

        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .setStatusCode("0000");
    }

    /**
     * 获取角色对应的权限
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ProfileRole')")
    @RequestMapping(value = "/fetchRoleAuth", method = RequestMethod.POST)
    @ApiOperation(value = "获取角色对应的权限", notes = "")
    public @ResponseBody
    CommonWithHeaderResponse fetchRoleAuth(
            @RequestBody ProfileRoleDelDisForm profileRoleDelDisForm) {
        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService
                .fetchRoleAuthByRoleId(profileRoleDelDisForm.getRoleId());
        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
                .build()
                .setResponseData(fetchResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    public void logSecuOperation(String userNo, OperationType operationType, String opIp, Date opTime, String beOper){
        ProfileSecoperLog ssCustSecoperLog = new ProfileSecoperLog();
        ssCustSecoperLog.setPuId(userNo);
        ssCustSecoperLog.setOperType(operationType);
        ssCustSecoperLog.setOperIp(opIp);
        ssCustSecoperLog.setOperTime(opTime);
        ssCustSecoperLog.setBeoperatedId(beOper);
        em.persist(ssCustSecoperLog);
    }

    public Object getPuid(String userId){
        FetchResponse<Map<String, Object>> rsp = profileUserService.getUserInfoByUserId(userId);
        List<Map<String, Object>> useData = rsp.getData();
        Map<String, Object> usetDataMap = useData.get(0);
        Object puid = usetDataMap.get("puId");
        return puid;
    }
}
