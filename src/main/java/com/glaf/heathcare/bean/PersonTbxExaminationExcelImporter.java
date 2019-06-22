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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.Authentication;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExcelUtils;
import com.glaf.core.util.StringTools;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonService;

/**
 * 从体检表导入班级及儿童基本信息
 *
 */
public class PersonTbxExaminationExcelImporter {
	protected final static Log logger = LogFactory.getLog(PersonTbxExaminationExcelImporter.class);

	public void doImport(String tenantId, java.io.InputStream inputStream) {
		logger.debug("----------------PersonTbxExaminationExcelImporter------------");
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(inputStream);
			Sheet sheet = wb.getSheetAt(0);
			List<Person> users = new ArrayList<Person>();
			int rows = sheet.getLastRowNum();
			logger.debug("row num:" + rows);
			List<String> gradeNames = new ArrayList<String>();
			for (int rowIndex = 2; rowIndex <= rows; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				Person user = new Person();

				if (Authentication.getAuthenticatedActorId() != null) {
					user.setCreateBy(Authentication.getAuthenticatedActorId());
				}
				String cellValue = null;
				int cells = row.getLastCellNum();
				for (int colIndex = 0; colIndex < cells; colIndex++) {
					Cell cell = row.getCell(colIndex);
					if (cell != null) {
						cellValue = ExcelUtils.getCellValue(cell);
						if (cellValue == null) {
							cellValue = "";
						}
						cellValue = cellValue.trim();
						switch (colIndex) {
						case 0:// 姓名
							user.setName(cellValue);
							break;
						case 1:// 性别
							if (StringUtils.contains(cellValue, "男")) {
								user.setSex("1");
							}
							if (StringUtils.contains(cellValue, "女")) {
								user.setSex("0");
							}
							break;
						case 2:// 出生日期
							cellValue = StringTools.replace(cellValue, ".", "-");
							cellValue = StringTools.replace(cellValue, "/", "-");
							try {
								user.setBirthday(DateUtils.toDate(cellValue));
							} catch (Throwable ex) {

							}
							break;
						case 3:// 班级
							user.setGradeName(cellValue);
							if (!gradeNames.contains(cellValue)) {
								gradeNames.add(cellValue);
							}
							break;
						default:
							break;
						}
					}
				}
				if (StringUtils.isNotEmpty(user.getName()) && user.getBirthday() != null) {
					users.add(user);
				}
			}
			if (users.size() > 0) {
				logger.debug("----------------------------------");
				logger.debug("persons size:" + users.size());
				GradeInfoService gradeInfoService = ContextFactory
						.getBean("com.glaf.heathcare.service.gradeInfoService");
				PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");

				List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(tenantId);
				Map<String, String> gradeMap = new HashMap<String, String>();
				for (GradeInfo gradeInfo : grades) {
					gradeMap.put(gradeInfo.getName(), gradeInfo.getId());
				}

				for (String gradeName : gradeNames) {
					if (gradeMap.get(gradeName) == null) {
						GradeInfo gradeInfo = new GradeInfo();
						gradeInfo.setCode(gradeName);
						gradeInfo.setName(gradeName);
						gradeInfo.setTenantId(tenantId);
						gradeInfo.setCreateBy(Authentication.getAuthenticatedActorId());
						gradeInfoService.save(gradeInfo);
						gradeMap.put(gradeInfo.getName(), gradeInfo.getId());
					}
				}

				PersonQuery query = new PersonQuery();
				query.tenantId(tenantId);
				query.deleteFlag(0);
				query.locked(0);
				List<Person> persons = personService.list(query);
				List<String> userIds = new ArrayList<String>();
				Map<String, Person> personMap = new HashMap<String, Person>();
				if (persons != null && !persons.isEmpty()) {
					for (Person person : persons) {
						personMap.put(person.getId(), person);
						if (person.getIdCardNo() != null) {
							personMap.put(person.getIdCardNo(), person);
						}
						userIds.add(person.getId());
					}
				}

				for (Person user : users) {
					try {
						if (personMap.get(user.getId()) != null) {
							continue;
						}
						Person model = personService.getPerson(user.getId());
						if (model != null) {
							if (gradeMap.get(model.getGradeName()) != null) {
								model.setGradeId(gradeMap.get(model.getGradeName()));
								model.setTenantId(tenantId);
								personService.update(model);
							}
						} else {
							if (gradeMap.get(user.getGradeName()) != null) {
								user.setGradeId(gradeMap.get(user.getGradeName()));
								user.setTenantId(tenantId);
								personService.save(user);
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						logger.error(ex);
					}
				}
			}

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
