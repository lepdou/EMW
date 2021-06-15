package com.lepdou.framework.emw.config.spring.javaConfigDemo;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.lepdou.framework.emw.config.spring.common.bean.AnnotatedBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class AnnotationApplication {
  
  public static void main(String[] args) throws IOException {
    ApplicationContext context = new AnnotationConfigApplicationContext("com.lepdou.framework.emw.config.spring.common");
    AnnotatedBean annotatedBean = context.getBean(AnnotatedBean.class);

    System.out.println("AnnotationApplication Demo. Input any key except quit to print the values. Input quit to exit.");
    while (true) {
      System.out.print("> ");
      String input = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
      if (!Strings.isNullOrEmpty(input) && input.trim().equalsIgnoreCase("quit")) {
        System.exit(0);
      }

      System.out.println(annotatedBean.toString());
    }
  }
}
