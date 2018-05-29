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

package com.glaf.modules.tree.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.StringTools;
import com.glaf.modules.tree.mapper.TreeNodeMapper;
import com.glaf.modules.tree.model.TreeNode;
import com.glaf.modules.tree.query.TreeNodeQuery;
import com.glaf.modules.tree.util.TreeNodeJsonFactory;

@Service("com.glaf.modules.tree.service.treeNodeService")
@Transactional(readOnly = true)
public class TreeNodeServiceImpl implements TreeNodeService {
	protected final static Log logger = LogFactory.getLog(TreeNodeServiceImpl.class);

	protected IdGenerator idGenerator;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected TreeNodeMapper treeNodeMapper;

	protected ITableDataService tableDataService;

	public TreeNodeServiceImpl() {

	}

	public int count(TreeNodeQuery query) {
		return treeNodeMapper.getTreeNodeCount(query);
	}

	@Transactional
	public boolean create(TreeNode bean) {
		String parentTreeId = null;
		if (bean.getParentId() != 0) {
			TreeNode parent = this.findById(bean.getTenantId(), bean.getParentId());
			if (parent != null) {
				parentTreeId = parent.getTreeId();
				if (bean.getDiscriminator() == null) {
					bean.setDiscriminator(parent.getDiscriminator());
				}
				if (bean.getCacheFlag() == null) {
					bean.setCacheFlag(parent.getCacheFlag());
				}
				if (SystemConfig.getBoolean("use_query_cache")) {
					String cacheKey = "cache_treenode_" + parent.getId();
					CacheFactory.remove("treenode", cacheKey);
					cacheKey = "treenode_" + parent.getId();
					CacheFactory.remove("treenode", cacheKey);
					CacheFactory.clear("treenode");
				}
			}
		}
		bean.setId(idGenerator.nextId("T_TREENODE"));
		bean.setCreateDate(new Date());
		bean.setSort(1);
		if (parentTreeId != null) {
			bean.setTreeId(parentTreeId + bean.getId() + "|");
		} else {
			bean.setTreeId(bean.getId() + "|");
		}
		treeNodeMapper.insertTreeNode(bean);
		return true;
	}

	@Transactional
	public boolean delete(String tenantId, long id) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear("treenode");
		}
		this.deleteById(tenantId, id);

		return true;
	}

	@Transactional
	public boolean delete(TreeNode bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear("treenode");
		}
		this.deleteById(bean.getTenantId(), bean.getId());

		return true;
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			List<TreeNode> treeList = this.getTreeNodeList(tenantId, id);
			if (treeList != null && !treeList.isEmpty()) {
				throw new RuntimeException("tree node exist children ");
			}
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = "cache_treenode_" + id;
				CacheFactory.remove("treenode", cacheKey);
				cacheKey = "treenode_" + id;
				CacheFactory.remove("treenode", cacheKey);
				CacheFactory.clear("treenode");
			}

			treeNodeMapper.deleteTreeNodeById(id);

		}
	}

	public TreeNode findById(String tenantId, long id) {
		TreeNode treeNode = this.getTreeNode(tenantId, id);
		if (treeNode != null) {
			if (StringUtils.equals(treeNode.getTenantId(), tenantId)) {
				return treeNode;
			}
		}
		return null;
	}

	public TreeNode findByName(String tenantId, String name) {
		TreeNodeQuery query = new TreeNodeQuery();
		query.tenantId(tenantId);
		query.name(name);
		query.setDeleteFlag(0);

		List<TreeNode> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	public List<TreeNode> getAllTreeNodeList(String tenantId) {
		TreeNodeQuery query = new TreeNodeQuery();
		query.tenantId(tenantId);
		query.setDeleteFlag(0);
		List<TreeNode> list = this.list(query);
		List<TreeNode> nodes = this.getAvailableTreeNodes(list);
		Collections.sort(nodes);
		return nodes;
	}

	public List<TreeNode> getAvailableTreeNodes(List<TreeNode> list) {
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		if (list != null && !list.isEmpty()) {
			Map<Long, TreeNode> disableMap = new HashMap<Long, TreeNode>();
			for (int i = 0, len = list.size(); i < len / 2; i++) {
				for (TreeNode tree : list) {
					if (tree.getLocked() == 1 || tree.getDeleteFlag() == 1) {
						disableMap.put(tree.getId(), tree);
						continue;
					}
					if (disableMap.get(tree.getParentId()) != null) {
						disableMap.put(tree.getId(), tree);
						continue;
					}
				}
			}
			for (TreeNode tree : list) {
				if (disableMap.get(tree.getId()) == null) {
					nodes.add(tree);
				}
			}
			disableMap.clear();
			disableMap = null;
		}
		return nodes;
	}

	public List<TreeNode> getDictoryTreeNodes(TreeNodeQuery query) {
		return treeNodeMapper.getDictoryTreeNodes(query);
	}

	/**
	 * 获取关联表树型结构
	 * 
	 * @param relationTable
	 *            表名
	 * @param relationColumn
	 *            关联字段名
	 * @param query
	 * @return
	 */
	public List<TreeNode> getRelationTreeNodes(String relationTable, String relationColumn, TreeNodeQuery query) {
		query.setRelationTable(relationTable);
		query.setRelationColumn(relationColumn);
		return treeNodeMapper.getRelationTreeNodes(query);
	}

	public void getTreeNode(List<TreeNode> treeList, String tenantId, long parentId, int deep) {
		this.loadTreeNodes(treeList, tenantId, parentId, deep);
	}

	public TreeNode getTreeNode(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		TreeNode treeNode = null;
		String cacheKey = "treenode_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("treenode", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					treeNode = TreeNodeJsonFactory.jsonToObject(json);
					if (treeNode != null) {
						return treeNode;
					}
				} catch (Exception ex) {
				}
			}
		}

		treeNode = treeNodeMapper.getTreeNodeById(id);
		if (treeNode != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("treenode", cacheKey, treeNode.toJsonObject().toJSONString());
			}
		}
		return treeNode;
	}

	public TreeNode getTreeNodeByCode(String tenantId, String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		TreeNodeQuery query = new TreeNodeQuery();
		query.tenantId(tenantId);
		query.code(code);
		query.setDeleteFlag(0);

		List<TreeNode> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * 获取某个节点及其祖先
	 * 
	 * @param id
	 * @return
	 */
	public TreeNode getTreeNodeByIdWithAncestor(String tenantId, long id) {
		TreeNode treeNode = this.getTreeNode(tenantId, id);
		if (treeNode != null) {
			if (treeNode.getParentId() > 0) {
				TreeNode parent = this.getTreeNodeByIdWithAncestor(tenantId, treeNode.getParentId());
				if (parent != null) {
					treeNode.setParent(parent);
					treeNode.setParentTree(parent);
				}
			}
		}
		return treeNode;
	}

	public int getTreeNodeCountByQueryCriteria(TreeNodeQuery query) {
		return treeNodeMapper.getTreeNodeCount(query);
	}

	public List<TreeNode> getTreeNodeList(String tenantId, long parentId) {
		TreeNodeQuery query = new TreeNodeQuery();
		query.tenantId(tenantId);
		query.setParentId(parentId);
		query.setOrderBy("  E.SORTNO asc ");
		List<TreeNode> list = this.list(query);
		List<TreeNode> trees = this.getAvailableTreeNodes(list);
		if (trees != null && !trees.isEmpty()) {
			Collections.sort(trees);
		}
		return trees;
	}

	public PageResult getTreeNodeList(String tenantId, long parentId, int pageNo, int pageSize) {
		// 计算总数
		PageResult pager = new PageResult();
		TreeNodeQuery query = new TreeNodeQuery();
		query.tenantId(tenantId);
		query.setDeleteFlag(0);
		query.parentId(parentId);

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.SORTNO asc ");

		int start = pageSize * (pageNo - 1);
		List<TreeNode> list = this.getTreeNodesByQueryCriteria(start, pageSize, query);
		Collections.sort(list);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	/**
	 * 获取全部列表
	 * 
	 * @param parent
	 * 
	 * @return List
	 */
	public List<TreeNode> getTreeNodeListWithChildren(String tenantId, long parentId) {
		TreeNode parent = this.findById(tenantId, parentId);
		TreeNodeQuery query = new TreeNodeQuery();
		query.tenantId(tenantId);
		query.setDeleteFlag(0);
		query.treeIdLike(parent.getTreeId() + "%");
		query.setOrderBy("  E.SORTNO asc ");
		List<TreeNode> list = this.list(query);
		List<TreeNode> nodes = this.getAvailableTreeNodes(list);
		return nodes;
	}

	/**
	 * 获取父节点列表，如:根目录>A>A1>A11
	 * 
	 * @param tree
	 * @param int
	 *            id
	 */
	public void getTreeNodeParent(List<TreeNode> parentList, String tenantId, long id) {
		// 查找是否有父节点
		TreeNode bean = findById(tenantId, id);
		if (bean != null) {
			if (bean.getParentId() > 0) {
				getTreeNodeParent(parentList, tenantId, bean.getParentId());
			}
			parentList.add(bean);
		}
	}

	public List<TreeNode> getTreeNodes(TreeNodeQuery query) {
		List<TreeNode> list = treeNodeMapper.getTreeNodes(query);
		if (list != null && !list.isEmpty()) {
			StringTokenizer token = null;
			for (TreeNode tree : list) {
				tree.setLevel(0);
				if (StringUtils.isNotEmpty(tree.getTreeId())) {
					token = new StringTokenizer(tree.getTreeId(), "|");
					while (token.hasMoreTokens()) {
						if (StringUtils.isNotEmpty(token.nextToken())) {
							tree.setLevel(tree.getLevel() + 1);
						}
					}
				}
			}
		}
		return list;
	}

	public List<TreeNode> getTreeNodesByQueryCriteria(int start, int pageSize, TreeNodeQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<TreeNode> rows = sqlSessionTemplate.selectList("getTreeNodes", query, rowBounds);
		Collections.sort(rows);
		return rows;
	}

	protected String getTreeId(Map<Long, TreeNode> dataMap, TreeNode tree) {
		long parentId = tree.getParentId();
		long id = tree.getId();
		TreeNode parent = dataMap.get(parentId);
		if (parent != null && parent.getId() != 0) {
			if (StringUtils.isEmpty(parent.getTreeId())) {
				return getTreeId(dataMap, parent) + id + "|";
			}
			if (!parent.getTreeId().endsWith("|")) {
				parent.setTreeId(parent.getTreeId() + "|");
			}
			return parent.getTreeId() + id + "|";
		}
		return tree.getTreeId();
	}

	public List<TreeNode> list(TreeNodeQuery query) {
		List<TreeNode> list = treeNodeMapper.getTreeNodes(query);
		return list;
	}

	public void loadChildren(List<TreeNode> treeList, String tenantId, long parentId) {
		logger.debug("--------------loadChildren---------");
		TreeNode root = this.findById(tenantId, parentId);
		if (root != null) {
			if (StringUtils.isNotEmpty(root.getTreeId())) {
				TreeNodeQuery query = new TreeNodeQuery();
				query.setDeleteFlag(0);
				query.treeIdLike(root.getTreeId() + "%");
				query.setOrderBy(" E.TREEID asc ");
				List<TreeNode> list = this.list(query);
				List<TreeNode> nodes = this.getAvailableTreeNodes(list);
				if (nodes != null && !nodes.isEmpty()) {
					Iterator<TreeNode> iter = nodes.iterator();
					while (iter.hasNext()) {
						TreeNode bean = iter.next();
						if (bean.getId() != parentId) {
							treeList.add(bean);
						}
					}
				}
			} else {
				TreeNodeQuery query = new TreeNodeQuery();
				query.setDeleteFlag(0);
				query.setParentId(parentId);
				List<TreeNode> list = this.list(query);
				List<TreeNode> nodes = this.getAvailableTreeNodes(list);
				if (nodes != null && !nodes.isEmpty()) {
					Iterator<TreeNode> iter = nodes.iterator();
					while (iter.hasNext()) {
						TreeNode bean = iter.next();
						treeList.add(bean);// 加入到数组
						loadChildren(treeList, tenantId, bean.getId());// 递归遍历
					}
				}
			}
		}
	}

	public void loadTreeNodes(List<TreeNode> treeList, String tenantId, long parentId, int deep) {
		logger.debug("--------------loadTreeNodes---------------");
		TreeNode root = this.findById(tenantId, parentId);
		if (root != null) {
			if (StringUtils.isNotEmpty(root.getTreeId())) {
				TreeNodeQuery query = new TreeNodeQuery();
				query.treeIdLike(root.getTreeId() + "%");
				query.setOrderBy(" E.TREEID asc ");
				List<TreeNode> list = this.list(query);
				List<TreeNode> nodes = this.getAvailableTreeNodes(list);
				if (nodes != null && !nodes.isEmpty()) {
					Iterator<TreeNode> iter = nodes.iterator();
					while (iter.hasNext()) {
						TreeNode bean = iter.next();
						if (bean.getId() != parentId) {
							String treeId = bean.getTreeId();
							String tmp = treeId.substring(root.getTreeId().length(), treeId.length());
							StringTokenizer token = new StringTokenizer(tmp, "|");
							bean.setLevel(token.countTokens());
							treeList.add(bean);// 加入到数组
							// logger.debug("organization level:" +
							// bean.getDeep());
						}
					}
				}
			} else {
				TreeNodeQuery query = new TreeNodeQuery();
				query.setParentId(parentId);
				List<TreeNode> list = this.list(query);
				List<TreeNode> nodes = this.getAvailableTreeNodes(list);
				if (nodes != null && !nodes.isEmpty()) {
					Iterator<TreeNode> iter = nodes.iterator();
					while (iter.hasNext()) {
						TreeNode bean = iter.next();
						bean.setLevel(deep + 1);
						treeList.add(bean);// 加入到数组
						loadTreeNodes(treeList, bean.getTenantId(), bean.getId(), bean.getLevel());// 递归遍历
					}
				}
			}
		}
	}

	@Transactional
	public void save(TreeNode bean) {
		String parentTreeId = null;
		if (bean.getParentId() != 0) {
			TreeNode parent = this.findById(bean.getTenantId(), bean.getParentId());
			if (parent != null) {
				if (bean.getDiscriminator() == null) {
					bean.setDiscriminator(parent.getDiscriminator());
				}
				if (bean.getCacheFlag() == null) {
					bean.setCacheFlag(parent.getCacheFlag());
				}
				if (StringUtils.isNotEmpty(parent.getTreeId())) {
					parentTreeId = parent.getTreeId();
				}
			}
		}

		if (bean.getId() == 0) {
			bean.setSort(1);
			bean.setId(idGenerator.nextId());
			bean.setCreateDate(new Date());
			if (parentTreeId != null) {
				bean.setTreeId(parentTreeId + bean.getId() + "|");
			} else {
				bean.setTreeId(bean.getId() + "|");
			}
			treeNodeMapper.insertTreeNode(bean);

			if (bean.getParentId() == 4 && bean.getCode() != null) {// 基础数据

			}
		} else {
			bean.setUpdateDate(new Date());

			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = "treenode_" + bean.getId();
				CacheFactory.remove("treenode", cacheKey);
				cacheKey = "cache_treenode_" + bean.getId();
				CacheFactory.remove("treenode", cacheKey);
				CacheFactory.clear("treenode");
			}

			this.update(bean);

		}
	}

	@Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Resource
	public void setTreeNodeMapper(TreeNodeMapper treeNodeMapper) {
		this.treeNodeMapper = treeNodeMapper;
	}

	@Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@Transactional
	public boolean update(TreeNode bean) {

		TreeNode model = this.findById(bean.getTenantId(), bean.getId());

		/**
		 * 如果节点移动了位置，即移动到别的节点下面去了
		 */
		if (model.getParentId() != bean.getParentId()) {
			List<TreeNode> list = new ArrayList<TreeNode>();
			this.loadChildren(list, bean.getTenantId(), bean.getId());
			if (!list.isEmpty()) {
				for (TreeNode node : list) {
					/**
					 * 不能移动到ta自己的子节点下面去
					 */
					if (bean.getParentId() == node.getId()) {
						throw new RuntimeException("Can't change node into children");
					}
				}
				/**
				 * 修正所有子节点的treeId
				 */
				TreeNode oldParent = this.findById(model.getTenantId(), model.getParentId());
				TreeNode newParent = this.findById(bean.getTenantId(), bean.getParentId());
				if (oldParent != null && newParent != null && StringUtils.isNotEmpty(oldParent.getTreeId())
						&& StringUtils.isNotEmpty(newParent.getTreeId())) {
					TableModel tableModel = new TableModel();
					tableModel.setTableName("T_TREENODE");
					ColumnModel idColumn = new ColumnModel();
					idColumn.setColumnName("ID");
					idColumn.setJavaType("Long");
					tableModel.setIdColumn(idColumn);

					ColumnModel treeColumn = new ColumnModel();
					treeColumn.setColumnName("TREEID");
					treeColumn.setJavaType("String");
					tableModel.addColumn(treeColumn);

					for (TreeNode node : list) {
						String treeId = node.getTreeId();
						if (StringUtils.isNotEmpty(treeId)) {
							treeId = StringTools.replace(treeId, oldParent.getTreeId(), newParent.getTreeId());
							idColumn.setValue(node.getId());
							treeColumn.setValue(treeId);
							tableDataService.updateTableData(tableModel);
						}
					}
				}
			}
		}

		if (bean.getParentId() != 0) {
			TreeNode parent = this.findById(bean.getTenantId(), bean.getParentId());
			if (parent != null) {
				if (bean.getDiscriminator() == null) {
					bean.setDiscriminator(parent.getDiscriminator());
				}
				if (bean.getCacheFlag() == null) {
					bean.setCacheFlag(parent.getCacheFlag());
				}
				if (StringUtils.isNotEmpty(parent.getTreeId())) {
					bean.setTreeId(parent.getTreeId() + bean.getId() + "|");
				}
			}
		}

		treeNodeMapper.updateTreeNode(bean);

		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "treenode_" + bean.getId();
			CacheFactory.remove("treenode", cacheKey);
			cacheKey = "cache_treenode_" + bean.getId();
			CacheFactory.remove("treenode", cacheKey);
			CacheFactory.clear("treenode");
		}

		return true;
	}

	@Transactional
	public void updateTreeIds(String tenantId) {
		TreeNode root = this.findById(tenantId, 1);
		if (root != null) {
			if (!StringUtils.equals(root.getTreeId(), "1|")) {
				root.setTreeId("1|");
				this.update(root);
			}
			List<TreeNode> trees = this.getAllTreeNodeList(tenantId);
			if (trees != null && !trees.isEmpty()) {
				Map<Long, TreeNode> dataMap = new HashMap<Long, TreeNode>();
				for (TreeNode tree : trees) {
					dataMap.put(tree.getId(), tree);
				}
				Map<Long, String> treeIdMap = new HashMap<Long, String>();
				for (TreeNode tree : trees) {
					String cacheKey = "treenode_" + tree.getId();
					CacheFactory.remove("treenode", cacheKey);
					if (StringUtils.isEmpty(tree.getTreeId())) {
						String treeId = this.getTreeId(dataMap, tree);
						if (treeId != null && treeId.endsWith("|")) {
							treeIdMap.put(tree.getId(), treeId);
						}
					}
				}
				this.updateTreeIds(treeIdMap);
			}
		}
	}

	/**
	 * 更新树的treeId字段
	 * 
	 * @param treeMap
	 */
	@Transactional
	public void updateTreeIds(Map<Long, String> treeMap) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear("treenode");
		}

		TableModel tableModel = new TableModel();
		tableModel.setTableName("T_TREENODE");
		ColumnModel idColumn = new ColumnModel();
		idColumn.setColumnName("ID");
		idColumn.setJavaType("Long");
		tableModel.setIdColumn(idColumn);

		ColumnModel treeColumn = new ColumnModel();
		treeColumn.setColumnName("TREEID");
		treeColumn.setJavaType("String");
		tableModel.addColumn(treeColumn);

		Iterator<Long> iterator = treeMap.keySet().iterator();
		while (iterator.hasNext()) {
			Long id = iterator.next();
			String value = treeMap.get(id);
			if (value != null) {
				idColumn.setValue(id);
				treeColumn.setValue(value);
				tableDataService.updateTableData(tableModel);
			}
		}
	}

}
