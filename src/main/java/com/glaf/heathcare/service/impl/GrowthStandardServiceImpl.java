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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.mapper.GrowthStandardMapper;
import com.glaf.heathcare.query.GrowthStandardQuery;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.util.GrowthStandardJsonFactory;

@Service("com.glaf.heathcare.service.growthStandardService")
@Transactional(readOnly = true)
public class GrowthStandardServiceImpl implements GrowthStandardService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GrowthStandardMapper growthStandardMapper;

	public GrowthStandardServiceImpl() {

	}

	public int count(GrowthStandardQuery query) {
		return growthStandardMapper.getGrowthStandardCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		CacheFactory.clear("growth_standard");
		if (id != null) {
			growthStandardMapper.deleteGrowthStandardById(id);
		}
	}

	public List<GrowthStandard> getAllGrowthStandards() {
		String cacheKey = "x_sys_growth_std_all";
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("growth_standard", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return GrowthStandardJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		GrowthStandardQuery query = new GrowthStandardQuery();
		List<GrowthStandard> list = growthStandardMapper.getGrowthStandards(query);
		if (SystemConfig.getBoolean("use_query_cache")) {
			JSONArray jsonArray = GrowthStandardJsonFactory.listToArray(list);
			CacheFactory.put("growth_standard", cacheKey, jsonArray.toJSONString());
		}
		return list;
	}

	public GrowthStandard getGrowthStandard(Long id) {
		if (id == null) {
			return null;
		}
		GrowthStandard growthStandard = growthStandardMapper.getGrowthStandardById(id);
		return growthStandard;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGrowthStandardCountByQueryCriteria(GrowthStandardQuery query) {
		return growthStandardMapper.getGrowthStandardCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GrowthStandard> getGrowthStandardsByQueryCriteria(int start, int pageSize, GrowthStandardQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<GrowthStandard> rows = sqlSessionTemplate.selectList("getGrowthStandards", query, rowBounds);
		return rows;
	}

	public List<GrowthStandard> list(GrowthStandardQuery query) {
		List<GrowthStandard> list = growthStandardMapper.getGrowthStandards(query);
		return list;
	}

	@Transactional
	public void save(GrowthStandard growthStandard) {
		CacheFactory.clear("growth_standard");
		if (growthStandard.getId() == 0) {
			growthStandard.setId(idGenerator.nextId("HEALTH_GROWTH_STANDARD"));
			growthStandard.setCreateTime(new Date());
			growthStandardMapper.insertGrowthStandard(growthStandard);
		} else {
			String complexKey = growthStandard.getStandardType() + "_" + growthStandard.getType() + "_"
					+ growthStandard.getSex() + "_" + growthStandard.getAgeOfTheMoon();
			GrowthStandard model = growthStandardMapper.getGrowthStandardByComplexKey(complexKey);
			if (model != null) {
				growthStandardMapper.updateGrowthStandard(growthStandard);
			} else {
				growthStandard.setId(idGenerator.nextId("HEALTH_GROWTH_STANDARD"));
				growthStandard.setCreateTime(new Date());
				growthStandardMapper.insertGrowthStandard(growthStandard);
			}
		}
	}

	@Transactional
	public void saveAll(String type, String sex, String standardType, List<GrowthStandard> growthStandards) {
		CacheFactory.clear("growth_standard");
		GrowthStandardQuery query = new GrowthStandardQuery();
		query.type(type);
		query.standardType(standardType);
		query.sex(sex);
		List<GrowthStandard> list = growthStandardMapper.getGrowthStandards(query);
		Map<String, GrowthStandard> dataMap = new HashMap<String, GrowthStandard>();
		if (list != null && !list.isEmpty()) {
			for (GrowthStandard std : list) {
				String complexKey = std.getStandardType() + "_" + std.getType() + "_" + std.getSex() + "_"
						+ std.getAgeOfTheMoon();
				dataMap.put(complexKey, std);
			}
		}

		if (growthStandards != null && !growthStandards.isEmpty()) {
			for (GrowthStandard std : growthStandards) {
				String complexKey = standardType + "_" + type + "_" + sex + "_" + std.getAgeOfTheMoon();
				if (dataMap.get(complexKey) != null) {
					GrowthStandard model = dataMap.get(complexKey);
					if (std.getOneDSDeviation() > 0) {
						model.setOneDSDeviation(std.getOneDSDeviation());
					}
					if (std.getTwoDSDeviation() > 0) {
						model.setTwoDSDeviation(std.getTwoDSDeviation());
					}
					if (std.getThreeDSDeviation() > 0) {
						model.setThreeDSDeviation(std.getThreeDSDeviation());
					}
					if (std.getFourDSDeviation() > 0) {
						model.setFourDSDeviation(std.getFourDSDeviation());
					}

					if (std.getNegativeOneDSDeviation() > 0) {
						model.setNegativeOneDSDeviation(std.getNegativeOneDSDeviation());
					}
					if (std.getNegativeTwoDSDeviation() > 0) {
						model.setNegativeTwoDSDeviation(std.getNegativeTwoDSDeviation());
					}
					if (std.getNegativeThreeDSDeviation() > 0) {
						model.setNegativeThreeDSDeviation(std.getNegativeThreeDSDeviation());
					}
					if (std.getNegativeFourDSDeviation() > 0) {
						model.setNegativeFourDSDeviation(std.getNegativeFourDSDeviation());
					}

					growthStandardMapper.updateGrowthStandard(model);
				} else {
					std.setId(idGenerator.nextId("HEALTH_GROWTH_STANDARD"));
					std.setCreateTime(new Date());
					std.setStandardType(standardType);
					std.setSex(sex);
					std.setType(type);
					std.setComplexKey(complexKey);
					growthStandardMapper.insertGrowthStandard(std);
				}
			}
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GrowthStandardMapper")
	public void setGrowthStandardMapper(GrowthStandardMapper growthStandardMapper) {
		this.growthStandardMapper = growthStandardMapper;
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
