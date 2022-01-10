/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.config;

import com.tabuyos.logging.context.LoggerContext;

import java.net.URL;

/**
 * ContextInitializer
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public class ContextInitializer {

  final public static String AUTOCONFIG_FILE = "tabuyos-logging.xml";
  final public static String YAML_FILE = "tabuyos-logging.yml";

  private static final LoggerContext DEFAULT_LOGGER_CONTEXT = new LoggerContext();

  public static void autoconfig() {
    URL url = getConfigUrl();
    if (url == null) {
      System.err.println("config[tabuyos-logging.xml or tabuyos-logging.yml] file not found!");
      return;
    }
    String urlString = url.toString();
    Configurator configurator = null;

    if (urlString.endsWith("xml")) {
      configurator = new XMLConfigurator(url, DEFAULT_LOGGER_CONTEXT);
    }
    if (urlString.endsWith("yml")) {
      configurator = new YAMLConfigurator(url, DEFAULT_LOGGER_CONTEXT);
    }
    assert configurator != null;
    configurator.doConfigure();
  }

  private static URL getConfigUrl() {
    URL url = null;
    ClassLoader classLoader = ContextInitializer.class.getClassLoader();
    url = classLoader.getResource(AUTOCONFIG_FILE);
    if (url != null) {
      return url;
    }
    url = classLoader.getResource(YAML_FILE);
    return url;
  }

  public static LoggerContext getDefaultLoggerContext() {
    return DEFAULT_LOGGER_CONTEXT;
  }
}
