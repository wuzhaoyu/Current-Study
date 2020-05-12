package com.design;

import com.n2.util.Sleeper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 类功能说明: 保护性延时或暂停设计模式
 * 类修改者	创建日期2020/5/5
 * 修改说明 :
 *   解决一个线程依赖另一个线程的运行结果
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "GuardedSuspension")
public class GuardedSuspension {

    public static void main(String[] args) {

        GuardedObject guardedObject = new GuardedObject();
        // 基础功能
        new Thread(()->{
           log.debug("获取结果");
           String a =  (String)guardedObject.get();
           log.debug("获取成功,结果为{}",a);
        },"t1").start();

        new Thread(()->{
            log.debug("执行程序 开始。。。。");
            Sleeper.sleep(5);
            guardedObject.complete("开心");
            log.debug("执行程序 结束。。。。");
        },"t2").start();

        // 超时测试
        /*new Thread(()->{
            log.debug("获取结果");
            String a =  (String)guardedObject.get(2000);
            log.debug("获取成功,结果为{}",a);
        },"t1").start();

        new Thread(()->{
            log.debug("执行程序 开始。。。。");
            Sleeper.sleep(3);
            //模拟虚假唤醒
            guardedObject.complete(null);
            log.debug("执行程序 结束。。。。");
        },"t2").start();*/
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Sleeper.sleep(1);
        for (Integer id : MainBoxes.getIds()) {
            new Postman(id, "内容" + id).start();
        }
    }
    /**
     * 邮递员
     */
    static class People extends Thread{

        @Override
        public void run(){
            GuardedObject guardedObject = MainBoxes.createGuardedObject();
            String mail =(String) guardedObject.get(5000);
            log.debug("收信id{},内容{}",guardedObject.getId(),mail);
        }
    }
    /**
     * 邮递员
     */
    static class Postman extends Thread{

        private Integer id;
        private String mail;

        public Postman(Integer id,String mail){
            this.id = id;
            this.mail = mail;
        }
        @Override
        public void run(){
            GuardedObject guardedObject = MainBoxes.get(id);
            log.debug("开始发信{}",id);
            guardedObject.complete(mail);
        }
    }

    /**
     * 作为解耦存在多个中间类GuardedObject
     */
    static class MainBoxes {
        //存在集合
       private static Map<Integer,GuardedObject> boxs = new Hashtable<>();

        private static int id = 0;

        private static synchronized int generateIds(){
            return id++;
        }

        public static GuardedObject createGuardedObject(){
            GuardedObject guardedObject = new GuardedObject();
            int i = generateIds();
            guardedObject.setId(i);
            boxs.put(i,guardedObject);
            return guardedObject;
        }

        public static Set<Integer> getIds(){
            return boxs.keySet();
        }

        //获取对象 并移除
        public static GuardedObject get(Integer id){
            return boxs.remove(id);
        }
    }


    /**
     *  中间结果类
     */
    static class GuardedObject {
        //结果
       private Object response;

       private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * 获取结果
         * @return
         */
        @SneakyThrows
        public Object get(){
            return  get(0);
        }
        /**
         * 获取结果
         * @param timeout 超时参数
         * @return
         */
        @SneakyThrows
        public Object get(long timeout){
            //开始时间 4:00
            long startTime = System.currentTimeMillis();
            long passTime = 0L;
            synchronized (this){
                while(response == null){
                    //防止虚假唤醒
                    long waitTime = timeout - passTime;
                    if(waitTime < 0 ){
                        break;
                    }
                    this.wait(waitTime);// 虚假唤醒 4:01
                    passTime = System.currentTimeMillis() - startTime;
                }
            }

            return response;
        }

        // 完成执行
        public void complete(Object response){
            synchronized (this){
                this.response = response;
                this.notifyAll();
            }
        }
    }
}
