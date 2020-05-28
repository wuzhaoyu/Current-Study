package com.lock.singleton;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/21
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public  class EnumSingleton {
    //私有化构造函数
    private EnumSingleton() {
    }
    //定义一个静态枚举类
    static enum EnumSingletonClass {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE;
        private EnumSingleton enumSingleton;
        //私有化枚举的构造函数
        private EnumSingletonClass() {
            enumSingleton = new EnumSingleton();
        }
        public EnumSingleton getInstance() {
            return enumSingleton;
        }

    }
    //对外暴露一个获取对象的静态方法
    public static EnumSingleton getInstance() {
        return EnumSingletonClass.INSTANCE.getInstance();
    }
}
