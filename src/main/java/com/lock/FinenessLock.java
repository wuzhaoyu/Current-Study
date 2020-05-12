package com.lock;

import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明:
 *  将锁的粒度细分 提高程序的并发
 * 类修改者	创建日期2020/5/12
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class FinenessLock {

    public static void main(String[] args) {
        Room room = new Room();
        new Thread(()->{
            room.study();
        },"t1").start();

        new Thread(()->{
            room.sleep();
        },"t2").start();
    }

     static class Room {
        //细粒度的锁
        Object o1 = new Object();
        Object o2 = new Object();
        public void study(){
            synchronized (o1){
                log.info("在学习");
            }
        }
        public void sleep(){
            synchronized (o2){
                log.info("在睡觉");
            }
        }

    }
}
