package com.kevin.javase;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/31
 * @version: 1.0
 */
public class Outter {
    public static void main(String[] args) {
        Outter out = null;
//        out.fun(); //NullPointerException
        out.sfun();
    }

    public void fun(){
        System.out.println("fun");
    }

    public static void sfun(){
        System.out.println("sfun");
    }

    Inner inner = new Inner();

    private class Inner{

    }
}
