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

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.util.PageResult;
import com.glaf.modules.tree.model.TreeNode;
import com.glaf.modules.tree.query.TreeNodeQuery;

@Transactional(readOnly = true)
public interface TreeNodeService {

	/**
	 * 保存
	 * 
	 * @param bean
	 *            TreeNode
	 * @return boolean
	 */
	@Transactional
	boolean create(TreeNode bean);

	/**
	 * 删除
	 * 
	 * @param id
	 *            int
	 * @return boolean
	 */
	@Transactional
	boolean delete(String tenantId, long id);

	/**
	 * 删除
	 * 
	 * @param bean
	 *            TreeNode
	 * @return boolean
	 */
	@Transactional
	boolean delete(TreeNode bean);

	/**
	 * 获取对象
	 * 
	 * @param id
	 * @return
	 */
	TreeNode findById(String tenantId, long id);

	/**
	 * 按名称查找对象
	 * 
	 * @param name
	 *            String
	 * @return TreeNode
	 */
	TreeNode findByName(String tenantId, String name);

	/**
	 * 获取全部列表
	 * 
	 * @return List
	 */
	List<TreeNode> getAllTreeNodeList(String tenantId);

	List<TreeNode> getAvailableTreeNodes(List<TreeNode> list);

	List<TreeNode> getDictoryTreeNodes(TreeNodeQuery query);

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
	List<TreeNode> getRelationTreeNodes(String relationTable, String relationColumn, TreeNodeQuery query);

	/**
	 * 获取树型列表
	 * 
	 * @param parent
	 *            int
	 * @return List
	 */
	void getTreeNode(List<TreeNode> treeList, String tenantId, long parentId, int deep);

	/**
	 * 按树编号获取树节点
	 * 
	 * @param tree
	 * @return TreeNode
	 */
	TreeNode getTreeNodeByCode(String tenantId, String code);

	/**
	 * 获取某个节点及其祖先
	 * 
	 * @param id
	 * @return
	 */
	TreeNode getTreeNodeByIdWithAncestor(String tenantId, long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getTreeNodeCountByQueryCriteria(TreeNodeQuery query);

	/**
	 * 获取全部列表
	 * 
	 * @param parentId
	 * 
	 * @return List
	 */
	List<TreeNode> getTreeNodeList(String tenantId, long parentId);

	/**
	 * 获取分页列表
	 * 
	 * @param parent
	 *            int
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @return
	 */
	PageResult getTreeNodeList(String tenantId, long parentId, int pageNo, int pageSize);

	/**
	 * 获取全部列表
	 * 
	 * @param parent
	 * 
	 * @return List
	 */
	List<TreeNode> getTreeNodeListWithChildren(String tenantId, long parentId);

	/**
	 * 获取父节点列表，如:根目录>A>A1>A11
	 * 
	 * @param tree
	 * @param int
	 *            id
	 */
	void getTreeNodeParent(List<TreeNode> tree, String tenantId, long id);

	List<TreeNode> getTreeNodes(TreeNodeQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<TreeNode> getTreeNodesByQueryCriteria(int start, int pageSize, TreeNodeQuery query);

	/**
	 * 获取树型列表
	 * 
	 * @param parent
	 *            int
	 * @return List
	 */
	void loadTreeNodes(List<TreeNode> treeList, String tenantId, long parentId, int deep);

	/**
	 * 更新
	 * 
	 * @param bean
	 *            TreeNode
	 * @return boolean
	 */
	@Transactional
	boolean update(TreeNode bean);

	/**
	 * 更新指定树的treeId字段
	 * 
	 * @param treeMap
	 */
	@Transactional
	void updateTreeIds(Map<Long, String> treeMap);

	/**
	 * 更新树的treeId字段
	 * 
	 * @param treeMap
	 */
	@Transactional
	void updateTreeIds(String tenantId);
}