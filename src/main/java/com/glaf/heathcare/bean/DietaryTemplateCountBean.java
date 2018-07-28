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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.heathcare.domain.CompositionCount;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.DietaryTemplateCount;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryTemplateCountService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.util.DietaryTemplateCountDomainFactory;

public class DietaryTemplateCountBean {

	protected DietaryTemplateService dietaryTemplateService;

	protected DietaryTemplateCountService dietaryTemplateCountService;

	protected DietaryItemService dietaryItemService;

	protected FoodCompositionService foodCompositionService;

	public DietaryTemplateCountBean() {

	}

	public CompositionCount countMulti(List<Long> templateIds) {
		CompositionCount count = new CompositionCount();
		DietaryItemQuery query = new DietaryItemQuery();
		query.templateIds(templateIds);
		List<DietaryItem> items = getDietaryItemService().list(query);
		List<Long> foodIds = new ArrayList<Long>();
		for (DietaryItem item : items) {
			if (!foodIds.contains(item.getFoodId())) {
				foodIds.add(item.getFoodId());
			}
		}

		FoodCompositionQuery query2 = new FoodCompositionQuery();
		query2.setFoodIds(foodIds);
		List<FoodComposition> foods = getFoodCompositionService().list(query2);// 获取食物成分
		Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
		for (FoodComposition food : foods) {
			foodMap.put(food.getId(), food);
		}

		double quantity = 0;
		// double radical = 0;
		double realQuantity = 0;
		for (DietaryItem item : items) {
			FoodComposition food = foodMap.get(item.getFoodId());
			if (food == null) {
				continue;
			}
			/**
			 * 调味品不计入营养成分
			 */
			if (food.getNodeId() == 4419) {
				continue;
			}
			quantity = item.getQuantity();
			// radical = food.getRadical();
			realQuantity = 0;
			// if (radical < 100 && radical > 0) {
			/**
			 * 计算每一份的实际量
			 */
			// realQuantity = quantity * (radical / 100);
			// } else {
			realQuantity = quantity;
			// }

			double factor = realQuantity / 100;// 转换成100g为标准

			if (food.getCarbohydrate() > 0) {
				count.setHeatEnergyCarbohydrate(
						count.getHeatEnergyCarbohydrate() + food.getCarbohydrate() * factor * 4);
			}

			if (food.getFat() > 0) {
				count.setHeatEnergyFat(count.getHeatEnergyFat() + food.getFat() * factor * 9);
			}

			if (food.getProtein() > 0) {
				count.setHeatEnergyProtein(count.getHeatEnergyProtein() + food.getProtein() * factor * 4);
			}

			if (food.getNodeId() == 4409 || food.getNodeId() == 4410 || food.getNodeId() == 4411
					|| food.getNodeId() == 4412 || food.getNodeId() == 4413) {
				count.setProteinAnimal(count.getProteinAnimal() + food.getProtein() * factor);
			}

			if (food.getNodeId() == 4404 || food.getNodeId() == 4409 || food.getNodeId() == 4410
					|| food.getNodeId() == 4411 || food.getNodeId() == 4412 || food.getNodeId() == 4413) {
				count.setProteinAnimalBeans(count.getProteinAnimalBeans() + food.getProtein() * factor);
			}
			// count.setHeatEnergy(count.getHeatEnergy() + food.getHeatEnergy()
			// * factor);// 累加计算
			count.setHeatEnergy(
					count.getHeatEnergyCarbohydrate() + count.getHeatEnergyFat() + count.getHeatEnergyProtein());
			count.setProtein(count.getProtein() + food.getProtein() * factor);// 累加计算
			count.setFat(count.getFat() + food.getFat() * factor);// 累加计算
			count.setCarbohydrate(count.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
			count.setVitaminA(count.getVitaminA() + food.getVitaminA() * factor);// 累加计算
			count.setVitaminB1(count.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
			count.setVitaminB2(count.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
			count.setVitaminB6(count.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
			count.setVitaminB12(count.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
			count.setVitaminC(count.getVitaminC() + food.getVitaminC() * factor);// 累加计算
			count.setCarotene(count.getCarotene() + food.getCarotene() * factor);// 累加计算
			count.setRetinol(count.getRetinol() + food.getRetinol() * factor);// 累加计算
			count.setNicotinicCid(count.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
			count.setCalcium(count.getCalcium() + food.getCalcium() * factor);// 累加计算
			count.setIron(count.getIron() + food.getIron() * factor);// 累加计算
			count.setZinc(count.getZinc() + food.getZinc() * factor);// 累加计算
			count.setIodine(count.getIodine() + food.getIodine() * factor);// 累加计算
			count.setPhosphorus(count.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
			count.setQuantity(count.getQuantity() + item.getQuantity());
		}

		return count;
	}

	public List<CompositionCount> countMultiItems(List<Long> templateIds) {
		List<CompositionCount> countList = new ArrayList<CompositionCount>();
		Map<Long, CompositionCount> countMap = new HashMap<Long, CompositionCount>();
		DietaryItemQuery query = new DietaryItemQuery();
		query.templateIds(templateIds);

		List<DietaryItem> items = getDietaryItemService().list(query);
		List<Long> foodIds = new ArrayList<Long>();
		for (DietaryItem item : items) {
			if (!foodIds.contains(item.getFoodId())) {
				foodIds.add(item.getFoodId());
			}
		}

		FoodCompositionQuery query2 = new FoodCompositionQuery();
		query2.setFoodIds(foodIds);
		List<FoodComposition> foods = getFoodCompositionService().list(query2);// 获取食物成分
		Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
		for (FoodComposition food : foods) {
			foodMap.put(food.getId(), food);
		}

		double quantity = 0;
		// double radical = 0;
		double realQuantity = 0;
		for (DietaryItem item : items) {
			FoodComposition food = foodMap.get(item.getFoodId());
			if (food == null) {
				continue;
			}
			/**
			 * 调味品不计入营养成分
			 */
			if (food.getNodeId() == 4419) {
				continue;
			}
			quantity = item.getQuantity();
			// radical = food.getRadical();
			realQuantity = 0;
			// if (radical < 100 && radical > 0) {
			/**
			 * 计算每一份的实际量
			 */
			// realQuantity = quantity * (radical / 100);
			// } else {
			realQuantity = quantity;
			// }

			double factor = realQuantity / 100;// 转换成100g为标准
			CompositionCount count = countMap.get(food.getNodeId());
			if (count == null) {
				count = new CompositionCount();
				count.setNodeId(food.getNodeId());
				countList.add(count);
			}

			if (food.getCarbohydrate() > 0) {
				count.setHeatEnergyCarbohydrate(
						count.getHeatEnergyCarbohydrate() + food.getCarbohydrate() * factor * 4);
			}

			if (food.getFat() > 0) {
				count.setHeatEnergyFat(count.getHeatEnergyFat() + food.getFat() * factor * 9);
			}

			if (food.getProtein() > 0) {
				count.setHeatEnergyProtein(count.getHeatEnergyProtein() + food.getProtein() * factor * 4);
			}

			if (food.getNodeId() == 4409 || food.getNodeId() == 4410 || food.getNodeId() == 4411
					|| food.getNodeId() == 4412 || food.getNodeId() == 4413) {
				count.setProteinAnimal(count.getProteinAnimal() + food.getProtein() * factor);
			}

			if (food.getNodeId() == 4404 || food.getNodeId() == 4409 || food.getNodeId() == 4410
					|| food.getNodeId() == 4411 || food.getNodeId() == 4412 || food.getNodeId() == 4413) {
				count.setProteinAnimalBeans(count.getProteinAnimalBeans() + food.getProtein() * factor);
			}
			// count.setHeatEnergy(count.getHeatEnergy() + food.getHeatEnergy()
			// * factor);// 累加计算

			count.setHeatEnergy(
					count.getHeatEnergyCarbohydrate() + count.getHeatEnergyFat() + count.getHeatEnergyProtein());
			count.setProtein(count.getProtein() + food.getProtein() * factor);// 累加计算
			count.setFat(count.getFat() + food.getFat() * factor);// 累加计算
			count.setCarbohydrate(count.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
			count.setVitaminA(count.getVitaminA() + food.getVitaminA() * factor);// 累加计算
			count.setVitaminB1(count.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
			count.setVitaminB2(count.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
			count.setVitaminB6(count.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
			count.setVitaminB12(count.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
			count.setVitaminC(count.getVitaminC() + food.getVitaminC() * factor);// 累加计算
			count.setCarotene(count.getCarotene() + food.getCarotene() * factor);// 累加计算
			count.setRetinol(count.getRetinol() + food.getRetinol() * factor);// 累加计算
			count.setNicotinicCid(count.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
			count.setCalcium(count.getCalcium() + food.getCalcium() * factor);// 累加计算
			count.setIron(count.getIron() + food.getIron() * factor);// 累加计算
			count.setZinc(count.getZinc() + food.getZinc() * factor);// 累加计算
			count.setIodine(count.getIodine() + food.getIodine() * factor);// 累加计算
			count.setPhosphorus(count.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
			count.setQuantity(count.getQuantity() + item.getQuantity());
			countMap.put(food.getNodeId(), count);
		}

		return countList;
	}

	public void executeCountAll(LoginContext loginContext, String sysFlag, int suitNo) {
		DietaryTemplateQuery query = new DietaryTemplateQuery();
		if (loginContext.isSystemAdministrator()) {
			query.sysFlag("Y");
		} else {
			if (StringUtils.equals(sysFlag, "Y")) {
				query.sysFlag("Y");
			} else {
				query.sysFlag("N");
				query.tenantId(loginContext.getTenantId());
			}
		}
		query.suitNo(suitNo);

		List<DietaryTemplate> list = getDietaryTemplateService().list(query);
		if (list != null && !list.isEmpty()) {
			Map<Integer, List<Long>> dataMap = new HashMap<Integer, List<Long>>();
			for (DietaryTemplate tpl : list) {
				List<Long> templateIds = dataMap.get(tpl.getDayOfWeek());
				if (templateIds == null) {
					templateIds = new ArrayList<Long>();
				}
				templateIds.add(tpl.getId());
				dataMap.put(tpl.getDayOfWeek(), templateIds);
			}

			String tenantId = loginContext.getTenantId();
			if (StringUtils.isEmpty(tenantId)) {
				tenantId = "SYS";
			}

			List<DietaryTemplateCount> countList = new ArrayList<DietaryTemplateCount>();
			Set<Entry<Integer, List<Long>>> entrySet = dataMap.entrySet();
			for (Entry<Integer, List<Long>> entry : entrySet) {
				Integer key = entry.getKey();
				List<Long> templateIds = entry.getValue();

				CompositionCount count = this.countMulti(templateIds);
				DietaryTemplateCount model = new DietaryTemplateCount();
				DietaryTemplateCountDomainFactory.copyProperties(model, count);
				model.setCreateTime(new Date());
				model.setTenantId(tenantId);
				model.setDayOfWeek(key);
				model.setSuitNo(suitNo);
				model.setType("ALL");
				countList.add(model);
			}

			getDietaryTemplateCountService().saveAll(tenantId, suitNo, "ALL", countList);
		}
	}

	public void executeCountItems(LoginContext loginContext, String sysFlag, int suitNo) {
		DietaryTemplateQuery query = new DietaryTemplateQuery();
		if (loginContext.isSystemAdministrator()) {
			query.sysFlag("Y");
		} else {
			if (StringUtils.equals(sysFlag, "Y")) {
				query.sysFlag("Y");
			} else {
				query.sysFlag("N");
				query.tenantId(loginContext.getTenantId());
			}
		}
		query.suitNo(suitNo);

		List<DietaryTemplate> list = getDietaryTemplateService().list(query);
		if (list != null && !list.isEmpty()) {
			Map<Integer, List<Long>> dataMap = new HashMap<Integer, List<Long>>();
			for (DietaryTemplate tpl : list) {
				List<Long> templateIds = dataMap.get(tpl.getDayOfWeek());
				if (templateIds == null) {
					templateIds = new ArrayList<Long>();
				}
				templateIds.add(tpl.getId());
				dataMap.put(tpl.getDayOfWeek(), templateIds);
			}

			String tenantId = loginContext.getTenantId();
			if (StringUtils.isEmpty(tenantId)) {
				tenantId = "SYS";
			}

			List<DietaryTemplateCount> countList = new ArrayList<DietaryTemplateCount>();
			Set<Entry<Integer, List<Long>>> entrySet = dataMap.entrySet();
			for (Entry<Integer, List<Long>> entry : entrySet) {
				Integer key = entry.getKey();
				List<Long> templateIds = entry.getValue();

				List<CompositionCount> countList2 = this.countMultiItems(templateIds);
				for (CompositionCount cnt : countList2) {
					DietaryTemplateCount model = new DietaryTemplateCount();
					DietaryTemplateCountDomainFactory.copyProperties(model, cnt);
					model.setNodeId(cnt.getNodeId());
					model.setCreateTime(new Date());
					model.setTenantId(tenantId);
					model.setDayOfWeek(key);
					model.setSuitNo(suitNo);
					model.setType("ITEM");
					countList.add(model);
				}
			}

			getDietaryTemplateCountService().saveAll(tenantId, suitNo, "ITEM", countList);
		}
	}

	public DietaryTemplateCountService getDietaryTemplateCountService() {
		if (dietaryTemplateCountService == null) {
			dietaryTemplateCountService = ContextFactory
					.getBean("com.glaf.heathcare.service.dietaryTemplateCountService");
		}
		return dietaryTemplateCountService;
	}

	public DietaryItemService getDietaryItemService() {
		if (dietaryItemService == null) {
			dietaryItemService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryItemService");
		}
		return dietaryItemService;
	}

	public DietaryTemplateService getDietaryTemplateService() {
		if (dietaryTemplateService == null) {
			dietaryTemplateService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryTemplateService");
		}
		return dietaryTemplateService;
	}

	public FoodCompositionService getFoodCompositionService() {
		if (foodCompositionService == null) {
			foodCompositionService = ContextFactory.getBean("com.glaf.heathcare.service.foodCompositionService");
		}
		return foodCompositionService;
	}

}
