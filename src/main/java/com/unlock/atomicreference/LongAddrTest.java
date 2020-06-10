package com.unlock.atomicreference;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 类功能说明: 原子累加器
 * 类修改者	创建日期2020/5/26
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class LongAddrTest {

    public static void main(String[] args) {

        for(int i=0; i< 5; i++){
            // 原子long增加
            demo(()->new AtomicLong(0),arary->arary.getAndIncrement());
        }
        log.info("==========================");
        for(int i=0; i< 5; i++){
            //原子累加器的累加方法
            demo(()->new LongAdder(), arary->arary.increment());
        }
    }

    /**
     *
     * @param supplier 提供者 （）->返回值
     * @param consumer 消费者 （参数）-> 无返回值
     * @param <T>
     */
    public static <T> void demo(Supplier<T> supplier, Consumer<T> consumer){

        T adder = supplier.get();

        long start = System.nanoTime();
        List<Thread> ts = new ArrayList<>();
        // 4 个线程，每人累加 50 万
        for (int i = 0; i < 40; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    consumer.accept(adder);
                }
            }));
        }
        ts.forEach(t -> t.start());
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(adder + " cost:" + (end - start)/1000_000);

    }
}
