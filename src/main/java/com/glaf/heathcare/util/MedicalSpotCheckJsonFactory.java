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
public class MedicalSpotCheckJsonFactory {

	public static MedicalSpotCheck jsonToObject(JSONObject jsonObject) {
		MedicalSpotCheck model = new MedicalSpotCheck();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("sex")) {
			model.setSex(jsonObject.getString("sex"));
		}
		if (jsonObject.containsKey("birthday")) {
			model.setBirthday(jsonObject.getDate("birthday"));
		}
		if (jsonObject.containsKey("nation")) {
			model.setNation(jsonObject.getString("nation"));
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
		if (jsonObject.containsKey("eyesightLeft")) {
			model.setEyesightLeft(jsonObject.getDouble("eyesightLeft"));
		}
		if (jsonObject.containsKey("eyesightRight")) {
			model.setEyesightRight(jsonObject.getDouble("eyesightRight"));
		}
		if (jsonObject.containsKey("hemoglobin")) {
			model.setHemoglobin(jsonObject.getDouble("hemoglobin"));
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
		if (jsonObject.containsKey("ageOfTheMoonString")) {
			model.setAgeOfTheMoonString(jsonObject.getString("ageOfTheMoonString"));
		}
		if (jsonObject.containsKey("province")) {
			model.setProvince(jsonObject.getString("province"));
		}
		if (jsonObject.containsKey("city")) {
			model.setCity(jsonObject.getString("city"));
		}
		if (jsonObject.containsKey("area")) {
			model.setArea(jsonObject.getString("area"));
		}
		if (jsonObject.containsKey("organization")) {
			model.setOrganization(jsonObject.getString("organization"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("checkDate")) {
			model.setCheckDate(jsonObject.getDate("checkDate"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(MedicalSpotCheck model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getBirthday() != null) {
			jsonObject.put("birthday", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_date", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_datetime", DateUtils.getDateTime(model.getBirthday()));
		}
		if (model.getNation() != null) {
			jsonObject.put("nation", model.getNation());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("heightLevel", model.getHeightLevel());
		if (model.getHeightEvaluate() != null) {
			jsonObject.put("heightEvaluate", model.getHeightEvaluate());
		}
		if (model.getHeightEvaluateHtml() != null) {
			jsonObject.put("heightEvaluateHtml", model.getHeightEvaluateHtml());
		}
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightLevel", model.getWeightLevel());
		if (model.getWeightEvaluate() != null) {
			jsonObject.put("weightEvaluate", model.getWeightEvaluate());
		}
		if (model.getWeightEvaluateHtml() != null) {
			jsonObject.put("weightEvaluateHtml", model.getWeightEvaluateHtml());
		}
		if (model.getWeightHeightEvaluate() != null) {
			jsonObject.put("weightHeightEvaluate", model.getWeightHeightEvaluate());
		}
		if (model.getWeightHeightEvaluateHtml() != null) {
			jsonObject.put("weightHeightEvaluateHtml", model.getWeightHeightEvaluateHtml());
		}
		jsonObject.put("weightHeightLevel", model.getWeightHeightLevel());
		jsonObject.put("weightHeightPercent", model.getWeightHeightPercent());
		jsonObject.put("bmi", model.getBmi());
		jsonObject.put("bmiIndex", model.getBmiIndex());
		if (model.getBmiEvaluate() != null) {
			jsonObject.put("bmiEvaluate", model.getBmiEvaluate());
		}
		if (model.getBmiEvaluateHtml() != null) {
			jsonObject.put("bmiEvaluateHtml", model.getBmiEvaluateHtml());
		}
		jsonObject.put("eyesightLeft", model.getEyesightLeft());
		jsonObject.put("eyesightRight", model.getEyesightRight());
		jsonObject.put("hemoglobin", model.getHemoglobin());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getAgeOfTheMoonString() != null) {
			jsonObject.put("ageOfTheMoonString", model.getAgeOfTheMoonString());
		}
		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
		}
		if (model.getArea() != null) {
			jsonObject.put("area", model.getArea());
		}
		if (model.getOrganization() != null) {
			jsonObject.put("organization", model.getOrganization());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
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

	public static ObjectNode toObjectNode(MedicalSpotCheck model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getBirthday() != null) {
			jsonObject.put("birthday", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_date", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_datetime", DateUtils.getDateTime(model.getBirthday()));
		}
		if (model.getNation() != null) {
			jsonObject.put("nation", model.getNation());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("heightLevel", model.getHeightLevel());
		if (model.getHeightEvaluate() != null) {
			jsonObject.put("heightEvaluate", model.getHeightEvaluate());
		}
		if (model.getHeightEvaluateHtml() != null) {
			jsonObject.put("heightEvaluateHtml", model.getHeightEvaluateHtml());
		}
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightLevel", model.getWeightLevel());
		if (model.getWeightEvaluate() != null) {
			jsonObject.put("weightEvaluate", model.getWeightEvaluate());
		}
		if (model.getWeightEvaluateHtml() != null) {
			jsonObject.put("weightEvaluateHtml", model.getWeightEvaluateHtml());
		}
		if (model.getWeightHeightEvaluate() != null) {
			jsonObject.put("weightHeightEvaluate", model.getWeightHeightEvaluate());
		}
		if (model.getWeightHeightEvaluateHtml() != null) {
			jsonObject.put("weightHeightEvaluateHtml", model.getWeightHeightEvaluateHtml());
		}
		jsonObject.put("weightHeightLevel", model.getWeightHeightLevel());
		jsonObject.put("weightHeightPercent", model.getWeightHeightPercent());
		jsonObject.put("bmi", model.getBmi());
		jsonObject.put("bmiIndex", model.getBmiIndex());
		if (model.getBmiEvaluate() != null) {
			jsonObject.put("bmiEvaluate", model.getBmiEvaluate());
		}
		if (model.getBmiEvaluateHtml() != null) {
			jsonObject.put("bmiEvaluateHtml", model.getBmiEvaluateHtml());
		}
		jsonObject.put("eyesightLeft", model.getEyesightLeft());
		jsonObject.put("eyesightRight", model.getEyesightRight());
		jsonObject.put("hemoglobin", model.getHemoglobin());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getAgeOfTheMoonString() != null) {
			jsonObject.put("ageOfTheMoonString", model.getAgeOfTheMoonString());
		}
		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
		}
		if (model.getArea() != null) {
			jsonObject.put("area", model.getArea());
		}
		if (model.getOrganization() != null) {
			jsonObject.put("organization", model.getOrganization());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
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

	public static JSONArray listToArray(java.util.List<MedicalSpotCheck> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (MedicalSpotCheck model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<MedicalSpotCheck> arrayToList(JSONArray array) {
		java.util.List<MedicalSpotCheck> list = new java.util.ArrayList<MedicalSpotCheck>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			MedicalSpotCheck model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private MedicalSpotCheckJsonFactory() {

	}

}
