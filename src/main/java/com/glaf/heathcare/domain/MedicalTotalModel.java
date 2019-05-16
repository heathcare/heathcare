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

import java.io.Serializable;

public class MedicalTotalModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 年龄组(岁、月)
	 */
	protected int ageOfTheMoon;

	/**
	 * 年龄组(岁、月)
	 */
	protected String ageOfTheMoonString;

	/**
	 * 小于M-3SD人数
	 */
	protected double negative3SD;

	/**
	 * 小于M-3SD人数
	 */
	protected int negative3SDNum;

	/**
	 * M-3SD~M-2SD人数
	 */
	protected double negative2SD;

	/**
	 * M-3SD~M-2SD人数
	 */
	protected int negative2SDNum;

	/**
	 * M-2SD~M-1SD人数
	 */
	protected double negative1SD;

	/**
	 * M-2SD~M-1SD人数
	 */
	protected int negative1SDNum;

	/**
	 * M±1SD人数
	 */
	protected double nomal;

	/**
	 * M±1SD人数
	 */
	protected int nomalNum;

	/**
	 * M+1SD~M+2SD人数
	 */
	protected double positive1SD;

	/**
	 * M+1SD~M+2SD人数
	 */
	protected int positive1SDNum;

	/**
	 * M+2SD~M+3SD人数
	 */
	protected double positive2SD;

	/**
	 * M+2SD~M+3SD人数
	 */
	protected int positive2SDNum;

	/**
	 * 大于M+3SD人数
	 */
	protected double positive3SD;

	/**
	 * 大于M+3SD人数
	 */
	protected int positive3SDNum;

	public MedicalTotalModel() {

	}

	public int getAgeOfTheMoon() {
		return ageOfTheMoon;
	}

	public String getAgeOfTheMoonString() {
		return ageOfTheMoonString;
	}

	public double getNegative1SD() {
		return negative1SD;
	}

	public int getNegative1SDNum() {
		return negative1SDNum;
	}

	public double getNegative2SD() {
		return negative2SD;
	}

	public int getNegative2SDNum() {
		return negative2SDNum;
	}

	public double getNegative3SD() {
		return negative3SD;
	}

	public int getNegative3SDNum() {
		return negative3SDNum;
	}

	public double getNomal() {
		return nomal;
	}

	public int getNomalNum() {
		return nomalNum;
	}

	public double getPositive1SD() {
		return positive1SD;
	}

	public int getPositive1SDNum() {
		return positive1SDNum;
	}

	public double getPositive2SD() {
		return positive2SD;
	}

	public int getPositive2SDNum() {
		return positive2SDNum;
	}

	public double getPositive3SD() {
		return positive3SD;
	}

	public int getPositive3SDNum() {
		return positive3SDNum;
	}

	public void setAgeOfTheMoon(int ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setAgeOfTheMoonString(String ageOfTheMoonString) {
		this.ageOfTheMoonString = ageOfTheMoonString;
	}

	public void setNegative1SD(double negative1sd) {
		negative1SD = negative1sd;
	}

	public void setNegative1SDNum(int negative1sdNum) {
		negative1SDNum = negative1sdNum;
	}

	public void setNegative2SD(double negative2sd) {
		negative2SD = negative2sd;
	}

	public void setNegative2SDNum(int negative2sdNum) {
		negative2SDNum = negative2sdNum;
	}

	public void setNegative3SD(double negative3sd) {
		negative3SD = negative3sd;
	}

	public void setNegative3SDNum(int negative3sdNum) {
		negative3SDNum = negative3sdNum;
	}

	public void setNomal(double nomal) {
		this.nomal = nomal;
	}

	public void setNomalNum(int nomalNum) {
		this.nomalNum = nomalNum;
	}

	public void setPositive1SD(double positive1sd) {
		positive1SD = positive1sd;
	}

	public void setPositive1SDNum(int positive1sdNum) {
		positive1SDNum = positive1sdNum;
	}

	public void setPositive2SD(double positive2sd) {
		positive2SD = positive2sd;
	}

	public void setPositive2SDNum(int positive2sdNum) {
		positive2SDNum = positive2sdNum;
	}

	public void setPositive3SD(double positive3sd) {
		positive3SD = positive3sd;
	}

	public void setPositive3SDNum(int positive3sdNum) {
		positive3SDNum = positive3sdNum;
	}

}
