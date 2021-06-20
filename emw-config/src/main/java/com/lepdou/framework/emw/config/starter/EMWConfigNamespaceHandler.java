package com.lepdou.framework.emw.config.starter;

import com.lepdou.framework.emw.config.apollo.spring.config.ConfigPropertySourcesProcessor;
import com.lepdou.framework.emw.config.spring.EMWConfigConstants;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;

/**
 * XML 方式启动入口。
 *
 <?xml version="1.0" encoding="UTF-8"?>
 <beans xmlns="http://www.springframework.org/schema/beans"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns:context="http://www.springframework.org/schema/context"
 xmlns:apollo="http://www.ctrip.com/schema/apollo"
 xmlns:emwconfig="http://www.lepdou.org/schema/emwconfig"
 xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
 http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
 http://www.ctrip.com/schema/apollo http://www.ctrip.com/schema/apollo.xsd
 http://www.lepdou.org/schema/emwconfig http://www.lepdou.org/schema/emwconfig.xsd">

 <!--    初始化 emwconfig 模块-->
 <emwconfig:starter jdbcUrl="jdbc:mysql://localhost:3306/emw" username="root" password="admin"/>
 <!-- 注入配置 -->
 <apollo:config order="10"/>

 <context:annotation-config/>
 </beans>
 * @author lepdou
 * @version : EMWConfigContextInitializer.java, v 0.1 2021年06月16日 下午7:35 lepdou Exp $
 */
public class EMWConfigNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("starter", new BeanParser());
    }

    static class BeanParser extends AbstractSingleBeanDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return ConfigPropertySourcesProcessor.class;
        }

        @Override
        protected boolean shouldGenerateId() {
            return true;
        }

        @Override
        protected void doParse(Element element, BeanDefinitionBuilder builder) {
            String jdbcUrl = element.getAttribute(EMWConfigConstants.EMW_CONFIG_PARAM_JDBC_URL);
            String username = element.getAttribute(EMWConfigConstants.EMW_CONFIG_PARAM_JDBC_USERNAME);
            String password = element.getAttribute(EMWConfigConstants.EMW_CONFIG_PARAM_JDBC_PASSWORD);
            String pollingInterval = element.getAttribute(EMWConfigConstants.EMW_CONFIG_PARAM_POLLING_INTERVAL);
            String pollingDBLogSampleRate = element.getAttribute(EMWConfigConstants.EMW_CONFIG_PARAM_POLLING_DB_LOG_RATE);
            String restPort = element.getAttribute(EMWConfigConstants.EMW_CONFIG_PARAM_REST_PORT);

            EMWConfigParams params = EMWConfigParams.createFromOptionalStringParams(jdbcUrl, username, password, pollingInterval,
                    pollingDBLogSampleRate, restPort);

            EMWConfigStarter.run(params);
        }
    }
}
