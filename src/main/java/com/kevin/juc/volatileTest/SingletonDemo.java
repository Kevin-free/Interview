package com.kevin.juc.volatileTest;

/**
 * @description: 单例模式
 * @author: Kevin
 * @createDate: 2020/2/20
 * @version: 1.0
 */
public class SingletonDemo {

    private static volatile SingletonDemo instance = null; // 加volatile 禁止指令重排！
    // 例子理解
    // 安排位置、坐座位、找人。正常情况
    // 安排位置、找人、坐座位。重排后，对象还没初始化完成

    private SingletonDemo(){
        System.out.println(Thread.currentThread().getName()+"\t 我是构造方法SingletonDemo()");
    }

//    public static synchronized SingletonDemo getInstance(){ 可以直接在方法前加synchronized，但是锁了整个方法，没必要
    public static SingletonDemo getInstance(){
        if (instance == null){
            synchronized (SingletonDemo.class){ // DCL(Double Check Lock 双端检锁机制)，但是由于存在指令重排，所以需要加volatile
                if (instance == null){
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {

        //单线程，构造方法只会被执行一次
//        System.out.println(getInstance() == getInstance());
//        System.out.println(getInstance() == getInstance());
//        System.out.println(getInstance() == getInstance());

        //并发多线程，构造方法会在一些情况下执行多次
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                SingletonDemo.getInstance();
            }, "Thread " + i).start();
        }
    }

}
