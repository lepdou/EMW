package com.lepdou.framework.emw.config.spring.springBootDemo;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.lepdou.framework.emw.config.spring.common.bean.AnnotatedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@SpringBootApplication(scanBasePackages = {"com.lepdou.framework.emw.config.spring.common",
        "com.lepdou.framework.emw.config.spring.springBootDemo"
})
public class SpringBootSampleApplication {

    @Autowired
    private AnnotatedBean annotatedBean;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringBootSampleApplication.class, args);

        System.out.println("SpringBootSampleApplication Demo. Input any key except quit to print the values. Input quit to exit.");
        while (true) {
            System.out.print("> ");
            String input = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
            if (!Strings.isNullOrEmpty(input) && input.trim().equalsIgnoreCase("quit")) {
                System.exit(0);
            }

        }
    }
}
