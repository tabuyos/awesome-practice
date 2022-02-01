/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.download.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * DownloadZipController
 *
 * @author tabuyos
 * @since 2022/1/24
 */
@RestController
public class DownloadZipController {

  @GetMapping("downloadZip")
  public void downloadZip(HttpServletResponse response) {
    String basePath = "C:\\Users\\tabuyos\\Desktop\\Tabuyos\\Work";
    File baseFile = Paths.get(basePath).toFile();
    File[] files = baseFile.listFiles(File::isFile);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setContentType("application/zip");
    int count = 100;
    try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()))) {
      zos.setMethod(ZipOutputStream.DEFLATED);
      for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
        File file = files[i];
        FileInputStream is = new FileInputStream(file);
        zos.putNextEntry(new ZipEntry(i + "_" + file.getName()));
        byte[] bytes = new byte[1024];
        int length;
        while ((length = is.read(bytes)) != -1) {
          zos.write(bytes, 0, length);
        }
        is.close();
        zos.closeEntry();
        if (--count <= 0) {
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
