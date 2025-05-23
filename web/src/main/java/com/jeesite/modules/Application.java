/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import com.jeesite.common.config.Global;
import com.jeesite.common.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application
 * @author ThinkGem
 */
@SpringBootApplication
@EnableScheduling
public class Application extends SpringBootServletInitializer {

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   启动完成，访问地址：http://127.0.0.1:"
				+ Global.getProperty("server.port") + FileUtils.path("/"
				+ Global.getProperty("server.servlet.context-path"))
				+ "\r\n\r\n   默认管理账号： system   密码： admin"
				+ "\r\n\r\n==============================================================\r\n");
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(Application.class);
	}
	
}