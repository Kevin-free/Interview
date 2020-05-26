package com.kevin.javase;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/4/10
 * @version: 1.0
 */
public class TestStringBuilder {

    public static void main(String[] args) {
        StringBuilder a = new StringBuilder("A");
        StringBuilder b = new StringBuilder("B");
        convert(a,b);
        System.out.println(a+","+b);  // AB,B
    }

    public static void convert(StringBuilder x, StringBuilder y){
        x.append(y);
        y = x;
    }
}
