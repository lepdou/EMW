package com.lepdou.framework.emw.config.spring.javaConfigDemo.config;

import com.lepdou.framework.emw.config.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@Configuration
//注入TEST1.apollo 和 application.yaml
@EnableApolloConfig(value = {"TEST1.apollo", "application.yaml"}, order = 11)
public class AnotherAppConfig {
}
