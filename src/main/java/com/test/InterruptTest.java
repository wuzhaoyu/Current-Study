package com.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/4/10
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "InterruptTest")
public class InterruptTest {
    @SneakyThrows
    public static void main(String[] args) {
        //测试睡眠状态的打断状态
        Thread thread = new Thread("t1"){
            @Override
            public void run() {
                log.debug("enter sleep....");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    log.debug("wake up ");
                    e.printStackTrace();
                }
            }
        };

        //测试运行状态的打断状态
        Thread thread1 = new Thread(()->{
            while(true){
                Thread thread2 = Thread.currentThread();
                if(thread2.isInterrupted()){
                    log.info("线程被打断，并退出");
                    break;
                }

            }

        },"t2");

        // -------------------
        thread.start();
        TimeUnit.SECONDS.sleep(2);
        log.debug("main");
        // 1 将睡眠状态的线程打断 所以抛出异常InterruptedException 类似存在 wait join
        // 2.使用sleep方式时 默认捕获InterruptedException异常 因为有可能线程被打断
        // 3.睡眠后被唤醒的线程不一定会被线程执行 需要等到cpu时间片分配到
        thread.interrupt();
        //打断的状态 sleep join wait 当处于睡眠状态时打断 会抛出异常 并且打断的状态会被清除 所以为false
        log.info("打断状态;{}",thread.isInterrupted());

        // -------------------
        thread1.start();
        log.info("正常运行的线程打断");
        // 正常运行的线程被打断 则打断状态true标记
        thread1.interrupt();
        log.info("打断状态;{}",thread1.isInterrupted());

    }
}
