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
import com.glaf.core.util.UUID32;
import com.glaf.heathcare.domain.MedicalSpotCheck;
import com.glaf.heathcare.domain.MedicalSpotCheckXlsArea;
import com.glaf.heathcare.service.MedicalSpotCheckService;

public class MedicalSpotCheckExcelImporter {
	protected final static Log logger = LogFactory.getLog(MedicalSpotCheckExcelImporter.class);

	public String importData(String userId, String type, List<MedicalSpotCheckXlsArea> areas, java.io.InputStream inputStream) {
		logger.debug("----------------MedicalSpotCheckExcelImporter------------------");
		MedicalSpotCheckService medicalSpotCheckService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalSpotCheckService");

		Workbook wb = null;
		Row row = null;
		Cell cell = null;
		Cell sexCell = null;
		Cell ageCell = null;
		String cellValue = null;
		String sexValue = null;
		String ageValue = null;
		Date tmpDate = null;
		try {
			wb = WorkbookFactory.create(inputStream);

			String checkId = UUID32.generateShortUuid();
			List<MedicalSpotCheck> exams = new ArrayList<MedicalSpotCheck>();

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
							ageCell = row.getCell(area.getAgeOfTheMoonColIndex());
							if (ageCell != null && sexCell != null) {
								ageValue = ExcelUtils.getCellValue(ageCell);
								sexValue = ExcelUtils.getCellValue(sexCell);
								if (sexValue != null && ageValue != null) {

									MedicalSpotCheck exam = new MedicalSpotCheck();

									exam.setType(type);
									exam.setSex(sexValue);
									exam.setAgeOfTheMoonString(ageValue);

									if (ageValue.length() == 5) {
										int year = Integer.parseInt(ageValue.substring(0, 1));
										int month = Integer.parseInt(ageValue.substring(2, 4));
										exam.setYear(year);
										exam.setMonth(month);
										exam.setAgeOfTheMoon(year * 12 + month);
									}

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

									if (area.getCheckDateColIndex() != -1) {
										cell = row.getCell(area.getCheckDateColIndex());
										if (cell != null) {
											cellValue = ExcelUtils.getCellValue(cell);
											if (StringUtils.isNotEmpty(cellValue)) {
												tmpDate = DateUtils.toDate(cellValue);
												exam.setCheckDate(tmpDate);
											}
										}
									}
									exam.setCreateBy(userId);
									exams.add(exam);
								}
							}
						} catch (Exception ex) {
							logger.error(ex);
						}
					}
				}
			}

			if (exams.size() > 0) {
				medicalSpotCheckService.bulkInsert(exams);
			}
			return checkId;
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
	}

}
