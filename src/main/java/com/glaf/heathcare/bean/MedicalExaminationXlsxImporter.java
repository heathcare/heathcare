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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExcelXlsxUtils;
import com.glaf.core.util.UUID32;

import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationXlsArea;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;

public class MedicalExaminationXlsxImporter {
	protected final static Log logger = LogFactory.getLog(MedicalExaminationXlsxImporter.class);

	public String importData(String tenantId, String gradeId, String type, Date checkDate,
			List<MedicalExaminationXlsArea> areas, java.io.InputStream inputStream) {
		logger.debug("----------------MedicalExaminationXlsxReader------------------");
		SysTenantService sysTenantService = ContextFactory.getBean("sysTenantService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);

		List<Person> persons = null;
		if (StringUtils.isNotEmpty(gradeId)) {
			persons = personService.getPersons(gradeId);
		} else {
			persons = personService.getTenantPersons(tenantId);
		}

		Map<String, Person> personMap = new HashMap<String, Person>();
		if (persons != null && !persons.isEmpty()) {
			for (Person person : persons) {
				personMap.put(person.getName().trim(), person);
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		MedicalExaminationQuery query = new MedicalExaminationQuery();
		query.tenantId(tenantId);
		if (StringUtils.isNotEmpty(gradeId)) {
			query.gradeId(gradeId);
		}
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

		XSSFWorkbook wb = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		String cellValue = null;
		Date tmpDate = null;
		try {
			wb = new XSSFWorkbook(inputStream);
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
			XSSFSheet sheet = wb.getSheetAt(0);
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
					cell = row.getCell(area.getNameColIndex());
					if (cell != null) {
						cellValue = ExcelXlsxUtils.getValue(cell, 0);
						if (cellValue != null && personMap.get(cellValue.trim()) != null) {
							cellValue = cellValue.trim();
							if (examMap.get(cellValue) != null) {
								continue;
							}
							if (examMap.get(personMap.get(cellValue).getId()) != null) {
								continue;
							}
							MedicalExamination exam = new MedicalExamination();
							exam.setTenantId(tenantId);
							if (StringUtils.isNotEmpty(gradeId)) {
								exam.setGradeId(gradeId);
							} else {
								exam.setGradeId(personMap.get(cellValue).getGradeId());
							}
							exam.setType(type);
							exam.setCheckDate(checkDate);
							exam.setPersonId(personMap.get(cellValue).getId());
							exam.setName(personMap.get(cellValue).getName().trim());
							exam.setBirthday(personMap.get(cellValue).getBirthday());
							exam.setSex(personMap.get(cellValue).getSex());
							exam.setCheckId(checkId);

							cell = row.getCell(area.getHeightColIndex());
							if (cell != null) {
								cellValue = ExcelXlsxUtils.getValue(cell, 2);
								if (StringUtils.isNotEmpty(cellValue)) {
									exam.setHeight(Double.parseDouble(cellValue));
								}
							}
							cell = row.getCell(area.getWeightColIndex());
							if (cell != null) {
								cellValue = ExcelXlsxUtils.getValue(cell, 2);
								if (StringUtils.isNotEmpty(cellValue)) {
									exam.setWeight(Double.parseDouble(cellValue));
								}
							}
							cell = row.getCell(area.getHemoglobinColIndex());
							if (cell != null) {
								cellValue = ExcelXlsxUtils.getValue(cell, 2);
								if (StringUtils.isNotEmpty(cellValue)) {
									exam.setHemoglobinValue(Double.parseDouble(cellValue));
								}
							}
							cell = row.getCell(area.getCheckDateColIndex());
							if (cell != null) {
								cellValue = ExcelXlsxUtils.getStringOrDateValue(cell, 0);
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
