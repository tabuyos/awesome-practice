/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.mailbox.store.JVMMailboxPathLocker;
import org.apache.james.mailbox.store.NoMailboxPathLocker;
import org.springframework.context.annotation.Bean;

/**
 * MailboxLockerConfiguration
 *
 * @author tabuyos
 * @since 2022/2/3
 */
public class MailboxLockerConfiguration {

  @Bean
  public JVMMailboxPathLocker jvmMailboxPathLocker() {
    return new JVMMailboxPathLocker();
  }

  @Bean
  public NoMailboxPathLocker noMailboxPathLocker() {
    return new NoMailboxPathLocker();
  }
}
