package com.kevin.juc.blockQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：synchronized 和 Lock 有什么区别？用心的Lock有什么好处？你举例说说
    1. 原始构成
        synchronized 是关键字，属于JVM层面，
            monitorenter(底层是通过monitor对象来完成，其实wait/notify等方法也依赖于monitor对象，只有在同步块或方法中才能调用wait/notify等方法)
            monitorexit
        Lock 是具体类（java.util.concurrent.locks.Lock）是api层面的锁

    2. 使用方法
        synchronized 不需要用户手动释放锁，当 synchronized 代码执行完毕后系统会自动让线程释放对锁的占用
        ReentrantLock 则需要用户手动释放锁，若没有主动释放锁，就有可能出现死锁的现象
            需要lock() 和 unlock() 方法配合try/finally语句块来完成

    3. 等待是否可中断
        synchronized 不可中断，除非抛出异常或者正常运行完成
        ReentrantLock 可中断， 1. 设置超时方法 tryLock(long time, TimeUnit unit)
                              2. LockInterruptibly() 放代码块中，调用 interrupt() 方法可中断

    4. 加锁是否公平
        synchronized 非公平锁
        ReentrantLock 两者都可以，默认非公平锁，构造方法可传入boolean值， true为公平锁， false为非公平锁

    5. 锁绑定多个条件condition
        synchronized 没有
        ReentrantLock 用来实现分组唤醒需要唤醒的线程们，可以精确唤醒！而不是像synchronized 要么随机唤醒一个要么唤醒全部线程。
 *
 * ==========================================
 *
 * 以下举例：多线程之间按顺序调用，实现 A --> B--> C 依次循环调用
 */

// 资源类
class ShareResource{
    private int n = 1;  // A:1, B:2, C:3
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void p5(){
        lock.lock();
        try{
            // 1. 判读
            while(n!= 1){
                c1.await();
            }
            // 2. 干活
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName()+"\t "+ i);
            }
            // 3. 通知
            n = 2;
            c2.signal();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    public void p10(){
        lock.lock();
        try{
            // 1. 判读
            while(n!= 2){
                c2.await();
            }
            // 2. 干活
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName()+"\t "+ i);
            }
            // 3. 通知
            n = 3;
            c3.signal();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    public void p15(){
        lock.lock();
        try{
            // 1. 判读
            while(n!= 3){
                c3.await();
            }
            // 2. 干活
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName()+"\t "+ i);
            }
            // 3. 通知
            n = 1;
            c1.signal();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
}

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/9
 * @version: 1.0
 */
public class SyncAndReentrantLock {
    public static void main(String[] args) {
        ShareResource shareDate = new ShareResource();
        new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                shareDate.p5();
            }
        },"AA").start();
        new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                shareDate.p10();
            }
        },"BB").start();
        new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                shareDate.p15();
            }
        },"CC").start();
    }
}
