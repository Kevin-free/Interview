package com.kevin.juc.container;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @description: 容器不安全问题
 * @author: Kevin
 * @createDate: 2020/2/29
 * @version: 1.0
 */
public class ContainerNotSafeDemo {
    /**
      * 笔记
      * 写时复制 copyOnWrite 容器即写时复制的容器 往容器添加元素的时候,不直接往当前容器object[]添加,而是先将当前容器object[]进行
      * copy 复制出一个新的object[] newElements 然后向新容器object[] newElements 里面添加元素 添加元素后,
      * 再将原容器的引用指向新的容器 setArray(newElements);
      * 这样的好处是可以对copyOnWrite容器进行并发的读,而不需要加锁 因为当前容器不会添加任何容器.所以copyOnwrite容器也是一种
      * 读写分离的思想,读和写不同的容器.
      *          public boolean add(E e) {
      *         final ReentrantLock lock = this.lock;
      *         lock.lock();
      *         try {
      *             Object[] elements = getArray();
      *             int len = elements.length;
      *             Object[] newElements = Arrays.copyOf(elements, len + 1);
      *             newElements[len] = e;
      *             setArray(newElements);
      *             return true;
      *         } finally {
      *             lock.unlock();
      *         }
      *     }
      */
    public static void main(String[] args) {
//        listNotSafe();
//        setNotSafe();
        mapNotSafe();
    }

    /**
     * 1. ArrayList 线程不安全，多线程可能出现
     *     java.util.ConcurrentModificationException 并发修改异常
     * 2. 导致原因：并发争抢修改导致（一个人正在写入，另一个同学来抢夺，导致数据不一致，并发修改异常）
     * 3. 解决方案：
     *    a. new Vector<>();  //安全但效率慢
     *    b. Collections.synchronizedList(new ArrayList<>());  //使用辅助类
     *    c. new CopyOnWriteArrayList<>();  //写时复制，读写分离。 最佳！
     */
    public static void listNotSafe(){
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }

    /**
     * 同理可用 CopyOnWriteArraySet 解决 Set 不安全问题
     *
     */
    public static void setNotSafe(){
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 1; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }

        // 注意！ HashSet 底层是基于 HashMap 实现，但是添加方法只加 key, value 为 Object 常量
        // public boolean add(E e) {
        //        return map.put(e, PRESENT)==null;
        //    }
        // Dummy value to associate with an Object in the backing Map
        // private static final Object PRESENT = new Object();
        new HashSet<>().add("a");
    }

    /**
     * 使用 ConcurrentHashMap 解决 Map 不安全问题
     */
    public static void mapNotSafe(){
        Map<String,String> map = new ConcurrentHashMap<>();
        for (int i = 1; i < 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0, 8));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }

}
