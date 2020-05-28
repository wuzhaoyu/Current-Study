package com.unlock.atomicreference;

import com.n2.util.Sleeper;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/26
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class ABA {
    static AtomicStampedReference<String> ref =  new AtomicStampedReference<>("A",0);
    static AtomicReference<String> stringAtomicReference = new AtomicReference<>("A");
    public static void main(String[] args) {
        /*new Thread(()->{
            Sleeper.sleep(1);
            if(stringAtomicReference.compareAndSet("A","B")){
                log.info("3---- A-->B 修改成功");
            }
        }).start();
        other(stringAtomicReference);*/
        new Thread(()->{
            int stamp = ref.getStamp();
            String reference = ref.getReference();
            Sleeper.sleep(1);
                if(ref.compareAndSet(reference,"B",stamp,stamp + 1)){
                log.info("3 版本号：{} A-->B 修改成功",stamp);
            }else{
                log.info("3 版本号：{} A-->B 修改失败",stamp);
            }
        }).start();
        resolve();
    }


   public static void other(){
       new Thread(()->{
           stringAtomicReference.compareAndSet("A","B");
           log.info("1---- A-->B 修改成功");
       }).start();
       Sleeper.sleep(0.5);
       new Thread(()->{
           stringAtomicReference.compareAndSet("B","A");
           log.info("2---- B-->A 修改成功");
       }).start();
   }

    /**
     *  解决
     */
   public static void resolve(){
       String reference = ref.getReference();
       new Thread(()->{
           ref.compareAndSet( ref.getReference(),"B",ref.getStamp(), ref.getStamp() + 1);
           log.info("1 版本号：{} A-->B 修改成功",ref.getStamp());
       }).start();
       Sleeper.sleep(0.5);
       new Thread(()->{
           ref.compareAndSet("B", ref.getReference(),ref.getStamp(), ref.getStamp() + 1);
           log.info("2 版本号：{} B-->A 修改成功",ref.getStamp());
       }).start();
   }
}
