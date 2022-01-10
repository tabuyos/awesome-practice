因为公司进行系统的服务化拆分，导致模块骤增，随之而来配置文件管理难度也随之增加，所以想采用一个配置集中管理的中间件。

下面对市面比较流行的Naocs和Apollo从各方面进行比较。

**1. 配置中心**

**1.1 什么是配置**

应用程序在启动和运行的时候往往需要读取一些配置信息，配置基本上伴随着应用程序的整个生命周期，比如：数 据库连接参数、启动参数等。

配置主要有以下几个特点：

- **配置是独立于程序的只读变量**

配置对于程序是只读的，程序通过读取配置来改变自己的行为，但是程序不应该去改变配置

- **配置伴随应用的整个生命周期**

配置贯穿于应用的整个生命周期，应用在启动时通过读取配置来初始化，在运行时根据配置调整行为。

比如：启动时需要读取服务的端口号、系统在运行过程中需要读取定时策略执行定时任务等。

- **配置可以有多种加载方式**

常见的有程序内部hard code，配置文件，环境变量，启动参数，基于数据库等

- **配置需要治理**

同一份程序在不同的环境(开发，测试，生产)、不同的集群(如不同的数据中心)经常需要有不同的配置，所以需要有完善的环境、集群配置管理

**1.2 什么是配置中心**

在分布式服务架构中，当系统从一个单体应用，被拆分成分布式系统上一个个服务节点后，配置文件也必须跟着迁移 (分割)，这样配置就分散了，不仅如此，分散中还包含着冗余，如下图

[![img](https://s2.51cto.com/oss/202102/10/183573216d3e295eef7ba8273c28a910.png-wh_600x-s_3065168856.png)](https://s2.51cto.com/oss/202102/10/183573216d3e295eef7ba8273c28a910.png-wh_600x-s_3065168856.png)

而配置中心将配置从各应用中剥离出来，对配置进行统一管理，应用自身不需要自己去管理配置。如下图

[![img](https://s2.51cto.com/oss/202102/10/bfd4e92308f412ae6159600ca6f474bd.png-wh_600x-s_1386817465.png)](https://s2.51cto.com/oss/202102/10/bfd4e92308f412ae6159600ca6f474bd.png-wh_600x-s_1386817465.png)

配置中心的服务流程如下：

1、用户在配置中心发布、更新配置信息。

2、服务A和服务B及时得到配置更新通知，从配置中心获取配置。

**总得来说，配置中心就是一种统一管理各种应用配置的基础服务组件。**

**1.3 为什么需要配置中心**

随分布式微服务的发展，服务节点越来越多，配置问题逐渐显现出来：

- 随着程序功能的日益复杂，程序的配置日益增多，各种功能的开关、参数的配置、服务器的地址
- 大量模块使用各自的配置，可能导致运维繁琐、管理混乱、各个节点配置文件不一致
- 对配置的期望也越来越高，配置修改后实时生效，灰度发布， 版本管理 ，环境区分，完善的权限、审核机制等

在这样的大环境下，传统的通过配置文件、数据库等方式已经越来越无法满足开发人员对配置管理的需求。

**1.4 配置中心小结**

总结一下，在传统巨型单体应用纷纷转向分布式服务架构的历史进程中，配置中心是服务化不可缺少的一个系统组件，在这种背景下中心化的配置服务即配置中心应运而生，一个合格的配置中心需要满足如下特性：

- 配置项容易读取和修改
- 分布式环境下应用配置的可管理性，即提供远程管理配置的能力
- 支持对配置的修改的检视以把控风险
- 可以查看配置修改的历史记录
- 不同部署环境下应用配置的隔离性

整个配置中心的作用系统运行时能够动态调整程序的行为。

**2. 开源配置中心介绍**

目前市面流行的配置中心有：

- **Disconf**

2014年7月，百度开源的配置管理中心，同样具备配置的管理能力，不过目前已经不维护了 。

- **Spring Cloud Config**

2014年9月，Spring Cloud 开源生态组件，可以和Spring Cloud体系无缝整合，但依赖Git或SVN 。

- **Apollo**

2016年5月，携程开源的配置管理中心，具备规范的权限、流程治理等特性。

- **Nacos**

2018年6月，阿里开源的配置中心，也可以做RPC的服务发现。

因Disconf不再维护，且Spring Cloud Config 需要依赖Git或SVN。所以只介绍下Apollo和Nacos

**2.1 Nacos**

Nacos包含的注册中心+配置中心，以下只说配置中心。

**2.1.1 简介**

Nacos 致力于帮助服务发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。

Nacos 更敏捷和容易地构建、交付和管理微服务平台。Nacos 是构建以“服务”为中心的现代应用架构 (例如微服务范式、云原生范式) 的服务基础设施。

- Nacos文档中心地址：https://nacos.io/zh-cn/docs/what-is-nacos.html

**2.1.2 特性**

Nacos 支持几乎所有主流类型的**服务发现、配置和管理**，现只说Nacos的**配置中心**功能。

- 动态配置服务可以让您以中心化、外部化和动态化的方式管理所有环境的应用配置和服务配置。
- 动态配置消除了配置变更时重新部署应用和服务的需要，让配置管理变得更加高效和敏捷。
- 配置中心化管理让实现无状态服务变得更简单，让服务按需弹性扩展变得更容易。
- Nacos 提供了一个简洁易用的UI帮助您管理所有的服务和应用的配置。
- Nacos 还提供包括配置**版本跟踪、金丝雀发布、一键回滚配置以及客户端配置更新状态跟踪**在内的一系列开箱即用的配置管理特性，帮助您更安全地在生产环境中管理配置变更和降低配置变更带来的风险。

**2.1.3 架构**

Nacos配置中心分为Server与Client，server采用Java编写，为client提供配置服务。

Client可以用多语言实现，Client与服务模块嵌套在一起，Nacos提供SDK和OpenAPI，如果没有SDK也可以根据OpenAPI手动写服务注册与发现和配置拉取的逻辑 。

配置中心架构图：

[![img](https://s4.51cto.com/oss/202102/10/d5a78ce15dba77a1b9f79a09e7817164.png)](https://s4.51cto.com/oss/202102/10/d5a78ce15dba77a1b9f79a09e7817164.png)

- 用户通过Nacos Server的控制台集中对多个服务的配置进行管理。
- 各服务统一从Nacos Server集群中获取各自的配置，并监听配置的变化。

**2.1.4 开发**

Nacos配置中心支持与Spring、Spring Boot、Spring Cloud整合，通过xml或注解方式即可轻松实现。演示下与Spring项目进行整合。

1.服务端

[![img](https://s6.51cto.com/oss/202102/10/b6dc139ca92f4ecb89407c76b1d9a510.png-wh_600x-s_3117570668.png)](https://s6.51cto.com/oss/202102/10/b6dc139ca92f4ecb89407c76b1d9a510.png-wh_600x-s_3117570668.png)

控制台发布配置截图

- Nacos服务端增加useLocalCacheSwitch配置，用于控制是否使用缓存
- 发布配置

2.客户端



```
<dependency>      <groupId>com.alibaba.nacos</groupId>      <artifactId>nacos-client</artifactId>      <version>1.4.1</version>  </dependency>  <dependency>      <groupId>com.alibaba.nacos</groupId>      <artifactId>nacos-spring-context</artifactId>      <version>1.0.0</version>  </dependency>
<!--NacosServer地址--> <nacos:global-properties server-addr="192.168.134.128:8848" /> <!--在NacosServer配置的文件--> <nacos:property-source data-id="application.properties"                         group-id="redirectpaymentservice"                         auto-refreshed="true"/>
@Service("Tx2101") public class Tx2101 extends TxBase {      @NacosValue(value = "${useLocalCacheSwitch}", autoRefreshed = true)     private boolean useLocalCacheSwitch;      @Override     public Document process(Document document) throws CodeException {         System.out.println("是否刷新缓存：" + useLocalCacheSwitch);         return null;     } }
```

- 编写java代码，动态刷新配置
- applicationContext.xml增加NacosServer的相关配置
- pom.xml 增加nacos-client的依赖

3.效果

```
是否刷新缓存：false
是否刷新缓存：true
```

- 在Nacos服务端改变useLocalCacheSwitch的配置后，再次访问2101接口，打印如下：
- 模块启动后访问2101接口，打印如下：

**2.1.5 灰度**

Nacos服务端修改配置后，勾选Beat发布，指定IP地址，然后选择发布Beta。

[![img](https://s4.51cto.com/oss/202102/10/e907794d64f0a931f866cad063c95850.png-wh_600x-s_749277881.png)](https://s4.51cto.com/oss/202102/10/e907794d64f0a931f866cad063c95850.png-wh_600x-s_749277881.png)

- 只有指定的IP节点的配置被更新

**2.1.5 部署**

在单机模式下，Nacos没有任何依赖，默认使用内嵌的数据库作为存储引擎，也可换成mysql;在集群模式下，Nacos依赖Mysql做存储。

生产环境使用Nacos为了达到高可用不能使用单机模式，需要搭建nacos集群。

下图是官方推荐的集群方案，通过域名 + VIP模式的方式来实现。客户端配置的nacos，当Nacos集群迁移时，客户端配置无需修改。

集群部署架构图：

[![img](https://s6.51cto.com/oss/202102/10/9119c3f758a947b798e0154f4b815c82.png-wh_600x-s_1031767502.png)](https://s6.51cto.com/oss/202102/10/9119c3f758a947b798e0154f4b815c82.png-wh_600x-s_1031767502.png)

集群部署架构图

**2.1.7 小结**

Nacos使用简单、部署方便、性能较高，能够实现基本的配置管理，提供的控制台也非常简洁。

但权限方面控制粒度较粗，且没有审核机制。

**2.2 Apollo**

**2.2.1 简介**

Apollo(阿波罗)是携程框架部门研发的分布式配置中心，能够集中化管理应用的不同环境、不同集群的配置，配 置修改后能够实时推送到应用端，并且具备规范的权限、流程治理等特性，适用于微服务配置管理场景。

Apollo包括服务端和客户端两部分：

服务端基于Spring Boot和Spring Cloud开发，打包后可以直接运行，不需要额外安装Tomcat等应用容器。Java客户端不依赖任何框架，能够运行于所有Java运行时环境，同时对Spring/Spring Boot环境也有较好的支持。

- 文档：https://github.com/ctripcorp/apollo/wiki。

**2.2.2 特性**

基于配置的特殊性，所以Apollo从设计之初就立志于成为一个有治理能力的配置发布平台，目前提供了以下的特性：

- 统一管理不同环境、不同集群的配置
- 配置修改实时生效(热发布)
- 版本发布管理
- 灰度发布
- 权限管理、发布审核、操作审计
- 客户端配置信息监控
- 提供Java和.Net原生客户端
- 提供开放平台API
- 部署简单

**2.2.3 架构**

Apollo架构从外部和内部进行分析

- 地址：https://ctripcorp.github.io/apollo/#/zh/design/apollo-design

**基础模型**

如下即是Apollo的**基础模型**：

1. [鸿蒙官方战略合作共建——HarmonyOS技术社区](https://harmonyos.51cto.com/#kfxqy)
2. 用户在配置中心对配置进行修改并发布
3. 配置中心通知Apollo客户端有配置更新
4. Apollo客户端从配置中心拉取最新的配置、更新本地配置并通知到应用

[![img](https://s3.51cto.com/oss/202102/10/ef968183ea5c1f96b7692f42814fc338.png-wh_600x-s_3632254553.png)](https://s3.51cto.com/oss/202102/10/ef968183ea5c1f96b7692f42814fc338.png-wh_600x-s_3632254553.png)

**2.2.4 开发**

Apollo支持API方式和Spring整合方式 。

API方式灵活，功能完备，配置值实时更新(热发布)，支持所有Java环境。

Spring方式接入简单，如

- 代码中直接使用，如：@Value("${someKeyFromApollo:someDefaultValue}")
- 直接托管spring的配置，如在apollo中直接配置spring.datasource.url=jdbc:mysql://localhost:3306/somedb?characterEncoding=utf8
- Placeholder方式：
- Spring boot的@ConfigurationProperties方式

Spring方式也可以结合API方式使用，如注入Apollo的Config对象，就可以照常通过API方式获取配置了

下面只介绍下Spring整合Apollo方式，做一个演示：

1.服务端

[![img](https://s2.51cto.com/oss/202102/10/fda1f2e33a8c630dbbe70560ed6e3af3.png-wh_600x-s_658607968.png)](https://s2.51cto.com/oss/202102/10/fda1f2e33a8c630dbbe70560ed6e3af3.png-wh_600x-s_658607968.png)

- 控制台添加useLocalCacheSwitch配置，用于控制是否使用缓存
- 发布配置

2.客户端



```
<dependency>      <groupId>com.ctrip.framework.apollo</groupId>       <artifactId>apollo-client</artifactId>       <version>1.1.0</version>  </dependency>
<apollo:config/>
-Dapp.id=RedirectPaymentService -Denv=DEV -Ddev_meta=http://localhost:8080
@Service("Tx2101") public class Tx2101 extends TxBase {      @Value("${useLocalCacheSwitch:false}")     private boolean useLocalCacheSwitch;      @Override     public Document process(Document document) throws CodeException {         System.out.println("是否刷新缓存：" + useLocalCacheSwitch);         return null;     } }
```

- 编写java代码，动态刷新配置
- VM options启动参数
- applicationContext.xml增加apollo相关配置
- pom.xml 增加apollo-client的依赖

3.效果

```
是否刷新缓存：false
是否刷新缓存：true
```

- 在Apollo控制台改变useLocalCacheSwitch的配置后，再次访问2101接口，打印如下：
- 模块启动后访问2101接口，打印如下：

**2.2.5 灰度**

Apollo控制台创建灰度版本，配置灰度规则，指定灰度的IP或AppID。

[![img](https://s5.51cto.com/oss/202102/10/fd544d48d4f5635e5ffd4eef34c8d134.png-wh_600x-s_212929867.png)](https://s5.51cto.com/oss/202102/10/fd544d48d4f5635e5ffd4eef34c8d134.png-wh_600x-s_212929867.png)

指定的IP节点或AppID模块的配置被更新

**2.2.6 部署**

Apollo高可用架构模块的概览 :

[![img](https://s6.51cto.com/oss/202102/10/ba8b76e69ff4d3b3674b62a56100fbb0.png-wh_600x-s_3728807059.png)](https://s6.51cto.com/oss/202102/10/ba8b76e69ff4d3b3674b62a56100fbb0.png-wh_600x-s_3728807059.png)

上图简要描述了Apollo的总体设计，我们可以从下往上看：

- Config Service提供配置的读取、推送等功能，服务对象是Apollo客户端
- Admin Service提供配置的修改、发布等功能，服务对象是Apollo Portal(管理界面)
- Config Service和Admin Service都是多实例、无状态部署，所以需要将自己注册到Eureka中并保持心跳
- 在Eureka之上我们架了一层Meta Server用于封装Eureka的服务发现接口
- Client通过域名访问Meta Server获取Config Service服务列表(IP+Port)，而后直接通过IP+Port访问服务，同时在Client侧会做load balance、错误重试
- Portal通过域名访问Meta Server获取Admin Service服务列表(IP+Port)，而后直接通过IP+Port访问服务，同时在Portal侧会做load balance、错误重试
- 为了简化部署，我们实际上会把Config Service、Eureka和Meta Server三个逻辑角色部署在同一个JVM进程中

**2.2.7 小结**

Apollo在配置管理流程上比较完善，相应配置的发布审核、权限管理等、配置的继承等，但Apollo需要使用人员进行简单学习，存在学习成本。

Appollo部署较为复杂需要3个模块同时工作，部署一套生产高可用集群至少需要7个节点。

**3. 总结**

**3.1 功能比较**

[![img](https://s3.51cto.com/oss/202102/10/71fb72a5d416ac50d9ab77f8dcc0e4fb.png-wh_600x-s_1540993293.png)](https://s3.51cto.com/oss/202102/10/71fb72a5d416ac50d9ab77f8dcc0e4fb.png-wh_600x-s_1540993293.png)

**3.2 结论**

从配置中心角度来看，性能方面Nacos的读写性能最高，Apollo次之;功能方面Apollo最为完善，但Nacos具有Apollo大部分配置管理功能。Nacos的一大优势是整合了注册中心、配置中心功能，部署和操作相比 Apollo都要直观简单，因此它简化了架构复杂度，并减轻运维及部署工作。

总的来看，Apollo和Nacos生态支持都很广泛，在配置管理流程上做的都很好。Apollo相对于Nacos在配置管理做的更加全面;Nacos则使用起来相对比较简洁，在对性能要求比较高的大规模场景更适合。

对于公司目前来说，修改配置的次数不是特别的频繁，对于配置权限的管理不是特别严格的，且对读写性能有一定要求的，可采用Nacos，反之使用Apollo。
