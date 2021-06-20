/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.facade;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.lepdou.framework.emw.config.bean.ConfigDO;
import com.lepdou.framework.emw.config.core.ConfigDAO;
import com.lepdou.framework.emw.config.core.EMWConfigException;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 提供后端管理能力接口
 *
 * @author lepdou
 * @version : EMWConfigManagerFacade.java, v 0.1 2021年06月11日 上午10:48 lepdou Exp $
 */
public class EMWConfigManagerFacade {
    private static ConfigDAO configDAO;

    public static void setConfigDAO(ConfigDAO configDAO) {
        EMWConfigManagerFacade.configDAO = configDAO;
    }

    public static ConfigDO createOrUpdateNamespace(ConfigDO config) {
        checkConfigField(config);

        if (StringUtils.isEmpty(config.getProfile())) {
            config.setProfile(ConfigConsts.CLUSTER_NAME_DEFAULT);
        }

        ConfigDO managedConfigDO = configDAO.findByNamespaceAndProfile(config.getAppId(), config.getNamespace(), config.getProfile());

        if (managedConfigDO == null) {
            return configDAO.save(config);
        }

        return configDAO.update(config);
    }

    public static ConfigDO findByNamespace(String appId, String namespace, String profile) {
        return configDAO.findByNamespaceAndProfile(appId, namespace, profile);
    }

    public static List<ConfigDO> findAll() {
        return null;
    }

    private static void checkConfigField(ConfigDO config) {
        if (StringUtils.isEmpty(config.getAppId())) {
            throw new EMWConfigException("app id can not be empty.");
        }

        if (StringUtils.isEmpty(config.getNamespace())) {
            throw new EMWConfigException("namespace can not be empty.");
        }
    }
}