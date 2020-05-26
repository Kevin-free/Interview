package com.kevin.juc.volatileTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1. 验证volatile的可见性
 *  1.1 假如 int number = 0；number变量之前根本没有添加volatile关键字修饰
 *  1.2 添加了volatile，可以解决可见性问题
 *
 * 2. 验证volatile不保证原子性
 *  2.1 原子性：不可分割，完整性。
 *      也即某个线程正在做某个业务，不可被加塞或者被分割。需要整体完整。
 *      要么同时成功，要么同时失败。
 *  2.2 why：线程加塞导致数据覆盖
 *  2.3 如何解决：
 *     2.3.1 加synchronized (没必要，synchronized重量级)
 *     2.3.2 使用juc下AtomicInteger
 *
 * 3. volatile禁止指令重排
 *
 * @author: Kevin
 * @createDate: 2020/2/13
 * @version: 1.0
 */
public class VolatileDemo {

    public static void main(String[] args) {
//        visibilityByVolatile();//验证volatile的可见性
        atomicByVolatile();//验证volatile不保证原子性
    }

    /**
     * volatile可以保证可见性，及时通知其他线程，主物理内存的值已经被修改
     */
    public static void visibilityByVolatile() {
        MyData myData = new MyData();

        //第一个线程
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                //线程暂停3s
                TimeUnit.SECONDS.sleep(3);
                myData.addToSixty();
                System.out.println(Thread.currentThread().getName() + "\t update value:" + myData.num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "thread1").start();
        //第二个线程是main线程
        while (myData.num == 0) {
            //如果myData的num一直为零，main线程一直在这里循环
        }
        System.out.println(Thread.currentThread().getName() + "\t mission is over, num value is " + myData.num);
    }

    /**
     * volatile不保证原子性
     * 以及使用Atomic保证原子性
     */
    public static void atomicByVolatile(){
        MyData myData = new MyData();
        for(int i = 1; i <= 20; i++){
            new Thread(() ->{
                for(int j = 1; j <= 1000; j++){
                    myData.addSelf();
                    myData.atomicAddSelf();
                }
            },"Thread "+i).start();
        }
        //等待上面的线程都计算完成后，再用main线程取得最终结果值
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (Thread.activeCount()>2){
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+"\t finally num value is "+myData.num);
        System.out.println(Thread.currentThread().getName()+"\t finally atomicnum value is "+myData.atomicInteger);
    }
}

class MyData {
//    int num = 0;
    volatile int num = 0;

    public synchronized void addToSixty() {
        this.num = 60;
    }

    public void addSelf(){
        num++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();
    public void atomicAddSelf(){
        atomicInteger.getAndIncrement();
    }
}

/**
 * 指令重排问题
 */
class ResortSeq{
    int a = 0;
    boolean flag = false;

    public void method01(){
        a = 1;
        flag = true;
        // 若发生重排如下情况
//        flag = true;  // 其他线程立即执行method02，if(true){  a = 0 + 5; }
//        a = 1;
    }
    public void method02(){
        if(flag){
            a = a + 5;
            // 多线程发生指令重排可能出现以下情况，造成不确定性
            // a = 1 + 5
            // a = 0 + 5
            System.out.println("\"a\" value is "+a);
        }
    }
}
