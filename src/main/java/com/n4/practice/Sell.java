package com.n4.practice;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/4/18
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j(topic = "Sell")
public class Sell {
    static Random random = new Random();

    static public int countRandom(){
        return (random.nextInt(5) + 1);
    }

    public static void main(String[] args) throws InterruptedException {

        List<Integer> sum = new Vector<>();
        List<Thread> threads = new ArrayList<>();
        TicketWindow window = new TicketWindow(4000);
          for (int i=0 ;i < 2000;i++){
              Thread thread = new Thread(()->{
                  Sleeper.sleep(countRandom());
                  int a = countRandom();
                  int sell = window.sell(a);
                  sum.add(sell);
              });
              thread.start();
              threads.add(thread);
          }
          for (Thread thread: threads){
              thread.join();
          }
          log.info("总共卖出：{}" ,sum.stream().mapToInt(i->i).sum());
          log.info("剩余票数：{}" ,window.acount);
    }



     static  class TicketWindow{

        //总共票数
        private int acount = 0 ;

        public TicketWindow(int acount){
            this.acount = acount;
        }

        private synchronized int sell(int count){
            if(acount >= count){
                acount -= count;
                return count;
            }else{
                return 0;
            }
        }

    }
}
