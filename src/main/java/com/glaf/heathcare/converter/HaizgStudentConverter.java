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

import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExcelUtils;
import com.glaf.core.util.StringTools;

import com.glaf.heathcare.domain.Person;

public class HaizgStudentConverter {
	protected final static Log logger = LogFactory.getLog(HaizgStudentConverter.class);

	public List<Person> getPersons(java.io.InputStream inputStream) {
		logger.debug("----------------HaizgStudentConverter------------------");
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
		List<Person> persons = new ArrayList<Person>();
		int rows = sheet.getLastRowNum();
		logger.debug("row num:" + rows);
		for (int rowIndex = 2; rowIndex <= rows; rowIndex++) {// 第三行开始取数
			HSSFRow row = sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			Person person = new Person();
			//String year = null;
			HSSFCell cell = null;
			String cellValue = null;
			String rel = null;
			String tel = null;
			String company = null;
			String xname = null;
			int cells = row.getLastCellNum();
			for (int colIndex = 0; colIndex < cells; colIndex++) {
				cell = row.getCell(colIndex);
				if (cell != null) {
					cellValue = ExcelUtils.getStringOrDateValue(cell, 0);
					if (cellValue == null) {
						cellValue = "";
					}
					cellValue = cellValue.trim();
					switch (colIndex) {
					case 0:
						//year = cellValue;
						break;
					case 1:// 班级
						//cellValue = StringTools.replace(cellValue, "小", "");
						//cellValue = StringTools.replace(cellValue, "中", "");
						//cellValue = StringTools.replace(cellValue, "大", "");
						person.setGradeName(cellValue);
						break;
					case 4:// 姓名
						person.setName(cellValue);
						break;
					case 5:// 性别
						if (StringUtils.contains(cellValue, "男")) {
							person.setSex("男");
						}
						if (StringUtils.contains(cellValue, "女")) {
							person.setSex("女");
						}
						break;
					case 6:// 出生日期
						cellValue = StringTools.replace(cellValue, ".", "-");
						cellValue = StringTools.replace(cellValue, "/", "-");
						try {
							person.setBirthday(DateUtils.toDate(cellValue));
						} catch (Throwable ex) {

						}
						break;
					case 7:// 身份证号码
						person.setIdCardNo(cellValue);
						break;
					case 8:// 民族
						person.setNation(cellValue);
						break;
					case 11:// 入园日期
						cellValue = StringTools.replace(cellValue, ".", "-");
						cellValue = StringTools.replace(cellValue, "/", "-");
						try {
							person.setJoinDate(DateUtils.toDate(cellValue));
						} catch (Throwable ex) {
						}
						break;
					case 16:// 籍贯
						person.setBirthPlace(cellValue);
						break;
					case 17:// 家庭住址
						person.setHomeAddress(cellValue);
						break;
					case 18:// 家庭成员1姓名
						xname = cellValue;
						break;
					case 20:// 与家庭成员1关系
						rel = cellValue;
						break;
					case 22:// 家庭成员1手机号码
						tel = cellValue;
						break;
					case 23:// 家庭成员1单位
						company = cellValue;
						break;
					case 24:
						if (StringUtils.isEmpty(person.getFather()) && StringUtils.equals(rel, "父亲")
								|| StringUtils.equals(rel, "爸爸")) {
							person.setFather(xname);
							person.setFatherTelephone(tel);
							person.setFatherCompany(company);
						} else if (StringUtils.isEmpty(person.getMother()) && StringUtils.equals(rel, "母亲")
								|| StringUtils.equals(rel, "妈妈")) {
							person.setMother(xname);
							person.setMotherTelephone(tel);
							person.setMotherCompany(company);
						}
						break;
					case 25:
						xname = cellValue;
						break;
					case 27:// 与家庭成员1关系
						rel = cellValue;
						break;
					case 29:// 家庭成员1手机号码
						tel = cellValue;
						break;
					case 30:// 家庭成员1单位
						company = cellValue;
						break;
					case 31:
						if (StringUtils.isEmpty(person.getFather()) && StringUtils.equals(rel, "父亲")
								|| StringUtils.equals(rel, "爸爸")) {
							person.setFather(xname);
							person.setFatherTelephone(tel);
							person.setFatherCompany(company);
						} else if (StringUtils.isEmpty(person.getMother()) && StringUtils.equals(rel, "母亲")
								|| StringUtils.equals(rel, "妈妈")) {
							person.setMother(xname);
							person.setMotherTelephone(tel);
							person.setMotherCompany(company);
						}
						break;
					default:
						break;
					}
				}
			}
			if (StringUtils.isNotEmpty(person.getName()) && person.getBirthday() != null) {
				persons.add(person);
			}
		}
		return persons;
	}

}
