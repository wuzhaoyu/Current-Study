package com.lock;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 类功能说明: 偏向锁撤销
 * 类修改者	创建日期2020/4/19
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "ThreadBiasedCancel")
public class ThreadBiasedCancel {

    // 默认对象中的偏向锁会打开 ，但是jdk有个延时加载的设置 所以 设置 -XX:BiasedLockingStartupDelay=0 去除延时加载
    // 对象默认为0001 偏向锁为 0101
    public static void main(String[] args) {
        //查看对象头信息
        Dog dog = new Dog();

        //撤销对象的偏向锁 原因为调用该方法后，会把hash值写入对象中，默认偏向锁的对象中存储的线程地址无法存储,导致normal状态
        //dog.hashCode();


        // 错开线程访问的时间，多个线程访问导致偏向锁撤销

        Thread t1 = new Thread(()->{
            log.debug("1");
            log.info( ClassLayout.parseClass(Dog.class).toPrintable(dog));

            // 加锁后 对象头中存在获取锁的当前线程的ID(这个ID不是对象ID是操作系统分配的)
            // 代码块执行完成后对象头中的地址未发生改变
            // 禁用偏向锁后，会升级为轻量级锁
            synchronized (dog){
                log.debug("2");
                log.info( ClassLayout.parseClass(Dog.class).toPrintable(dog));
            }
            log.info( ClassLayout.parseClass(Dog.class).toPrintable(dog));
            log.debug("3");
            synchronized (ThreadBiasedCancel.class){
                log.debug("4");
                ThreadBiasedCancel.class.notify();
                log.debug("5");
            }

        },"t1");
        t1.start();
        Thread t2 = new Thread(()->{

            log.debug("6");
            synchronized (ThreadBiasedCancel.class){
                try {
                    log.debug("7");
                    ThreadBiasedCancel.class.wait();
                    log.debug("8");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("9");
            log.info( ClassLayout.parseClass(Dog.class).toPrintable(dog));

            // 加锁后 对象头中存在获取锁的当前线程的ID(这个ID不是对象ID是操作系统分配的)
            // 代码块执行完成后对象头中的地址未发生改变
            // 禁用偏向锁后，会升级为轻量级锁
            synchronized (dog){
                log.debug("10");
                log.info( ClassLayout.parseClass(Dog.class).toPrintable(dog));
                log.debug("11");
            }
            log.info( ClassLayout.parseClass(Dog.class).toPrintable(dog));
            log.debug("12");

        },"t2");
        t2.start();
    }

    static class Dog {

    }
}
