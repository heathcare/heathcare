/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.heathcare.util;

import java.math.BigDecimal;

public class HeightUtils {

	public static String expectStringValue(double value) {
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

		double val = longPart;
		if (dPoint == 0.5) {
			val = longPart + dPoint;
			return String.valueOf(val);
		} else {
			val = longPart + dPoint;
			val = Math.round(val);
			return String.valueOf((int) val) + ".0";
		}
	}

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

		double val = longPart;
		if (dPoint == 0.5) {
			val = longPart + dPoint;
		} else {
			val = longPart + dPoint;
			val = Math.round(val);
		}

		return val;
	}

}
