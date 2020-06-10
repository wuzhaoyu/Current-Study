package com.lock.singleton;

import java.io.Serializable;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/20
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public final class HungrySingleton implements Serializable {

    private HungrySingleton(){}

    private static final HungrySingleton INSTANCE = new HungrySingleton();

    public HungrySingleton getInstance(){
        return INSTANCE;
    }

    private Object readResolve(){
        return INSTANCE;
    }

}
