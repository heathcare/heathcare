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

package com.glaf.heathcare.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.glaf.core.security.Authentication;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExcelUtils;
import com.glaf.core.util.StringTools;

import com.glaf.heathcare.domain.MedicalExamination;

public class HaizgToTBNMedicalExaminationConverter {
	protected final static Log logger = LogFactory.getLog(HaizgToTBNMedicalExaminationConverter.class);

	public List<MedicalExamination> getMedicalExaminations(java.io.InputStream inputStream) {
		logger.debug("----------------HaizgToTBNMedicalExaminationConverter------------------");
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(inputStream);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
				}
			}
		}
		HSSFSheet sheet = wb.getSheetAt(0);
		List<MedicalExamination> exams = new ArrayList<MedicalExamination>();
		int rows = sheet.getLastRowNum();
		logger.debug("row num:" + rows);
		for (int rowIndex = 2; rowIndex <= rows; rowIndex++) {// 第三行开始取数
			HSSFRow row = sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			MedicalExamination exam = new MedicalExamination();

			if (Authentication.getAuthenticatedActorId() != null) {
				exam.setCreateBy(Authentication.getAuthenticatedActorId());
			}
			String cellValue = null;
			String year = null;
			int cells = row.getLastCellNum();
			for (int colIndex = 0; colIndex < cells; colIndex++) {
				HSSFCell cell = row.getCell(colIndex);
				if (cell != null) {
					cellValue = ExcelUtils.getStringOrDateValue(cell, 0);
					if (cellValue == null) {
						cellValue = "";
					}
					cellValue = cellValue.trim();
					switch (colIndex) {
					case 0:// 姓名
						exam.setName(cellValue);
						break;
					case 1:// 性别
						if (StringUtils.contains(cellValue, "男")) {
							exam.setSex("男");
						}
						if (StringUtils.contains(cellValue, "女")) {
							exam.setSex("女");
						}
						break;
					case 4:// 学年
						year = cellValue;
						break;
					case 3:// 班级
						cellValue = StringTools.replace(cellValue, "小", "");
						cellValue = StringTools.replace(cellValue, "中", "");
						cellValue = StringTools.replace(cellValue, "大", "");
						exam.setGradeName(year + cellValue);
						break;
					case 5:// 体检日期
						cellValue = StringTools.replace(cellValue, ".", "-");
						cellValue = StringTools.replace(cellValue, "/", "-");
						try {
							exam.setCheckDate(DateUtils.toDate(cellValue));
						} catch (Throwable ex) {

						}
						break;
					case 6:// 身高
						cellValue = ExcelUtils.getString(cell, 1);
						if (StringUtils.isNotEmpty(cellValue)) {
							exam.setHeight(Double.parseDouble(cellValue));
						}
						break;
					case 8:// 体重
						cellValue = ExcelUtils.getString(cell, 1);
						if (StringUtils.isNotEmpty(cellValue)) {
							exam.setWeight(Double.parseDouble(cellValue));
						}
						break;
					case 10:// 左视力
						cellValue = ExcelUtils.getString(cell, 1);
						if (StringUtils.isNotEmpty(cellValue)) {
							exam.setEyesightLeft(Double.parseDouble(cellValue));
						}
						break;
					case 12:// 右视力
						cellValue = ExcelUtils.getString(cell, 1);
						if (StringUtils.isNotEmpty(cellValue)) {
							exam.setEyesightRight(Double.parseDouble(cellValue));
						}
						break;
					case 14:// 龋齿数
						cellValue = ExcelUtils.getString(cell, 0);
						if (StringUtils.isNotEmpty(cellValue)) {
							exam.setSaprodontia(Integer.parseInt(cellValue));
						}
						break;
					case 15:// 血红蛋白(g/L)
						cellValue = ExcelUtils.getString(cell, 1);
						if (StringUtils.isNotEmpty(cellValue)) {
							exam.setHemoglobinValue(Double.parseDouble(cellValue));
						}
						break;
					default:
						break;
					}
				}
			}
			if (StringUtils.isNotEmpty(exam.getName()) && exam.getCheckDate() != null) {
				exams.add(exam);
			}
		}
		return exams;
	}

}
