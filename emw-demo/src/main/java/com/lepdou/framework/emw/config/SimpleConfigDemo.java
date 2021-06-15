package com.lepdou.framework.emw.config;

import com.google.common.base.Charsets;
import com.lepdou.framework.emw.config.apollo.Config;
import com.lepdou.framework.emw.config.apollo.ConfigChangeListener;
import com.lepdou.framework.emw.config.apollo.ConfigService;
import com.lepdou.framework.emw.config.apollo.model.ConfigChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleConfigDemo {
    private static final Logger logger = LoggerFactory.getLogger(SimpleConfigDemo.class);
    private              Config config;

    public SimpleConfigDemo() {
        config = ConfigService.getAppConfig();

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
        String jdbcUrl = "jdbc:mysql://localhost:3306/emw";
        String username = "root";
        String password = "admin";

        EMWConfigStarter.run(EMWConfigParams.builder().jdbcUrl(jdbcUrl).username(username).password(password).build());

        SimpleConfigDemo apolloConfigDemo = new SimpleConfigDemo();

        System.out.println(
                "Apollo Config Demo. Please input key to get the value. Input quit to exit.");

        while (true) {
            System.out.print("> ");
            String input = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
            if (input == null || input.length() == 0) {
                continue;
            }
            input = input.trim();
            if (input.equalsIgnoreCase("quit")) {
                System.exit(0);
            }
            logger.info(apolloConfigDemo.getConfig(input));
        }
    }
}
