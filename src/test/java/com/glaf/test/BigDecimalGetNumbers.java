package com.glaf.test;

import java.math.BigDecimal;
import java.util.Scanner;

public class BigDecimalGetNumbers {

	public static void main(String args[]) {
		System.out.println("-----------请输入数字------------");
		Scanner in = new Scanner(System.in);
		double dInput = in.nextDouble();
		long longPart = (long) dInput;
		BigDecimal bigDecimal = new BigDecimal(Double.toString(dInput));
		BigDecimal bigDecimalLongPart = new BigDecimal(Double.toString(longPart));
		double dPoint = bigDecimal.subtract(bigDecimalLongPart).doubleValue();
		System.out.println("整数部分为:" + longPart + "\n" + "小数部分为: " + dPoint);
		in.close();
	}

}