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

package com.glaf.modules.tree.bean;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import com.glaf.modules.tree.model.TreeNode;
import com.glaf.modules.tree.service.TreeNodeService;
import com.glaf.core.base.TreeModel;

public class UpdateTreeBean {

	protected TreeNodeService treeNodeService;

	public String getTreeId(Map<Long, TreeModel> treeMap, TreeModel tree) {
		long parentId = tree.getParentId();
		long id = tree.getId();
		TreeModel parent = treeMap.get(parentId);
		if (parent != null && parent.getId() != 0) {
			if (StringUtils.isEmpty(parent.getTreeId())) {
				return getTreeId(treeMap, parent) + id + "|";
			}
			if (!parent.getTreeId().endsWith("|")) {
				parent.setTreeId(parent.getTreeId() + "|");
			}
			if (parent.getTreeId() != null) {
				return parent.getTreeId() + id + "|";
			}
		}
		return tree.getTreeId();
	}

	public String getTreeId(Map<Long, TreeNode> dataMap, TreeNode tree) {
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
			if (parent.getTreeId() != null) {
				return parent.getTreeId() + id + "|";
			}
		}
		return tree.getTreeId();
	}

	public void setTreeNodeService(TreeNodeService treeNodeService) {
		this.treeNodeService = treeNodeService;
	}

}
