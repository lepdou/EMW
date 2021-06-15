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

    private static final String SQL_FIND_BY_NAMESPACE_AND_PROFILE = "select * from emw_config where namespace = ? and profile = ?";

    private Connection conn;

    public DefaultConfigDAO(String url, String username, String password) {

        try {
            //1. 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");

            logger.info("Load jdbc driver success.");

            //2.获取与数据库的链接
            conn = DriverManager.getConnection(url, username, password);

            logger.info("Create connection with database success.");
        } catch (Exception e) {
            logger.error("Connection to database failed. Please check database params. url = {}, username = {}, password = {}", url,
                    username, password, e);
        }

    }

    @Override
    public ConfigDO findByNamespaceAndProfile(String namespace, String profile) {
        if (profile == null) {
            profile = "";
        }

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(SQL_FIND_BY_NAMESPACE_AND_PROFILE);
            st.setString(1, namespace);
            st.setString(2, profile);

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
        return null;
    }

    @Override
    public ConfigDO update(ConfigDO configDO) {
        return null;
    }

}