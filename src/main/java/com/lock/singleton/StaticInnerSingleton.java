package com.lock.singleton;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/21
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class StaticInnerSingleton {

    private StaticInnerSingleton(){}

    // 问题1：属于懒汉式还是饿汉式
    private static class LazyHolder{
       static final StaticInnerSingleton INSTANCE = new StaticInnerSingleton();
    }
    // 问题2：在创建时是否有并发问题
    public static StaticInnerSingleton getInstance(){
        return LazyHolder.INSTANCE;
    }
}
