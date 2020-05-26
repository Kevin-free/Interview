package com.kevin.juc.blockQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 资源类
class ShareDate{
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void produce() throws Exception{
        lock.lock();
        try{
            // 1. 判断，多线程一定要用 while ！防止虚假唤醒
            while (number != 0){
                // 等待，不能生产
                condition.await();
            }
            // 2. 干活
            number++;
            System.out.println(Thread.currentThread().getName()+"\t 生产"+number);
            // 3. 通知唤醒
            condition.signalAll();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    public void consum() throws Exception{
        lock.lock();
        try{
            // 1. 判断，多线程一定要用 while ！防止虚假唤醒
            while (number == 0){
                // 等待，不能消费
                condition.await();
            }
            // 2. 干活
            number--;
            System.out.println(Thread.currentThread().getName()+"\t 消费"+number);
            // 3. 通知唤醒
            condition.signalAll();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

}

/**
 * @description: 多线程口诀
 * 1. 线程    操作    资源类
 * 2. 判断    干活    通知
 * 3. 防止虚假唤醒
 * @author: Kevin
 * @createDate: 2020/3/9
 * @version: 1.0
 */
public class ProdConsumer_TraditionDemo {
    public static void main(String[] args) {
        ShareDate shareDate = new ShareDate();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    shareDate.produce();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"AA").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    shareDate.consum();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"BB").start();
    }
}
