package net.engining.profile.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.web.BaseResponseBean;
import net.engining.pg.web.NewWebCommonResponseBuilder;
import net.engining.pg.web.bean.DefaultResponseHeader;

public class JsonAuthSuccessHandler implements AuthenticationSuccessHandler {
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		response.setStatus(HttpStatus.OK.value());
		
		BaseResponseBean baseResponseBean = new BaseResponseBean();
		
		mapper.writeValue(response.getOutputStream(), 
				new NewWebCommonResponseBuilder<DefaultResponseHeader,BaseResponseBean>()
					.build()
					.setStatusCode(ErrorCode.Success.getValue())
					.setStatusDesc(ErrorCode.Success.getLabel())
					.setResponseData(baseResponseBean)
					);
		
	}

}
