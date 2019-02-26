package com.jkzz.num;

import java.math.BigDecimal;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-02-26 09:31
 **/
public class Arith {
    /**
     * 提供精确加法计算的add方法
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(double value1,double value2){
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确减法运算的sub方法
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(double value1,double value2){
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(double value1,double value2){
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     * @param value1 被除数
     * @param value2 除数
     * @param scale 精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double div(double value1,double value2,int scale) throws IllegalAccessException{
        //如果精确范围小于0，抛出异常信息
        if(scale<0){
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale).doubleValue();
    }


    public static void main(String[] args) {
     double a=1d;
     double b=0.9d;
     System.out.println(a-b);
        double sub = sub(a, b);
        System.out.println(sub);
        String s = MoneyUtil.moneySub("1", "0.9");
System.out.println(s);
double s1=Double.parseDouble(s);
System.out.println(s1);
        String format = String.format("%.2f", s1);
        System.out.println(format);
        double c=1.5d;
        double d=1.5d;
        String s2 = String.valueOf(c + d);
        System.out.println(s2);
        System.out.println(c+d);
    }
}
