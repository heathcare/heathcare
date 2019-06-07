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

public class MedicalSpotCheckAggregationModel {

	protected String key;

	/**
	 * 月龄
	 */
	protected int ageOfTheMoon;

	/**
	 * 月龄
	 */
	protected String ageOfTheMoonString;

	/**
	 * 小计1
	 */
	protected int subTotal1;

	protected String subTotalString1;

	/**
	 * 小计2
	 */
	protected int subTotal2;

	protected String subTotalString2;

	/**
	 * 小计3
	 */
	protected int subTotal3;

	protected String subTotalString3;

	/**
	 * 维度模型1
	 */
	protected MedicalSpotCheckTotalModel model1 = new MedicalSpotCheckTotalModel();

	/**
	 * 维度模型2
	 */
	protected MedicalSpotCheckTotalModel model2 = new MedicalSpotCheckTotalModel();

	/**
	 * 维度模型3
	 */
	protected MedicalSpotCheckTotalModel model3 = new MedicalSpotCheckTotalModel();

	public MedicalSpotCheckAggregationModel() {

	}

	public int getAgeOfTheMoon() {
		return ageOfTheMoon;
	}

	public String getAgeOfTheMoonString() {
		if (ageOfTheMoon > 0) {
			int year = ageOfTheMoon / 12;
			int month = ageOfTheMoon % 12;
			ageOfTheMoonString = year + "岁" + month + "月";
			return ageOfTheMoonString;
		}
		return "";
	}

	public String getKey() {
		return key;
	}

	public MedicalSpotCheckTotalModel getModel1() {
		return model1;
	}

	public MedicalSpotCheckTotalModel getModel2() {
		return model2;
	}

	public MedicalSpotCheckTotalModel getModel3() {
		return model3;
	}

	public int getSubTotal1() {
		return subTotal1;
	}

	public int getSubTotal2() {
		return subTotal2;
	}

	public int getSubTotal3() {
		return subTotal3;
	}

	public String getSubTotalString1() {
		if (subTotal1 > 0) {
			return String.valueOf(subTotal1);
		}
		return "";
	}

	public String getSubTotalString2() {
		if (subTotal2 > 0) {
			return String.valueOf(subTotal2);
		}
		return subTotalString2;
	}

	public String getSubTotalString3() {
		if (subTotal3 > 0) {
			return String.valueOf(subTotal3);
		}
		return subTotalString3;
	}

	public void setAgeOfTheMoon(int ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setAgeOfTheMoonString(String ageOfTheMoonString) {
		this.ageOfTheMoonString = ageOfTheMoonString;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setModel1(MedicalSpotCheckTotalModel model1) {
		this.model1 = model1;
	}

	public void setModel2(MedicalSpotCheckTotalModel model2) {
		this.model2 = model2;
	}

	public void setModel3(MedicalSpotCheckTotalModel model3) {
		this.model3 = model3;
	}

	public void setSubTotal1(int subTotal1) {
		this.subTotal1 = subTotal1;
	}

	public void setSubTotal2(int subTotal2) {
		this.subTotal2 = subTotal2;
	}

	public void setSubTotal3(int subTotal3) {
		this.subTotal3 = subTotal3;
	}

}
