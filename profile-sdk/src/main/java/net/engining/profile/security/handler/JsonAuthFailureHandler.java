package net.engining.profile.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.web.CommonResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户身份验证失败时的处理
 *
 * @author luxue
 */
public class JsonAuthFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        mapper.writeValue(response.getOutputStream(),
                new CommonResponseBuilder()
                        .build()
                        .setReturnCode(ErrorCode.Restricted.getValue())
                        .setReturnDesc(ErrorCode.Restricted.getLabel())

        );

    }
}
