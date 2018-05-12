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

import com.glaf.heathcare.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class FoodFavoriteJsonFactory {

	public static java.util.List<FoodFavorite> arrayToList(JSONArray array) {
		java.util.List<FoodFavorite> list = new java.util.ArrayList<FoodFavorite>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			FoodFavorite model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static FoodFavorite jsonToObject(JSONObject jsonObject) {
		FoodFavorite model = new FoodFavorite();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("foodId")) {
			model.setFoodId(jsonObject.getLong("foodId"));
		}
		if (jsonObject.containsKey("nodeId")) {
			model.setNodeId(jsonObject.getLong("nodeId"));
		}
		if (jsonObject.containsKey("subNodeId")) {
			model.setSubNodeId(jsonObject.getLong("subNodeId"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<FoodFavorite> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (FoodFavorite model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(FoodFavorite model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("foodId", model.getFoodId());
		jsonObject.put("nodeId", model.getNodeId());
		jsonObject.put("subNodeId", model.getSubNodeId());
		jsonObject.put("sortNo", model.getSortNo());
		return jsonObject;
	}

	public static ObjectNode toObjectNode(FoodFavorite model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("foodId", model.getFoodId());
		jsonObject.put("nodeId", model.getNodeId());
		jsonObject.put("subNodeId", model.getSubNodeId());
		jsonObject.put("sortNo", model.getSortNo());
		return jsonObject;
	}

	private FoodFavoriteJsonFactory() {

	}

}
