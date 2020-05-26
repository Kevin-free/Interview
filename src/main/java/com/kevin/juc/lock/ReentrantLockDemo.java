package com.kevin.juc.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: ReentrantLock 是典型的可重入锁
 * @author: Kevin
 * @createDate: 2020/3/2
 * @version: 1.0
 */
class Mobile implements Runnable{
    private Lock lock = new ReentrantLock(true);

    @Override
    public void run() {
        get();
    }

    private void get(){
        lock.lock();
//        lock.lock(); // 只要配对使用！！多重锁也可以编译运行成功
        try{
            System.out.println(Thread.currentThread().getName() + "\t get");
            set();
        }finally {
            lock.unlock();
//            lock.unlock();
        }
    }

    private void set(){
        lock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + "\t ###set");
        }finally {
            lock.unlock();
        }
    }
}
public class ReentrantLockDemo {
    /**
     * Thread-0	 get
     * Thread-0	 ###set
     * Thread-1	 get
     * Thread-1	 ###set
     */
    public static void main(String[] args) {
        Mobile mobile = new Mobile();
        Thread t3 = new Thread(mobile);
        Thread t4 = new Thread(mobile);
        t3.start();
        t4.start();
    }
}
