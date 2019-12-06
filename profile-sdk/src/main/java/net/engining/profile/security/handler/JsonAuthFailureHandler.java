package net.engining.profile.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.profile.enums.ProfileErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 返回Json格式的spring security框架用户身份验证失败后处理
 *
 * @author luxue
 */
public class JsonAuthFailureHandler implements AuthenticationFailureHandler {


    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        if (exception instanceof LockedException) {

            mapper.writeValue(
                    response.getOutputStream(),
                    new CommonWithHeaderResponseBuilder<Void, Void>()
                            .build()
                            .setStatusCode(ProfileErrorCode.Error9100.getValue())
                            .setStatusDesc(ProfileErrorCode.Error9100.getLabel())
            );
        } else {
            mapper.writeValue(response.getOutputStream(),
                    new CommonWithHeaderResponseBuilder<Void, Void>()
                            .build()
                            .setStatusCode(ProfileErrorCode.Error9200.getValue())
                            .setStatusDesc(ProfileErrorCode.Error9200.getLabel())
                            //TODO key命名不合理
                            .putAdditionalRepMap("111", ProfileErrorCode.Error9200.getLabel())

            );
        }

    }
}




