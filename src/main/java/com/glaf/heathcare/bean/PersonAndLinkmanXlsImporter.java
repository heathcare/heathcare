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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.Authentication;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.StringTools;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.PersonService;

public class PersonAndLinkmanXlsImporter {
	protected final static Log logger = LogFactory.getLog(PersonAndLinkmanXlsImporter.class);

	public void doImport(String tenantId, String gradeId, java.io.InputStream inputStream) {
		logger.debug("----------------PersonAndLinkmanXlsImporter------------------");
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
		List<Person> users = new ArrayList<Person>();
		int rows = sheet.getLastRowNum();
		logger.debug("row num:" + rows);
		for (int rowIndex = 4; rowIndex <= rows; rowIndex++) {
			HSSFRow row = sheet.getRow(rowIndex);
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
				HSSFCell cell = row.getCell(colIndex);
				if (cell != null) {
					cellValue = this.getValue(cell, 0);
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
					case 3:// 入园日期
						cellValue = StringTools.replace(cellValue, ".", "-");
						cellValue = StringTools.replace(cellValue, "/", "-");
						try {
							user.setJoinDate(DateUtils.toDate(cellValue));
						} catch (Throwable ex) {
						}
						break;
					case 4:// 民族
						user.setNation(cellValue);
						break;
					case 5:// 籍贯
						user.setBirthPlace(cellValue);
						break;
					case 6:// 家庭住址
						user.setHomeAddress(cellValue);
						break;
					case 7:// 身份证号码
						user.setIdCardNo(cellValue);
						break;
					case 8:// 父亲姓名
						user.setFather(cellValue);
						break;
					case 9:// 父亲电话
							// cellValue = cell.getStringCellValue();
						user.setFatherTelephone(cellValue);
						break;
					case 10:// 父亲工作单位
						user.setFatherCompany(cellValue);
						break;
					case 11:// 母亲姓名
						user.setMother(cellValue);
						break;
					case 12:// 母亲电话
						// cellValue = cell.getStringCellValue();
						user.setMotherTelephone(cellValue);
						break;
					case 13:// 母亲工作单位
						user.setMotherCompany(cellValue);
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
					ex.printStackTrace();
					logger.error(ex);
				}
			}
		}
	}

	public String getValue(HSSFCell cell, int precision) {
		String strValue = null;
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			short format = cell.getCellStyle().getDataFormat();
			SimpleDateFormat sdf = null;
			if (format == 14 || format == 31 || format == 57 || format == 58 || (176 <= format && format <= 178)
					|| (182 <= format && format <= 196) || (210 <= format && format <= 213) || (208 == format)) { // 日期
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			} else if (format == 20 || format == 32 || format == 183 || (200 <= format && format <= 209)) { // 时间
				sdf = new SimpleDateFormat("HH:mm");
			} else { // 不是日期格式
				double value = cell.getNumericCellValue();
				DecimalFormat nf = new DecimalFormat("###");
				//logger.debug("value->" + nf.format(value));
				return String.valueOf(nf.format(value));
			}
			double value = cell.getNumericCellValue();
			Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
			if (date == null) {
				return "";
			}
			String result = "";
			try {
				result = sdf.format(date);
			} catch (Exception e) {
				return "";
			}
			//logger.debug(result);
			return result;
		} else if (cell.getCellTypeEnum() == CellType.STRING) {
			strValue = cell.getStringCellValue();
		} else if (cell.getCellTypeEnum() == CellType.FORMULA) {

		}
		if (strValue != null) {
			return strValue;
		}
		return "";
	}

}
