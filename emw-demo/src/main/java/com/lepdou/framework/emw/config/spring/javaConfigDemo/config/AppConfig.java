package com.lepdou.framework.emw.config.spring.javaConfigDemo.config;

import com.lepdou.framework.emw.config.apollo.spring.annotation.EnableApolloConfig;
import com.lepdou.framework.emw.config.spring.EnableEMWConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author lepdou (lepdou@126.com)
 */
@Configuration
//注入 application namespace
@EnableApolloConfig(value = "application", order = 10)
//emw-config 启动参数
@EnableEMWConfig(jdbcUrl = "jdbc:mysql://localhost:3306/emw", username = "root", password = "admin")
public class AppConfig {
}
