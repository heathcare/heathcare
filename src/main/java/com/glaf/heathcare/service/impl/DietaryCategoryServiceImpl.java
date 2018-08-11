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

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.security.LoginContext;
import com.glaf.heathcare.domain.DietaryCategory;
import com.glaf.heathcare.mapper.DietaryCategoryMapper;
import com.glaf.heathcare.query.DietaryCategoryQuery;
import com.glaf.heathcare.service.DietaryCategoryService;

@Service("com.glaf.heathcare.service.dietaryCategoryService")
@Transactional(readOnly = true)
public class DietaryCategoryServiceImpl implements DietaryCategoryService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DietaryCategoryMapper dietaryCategoryMapper;

	public DietaryCategoryServiceImpl() {

	}

	public int count(DietaryCategoryQuery query) {
		return dietaryCategoryMapper.getDietaryCategoryCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			dietaryCategoryMapper.deleteDietaryCategoryById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				dietaryCategoryMapper.deleteDietaryCategoryById(id);
			}
		}
	}

	public List<DietaryCategory> getDietaryCategories(LoginContext loginContext, boolean incluseSys) {
		List<DietaryCategory> categories = new ArrayList<DietaryCategory>();

		DietaryCategoryQuery query = new DietaryCategoryQuery();
		if (!loginContext.isSystemAdministrator()) {
			query.sysFlag("N");
			query.tenantId(loginContext.getTenantId());
			List<DietaryCategory> list = this.list(query);
			if (list != null && !list.isEmpty()) {
				categories.addAll(list);
			}
		}

		if (incluseSys) {
			categories.addAll(this.getSysDietaryCategories());
		}

		return categories;
	}

	public DietaryCategory getDietaryCategory(LoginContext loginContext, int suitNo) {
		if (loginContext.isSystemAdministrator() && suitNo < 1000) {
			DietaryCategoryQuery query = new DietaryCategoryQuery();
			query.sysFlag("Y");
			query.createBy("admin");
			List<DietaryCategory> list = this.list(query);
			if (list != null && !list.isEmpty()) {
				for (DietaryCategory cat : list) {
					if (cat.getSuitNo() == suitNo) {
						return cat;
					}
				}
			}
		} else {
			DietaryCategoryQuery query = new DietaryCategoryQuery();
			query.sysFlag("N");
			query.tenantId(loginContext.getTenantId());
			List<DietaryCategory> list = this.list(query);
			if (list != null && !list.isEmpty()) {
				for (DietaryCategory cat : list) {
					if (cat.getSuitNo() == suitNo) {
						return cat;
					}
				}
			}
		}
		return null;
	}

	public DietaryCategory getDietaryCategory(long id) {
		if (id == 0) {
			return null;
		}
		DietaryCategory dietaryCategory = dietaryCategoryMapper.getDietaryCategoryById(id);
		return dietaryCategory;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietaryCategoryCountByQueryCriteria(DietaryCategoryQuery query) {
		return dietaryCategoryMapper.getDietaryCategoryCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<DietaryCategory> getDietaryCategorysByQueryCriteria(int start, int pageSize,
			DietaryCategoryQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<DietaryCategory> rows = sqlSessionTemplate.selectList("getDietaryCategorys", query, rowBounds);
		return rows;
	}

	public int getMaxSuitNo(LoginContext loginContext) {
		int suitNo = 1001;
		if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
			Integer x = dietaryCategoryMapper.getTenantMaxSuitNo(loginContext.getTenantId());
			if (x != null) {
				suitNo = x.intValue();
				suitNo = suitNo + 1;
			} else {
				suitNo = 1001;
			}
		} else {
			Integer x = dietaryCategoryMapper.getSysMaxSuitNo("Y");
			if (x != null) {
				suitNo = x.intValue();
				suitNo = suitNo + 1;
			}
		}
		if (suitNo == 0) {
			suitNo = 1001;
		}
		return suitNo;
	}

	public List<DietaryCategory> getSysDietaryCategories() {
		DietaryCategoryQuery query = new DietaryCategoryQuery();
		query.sysFlag("Y");
		List<DietaryCategory> list = this.list(query);
		return list;
	}

	public DietaryCategory getSysDietaryCategory(int suitNo) {
		DietaryCategoryQuery query = new DietaryCategoryQuery();
		query.sysFlag("Y");
		query.createBy("admin");
		List<DietaryCategory> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			for (DietaryCategory cat : list) {
				if (cat.getSuitNo() == suitNo) {
					return cat;
				}
			}
		}
		return null;
	}

	public List<DietaryCategory> list(DietaryCategoryQuery query) {
		List<DietaryCategory> list = dietaryCategoryMapper.getDietaryCategorys(query);
		return list;
	}

	@Transactional
	public void save(DietaryCategory dietaryCategory) {
		if (dietaryCategory.getId() == 0) {
			dietaryCategory.setId(idGenerator.nextId());
			dietaryCategory.setCreateTime(new Date());
			int suitNo = 1001;
			if (StringUtils.isNotEmpty(dietaryCategory.getTenantId())) {
				Integer x = dietaryCategoryMapper.getTenantMaxSuitNo(dietaryCategory.getTenantId());
				if (x != null) {
					suitNo = x.intValue();
					suitNo = suitNo + 1;
				} else {
					suitNo = 1001;
				}
			} else {
				Integer x = dietaryCategoryMapper.getSysMaxSuitNo("Y");
				if (x != null) {
					suitNo = x.intValue();
					suitNo = suitNo + 1;
				}
			}
			if (suitNo == 0) {
				suitNo = 1001;
			}
			dietaryCategory.setSuitNo(suitNo);
			dietaryCategoryMapper.insertDietaryCategory(dietaryCategory);
		} else {
			dietaryCategoryMapper.updateDietaryCategory(dietaryCategory);
		}
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryCategoryMapper")
	public void setDietaryCategoryMapper(DietaryCategoryMapper dietaryCategoryMapper) {
		this.dietaryCategoryMapper = dietaryCategoryMapper;
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
