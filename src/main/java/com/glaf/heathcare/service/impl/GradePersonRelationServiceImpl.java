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

@Service("com.glaf.heathcare.service.gradePersonRelationService")
@Transactional(readOnly = true)
public class GradePersonRelationServiceImpl implements GradePersonRelationService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GradePersonRelationMapper gradePersonRelationMapper;

	public GradePersonRelationServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<GradePersonRelation> list) {
		for (GradePersonRelation gradePersonRelation : list) {
			if (StringUtils.isEmpty(gradePersonRelation.getId())) {
				gradePersonRelation.setId(UUID32.generateShortUuid());
				gradePersonRelation.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<GradePersonRelation> rows = new ArrayList<GradePersonRelation>(batch_size);

		for (GradePersonRelation bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					gradePersonRelationMapper.bulkInsertGradePersonRelation_oracle(rows);
				} else {
					gradePersonRelationMapper.bulkInsertGradePersonRelation(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				gradePersonRelationMapper.bulkInsertGradePersonRelation_oracle(rows);
			} else {
				gradePersonRelationMapper.bulkInsertGradePersonRelation(rows);
			}
			rows.clear();
		}
	}

	public int count(GradePersonRelationQuery query) {
		return gradePersonRelationMapper.getGradePersonRelationCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			gradePersonRelationMapper.deleteGradePersonRelationById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				gradePersonRelationMapper.deleteGradePersonRelationById(id);
			}
		}
	}

	public GradePersonRelation getGradePersonRelation(String id) {
		if (id == null) {
			return null;
		}
		GradePersonRelation gradePersonRelation = gradePersonRelationMapper.getGradePersonRelationById(id);
		return gradePersonRelation;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGradePersonRelationCountByQueryCriteria(GradePersonRelationQuery query) {
		return gradePersonRelationMapper.getGradePersonRelationCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GradePersonRelation> getGradePersonRelationsByQueryCriteria(int start, int pageSize,
			GradePersonRelationQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<GradePersonRelation> rows = sqlSessionTemplate.selectList("getGradePersonRelations", query, rowBounds);
		return rows;
	}

	public int getPersonCount(String tenantId, String gradeId, int year, int month) {
		GradePersonRelationQuery query = new GradePersonRelationQuery();
		query.tenantId(tenantId);
		query.gradeId(gradeId);
		query.year(year);
		if (month > 0) {
			query.month(month);
		}
		return this.count(query);
	}

	public Map<String, Integer> getPersonCountMap(String tenantId, int year, int month) {
		GradePersonRelationQuery query = new GradePersonRelationQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.month(month);
		List<GradePersonRelation> list = gradePersonRelationMapper.selectGradePersonRelationCountGroupByGrade(query);
		Map<String, Integer> cntMap = new HashMap<String, Integer>();
		for (GradePersonRelation rel : list) {
			if (rel.getCount() > 0) {
				cntMap.put(rel.getGradeId(), rel.getCount());
			}
		}
		return cntMap;
	}

	public List<GradePersonRelation> list(GradePersonRelationQuery query) {
		List<GradePersonRelation> list = gradePersonRelationMapper.getGradePersonRelations(query);
		return list;
	}

	@Transactional
	public void save(GradePersonRelation gradePersonRelation) {
		if (StringUtils.isEmpty(gradePersonRelation.getId())) {
			gradePersonRelation.setId(UUID32.generateShortUuid());
			gradePersonRelation.setCreateTime(new Date());

			gradePersonRelationMapper.insertGradePersonRelation(gradePersonRelation);
		} else {
			gradePersonRelationMapper.updateGradePersonRelation(gradePersonRelation);
		}
	}

	@Transactional
	public void saveAll(String tenantId, String gradeId, int year, int semester, int month,
			List<GradePersonRelation> rows) {
		GradePersonRelationQuery query = new GradePersonRelationQuery();
		query.tenantId(tenantId);
		query.gradeId(gradeId);
		query.year(year);
		if (semester > 0) {
			query.semester(semester);
		}
		if (month > 0) {
			query.month(month);
		}
		gradePersonRelationMapper.deleteGradePersonRelations(query);
		this.bulkInsert(rows);
	}

	public List<GradePersonRelation> selectGradePersonRelationCountGroupBySex(GradePersonRelationQuery query) {
		return gradePersonRelationMapper.selectGradePersonRelationCountGroupBySex(query);
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GradePersonRelationMapper")
	public void setGradePersonRelationMapper(GradePersonRelationMapper gradePersonRelationMapper) {
		this.gradePersonRelationMapper = gradePersonRelationMapper;
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
