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

package com.glaf.heathcare.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExcelUtils;

import com.glaf.heathcare.domain.MedicalSpotCheck;
import com.glaf.heathcare.domain.MedicalSpotCheckXlsArea;
import com.glaf.heathcare.service.MedicalSpotCheckService;

public class MedicalSpotCheckExcelImporterV2 {
	protected final static Log logger = LogFactory.getLog(MedicalSpotCheckExcelImporterV2.class);

	public String importData(String tenantId, String userId, String type, String checkId,
			List<MedicalSpotCheckXlsArea> areas, java.io.InputStream inputStream) {
		logger.debug("----------------MedicalSpotCheckExcelImporterV2------------------");
		MedicalSpotCheckService medicalSpotCheckService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalSpotCheckService");
		StringBuilder buffer = new StringBuilder();
		Workbook wb = null;
		Row row = null;
		Cell cell = null;
		Cell sexCell = null;
		Cell birthdayCell = null;
		Cell checkDateCell = null;
		String cellValue = null;
		String sexValue = null;
		String birthdayValue = null;
		String checkDateValue = null;
		try {
			wb = WorkbookFactory.create(inputStream);

			List<MedicalSpotCheck> exams = new ArrayList<MedicalSpotCheck>();
			int ordinal = 0;
			for (MedicalSpotCheckXlsArea area : areas) {
				int sheetCnt = wb.getNumberOfSheets();
				for (int index = 0; index < sheetCnt; index++) {
					Sheet sheet = wb.getSheetAt(index);
					int endRow = sheet.getLastRowNum();
					if (area.getEndRow() < endRow) {
						endRow = area.getEndRow();
					}
					logger.debug("start row:" + area.getStartRow());
					logger.debug("end row:" + endRow);
					for (int rowIndex = area.getStartRow(); rowIndex <= endRow; rowIndex++) {
						row = sheet.getRow(rowIndex);
						if (row == null) {
							continue;
						}
						try {
							sexCell = row.getCell(area.getSexColIndex());
							birthdayCell = row.getCell(area.getBirthdayColIndex());
							checkDateCell = row.getCell(area.getCheckDateColIndex());
							if (birthdayCell != null && checkDateCell != null && sexCell != null) {
								birthdayValue = ExcelUtils.getCellValue(birthdayCell);
								checkDateValue = ExcelUtils.getCellValue(checkDateCell);
								sexValue = ExcelUtils.getCellValue(sexCell);
								if (sexValue != null && birthdayValue != null && checkDateValue != null) {

									MedicalSpotCheck exam = new MedicalSpotCheck();

									Date birthday = DateUtils.toDate(birthdayValue);
									Date checkDate = DateUtils.toDate(checkDateValue);
									if (birthday == null || checkDate == null
											|| birthday.getTime() > checkDate.getTime()) {
										buffer.append("<br><div style='color:red;'>")
												.append((rowIndex + 1) + "行记录数据有误，出生日期及体检日期不能为空！</div>");
										continue;
									}

									int days = DateUtils.getDaysBetween(birthday, checkDate);

									if (StringUtils.contains(sexValue, "男")) {
										exam.setSex("1");
									} else {
										exam.setSex("0");
									}
									exam.setType(type);

									int year = days / 365;
									int month = days % 365 / 30;
									exam.setYear(year);
									exam.setMonth(month);
									exam.setAgeOfTheMoon(year * 12 + month);
									exam.setAgeOfTheMoonString(year + "岁" + month + "月");

									if (area.getNameColIndex() != -1) {
										cell = row.getCell(area.getNameColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											exam.setName(cellValue);
										}
									}

									if (area.getNationColIndex() != -1) {
										cell = row.getCell(area.getNationColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											exam.setNation(cellValue);
										}
									}

									if (area.getOrganizationColIndex() != -1) {
										cell = row.getCell(area.getOrganizationColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											exam.setOrganization(cellValue);
										}
									}

									if (area.getOrganizationLevelColIndex() != -1) {
										cell = row.getCell(area.getOrganizationLevelColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											exam.setOrganizationLevel(cellValue);
										}
									}

									if (area.getOrganizationPropertyColIndex() != -1) {
										cell = row.getCell(area.getOrganizationPropertyColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											exam.setOrganizationProperty(cellValue);
										}
									}

									if (area.getOrganizationTerritoryColIndex() != -1) {
										cell = row.getCell(area.getOrganizationTerritoryColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											exam.setOrganizationTerritory(cellValue);
										}
									}

									if (area.getGradeColIndex() != -1) {
										cell = row.getCell(area.getGradeColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											exam.setGradeName(cellValue);
										}
									}

									if (area.getCityColIndex() != -1) {
										cell = row.getCell(area.getCityColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											exam.setCity(cellValue);
										}
									}

									if (area.getAreaColIndex() != -1) {
										cell = row.getCell(area.getAreaColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											exam.setArea(cellValue);
										}
									}

									cell = row.getCell(area.getHeightColIndex());
									if (cell != null) {
										cellValue = ExcelUtils.getValue(cell, 1);
										if (StringUtils.isNotEmpty(cellValue)) {
											exam.setHeight(Double.parseDouble(cellValue));
										}
									}

									cell = row.getCell(area.getWeightColIndex());
									if (cell != null) {
										cellValue = ExcelUtils.getValue(cell, 1);
										if (StringUtils.isNotEmpty(cellValue)) {
											exam.setWeight(Double.parseDouble(cellValue));
										}
									}

									if (area.getEyesightLeftColIndex() != -1) {
										cell = row.getCell(area.getEyesightLeftColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getValue(cell, 1);
											if (StringUtils.isNotEmpty(cellValue)) {
												exam.setEyesightLeft(Double.parseDouble(cellValue));
											}
										}
									}

									if (area.getEyesightRightColIndex() != -1) {
										cell = row.getCell(area.getEyesightRightColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getValue(cell, 1);
											if (StringUtils.isNotEmpty(cellValue)) {
												exam.setEyesightRight(Double.parseDouble(cellValue));
											}
										}
									}

									if (area.getHemoglobinColIndex() != -1) {
										cell = row.getCell(area.getHemoglobinColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getValue(cell, 1);
											if (StringUtils.isNotEmpty(cellValue)) {
												exam.setHemoglobin(Double.parseDouble(cellValue));
											}
										}
									}

									exam.setTenantId(tenantId);
									exam.setCheckId(checkId);
									exam.setCheckDate(checkDate);
									exam.setCreateBy(userId);
									exam.setOrdinal(++ordinal);
									exams.add(exam);
								}
							}
						} catch (Exception ex) {
							buffer.append("<br><div style='color:red;'>")
									.append((rowIndex + 1) + "行记录数据有误:" + ex.getMessage()).append("</div>");
							logger.error((rowIndex + 1) + "行记录数据有误！");
							logger.error(ex);
						}
					}
				}
			}

			if (exams.size() > 0 && buffer.length() == 0) {
				logger.debug("本次准备导入数据条数:" + exams.size());
				medicalSpotCheckService.bulkInsert(tenantId, exams);
				buffer.append("<br><div style='color:green;'>导入成功！</div>");
				buffer.append("<br><div style='color:green;'>导入数据条数:" + exams.size() + "</div>");
			}
			return buffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
