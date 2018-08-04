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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.modules.attendance.domain.AttendanceCalendar;

/**
 * 
 * JSON工厂类
 *
 */
public class AttendanceCalendarJsonFactory {

	public static AttendanceCalendar jsonToObject(JSONObject jsonObject) {
		AttendanceCalendar model = new AttendanceCalendar();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("status1")) {
			model.setStatus1(jsonObject.getInteger("status1"));
		}
		if (jsonObject.containsKey("status2")) {
			model.setStatus2(jsonObject.getInteger("status2"));
		}
		if (jsonObject.containsKey("status3")) {
			model.setStatus3(jsonObject.getInteger("status3"));
		}
		if (jsonObject.containsKey("status4")) {
			model.setStatus4(jsonObject.getInteger("status4"));
		}
		if (jsonObject.containsKey("status5")) {
			model.setStatus5(jsonObject.getInteger("status5"));
		}
		if (jsonObject.containsKey("status6")) {
			model.setStatus6(jsonObject.getInteger("status6"));
		}
		if (jsonObject.containsKey("status7")) {
			model.setStatus7(jsonObject.getInteger("status7"));
		}
		if (jsonObject.containsKey("status8")) {
			model.setStatus8(jsonObject.getInteger("status8"));
		}
		if (jsonObject.containsKey("status9")) {
			model.setStatus9(jsonObject.getInteger("status9"));
		}
		if (jsonObject.containsKey("status10")) {
			model.setStatus10(jsonObject.getInteger("status10"));
		}
		if (jsonObject.containsKey("status11")) {
			model.setStatus11(jsonObject.getInteger("status11"));
		}
		if (jsonObject.containsKey("status12")) {
			model.setStatus12(jsonObject.getInteger("status12"));
		}
		if (jsonObject.containsKey("status13")) {
			model.setStatus13(jsonObject.getInteger("status13"));
		}
		if (jsonObject.containsKey("status14")) {
			model.setStatus14(jsonObject.getInteger("status14"));
		}
		if (jsonObject.containsKey("status15")) {
			model.setStatus15(jsonObject.getInteger("status15"));
		}
		if (jsonObject.containsKey("status16")) {
			model.setStatus16(jsonObject.getInteger("status16"));
		}
		if (jsonObject.containsKey("status17")) {
			model.setStatus17(jsonObject.getInteger("status17"));
		}
		if (jsonObject.containsKey("status18")) {
			model.setStatus18(jsonObject.getInteger("status18"));
		}
		if (jsonObject.containsKey("status19")) {
			model.setStatus19(jsonObject.getInteger("status19"));
		}
		if (jsonObject.containsKey("status20")) {
			model.setStatus20(jsonObject.getInteger("status20"));
		}
		if (jsonObject.containsKey("status21")) {
			model.setStatus21(jsonObject.getInteger("status21"));
		}
		if (jsonObject.containsKey("status22")) {
			model.setStatus22(jsonObject.getInteger("status22"));
		}
		if (jsonObject.containsKey("status23")) {
			model.setStatus23(jsonObject.getInteger("status23"));
		}
		if (jsonObject.containsKey("status24")) {
			model.setStatus24(jsonObject.getInteger("status24"));
		}
		if (jsonObject.containsKey("status25")) {
			model.setStatus25(jsonObject.getInteger("status25"));
		}
		if (jsonObject.containsKey("status26")) {
			model.setStatus26(jsonObject.getInteger("status26"));
		}
		if (jsonObject.containsKey("status27")) {
			model.setStatus27(jsonObject.getInteger("status27"));
		}
		if (jsonObject.containsKey("status28")) {
			model.setStatus28(jsonObject.getInteger("status28"));
		}
		if (jsonObject.containsKey("status29")) {
			model.setStatus29(jsonObject.getInteger("status29"));
		}
		if (jsonObject.containsKey("status30")) {
			model.setStatus30(jsonObject.getInteger("status30"));
		}
		if (jsonObject.containsKey("status31")) {
			model.setStatus31(jsonObject.getInteger("status31"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(AttendanceCalendar model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("status1", model.getStatus1());
		jsonObject.put("status2", model.getStatus2());
		jsonObject.put("status3", model.getStatus3());
		jsonObject.put("status4", model.getStatus4());
		jsonObject.put("status5", model.getStatus5());
		jsonObject.put("status6", model.getStatus6());
		jsonObject.put("status7", model.getStatus7());
		jsonObject.put("status8", model.getStatus8());
		jsonObject.put("status9", model.getStatus9());
		jsonObject.put("status10", model.getStatus10());
		jsonObject.put("status11", model.getStatus11());
		jsonObject.put("status12", model.getStatus12());
		jsonObject.put("status13", model.getStatus13());
		jsonObject.put("status14", model.getStatus14());
		jsonObject.put("status15", model.getStatus15());
		jsonObject.put("status16", model.getStatus16());
		jsonObject.put("status17", model.getStatus17());
		jsonObject.put("status18", model.getStatus18());
		jsonObject.put("status19", model.getStatus19());
		jsonObject.put("status20", model.getStatus20());
		jsonObject.put("status21", model.getStatus21());
		jsonObject.put("status22", model.getStatus22());
		jsonObject.put("status23", model.getStatus23());
		jsonObject.put("status24", model.getStatus24());
		jsonObject.put("status25", model.getStatus25());
		jsonObject.put("status26", model.getStatus26());
		jsonObject.put("status27", model.getStatus27());
		jsonObject.put("status28", model.getStatus28());
		jsonObject.put("status29", model.getStatus29());
		jsonObject.put("status30", model.getStatus30());
		jsonObject.put("status31", model.getStatus31());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(AttendanceCalendar model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("status1", model.getStatus1());
		jsonObject.put("status2", model.getStatus2());
		jsonObject.put("status3", model.getStatus3());
		jsonObject.put("status4", model.getStatus4());
		jsonObject.put("status5", model.getStatus5());
		jsonObject.put("status6", model.getStatus6());
		jsonObject.put("status7", model.getStatus7());
		jsonObject.put("status8", model.getStatus8());
		jsonObject.put("status9", model.getStatus9());
		jsonObject.put("status10", model.getStatus10());
		jsonObject.put("status11", model.getStatus11());
		jsonObject.put("status12", model.getStatus12());
		jsonObject.put("status13", model.getStatus13());
		jsonObject.put("status14", model.getStatus14());
		jsonObject.put("status15", model.getStatus15());
		jsonObject.put("status16", model.getStatus16());
		jsonObject.put("status17", model.getStatus17());
		jsonObject.put("status18", model.getStatus18());
		jsonObject.put("status19", model.getStatus19());
		jsonObject.put("status20", model.getStatus20());
		jsonObject.put("status21", model.getStatus21());
		jsonObject.put("status22", model.getStatus22());
		jsonObject.put("status23", model.getStatus23());
		jsonObject.put("status24", model.getStatus24());
		jsonObject.put("status25", model.getStatus25());
		jsonObject.put("status26", model.getStatus26());
		jsonObject.put("status27", model.getStatus27());
		jsonObject.put("status28", model.getStatus28());
		jsonObject.put("status29", model.getStatus29());
		jsonObject.put("status30", model.getStatus30());
		jsonObject.put("status31", model.getStatus31());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<AttendanceCalendar> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (AttendanceCalendar model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<AttendanceCalendar> arrayToList(JSONArray array) {
		java.util.List<AttendanceCalendar> list = new java.util.ArrayList<AttendanceCalendar>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			AttendanceCalendar model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private AttendanceCalendarJsonFactory() {

	}

}
