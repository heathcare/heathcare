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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.base.TableModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.DBUtils;

import com.glaf.heathcare.domain.FoodFavorite;
import com.glaf.heathcare.mapper.FoodFavoriteMapper;
import com.glaf.heathcare.query.FoodFavoriteQuery;
import com.glaf.heathcare.service.FoodFavoriteService;

@Service("com.glaf.heathcare.service.foodFavoriteService")
@Transactional(readOnly = true)
public class FoodFavoriteServiceImpl implements FoodFavoriteService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected FoodFavoriteMapper foodFavoriteMapper;

	protected ITableDataService tableDataService;

	public FoodFavoriteServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<FoodFavorite> list) {
		for (FoodFavorite foodFavorite : list) {
			if (foodFavorite.getId() == 0) {
				foodFavorite.setId(idGenerator.nextId("HEALTH_FOOD_FAVORITE"));
			}
		}

		int batch_size = 50;
		List<FoodFavorite> rows = new ArrayList<FoodFavorite>(batch_size);

		for (FoodFavorite bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					foodFavoriteMapper.bulkInsertFoodFavorite_oracle(rows);
				} else {
					foodFavoriteMapper.bulkInsertFoodFavorite(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				foodFavoriteMapper.bulkInsertFoodFavorite_oracle(rows);
			} else {
				foodFavoriteMapper.bulkInsertFoodFavorite(rows);
			}
			rows.clear();
		}
	}

	public int count(FoodFavoriteQuery query) {
		return foodFavoriteMapper.getFoodFavoriteCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			foodFavoriteMapper.deleteFoodFavoriteById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				foodFavoriteMapper.deleteFoodFavoriteById(id);
			}
		}
	}

	public FoodFavorite getFoodFavorite(Long id) {
		if (id == null) {
			return null;
		}
		FoodFavorite foodFavorite = foodFavoriteMapper.getFoodFavoriteById(id);
		return foodFavorite;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getFoodFavoriteCountByQueryCriteria(FoodFavoriteQuery query) {
		return foodFavoriteMapper.getFoodFavoriteCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<FoodFavorite> getFoodFavoritesByQueryCriteria(int start, int pageSize, FoodFavoriteQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<FoodFavorite> rows = sqlSessionTemplate.selectList("getFoodFavorites", query, rowBounds);
		return rows;
	}

	public List<FoodFavorite> list(FoodFavoriteQuery query) {
		List<FoodFavorite> list = foodFavoriteMapper.getFoodFavorites(query);
		return list;
	}

	@Transactional
	public void save(FoodFavorite foodFavorite) {
		if (foodFavorite.getId() == 0) {
			foodFavorite.setId(idGenerator.nextId("HEALTH_FOOD_FAVORITE"));

			foodFavoriteMapper.insertFoodFavorite(foodFavorite);
		} else {
			foodFavoriteMapper.updateFoodFavorite(foodFavorite);
		}
	}

	@Transactional
	public void saveAll(String tenantId, long nodeId, List<FoodFavorite> foodFavorites) {
		TableModel table = new TableModel();
		table.setTableName("HEALTH_FOOD_FAVORITE");
		table.addStringColumn("TENANTID_", tenantId);
		table.addLongColumn("NODEID_", nodeId);
		tableDataService.deleteTableData(table);

		if (foodFavorites != null && !foodFavorites.isEmpty()) {
			for (FoodFavorite foodFavorite : foodFavorites) {
				foodFavorite.setId(idGenerator.nextId("HEALTH_FOOD_FAVORITE"));
				foodFavorite.setTenantId(tenantId);
				foodFavorite.setNodeId(nodeId);
				foodFavoriteMapper.insertFoodFavorite(foodFavorite);
			}
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.FoodFavoriteMapper")
	public void setFoodFavoriteMapper(FoodFavoriteMapper foodFavoriteMapper) {
		this.foodFavoriteMapper = foodFavoriteMapper;
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
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

}
