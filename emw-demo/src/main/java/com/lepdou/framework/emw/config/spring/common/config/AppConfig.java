package com.lepdou.framework.emw.config.spring.common.config;

import com.lepdou.framework.emw.config.apollo.spring.annotation.EnableApolloConfig;
import com.lepdou.framework.emw.config.spring.EnableEMWConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@Configuration
@EnableApolloConfig(value = "application", order = 10)
@EnableEMWConfig(jdbcUrl = "jdbc:mysql://localhost:3306/emw", username = "root", password = "admin")
public class AppConfig {
}
