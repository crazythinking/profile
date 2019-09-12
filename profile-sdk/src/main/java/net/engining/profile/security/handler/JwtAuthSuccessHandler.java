package net.engining.profile.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.WebCommonUtils;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.enums.OperationType;
import net.engining.profile.sdk.service.bean.ErrorCodeDef;
import net.engining.profile.security.ProfileSecurityLoggerService;
import net.engining.profile.security.ProfileUserDetails;
import org.apache.commons.lang3.StringUtils;
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
	private String signingKey = WebCommonUtils.SE_JWT_SIGNKEY;
	
	private long expirationMills = 30 * 60 * 1000;

	private String issuer = "PROFILE";
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication)throws IOException, ServletException {
		
		ProfileUserDetails profileUserDetails = (ProfileUserDetails)authentication.getPrincipal();
		//Collection 转为逗号分隔String
		String authorities = Joiner.on(",").join(authentication.getAuthorities());

		if (StatusDef.L.equals(profileUserDetails.getStatus())) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			mapper.writeValue(
					response.getOutputStream(),
					new CommonWithHeaderResponseBuilder<Void, Void>()
							.build()
							.setStatusCode(ErrorCodeDef.CheckErrorA.getValue())
							.setStatusDesc(ErrorCodeDef.CheckErrorA.getLabel())
			);
		}

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

		String token = Jwts.builder()
				//代表这个JWT的主体，即它的所有人
				.setSubject(authentication.getName())
				//代表这个JWT的签发时间
				.setIssuedAt(new Date())
				//代表这个JWT的签发主体
				.setIssuer(issuer)
				//代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的；
				.setNotBefore(new Date())
				//代表这个JWT的过期时间
				.setExpiration(new Date(System.currentTimeMillis() + expirationMills))
				//.claim("roles", roles)
				.claim("authorities", authorities)
				//采用HS512加签
				.signWith(SignatureAlgorithm.HS512, signingKey)
				.compact();
		response.addHeader("Authorization", "Bearer " + token);
		
		response.setStatus(HttpStatus.OK.value());
		
		mapper.writeValue(
				response.getOutputStream(),
				new CommonWithHeaderResponseBuilder<Void, Void>()
					.build()
		);
	}
	
	
}
