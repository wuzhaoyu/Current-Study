package com.lock;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * 类功能说明: park与unpark的区别
 *
 * 实现锁与解锁的功能 与wait notify相似 但又不同
 * 1. wait与notify需要object monitor
 * 2.notify与notifyAll不能够指定解锁
 * 3.park & unpark 可以先 unpark，而 wait & notify 不能先 notify
 *
 *
 * 类修改者	创建日期2020/5/11
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "park")
public class ParkAndUnpark {

    public static void main(String[] args) {

        //先执行park 分为三部分分别为 _counter ， _cond 和 _mutex
        //1. 当前线程调用 Unsafe.park() 方法
        //2. 检查 _counter ，本情况为 0，这时，获得 _mutex 互斥锁
        //3. 线程进入 _cond 条件变量阻塞
        //4. 设置 _counter = 0
        Thread t1 = new Thread(() -> {
            log.info("启动");
            Sleeper.sleep(1);
            log.info("调用park方法");
            LockSupport.park();
            log.info("程序继续执行");
        }, "t1");
        t1.start();
        log.info("执行主线程");
        Sleeper.sleep(2);
        //t1的状态为WAIT
        LockSupport.unpark(t1);


        //先执行park 分为三部分分别为 _counter ， _cond 和 _mutex
        //1. 调用 Unsafe.unpark(Thread_0) 方法，设置 _counter 为 1
        //2. 当前线程调用 Unsafe.park() 方法
        //3. 检查 _counter ，本情况为 1，这时线程无需阻塞，继续运行
        //4. 设置 _counter 为 0
        Thread t2 = new Thread(() -> {
            log.info("启动");
            Sleeper.sleep(2);
            log.info("调用park方法");
            LockSupport.park();
            log.info("程序继续执行");
        }, "t2");
        t2.start();
        log.info("执行主线程");
        Sleeper.sleep(1);
        //t1的状态为WAIT
        LockSupport.unpark(t2);


        //总结
        // park 会将_counter置为0,unpark方法会将_counter置为1 ，当前park时_counter为1无需阻塞，继续运行
    }

}
