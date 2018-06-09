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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.Authentication;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExcelXlsxUtils;
import com.glaf.core.util.StringTools;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.PersonService;

public class PersonXlsxImporter {
	protected final static Log logger = LogFactory.getLog(PersonXlsxImporter.class);

	public void doImport(String tenantId, String gradeId, java.io.InputStream inputStream) {
		logger.debug("----------------PersonXlsImporter------------------");
		XSSFWorkbook wb = null;
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
		XSSFSheet sheet = wb.getSheetAt(0);
		List<Person> users = new ArrayList<Person>();
		int rows = sheet.getLastRowNum();
		logger.debug("rows size:" + rows);
		for (int rowIndex = 2; rowIndex <= rows; rowIndex++) {
			XSSFRow row = sheet.getRow(rowIndex);
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
				XSSFCell cell = row.getCell(colIndex);
				if (cell != null) {
					cellValue = ExcelXlsxUtils.getStringOrDateValue(cell, 0);
					if (cellValue == null) {
						cellValue = "";
					}
					cellValue = cellValue.trim();
					switch (colIndex) {
					case 0:// 姓名
						user.setName(cellValue);
						break;
					case 1:// 班级
						break;
					case 2:// 性别
						if (StringUtils.contains(cellValue, "男")) {
							user.setSex("1");
						}
						if (StringUtils.contains(cellValue, "女")) {
							user.setSex("0");
						}
						break;
					case 3:// 出生日期
						cellValue = StringTools.replace(cellValue, ".", "-");
						cellValue = StringTools.replace(cellValue, "/", "-");
						try {
							user.setBirthday(DateUtils.toDate(cellValue));
						} catch (java.lang.Throwable ex) {

						}
						break;
					case 4:// 证件类型
						break;
					case 5:// 身份证号码
						user.setIdCardNo(cellValue);
						break;
					case 6:// 血型
						user.setBloodType(cellValue);
						break;
					case 7:// 国籍/地区
						user.setNationality(cellValue);
						break;
					case 8:// 民族
						user.setNation(cellValue);
						break;
					case 9:// 港澳台侨外
						break;
					case 10:// 出生地代码
						break;
					case 11:// 籍贯
						user.setBirthPlace(cellValue);
						break;
					case 12:// 户口性质
						user.setNatureAccount(cellValue);
						break;
					case 13:// 非农业户口类型
						user.setNatureType(cellValue);
						break;
					case 14:// 户口所在地代码
						break;
					case 15:// 家庭住址
						user.setHomeAddress(cellValue);
						break;
					case 16:// 入园日期
						cellValue = StringTools.replace(cellValue, ".", "-");
						cellValue = StringTools.replace(cellValue, "/", "-");
						try {
							user.setJoinDate(DateUtils.toDate(cellValue));
						} catch (Throwable ex) {
						}
						break;
					case 17:// 就读方式
						break;
					case 18:// 是否独生子女
						user.setOneChild(cellValue);
						break;
					case 19:// 是否留守儿童
						break;
					case 20:// 否进城务工人员子女
						break;
					case 21:// 健康状况
						user.setHealthCondition(cellValue);
						break;
					case 22:// 是否残疾幼儿
						user.setDisability(cellValue);
						break;
					case 23:// 残疾幼儿类别
						break;
					case 24:// 是否孤儿
						break;
					case 25:// 监护人姓名
						user.setGuardian(cellValue);
						break;
					case 26:// 监护人身份证件类型
						user.setGuardianCardType(cellValue);
						break;
					case 27:// 监护人身份证件号码
						user.setGuardianNo(cellValue);
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
			PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
			PersonQuery query = new PersonQuery();
			query.tenantId(tenantId);
			query.gradeId(gradeId);
			query.deleteFlag(0);
			query.locked(0);
			List<Person> persons = personService.list(query);
			List<String> userIds = new ArrayList<String>();
			Map<String, Person> personMap = new HashMap<String, Person>();
			if (persons != null && !persons.isEmpty()) {
				for (Person person : persons) {
					personMap.put(person.getId(), person);
					personMap.put(person.getIdCardNo(), person);
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
						model.setGradeId(gradeId);
						model.setTenantId(tenantId);
						personService.update(model);
					} else {
						user.setGradeId(gradeId);
						user.setTenantId(tenantId);
						personService.save(user);
					}
				} catch (Exception ex) {
					//ex.printStackTrace();
					logger.error(ex);
				}
			}
		}
	}

}
