/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.spring;

import com.lepdou.framework.emw.config.starter.EMWConfigSpringStarter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author lepdou
 * @version : EnableEMWConfig.java, v 0.1 2021年06月16日 下午8:24 lepdou Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(EMWConfigSpringStarter.class)
public @interface EnableEMWConfig {

    /**
     * database connection url.
     * for example: jdbc:mysql://localhost:3306/emw
     */
    String jdbcUrl();

    /**
     * database username.
     */
    String username();

    /**
     * database password.
     */
    String password();

    /**
     * log sample rate for polling db config.
     */
    String pollingDBLogSampleRate() default "";

    /**
     * the interval of polling db config.
     */
    String pollingInterval() default "";

    /**
     * the port for rest server.
     */
    String restPort() default "";

}