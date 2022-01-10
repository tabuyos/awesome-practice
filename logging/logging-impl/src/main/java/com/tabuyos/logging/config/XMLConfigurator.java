/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.config;

import com.tabuyos.logging.appender.Appender;
import com.tabuyos.logging.context.LoggerContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * XMLConfigurator
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public class XMLConfigurator implements Configurator {

  /**
   * url
   */
  private final URL url;

  /**
   * context
   */
  private final LoggerContext loggerContext;

  /**
   * appender
   */
  static final String APPENDER_TAG = "appender";

  /**
   * tag
   */
  static final String LOGGER_TAG = "logger";

  /**
   * class attr
   */
  static final String CLASS_ATTR = "class";

  /**
   * name attr
   */
  static final String NAME_ATTR = "name";

  /**
   * value attr
   */
  static final String VALUE_ATTR = "value";

  /**
   * level attr
   */
  static final String LEVEL_ATTR = "level";

  /**
   * filter attr
   */
  static final String FILTER_ATTR = "filter";

  /**
   * layout tag
   */
  static final String LAYOUT_TAG = "layout";

  /**
   * encoding tag
   */
  static final String ENCODING_TAG = "encoding";

  /**
   * param tag
   */
  static final String PARAM_TAG = "param";

  /**
   * root tag
   */
  static final String ROOT_TAG = "root";

  /**
   * appender ref tag
   */
  static final String APPENDER_REF_TAG = "appender-ref";

  /**
   * appender ref attr
   */
  static final String APPENDER_REF_ATTR = "ref";

  /**
   * appender cache
   */
  private Map<String, Appender> appenderCache = new HashMap<>();

  public XMLConfigurator(URL url, LoggerContext loggerContext) {
    this.url = url;
    this.loggerContext = loggerContext;
  }

  @Override
  public void doConfigure() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = factory.newDocumentBuilder();
      Document document = documentBuilder.parse(url.openStream());
      parse(document.getDocumentElement());
    } catch (ParserConfigurationException | SAXException | IOException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
      e.printStackTrace();
    }
  }

  private void parse(Element document) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
    //do parse...
  }
}
