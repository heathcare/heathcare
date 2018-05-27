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

package com.glaf.modules.dictory.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.util.PageResult;
import com.glaf.modules.dictory.mapper.DictoryMapper;
import com.glaf.modules.dictory.model.Dictory;
import com.glaf.modules.dictory.query.DictoryQuery;
import com.glaf.modules.dictory.util.DictoryJsonFactory;
import com.glaf.modules.tree.model.TreeNode;
import com.glaf.modules.tree.query.TreeNodeQuery;
import com.glaf.modules.tree.service.TreeNodeService;

@Service("com.glaf.modules.dictory.service.dictoryService")
@Transactional(readOnly = true)
public class DictoryServiceImpl implements DictoryService {
	protected final static Log logger = LogFactory.getLog(DictoryServiceImpl.class);

	protected IdGenerator idGenerator;

	protected DictoryMapper dictoryMapper;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected TreeNodeService treeNodeService;

	public DictoryServiceImpl() {

	}

	public int count(DictoryQuery query) {
		return dictoryMapper.getDictoryCount(query);
	}

	@Transactional
	public boolean create(Dictory bean) {
		this.save(bean);
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear("mydictory");
		}
		return true;
	}

	@Transactional
	public boolean delete(Dictory bean) {
		this.deleteById(bean.getId());
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear("mydictory");
		}
		return true;
	}

	@Transactional
	public boolean delete(long id) {
		this.deleteById(id);
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear("mydictory");
		}
		return true;
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			dictoryMapper.deleteDictoryById(id);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.clear("mydictory");
			}
		}
	}

	public Dictory find(long id) {
		return this.getDictory(id);
	}

	/**
	 * 获取全部基础数据的分类树
	 * 
	 * @return
	 */
	public List<TreeNode> getAllCategories(String tenantId) {
		TreeNodeQuery query = new TreeNodeQuery();
		query.tenantId(tenantId);
		query.locked(0);
		List<TreeNode> trees = treeNodeService.getDictoryTreeNodes(query);
		return trees;
	}

	public List<Dictory> getAvailableDictoryList(String tenantId, long nodeId) {
		String cacheKey = "my_dicts_" + nodeId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("mydictory", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return DictoryJsonFactory.arrayToList(array);
				} catch (Exception ex) {
				}
			}
		}
		DictoryQuery query = new DictoryQuery();
		query.nodeId(nodeId);
		query.locked(0);
		query.setOrderBy(" E.SORTNO asc");
		List<Dictory> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = DictoryJsonFactory.listToArray(list);
				CacheFactory.put("mydictory", cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	public String getCodeById(String tenantId, long id) {
		Dictory dic = find(id);
		return dic.getCode();
	}

	public List<Dictory> getDictories(DictoryQuery query) {
		return dictoryMapper.getDictories(query);
	}

	public List<Dictory> getDictories(String tenantId, String codeLike) {
		DictoryQuery query = new DictoryQuery();
		query.tenantId(tenantId);
		query.setCodeLike(codeLike);
		return dictoryMapper.getDictories(query);
	}

	public Dictory getDictory(Long id) {
		if (id == null) {
			return null;
		}
		String cacheKey = "sys_dict_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("mydictory", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return DictoryJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
				}
			}
		}

		Dictory dictory = dictoryMapper.getDictoryById(id);

		if (dictory != null && SystemConfig.getBoolean("use_query_cache")) {
			JSONObject json = dictory.toJsonObject();
			CacheFactory.put("mydictory", cacheKey, json.toJSONString());
		}
		return dictory;
	}

	public int getDictoryCountByQueryCriteria(DictoryQuery query) {
		return dictoryMapper.getDictoryCount(query);
	}

	public PageResult getDictoryList(String tenantId, int pageNo, int pageSize) {
		// 计算总数
		PageResult pager = new PageResult();
		DictoryQuery query = new DictoryQuery();
		query.tenantId(tenantId);
		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.SORTNO asc");

		int start = pageSize * (pageNo - 1);
		List<Dictory> list = this.getDictorysByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public List<Dictory> getDictoryList(String tenantId, long nodeId) {
		DictoryQuery query = new DictoryQuery();
		query.tenantId(tenantId);
		query.nodeId(nodeId);
		query.setOrderBy(" E.SORTNO asc ");
		return this.list(query);
	}

	public PageResult getDictoryList(String tenantId, long nodeId, int pageNo, int pageSize) {
		// 计算总数
		PageResult pager = new PageResult();
		DictoryQuery query = new DictoryQuery();
		query.tenantId(tenantId);
		query.nodeId(nodeId);
		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.SORTNO asc");

		int start = pageSize * (pageNo - 1);
		List<Dictory> list = this.getDictorysByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	/**
	 * 返回某分类下的所有字典列表
	 * 
	 * @param nodeCode
	 * @return
	 */
	public List<Dictory> getDictoryList(String tenantId, String nodeCode) {
		TreeNode tree = treeNodeService.getTreeNodeByCode(tenantId, nodeCode);
		if (tree == null) {
			return null;
		}
		return this.getAvailableDictoryList(tenantId, tree.getId());
	}

	/**
	 * 返回某分类下的所有字典列表
	 * 
	 * @param category
	 * @return
	 */
	public List<Dictory> getDictoryListByCategory(String tenantId, String category) {
		DictoryQuery query = new DictoryQuery();
		query.tenantId(tenantId);
		query.category(category);

		return dictoryMapper.getDictoryListByCategory(query);
	}

	public List<Dictory> getDictorysByQueryCriteria(int start, int pageSize, DictoryQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Dictory> rows = sqlSessionTemplate.selectList("getDictories", query, rowBounds);
		return rows;
	}

	public List<Dictory> list(DictoryQuery query) {
		List<Dictory> list = dictoryMapper.getDictories(query);
		return list;
	}

	@Transactional
	public void save(Dictory dictory) {
		if (dictory.getId() == 0) {
			dictory.setId(idGenerator.nextId("T_DICTORY"));
			dictory.setCreateDate(new Date());
			dictory.setSort(1);
			dictoryMapper.insertDictory(dictory);

			long nodeId = dictory.getNodeId();
			TreeNode tree = treeNodeService.findById(dictory.getTenantId(), nodeId);
			if (tree != null && tree.getCode() != null) {

			}
		} else {
			dictory.setUpdateDate(new Date());
			dictoryMapper.updateDictory(dictory);
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = "sys_dict_" + dictory.getId();
				CacheFactory.remove("mydictory", cacheKey);
			}
		}
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "my_dicts_" + dictory.getNodeId();
			CacheFactory.remove("mydictory", cacheKey);
		}
	}

	@javax.annotation.Resource(name = "com.glaf.modules.dictory.mapper.DictoryMapper")
	public void setDictoryMapper(DictoryMapper dictoryMapper) {
		this.dictoryMapper = dictoryMapper;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.modules.tree.service.treeNodeService")
	public void setTreeNodeService(TreeNodeService treeNodeService) {
		this.treeNodeService = treeNodeService;
	}

	@Transactional
	public boolean update(Dictory bean) {
		this.save(bean);
		return true;
	}

}
