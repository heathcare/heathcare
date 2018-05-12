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
public class MedicalExaminationCountDomainFactory {

	public static final String TABLENAME = "HEALTH_MEDICAL_EXAM_COUNT";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("gradeName", "GRADENAME_");
		columnMap.put("checkId", "CHECKID_");
		columnMap.put("totalPerson", "TOTALPERSON_");
		columnMap.put("totalMale", "TOTALMALE_");
		columnMap.put("totalFemale", "TOTALFEMALE_");
		columnMap.put("checkPerson", "CHECKPERSON_");
		columnMap.put("checkPercent", "CHECKPCT_");
		columnMap.put("meanWeightNormal", "MEANWEIGHTNORMAL_");
		columnMap.put("meanWeightNormalPercent", "MEANWEIGHTNORMAL_PCT_");
		columnMap.put("meanHeightNormal", "MEANHEIGHTNORMAL_");
		columnMap.put("meanHeightNormalPercent", "MEANHEIGHTNORMAL_PCT_");
		columnMap.put("meanWeightLow", "MEANWEIGHTLOW_");
		columnMap.put("meanWeightLowPercent", "MEANWEIGHTLOWPCT_");
		columnMap.put("meanHeightLow", "MEANHEIGHTLOW_");
		columnMap.put("meanHeightLowPercent", "MEANHEIGHTLOWPCT_");
		columnMap.put("meanWeightSkinny", "MEANWEIGHTSKINNY_");
		columnMap.put("meanWeightSkinnyPercent", "MEANWEIGHTSKINNYPCT_");
		columnMap.put("meanOverWeight", "MEANOVERWEIGHT_");
		columnMap.put("meanOverWeightPercent", "MEANOVERWEIGHTPCT_");
		columnMap.put("meanWeightObesity", "MEANWEIGHTOBESITY_");
		columnMap.put("meanWeightObesityPercent", "MEANWEIGHTOBESITYPCT_");
		columnMap.put("prctileWeightHeightNormal", "PRCTILE_WH_NORMAL_");
		columnMap.put("prctileWeightHeightNormalPercent", "PRCTILE_WH_NORMAL_PCT_");
		columnMap.put("prctileWeightAgeNormal", "PRCTILE_WA_NORMAL_");
		columnMap.put("prctileWeightAgeNormalPercent", "PRCTILE_WA_NORMAL_PCT_");
		columnMap.put("prctileHeightAgeNormal", "PRCTILE_HA_NORMAL_");
		columnMap.put("prctileHeightAgeNormalPercent", "PRCTILE_HA_NORMAL_PCT_");
		columnMap.put("prctileHeightAgeLow", "PRCTILE_HA_LOW_");
		columnMap.put("prctileHeightAgeLowPercent", "PRCTILE_HA_LOWPCT_");
		columnMap.put("prctileWeightHeightLow", "PRCTILE_WH_LOW_");
		columnMap.put("prctileWeightHeightLowPercent", "PRCTILE_WH_LOWPCT_");
		columnMap.put("prctileWeightAgeLow", "PRCTILE_WA_LOW_");
		columnMap.put("prctileWeightAgeLowPercent", "PRCTILE_WA_LOWPCT_");
		columnMap.put("prctileOverWeight", "PRCTILEOVERWEIGHT_");
		columnMap.put("prctileOverWeightPercent", "PRCTILEOVERWEIGHTPCT_");
		columnMap.put("prctileWeightObesity", "PRCTILEWEIGHTOBESITY_");
		columnMap.put("prctileWeightObesityPercent", "PRCTILEWEIGHTOBESITYPCT_");
		columnMap.put("gradeYear", "GRADEYEAR_");
		columnMap.put("targetType", "TARGETTYPE_");
		columnMap.put("type", "TYPE_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("gradeName", "String");
		javaTypeMap.put("checkId", "String");
		javaTypeMap.put("totalPerson", "Integer");
		javaTypeMap.put("totalMale", "Integer");
		javaTypeMap.put("totalFemale", "Integer");
		javaTypeMap.put("checkPerson", "Integer");
		javaTypeMap.put("checkPercent", "Double");
		javaTypeMap.put("meanWeightNormal", "Integer");
		javaTypeMap.put("meanWeightNormalPercent", "Double");
		javaTypeMap.put("meanHeightNormal", "Integer");
		javaTypeMap.put("meanHeightNormalPercent", "Double");
		javaTypeMap.put("meanWeightLow", "Integer");
		javaTypeMap.put("meanWeightLowPercent", "Double");
		javaTypeMap.put("meanHeightLow", "Integer");
		javaTypeMap.put("meanHeightLowPercent", "Double");
		javaTypeMap.put("meanWeightSkinny", "Integer");
		javaTypeMap.put("meanWeightSkinnyPercent", "Double");
		javaTypeMap.put("meanOverWeight", "Integer");
		javaTypeMap.put("meanOverWeightPercent", "Double");
		javaTypeMap.put("meanWeightObesity", "Integer");
		javaTypeMap.put("meanWeightObesityPercent", "Double");
		javaTypeMap.put("prctileWeightHeightNormal", "Integer");
		javaTypeMap.put("prctileWeightHeightNormalPercent", "Double");
		javaTypeMap.put("prctileWeightAgeNormal", "Integer");
		javaTypeMap.put("prctileWeightAgeNormalPercent", "Double");
		javaTypeMap.put("prctileHeightAgeNormal", "Integer");
		javaTypeMap.put("prctileHeightAgeNormalPercent", "Double");
		javaTypeMap.put("prctileHeightAgeLow", "Integer");
		javaTypeMap.put("prctileHeightAgeLowPercent", "Double");
		javaTypeMap.put("prctileWeightHeightLow", "Integer");
		javaTypeMap.put("prctileWeightHeightLowPercent", "Double");
		javaTypeMap.put("prctileWeightAgeLow", "Integer");
		javaTypeMap.put("prctileWeightAgeLowPercent", "Double");
		javaTypeMap.put("prctileOverWeight", "Integer");
		javaTypeMap.put("prctileOverWeightPercent", "Double");
		javaTypeMap.put("prctileWeightObesity", "Integer");
		javaTypeMap.put("prctileWeightObesityPercent", "Double");
		javaTypeMap.put("gradeYear", "Integer");
		javaTypeMap.put("targetType", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
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
		tableDefinition.setName("MedicalExaminationCount");

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

		ColumnDefinition gradeId = new ColumnDefinition();
		gradeId.setName("gradeId");
		gradeId.setColumnName("GRADEID_");
		gradeId.setJavaType("String");
		gradeId.setLength(50);
		tableDefinition.addColumn(gradeId);

		ColumnDefinition gradeName = new ColumnDefinition();
		gradeName.setName("gradeName");
		gradeName.setColumnName("GRADENAME_");
		gradeName.setJavaType("String");
		gradeName.setLength(100);
		tableDefinition.addColumn(gradeName);

		ColumnDefinition checkId = new ColumnDefinition();
		checkId.setName("checkId");
		checkId.setColumnName("CHECKID_");
		checkId.setJavaType("String");
		checkId.setLength(50);
		tableDefinition.addColumn(checkId);

		ColumnDefinition totalPerson = new ColumnDefinition();
		totalPerson.setName("totalPerson");
		totalPerson.setColumnName("TOTALPERSON_");
		totalPerson.setJavaType("Integer");
		tableDefinition.addColumn(totalPerson);

		ColumnDefinition totalMale = new ColumnDefinition();
		totalMale.setName("totalMale");
		totalMale.setColumnName("TOTALMALE_");
		totalMale.setJavaType("Integer");
		tableDefinition.addColumn(totalMale);

		ColumnDefinition totalFemale = new ColumnDefinition();
		totalFemale.setName("totalFemale");
		totalFemale.setColumnName("TOTALFEMALE_");
		totalFemale.setJavaType("Integer");
		tableDefinition.addColumn(totalFemale);

		ColumnDefinition checkPerson = new ColumnDefinition();
		checkPerson.setName("checkPerson");
		checkPerson.setColumnName("CHECKPERSON_");
		checkPerson.setJavaType("Integer");
		tableDefinition.addColumn(checkPerson);

		ColumnDefinition checkPercent = new ColumnDefinition();
		checkPercent.setName("checkPercent");
		checkPercent.setColumnName("CHECKPCT_");
		checkPercent.setJavaType("Double");
		tableDefinition.addColumn(checkPercent);

		ColumnDefinition meanWeightNormal = new ColumnDefinition();
		meanWeightNormal.setName("meanWeightNormal");
		meanWeightNormal.setColumnName("MEANWEIGHTNORMAL_");
		meanWeightNormal.setJavaType("Integer");
		tableDefinition.addColumn(meanWeightNormal);

		ColumnDefinition meanWeightNormalPercent = new ColumnDefinition();
		meanWeightNormalPercent.setName("meanWeightNormalPercent");
		meanWeightNormalPercent.setColumnName("MEANWEIGHTNORMAL_PCT_");
		meanWeightNormalPercent.setJavaType("Double");
		tableDefinition.addColumn(meanWeightNormalPercent);

		ColumnDefinition meanHeightNormal = new ColumnDefinition();
		meanHeightNormal.setName("meanHeightNormal");
		meanHeightNormal.setColumnName("MEANHEIGHTNORMAL_");
		meanHeightNormal.setJavaType("Integer");
		tableDefinition.addColumn(meanHeightNormal);

		ColumnDefinition meanHeightNormalPercent = new ColumnDefinition();
		meanHeightNormalPercent.setName("meanHeightNormalPercent");
		meanHeightNormalPercent.setColumnName("MEANHEIGHTNORMAL_PCT_");
		meanHeightNormalPercent.setJavaType("Double");
		tableDefinition.addColumn(meanHeightNormalPercent);

		ColumnDefinition meanWeightLow = new ColumnDefinition();
		meanWeightLow.setName("meanWeightLow");
		meanWeightLow.setColumnName("MEANWEIGHTLOW_");
		meanWeightLow.setJavaType("Integer");
		tableDefinition.addColumn(meanWeightLow);

		ColumnDefinition meanWeightLowPercent = new ColumnDefinition();
		meanWeightLowPercent.setName("meanWeightLowPercent");
		meanWeightLowPercent.setColumnName("MEANWEIGHTLOWPCT_");
		meanWeightLowPercent.setJavaType("Double");
		tableDefinition.addColumn(meanWeightLowPercent);

		ColumnDefinition meanHeightLow = new ColumnDefinition();
		meanHeightLow.setName("meanHeightLow");
		meanHeightLow.setColumnName("MEANHEIGHTLOW_");
		meanHeightLow.setJavaType("Integer");
		tableDefinition.addColumn(meanHeightLow);

		ColumnDefinition meanHeightLowPercent = new ColumnDefinition();
		meanHeightLowPercent.setName("meanHeightLowPercent");
		meanHeightLowPercent.setColumnName("MEANHEIGHTLOWPCT_");
		meanHeightLowPercent.setJavaType("Double");
		tableDefinition.addColumn(meanHeightLowPercent);

		ColumnDefinition meanWeightSkinny = new ColumnDefinition();
		meanWeightSkinny.setName("meanWeightSkinny");
		meanWeightSkinny.setColumnName("MEANWEIGHTSKINNY_");
		meanWeightSkinny.setJavaType("Integer");
		tableDefinition.addColumn(meanWeightSkinny);

		ColumnDefinition meanWeightSkinnyPercent = new ColumnDefinition();
		meanWeightSkinnyPercent.setName("meanWeightSkinnyPercent");
		meanWeightSkinnyPercent.setColumnName("MEANWEIGHTSKINNYPCT_");
		meanWeightSkinnyPercent.setJavaType("Double");
		tableDefinition.addColumn(meanWeightSkinnyPercent);

		ColumnDefinition meanOverWeight = new ColumnDefinition();
		meanOverWeight.setName("meanOverWeight");
		meanOverWeight.setColumnName("MEANOVERWEIGHT_");
		meanOverWeight.setJavaType("Integer");
		tableDefinition.addColumn(meanOverWeight);

		ColumnDefinition meanOverWeightPercent = new ColumnDefinition();
		meanOverWeightPercent.setName("meanOverWeightPercent");
		meanOverWeightPercent.setColumnName("MEANOVERWEIGHTPCT_");
		meanOverWeightPercent.setJavaType("Double");
		tableDefinition.addColumn(meanOverWeightPercent);

		ColumnDefinition meanWeightObesity = new ColumnDefinition();
		meanWeightObesity.setName("meanWeightObesity");
		meanWeightObesity.setColumnName("MEANWEIGHTOBESITY_");
		meanWeightObesity.setJavaType("Integer");
		tableDefinition.addColumn(meanWeightObesity);

		ColumnDefinition meanWeightObesityPercent = new ColumnDefinition();
		meanWeightObesityPercent.setName("meanWeightObesityPercent");
		meanWeightObesityPercent.setColumnName("MEANWEIGHTOBESITYPCT_");
		meanWeightObesityPercent.setJavaType("Double");
		tableDefinition.addColumn(meanWeightObesityPercent);

		ColumnDefinition prctileWeightHeightNormal = new ColumnDefinition();
		prctileWeightHeightNormal.setName("prctileWeightHeightNormal");
		prctileWeightHeightNormal.setColumnName("PRCTILE_WH_NORMAL_");
		prctileWeightHeightNormal.setJavaType("Integer");
		tableDefinition.addColumn(prctileWeightHeightNormal);

		ColumnDefinition prctileWeightHeightNormalPercent = new ColumnDefinition();
		prctileWeightHeightNormalPercent.setName("prctileWeightHeightNormalPercent");
		prctileWeightHeightNormalPercent.setColumnName("PRCTILE_WH_NORMAL_PCT_");
		prctileWeightHeightNormalPercent.setJavaType("Double");
		tableDefinition.addColumn(prctileWeightHeightNormalPercent);

		ColumnDefinition prctileWeightAgeNormal = new ColumnDefinition();
		prctileWeightAgeNormal.setName("prctileWeightAgeNormal");
		prctileWeightAgeNormal.setColumnName("PRCTILE_WA_NORMAL_");
		prctileWeightAgeNormal.setJavaType("Integer");
		tableDefinition.addColumn(prctileWeightAgeNormal);

		ColumnDefinition prctileWeightAgeNormalPercent = new ColumnDefinition();
		prctileWeightAgeNormalPercent.setName("prctileWeightAgeNormalPercent");
		prctileWeightAgeNormalPercent.setColumnName("PRCTILE_WA_NORMAL_PCT_");
		prctileWeightAgeNormalPercent.setJavaType("Double");
		tableDefinition.addColumn(prctileWeightAgeNormalPercent);

		ColumnDefinition prctileHeightAgeNormal = new ColumnDefinition();
		prctileHeightAgeNormal.setName("prctileHeightAgeNormal");
		prctileHeightAgeNormal.setColumnName("PRCTILE_HA_NORMAL_");
		prctileHeightAgeNormal.setJavaType("Integer");
		tableDefinition.addColumn(prctileHeightAgeNormal);

		ColumnDefinition prctileHeightAgeNormalPercent = new ColumnDefinition();
		prctileHeightAgeNormalPercent.setName("prctileHeightAgeNormalPercent");
		prctileHeightAgeNormalPercent.setColumnName("PRCTILE_HA_NORMAL_PCT_");
		prctileHeightAgeNormalPercent.setJavaType("Double");
		tableDefinition.addColumn(prctileHeightAgeNormalPercent);

		ColumnDefinition prctileHeightAgeLow = new ColumnDefinition();
		prctileHeightAgeLow.setName("prctileHeightAgeLow");
		prctileHeightAgeLow.setColumnName("PRCTILE_HA_LOW_");
		prctileHeightAgeLow.setJavaType("Integer");
		tableDefinition.addColumn(prctileHeightAgeLow);

		ColumnDefinition prctileHeightAgeLowPercent = new ColumnDefinition();
		prctileHeightAgeLowPercent.setName("prctileHeightAgeLowPercent");
		prctileHeightAgeLowPercent.setColumnName("PRCTILE_HA_LOWPCT_");
		prctileHeightAgeLowPercent.setJavaType("Double");
		tableDefinition.addColumn(prctileHeightAgeLowPercent);

		ColumnDefinition prctileWeightHeightLow = new ColumnDefinition();
		prctileWeightHeightLow.setName("prctileWeightHeightLow");
		prctileWeightHeightLow.setColumnName("PRCTILE_WH_LOW_");
		prctileWeightHeightLow.setJavaType("Integer");
		tableDefinition.addColumn(prctileWeightHeightLow);

		ColumnDefinition prctileWeightHeightLowPercent = new ColumnDefinition();
		prctileWeightHeightLowPercent.setName("prctileWeightHeightLowPercent");
		prctileWeightHeightLowPercent.setColumnName("PRCTILE_WH_LOWPCT_");
		prctileWeightHeightLowPercent.setJavaType("Double");
		tableDefinition.addColumn(prctileWeightHeightLowPercent);

		ColumnDefinition prctileWeightAgeLow = new ColumnDefinition();
		prctileWeightAgeLow.setName("prctileWeightAgeLow");
		prctileWeightAgeLow.setColumnName("PRCTILE_WA_LOW_");
		prctileWeightAgeLow.setJavaType("Integer");
		tableDefinition.addColumn(prctileWeightAgeLow);

		ColumnDefinition prctileWeightAgeLowPercent = new ColumnDefinition();
		prctileWeightAgeLowPercent.setName("prctileWeightAgeLowPercent");
		prctileWeightAgeLowPercent.setColumnName("PRCTILE_WA_LOWPCT_");
		prctileWeightAgeLowPercent.setJavaType("Double");
		tableDefinition.addColumn(prctileWeightAgeLowPercent);

		ColumnDefinition prctileOverWeight = new ColumnDefinition();
		prctileOverWeight.setName("prctileOverWeight");
		prctileOverWeight.setColumnName("PRCTILEOVERWEIGHT_");
		prctileOverWeight.setJavaType("Integer");
		tableDefinition.addColumn(prctileOverWeight);

		ColumnDefinition prctileOverWeightPercent = new ColumnDefinition();
		prctileOverWeightPercent.setName("prctileOverWeightPercent");
		prctileOverWeightPercent.setColumnName("PRCTILEOVERWEIGHTPCT_");
		prctileOverWeightPercent.setJavaType("Double");
		tableDefinition.addColumn(prctileOverWeightPercent);

		ColumnDefinition prctileWeightObesity = new ColumnDefinition();
		prctileWeightObesity.setName("prctileWeightObesity");
		prctileWeightObesity.setColumnName("PRCTILEWEIGHTOBESITY_");
		prctileWeightObesity.setJavaType("Integer");
		tableDefinition.addColumn(prctileWeightObesity);

		ColumnDefinition prctileWeightObesityPercent = new ColumnDefinition();
		prctileWeightObesityPercent.setName("prctileWeightObesityPercent");
		prctileWeightObesityPercent.setColumnName("PRCTILEWEIGHTOBESITYPCT_");
		prctileWeightObesityPercent.setJavaType("Double");
		tableDefinition.addColumn(prctileWeightObesityPercent);

		ColumnDefinition gradeYear = new ColumnDefinition();
		gradeYear.setName("gradeYear");
		gradeYear.setColumnName("GRADEYEAR_");
		gradeYear.setJavaType("Integer");
		tableDefinition.addColumn(gradeYear);

		ColumnDefinition targetType = new ColumnDefinition();
		targetType.setName("targetType");
		targetType.setColumnName("TARGETTYPE_");
		targetType.setJavaType("String");
		targetType.setLength(50);
		tableDefinition.addColumn(targetType);

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

	private MedicalExaminationCountDomainFactory() {

	}

}
