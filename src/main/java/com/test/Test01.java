package com.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/4/8
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "Thread.test01")
public class Test01 {


    public static void test01(){
        Runnable r = ()->{
           log.debug("测试一");
        };
        Thread thread = new Thread(r,"线程一");
        thread.start();
    }

    public static void test02(){
        while (true){
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    log.debug("测试二");
                }
            };
            Thread thread = new Thread(r,"线程一");
            thread.start();
        }

    }

    public static void main(String[] args) {
        test01();
        test02();
    }

}
