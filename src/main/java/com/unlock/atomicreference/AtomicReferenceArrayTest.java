package com.unlock.atomicreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/26
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class AtomicReferenceArrayTest {

    public static void main(String[] args) {
        // 不安全
        demo(
                ()-> new int[10],
                (array) -> array.length,
                (array,index) -> array[index]++,
                (array)-> System.out.println(Arrays.toString(array))
        );
        demo(
                ()-> new AtomicIntegerArray(10),
                (array) -> array.length(),
                (array,index) ->array.getAndIncrement(index),
                (array)-> System.out.println(array)
        );

    }

    /**
     *  参数1，提供数组、可以是线程不安全数组或线程安全数组
     *  参数2，获取数组长度的方法
     *  参数3，自增方法，回传 array, index
     *  参数4，打印数组的方法
     *
     *  Supplier 提供者 无中生有 （）->结果
     *  Function 函数 一个参数一个结果（参数）->结果，BiFunction （参数1，参数2）->结果
     *  Consumer 消费者 没有参数 （参数）->void，BiConsumer(参数1，参数2)
     * @param arraySupplier
     * @param <T>
     */
    private static <T> void  demo(
            Supplier<T> arraySupplier,
            Function<T,Integer> lengthFun,
            BiConsumer<T,Integer> putConsumer,
            Consumer<T> printerConsumer){
        List<Thread> ts = new ArrayList<>();
        //获取数组
        T array = arraySupplier.get();
        //数组长度
        Integer length = lengthFun.apply(array);
        for(int i=0; i< length; i++){
            // 每个线程对数组作 10000 次操作
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j%length);
                }
            }));
        }
        // 启动所有线程
        ts.forEach(t -> t.start());
        // 等所有线程结束
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //打印数组
        printerConsumer.accept(array);
    }
}
