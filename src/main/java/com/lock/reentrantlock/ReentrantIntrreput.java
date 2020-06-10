package com.lock.reentrantlock;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 类功能说明:  reentrantlock 可打断
 *
 *   存在竞争时，被阻塞时可通过interrupt中止打断
 *
 * 类修改者	创建日期2020/5/12
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "可重入测试")
public class ReentrantIntrreput {

    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
      Thread t1 = new Thread(()->{
            try {
                    // 不存在竞争时 获取锁
                    // 存在竞争时进入阻塞队列,可被其他线程打断
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.info("被打断");
                    return;
                }
                log.info("进入t1");
                m1();

        },"t1");

        lock.lock();
        t1.start();
        Sleeper.sleep(1);
        t1.interrupt();


    }

    public static void m1() {
        lock.lock();
        try {
            log.info("进入m1");
            m2();
        } finally {
            lock.unlock();
        }
    }

    public static void m2() {
        lock.lock();
        try {
            log.info("进入m2");
        } finally {
            lock.unlock();
        }
    }
}
