package net.engining.profile.security.adapter;

import net.engining.pg.web.security.DefaultJsonAuthSuccessHandler;
import net.engining.profile.config.adapter.ParentWebSecurityConfigurerAdapter;
import net.engining.profile.security.handler.JsonAuthFailureHandler;
import net.engining.profile.security.handler.JwtAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 使用SecurityProperties.BASIC_AUTH_ORDER-2，即比pring security默认的安全配置优先级高；
 * 用于支持FormLogin登录方式的安全控制，主要用于前后分离架构，使用无session的模式；
 *
 * @author : Eric Lu
 * @version : 4.0.3
 * @date : 2020-03-17 11:25
 * @since : 4.0.3
 **/
public class FormLoginWebSecurityExtContextConfig extends ParentWebSecurityConfigurerAdapter {

	@Autowired
	AuthenticationSuccessHandler jsonAuthSuccessHandler;

	@Autowired
	AuthenticationFailureHandler jsonAuthFailureHandler;

	@Bean
	public AuthenticationSuccessHandler jsonAuthSuccessHandler() {
		return new DefaultJsonAuthSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler jsonAuthFailureHandler() {
		return new JsonAuthFailureHandler();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/", "/home").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginProcessingUrl("/login").permitAll()
				.failureHandler(jsonAuthFailureHandler)
				.successHandler(jsonAuthSuccessHandler)
			.and()
			.logout()
				.logoutUrl("/logout").permitAll()
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
		;
	}

}
