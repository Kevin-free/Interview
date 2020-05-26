package com.kevin.jvm;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/4/6
 * @version: 1.0
 */
public class HelloGC {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("HelloGC");

        long totalMemory = Runtime.getRuntime().totalMemory(); // Java虚拟机栈中内存总量
        long maxMemory = Runtime.getRuntime().maxMemory(); // Java虚拟机栈中试图使用的最大内存值

        System.out.println("totalMemory(-Xms) = " + totalMemory + "（字节）、" + (totalMemory / (double) 1024 / 1024) + "MB"); // Memory 1/64
        System.out.println("maxMemory(-Xmx) = " + maxMemory + "（字节）、" + (maxMemory / (double) 1024 / 1024) + "MB");  // Memory 1/4

        Thread.sleep(Integer.MAX_VALUE);
    }
}
