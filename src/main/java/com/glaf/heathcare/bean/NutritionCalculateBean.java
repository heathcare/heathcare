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

package com.glaf.heathcare.bean;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.glaf.heathcare.util.NutritionUtils;

public class NutritionCalculateBean {

	/**
	 * 平均
	 * 
	 * @param list
	 *            JSON数据列表
	 * @return
	 */
	public JSONObject avg(List<JSONObject> list) {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> nutritionNameMap = NutritionUtils.getNutritionNameMap();
		Set<String> keys = nutritionNameMap.keySet();
		int size = list.size();
		if (size > 0) {
			for (JSONObject json : list) {
				for (String key : keys) {
					Double value = jsonObject.getDouble(key);
					if (value == null) {
						value = 0.0D;
					}
					if (json.getDouble(key) != null) {
						value = value + json.getDoubleValue(key);
					}
					jsonObject.put(key, value);
				}
			}
			for (String key : keys) {
				if (jsonObject.getDouble(key) != null) {
					double t = jsonObject.getDoubleValue(key);
					jsonObject.put(key, t / size);// 平均值
				}
			}
		}
		return jsonObject;
	}

	/**
	 * 以1.0为100%的百分比，小数位是两位，如80%值是0.8，100%值为1.0
	 * 
	 * @param list
	 *            JSON数据列表
	 * @param standard
	 *            标准值
	 * @return
	 */
	public JSONObject percent(List<JSONObject> list, JSONObject standard) {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> nutritionNameMap = NutritionUtils.getNutritionNameMap();
		Set<String> keys = nutritionNameMap.keySet();
		int size = list.size();
		if (size > 0) {
			for (JSONObject json : list) {
				for (String key : keys) {
					Double value = jsonObject.getDouble(key);
					if (value == null) {
						value = 0.0D;
					}
					if (json.getDouble(key) != null) {
						value = value + json.getDoubleValue(key);
					}
					jsonObject.put(key, value);
				}
			}
			for (String key : keys) {
				if (jsonObject.getDouble(key) != null) {
					double t = jsonObject.getDoubleValue(key);
					jsonObject.put(key, t / size);// 平均值
				}
			}
			JSONObject result = new JSONObject();
			for (String key : keys) {
				if (jsonObject.getDouble(key) != null && standard.getDouble(key) != null) {
					double t = jsonObject.getDoubleValue(key);
					double std = standard.getDoubleValue(key);
					double value = t / std;
					value = Math.round(value * 100D) / 100D;
					result.put(key, value);
				}
			}
			jsonObject = result;
		}
		return jsonObject;
	}

	/**
	 * 以100为100%的百分比，小数位是两位，如80.25%值是80.25，100%值为100
	 * 
	 * @param list
	 *            JSON数据列表
	 * @param standard
	 *            标准值
	 * @return
	 */
	public JSONObject percent100(List<JSONObject> list, JSONObject standard) {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> nutritionNameMap = NutritionUtils.getNutritionNameMap();
		Set<String> keys = nutritionNameMap.keySet();
		int size = list.size();
		if (size > 0) {
			for (JSONObject json : list) {
				for (String key : keys) {
					Double value = jsonObject.getDouble(key);
					if (value == null) {
						value = 0.0D;
					}
					if (json.getDouble(key) != null) {
						value = value + json.getDoubleValue(key);
					}
					jsonObject.put(key, value);
				}
			}
			for (String key : keys) {
				if (jsonObject.getDouble(key) != null) {
					double t = jsonObject.getDoubleValue(key);
					jsonObject.put(key, t / size);// 平均值
				}
			}
			JSONObject result = new JSONObject();
			for (String key : keys) {
				if (jsonObject.getDouble(key) != null && standard.getDouble(key) != null) {
					double t = jsonObject.getDoubleValue(key);
					double std = standard.getDoubleValue(key);
					double value = t / std;
					value = Math.round(value * 10000D) / 100D;
					result.put(key, value);
				}
			}
			jsonObject = result;
		}
		return jsonObject;
	}

	/**
	 * 汇总
	 * 
	 * @param list
	 *            JSON数据列表
	 * @return
	 */
	public JSONObject sum(List<JSONObject> list) {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> nutritionNameMap = NutritionUtils.getNutritionNameMap();
		Set<String> keys = nutritionNameMap.keySet();
		for (JSONObject json : list) {
			for (String key : keys) {
				Double value = jsonObject.getDouble(key);
				if (value == null) {
					value = 0.0D;
				}
				if (json.getDouble(key) != null) {
					value = value + json.getDoubleValue(key);
				}
				jsonObject.put(key, value);
			}
		}
		return jsonObject;
	}

}
