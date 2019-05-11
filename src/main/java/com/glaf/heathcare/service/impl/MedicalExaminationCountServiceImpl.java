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
import java.util.Calendar;
import java.util.Collection;
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

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.base.TableModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.DBUtils;

import com.glaf.heathcare.domain.MedicalExaminationCount;
import com.glaf.heathcare.mapper.MedicalExaminationCountMapper;
import com.glaf.heathcare.query.MedicalExaminationCountQuery;
import com.glaf.heathcare.service.MedicalExaminationCountService;

@Service("com.glaf.heathcare.service.medicalExaminationCountService")
@Transactional(readOnly = true)
public class MedicalExaminationCountServiceImpl implements MedicalExaminationCountService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected MedicalExaminationCountMapper medicalExaminationCountMapper;

	protected ITableDataService tableDataService;

	protected SysTenantService sysTenantService;

	public MedicalExaminationCountServiceImpl() {

	}

	@Transactional
	public void bulkInsert(Collection<MedicalExaminationCount> list) {
		long ts = System.currentTimeMillis();
		Date date = new Date(ts);
		StringBuilder buffer = new StringBuilder();
		for (MedicalExaminationCount model : list) {
			if (StringUtils.isEmpty(model.getId())) {
				buffer.delete(0, buffer.length());
				buffer.append(model.getTenantId());
				if (model.getYear() > 0) {
					buffer.append("_").append(model.getYear());
				}
				if (model.getMonth() > 0) {
					buffer.append("_").append(model.getMonth());
				}
				if (model.getType() != null) {
					buffer.append("_").append(model.getType());
				}
				if (model.getTargetType() != null) {
					buffer.append("_").append(model.getTargetType());
				}
				buffer.append("_").append(ts++);
				model.setId(buffer.toString());
				model.setCreateTime(date);
			}
		}

		int batch_size = 50;
		List<MedicalExaminationCount> rows = new ArrayList<MedicalExaminationCount>(batch_size);

		for (MedicalExaminationCount model : list) {
			if (model.getTotalPerson() > 0 && model.getCheckPerson() > 0 && StringUtils.isNotEmpty(model.getType())) {
				rows.add(model);
			}
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					medicalExaminationCountMapper.bulkInsertMedicalExaminationCount_oracle(rows);
				} else {
					medicalExaminationCountMapper.bulkInsertMedicalExaminationCount(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				medicalExaminationCountMapper.bulkInsertMedicalExaminationCount_oracle(rows);
			} else {
				medicalExaminationCountMapper.bulkInsertMedicalExaminationCount(rows);
			}
			rows.clear();
		}
	}

	public int count(MedicalExaminationCountQuery query) {
		return medicalExaminationCountMapper.getMedicalExaminationCountCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			medicalExaminationCountMapper.deleteMedicalExaminationCountById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				medicalExaminationCountMapper.deleteMedicalExaminationCountById(id);
			}
		}
	}

	public MedicalExaminationCount getMedicalExaminationCount(String id) {
		if (id == null) {
			return null;
		}
		MedicalExaminationCount medicalExaminationCount = medicalExaminationCountMapper
				.getMedicalExaminationCountById(id);
		return medicalExaminationCount;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getMedicalExaminationCountCountByQueryCriteria(MedicalExaminationCountQuery query) {
		return medicalExaminationCountMapper.getMedicalExaminationCountCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<MedicalExaminationCount> getMedicalExaminationCountsByQueryCriteria(int start, int pageSize,
			MedicalExaminationCountQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<MedicalExaminationCount> rows = sqlSessionTemplate.selectList("getMedicalExaminationCounts", query,
				rowBounds);
		return rows;
	}

	public List<MedicalExaminationCount> list(MedicalExaminationCountQuery query) {
		List<MedicalExaminationCount> list = medicalExaminationCountMapper.getMedicalExaminationCounts(query);
		return list;
	}

	@Transactional
	public void save(MedicalExaminationCount model) {
		if (StringUtils.isEmpty(model.getId())) {
			if (model.getTotalPerson() > 0 && model.getCheckPerson() > 0 && StringUtils.isNotEmpty(model.getType())) {
				model.setId(idGenerator.getNextId("HEALTH_MEDICAL_EXAM_COUNT"));
				model.setCreateTime(new Date());
				medicalExaminationCountMapper.insertMedicalExaminationCount(model);
			}
		} else {
			medicalExaminationCountMapper.updateMedicalExaminationCount(model);
		}
	}

	@Transactional
	public void saveAll(String tenantId, int year, int month, String type, String targetType,
			Collection<MedicalExaminationCount> list) {
		TableModel table = new TableModel();
		table.setTableName("HEALTH_MEDICAL_EXAM_COUNT");
		table.addStringColumn("TENANTID_", tenantId);
		table.addStringColumn("TARGETTYPE_", targetType);
		table.addStringColumn("TYPE_", type);
		table.addIntegerColumn("YEAR_", year);
		table.addIntegerColumn("MONTH_", month);
		tableDataService.deleteTableData(table);

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
		for (MedicalExaminationCount model : list) {
			model.setAreaId(tenant.getAreaId());
			model.setCityId(tenant.getCityId());
			model.setProvinceId(tenant.getProvinceId());
		}

		this.bulkInsert(list);
	}

	@Transactional
	public void saveAll(String tenantId, String targetType, Collection<MedicalExaminationCount> list) {
		java.util.Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date());
		int year = calendar.get(Calendar.YEAR);
		MedicalExaminationCountQuery query = new MedicalExaminationCountQuery();
		query.tenantId(tenantId);
		query.yearGreaterThanOrEqual(year - 2);
		query.targetType(targetType);
		medicalExaminationCountMapper.deleteMedicalExaminationCounts(query);

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
		for (MedicalExaminationCount model : list) {
			model.setAreaId(tenant.getAreaId());
			model.setCityId(tenant.getCityId());
			model.setProvinceId(tenant.getProvinceId());
		}

		this.bulkInsert(list);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.MedicalExaminationCountMapper")
	public void setMedicalExaminationCountMapper(MedicalExaminationCountMapper medicalExaminationCountMapper) {
		this.medicalExaminationCountMapper = medicalExaminationCountMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}
}
