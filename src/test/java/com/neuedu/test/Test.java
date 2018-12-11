package com.neuedu.test;

import com.neuedu.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {
      //  System.out.println(0.5+0.1);
       // System.out.println(1.0-0.42);

        //调用字符串类型的构造方法才能得到精确的结果
        /*BigDecimal bigDecimal=new BigDecimal("0.05");
        BigDecimal bigDecimal1=new BigDecimal("0.01");
        System.out.println(bigDecimal.add(bigDecimal1));*/
        System.out.println(BigDecimalUtils.add(0.05,0.01));
        System.out.println(BigDecimalUtils.div(1.0,0.42));
        System.out.println(BigDecimalUtils.mul(0.02,0.03));
        System.out.println(BigDecimalUtils.sub(1.0,0.05));
    }
}
