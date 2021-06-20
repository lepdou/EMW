package com.lepdou.framework.emw.config.spring.xmlConfigDemo;

import com.lepdou.framework.emw.config.spring.xmlConfigDemo.bean.XmlBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * 通过 spring xml 的方式初始化 emw-config
 * @author lepdou(lepdou@126.com)
 */
public class XmlApplication {
    
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        XmlBean xmlBean = context.getBean(XmlBean.class);

        while (true) {
            TimeUnit.SECONDS.sleep(5);

            System.err.println(xmlBean.toString());
        }
    }
}
