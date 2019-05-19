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
package com.glaf.heathcare.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.FilterDescriptor;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DBUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class MedicalSpotCheckDomainFactory {

	public static final String TABLENAME = "HEALTH_MEDICAL_SPOT_CHECK";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeName", "GRADENAME_");
		columnMap.put("personId", "PERSONID_");
		columnMap.put("name", "NAME_");
		columnMap.put("sex", "SEX_");
		columnMap.put("birthday", "BIRTHDAY_");
		columnMap.put("nation", "NATION_");
		columnMap.put("height", "HEIGHT_");
		columnMap.put("heightLevel", "HEIGHTLEVEL_");
		columnMap.put("heightEvaluate", "HEIGHTEVALUATE_");
		columnMap.put("weight", "WEIGHT_");
		columnMap.put("weightLevel", "WEIGHTLEVEL_");
		columnMap.put("weightEvaluate", "WEIGHTEVALUATE_");
		columnMap.put("weightHeightPercent", "WEIGHTHEIGHTPERCENT_");
		columnMap.put("bmi", "BMI_");
		columnMap.put("bmiIndex", "BMIINDEX_");
		columnMap.put("bmiEvaluate", "BMIEVALUATE_");
		columnMap.put("eyesightLeft", "EYESIGHTLEFT_");
		columnMap.put("eyesightRight", "EYESIGHTRIGHT_");
		columnMap.put("hemoglobin", "HEMOGLOBIN_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("ageOfTheMoon", "AGEOFTHEMOON_");
		columnMap.put("ageOfTheMoonString", "AGEOFTHEMOONSTRING_");
		columnMap.put("province", "PROVINCE_");
		columnMap.put("city", "CITY_");
		columnMap.put("area", "AREA_");
		columnMap.put("organization", "ORGANIZATION_");
		columnMap.put("type", "TYPE_");
		columnMap.put("checkDate", "CHECKDATE_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeName", "String");
		javaTypeMap.put("personId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("sex", "String");
		javaTypeMap.put("birthday", "Date");
		javaTypeMap.put("nation", "String");
		javaTypeMap.put("height", "Double");
		javaTypeMap.put("heightLevel", "Integer");
		javaTypeMap.put("heightEvaluate", "String");
		javaTypeMap.put("weight", "Double");
		javaTypeMap.put("weightLevel", "Integer");
		javaTypeMap.put("weightEvaluate", "String");
		javaTypeMap.put("weightHeightPercent", "Double");
		javaTypeMap.put("bmi", "Double");
		javaTypeMap.put("bmiIndex", "Double");
		javaTypeMap.put("bmiEvaluate", "String");
		javaTypeMap.put("eyesightLeft", "Double");
		javaTypeMap.put("eyesightRight", "Double");
		javaTypeMap.put("hemoglobin", "Double");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("ageOfTheMoon", "Integer");
		javaTypeMap.put("ageOfTheMoonString", "String");
		javaTypeMap.put("province", "String");
		javaTypeMap.put("city", "String");
		javaTypeMap.put("area", "String");
		javaTypeMap.put("organization", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("checkDate", "Date");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
	}

	public static Map<String, String> getColumnMap() {
		return columnMap;
	}

	public static Map<String, String> getJavaTypeMap() {
		return javaTypeMap;
	}

	public static TableDefinition getTableDefinition() {
		return getTableDefinition(TABLENAME);
	}

	public static TableDefinition getTableDefinition(String tableName) {
		tableName = tableName.toUpperCase();
		TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setTableName(tableName);
		tableDefinition.setName("MedicalSpotCheck");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("String");
		idColumn.setLength(64);
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition gradeName = new ColumnDefinition();
		gradeName.setName("gradeName");
		gradeName.setColumnName("GRADENAME_");
		gradeName.setJavaType("String");
		gradeName.setLength(100);
		tableDefinition.addColumn(gradeName);

		ColumnDefinition personId = new ColumnDefinition();
		personId.setName("personId");
		personId.setColumnName("PERSONID_");
		personId.setJavaType("String");
		personId.setLength(50);
		tableDefinition.addColumn(personId);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(50);
		tableDefinition.addColumn(name);

		ColumnDefinition sex = new ColumnDefinition();
		sex.setName("sex");
		sex.setColumnName("SEX_");
		sex.setJavaType("String");
		sex.setLength(2);
		tableDefinition.addColumn(sex);

		ColumnDefinition birthday = new ColumnDefinition();
		birthday.setName("birthday");
		birthday.setColumnName("BIRTHDAY_");
		birthday.setJavaType("Date");
		tableDefinition.addColumn(birthday);

		ColumnDefinition nation = new ColumnDefinition();
		nation.setName("nation");
		nation.setColumnName("NATION_");
		nation.setJavaType("String");
		nation.setLength(50);
		tableDefinition.addColumn(nation);

		ColumnDefinition height = new ColumnDefinition();
		height.setName("height");
		height.setColumnName("HEIGHT_");
		height.setJavaType("Double");
		tableDefinition.addColumn(height);

		ColumnDefinition heightLevel = new ColumnDefinition();
		heightLevel.setName("heightLevel");
		heightLevel.setColumnName("HEIGHTLEVEL_");
		heightLevel.setJavaType("Integer");
		tableDefinition.addColumn(heightLevel);

		ColumnDefinition heightEvaluate = new ColumnDefinition();
		heightEvaluate.setName("heightEvaluate");
		heightEvaluate.setColumnName("HEIGHTEVALUATE_");
		heightEvaluate.setJavaType("String");
		heightEvaluate.setLength(200);
		tableDefinition.addColumn(heightEvaluate);

		ColumnDefinition weight = new ColumnDefinition();
		weight.setName("weight");
		weight.setColumnName("WEIGHT_");
		weight.setJavaType("Double");
		tableDefinition.addColumn(weight);

		ColumnDefinition weightLevel = new ColumnDefinition();
		weightLevel.setName("weightLevel");
		weightLevel.setColumnName("WEIGHTLEVEL_");
		weightLevel.setJavaType("Integer");
		tableDefinition.addColumn(weightLevel);

		ColumnDefinition weightEvaluate = new ColumnDefinition();
		weightEvaluate.setName("weightEvaluate");
		weightEvaluate.setColumnName("WEIGHTEVALUATE_");
		weightEvaluate.setJavaType("String");
		weightEvaluate.setLength(200);
		tableDefinition.addColumn(weightEvaluate);

		ColumnDefinition weightHeightPercent = new ColumnDefinition();
		weightHeightPercent.setName("weightHeightPercent");
		weightHeightPercent.setColumnName("WEIGHTHEIGHTPERCENT_");
		weightHeightPercent.setJavaType("Double");
		tableDefinition.addColumn(weightHeightPercent);

		ColumnDefinition bmi = new ColumnDefinition();
		bmi.setName("bmi");
		bmi.setColumnName("BMI_");
		bmi.setJavaType("Double");
		tableDefinition.addColumn(bmi);

		ColumnDefinition bmiIndex = new ColumnDefinition();
		bmiIndex.setName("bmiIndex");
		bmiIndex.setColumnName("BMIINDEX_");
		bmiIndex.setJavaType("Double");
		tableDefinition.addColumn(bmiIndex);

		ColumnDefinition bmiEvaluate = new ColumnDefinition();
		bmiEvaluate.setName("bmiEvaluate");
		bmiEvaluate.setColumnName("BMIEVALUATE_");
		bmiEvaluate.setJavaType("String");
		bmiEvaluate.setLength(200);
		tableDefinition.addColumn(bmiEvaluate);

		ColumnDefinition eyesightLeft = new ColumnDefinition();
		eyesightLeft.setName("eyesightLeft");
		eyesightLeft.setColumnName("EYESIGHTLEFT_");
		eyesightLeft.setJavaType("Double");
		tableDefinition.addColumn(eyesightLeft);

		ColumnDefinition eyesightRight = new ColumnDefinition();
		eyesightRight.setName("eyesightRight");
		eyesightRight.setColumnName("EYESIGHTRIGHT_");
		eyesightRight.setJavaType("Double");
		tableDefinition.addColumn(eyesightRight);

		ColumnDefinition hemoglobin = new ColumnDefinition();
		hemoglobin.setName("hemoglobin");
		hemoglobin.setColumnName("HEMOGLOBIN_");
		hemoglobin.setJavaType("Double");
		tableDefinition.addColumn(hemoglobin);

		ColumnDefinition year = new ColumnDefinition();
		year.setName("year");
		year.setColumnName("YEAR_");
		year.setJavaType("Integer");
		tableDefinition.addColumn(year);

		ColumnDefinition month = new ColumnDefinition();
		month.setName("month");
		month.setColumnName("MONTH_");
		month.setJavaType("Integer");
		tableDefinition.addColumn(month);

		ColumnDefinition ageOfTheMoon = new ColumnDefinition();
		ageOfTheMoon.setName("ageOfTheMoon");
		ageOfTheMoon.setColumnName("AGEOFTHEMOON_");
		ageOfTheMoon.setJavaType("Integer");
		tableDefinition.addColumn(ageOfTheMoon);

		ColumnDefinition ageOfTheMoonString = new ColumnDefinition();
		ageOfTheMoonString.setName("ageOfTheMoonString");
		ageOfTheMoonString.setColumnName("AGEOFTHEMOONSTRING_");
		ageOfTheMoonString.setJavaType("String");
		ageOfTheMoonString.setLength(20);
		tableDefinition.addColumn(ageOfTheMoonString);

		ColumnDefinition province = new ColumnDefinition();
		province.setName("province");
		province.setColumnName("PROVINCE_");
		province.setJavaType("String");
		province.setLength(50);
		tableDefinition.addColumn(province);

		ColumnDefinition city = new ColumnDefinition();
		city.setName("city");
		city.setColumnName("CITY_");
		city.setJavaType("String");
		city.setLength(250);
		tableDefinition.addColumn(city);

		ColumnDefinition area = new ColumnDefinition();
		area.setName("area");
		area.setColumnName("AREA_");
		area.setJavaType("String");
		area.setLength(250);
		tableDefinition.addColumn(area);

		ColumnDefinition organization = new ColumnDefinition();
		organization.setName("organization");
		organization.setColumnName("ORGANIZATION_");
		organization.setJavaType("String");
		organization.setLength(250);
		tableDefinition.addColumn(organization);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition checkDate = new ColumnDefinition();
		checkDate.setName("checkDate");
		checkDate.setColumnName("CHECKDATE_");
		checkDate.setJavaType("Date");
		tableDefinition.addColumn(checkDate);

		ColumnDefinition createBy = new ColumnDefinition();
		createBy.setName("createBy");
		createBy.setColumnName("CREATEBY_");
		createBy.setJavaType("String");
		createBy.setLength(50);
		tableDefinition.addColumn(createBy);

		ColumnDefinition createTime = new ColumnDefinition();
		createTime.setName("createTime");
		createTime.setColumnName("CREATETIME_");
		createTime.setJavaType("Date");
		tableDefinition.addColumn(createTime);

		return tableDefinition;
	}

	public static TableDefinition createTable() {
		TableDefinition tableDefinition = getTableDefinition(TABLENAME);
		if (!DBUtils.tableExists(TABLENAME)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static TableDefinition createTable(String tableName) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		if (!DBUtils.tableExists(tableName)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static void processDataRequest(DataRequest dataRequest) {
		if (dataRequest != null) {
			if (dataRequest.getFilter() != null) {
				if (dataRequest.getFilter().getField() != null) {
					dataRequest.getFilter().setColumn(columnMap.get(dataRequest.getFilter().getField()));
					dataRequest.getFilter().setJavaType(javaTypeMap.get(dataRequest.getFilter().getField()));
				}

				List<FilterDescriptor> filters = dataRequest.getFilter().getFilters();
				for (FilterDescriptor filter : filters) {
					filter.setParent(dataRequest.getFilter());
					if (filter.getField() != null) {
						filter.setColumn(columnMap.get(filter.getField()));
						filter.setJavaType(javaTypeMap.get(filter.getField()));
					}

					List<FilterDescriptor> subFilters = filter.getFilters();
					for (FilterDescriptor f : subFilters) {
						f.setColumn(columnMap.get(f.getField()));
						f.setJavaType(javaTypeMap.get(f.getField()));
						f.setParent(filter);
					}
				}
			}
		}
	}

	private MedicalSpotCheckDomainFactory() {

	}

}
