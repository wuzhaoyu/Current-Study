package com.design;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明: 两阶段打断模式
 *  Balking模式
 * 类修改者	创建日期2020/5/18
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class TwoTerminate {

    public static void main(String[] args) {

       /* InterruptDemo demo = new InterruptDemo();
        demo.start();
        Sleeper.sleep(3);
        demo.end();*/

        ValidateDemo validateDemo = new ValidateDemo();
        validateDemo.start();
        validateDemo.start();
        validateDemo.start();
        Sleeper.sleep(3);
        validateDemo.end();

    }

    /**
     * 普通
     */
    static class InterruptDemo{
        Thread monitor ;

        public void start(){
            monitor =  new Thread(()->{
                Thread thread = Thread.currentThread();
                while(true){
                    if(thread.isInterrupted()){
                        log.info("顺利结束");
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                        log.info("未结束");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        log.info("调用该方法，是正常结束线程的方式，打断标记不会清空true,程序会正常结束");
                        thread.interrupt();
                    }
                }
            },"线程监控");
            monitor.start();
        }

        public void end(){
            log.info("结束调用，打断睡眠中的线程会将打断标记清空false");
            monitor.interrupt();
        }



    }

    /**
     * validate
     */
    static class ValidateDemo{

        private volatile boolean flag = false;

        private volatile boolean starting = false;

        public void start(){

            //balking 犹豫模式 解决方法只调用一次
            // 1.多线程时同时去调用时，starting值并未修改完成
            /*if(starting){
                return;
            }
            starting = true;*/
            synchronized (this){
                if(starting){
                    return;
                }
                starting = true;
            }
            new Thread(()->{
                while(true){
                    if(flag){
                        log.info("顺利结束");
                        break;
                    }
                    Sleeper.sleep(1);
                   log.info("未结束");

                }
            },"线程监控").start();
        }

        public void end(){
            log.info("结束调用");
            flag  = true;
        }



    }
}
