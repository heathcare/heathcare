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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.base.ListModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.DBUtils;

import com.glaf.heathcare.domain.DietaryTemplateCount;
import com.glaf.heathcare.mapper.DietaryTemplateCountMapper;
import com.glaf.heathcare.query.DietaryTemplateCountQuery;
import com.glaf.heathcare.service.DietaryTemplateCountService;

@Service("com.glaf.heathcare.service.dietaryTemplateCountService")
@Transactional(readOnly = true)
public class DietaryTemplateCountServiceImpl implements DietaryTemplateCountService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DietaryTemplateCountMapper dietaryTemplateCountMapper;

	protected ITableDataService tableDataService;

	public DietaryTemplateCountServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<DietaryTemplateCount> list) {
		for (DietaryTemplateCount dietaryTemplateCount : list) {
			if (dietaryTemplateCount.getId() == 0) {
				dietaryTemplateCount.setId(idGenerator.nextId("HEALTH_DIETARY_TPL_COUNT"));
				if (StringUtils.isNotEmpty(tenantId)) {

				}
			}
		}

		int batch_size = 50;
		List<DietaryTemplateCount> rows = new ArrayList<DietaryTemplateCount>(batch_size);

		for (DietaryTemplateCount bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					dietaryTemplateCountMapper.bulkInsertDietaryTemplateCount_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					listModel.setList(rows);
					dietaryTemplateCountMapper.bulkInsertDietaryTemplateCount(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				dietaryTemplateCountMapper.bulkInsertDietaryTemplateCount_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				listModel.setList(rows);
				dietaryTemplateCountMapper.bulkInsertDietaryTemplateCount(listModel);
			}
			rows.clear();
		}
	}

	public int count(DietaryTemplateCountQuery query) {
		return dietaryTemplateCountMapper.getDietaryTemplateCountCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			DietaryTemplateCountQuery query = new DietaryTemplateCountQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
			}
			dietaryTemplateCountMapper.deleteDietaryTemplateCountById(query);
		}
	}

	public DietaryTemplateCount getDietaryTemplateCount(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		DietaryTemplateCountQuery query = new DietaryTemplateCountQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
		}
		DietaryTemplateCount dietaryTemplateCount = dietaryTemplateCountMapper.getDietaryTemplateCountById(query);
		return dietaryTemplateCount;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietaryTemplateCountCountByQueryCriteria(DietaryTemplateCountQuery query) {
		return dietaryTemplateCountMapper.getDietaryTemplateCountCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<DietaryTemplateCount> getDietaryTemplateCountsByQueryCriteria(int start, int pageSize,
			DietaryTemplateCountQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<DietaryTemplateCount> rows = sqlSessionTemplate.selectList("getDietaryTemplateCounts", query, rowBounds);
		return rows;
	}

	public List<DietaryTemplateCount> list(DietaryTemplateCountQuery query) {
		List<DietaryTemplateCount> list = dietaryTemplateCountMapper.getDietaryTemplateCounts(query);
		return list;
	}

	@Transactional
	public void save(DietaryTemplateCount dietaryTemplateCount) {
		if (dietaryTemplateCount.getId() == 0) {
			dietaryTemplateCount.setId(idGenerator.nextId("HEALTH_DIETARY_TPL_COUNT"));
			dietaryTemplateCount.setCreateTime(new Date());
			dietaryTemplateCountMapper.insertDietaryTemplateCount(dietaryTemplateCount);
		}
	}

	@Transactional
	public void saveAll(String tenantId, int suitNo, String type, List<DietaryTemplateCount> list) {
		TableModel table = new TableModel();
		table.setTableName("HEALTH_DIETARY_TPL_COUNT");

		if (tenantId != null) {
			table.addStringColumn("TENANTID_", tenantId);
		} else {
			table.addStringColumn("TENANTID_", "SYS");
		}

		table.addIntegerColumn("SUITNO_", suitNo);
		table.addStringColumn("TYPE_", type);

		tableDataService.deleteTableData(table);

		this.bulkInsert(tenantId, list);
	}

	@Transactional
	public void saveDay(String tenantId, int suitNo, String type, List<DietaryTemplateCount> list) {
		TableModel table = new TableModel();
		table.setTableName("HEALTH_DIETARY_TPL_COUNT");
		if (tenantId != null) {
			table.addStringColumn("TENANTID_", tenantId);
		} else {
			table.addStringColumn("TENANTID_", "SYS");
		}

		table.addIntegerColumn("SUITNO_", suitNo);
		table.addStringColumn("TYPE_", type);
		tableDataService.deleteTableData(table);

		this.bulkInsert(tenantId, list);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryTemplateCountMapper")
	public void setDietaryTemplateCountMapper(DietaryTemplateCountMapper dietaryTemplateCountMapper) {
		this.dietaryTemplateCountMapper = dietaryTemplateCountMapper;
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

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

}
