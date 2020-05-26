package com.kevin.juc.lock;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// 手写缓存资源类
class MyCache{
    // volatile 保证可见性
    private volatile Map<String,Object> map = new HashMap<>();
    // 读写锁：读共享，写独占
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    // 写
    public void put(String k,Object v){
//        rwLock.writeLock().lock();
        try{
            System.out.println(Thread.currentThread().getName() + "\t正在写入：" + k);
            // 模拟网络延时，暂停一会线程
            try{ TimeUnit.MILLISECONDS.sleep(300); } catch(InterruptedException e){ e.printStackTrace();}
            map.put(k, v);
            System.out.println(Thread.currentThread().getName() + "\t写入完成：");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
//            rwLock.writeLock().unlock();
        }
    }

    // 读
    public void get(String k){
//        rwLock.readLock().lock();
        try{
            System.out.println(Thread.currentThread().getName() + "正在读取：");
            // 暂停一会线程
            try{ TimeUnit.MILLISECONDS.sleep(300); } catch(InterruptedException e){ e.printStackTrace();}
            Object v = map.get(k);
            System.out.println(Thread.currentThread().getName() + "读取完成：" + v);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
//            rwLock.readLock().unlock();
        }
    }

    // 清缓存
    public void clear(){
        map.clear();
    }
}

/**
 * @description: 测试读写锁
 * 多个线程同时操作一个资源类时没有任何问题 所以为了满足并发量
 * 读取共享资源应该可以同时进行
 * 但是
 * 如果有一个线程想去写共享资源 就不应该有其他线程对其资源进行读或写
 * <P></P>
 * 总结：
 * 读-读：能共享
 * 读-学：不能共享
 * 写-写：不能共享
 * 写操作，原子+独占！整个过程必须是一个完整的统一整体，中间不允许被分割，被打断。
 * @author: Kevin
 * @createDate: 2020/3/3
 * @version: 1.0
 */
public class ReadWirteLockDemo {
    public static void main(String[] args){
        MyCache myCache = new MyCache();
        for(int i = 1; i <= 5; i++){
            int temp = i;
            new Thread(() -> {
                myCache.put(temp+"",temp+"");
            },String.valueOf(i)).start();
        }

        for(int i = 1; i <= 5; i++){
            int temp = i;
            new Thread(() -> {
                myCache.get(temp+"");
            },String.valueOf(i)).start();
        }
    }
}
