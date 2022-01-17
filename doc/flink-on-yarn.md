# Flink on Yarn流程图详解

## Yarn通俗介绍

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ad9398d0394e4635b4616b8e5e9c09dc~tplv-k3u1fbpfcp-watermark.awebp)

Apache Hadoop YARN（Yet Another Resource Negotiator，另一种资源协调者），是一个新的Hadoop中的资源管理器，也是一个通用的资源管理系统和调度平台，可以为基于其运行的任务，应用提供统一的资源管理和调度。他的引入为集群在利用率，资源统一管理和数据共享等方面带来巨大好处。 可以把YARN一个分布式集群中的一个平台，他可以为基于Yarn运行的程序应用提供资源（内存，CPU）。

- yarn不关心用户提交程序的具体逻辑是如何如何
- yarn只提供运算资源的调度（第三方应用（用户的应用）向Yarn申请资源，Yarn就分配资源）
- Yarn中的老大是ResourceManager（总的资源管理者，做管理，统筹工作）
- Yarn中具体提供资源的角色是NodeManager（节点的管理者，真正手里有资源的人才可以分配）
- Yarn和用户/第三方程序完全没有耦合，所以Yarn根本不关系你的程序是个啥。什么都可以，比如mapreduce，storm，spark，tez，Flink...
- 只要兼容，符合Yarn的资源请求规范，就都可以放到Yarn运行，向Spark，Storm，Flink
- Yarn成为一个通用的资源调度平台，企业中的各种运算集群都可以整合在一个物理集群上运行，提高资源的利用率，方便数据共享。

## Yarn的基本架构

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/46bbc06d79cc42d6b9eb739729d13d39~tplv-k3u1fbpfcp-watermark.awebp)

YARN作为一个资源管理，任务调度框架，其中主要包含三个模块ResourceManager（RM）<这是老大，做管理>，NodeManager（NM）<这是封疆大吏，手里有真正的资源，可以分配>，Application（AM）<这个监工，负责盯着第三方程序的运行> ResourceManager 负责所有资源的监控。分配和管理 NodeManager 负责每一个节点的维护 ApplicationMaster 负责每一个具体应用程序的调度和协调 对于所有的applications，RM拥有绝对的控制权和对资源的分配权。而每个AM则会和RM协商资源，同时和NodeManager通信来执行和监控task。

## Yarn三大组件介绍

### ResourceManager（老大）

- ResourceManager是老大，负责整个集群的资源分配和管理，一个全局资源统筹系统。
- NodeManager以心跳的方式向ResourceManager汇报资源的使用情况（目前主要是CPU和内存的使用情况）。ResourceManager只接受NodeManager的资源汇报信息，具体的处理全权交给了NodeManager，所以说NodeManager是封疆大吏嘛。
- ResourceManager中的YARN Scheduler组件根据application的请求为其分配资源，不负责具体的job的监控，追踪，运行状态反馈，启动等工作。

### NodeManager（封疆大吏）

- NodeManager是每个节点上的资源和任务管理器，他是管理这台机器的代理，负责该节点程序的运行，以及该节点资源的管理和监控。YARN集群每个节点都运行一个NodeManager。
- NodeManager定时向ResourceManager汇报本节点资源（CPU，内存）的使用情况和Container的运行状态。（封疆大吏定时上京述职，汇报工作）当ResourceManager宕机时NodeManager自动连接ResourceManager备用节点。（皇帝死了，找太子汇报工作）
- NodeManager接收并处理来自ApplicationMaster的Container启动，停止等各种请求。

### ApplicationMaster

- 用户提交的每个应用程序均包含一个ApplicationMaster，他可以运行在ResourceManager以外的机器上。
- 负责与ResourceManger调度器协商获取资源（用Container表示）
- 将得到的资源进一步分配给内部的任务（资源的二次分配）
- 与NodeManager通信以启动/停止任务
- 监控所有任务运行的状态，并在任务运行失败时重新为任务申请资源以重启任务。

## Yarn的运行流程

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/6f23eea1b25f4d5bb670877ed02a9401~tplv-k3u1fbpfcp-watermark.awebp)

- client想老大ResourceManager提交应用，其中需要向老大说明ApplicationManager的必须信息，例如ApplicationMaster程序，启动ApplicationManager的命令，用于程序等
- ResourceManager启动一个container用于运行ApplicationMaster
- 启动中的ApplicationMaster向ResourceManager注册自己，启动成功后与RM保持心跳。
- ApplicationMaster向ResourceManager发送请求，申请相应数目的container
- ResourceManager返回ApplicationMaster申请containers信息。申请成功的container由ApplicationMaster进行初始化。container的启动信息初始化后，AM与对应的NodeManager通信，要求NM启动container。AM与NM保持心跳，从而对NM上运行的任务进行监控和管理。
- container运行期间，ApplicationMaster对container进行监控。container通过RPC协议向对应的AM汇报自己的进度和状态信息。
- 应用运行期间，client直接与AM通信获取应用的状态，进度等更新信息。
- 应用运行结束后，ApplicationMaster想ResourceManager注销自己，并允许属于他的container被回收。

## Flink如何和Yarn进行交互

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f347682ecc894c7a8df845ac7c256cdc~tplv-k3u1fbpfcp-watermark.awebp)

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/99bbc6eff6514d54bfcc88301a3e81a1~tplv-k3u1fbpfcp-watermark.awebp)

1. Client（用户）上传一个jar（一个应用程序，计算任务）到HDFS文件系统中

2. Client向Yarn ResourceManager（老大，最高领导者）提交任务并申请资源

3. ResourceManager（最高领导）分配Container（容器）资源并启动一个ApplicationMaster（相当于一个包工头）加载Flink的jar包（用户的一个项目）和配置构建环境启动JobManager。

   JobManager（这个是Flink中的角色）和ApplicationMaster（这个是Yarn中的角色）运行在同一个container上。

   一旦他们被成功启动，AppMaster就知道JobManager的地址（AM它自己所在的地址就是JobManager的地址）

   他就会为TaskManager（码农，社畜）生成一个新的Flink配置文件（这个配置文件会告诉社畜你们的直属上级是谁，这些社畜就可以直接连接到JobManager）。

   这个配置文件也被上传到HDFS上。

   此外，AppMaster容器也提供了Flink的web服务接口。

   YARN所分配的所有端口都是临时端口，这允许用户并行执行多个Flink。

4. ApplicationMaster（包工头）向ResourceManager（老大，最高领导）申请执行任务所需的资源，NodeManager加载Flink的jar包和配置构建环境并启动TaskManager（真正干活的就是TaskManager，这才是真正的社畜）。

5. TaskManager启动后向JobManager（JobManager是Flink的一个角色，直接管理社畜的小领导）发送心跳包，并等待JobManager向其分配任务。

### 两种方式

#### Session模式

![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/1c9bb5c5a4144aebbf2778da251c4a54~tplv-k3u1fbpfcp-watermark.awebp)

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b939585fc5b94403baa34257e27be2d8~tplv-k3u1fbpfcp-watermark.awebp)

特点：需要事先申请资源，启动JobManager和TaskManager 优点：不需要每次递交作业申请，而是使用以及申请好的资源，从而提高执行效率 缺点：作业执行完成以后，资源不会被释放，因此一直会占用系统资源 应用场景：适合作业递交比较频繁的场景，小作业比较多的场景。

#### Per-Job模式

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/686d7f4f943b4e32ae384b28a6f0c52d~tplv-k3u1fbpfcp-watermark.awebp)

![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/335e855df90044a0958d81636186e02d~tplv-k3u1fbpfcp-watermark.awebp)

特点：每次递交作业都需要申请一次资源。 优点：作业运行完成，资源会立刻被释放，不会一直占用系统资源 缺点：每次递交作业都需要申请资源，会影响执行效率，因为申请资源需要消耗时间 应用场景：适合作业比较少的场景，大作业的场景。