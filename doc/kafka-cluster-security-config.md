### kafka 集群安全认证配置
本文主要介绍下 kafka 0.10.0 版如何实现sasl/plain认证机制及权限控制

#### kafka安全机制
kakfa 的安全机制主要分为两部分：
- 身份认证（Authentication）： 对客户端的身份进行认证
- 权限控制（Authorization）： 对topic级别的权限进行控制

#### kafka 身份认证
kafka 目前支持 SSL，SASL(Kerberos)，SASL（PLAIN) 三种认证机制。
这里只讲解最容易实现的SASL（PLAIN）机制，值的注意的是SASL(PLAIN)是通过明文传输用户名和密码的。因此在不安全的网络环境下需要建立在TLS安全层之上。

#### SASL(PLAIN)认证
##### 服务端配置
在 kafka 安装目录下的 config/server.properties 配置如下信息
```bash
listeners=SASL_PLAINTEXT://hostname:port
security.inter.broker.protocol=SASL_PLAINTEXT
sasl.mechanism.inter.broker.protocol=PLAIN
sasl.enabled.mechanisms=PLAIN
authorizer.class.name = kafka.security.auth.SimpleAclAuthorizer
super.users=User:admin
```
还需要配置一个名为 *==kafka_server_jaas.conf==* 的配置文件，将配置文件放在**conf**目录下。
```
KafkaServer {
    org.apache.kafka.common.security.plain.PlainLoginModule required
    username="admin"
    password="admin-secret"
    user_admin="admin-secret"
    user_alice="alice-secret";
};
```
这里我们配置了两个用户： **admin** 和 **alice** 它们的密码分别为**admin-secret** 和 **alice-secret**。
最后我们在启动 kafka broker 之前导出一个环境变量
```bash
export KAFKA_OPTS='-Djava.security.auth.login.config=conf/kafka_server_jaas.conf'
```
该环境变量在脚本 kafka-run-class.sh 中被传递到broker的jvm中。
然后执行 broker 启动的脚本即可。

##### 客户端的配置
首先要在客户端配置 ==*kafka_client_jaas.conf*== 文件
```
KafkaClient {
  org.apache.kafka.common.security.plain.PlainLoginModule required
  username="alice"
  password="alice";
};
```
然后在客户端的配置中添加如下两项
```bash
security.protocol=SASL_PLAINTEXT
sasl.mechanis=PLAIN
```
配置好后将 kafka_client_jaas.conf 文件传入客户端的jvm中
```bash
-Djava.security.auth.login.config=kafka_client_jaas.conf
```
这样客户端即可运行。如果用户名或密码错误，则客户端不能正常运行，但是不会有任何提示，这个以后应该会改进。

#### kafka 权限的配置
权限的内容

| 权限     | 说明          |
| -------- | ------------- |
| READ     | 读取topic     |
| WRITE    | 写入topic     |
| DELETE   | 删除topic     |
| CREATE   | 创建topic     |
| ALTER    | 修改topic     |
| DESCRIBE | 获取topic信息 |

kafka提供命令行工具来添加和修改acl。该命令行工具位于 kafka 目录 ==bin/kafka-acls.sh==


| Option                 | Description                                                  | Default                                 | Option type   |
| ---------------------- | ------------------------------------------------------------ | --------------------------------------- | ------------- |
| –add                   | Indicates to the script that user is trying to add an acl.   |                                         | Action        |
| –remove                | Indicates to the script that user is trying to remove an acl. |                                         | Action        |
| –list                  | Indicates to the script that user is trying to list acts.    |                                         | Action        |
| –authorizer            | Fully qualified class name of the authorizer.                | kafka.security.auth.SimpleAclAuthorizer | Configuration |
| –authorizer-properties | key=val pairs that will be passed to authorizer for initialization. For the default authorizer the example values are: zookeeper.connect=localhost:2181 |                                         | Configuration |
| –cluster               | Specifies cluster as resource.                               |                                         | Resource      |
| –topic [topic-name]    | Specifies the topic as resource.                             |                                         | Resource      |
| –group [group-name]    | Specifies the consumer-group as resource.                    |                                         | Resource      |
| –allow-principal       | Principal is in PrincipalType:name format that will be added to ACL with Allow permission. You can specify multiple –allow-principal in a single command. |                                         | Principal     |
| –deny-principal        | Principal is in PrincipalType:name format that will be added to ACL with Deny permission. You can specify multiple –deny-principal in a single command. |                                         | Principal     |
| –allow-host            | IP address from which principals listed in –allow-principal will have access.	if –allow-principal is specified defaults to * which translates to “all hosts” |                                         | Host          |
| –deny-host             | IP address from which principals listed in –deny-principal will be denied access.	if –deny-principal is specified defaults to * which translates to “all hosts” |                                         | Host          |
| –operation             | Operation that will be allowed or denied. Valid values are : Read, Write, Create, Delete, Alter, Describe, ClusterAction, All | All                                     | Operation     |
| –producer              | Convenience option to add/remove acls for producer role. This will generate acls that allows WRITE, DESCRIBE on topic and CREATE on cluster. |                                         | Convenience   |
| –consumer              | Convenience option to add/remove acls for consumer role. This will generate acls that allows READ, DESCRIBE on topic and READ on consumer-group. |                                         | Convenience   |

配置例子：
add 操作
```bash
# 为用户 alice 在 test（topic）上添加读写的权限
bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --add --allow-principal User:alice --operation Read --operation Write --topic test
```
list 操作
```bash
# 列出 topic 为 test 的所有权限账户
bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --list --topic test
```
remove 操作
```bash
# 移除 Alice 在 test(topic) 上的读写权限
bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --remove --allow-principal User:Alice --operation Read --operation Write --topic test
```
producer 和 consumer 的操作
```bash
# producer
bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --add --allow-principal User:alice --producer --topic test
#consumer
bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --add --allow-principal User:alice --consumer --topic test --group test-group
```