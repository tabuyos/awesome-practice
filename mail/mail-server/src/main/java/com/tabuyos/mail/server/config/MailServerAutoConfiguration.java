/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * MailServerAutoConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
@Configuration
@Import({MailServerConfiguration.class})
public class MailServerAutoConfiguration {

}
