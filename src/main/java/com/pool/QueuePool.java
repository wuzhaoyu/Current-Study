package com.pool;

import com.n2.util.Sleeper;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/6/1
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
@Slf4j
public class QueuePool {

    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(2, 1, TimeUnit.SECONDS, 5,((queue, task) -> {
            // 死等
            //    queue.put(task);
            // 等待超时
            //queue.offer(500,TimeUnit.MILLISECONDS,task);
            // 3) 让调用者放弃任务执行
//             log.debug("放弃{}", task);
            // 4) 让调用者抛出异常
//            throw new RuntimeException("任务执行失败 " + task);
            // 5) 让调用者自己执行任务
            task.run();
        }));
        for (int i = 0; i < 15; i++) {
            int j = i;
            pool.execute(() -> {
                Sleeper.sleep(1);
                log.info("{}", j);
            });
        }

    }

    static class ThreadPool {

        // 队列
        private BlockingQueue<Runnable> takeQueues;

        private HashSet<Worker> workers = new HashSet<>();

        //核心线程数
        private int coreSize;

        // 超时时间
        private long timeout;

        // 时间单位
        private TimeUnit timeUnit;

        //策略
        private RejectPolicy<Runnable> policy;

        public void execute(Runnable runnable) {
            // 若队列的task任务没有超过，coreSize就交给我work线程执行
            // 若队列的task任务超过，coreSize就加入队列中
            synchronized (workers) {
                if (workers.size() < coreSize) {
                    Worker worker = new Worker(runnable);
                    workers.add(worker);
                    log.info("新增 work {} {} ", worker, runnable);
                    worker.start();
                } else {
                    // 进入队列策略，若将策略写在线程池中，代码将非常冗余
                    // 将处理处理进入的队列方式的权利下方给我任务执行者
//                    takeQueues.put(runnable);
//                    takeQueues.offer(1, TimeUnit.SECONDS, runnable);
                    takeQueues.tryPut(policy,runnable);
                    // 死等
                    // 超时等待
                    //
                }
            }
        }

        public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int capacity,RejectPolicy<Runnable> rejectPolicy) {
            this.takeQueues = new BlockingQueue<>(capacity);
            this.coreSize = coreSize;
            this.timeout = timeout;
            this.timeUnit = timeUnit;
            this.policy = rejectPolicy;
        }

        class Worker extends Thread {

            private Runnable task;

            public Worker(Runnable task) {
                this.task = task;
            }

            @Override
            public void run() {
                // 执行任务
                // task不为空,执行任务
                // task执行完成，在从队列中获取任何执行
                // 这里会一直执行队列中的任务 因为take方式会一直队列获取任务
//                while(task != null || (task = takeQueues.take()) != null){
                while (task != null || (task = takeQueues.poll(timeout, timeUnit)) != null) {
                    try {
                        log.info("正在执行{}", task);
                        task.run();
                    } catch (Exception e) {

                    } finally {
                        task = null;
                    }
                }
                synchronized (workers) {
                    workers.remove(this);
                    log.info("work 被移除{}", this);
                }
            }
        }

    }

    /**
     * 策略模式
     */
    @FunctionalInterface
    interface RejectPolicy<T> {

        void reject(BlockingQueue<T> queue, T task);

    }


    static class BlockingQueue<T> {

        //队列类型 双向类列表
        private Deque<T> blockQueue = new ArrayDeque<>();

        // 获取队列task 需要lock
        private ReentrantLock lock = new ReentrantLock();

        // 生产者（创建任务）,队列任务满的话，需要到阻塞队列中
        private Condition waitSetFull = lock.newCondition();

        // 消费者（创建线程），队列任务为空，也需要到阻塞队列中
        private Condition waitSetEmpty = lock.newCondition();

        //容量
        private int capacity;

        public BlockingQueue(int capacity) {
            this.capacity = capacity;
        }

        // 获取任务 待超时
        public T poll(long timeout, TimeUnit unit) {
            lock.lock();
            // 将时间转换为纳秒
            long l = unit.toNanos(timeout);
            try {
                //不断去在队列中尝试获取
                while (blockQueue.isEmpty()) {
                    if (l <= 0) {
                        return null;
                    }
                    try {
                        // 虚假唤醒问题
                        // 该方法会在产生虚假唤醒情况下，减去已等待时间
                        // 剩余等待的时间为在阻塞队列中等待时间
                        l = waitSetEmpty.awaitNanos(l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                T t = blockQueue.removeFirst();
                //唤醒 waitSetFull （队列满的阻塞）
                waitSetFull.signal();
                return t;
            } finally {
                lock.unlock();
            }
        }

        // 获取任务
        public T take() {
            lock.lock();
            try {
                //不断去在队列中尝试获取
                while (blockQueue.isEmpty()) {
                    try {
                        waitSetEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                T t = blockQueue.removeFirst();
                //唤醒 waitSetFull （队列满的阻塞）
                waitSetFull.signal();
                return t;
            } finally {
                lock.unlock();
            }
        }

        //添加任务
        public void put(T task) {
            lock.lock();
            try {
                // 不断尝试加入任务
                while (blockQueue.size() == capacity) {
                    try {
                        log.info("等待加入队列 queue {}", task);
                        waitSetFull.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                blockQueue.addLast(task);
                log.info("加入队列 queue {}", task);
                //唤醒 waitSetEmpty（为空的阻塞）
                waitSetEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        /**
         * 任务队列的满时，提供超时
         *
         * @return
         */
        public boolean offer(long timeout, TimeUnit timeUnit, T task) {
            lock.lock();
            try {
                long l = timeUnit.toNanos(timeout);
                // 不断尝试加入任务
                while (blockQueue.size() == capacity) {
                    if (l <= 0) {
                        return false;
                    }
                    try {
                        log.info("等待加入队列 queue {}", task);
                        l = waitSetFull.awaitNanos(l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                blockQueue.addLast(task);
                log.info("加入队列 queue {}", task);
                //唤醒 waitSetEmpty（为空的阻塞）
                waitSetEmpty.signal();
                return true;
            } finally {
                lock.unlock();
            }
        }

        /**
         * 入队列的策略
         * @param rejectPolicy
         */
        public void tryPut(RejectPolicy<T> rejectPolicy,T task) {
            lock.lock();
            try {
                if (blockQueue.size() == capacity) {
                    rejectPolicy.reject(this,task);
                }else{
                    blockQueue.addLast(task);
                    log.info("加入队列 queue {}", task);
                    //唤醒 waitSetEmpty（为空的阻塞）
                    waitSetEmpty.signal();
                }

            } finally {
                lock.unlock();
            }

        }

        public int size() {
            lock.lock();
            try {
                return blockQueue.size();
            } finally {
                lock.unlock();
            }
        }
    }
}
