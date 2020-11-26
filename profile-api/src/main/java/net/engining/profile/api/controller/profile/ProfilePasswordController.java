package net.engining.profile.api.controller.profile;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.control.sdk.FlowTransProcessorTemplate;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderRequest;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.pg.web.bean.DefaultRequestHeader;
import net.engining.pg.web.bean.DefaultResponseHeader;
import net.engining.profile.api.bean.request.password.ChangePasswordRequest;
import net.engining.profile.api.bean.request.password.ResetPasswordRequest;
import net.engining.profile.api.bean.response.department.AddDepartmentResponse;
import net.engining.profile.api.util.CheckRequestUtils;
import net.engining.profile.api.util.ControllerUtils;
import net.engining.profile.config.props.ProfileOauthProperties;
import net.engining.profile.enums.OperationType;
import net.engining.profile.flow.sdk.department.AddDepartmentFlowResponse;
import net.engining.profile.flow.sdk.password.ChangePasswordFlowRequest;
import net.engining.profile.flow.sdk.password.ChangePasswordFlowResponse;
import net.engining.profile.flow.sdk.password.ResetPasswordFlowRequest;
import net.engining.profile.sdk.service.ProfilePasswordService;
import net.engining.profile.sdk.service.UserManagementService;
import net.engining.profile.security.service.ProfileSecurityLoggerService;
import net.engining.profile.security.service.ProfileSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

import static net.engining.profile.api.constant.ParameterNameConstants.OPERATOR_ID;
import static net.engining.profile.api.constant.ParameterNameConstants.USER_ID;

/**
 * @author yangxing
 */
@Api(value = "ProfilePasswordController", description = "密码服务模块")
@RequestMapping("/profile")
@RestController
public class ProfilePasswordController {
    /**
     * 用户服务
     */
    @Autowired
    private UserManagementService userManagementService;
    /**
     * 密码服务
     */
    @Autowired
    ProfilePasswordService profilePasswordService;
    /**
     * 安全服务
     */
    @Autowired
    ProfileSecurityService profileSecurityService;
    /**
     * 安全日志服务
     */
    @Autowired
    ProfileSecurityLoggerService profileSecurityLoggerService;
    /**
     * 交易流程模版
     */
    @Autowired
    private FlowTransProcessorTemplate flowTransProcessorTemplate;
    /**
     * 授权中心配置参数
     */
    @Autowired
    private ProfileOauthProperties profileOauthProperties;

    /**
     * 重置登陆密码
     *
     * @param httpServletRequest http请求
     * @param request            请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('resetPassword')")
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    resetPasswordByAdmin(HttpServletRequest httpServletRequest,
                         @Valid @RequestBody CommonWithHeaderRequest<DefaultRequestHeader, ResetPasswordRequest> request) {
        ResetPasswordRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String userId = requestData.getUserId();
        CheckRequestUtils.checkIsNumberOrLetter(userId, USER_ID);
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetter(operatorId, OPERATOR_ID);

        ResetPasswordFlowRequest flowRequest = new ResetPasswordFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setUserId(userId);
        flowRequest.setPassword(profileOauthProperties.getDefaultPassword());
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.RP);
        flowRequest.setOperationObject(operatorId);

        AddDepartmentFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                AddDepartmentFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        AddDepartmentResponse responseData = new AddDepartmentResponse();
        responseData.setDepartmentId(flowResponse.getDepartmentId());

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(), txnSerialNo,
                timestamp);
    }

    /**
     * 修改密码
     *
     * @param request 请求
     * @return 响应结果
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    changePassword(HttpServletRequest httpServletRequest,
                   @Valid @RequestBody CommonWithHeaderRequest<DefaultRequestHeader, ChangePasswordRequest> request) {
        // 请求体
        ChangePasswordRequest requestData = request.getRequestData();
        // 请求头
        DefaultRequestHeader requestHead = request.getRequestHead();

        // 交易流水
        String txnSerialNo = requestHead.getTxnSerialNo();
        // 交易时间
        Date timestamp = requestHead.getTimestamp();

        ChangePasswordFlowRequest flowRequest = new ChangePasswordFlowRequest();
        // 封装请求体里的参数
        flowRequest.setPuId(requestData.getPuId());
        flowRequest.setOriginalPassword(requestData.getOldPassword());
        flowRequest.setPassword(requestData.getNewPassword());
        flowRequest.setOperatorId(requestData.getOperatorId());
        // 封装请求头里的参数
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setOnlineData(JSON.toJSONString(request));
        // 其它
        flowRequest.setOperationType(OperationType.CP);
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));

        ChangePasswordFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest, ChangePasswordFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(),
                txnSerialNo, timestamp);
    }

    //    @PreAuthorize("hasAuthority('ProfileRole')")
//    @ApiOperation(value = "修改密码", notes = "修改密码")
//    @RequestMapping(value = "/changePwdByAdmin/{puId}", method = RequestMethod.POST)
//    public @ResponseBody
//    CommonWithHeaderResponse changePasswordByAdmin(@PathVariable String puId,
//                                                   @RequestBody @Validated ChangePasswordForm changePasswordForm,
//                                                   HttpServletRequest request) {
//
//        String opUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        profileSecurityService.changeMyPassword(puId, changePasswordForm.getOldPassword(), changePasswordForm.getNewPassword(), opUser);
//        Date date = new Date();
//        ProfileUser profileUserInfo = userManagementService.findProfileUserInfo(puId);
//        String userId=profileUserInfo.getUserId();
//        profileSecurityLoggerService.logSecuOperation(puId, OperationType.CP, WebCommonUtils.getIpAddress(request),date,userId, null);
//        return new CommonWithHeaderResponseBuilder<Void,Void>()
//                .build();
//    }
//    /**
//     * 重置登陆密码
//     *
//     * @param puId
//     * @return
//     */
//    @PreAuthorize("hasAuthority('ProfileRole')")
//    @ApiOperation(value = "重置密码", notes = "重置密码")
//    @RequestMapping(value = "/resetPwdByAdmin/{puId}", method = RequestMethod.POST)
//    public @ResponseBody
//    CommonWithHeaderResponse resetPasswordByAdmin(@PathVariable String puId) {
//
//        String opUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        profilePasswordService.resetPassword(puId, opUser);
//
//        return new CommonWithHeaderResponseBuilder<Void,Void>().build();
//    }

}