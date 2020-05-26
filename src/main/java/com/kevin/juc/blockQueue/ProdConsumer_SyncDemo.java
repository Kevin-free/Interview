package com.kevin.juc.blockQueue;

import java.util.LinkedList;
import java.util.Queue;

class PubData{
    private Queue<Integer> queue;
    private int            maxSize;

    PubData(Queue<Integer> queue, int maxSize) {
        this.queue = queue;
        this.maxSize = maxSize;
    }

    public void prod() throws InterruptedException {
        synchronized (queue){
            while (true){
                while (queue.size() == maxSize){
                    queue.wait();
                }
                System.out.println("生产1个");
                queue.add(1);
                //如果多个消费者 可改为notifyAll
                queue.notify();
            }
        }
    }

    public void consume() throws InterruptedException {
        synchronized (queue){
            while (true){
                while (queue.isEmpty()){
                    queue.wait();
                }
                System.out.println("消费1个");
                queue.remove();
                //如果多个消费者 可改为notifyAll
                queue.notify();
            }
        }
    }
}
/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/9
 * @version: 1.0
 */
public class ProdConsumer_SyncDemo {
    public static void main(String[] args) {
        PubData pubData = new PubData(new LinkedList<>(),10);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t 生产线程启动");
            try {
                pubData.prod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AA").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t 消费线程启动");
            try {
                pubData.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BB").start();
    }
}
