package net.engining.profile.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.engining.pg.web.BaseResponseBean;
import net.engining.pg.web.WebCommonResponse;
import net.engining.pg.web.WebCommonResponseBuilder;

public class JsonAuthSuccessHandler implements AuthenticationSuccessHandler {
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		response.setStatus(HttpStatus.OK.value());
		
		BaseResponseBean baseResponseBean = new BaseResponseBean();
		baseResponseBean.setReturnCode(WebCommonResponse.CODE_OK);
		baseResponseBean.setReturnDesc(WebCommonResponse.DESC_SUCCESS);
		
		mapper.writeValue(response.getOutputStream(), 
				new WebCommonResponseBuilder<BaseResponseBean>()
					.build()
					.setStatusCode(WebCommonResponse.CODE_OK)
					.setStatusDesc(WebCommonResponse.DESC_SUCCESS)
					.setTimestamp()
					.setResponseData(baseResponseBean)
					);
		
	}

}
