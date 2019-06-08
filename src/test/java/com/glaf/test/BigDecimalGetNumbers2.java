package com.glaf.test;

import java.math.BigDecimal;
import java.util.Scanner;

public class BigDecimalGetNumbers2 {

	public static double expectValue(double value) {
		long longPart = (long) value;
		BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
		BigDecimal bigDecimalLongPart = new BigDecimal(Double.toString(longPart));
		double dPoint = bigDecimal.subtract(bigDecimalLongPart).doubleValue();
		if (dPoint <= 0.25) {
			dPoint = 0;
		} else if (dPoint >= 0.8) {
			dPoint = 1;
		} else {
			dPoint = 0.5;
		}
		double val = longPart + dPoint;
		return val;
	}

	public static void main(String args[]) {
		System.out.println("-----------请输入数字------------\n");
		Scanner in = new Scanner(System.in);
		double val = expectValue(in.nextDouble());
		System.out.println("val:" + val);
		in.close();
	}
}
