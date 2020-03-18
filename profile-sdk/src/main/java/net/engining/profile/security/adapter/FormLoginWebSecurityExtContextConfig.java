package net.engining.profile.security.adapter;

import net.engining.gm.config.props.GmCommonProperties;
import net.engining.profile.config.adapter.ParentWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 用于支持FormLogin登录方式的安全控制，主要用于前后分离架构，使用无session的模式；
 *
 * @author : Eric Lu
 * @version : 4.0.3
 * @date : 2020-03-17 11:25
 * @since : 4.0.3
 **/
@Configuration
public class FormLoginWebSecurityExtContextConfig extends ParentWebSecurityConfigurerAdapter {

	@Autowired
	AuthenticationSuccessHandler jsonAuthSuccessHandler;

	@Autowired
	AuthenticationFailureHandler jsonAuthFailureHandler;

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
