/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.utils;

import org.springframework.util.StringUtils;

/**
 *
 * @author lepdou
 * @version : NamespaceKeyGen.java, v 0.1 2021年06月15日 下午7:46 lepdou Exp $
 */
public class NamespaceKeyUtils {
    private static final String SEPARATOR = "\\+";

    public static String genNamespaceUniqueKey(String appId, String namespace, String profile) {
        if (StringUtils.isEmpty(profile)) {
            return namespace;
        }

        return appId + "+" + namespace + "+" + profile;
    }

    public static String[] parseFromNamespaceUniqueKey(String namespaceUniqueKey) {
        String[] result = namespaceUniqueKey.split(SEPARATOR);

        String appId = result[0];
        String namespace = result[1];
        String profile = "";

        if (result.length == 3) {
            profile = result[2];
        }

        return new String[] {appId, namespace, profile};
    }
}