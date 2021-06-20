/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.core.impl;

import com.lepdou.framework.emw.config.bean.ConfigDO;
import com.lepdou.framework.emw.config.core.ConfigDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author lepdou
 * @version : DefaultConfigDAO.java, v 0.1 2021年06月10日 下午8:45 lepdou Exp $
 */
public class DefaultConfigDAO implements ConfigDAO {
    private static final Logger logger = LoggerFactory.getLogger(DefaultConfigDAO.class);

    private static final String SQL_FIND_BY_NAMESPACE_AND_PROFILE
                                                  = "select * from emw_config where app_id = ? and namespace = ? and profile = ?";
    private static final String SQL_SAVE_CONFIG   = "INSERT INTO `emw_config` (`app_id`, `namespace`, `profile`, `value`, "
            + "`version`, `context`, `grayRules`, `gmt_create`, `gmt_modified`, `operator`) VALUES (?, ?, ?, ?, 1, ?, ?, NOW(), NOW(), ?)";
    private static final String SQL_UPDATE_CONFIG = "update emw_config set `value` = ?,version =version+1 where "
            + "app_id = ? and namespace = ? and profile=?";

    private Connection conn;
    private String     jdbcUrl;
    private String     username;
    private String     password;

    public DefaultConfigDAO(String url, String username, String password) {
        this.jdbcUrl = url;
        this.username = username;
        this.password = password;

        try {
            //1. 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");

            logger.info("Load jdbc driver success.");

            checkAndInitConn();
        } catch (Exception e) {
            logger.error("Connection to database failed. Please check database params. url = {}, username = {}, password = {}", url,
                    username, password, e);
        }

    }

    @Override
    public ConfigDO findByNamespaceAndProfile(String appId, String namespace, String profile) {
        if (profile == null) {
            profile = "";
        }

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(SQL_FIND_BY_NAMESPACE_AND_PROFILE);
            st.setString(1, appId);
            st.setString(2, namespace);
            st.setString(3, profile);

            ResultSet resultSet = st.executeQuery();

            if (resultSet.first()) {
                return parseResultSetToConfigDO(resultSet);
            }

        } catch (SQLException e) {
            logger.error("Query namespace from db failed. namespace = {}", namespace, e);
        } finally {
            if (st != null) {

                try {
                    st.close();
                } catch (SQLException e) {
                    logger.error("Close connection failed. namespace = {}", namespace, e);
                }
            }
        }
        return null;
    }

    private ConfigDO parseResultSetToConfigDO(ResultSet rs) {
        if (rs == null) {
            return null;
        }

        ConfigDO configDO = new ConfigDO();

        try {
            configDO.setId(rs.getLong(ConfigDO.FIELD_ID));
            configDO.setAppId(rs.getString(ConfigDO.FIELD_APP_ID));
            configDO.setNamespace(rs.getString(ConfigDO.FIELD_NAMESPACE));
            configDO.setProfile(rs.getString(ConfigDO.FIELD_PROFILE));
            configDO.setValue(rs.getString(ConfigDO.FIELD_VALUE));
            configDO.setVersion(rs.getInt(ConfigDO.FIELD_VERSION));
            configDO.setContext(rs.getString(ConfigDO.FIELD_CONTEXT));
            configDO.setGmtCreateTime(rs.getDate(ConfigDO.FIELD_CREATE_TIME));
            configDO.setGmtLastModifiedTime(rs.getDate(ConfigDO.FIELD_MODIFIED_TIME));
            configDO.setOperator(rs.getString(ConfigDO.FIELD_OPERATOR));
            configDO.setGrayRules(rs.getString(ConfigDO.FIELD_GRAY_RULES));
            return configDO;
        } catch (SQLException e) {
            logger.error("Parse result set to config do failed. ", e);
        }

        return null;
    }

    @Override
    public List<ConfigDO> findAll() {
        return null;
    }

    @Override
    public ConfigDO save(ConfigDO configDO) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(SQL_SAVE_CONFIG);
            st.setString(1, configDO.getAppId());
            st.setString(2, configDO.getNamespace());
            st.setString(3, configDO.getProfile());
            st.setString(4, configDO.getValue());
            st.setString(5, configDO.getContext());
            st.setString(6, configDO.getGrayRules());
            st.setString(7, configDO.getOperator());

            st.execute();

            ConfigDO savedConfig = findByNamespaceAndProfile(configDO.getAppId(), configDO.getNamespace(), configDO.getProfile());

            logger.info("Save emw config success. config = {}", savedConfig);

            return savedConfig;
        } catch (SQLException e) {
            logger.error("Save emw config failed. config = {}", configDO, e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    logger.error("Close connection failed. config = {}", configDO, e);
                }
            }
        }

        return null;
    }

    @Override
    public ConfigDO update(ConfigDO configDO) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(SQL_UPDATE_CONFIG);
            st.setString(1, configDO.getValue());
            st.setString(2, configDO.getAppId());
            st.setString(3, configDO.getNamespace());
            st.setString(4, configDO.getProfile());

            st.executeUpdate();

            ConfigDO updatedConfig = findByNamespaceAndProfile(configDO.getAppId(), configDO.getNamespace(), configDO.getProfile());

            logger.info("Update emw config success. config = {}", updatedConfig);

            return updatedConfig;
        } catch (SQLException e) {
            logger.error("Update emw config failed. config = {}", configDO, e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    logger.error("Close connection failed. config = {}", configDO, e);
                }
            }
        }
        return null;
    }

    private void checkAndInitConn() {
        if (conn == null) {
            createConn();
            return;
        }

        try {
            if (conn.isClosed()) {
                createConn();
            }
        } catch (SQLException e) {
            logger.error("Check connection failed.", e);
        }
    }

    private void createConn() {
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            logger.error("Connection to database failed. Please check database params. url = {}, username = {}, password = {}", jdbcUrl,
                    username, password, e);
        }
        logger.info("Create connection with database success.");
    }

}