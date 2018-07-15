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
public class DietaryCategoryDomainFactory {

	public static final String TABLENAME = "HEALTH_DIETARY_CATEGORY";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("name", "NAME_");
		columnMap.put("description", "DESCRIPTION_");
		columnMap.put("season", "SEASON_");
		columnMap.put("province", "PROVINCE_");
		columnMap.put("region", "REGION_");
		columnMap.put("typeId", "TYPEID_");
		columnMap.put("peopleType", "PEOPLETYPE_");
		columnMap.put("suitNo", "SUITNO_");
		columnMap.put("sortNo", "SORTNO_");
		columnMap.put("sysFlag", "SYSFLAG_");
		columnMap.put("enableFlag", "ENABLEFLAG_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("description", "String");
		javaTypeMap.put("season", "Integer");
		javaTypeMap.put("province", "String");
		javaTypeMap.put("region", "String");
		javaTypeMap.put("typeId", "Long");
		javaTypeMap.put("peopleType", "String");
		javaTypeMap.put("suitNo", "Integer");
		javaTypeMap.put("sortNo", "Integer");
		javaTypeMap.put("sysFlag", "String");
		javaTypeMap.put("enableFlag", "String");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("updateBy", "String");
		javaTypeMap.put("updateTime", "Date");
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
		tableDefinition.setName("DietaryCategory");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(200);
		tableDefinition.addColumn(name);

		ColumnDefinition description = new ColumnDefinition();
		description.setName("description");
		description.setColumnName("DESCRIPTION_");
		description.setJavaType("String");
		description.setLength(4000);
		tableDefinition.addColumn(description);

		ColumnDefinition season = new ColumnDefinition();
		season.setName("season");
		season.setColumnName("SEASON_");
		season.setJavaType("Integer");
		tableDefinition.addColumn(season);

		ColumnDefinition province = new ColumnDefinition();
		province.setName("province");
		province.setColumnName("PROVINCE_");
		province.setJavaType("String");
		province.setLength(50);
		tableDefinition.addColumn(province);

		ColumnDefinition region = new ColumnDefinition();
		region.setName("region");
		region.setColumnName("REGION_");
		region.setJavaType("String");
		region.setLength(50);
		tableDefinition.addColumn(region);

		ColumnDefinition typeId = new ColumnDefinition();
		typeId.setName("typeId");
		typeId.setColumnName("TYPEID_");
		typeId.setJavaType("Long");
		tableDefinition.addColumn(typeId);

		ColumnDefinition peopleType = new ColumnDefinition();
		peopleType.setName("peopleType");
		peopleType.setColumnName("PEOPLETYPE_");
		peopleType.setJavaType("String");
		peopleType.setLength(50);
		tableDefinition.addColumn(peopleType);

		ColumnDefinition suitNo = new ColumnDefinition();
		suitNo.setName("suitNo");
		suitNo.setColumnName("SUITNO_");
		suitNo.setJavaType("Integer");
		tableDefinition.addColumn(suitNo);

		ColumnDefinition sortNo = new ColumnDefinition();
		sortNo.setName("sortNo");
		sortNo.setColumnName("SORTNO_");
		sortNo.setJavaType("Integer");
		tableDefinition.addColumn(sortNo);

		ColumnDefinition sysFlag = new ColumnDefinition();
		sysFlag.setName("sysFlag");
		sysFlag.setColumnName("SYSFLAG_");
		sysFlag.setJavaType("String");
		sysFlag.setLength(1);
		tableDefinition.addColumn(sysFlag);

		ColumnDefinition enableFlag = new ColumnDefinition();
		enableFlag.setName("enableFlag");
		enableFlag.setColumnName("ENABLEFLAG_");
		enableFlag.setJavaType("String");
		enableFlag.setLength(1);
		tableDefinition.addColumn(enableFlag);

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

		ColumnDefinition updateBy = new ColumnDefinition();
		updateBy.setName("updateBy");
		updateBy.setColumnName("UPDATEBY_");
		updateBy.setJavaType("String");
		updateBy.setLength(50);
		tableDefinition.addColumn(updateBy);

		ColumnDefinition updateTime = new ColumnDefinition();
		updateTime.setName("updateTime");
		updateTime.setColumnName("UPDATETIME_");
		updateTime.setJavaType("Date");
		tableDefinition.addColumn(updateTime);

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

	private DietaryCategoryDomainFactory() {

	}

}
