package com.design;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * 类功能说明: 异步消息队列 为线程间通讯
 * 类修改者	创建日期2020/5/6
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "MessageQueue")
public class MessageQueue {

    public static void main(String[] args) {

        MessageQueue messageQueue = new MessageQueue(2);
        for (int i=0;i<50;i++){
            int id = i;
            new Thread(()->{
                     messageQueue.put(new Message(id,"消息"));
            },"生产者").start();
        }
        new Thread(()->{
            while(true){
                Sleeper.sleep(1);
                messageQueue.take();
            }
        },"消费者").start();
    }

    //容器
    private LinkedList<Message> linkedList = new LinkedList<>();

    //大小
    private int capcity;

    public MessageQueue(int capcity){
        this.capcity = capcity;
    }

    //获取消息
    public Message take(){
        //检查队列是否为空
        synchronized (linkedList){
            while(linkedList.isEmpty()){
                try {
                    linkedList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //获取的时候从队列的第一个获取
            Message message = linkedList.removeFirst();
            log.debug("消息被消费{}",message);
            //获取完成后唤醒put方法
            linkedList.notifyAll();
            return message;
        }
    }
    //存入消息
    public void put(Message message){
        synchronized (linkedList){
            if(linkedList.size() == capcity){
                log.debug("消息已满");
                try {
                    linkedList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //消息存入队列尾部
            linkedList.addLast(message);
            log.debug("消息产生{}",message);
            // 通知获取的线程
            linkedList.notifyAll();
        }


    }

    final static class Message{

        private Integer id;
        private Object message;

        public Message(Integer id, Object message) {
            this.id = id;
            this.message = message;
        }

        public Integer getId() {
            return id;
        }

        public Object getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "id=" + id +
                    ", message=" + message +
                    '}';
        }
    }
}
