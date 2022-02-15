/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.mail.server.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.james.events.EventBus;
import org.apache.james.mailbox.SessionProvider;
import org.apache.james.mailbox.jpa.JPAId;
import org.apache.james.mailbox.jpa.JPAMailboxSessionMapperFactory;
import org.apache.james.mailbox.jpa.mail.JPAModSeqProvider;
import org.apache.james.mailbox.jpa.mail.JPAUidProvider;
import org.apache.james.mailbox.jpa.openjpa.OpenJPAMailboxManager;
import org.apache.james.mailbox.jpa.quota.JPAPerUserMaxQuotaDAO;
import org.apache.james.mailbox.jpa.quota.JPAPerUserMaxQuotaManager;
import org.apache.james.mailbox.jpa.quota.JpaCurrentQuotaManager;
import org.apache.james.mailbox.model.MessageId;
import org.apache.james.mailbox.store.StoreMailboxAnnotationManager;
import org.apache.james.mailbox.store.StoreRightManager;
import org.apache.james.mailbox.store.StoreSubscriptionManager;
import org.apache.james.mailbox.store.mail.model.impl.MessageParser;
import org.apache.james.mailbox.store.quota.QuotaComponents;
import org.apache.james.mailbox.store.search.MessageSearchIndex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * MailboxJpaConfiguration
 *
 * @author tabuyos
 * @since 2022/2/3
 */
public class MailboxJpaConfiguration {

  @Bean
  public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
    return new PersistenceAnnotationBeanPostProcessor();
  }

  @Bean
  public JPAId.Factory factory() {
    return new JPAId.Factory();
  }

  @Bean
  public OpenJPAMailboxManager openJPAMailboxManager(JPAMailboxSessionMapperFactory mapperFactory,
                                                     SessionProvider sessionProvider,
                                                     MessageParser messageParser,
                                                     MessageId.Factory messageIdFactory,
                                                     EventBus eventBus,
                                                     StoreMailboxAnnotationManager annotationManager,
                                                     StoreRightManager storeRightManager,
                                                     QuotaComponents quotaComponents,
                                                     MessageSearchIndex index) {
    return new OpenJPAMailboxManager(mapperFactory, sessionProvider, messageParser, messageIdFactory, eventBus, annotationManager, storeRightManager, quotaComponents, index);
  }

  @Bean
  public JPAMailboxSessionMapperFactory jpaMailboxSessionMapperFactory(EntityManagerFactory entityManagerFactory, JPAUidProvider jpaUidProvider, JPAModSeqProvider jpaModSeqProvider) {
    return new JPAMailboxSessionMapperFactory(entityManagerFactory, jpaUidProvider, jpaModSeqProvider);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactory.setDataSource(dataSource);
    entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
    entityManagerFactory.setPersistenceUnitName("Tabuyos");
    return entityManagerFactory;
  }

  @Bean
  public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean factoryBean) {
    return factoryBean.getObject();
  }

  @Bean(destroyMethod = "close")
  public HikariDataSource dataSource() {
    return new HikariDataSource();
  }

  @Bean
  public JpaVendorAdapter openJpaVendorAdapter() {
    HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setShowSql(false);
    hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
    return hibernateJpaVendorAdapter;
  }

  @Bean
  public JPAUidProvider jpaUidProvider(EntityManagerFactory entityManagerFactory) {
    return new JPAUidProvider(entityManagerFactory);
  }

  @Bean
  public JPAModSeqProvider jpaModSeqProvider(EntityManagerFactory entityManagerFactory) {
    return new JPAModSeqProvider(entityManagerFactory);
  }

  @Bean
  public StoreSubscriptionManager storeSubscriptionManager(JPAMailboxSessionMapperFactory mapperFactory) {
    return new StoreSubscriptionManager(mapperFactory);
  }

  @Bean
  public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setIgnoreUnresolvablePlaceholders(true);
    configurer.setLocation(new ClassPathResource("tabuyos-database.properties"));
    return configurer;
  }

  @Bean
  public JPAPerUserMaxQuotaDAO jpaPerUserMaxQuotaDAO(EntityManagerFactory entityManagerFactory) {
    return new JPAPerUserMaxQuotaDAO(entityManagerFactory);
  }

  @Bean
  public JPAPerUserMaxQuotaManager jpaPerUserMaxQuotaManager(JPAPerUserMaxQuotaDAO jpaPerUserMaxQuotaDAO) {
    return new JPAPerUserMaxQuotaManager(jpaPerUserMaxQuotaDAO);
  }

  @Bean
  public JpaCurrentQuotaManager jpaCurrentQuotaManager(EntityManagerFactory entityManagerFactory) {
    return new JpaCurrentQuotaManager(entityManagerFactory);
  }
}
