package Parse;

import java.util.*;

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
