package com.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 类功能说明: 相比较与Runnable 该方式存在回调返回值
 * 类修改者	创建日期2020/4/8
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "FutureTaskCall")
public class FutrueTaskCall {

    public static void test01() throws ExecutionException, InterruptedException {
        FutureTask<Integer> task  = new FutureTask<Integer>(new Callable() {
            @Override
            public Object call() throws Exception {
                log.debug("测试一");
                Thread.sleep(2000);
                return 100;
            }
        });
        Thread thread = new Thread(task,"线程一");
        thread.start();

        //会阻塞一直到call方法返回
        log.debug("{}",task.get());

    }

    public static void main(String[] args) {
        try {
            test01();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
