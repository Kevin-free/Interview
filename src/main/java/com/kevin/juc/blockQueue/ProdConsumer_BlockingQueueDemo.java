package com.kevin.juc.blockQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource{
    private volatile boolean flag = true; // 默认开启，进行生产和消费
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue = null;
    // 构造注入，传入接口
    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProd() throws Exception{
        String data = null;
        boolean ret;
        while (flag){
            data = atomicInteger.incrementAndGet()+"";
            ret = blockingQueue.offer(data,2L,TimeUnit.SECONDS);
            if (ret){
                System.out.println(Thread.currentThread().getName()+"\t 队列生产"+data+"成功");
            }else{
                System.out.println(Thread.currentThread().getName()+"\t 队列生产"+data+"失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"\tboss 叫停，表示flag=false，生产动作结束");
    }

    public void myConsume() throws Exception{
        String res;
        while(flag){
            res = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if (res == null || res.equals("")){
                flag = false;
                System.out.println(Thread.currentThread().getName()+"\t 超过2秒没有取到，消费退出");
                System.out.println();
                System.out.println();
                return;
            }
            System.out.println(Thread.currentThread().getName()+"\t 队列消费"+ res +"成功");
        }
    }

    public void stop(){
        this.flag = false;
    }

}

/**
 * @description: 阻塞队列版生产者消费者模式
 * volatile/CAS/AtomicInteger/BlockingQueue
 * @author: Kevin
 * @createDate: 2020/3/9
 * @version: 1.0
 */
public class ProdConsumer_BlockingQueueDemo {
    public static void main(String[] args) {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t 生产线程启动");
            try {
                myResource.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"Pord").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t 消费线程启动");
            System.out.println();
            System.out.println();
            try {
                myResource.myConsume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"Consume").start();

        // 暂停一会线程
        try{ TimeUnit.SECONDS.sleep(5); } catch(InterruptedException e){ e.printStackTrace();}

        System.out.println();
        System.out.println();

        System.out.println("5秒钟到，boss main程序叫停，活动结束");
        myResource.stop();
    }
}
