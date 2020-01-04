package net.engining.profile.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 系统初始化项目的启动类-示例
 * @author 作者
 * @version 版本
 * @since
 * @date 2019/8/14 10:20
 */
@SpringBootApplication
public class InitApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(InitApplication.class, args).close();
	}
	
}
