package com.kevin.juc.conditionThread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/6
 * @version: 1.0
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        // 模拟 3 个停车位资源
        Semaphore semaphore = new Semaphore(3);

        // 模拟 6 部车
        for(int i = 1; i <= 6; i++){
            new Thread(() -> {
                try {
                    // Acquires a permit from this semaphore, blocking until one is available,
                    // or the thread is {@linkplain Thread#interrupt interrupted}.
                    // 从此信号量获取许可，直到获得一个许可为止,
                    // 或线程为 interrupted 。
                    semaphore.acquire();  //抢到资源
                    System.out.println(Thread.currentThread().getName()+"抢到车位");
                    try{ TimeUnit.SECONDS.sleep(3); } catch(InterruptedException e){ e.printStackTrace();}
                    System.out.println(Thread.currentThread().getName()+"停车3秒后离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    // Releases a permit, returning it to the semaphore.
                    // 释放许可证，将其返回到信号量。
                    semaphore.release();  // 释放资源
                }
            },String.valueOf(i)).start();
        }
    }
}
