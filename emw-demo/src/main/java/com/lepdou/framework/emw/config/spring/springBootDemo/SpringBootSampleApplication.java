package com.lepdou.framework.emw.config.spring.springBootDemo;

import com.lepdou.framework.emw.config.spring.springBootDemo.config.AnnotatedBean;
import com.lepdou.framework.emw.config.spring.springBootDemo.config.SampleRedisConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * 通过 spring boot 的方式启动 emw-config
 *emw:
 *   emw-config:
 *     bootstrap:
 *       jdbc:
 *         url: jdbc:mysql://localhost:3306/emw
 *         username: root
 *         password: admin
 *
 * @author lepdou (lepdou@126.com)
 */
@SpringBootApplication(scanBasePackages = {"com.lepdou.framework.emw.config.spring.springBootDemo"})
public class SpringBootSampleApplication {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new SpringApplicationBuilder(SpringBootSampleApplication.class).run(args);

        AnnotatedBean annotatedBean = context.getBean(AnnotatedBean.class);
        SampleRedisConfig redisConfig = context.getBean(SampleRedisConfig.class);

        System.out.println("SpringBootSampleApplication Demo. Input any key except quit to print the values. Input quit to exit.");

        while (true) {
            TimeUnit.SECONDS.sleep(5);
            System.err.println(">>>>>>> annotated bean >>>>>>\n" + annotatedBean);
            System.err.println(">>>>>>> redis config bean >>>>>>\n" + redisConfig);
        }
    }
}
