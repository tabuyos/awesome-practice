/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.appender;

import com.tabuyos.logging.enums.Level;
import com.tabuyos.logging.event.LoggingEvent;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * ConsoleAppender
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public class ConsoleAppender implements Appender, Closeable {

  private final OutputStream out = System.out;
  private final OutputStream err = System.err;

  @Override
  public void append(LoggingEvent event) {
    try {
      if (event.getLevel() == Level.ERROR) {
        err.write(event.toString().getBytes(StandardCharsets.UTF_8));
      } else {
        out.write(event.toString().getBytes(StandardCharsets.UTF_8));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() throws IOException {
    if (out != null) {
      out.close();
    }
    if (err != null) {
      err.close();
    }
  }
}
