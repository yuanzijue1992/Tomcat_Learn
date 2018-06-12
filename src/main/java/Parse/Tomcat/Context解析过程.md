[TOC]

Context相关的配置文件不止一处，server.xml和context.xml中都可能有相关配置。这里的解析过程是server.xml文件的解析过程。

### 1.Context实例化

Context来源于多处，通过server.xml配置context时create为==true==；通过HostConfig自动创建Context时，create为false，此时仅需解析子节点即可。

```java
if (create) {
    // 使用StandardContext
    digester.addObjectCreate(prefix + "Context",
                             "org.apache.catalina.core.StandardContext", "className");
    digester.addSetProperties(prefix + "Context");
} else {
    digester.addRule(prefix + "Context", new SetContextPropertiesRule());
}

if (create) {
    // 配置生命周期监听器：ContextConfig
    digester.addRule(prefix + "Context",
                     new LifecycleListenerRule
                     ("org.apache.catalina.startup.ContextConfig",
                      "configClass"));
    digester.addSetNext(prefix + "Context",
                        "addChild",
                        "org.apache.catalina.Container");
}
```

