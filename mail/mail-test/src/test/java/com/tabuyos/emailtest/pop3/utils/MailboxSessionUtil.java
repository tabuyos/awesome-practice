/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package com.tabuyos.emailtest.pop3.utils;

import com.google.common.annotations.VisibleForTesting;
import org.apache.james.core.Username;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.model.MailboxConstants;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class MailboxSessionUtil {

  public static MailboxSession create(Username username) {
    return create(username, MailboxSession.SessionId.of(ThreadLocalRandom.current().nextLong()));
  }

  @VisibleForTesting
  public static MailboxSession create(Username username, MailboxSession.SessionId sessionId) {
    ArrayList<Locale> locales = new ArrayList<>();

    return new MailboxSession(
      sessionId,
      username,
      locales,
      MailboxConstants.DEFAULT_DELIMITER,
      MailboxSession.SessionType.User);
  }
}
