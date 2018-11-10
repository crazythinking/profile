package net.engining.profile.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.web.BaseResponseBean;
import net.engining.pg.web.NewWebCommonResponseBuilder;
import net.engining.pg.web.bean.DefaultResponseHeader;

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
		baseResponseBean.setReturnCode(ErrorCode.UnknowFail.getValue());
		baseResponseBean.setReturnDesc(ErrorCode.UnknowFail.getLabel());
		
		mapper.writeValue(response.getOutputStream(), 
				new NewWebCommonResponseBuilder<DefaultResponseHeader,BaseResponseBean>()
					.build()
					.setStatusCode(ErrorCode.UnknowFail.getValue())
					.setStatusDesc(ErrorCode.UnknowFail.getLabel())
					.setResponseData(baseResponseBean)
					);
		
	}
}
