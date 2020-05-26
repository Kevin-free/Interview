package com.kevin.juc.conditionThread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/6
 * @version: 1.0
 */
public class CyclicBarrierDemo {
    public static void main(String[] args){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,new Thread(() -> {
            System.out.println("******召唤神龙");
        }));

        for(int i = 1; i <= 7; i++){
            final int tempInt = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t 找到第" + tempInt + "颗龙珠");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
