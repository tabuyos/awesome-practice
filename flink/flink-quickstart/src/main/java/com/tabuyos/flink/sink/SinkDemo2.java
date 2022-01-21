/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.flink.sink;

import com.tabuyos.flink.source.SourceDemo5;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.StringJoiner;

/**
 * SinkDemo2
 *
 * @author tabuyos
 * @since 2022/1/19
 */
public class SinkDemo2 {


  public static void main(String[] args) throws Exception {
    //1:env
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
    //2:source
    DataStreamSource<Student> studentDS = env.fromElements(new Student(null, "tony", "email"));
    //3:transformation
    //4:sink
    studentDS.addSink(new RichSinkFunction<Student>() {
      private Connection conn;
      private PreparedStatement ps;

      @Override
      public void open(Configuration parameters) throws Exception {
        conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/tabuyos", "tabuyos", "tabuyos");
        ps = conn.prepareStatement("select id,username,email from user_account");
      }

      //执行插入操作
      @Override
      public void invoke(Student value, Context context) throws Exception {
        //设置？占位符
        // ...
        //执行
        ps.executeQuery();
      }

      //关闭连接
      @Override
      public void close() throws Exception {
        if (conn != null) {
          conn.close();
        }
        if (ps != null) {
          ps.close();
        }
      }
    });
    //5：execution
    env.execute();
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
      return new StringJoiner(", ", SourceDemo5.Student.class.getSimpleName() + "(", ")")
        .add("id=" + id)
        .add("username='" + username + "'")
        .add("email='" + email + "'")
        .toString();
    }
  }
}
