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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.domain.Dishes;
import com.glaf.heathcare.domain.DishesItem;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.query.DishesItemQuery;
import com.glaf.heathcare.service.DishesItemService;
import com.glaf.heathcare.service.DishesService;
import com.glaf.heathcare.service.FoodCompositionService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dishesItem")
@RequestMapping("/heathcare/dishesItem")
public class DishesItemController {
	protected static final Log logger = LogFactory.getLog(DishesItemController.class);

	protected DishesService dishesService;

	protected DishesItemService dishesItemService;

	protected FoodCompositionService foodCompositionService;

	protected SysTreeService sysTreeService;

	public DishesItemController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					DishesItem dishesItem = dishesItemService.getDishesItem(Long.valueOf(x));
					if (dishesItem != null && (StringUtils.equals(dishesItem.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						dishesItemService.deleteById(dishesItem.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			DishesItem dishesItem = dishesItemService.getDishesItem(Long.valueOf(id));
			if (dishesItem != null && (StringUtils.equals(dishesItem.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				dishesItemService.deleteById(dishesItem.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.setAttribute("canEdit", true);

		long dishesId = RequestUtils.getLong(request, "dishesId");
		Dishes dishes = dishesService.getDishes(dishesId);

		if (loginContext.isSystemAdministrator()) {
			if (!StringUtils.equals(dishes.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("canEdit", false);
			}
		} else {
			if (!StringUtils.equals(dishes.getTenantId(), loginContext.getTenantId())) {
				request.setAttribute("canEdit", false);
			}
		}

		DishesItem dishesItem = dishesItemService.getDishesItem(RequestUtils.getLong(request, "id"));
		if (dishesItem != null) {
			request.setAttribute("dishesItem", dishesItem);

			if (dishesItem.getFoodId() > 0) {
				FoodComposition foodComposition = foodCompositionService.getFoodComposition(dishesItem.getFoodId());
				if (foodComposition != null) {
					List<FoodComposition> foods = foodCompositionService
							.getFoodCompositions(foodComposition.getNodeId());
					request.setAttribute("foods", foods);
					request.setAttribute("nodeId", foodComposition.getNodeId());
				}
			}

		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}

		request.setAttribute("ts", System.currentTimeMillis());

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dishesItem.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dishesItem/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DishesItemQuery query = new DishesItemQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		long dishesId = RequestUtils.getLong(request, "dishesId");
		query.dishesId(dishesId);

		if (loginContext.isSystemAdministrator()) {
			query.createBy(loginContext.getActorId());
		} else {
			query.tenantId(loginContext.getTenantId());
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
		int total = dishesItemService.getDishesItemCountByQueryCriteria(query);
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

			List<DishesItem> list = dishesItemService.getDishesItemsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (DishesItem dishesItem : list) {
					JSONObject rowJSON = dishesItem.toJsonObject();
					rowJSON.put("id", dishesItem.getId());
					rowJSON.put("rowId", dishesItem.getId());
					rowJSON.put("dishesItemId", dishesItem.getId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		long dishesId = RequestUtils.getLong(request, "dishesId");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.setAttribute("canEdit", true);

		Dishes dishes = dishesService.getDishes(dishesId);

		if (loginContext.isSystemAdministrator()) {
			if (!StringUtils.equals(dishes.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("canEdit", false);
			}
		} else {
			if (!StringUtils.equals(dishes.getTenantId(), loginContext.getTenantId())) {
				request.setAttribute("canEdit", false);
			}
		}

		request.setAttribute("canEditDishesName", false);

		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("canEditDishesName", true);
		} else {
			if (StringUtils.equals(dishes.getCreateBy(), loginContext.getActorId())) {
				if (StringUtils.equals(dishes.getSysFlag(), "N") && StringUtils.isNotEmpty(dishes.getTenantId())) {
					request.setAttribute("canEditDishesName", true);
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dishesItem/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("dishesItem.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/dishesItem/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveItem")
	public byte[] saveItem(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		long itemId = RequestUtils.getLong(request, "itemId");
		String saveAsFlag = request.getParameter("saveAsFlag");
		DishesItem dishesItem = null;
		try {
			if (StringUtils.equals(saveAsFlag, "true")) {
				dishesItem = new DishesItem();
				Tools.populate(dishesItem, params);
				dishesItem.setDishesId(RequestUtils.getLong(request, "dishesId"));
				if (!loginContext.isSystemAdministrator()) {
					dishesItem.setTenantId(loginContext.getTenantId());
				}
			} else {
				if (itemId > 0) {
					dishesItem = dishesItemService.getDishesItem(itemId);
				} else {
					dishesItem = new DishesItem();
					Tools.populate(dishesItem, params);
					dishesItem.setDishesId(RequestUtils.getLong(request, "dishesId"));
					if (!loginContext.isSystemAdministrator()) {
						dishesItem.setTenantId(loginContext.getTenantId());
					}
				}
			}

			dishesItem.setName(request.getParameter("name"));
			dishesItem.setDescription(request.getParameter("description"));
			dishesItem.setFoodId(RequestUtils.getLong(request, "foodId"));
			dishesItem.setFoodName(request.getParameter("foodName"));
			dishesItem.setQuantity(RequestUtils.getDouble(request, "quantity"));
			dishesItem.setCreateBy(actorId);
			dishesItem.setUpdateBy(actorId);
			if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
					|| loginContext.getRoles().contains("TenantAdmin")) {
				dishesItemService.save(dishesItem);
				dishesService.calculate(dishesItem.getDishesId());
				return ResponseUtils.responseJsonResult(true);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dishesItemService")
	public void setDishesItemService(DishesItemService dishesItemService) {
		this.dishesItemService = dishesItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dishesService")
	public void setDishesService(DishesService dishesService) {
		this.dishesService = dishesService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

}
