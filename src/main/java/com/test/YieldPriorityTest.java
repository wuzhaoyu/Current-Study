package com.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/4/10
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "c.test02")
public class YieldPriorityTest {

    public static void main(String[] args) {

        Runnable r1 = ()->{
            int count = 0;
            for(;;){
                System.out.println("--------->" + count++);
            }
        };
        Runnable r2 = ()->{
            int count = 0;
            // Thread.yield();
            for(;;){
                System.out.println("      --------->" + count++);
            }
        };

        Thread t1 = new Thread(r1,"t1");
        Thread t2 = new Thread(r2,"t2");
        // yield 让出当前线程的执行，是否真的执行取决于操作的系统的任务调度器
        // 是运行中线程处于就绪状态，但是cup时间片可随时执行
        // sleep 是使当前的线程处于阻塞状态，不能被cup时间片所执行
        // Thread.yield();
        t1.start();
        t2.start();
        //比较级的设置值越大优先执行,是否真的执行取决于操作的系统的任务调度器
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
    }
}
