package com.lock.reentrantlock;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/12
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "尝试获取锁")
public class TryLock {

    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.info("尝试获取锁");
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {
                    log.info("获取锁失败");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.info("获取锁失败");
                return;
            }
            try {
                log.info("获取锁成功");
            } finally {
                lock.unlock();
            }

        }, "t1");

        lock.lock();
        t1.start();
        Sleeper.sleep(3);
        log.info("主线程解锁");
        lock.unlock();
//        t1.interrupt();


    }
}
