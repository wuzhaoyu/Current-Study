package com.lock.dead;

import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明: 筷子
 * 类修改者	创建日期2020/5/12
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class Chopstick {

    String name;

    public Chopstick(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }

}
