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

import java.util.HashMap;
import java.util.Map;

public class NutritionUtils {

	protected static Map<String, String> nutritionNameMap = new HashMap<String, String>();

	protected static Map<String, String> nutritionUnitMap = new HashMap<String, String>();

	static {
		nutritionNameMap.put("heatEnergy", "热能");
		nutritionNameMap.put("protein", "蛋白质");
		nutritionNameMap.put("fat", "脂肪");
		nutritionNameMap.put("carbohydrate", "碳水化合物");
		nutritionNameMap.put("vitaminA", "V-A");
		nutritionNameMap.put("vitaminB1", "V-B1");
		nutritionNameMap.put("vitaminB2", "V-B2");
		nutritionNameMap.put("vitaminC", "V-C");
		nutritionNameMap.put("carotene", "胡萝卜素");
		nutritionNameMap.put("retinol", "视黄醇");
		nutritionNameMap.put("nicotinicCid", "尼克酸");
		nutritionNameMap.put("calcium", "钙");
		nutritionNameMap.put("iron", "铁");
		nutritionNameMap.put("zinc", "锌");
		nutritionNameMap.put("iodine", "碘");
		nutritionNameMap.put("phosphorus", "磷");

		nutritionUnitMap.put("heatEnergy", "千卡");
		nutritionUnitMap.put("protein", "克");
		nutritionUnitMap.put("fat", "克");
		nutritionUnitMap.put("carbohydrate", "克");
		nutritionUnitMap.put("vitaminA", "μgRE");
		nutritionUnitMap.put("vitaminB1", "毫克");
		nutritionUnitMap.put("vitaminB2", "毫克");
		nutritionUnitMap.put("vitaminC", "毫克");
		nutritionUnitMap.put("carotene", "微克");
		nutritionUnitMap.put("retinol", "微克");
		nutritionUnitMap.put("nicotinicCid", "毫克");
		nutritionUnitMap.put("calcium", "毫克");
		nutritionUnitMap.put("iron", "毫克");
		nutritionUnitMap.put("zinc", "毫克");
		nutritionUnitMap.put("iodine", "毫克");
		nutritionUnitMap.put("phosphorus", "毫克");

	}

	private NutritionUtils() {

	}

	public static Map<String, String> getNutritionNameMap() {
		return nutritionNameMap;
	}

	public static Map<String, String> getNutritionUnitMap() {
		return nutritionUnitMap;
	}

}
