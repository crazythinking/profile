package net.engining.profile.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.engining.pg.web.BaseResponseBean;
import net.engining.pg.web.WebCommonResponse;
import net.engining.pg.web.WebCommonResponseBuilder;

/**
 * 用户身份验证失败时的处理
 * @author luxue
 *
 */
public class JsonAuthFailureHandler implements AuthenticationFailureHandler {
	
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,	HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		
		BaseResponseBean baseResponseBean = new BaseResponseBean();
		baseResponseBean.setReturnCode(WebCommonResponse.CODE_UNKNOW_FAIL);
		baseResponseBean.setReturnDesc(exception.getMessage());
		
		mapper.writeValue(response.getOutputStream(), 
				new WebCommonResponseBuilder<BaseResponseBean>()
					.build()
					.setStatusCode(WebCommonResponse.CODE_UNKNOW_FAIL)
					.setStatusDesc(exception.getMessage())
					.setTimestamp()
					.setResponseData(baseResponseBean)
					);
		
	}
}
