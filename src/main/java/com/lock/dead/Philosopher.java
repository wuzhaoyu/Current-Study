package com.lock.dead;

import com.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明:
 *  哲学家吃饭问题死锁
 *   每个人都需要获取两个筷子才能够调用eat方法，最终每个人都占有一支筷子（相互依赖占有）
 * 类修改者	创建日期2020/5/12
 * 修改说明
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class Philosopher extends Thread {

    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        //都在等待对方锁释放，导致死锁
        new Philosopher("阿基米德", c5, c1).start();
        // 顺序调用 使c5释放锁；赫拉克利特执行，释放c4以此类推就不会导致死锁
        new Philosopher("阿基米德", c1, c5).start();
    }

    private Chopstick left;
    private Chopstick right;

    public Philosopher(String name,Chopstick left,Chopstick right){
        super(name);
        this.right = right;
        this.left = left;
    }

    @Override
    public void run(){
        while(true){
            synchronized (left){
                synchronized (right){
                    this.eat(super.getName());
                }
            }
        }
    }

    public void eat(String name){
        Sleeper.sleep(0.5);
        log.info("{}--正在吃饭",name);
    }
}
