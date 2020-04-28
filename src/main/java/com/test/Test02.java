package com.test;

import com.Constants;
import com.n2.util.FileReader;
import com.sun.imageio.plugins.common.ReaderUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/4/10
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "c.test02")
public class Test02 {

    public static void main(String[] args) {

        Thread thread = new Thread("t1"){
          @Override
            public void run(){
              log.debug("执行状态{}",this.getState());
              FileReader.read(Constants.MP4_FULL_PATH);
              try {
                  Thread.sleep(1000);
                  log.debug("执行状态{}",this.getState());
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
        };
        //直接执行run方法，是由main线程执行，没有异步执行
        //thread.run();
        System.out.println(thread.getState() + "NEW");
        thread.start();
        System.out.println(thread.getState() + "RUNNABLE");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Thread.sleep() 当前线程睡眠 线程睡眠时的状态为TIMED_WAITING
        System.out.println(thread.getState() + "TIMED_WAITING");
    }
}
