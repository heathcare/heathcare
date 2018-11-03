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

package com.glaf.heathcare.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.TenantConfigService;

import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.Authentication;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DBUtils;

import com.glaf.heathcare.domain.DietaryCategory;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.Dishes;
import com.glaf.heathcare.domain.DishesItem;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.mapper.DietaryItemMapper;
import com.glaf.heathcare.mapper.DietaryTemplateMapper;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryCategoryService;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.DishesService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.util.DietaryTemplateJsonFactory;

@Service("com.glaf.heathcare.service.dietaryTemplateService")
@Transactional(readOnly = true)
public class DietaryTemplateServiceImpl implements DietaryTemplateService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DietaryItemMapper dietaryItemMapper;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateMapper dietaryTemplateMapper;

	protected DietaryCategoryService dietaryCategoryService;

	protected DishesService dishesService;

	protected DictoryService dictoryService;

	protected FoodCompositionService foodCompositionService;

	protected TenantConfigService tenantConfigService;

	public DietaryTemplateServiceImpl() {

	}

	/**
	 * 增加菜肴到食谱模板
	 * 
	 * @param templateId
	 * @param dishesId
	 */
	@Transactional
	public void addDishes(String tenantId, long templateId, long dishesId) {
		Dishes dishes = dishesService.getDishes(dishesId);
		DietaryTemplate dietaryTemplate = this.getDietaryTemplate(templateId);
		if (dietaryTemplate != null && dishes != null && dishes.getItems() != null && !dishes.getItems().isEmpty()) {
			dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
			dietaryTemplate.setName(dishes.getName());
			dietaryTemplate.setDescription(dishes.getDescription());
			long typeId = 0;
			int fullday = 0;
			if (dietaryTemplate.getItems() != null && !dietaryTemplate.getItems().isEmpty()) {
				typeId = dietaryTemplate.getItems().get(0).getTypeId();
				fullday = dietaryTemplate.getItems().get(0).getFullDay();
			}
			logger.debug("fullday:" + fullday);
			List<DietaryItem> items = new ArrayList<DietaryItem>();
			for (DishesItem item : dishes.getItems()) {
				DietaryItem model = new DietaryItem();
				model.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
				model.setTemplateId(dietaryTemplate.getId());
				model.setCreateBy(Authentication.getAuthenticatedActorId());
				model.setCreateTime(new Date());
				model.setDescription(item.getDescription());
				model.setFoodId(item.getFoodId());
				model.setFoodName(item.getName());
				model.setName(item.getName());
				model.setFullDay(fullday);
				model.setQuantity(item.getQuantity());
				model.setTypeId(typeId);
				model.setUnit("g");
				model.setLastModified(System.currentTimeMillis());
				items.add(model);
				dietaryItemMapper.insertDietaryItem(model);
			}

			List<Long> foodIds = new ArrayList<Long>();
			for (DietaryItem item : items) {
				if (!foodIds.contains(item.getFoodId())) {
					foodIds.add(item.getFoodId());
				}
			}

			FoodCompositionQuery query3 = new FoodCompositionQuery();
			query3.setFoodIds(foodIds);
			List<FoodComposition> foods = foodCompositionService.list(query3);// 获取食物成分
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food);
			}

			dietaryTemplate.setHeatEnergy(0);
			dietaryTemplate.setProtein(0);
			dietaryTemplate.setFat(0);
			dietaryTemplate.setCarbohydrate(0);
			dietaryTemplate.setVitaminA(0);
			dietaryTemplate.setVitaminB1(0);
			dietaryTemplate.setVitaminB2(0);
			dietaryTemplate.setVitaminB6(0);
			dietaryTemplate.setVitaminB12(0);
			dietaryTemplate.setVitaminC(0);
			dietaryTemplate.setCarotene(0);
			dietaryTemplate.setRetinol(0);
			dietaryTemplate.setNicotinicCid(0);
			dietaryTemplate.setCalcium(0);
			dietaryTemplate.setIron(0);
			dietaryTemplate.setZinc(0);
			dietaryTemplate.setIodine(0);
			dietaryTemplate.setPhosphorus(0);

			for (DietaryItem item : items) {
				FoodComposition food = foodMap.get(item.getFoodId());
				/**
				 * 调味品不计入营养成分
				 */
				if (food.getNodeId() == 4419) {
					continue;
				}
				double quantity = item.getQuantity();
				double realQuantity = 0;
				realQuantity = quantity;
				double factor = realQuantity / 100;// 转换成100g为标准
				dietaryTemplate.setHeatEnergy(dietaryTemplate.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
				dietaryTemplate.setProtein(dietaryTemplate.getProtein() + food.getProtein() * factor);// 累加计算
				dietaryTemplate.setFat(dietaryTemplate.getFat() + food.getFat() * factor);// 累加计算
				dietaryTemplate.setCarbohydrate(dietaryTemplate.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
				dietaryTemplate.setVitaminA(dietaryTemplate.getVitaminA() + food.getVitaminA() * factor);// 累加计算
				dietaryTemplate.setVitaminB1(dietaryTemplate.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
				dietaryTemplate.setVitaminB2(dietaryTemplate.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
				dietaryTemplate.setVitaminB6(dietaryTemplate.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
				dietaryTemplate.setVitaminB12(dietaryTemplate.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
				dietaryTemplate.setVitaminC(dietaryTemplate.getVitaminC() + food.getVitaminC() * factor);// 累加计算
				dietaryTemplate.setCarotene(dietaryTemplate.getCarotene() + food.getCarotene() * factor);// 累加计算
				dietaryTemplate.setRetinol(dietaryTemplate.getRetinol() + food.getRetinol() * factor);// 累加计算
				dietaryTemplate.setNicotinicCid(dietaryTemplate.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
				dietaryTemplate.setCalcium(dietaryTemplate.getCalcium() + food.getCalcium() * factor);// 累加计算
				dietaryTemplate.setIron(dietaryTemplate.getIron() + food.getIron() * factor);// 累加计算
				dietaryTemplate.setZinc(dietaryTemplate.getZinc() + food.getZinc() * factor);// 累加计算
				dietaryTemplate.setIodine(dietaryTemplate.getIodine() + food.getIodine() * factor);// 累加计算
				dietaryTemplate.setPhosphorus(dietaryTemplate.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
			}

			dietaryTemplate.setCreateTime(new Date());
			dietaryTemplate.setEnableFlag("Y");
			dietaryTemplate.setTenantId(tenantId);
			dietaryTemplate.setSortNo(dietaryTemplate.getSortNo() + 1);// 向后排
			if (StringUtils.isNotEmpty(tenantId)) {
				dietaryTemplate.setSysFlag("N");
			}
			dietaryTemplateMapper.insertDietaryTemplate(dietaryTemplate);

			CacheFactory.clear("dietary_template");
			CacheFactory.clear("health_di_list2");
			CacheFactory.clear("health_di_list");
			CacheFactory.clear("health_di_count");
		}
	}

	@Transactional
	public void bulkInsert(List<DietaryTemplate> list) {
		CacheFactory.clear("dietary_template");
		for (DietaryTemplate dietaryTemplate : list) {
			if (dietaryTemplate.getId() == 0) {
				dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
			}
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			dietaryTemplateMapper.bulkInsertDietaryTemplate_oracle(list);
		} else {
			dietaryTemplateMapper.bulkInsertDietaryTemplate(list);
		}
	}

	@Transactional
	public void calculate(long templateId) {
		if (templateId > 0) {
			DietaryTemplate dietaryTemplate = this.getDietaryTemplate(templateId);
			List<DietaryItem> items = dietaryItemService.getDietaryItemsByTemplateId(templateId);
			if (items != null && !items.isEmpty()) {
				List<Long> foodIds = new ArrayList<Long>();
				for (DietaryItem item : items) {
					if (!foodIds.contains(item.getFoodId())) {
						foodIds.add(item.getFoodId());
					}
				}

				FoodCompositionQuery query3 = new FoodCompositionQuery();
				query3.setFoodIds(foodIds);
				List<FoodComposition> foods = foodCompositionService.list(query3);// 获取食物成分
				Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
				for (FoodComposition food : foods) {
					foodMap.put(food.getId(), food);
				}

				dietaryTemplate.setHeatEnergy(0);
				dietaryTemplate.setProtein(0);
				dietaryTemplate.setFat(0);
				dietaryTemplate.setCarbohydrate(0);
				dietaryTemplate.setVitaminA(0);
				dietaryTemplate.setVitaminB1(0);
				dietaryTemplate.setVitaminB2(0);
				dietaryTemplate.setVitaminB6(0);
				dietaryTemplate.setVitaminB12(0);
				dietaryTemplate.setVitaminC(0);
				dietaryTemplate.setCarotene(0);
				dietaryTemplate.setRetinol(0);
				dietaryTemplate.setNicotinicCid(0);
				dietaryTemplate.setCalcium(0);
				dietaryTemplate.setIron(0);
				dietaryTemplate.setZinc(0);
				dietaryTemplate.setIodine(0);
				dietaryTemplate.setPhosphorus(0);

				for (DietaryItem item : items) {
					FoodComposition food = foodMap.get(item.getFoodId());
					/**
					 * 调味品不计入营养成分
					 */
					if (food.getNodeId() == 4419) {
						continue;
					}
					double quantity = item.getQuantity();
					double realQuantity = 0;
					realQuantity = quantity;
					double factor = realQuantity / 100;// 转换成100g为标准
					dietaryTemplate.setHeatEnergy(dietaryTemplate.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
					dietaryTemplate.setProtein(dietaryTemplate.getProtein() + food.getProtein() * factor);// 累加计算
					dietaryTemplate.setFat(dietaryTemplate.getFat() + food.getFat() * factor);// 累加计算
					dietaryTemplate
							.setCarbohydrate(dietaryTemplate.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
					dietaryTemplate.setVitaminA(dietaryTemplate.getVitaminA() + food.getVitaminA() * factor);// 累加计算
					dietaryTemplate.setVitaminB1(dietaryTemplate.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
					dietaryTemplate.setVitaminB2(dietaryTemplate.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
					dietaryTemplate.setVitaminB6(dietaryTemplate.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
					dietaryTemplate.setVitaminB12(dietaryTemplate.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
					dietaryTemplate.setVitaminC(dietaryTemplate.getVitaminC() + food.getVitaminC() * factor);// 累加计算
					dietaryTemplate.setCarotene(dietaryTemplate.getCarotene() + food.getCarotene() * factor);// 累加计算
					dietaryTemplate.setRetinol(dietaryTemplate.getRetinol() + food.getRetinol() * factor);// 累加计算
					dietaryTemplate
							.setNicotinicCid(dietaryTemplate.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
					dietaryTemplate.setCalcium(dietaryTemplate.getCalcium() + food.getCalcium() * factor);// 累加计算
					dietaryTemplate.setIron(dietaryTemplate.getIron() + food.getIron() * factor);// 累加计算
					dietaryTemplate.setZinc(dietaryTemplate.getZinc() + food.getZinc() * factor);// 累加计算
					dietaryTemplate.setIodine(dietaryTemplate.getIodine() + food.getIodine() * factor);// 累加计算
					dietaryTemplate.setPhosphorus(dietaryTemplate.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
				}

				CacheFactory.clear("dietary_template");
				CacheFactory.clear("health_di_list2");
				CacheFactory.clear("health_di_list");
				CacheFactory.clear("health_di_count");

				this.save(dietaryTemplate);
			}
		}
	}

	/**
	 * 用菜肴替换模板中的食谱
	 * 
	 * @param templateId
	 * @param dishesId
	 */
	@Transactional
	public void changeDishes(long templateId, long dishesId) {
		Dishes dishes = dishesService.getDishes(dishesId);
		if (dishes != null && dishes.getItems() != null && !dishes.getItems().isEmpty()) {
			DietaryItemQuery query = new DietaryItemQuery();
			query.setTableSuffix("");
			query.templateId(templateId);
			dietaryItemMapper.deleteDietaryItemsByTemplateId(query);

			DietaryTemplate dietaryTemplate = this.getDietaryTemplate(templateId);
			dietaryTemplate.setName(dishes.getName());
			dietaryTemplate.setDescription(dishes.getDescription());

			long typeId = 0;
			if (dietaryTemplate.getItems() != null && !dietaryTemplate.getItems().isEmpty()) {
				typeId = dietaryTemplate.getItems().get(0).getTypeId();
			}
			List<DietaryItem> items = new ArrayList<DietaryItem>();
			for (DishesItem item : dishes.getItems()) {
				DietaryItem model = new DietaryItem();
				model.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
				model.setCreateBy(Authentication.getAuthenticatedActorId());
				model.setCreateTime(new Date());
				model.setDescription(item.getDescription());
				model.setFoodId(item.getFoodId());
				model.setFoodName(item.getName());
				model.setName(item.getName());
				model.setQuantity(item.getQuantity());
				model.setTypeId(typeId);
				model.setTemplateId(templateId);
				model.setTenantId(dietaryTemplate.getTenantId());
				model.setUnit("g");
				model.setTableSuffix("");
				items.add(model);
				dietaryItemMapper.insertDietaryItem(model);
			}
			List<Long> foodIds = new ArrayList<Long>();
			for (DietaryItem item : items) {
				if (!foodIds.contains(item.getFoodId())) {
					foodIds.add(item.getFoodId());
				}
			}

			FoodCompositionQuery query3 = new FoodCompositionQuery();
			query3.setFoodIds(foodIds);
			List<FoodComposition> foods = foodCompositionService.list(query3);// 获取食物成分
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food);
			}

			dietaryTemplate.setHeatEnergy(0);
			dietaryTemplate.setProtein(0);
			dietaryTemplate.setFat(0);
			dietaryTemplate.setCarbohydrate(0);
			dietaryTemplate.setVitaminA(0);
			dietaryTemplate.setVitaminB1(0);
			dietaryTemplate.setVitaminB2(0);
			dietaryTemplate.setVitaminB6(0);
			dietaryTemplate.setVitaminB12(0);
			dietaryTemplate.setVitaminC(0);
			dietaryTemplate.setCarotene(0);
			dietaryTemplate.setRetinol(0);
			dietaryTemplate.setNicotinicCid(0);
			dietaryTemplate.setCalcium(0);
			dietaryTemplate.setIron(0);
			dietaryTemplate.setZinc(0);
			dietaryTemplate.setIodine(0);
			dietaryTemplate.setPhosphorus(0);

			for (DietaryItem item : items) {
				FoodComposition food = foodMap.get(item.getFoodId());
				/**
				 * 调味品不计入营养成分
				 */
				if (food.getNodeId() == 4419) {
					continue;
				}
				double quantity = item.getQuantity();
				double realQuantity = 0;
				realQuantity = quantity;
				double factor = realQuantity / 100;// 转换成100g为标准
				dietaryTemplate.setHeatEnergy(dietaryTemplate.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
				dietaryTemplate.setProtein(dietaryTemplate.getProtein() + food.getProtein() * factor);// 累加计算
				dietaryTemplate.setFat(dietaryTemplate.getFat() + food.getFat() * factor);// 累加计算
				dietaryTemplate.setCarbohydrate(dietaryTemplate.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
				dietaryTemplate.setVitaminA(dietaryTemplate.getVitaminA() + food.getVitaminA() * factor);// 累加计算
				dietaryTemplate.setVitaminB1(dietaryTemplate.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
				dietaryTemplate.setVitaminB2(dietaryTemplate.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
				dietaryTemplate.setVitaminB6(dietaryTemplate.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
				dietaryTemplate.setVitaminB12(dietaryTemplate.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
				dietaryTemplate.setVitaminC(dietaryTemplate.getVitaminC() + food.getVitaminC() * factor);// 累加计算
				dietaryTemplate.setCarotene(dietaryTemplate.getCarotene() + food.getCarotene() * factor);// 累加计算
				dietaryTemplate.setRetinol(dietaryTemplate.getRetinol() + food.getRetinol() * factor);// 累加计算
				dietaryTemplate.setNicotinicCid(dietaryTemplate.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
				dietaryTemplate.setCalcium(dietaryTemplate.getCalcium() + food.getCalcium() * factor);// 累加计算
				dietaryTemplate.setIron(dietaryTemplate.getIron() + food.getIron() * factor);// 累加计算
				dietaryTemplate.setZinc(dietaryTemplate.getZinc() + food.getZinc() * factor);// 累加计算
				dietaryTemplate.setIodine(dietaryTemplate.getIodine() + food.getIodine() * factor);// 累加计算
				dietaryTemplate.setPhosphorus(dietaryTemplate.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
			}

			String cacheKey = "dietary_template_" + "_" + dietaryTemplate.getSuitNo() + "_"
					+ dietaryTemplate.getSysFlag();
			CacheFactory.remove("dietary_template", cacheKey);

			cacheKey = "dietary_template_" + dietaryTemplate.getDayOfWeek() + "_" + dietaryTemplate.getSuitNo() + "_"
					+ dietaryTemplate.getSysFlag();
			CacheFactory.remove("dietary_template", cacheKey);

			CacheFactory.clear("dietary_template");
			CacheFactory.clear("health_di_list2");
			CacheFactory.clear("health_di_list");
			CacheFactory.clear("health_di_count");

			dietaryTemplate.setUpdateTime(new Date());
			dietaryTemplateMapper.updateDietaryTemplate(dietaryTemplate);

			logger.debug("##################" + CacheFactory.getString("dietary_template", cacheKey));
		}
	}

	/**
	 * 复制食谱模板
	 * 
	 * @param loginContext
	 * @param suitNo
	 * @param sysFlag
	 * @param targetSuitNo
	 */
	@Transactional
	public void copyTemplates(LoginContext loginContext, int suitNo, String sysFlag, int targetSuitNo) {
		DietaryTemplateQuery query = new DietaryTemplateQuery();
		query.suitNo(suitNo);
		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag(sysFlag);
		} else {
			if (!loginContext.isSystemAdministrator()) {
				query.tenantId(loginContext.getTenantId());
			}
		}

		List<DietaryTemplate> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			if (!loginContext.isSystemAdministrator()) {
				query.sysFlag("N");
				query.tenantId(loginContext.getTenantId());
			}
			query.suitNo(targetSuitNo);
			int total = this.count(query);
			logger.debug("total->" + total);
			if (total == 0) {

				DietaryCategory category = null;
				if (suitNo < 1000) {
					category = dietaryCategoryService.getSysDietaryCategory(suitNo);
				} else {
					category = dietaryCategoryService.getDietaryCategory(loginContext, suitNo);
				}
				if (category != null) {
					category.setId(0);
					category.setCreateBy(loginContext.getActorId());
					if (!loginContext.isSystemAdministrator()) {
						category.setTenantId(loginContext.getTenantId());
						category.setSysFlag("N");
					}
					category.setSuitNo(targetSuitNo);
					dietaryCategoryService.save(category);
					logger.debug("targetSuitNo:" + targetSuitNo);
				}

				List<Long> templateIds = new ArrayList<Long>();
				for (DietaryTemplate dietaryTemplate : list) {
					templateIds.add(dietaryTemplate.getId());
				}
				DietaryItemQuery query2 = new DietaryItemQuery();
				query2.templateIds(templateIds);
				List<DietaryItem> allItems = dietaryItemService.list(query2);
				if (allItems != null && !allItems.isEmpty()) {
					List<DietaryItem> items = null;

					Map<Long, List<DietaryItem>> dietaryTemplateMap = new HashMap<Long, List<DietaryItem>>();
					for (DietaryItem item : allItems) {
						items = dietaryTemplateMap.get(item.getTemplateId());
						if (items == null) {
							items = new ArrayList<DietaryItem>();
						}
						items.add(item);
						dietaryTemplateMap.put(item.getTemplateId(), items);
					}

					Date now = new Date();
					for (DietaryTemplate dietaryTemplate : list) {
						items = dietaryTemplateMap.get(dietaryTemplate.getId());
						if (items != null && !items.isEmpty()) {
							dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
							dietaryTemplate.setCreateBy(loginContext.getActorId());
							dietaryTemplate.setCreateTime(now);
							dietaryTemplate.setEnableFlag("Y");
							dietaryTemplate.setSuitNo(targetSuitNo);
							dietaryTemplate.setUpdateBy(loginContext.getActorId());
							dietaryTemplate.setUpdateTime(now);

							if (StringUtils.equals(sysFlag, "Y")) {
								dietaryTemplate.setSysFlag(sysFlag);
							}

							if (!loginContext.isSystemAdministrator()) {
								dietaryTemplate.setSysFlag("N");
								dietaryTemplate.setTenantId(loginContext.getTenantId());
							}

							CacheFactory.clear("dietary_template");

							dietaryTemplateMapper.insertDietaryTemplate(dietaryTemplate);

							for (DietaryItem item : items) {
								item.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
								item.setTemplateId(dietaryTemplate.getId());
								item.setCreateBy(loginContext.getActorId());
								item.setCreateTime(now);
								if (!loginContext.isSystemAdministrator()) {
									item.setTenantId(loginContext.getTenantId());
								}
								dietaryItemMapper.insertDietaryItem(item);
							}
						}
					}
				}
			}
		}
	}

	public int count(DietaryTemplateQuery query) {
		return dietaryTemplateMapper.getDietaryTemplateCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			CacheFactory.clear("dietary_template");
			DietaryItemQuery query = new DietaryItemQuery();
			query.templateId(id);
			dietaryItemMapper.deleteDietaryItemsByTemplateId(query);
			dietaryTemplateMapper.deleteDietaryTemplateById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			CacheFactory.clear("dietary_template");
			for (Long id : ids) {
				DietaryItemQuery query = new DietaryItemQuery();
				query.templateId(id);
				dietaryItemMapper.deleteDietaryItemsByTemplateId(query);
				dietaryTemplateMapper.deleteDietaryTemplateById(id);
			}
		}
	}

	public DietaryTemplate getDietaryTemplate(long templateId) {
		if (templateId == 0) {
			return null;
		}
		String cacheKey = "dietary_template_" + templateId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("dietary_template", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					DietaryTemplate model = DietaryTemplateJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}

		DietaryTemplate dietaryTemplate = dietaryTemplateMapper.getDietaryTemplateById(templateId);
		if (dietaryTemplate != null) {
			DietaryItemQuery query = new DietaryItemQuery();
			query.templateId(templateId);
			List<DietaryItem> items = dietaryItemService.getDietaryItemsByTemplateId(templateId);
			dietaryTemplate.setItems(items);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("dietary_template", cacheKey, dietaryTemplate.toJsonObject().toJSONString());
			}
		}
		return dietaryTemplate;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietaryTemplateCountByQueryCriteria(DietaryTemplateQuery query) {
		return dietaryTemplateMapper.getDietaryTemplateCount(query);
	}

	public List<DietaryTemplate> getDietaryTemplates(int dayOfWeek, int suitNo, String sysFlag) {
		String cacheKey = "dietary_template_" + dayOfWeek + "_" + suitNo + "_" + sysFlag;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("dietary_template", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return DietaryTemplateJsonFactory.arrayToList(array);
				} catch (Exception ex) {
				}
			}
		}

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		query.dayOfWeek(dayOfWeek);
		query.suitNo(suitNo);
		query.sysFlag(sysFlag);
		List<DietaryTemplate> list = dietaryTemplateMapper.getDietaryTemplates(query);
		if (list != null && !list.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = DietaryTemplateJsonFactory.listToArray(list);
				CacheFactory.put("dietary_template", cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	public List<DietaryTemplate> getDietaryTemplates(int suitNo, String sysFlag) {
		String cacheKey = "dietary_template_" + "_" + suitNo + "_" + sysFlag;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("dietary_template", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return DietaryTemplateJsonFactory.arrayToList(array);
				} catch (Exception ex) {
				}
			}
		}

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		query.suitNo(suitNo);
		query.sysFlag(sysFlag);
		List<DietaryTemplate> list = dietaryTemplateMapper.getDietaryTemplates(query);
		if (list != null && !list.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = DietaryTemplateJsonFactory.listToArray(list);
				CacheFactory.put("dietary_template", cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<DietaryTemplate> getDietaryTemplatesByQueryCriteria(int start, int pageSize,
			DietaryTemplateQuery query) {
		if (query.getSuitNo() != null && query.getSuitNo() > 0) {
			query.setOrderBy(" E.DAYOFWEEK_ asc, E.TYPEID_ asc, E.SORTNO_ asc ");
		}
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<DietaryTemplate> rows = sqlSessionTemplate.selectList("getDietaryTemplates", query, rowBounds);
		return rows;
	}

	public JSONArray getSuiteData(LoginContext loginContext, String sysFlag, int suitNo, long typeIdX) {
		JSONArray result = new JSONArray();
		Map<String, Long> typeMap = new HashMap<String, Long>();

		if (loginContext.isSystemAdministrator()) {
			Dictory dict = dictoryService.find(typeIdX);
			List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
			if (dicts != null && !dicts.isEmpty()) {
				for (Dictory d : dicts) {
					String code = d.getCode();
					if (code.indexOf("|") > 0) {
						code = code.substring(code.lastIndexOf("|") + 1, code.length());
						typeMap.put(code, d.getId());
					}
				}
			}
		} else {
			TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			if (tenantConfig != null && tenantConfig.getTypeId() > 0) {
				Dictory dict = dictoryService.find(tenantConfig.getTypeId());
				List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
				if (dicts != null && !dicts.isEmpty()) {
					for (Dictory d : dicts) {
						String code = d.getCode();
						if (code.indexOf("|") > 0) {
							code = code.substring(code.lastIndexOf("|") + 1, code.length());
							typeMap.put(code, d.getId());
						}
					}
				}
			}
		}

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		query.suitNo(suitNo);
		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag("Y");
		} else {
			if (!loginContext.isSystemAdministrator()) {
				query.tenantId(loginContext.getTenantId());
				query.sysFlag("N");
			}
		}

		List<DietaryTemplate> list = this.list(query);
		List<Long> dietaryTemplateIds = new ArrayList<Long>();
		Map<Long, DietaryTemplate> dietaryTemplateMap = new HashMap<Long, DietaryTemplate>();
		Map<Integer, List<DietaryTemplate>> dayOfWeekTemplateMap = new HashMap<Integer, List<DietaryTemplate>>();
		for (DietaryTemplate dietaryTemplate : list) {
			if (!dietaryTemplateIds.contains(dietaryTemplate.getId())) {
				dietaryTemplateIds.add(dietaryTemplate.getId());
				dietaryTemplateMap.put(dietaryTemplate.getId(), dietaryTemplate);
			}
			List<DietaryTemplate> rows = dayOfWeekTemplateMap.get(dietaryTemplate.getDayOfWeek());
			if (rows == null) {
				rows = new ArrayList<DietaryTemplate>();
				dayOfWeekTemplateMap.put(dietaryTemplate.getDayOfWeek(), rows);
			}
			rows.add(dietaryTemplate);
		}

		List<Long> templateIds = new ArrayList<Long>();
		for (DietaryTemplate t : list) {
			templateIds.add(t.getId());
		}
		DietaryItemQuery query2 = new DietaryItemQuery();
		query2.templateIds(dietaryTemplateIds);
		List<DietaryItem> items = dietaryItemMapper.getDietaryItems(query2);
		if (items != null && !items.isEmpty()) {
			for (DietaryItem item : items) {
				DietaryTemplate dietaryTemplate = dietaryTemplateMap.get(item.getTemplateId());
				if (dietaryTemplate != null) {
					dietaryTemplate.addItem(item);
				}
			}
		}

		JSONObject jsonObject = null;
		JSONArray array = null;
		Iterator<Entry<Integer, List<DietaryTemplate>>> iterator = dayOfWeekTemplateMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, List<DietaryTemplate>> entry = iterator.next();
			Integer dayOfWeek = entry.getKey();
			List<DietaryTemplate> rows = entry.getValue();
			jsonObject = new JSONObject();
			jsonObject.put("DayOfWeek", dayOfWeek);

			Iterator<Entry<String, Long>> iterator2 = typeMap.entrySet().iterator();
			while (iterator2.hasNext()) {
				Entry<String, Long> entry2 = iterator2.next();
				String key = entry2.getKey();
				Long typeId = entry2.getValue();
				for (DietaryTemplate dietaryTemplate : rows) {
					if (typeId == dietaryTemplate.getTypeId()) {
						array = jsonObject.getJSONArray(key);
						if (array == null) {
							array = new JSONArray();
							jsonObject.put(key, array);
						}
						array.add(DietaryTemplateJsonFactory.toJsonObject(dietaryTemplate));
					}
				}
			}
			result.add(jsonObject);
		}

		return result;
	}

	public List<DietaryTemplate> list(DietaryTemplateQuery query) {
		if (query.getSuitNo() != null && query.getSuitNo() > 0) {
			query.setOrderBy(" E.DAYOFWEEK_ asc, E.TYPEID_ asc, E.SORTNO_ asc ");
		}
		List<DietaryTemplate> list = dietaryTemplateMapper.getDietaryTemplates(query);
		return list;
	}

	@Transactional
	public void save(DietaryTemplate dietaryTemplate) {
		if (dietaryTemplate.getId() == 0) {
			CacheFactory.clear("dietary_template");
			dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
			dietaryTemplate.setCreateTime(new Date());
			dietaryTemplateMapper.insertDietaryTemplate(dietaryTemplate);
		} else {
			CacheFactory.clear("dietary_template");
			dietaryTemplate.setUpdateTime(new Date());
			dietaryTemplateMapper.updateDietaryTemplate(dietaryTemplate);
		}
	}

	@Transactional
	public void saveAll(LoginContext loginContext, JSONArray array, int suitNo, long typeIdX) {
		JSONObject jsonObject = null;
		JSONArray children = null;
		List<DietaryTemplate> list = null;
		int sortNo = 0;
		int dayOfWeek = 0;
		Date now = new Date();

		Map<String, Long> typeMap = new HashMap<String, Long>();

		if (loginContext.isSystemAdministrator()) {
			Dictory dict = dictoryService.find(typeIdX);
			List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
			if (dicts != null && !dicts.isEmpty()) {
				for (Dictory d : dicts) {
					String code = d.getCode();
					if (code.indexOf("|") > 0) {
						code = code.substring(code.lastIndexOf("|") + 1, code.length());
						typeMap.put(code, d.getId());
					}
				}
			}
		} else {
			TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			if (tenantConfig != null && tenantConfig.getTypeId() > 0) {
				Dictory dict = dictoryService.find(tenantConfig.getTypeId());
				List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
				if (dicts != null && !dicts.isEmpty()) {
					for (Dictory d : dicts) {
						String code = d.getCode();
						if (code.indexOf("|") > 0) {
							code = code.substring(code.lastIndexOf("|") + 1, code.length());
							typeMap.put(code, d.getId());
						}
					}
				}
			}
		}

		int size = array.size();
		for (int i = 0; i < size; i++) {
			sortNo = 0;

			jsonObject = array.getJSONObject(i);
			dayOfWeek = jsonObject.getInteger("DayOfWeek");
			DietaryTemplateQuery query = new DietaryTemplateQuery();
			if (loginContext.isSystemAdministrator()) {
				query.sysFlag("Y");
				query.createBy(loginContext.getActorId());
			} else {
				query.tenantId(loginContext.getTenantId());
				query.sysFlag("N");
			}
			query.suitNo(suitNo);

			List<DietaryTemplate> templates = this.list(query);
			List<Long> templateIds = new ArrayList<Long>();
			for (DietaryTemplate t : templates) {
				templateIds.add(t.getId());
			}

			dietaryTemplateMapper.deleteDietaryTemplates(query);

			DietaryItemQuery query2 = new DietaryItemQuery();
			query2.templateIds(templateIds);

			dietaryItemMapper.deleteDietaryTemplates(query2);

			Iterator<Entry<String, Long>> iterator = typeMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Long> entry = iterator.next();
				String key = entry.getKey();
				Long typeId = entry.getValue();
				children = jsonObject.getJSONArray(key);
				if (children != null && !children.isEmpty()) {
					list = DietaryTemplateJsonFactory.arrayToList(children);
					if (list != null && !list.isEmpty()) {
						for (DietaryTemplate dietaryTemplate : list) {
							dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
							dietaryTemplate.setCreateTime(now);
							dietaryTemplate.setCreateBy(loginContext.getActorId());
							dietaryTemplate.setDayOfWeek(dayOfWeek);
							dietaryTemplate.setSuitNo(suitNo);
							dietaryTemplate.setTypeId(typeId);
							dietaryTemplate.setSortNo(++sortNo);

							if (loginContext.isSystemAdministrator()) {
								dietaryTemplate.setSysFlag("Y");
								dietaryTemplate.setCreateBy(loginContext.getActorId());
							} else {
								dietaryTemplate.setTenantId(loginContext.getTenantId());
								dietaryTemplate.setSysFlag("N");
							}

							dietaryTemplateMapper.insertDietaryTemplate(dietaryTemplate);

							if (dietaryTemplate.getItems() != null && !dietaryTemplate.getItems().isEmpty()) {
								for (DietaryItem item : dietaryTemplate.getItems()) {
									item.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
									item.setTemplateId(dietaryTemplate.getId());
									item.setTypeId(typeId);
									item.setLastModified(System.currentTimeMillis());
									item.setCreateTime(now);
									item.setCreateBy(loginContext.getActorId());
									dietaryItemMapper.insertDietaryItem(item);
								}
							}
						}
					}
				}
			}
		}
	}

	@Transactional
	public void saveAs(DietaryTemplate dietaryTemplate) {
		DietaryTemplate model = this.getDietaryTemplate(dietaryTemplate.getId());
		if (model != null) {
			CacheFactory.clear("dietary_template");
			dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
			dietaryTemplate.setCreateTime(new Date());
			dietaryTemplate.setEnableFlag("Y");
			dietaryTemplateMapper.insertDietaryTemplate(dietaryTemplate);

			if (model.getItems() != null && !model.getItems().isEmpty()) {
				for (DietaryItem item : model.getItems()) {
					item.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
					item.setTemplateId(dietaryTemplate.getId());
					item.setTenantId(dietaryTemplate.getTenantId());
					dietaryItemMapper.insertDietaryItem(item);
				}
			}
		}
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryCategoryService")
	public void setDietaryCategoryService(DietaryCategoryService dietaryCategoryService) {
		this.dietaryCategoryService = dietaryCategoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryItemMapper")
	public void setDietaryItemMapper(DietaryItemMapper dietaryItemMapper) {
		this.dietaryItemMapper = dietaryItemMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryItemService")
	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryTemplateMapper")
	public void setDietaryTemplateMapper(DietaryTemplateMapper dietaryTemplateMapper) {
		this.dietaryTemplateMapper = dietaryTemplateMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dishesService")
	public void setDishesService(DishesService dishesService) {
		this.dishesService = dishesService;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

	@Transactional
	public void updateAll(List<DietaryTemplate> list) {
		if (list != null && !list.isEmpty()) {
			CacheFactory.clear("dietary_template");
			for (DietaryTemplate tpl : list) {
				dietaryTemplateMapper.updateDietaryTemplate(tpl);
			}
		}
	}

}
