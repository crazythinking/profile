package net.engining.profile.api.controller.profile;


import io.swagger.annotations.ApiOperation;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.enums.OperationType;
import net.engining.profile.sdk.service.ProfilePasswordService;
import net.engining.profile.sdk.service.ProfileUserService;
import net.engining.profile.sdk.service.bean.ChangePasswordForm;
import net.engining.profile.security.ProfileSecurityLoggerService;
import net.engining.profile.security.ProfileSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author yangxing
 */
@RequestMapping("/profile")
@RestController
public class ProfilePasswordController {

    @Autowired
    private ProfileUserService profileUserService;

    @Autowired
    ProfilePasswordService profilePasswordService;

    @Autowired
    ProfileSecurityService profileSecurityService;

    @Autowired
    ProfileSecurityLoggerService profileSecurityLoggerService;

    @ApiOperation(value = "修改密码", notes = "")
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
    @ApiOperation(value = "重置密码", notes = "")
    @RequestMapping(value = "/resetPwdByAdmin/{puId}", method = RequestMethod.POST)
    public @ResponseBody
    CommonWithHeaderResponse resetPasswordByAdmin(@PathVariable String puId) {

        String opUser = SecurityContextHolder.getContext().getAuthentication().getName();
        profilePasswordService.resetPassword(puId, opUser);

        return new CommonWithHeaderResponseBuilder<Void,Void>().build();
    }
}