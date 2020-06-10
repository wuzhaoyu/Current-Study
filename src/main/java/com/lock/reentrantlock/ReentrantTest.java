package com.lock.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 类功能说明:  reentrantlock可重入
 * 类修改者	创建日期2020/5/12
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "可重入测试")
public class ReentrantTest {

    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        try {
            log.info("进入主方法");
            m1();
        } finally {
            lock.unlock();
        }

    }

    public static void m1() {
        lock.lock();
        try {
            log.info("进入m1");
            m2();
        } finally {
            lock.unlock();
        }
    }

    public static void m2() {
        lock.lock();
        try {
            log.info("进入m2");
        } finally {
            lock.unlock();
        }
    }
}
