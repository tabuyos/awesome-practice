Zookeeper 保证了 CP(C: 一致性, P: 分区容错性), Eureka 保证了 AP(A: 高可用)

1. 当向注册中心查询服务列表时, 我们可以容忍注册中心返回的是几分钟以前的信息,
   但不能容忍直接 down 掉不可用. 也就是说, 服务注册功能对高可用性要求比较高,
   但 zk 会出现这样一种情况, 当 master 节点因为网络故障与其他节点失去联系时,
   剩余节点会重新选 leader. 问题在于, 选取 leader 时间过长, 30 ~ 120s,
   且选取期间 zk 集群都不可用, 这样就会导致选取期间注册服务瘫痪. 在云部署的环境下,
   因网络问题使得 zk 集群失去 master 节点是较大概率会发生的事, 虽然服务能够恢复,
   但是漫长的选取时间导致的注册长期不可用是不能容忍的.

2. Eureka 保证了可用性, Eureka 各个节点是平等的, 几个节点挂掉不会影响正常节点的工作,
   剩余的节点仍然可以提供注册和查询服务. 而 Eureka 的客户端向某个 Eureka 注册或发现时发生连接失败,
   则会自动切换到其他节点, 只要有一台 Eureka 还在, 就能保证注册服务可用,
   只是查到的信息可能不是最新的. 除此之外, Eureka 还有自我保护机制,
   如果在15分钟内超过 85% 的节点没有正常的心跳, 那么 Eureka 就认为客户端与注册中心发生了网络故障,
   此时会出现以下几种情况:

> 1. Eureka 不在从注册列表中移除因为长时间没有收到心跳而应该过期的服务.
>
> 2. Eureka 仍然能够接受新服务的注册和查询请求, 但是不会被同步到其他节点上(即保证当前节点仍然可用)
>
> 3. 当网络稳定时, 当前实例新的注册信息会被同步到其他节点.

因此, Eureka 可以很好的应对因网络故障导致部分节点失去联系的情况, 而不会像 Zookeeper 那样使整个微服务瘫痪.
