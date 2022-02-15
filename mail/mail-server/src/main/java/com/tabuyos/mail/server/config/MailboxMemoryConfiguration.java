/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.events.EventBus;
import org.apache.james.mailbox.MailboxPathLocker;
import org.apache.james.mailbox.SessionProvider;
import org.apache.james.mailbox.inmemory.InMemoryId;
import org.apache.james.mailbox.inmemory.InMemoryMailboxManager;
import org.apache.james.mailbox.inmemory.InMemoryMailboxSessionMapperFactory;
import org.apache.james.mailbox.model.MessageId;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.PreDeletionHooks;
import org.apache.james.mailbox.store.StoreMailboxAnnotationManager;
import org.apache.james.mailbox.store.StoreRightManager;
import org.apache.james.mailbox.store.StoreSubscriptionManager;
import org.apache.james.mailbox.store.mail.model.impl.MessageParser;
import org.apache.james.mailbox.store.quota.QuotaComponents;
import org.apache.james.mailbox.store.search.MessageSearchIndex;
import org.springframework.context.annotation.Bean;

/**
 * MailboxMemoryConfiguration
 *
 * @author tabuyos
 * @since 2022/2/3
 */
public class MailboxMemoryConfiguration {

  @Bean
  public InMemoryId.Factory factory() {
    return new InMemoryId.Factory();
  }

  @Bean
  public InMemoryMailboxManager inMemoryMailboxManager(MailboxSessionMapperFactory mailboxSessionMapperFactory, SessionProvider sessionProvider,
                                                       MailboxPathLocker locker, MessageParser messageParser, MessageId.Factory messageIdFactory,
                                                       EventBus eventBus,
                                                       StoreMailboxAnnotationManager annotationManager,
                                                       StoreRightManager storeRightManager,
                                                       QuotaComponents quotaComponents,
                                                       MessageSearchIndex searchIndex,
                                                       PreDeletionHooks preDeletionHooks) {
    return new InMemoryMailboxManager(mailboxSessionMapperFactory, sessionProvider, locker, messageParser, messageIdFactory, eventBus, annotationManager, storeRightManager, quotaComponents, searchIndex, preDeletionHooks);
  }

  @Bean
  public StoreSubscriptionManager storeSubscriptionManager(InMemoryMailboxSessionMapperFactory factory) {
    return new StoreSubscriptionManager(factory);
  }

  @Bean
  public InMemoryMailboxSessionMapperFactory inMemoryMailboxSessionMapperFactory() {
    return new InMemoryMailboxSessionMapperFactory();
  }
}
