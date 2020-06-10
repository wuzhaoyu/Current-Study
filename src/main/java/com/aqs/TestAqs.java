package com.aqs;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/6/8
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "TestAqs")
public class TestAqs {

    public static void main(String[] args) {
        MyLock myLock = new MyLock();
        new Thread(()->{
            myLock.lock();
            // 不可重入
            // myLock.lock();
            try{
                log.info("locking...");
                Sleeper.sleep(1);
            }finally {
                log.info("unlocking...");
                myLock.unlock();

            }

        },"t1").start();

        new Thread(()->{
            myLock.lock();
            try{
                log.info("locking...");
            }finally {
                log.info("unlocking...");
                myLock.unlock();
            }

        },"t2").start();

    }


    static class MyLock implements Lock {
        /**
         * 不可重入锁（独占锁）
         */
         class MyAqs extends AbstractQueuedSynchronizer {

            @Override // 尝试获取锁
            protected boolean tryAcquire(int args) {
                if (compareAndSetState(0, 1)) {
                    // 设置独占锁线程 （持有者线程）
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
                return false;
            }

            @Override
            protected boolean tryRelease(int args) {
                //写在setState之上是validate关键字 可以防止指令重排序 也是内存可见
                if(args == 1) {
                    if(getState() == 0) {
                        throw new IllegalMonitorStateException();
                    }
                    setExclusiveOwnerThread(null);
                    setState(0);
                    return true;
                }
                return false;
            }

            @Override
            protected boolean isHeldExclusively() {
                return getState() == 1;
            }
            protected Condition newCondition() {
                return new ConditionObject();
            }
        }
        MyAqs myAqs = new MyAqs();

        @Override // 获取锁 若失败进入阻塞队列
        public void lock() {
            myAqs.acquire(1);
        }

        @Override // 可打断锁
        public void lockInterruptibly() throws InterruptedException {
            myAqs.acquireInterruptibly(1);
        }

        @Override // 尝试获取锁(一次)
        public boolean tryLock() {
            return  myAqs.tryRelease(1);
        }

        @Override // 带超时尝试
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return myAqs.tryAcquireNanos(1,unit.toNanos(time));
        }

        @Override // 解锁
        public void unlock() {
            myAqs.release(1);
        }

        @Override
        public Condition newCondition() {
            return myAqs.newCondition();
        }
    }


}
