package com.kevin.juc.blockQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @description: SynchronousQueue:不存储元素的阻塞队列,也即是单个元素的队列.
 * 专属品版阻塞队列
 * @author: Kevin
 * @createDate: 2020/3/7
 * @version: 1.0
 */
public class SynchronousQueueDemo {

    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName()+"\t put 1");
                blockingQueue.put("1");

                System.out.println(Thread.currentThread().getName()+"\t put 2");
                blockingQueue.put("2");

                System.out.println(Thread.currentThread().getName()+"\t put 3");
                blockingQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AAA").start();

        new Thread(() -> {
            try {
                try{ TimeUnit.SECONDS.sleep(3); } catch(InterruptedException e){ e.printStackTrace();}
                System.out.println(Thread.currentThread().getName()+"\t take"+blockingQueue.take());

                try{ TimeUnit.SECONDS.sleep(3); } catch(InterruptedException e){ e.printStackTrace();}
                System.out.println(Thread.currentThread().getName()+"\t take"+blockingQueue.take());

                try{ TimeUnit.SECONDS.sleep(3); } catch(InterruptedException e){ e.printStackTrace();}
                System.out.println(Thread.currentThread().getName()+"\t take"+blockingQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BBB").start();
    }
}
