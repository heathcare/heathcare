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
public class MedicalExaminationGradeCountJsonFactory {

	public static MedicalExaminationGradeCount jsonToObject(JSONObject jsonObject) {
		MedicalExaminationGradeCount model = new MedicalExaminationGradeCount();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("checkId")) {
			model.setCheckId(jsonObject.getString("checkId"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("gradeId")) {
			model.setGradeId(jsonObject.getString("gradeId"));
		}
		if (jsonObject.containsKey("female")) {
			model.setFemale(jsonObject.getInteger("female"));
		}
		if (jsonObject.containsKey("male")) {
			model.setMale(jsonObject.getInteger("male"));
		}
		if (jsonObject.containsKey("personCount")) {
			model.setPersonCount(jsonObject.getInteger("personCount"));
		}
		if (jsonObject.containsKey("checkPerson")) {
			model.setCheckPerson(jsonObject.getInteger("checkPerson"));
		}
		if (jsonObject.containsKey("checkPercent")) {
			model.setCheckPercent(jsonObject.getDouble("checkPercent"));
		}
		if (jsonObject.containsKey("growthRatePerson")) {
			model.setGrowthRatePerson(jsonObject.getInteger("growthRatePerson"));
		}
		if (jsonObject.containsKey("growthRatePersonPercent")) {
			model.setGrowthRatePersonPercent(jsonObject.getDouble("growthRatePersonPercent"));
		}
		if (jsonObject.containsKey("weightLow")) {
			model.setWeightLow(jsonObject.getInteger("weightLow"));
		}
		if (jsonObject.containsKey("weightSkinny")) {
			model.setWeightSkinny(jsonObject.getInteger("weightSkinny"));
		}
		if (jsonObject.containsKey("weightRetardation")) {
			model.setWeightRetardation(jsonObject.getInteger("weightRetardation"));
		}
		if (jsonObject.containsKey("weightSevereMalnutrition")) {
			model.setWeightSevereMalnutrition(jsonObject.getInteger("weightSevereMalnutrition"));
		}
		if (jsonObject.containsKey("overWeight")) {
			model.setOverWeight(jsonObject.getInteger("overWeight"));
		}
		if (jsonObject.containsKey("weightObesityLow")) {
			model.setWeightObesityLow(jsonObject.getInteger("weightObesityLow"));
		}
		if (jsonObject.containsKey("weightObesityMid")) {
			model.setWeightObesityMid(jsonObject.getInteger("weightObesityMid"));
		}
		if (jsonObject.containsKey("weightObesityHigh")) {
			model.setWeightObesityHigh(jsonObject.getInteger("weightObesityHigh"));
		}
		if (jsonObject.containsKey("anemiaCheck")) {
			model.setAnemiaCheck(jsonObject.getInteger("anemiaCheck"));
		}
		if (jsonObject.containsKey("anemiaCheckNormal")) {
			model.setAnemiaCheckNormal(jsonObject.getInteger("anemiaCheckNormal"));
		}
		if (jsonObject.containsKey("anemiaCheckNormalPercent")) {
			model.setAnemiaCheckNormalPercent(jsonObject.getDouble("anemiaCheckNormalPercent"));
		}
		if (jsonObject.containsKey("anemiaLow")) {
			model.setAnemiaLow(jsonObject.getInteger("anemiaLow"));
		}
		if (jsonObject.containsKey("anemiaMid")) {
			model.setAnemiaMid(jsonObject.getInteger("anemiaMid"));
		}
		if (jsonObject.containsKey("anemiaHigh")) {
			model.setAnemiaHigh(jsonObject.getInteger("anemiaHigh"));
		}
		if (jsonObject.containsKey("bloodLead")) {
			model.setBloodLead(jsonObject.getInteger("bloodLead"));
		}
		if (jsonObject.containsKey("internalDisease")) {
			model.setInternalDisease(jsonObject.getInteger("internalDisease"));
		}
		if (jsonObject.containsKey("surgicalDisease")) {
			model.setSurgicalDisease(jsonObject.getInteger("surgicalDisease"));
		}
		if (jsonObject.containsKey("saprodontia")) {
			model.setSaprodontia(jsonObject.getInteger("saprodontia"));
		}
		if (jsonObject.containsKey("trachoma")) {
			model.setTrachoma(jsonObject.getInteger("trachoma"));
		}
		if (jsonObject.containsKey("amblyopia")) {
			model.setAmblyopia(jsonObject.getInteger("amblyopia"));
		}
		if (jsonObject.containsKey("eyesightDysfunction")) {
			model.setEyesightDysfunction(jsonObject.getInteger("eyesightDysfunction"));
		}
		if (jsonObject.containsKey("hemoglobin110")) {
			model.setHemoglobin110(jsonObject.getInteger("hemoglobin110"));
		}
		if (jsonObject.containsKey("hemoglobin90")) {
			model.setHemoglobin90(jsonObject.getInteger("hemoglobin90"));
		}
		if (jsonObject.containsKey("hemoglobin60")) {
			model.setHemoglobin60(jsonObject.getInteger("hemoglobin60"));
		}
		if (jsonObject.containsKey("alt")) {
			model.setAlt(jsonObject.getInteger("alt"));
		}
		if (jsonObject.containsKey("hbsab")) {
			model.setHbsab(jsonObject.getInteger("hbsab"));
		}
		if (jsonObject.containsKey("sgpt")) {
			model.setSgpt(jsonObject.getInteger("sgpt"));
		}
		if (jsonObject.containsKey("hvaigm")) {
			model.setHvaigm(jsonObject.getInteger("hvaigm"));
		}
		if (jsonObject.containsKey("checkDate")) {
			model.setCheckDate(jsonObject.getDate("checkDate"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		return model;
	}

	public static JSONObject toJsonObject(MedicalExaminationGradeCount model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		jsonObject.put("female", model.getFemale());
		jsonObject.put("male", model.getMale());
		jsonObject.put("personCount", model.getPersonCount());
		jsonObject.put("checkPerson", model.getCheckPerson());
		jsonObject.put("checkPercent", model.getCheckPercent());
		jsonObject.put("growthRatePerson", model.getGrowthRatePerson());
		jsonObject.put("growthRatePersonPercent", model.getGrowthRatePersonPercent());
		jsonObject.put("weightLow", model.getWeightLow());
		jsonObject.put("weightSkinny", model.getWeightSkinny());
		jsonObject.put("weightRetardation", model.getWeightRetardation());
		jsonObject.put("weightSevereMalnutrition", model.getWeightSevereMalnutrition());
		jsonObject.put("overWeight", model.getOverWeight());
		jsonObject.put("weightObesityLow", model.getWeightObesityLow());
		jsonObject.put("weightObesityMid", model.getWeightObesityMid());
		jsonObject.put("weightObesityHigh", model.getWeightObesityHigh());
		jsonObject.put("anemiaCheck", model.getAnemiaCheck());
		jsonObject.put("anemiaCheckNormal", model.getAnemiaCheckNormal());
		jsonObject.put("anemiaCheckNormalPercent", model.getAnemiaCheckNormalPercent());
		jsonObject.put("anemiaLow", model.getAnemiaLow());
		jsonObject.put("anemiaMid", model.getAnemiaMid());
		jsonObject.put("anemiaHigh", model.getAnemiaHigh());
		jsonObject.put("bloodLead", model.getBloodLead());
		jsonObject.put("internalDisease", model.getInternalDisease());
		jsonObject.put("surgicalDisease", model.getSurgicalDisease());
		jsonObject.put("saprodontia", model.getSaprodontia());
		jsonObject.put("trachoma", model.getTrachoma());
		jsonObject.put("amblyopia", model.getAmblyopia());
		jsonObject.put("eyesightDysfunction", model.getEyesightDysfunction());
		jsonObject.put("hemoglobin110", model.getHemoglobin110());
		jsonObject.put("hemoglobin90", model.getHemoglobin90());
		jsonObject.put("hemoglobin60", model.getHemoglobin60());
		jsonObject.put("alt", model.getAlt());
		jsonObject.put("hbsab", model.getHbsab());
		jsonObject.put("sgpt", model.getSgpt());
		jsonObject.put("hvaigm", model.getHvaigm());
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(MedicalExaminationGradeCount model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		jsonObject.put("female", model.getFemale());
		jsonObject.put("male", model.getMale());
		jsonObject.put("personCount", model.getPersonCount());
		jsonObject.put("checkPerson", model.getCheckPerson());
		jsonObject.put("checkPercent", model.getCheckPercent());
		jsonObject.put("growthRatePerson", model.getGrowthRatePerson());
		jsonObject.put("growthRatePersonPercent", model.getGrowthRatePersonPercent());
		jsonObject.put("weightLow", model.getWeightLow());
		jsonObject.put("weightSkinny", model.getWeightSkinny());
		jsonObject.put("weightRetardation", model.getWeightRetardation());
		jsonObject.put("weightSevereMalnutrition", model.getWeightSevereMalnutrition());
		jsonObject.put("overWeight", model.getOverWeight());
		jsonObject.put("weightObesityLow", model.getWeightObesityLow());
		jsonObject.put("weightObesityMid", model.getWeightObesityMid());
		jsonObject.put("weightObesityHigh", model.getWeightObesityHigh());
		jsonObject.put("anemiaCheck", model.getAnemiaCheck());
		jsonObject.put("anemiaCheckNormal", model.getAnemiaCheckNormal());
		jsonObject.put("anemiaCheckNormalPercent", model.getAnemiaCheckNormalPercent());
		jsonObject.put("anemiaLow", model.getAnemiaLow());
		jsonObject.put("anemiaMid", model.getAnemiaMid());
		jsonObject.put("anemiaHigh", model.getAnemiaHigh());
		jsonObject.put("bloodLead", model.getBloodLead());
		jsonObject.put("internalDisease", model.getInternalDisease());
		jsonObject.put("surgicalDisease", model.getSurgicalDisease());
		jsonObject.put("saprodontia", model.getSaprodontia());
		jsonObject.put("trachoma", model.getTrachoma());
		jsonObject.put("amblyopia", model.getAmblyopia());
		jsonObject.put("eyesightDysfunction", model.getEyesightDysfunction());
		jsonObject.put("hemoglobin110", model.getHemoglobin110());
		jsonObject.put("hemoglobin90", model.getHemoglobin90());
		jsonObject.put("hemoglobin60", model.getHemoglobin60());
		jsonObject.put("alt", model.getAlt());
		jsonObject.put("hbsab", model.getHbsab());
		jsonObject.put("sgpt", model.getSgpt());
		jsonObject.put("hvaigm", model.getHvaigm());
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<MedicalExaminationGradeCount> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (MedicalExaminationGradeCount model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<MedicalExaminationGradeCount> arrayToList(JSONArray array) {
		java.util.List<MedicalExaminationGradeCount> list = new java.util.ArrayList<MedicalExaminationGradeCount>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			MedicalExaminationGradeCount model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private MedicalExaminationGradeCountJsonFactory() {

	}

}
