package com.aqs;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/6/9
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "Semaphore")
public class SemaphoreTest {

    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Sleeper.sleep(1);
                } finally {
                    log.info("end......");
                    semaphore.release();
                }
            }).start();
        }

    }
}
