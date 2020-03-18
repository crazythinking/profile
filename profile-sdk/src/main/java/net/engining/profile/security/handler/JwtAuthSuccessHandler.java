package net.engining.profile.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.pg.web.WebCommonUtils;
import net.engining.profile.enums.OperationType;
import net.engining.profile.security.service.ProfileSecurityLoggerService;
import net.engining.profile.security.service.ProfileUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 继承{@link AuthenticationSuccessHandler}; 实现以JWT方式传递用户登录成功后的信息；<br>
 * 由于该方式是sessionless的，即服务端不产生session，因此在JWT中保存需要保存用户之后操作中必须的信息主键；<br>
 * 要避免敏感信息，尽量少且不变的相关信息主键，其他信息交给后端实时查询；
 * @author luxue
 *
 */
public class JwtAuthSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	ProfileSecurityLoggerService securityLoggerService;
	
	@Autowired
	GmCommonProperties commonProperties;

	/**
	 * JWT secret key
	 */
	private static String signingKey = WebCommonUtils.SE_JWT_SIGNKEY;
	
	private static long expirationMills = 30 * 60 * 1000;

	private static String issuer = "PROFILE";
	
	private ObjectMapper mapper;

	/**
	 * 由外部传入，减少ObjectMapper实例化的消耗，此对象的实例化比较耗费资源
	 */
	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication)throws IOException, ServletException {
		
		ProfileUserDetails profileUserDetails = (ProfileUserDetails)authentication.getPrincipal();
		//Collection 转为逗号分隔String
		String authorities = Joiner.on(",").join(authentication.getAuthorities());

		//记录安全日志
		securityLoggerService.logSecuOperation(
				profileUserDetails.getPuId(),
				OperationType.LG,
				WebCommonUtils.getIpAddress(request),
				new Date(),
				null
		);
		
		//生成JWT，并存入Header
		if(StringUtils.isNotBlank(commonProperties.getJwtSignKey())){
			signingKey = commonProperties.getJwtSignKey();
		}
		if(commonProperties.getJwtExpirationMills() >= 0L){
			expirationMills = commonProperties.getJwtExpirationMills();
		}
		if (ValidateUtilExt.isNotNullOrEmpty(commonProperties.getJwtIssuer())){
			issuer = commonProperties.getJwtIssuer();
		}

		//兜底
		if (ValidateUtilExt.isNullOrEmpty(mapper)){
			mapper = new ObjectMapper();
		}
		//生成JWT，并存入Header
		WebCommonUtils.jwtBuilder(
				response,
				authentication,
				authorities,
				issuer,
				expirationMills,
				signingKey,
				mapper
		);
	}




}
