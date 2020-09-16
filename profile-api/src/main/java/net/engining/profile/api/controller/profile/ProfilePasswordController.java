package net.engining.profile.api.controller.profile;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.control.sdk.FlowTransProcessorTemplate;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderRequest;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.pg.web.bean.DefaultRequestHeader;
import net.engining.pg.web.bean.DefaultResponseHeader;
import net.engining.profile.api.bean.request.ChangePasswordRequest;
import net.engining.profile.api.util.ControllerUtils;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.enums.OperationType;
import net.engining.profile.flow.sdk.ChangePasswordFlowRequest;
import net.engining.profile.flow.sdk.ChangePasswordFlowResponse;
import net.engining.profile.sdk.service.ProfilePasswordService;
import net.engining.profile.sdk.service.ProfileUserService;
import net.engining.profile.sdk.service.bean.ChangePasswordForm;
import net.engining.profile.security.service.ProfileSecurityLoggerService;
import net.engining.profile.security.service.ProfileSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author yangxing
 */
@Api(value = "ProfilePasswordController",description = "密码服务模块")
@RequestMapping("/profile")
@RestController
public class ProfilePasswordController {
    /**
     * 用户服务
     */
    @Autowired
    private ProfileUserService profileUserService;
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

    @PreAuthorize("hasAuthority('ProfileRole')")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/changePwdByAdmin/{puId}", method = RequestMethod.POST)
    public @ResponseBody
    CommonWithHeaderResponse changePasswordByAdmin(@PathVariable String puId,
                                                   @RequestBody @Validated ChangePasswordForm changePasswordForm,
                                                   HttpServletRequest request) {

        String opUser = SecurityContextHolder.getContext().getAuthentication().getName();
        profileSecurityService.changeMyPassword(puId, changePasswordForm.getOldPassword(), changePasswordForm.getNewPassword(), opUser);
        Date date = new Date();
        ProfileUser profileUserInfo = profileUserService.findProfileUserInfo(puId);
        String userId=profileUserInfo.getUserId();
        profileSecurityLoggerService.logSecuOperation(puId, OperationType.CP, WebCommonUtils.getIpAddress(request),date,userId);
        return new CommonWithHeaderResponseBuilder<Void,Void>()
                .build();
    }

    /**
     * 重置登陆密码
     *
     * @param puId
     * @return
     */
    @PreAuthorize("hasAuthority('ProfileRole')")
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @RequestMapping(value = "/resetPwdByAdmin/{puId}", method = RequestMethod.POST)
    public @ResponseBody
    CommonWithHeaderResponse resetPasswordByAdmin(@PathVariable String puId) {

        String opUser = SecurityContextHolder.getContext().getAuthentication().getName();
        profilePasswordService.resetPassword(puId, opUser);

        return new CommonWithHeaderResponseBuilder<Void,Void>().build();
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
    changePassword(CommonWithHeaderRequest<DefaultRequestHeader, ChangePasswordRequest> request) {
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

        ChangePasswordFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest, ChangePasswordFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        DefaultResponseHeader responseHeader = new DefaultResponseHeader();
        responseHeader.setTxnSerialNo(txnSerialNo);
        responseHeader.setTimestamp(timestamp);
        responseHeader.setSvPrSerialNo(flowResponse.getTransId());

        return new CommonWithHeaderResponse<DefaultResponseHeader, Void>()
                .setStatusCode(ErrorCode.Success.getValue())
                .setStatusDesc(ErrorCode.Success.getLabel())
                .setResponseHead(responseHeader);
    }

}