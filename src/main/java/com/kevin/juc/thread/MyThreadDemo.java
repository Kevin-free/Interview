package com.kevin.juc.thread;

import java.util.Calendar;
import java.util.concurrent.*;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/11
 * @version: 1.0
 */
public class MyThreadDemo {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5); // 一池5个处理线程
//        ExecutorService threadPool = Executors.newSingleThreadExecutor(); // 一池1个处理线程
//        ExecutorService threadPool = Executors.newCachedThreadPool(); // 一池N个处理线程

        try{
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName()+"\t 办理任务");
                });
                // 暂停一会线程
                try{ TimeUnit.MILLISECONDS.sleep(200); } catch(InterruptedException e){ e.printStackTrace();}
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }
}




