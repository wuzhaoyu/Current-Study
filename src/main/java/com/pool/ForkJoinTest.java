package com.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/6/7
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "ForkJoinTest")
public class ForkJoinTest {

    public static void main(String[] args) {

        // parallelism 平行度 水平执行的并行
        ForkJoinPool pool = new ForkJoinPool(5);
        System.out.println(pool.invoke(new MyTask(5)));
    }



}

@Slf4j(topic = "MyTask")
class MyTask extends RecursiveTask{

     int n;

    public MyTask(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "MyTask{" +  "n=" + n + '}';
    }

    @Override
    protected Object compute() {

        if(n == 1){
            log.debug("join() {}", n);
            return 1;
        }

        // 将任务进行拆分(fork)
        MyTask t1 = new MyTask(n - 1);
        t1.fork();
        log.debug("fork() {} + {}", n, t1);

        // 合并(join)结果
        int result = n + (int)t1.join();
        log.debug("join() {} + {} = {}", n, t1, result);

        return result;
    }
}
