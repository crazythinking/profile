package net.engining.profile.security.adapter;

import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.web.filter.JwtBasicAuthenticationFilter;
import net.engining.pg.web.filter.RESTfulUsernamePasswordAuthenticationFilter;
import net.engining.profile.config.adapter.ParentWebSecurityConfigurerAdapter;
import net.engining.profile.security.handler.JsonAuthFailureHandler;
import net.engining.profile.security.handler.JwtAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 使用SecurityProperties.BASIC_AUTH_ORDER-1，即比pring security默认的安全配置优先级高；
 * 用于支持JwtLogin登录方式的安全控制，主要用于前后分离架构，使用无session的模式；
 *
 * @author : Eric Lu
 * @version : 4.0.3
 * @date : 2020-03-17 11:25
 * @since : 4.0.3
 **/
public class JwtLoginWebSecurityExtContextConfig extends ParentWebSecurityConfigurerAdapter {

    @Autowired
    GmCommonProperties commonProperties;

    @Autowired
    JwtAuthSuccessHandler jwtAuthSuccessHandler;

    @Autowired
    JsonAuthFailureHandler jsonAuthFailureHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                //打开跨域请求支持，默认浏览器不能进行跨域请求；FIXME
                .cors().and()
                //关闭跨站请求伪造保护功能，FIXME
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //POST请求只放行“/login”
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .and()
                //登录
                .addFilter(
                        new RESTfulUsernamePasswordAuthenticationFilter(
                                authenticationManager(),
                                jwtAuthSuccessHandler,
                                jsonAuthFailureHandler
                        ))
                //验证是否登录
                .addFilter(
                        new JwtBasicAuthenticationFilter(
                                authenticationManager(),
                                commonProperties.getJwtSignKey()
                        ))
        ;
    }

}
