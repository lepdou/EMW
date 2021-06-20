package com.lepdou.framework.emw.config;

import com.google.common.base.Charsets;
import com.lepdou.framework.emw.config.apollo.Config;
import com.lepdou.framework.emw.config.apollo.ConfigChangeListener;
import com.lepdou.framework.emw.config.apollo.ConfigService;
import com.lepdou.framework.emw.config.apollo.model.ConfigChange;
import com.lepdou.framework.emw.config.starter.EMWConfigParams;
import com.lepdou.framework.emw.config.starter.EMWConfigStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleConfigDemo {
    private static final Logger logger = LoggerFactory.getLogger(SimpleConfigDemo.class);
    private              Config config;

    public SimpleConfigDemo() {
        //获取 application namespace 的配置
        config = ConfigService.getAppConfig();

        //增加监听器，当配置变更的时候触发回调事件
        ConfigChangeListener changeListener = changeEvent -> {

            logger.info("Changes for namespace {}", changeEvent.getNamespace());

            for (String key : changeEvent.changedKeys()) {
                ConfigChange change = changeEvent.getChange(key);
                logger.info("Change - key: {}, oldValue: {}, newValue: {}, changeType: {}",
                        change.getPropertyName(), change.getOldValue(), change.getNewValue(),
                        change.getChangeType());
            }
        };

        config.addChangeListener(changeListener);
    }

    private String getConfig(String key) {
        String DEFAULT_VALUE = "undefined";
        String result = config.getProperty(key, DEFAULT_VALUE);
        logger.info(String.format("Loading key : %s with value: %s", key, result));
        return result;
    }

    public static void main(String[] args) throws IOException {
        //1. 启动 ewm-config 模块
        String jdbcUrl = "jdbc:mysql://localhost:3306/emw";
        String username = "root";
        String password = "admin";

        EMWConfigStarter.run(EMWConfigParams.createFromBaseParams(jdbcUrl, username, password));

        SimpleConfigDemo apolloConfigDemo = new SimpleConfigDemo();

        System.out.println(
                "Apollo Config Demo. Please input key to get the value. Input quit to exit.");

        //2. 获取配置
        while (true) {
            System.out.print("> ");

            String key = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
            if (key == null || key.length() == 0) {
                continue;
            }
            key = key.trim();
            if (key.equalsIgnoreCase("quit")) {
                System.exit(0);
            }

            logger.info(apolloConfigDemo.getConfig(key));
        }
    }
}
