[TOC]

## Tomcat中的xml文件解析
Catalina使用Digester解析XML配置文件。

### 1. 导入相应jar包
```
<dependency>
    <groupId>commons-digester</groupId>
    <artifactId>commons-digester</artifactId>
    <version>2.1</version>
</dependency>
```
### 例子
#### 创建使用的类
##### Course
```java
public class Course {

    private String name;

    private String teacher;

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
```
##### Student
```java
public class Student {

    private String name;

    private int age;

    private List<Course> course= new ArrayList<Course>();

    private Map<String,String> address = new HashMap<String,String>();

    // 用于解析到course节点时，将course插入到student的course字段中
    public void addCourse(Course course){
        this.course.add(course);
    }

    public void putAddress(String name,String value){
        address.put(name,value);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name+":"+age+"\n");
        for(Course c:course){
            sb.append(c.getName()+":"+c.getTeacher()+"\n");
        }

        Iterator it = address.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry e = (Map.Entry) it.next();
            sb.append(e.getKey()+":"+e.getValue()+"\n");
        }
        return sb.toString();
    }

}
```

##### student.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>

<student name="mzw" age="18">
    <course name="yuwen" teacher="zhang" />
    <course name="shuxue" teacher="wang" />
    <address>
        <name>school</name>
        <value>nanjing</value>
    </address>
    <address>
        <name>home</name>
        <value>yangzhou</value>
    </address>
</student>
```

#### 解析文件
```java
public class Digester_test {

    @Test
    public void Parse_Student(){

        // 创建一个新的解析器
        Digester dig = new Digester();

        dig.setValidating(false);

        // 匹配student节点时，创建student对象
        dig.addObjectCreate("student",Student.class);

        // 匹配student对象时，设置对象属性
        // 也就是在匹配到这个节点时，会调用这个类中的相关set方法设置属性，如果相应的设置方法不存在则设为null
        dig.addSetProperties("student");

        // 匹配studnet下的course节点时，创建course对象
        dig.addObjectCreate("student/course",Course.class);

        // 匹配course节点时，设置对象属性
        dig.addSetProperties("student/course");

        // 匹配student/course时，调用student的addCourse(Course course)方法
        dig.addSetNext("student/course","addCourse");

        // 匹配student/address时，调用putAddress来传入值
        dig.addCallMethod("student/address","putAddress",2);
        // 设置第一个参数
        dig.addCallParam("student/address/name",0);
        // 设置第二个参数
        dig.addCallParam("student/address/value",1);


        try {
            Student s = (Student) dig.parse(new File("F:/MyBatisSrc/Tomcat_Learn/target/classes/student.xml"));
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}
```