package com.kevin.javase;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/2
 * @version: 1.0
 */

public class Person {
    private Integer id;
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}
