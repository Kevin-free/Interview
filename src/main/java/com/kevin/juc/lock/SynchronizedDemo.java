package com.kevin.juc.lock;

/**
 * @description: synchronized 是典型的可重入锁
 * 可重入锁（也叫递归锁）
 * 指的是同一线程外层函数获得锁后，内层递归函数仍能获取该锁的代码，
 * 在同一个线程在外部方法获取锁的时候，在进入内层方法会自动解锁，
 * 也就是说，==线程可以进入任何一个它已经拥有的锁所同步着的代码块==
 * @author: Kevin
 * @createDate: 2020/3/2
 * @version: 1.0
 */
class Phone{
    public synchronized void sendSms() throws Exception{
        System.out.println(Thread.currentThread().getName()+"\t sendSmS");
        sendEmail();
    }
    public synchronized void sendEmail() throws Exception{
        System.out.println(Thread.currentThread().getName()+"\t ###sendEmail");
    }
}
public class SynchronizedDemo {
    /**
     * t1	 sendSmS
     * t1	 ###sendEmail
     * t2	 sendSmS
     * t2	 ###sendEmail
     */
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() -> {
            try {
                phone.sendSms();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"t1").start();
        new Thread(() -> {
            try {
                phone.sendSms();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"t2").start();
    }
}
