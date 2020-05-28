package com.unlock.unsafe;

import com.unlock.Account;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 类功能说明: 模拟AtomicInteger
 * 类修改者	创建日期2020/5/27
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class MyAtomicInteger implements Account {

    public static void main(String[] args) {
        Account account = new MyAtomicInteger(10000);
        Account.demo(account);
    }

    // 获取unsafe对象
    // 内部维护value值
    private volatile int value;
    private  static long fieldOffset;
    private static final Unsafe UNSAFE;
    static {
        UNSAFE = UnsafeAccessor.getUnsafe();
        try {
            Field field = MyAtomicInteger.class.getDeclaredField("value");
            field.setAccessible(true);
            // 对象可以视为内存，属性相对内存的偏移量，该类的不同实例的同一属性偏移量相等。
             fieldOffset = UNSAFE.objectFieldOffset(field);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    public int getValue(){
        return value;
    }

    public void addIncrement(int amount){
        while(true){
            int prve = this.value ;
            int next = value - amount;
            // 通过对象实例与内存中相对偏移量能够确定该实例属性字段的值
            if( UNSAFE.compareAndSwapInt(this,fieldOffset,prve,next)){
               return;
            }
        }
    }

    public MyAtomicInteger(int value) {
        this.value = value;
    }

    @Override
    public Integer getBalance() {
        return this.value;
    }

    @Override
    public void withdraw(Integer amount) {
         this.value = this.value - amount;
    }
}
