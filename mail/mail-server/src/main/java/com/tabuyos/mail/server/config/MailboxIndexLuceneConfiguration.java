/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.mailbox.SessionProvider;
import org.apache.james.mailbox.lucene.search.LuceneMessageSearchIndex;
import org.apache.james.mailbox.model.MailboxId;
import org.apache.james.mailbox.model.MessageId;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.search.LazyMessageSearchIndex;
import org.apache.james.mailbox.store.search.ListeningMessageSearchIndex;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;

/**
 * MailboxIndexLuceneConfiguration
 *
 * @author tabuyos
 * @since 2022/2/3
 */
public class MailboxIndexLuceneConfiguration {

  @Bean
  public LazyMessageSearchIndex lazyMessageSearchIndex(ListeningMessageSearchIndex index, MailboxSessionMapperFactory factory, SessionProvider sessionProvider) {
    return new LazyMessageSearchIndex(index, factory, sessionProvider);
  }

  @Bean
  public LuceneMessageSearchIndex luceneMessageSearchIndex(MailboxSessionMapperFactory factory,
                                                           MailboxId.Factory mailboxIdFactory,
                                                           Directory directory,
                                                           MessageId.Factory messageIdFactory,
                                                           SessionProvider sessionProvider) throws IOException {
    LuceneMessageSearchIndex searchIndex = new LuceneMessageSearchIndex(factory, mailboxIdFactory, directory, messageIdFactory, sessionProvider);
    searchIndex.setEnableSuffixMatch(true);
    return searchIndex;
  }

  @Bean
  public FSDirectory fsDirectory() throws IOException {
    return FSDirectory.open(new File("../var/store/lucene"));
  }
}
