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

    public static String genNamespaceUniqueKey(String namespace, String profile) {
        if (StringUtils.isEmpty(profile)) {
            return namespace;
        }

        return namespace + "+" + profile;
    }

    public static String[] parseFromNamespaceUniqueKey(String namespaceUniqueKey) {
        String[] result = namespaceUniqueKey.split(SEPARATOR);
        String namespace = result[0];
        String profile = "";

        if (result.length == 2) {
            profile = result[1];
        }

        return new String[] {namespace, profile};
    }
}