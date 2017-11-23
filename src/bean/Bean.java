package bean;

/**
 * 泛型限定父类
 * Bean的子类的字段只可以是private修饰的用来储存数据的，并且这些字段需要有相应的get与set方法来对其进行操作
 * 作为一个数据储存对象，可以被克隆，所以Bean实现了克隆接口
 * @author 神眷之樱花
 *
 */
public class Bean implements Cloneable{
	public Object clone() {  
        Object o = null;  
        try {  
            o =super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  
}
