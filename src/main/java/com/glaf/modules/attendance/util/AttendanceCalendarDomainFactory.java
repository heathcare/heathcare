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

package com.glaf.modules.attendance.util;

import java.util.List;
import java.util.Map;
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
public class AttendanceCalendarDomainFactory {

	public static final String TABLENAME = "T_ATTENDANCE_CALENDAR";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("status1", "STATUS1_");
		columnMap.put("status2", "STATUS2_");
		columnMap.put("status3", "STATUS3_");
		columnMap.put("status4", "STATUS4_");
		columnMap.put("status5", "STATUS5_");
		columnMap.put("status6", "STATUS6_");
		columnMap.put("status7", "STATUS7_");
		columnMap.put("status8", "STATUS8_");
		columnMap.put("status9", "STATUS9_");
		columnMap.put("status10", "STATUS10_");
		columnMap.put("status11", "STATUS11_");
		columnMap.put("status12", "STATUS12_");
		columnMap.put("status13", "STATUS13_");
		columnMap.put("status14", "STATUS14_");
		columnMap.put("status15", "STATUS15_");
		columnMap.put("status16", "STATUS16_");
		columnMap.put("status17", "STATUS17_");
		columnMap.put("status18", "STATUS18_");
		columnMap.put("status19", "STATUS19_");
		columnMap.put("status20", "STATUS20_");
		columnMap.put("status21", "STATUS21_");
		columnMap.put("status22", "STATUS22_");
		columnMap.put("status23", "STATUS23_");
		columnMap.put("status24", "STATUS24_");
		columnMap.put("status25", "STATUS25_");
		columnMap.put("status26", "STATUS26_");
		columnMap.put("status27", "STATUS27_");
		columnMap.put("status28", "STATUS28_");
		columnMap.put("status29", "STATUS29_");
		columnMap.put("status30", "STATUS30_");
		columnMap.put("status31", "STATUS31_");
		columnMap.put("type", "TYPE_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("status1", "Integer");
		javaTypeMap.put("status2", "Integer");
		javaTypeMap.put("status3", "Integer");
		javaTypeMap.put("status4", "Integer");
		javaTypeMap.put("status5", "Integer");
		javaTypeMap.put("status6", "Integer");
		javaTypeMap.put("status7", "Integer");
		javaTypeMap.put("status8", "Integer");
		javaTypeMap.put("status9", "Integer");
		javaTypeMap.put("status10", "Integer");
		javaTypeMap.put("status11", "Integer");
		javaTypeMap.put("status12", "Integer");
		javaTypeMap.put("status13", "Integer");
		javaTypeMap.put("status14", "Integer");
		javaTypeMap.put("status15", "Integer");
		javaTypeMap.put("status16", "Integer");
		javaTypeMap.put("status17", "Integer");
		javaTypeMap.put("status18", "Integer");
		javaTypeMap.put("status19", "Integer");
		javaTypeMap.put("status20", "Integer");
		javaTypeMap.put("status21", "Integer");
		javaTypeMap.put("status22", "Integer");
		javaTypeMap.put("status23", "Integer");
		javaTypeMap.put("status24", "Integer");
		javaTypeMap.put("status25", "Integer");
		javaTypeMap.put("status26", "Integer");
		javaTypeMap.put("status27", "Integer");
		javaTypeMap.put("status28", "Integer");
		javaTypeMap.put("status29", "Integer");
		javaTypeMap.put("status30", "Integer");
		javaTypeMap.put("status31", "Integer");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
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
		tableDefinition.setName("AttendanceCalendar");

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

		ColumnDefinition status1 = new ColumnDefinition();
		status1.setName("status1");
		status1.setColumnName("STATUS1_");
		status1.setJavaType("Integer");
		tableDefinition.addColumn(status1);

		ColumnDefinition status2 = new ColumnDefinition();
		status2.setName("status2");
		status2.setColumnName("STATUS2_");
		status2.setJavaType("Integer");
		tableDefinition.addColumn(status2);

		ColumnDefinition status3 = new ColumnDefinition();
		status3.setName("status3");
		status3.setColumnName("STATUS3_");
		status3.setJavaType("Integer");
		tableDefinition.addColumn(status3);

		ColumnDefinition status4 = new ColumnDefinition();
		status4.setName("status4");
		status4.setColumnName("STATUS4_");
		status4.setJavaType("Integer");
		tableDefinition.addColumn(status4);

		ColumnDefinition status5 = new ColumnDefinition();
		status5.setName("status5");
		status5.setColumnName("STATUS5_");
		status5.setJavaType("Integer");
		tableDefinition.addColumn(status5);

		ColumnDefinition status6 = new ColumnDefinition();
		status6.setName("status6");
		status6.setColumnName("STATUS6_");
		status6.setJavaType("Integer");
		tableDefinition.addColumn(status6);

		ColumnDefinition status7 = new ColumnDefinition();
		status7.setName("status7");
		status7.setColumnName("STATUS7_");
		status7.setJavaType("Integer");
		tableDefinition.addColumn(status7);

		ColumnDefinition status8 = new ColumnDefinition();
		status8.setName("status8");
		status8.setColumnName("STATUS8_");
		status8.setJavaType("Integer");
		tableDefinition.addColumn(status8);

		ColumnDefinition status9 = new ColumnDefinition();
		status9.setName("status9");
		status9.setColumnName("STATUS9_");
		status9.setJavaType("Integer");
		tableDefinition.addColumn(status9);

		ColumnDefinition status10 = new ColumnDefinition();
		status10.setName("status10");
		status10.setColumnName("STATUS10_");
		status10.setJavaType("Integer");
		tableDefinition.addColumn(status10);

		ColumnDefinition status11 = new ColumnDefinition();
		status11.setName("status11");
		status11.setColumnName("STATUS11_");
		status11.setJavaType("Integer");
		tableDefinition.addColumn(status11);

		ColumnDefinition status12 = new ColumnDefinition();
		status12.setName("status12");
		status12.setColumnName("STATUS12_");
		status12.setJavaType("Integer");
		tableDefinition.addColumn(status12);

		ColumnDefinition status13 = new ColumnDefinition();
		status13.setName("status13");
		status13.setColumnName("STATUS13_");
		status13.setJavaType("Integer");
		tableDefinition.addColumn(status13);

		ColumnDefinition status14 = new ColumnDefinition();
		status14.setName("status14");
		status14.setColumnName("STATUS14_");
		status14.setJavaType("Integer");
		tableDefinition.addColumn(status14);

		ColumnDefinition status15 = new ColumnDefinition();
		status15.setName("status15");
		status15.setColumnName("STATUS15_");
		status15.setJavaType("Integer");
		tableDefinition.addColumn(status15);

		ColumnDefinition status16 = new ColumnDefinition();
		status16.setName("status16");
		status16.setColumnName("STATUS16_");
		status16.setJavaType("Integer");
		tableDefinition.addColumn(status16);

		ColumnDefinition status17 = new ColumnDefinition();
		status17.setName("status17");
		status17.setColumnName("STATUS17_");
		status17.setJavaType("Integer");
		tableDefinition.addColumn(status17);

		ColumnDefinition status18 = new ColumnDefinition();
		status18.setName("status18");
		status18.setColumnName("STATUS18_");
		status18.setJavaType("Integer");
		tableDefinition.addColumn(status18);

		ColumnDefinition status19 = new ColumnDefinition();
		status19.setName("status19");
		status19.setColumnName("STATUS19_");
		status19.setJavaType("Integer");
		tableDefinition.addColumn(status19);

		ColumnDefinition status20 = new ColumnDefinition();
		status20.setName("status20");
		status20.setColumnName("STATUS20_");
		status20.setJavaType("Integer");
		tableDefinition.addColumn(status20);

		ColumnDefinition status21 = new ColumnDefinition();
		status21.setName("status21");
		status21.setColumnName("STATUS21_");
		status21.setJavaType("Integer");
		tableDefinition.addColumn(status21);

		ColumnDefinition status22 = new ColumnDefinition();
		status22.setName("status22");
		status22.setColumnName("STATUS22_");
		status22.setJavaType("Integer");
		tableDefinition.addColumn(status22);

		ColumnDefinition status23 = new ColumnDefinition();
		status23.setName("status23");
		status23.setColumnName("STATUS23_");
		status23.setJavaType("Integer");
		tableDefinition.addColumn(status23);

		ColumnDefinition status24 = new ColumnDefinition();
		status24.setName("status24");
		status24.setColumnName("STATUS24_");
		status24.setJavaType("Integer");
		tableDefinition.addColumn(status24);

		ColumnDefinition status25 = new ColumnDefinition();
		status25.setName("status25");
		status25.setColumnName("STATUS25_");
		status25.setJavaType("Integer");
		tableDefinition.addColumn(status25);

		ColumnDefinition status26 = new ColumnDefinition();
		status26.setName("status26");
		status26.setColumnName("STATUS26_");
		status26.setJavaType("Integer");
		tableDefinition.addColumn(status26);

		ColumnDefinition status27 = new ColumnDefinition();
		status27.setName("status27");
		status27.setColumnName("STATUS27_");
		status27.setJavaType("Integer");
		tableDefinition.addColumn(status27);

		ColumnDefinition status28 = new ColumnDefinition();
		status28.setName("status28");
		status28.setColumnName("STATUS28_");
		status28.setJavaType("Integer");
		tableDefinition.addColumn(status28);

		ColumnDefinition status29 = new ColumnDefinition();
		status29.setName("status29");
		status29.setColumnName("STATUS29_");
		status29.setJavaType("Integer");
		tableDefinition.addColumn(status29);

		ColumnDefinition status30 = new ColumnDefinition();
		status30.setName("status30");
		status30.setColumnName("STATUS30_");
		status30.setJavaType("Integer");
		tableDefinition.addColumn(status30);

		ColumnDefinition status31 = new ColumnDefinition();
		status31.setName("status31");
		status31.setColumnName("STATUS31_");
		status31.setJavaType("Integer");
		tableDefinition.addColumn(status31);

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

	private AttendanceCalendarDomainFactory() {

	}

}
