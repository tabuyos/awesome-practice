/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;

import java.io.Serializable;
import java.util.List;

/**
 * DysqlMain
 *
 * @author tabuyos
 * @since 2023/5/31
 */
@SuppressWarnings({"DataFlowIssue", "ConstantConditions"})
public class DysqlMain {

  static class User implements Serializable {
    private String name;
    private Integer age;
    private Integer gender;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }

    public Integer getGender() {
      return gender;
    }

    public void setGender(Integer gender) {
      this.gender = gender;
    }
  }

  static class Role implements Serializable {
    private String name;
    private Integer age;
    private Integer gender;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }

    public Integer getGender() {
      return gender;
    }

    public void setGender(Integer gender) {
      this.gender = gender;
    }
  }

  public static void main(String[] args) throws NoSuchMethodException {

    SqlBuilder sb = null;

    sb.select(
        ctx -> {
          ctx.col("name");
          ctx.col(User::getName);
          ctx.col(User::getAge);
          ctx.col(Role::getGender);

          ctx.fun("length(code)");
          ctx.fun(() -> "length(%s)", User::getName);
          SFunction<User, ?> ss = User::getName;
          SFunction<Role, ?> ee = Role::getName;
          ctx.fun(() -> "length(%s, %s, %s)", () -> List.of(ss, ee));
          ctx.fun(() -> "length(%s, %s, %s)", User::getName, Role::getAge, Role::getGender);
        });
    sb.from(
        ctx -> {
          ctx.table("course");
          ctx.table(User.class);
          ctx.join("student", "course.sid = student.id");
          ctx.join(Role.class, Role::getName, "=", User::getName);
          ctx.leftJoin("", "");
          ctx.rightJoin("", "");
        });
    sb.where(
        ctx -> {
          ctx.cond(User::getName, "=", "name");
          ctx.cond(User::getAge, ">", 12);
          ctx.cond("student.score = ?", 12);
          ctx.and(
              () -> {
                // and
                ctx.cond("course.code in (?, ?)", "A1", "B2");
              });
          ctx.or(
              () -> {
                // or
                ctx.cond("course.code in (?, ?)", "A1", "B2");
              });
        });
  }
}
