/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.openapi.quickstart.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig
 *
 * @author tabuyos
 * @since 2022/2/15
 */
@OpenAPIDefinition(
  tags = {
    @Tag(name = "用户管理", description = "用户模块操作"),
    @Tag(name = "角色管理", description = "角色模块操作")
  },
  info = @Info(
    title = "用户接口 API 文档",
    description = "用户数据管理......",
    version = "1.0.0",
    contact = @Contact(name = "tabuyos", email = "tabuyos@outlook.com", url = "https://www.tabuyos.com"),
    license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
  ),
  servers = {
    @Server(description = "生产环境服务器", url = "https://xxxx.com/api/v1"),
    @Server(description = "测试环境服务器", url = "https://test.xxxx.com/api/v1")
  },
  security = @SecurityRequirement(name = "Oauth2"),
  externalDocs = @ExternalDocumentation(
    description = "项目编译部署说明",
    url = "http://localhost/deploy/README.md"
  )
)
@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi userApi() {
    return GroupedOpenApi.builder()
                         .group("User")
                         // 指定路径
                         .pathsToMatch("/api/users/**")
                         // 指定特定的 API 文档信息
                         .addOpenApiCustomiser(userApiCustomizer())
                         .build();
  }

  @Bean
  public GroupedOpenApi roleApi() {
    return GroupedOpenApi.builder()
                         .group("Role")
                         // 指定扫描包
                         .packagesToScan("com.tabuyos.openapi.quickstart.controller.role")
                         .pathsToMatch("/api/roles/**")
                         .build();
  }

  /**
   * 定义 OpenApiCustomizer ，用于指定的 group
   * 用于可参考上面写的 @OpenAPIDefinition 内容
   * @return OpenApiCustomiser
   */
  public OpenApiCustomiser userApiCustomizer() {
    return openApi -> openApi.info(new io.swagger.v3.oas.models.info.Info()
                                     .title("用户管理 API 文档")
                                     .description("实现对用户数据的增删改查等操作"));
  }
}
