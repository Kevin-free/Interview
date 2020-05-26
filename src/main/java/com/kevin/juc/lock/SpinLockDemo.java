package com.kevin.juc.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: 自旋锁示例
 * @author: Kevin
 * @createDate: 2020/3/3
 * @version: 1.0
 */
public class SpinLockDemo {
    private AtomicReference<Thread> atomicReference = new AtomicReference<>();

    private void myLock(){
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName()+"\t come in");
        while (!atomicReference.compareAndSet(null, thread)) {

        }
    }

    private void myUnlock(){
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(Thread.currentThread().getName()+"\t invoked unlock");
    }

    /**
     * AA	 come in
     * BB	 come in
     * AA	 invoked unlock
     * BB	 invoked unlock
     * @param args
     */
    public static void main(String[] args){
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(() -> {
            spinLockDemo.myLock();
            try{TimeUnit.SECONDS.sleep(5);}catch (InterruptedException e){e.printStackTrace();}
            spinLockDemo.myUnlock();
        },"AA").start();

        try{TimeUnit.SECONDS.sleep(1);}catch (InterruptedException e){e.printStackTrace();}

        new Thread(() -> {
            spinLockDemo.myLock();
            try{TimeUnit.SECONDS.sleep(1);}catch (InterruptedException e){e.printStackTrace();}
            spinLockDemo.myUnlock();
        },"BB").start();
    }
}
