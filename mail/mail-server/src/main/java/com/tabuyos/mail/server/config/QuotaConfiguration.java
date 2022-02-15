/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.events.EventBus;
import org.apache.james.mailbox.SessionProvider;
import org.apache.james.mailbox.inmemory.quota.InMemoryCurrentQuotaManager;
import org.apache.james.mailbox.inmemory.quota.InMemoryPerUserMaxQuotaManager;
import org.apache.james.mailbox.jpa.quota.JpaCurrentQuotaManager;
import org.apache.james.mailbox.quota.CurrentQuotaManager;
import org.apache.james.mailbox.quota.MaxQuotaManager;
import org.apache.james.mailbox.quota.QuotaManager;
import org.apache.james.mailbox.quota.QuotaRootResolver;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.quota.CurrentQuotaCalculator;
import org.apache.james.mailbox.store.quota.DefaultUserQuotaRootResolver;
import org.apache.james.mailbox.store.quota.FixedMaxQuotaManager;
import org.apache.james.mailbox.store.quota.ListeningCurrentQuotaUpdater;
import org.apache.james.mailbox.store.quota.NoMaxQuotaManager;
import org.apache.james.mailbox.store.quota.NoQuotaManager;
import org.apache.james.mailbox.store.quota.NoQuotaUpdater;
import org.apache.james.mailbox.store.quota.QuotaComponents;
import org.apache.james.mailbox.store.quota.StoreQuotaManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import javax.persistence.EntityManagerFactory;

/**
 * QuotaConfiguration
 *
 * @author tabuyos
 * @since 2022/2/3
 */
public class QuotaConfiguration {

  @Bean
  public JpaCurrentQuotaManager jpaCurrentQuotaManager(EntityManagerFactory entityManagerFactory) {
    return new JpaCurrentQuotaManager(entityManagerFactory);
  }

  @Bean
  @Lazy
  public DefaultUserQuotaRootResolver defaultUserQuotaRootResolver(SessionProvider sessionProvider, MailboxSessionMapperFactory factory) {
    return new DefaultUserQuotaRootResolver(sessionProvider, factory);
  }

  @Bean
  @Lazy
  public CurrentQuotaCalculator currentQuotaCalculator(MailboxSessionMapperFactory factory,
                                                       QuotaRootResolver quotaRootResolver) {
    return new CurrentQuotaCalculator(factory, quotaRootResolver);
  }

  @Bean
  @Lazy
  public InMemoryCurrentQuotaManager inMemoryCurrentQuotaManager(CurrentQuotaCalculator quotaCalculator, SessionProvider sessionProvider) {
    return new InMemoryCurrentQuotaManager(quotaCalculator, sessionProvider);
  }

  @Bean
  @Lazy
  public NoMaxQuotaManager noMaxQuotaManager() {
    return new NoMaxQuotaManager();
  }

  @Bean
  @Lazy
  public FixedMaxQuotaManager fixedMaxQuotaManager() {
    return new FixedMaxQuotaManager();
  }

  @Bean
  @Lazy
  public InMemoryPerUserMaxQuotaManager inMemoryPerUserMaxQuotaManager() {
    return new InMemoryPerUserMaxQuotaManager();
  }

  @Bean
  @Lazy
  public NoQuotaManager noQuotaManager() {
    return new NoQuotaManager();
  }

  @Bean
  @Lazy
  public StoreQuotaManager storeQuotaManager(CurrentQuotaManager currentQuotaManager, MaxQuotaManager maxQuotaManager) {
    return new StoreQuotaManager(currentQuotaManager, maxQuotaManager);
  }

  @Bean
  @Lazy
  public NoQuotaUpdater noQuotaUpdater() {
    return new NoQuotaUpdater();
  }

  @Bean
  @Lazy
  public ListeningCurrentQuotaUpdater listeningCurrentQuotaUpdater(CurrentQuotaManager currentQuotaManager, QuotaRootResolver quotaRootResolver, EventBus eventBus, QuotaManager quotaManager) {
    return new ListeningCurrentQuotaUpdater(currentQuotaManager, quotaRootResolver, eventBus, quotaManager);
  }

  @Bean
  public QuotaComponents quotaComponents(MaxQuotaManager maxQuotaManager, QuotaManager quotaManager, QuotaRootResolver quotaRootResolver) {
    return new QuotaComponents(maxQuotaManager, quotaManager, quotaRootResolver);
  }
}
