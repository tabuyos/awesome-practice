/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.flink.source;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringJoiner;

/**
 * SourceDemo5
 *
 * @author tabuyos
 * @since 2022/1/18
 */
public class SourceDemo5 {

  public static void main(String[] args) throws Exception {
    //1：env
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
    //2：Source
    DataStreamSource<Student> ds = env.addSource(new MySQLSource()).setParallelism(1);
    //3:transformation
    //4:sink
    ds.print();
    //5:executor
    env.execute();
  }

  /**
   * 使用自定义数据源加载MySQL中的最新数据，每隔两秒加载一次 SourceFunction:非并行数据源(并行度只能=1) RichSourceFunction:多功能非并行数据源(并行度只能=1)
   * ParallelSourceFunction:并行数据源(并行度能够>=1) RichParallelSourceFunction:多功能并行数据源(并行度能够>=1)
   */
  public static class MySQLSource extends RichParallelSourceFunction<Student> {

    private Boolean flag = true;
    private Connection conn;
    private PreparedStatement ps;

    @Override
    public void open(Configuration parameters) throws Exception {
      conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/tabuyos", "tabuyos", "tabuyos");
      ps = conn.prepareStatement("select id,username,email from user_account");
    }

    //查询数据要一直执行，每隔2s查询一次最新数据
    @Override
    public void run(SourceContext<Student> sourceContext) throws Exception {
      while (flag) {
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
          int id = resultSet.getInt("id");
          String username = resultSet.getString("username");
          String email = resultSet.getString("email");
          sourceContext.collect(new Student(id, username, email));
        }
        Thread.sleep(2000);
      }
    }

    //执行cancel命令时执行
    @Override
    public void cancel() {
      flag = false;
    }

    //关闭连接的时候执行一次
    @Override
    public void close() throws Exception {
      super.close();
    }
  }

  public static class Student {

    private Integer id;
    private String username;
    private String email;

    public Student(Integer id, String username, String email) {
      this.id = id;
      this.username = username;
      this.email = email;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", Student.class.getSimpleName() + "(", ")")
        .add("id=" + id)
        .add("username='" + username + "'")
        .add("email='" + email + "'")
        .toString();
    }
  }
}
