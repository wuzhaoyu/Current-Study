package com.lock;

/**
 * 类功能说明:  锁消除
 *
 * 类修改者	创建日期2020/5/16
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class RemoveLock {

    static int x = 0;


    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        System.out.println(start);
        for(int i=0;i<10000000;i++){
           a();
       }
        long aend = System.currentTimeMillis();
        System.out.println(aend);

        //
        for(int j=0;j<10000000;j++){
            b();
        }
        long bend = System.currentTimeMillis();
        System.out.println(bend);

        System.out.println(aend - start);
        System.out.println(bend - aend);
    }

    public static void a(){
        x++;
    }

    public static void b(){
        // JIT(Just In Time) 即时解释编译器
        // 该锁为局部变量，不存在锁的竞争，
        // JIT优化将锁消除，这将大大提高程序运行效率
        // -XX:-EliminateLocks 改参数会将默认的lock优化去除
        Object o = new Object();
        synchronized (o){
            x++;
        }

    }
}
