/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.events.EventBus;
import org.apache.james.mailbox.MailboxAnnotationManager;
import org.apache.james.mailbox.MailboxPathLocker;
import org.apache.james.mailbox.SessionProvider;
import org.apache.james.mailbox.maildir.MaildirId;
import org.apache.james.mailbox.maildir.MaildirMailboxSessionMapperFactory;
import org.apache.james.mailbox.maildir.MaildirStore;
import org.apache.james.mailbox.model.MessageId;
import org.apache.james.mailbox.store.MailboxManagerConfiguration;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.PreDeletionHooks;
import org.apache.james.mailbox.store.StoreMailboxManager;
import org.apache.james.mailbox.store.StoreRightManager;
import org.apache.james.mailbox.store.StoreSubscriptionManager;
import org.apache.james.mailbox.store.mail.model.impl.MessageParser;
import org.apache.james.mailbox.store.quota.QuotaComponents;
import org.apache.james.mailbox.store.search.MessageSearchIndex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * MailboxMaildirConfiguration
 *
 * @author tabuyos
 * @since 2022/2/3
 */
public class MailboxMaildirConfiguration {

  @Bean
  public MaildirId.Factory factory() {
    return new MaildirId.Factory();
  }

  @Bean
  public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setIgnoreUnresolvablePlaceholders(true);
    configurer.setLocation(new ClassPathResource("mailbox-maildir.properties"));
    return configurer;
  }

  @Bean
  public StoreMailboxManager storeMailboxManager(MailboxSessionMapperFactory mailboxSessionMapperFactory, SessionProvider sessionProvider,
                                                 MailboxPathLocker locker, MessageParser messageParser,
                                                 MessageId.Factory messageIdFactory, MailboxAnnotationManager annotationManager,
                                                 EventBus eventBus, StoreRightManager storeRightManager,
                                                 QuotaComponents quotaComponents, MessageSearchIndex searchIndex, MailboxManagerConfiguration configuration,
                                                 PreDeletionHooks preDeletionHooks) {
    return new StoreMailboxManager(mailboxSessionMapperFactory, sessionProvider, locker, messageParser, messageIdFactory, annotationManager, eventBus, storeRightManager, quotaComponents, searchIndex, configuration, preDeletionHooks);
  }

  @Bean
  public MailboxManagerConfiguration mailboxManagerConfiguration(MaildirMailboxSessionMapperFactory maildirMailboxSessionMapperFactory) {
    return MailboxManagerConfiguration.DEFAULT;
  }

  @Bean
  public StoreSubscriptionManager storeSubscriptionManager(MaildirMailboxSessionMapperFactory maildirMailboxSessionMapperFactory) {
    return new StoreSubscriptionManager(maildirMailboxSessionMapperFactory);
  }

  @Bean
  public MaildirMailboxSessionMapperFactory maildirMailboxSessionMapperFactory(MaildirStore maildirStore) {
    return new MaildirMailboxSessionMapperFactory(maildirStore);
  }

  @Bean
  public MaildirStore maildirStore() {
    MaildirStore store = new MaildirStore("mailbox-dir");
    store.setMessageNameStrictParse(false);
    return store;
  }
}
