package com.kevin.juc.thread;


import java.util.concurrent.*;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/13
 * @version: 1.0
 */
public class MyThreadPoolDemo {
    public static void main(String[] args) {

        // 此电脑CPU核数
        System.out.println(Runtime.getRuntime().availableProcessors());

        // ThreadPoolExecutor(
        //                              int corePoolSize,
        //                              int maximumPoolSize,
        //                              long keepAliveTime,
        //                              TimeUnit unit,
        //                              BlockingQueue<Runnable> workQueue,
        //                              ThreadFactory threadFactory,
        //                              RejectedExecutionHandler handler)
        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                5,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
//new ThreadPoolExecutor.AbortPolicy();  最多同时执行任务数 = maximumPoolSize + workQueue
//new ThreadPoolExecutor.CallerRunsPolicy();
//new ThreadPoolExecutor.DiscardOldestPolicy();
//new ThreadPoolExecutor.DiscardPolicy();

        try{
            for (int i = 1; i <= 8; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName()+"\t 办理任务");
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }
}
