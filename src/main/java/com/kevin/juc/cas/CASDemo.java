package com.kevin.juc.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: CAS
 * 1. CAS 是什么 ？==》 CompareAndSwap
 *    比较并交换
 * @author: Kevin
 * @createDate: 2020/2/24
 * @version: 1.0
 */
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
atomicInteger.getAndIncrement();
        System.out.println(atomicInteger.compareAndSet(5, 2020)+"\t current data:"+atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 1024)+"\t current data:"+atomicInteger.get());
    }
}
