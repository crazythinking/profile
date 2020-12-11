package net.engining.profile.api.controller.profile;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.control.sdk.FlowTransProcessorTemplate;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderRequest;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.pg.web.bean.DefaultRequestHeader;
import net.engining.pg.web.bean.DefaultResponseHeader;
import net.engining.profile.api.bean.request.user.AddUserRequest;
import net.engining.profile.api.bean.request.user.DeleteUserRequest;
import net.engining.profile.api.bean.request.user.DistributeRolesRequest;
import net.engining.profile.api.bean.request.user.ListUserRequest;
import net.engining.profile.api.bean.request.user.ListUserRoleRequest;
import net.engining.profile.api.bean.request.user.UpdateUserRequest;
import net.engining.profile.api.bean.request.user.UpdateUserStatusRequest;
import net.engining.profile.api.bean.response.user.ListUserResponse;
import net.engining.profile.api.bean.response.user.ListUserRoleResponse;
import net.engining.profile.api.bean.vo.UserListVo;
import net.engining.profile.api.util.CheckRequestUtils;
import net.engining.profile.api.util.ControllerUtils;
import net.engining.profile.api.util.VoTransformationUtils;
import net.engining.profile.enums.OperationType;
import net.engining.profile.enums.UserStatusEnum;
import net.engining.profile.flow.sdk.authority.DistributeRolesFlowRequest;
import net.engining.profile.flow.sdk.authority.DistributeRolesFlowResponse;
import net.engining.profile.flow.sdk.user.AddUserFlowRequest;
import net.engining.profile.flow.sdk.user.AddUserFlowResponse;
import net.engining.profile.flow.sdk.user.DeleteUserFlowRequest;
import net.engining.profile.flow.sdk.user.DeleteUserFlowResponse;
import net.engining.profile.flow.sdk.user.UpdateUserFlowRequest;
import net.engining.profile.flow.sdk.user.UpdateUserStatusFlowRequest;
import net.engining.profile.flow.sdk.user.UpdateUserStatusFlowResponse;
import net.engining.profile.sdk.service.UserManagementService;
import net.engining.profile.sdk.service.bean.dto.RoleSimpleDto;
import net.engining.profile.sdk.service.bean.dto.UserListDto;
import net.engining.profile.sdk.service.bean.query.UserPagingQuery;
import net.engining.profile.sdk.service.util.PagingQueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import static net.engining.profile.api.constant.ParameterNameConstants.USER_ID;

/**
 * @author yangxing
 */
@RequestMapping("/profile")
@RestController
@Api(value = "ProfileUserController", description = "用户服务模块")
public class ProfileUserController {

    /**
     * 用户管理服务
     */
    @Autowired
    private UserManagementService userManagementService;
    /**
     * 交易流程模版
     */
    @Autowired
    private FlowTransProcessorTemplate flowTransProcessorTemplate;

    /**
     * 用户列表查询
     *
     * @param request 请求
     * @return 返回结果
     */
    @PreAuthorize("hasAuthority('listUser')")
    @ApiOperation(value = "用户列表查询", notes = "分页查询用户表")
    @RequestMapping(value = "/listUser", method = RequestMethod.GET)
    public CommonWithHeaderResponse<Void, ListUserResponse<UserListVo>> getUsers(@Validated ListUserRequest request) {
        String userId = request.getUserId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(userId, USER_ID);
        String userName = request.getUserName();
//        CheckRequestUtils.checkIsChinese(userName, USER_NAME);
        String departmentId = request.getDepartmentId();
        CheckRequestUtils.checkIsNumber(departmentId, DEPARTMENT_ID);
        Long pageSize = request.getPageSize();
        CheckRequestUtils.checkPageSizeIsWithinRange(pageSize);

        UserPagingQuery query = PagingQueryUtils.initUserPagingQuery(userId, userName, departmentId,
                request.getPageNum(), pageSize);

        FetchResponse<UserListDto> fetchResponse = userManagementService.listUser(query);
        ListUserResponse<UserListVo> response = new ListUserResponse<>();
        VoTransformationUtils.convertToPagingQueryResponse(fetchResponse, response, source -> {
            List<UserListVo> data = new ArrayList<>(source.size());
            for (UserListDto userListDto : source) {
                UserListVo userListVo = new UserListVo();
                userListVo.setUserId(userListDto.getUserId());
                userListVo.setUserName(userListDto.getUserName());
                userListVo.setDepartmentId(userListDto.getDepartmentId());
                userListVo.setDepartmentName(userListDto.getDepartmentName());
                userListVo.setUserStatus(userListDto.getUserStatus().getLabel());
                userListVo.setRoleNameList(userListDto.getRoleNameList());
                data.add(userListVo);
            }
            return data;
        });

        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }


    /**
     * 用户新增
     *
     * @param httpServletRequest http请求
     * @param request            请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('addUser')")
    @ApiOperation(value = "用户新增", notes = "新增用户表记录")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    addUser(HttpServletRequest httpServletRequest,
            @RequestBody @Validated CommonWithHeaderRequest<DefaultRequestHeader, AddUserRequest> request) {
        AddUserRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String userId = requestData.getUserId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(userId, USER_ID);
        String userName = requestData.getUserName();
//        CheckRequestUtils.checkIsChinese(userName, USER_NAME);
        String departmentId = requestData.getDepartmentId();
        CheckRequestUtils.checkIsNumber(departmentId, DEPARTMENT_ID);
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(operatorId, OPERATOR_ID);

        AddUserFlowRequest flowRequest = new AddUserFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setUserId(userId);
        flowRequest.setUserName(userName);
        flowRequest.setDepartmentId(departmentId);
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.AD);
        flowRequest.setOperationObject(userId);

        AddUserFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                AddUserFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(),
                txnSerialNo, timestamp);
    }

    /**
     * 用户修改
     *
     * @param httpServletRequest http请求
     * @param request            请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('updateUser')")
    @ApiOperation(value = "用户修改", notes = "修改用户表记录")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    updateUser(HttpServletRequest httpServletRequest,
               @RequestBody @Validated CommonWithHeaderRequest<DefaultRequestHeader, UpdateUserRequest> request) {
        UpdateUserRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String userId = requestData.getUserId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(userId, USER_ID);
        String userName = requestData.getUserName();
//        CheckRequestUtils.checkIsChinese(userName, USER_NAME);
        String departmentId = requestData.getDepartmentId();
        CheckRequestUtils.checkIsNumber(departmentId, DEPARTMENT_ID);
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(operatorId, OPERATOR_ID);

        UpdateUserFlowRequest flowRequest = new UpdateUserFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setUserId(userId);
        flowRequest.setUserName(userName);
        flowRequest.setDepartmentId(departmentId);
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.UP);
        flowRequest.setOperationObject(userId);

        AddUserFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                AddUserFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(),
                txnSerialNo, timestamp);
    }

    /**
     * 用户删除
     *
     * @param httpServletRequest http请求
     * @param request            请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('deleteUser')")
    @ApiOperation(value = "用户删除", notes = "修改用户表记录逻辑删除标识")
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    deleteUser(HttpServletRequest httpServletRequest,
               @RequestBody @Validated CommonWithHeaderRequest<DefaultRequestHeader, DeleteUserRequest> request) {
        DeleteUserRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String userId = requestData.getUserId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(userId, USER_ID);
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(operatorId, OPERATOR_ID);

        DeleteUserFlowRequest flowRequest = new DeleteUserFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setUserId(userId);
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.DE);
        flowRequest.setOperationObject(userId);

        DeleteUserFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                DeleteUserFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(),
                txnSerialNo, timestamp);
    }

    /**
     * 修改用户状态
     *
     * @param httpServletRequest http请求
     * @param request            请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('updateUserStatus')")
    @ApiOperation(value = "修改用户状态", notes = "修改用户表记录的用户状态字段")
    @RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST)
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    updateUserStatus(HttpServletRequest httpServletRequest,
                     @RequestBody @Validated CommonWithHeaderRequest<DefaultRequestHeader, UpdateUserStatusRequest> request) {
        UpdateUserStatusRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String userId = requestData.getUserId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(userId, USER_ID);
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(operatorId, OPERATOR_ID);
        UserStatusEnum userStatus = requestData.getUserStatus();
        if (!UserStatusEnum.A.equals(userStatus) && !UserStatusEnum.L.equals(userStatus)) {
            throw new ErrorMessageException(ErrorCode.BadRequest, "用户状态类型错误");
        }

        UpdateUserStatusFlowRequest flowRequest = new UpdateUserStatusFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setUserId(userId);
        flowRequest.setUserStatus(userStatus);
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.US);
        flowRequest.setOperationObject(userId);

        UpdateUserStatusFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                UpdateUserStatusFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(),
                txnSerialNo, timestamp);
    }

    /**
     * 用户拥有角色查询
     *
     * @param request 请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('Menu_UserManagement')")
    @RequestMapping(value = "/listUserRole", method = RequestMethod.GET)
    @ApiOperation(value = "用户拥有角色查询", notes = "根据用户ID查询用户拥有的角色ID")
    public CommonWithHeaderResponse<Void, ListUserRoleResponse> listUserRole(@Validated ListUserRoleRequest request) {
        String userId = request.getUserId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(userId, USER_ID);

        List<RoleSimpleDto> dtoList = userManagementService.listRoleSimpleDtoByUserId(userId);
        ListUserRoleResponse response = new ListUserRoleResponse();
        response.setData(VoTransformationUtils.convertToRoleSimpleVoList(dtoList));

        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }

    /**
     * 分配角色
     *
     * @param httpServletRequest http请求
     * @param request            请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('distributeRoles')")
    @RequestMapping(value = "/distributeRoles", method = RequestMethod.POST)
    @ApiOperation(value = "分配角色", notes = "新增用户和角色对应关系记录")
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    distributeRoles(HttpServletRequest httpServletRequest,
                    @RequestBody @Validated CommonWithHeaderRequest<DefaultRequestHeader, DistributeRolesRequest> request) {
        DistributeRolesRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String userId = requestData.getUserId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(userId, USER_ID);
        List<String> roleIdList = requestData.getRoleIdList();
        for (String roleId : roleIdList) {
            if (roleId.length() > 20) {
                throw new ErrorMessageException(ErrorCode.BadRequest, "角色ID的字段长度不能超过20个字符");
            }
            CheckRequestUtils.checkIsNumberOrLetter(roleId, ROLE_ID);
        }
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetterOrUnderline(operatorId, OPERATOR_ID);

        DistributeRolesFlowRequest flowRequest = new DistributeRolesFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setUserId(userId);
        flowRequest.setRoleIdList(roleIdList);
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.GN);
        flowRequest.setOperationObject(userId);

        DistributeRolesFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                DistributeRolesFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(),
                txnSerialNo, timestamp);
    }

//    @PreAuthorize("hasAuthority('Maintenance')")
//    @ApiOperation(value = "根据用户userId获取用户信息", notes = "根据用户userId获取用户信息")
//    @RequestMapping(value = "/userId", method = RequestMethod.POST)
//    public @ResponseBody
//    CommonWithHeaderResponse getUserByName(@RequestBody @Validated ProfileUserForm user,
//                                           HttpServletRequest request) {
//        FetchResponse<Map<String, Object>> rsp = userManagementService.getUserInfoByUserId(user.getUserId());
//        Date date = new Date();
//        Object puid = profileSecurityLoggerService.getPuid(
//                user.getOperUserId());
//        //userNo是操作员的puid，beOper是被操作员的登录id
//        profileSecurityLoggerService.logSecuOperation(puid.toString(),
//                OperationType.QU,
//                WebCommonUtils.getIpAddress(request),
//                date, user.getUserId()
//        );
//        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>().build()
//                .setResponseData(rsp);
//    }
//
//    @PreAuthorize("hasAuthority('ProfileUser')")
//    @ApiOperation(value = "登录时根据用户userId获取用户信息", notes = "登录时根据用户userId获取用户信息")
//    @RequestMapping(value = "/loginUserId", method = RequestMethod.POST)
//    public @ResponseBody
//    CommonWithHeaderResponse loginUserByName(@RequestBody LoginUserIdForm user,
//                                             HttpServletRequest request) {
//        FetchResponse<Map<String, Object>> rsp = userManagementService.getUserInfoByUserId(user.getUserId());
//        Date date = new Date();
//        Object puid = profileSecurityLoggerService.getPuid(user.getOperUserId());
//        //userNo是操作员的puid，beOper是被操作员的登录id
//        profileSecurityLoggerService.logSecuOperation(
//                puid.toString(),
//                OperationType.LG,
//                WebCommonUtils.getIpAddress(request),
//                date, user.getUserId()
//        );
//        return new CommonWithHeaderResponseBuilder<Void, FetchResponse<Map<String, Object>>>()
//                .build()
//                .setResponseData(rsp);
//    }
//
//    @PreAuthorize("hasAuthority('PuId')")
//    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
//    @RequestMapping(value = "/puId", method = RequestMethod.POST)
//    public @ResponseBody
//    CommonWithHeaderResponse getUser(@RequestBody @Validated ProfileUserForm user,
//                                     HttpServletRequest request) {
//        ProfileUser profileUser = profileSecurityService.getUserInfo(user.getPuId());
//        MgmWebUser mgmWebUser = userService.mgmWebUser(profileUser);
//        Date date = new Date();
//        Object puid = profileSecurityLoggerService.getPuid(user.getOperUserId());
//        profileSecurityLoggerService.logSecuOperation(
//                puid.toString(),
//                OperationType.QU,
//                WebCommonUtils.getIpAddress(request), date,
//                user.getUserId()
//        );
//        return new CommonWithHeaderResponseBuilder<Void, MgmWebUser>()
//                .build()
//                .setResponseData(mgmWebUser);
//    }

//    @PreAuthorize("hasAuthority('Maintenance')")
//    @ApiOperation(value = "删除多个用户", notes = "删除多个用户")
//    @RequestMapping(value = "/removeUsers", method = RequestMethod.POST)
//    public @ResponseBody
//    CommonWithHeaderResponse removeUser(@RequestBody
//                                        @ApiParam(value = "puIds", required = true)
//                                                String[] puIds) {
//        List<String> usrs = Arrays.asList(puIds);
//        userManagementService.deleteProfileUsers(usrs);
//        return new CommonWithHeaderResponseBuilder<Void, Void>().build();
//    }

}