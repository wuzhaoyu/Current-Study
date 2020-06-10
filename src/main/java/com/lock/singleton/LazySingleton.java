package com.lock.singleton;

import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.INTERNAL;

import java.io.Serializable;

/**
 * 类功能说明: 懒汉式
 * 类修改者	创建日期2020/5/20
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
//使用final关键字防止子类继承破坏父类内部就够
//serializable https://baijiahao.baidu.com/s?id=1633305649182361563&wfr=spider&for=pc
//serializable https://blog.csdn.net/u014653197/article/details/78114041
public final class LazySingleton implements Serializable {

    private LazySingleton(){}

    // 解决由于cpu重排序时指令交错导致对象创建问题（可参考：）
    private static  volatile LazySingleton INSTANCE = null;

    //
    public static LazySingleton getInstance(){
        if(INSTANCE != null){
            return INSTANCE;
        }
        synchronized(LazySingleton.class){
           if(INSTANCE != null){
               return INSTANCE;
           }
            INSTANCE = new LazySingleton();
           return INSTANCE;
        }
    }

    //解决对象在反序列化时创建不同的实例
    private  Object readResolve(){
        return INSTANCE;
    }



}
