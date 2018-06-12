[TOC]

## 简介
Digester类用来将XML映射成Java对象，简化XML的处理。它是Apache Commons库中的一个jar包：common-digester包。

## 常用方法
### 1.addObjectCreate(String rule,Class class)

设置节点与Java对象的映射规则，rule指定节点的筛选规则，class设置映射对象。SAX解析时，遇到rule指定的节节点，会创建一个class实例放入堆栈中。

比如：digester.addObectCreate("database/user",com.model.UserBean.class).解析遇到databse里的user节点时，会创建一个UserBean实例并放入堆栈中。



### 2.addSetProperties(String rule)

设置节点的属性设置规则。当解析遇到符合rule的节点时，根据属性列表中的属性值对，使用Java反射机制使用标准的JavaBean方法设置栈顶对象实例。

必须要有相应的set函数才能完成属性的传入，否则为null。

比如：digester.addSetProperties("database/user")，解析遇到user节点时，会获取键值对 userName=guest,password=guest，获得==栈顶==的UserBean对象，设置实例的userName、password属性。



### 3.addBeanPropertySetter(String rule)

该方法的作用及使用方法类似于addSetProperties，只不过它是用rule所指定的标签来调用对象的setter。



### 4.addSetNext(String rule,String methodName)

设置当前rule节点与父节点的调用规则，当遇到rule节点时，调用堆栈中的次栈顶元素调用methodName方法。将栈顶元素作为次顶元素指定方法的输入参数。

比如:digester.addSetNext("database/user","addUser"),调用database实例的addUser，user为参数



### 5.addCallMethod(String rule,String methodName,int paraNumber)

该方法同样设置对象的属性，但更加灵活，不需要对象具有setter

根据rule规则指定的属性，调用对象的methodName方法，paraNumber参数是表示方法需要的参数个数，当paraNumber=0时，可以单独使用，不然需要配合addCallParam方法

paraNumber为==参数的个数==

比如:digester.addCallMethod("database/user/userName","setUserName",0), 参数为xml当前值;无参方法:digester.addCallMethod( "pattern", "methodName" )



### 6.addCallParam(String rule,int paraIndex,String attributeName)

该方法与addCallMethod配合使用，根据rule指定的标签属性来调用方法

paraIndex表明需要填充的方法形参序号，==从0开始==，方法由addCallMethdo指定，attributeName指定标签属性名；