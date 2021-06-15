/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author lepdou
 * @version : EMWConfigUtils.java, v 0.1 2021年06月15日 下午8:22 lepdou Exp $
 */
public class EMWConfigUtils {
    private static final Logger logger = LoggerFactory.getLogger(EMWConfigUtils.class);

    public static Properties parseToProperties(String value) {
        Properties properties = new Properties();

        if (StringUtils.isEmpty(value)) {
            return properties;
        }

        try {
            properties.load(new ByteArrayInputStream(value.getBytes()));
        } catch (IOException e) {
            logger.error("Parse value to properties failed. value = {}", value, e);
        }

        return properties;
    }
}