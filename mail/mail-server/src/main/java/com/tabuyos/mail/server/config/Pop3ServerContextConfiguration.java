/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.pop3server.netty.POP3ServerFactory;
import org.apache.james.protocols.lib.handler.ProtocolHandlerLoader;
import org.springframework.context.annotation.Bean;

/**
 * Pop3ServerContextConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class Pop3ServerContextConfiguration {

  @Bean
  public POP3ServerFactory pop3ServerFactory(ProtocolHandlerLoader handlerLoader, FileSystem fileSystem) {
    POP3ServerFactory factory = new POP3ServerFactory();
    factory.setFileSystem(fileSystem);
    factory.setProtocolHandlerLoader(handlerLoader);
    return factory;
  }
}
