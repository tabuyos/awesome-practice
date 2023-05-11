/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.quartz.quickstart.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * QuartzConfig
 *
 * @author tabuyos
 * @since 2022/6/29
 */
@Configuration
public class QuartzConfig {

  @Bean("oneDs")
  @Primary
  @ConfigurationProperties(prefix = "tabuyos.one.datasource")
  public DataSource oneDs() {
    System.out.println(1);
    return new HikariDataSource();
  }

  @Bean("twoDs")
  @ConfigurationProperties(prefix = "tabuyos.two.datasource")
  public DataSource twoDs() {
    System.out.println(2);
    return new HikariDataSource();
  }
}
