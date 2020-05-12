package com.lock.dead;

import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明: 死锁
 *
 *  排查死锁
 *   第一种： jps获取进程号number， 执行：jstack number 进程号 查看
 *   第二种： jconsole查看
 *
 * 类修改者	创建日期2020/5/12
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "死锁")
public class DeadLock {

    public static void main(String[] args) {

        Object A = new Object();
        Object B = new Object();
        Thread t1 = new Thread(()->{
            synchronized (A){
               log.info("获取锁A");
               synchronized(B){
                   log.info("A中获取锁B");
               }
            }

        },"t1");

        Thread t2 = new Thread(()->{
            synchronized (B){
                log.info("获取锁A");
                synchronized(A){
                    log.info("A中获取锁B");
                }
            }
        },"t2");

        t1.start();
        t2.start();

    }


}
