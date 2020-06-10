package com.aqs;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类功能说明:  CountDownLatch 以递减的方式 当为0时 取消阻塞
 * 类修改者	创建日期2020/6/9
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "CountDownLatchTest")
public class CountDownLatchTest {

    public static void main(String[] args) {
       // 基础使用
       // base1()
        //
        basePool();
    }

    public void base1(){
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(()->{
            Sleeper.sleep(1);
            log.info("latch...");
            latch.countDown();
        }).start();
        new Thread(()->{
            Sleeper.sleep(2);
            log.info("latch...");
            latch.countDown();
        }).start();
        new Thread(()->{
            Sleeper.sleep(1.5);
            log.info("latch...");
            latch.countDown();
        }).start();
        try {
            log.info("wait...");
            latch.await();
            log.info("end...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void basePool(){
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(3);
        executorService.submit(()->{
            latch.countDown();
            log.info("latch... {}" , latch.getCount());
        });
        executorService.submit(()->{
            latch.countDown();
            log.info("latch... {}" , latch.getCount());
        });
        executorService.submit(()->{
            latch.countDown();
            log.info("latch... {}" , latch.getCount());
        });
        executorService.submit(()->{
            log.info("await...");
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("end... {}" , latch.getCount());
        });

    }

}
