/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.hikaricp.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * HikaricpSpringbootApplication
 *
 * @author tabuyos
 * @since 2022/1/24
 */
@SpringBootApplication
public class HikaricpSpringbootApplication implements CommandLineRunner {

  private final DataSource dataSource;

  public HikaricpSpringbootApplication(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public static void main(String[] args) {
    SpringApplication.run(HikaricpSpringbootApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println(dataSource.getClass());
    Connection conn = dataSource.getConnection();
    conn.close();
  }
}
