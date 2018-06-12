[TOC]

### 1. 创建Engine实例

```java
// 创建Engine实例，默认是StandardEngine
digester.addObjectCreate(prefix + "Engine",
                                 "org.apache.catalina.core.StandardEngine",
                                 "className");
// 设置属性
digester.addSetProperties(prefix + "Engine");
// 添加一个生命周期监听器：EngineConfig
digester.addRule(prefix + "Engine",
                         new LifecycleListenerRule
                         ("org.apache.catalina.startup.EngineConfig",
                          "engineConfigClass"));
// 使用setContainer将Engine添加到Server中
digester.addSetNext(prefix + "Engine",
                            "setContainer",
                            "org.apache.catalina.Container");
```

### 2.为Engine添加集群配置

```java
// 创建Cluster,具体的集群实现类由className实现
digester.addObjectCreate(prefix + "Engine/Cluster",
                                 null, // MUST be specified in the element
                                 "className");
// 设置属性
digester.addSetProperties(prefix + "Engine/Cluster");
// 设置Engine中的Cluster
digester.addSetNext(prefix + "Engine/Cluster",
                            "setCluster",
                            "org.apache.catalina.Cluster");
```

### 3.为Engine添加生命周期监听器

```java
// 创建监听器，具体的监听器由className决定
digester.addObjectCreate(prefix + "Engine/Listener",
                                 null, // MUST be specified in the element
                                 "className");
// 设置属性
digester.addSetProperties(prefix + "Engine/Listener");
// 添加Engine中监听器
digester.addSetNext(prefix + "Engine/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");
```

### 4.为Engine添加安全配置

==后面讲解==

```java
digester.addRuleSet(new RealmRuleSet(prefix + "Engine/"));

digester.addObjectCreate(prefix + "Engine/Valve",
                         null, // MUST be specified in the element
                         "className");
digester.addSetProperties(prefix + "Engine/Valve");
digester.addSetNext(prefix + "Engine/Valve",
                    "addValve",
                    "org.apache.catalina.Valve");
```

