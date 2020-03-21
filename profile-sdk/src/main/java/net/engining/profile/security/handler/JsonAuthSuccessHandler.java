package net.engining.profile.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.WebCommonUtils;
import net.engining.profile.enums.OperationType;
import net.engining.profile.security.service.ProfileSecurityLoggerService;
import net.engining.profile.security.service.ProfileUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 返回Json格式的spring security框架用户身份验证成功后处理
 * @author Eric Lu
 */
public class JsonAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ProfileSecurityLoggerService securityLoggerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        ProfileUserDetails profileUserDetails = (ProfileUserDetails)authentication.getPrincipal();

        //记录安全日志
        securityLoggerService.logSecuOperation(
                profileUserDetails.getPuId(),
                OperationType.LG,
                WebCommonUtils.getIpAddress(request),
                new Date(),
                null
        );

        response.setStatus(HttpStatus.OK.value());

        mapper.writeValue(response.getOutputStream(),
                new CommonWithHeaderResponseBuilder<Void, Void>()
                        .build()
                        .setStatusCode(ErrorCode.Success.getValue())
                        .setStatusDesc(ErrorCode.Success.getLabel())
        );

    }

}
