package com.unlock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/23
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class AtomicIntegerTest {

    public static void main(String[] args) {

        AtomicInteger i = new AtomicInteger(0);

        System.out.println(i.incrementAndGet());
        System.out.println(i.getAndIncrement());

        System.out.println(i.getAndAdd(5));
        System.out.println(i.addAndGet(5));

        System.out.println(compareResult((t) -> t * 2));
    }

    public static int compareResult(FunctionInterface function){

        int i = function.compareMethod(2);
        return i;


    }
}
