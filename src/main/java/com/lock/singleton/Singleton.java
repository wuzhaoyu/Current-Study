package com.lock.singleton;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/19
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class Singleton {
    // 私有化构造
    private void Singleton(){}
    private static Singleton singleton = null;
    //在静态方法上添加synchronized锁住的事类对象
    public static synchronized Singleton getInstance(){
       if(singleton == null){
           singleton = new Singleton();
       }
       return singleton;
    }
    //与getInstance相同
    public static  Singleton getInstance1(){
        synchronized(Singleton.class){
            if(singleton == null){
                singleton = new Singleton();
            }
        }
        return singleton;
    }
    //解决了如果已经存在实例对象无须进行加解锁操作，提高性
    public static  Singleton getInstance2(){
        if(singleton == null){
            synchronized(Singleton.class){
                if(singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
        Singleton instance2 = Singleton.getInstance2();
    }
}
