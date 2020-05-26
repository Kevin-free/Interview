package com.kevin.juc.conditionThread;

import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/4
 * @version: 1.0
 */
public class CountDownLatchDemo {
    private static final int COUNT = 6;

    public static void main(String[] args) throws InterruptedException {
//        launch();
        unifyCountry();
    }

    private static void unifyCountry() throws InterruptedException{
        CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        for(int i = 1; i<=COUNT;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t 被灭");
                countDownLatch.countDown();
            },CountryEnum.forEach_countryEnum(i).getRetMsg()).start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"**** 秦灭六国！一统华夏！");
    }

    private static void launch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for(int i = 1; i<=6;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"准备就绪");
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"****火箭发射！！！");
    }
}
