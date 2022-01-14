参考资料: [2万字长文搞懂Kafka - DockOne.io](http://dockone.io/article/2434401)

# kafka 消息模型

- 点对点(队列)
- 发布/订阅(主题)

在点对点模型中, 一个生产者产生的消息, 只能被一个消费者进行消费

而在发布订阅模型中, 发布者发送消息到 `topic` 中, 只能被订阅了该 `topic` 的订阅者接收

`Kafka` 底子里只有主题, 通过使用一个消费者组实现点对点, 以及通过使用多个消费者组实现发布订阅

**相同组 `ID` 的消费者为同一个组**

## ConcurrentModificationException 错误

kafka 的 Consumer 是单线程的, 因此, 在多线程中调用时会抛出该异常
关键代码: org.apache.kafka.clients.consumer.KafkaConsumer#acquire()
该方法判断了是否是同一个线程, 如果不是, 则抛出异常

## 多线程方案设计

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d882b41312564004b44c5319bf422c37~tplv-k3u1fbpfcp-watermark.awebp" alt="多线程方案设计" style="zoom:150%;" />

##### 方案一

**优势**

- 实现起来简单，因为它比较符合目前我们使用 Consumer API 的习惯。我们在写代码的时候，使用多个线程并在每个线程中创建专属的 KafkaConsumer 实例就可以了。
- 多个线程之间彼此没有任何交互，省去了很多保障线程安全方面的开销。
- 由于每个线程使用专属的 KafkaConsumer 实例来执行消息获取和消息处理逻辑，因此，Kafka 主题中的每个分区都能保证只被一个线程处理，这样就很容易实现分区内的消息消费顺序。这对在乎事件先后顺序的应用场景来说，是非常重要的优势。

**不足**

- 每个线程都维护自己的 KafkaConsumer 实例，必然会占用更多的系统资源，比如内存、TCP 连接等。在资源紧张的系统环境中，方案 1 的这个劣势会表现得更加明显。
- 这个方案能使用的线程数受限于 Consumer 订阅主题的总分区数。我们知道，在一个消费者组中，每个订阅分区都只能被组内的一个消费者实例所消费。假设一个消费者组订阅了 100 个分区，那么方案 1 最多只能扩展到 100 个线程，多余的线程无法分配到任何分区，只会白白消耗系统资源。
- 当然了，这种扩展性方面的局限可以被多机架构所缓解。除了在一台机器上启用 100 个线程消费数据，我们也可以选择在 100 台机器上分别创建 1 个线程，效果是一样的。因此，如果你的机器资源很丰富，这个劣势就不足为虑了。
- 每个线程完整地执行消息获取和消息处理逻辑。一旦消息处理逻辑很重，造成消息处理速度慢，就很容易出现不必要的 Rebalance，从而引发整个消费者组的消费停滞。这个劣势你一定要注意。我们之前讨论过如何避免 Rebalance。

##### 方案二

**优势**

- 与方案 1 的粗粒度不同，方案 2 将任务切分成了消息获取和消息处理两个部分，分别由不同的线程处理它们。比起方案 1，方案 2 的最大优势就在于它的高伸缩性，就是说我们可以独立地调节消息获取的线程数，以及消息处理的线程数，而不必考虑两者之间是否相互影响。如果你的消费获取速度慢，那么增加消费获取的线程数即可；如果是消息的处理速度慢，那么增加 Worker 线程池线程数即可。

**优势**

- 它的实现难度要比方案 1 大得多，毕竟它有两组线程，你需要分别管理它们。因为该方案将消息获取和消息处理分开了，也就是说获取某条消息的线程不是处理该消息的线程，因此无法保证分区内的消费顺序。举个例子，比如在某个分区中，消息 1 在消息 2 之前被保存，那么 Consumer 获取消息的顺序必然是消息 1 在前，消息 2 在后，但是，后面的 Worker 线程却有可能先处理消息 2，再处理消息 1，这就破坏了消息在分区中的顺序。还是那句话，如果你在意 Kafka 中消息的先后顺序，方案 2 的这个劣势是致命的。
- 方案 2 引入了多组线程，使得整个消息消费链路被拉长，最终导致正确位移提交会变得异常困难，结果就是可能会出现消息的重复消费。如果你在意这一点，那么我不推荐你使用方案 2。



## 主题与分区

```shell
# 查看所有主题
kafka-topics.bat --zookeeper localhost:2181 --list

# 查看主题的描述信息
kafka-topics.bat --zookeeper localhost:2181 --describe --topic hello-topic

# 创建主题, 三个分区, 一个副本
kafka-topics.sh --zookeeper localhost:2181 --create --topic hello-topic --partitions 3 --replication-factor 1

# 增加分区(分区只能增加不能减少, 要想减少, 需要删除分区, 重新创建)
kafka-topics.bat --zookeeper localhost:2181 --alter --topic hello-topic --partitions 2
```

发送到指定分区, 在 `ProducerRecord` 中指定分区号

从指定分区消费,  使用 `assign` 方法

## kafka 消费方式

### 几种不同的注册方式

- `subscribe` 方式：当主题分区数量变化或者 `consumer` 数量变化时，会进行 `rebalance`；注册`rebalance`监听器，可以手动管理`offset`不注册监听器，`kafka`自动管理
- `assign` 方式：手动将 `consumer` 与 `partition` 进行对应，`kafka` 不会进行 `rebanlance`

### 关键配置及含义

- `enable.auto.commit` 是否自动提交自己的`offset`值；默认值 true`
- `auto.commit.interval.ms` 自动提交时长间隔；默认值 5000 ms
- `consumer.commitSync()`; `offset`提交命令；

### 默认配置

采用默认配置情况下，既不能完全保证 `at-least-once` 也不能完全保证 `at-most-once`；
比如：

> 在自动提交之后，数据消费流程失败，这样就会有丢失，不能保证`at-least-once`；
> 数据消费成功，但是自动提交失败，可能会导致重复消费，这样也不能保证 `at-most-once`；
> 但是将自动提交时长设置得足够小，则可以最大限度地保证 `at-most-once`；

#### at most onece模式

基本思想是保证每一条消息`commit`成功之后，再进行消费处理；
设置自动提交为`false`，接收到消息之后，首先`commit`，然后再进行消费

#### at least onece模式

基本思想是保证每一条消息处理成功之后，再进行`commit`；
设置自动提交为`false`；消息处理成功之后，手动进行`commit`；
采用这种模式时，最好保证消费操作的“幂等性”，防止重复消费；

#### exactly onece模式

核心思想是将`offset`作为唯一id与消息同时处理，并且保证处理的原子性；
设置自动提交为`false`；消息处理成功之后再提交；
比如对于关系型数据库来说，可以将`id`设置为消息处理结果的唯一索引，再次处理时，如果发现该索引已经存在，那么就不处理；

### kafka基本信息

kafka的特点：

- 可靠性：分布式，分区，复制，容错
- 可扩展性：kafka消息传递系统轻松缩放，无需停机
- 耐用性：kafka使用分布式提交日志，这个以为这他会尽可能的快速将数据持久化到磁盘上，因此它是持久的。
- 性能：kafka对于发布订阅和消息订阅都具有高吞吐量。

Kafka集群由**多个**kafka实例组成，每个实例（Server）叫做`broker`

**Kafka Cluster：** 由多个实例组成cluster。每个实例（服务器）称为broker（掮客）

**Kafka broker：** kafka集群的每个实例。每个实例都有一个唯一的编号，起标识作用。

**Kafka consumer：** 消息的消费者，负责消费消息。

**Kafka Topic：** 主题，用来区分出不同的消息种类。存储消息的时候按种类区分，放入不同的topic下。比如向曾经的广播，每个台有一个频率，然后你要听某个台的话你需要把频率调到对应的频率上，这个频率就是topic。其实就是一个区分的作用。topic是一个逻辑划分单位。

**shard：** topic的分片。一般来说，不同的分片会放在不同的节点上（broker）。分片的数量理论上是没有上限的。对于一个topic，可以划分为多个小的容器，每个容器其实就是一个分片（分区），然后每个分区可以均匀的落在各个节点上。主要作用是在存储数据的时候，可以让数据在不同的分片上来存储，相当于将数据存储在不同的服务器中，从而达到提高topic存储容量的目的。

**replicas：** 副本，对每个分片构建多个副本，保证数据不会丢失。副本的上限就是节点的数量。比如kafka集群里有3个实例，那么最多只能设置3个副本。多个副本之间是存在主从关系的。主副本负责数据读写，从副本负责数据拷贝。不过在新版本2.0中有所变化。从副本在一定程度上可以进行读写。副本越多，数据越安全，同时对磁盘占用的空间越多。

#### Kafka的基础架构

[<img src="http://dockone.io/uploads/article/20210706/8b818366206197bd33361856b293c9e4.png" alt="4.png" style="zoom:150%;" />](http://dockone.io/uploads/article/20210706/8b818366206197bd33361856b293c9e4.png)


Kafka像其他MQ一样，也有自己的基础架构，主要存在生产者Producer、Kafka集群Broker、消费者Consumer、注册消息ZooKeeper。

- Producer：消息生产者，向Kafka中发布消息的角色。
- Consumer：消息消费者，即从Kafka中拉取消息消费的客户端。
- Consumer Group：消费者组，消费者组则是一组中存在多个消费者，消费者消费Broker中当前Topic的不同分区中的消息，消费者组之间互不影响，所有的消费者都属于某个消费者组，即消费者组是逻辑上的一个订阅者。某一个分区中的消息只能够一个消费者组中的一个消费者所消费。
- Broker：经纪人，一台Kafka服务器就是一个Broker，一个集群由多个Broker组成，一个Broker可以容纳多个Topic。
- Topic：主题，可以理解为一个队列，生产者和消费者都是面向一个Topic
- Partition：分区，为了实现扩展性，一个非常大的Topic可以分布到多个Broker上，一个Topic可以分为多个Partition，每个Partition是一个有序的队列（分区有序，不能保证全局有序）。
- Replica：副本Replication，为保证集群中某个节点发生故障，节点上的Partition数据不丢失，Kafka可以正常的工作，Kafka提供了副本机制，一个Topic的每个分区有若干个副本，一个Leader和多个Follower。
- Leader：每个分区多个副本的主角色，生产者发送数据的对象，以及消费者消费数据的对象都是Leader。
- Follower：每个分区多个副本的从角色，实时的从Leader中同步数据，保持和Leader数据的同步，Leader发生故障的时候，某个Follower会成为新的Leader。



> 上述一个Topic会产生多个分区Partition，分区中分为Leader和Follower，消息一般发送到Leader，Follower通过数据的同步与Leader保持同步，消费的话也是在Leader中发生消费，如果多个消费者，则分别消费Leader和各个Follower中的消息，当Leader发生故障的时候，某个Follower会成为主节点，此时会对齐消息的偏移量。

# kafka原理 分片和副本机制

一个消费者可以监听多个topic。 偏移量。 ![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/1c9bb6a71b98484c933576dcd1603586~tplv-k3u1fbpfcp-watermark.awebp)

**分片：** 对一个topic（主题，理解为一个大容器即可），划分为多个小容器，每个小容器其实就是一个分片（分区），然后这些分片会均匀的落在各个broker上。在存储数据的时候，数据会存储在不同的分片上，也就是数据会落在不同的机器上，从而扩展了topic的存储容量。

**副本：** 对每个分片的数据复制多个副本，从而保证数据不容易丢失。需要注意的是，同分片的多个副本不能放在一个节点上，因为当这个节点挂掉，那么这些副本就都丢了。副本的目的就是为了防止丢失，所以需要保证副本的分散存储。所以副本的数量受限于节点的数量，副本的最大值只能和节点的最大值相等。

# kafka数据不丢失原理(ack)

## 生产者端如何保证数据不丢失

生产者端是靠ack校验机制保证数据不丢失的。

ack的三个值（0，1，-1）

0：生产者只负责发送消息，不关心消息是否被kafka成功接收。

1：生产者需要保证数据成功发送到指定topic的分片的主副本上，然后kafka会给出ack响应。

-1（all）：生产者需要确保消息成功发送到kafka指定topic的分片的所有副本中，并且都给出ack响应，才可以认为消息发送成功了。

## broker端如何保证消息不丢失

broker端主要是通过数据的副本机制和ack为-1来保证数据不丢失。

## 消费端如何保证数据不丢失

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d4fdbf0aa2954d9fa1710b4eb196d0a2~tplv-k3u1fbpfcp-watermark.awebp) 1：消费者去连接kafka集群，kafka根据消费者的groupId找到其上次消费的位置（偏移量），如果该消费者是第一次消费，默认从监听的时间开始监听消息。（这里可以配置不同消费机制，也可以从头消费）

2：消费者开始获取数据，之后进行业务处理，然后提交偏移量给kafka。

这里会不会存在消息丢失呢？

答案是不会的！但是这里可能会存在消息重复消费的问题，因为如果当消息消费完成，然后没来得及提交偏移量，消费者挂了，那么下次消费的时候，kafka根据这个消费者groupId找上次消费的位置，而因为消费者上次没有提交偏移量，所以这里就会造成消息重复消费。

**kafka的每个消费者组的偏移量信息都记录在哪里呢？**

版本不同，记录的位置不同。

在0.8.x版本及之前，偏移量信息被记录在Zookeeper中 在0.8.x之后，偏移量被记录在kafka中，在kafka中专门有一个主题来进行统一的记录（_consumer_offsets 此topic有50个分区，每个分区一个副本）

# 什么是ISR？

先来看几个概念

- AR（Assigned Repllicas）一个partition的所有副本（就是replica，不区分leader或follower）
- ISR（In-Sync Replicas）能够和 leader 保持同步的 follower + leader本身 组成的集合。

- OSR（Out-Sync Relipcas）不能和 leader 保持同步的 follower 集合


公式：**AR = ISR + OSR**

所以，看明白了吗？

Kafka对外依然可以声称是完全同步，但是承诺是对AR中的所有replica完全同步了吗？

并没有。Kafka只保证对ISR集合中的所有副本保证完全同步。

至于，ISR到底有多少个follower，那不知道，别问，问就是完全同步，你再问就多了。

这就好比网购买一送一，结果邮来了一大一小两个产品。

你可能觉得有问题，其实是没问题的，商家说送的那个是一模一样的了吗？并没有。

ISR就是这个道理，Kafka是一定会保证leader接收到的消息完全同步给ISR中的所有副本。

而最坏的情况下，ISR中只剩leader自己。

基于此，上述完全同步会出现的问题就不是问题了。

因为ISR的机制就保证了，处于ISR内部的follower都是可以和leader进行同步的，一旦出现故障或延迟，就会被踢出ISR。

ISR 的核心就是：**动态调整**

总结：Kafka采用的就是一种完全同步的方案，而ISR是基于完全同步的一种优化机制。

follower的作用
读写都是由leader处理，follower只是作备份功能，不对外提供服务。

# kafka的存储机制

kafka中数据存储机制：以一个topic分片的副本为例：

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/dd3eacb1350d4fe19c5a7436679ad329~tplv-k3u1fbpfcp-watermark.awebp)

index是索引文件，log是日志文件，数据记录在log中。index文件主要用于存储消息的偏移量在log文件的物理偏移量的信息。

kafka是一个消息中间件，当数据被消费了，此时这个数据就可以被认为是无用了，需要在某个时间点删除。

**数据存储在副本中，副本被某个broker节点进行管理，最终的数据是存储在磁盘中，那么数据是存储在一个文件中还是分文件存储？**

是分文件来存储的。每个文件存储1GB的数据。

在一个文件段中主要由两个文件构成。一个是index，一个是log。index是log的索引文件。

文件名是此文件存储消息的起始偏移量。

**为什么kafka要进行分文件来存储数据呢？**

1）保证每个文件不至于过大，这样读取效率会更高。

2）kafka仅仅是一个临时存储数据的中介，默认情况下kafka会删除过期数据（时间为7天）。如果放在一个文件中，删除时需要遍历文件内容，效率低，操作麻烦。分文件的话只需要用文件的最后修改时间判断即可。

# Kafka的数据查询机制

![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0d5dcba9c9d64eeeb95b4347da8cefb4~tplv-k3u1fbpfcp-watermark.awebp)

上图是一个副本的数据，如何快速的找到777777这条数据呢？

1）确定数据所在的segment段

2）在这个段（737337）中先去查询index，从中找到777777消息在log文件中具体的物理偏移量

3）遍历log文件，顺序查找到具体位置，获取数据即可

# kafka的生产者分区策略

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/1759f3175dab402a9dce009136c7e59f~tplv-k3u1fbpfcp-watermark.awebp)

**假设，有一个topic，此topic有三个分片，三个副本。这是消息生产者生产的数据应该发往那个分片，或者所有的分片都会接收到消息吗？**

消息只会发送给某一个分片的主副本，然后主副本将信息数据同步给其他两个从副本。

kafka中有四种生产者发送消息的分区策略：

1）hash取模

2）粘性分区（轮询）

3）指定分区方案

4）自定义分区方案

等等...

分区的原因：

- 方便在集群中扩展：每个partition通过调整以适应它所在的机器，而一个Topic又可以有多个partition组成，因此整个集群可以适应适合的数据。
- 可以提高并发：以Partition为单位进行读写，类似于多路。


分区的原则：

- 指明partition（这里的指明是指第几个分区）的情况下，直接将指明的值作为partition的值
- 没有指明partition的情况下，但是存在值key，此时将key的hash值与topic的partition总数进行取余得到partition值
- 值与partition均无的情况下，第一次调用时随机生成一个整数，后面每次调用在这个整数上自增，将这个值与topic可用的partition总数取余得到partition值，即round-robin算法。

## 生产者ISR

为保证producer发送的数据能够可靠的发送到指定的topic中，topic的每个partition收到producer发送的数据后，都需要向producer发送ackacknowledgement，如果producer收到ack就会进行下一轮的发送，否则重新发送数据。

[![12.png](http://dockone.io/uploads/article/20210706/43df73080f8ebf53c8fc4d8b069f0c73.png)](http://dockone.io/uploads/article/20210706/43df73080f8ebf53c8fc4d8b069f0c73.png)


发送ack的时机：

确保有follower与leader同步完成，leader在发送ack，这样可以保证在leader挂掉之后，follower中可以选出新的leader（主要是确保follower中数据不丢失）。

follower同步完成多少才发送ack。

- 半数以上的follower同步完成，即可发送ack
- 全部的follower同步完成，才可以发送ack

## 生产者ack机制

对于某些不太重要的数据，对数据的可靠性要求不是很高，能够容忍数据的少量丢失，所以没有必要等到ISR中所有的follower全部接受成功。

Kafka为用户提供了三种可靠性级别，用户根据可靠性和延迟的要求进行权衡选择不同的配置。

ack参数配置：

- 0：producer不等待broker的ack，这一操作提供了最低的延迟，broker接收到还没有写入磁盘就已经返回，当broker故障时有可能丢失数据。

- 1：producer等待broker的ack，partition的leader落盘成功后返回ack，如果在follower同步成功之前leader故障，那么将丢失数据。（只是leader落盘）

  [![13.png](http://dockone.io/uploads/article/20210706/2117f62c83c21fc57fc09437be588348.png)](http://dockone.io/uploads/article/20210706/2117f62c83c21fc57fc09437be588348.png)

- -1（all）：producer等待broker的ack，partition的leader和ISR的follower全部落盘成功才返回ack，但是如果在follower同步完成后，broker发送ack之前，如果leader发生故障，会造成数据重复。(这里的数据重复是因为没有收到，所以继续重发导致的数据重复)

  [![14.png](http://dockone.io/uploads/article/20210706/1e839c1c3e6b278e4eade55d8168ad65.png)](http://dockone.io/uploads/article/20210706/1e839c1c3e6b278e4eade55d8168ad65.png)


producer返ack，0无落盘直接返，1只leader落盘然后返，-1全部落盘然后返。

# kafka消费者的负载均衡策略

如果消息生产的速度远远大于消息消费的速度就会造成消息的积压，那么如何解决呢？

**增加消费者数量**（需要保证他们在同一组内才能达到提高消费速度的目的啊），这里需要注意的是，kafka的消费者负载均衡规定，**在一个消费者组内，消费者的数量最多只能和监听topic的分片数量相等，如果消费者数据量大于了topic的分片数据量，那么总会有消费者处于闲置状态**。且一个分片的数据，只能被一个消费者所消费，不能被组内其他消费者所消费。

# 如何使用kafka模拟点对点和发布订阅呢？

定义多个消费者，让消费者属于不同的group，订阅同一个topic即可，模拟发布订阅

让所有监听topic的消费者，都属于同一个消费者组即可模拟点对点

# Kafka数据积压

（可以通过kafka-eagle查看）

# Kafka配额限速机制

生产者和消费者以极高的速度生产/消费消息，从而占用broker的全部或者大量资源，造成网络IO饱和。会影响其他topic的正常运行。

配额（Quotas）就是为了解决这个问题。



# Kafka Producer 异步发送消息居然也会阻塞？

Kafka 一直以来都以高吞吐量的特性而家喻户晓，就在上周，在一个性能监控项目中，需要使用到 Kafka 传输海量消息，在这过程中遇到了一个 Kafka Producer 异步发送消息会被阻塞的问题，导致生产端发送耗时很大。

是的，你没听错，Kafka Producer 异步发送消息也会发生阻塞现象，那究竟是怎么回事呢？

在新版的 Kafka Producer 中，设计了一个消息缓冲池，客户端发送的消息都会被存储到缓冲池中，同时 Producer 启动后还会开启一个 Sender 线程，不断地从缓冲池获取消息并将其发送到 Broker，如下图所示：

![img](https://gitee.com/objcoding/md-picture/raw/master/img/20200912172553.png)

这么看来，Kafka 的所有发送，都可以看作是异步发送了，因此在新版的 Kafka Producer 中废弃掉异步发送的方法了，仅保留了一个 send 方法，同时返回一个 Futrue 对象，需要同步等待发送结果，就使用 Futrue#get 方法阻塞获取发送结果。而我在项目中直接调用 send 方法，为何还会发送阻塞呢？

我们在构建 Kafka Producer 时，会有一个自定义缓冲池大小的参数 `buffer.memory`，默认大小为 32M，因此缓冲池的大小是有限制的，我们不妨想一下，缓冲池内存资源耗尽了会怎么样？

Kafka 源码的注释是非常详细的，RecordAccumulator 类是 Kafka Producer 缓冲池的核心类，而 RecordAccumulator 类就有那么一段注释：

> The accumulator uses a bounded amount of memory and append calls will block when that memory is exhausted, unless this behavior is explicitly disabled.

大概的意思是：

当缓冲池的内存块用完后，消息追加调用将会被阻塞，直到有空闲的内存块。

由于性能监控项目每分钟需要发送几百万条消息，只要 Kafka 集群负载很高或者网络稍有波动，Sender 线程从缓冲池捞取消息的速度赶不上客户端发送的速度，就会造成客户端发送被阻塞。

我写个例子让大家直观感受一下被阻塞的现象：

```
public static void main(String[] args) {
  Properties properties = new Properties();
  properties.put(ProducerConfig.ACKS_CONFIG, "0");
  properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
  properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
  properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
  properties.put(ProducerConfig.LINGER_MS_CONFIG, 1000);
  properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024 * 1024);
  properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 5242880);
  properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
  KafkaProducer<String, byte[]> producer = new KafkaProducer<>(properties);
  String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  List<byte[]> bytesList = new ArrayList<>();
  Random random = new Random();
  for (int j = 0; j < 1024; j++) {
    int i1 = random.nextInt(10);
    if (i1 == 0) {
      i1 = 1;
    }
    byte[] bytes = new byte[1024 * i1];
    for (int i = 0; i < bytes.length - 1; i++) {
      bytes[i] = (byte) str.charAt(random.nextInt(62));
    }
    bytesList.add(bytes);
  }

  while (true) {
    long start = System.currentTimeMillis();
    producer.send(new ProducerRecord<>("test_topic", bytesList.get(random.nextInt(1023))));
    long end = System.currentTimeMillis() - start;
    if (end > 100) {
      System.out.println("发送耗时:" + end);
    }
    // Thread.sleep(10);
  }
}
```

以上例子构建了一个 Kafka Producer 对象，同时使用死循环不断地发送消息，这时如果把 `Thread.sleep(10);`注释掉，则会出现发送耗时很长的现象：

![img](https://gitee.com/objcoding/md-picture/raw/master/img/20200912223722.png)

使用 JProfiler 可以查看到分配内存的地方出现了阻塞：

![img](https://gitee.com/objcoding/md-picture/raw/master/img/20200912223106.png)

跟踪到源码：

![img](https://gitee.com/objcoding/md-picture/raw/master/img/20200912223239.png)

发现在 `org.apache.kafka.clients.producer.internals.BufferPool#allocate` 方法中，如果判断缓冲池没有空闲的内存了，则会阻塞内存分配，直到有空闲内存为止。

如果不注释 `Thread.sleep(10);`这段代码则不会发生阻塞现象，打断点到阻塞的地方，也不会被 Debug 到，从现象能够得知，`Thread.sleep(10);`使得发送消息的频率变低了，此时 Sender 线程发送的速度超过了客户端的发送速度，缓冲池一直处于未满状态，因此不会产生阻塞现象。

除了以上缓冲池内存满了会发生阻塞之外，Kafka Produer 其它情况都不会发生阻塞了吗？非也，其实还有一个地方，也会发生阻塞！

Kafka Producer 通常在第一次发送消息之前，需要获取该主题的元数据 Metadata，Metadata 内容包括了主题相关分区 Leader 所在节点信息、副本所在节点信息、ISR 列表等，Kafka Producer 获取 Metadata 后，便会根据 Metadata 内容将消息发送到指定的分区 Leader 上，整个获取流程大致如下：

![img](https://gitee.com/objcoding/md-picture/raw/master/img/20200912190702.png)

如上图所示，Kafka Producer 在发送消息之前，会检查主题的 Metadata 是否需要更新，如果需要更新，则会唤醒 Sender 线程并发送 Metatadata 更新请求，此时 Kafka Producer 主线程则会阻塞等待 Metadata 的更新。

如果 Metadata 一直无法更新，则会导致客户端一直阻塞在那里。
