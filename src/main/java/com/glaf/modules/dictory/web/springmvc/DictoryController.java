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

package com.glaf.modules.dictory.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.utils.ParamUtil;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.DictoryDefinition;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.DictoryDefinitionService;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.core.util.ZipUtils;

import com.glaf.modules.dictory.model.Dictory;
import com.glaf.modules.dictory.query.DictoryQuery;
import com.glaf.modules.dictory.service.DictoryService;
import com.glaf.modules.dictory.util.DictoryJsonFactory;
import com.glaf.modules.tree.model.TreeNode;
import com.glaf.modules.tree.query.TreeNodeQuery;
import com.glaf.modules.tree.service.TreeNodeService;
import com.glaf.modules.tree.util.TreeNodeJsonFactory;

@Controller("/mydictory")
@RequestMapping("/mydictory")
public class DictoryController {
	private static final Log logger = LogFactory.getLog(DictoryController.class);

	protected DictoryDefinitionService dictoryDefinitionService;

	protected DictoryService dictoryService;

	protected TreeNodeService treeNodeService;

	/**
	 * 提交删除
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public byte[] batchDelete(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		try {
			long[] ids = ParamUtil.getLongParameterValues(request, "id");
			for (long id : ids) {
				Dictory bean = dictoryService.find(id);
				if (StringUtils.equals(bean.getTenantId(), loginContext.getTenantId())) {
					dictoryService.delete(id);
				}
			}
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/choose")
	public ModelAndView choose(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("mydictory.choose");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/modules/dictory/choose", modelMap);
	}

	@RequestMapping("/exportJson")
	@ResponseBody
	public void exportJson(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		int parentId = ParamUtil.getIntParameter(request, "parentId", 0);
		if (parentId > 0) {
			TreeNode tree = treeNodeService.findById(loginContext.getTenantId(), parentId);
			if (tree != null) {
				if (StringUtils.equals(tree.getCode(), "DICTORY")) {
					TreeNodeQuery query = new TreeNodeQuery();
					query.setLocked(0);
					List<TreeNode> trees = treeNodeService.getDictoryTreeNodes(query);
					if (trees != null && trees.size() > 0) {
						Map<String, byte[]> zipMap = new HashMap<String, byte[]>();
						try {
							for (TreeNode t : trees) {
								JSONObject result = t.toJsonObject();
								result.remove("id");
								result.remove("_id_");
								result.remove("_oid_");
								result.remove("treeId");
								result.remove("createBy");
								result.remove("createDate");
								result.remove("updateBy");
								result.remove("updateDate");
								List<Dictory> list = dictoryService.getDictoryList(loginContext.getTenantId(),
										t.getId());
								JSONArray array = DictoryJsonFactory.listToArray(list);
								result.put("dicts", array);
								zipMap.put(t.getName() + ".json", result.toJSONString().getBytes("UTF-8"));
							}
							byte[] zipBytes = ZipUtils.toZipBytes(zipMap);
							ResponseUtils.download(request, response, zipBytes, "dicts.zip");
						} catch (IOException e) {
						} catch (ServletException e) {
						}
					}
				} else {
					JSONObject result = tree.toJsonObject();
					result.remove("id");
					result.remove("_id_");
					result.remove("_oid_");
					result.remove("treeId");
					result.remove("createBy");
					result.remove("createDate");
					result.remove("updateBy");
					result.remove("updateDate");
					try {
						List<Dictory> list = dictoryService.getDictoryList(loginContext.getTenantId(), parentId);
						JSONArray array = DictoryJsonFactory.listToArray(list);
						result.put("dicts", array);
						ResponseUtils.download(request, response, result.toJSONString().getBytes("UTF-8"),
								parentId + ".json");
					} catch (IOException e) {
					} catch (ServletException e) {
					}
				}
			}
		}
	}

	protected void importData(LoginContext loginContext, byte[] bytes) throws IOException {
		JSONObject json = JSON.parseObject(new String(bytes, "UTF-8"));
		TreeNode tree = TreeNodeJsonFactory.jsonToObject(json);
		if (treeNodeService.getTreeNodeByCode(loginContext.getTenantId(), tree.getCode()) == null) {
			TreeNode parent = treeNodeService.getTreeNodeByCode(loginContext.getTenantId(), "DICTORY");
			if (parent != null) {
				tree.setId(0);
				tree.setParent(parent);
				tree.setParentId(parent.getId());
				tree.setTreeId(null);
				tree.setUpdateBy(null);
				tree.setUpdateDate(null);
				treeNodeService.create(tree);
				logger.debug("create tree......");
			}

			tree = treeNodeService.getTreeNodeByCode(loginContext.getTenantId(), tree.getCode());
			logger.debug("find tree......");

			if (tree != null && json.containsKey("dicts")) {
				logger.debug("tree:" + tree.toJsonObject().toJSONString());
				JSONArray array = json.getJSONArray("dicts");
				if (array != null && array.size() > 0) {
					logger.debug("dicts size:" + array.size());
					List<Dictory> rows = dictoryService.getDictoryList(loginContext.getTenantId(), tree.getId());
					List<Dictory> list = DictoryJsonFactory.arrayToList(array);
					if (list != null && list.size() > 0) {
						for (Dictory dict : list) {
							boolean create = true;
							if (rows != null && rows.size() > 0) {
								for (Dictory d : rows) {
									if (StringUtils.equalsIgnoreCase(d.getCode(), dict.getCode())) {
										create = false;
										break;
									}
								}
							}
							if (create) {
								dict.setId(0);
								dict.setNodeId(tree.getId());
								dict.setCreateBy(loginContext.getActorId());
								dictoryService.create(dict);
							}
						}
					}
				}
			}
		}
	}

	@RequestMapping("/importJson")
	@ResponseBody
	public byte[] importJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		ZipInputStream zipInputStream = null;
		boolean status = false;
		// 将当前上下文初始化给 CommonsMutipartResolver（多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			try {
				MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = req.getFileMap();
				Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
				int maxSize = 5 * FileUtils.MB_SIZE;
				for (Entry<String, MultipartFile> entry : entrySet) {
					MultipartFile mFile = entry.getValue();
					if (mFile.getOriginalFilename() != null && mFile.getSize() > 0) {
						String filename = mFile.getOriginalFilename();
						logger.debug("upload file:" + filename);
						logger.debug("fize size:" + mFile.getSize());
						if (StringUtils.endsWithIgnoreCase(filename, ".json") && mFile.getSize() < maxSize) {
							this.importData(loginContext, mFile.getBytes());
						} else if (StringUtils.endsWithIgnoreCase(filename, ".zip") && mFile.getSize() < maxSize) {
							zipInputStream = new ZipInputStream(mFile.getInputStream());
							Map<String, byte[]> zipMap = ZipUtils.getZipBytesMap(zipInputStream);
							if (zipMap != null && !zipMap.isEmpty()) {
								Set<Entry<String, byte[]>> entrySet2 = zipMap.entrySet();
								for (Entry<String, byte[]> entry2 : entrySet2) {
									String key = entry2.getKey();
									if (StringUtils.endsWithIgnoreCase(key, ".json")) {
										byte[] value = entry2.getValue();
										try {
											this.importData(loginContext, value);
											return ResponseUtils.responseResult(true);
										} catch (Exception ex) {
											status = false;
											logger.error("error import json data ", ex);
										}
									}
								}
							}
						}
					}
				}
				status = true;
			} catch (Exception ex) {
				status = false;
				logger.error("error import data ", ex);
			} finally {
				IOUtils.closeStream(zipInputStream);
			}
		}
		return ResponseUtils.responseResult(status);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug(params);
		DictoryQuery query = new DictoryQuery();
		query.setNodeId(-1L);
		Tools.populate(query, params);
		query.tenantId(loginContext.getTenantId());

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
		int total = dictoryService.getDictoryCountByQueryCriteria(query);
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

			List<Dictory> list = dictoryService.getDictorysByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Dictory dictory : list) {
					JSONObject rowJSON = dictory.toJsonObject();
					rowJSON.put("id", dictory.getId());
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

		String x_view = ViewProperties.getString("mydictory.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/modules/dictory/list", modelMap);
	}

	/**
	 * 显示增加字典页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareAdd")
	public ModelAndView prepareAdd(HttpServletRequest request, ModelMap modelMap) {
		// 显示列表页面
		RequestUtils.setRequestParameterToAttribute(request);
		request.removeAttribute("parent");
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		Long nodeId = ParamUtils.getLong(params, "nodeId");
		if (nodeId > 0) {
			List<DictoryDefinition> list = dictoryDefinitionService.getDictoryDefinitions(nodeId, "T_DICTORY");
			if (list != null && !list.isEmpty()) {
				Collections.sort(list);
				request.setAttribute("dicts", list);
			}
		}

		String x_view = ViewProperties.getString("mydictory.prepareAdd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/dictory/dictory_add", modelMap);
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
		request.removeAttribute("parent");
		long id = ParamUtil.getIntParameter(request, "id", 0);
		Dictory bean = dictoryService.find(id);
		request.setAttribute("dictory", bean);

		long nodeId = ParamUtil.getLongParameter(request, "parentId", 0);
		if (bean != null && bean.getNodeId() > 0) {
			nodeId = bean.getNodeId();
		}
		if (nodeId > 0) {
			List<DictoryDefinition> list = dictoryDefinitionService.getDictoryDefinitions(nodeId, "T_DICTORY");
			if (list != null && !list.isEmpty()) {
				if (bean != null) {
					Map<String, Object> dataMap = Tools.getDataMap(bean);
					for (DictoryDefinition d : list) {
						Object value = dataMap.get(d.getName());
						d.setValue(value);
					}
				}
				Collections.sort(list);
				request.setAttribute("dicts", list);
			}
		}

		TreeNode parent = treeNodeService.getTreeNodeByCode(loginContext.getTenantId(), "DICTORY");
		List<TreeNode> list = new ArrayList<TreeNode>();
		parent.setLevel(0);
		list.add(parent);
		treeNodeService.getTreeNode(list, loginContext.getTenantId(), parent.getId(), 1);
		request.setAttribute("parent", list);

		String x_view = ViewProperties.getString("mydictory.prepareModify");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/dictory/dictory_modify", modelMap);
	}

	/**
	 * 提交增加字典信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveAdd")
	@ResponseBody
	public byte[] saveAdd(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		Dictory bean = new Dictory();
		try {
			Tools.populate(bean, params);
			bean.setTenantId(loginContext.getTenantId());
			dictoryService.create(bean);
			bean.setCreateBy(RequestUtils.getActorId(request));
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交修改字典信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveModify")
	@ResponseBody
	public byte[] saveModify(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		long id = ParamUtil.getIntParameter(request, "id", 0);
		logger.debug("params:" + params);
		try {
			Dictory bean = dictoryService.find(id);
			if (StringUtils.equals(bean.getTenantId(), loginContext.getTenantId())) {
				Tools.populate(bean, params);
				bean.setUpdateBy(RequestUtils.getActorId(request));
				if (dictoryService.update(bean)) {// 保存成功
					return ResponseUtils.responseResult(true);
				}
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
					t1.setTableName("T_DICTORY");
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

	@javax.annotation.Resource
	public void setDictoryDefinitionService(DictoryDefinitionService dictoryDefinitionService) {
		this.dictoryDefinitionService = dictoryDefinitionService;
	}

	@javax.annotation.Resource(name = "com.glaf.modules.dictory.service.dictoryService")
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.modules.tree.service.treeNodeService")
	public void setTreeNodeService(TreeNodeService treeNodeService) {
		this.treeNodeService = treeNodeService;
	}

	/**
	 * 显示框架页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showFrame")
	public ModelAndView showFrame(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		TreeNode bean = treeNodeService.getTreeNodeByCode(loginContext.getTenantId(), "DICTORY");
		request.setAttribute("parent", bean.getId() + "");

		String x_view = ViewProperties.getString("mydictory.showFrame");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/dictory/dictory_frame", modelMap);
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

		String x_view = ViewProperties.getString("mydictory.showImport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		// 显示列表页面
		return new ModelAndView("/modules/dictory/showImport", modelMap);
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
		int parent = ParamUtil.getIntParameter(request, "parent", 0);
		int pageNo = ParamUtil.getIntParameter(request, "page_no", 1);
		int pageSize = ParamUtil.getIntParameter(request, "page_size", 10);
		PageResult pager = dictoryService.getDictoryList(loginContext.getTenantId(), parent, pageNo, pageSize);
		request.setAttribute("pager", pager);

		String x_view = ViewProperties.getString("mydictory.showList");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		// 显示列表页面
		return new ModelAndView("/modules/dictory/dictory_list", modelMap);
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
		RequestUtils.setRequestParameterToAttribute(request);
		long parentId = ParamUtil.getIntParameter(request, "parentId", 0);
		if (parentId > 0) {
			List<Dictory> list = dictoryService.getDictoryList(loginContext.getTenantId(), parentId);
			request.setAttribute("trees", list);
		}

		String x_view = ViewProperties.getString("sys.mydictory.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/dictory/showSort", modelMap);
	}
}