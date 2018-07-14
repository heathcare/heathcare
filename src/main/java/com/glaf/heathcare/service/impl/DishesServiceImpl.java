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

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.*;

import com.glaf.heathcare.mapper.*;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.*;

@Service("com.glaf.heathcare.service.dishesService")
@Transactional(readOnly = true)
public class DishesServiceImpl implements DishesService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DishesMapper dishesMapper;

	protected DishesItemMapper dishesItemMapper;

	protected FoodCompositionService foodCompositionService;

	public DishesServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<Dishes> list) {
		for (Dishes dishes : list) {
			if (dishes.getId() == 0) {
				dishes.setId(idGenerator.nextId("HEALTH_DISHES"));
			}
		}

		int batch_size = 50;
		List<Dishes> rows = new ArrayList<Dishes>(batch_size);

		for (Dishes bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					dishesMapper.bulkInsertDishes_oracle(rows);
				} else {
					dishesMapper.bulkInsertDishes(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				dishesMapper.bulkInsertDishes_oracle(rows);
			} else {
				dishesMapper.bulkInsertDishes(rows);
			}
			rows.clear();
		}
	}

	@Transactional
	public void calculate(Dishes dishes) {
		FoodComposition food = null;
		double realQuantity = 0;
		double quantity = 0;

		dishes.setHeatEnergy(0);
		dishes.setProtein(0);
		dishes.setFat(0);
		dishes.setCarbohydrate(0);
		dishes.setVitaminA(0);
		dishes.setVitaminB1(0);
		dishes.setVitaminB2(0);
		dishes.setVitaminB6(0);
		dishes.setVitaminB12(0);
		dishes.setVitaminC(0);
		dishes.setCarotene(0);
		dishes.setRetinol(0);
		dishes.setNicotinicCid(0);
		dishes.setCalcium(0);
		dishes.setIron(0);
		dishes.setZinc(0);
		dishes.setIodine(0);
		dishes.setPhosphorus(0);

		List<DishesItem> items = dishesItemMapper.getDishesItemsByDishesId(dishes.getId());
		for (DishesItem item : items) {
			food = foodCompositionService.getFoodComposition(item.getFoodId());
			/**
			 * 调味品不计入营养成分
			 */
			if (food.getNodeId() == 4419) {
				continue;
			}
			quantity = item.getQuantity();
			// double radical = food.getRadical();

			// if (radical < 100 && radical > 0) {
			/**
			 * 计算每一份的实际量
			 */
			// realQuantity = quantity * (radical / 100);
			// } else {
			realQuantity = quantity;
			// }
			double factor = realQuantity / 100;// 转换成100g为标准
			dishes.setHeatEnergy(dishes.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
			dishes.setProtein(dishes.getProtein() + food.getProtein() * factor);// 累加计算
			dishes.setFat(dishes.getFat() + food.getFat() * factor);// 累加计算
			dishes.setCarbohydrate(dishes.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
			dishes.setVitaminA(dishes.getVitaminA() + food.getVitaminA() * factor);// 累加计算
			dishes.setVitaminB1(dishes.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
			dishes.setVitaminB2(dishes.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
			dishes.setVitaminB6(dishes.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
			dishes.setVitaminB12(dishes.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
			dishes.setVitaminC(dishes.getVitaminC() + food.getVitaminC() * factor);// 累加计算
			dishes.setCarotene(dishes.getCarotene() + food.getCarotene() * factor);// 累加计算
			dishes.setRetinol(dishes.getRetinol() + food.getRetinol() * factor);// 累加计算
			dishes.setNicotinicCid(dishes.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
			dishes.setCalcium(dishes.getCalcium() + food.getCalcium() * factor);// 累加计算
			dishes.setIron(dishes.getIron() + food.getIron() * factor);// 累加计算
			dishes.setZinc(dishes.getZinc() + food.getZinc() * factor);// 累加计算
			dishes.setIodine(dishes.getIodine() + food.getIodine() * factor);// 累加计算
			dishes.setPhosphorus(dishes.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算

		}
		dishesMapper.updateDishes(dishes);
	}

	@Transactional
	public void calculate(long dishesId) {
		Dishes dishes = this.getDishes(dishesId);
		this.calculate(dishes);
	}

	@Transactional
	public void calculateAll(LoginContext loginContext) {
		DishesQuery query = new DishesQuery();
		if (loginContext.isSystemAdministrator()) {
			query.sysFlag("Y");
			query.createBy(loginContext.getActorId());
		} else {
			query.sysFlag("N");
			query.tenantId(loginContext.getTenantId());
		}
		List<Dishes> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			for (Dishes dishes : list) {
				this.calculate(dishes);
			}
		}
	}

	public int count(DishesQuery query) {
		return dishesMapper.getDishesCount(query);
	}

	@Transactional
	public void deleteById(long dishesId) {
		if (dishesId != 0) {
			dishesItemMapper.deleteDishesItemsByDishesId(dishesId);
			dishesMapper.deleteDishesById(dishesId);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long dishesId : ids) {
				dishesItemMapper.deleteDishesItemsByDishesId(dishesId);
				dishesMapper.deleteDishesById(dishesId);
			}
		}
	}

	public Dishes getDishes(long dishesId) {
		if (dishesId == 0) {
			return null;
		}
		Dishes dishes = dishesMapper.getDishesById(dishesId);
		if (dishes != null) {
			dishes.setItems(dishesItemMapper.getDishesItemsByDishesId(dishesId));
		}
		return dishes;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDishesCountByQueryCriteria(DishesQuery query) {
		return dishesMapper.getDishesCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Dishes> getDishesListByQueryCriteria(int start, int pageSize, DishesQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Dishes> rows = sqlSessionTemplate.selectList("getDishesList", query, rowBounds);
		return rows;
	}

	public List<Dishes> list(DishesQuery query) {
		List<Dishes> list = dishesMapper.getDishesList(query);
		return list;
	}

	@Transactional
	public void save(Dishes dishes) {
		boolean isNew = false;
		if (dishes.getId() == 0) {
			dishes.setId(idGenerator.nextId("HEALTH_DISHES"));
			dishes.setCreateTime(new Date());
			if (dishes.getItems() != null && dishes.getItems().size() > 0) {
				isNew = true;
			} else {
				dishesMapper.insertDishes(dishes);
			}
		} else {
			dishes.setUpdateTime(new Date());
			dishesMapper.updateDishes(dishes);
		}
		if (dishes.getItems() != null && dishes.getItems().size() > 0) {
			if (!isNew) {
				dishesItemMapper.deleteDishesItemsByDishesId(dishes.getId());
			}

			dishes.setHeatEnergy(0);
			dishes.setProtein(0);
			dishes.setFat(0);
			dishes.setCarbohydrate(0);
			dishes.setVitaminA(0);
			dishes.setVitaminB1(0);
			dishes.setVitaminB2(0);
			dishes.setVitaminB6(0);
			dishes.setVitaminB12(0);
			dishes.setVitaminC(0);
			dishes.setCarotene(0);
			dishes.setRetinol(0);
			dishes.setNicotinicCid(0);
			dishes.setCalcium(0);
			dishes.setIron(0);
			dishes.setZinc(0);
			dishes.setIodine(0);
			dishes.setPhosphorus(0);

			FoodComposition food = null;
			double realQuantity = 0;
			double quantity = 0;

			for (DishesItem item : dishes.getItems()) {
				food = foodCompositionService.getFoodComposition(item.getFoodId());
				/**
				 * 调味品不计入营养成分
				 */
				if (food.getNodeId() == 4419) {
					continue;
				}
				quantity = item.getQuantity();
				// double radical = food.getRadical();

				// if (radical < 100 && radical > 0) {
				/**
				 * 计算每一份的实际量
				 */
				// realQuantity = quantity * (radical / 100);
				// } else {
				realQuantity = quantity;
				// }
				double factor = realQuantity / 100;// 转换成100g为标准
				dishes.setHeatEnergy(dishes.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
				dishes.setProtein(dishes.getProtein() + food.getProtein() * factor);// 累加计算
				dishes.setFat(dishes.getFat() + food.getFat() * factor);// 累加计算
				dishes.setCarbohydrate(dishes.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
				dishes.setVitaminA(dishes.getVitaminA() + food.getVitaminA() * factor);// 累加计算
				dishes.setVitaminB1(dishes.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
				dishes.setVitaminB2(dishes.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
				dishes.setVitaminB6(dishes.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
				dishes.setVitaminB12(dishes.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
				dishes.setVitaminC(dishes.getVitaminC() + food.getVitaminC() * factor);// 累加计算
				dishes.setCarotene(dishes.getCarotene() + food.getCarotene() * factor);// 累加计算
				dishes.setRetinol(dishes.getRetinol() + food.getRetinol() * factor);// 累加计算
				dishes.setNicotinicCid(dishes.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
				dishes.setCalcium(dishes.getCalcium() + food.getCalcium() * factor);// 累加计算
				dishes.setIron(dishes.getIron() + food.getIron() * factor);// 累加计算
				dishes.setZinc(dishes.getZinc() + food.getZinc() * factor);// 累加计算
				dishes.setIodine(dishes.getIodine() + food.getIodine() * factor);// 累加计算
				dishes.setPhosphorus(dishes.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
				item.setId(idGenerator.nextId("HEALTH_DISHES_ITEM"));
				item.setDishesId(dishes.getId());
			}
			if (isNew) {
				dishesMapper.insertDishes(dishes);
			}
			dishesItemMapper.bulkInsertDishesItem(dishes.getItems());
		}
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DishesItemMapper")
	public void setDishesItemMapper(DishesItemMapper dishesItemMapper) {
		this.dishesItemMapper = dishesItemMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DishesMapper")
	public void setDishesMapper(DishesMapper dishesMapper) {
		this.dishesMapper = dishesMapper;
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

}
