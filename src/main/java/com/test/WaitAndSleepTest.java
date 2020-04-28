package com.test;

import com.n2.util.Sleeper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/4/8
 * 修改说明
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "Thread.WaitAndNotifyTest")
public class WaitAndSleepTest {
    static final Object lock = new Object();

    @SneakyThrows
    public static void main(String[] args) {

        Thread t1 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("开始执行s1");
                synchronized(lock){
                    //线程睡眠的时候并不会释放锁
//                    Thread.sleep(10000);
//                    使当前线程进入waitset队列中 并且不会占用CPU时间片
                    lock.wait();
                }
            }
        };
        t1.start();
        Sleeper.sleep(2);
        //只有获取锁才能wait notify
        synchronized (lock){
            log.info("获取所成功");
        }

    }

}
