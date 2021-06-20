package com.lepdou.framework.emw.config.spring.javaConfigDemo;

import com.lepdou.framework.emw.config.spring.javaConfigDemo.bean.AnnotatedBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 *  基于 Java 注解的方式启动 emw-config 模块
 * @see com.lepdou.framework.emw.config.spring.javaConfigDemo.config.AppConfig
 */
public class AnnotationApplication {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.lepdou.framework.emw.config.spring.javaConfigDemo");
        AnnotatedBean annotatedBean = context.getBean(AnnotatedBean.class);

        while (true) {
            TimeUnit.SECONDS.sleep(3);
            System.err.println(annotatedBean.toString());
        }
    }
}
