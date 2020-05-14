package com.design;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 类功能说明: 交替有次序输出
 * 类修改者	创建日期2020/5/14
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "alternate")
public class Alternate {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) {
        // 顺序交替输出
        // waitNotifyMethod();
       // awaitSingleMethod();
        parkAndUnParkMethod();


    }


    /**
     * 顺序交替输出
     */
    static class WaitNotifyClass {
        public void print(String printStr, int waitFlag, int nextFlag) {
            for (int i = 0; i < loopNumber; i++) {
                synchronized (this) {
                    while (waitFlag != this.flag) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    log.info(printStr);
                    this.setFlag(nextFlag);
                    this.notifyAll();
                }
            }
        }

        //等待标记
        private int flag;

        private int loopNumber;

        public WaitNotifyClass(int flag, int loopNumber) {
            this.flag = flag;
            this.loopNumber = loopNumber;
        }

        public synchronized int getFlag() {
            return flag;
        }

        public synchronized void setFlag(int flag) {
            this.flag = flag;
        }
    }

    public static void waitNotifyMethod() {
        WaitNotifyClass waitNotifyClass = new WaitNotifyClass(1, 5);
        Thread t1 = new Thread(() -> {
            waitNotifyClass.print("a", 1, 2);
        }, "t1");
        Thread t2 = new Thread(() -> {
            waitNotifyClass.print("b", 2, 3);
        }, "t2");
        Thread t3 = new Thread(() -> {
            waitNotifyClass.print("c", 3, 1);
        }, "t3");

        t3.start();
        t1.start();
        t2.start();
    }

    /**
     * 顺序交替输出
     */
    static class AwaitSingleClass extends ReentrantLock {
        public void print(String printStr, Condition current, Condition nextCondition) {
            for (int i = 0; i < loopNumber; i++) {
                try {
                    lock();
                    current.await();
                    log.info(printStr);
                    nextCondition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    unlock();
                }
            }
        }

        private int loopNumber;

        public AwaitSingleClass(int loopNumber) {
            this.loopNumber = loopNumber;
        }

    }

    public static void awaitSingleMethod() {
        AwaitSingleClass awaitSingleClass = new AwaitSingleClass( 5);
        Condition acondition = awaitSingleClass.newCondition();
        Condition bcondition = awaitSingleClass.newCondition();
        Condition ccondition = awaitSingleClass.newCondition();
        Thread t1 = new Thread(() -> {
                awaitSingleClass.print("a", acondition, bcondition);
        }, "t1");
        Thread t2 = new Thread(() -> {
                awaitSingleClass.print("b", bcondition, ccondition);
        }, "t2");
        Thread t3 = new Thread(() -> {
                awaitSingleClass.print("c", ccondition, acondition);
        }, "t3");
        t3.start();
        t1.start();
        t2.start();

        Sleeper.sleep(1);
        //在wait之前需要获取锁
        try{
            awaitSingleClass.lock();
            acondition.signal();
        }finally {
            awaitSingleClass.unlock();
        }



    }

    static class ParkAndUnParkClass{
        public void print(String printStr, Thread next) {
            for (int i = 0; i < loopNumber; i++) {
                    LockSupport.park();
                    log.info(printStr);
                    LockSupport.unpark(next);
            }
        }

        private int loopNumber;

        public ParkAndUnParkClass(int loopNumber) {
            this.loopNumber = loopNumber;
        }

    }
    public static void parkAndUnParkMethod() {
        ParkAndUnParkClass parkAndUnParkClass = new ParkAndUnParkClass( 5);

        t3 = new Thread(() -> {
             parkAndUnParkClass.print("b",t1);
        }, "t3");
        t2 = new Thread(() -> {
            parkAndUnParkClass.print("c",t3);
        }, "t2");
        t1 = new Thread(() -> {
            parkAndUnParkClass.print("a",t2);
        }, "t1");
        t3.start();
        t1.start();
        t2.start();

        LockSupport.unpark(t1);
        }
}
