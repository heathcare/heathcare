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

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class MedicalExaminationEvaluateJsonFactory {

	public static java.util.List<MedicalExaminationEvaluate> arrayToList(JSONArray array) {
		java.util.List<MedicalExaminationEvaluate> list = new java.util.ArrayList<MedicalExaminationEvaluate>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			MedicalExaminationEvaluate model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static MedicalExaminationEvaluate jsonToObject(JSONObject jsonObject) {
		MedicalExaminationEvaluate model = new MedicalExaminationEvaluate();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("batchId")) {
			model.setBatchId(jsonObject.getLong("batchId"));
		}
		if (jsonObject.containsKey("checkId")) {
			model.setCheckId(jsonObject.getString("checkId"));
		}
		if (jsonObject.containsKey("jobNo")) {
			model.setJobNo(jsonObject.getString("jobNo"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("gradeId")) {
			model.setGradeId(jsonObject.getString("gradeId"));
		}
		if (jsonObject.containsKey("personId")) {
			model.setPersonId(jsonObject.getString("personId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("sex")) {
			model.setSex(jsonObject.getString("sex"));
		}
		if (jsonObject.containsKey("height")) {
			model.setHeight(jsonObject.getDouble("height"));
		}
		if (jsonObject.containsKey("heightLevel")) {
			model.setHeightLevel(jsonObject.getInteger("heightLevel"));
		}
		if (jsonObject.containsKey("heightEvaluate")) {
			model.setHeightEvaluate(jsonObject.getString("heightEvaluate"));
		}
		if (jsonObject.containsKey("stdWeight")) {
			model.setStdWeight(jsonObject.getDouble("stdWeight"));
		}
		if (jsonObject.containsKey("weightOffsetPercent")) {
			model.setWeightOffsetPercent(jsonObject.getDouble("weightOffsetPercent"));
		}
		if (jsonObject.containsKey("weight")) {
			model.setWeight(jsonObject.getDouble("weight"));
		}
		if (jsonObject.containsKey("weightLevel")) {
			model.setWeightLevel(jsonObject.getInteger("weightLevel"));
		}
		if (jsonObject.containsKey("weightEvaluate")) {
			model.setWeightEvaluate(jsonObject.getString("weightEvaluate"));
		}
		if (jsonObject.containsKey("weightHeightEvaluate")) {
			model.setWeightHeightEvaluate(jsonObject.getString("weightHeightEvaluate"));
		}
		if (jsonObject.containsKey("weightHeightLevel")) {
			model.setWeightHeightLevel(jsonObject.getInteger("weightHeightLevel"));
		}
		if (jsonObject.containsKey("weightHeightPercent")) {
			model.setWeightHeightPercent(jsonObject.getDouble("weightHeightPercent"));
		}
		if (jsonObject.containsKey("bmi")) {
			model.setBmi(jsonObject.getDouble("bmi"));
		}
		if (jsonObject.containsKey("bmiIndex")) {
			model.setBmiIndex(jsonObject.getDouble("bmiIndex"));
		}
		if (jsonObject.containsKey("bmiEvaluate")) {
			model.setBmiEvaluate(jsonObject.getString("bmiEvaluate"));
		}
		if (jsonObject.containsKey("healthEvaluate")) {
			model.setHealthEvaluate(jsonObject.getString("healthEvaluate"));
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
		if (jsonObject.containsKey("ageOfTheMoon")) {
			model.setAgeOfTheMoon(jsonObject.getInteger("ageOfTheMoon"));
		}
		if (jsonObject.containsKey("checkDate")) {
			model.setCheckDate(jsonObject.getDate("checkDate"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<MedicalExaminationEvaluate> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (MedicalExaminationEvaluate model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(MedicalExaminationEvaluate model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("batchId", model.getBatchId());
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
		if (model.getJobNo() != null) {
			jsonObject.put("jobNo", model.getJobNo());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getPersonId() != null) {
			jsonObject.put("personId", model.getPersonId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("heightLevel", model.getHeightLevel());
		if (model.getHeightEvaluate() != null) {
			jsonObject.put("heightEvaluate", model.getHeightEvaluate());
		}
		jsonObject.put("stdWeight", model.getStdWeight());
		jsonObject.put("weightOffsetPercent", model.getWeightOffsetPercent());
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightLevel", model.getWeightLevel());
		if (model.getWeightEvaluate() != null) {
			jsonObject.put("weightEvaluate", model.getWeightEvaluate());
		}
		if (model.getWeightHeightEvaluate() != null) {
			jsonObject.put("weightHeightEvaluate", model.getWeightHeightEvaluate());
		}
		jsonObject.put("weightHeightLevel", model.getWeightHeightLevel());
		jsonObject.put("weightHeightPercent", model.getWeightHeightPercent());
		jsonObject.put("bmi", model.getBmi());
		jsonObject.put("bmiIndex", model.getBmiIndex());
		if (model.getBmiEvaluate() != null) {
			jsonObject.put("bmiEvaluate", model.getBmiEvaluate());
		}
		if (model.getHealthEvaluate() != null) {
			jsonObject.put("healthEvaluate", model.getHealthEvaluate());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(MedicalExaminationEvaluate model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("batchId", model.getBatchId());
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
		if (model.getJobNo() != null) {
			jsonObject.put("jobNo", model.getJobNo());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getPersonId() != null) {
			jsonObject.put("personId", model.getPersonId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("heightLevel", model.getHeightLevel());
		if (model.getHeightEvaluate() != null) {
			jsonObject.put("heightEvaluate", model.getHeightEvaluate());
		}
		jsonObject.put("stdWeight", model.getStdWeight());
		jsonObject.put("weightOffsetPercent", model.getWeightOffsetPercent());
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightLevel", model.getWeightLevel());
		if (model.getWeightEvaluate() != null) {
			jsonObject.put("weightEvaluate", model.getWeightEvaluate());
		}
		if (model.getWeightHeightEvaluate() != null) {
			jsonObject.put("weightHeightEvaluate", model.getWeightHeightEvaluate());
		}
		jsonObject.put("weightHeightLevel", model.getWeightHeightLevel());
		jsonObject.put("weightHeightPercent", model.getWeightHeightPercent());
		jsonObject.put("bmi", model.getBmi());
		jsonObject.put("bmiIndex", model.getBmiIndex());
		if (model.getBmiEvaluate() != null) {
			jsonObject.put("bmiEvaluate", model.getBmiEvaluate());
		}
		if (model.getHealthEvaluate() != null) {
			jsonObject.put("healthEvaluate", model.getHealthEvaluate());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	private MedicalExaminationEvaluateJsonFactory() {

	}

}
