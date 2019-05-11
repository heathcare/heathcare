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
public class MedicalExaminationCountJsonFactory {

	public static MedicalExaminationCount jsonToObject(JSONObject jsonObject) {
		MedicalExaminationCount model = new MedicalExaminationCount();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("gradeId")) {
			model.setGradeId(jsonObject.getString("gradeId"));
		}
		if (jsonObject.containsKey("gradeName")) {
			model.setGradeName(jsonObject.getString("gradeName"));
		}
		if (jsonObject.containsKey("provinceId")) {
			model.setProvinceId(jsonObject.getLong("provinceId"));
		}
		if (jsonObject.containsKey("cityId")) {
			model.setCityId(jsonObject.getLong("cityId"));
		}
		if (jsonObject.containsKey("areaId")) {
			model.setAreaId(jsonObject.getLong("areaId"));
		}
		if (jsonObject.containsKey("checkId")) {
			model.setCheckId(jsonObject.getString("checkId"));
		}
		if (jsonObject.containsKey("totalPerson")) {
			model.setTotalPerson(jsonObject.getInteger("totalPerson"));
		}
		if (jsonObject.containsKey("totalMale")) {
			model.setTotalMale(jsonObject.getInteger("totalMale"));
		}
		if (jsonObject.containsKey("totalFemale")) {
			model.setTotalFemale(jsonObject.getInteger("totalFemale"));
		}
		if (jsonObject.containsKey("checkPerson")) {
			model.setCheckPerson(jsonObject.getInteger("checkPerson"));
		}
		if (jsonObject.containsKey("checkPercent")) {
			model.setCheckPercent(jsonObject.getDouble("checkPercent"));
		}
		if (jsonObject.containsKey("meanWeightNormal")) {
			model.setMeanWeightNormal(jsonObject.getInteger("meanWeightNormal"));
		}
		if (jsonObject.containsKey("meanWeightNormalPercent")) {
			model.setMeanWeightNormalPercent(jsonObject.getDouble("meanWeightNormalPercent"));
		}
		if (jsonObject.containsKey("meanHeightNormal")) {
			model.setMeanHeightNormal(jsonObject.getInteger("meanHeightNormal"));
		}
		if (jsonObject.containsKey("meanHeightNormalPercent")) {
			model.setMeanHeightNormalPercent(jsonObject.getDouble("meanHeightNormalPercent"));
		}
		if (jsonObject.containsKey("meanWeightLow")) {
			model.setMeanWeightLow(jsonObject.getInteger("meanWeightLow"));
		}
		if (jsonObject.containsKey("meanWeightLowPercent")) {
			model.setMeanWeightLowPercent(jsonObject.getDouble("meanWeightLowPercent"));
		}
		if (jsonObject.containsKey("meanHeightLow")) {
			model.setMeanHeightLow(jsonObject.getInteger("meanHeightLow"));
		}
		if (jsonObject.containsKey("meanHeightLowPercent")) {
			model.setMeanHeightLowPercent(jsonObject.getDouble("meanHeightLowPercent"));
		}
		if (jsonObject.containsKey("meanWeightSkinny")) {
			model.setMeanWeightSkinny(jsonObject.getInteger("meanWeightSkinny"));
		}
		if (jsonObject.containsKey("meanWeightSkinnyPercent")) {
			model.setMeanWeightSkinnyPercent(jsonObject.getDouble("meanWeightSkinnyPercent"));
		}
		if (jsonObject.containsKey("meanOverWeight")) {
			model.setMeanOverWeight(jsonObject.getInteger("meanOverWeight"));
		}
		if (jsonObject.containsKey("meanOverWeightPercent")) {
			model.setMeanOverWeightPercent(jsonObject.getDouble("meanOverWeightPercent"));
		}
		if (jsonObject.containsKey("meanWeightObesity")) {
			model.setMeanWeightObesity(jsonObject.getInteger("meanWeightObesity"));
		}
		if (jsonObject.containsKey("meanWeightObesityPercent")) {
			model.setMeanWeightObesityPercent(jsonObject.getDouble("meanWeightObesityPercent"));
		}
		if (jsonObject.containsKey("prctileWeightHeightNormal")) {
			model.setPrctileWeightHeightNormal(jsonObject.getInteger("prctileWeightHeightNormal"));
		}
		if (jsonObject.containsKey("prctileWeightHeightNormalPercent")) {
			model.setPrctileWeightHeightNormalPercent(jsonObject.getDouble("prctileWeightHeightNormalPercent"));
		}
		if (jsonObject.containsKey("prctileWeightAgeNormal")) {
			model.setPrctileWeightAgeNormal(jsonObject.getInteger("prctileWeightAgeNormal"));
		}
		if (jsonObject.containsKey("prctileWeightAgeNormalPercent")) {
			model.setPrctileWeightAgeNormalPercent(jsonObject.getDouble("prctileWeightAgeNormalPercent"));
		}
		if (jsonObject.containsKey("prctileHeightAgeNormal")) {
			model.setPrctileHeightAgeNormal(jsonObject.getInteger("prctileHeightAgeNormal"));
		}
		if (jsonObject.containsKey("prctileHeightAgeNormalPercent")) {
			model.setPrctileHeightAgeNormalPercent(jsonObject.getDouble("prctileHeightAgeNormalPercent"));
		}
		if (jsonObject.containsKey("prctileHeightAgeLow")) {
			model.setPrctileHeightAgeLow(jsonObject.getInteger("prctileHeightAgeLow"));
		}
		if (jsonObject.containsKey("prctileHeightAgeLowPercent")) {
			model.setPrctileHeightAgeLowPercent(jsonObject.getDouble("prctileHeightAgeLowPercent"));
		}
		if (jsonObject.containsKey("prctileWeightHeightLow")) {
			model.setPrctileWeightHeightLow(jsonObject.getInteger("prctileWeightHeightLow"));
		}
		if (jsonObject.containsKey("prctileWeightHeightLowPercent")) {
			model.setPrctileWeightHeightLowPercent(jsonObject.getDouble("prctileWeightHeightLowPercent"));
		}
		if (jsonObject.containsKey("prctileWeightAgeLow")) {
			model.setPrctileWeightAgeLow(jsonObject.getInteger("prctileWeightAgeLow"));
		}
		if (jsonObject.containsKey("prctileWeightAgeLowPercent")) {
			model.setPrctileWeightAgeLowPercent(jsonObject.getDouble("prctileWeightAgeLowPercent"));
		}
		if (jsonObject.containsKey("prctileOverWeight")) {
			model.setPrctileOverWeight(jsonObject.getInteger("prctileOverWeight"));
		}
		if (jsonObject.containsKey("prctileOverWeightPercent")) {
			model.setPrctileOverWeightPercent(jsonObject.getDouble("prctileOverWeightPercent"));
		}
		if (jsonObject.containsKey("prctileWeightObesity")) {
			model.setPrctileWeightObesity(jsonObject.getInteger("prctileWeightObesity"));
		}
		if (jsonObject.containsKey("prctileWeightObesityPercent")) {
			model.setPrctileWeightObesityPercent(jsonObject.getDouble("prctileWeightObesityPercent"));
		}
		if (jsonObject.containsKey("gradeYear")) {
			model.setGradeYear(jsonObject.getInteger("gradeYear"));
		}
		if (jsonObject.containsKey("targetType")) {
			model.setTargetType(jsonObject.getString("targetType"));
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
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(MedicalExaminationCount model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getGradeName() != null) {
			jsonObject.put("gradeName", model.getGradeName());
		}
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
		jsonObject.put("provinceId", model.getProvinceId());
		jsonObject.put("cityId", model.getCityId());
		jsonObject.put("areaId", model.getAreaId());
		jsonObject.put("totalPerson", model.getTotalPerson());
		jsonObject.put("totalMale", model.getTotalMale());
		jsonObject.put("totalFemale", model.getTotalFemale());
		jsonObject.put("checkPerson", model.getCheckPerson());
		jsonObject.put("checkPercent", model.getCheckPercent());
		jsonObject.put("meanWeightNormal", model.getMeanWeightNormal());
		jsonObject.put("meanWeightNormalPercent", model.getMeanWeightNormalPercent());
		jsonObject.put("meanHeightNormal", model.getMeanHeightNormal());
		jsonObject.put("meanHeightNormalPercent", model.getMeanHeightNormalPercent());
		jsonObject.put("meanWeightLow", model.getMeanWeightLow());
		jsonObject.put("meanWeightLowPercent", model.getMeanWeightLowPercent());
		jsonObject.put("meanHeightLow", model.getMeanHeightLow());
		jsonObject.put("meanHeightLowPercent", model.getMeanHeightLowPercent());
		jsonObject.put("meanWeightSkinny", model.getMeanWeightSkinny());
		jsonObject.put("meanWeightSkinnyPercent", model.getMeanWeightSkinnyPercent());
		jsonObject.put("meanOverWeight", model.getMeanOverWeight());
		jsonObject.put("meanOverWeightPercent", model.getMeanOverWeightPercent());
		jsonObject.put("meanWeightObesity", model.getMeanWeightObesity());
		jsonObject.put("meanWeightObesityPercent", model.getMeanWeightObesityPercent());
		jsonObject.put("prctileWeightHeightNormal", model.getPrctileWeightHeightNormal());
		jsonObject.put("prctileWeightHeightNormalPercent", model.getPrctileWeightHeightNormalPercent());
		jsonObject.put("prctileWeightAgeNormal", model.getPrctileWeightAgeNormal());
		jsonObject.put("prctileWeightAgeNormalPercent", model.getPrctileWeightAgeNormalPercent());
		jsonObject.put("prctileHeightAgeNormal", model.getPrctileHeightAgeNormal());
		jsonObject.put("prctileHeightAgeNormalPercent", model.getPrctileHeightAgeNormalPercent());
		jsonObject.put("prctileHeightAgeLow", model.getPrctileHeightAgeLow());
		jsonObject.put("prctileHeightAgeLowPercent", model.getPrctileHeightAgeLowPercent());
		jsonObject.put("prctileWeightHeightLow", model.getPrctileWeightHeightLow());
		jsonObject.put("prctileWeightHeightLowPercent", model.getPrctileWeightHeightLowPercent());
		jsonObject.put("prctileWeightAgeLow", model.getPrctileWeightAgeLow());
		jsonObject.put("prctileWeightAgeLowPercent", model.getPrctileWeightAgeLowPercent());
		jsonObject.put("prctileOverWeight", model.getPrctileOverWeight());
		jsonObject.put("prctileOverWeightPercent", model.getPrctileOverWeightPercent());
		jsonObject.put("prctileWeightObesity", model.getPrctileWeightObesity());
		jsonObject.put("prctileWeightObesityPercent", model.getPrctileWeightObesityPercent());
		jsonObject.put("gradeYear", model.getGradeYear());
		if (model.getTargetType() != null) {
			jsonObject.put("targetType", model.getTargetType());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(MedicalExaminationCount model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getGradeName() != null) {
			jsonObject.put("gradeName", model.getGradeName());
		}
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
		jsonObject.put("provinceId", model.getProvinceId());
		jsonObject.put("cityId", model.getCityId());
		jsonObject.put("areaId", model.getAreaId());
		jsonObject.put("totalPerson", model.getTotalPerson());
		jsonObject.put("totalMale", model.getTotalMale());
		jsonObject.put("totalFemale", model.getTotalFemale());
		jsonObject.put("checkPerson", model.getCheckPerson());
		jsonObject.put("checkPercent", model.getCheckPercent());
		jsonObject.put("meanWeightNormal", model.getMeanWeightNormal());
		jsonObject.put("meanWeightNormalPercent", model.getMeanWeightNormalPercent());
		jsonObject.put("meanHeightNormal", model.getMeanHeightNormal());
		jsonObject.put("meanHeightNormalPercent", model.getMeanHeightNormalPercent());
		jsonObject.put("meanWeightLow", model.getMeanWeightLow());
		jsonObject.put("meanWeightLowPercent", model.getMeanWeightLowPercent());
		jsonObject.put("meanHeightLow", model.getMeanHeightLow());
		jsonObject.put("meanHeightLowPercent", model.getMeanHeightLowPercent());
		jsonObject.put("meanWeightSkinny", model.getMeanWeightSkinny());
		jsonObject.put("meanWeightSkinnyPercent", model.getMeanWeightSkinnyPercent());
		jsonObject.put("meanOverWeight", model.getMeanOverWeight());
		jsonObject.put("meanOverWeightPercent", model.getMeanOverWeightPercent());
		jsonObject.put("meanWeightObesity", model.getMeanWeightObesity());
		jsonObject.put("meanWeightObesityPercent", model.getMeanWeightObesityPercent());
		jsonObject.put("prctileWeightHeightNormal", model.getPrctileWeightHeightNormal());
		jsonObject.put("prctileWeightHeightNormalPercent", model.getPrctileWeightHeightNormalPercent());
		jsonObject.put("prctileWeightAgeNormal", model.getPrctileWeightAgeNormal());
		jsonObject.put("prctileWeightAgeNormalPercent", model.getPrctileWeightAgeNormalPercent());
		jsonObject.put("prctileHeightAgeNormal", model.getPrctileHeightAgeNormal());
		jsonObject.put("prctileHeightAgeNormalPercent", model.getPrctileHeightAgeNormalPercent());
		jsonObject.put("prctileHeightAgeLow", model.getPrctileHeightAgeLow());
		jsonObject.put("prctileHeightAgeLowPercent", model.getPrctileHeightAgeLowPercent());
		jsonObject.put("prctileWeightHeightLow", model.getPrctileWeightHeightLow());
		jsonObject.put("prctileWeightHeightLowPercent", model.getPrctileWeightHeightLowPercent());
		jsonObject.put("prctileWeightAgeLow", model.getPrctileWeightAgeLow());
		jsonObject.put("prctileWeightAgeLowPercent", model.getPrctileWeightAgeLowPercent());
		jsonObject.put("prctileOverWeight", model.getPrctileOverWeight());
		jsonObject.put("prctileOverWeightPercent", model.getPrctileOverWeightPercent());
		jsonObject.put("prctileWeightObesity", model.getPrctileWeightObesity());
		jsonObject.put("prctileWeightObesityPercent", model.getPrctileWeightObesityPercent());
		jsonObject.put("gradeYear", model.getGradeYear());
		if (model.getTargetType() != null) {
			jsonObject.put("targetType", model.getTargetType());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<MedicalExaminationCount> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (MedicalExaminationCount model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<MedicalExaminationCount> arrayToList(JSONArray array) {
		java.util.List<MedicalExaminationCount> list = new java.util.ArrayList<MedicalExaminationCount>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			MedicalExaminationCount model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private MedicalExaminationCountJsonFactory() {

	}

}
