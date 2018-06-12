## Server解析

本节讲解**Server**解析的步骤与细节。

Server的结构图如下：

![Catalina结构图](/Catalina结构图.png)


### 1.创建Server实例

```java
// 创建Server对象
digester.addObjectCreate("Server","org.apache.catelina.core.StandardServer","className");
// 初始化参数
digester.addSerProperties("Server");
// 调用setServer函数设置server
digester.addSetNext("Server","setServer","org.apache.catalina.Server");
```

### 2.创建全局J2EE企业命名上下文

```java
digester.addObjectCreate("Server/GlobalNamingResources",
                                 "org.apache.catalina.deploy.NamingResources");
digester.addSetProperties("Server/GlobalNamingResources");
digester.addSetNext("Server/GlobalNamingResources",
                            "setGlobalNamingResources",
                            "org.apache.catalina.deploy.NamingResources");
```

### 3.为Server添加声明周期监听器

```java
// 初始化Listener
digester.addObjectCreate("Server/Listener",
                         null, // MUST be specified in the element
                         "className");
// 初始化Listener属性
digester.addSetProperties("Server/Listener");
// Server里增加监听器,具体的监听器由className决定
digester.addSetNext("Server/Listener",
                    "addLifecycleListener",
                    "org.apache.catalina.LifecycleListener");
```

Catalina默认配置了5个LifecycleListener监听器:

| 类                                |                             描述                             |
| :-------------------------------- | :----------------------------------------------------------: |
| AprLifecycleListener              |     在Server初始化之前加载APR库，并于Server停止之前销毁      |
| VersionLoggerListener             |   在Server初始化之前打印操作系统、JVM以及服务器的版本信息    |
| JreMemoryLeakPreventionListener   | 在Server初始化之前调用，以解决单例对象创建导致的JVM内存泄漏问题以及锁文件问题 |
| GlobalResourceLifecycleListener   |        在Server启动时，将JNDI资源注册为MBean进行管理         |
| ThreadLocalLeakPreventionListener |   用于在Contex停止重建Executor池中的线程，避免导致内存泄漏   |

### 4.构造Service实例

```java
// 创建Service对象
digester.addObjectCreate("Server/Service",
                                 "org.apache.catalina.core.StandardService",
                                 "className");
// 配置属性
digester.addSetProperties("Server/Service");
// Server里增加Service，默认为org.apache.catalina.core.StandardService
digester.addSetNext("Server/Service",
                            "addService",
                            "org.apache.catalina.Service");
```

### 5.为Service添加生命周期监听器

```java
// 初始化Listener对象，具体监听器类有className决定，默认情况下Catalina未指定监听器
digester.addObjectCreate("Server/Service/Listener",
                                 null, // MUST be specified in the element
                                 "className");
// 配置属性
digester.addSetProperties("Server/Service/Listener");
// 
digester.addSetNext("Server/Service/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");
```

### 6.为Service添加Executor

```java
// 创建Executor,默认为StandardThreadExecutor
digester.addObjectCreate("Server/Service/Executor",
                         "org.apache.catalina.core.StandardThreadExecutor",
                         "className");
// 配置属性
digester.addSetProperties("Server/Service/Executor");
// Service调用addExecutor函数来添加Executor
digester.addSetNext("Server/Service/Executor",
                            "addExecutor",
                            "org.apache.catalina.Executor");
```

Catalina共享Exector的级别为Service，默认情况下未配置Executor，即不共享。

### 7.为Service添加Connector

```java
// 创建Connector
digester.addRule("Server/Service/Connector",
                         new ConnectorCreateRule());
digester.addRule("Server/Service/Connector",
                         new SetAllPropertiesRule(new String[]{"executor"}));
digester.addSetNext("Server/Service/Connector",
                            "addConnector",
                            "org.apache.catalina.connector.Connector");
```

### 8.为Connector添加虚拟主机SSL配置

```
略，可能由于版本过低，没有相关代码
```

### 9.为Connector添加生命周期监听器

```java
// 有className指定监听器，默认是没有配置
digester.addObjectCreate("Server/Service/Connector/Listener",
                                 null, // MUST be specified in the element
                                 "className");
digester.addSetProperties("Server/Service/Connector/Listener");
digester.addSetNext("Server/Service/Connector/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");
```

### 10.为Connector添加升级协议

```
略，
```

### 11.添加子元素解析规则

此部分指定了Servlet容器相关的各级嵌套子节点的解析规则，而且每类嵌套子节点的解析封装为一个RuleSet。

```java
digester.addRuleSet(new NamingRuleSet("Server/GlobalNamingResources/"));
digester.addRuleSet(new EngineRuleSet("Server/Service/"));
digester.addRuleSet(new HostRuleSet("Server/Service/Engine/"));
digester.addRuleSet(new ContextRuleSet("Server/Service/Engine/Host/"));
addClusterRuleSet(digester, "Server/Service/Engine/Host/Cluster/");
digester.addRuleSet(new NamingRuleSet("Server/Service/Engine/Host/Context/"));

digester.addRule("Server/Service/Engine",
new SetParentClassLoaderRule(parentClassLoader));
addClusterRuleSet(digester, "Server/Service/Engine/Cluster/");
```







