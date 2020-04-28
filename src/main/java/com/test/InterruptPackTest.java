package com.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明:
 *     主线程运行时 会开启 监控线程 ，当需要不停止监控线程时以一种优雅的方式
 *      进行停止
 * 类修改者	创建日期2020/4/10
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "InterruptPackTest")
public class InterruptPackTest {
    public static void main(String[] args) throws InterruptedException {
        InterruptPackThread interruptPackThread = new InterruptPackThread();

        interruptPackThread.start();

        Thread.sleep(4000);
        interruptPackThread.stop();

    }

    @Slf4j(topic = "InterruptPackThread")
    static
    class InterruptPackThread {

       private Thread monitor;

       public void start(){
         monitor = new Thread(()->{
             while(true) {
                 Thread current = Thread.currentThread();
                 if (current.isInterrupted()) {
                     log.info("退出线程");
                     break;
                 }else{
                     log.info("监控线程");
                     try {
                         Thread.sleep(1000);
                     } catch (InterruptedException e) {

                         //处在睡眠中的线程 会清除打断标记 可以重新赋值打断标记为true
                         //  LockSupport.park(); 打断线程，在打断标记为false生效 ，true的时候不执行该程序
                         current.interrupt();
                         e.printStackTrace();
                     }
                 }
             }
           });
           monitor.start();
       }
       public void stop(){
           monitor.interrupt();

       }

    }
}
