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
package com.glaf.heathcare.domain;

public class ItemValue implements java.io.Serializable, java.lang.Comparable<ItemValue> {

	private static final long serialVersionUID = 1L;

	protected String name;

	protected double value;

	public ItemValue() {

	}

	@Override
	public int compareTo(ItemValue o) {
		if (o == null) {
			return -1;
		}

		ItemValue field = o;

		double val = this.value - field.getValue();

		int ret = 0;

		if (val > 0) {
			ret = 1;
		} else if (val < 0) {
			ret = -1;
		}
		return ret;
	}

	public String getName() {
		return name;
	}

	public double getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
