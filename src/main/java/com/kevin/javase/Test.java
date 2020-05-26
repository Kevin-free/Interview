package com.kevin.javase;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/6
 * @version: 1.0
 */
public class Test {
    public static void main(String[] args) {
        System.out.println((0 % 9));//0
        System.out.println((9 % 9));//0
        System.out.println((7 % 9));//7
        System.out.println((12 % 9));//3
        System.out.println((-12 % 9));//-3
        System.out.println((12 % -9));//3
        System.out.println((-12 % -9));//-3

        List<Object> objectList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
//        objectList = stringList;  error
//        stringList = objectList;  error
    }
}
