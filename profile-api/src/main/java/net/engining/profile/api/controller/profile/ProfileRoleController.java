package net.engining.profile.api.controller.profile;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.control.sdk.FlowTransProcessorTemplate;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderRequest;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.pg.web.bean.DefaultRequestHeader;
import net.engining.pg.web.bean.DefaultResponseHeader;
import net.engining.profile.api.bean.request.authority.DistributeAuthorityRequest;
import net.engining.profile.api.bean.request.role.AddRoleRequest;
import net.engining.profile.api.bean.request.role.ListRoleAuthRequest;
import net.engining.profile.api.bean.request.role.ListRoleRequest;
import net.engining.profile.api.bean.request.role.UpdateRoleRequest;
import net.engining.profile.api.bean.response.role.AddRoleResponse;
import net.engining.profile.api.bean.response.role.ListRoleAuthResponse;
import net.engining.profile.api.bean.response.role.ListRoleResponse;
import net.engining.profile.api.bean.vo.RoleAuthVo;
import net.engining.profile.api.bean.vo.RoleListVo;
import net.engining.profile.api.util.CheckRequestUtils;
import net.engining.profile.api.util.ControllerUtils;
import net.engining.profile.api.util.VoTransformationUtils;
import net.engining.profile.entity.dto.ProfileRoleAuthDto;
import net.engining.profile.enums.OperationType;
import net.engining.profile.flow.sdk.authority.DistributeAuthoritiesFlowRequest;
import net.engining.profile.flow.sdk.authority.DistributeAuthoritiesFlowResponse;
import net.engining.profile.flow.sdk.role.AddRoleFlowRequest;
import net.engining.profile.flow.sdk.role.AddRoleFlowResponse;
import net.engining.profile.flow.sdk.role.UpdateRoleFlowRequest;
import net.engining.profile.flow.sdk.role.UpdateRoleFlowResponse;
import net.engining.profile.sdk.service.RoleManagementService;
import net.engining.profile.sdk.service.bean.dto.RoleListDto;
import net.engining.profile.sdk.service.bean.query.RolePagingQuery;
import net.engining.profile.sdk.service.util.PagingQueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.engining.profile.api.constant.ParameterNameConstants.DEPARTMENT_ID;
import static net.engining.profile.api.constant.ParameterNameConstants.OPERATOR_ID;
import static net.engining.profile.api.constant.ParameterNameConstants.ROLE_ID;

/**
 * @author yangxing
 */
@Api(value = "角色管理服务")
@RequestMapping("/profile")
@RestController
public class ProfileRoleController {

    /**
     * 角色名称
     */
    private final String ROLE_NAME = "角色名称";

    /**
     * 角色服务
     */
    @Autowired
    private RoleManagementService roleManagementService;
    /**
     * 交易流程模版
     */
    @Autowired
    private FlowTransProcessorTemplate flowTransProcessorTemplate;

    /**
     * 角色列表查询
     *
     * @param request 请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('listRole')")
    @RequestMapping(value = "/listRole", method = RequestMethod.GET)
    @ApiOperation(value = "角色列表查询", notes = "分页查询角色表")
    public CommonWithHeaderResponse<Void, ListRoleResponse<RoleListVo>> listRole(@Validated ListRoleRequest request) {
        Long pageSize = request.getPageSize();
        CheckRequestUtils.checkPageSizeIsWithinRange(pageSize);
        String roleName = request.getRoleName();
        CheckRequestUtils.checkIsChinese(roleName, ROLE_NAME);

        RolePagingQuery query = PagingQueryUtils.initRolePagingQuery(roleName, request.getPageNum(), pageSize);

        FetchResponse<RoleListDto> fetchResponse = roleManagementService.listRole(query);
        ListRoleResponse<RoleListVo> response = new ListRoleResponse<>();
        VoTransformationUtils.convertToPagingQueryResponse(fetchResponse, response, source -> {
            BeanCopier copier = BeanCopier.create(RoleListDto.class, RoleListVo.class, false);
            List<RoleListVo> data = new ArrayList<>(source.size());
            for (RoleListDto dto : source) {
                RoleListVo vo = new RoleListVo();
                copier.copy(dto, vo, null);
                data.add(vo);
            }
            return data;
        });

        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }


    /**
     * 角色新增
     *
     * @param httpServletRequest http请求
     * @param request            请求
     * @return 结果
     */
	@PreAuthorize("hasAuthority('addRole')")
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色新增", notes = "新增角色表记录")
    public CommonWithHeaderResponse<DefaultResponseHeader, AddRoleResponse>
    saveProfileRole(HttpServletRequest httpServletRequest,
                    @RequestBody @Validated CommonWithHeaderRequest<DefaultRequestHeader, AddRoleRequest> request) {
        AddRoleRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String roleName = requestData.getRoleName();
        CheckRequestUtils.checkIsChinese(roleName, ROLE_NAME);
        String departmentId = requestData.getDepartmentId();
        CheckRequestUtils.checkIsNumber(departmentId, DEPARTMENT_ID);
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetter(operatorId, OPERATOR_ID);

        AddRoleFlowRequest flowRequest = new AddRoleFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setRoleName(roleName);
        flowRequest.setDepartmentId(departmentId);
        flowRequest.setSystem(requestData.getSystem());
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.AR);

        AddRoleFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                AddRoleFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        AddRoleResponse responseData = new AddRoleResponse();
        responseData.setRoleId(flowResponse.getRoleId());

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(),
                txnSerialNo, timestamp, responseData);
    }

    /**
     * 角色修改
     *
     * @param httpServletRequest http请求
     * @param request            请求
     * @return 结果
     */
	@PreAuthorize("hasAuthority('updateRole')")
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    @ApiOperation(value = "角色修改", notes = "修改角色表记录")
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    updateProfileRole(HttpServletRequest httpServletRequest,
                      @RequestBody @Validated CommonWithHeaderRequest<DefaultRequestHeader, UpdateRoleRequest> request) {
        UpdateRoleRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String roleId = requestData.getRoleId();
        CheckRequestUtils.checkIsNumberOrLetter(roleId, ROLE_ID);
        String roleName = requestData.getRoleName();
        CheckRequestUtils.checkIsChinese(roleName, ROLE_NAME);
        String departmentId = requestData.getDepartmentId();
        CheckRequestUtils.checkIsNumber(departmentId, DEPARTMENT_ID);
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetter(operatorId, OPERATOR_ID);

        UpdateRoleFlowRequest flowRequest = new UpdateRoleFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setRoleId(roleId);
        flowRequest.setRoleName(roleName);
        flowRequest.setDepartmentId(departmentId);
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.UR);
        flowRequest.setOperationObject(roleId);

        UpdateRoleFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                UpdateRoleFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(), txnSerialNo, timestamp);
    }

    /**
     * 角色权限分配
     *
     * @param httpServletRequest http请求
     * @param request            请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('distributeAuthority')")
    @RequestMapping(value = "/distributeAuthority", method = RequestMethod.POST)
    @ApiOperation(value = "分配权限", notes = "新增角色和权限对应关系记录")
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    distributeAuthority(HttpServletRequest httpServletRequest,
                        @RequestBody @Validated CommonWithHeaderRequest<DefaultRequestHeader, DistributeAuthorityRequest> request) {
        DistributeAuthorityRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String roleId = requestData.getRoleId();
        CheckRequestUtils.checkIsNumberOrLetter(roleId, ROLE_ID);
        List<RoleAuthVo> authList = requestData.getAuthList();
        List<ProfileRoleAuthDto> profileRoleAuthDtoList = new ArrayList<>(authList.size());
        for (RoleAuthVo roleAuthVo : authList) {
            String authority = roleAuthVo.getAuthority();
            String autuUri = roleAuthVo.getAutuUri();
            CheckRequestUtils.checkIsNumberOrLetterOrUnderline(authority, "权限标识");
            CheckRequestUtils.checkIsNumberOrLine(autuUri, "权限url");
            ProfileRoleAuthDto profileRoleAuthDto = new ProfileRoleAuthDto();
            profileRoleAuthDto.setAuthority(authority);
            profileRoleAuthDto.setAutuUri(autuUri);
            profileRoleAuthDtoList.add(profileRoleAuthDto);
        }
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetter(operatorId, OPERATOR_ID);

        DistributeAuthoritiesFlowRequest flowRequest = new DistributeAuthoritiesFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setRoleId(roleId);
        flowRequest.setProfileRoleAuthDtoList(profileRoleAuthDtoList);
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.DA);
        flowRequest.setOperationObject(roleId);

        DistributeAuthoritiesFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                DistributeAuthoritiesFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(), txnSerialNo, timestamp);
    }

    /**
     * 角色拥有的权限查询请求
     *
     * @param request            请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('Menu_RoleManagement')")
    @RequestMapping(value = "/listRoleAuth", method = RequestMethod.GET)
    @ApiOperation(value = "角色拥有的权限查询", notes = "根据角色ID查询角色拥有的权限")
    public CommonWithHeaderResponse<Void, ListRoleAuthResponse> listRoleAuthByRoleId(ListRoleAuthRequest request) {
        String roleId = request.getRoleId();
        CheckRequestUtils.checkIsNumberOrLetter(roleId, ROLE_ID);

        List<String> authorities = roleManagementService.listAuthorityByRoleId(roleId);
        ListRoleAuthResponse response = new ListRoleAuthResponse();
        response.setData(authorities);

        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }

//    /**
//     * 角色删除
//     *
//     * @param request 请求
//     * @return 结果
//     */
//    @PreAuthorize("hasAuthority('DeleteProfileRole')")
//    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
//    @ApiOperation(value = "角色删除", notes = "修改角色表记录逻辑删除标识")
//    public CommonWithHeaderResponse<Void, Void>
//    deleteProfileRole(HttpServletRequest httpServletRequest,
//                      @RequestBody @Validated CommonWithHeaderRequest<DefaultRequestHeader, DeleteRoleRequest> request) {
//        profileRoleService.deleteProfileRoles(
//                profileRoleDelForm.getRoleId()
//        );

//        return null;
//    }

    //	@PreAuthorize("hasAuthority('ProfileRole')")
//	@ApiOperation(value="通过角色id查询角色信息", notes="通过角色id查询角色信息")
//	@RequestMapping(value="/getRole/{roleId}",method= RequestMethod.POST)
//	public @ResponseBody
//	CommonWithHeaderResponse getRole(@PathVariable String roleId) {
//		ProfileRole fetchResponse = profileRoleService.getProfileRoleInfo(roleId);
//
//		return new CommonWithHeaderResponseBuilder<Void,ProfileRole>()
//				.build()
//				.setResponseData(fetchResponse);
//	}


//	@PreAuthorize("hasAuthority('ProfileRole')")
//	@ApiOperation(value="删除角色下的用户", notes="删除角色下的用户")
//	@RequestMapping(value="/delRoleUsers",method= RequestMethod.POST)
//	public @ResponseBody
//	CommonWithHeaderResponse deleteRole2User(@RequestBody
//											 @ApiParam(value = "key:角色id、value:用户id", required = true)
//													 Map<String, String> roleUsers){
//		profileRoleService.deleteProfileUserRoles(roleUsers);
//
//		return new CommonWithHeaderResponseBuilder<Void,Void>()
//				.build();
//
//	}

}