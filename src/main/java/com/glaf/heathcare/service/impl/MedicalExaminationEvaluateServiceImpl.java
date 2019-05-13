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

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;

import com.glaf.heathcare.domain.MedicalExaminationEvaluate;
import com.glaf.heathcare.mapper.MedicalExaminationEvaluateMapper;
import com.glaf.heathcare.query.MedicalExaminationEvaluateQuery;
import com.glaf.heathcare.service.MedicalExaminationEvaluateService;

@Service("com.glaf.heathcare.service.medicalExaminationEvaluateService")
@Transactional(readOnly = true)
public class MedicalExaminationEvaluateServiceImpl implements MedicalExaminationEvaluateService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected MedicalExaminationEvaluateMapper medicalExaminationEvaluateMapper;

	protected SysTenantService sysTenantService;

	public MedicalExaminationEvaluateServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<MedicalExaminationEvaluate> list) {
		for (MedicalExaminationEvaluate medicalExaminationEvaluate : list) {
			if (medicalExaminationEvaluate.getId() == 0) {
				medicalExaminationEvaluate.setId(idGenerator.nextId("HEALTH_MEDICAL_EXAM_EVAL"));
			}
		}

		int batch_size = 50;
		List<MedicalExaminationEvaluate> rows = new ArrayList<MedicalExaminationEvaluate>(batch_size);

		for (MedicalExaminationEvaluate bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					medicalExaminationEvaluateMapper.bulkInsertMedicalExaminationEvaluate_oracle(rows);
				} else {
					medicalExaminationEvaluateMapper.bulkInsertMedicalExaminationEvaluate(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				medicalExaminationEvaluateMapper.bulkInsertMedicalExaminationEvaluate_oracle(rows);
			} else {
				medicalExaminationEvaluateMapper.bulkInsertMedicalExaminationEvaluate(rows);
			}
			rows.clear();
		}
	}

	public int count(MedicalExaminationEvaluateQuery query) {
		return medicalExaminationEvaluateMapper.getMedicalExaminationEvaluateCount(query);
	}

	@Transactional
	public void deleteMedicalExaminationEvaluateByTenantId(String tenantId) {
		medicalExaminationEvaluateMapper.deleteMedicalExaminationEvaluateByTenantId(tenantId);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getMedicalExaminationEvaluateCountByQueryCriteria(MedicalExaminationEvaluateQuery query) {
		return medicalExaminationEvaluateMapper.getMedicalExaminationEvaluateCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<MedicalExaminationEvaluate> getMedicalExaminationEvaluatesByQueryCriteria(int start, int pageSize,
			MedicalExaminationEvaluateQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<MedicalExaminationEvaluate> rows = sqlSessionTemplate.selectList("getMedicalExaminationEvaluates", query,
				rowBounds);
		return rows;
	}

	public List<MedicalExaminationEvaluate> list(MedicalExaminationEvaluateQuery query) {
		List<MedicalExaminationEvaluate> list = medicalExaminationEvaluateMapper.getMedicalExaminationEvaluates(query);
		return list;
	}

	/**
	 * 批量保存记录
	 * 
	 * @return
	 */
	@Transactional
	public void saveAll(String tenantId, int year, int month, List<MedicalExaminationEvaluate> rows) {
		MedicalExaminationEvaluateQuery query = new MedicalExaminationEvaluateQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.month(month);
		medicalExaminationEvaluateMapper.deleteMedicalExaminationEvaluates(query);

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
		for (MedicalExaminationEvaluate model : rows) {
			model.setAreaId(tenant.getAreaId());
			model.setCityId(tenant.getCityId());
			model.setProvinceId(tenant.getProvinceId());
			model.setYear(year);
			model.setMonth(month);
			model.setTenantId(tenantId);
		}

		this.bulkInsert(rows);
	}

	/**
	 * 批量保存记录
	 * 
	 * @return
	 */
	@Transactional
	public void saveAll(String tenantId, List<MedicalExaminationEvaluate> rows) {
		medicalExaminationEvaluateMapper.deleteMedicalExaminationEvaluateByTenantId(tenantId);

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
		for (MedicalExaminationEvaluate model : rows) {
			model.setAreaId(tenant.getAreaId());
			model.setCityId(tenant.getCityId());
			model.setProvinceId(tenant.getProvinceId());
			model.setTenantId(tenantId);
		}

		this.bulkInsert(rows);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.MedicalExaminationEvaluateMapper")
	public void setMedicalExaminationEvaluateMapper(MedicalExaminationEvaluateMapper medicalExaminationEvaluateMapper) {
		this.medicalExaminationEvaluateMapper = medicalExaminationEvaluateMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

}
