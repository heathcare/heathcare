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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.Dishes;
import com.glaf.heathcare.domain.DishesItem;
import com.glaf.heathcare.query.DishesQuery;
import com.glaf.heathcare.service.DishesService;
import com.glaf.heathcare.util.DishesJsonFactory;

import net.iharder.Base64;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dishes")
@RequestMapping("/heathcare/dishes")
public class DishesController {
	protected static final Log logger = LogFactory.getLog(DishesController.class);

	protected DishesService dishesService;

	protected SysTreeService sysTreeService;

	public DishesController() {

	}

	@ResponseBody
	@RequestMapping("/calculate")
	public byte[] calculate(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		long dishesId = RequestUtils.getLong(request, "dishesId");
		if (dishesId > 0) {
			Dishes dishes = dishesService.getDishes(dishesId);
			if (dishes != null) {
				if (loginContext.isSystemAdministrator()) {
					dishesService.calculate(dishesId);
					return ResponseUtils.responseResult(true);
				} else {
					if (loginContext.getRoles().contains("HealthPhysician")
							|| loginContext.getRoles().contains("TenantAdmin")) {
						if (StringUtils.equals(dishes.getTenantId(), loginContext.getTenantId())) {
							dishesService.calculate(dishesId);
							return ResponseUtils.responseResult(true);
						}
					}
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/calculateAll")
	public byte[] calculateAll(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		try {
			if (loginContext.isSystemAdministrator()) {
				dishesService.calculateAll(loginContext);
				return ResponseUtils.responseResult(true);
			} else {
				if (loginContext.getRoles().contains("HealthPhysician")
						|| loginContext.getRoles().contains("TenantAdmin")) {
					dishesService.calculateAll(loginContext);
					return ResponseUtils.responseResult(true);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
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
					Dishes dishes = dishesService.getDishes(Long.valueOf(x));
					if (dishes != null && (StringUtils.equals(dishes.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						dishesService.deleteById(dishes.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Dishes dishes = dishesService.getDishes(Long.valueOf(id));
			if (dishes != null && (StringUtils.equals(dishes.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				dishesService.deleteById(dishes.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		List<SysTree> trees = sysTreeService.getSysTreeList(4801L);// 菜肴分类
		request.setAttribute("categories", trees);

		request.setAttribute("canSave", true);

		Dishes dishes = dishesService.getDishes(RequestUtils.getLong(request, "id"));
		if (dishes != null) {
			request.setAttribute("dishes", dishes);

			if (loginContext.isSystemAdministrator()) {
				if (!StringUtils.equals(dishes.getCreateBy(), loginContext.getActorId())) {
					request.setAttribute("canSave", false);
				}
			} else {
				if (!StringUtils.equals(dishes.getTenantId(), loginContext.getTenantId())) {
					request.setAttribute("canSave", false);
					logger.debug("--------canSave false");
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dishes.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dishes/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DishesQuery query = new DishesQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);

		String sysFlag = request.getParameter("sysFlag");

		if (StringUtils.equals(sysFlag, "N")) {
			if (loginContext.isSystemAdministrator()) {
				query.createBy(loginContext.getActorId());
			} else {
				query.sysFlag("N");
				query.tenantId(loginContext.getTenantId());
			}
		} else {
			if (StringUtils.isNotEmpty(sysFlag)) {
				query.sysFlag(sysFlag);
			} else {
				if (loginContext.isSystemAdministrator()) {
					query.sysFlag("Y");
				} else {
					query.sysFlag("N");
				}
			}
		}

		String nameLike = request.getParameter("nameLike_enc");
		if (StringUtils.isNotEmpty(nameLike)) {
			query.setNameLike(RequestUtils.decodeString(nameLike));
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
			limit = 100;
		}

		JSONObject result = new JSONObject();
		int total = dishesService.getDishesCountByQueryCriteria(query);
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

			List<Dishes> list = dishesService.getDishesListByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Dishes dishes : list) {
					JSONObject rowJSON = dishes.toJsonObject();
					rowJSON.put("id", dishes.getId());
					rowJSON.put("rowId", dishes.getId());
					rowJSON.put("dishesId", dishes.getId());
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
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		request.setAttribute("canEdit", true);

		String nameLike = request.getParameter("nameLike");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = nameLike.trim();
			request.setAttribute("nameLike_enc", RequestUtils.encodeString(nameLike));
			request.setAttribute("nameLike_base64", Base64.encodeBytes(nameLike.getBytes()));
		}

		String sysFlag = request.getParameter("sysFlag");
		if (loginContext.isSystemAdministrator()) {
			sysFlag = "Y";
		} else {
			if (StringUtils.isEmpty(sysFlag)) {
				sysFlag = "N";
			}
			if (StringUtils.equals(sysFlag, "Y")) {
				request.setAttribute("canEdit", false);
			}
		}
		request.setAttribute("sysFlag", sysFlag);

		List<SysTree> trees = sysTreeService.getSysTreeList(4801L);// 菜肴分类
		request.setAttribute("categories", trees);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dishes/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("dishes.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/dishes/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		long dishesId = RequestUtils.getLong(request, "dishesId");
		String saveAsFlag = request.getParameter("saveAsFlag");
		Dishes dishes = null;
		try {
			if (dishesId > 0) {
				dishes = dishesService.getDishes(dishesId);
				if (StringUtils.equals(dishes.getSysFlag(), "Y")) {
					if (!loginContext.isSystemAdministrator()) {
						saveAsFlag = "true";
					}
				}
				logger.debug("saveAsFlag:" + saveAsFlag);
				logger.debug("items:" + dishes.getItems());
				if (StringUtils.equals(saveAsFlag, "true")) {
					List<DishesItem> items = dishes.getItems();
					List<DishesItem> newItems = new ArrayList<DishesItem>();
					for (DishesItem item : items) {
						DishesItem m = new DishesItem();
						m.setCreateBy(actorId);
						m.setUpdateBy(actorId);
						m.setDescription(item.getDescription());
						m.setFoodId(item.getFoodId());
						m.setFoodName(item.getFoodName());
						m.setName(item.getName());
						m.setQuantity(item.getQuantity());
						m.setSortNo(item.getSortNo());
						m.setTenantId(loginContext.getTenantId());
						newItems.add(m);
					}
					dishes.setId(0);
					dishes.setItems(newItems);
					if (loginContext.isSystemAdministrator()) {
						dishes.setSysFlag("Y");
					} else {
						dishes.setTenantId(loginContext.getTenantId());
						dishes.setSysFlag("N");
					}
				}
			} else {
				dishes = new Dishes();
				Tools.populate(dishes, params);
				if (loginContext.isSystemAdministrator()) {
					dishes.setSysFlag("Y");
				} else {
					dishes.setTenantId(loginContext.getTenantId());
					dishes.setSysFlag("N");
				}
			}

			dishes.setNodeId(RequestUtils.getLong(request, "nodeId"));
			dishes.setType(request.getParameter("type"));
			dishes.setName(request.getParameter("name"));
			dishes.setDescription(request.getParameter("description"));
			dishes.setCreateBy(actorId);
			dishes.setUpdateBy(actorId);
			if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
					|| loginContext.getRoles().contains("TenantAdmin")) {
				this.dishesService.save(dishes);
				return ResponseUtils.responseJsonResult(true);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveJson")
	public byte[] saveJson(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		String json = request.getParameter("json");
		try {
			if (StringUtils.isNotEmpty(json)) {
				JSONObject jsonObject = JSON.parseObject(json);
				Dishes dishes = DishesJsonFactory.jsonToObject(jsonObject);

				if (dishes.getId() == 0) {
					dishes.setCreateBy(actorId);
					if (loginContext.isSystemAdministrator()) {
						dishes.setSysFlag("Y");
					} else {
						dishes.setTenantId(loginContext.getTenantId());
					}
				} else {
					if (!loginContext.isSystemAdministrator()) {
						if (!StringUtils.equals(loginContext.getTenantId(), dishes.getTenantId())) {
							return ResponseUtils.responseJsonResult(false, "您没有修改该记录的权限。");
						}
					}
				}
				if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
						|| loginContext.getRoles().contains("TenantAdmin")) {
					dishes.setUpdateBy(actorId);
					this.dishesService.save(dishes);
					return ResponseUtils.responseJsonResult(true);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dishesService")
	public void setDishesService(DishesService dishesService) {
		this.dishesService = dishesService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

}
