package Parse;

import org.apache.commons.digester.Digester;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

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
