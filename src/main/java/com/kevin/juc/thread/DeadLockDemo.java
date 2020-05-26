package com.kevin.juc.thread;

import java.util.concurrent.TimeUnit;

class HoldLockThread implements Runnable
{
    String lockA;
    String lockB;

    public HoldLockThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+"\t 持有自己的锁："+lockA+"\t 尝试获取："+lockB);
            // 暂停一会线程
            try{ TimeUnit.SECONDS.sleep(2); } catch(InterruptedException e){ e.printStackTrace();}
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+"\t 持有自己的锁："+lockB+"\t 尝试获取："+lockA);
            }
        }
    }
}

/**
 * @description: 死锁示例
 * 死锁是指两个或两个以上的线程在执行过程中，
 * 因争夺资源而造成互相等待的一种情况，
 * 若无外力干涉他们都将无法推进下去
 * @author: Kevin
 * @createDate: 2020/3/15
 * @version: 1.0
 */
public class DeadLockDemo {

    public static void main(String[] a){
        String lockA = "lockA";
        String lockB = "lockB";
        new Thread(new HoldLockThread(lockA,lockB),"threadA").start();
        new Thread(new HoldLockThread(lockB,lockA),"threadB").start();
    }
}
