package net.engining.profile.config.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 因为此配置必然需要控制端点层的权限，所以需要使用@EnableGlobalMethodSecurity 开启支持方法注解的权限控制,
 * 等同于<security:global-method-security pre-post-annotations="enabled" mode="aspectj"/>：
 * <br>
 * "@PreAuthorize" 该注解用来确定一个方法是否应该被执行。该注解后面跟着的是一个表达式，如果表达式的值为真，则该方法会被执行。
 * 如 @PreAuthorize("hasRole('ROLE_USER')")就说明只有当前用户具有角色 ROLE_USER的时候才会执行。
 * <br>
 * "@PostAuthorize" 该注解用来在方法执行完之后进行访问控制检查。
 * <br>
 * "@PostFilter" 该注解用来对方法的返回结果进行过滤。从返回的集合中过滤掉表达式值为假的元素。
 * 如@PostFilter("hasPermission(filterObject, 'read')")说明返回的结果中只保留当前用户有读权限的元素。
 * <br>
 * "@PreFilter" 该注解用来对方法调用时的参数进行过滤。
 * <br>
 * spring boot 2.x后没有ACCESS_OVERRIDE_ORDER了，用BASIC_AUTH_ORDER-1等同于原来的ACCESS_OVERRIDE_ORDER
 *
 * @author : Eric Lu
 * @version : 4.0.3
 * @date : 2020-03-17 11:25
 * @since : 4.0.3
 */
@Order(SecurityProperties.BASIC_AUTH_ORDER-1)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, mode = AdviceMode.ASPECTJ)
public abstract class ParentWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsService profileUserDetailsService;

    /**
     * 配置authenticationManager
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件
        auth.userDetailsService(profileUserDetailsService).passwordEncoder(passwordEncoder);
        auth.eraseCredentials(true);
    }

}
