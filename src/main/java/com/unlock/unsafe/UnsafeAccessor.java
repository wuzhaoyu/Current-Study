package com.unlock.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/27
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class UnsafeAccessor {

    private static final Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
             unsafe = (Unsafe)  theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
           throw new Error(e);
        }
    }

    public static Unsafe getUnsafe(){
        return unsafe;
    }
}
