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

import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DBUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class MedicalExaminationEvaluateDomainFactory {

	public static final String TABLENAME = "HEALTH_MEDICAL_EXAM_EVAL";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("batchId", "BATCHID_");
		columnMap.put("checkId", "CHECKID_");
		columnMap.put("jobNo", "JOBNO_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("provinceId", "PROVINCEID_");
		columnMap.put("cityId", "CITYID_");
		columnMap.put("areaId", "AREAID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("personId", "PERSONID_");
		columnMap.put("name", "NAME_");
		columnMap.put("sex", "SEX_");
		columnMap.put("height", "HEIGHT_");
		columnMap.put("heightLevel", "HEIGHTLEVEL_");
		columnMap.put("heightEvaluate", "HEIGHTEVALUATE_");
		columnMap.put("stdWeight", "STDWEIGHT_");
		columnMap.put("weightOffsetPercent", "WEIGHTOFFSETPERCENT_");
		columnMap.put("weight", "WEIGHT_");
		columnMap.put("weightLevel", "WEIGHTLEVEL_");
		columnMap.put("weightEvaluate", "WEIGHTEVALUATE_");
		columnMap.put("weightHeightEvaluate", "WEIGHTHEIGHTEVALUATE_");
		columnMap.put("weightHeightLevel", "WEIGHTHEIGHTLEVEL_");
		columnMap.put("weightHeightPercent", "WEIGHTHEIGHTPERCENT_");
		columnMap.put("bmi", "BMI_");
		columnMap.put("bmiIndex", "BMIINDEX_");
		columnMap.put("bmiEvaluate", "BMIEVALUATE_");
		columnMap.put("healthEvaluate", "HEALTHEVALUATE_");
		columnMap.put("type", "TYPE_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("ageOfTheMoon", "AGEOFTHEMOON_");
		columnMap.put("checkDate", "CHECKDATE_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("batchId", "Long");
		javaTypeMap.put("checkId", "String");
		javaTypeMap.put("jobNo", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("provinceId", "Long");
		javaTypeMap.put("cityId", "Long");
		javaTypeMap.put("areaId", "Long");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("personId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("sex", "String");
		javaTypeMap.put("height", "Double");
		javaTypeMap.put("heightLevel", "Integer");
		javaTypeMap.put("heightEvaluate", "String");
		javaTypeMap.put("stdWeight", "Double");
		javaTypeMap.put("weightOffsetPercent", "Double");
		javaTypeMap.put("weight", "Double");
		javaTypeMap.put("weightLevel", "Integer");
		javaTypeMap.put("weightEvaluate", "String");
		javaTypeMap.put("weightHeightEvaluate", "String");
		javaTypeMap.put("weightHeightLevel", "Double");
		javaTypeMap.put("weightHeightPercent", "Double");
		javaTypeMap.put("bmi", "Double");
		javaTypeMap.put("bmiIndex", "Double");
		javaTypeMap.put("bmiEvaluate", "String");
		javaTypeMap.put("healthEvaluate", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("ageOfTheMoon", "Integer");
		javaTypeMap.put("checkDate", "Date");
		javaTypeMap.put("createTime", "Date");
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
		tableDefinition.setName("MedicalExaminationEvaluate");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition batchId = new ColumnDefinition();
		batchId.setName("batchId");
		batchId.setColumnName("BATCHID_");
		batchId.setJavaType("Long");
		tableDefinition.addColumn(batchId);

		ColumnDefinition checkId = new ColumnDefinition();
		checkId.setName("checkId");
		checkId.setColumnName("CHECKID_");
		checkId.setJavaType("String");
		checkId.setLength(50);
		tableDefinition.addColumn(checkId);

		ColumnDefinition jobNo = new ColumnDefinition();
		jobNo.setName("jobNo");
		jobNo.setColumnName("JOBNO_");
		jobNo.setJavaType("String");
		jobNo.setLength(50);
		tableDefinition.addColumn(jobNo);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition provinceId = new ColumnDefinition();
		provinceId.setName("provinceId");
		provinceId.setColumnName("PROVINCEID_");
		provinceId.setJavaType("Long");
		tableDefinition.addColumn(provinceId);

		ColumnDefinition cityId = new ColumnDefinition();
		cityId.setName("cityId");
		cityId.setColumnName("CITYID_");
		cityId.setJavaType("Long");
		tableDefinition.addColumn(cityId);

		ColumnDefinition areaId = new ColumnDefinition();
		areaId.setName("areaId");
		areaId.setColumnName("AREAID_");
		areaId.setJavaType("Long");
		tableDefinition.addColumn(areaId);

		ColumnDefinition gradeId = new ColumnDefinition();
		gradeId.setName("gradeId");
		gradeId.setColumnName("GRADEID_");
		gradeId.setJavaType("String");
		gradeId.setLength(50);
		tableDefinition.addColumn(gradeId);

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
		name.setLength(100);
		tableDefinition.addColumn(name);

		ColumnDefinition sex = new ColumnDefinition();
		sex.setName("sex");
		sex.setColumnName("SEX_");
		sex.setJavaType("String");
		sex.setLength(2);
		tableDefinition.addColumn(sex);

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

		ColumnDefinition stdWeight = new ColumnDefinition();
		stdWeight.setName("stdWeight");
		stdWeight.setColumnName("STDWEIGHT_");
		stdWeight.setJavaType("Double");
		tableDefinition.addColumn(stdWeight);

		ColumnDefinition weightOffsetPercent = new ColumnDefinition();
		weightOffsetPercent.setName("weightOffsetPercent");
		weightOffsetPercent.setColumnName("WEIGHTOFFSETPERCENT_");
		weightOffsetPercent.setJavaType("Double");
		tableDefinition.addColumn(weightOffsetPercent);

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

		ColumnDefinition weightHeightEvaluate = new ColumnDefinition();
		weightHeightEvaluate.setName("weightHeightEvaluate");
		weightHeightEvaluate.setColumnName("WEIGHTHEIGHTEVALUATE_");
		weightHeightEvaluate.setJavaType("String");
		weightHeightEvaluate.setLength(200);
		tableDefinition.addColumn(weightHeightEvaluate);

		ColumnDefinition weightHeightLevel = new ColumnDefinition();
		weightHeightLevel.setName("weightHeightLevel");
		weightHeightLevel.setColumnName("WEIGHTHEIGHTLEVEL_");
		weightHeightLevel.setJavaType("Double");
		tableDefinition.addColumn(weightHeightLevel);

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

		ColumnDefinition healthEvaluate = new ColumnDefinition();
		healthEvaluate.setName("healthEvaluate");
		healthEvaluate.setColumnName("HEALTHEVALUATE_");
		healthEvaluate.setJavaType("String");
		healthEvaluate.setLength(500);
		tableDefinition.addColumn(healthEvaluate);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

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

		ColumnDefinition checkDate = new ColumnDefinition();
		checkDate.setName("checkDate");
		checkDate.setColumnName("CHECKDATE_");
		checkDate.setJavaType("Date");
		tableDefinition.addColumn(checkDate);

		ColumnDefinition createTime = new ColumnDefinition();
		createTime.setName("createTime");
		createTime.setColumnName("CREATETIME_");
		createTime.setJavaType("Date");
		tableDefinition.addColumn(createTime);

		return tableDefinition;
	}

	private MedicalExaminationEvaluateDomainFactory() {

	}

}
