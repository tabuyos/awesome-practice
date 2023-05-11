/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.quartz.quickstart.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * QuartzQuickstartApplication
 *
 * @author tabuyos
 * @since 2022/6/29
 */
@SpringBootApplication(scanBasePackages = {"com.tabuyos.quartz"})
public class QuartzQuickstartApplication {
  public static void main(String[] args) {
    SpringApplication.run(QuartzQuickstartApplication.class, args);
  }
}
