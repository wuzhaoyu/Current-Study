package com.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明:   java线程的六种状态
 * 类修改者	创建日期2020/4/8
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "StateThread")
public class StateThread {

    public static void test01(){
        Runnable r = ()->{
           log.debug("测试一");
        };
        Thread thread = new Thread(r,"线程一");
        thread.start();
    }
    public static void test02(){
        while (true){
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    log.debug("测试二");
                }
            };
            Thread thread = new Thread(r,"线程一");
            thread.start();
        }

    }

    public static void main(String[] args) {

        //NEW
        Thread t1 = new Thread(()->{

        },"t1");

        // RUNABLE
        Thread t2 = new Thread(()->{
            while(true){

            }
        },"T2");

        // TERMINATED
        Thread t3 = new Thread(()->{

        },"T3");

        // WAITING
        Thread t4 = new Thread(()->{
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"T4");

        // TIMED_WAITING
        Thread t5 = new Thread(()->{
            synchronized (StateThread.class){
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"T5");

        // BLOCKED
        Thread t6 = new Thread(()->{
            synchronized (StateThread.class){

            }
        },"T6");

        t5.start();
        t6.start();
        t4.start();
        t3.start();
        t2.start();
        log.info("T1 状态 {}",t1.getState());
        log.info("T2 状态 {}",t2.getState());
        log.info("T3 状态 {}",t3.getState());
        log.info("T4 状态 {}",t4.getState());
        log.info("T5 状态 {}",t5.getState());
        log.info("T6 状态 {}",t6.getState());
    }

}
