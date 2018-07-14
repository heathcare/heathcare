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
import com.glaf.core.util.*;

import com.glaf.heathcare.mapper.*;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.*;

@Service("com.glaf.heathcare.service.dishesItemService")
@Transactional(readOnly = true)
public class DishesItemServiceImpl implements DishesItemService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DishesItemMapper dishesItemMapper;

	public DishesItemServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<DishesItem> list) {
		for (DishesItem dishesItem : list) {
			if (dishesItem.getId() == 0) {
				dishesItem.setId(idGenerator.nextId("HEALTH_DISHES_ITEM"));
			}
		}

		int batch_size = 50;
		List<DishesItem> rows = new ArrayList<DishesItem>(batch_size);

		for (DishesItem bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					dishesItemMapper.bulkInsertDishesItem_oracle(rows);
				} else {
					dishesItemMapper.bulkInsertDishesItem(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				dishesItemMapper.bulkInsertDishesItem_oracle(rows);
			} else {
				dishesItemMapper.bulkInsertDishesItem(rows);
			}
			rows.clear();
		}
	}

	public int count(DishesItemQuery query) {
		return dishesItemMapper.getDishesItemCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			dishesItemMapper.deleteDishesItemById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				dishesItemMapper.deleteDishesItemById(id);
			}
		}
	}

	@Transactional
	public void deleteDishesItemsByDishesId(long dishesId) {
		dishesItemMapper.deleteDishesItemsByDishesId(dishesId);
	}

	public DishesItem getDishesItem(Long id) {
		if (id == null) {
			return null;
		}
		DishesItem dishesItem = dishesItemMapper.getDishesItemById(id);
		return dishesItem;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDishesItemCountByQueryCriteria(DishesItemQuery query) {
		return dishesItemMapper.getDishesItemCount(query);
	}

	public List<DishesItem> getDishesItemsByDishesId(long dishesId) {
		return dishesItemMapper.getDishesItemsByDishesId(dishesId);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<DishesItem> getDishesItemsByQueryCriteria(int start, int pageSize, DishesItemQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<DishesItem> rows = sqlSessionTemplate.selectList("getDishesItems", query, rowBounds);
		return rows;
	}

	public List<DishesItem> list(DishesItemQuery query) {
		List<DishesItem> list = dishesItemMapper.getDishesItems(query);
		return list;
	}

	@Transactional
	public void save(DishesItem dishesItem) {
		if (dishesItem.getId() == 0) {
			dishesItem.setId(idGenerator.nextId("HEALTH_DISHES_ITEM"));
			dishesItem.setCreateTime(new Date());

			dishesItemMapper.insertDishesItem(dishesItem);
		} else {
			dishesItem.setUpdateTime(new Date());
			dishesItemMapper.updateDishesItem(dishesItem);
		}
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DishesItemMapper")
	public void setDishesItemMapper(DishesItemMapper dishesItemMapper) {
		this.dishesItemMapper = dishesItemMapper;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
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
