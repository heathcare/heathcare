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

@Service("com.glaf.heathcare.service.medicalSpotCheckService")
@Transactional(readOnly = true)
public class MedicalSpotCheckServiceImpl implements MedicalSpotCheckService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected MedicalSpotCheckMapper medicalSpotCheckMapper;

	public MedicalSpotCheckServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<MedicalSpotCheck> list) {
		for (MedicalSpotCheck medicalSpotCheck : list) {
			if (StringUtils.isEmpty(medicalSpotCheck.getId())) {
				medicalSpotCheck.setId(UUID32.generateShortUuid());
			}
		}

		int batch_size = 50;
		List<MedicalSpotCheck> rows = new ArrayList<MedicalSpotCheck>(batch_size);

		for (MedicalSpotCheck bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					medicalSpotCheckMapper.bulkInsertMedicalSpotCheck_oracle(rows);
				} else {
					medicalSpotCheckMapper.bulkInsertMedicalSpotCheck(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				medicalSpotCheckMapper.bulkInsertMedicalSpotCheck_oracle(rows);
			} else {
				medicalSpotCheckMapper.bulkInsertMedicalSpotCheck(rows);
			}
			rows.clear();
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			MedicalSpotCheckQuery query = new MedicalSpotCheckQuery();
			query.setIds(ids);
			medicalSpotCheckMapper.deleteMedicalSpotChecks(query);
		}
	}

	public int count(MedicalSpotCheckQuery query) {
		return medicalSpotCheckMapper.getMedicalSpotCheckCount(query);
	}

	public List<MedicalSpotCheck> list(MedicalSpotCheckQuery query) {
		List<MedicalSpotCheck> list = medicalSpotCheckMapper.getMedicalSpotChecks(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getMedicalSpotCheckCountByQueryCriteria(MedicalSpotCheckQuery query) {
		return medicalSpotCheckMapper.getMedicalSpotCheckCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<MedicalSpotCheck> getMedicalSpotChecksByQueryCriteria(int start, int pageSize,
			MedicalSpotCheckQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<MedicalSpotCheck> rows = sqlSessionTemplate.selectList("getMedicalSpotChecks", query, rowBounds);
		return rows;
	}

	@Transactional
	public void save(MedicalSpotCheck medicalSpotCheck) {
		if (StringUtils.isEmpty(medicalSpotCheck.getId())) {
			medicalSpotCheck.setId(UUID32.generateShortUuid());

			medicalSpotCheckMapper.insertMedicalSpotCheck(medicalSpotCheck);
		}
	}

	@Transactional
	public void updateAll(List<MedicalSpotCheck> list) {
		for (MedicalSpotCheck model : list) {
			medicalSpotCheckMapper.updateMedicalSpotCheck(model);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.MedicalSpotCheckMapper")
	public void setMedicalSpotCheckMapper(MedicalSpotCheckMapper medicalSpotCheckMapper) {
		this.medicalSpotCheckMapper = medicalSpotCheckMapper;
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
