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

package com.glaf.base.modules.sys.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.modules.sys.model.*;
import com.glaf.base.modules.sys.query.*;

@Transactional(readOnly = true)
public interface TreePermissionService {

	@Transactional
	void bulkInsert(List<TreePermission> list);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(Long id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<Long> ids);

	/**
	 * 获取某个用户某种权限的节点集合
	 * 
	 * @param userId
	 * @param privilege
	 * @return
	 */
	List<Long> getNodeIds(String userId, String privilege);

	/**
	 * 获取某个租户某种类型权限的节点集合
	 * 
	 * @param tenantId
	 * @param type
	 * @return
	 */
	List<Long> getTenantNodeIds(String tenantId, String type);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	TreePermission getTreePermission(Long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getTreePermissionCountByQueryCriteria(TreePermissionQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<TreePermission> getTreePermissionsByQueryCriteria(int start, int pageSize, TreePermissionQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<TreePermission> list(TreePermissionQuery query);

	/**
	 * 保存记录
	 * 
	 * @return
	 */
	@Transactional
	void saveAll(String userId, String type, String privilege, List<TreePermission> treePermissions);

	@Transactional
	void saveTenantAll(String tenantId, String type, String privilege, List<TreePermission> treePermissions);
}
