package com.design;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * 类功能说明: 多线程中顺序输出
 * 类修改者	创建日期2020/5/13
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "order")
public class OrderPrint {

    Object lock = new Object();

    static boolean isReturn = false;

    public static void main(String[] args) {

        OrderPrint orderPrint = new OrderPrint();
        //第一种方式
        //orderPrint.orderWaitNotify();
        //第一种方式
        orderPrint.orderParkUnPark();

    }

    /**
     * 使用等待唤醒控制顺序输出
     */
    public void orderWaitNotify() {

        new Thread(() -> {
            synchronized (lock) {
                while (!isReturn) {
                    log.info("线程1进入阻塞队列1");
                    try {
                        lock.wait();
                        log.info("线程1进入阻塞队列2");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("输出线程1");
            }

        }, "t1").start();
        Sleeper.sleep(2);

        new Thread(() -> {
            synchronized (lock) {
                isReturn = true;
                lock.notify();
                log.info("输出线程2");
            }

        }, "t2").start();
    }

    /**
     * 使用park控制顺序输出
     */
    public void orderParkUnPark() {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.info("输出线程1");
        }, "t1");
        t1.start();
        new Thread(() -> {
            log.info("输出线程2");
            LockSupport.unpark(t1);
        }, "t2").start();
    }
}
