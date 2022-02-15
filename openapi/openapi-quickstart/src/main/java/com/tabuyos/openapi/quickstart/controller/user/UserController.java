/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.openapi.quickstart.controller.user;

import com.tabuyos.openapi.quickstart.pojo.UserParam;
import com.tabuyos.openapi.quickstart.vo.JsonResult;
import com.tabuyos.openapi.quickstart.vo.User;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * UserController
 *
 * @author tabuyos
 * @since 2022/2/15
 */
@Tag(name = "用户管理", description = "用户数据增删改查")
@RestController
@RequestMapping("/api/users")
public class UserController {

  @Operation(summary = "查询用户列表", description = "返回所有用户数据")
  @GetMapping("")
  public JsonResult<List<User>> getUserList(@Parameter(description = "用户名") @RequestParam(value = "username", required = false) String userName) {
    List<User> result = new ArrayList<>();
    // TODO
    return JsonResult.ok(result);
  }

  @Operation(summary = "通过用户名查询用户", description = "根据用户名查询用户详细信息")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "请求成功"),
    @ApiResponse(responseCode = "404", description = "用户不存在", content = @Content)
  })
  @GetMapping("/{username}")
  public JsonResult<User> getUserByName(@Parameter(description = "用户名", required = true) @PathVariable("username") String userName) {
    // TODO
    return JsonResult.ok();
  }

  @Operation(summary = "新增用户")
  @PostMapping("")
  public JsonResult<User> addUser(@Parameter(required = true) @Valid @RequestBody UserParam param) {
    // TODO
    return JsonResult.ok();
  }

  @Operation(summary = "修改用户")
  @PutMapping("")
  public JsonResult<User> updateUser(@Parameter(description = "用户参数", required = true) @Valid @RequestBody UserParam param) {
    // TODO
    return JsonResult.ok();
  }

  @Operation(summary = "删除用户")
  @DeleteMapping("/{username}")
  public JsonResult<User> deleteUserByName(@Parameter(name = "username", in = ParameterIn.PATH, description = "用户名", required = true) @PathVariable("username") String userName) {
    // TODO
    return JsonResult.ok();
  }

  @Operation(summary = "测试新增用户接口", hidden = true)
  @PostMapping("/test")
  @Hidden
  public JsonResult<User> testAddUser(@Parameter(required = true) @Valid @RequestBody UserParam param) {
    // TODO
    return JsonResult.ok();
  }
}
