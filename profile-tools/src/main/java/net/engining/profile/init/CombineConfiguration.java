package net.engining.profile.init;

import net.engining.gm.config.GeneralContextConfig;
import net.engining.gm.config.JPAContextConfig;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.param.props.PgParamAndCacheProperties;
import net.engining.pg.props.CommonProperties;
import net.engining.profile.config.props.ProfileParamProperties;
import net.engining.profile.init.data.security.AdministrationInit;
import net.engining.profile.init.param.security.SecurityControlInit;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 这个类用来组装需要的配置，根据不同的项目组装需要的配置项 <br>
 * 使用@Import用来导入@Configuration注解的config类(也可以通过@SpringBootApplication指定scanBasePackages来扫描@Configuration) <br>
 * 使用@ImportResource用来加载传统的xml配置
 * @author 作者
 * @version 版本
 * @since
 * @date 2019/8/14 10:20
 */
@Configuration
@EnableConfigurationProperties(value = {
		CommonProperties.class,
		PgParamAndCacheProperties.class,
		GmCommonProperties.class,
		ProfileParamProperties.class
		})
@Import(value = {
		GeneralContextConfig.class,
		JPAContextConfig.class,
		AdministrationInit.class,
		SecurityControlInit.class
		})
@EntityScan(basePackages = {
		"net.engining.pg.parameter.entity",
		"net.engining.profile.entity",
		})
public class CombineConfiguration {

	@Bean
	public PasswordEncoder passwordEncoder(GmCommonProperties gmCommonProperties) {

		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
