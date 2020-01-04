package net.engining.profile.init;

import com.google.common.collect.Lists;
import net.engining.pg.support.core.context.ApplicationContextHolder;
import net.engining.pg.support.init.ParameterInitializer;
import net.engining.pg.support.init.TableDataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

/**
 * Spring Boot应用程序在启动后，会遍历CommandLineRunner接口的实例并运行它们的run方法;
 * 该方法在SpringApplication.run之时，在ApplicationContext容器加载完成后被调用;
 * 也可以利用@Order注解（或者实现Order接口）来规定所有CommandLineRunner实例的运行顺序;
 * 区别于{@link ApplicationRunner}，接收{@link ApplicationArguments}作为参数，接收简单的String参数;
 * @author 作者
 * @version 版本
 * @since
 * @date 2019/8/14 10:20
 */
@Component
public class SystemInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SystemInitializer.class);

	/**
	 * 初始化
	 * @throws Exception
	 */
	private void init() throws Exception {

		// 执行所有参数初始化器
		log.info("执行所有参数初始化器");
		Map<String, ParameterInitializer> parameterInitializers = ApplicationContextHolder
				.getBeansOfType(ParameterInitializer.class);
		for (ParameterInitializer init : parameterInitializers.values()) {
			init.init();
		}

		// 执行所有数据初始化器
		log.info("执行所有数据初始化器");
		Map<String, TableDataInitializer> tableDataInitializer = ApplicationContextHolder
				.getBeansOfType(TableDataInitializer.class);
		for (TableDataInitializer init : tableDataInitializer.values()) {
			init.init();
		}

	}

	/**
	 * 运行
	 * @param args 参数
	 * @throws Exception 异常
	 */
	@Override
	public void run(String... args) throws Exception {
		if (args.length < 1) {
			System.err.println("请指定 init 或  patch 参数");
			return;
		}

		ArrayList<String> argsLs = Lists.newArrayList(args);

		for(String item : argsLs){
			log.debug(String.format("传入系统参数:%s", item));
			if("init".equals(item)){
				init();
				break;
			}
		}

	}

}
