package net.engining.profile.api.controller.profile;


import io.swagger.annotations.ApiOperation;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.api.bean.request.authority.ListAuthorityRequest;
import net.engining.profile.api.bean.response.DropdownResponse;
import net.engining.profile.api.bean.response.authority.ListAuthorityResponse;
import net.engining.profile.api.bean.vo.DepartmentSimpleVo;
import net.engining.profile.api.bean.vo.RoleSimpleVo;
import net.engining.profile.api.bean.vo.SystemSimpleVo;
import net.engining.profile.api.util.ControllerUtils;
import net.engining.profile.api.util.VoTransformationUtils;
import net.engining.profile.enums.SystemEnum;
import net.engining.profile.sdk.service.DepartmentManagementService;
import net.engining.profile.sdk.service.ProfileMgmService;
import net.engining.profile.sdk.service.RoleManagementService;
import net.engining.profile.sdk.service.bean.dto.DepartmentSimpleDto;
import net.engining.profile.sdk.service.bean.dto.RoleSimpleDto;
import net.engining.profile.sdk.service.bean.dto.SystemSimpleDto;
import net.engining.profile.sdk.service.query.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liudongjing
 */
@RequestMapping(value = "/profile")
@RestController
public class ProfileMgmController {

    /**
     * 后台管理服务
     */
    @Autowired
    private ProfileMgmService profileMgmService;
    /**
     * 菜单权限服务
     */
    @Autowired
    private AuthService authService;
    /**
     * 角色服务
     */
    @Autowired
    private RoleManagementService roleManagementService;
    /**
     * 机构服务
     */
    @Autowired
    private DepartmentManagementService departmentManagementService;

    /**
     * 可分配权限查询
     *
     * @param request 请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('Menu_RoleManagement')")
    @RequestMapping(value = "/listAuthority", method = RequestMethod.GET)
    @ApiOperation(value = "可分配权限查询", notes = "根据所属系统查询旗下所有权限")
    public CommonWithHeaderResponse<Void, ListAuthorityResponse>
    listAuthorityBySystem(@Valid ListAuthorityRequest request) {
        SystemEnum system = request.getSystem();
        String menuList;
        if (SystemEnum.OAUTH2.equals(system)) {
            menuList = null;
        } else {
            //所有角色
            menuList = authService.getAuthorityData(system);
        }
        ListAuthorityResponse response = new ListAuthorityResponse();
        response.setData(menuList);
        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }

    /**
     * 系统下拉框查询
     *
     * @return 结果
     */
    @ApiOperation(value = "系统下拉框查询", notes = "查询所有系统信息")
    @RequestMapping(value = "/getAllSystem", method = RequestMethod.GET)
    public CommonWithHeaderResponse<Void, DropdownResponse<SystemSimpleVo>> getAllSystemForDropdown() {
        DropdownResponse<SystemSimpleVo> response = new DropdownResponse<>();
        List<SystemSimpleDto> systemSimpleDtoList = profileMgmService.getAllSystem();
        if (ValidateUtilExt.isNotNullOrEmpty(systemSimpleDtoList)) {
            List<SystemSimpleVo> systemSimpleVoList = new ArrayList<>(systemSimpleDtoList.size());
            for (SystemSimpleDto dto : systemSimpleDtoList) {
                SystemSimpleVo vo = new SystemSimpleVo();
                vo.setSystemId(dto.getSystemId());
                vo.setSystemName(dto.getSystemName());
                systemSimpleVoList.add(vo);
            }
            response.setData(systemSimpleVoList);
        }

        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }

    /**
     * 角色下拉框查询
     *
     * @return 结果
     */
    @PreAuthorize("hasAuthority('Menu_UserManagement')")
    @ApiOperation(value = "角色下拉框查询", notes = "查询所有角色信息")
    @RequestMapping(value = "/getAllRole", method = RequestMethod.GET)
    public CommonWithHeaderResponse<Void, DropdownResponse<RoleSimpleVo>> getAllRoleForDropdown() {
        List<RoleSimpleDto> roleSimpleDtoList = roleManagementService.getAllRole();
        List<RoleSimpleVo> roleSimpleVoList = VoTransformationUtils.convertToRoleSimpleVoList(roleSimpleDtoList);

        DropdownResponse<RoleSimpleVo> response = new DropdownResponse<>();
        response.setData(roleSimpleVoList);

        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }

    /**
     * 部门下拉框查询
     *
     * @return 结果
     */
    @PreAuthorize("hasAnyAuthority('Menu_RoleManagement', 'Menu_UserManagement', 'Menu_DepartmentManagement')")
    @ApiOperation(value = "部门下拉框查询", notes = "查询所有部门信息")
    @RequestMapping(value = "/getAllDepartment", method = RequestMethod.GET)
    public CommonWithHeaderResponse<Void, DropdownResponse<DepartmentSimpleVo>> getAllDepartmentForDropdown() {
        List<DepartmentSimpleDto> departmentSimpleDtoList = departmentManagementService.getAllDepartment();
        DropdownResponse<DepartmentSimpleVo> response = new DropdownResponse<>();
        if (ValidateUtilExt.isNotNullOrEmpty(departmentSimpleDtoList)) {
            List<DepartmentSimpleVo> departmentSimpleVoList = new ArrayList<>(departmentSimpleDtoList.size());
            for (DepartmentSimpleDto dto : departmentSimpleDtoList) {
                DepartmentSimpleVo vo = new DepartmentSimpleVo();
                vo.setDepartmentId(dto.getDepartmentId());
                vo.setDepartmentName(dto.getDepartmentName());
                departmentSimpleVoList.add(vo);
            }
            response.setData(departmentSimpleVoList);
        }

        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }
//    /**
//     * 角色权限分配
//     *
//     * @return
//     */
//    @PreAuthorize("hasAuthority('DistributionProfileRole')")
//    @RequestMapping(value = "/distributeAuthority", method = RequestMethod.POST)
//    @ApiOperation(value = "分配权限", notes = "新增角色和权限对应关系记录")
//    public CommonWithHeaderResponse<Void, Void>
//    distributeAuthority(@RequestBody @Validated DistributeAuthorityRequest request) {
//
//        authService.distributionProfileRole(request.getRoleId(),
//                request.getAuthList());
//
//        return ControllerUtils.returnSuccessResponseWithNothing();
//    }
    //    /**
//     * 分页查询用户角色
//     *
//     * @return
//     */
////    @PreAuthorize("hasAuthority('ProfileUser')")
//    @RequestMapping(value = "/fetchProfileUser", method = RequestMethod.POST)
//    @ApiOperation(value = "根据用户信息查询其角色", notes = "根据用户信息查询其角色")
//    public @ResponseBody
//    CommonWithHeaderResponse fetchProfileUser(
//            @RequestBody @Validated ProfileUserBranchForm profileUserBranchForm) {
//
//        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService.fetchProfileUser(
//                profileUserBranchForm.getBranchId(), provider4Organization.getCurrentOrganizationId(),
//                profileUserBranchForm.getName(), profileUserBranchForm.getRange());
//
//        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
//                .build()
//                .setResponseData(fetchResponse);
//    }

//    /**
//     * 获取所有的角色（分配角色显示）
//     */
////    @PreAuthorize("hasAuthority('ProfileUser')")
//    @RequestMapping(value = "/fetchAllProfileRole", method = RequestMethod.GET)
//    @ApiOperation(value = " 可分配权限查询", notes = "获取所有的角色")
//    public
//    CommonWithHeaderResponse fetchAllProfileRole(@ApiParam(value = "应用代码", required = false)
//                                                 @RequestParam(required = false)
//                                                         String appCd) {
//        FetchResponse<Map<String, Object>> fetchResponse = profileMgmService.fetchAllProfileRole(appCd);
//
//        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
//                .build()
//                .setResponseData(fetchResponse);
//    }
}
