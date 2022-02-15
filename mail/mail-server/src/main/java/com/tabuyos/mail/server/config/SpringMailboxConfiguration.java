/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.events.EventBus;
import org.apache.james.mailbox.Authenticator;
import org.apache.james.mailbox.Authorizator;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.SessionProvider;
import org.apache.james.mailbox.acl.GroupMembershipResolver;
import org.apache.james.mailbox.acl.MailboxACLResolver;
import org.apache.james.mailbox.acl.SimpleGroupMembershipResolver;
import org.apache.james.mailbox.acl.UnionMailboxACLResolver;
import org.apache.james.mailbox.copier.MailboxCopier;
import org.apache.james.mailbox.spring.MailboxInitializer;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.PreDeletionHooks;
import org.apache.james.mailbox.store.SessionProviderImpl;
import org.apache.james.mailbox.store.StoreMailboxAnnotationManager;
import org.apache.james.mailbox.store.StoreRightManager;
import org.apache.james.mailbox.store.mail.model.DefaultMessageId;
import org.apache.james.mailbox.store.mail.model.impl.MessageParser;
import org.apache.james.mailbox.store.quota.QuotaUpdater;
import org.apache.james.mailbox.store.search.MessageSearchIndex;
import org.apache.james.mailbox.tools.copier.MailboxCopierImpl;
import org.apache.james.metrics.api.MetricFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.HashSet;

/**
 * SpringMailboxConfiguration
 *
 * @author tabuyos
 * @since 2022/2/3
 */
@Import({
  MailboxLockerConfiguration.class,
  MailboxJpaConfiguration.class,
  MailboxIndexLuceneConfiguration.class,
  MailboxMaildirConfiguration.class,
  MailboxMemoryConfiguration.class,
})
public class SpringMailboxConfiguration {

  @Bean
  public DefaultMessageId.Factory factory() {
    return new DefaultMessageId.Factory();
  }

  @Bean
  public MailboxCopier mailboxCopier() {
    return new MailboxCopierImpl();
  }

  @Bean
  public UnionMailboxACLResolver unionMailboxACLResolver() {
    return new UnionMailboxACLResolver();
  }

  @Bean
  public SimpleGroupMembershipResolver simpleGroupMembershipResolver() {
    return new SimpleGroupMembershipResolver();
  }

  @Bean
  public MessageParser messageParser() {
    return new MessageParser();
  }

  @Bean
  public StoreMailboxAnnotationManager annotationManager(MailboxSessionMapperFactory mailboxSessionMapperFactory,
                                                         StoreRightManager rightManager) {
    return new StoreMailboxAnnotationManager(mailboxSessionMapperFactory, rightManager);
  }

  @Bean
  public StoreRightManager storeRightManager(MailboxSessionMapperFactory mailboxSessionMapperFactory,
                                             MailboxACLResolver aclResolver,
                                             GroupMembershipResolver groupMembershipResolver,
                                             EventBus eventBus) {
    return new StoreRightManager(mailboxSessionMapperFactory, aclResolver, groupMembershipResolver, eventBus);
  }

  @Bean
  public SessionProvider sessionProvider(Authenticator authenticator, Authorizator authorizator) {
    return new SessionProviderImpl(authenticator, authorizator);
  }

  @Bean(initMethod = "init")
  public MailboxInitializer mailboxInitializer(SessionProvider sessionProvider, EventBus eventBus, MessageSearchIndex messageSearchIndex, QuotaUpdater quotaUpdater, MailboxManager mailboxManager, MailboxSessionMapperFactory mapperFactory) {
    return new MailboxInitializer(sessionProvider, eventBus, messageSearchIndex, quotaUpdater, mailboxManager, mapperFactory);
  }

  @Bean
  public PreDeletionHooks preDeletionHooks(MetricFactory metricFactory) {
    return new PreDeletionHooks(new HashSet<>(), metricFactory);
  }
}
