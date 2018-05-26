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

package com.glaf.modules.supplier.util;

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
public class SupplierDomainFactory {

	public static final String TABLENAME = "SYS_SUPPLIER";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("supplierId", "SUPPLIERID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("name", "NAME_");
		columnMap.put("shortName", "SHORTNAME_");
		columnMap.put("code", "CODE_");
		columnMap.put("material", "MATERIAL_");
		columnMap.put("level", "LEVEL_");
		columnMap.put("province", "PROVINCE_");
		columnMap.put("provinceId", "PROVINCEID_");
		columnMap.put("city", "CITY_");
		columnMap.put("cityId", "CITYID_");
		columnMap.put("area", "AREA_");
		columnMap.put("areaId", "AREAID_");
		columnMap.put("town", "TOWN_");
		columnMap.put("townId", "TOWNID_");
		columnMap.put("principal", "PRINCIPAL_");
		columnMap.put("telephone", "TELEPHONE_");
		columnMap.put("address", "ADDRESS_");
		columnMap.put("property", "PROPERTY_");
		columnMap.put("mail", "MAIL_");
		columnMap.put("legalPerson", "LEGALPERSON_");
		columnMap.put("taxpayerID", "TAXPAYERID_");
		columnMap.put("taxpayerIdentity", "TAXPAYERIDENTITY_");
		columnMap.put("bankOfDeposit", "BANKOFDEPOSIT_");
		columnMap.put("bankAccount", "BANKACCOUNT_");
		columnMap.put("type", "TYPE_");
		columnMap.put("verify", "VERIFY_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("supplierId", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("shortName", "String");
		javaTypeMap.put("code", "String");
		javaTypeMap.put("material", "String");
		javaTypeMap.put("level", "Integer");
		javaTypeMap.put("province", "String");
		javaTypeMap.put("provinceId", "Long");
		javaTypeMap.put("city", "String");
		javaTypeMap.put("cityId", "Long");
		javaTypeMap.put("area", "String");
		javaTypeMap.put("areaId", "Long");
		javaTypeMap.put("town", "String");
		javaTypeMap.put("townId", "Long");
		javaTypeMap.put("principal", "String");
		javaTypeMap.put("telephone", "String");
		javaTypeMap.put("address", "String");
		javaTypeMap.put("property", "String");
		javaTypeMap.put("mail", "String");
		javaTypeMap.put("legalPerson", "String");
		javaTypeMap.put("taxpayerID", "String");
		javaTypeMap.put("taxpayerIdentity", "String");
		javaTypeMap.put("bankOfDeposit", "String");
		javaTypeMap.put("bankAccount", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("verify", "String");
		javaTypeMap.put("remark", "String");
		javaTypeMap.put("locked", "Integer");
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
		tableDefinition.setName("Supplier");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("supplierId");
		idColumn.setColumnName("SUPPLIERID_");
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
		name.setLength(500);
		tableDefinition.addColumn(name);

		ColumnDefinition shortName = new ColumnDefinition();
		shortName.setName("shortName");
		shortName.setColumnName("SHORTNAME_");
		shortName.setJavaType("String");
		shortName.setLength(200);
		tableDefinition.addColumn(shortName);

		ColumnDefinition code = new ColumnDefinition();
		code.setName("code");
		code.setColumnName("CODE_");
		code.setJavaType("String");
		code.setLength(50);
		tableDefinition.addColumn(code);

		ColumnDefinition material = new ColumnDefinition();
		material.setName("material");
		material.setColumnName("MATERIAL_");
		material.setJavaType("String");
		material.setLength(500);
		tableDefinition.addColumn(material);

		ColumnDefinition level = new ColumnDefinition();
		level.setName("level");
		level.setColumnName("LEVEL_");
		level.setJavaType("Integer");
		tableDefinition.addColumn(level);

		ColumnDefinition province = new ColumnDefinition();
		province.setName("province");
		province.setColumnName("PROVINCE_");
		province.setJavaType("String");
		province.setLength(100);
		tableDefinition.addColumn(province);

		ColumnDefinition provinceId = new ColumnDefinition();
		provinceId.setName("provinceId");
		provinceId.setColumnName("PROVINCEID_");
		provinceId.setJavaType("Long");
		tableDefinition.addColumn(provinceId);

		ColumnDefinition city = new ColumnDefinition();
		city.setName("city");
		city.setColumnName("CITY_");
		city.setJavaType("String");
		city.setLength(100);
		tableDefinition.addColumn(city);

		ColumnDefinition cityId = new ColumnDefinition();
		cityId.setName("cityId");
		cityId.setColumnName("CITYID_");
		cityId.setJavaType("Long");
		tableDefinition.addColumn(cityId);

		ColumnDefinition area = new ColumnDefinition();
		area.setName("area");
		area.setColumnName("AREA_");
		area.setJavaType("String");
		area.setLength(100);
		tableDefinition.addColumn(area);

		ColumnDefinition areaId = new ColumnDefinition();
		areaId.setName("areaId");
		areaId.setColumnName("AREAID_");
		areaId.setJavaType("Long");
		tableDefinition.addColumn(areaId);

		ColumnDefinition town = new ColumnDefinition();
		town.setName("town");
		town.setColumnName("TOWN_");
		town.setJavaType("String");
		town.setLength(200);
		tableDefinition.addColumn(town);

		ColumnDefinition townId = new ColumnDefinition();
		townId.setName("townId");
		townId.setColumnName("TOWNID_");
		townId.setJavaType("Long");
		tableDefinition.addColumn(townId);

		ColumnDefinition principal = new ColumnDefinition();
		principal.setName("principal");
		principal.setColumnName("PRINCIPAL_");
		principal.setJavaType("String");
		principal.setLength(250);
		tableDefinition.addColumn(principal);

		ColumnDefinition telephone = new ColumnDefinition();
		telephone.setName("telephone");
		telephone.setColumnName("TELEPHONE_");
		telephone.setJavaType("String");
		telephone.setLength(200);
		tableDefinition.addColumn(telephone);

		ColumnDefinition address = new ColumnDefinition();
		address.setName("address");
		address.setColumnName("ADDRESS_");
		address.setJavaType("String");
		address.setLength(500);
		tableDefinition.addColumn(address);

		ColumnDefinition property = new ColumnDefinition();
		property.setName("property");
		property.setColumnName("PROPERTY_");
		property.setJavaType("String");
		property.setLength(50);
		tableDefinition.addColumn(property);

		ColumnDefinition mail = new ColumnDefinition();
		mail.setName("mail");
		mail.setColumnName("MAIL_");
		mail.setJavaType("String");
		mail.setLength(100);
		tableDefinition.addColumn(mail);

		ColumnDefinition legalPerson = new ColumnDefinition();
		legalPerson.setName("legalPerson");
		legalPerson.setColumnName("LEGALPERSON_");
		legalPerson.setJavaType("String");
		legalPerson.setLength(50);
		tableDefinition.addColumn(legalPerson);

		ColumnDefinition taxpayerID = new ColumnDefinition();
		taxpayerID.setName("taxpayerID");
		taxpayerID.setColumnName("TAXPAYERID_");
		taxpayerID.setJavaType("String");
		taxpayerID.setLength(50);
		tableDefinition.addColumn(taxpayerID);

		ColumnDefinition taxpayerIdentity = new ColumnDefinition();
		taxpayerIdentity.setName("taxpayerIdentity");
		taxpayerIdentity.setColumnName("TAXPAYERIDENTITY_");
		taxpayerIdentity.setJavaType("String");
		taxpayerIdentity.setLength(50);
		tableDefinition.addColumn(taxpayerIdentity);

		ColumnDefinition bankOfDeposit = new ColumnDefinition();
		bankOfDeposit.setName("bankOfDeposit");
		bankOfDeposit.setColumnName("BANKOFDEPOSIT_");
		bankOfDeposit.setJavaType("String");
		bankOfDeposit.setLength(250);
		tableDefinition.addColumn(bankOfDeposit);

		ColumnDefinition bankAccount = new ColumnDefinition();
		bankAccount.setName("bankAccount");
		bankAccount.setColumnName("BANKACCOUNT_");
		bankAccount.setJavaType("String");
		bankAccount.setLength(50);
		tableDefinition.addColumn(bankAccount);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition verify = new ColumnDefinition();
		verify.setName("verify");
		verify.setColumnName("VERIFY_");
		verify.setJavaType("String");
		verify.setLength(1);
		tableDefinition.addColumn(verify);

		ColumnDefinition remark = new ColumnDefinition();
		remark.setName("remark");
		remark.setColumnName("REMARK_");
		remark.setJavaType("String");
		remark.setLength(500);
		tableDefinition.addColumn(remark);

		ColumnDefinition locked = new ColumnDefinition();
		locked.setName("locked");
		locked.setColumnName("LOCKED_");
		locked.setJavaType("Integer");
		tableDefinition.addColumn(locked);

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

	private SupplierDomainFactory() {

	}

}
