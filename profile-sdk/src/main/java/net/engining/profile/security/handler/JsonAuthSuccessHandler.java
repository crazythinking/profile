package net.engining.profile.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 返回Json格式的spring security框架用户身份验证成功后处理
 * @author Eric Lu
 */
@Deprecated
public class JsonAuthSuccessHandler implements AuthenticationSuccessHandler {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.OK.value());

        mapper.writeValue(response.getOutputStream(),
                new CommonWithHeaderResponseBuilder<Void, Void>()
                        .build()
                        .setStatusCode(ErrorCode.Success.getValue())
                        .setStatusDesc(ErrorCode.Success.getLabel())
        );

    }

}
