package com.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.TIMEOUT;

import java.util.concurrent.TimeUnit;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/4/8
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "Thread.JoinTest")
public class JoinTest {

   static int s1 = 0;
   static int s2 = 0;


    @SneakyThrows
    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("开始执行s1");
                TimeUnit.SECONDS.sleep(1);
                s1 = 1;
            }
        };
        Thread t2 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("开始执行s2");
                TimeUnit.SECONDS.sleep(2);
                s2 = 2;
            }
        };
        long start = System.currentTimeMillis();
        // join 主线程等待子线程结束再执行
        t1.start();
        t2.start();
        t2.join();
        t1.join();
        long end = System.currentTimeMillis();
        log.info("{} {} 执行完成花费{}" ,s1 ,s2, start - end );
    }

}
