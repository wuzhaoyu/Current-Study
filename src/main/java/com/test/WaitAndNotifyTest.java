package com.test;

import com.n2.util.Sleeper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/4/8
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "Thread.WaitAndNotifyTest")
public class WaitAndNotifyTest {

    // wait 需要与synchronize一起使用

    @SneakyThrows
    public static void main(String[] args) {

        Object lock = new Object();

        Thread t1 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("开始执行s1");
                synchronized(lock){
                    lock.wait();
                }
                log.info("结束执行s1");
            }
        };
        Thread t2 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("开始执行s2");
                synchronized(lock){
                    lock.wait();
                }
                log.info("结束执行s2");
            }
        };
        t1.start();
        t2.start();
        Sleeper.sleep(2);
        log.info("唤醒其他的等待线程");
        //只有获取锁才能wait notify
        synchronized (lock){
//            lock.notify();
            lock.notifyAll();
        }

    }

}
