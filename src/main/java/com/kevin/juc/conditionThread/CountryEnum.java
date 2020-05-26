package com.kevin.juc.conditionThread;

import lombok.Getter;

/**
 * 枚举看作数据库
 * mysql dbName = CountryEnum
 * table ONE, TWO ...
 * ID name age ...
 * 1  一娃 100 ...
 */
public enum CountryEnum {
    ONE(1,"齐国"),TWO(2,"楚国"),THREE(3,"燕国"),FOUR(4,"韩国"),FIVE(5,"赵国"),SIX(6,"魏国");

    @Getter
    private Integer retCode;
    @Getter
    private String retMsg;

    CountryEnum(Integer retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    // 通过下标获取枚举元素
    public static CountryEnum forEach_countryEnum(int index){
        CountryEnum[] countryEnums = CountryEnum.values();
        for(CountryEnum item:countryEnums){
            if (index == item.getRetCode()){
                return item;
            }
        }
        return null;
    }
}
