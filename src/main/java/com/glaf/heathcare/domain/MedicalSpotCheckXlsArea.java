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

public class MedicalSpotCheckXlsArea {

	/**
	 * 开始行号
	 */
	protected int startRow;

	/**
	 * 结束行号
	 */
	protected int endRow;

	/**
	 * 姓名所在列
	 */
	protected int nameColIndex = -1;

	/**
	 * 性别所在列
	 */
	protected int sexColIndex;

	/**
	 * 民族所在列
	 */
	protected int nationColIndex = -1;

	/**
	 * 身高所在列
	 */
	protected int heightColIndex;

	/**
	 * 体重所在列
	 */
	protected int weightColIndex;

	/**
	 * 左视力
	 */
	protected int eyesightLeftColIndex = -1;

	/**
	 * 右视力
	 */
	protected int eyesightRightColIndex = -1;

	/**
	 * 血色素所在列
	 */
	protected int hemoglobinColIndex = -1;

	/**
	 * 市所在列
	 */
	protected int cityColIndex = -1;

	/**
	 * 区/县所在列
	 */
	protected int areaColIndex = -1;

	/**
	 * 机构名称所在列
	 */
	protected int organizationColIndex = -1;

	/**
	 * 班级所在列
	 */
	protected int gradeColIndex = -1;

	/**
	 * 年龄所在列
	 */
	protected int ageOfTheMoonColIndex;

	/**
	 * 体检日期所在列
	 */
	protected int checkDateColIndex = -1;

	public MedicalSpotCheckXlsArea() {

	}

	public int getAgeOfTheMoonColIndex() {
		return ageOfTheMoonColIndex;
	}

	public int getAreaColIndex() {
		return areaColIndex;
	}

	public int getCheckDateColIndex() {
		return checkDateColIndex;
	}

	public int getCityColIndex() {
		return cityColIndex;
	}

	public int getEndRow() {
		return endRow;
	}

	public int getEyesightLeftColIndex() {
		return eyesightLeftColIndex;
	}

	public int getEyesightRightColIndex() {
		return eyesightRightColIndex;
	}

	public int getGradeColIndex() {
		return gradeColIndex;
	}

	public int getHeightColIndex() {
		return heightColIndex;
	}

	public int getHemoglobinColIndex() {
		return hemoglobinColIndex;
	}

	public int getNameColIndex() {
		return nameColIndex;
	}

	public int getNationColIndex() {
		return nationColIndex;
	}

	public int getOrganizationColIndex() {
		return organizationColIndex;
	}

	public int getSexColIndex() {
		return sexColIndex;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getWeightColIndex() {
		return weightColIndex;
	}

	public void setAgeOfTheMoonColIndex(int ageOfTheMoonColIndex) {
		this.ageOfTheMoonColIndex = ageOfTheMoonColIndex;
	}

	public void setAreaColIndex(int areaColIndex) {
		this.areaColIndex = areaColIndex;
	}

	public void setCheckDateColIndex(int checkDateColIndex) {
		this.checkDateColIndex = checkDateColIndex;
	}

	public void setCityColIndex(int cityColIndex) {
		this.cityColIndex = cityColIndex;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public void setEyesightLeftColIndex(int eyesightLeftColIndex) {
		this.eyesightLeftColIndex = eyesightLeftColIndex;
	}

	public void setEyesightRightColIndex(int eyesightRightColIndex) {
		this.eyesightRightColIndex = eyesightRightColIndex;
	}

	public void setGradeColIndex(int gradeColIndex) {
		this.gradeColIndex = gradeColIndex;
	}

	public void setHeightColIndex(int heightColIndex) {
		this.heightColIndex = heightColIndex;
	}

	public void setHemoglobinColIndex(int hemoglobinColIndex) {
		this.hemoglobinColIndex = hemoglobinColIndex;
	}

	public void setNameColIndex(int nameColIndex) {
		this.nameColIndex = nameColIndex;
	}

	public void setNationColIndex(int nationColIndex) {
		this.nationColIndex = nationColIndex;
	}

	public void setOrganizationColIndex(int organizationColIndex) {
		this.organizationColIndex = organizationColIndex;
	}

	public void setSexColIndex(int sexColIndex) {
		this.sexColIndex = sexColIndex;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public void setWeightColIndex(int weightColIndex) {
		this.weightColIndex = weightColIndex;
	}

}
