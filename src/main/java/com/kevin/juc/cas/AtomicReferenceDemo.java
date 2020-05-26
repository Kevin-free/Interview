package com.kevin.juc.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/2/26
 * @version: 1.0
 */

public class AtomicReferenceDemo {

    public static void main(String[] args) {
        User u1 = new User("u1", 10);
        User u2 = new User("u2", 20);

        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(u1);

        System.out.println(atomicReference.compareAndSet(u1, u2)+"\t"+atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(u1, u2)+"\t"+atomicReference.get().toString());

    }

}

class User{
    String name;
    int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
