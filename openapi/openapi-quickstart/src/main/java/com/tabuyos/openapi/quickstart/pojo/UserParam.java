/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.openapi.quickstart.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Set;
import java.util.StringJoiner;

/**
 * UserParam
 *
 * @author tabuyos
 * @since 2022/2/15
 */
@Schema(description = "用户参数实体")
public class UserParam {

  @NotBlank(message = "用户名不能为空")
  @Schema(description = "用户名")
  private String userName;

  @Schema(name = "roles", description = "角色id列表")
  @NotNull(message = "角色id列表不能为空")
  @Size(min = 1)
  private Set<String> roleList;

  @NotBlank(message = "密码不能为空")
  @Schema(description = "密码，6-18位，包含大小写、数字及特殊字符")
  @Size(min = 6, max = 18)
  private String password;

  @Schema(example = "zhangsan@lanweihong.com", description = "邮箱")
  private String email;

  @Schema(description = "年龄")
  @Min(value = 1, message = "最小年龄为1")
  @Max(value = 150, message = "最大年龄为150")
  private Integer age;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Set<String> getRoleList() {
    return roleList;
  }

  public void setRoleList(Set<String> roleList) {
    this.roleList = roleList;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", UserParam.class.getSimpleName() + "(", ")")
      .add("userName='" + userName + "'")
      .add("roleList=" + roleList)
      .add("password='" + password + "'")
      .add("email='" + email + "'")
      .add("age=" + age)
      .toString();
  }
}
