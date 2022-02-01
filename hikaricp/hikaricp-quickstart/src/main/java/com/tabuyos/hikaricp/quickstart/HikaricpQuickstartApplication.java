/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.hikaricp.quickstart;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * HikaricpQuickstartApplication
 *
 * @author tabuyos
 * @since 2022/1/24
 */
public class HikaricpQuickstartApplication {

  public static void main(String[] args) throws SQLException {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/tabuyos?useUnicode=true&characterEncoding=utf8");
    dataSource.setUsername("tabuyos");
    dataSource.setPassword("tabuyos");
    dataSource.setMinimumIdle(5);
    dataSource.setMaximumPoolSize(15);
    dataSource.setAutoCommit(true);
    dataSource.setIdleTimeout(30_000);
    dataSource.setMaxLifetime(1800_000);
    dataSource.setConnectionTimeout(30_000);
    dataSource.setConnectionTestQuery("select 1");

    Connection connection = dataSource.getConnection();
    PreparedStatement preparedStatement = connection.prepareStatement("select * from user_account");
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.println(resultSet.getString("username"));
    }
    resultSet.close();
    preparedStatement.close();
    connection.close();
  }
}
