package com.test;

import com.n2.util.Sleeper;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/4/8
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "Thread.SynchronizedTest")
public class SynchronizedTest {

    @SneakyThrows
    public static void main(String[] args) {
        Number number = new Number();
        Thread t1 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("开始执行s1");
                number.a();
            }
        };
        Thread t2 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("开始执行s2");
                number.b();
            }
        };
        Thread t3 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("开始执行s3");
                number.c();
            }
        };
        t1.start();
        t2.start();
        t3.start();
    }

    @Slf4j(topic = "SynchronizedTest.number")
    static class Number{
        // synchronized关键字
        // 方法锁 静态方法锁定的为类对象，普通方法为对象实例 若达到互斥效果必须统一锁的对象
        public synchronized  void  a(){
           Sleeper.sleep(1);
           log.info("a");
        }
        static synchronized  void  b(){
            log.info("b");
        }
        public   void  c(){
            log.info("c");
        }
    }

}
