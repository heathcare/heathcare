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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.modules.employee.domain.Employee;

/**
 * 
 * JSON工厂类
 *
 */
public class EmployeeJsonFactory {

	public static java.util.List<Employee> arrayToList(JSONArray array) {
		java.util.List<Employee> list = new java.util.ArrayList<Employee>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Employee model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static Employee jsonToObject(JSONObject jsonObject) {
		Employee model = new Employee();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("userId")) {
			model.setUserId(jsonObject.getString("userId"));
		}
		if (jsonObject.containsKey("sex")) {
			model.setSex(jsonObject.getString("sex"));
		}
		if (jsonObject.containsKey("birthday")) {
			model.setBirthday(jsonObject.getDate("birthday"));
		}
		if (jsonObject.containsKey("idCardNo")) {
			model.setIdCardNo(jsonObject.getString("idCardNo"));
		}
		if (jsonObject.containsKey("employeeID")) {
			model.setEmployeeID(jsonObject.getString("employeeID"));
		}
		if (jsonObject.containsKey("mobile")) {
			model.setMobile(jsonObject.getString("mobile"));
		}
		if (jsonObject.containsKey("telephone")) {
			model.setTelephone(jsonObject.getString("telephone"));
		}
		if (jsonObject.containsKey("nationality")) {
			model.setNationality(jsonObject.getString("nationality"));
		}
		if (jsonObject.containsKey("nation")) {
			model.setNation(jsonObject.getString("nation"));
		}
		if (jsonObject.containsKey("birthPlace")) {
			model.setBirthPlace(jsonObject.getString("birthPlace"));
		}
		if (jsonObject.containsKey("homeAddress")) {
			model.setHomeAddress(jsonObject.getString("homeAddress"));
		}
		if (jsonObject.containsKey("marryStatus")) {
			model.setMarryStatus(jsonObject.getString("marryStatus"));
		}
		if (jsonObject.containsKey("natureAccount")) {
			model.setNatureAccount(jsonObject.getString("natureAccount"));
		}
		if (jsonObject.containsKey("natureType")) {
			model.setNatureType(jsonObject.getString("natureType"));
		}
		if (jsonObject.containsKey("education")) {
			model.setEducation(jsonObject.getString("education"));
		}
		if (jsonObject.containsKey("seniority")) {
			model.setSeniority(jsonObject.getString("seniority"));
		}
		if (jsonObject.containsKey("position")) {
			model.setPosition(jsonObject.getString("position"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("category")) {
			model.setCategory(jsonObject.getString("category"));
		}
		if (jsonObject.containsKey("joinDate")) {
			model.setJoinDate(jsonObject.getDate("joinDate"));
		}
		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}
		if (jsonObject.containsKey("updateBy")) {
			model.setUpdateBy(jsonObject.getString("updateBy"));
		}
		if (jsonObject.containsKey("updateTime")) {
			model.setUpdateTime(jsonObject.getDate("updateTime"));
		}
		if (jsonObject.containsKey("deleteFlag")) {
			model.setDeleteFlag(jsonObject.getInteger("deleteFlag"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<Employee> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (Employee model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(Employee model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getUserId() != null) {
			jsonObject.put("userId", model.getUserId());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getBirthday() != null) {
			jsonObject.put("birthday", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_date", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_datetime", DateUtils.getDateTime(model.getBirthday()));
		}
		if (model.getIdCardNo() != null) {
			jsonObject.put("idCardNo", model.getIdCardNo());
		}
		if (model.getEmployeeID() != null) {
			jsonObject.put("employeeID", model.getEmployeeID());
		}
		if (model.getMobile() != null) {
			jsonObject.put("mobile", model.getMobile());
		}
		if (model.getTelephone() != null) {
			jsonObject.put("telephone", model.getTelephone());
		}
		if (model.getNationality() != null) {
			jsonObject.put("nationality", model.getNationality());
		}
		if (model.getNation() != null) {
			jsonObject.put("nation", model.getNation());
		}
		if (model.getBirthPlace() != null) {
			jsonObject.put("birthPlace", model.getBirthPlace());
		}
		if (model.getHomeAddress() != null) {
			jsonObject.put("homeAddress", model.getHomeAddress());
		}
		if (model.getMarryStatus() != null) {
			jsonObject.put("marryStatus", model.getMarryStatus());
		}
		if (model.getNatureAccount() != null) {
			jsonObject.put("natureAccount", model.getNatureAccount());
		}
		if (model.getNatureType() != null) {
			jsonObject.put("natureType", model.getNatureType());
		}
		if (model.getEducation() != null) {
			jsonObject.put("education", model.getEducation());
		}
		if (model.getSeniority() != null) {
			jsonObject.put("seniority", model.getSeniority());
		}
		if (model.getPosition() != null) {
			jsonObject.put("position", model.getPosition());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getCategory() != null) {
			jsonObject.put("category", model.getCategory());
		}
		if (model.getJoinDate() != null) {
			jsonObject.put("joinDate", DateUtils.getDate(model.getJoinDate()));
			jsonObject.put("joinDate_date", DateUtils.getDate(model.getJoinDate()));
			jsonObject.put("joinDate_datetime", DateUtils.getDateTime(model.getJoinDate()));
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		return jsonObject;
	}

	public static ObjectNode toObjectNode(Employee model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getUserId() != null) {
			jsonObject.put("userId", model.getUserId());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getBirthday() != null) {
			jsonObject.put("birthday", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_date", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_datetime", DateUtils.getDateTime(model.getBirthday()));
		}
		if (model.getIdCardNo() != null) {
			jsonObject.put("idCardNo", model.getIdCardNo());
		}
		if (model.getEmployeeID() != null) {
			jsonObject.put("employeeID", model.getEmployeeID());
		}
		if (model.getMobile() != null) {
			jsonObject.put("mobile", model.getMobile());
		}
		if (model.getTelephone() != null) {
			jsonObject.put("telephone", model.getTelephone());
		}
		if (model.getNationality() != null) {
			jsonObject.put("nationality", model.getNationality());
		}
		if (model.getNation() != null) {
			jsonObject.put("nation", model.getNation());
		}
		if (model.getBirthPlace() != null) {
			jsonObject.put("birthPlace", model.getBirthPlace());
		}
		if (model.getHomeAddress() != null) {
			jsonObject.put("homeAddress", model.getHomeAddress());
		}
		if (model.getMarryStatus() != null) {
			jsonObject.put("marryStatus", model.getMarryStatus());
		}
		if (model.getNatureAccount() != null) {
			jsonObject.put("natureAccount", model.getNatureAccount());
		}
		if (model.getNatureType() != null) {
			jsonObject.put("natureType", model.getNatureType());
		}
		if (model.getEducation() != null) {
			jsonObject.put("education", model.getEducation());
		}
		if (model.getSeniority() != null) {
			jsonObject.put("seniority", model.getSeniority());
		}
		if (model.getPosition() != null) {
			jsonObject.put("position", model.getPosition());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getCategory() != null) {
			jsonObject.put("category", model.getCategory());
		}
		if (model.getJoinDate() != null) {
			jsonObject.put("joinDate", DateUtils.getDate(model.getJoinDate()));
			jsonObject.put("joinDate_date", DateUtils.getDate(model.getJoinDate()));
			jsonObject.put("joinDate_datetime", DateUtils.getDateTime(model.getJoinDate()));
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		return jsonObject;
	}

	private EmployeeJsonFactory() {

	}

}
