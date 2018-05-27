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

package com.glaf.modules.tree.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.glaf.base.utils.ParamUtil;
import com.glaf.core.base.BaseTree;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.tree.helper.JacksonTreeHelper;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.modules.tree.bean.UpdateTreeBean;
import com.glaf.modules.tree.model.TreeNode;
import com.glaf.modules.tree.query.TreeNodeQuery;
import com.glaf.modules.tree.service.TreeNodeService;

@Controller("/mytreenode")
@RequestMapping("/mytreenode")
public class TreeNodeController {
	private static final Log logger = LogFactory.getLog(TreeNodeController.class);

	protected TreeNodeService treeNodeService;

	@ResponseBody
	@RequestMapping("/deleteById")
	public byte[] deleteById(HttpServletRequest request, @RequestParam(value = "id") long id) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		try {
			TreeNode bean = treeNodeService.findById(loginContext.getTenantId(), id);
			if (bean != null && StringUtils.equals(bean.getTenantId(), loginContext.getTenantId())) {
				List<TreeNode> trees = treeNodeService.getTreeNodeListWithChildren(loginContext.getTenantId(), id);
				if (trees != null && trees.size() > 1) {
					return ResponseUtils.responseJsonResult(false, "不能删除存在子节点的分类");
				}

				bean.setDeleteFlag(1);
				bean.setDeleteTime(new Date());
				boolean result = treeNodeService.update(bean);
				return ResponseUtils.responseResult(result);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 显示修改页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		long id = ParamUtil.getIntParameter(request, "id", 0);
		TreeNode tree = treeNodeService.findById(loginContext.getTenantId(), id);
		if (tree != null && StringUtils.equals(loginContext.getTenantId(), tree.getTenantId())) {
			if (tree.getParentId() > 0) {
				TreeNode parent = treeNodeService.findById(loginContext.getTenantId(), tree.getParentId());
				tree.setParent(parent);
				request.setAttribute("parentId", tree.getParentId());
			}
			request.setAttribute("tree", tree);
		}

		List<TreeNode> list = treeNodeService.getAllTreeNodeList(loginContext.getTenantId());
		if (list != null && !list.isEmpty()) {
			Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
			List<TreeModel> treeModels = new ArrayList<TreeModel>();
			for (TreeNode app2 : list) {
				treeModels.add(app2);
				treeMap.put(app2.getId(), app2);
			}
			StringTokenizer token = null;
			StringBuilder blank = new StringBuilder();
			UpdateTreeBean bean = new UpdateTreeBean();
			List<TreeNode> appList = new ArrayList<TreeNode>();
			for (TreeNode app2 : list) {
				String treeId = bean.getTreeId(treeMap, app2);
				if (treeId != null && treeId.indexOf("|") != -1) {
					token = new StringTokenizer(treeId, "|");
					if (token != null) {
						app2.setLevel(token.countTokens());
						blank.delete(0, blank.length());
						for (int i = 0; i < app2.getLevel(); i++) {
							blank.append("&nbsp;&nbsp;&nbsp;&nbsp;");
						}
						app2.setBlank(blank.toString());
					}
				}
				appList.add(app2);
			}
			request.setAttribute("trees", appList);
		}

		String x_view = ViewProperties.getString("treenode.prepareModify");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/treenode/edit", modelMap);
	}

	@RequestMapping("/exportTrees")
	public void exportTrees(HttpServletRequest request, HttpServletResponse response) {
		String objectIds = request.getParameter("nodeIds");
		List<Long> nodeIds = StringTools.splitToLong(objectIds);
		if (nodeIds != null && !nodeIds.isEmpty()) {
			StringBuilder buffer = new StringBuilder();
			TreeNodeQuery query = new TreeNodeQuery();
			query.locked(0);
			query.setDeleteFlag(0);
			query.nodeIds(nodeIds);
			List<TreeNode> trees = treeNodeService.getTreeNodesByQueryCriteria(0, 50000, query);
			if (trees != null && !trees.isEmpty()) {
				for (TreeNode tree : trees) {
					if (nodeIds.contains(tree.getId())) {
						buffer.append(
								"insert into T_TREENODE (id, parent, name, nodedesc, sort, code, discriminator, treeid, moveable, icon, iconCls, url, ALLOWEDFILEEXTS, ALLOWEDFIZESIZE, PROVIDERCLASS) values (");
						buffer.append(tree.getId()).append(",");
						buffer.append(tree.getParentId()).append(",");

						if (StringUtils.isNotEmpty(tree.getName())) {
							String text = tree.getName();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						if (StringUtils.isNotEmpty(tree.getDesc())) {
							String text = tree.getDesc();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						buffer.append(tree.getSort()).append(", ");

						if (StringUtils.isNotEmpty(tree.getCode())) {
							String text = tree.getCode();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						if (StringUtils.isNotEmpty(tree.getDiscriminator())) {
							String text = tree.getDiscriminator();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						if (StringUtils.isNotEmpty(tree.getTreeId())) {
							String text = tree.getTreeId();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						if (StringUtils.isNotEmpty(tree.getMoveable())) {
							String text = tree.getMoveable();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						if (StringUtils.isNotEmpty(tree.getIcon())) {
							String text = tree.getIcon();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						if (StringUtils.isNotEmpty(tree.getIconCls())) {
							String text = tree.getIconCls();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						if (StringUtils.isNotEmpty(tree.getUrl())) {
							String text = tree.getUrl();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						if (StringUtils.isNotEmpty(tree.getAllowedFileExts())) {
							String text = tree.getAllowedFileExts();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("', ");
						} else {
							buffer.append("null, ");
						}

						buffer.append(tree.getAllowedFizeSize()).append(", ");

						if (StringUtils.isNotEmpty(tree.getProviderClass())) {
							String text = tree.getProviderClass();
							text = StringTools.replace(text, "'", "\'");
							buffer.append("'").append(text).append("' ");
						} else {
							buffer.append("null ");
						}

						buffer.append(");");
						buffer.append(FileUtils.newline);
					}
				}
			}

			try {
				ResponseUtils.download(request, response, buffer.toString().getBytes("UTF-8"), "T_TREENODE.insert.sql");
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 显示下级节点
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/getSubTree")
	public ModelAndView getSubTree(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		int id = ParamUtil.getIntParameter(request, "id", 0);
		List<TreeNode> list = treeNodeService.getTreeNodeList(loginContext.getTenantId(), id);
		Collections.sort(list);
		request.setAttribute("list", list);

		String x_view = ViewProperties.getString("treenode.getSubTree");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/treenode/subtree_list", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		TreeNodeQuery query = new TreeNodeQuery();
		Tools.populate(query, params);
		query.setDeleteFlag(0);

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtils.getString(params, "sortName");
		order = ParamUtils.getString(params, "sortOrder");

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = treeNodeService.getTreeNodeCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortOrder(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<TreeNode> list = treeNodeService.getTreeNodesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TreeNode treeNode : list) {
					JSONObject rowJSON = treeNode.toJsonObject();
					rowJSON.put("id", treeNode.getId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			result.put("total", total);
			result.put("totalCount", total);
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
		}
		return result.toString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("treenode.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/modules/treenode/list", modelMap);
	}

	/**
	 * 显示增加页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareAdd")
	public ModelAndView prepareAdd(HttpServletRequest request, ModelMap modelMap) {
		request.setAttribute("contextPath", request.getContextPath());

		String x_view = ViewProperties.getString("treenode.prepareAdd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/treenode/tree_add", modelMap);
	}

	/**
	 * 显示修改页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareModify")
	public ModelAndView prepareModify(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		long id = ParamUtil.getIntParameter(request, "id", 0);
		TreeNode bean = treeNodeService.findById(loginContext.getTenantId(), id);
		if (bean != null && bean.getParentId() > 0) {
			TreeNode parent = treeNodeService.findById(loginContext.getTenantId(), bean.getParentId());
			bean.setParent(parent);
		}
		request.setAttribute("bean", bean);
		List<TreeNode> list = new ArrayList<TreeNode>();
		treeNodeService.getTreeNode(list, loginContext.getTenantId(), 1, 0);
		request.setAttribute("parent", list);

		String x_view = ViewProperties.getString("treenode.prepareModify");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/treenode/tree_modify", modelMap);
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public byte[] save(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		TreeNode bean = null;
		boolean insert = false;
		long id = RequestUtils.getLong(request, "id");
		try {
			if (id > 0) {
				bean = treeNodeService.findById(loginContext.getTenantId(), id);
			}
			if (bean == null) {
				bean = new TreeNode();
				insert = true;
			}
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			Tools.populate(bean, params);
			bean.setTenantId(loginContext.getTenantId());
			bean.setParentId(ParamUtil.getIntParameter(request, "parentId", 0));
			bean.setName(ParamUtil.getParameter(request, "name"));
			bean.setDesc(ParamUtil.getParameter(request, "desc"));
			bean.setCode(ParamUtil.getParameter(request, "code"));
			bean.setValue(ParamUtil.getParameter(request, "value"));
			bean.setCreateBy(RequestUtils.getActorId(request));
			bean.setUpdateBy(RequestUtils.getActorId(request));
			if (insert) {
				boolean ret = treeNodeService.create(bean);
				return ResponseUtils.responseResult(ret);
			} else {
				boolean ret = treeNodeService.update(bean);
				return ResponseUtils.responseResult(ret);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveAdd")
	@ResponseBody
	public byte[] saveAdd(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		TreeNode bean = new TreeNode();
		try {
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			Tools.populate(bean, params);
			bean.setTenantId(loginContext.getTenantId());
			bean.setParentId(ParamUtil.getIntParameter(request, "parentId", 0));
			bean.setName(ParamUtil.getParameter(request, "name"));
			bean.setDesc(ParamUtil.getParameter(request, "desc"));
			bean.setCode(ParamUtil.getParameter(request, "code"));
			bean.setValue(ParamUtil.getParameter(request, "value"));
			bean.setCreateBy(RequestUtils.getActorId(request));
			bean.setUpdateBy(RequestUtils.getActorId(request));
			boolean ret = treeNodeService.create(bean);
			return ResponseUtils.responseResult(ret);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交修改信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveModify")
	@ResponseBody
	public byte[] saveModify(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long id = ParamUtil.getIntParameter(request, "id", 0);
		try {
			TreeNode bean = treeNodeService.findById(loginContext.getTenantId(), id);
			if (bean != null) {
				Map<String, Object> params = RequestUtils.getParameterMap(request);
				Tools.populate(bean, params);
				bean.setParentId(ParamUtil.getIntParameter(request, "parentId", 0));
				bean.setName(ParamUtil.getParameter(request, "name"));
				bean.setDesc(ParamUtil.getParameter(request, "desc"));
				bean.setCode(ParamUtil.getParameter(request, "code"));
				bean.setValue(ParamUtil.getParameter(request, "value"));
				bean.setSort(ParamUtil.getIntParameter(request, "sort", 50));
				bean.setUpdateBy(RequestUtils.getActorId(request));
				treeNodeService.update(bean);
				return ResponseUtils.responseResult(true);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 排序
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/saveSort")
	@ResponseBody
	public byte[] saveSort(HttpServletRequest request) {
		String items = request.getParameter("items");
		if (StringUtils.isNotEmpty(items)) {
			int sort = 0;
			List<TableModel> rows = new ArrayList<TableModel>();
			StringTokenizer token = new StringTokenizer(items, ",");
			while (token.hasMoreTokens()) {
				String item = token.nextToken();
				if (StringUtils.isNotEmpty(item)) {
					sort++;
					TableModel t1 = new TableModel();
					t1.setTableName("T_TREENODE");
					ColumnModel idColumn1 = new ColumnModel();
					idColumn1.setColumnName("ID");
					idColumn1.setValue(Long.parseLong(item));
					t1.setIdColumn(idColumn1);
					ColumnModel column = new ColumnModel();
					column.setColumnName("SORTNO");
					column.setValue(sort);
					t1.addColumn(column);
					rows.add(t1);
				}
			}
			try {
				DataServiceFactory.getInstance().updateAllTableData(rows);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.modules.tree.service.treeNodeService")
	public void setTreeNodeService(TreeNodeService treeNodeService) {
		this.treeNodeService = treeNodeService;
	}

	/**
	 * 显示导入页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showImport")
	public ModelAndView showImport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("treenode.showImport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		// 显示列表页面
		return new ModelAndView("/modules/treenode/showImport", modelMap);
	}

	/**
	 * 显示左边菜单
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showLeft")
	public ModelAndView showLeft(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		ModelAndView forward = new ModelAndView("/modules/treenode/tree_left", modelMap);
		int parent = ParamUtil.getIntParameter(request, "parent", 0);
		request.setAttribute("parent", treeNodeService.findById(loginContext.getTenantId(), parent));
		String url = ParamUtil.getParameter(request, "url");
		request.setAttribute("url", url);

		String x_view = ViewProperties.getString("treenode.showLeft");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		// 显示列表页面
		return forward;
	}

	/**
	 * 显示所有列表
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showList")
	public ModelAndView showList(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		int parentId = ParamUtil.getIntParameter(request, "parent", 0);
		int pageNo = ParamUtil.getIntParameter(request, "page_no", 1);
		int pageSize = ParamUtil.getIntParameter(request, "page_size", 15);
		if (parentId > 0) {
			TreeNode parent = treeNodeService.findById(loginContext.getTenantId(), parentId);
			request.setAttribute("parent", parent);
		}
		PageResult pager = treeNodeService.getTreeNodeList(loginContext.getTenantId(), parentId, pageNo, pageSize);
		request.setAttribute("pager", pager);

		String x_view = ViewProperties.getString("treenode.showList");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/treenode/tree_list", modelMap);
	}

	/**
	 * 显示主页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showMain")
	public ModelAndView showMain(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("treenode.showMain");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/treenode/tree_frame", modelMap);
	}

	/**
	 * 显示排序页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showSort")
	public ModelAndView showSort(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		// RequestUtils.setRequestParameterToAttribute(request);
		long parentId = ParamUtil.getIntParameter(request, "parentId", 0);
		if (parentId > 0) {
			List<TreeNode> trees = treeNodeService.getTreeNodeList(loginContext.getTenantId(), parentId);
			request.setAttribute("trees", trees);
		}

		String x_view = ViewProperties.getString("treenode.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/treenode/showSort", modelMap);
	}

	@RequestMapping("/showTop")
	public ModelAndView showTop(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("treenode.showTop");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/treenode/tree_top", modelMap);
	}

	@RequestMapping("/showTree")
	public ModelAndView showTree(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("treenode.showTree");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/treenode/showTree", modelMap);
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("------------------------treeJson--------------------");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		List<Long> selected = new ArrayList<Long>();

		TreeNode root = null;
		long parentId = RequestUtils.getLong(request, "parentId");
		String nodeCode = request.getParameter("nodeCode");
		if (StringUtils.isNotEmpty(nodeCode)) {
			root = treeNodeService.getTreeNodeByCode(loginContext.getTenantId(), nodeCode);
		} else if (parentId > 0) {
			root = treeNodeService.findById(loginContext.getTenantId(), parentId);
		}

		List<TreeNode> trees = null;
		if (root != null) {
			trees = treeNodeService.getTreeNodeListWithChildren(loginContext.getTenantId(), root.getId());
		} else {
			trees = treeNodeService.getAllTreeNodeList(loginContext.getTenantId());
		}

		List<TreeModel> treeModels = new ArrayList<TreeModel>();
		if (trees != null && !trees.isEmpty()) {
			for (TreeNode t : trees) {
				TreeModel tree = new BaseTree();
				tree.setId(t.getId());
				tree.setParentId(t.getParentId());
				tree.setName(t.getName());
				tree.setTreeId(t.getTreeId());
				tree.setIcon(t.getIcon());
				tree.setIconCls(t.getIcon());
				tree.setDiscriminator(t.getDiscriminator());
				tree.setCode(t.getCode());
				tree.setSortNo(t.getSort());
				tree.setLevel(t.getLevel());
				Map<String, Object> dataMap = new HashMap<String, Object>();
				String url = t.getUrl();
				if (StringUtils.isNotEmpty(url)) {
					if (!(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))) {
						if (url.startsWith("/")) {
							url = request.getContextPath() + url;
						} else {
							url = request.getContextPath() + "/" + url;
						}
					}
					if (url.indexOf("?") != -1) {
						url = url + "&time=" + System.currentTimeMillis();
					} else {
						url = url + "?time=" + System.currentTimeMillis();
					}
				}
				dataMap.put("url", url);
				if (selected.contains(t.getId())) {
					tree.setChecked(true);
					dataMap.put("checked", true);
				} else {
					dataMap.put("checked", false);
				}
				tree.setDataMap(dataMap);

				treeModels.add(tree);
			}
		}

		JacksonTreeHelper treeHelper = new JacksonTreeHelper();
		ArrayNode responseJSON = treeHelper.getTreeArrayNode(treeModels);
		try {
			return responseJSON.toString().getBytes("UTF-8");
		} catch (IOException e) {
			return responseJSON.toString().getBytes();
		}

	}
}