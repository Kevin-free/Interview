package com.kevin.javase;

import java.util.Set;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/30
 * @version: 1.0
 */
public abstract class AbstractMap<K,V> {

    int size;

    protected AbstractMap(){

    }

    public int size(){
        return size;
    }

    public abstract boolean isEmpty();

}
