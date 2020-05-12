package com.test.state;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明:
 *
 *   runnable --> wait (wait方法)
 *   wait --> runnable 竞争成功
 *   wait --> block  竞争失败
 *
 * 类修改者	创建日期2020/5/12
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class WaitAndNotifyAll {

    public static void main(String[] args) {

        Object lock = new Object();

        new Thread(()->{
            synchronized (lock){
                log.info("线程开始运行");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("行程继续运行");
            }
        },"t1").start();

        new Thread(()->{
            synchronized (lock){
                log.info("线程开始运行");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("行程继续运行");
            }
        },"t2").start();

        Sleeper.sleep(0.5);
        log.info("主线程开始运行");
        synchronized (lock){
            lock.notifyAll();
        }


    }
}
