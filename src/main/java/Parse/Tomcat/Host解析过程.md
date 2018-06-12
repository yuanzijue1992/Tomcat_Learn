[TOC]

### 1.创建Host实例

```java
// 创建Host实例，默认为StandardHost
digester.addObjectCreate(prefix + "Host",
                         "org.apache.catalina.core.StandardHost",
                         "className");
// 设置属性
digester.addSetProperties(prefix + "Host");

digester.addRule(prefix + "Host",
                 new CopyParentClassLoaderRule());
// 添加生命周期监听器：HostConfig
digester.addRule(prefix + "Host",
                 new LifecycleListenerRule
                         ("org.apache.catalina.startup.HostConfig",
                          "hostConfigClass"));
// 添加Host到Engine
digester.addSetNext(prefix + "Host",
                    "addChild",
                    "org.apache.catalina.Container");

digester.addCallMethod(prefix + "Host/Alias",
                       "addAlias", 0);
```

### 2.为Host添加集群

```java
// 创建实例
digester.addObjectCreate(prefix + "Host/Cluster",
                                 null, // MUST be specified in the element
                                 "className");
// 设置属性
digester.addSetProperties(prefix + "Host/Cluster");
// 添加集群到Host
digester.addSetNext(prefix + "Host/Cluster",
                            "setCluster",
                            "org.apache.catalina.Cluster");
```

### 3.为Host添加生命周期管理

此部分监听器由server.xml配置。默认情况下，Catalina未指定Host监听器

```java
// 创建实例
digester.addObjectCreate(prefix + "Host/Listener",
                         null, // MUST be specified in the element
                         "className");
// 设置属性
digester.addSetProperties(prefix + "Host/Listener");
// 将Listener添加到Host
digester.addSetNext(prefix + "Host/Listener",
                    "addLifecycleListener",
                    "org.apache.catalina.LifecycleListener");
```

### 4.为Host添加安全配置

```java

digester.addRuleSet(new RealmRuleSet(prefix + "Host/"));
// 添加拦截器，具体的拦截器由className决定，默认为AccessLogValve来记录访问日志
digester.addObjectCreate(prefix + "Host/Valve",
                         null, // MUST be specified in the element
                         "className");
digester.addSetProperties(prefix + "Host/Valve");
digester.addSetNext(prefix + "Host/Valve",
                    "addValve",
                    "org.apache.catalina.Valve");
```

