package com.unlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/5/23
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public interface Account {

    public static void main(String[] args) {
        // 不安全 无锁
        /*Account accountUnsafe = new AccountUnsafe(10000);
        Account.demo(accountUnsafe);*/

       // synchronize 加锁
        Account accountSynchronize = new AccountSynchronized(10000);
        Account.demo(accountSynchronize);

        // Cas 无锁
       /*  Account accountCas = new AccountCas(10000);
        Account.demo(accountCas);*/
    }

    class AccountCas implements Account{

        //余额
        private AtomicInteger balance;

        public AccountCas(Integer balance) {
            this.balance = new AtomicInteger(balance);
        }

        @Override
        public Integer getBalance() {
            return balance.get();
        }

        @Override
        public void withdraw(Integer amount) {
            //CPU 指令级别 原子操作不可分割
           while(true){
               int prve = balance.get();
               int next = prve - amount;
               // CAS 比较并设置 机制：会以prve与当前最新的balance值作比较，如果过相同则将值设置为next
               // 若失败则不断进行尝试
               if(balance.compareAndSet(prve,next)){
                   break;
               }
           }
        }
    }
    class AccountSynchronized implements Account{

        //余额
        private Integer balance;

        public AccountSynchronized(Integer balance) {
            this.balance = balance;
        }

        @Override
        public Integer getBalance() {
            return this.balance;
        }

        @Override
        public void withdraw(Integer amount) {
           synchronized (this){
               this.balance -= amount;
           }
        }
    }
    class AccountUnsafe implements Account{

        //余额
        private Integer balance;

        public AccountUnsafe(Integer balance) {
            this.balance = balance;
        }

        @Override
        public Integer getBalance() {
            return this.balance;
        }

        @Override
        public void withdraw(Integer amount) {
            this.balance -= amount;
        }
    }

    // 获取余额
    Integer getBalance();
    // 取款
    void withdraw(Integer amount);
    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        //创建1000个线程
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance()
                + " cost: " + (end-start)/1000_000 + " ms");
    /*    无保户：      250 cost: 197 ms
        synchronied： 0 cost: 209 ms
        CAS无锁：     0 cost: 279 ms*/
    }
}
