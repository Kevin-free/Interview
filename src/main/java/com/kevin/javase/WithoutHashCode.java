package com.kevin.javase;

import java.util.HashMap;
import java.util.Objects;

/**
 * @description:
 * @author: Kevin
 * @createDate: 2020/3/29
 * @version: 1.0
 */
class Key {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public Key(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return Objects.equals(id, key.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

public class WithoutHashCode {
    public static void main(String[] args) {
        Key k1 = new Key(1);
        Key k2 = new Key(1);
        HashMap<Key,String> map = new HashMap<>();
        map.put(k1, "id is 1");
        System.out.println(map.get(k2));
    }
}
