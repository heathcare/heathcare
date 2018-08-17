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

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.heathcare.domain.Dishes;
import com.glaf.heathcare.domain.DishesItem;

/**
 * 
 * JSON工厂类
 *
 */
public class DishesJsonFactory {

	public static java.util.List<Dishes> arrayToList(JSONArray array) {
		java.util.List<Dishes> list = new java.util.ArrayList<Dishes>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Dishes model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static Dishes jsonToObject(JSONObject jsonObject) {
		Dishes model = new Dishes();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("nodeId")) {
			model.setNodeId(jsonObject.getLong("nodeId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("namePinyin")) {
			model.setNamePinyin(jsonObject.getString("namePinyin"));
		}
		if (jsonObject.containsKey("description")) {
			model.setDescription(jsonObject.getString("description"));
		}
		if (jsonObject.containsKey("heatEnergy")) {
			model.setHeatEnergy(jsonObject.getDouble("heatEnergy"));
		}
		if (jsonObject.containsKey("protein")) {
			model.setProtein(jsonObject.getDouble("protein"));
		}
		if (jsonObject.containsKey("fat")) {
			model.setFat(jsonObject.getDouble("fat"));
		}
		if (jsonObject.containsKey("carbohydrate")) {
			model.setCarbohydrate(jsonObject.getDouble("carbohydrate"));
		}
		if (jsonObject.containsKey("vitaminA")) {
			model.setVitaminA(jsonObject.getDouble("vitaminA"));
		}
		if (jsonObject.containsKey("vitaminB1")) {
			model.setVitaminB1(jsonObject.getDouble("vitaminB1"));
		}
		if (jsonObject.containsKey("vitaminB2")) {
			model.setVitaminB2(jsonObject.getDouble("vitaminB2"));
		}
		if (jsonObject.containsKey("vitaminB6")) {
			model.setVitaminB6(jsonObject.getDouble("vitaminB6"));
		}
		if (jsonObject.containsKey("vitaminB12")) {
			model.setVitaminB12(jsonObject.getDouble("vitaminB12"));
		}
		if (jsonObject.containsKey("vitaminC")) {
			model.setVitaminC(jsonObject.getDouble("vitaminC"));
		}
		if (jsonObject.containsKey("carotene")) {
			model.setCarotene(jsonObject.getDouble("carotene"));
		}
		if (jsonObject.containsKey("retinol")) {
			model.setRetinol(jsonObject.getDouble("retinol"));
		}
		if (jsonObject.containsKey("nicotinicCid")) {
			model.setNicotinicCid(jsonObject.getDouble("nicotinicCid"));
		}
		if (jsonObject.containsKey("calcium")) {
			model.setCalcium(jsonObject.getDouble("calcium"));
		}
		if (jsonObject.containsKey("iron")) {
			model.setIron(jsonObject.getDouble("iron"));
		}
		if (jsonObject.containsKey("zinc")) {
			model.setZinc(jsonObject.getDouble("zinc"));
		}
		if (jsonObject.containsKey("iodine")) {
			model.setIodine(jsonObject.getDouble("iodine"));
		}
		if (jsonObject.containsKey("phosphorus")) {
			model.setPhosphorus(jsonObject.getDouble("phosphorus"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}
		if (jsonObject.containsKey("sysFlag")) {
			model.setSysFlag(jsonObject.getString("sysFlag"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("verifyFlag")) {
			model.setVerifyFlag(jsonObject.getString("verifyFlag"));
		}
		
		if (jsonObject.getJSONArray("items") != null) {
			JSONArray array = jsonObject.getJSONArray("items");
			List<DishesItem> items = DishesItemJsonFactory.arrayToList(array);
			model.setItems(items);
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<Dishes> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (Dishes model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(Dishes model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("nodeId", model.getNodeId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getNamePinyin() != null) {
			jsonObject.put("namePinyin", model.getNamePinyin());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		jsonObject.put("heatEnergy", model.getHeatEnergy());
		jsonObject.put("protein", model.getProtein());
		jsonObject.put("fat", model.getFat());
		jsonObject.put("carbohydrate", model.getCarbohydrate());
		jsonObject.put("vitaminA", model.getVitaminA());
		jsonObject.put("vitaminB1", model.getVitaminB1());
		jsonObject.put("vitaminB2", model.getVitaminB2());
		jsonObject.put("vitaminB6", model.getVitaminB6());
		jsonObject.put("vitaminB12", model.getVitaminB12());
		jsonObject.put("vitaminC", model.getVitaminC());
		jsonObject.put("carotene", model.getCarotene());
		jsonObject.put("retinol", model.getRetinol());
		jsonObject.put("nicotinicCid", model.getNicotinicCid());
		jsonObject.put("calcium", model.getCalcium());
		jsonObject.put("iron", model.getIron());
		jsonObject.put("zinc", model.getZinc());
		jsonObject.put("iodine", model.getIodine());
		jsonObject.put("phosphorus", model.getPhosphorus());
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getSysFlag() != null) {
			jsonObject.put("sysFlag", model.getSysFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getVerifyFlag() != null) {
			jsonObject.put("verifyFlag", model.getVerifyFlag());
		}
		
		if (model.getItems() != null && !model.getItems().isEmpty()) {
			JSONArray array = DishesItemJsonFactory.listToArray(model.getItems());
			jsonObject.put("items", array);
		}

		return jsonObject;
	}

	public static ObjectNode toObjectNode(Dishes model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("nodeId", model.getNodeId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getNamePinyin() != null) {
			jsonObject.put("namePinyin", model.getNamePinyin());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		jsonObject.put("heatEnergy", model.getHeatEnergy());
		jsonObject.put("protein", model.getProtein());
		jsonObject.put("fat", model.getFat());
		jsonObject.put("carbohydrate", model.getCarbohydrate());
		jsonObject.put("vitaminA", model.getVitaminA());
		jsonObject.put("vitaminB1", model.getVitaminB1());
		jsonObject.put("vitaminB2", model.getVitaminB2());
		jsonObject.put("vitaminB6", model.getVitaminB6());
		jsonObject.put("vitaminB12", model.getVitaminB12());
		jsonObject.put("vitaminC", model.getVitaminC());
		jsonObject.put("carotene", model.getCarotene());
		jsonObject.put("retinol", model.getRetinol());
		jsonObject.put("nicotinicCid", model.getNicotinicCid());
		jsonObject.put("calcium", model.getCalcium());
		jsonObject.put("iron", model.getIron());
		jsonObject.put("zinc", model.getZinc());
		jsonObject.put("iodine", model.getIodine());
		jsonObject.put("phosphorus", model.getPhosphorus());
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getSysFlag() != null) {
			jsonObject.put("sysFlag", model.getSysFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getVerifyFlag() != null) {
			jsonObject.put("verifyFlag", model.getVerifyFlag());
		}
		
		return jsonObject;
	}

	private DishesJsonFactory() {

	}

}
