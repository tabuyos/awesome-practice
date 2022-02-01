/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.managesieve.api.SieveParser;
import org.apache.james.managesieve.jsieve.Parser;
import org.apache.james.managesieveserver.netty.ManageSieveServerFactory;
import org.apache.james.sieverepository.api.SieveRepository;
import org.apache.james.user.api.UsersRepository;
import org.springframework.context.annotation.Bean;

/**
 * ManageSieveServerContextConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class ManageSieveServerContextConfiguration {

  @Bean
  public ManageSieveServerFactory manageSieveServerFactory(FileSystem fileSystem,
                                                           SieveRepository sieveRepository,
                                                           UsersRepository usersRepository,
                                                           Parser parser) {
    ManageSieveServerFactory factory = new ManageSieveServerFactory();
    factory.setFileSystem(fileSystem);
    factory.setSieveRepository(sieveRepository);
    factory.setUsersRepository(usersRepository);
    factory.setParser(parser);
    return factory;
  }
}
