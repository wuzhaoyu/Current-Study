package com.lock.singleton;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/22
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class EunmSecurityTest {

    public static void main(String[] args) {

        EnumSingleton singleton1 = EnumSingleton.getInstance();
        EnumSingleton singleton2 = EnumSingleton.getInstance();
        log.info("对象比较{}",singleton1 == singleton2);

        try {
            Constructor<EnumSingleton> declaredConstructor = EnumSingleton.class.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            EnumSingleton singleton = declaredConstructor.newInstance();
            log.info("反射对象比较{}",singleton1 == singleton);

            EnumSingleton.EnumSingletonClass enumSingletonClass1 = EnumSingleton.EnumSingletonClass.INSTANCE;
            EnumSingleton.EnumSingletonClass enumSingletonClass2 = EnumSingleton.EnumSingletonClass.INSTANCE;
            Constructor<EnumSingleton.EnumSingletonClass> declaredConstructor1 = EnumSingleton.EnumSingletonClass.class.getDeclaredConstructor(String.class,int.class);
            EnumSingleton.EnumSingletonClass enumSingletonClass = declaredConstructor1.newInstance("instance",66);
            log.info("对象比较{}",enumSingletonClass1 == enumSingletonClass2);
            log.info("反射对象比较{}",enumSingletonClass2 == enumSingletonClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
