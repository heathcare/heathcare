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

package com.glaf.modules.employee.util;

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
public class EmployeeDomainFactory {

	public static final String TABLENAME = "T_EMPLOYEE";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("userId", "USERID_");
		columnMap.put("name", "NAME_");
		columnMap.put("sex", "SEX_");
		columnMap.put("birthday", "BIRTHDAY_");
		columnMap.put("idCardNo", "IDCARDNO_");
		columnMap.put("employeeID", "EMPLOYEEID_");
		columnMap.put("mobile", "MOBILE_");
		columnMap.put("telephone", "TELEPHONE_");
		columnMap.put("nationality", "NATIONALITY_");
		columnMap.put("nation", "NATION_");
		columnMap.put("birthPlace", "BIRTHPLACE_");
		columnMap.put("homeAddress", "HOMEADDRESS_");
		columnMap.put("marryStatus", "MARRYSTATUS_");
		columnMap.put("natureAccount", "NATUREACCOUNT_");
		columnMap.put("natureType", "NATURETYPE_");
		columnMap.put("education", "EDUCATION_");
		columnMap.put("seniority", "SENIORITY_");
		columnMap.put("position", "POSITION_");
		columnMap.put("type", "TYPE_");
		columnMap.put("category", "CATEGORY_");
		columnMap.put("joinDate", "JOINDATE_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");
		columnMap.put("deleteFlag", "DELETEFLAG_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("userId", "String");
		javaTypeMap.put("sex", "String");
		javaTypeMap.put("birthday", "Date");
		javaTypeMap.put("idCardNo", "String");
		javaTypeMap.put("employeeID", "String");
		javaTypeMap.put("mobile", "String");
		javaTypeMap.put("telephone", "String");
		javaTypeMap.put("nationality", "String");
		javaTypeMap.put("nation", "String");
		javaTypeMap.put("birthPlace", "String");
		javaTypeMap.put("homeAddress", "String");
		javaTypeMap.put("marryStatus", "String");
		javaTypeMap.put("natureAccount", "String");
		javaTypeMap.put("natureType", "String");
		javaTypeMap.put("education", "String");
		javaTypeMap.put("seniority", "String");
		javaTypeMap.put("position", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("category", "String");
		javaTypeMap.put("joinDate", "Date");
		javaTypeMap.put("remark", "String");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("updateBy", "String");
		javaTypeMap.put("updateTime", "Date");
		javaTypeMap.put("deleteFlag", "Integer");
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
		tableDefinition.setName("Employee");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("String");
		idColumn.setLength(50);
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
		name.setLength(100);
		tableDefinition.addColumn(name);

		ColumnDefinition userId = new ColumnDefinition();
		userId.setName("userId");
		userId.setColumnName("USERID_");
		userId.setJavaType("String");
		userId.setLength(50);
		tableDefinition.addColumn(userId);

		ColumnDefinition sex = new ColumnDefinition();
		sex.setName("sex");
		sex.setColumnName("SEX_");
		sex.setJavaType("String");
		sex.setLength(1);
		tableDefinition.addColumn(sex);

		ColumnDefinition birthday = new ColumnDefinition();
		birthday.setName("birthday");
		birthday.setColumnName("BIRTHDAY_");
		birthday.setJavaType("Date");
		tableDefinition.addColumn(birthday);

		ColumnDefinition idCardNo = new ColumnDefinition();
		idCardNo.setName("idCardNo");
		idCardNo.setColumnName("IDCARDNO_");
		idCardNo.setJavaType("String");
		idCardNo.setLength(20);
		tableDefinition.addColumn(idCardNo);

		ColumnDefinition employeeID = new ColumnDefinition();
		employeeID.setName("employeeID");
		employeeID.setColumnName("EMPLOYEEID_");
		employeeID.setJavaType("String");
		employeeID.setLength(20);
		tableDefinition.addColumn(employeeID);

		ColumnDefinition mobile = new ColumnDefinition();
		mobile.setName("mobile");
		mobile.setColumnName("MOBILE_");
		mobile.setJavaType("String");
		mobile.setLength(50);
		tableDefinition.addColumn(mobile);

		ColumnDefinition telephone = new ColumnDefinition();
		telephone.setName("telephone");
		telephone.setColumnName("TELEPHONE_");
		telephone.setJavaType("String");
		telephone.setLength(50);
		tableDefinition.addColumn(telephone);

		ColumnDefinition nationality = new ColumnDefinition();
		nationality.setName("nationality");
		nationality.setColumnName("NATIONALITY_");
		nationality.setJavaType("String");
		nationality.setLength(200);
		tableDefinition.addColumn(nationality);

		ColumnDefinition nation = new ColumnDefinition();
		nation.setName("nation");
		nation.setColumnName("NATION_");
		nation.setJavaType("String");
		nation.setLength(50);
		tableDefinition.addColumn(nation);

		ColumnDefinition birthPlace = new ColumnDefinition();
		birthPlace.setName("birthPlace");
		birthPlace.setColumnName("BIRTHPLACE_");
		birthPlace.setJavaType("String");
		birthPlace.setLength(200);
		tableDefinition.addColumn(birthPlace);

		ColumnDefinition homeAddress = new ColumnDefinition();
		homeAddress.setName("homeAddress");
		homeAddress.setColumnName("HOMEADDRESS_");
		homeAddress.setJavaType("String");
		homeAddress.setLength(250);
		tableDefinition.addColumn(homeAddress);

		ColumnDefinition marryStatus = new ColumnDefinition();
		marryStatus.setName("marryStatus");
		marryStatus.setColumnName("MARRYSTATUS_");
		marryStatus.setJavaType("String");
		marryStatus.setLength(1);
		tableDefinition.addColumn(marryStatus);

		ColumnDefinition natureAccount = new ColumnDefinition();
		natureAccount.setName("natureAccount");
		natureAccount.setColumnName("NATUREACCOUNT_");
		natureAccount.setJavaType("String");
		natureAccount.setLength(200);
		tableDefinition.addColumn(natureAccount);

		ColumnDefinition natureType = new ColumnDefinition();
		natureType.setName("natureType");
		natureType.setColumnName("NATURETYPE_");
		natureType.setJavaType("String");
		natureType.setLength(200);
		tableDefinition.addColumn(natureType);

		ColumnDefinition education = new ColumnDefinition();
		education.setName("education");
		education.setColumnName("EDUCATION_");
		education.setJavaType("String");
		education.setLength(200);
		tableDefinition.addColumn(education);

		ColumnDefinition seniority = new ColumnDefinition();
		seniority.setName("seniority");
		seniority.setColumnName("SENIORITY_");
		seniority.setJavaType("String");
		seniority.setLength(200);
		tableDefinition.addColumn(seniority);

		ColumnDefinition position = new ColumnDefinition();
		position.setName("position");
		position.setColumnName("POSITION_");
		position.setJavaType("String");
		position.setLength(200);
		tableDefinition.addColumn(position);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition category = new ColumnDefinition();
		category.setName("category");
		category.setColumnName("CATEGORY_");
		category.setJavaType("String");
		category.setLength(50);
		tableDefinition.addColumn(category);

		ColumnDefinition joinDate = new ColumnDefinition();
		joinDate.setName("joinDate");
		joinDate.setColumnName("JOINDATE_");
		joinDate.setJavaType("Date");
		tableDefinition.addColumn(joinDate);

		ColumnDefinition remark = new ColumnDefinition();
		remark.setName("remark");
		remark.setColumnName("REMARK_");
		remark.setJavaType("String");
		remark.setLength(4000);
		tableDefinition.addColumn(remark);

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

		ColumnDefinition deleteFlag = new ColumnDefinition();
		deleteFlag.setName("deleteFlag");
		deleteFlag.setColumnName("DELETEFLAG_");
		deleteFlag.setJavaType("Integer");
		tableDefinition.addColumn(deleteFlag);

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

	private EmployeeDomainFactory() {

	}

}
