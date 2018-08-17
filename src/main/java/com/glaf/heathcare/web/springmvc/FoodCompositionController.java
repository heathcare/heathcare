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

package com.glaf.heathcare.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.service.TreePermissionService;
import com.glaf.base.modules.sys.util.PinyinUtils;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.FoodFavorite;
import com.glaf.heathcare.helper.PermissionHelper;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.FoodFavoriteQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodFavoriteService;

import net.iharder.Base64;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/foodComposition")
@RequestMapping("/heathcare/foodComposition")
public class FoodCompositionController {
	protected static final Log logger = LogFactory.getLog(FoodCompositionController.class);

	protected FoodCompositionService foodCompositionService;

	protected FoodFavoriteService foodFavoriteService;

	protected TreePermissionService treePermissionService;

	protected SysTreeService sysTreeService;

	public FoodCompositionController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setUserPermission(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		FoodComposition foodComposition = foodCompositionService
				.getFoodComposition(RequestUtils.getLong(request, "id"));
		if (foodComposition != null) {
			request.setAttribute("foodComposition", foodComposition);
			if (StringUtils.equals(foodComposition.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("heathcare_curd_perm", true);
			}
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			if (StringUtils.isEmpty(loginContext.getTenantId()) && loginContext.getRoles().contains("Keyboarder")) {
				List<Long> nodeIds = treePermissionService.getNodeIds(loginContext.getActorId(), "rw");
				List<SysTree> list = new ArrayList<SysTree>();
				for (SysTree tree : foodCategories) {
					if (nodeIds.contains(tree.getId())) {
						list.add(tree);
					}
				}
				foodCategories = list;
			}
			request.setAttribute("foodCategories", foodCategories);
		}

		if (foodComposition != null) {
			if (!StringUtils.equals(foodComposition.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("heathcare_curd_perm", false);
			}
		}

		logger.debug("roles:" + loginContext.getRoles());
		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("heathcare_curd_perm", true);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("foodComposition.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/foodComposition/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/genJS")
	public byte[] genJS(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			try {
				FoodCompositionQuery query = new FoodCompositionQuery();
				query.locked(0);
				List<FoodComposition> list = foodCompositionService.list(query);
				if (list != null && !list.isEmpty()) {
					JSONArray array = new JSONArray();
					if (list != null && !list.isEmpty()) {
						for (FoodComposition model : list) {
							JSONObject jsonObject = model.toJsonObject();
							jsonObject.remove("tenantId");
							jsonObject.remove("createBy");
							jsonObject.remove("createTime");
							jsonObject.remove("updateBy");
							jsonObject.remove("updateTime");
							array.add(jsonObject);
						}
					}
					String filename = SystemProperties.getAppPath() + "/static/generate/js/foodComposition.js";
					StringBuilder buff = new StringBuilder();
					buff.append(" var foods = ").append(array.toJSONString()).append("; ");
					FileUtils.save(filename, buff.toString().getBytes("UTF-8"));
					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/genJSON")
	public byte[] genJSON(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			try {
				FoodCompositionQuery query = new FoodCompositionQuery();
				query.locked(0);
				List<FoodComposition> list = foodCompositionService.list(query);
				if (list != null && !list.isEmpty()) {
					JSONArray array = new JSONArray();
					if (list != null && !list.isEmpty()) {
						for (FoodComposition model : list) {
							JSONObject jsonObject = model.toJsonObject();
							jsonObject.remove("tenantId");
							jsonObject.remove("createBy");
							jsonObject.remove("createTime");
							jsonObject.remove("updateBy");
							jsonObject.remove("updateTime");
							array.add(jsonObject);
						}
					}
					String filename = SystemProperties.getAppPath() + "/static/generate/json/foodComposition.json";
					FileUtils.save(filename, array.toJSONString().getBytes("UTF-8"));
					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/goodslist")
	public ModelAndView goodslist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String wordLike = request.getParameter("wordLike");
		if (StringUtils.isNotEmpty(wordLike)) {
			wordLike = wordLike.trim();
			request.setAttribute("wordLike_enc", RequestUtils.encodeString(wordLike));
			request.setAttribute("wordLike_base64", Base64.encodeBytes(wordLike.getBytes()));
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}
		
		List<String> charList = new ArrayList<String>();
		for (int i = 65; i < 91; i++) {
			charList.add("" + (char) i);
		}
		request.setAttribute("charList", charList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/foodComposition/goodslist", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		FoodCompositionQuery query = new FoodCompositionQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		long nodeId = RequestUtils.getLong(request, "nodeId");
		if (nodeId > 0) {
			query.nodeId(nodeId);
		}

		if (StringUtils.isEmpty(loginContext.getTenantId()) && loginContext.getRoles().contains("Keyboarder")) {
			List<Long> nodeIds = treePermissionService.getNodeIds(loginContext.getActorId(), "rw");
			query.nodeIds(nodeIds);
		}

		long typeId = RequestUtils.getLong(request, "typeId");
		if (typeId > 0) {
			query.nodeId(typeId);
		}

		String wordLike = request.getParameter("wordLike_enc");
		if (StringUtils.isNotEmpty(wordLike)) {
			query.setNameLike(RequestUtils.decodeString(wordLike));
		}

		String namePinyinLike = request.getParameter("namePinyinLike");
		if (StringUtils.isNotEmpty(namePinyinLike)) {
			query.setNamePinyinLike(namePinyinLike);
		}

		int start = 0;
		int limit = 100;
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
			limit = 100;
		}

		JSONObject result = new JSONObject();
		int total = foodCompositionService.getFoodCompositionCountByQueryCriteria(query);
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

			Set<Long> favorites = new HashSet<Long>();
			FoodFavoriteQuery queryx = new FoodFavoriteQuery();
			if (!loginContext.isSystemAdministrator()) {
				queryx.tenantId(loginContext.getTenantId());
			}
			if (nodeId > 0) {
				queryx.nodeId(nodeId);
			}

			if (!loginContext.isSystemAdministrator()) {
				List<FoodFavorite> foodFavorites = foodFavoriteService.list(queryx);
				if (foodFavorites != null && !foodFavorites.isEmpty()) {
					for (FoodFavorite f : foodFavorites) {
						favorites.add(f.getFoodId());
					}
				}
			}

			List<FoodComposition> list = foodCompositionService.getFoodCompositionsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				for (FoodComposition foodComposition : list) {
					if (StringUtils.equals(foodComposition.getEnableFlag(), "N")) {
						continue;
					}
					JSONObject rowJSON = foodComposition.toJsonObject();
					rowJSON.put("startIndex", ++start);
					rowJSON.put("id", foodComposition.getId());
					rowJSON.put("foodCompositionId", foodComposition.getId());
					if (favorites.contains(foodComposition.getId())) {
						rowJSON.put("favorite", true);
					}
					rowsJSON.add(rowJSON);
				}
				result.put("rows", rowsJSON);
			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		// logger.debug(result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/jsonAll")
	@ResponseBody
	public byte[] jsonAll(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("all params:" + params);
		FoodCompositionQuery query = new FoodCompositionQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		long nodeId = RequestUtils.getLong(request, "nodeId");
		if (nodeId > 0) {
			query.nodeId(nodeId);
		}

		if (StringUtils.isEmpty(loginContext.getTenantId()) && loginContext.getRoles().contains("Keyboarder")) {
			List<Long> nodeIds = treePermissionService.getNodeIds(loginContext.getActorId(), "rw");
			query.nodeIds(nodeIds);
		}

		long typeId = RequestUtils.getLong(request, "typeId");
		if (typeId > 0) {
			query.nodeId(typeId);
		}

		String wordLike = request.getParameter("wordLike_enc");
		if (StringUtils.isNotEmpty(wordLike)) {
			query.setNameLike(RequestUtils.decodeString(wordLike));
		}

		String namePinyinLike = request.getParameter("namePinyinLike");
		if (StringUtils.isNotEmpty(namePinyinLike)) {
			query.setNamePinyinLike(namePinyinLike);
		}

		int start = 0;
		int limit = 100;
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
			limit = 100;
		}

		JSONObject result = new JSONObject();
		int total = foodCompositionService.getFoodCompositionCountByQueryCriteria(query);
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

			Set<Long> favorites = new HashSet<Long>();
			FoodFavoriteQuery queryx = new FoodFavoriteQuery();
			if (!loginContext.isSystemAdministrator()) {
				queryx.tenantId(loginContext.getTenantId());
			}
			if (nodeId > 0) {
				queryx.nodeId(nodeId);
			}

			if (!loginContext.isSystemAdministrator()) {
				List<FoodFavorite> foodFavorites = foodFavoriteService.list(queryx);
				if (foodFavorites != null && !foodFavorites.isEmpty()) {
					for (FoodFavorite f : foodFavorites) {
						favorites.add(f.getFoodId());
					}
				}
			}

			List<FoodComposition> list = foodCompositionService.getFoodCompositionsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				for (FoodComposition foodComposition : list) {
					JSONObject rowJSON = foodComposition.toJsonObject();
					rowJSON.put("startIndex", ++start);
					rowJSON.put("id", foodComposition.getId());
					rowJSON.put("foodCompositionId", foodComposition.getId());

					if (favorites.contains(foodComposition.getId())) {
						rowJSON.put("favorite", true);
					}

					rowsJSON.add(rowJSON);
				}
				result.put("rows", rowsJSON);
			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		// logger.debug(result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/jsonFavorites")
	@ResponseBody
	public byte[] jsonFavorites(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		FoodCompositionQuery query = new FoodCompositionQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		Set<Long> favorites = new HashSet<Long>();
		long nodeId = RequestUtils.getLong(request, "nodeId");
		FoodComposition fd = null;
		if (nodeId > 0) {
			query.nodeId(nodeId);
			try {
				FoodFavoriteQuery queryx = new FoodFavoriteQuery();
				if (!loginContext.isSystemAdministrator()) {
					queryx.tenantId(loginContext.getTenantId());
				}
				if (nodeId > 0) {
					queryx.nodeId(nodeId);

					List<FoodComposition> foods = foodCompositionService.getFoodCompositions(nodeId);
					request.setAttribute("foods", foods);
					Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
					for (FoodComposition food : foods) {
						if (StringUtils.equals(food.getEnableFlag(), "N")) {
							continue;
						}
						foodMap.put(food.getId(), food);
					}

					long selected = RequestUtils.getLong(request, "selected");
					if (selected > 0) {
						fd = foodCompositionService.getFoodComposition(selected);
					}

					if (!loginContext.isSystemAdministrator()) {
						List<FoodFavorite> foodFavorites = foodFavoriteService.list(queryx);
						if (foodFavorites != null && !foodFavorites.isEmpty()) {
							for (FoodFavorite f : foodFavorites) {
								if (foodMap.get(f.getFoodId()) != null) {
									favorites.add(f.getFoodId());
								}
							}
						}
					}

				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

		if (StringUtils.isEmpty(loginContext.getTenantId()) && loginContext.getRoles().contains("Keyboarder")) {
			List<Long> nodeIds = treePermissionService.getNodeIds(loginContext.getActorId(), "rw");
			query.nodeIds(nodeIds);
		}

		long typeId = RequestUtils.getLong(request, "typeId");
		if (typeId > 0) {
			query.nodeId(typeId);
		}

		String wordLike = request.getParameter("wordLike_enc");
		if (StringUtils.isNotEmpty(wordLike)) {
			query.setNameLike(RequestUtils.decodeString(wordLike));
		}

		String namePinyinLike = request.getParameter("namePinyinLike");
		if (StringUtils.isNotEmpty(namePinyinLike)) {
			query.setNamePinyinLike(namePinyinLike);
		}

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
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = foodCompositionService.getFoodCompositionCountByQueryCriteria(query);
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

			List<FoodComposition> list = foodCompositionService.getFoodCompositionsByQueryCriteria(start, limit, query);
			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();
				for (FoodComposition foodComposition : list) {
					if (fd != null && foodComposition.getId() == fd.getId()) {
						JSONObject rowJSON = foodComposition.toJsonObject();
						rowJSON.put("startIndex", ++start);
						rowJSON.put("id", foodComposition.getId());
						rowJSON.put("foodCompositionId", foodComposition.getId());
						rowsJSON.add(rowJSON);
						continue;
					}
					if (StringUtils.equals(foodComposition.getEnableFlag(), "N")) {
						continue;
					}
					if (favorites != null && !favorites.isEmpty()) {
						if (!favorites.contains(foodComposition.getId())) {
							continue;
						}
					}
					JSONObject rowJSON = foodComposition.toJsonObject();
					rowJSON.put("startIndex", ++start);
					rowJSON.put("id", foodComposition.getId());
					rowJSON.put("foodCompositionId", foodComposition.getId());
					rowsJSON.add(rowJSON);
				}
				result.put("rows", rowsJSON);
			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		// logger.debug(result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setUserPermission(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String wordLike = request.getParameter("wordLike");
		if (StringUtils.isNotEmpty(wordLike)) {
			wordLike = wordLike.trim();
			request.setAttribute("wordLike_enc", RequestUtils.encodeString(wordLike));
			request.setAttribute("wordLike_base64", Base64.encodeBytes(wordLike.getBytes()));
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			if (StringUtils.isEmpty(loginContext.getTenantId()) && loginContext.getRoles().contains("Keyboarder")) {
				List<Long> nodeIds = treePermissionService.getNodeIds(loginContext.getActorId(), "rw");
				List<SysTree> list = new ArrayList<SysTree>();
				for (SysTree tree : foodCategories) {
					if (nodeIds.contains(tree.getId())) {
						list.add(tree);
					}
				}
				foodCategories = list;
			}
			request.setAttribute("foodCategories", foodCategories);
		}

		List<String> charList = new ArrayList<String>();
		for (int i = 65; i < 91; i++) {
			charList.add("" + (char) i);
		}
		request.setAttribute("charList", charList);

		request.removeAttribute("heathcare_write_perm");

		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("heathcare_write_perm", true);
		}
		
		request.setAttribute("heathcare_gen_js_perm", false);
		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("heathcare_gen_js_perm", true);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/foodComposition/list", modelMap);
	}

	@RequestMapping("/list2")
	public ModelAndView list2(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setUserPermission(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String wordLike = request.getParameter("wordLike");
		if (StringUtils.isNotEmpty(wordLike)) {
			wordLike = wordLike.trim();
			request.setAttribute("wordLike_enc", RequestUtils.encodeString(wordLike));
			request.setAttribute("wordLike_base64", Base64.encodeBytes(wordLike.getBytes()));
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			if (StringUtils.isEmpty(loginContext.getTenantId()) && loginContext.getRoles().contains("Keyboarder")) {
				List<Long> nodeIds = treePermissionService.getNodeIds(loginContext.getActorId(), "rw");
				List<SysTree> list = new ArrayList<SysTree>();
				for (SysTree tree : foodCategories) {
					if (nodeIds.contains(tree.getId())) {
						list.add(tree);
					}
				}
				foodCategories = list;
			}
			request.setAttribute("foodCategories", foodCategories);
		}

		List<String> charList = new ArrayList<String>();
		for (int i = 65; i < 91; i++) {
			charList.add("" + (char) i);
		}
		request.setAttribute("charList", charList);

		request.removeAttribute("heathcare_write_perm");

		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("heathcare_write_perm", true);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/foodComposition/list2", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("foodComposition.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/foodComposition/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveFoodComposition")
	public byte[] saveFoodComposition(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String actorId = loginContext.getActorId();
		long id = RequestUtils.getLong(request, "id");
		long nodeId = RequestUtils.getLong(request, "nodeId");
		FoodComposition foodComposition = null;
		try {
			if (id > 0) {
				foodComposition = foodCompositionService.getFoodComposition(id);
				nodeId = foodComposition.getNodeId();
			}

			FoodComposition tmp = foodCompositionService.getFoodCompositionByName(request.getParameter("name"));
			if (tmp != null) {
				if (foodComposition != null) {
					if (tmp.getId() != foodComposition.getId()) {// 修改，判断名称是否重复
						return ResponseUtils.responseJsonResult(false, tmp.getName() + "已经存在了。");
					}
				} else {
					// 新增时名称也不能重复。
					return ResponseUtils.responseJsonResult(false, tmp.getName() + "已经存在了。");
				}
				if (!loginContext.isSystemAdministrator()) {
					if (!StringUtils.equals(tmp.getCreateBy(), actorId)) {
						return ResponseUtils.responseJsonResult(false, "您没有修改该数据的权限。");
					}
				}
			}

			if (!loginContext.isSystemAdministrator()) {
				if (StringUtils.isEmpty(loginContext.getTenantId()) && loginContext.getRoles().contains("Keyboarder")) {
					List<Long> nodeIds = treePermissionService.getNodeIds(loginContext.getActorId(), "rw");
					if (!nodeIds.contains(nodeId)) {
						return ResponseUtils.responseJsonResult(false, "您没有修改该数据的权限。");
					}
				}
			}

			if (foodComposition == null) {
				foodComposition = new FoodComposition();
				Tools.populate(foodComposition, params);

				if (loginContext.isSystemAdministrator() || (StringUtils.isEmpty(loginContext.getTenantId())
						&& loginContext.getRoles().contains("Keyboarder"))) {
					foodComposition.setSysFlag("Y");
				} else {
					foodComposition.setSysFlag("N");
					foodComposition.setTenantId(loginContext.getTenantId());
				}
			} else {
				if (!loginContext.isSystemAdministrator()) {
					if (!(loginContext.getRoles().contains("Keyboarder"))) {
						if (!StringUtils.equals(foodComposition.getCreateBy(), actorId)) {
							return ResponseUtils.responseJsonResult(false, "您没有修改该数据的权限。");
						}
					}
				}
			}

			foodComposition.setNodeId(nodeId);
			foodComposition.setTreeId(request.getParameter("treeId"));
			foodComposition.setName(request.getParameter("name"));
			foodComposition.setNamePinyin(PinyinUtils.converterToFirstSpell(foodComposition.getName(), true));
			foodComposition.setAlias(request.getParameter("alias"));
			foodComposition.setCode(request.getParameter("code"));
			foodComposition.setDiscriminator(request.getParameter("discriminator"));
			foodComposition.setDescription(request.getParameter("description"));
			foodComposition.setInitQuantity(RequestUtils.getDouble(request, "initQuantity"));
			foodComposition.setRadical(RequestUtils.getDouble(request, "radical"));
			foodComposition.setHeatEnergy(RequestUtils.getDouble(request, "heatEnergy"));
			foodComposition.setProtein(RequestUtils.getDouble(request, "protein"));
			foodComposition.setFat(RequestUtils.getDouble(request, "fat"));
			foodComposition.setCarbohydrate(RequestUtils.getDouble(request, "carbohydrate"));
			foodComposition.setVitaminA(RequestUtils.getDouble(request, "vitaminA"));
			foodComposition.setVitaminB1(RequestUtils.getDouble(request, "vitaminB1"));
			foodComposition.setVitaminB2(RequestUtils.getDouble(request, "vitaminB2"));
			foodComposition.setVitaminB6(RequestUtils.getDouble(request, "vitaminB6"));
			foodComposition.setVitaminB12(RequestUtils.getDouble(request, "vitaminB12"));
			foodComposition.setVitaminC(RequestUtils.getDouble(request, "vitaminC"));
			foodComposition.setVitaminE(RequestUtils.getDouble(request, "vitaminE"));
			foodComposition.setCarotene(RequestUtils.getDouble(request, "carotene"));
			foodComposition.setRetinol(RequestUtils.getDouble(request, "retinol"));
			foodComposition.setNicotinicCid(RequestUtils.getDouble(request, "nicotinicCid"));
			foodComposition.setCalcium(RequestUtils.getDouble(request, "calcium"));
			foodComposition.setIron(RequestUtils.getDouble(request, "iron"));
			foodComposition.setZinc(RequestUtils.getDouble(request, "zinc"));
			foodComposition.setIodine(RequestUtils.getDouble(request, "iodine"));
			foodComposition.setPhosphorus(RequestUtils.getDouble(request, "phosphorus"));
			foodComposition.setCopper(RequestUtils.getDouble(request, "copper"));
			foodComposition.setMagnesium(RequestUtils.getDouble(request, "magnesium"));
			foodComposition.setManganese(RequestUtils.getDouble(request, "manganese"));
			foodComposition.setPotassium(RequestUtils.getDouble(request, "potassium"));
			foodComposition.setSelenium(RequestUtils.getDouble(request, "selenium"));
			foodComposition.setSortNo(RequestUtils.getInt(request, "sortNo"));
			foodComposition.setDailyFlag(request.getParameter("dailyFlag"));
			foodComposition.setColorFlag(request.getParameter("colorFlag"));
			foodComposition.setCerealFlag(request.getParameter("cerealFlag"));
			foodComposition.setBeansFlag(request.getParameter("beansFlag"));
			foodComposition.setEnableFlag(request.getParameter("enableFlag"));
			foodComposition.setCreateBy(actorId);
			foodComposition.setUpdateBy(actorId);
			// logger.debug(foodComposition.getNicotinicCid());
			this.foodCompositionService.save(foodComposition);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/searchlist")
	public ModelAndView searchlist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String wordLike = request.getParameter("wordLike");
		if (StringUtils.isNotEmpty(wordLike)) {
			wordLike = wordLike.trim();
			request.setAttribute("wordLike_enc", RequestUtils.encodeString(wordLike));
			request.setAttribute("wordLike_base64", Base64.encodeBytes(wordLike.getBytes()));
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}
		
		List<String> charList = new ArrayList<String>();
		for (int i = 65; i < 91; i++) {
			charList.add("" + (char) i);
		}
		request.setAttribute("charList", charList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/foodComposition/searchlist", modelMap);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodFavoriteService")
	public void setFoodFavoriteService(FoodFavoriteService foodFavoriteService) {
		this.foodFavoriteService = foodFavoriteService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@javax.annotation.Resource
	public void setTreePermissionService(TreePermissionService treePermissionService) {
		this.treePermissionService = treePermissionService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		FoodComposition foodComposition = foodCompositionService
				.getFoodComposition(RequestUtils.getLong(request, "id"));
		request.setAttribute("foodComposition", foodComposition);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("foodComposition.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/foodComposition/view");
	}

}
