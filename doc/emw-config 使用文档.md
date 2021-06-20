# 1. 使用场景
在有些场景下，不希望依赖公司的统一配置中心作为配置管理中间件，因为这额外增加了外部依赖。例如：

1. 你的应用是一个开源软件
1. 你的应用作为一个云厂商提供的服务
1. 公司没有统一的配置中心服务，但是又需要动态配置管理的能力

如果依赖外部的配置中心，那么意味着输出的时候需要带上配置中心，这显然会增加输出成本。但是几乎任何应用都依赖配置动态更新的能力，那如何实现呢？一种实现方式就是通过自身应用的数据库实现配置中心的能力。
# 2. 使用方式
## 2.1 新增 emw-config 表
```sql
CREATE TABLE `emw_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `app_id` varchar(255) DEFAULT 'default' COMMENT '应用名',
  `namespace` varchar(255) NOT NULL DEFAULT '' COMMENT 'namespace名称',
  `profile` varchar(128) NOT NULL DEFAULT 'default' COMMENT 'namespace 不同的版本',
  `value` longtext NOT NULL COMMENT 'properties 文件内容',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `context` varchar(10240) DEFAULT NULL COMMENT '上下文信息',
  `gmt_create` date DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` date DEFAULT NULL COMMENT '最后更新时间',
  `operator` varchar(64) DEFAULT NULL COMMENT '最后操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_namespace_cluster` (`app_id`,`namespace`,`profile`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='emw 配置文件表';
```
表字段说明：

1. app_id
   1. 一个数据库可能被多个应用同时使用，为了隔离开多个应用的配置，使用 app_id 逻辑隔离，默认值为 default
2. namespace
   1. 类似于文件名的概念，在 spring boot 应用中，有一系列的配置文件，文件名格式为 {文件名}-{profile}.properties，例如 application-dev.properties，其中 application 为文件名（namespace），dev 为 profile
3. profile
   1. 同一个配置文件，根据不同的环境，集群可以有不同的值。通过 profile 参数来区分。类似于 application-dev.properties 中的 dev
4. value
   1. 目前仅支持 properties 文件格式的字符串，换行符为 \n
5. version
   1. 内置的字段，用来判断文件内容是否有更新
6. context
   1. 扩展字段，记录文件的上下文信息



### 2.2 emw-config 插入一条数据
value 的格式为 properties 文件格式，例如:
```sql
timeout=100
batch=300
```
```sql
INSERT INTO `emw_config` (`app_id`, `namespace`, `profile`, `value`, `version`, `context`, `gmt_create`, `gmt_modified`, `operator`)
VALUES
	('default', 'application', 'default', 'timeout=100\nbatch=300', 1, NULL, NULL, NOW(), NOW(), NULL);

```
## 2.2 新增 emw-config 依赖
clone 仓库代码到本地，并在根目录下执行：
> mvn clean install -Dmaven.test.skip=true

项目里新增 emw-config maven 依赖
```sql
<dependency>
  <groupId>org.lepdou.framework.emw</groupId>
  <artifactId>emw-config</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
## 2.3 Java 原生方式使用 emw-config
### 2.3.1 初始化 emw-config 模块
因为依赖应用自身的数据库，所以需要传入数据库的连接信息。
```java
String jdbcUrl = "jdbc:mysql://localhost:3306/emw";
String username = "root";
String password = "admin";

EMWConfigStarter.run(EMWConfigParams.createFromBaseParams(jdbcUrl, username, password));
```
### 2.3.2 获取 timeout 参数值
```java
//获取 application namespace 配置对象
Config config = ConfigService.getAppConfig();
//获取 timeout 参数
config.getIntProperty("timeout", 100);
//获取 batch 参数
config.getIntProperty("batch", 100);
```
### 2.3.3 增加配置变更监听器
```java
//1. 创建 listener 对象
ConfigChangeListener changeListener = changeEvent -> {

            logger.info("Changes for namespace {}", changeEvent.getNamespace());

            for (String key : changeEvent.changedKeys()) {
                ConfigChange change = changeEvent.getChange(key);
                logger.info("Change - key: {}, oldValue: {}, newValue: {}, changeType: {}",
                        change.getPropertyName(), change.getOldValue(), change.getNewValue(),
                        change.getChangeType());
            }
        };
//2. 添加监听器
config.addChangeListener(changeListener);
```
## 2.4 Spring Annotation 的方式
### 2.4.1 新增配置类
```java
@Configuration
//注入 application namespace
@EnableApolloConfig(value = "application", order = 10)
//emw-config 启动参数
@EnableEMWConfig(jdbcUrl = "jdbc:mysql://localhost:3306/emw", username = "root", password = "admin")
public class AppConfig {
}
```
### 2.4.2 新增 Spring Bean 并通过 @Value 注入
```java
@Component("annotatedBean")
public class AnnotatedBean {
    private static final Logger logger = LoggerFactory.getLogger(AnnotatedBean.class);

    private int            timeout;
    private int            batch;
    private List<JsonBean> jsonBeans;

    /**
     * ApolloJsonValue annotated on fields example, the default value is specified as empty list - []
     * 

     * jsonBeanProperty=[{"someString":"hello","someInt":100},{"someString":"world!","someInt":200}]
     */
    @ApolloJsonValue("${jsonBeanProperty:[]}")
    private List<JsonBean> anotherJsonBeans;

    @Value("${batch:100}")
    public void setBatch(int batch) {
        logger.info("updating batch, old value: {}, new value: {}", this.batch, batch);
        this.batch = batch;
    }

    @Value("${timeout:200}")
    public void setTimeout(int timeout) {
        logger.info("updating timeout, old value: {}, new value: {}", this.timeout, timeout);
        this.timeout = timeout;
    }

    /**
     * ApolloJsonValue annotated on methods example, the default value is specified as empty list - []
     * 

     * jsonBeanProperty=[{"someString":"hello","someInt":100},{"someString":"world!","someInt":200}]
     */
    @ApolloJsonValue("${jsonBeanProperty:[]}")
    public void setJsonBeans(List<JsonBean> jsonBeans) {
        logger.info("updating json beans, old value: {}, new value: {}", this.jsonBeans, jsonBeans);
        this.jsonBeans = jsonBeans;
    }

    @Override
    public String toString() {
        return String.format("[AnnotatedBean] timeout: %d, batch: %d, jsonBeans: %s", timeout, batch, jsonBeans);
    }

    private static class JsonBean {

        private String someString;
        private int    someInt;

        @Override
        public String toString() {
            return "JsonBean{" +
                    "someString='" + someString + '\'' +
                    ", someInt=" + someInt +
                    '}';
        }
    }
}
```
### 2.4.3 启动应用
```java
/**
 *  基于 Java 注解的方式启动 emw-config 模块
 * @see com.lepdou.framework.emw.config.spring.javaConfigDemo.config.AppConfig
 */
public class AnnotationApplication {

    public static void main(String[] args) throws InterruptedException {
        // 加载 Spring 上下文
        ApplicationContext context = new AnnotationConfigApplicationContext("com.lepdou.framework.emw.config.spring.javaConfigDemo");
        AnnotatedBean annotatedBean = context.getBean(AnnotatedBean.class);

        while (true) {
            TimeUnit.SECONDS.sleep(4);
            System.err.println(annotatedBean.toString());
        }
    }
}
```
## 2.5 Spring Boot 方式使用
### 2.5.1 application.yaml 里配置相关参数
```java
apollo:
  bootstrap:
    enabled: true
    # 注入的namespace列表
    namespaces: application,TEST1.apollo,application.yaml

# emw 启动参数
emw:
  emw-config:
    bootstrap:
      jdbc:
        url: jdbc:mysql://localhost:3306/emw
        username: root
        password: admin
```
### 2.5.2 Spring Bean 通过 @Value 注入
```java
@Component("annotatedBean")
public class AnnotatedBean {
    private static final Logger logger = LoggerFactory.getLogger(AnnotatedBean.class);

    private int            timeout;
    private int            batch;
    private List<JsonBean> jsonBeans;

    /**
     * ApolloJsonValue annotated on fields example, the default value is specified as empty list - []
     * 

     * jsonBeanProperty=[{"someString":"hello","someInt":100},{"someString":"world!","someInt":200}]
     */
    @ApolloJsonValue("${jsonBeanProperty:[]}")
    private List<JsonBean> anotherJsonBeans;

    @Value("${batch:100}")
    public void setBatch(int batch) {
        logger.info("updating batch, old value: {}, new value: {}", this.batch, batch);
        this.batch = batch;
    }

    @Value("${timeout:200}")
    public void setTimeout(int timeout) {
        logger.info("updating timeout, old value: {}, new value: {}", this.timeout, timeout);
        this.timeout = timeout;
    }

    /**
     * ApolloJsonValue annotated on methods example, the default value is specified as empty list - []
     * 

     * jsonBeanProperty=[{"someString":"hello","someInt":100},{"someString":"world!","someInt":200}]
     */
    @ApolloJsonValue("${jsonBeanProperty:[]}")
    public void setJsonBeans(List<JsonBean> jsonBeans) {
        logger.info("updating json beans, old value: {}, new value: {}", this.jsonBeans, jsonBeans);
        this.jsonBeans = jsonBeans;
    }

    @Override
    public String toString() {
        return String.format("[AnnotatedBean] timeout: %d, batch: %d, jsonBeans: %s", timeout, batch, jsonBeans);
    }

    private static class JsonBean {

        private String someString;
        private int    someInt;

        @Override
        public String toString() {
            return "JsonBean{" +
                    "someString='" + someString + '\'' +
                    ", someInt=" + someInt +
                    '}';
        }
    }
}
```
### 2.5.3 通过 @ConfigurationProperties 注入
```java
package com.lepdou.framework.emw.config.spring.springBootDemo.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * You may set up data like the following in Apollo:
 * 


 * Properties Sample: application.properties
 * <pre>
 * redis.cache.enabled = true
 * redis.cache.expireSeconds = 100
 * redis.cache.clusterNodes = 1,2
 * redis.cache.commandTimeout = 50
 * redis.cache.someMap.key1 = a
 * redis.cache.someMap.key2 = b
 * redis.cache.someList[0] = c
 * redis.cache.someList[1] = d
 * </pre>
 *
 * Yaml Sample: application.yaml
 * <pre>
 * redis:
 *   cache:
 *     enabled: true
 *     expireSeconds: 100
 *     clusterNodes: 1,2
 *     commandTimeout: 50
 *     someMap:
 *       key1: a
 *       key2: b
 *     someList:
 *     - c
 *     - d
 * </pre>
 *
 * To make <code>@ConditionalOnProperty</code> work properly, <code>apollo.bootstrap.enabled</code> should be set to true
 * and <code>redis.cache.enabled</code> should also be set to true. Check 'src/main/resources/application.yml' for more information.
 *
 */
@ConfigurationProperties(prefix = "redis.cache")
@Component("sampleRedisConfig")
public class SampleRedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(SampleRedisConfig.class);

    private int    expireSeconds;
    private String clusterNodes;
    private int    commandTimeout;

    private Map<String, String> someMap  = Maps.newLinkedHashMap();
    private List<String>        someList = Lists.newLinkedList();

    @PostConstruct
    private void initialize() {
        logger.info(
                "SampleRedisConfig initialized - expireSeconds: {}, clusterNodes: {}, commandTimeout: {}, someMap: {}, someList: {}",
                expireSeconds, clusterNodes, commandTimeout, someMap, someList);
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public void setCommandTimeout(int commandTimeout) {
        this.commandTimeout = commandTimeout;
    }

    public Map<String, String> getSomeMap() {
        return someMap;
    }

    public List<String> getSomeList() {
        return someList;
    }

    @Override
    public String toString() {
        return String.format(
                "[SampleRedisConfig] expireSeconds: %d, clusterNodes: %s, commandTimeout: %d, someMap: %s, someList: %s",
                expireSeconds, clusterNodes, commandTimeout, someMap, someList);
    }
}

```
## 2.6 Spring XML 方式
### 2.6.1 XML 配置文件
```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:apollo="http://www.ctrip.com/schema/apollo"
       xmlns:emwconfig="http://www.lepdou.org/schema/emwconfig"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.ctrip.com/schema/apollo http://www.ctrip.com/schema/apollo.xsd
       http://www.lepdou.org/schema/emwconfig http://www.lepdou.org/schema/emwconfig.xsd">

    <!--    初始化 emwconfig 模块，注意保证 emwconfig-starter 在 apollo:config 之前，这样才能保证在注入apollo config之前获取到配置-->
    <emwconfig:starter jdbcUrl="jdbc:mysql://localhost:3306/emw" username="root" password="admin"/>
    <!-- 注入 applicaiton namespace配置 -->
    <apollo:config order="10"/>
    <!-- 注入 TEST1.apollo,application.yaml -->
    <apollo:config namespaces="TEST1.apollo,application.yaml" order="11"/>

    <bean class="com.lepdou.framework.emw.config.spring.xmlConfigDemo.bean.XmlBean">
        <property name="timeout" value="${timeout:200}"/>
        <property name="batch" value="${batch:100}"/>
    </bean>

    <context:annotation-config/>
</beans>

```
# 3. 配置管理接口
emw-config 提供了两种形式的管理接口：

1. Java 原生的方式
1. Restful 接口
## 3.1 创建 or 更新配置
**方式一：Java 接口**
> EMWConfigManagerFacade.createOrUpdateNamespace(ConfigDO config)

**方式二：Restful 接口**
> curl -d'login=emma\npassword=123'-X POST https://127.0.0.1:18080/emw/config

## 3.2 查询配置
**方式一：Java 接口**
> EMWConfigManagerFacade.findNamespace(String appId, String namespace, String profile)

**方式二：Restful 接口**
> curl -d'login=emma\npassword=123'-X GET https://127.0.0.1:18080/emw/config

# 4. 设置应用启动参数
## 4.1 AppId
AppId是应用的身份信息，是从服务端获取配置的一个重要信息。
有以下几种方式设置，按照优先级从高到低分别为：

1. System Property

Apollo 0.7.0+支持通过System Property传入app.id信息，如
> -Dapp.id=YOUR-APP-ID点击复制错误复制成功

2. 操作系统的System Environment

Apollo 1.4.0+支持通过操作系统的System Environment APP_ID来传入app.id信息，如
> APP_ID=YOUR-APP-ID点击复制错误复制成功

3. Spring Boot application.properties

Apollo 1.0.0+支持通过Spring Boot的application.properties文件配置，如
> app.id=YOUR-APP-ID点击复制错误复制成功

**该配置方式不适用于多个war包部署在同一个tomcat的使用场景**

4. app.properties

确保classpath:/META-INF/app.properties文件存在，并且其中内容形如：
> **app.id=YOUR-APP-ID**

文件位置参考如下：
![](https://raw.githubusercontent.com/ctripcorp/apollo/master/apollo-client/doc/pic/app-id-location.png#from=url&id=krJtj&margin=%5Bobject%20Object%5D&originHeight=153&originWidth=241&originalType=binary&ratio=2&status=done&style=none)
**注：app.id是用来标识应用身份的唯一id，格式为string。**
## 4.2 cluster
1.0.0版本开始支持以下方式集群，按照优先级从高到低分别为：

1. 通过Java System Property apollo.cluster
   - 可以通过Java的System Property apollo.cluster来指定
   - 在Java程序启动脚本中，可以指定-Dapollo.cluster=SomeCluster
      - 如果是运行jar文件，需要注意格式是java -Dapollo.cluster=SomeCluster -jar xxx.jar
   - 也可以通过程序指定，如System.setProperty("apollo.cluster", "SomeCluster");
2. 通过Spring Boot的配置文件
   - 可以在Spring Boot的application.properties或bootstrap.properties中指定apollo.cluster=SomeCluster
3. 通过Java System Property
   - 可以通过Java的System Property idc来指定环境
   - 在Java程序启动脚本中，可以指定-Didc=xxx
      - 如果是运行jar文件，需要注意格式是java -Didc=xxx -jar xxx.jar
   - 注意key为全小写
4. 通过操作系统的System Environment
   - 还可以通过操作系统的System Environment IDC来指定
   - 注意key为全大写
5. 通过server.properties配置文件
   - 可以在server.properties配置文件中指定idc=xxx
   - 对于Mac/Linux，默认文件位置为/opt/settings/server.properties
   - 对于Windows，默认文件位置为C:\opt\settings\server.properties

**Cluster Precedence**（集群顺序）

1. 如果apollo.cluster和idc同时指定：
   - 我们会首先尝试从apollo.cluster指定的集群加载配置
   - 如果没找到任何配置，会尝试从idc指定的集群加载配置
   - 如果还是没找到，会从默认的集群（default）加载
2. 如果只指定了apollo.cluster：
   - 我们会首先尝试从apollo.cluster指定的集群加载配置
   - 如果没找到，会从默认的集群（default）加载
3. 如果只指定了idc：
   - 我们会首先尝试从idc指定的集群加载配置
   - 如果没找到，会从默认的集群（default）加载
4. 如果apollo.cluster和idc都没有指定：
   - 我们会从默认的集群（default）加载配置
# 5. 自定义参数
所有自定义参数都在 EMWConfigParams 里维护，启动时读取 EMWConfigParams。不同的启动方式有不同的设置方式。参考 jdbcUrl，paasword，username 参数设置方式。
## 5.1 pollingInterval
因为 emw-config 是通过轮训数据库是感知配置变更的，所以轮训的时间越短灵敏度越高，但是对数据库的压力越大。默认为 3000 ms
## 5.2 pollingDBLogSampleRate
轮训数据库打印日志的采样比例，如果设置为 100，则每次轮训都打印日志。
## 5.3 restPort
rest server 暴露的端口号，默认为 18080 
# 6. 原理
emw-config 的原来非常简单，使用 [Apollo](https://github.com/ctripcorp/apollo) 的 Client 扩展了基于本地数据库的实现方式。核心扩展类为 [DBConfigRepository](https://github.com/lepdou/EMW/blob/main/emw-config/src/main/java/com/lepdou/framework/emw/config/core/DBConfigRepository.java)。
emw-config sdk 定时轮训数据库的 emw-config 表，通过 version 字段判断是否有变更，如果有变更则更新内存并触发回调事件。
