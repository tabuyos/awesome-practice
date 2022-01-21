/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.flink.sink;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.File;
import java.nio.file.Paths;

/**
 * SinkDemo1
 *
 * @author tabuyos
 * @since 2022/1/18
 */
public class SinkDemo1 {

  /**
   * readTextFile 无法在 JDK 16 上正常工作
   *
   * @param args args
   * @throws Exception ex
   */
  public static void main(String[] args) throws Exception {
    //1:env
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
    //2:source
    DataStreamSource<String> ds = env.readTextFile("temp/input/words.txt");
    //3:transformation
    //4:sink
    //4.1:ds.print 直接输出到控制台
    ds.print();
    ds.print("提示符");
    //4.2:ds.printToErr() 直接输出到到控制台，用红色
    ds.printToErr();
    //4.3:ds.writeAsText("本地/HDFS的path",WriteMode.OVERWRITE).setParallelism(1);
    //以一个并行度写，生成一个文件
    ds.writeAsText("temp/output/result1").setParallelism(1);
    //以多个并行度写，生成多个文件
    ds.writeAsText("temp/output/result2").setParallelism(2);
    //在输出到path的时候，可以在前面设置并行度，如果
    //并行度>1，则path为目录
    //并行度=1，则path为文件名
    //5：execution
    env.execute();
  }
}
