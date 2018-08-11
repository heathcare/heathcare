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
import com.glaf.core.security.Authentication;
import com.glaf.core.util.*;

import com.glaf.heathcare.mapper.*;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.*;

@Service("com.glaf.heathcare.service.gradeAdjustService")
@Transactional(readOnly = true)
public class GradeAdjustServiceImpl implements GradeAdjustService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GradeAdjustMapper gradeAdjustMapper;

	protected GradeInfoService gradeInfoService;

	public GradeAdjustServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<GradeAdjust> list) {
		for (GradeAdjust gradeAdjust : list) {
			if (StringUtils.isEmpty(gradeAdjust.getId())) {
				gradeAdjust.setId(idGenerator.getNextId("HEALTH_GRADE_ADJUST"));
			}
		}

		int batch_size = 50;
		List<GradeAdjust> rows = new ArrayList<GradeAdjust>(batch_size);

		for (GradeAdjust bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					gradeAdjustMapper.bulkInsertGradeAdjust_oracle(rows);
				} else {
					gradeAdjustMapper.bulkInsertGradeAdjust(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				gradeAdjustMapper.bulkInsertGradeAdjust_oracle(rows);
			} else {
				gradeAdjustMapper.bulkInsertGradeAdjust(rows);
			}
			rows.clear();
		}
	}

	public int count(GradeAdjustQuery query) {
		return gradeAdjustMapper.getGradeAdjustCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			gradeAdjustMapper.deleteGradeAdjustById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				gradeAdjustMapper.deleteGradeAdjustById(id);
			}
		}
	}

	public GradeAdjust getGradeAdjust(String id) {
		if (id == null) {
			return null;
		}
		GradeAdjust gradeAdjust = gradeAdjustMapper.getGradeAdjustById(id);
		return gradeAdjust;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGradeAdjustCountByQueryCriteria(GradeAdjustQuery query) {
		return gradeAdjustMapper.getGradeAdjustCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GradeAdjust> getGradeAdjustsByQueryCriteria(int start, int pageSize, GradeAdjustQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<GradeAdjust> rows = sqlSessionTemplate.selectList("getGradeAdjusts", query, rowBounds);
		return rows;
	}

	public List<GradeAdjust> list(GradeAdjustQuery query) {
		List<GradeAdjust> list = gradeAdjustMapper.getGradeAdjusts(query);
		return list;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GradeAdjustMapper")
	public void setGradeAdjustMapper(GradeAdjustMapper gradeAdjustMapper) {
		this.gradeAdjustMapper = gradeAdjustMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
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

	/**
	 * 升班
	 * 
	 * @return
	 */
	@Transactional
	public void upgrade(String tenantId) {
		List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(tenantId);
		if (grades != null && !grades.isEmpty()) {
			List<GradeAdjust> list = gradeAdjustMapper.getGradeAdjustsByTenantId(tenantId);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int year = calendar.get(Calendar.YEAR);
			Map<String, GradeAdjust> adjustMap = new HashMap<String, GradeAdjust>();
			if (list != null && !list.isEmpty()) {
				for (GradeAdjust adjust : list) {
					if (adjust.getYear() == year) {
						adjustMap.put(adjust.getGradeId(), adjust);
					}
				}
			}
			for (GradeInfo grade : grades) {
				if (adjustMap.get(grade.getId()) == null) {
					/**
					 * 满足这个条件的才可以自动升班，否则手动调整。
					 */
					if (grade.getYear() > 0 && (year + 2 - grade.getYear() == grade.getLevel())) {
						String oname = grade.getName();
						String name = oname;
						switch (grade.getLevel()) {
						case 2:
							name = StringTools.replace(oname, "小小", "小");
							name = StringTools.replace(oname, "托", "小");
							break;
						case 3:
							name = StringTools.replace(oname, "小", "中");
							break;
						case 4:
							name = StringTools.replace(oname, "中", "大");
							break;
						case 5:
							name = StringTools.replace(oname, "大", "学前");
							break;
						case 6:
							name = StringTools.replace(oname, "大", "学前");
							break;
						default:
							break;
						}

						grade.setName(name);
						grade.setLevel(grade.getLevel() + 1);
						gradeInfoService.save(grade);

						GradeAdjust adjust = new GradeAdjust();
						adjust.setId(UUID32.getUUID());
						adjust.setTenantId(tenantId);
						adjust.setGradeId(grade.getId());
						adjust.setYear(year);
						adjust.setLevel(grade.getLevel());
						if (adjustMap.get(grade.getId()) != null) {
							adjust.setVersion(adjustMap.get(grade.getId()).getVersion() + 1);
						} else {
							adjust.setVersion(1);
						}
						adjust.setSource(oname);
						adjust.setTarget(name);
						adjust.setCreateBy(Authentication.getAuthenticatedActorId());
						adjust.setCreateTime(new Date());
						gradeAdjustMapper.insertGradeAdjust(adjust);
					}
				}
			}
		}
	}

}
