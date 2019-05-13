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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExcelUtils;
import com.glaf.core.util.UUID32;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationXlsArea;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;

public class MedicalExaminationTbxExcelImporter {
	protected final static Log logger = LogFactory.getLog(MedicalExaminationTbxExcelImporter.class);

	public String importData(String tenantId, String type, Date checkDate, List<MedicalExaminationXlsArea> areas,
			java.io.InputStream inputStream) {
		logger.debug("----------------MedicalExaminationTbxExcelImporter------------------");
		SysTenantService sysTenantService = ContextFactory.getBean("sysTenantService");
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);

		List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(tenantId);
		Map<String, String> gradeMap = new HashMap<String, String>();
		for (GradeInfo gradeInfo : grades) {
			gradeMap.put(gradeInfo.getId(), gradeInfo.getName());
			gradeMap.put(gradeInfo.getName(), gradeInfo.getId());
		}

		List<Person> persons = personService.getTenantPersons(tenantId);

		Map<String, Person> personMap = new HashMap<String, Person>();
		if (persons != null && !persons.isEmpty()) {
			for (Person person : persons) {
				if (gradeMap.get(person.getGradeId()) != null) {
					personMap.put(gradeMap.get(person.getGradeId()) + "_" + person.getName().trim(), person);
				}
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		MedicalExaminationQuery query = new MedicalExaminationQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.month(month);

		Map<String, String> examMap = new HashMap<String, String>();
		List<MedicalExamination> rows = medicalExaminationService.list(query);
		if (rows != null && !rows.isEmpty()) {
			for (MedicalExamination me : rows) {
				examMap.put(me.getName().trim(), me.getPersonId());
				examMap.put(me.getPersonId(), me.getPersonId());
			}
		}

		Workbook wb = null;
		Row row = null;
		Cell cell = null;
		Cell gradeCell = null;
		Cell personCell = null;
		String cellValue = null;
		String gradeValue = null;
		String personValue = null;
		Person person = null;
		Date tmpDate = null;
		try {
			wb = WorkbookFactory.create(inputStream);
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

		String checkId = UUID32.getUUID();
		List<MedicalExamination> exams = new ArrayList<MedicalExamination>();

		for (MedicalExaminationXlsArea area : areas) {
			Sheet sheet = wb.getSheetAt(0);
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
					gradeCell = row.getCell(area.getGradeColIndex());
					personCell = row.getCell(area.getNameColIndex());
					if (gradeCell != null && personCell != null) {
						gradeValue = ExcelUtils.getCellValue(cell);
						personValue = ExcelUtils.getCellValue(cell);
						if (gradeValue != null && personValue != null) {
							person = personMap.get(gradeValue.trim() + "_" + personValue.trim());
							if (person != null) {
								MedicalExamination exam = new MedicalExamination();
								exam.setTenantId(tenantId);
								exam.setCheckId(checkId);
								exam.setType(type);
								exam.setCheckDate(checkDate);
								exam.setPersonId(person.getId());
								exam.setName(person.getName().trim());
								exam.setBirthday(person.getBirthday());
								exam.setSex(person.getSex());

								cell = row.getCell(area.getHeightColIndex());
								if (cell != null) {
									cellValue = ExcelUtils.getValue(cell, 2);
									if (StringUtils.isNotEmpty(cellValue)) {
										exam.setHeight(Double.parseDouble(cellValue));
									}
								}

								cell = row.getCell(area.getWeightColIndex());
								if (cell != null) {
									cellValue = ExcelUtils.getValue(cell, 2);
									if (StringUtils.isNotEmpty(cellValue)) {
										exam.setWeight(Double.parseDouble(cellValue));
									}
								}

								cell = row.getCell(area.getEyesightLeftColIndex());
								if (cell != null) {
									cellValue = ExcelUtils.getValue(cell, 1);
									if (StringUtils.isNotEmpty(cellValue)) {
										exam.setEyesightLeft(Double.parseDouble(cellValue));
									}
								}

								cell = row.getCell(area.getEyesightRightColIndex());
								if (cell != null) {
									cellValue = ExcelUtils.getValue(cell, 1);
									if (StringUtils.isNotEmpty(cellValue)) {
										exam.setEyesightRight(Double.parseDouble(cellValue));
									}
								}

								cell = row.getCell(area.getHemoglobinColIndex());
								if (cell != null) {
									cellValue = ExcelUtils.getValue(cell, 2);
									if (StringUtils.isNotEmpty(cellValue)) {
										exam.setHemoglobinValue(Double.parseDouble(cellValue));
									}
								}

								cell = row.getCell(area.getCheckDateColIndex());
								if (cell != null) {
									cellValue = ExcelUtils.getCellValue(cell);
									if (StringUtils.isNotEmpty(cellValue)) {
										tmpDate = DateUtils.toDate(cellValue);
										if (DateUtils.getYearMonth(tmpDate) == DateUtils.getYearMonth(checkDate)) {
											exam.setCheckDate(tmpDate);
										}
									}
								}

								exam.setProvinceId(tenant.getProvinceId());
								exam.setCityId(tenant.getCityId());
								exam.setAreaId(tenant.getAreaId());
								exams.add(exam);
							}
						}
					}
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}

		if (exams.size() > 0) {
			medicalExaminationService.bulkInsert(exams);
		}
		return checkId;
	}

}
