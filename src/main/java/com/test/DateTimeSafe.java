package com.test;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/28
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class DateTimeSafe {

    public static void main(String[] args) {

        // 线程安全（内部设计更多的为final修饰）
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 不安全
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TemporalAccessor parse = dateTimeFormatter.parse("2020-12-20");
        System.out.println(parse);
        Long l = new Long(0);
    }
}
