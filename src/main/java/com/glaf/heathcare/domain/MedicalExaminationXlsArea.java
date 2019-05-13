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

public class MedicalExaminationXlsArea {

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
	protected int nameColIndex;

	/**
	 * 班级所在列
	 */
	protected int gradeColIndex;

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
	protected int eyesightLeftColIndex;

	/**
	 * 右视力
	 */
	protected int eyesightRightColIndex;

	/**
	 * 血色素所在列
	 */
	protected int hemoglobinColIndex;

	/**
	 * 体检日期所在列
	 */
	protected int checkDateColIndex;

	public MedicalExaminationXlsArea() {

	}

	public int getCheckDateColIndex() {
		return checkDateColIndex;
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

	public int getStartRow() {
		return startRow;
	}

	public int getWeightColIndex() {
		return weightColIndex;
	}

	public void setCheckDateColIndex(int checkDateColIndex) {
		this.checkDateColIndex = checkDateColIndex;
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

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public void setWeightColIndex(int weightColIndex) {
		this.weightColIndex = weightColIndex;
	}

}
