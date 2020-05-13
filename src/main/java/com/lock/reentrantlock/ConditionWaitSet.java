package com.lock.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.n2.util.Sleeper.sleep;

/**
 * 类功能说明:  条件变量
 * 类修改者	创建日期2020/5/13
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class ConditionWaitSet {

    static boolean hasCigarette = false;
    static boolean hasTakeout = false;
    static ReentrantLock ROOM = new ReentrantLock();
    static Condition waitCigarette = ROOM.newCondition();
    static Condition waitTakeOut = ROOM.newCondition();


    public static void main(String[] args) {

        new Thread(() -> {
            ROOM.lock();
            try {
                log.debug("有烟没？[{}]", hasCigarette);
                //循环检验被唤醒时条件是否符合 若符合会进入下一轮wait
                while (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        waitCigarette.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("可以开始干活了");
            } finally {
                ROOM.unlock();
            }
        }, "小南").start();

        new Thread(() -> {
            ROOM.lock();
            try {
                log.debug("外卖送到没？[{}]", hasTakeout);
                while (!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        waitTakeOut.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("可以开始干活了");
            } finally {
                ROOM.unlock();
            }
        }, "小女").start();
        sleep(1);
        new Thread(() -> {
            ROOM.lock();
            try{
                hasTakeout = true;
                log.debug("外卖到了噢！");
                waitCigarette.signalAll();
            }finally {
                ROOM.unlock();
            }
        }, "送外卖的").start();

        sleep(1);
        new Thread(() -> {
            ROOM.lock();
            try{
                hasTakeout = true;
                log.debug("香烟到了噢！");
                waitTakeOut.signalAll();
            }finally {
                ROOM.unlock();
            }
        }, "送香烟的").start();
    }

    }
