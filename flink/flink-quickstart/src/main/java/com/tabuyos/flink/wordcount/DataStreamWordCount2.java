/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.flink.wordcount;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * DataStreamWordCount2
 *
 * @author tabuyos
 * @since 2022/1/18
 */
public class DataStreamWordCount2 {

  public static void main(String[] args) throws Exception {
    // 新版本的流批一体API，即支持流处理也支持批处理
    // 1：准备环境-env
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // env.fromElements(1, 2, 3)
    //    // 返回 i 的平方
    //    .map(i -> i*i)
    //    .print();
    // env.execute();
    // 自动判断
    // env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
    // 流
    env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
    // 批
    // env.setRuntimeMode(RuntimeExecutionMode.BATCH);
    // 2：准备数据-source
    DataStream<String> linesDs = env.fromElements("itcast hadoop spark",
                                                  "itcast hadoop spark",
                                                  "itcast hadoop",
                                                  "itcast");
    linesDs.print("linesDs");
    // 3：处理数据-transformation
    // 3.1：每一行数据按照空格切分成一个个的单词组成一个集合
    // Collector<R> 使用泛型, 由于 Java 在编译时会发生泛型擦除
    // void flatMap(T value, Collector<O> out) 变为 void flatMap(T value, Collector out)
    // 因此需要使用 returns 显式的声明, 或者自定义 FlatMapFunction 实现 ResultTypeQueryable 和 FlatMapFunction
    // 解决方案有多种, 显式声明只是其中的一种
    // 具体参考: https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/java_lambdas/
    DataStream<String> wordsDs = linesDs
      .flatMap((FlatMapFunction<String, String>) (string, collector) -> {
        String[] arrays = string.split(" ");
        for (String el : arrays) {
          collector.collect(el);
        }
      })
      // .returns(TypeInformation.of(String.class));
      .returns(Types.STRING);
    wordsDs.print("wordsDs");
    // 3.2：将集合中的每一个单词记为1
    // 原因同上, 泛型擦除
    DataStream<Tuple2<String, Integer>> wordAndOnesDs = wordsDs
      .map((MapFunction<String, Tuple2<String, Integer>>) word ->
        Tuple2.of(word, 1))
      .returns(Types.TUPLE(Types.STRING, Types.INT));
    wordAndOnesDs.print("wordAndOnesDs");
    // 3.3：对数据按照单词（key）进行分组
    // 0表示按照tuple中索引为0的字段，也就是key进行分组
    // KeyedStream<Tuple2<String, Integer>, Tuple> groupedDs = wordAndOnesDs.keyBy(0);
    KeyedStream<Tuple2<String, Integer>, String> groupedDs = wordAndOnesDs.keyBy(t -> t.f0);
    groupedDs.print("groupedDs");
    // 3.4：对各个组内的数据按照数量（value）进行聚合就是求sum
    // 1 表示tuple中索引为1的字段
    DataStream<Tuple2<String, Integer>> result = groupedDs.sum(1);
    // 4：输出结果-sink
    result.print("result");
    // 5：触发执行-execute
    // DataStream需要调用execute
    env.execute();
  }
}
