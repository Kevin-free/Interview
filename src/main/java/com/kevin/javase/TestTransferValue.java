package com.kevin.javase;

/**
 * @description: Java 只有 传值！（所谓传'引用'实际是内存地址）
 * @author: Kevin
 * @createDate: 2020/3/2
 * @version: 1.0
 */
public class TestTransferValue {
    public void changeValue1(int age){
        age = 30;
    }
    public void changeValue2(Person person){
        person.setName("xxx");
    }
    public void changeValue3(String str){
        str = "xxx";
    }

    public static void main(String[] args) {
        TestTransferValue test = new TestTransferValue();

        int age = 20;
        test.changeValue1(age);
        System.out.println(age);  // 20

        Person person = new Person("kevin");
        test.changeValue2(person);
        System.out.println(person.getName()); // xxx

        String str = "aaa";
        test.changeValue3(str);
        System.out.println(str); // aaa
        String str2 = "aaa";
        System.out.println(str == str2); // true
        System.out.println(str.equals(str2)); // true

        String string = new String("bbb");
        test.changeValue3(string);
        System.out.println(string); // bbb
        String string2 = new String("bbb");
        System.out.println(string == string2); // false
        System.out.println(string.equals(string2)); // true
    }
}
