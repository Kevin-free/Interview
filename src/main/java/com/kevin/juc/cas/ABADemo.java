package com.kevin.juc.cas;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @description: ABA 问题
 * @author: Kevin
 * @createDate: 2020/2/26
 * @version: 1.0
 */
public class ABADemo {
    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100,1);

    public static void main(String[] args) {

        System.out.println("=========以下是 ABA 问题的产生=========");
        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "t1").start();

        new Thread(() -> {
            // 暂停1秒钟 t2 线程，确保 t1 线程完成了 ABA 操作
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(atomicReference.compareAndSet(100, 2020)+"\t"+atomicReference.get());
        }, "t2").start();

        // 暂停一会线程，确保以上操作已完成，不影响下面操作
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("=========以下是 ABA 问题的解决=========");

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第1次版本号：" + stamp);
            // 暂停1秒钟t3线程，确保t4线程拿到相同的初始版本号
            try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {e.printStackTrace();}
            atomicStampedReference.compareAndSet(100, 101, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t 第2次版本号：" + atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 第3次版本号：" + atomicStampedReference.getStamp());
        },"t3").start();

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第1次版本号：" + atomicStampedReference.getStamp());
            // 暂停3秒钟t4线程，确保t3线程完成ABA操作
            try{ TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) {e.printStackTrace();}
            boolean res = atomicStampedReference.compareAndSet(100, 2020, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t 修改成功与否：" + res+"\t当前实际版本号："+atomicStampedReference.getStamp());
            System.out.println(Thread.currentThread().getName() + "\t 当前实际最新值：" + atomicStampedReference.getReference());
        },"t4").start();

    }

}
