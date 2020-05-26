package com.kevin.jvm;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/4/6
 * @version: 1.0
 *
 * 在 java 中，可作为 GC Roots 的对象有：
 * 1. 虚拟机栈（栈帧中的本地变量表）中引用的对象
 * 2. 方法区中类静态属性引用的对象
 * 3. 方法区中常量引用的对象
 * 4. 本地方法栈中 JNI（即一般说是Native方法）中引用的对象
 */
public class GCRootDemo {
    private byte[] byteArray = new byte[100 * 1024];

//    private static GCRootDemo2 t2; //方法区中类静态属性引用的对象
//    private static final GCRootDemo3 t3 = new GCRootDemo3(); //方法区中常量引用的对象

    public static void m1(){ // 虚拟机栈
        GCRootDemo t1 = new GCRootDemo(); //虚拟机栈（栈帧中的本地变量表）中引用的对象
        System.gc();
        System.out.println("first GC done!");
    }

    public static void main(String[] args) {
        m1();
    }
}
