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

package com.glaf.base.modules.mgmr.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.base.district.domain.District;
import com.glaf.base.district.query.DistrictQuery;
import com.glaf.base.district.service.DistrictService;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TreePermission;
import com.glaf.base.modules.sys.query.SysTenantQuery;
import com.glaf.base.modules.sys.query.TreePermissionQuery;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TreePermissionService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/mgmr/tenant")
@RequestMapping("/mgmr/tenant")
public class TenantMgmrController {
	protected static final Log logger = LogFactory.getLog(TenantMgmrController.class);

	protected DistrictService districtService;

	protected SysTenantService sysTenantService;

	protected TreePermissionService treePermissionService;

	public TenantMgmrController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		List<Long> nodeIds = treePermissionService.getUserTypeNodeIds(loginContext.getActorId(), "district");
		if (nodeIds == null) {
			nodeIds = new ArrayList<Long>();
		}
		nodeIds.add(0L);
		DistrictQuery query = new DistrictQuery();
		query.setNodeIds(nodeIds);
		query.parentId(0L);

		List<District> provinces = districtService.list(query);
		request.setAttribute("provinces", provinces);

		String tenantId = request.getParameter("tenantId");

		if (StringUtils.isNotEmpty(tenantId)) {
			SysTenant sysTenant = sysTenantService.getSysTenantByTenantId(tenantId);
			if (sysTenant != null) {
				request.setAttribute("sysTenant", sysTenant);

				if (sysTenant.getProvinceId() > 0) {
					query.parentId(sysTenant.getProvinceId());
					List<District> citys = districtService.list(query);
					request.setAttribute("citys", citys);
				}
				if (sysTenant.getCityId() > 0) {
					query.parentId(sysTenant.getCityId());
					List<District> areas = districtService.list(query);
					request.setAttribute("areas", areas);
				}
				if (sysTenant.getAreaId() > 0) {
					query.parentId(sysTenant.getAreaId());
					List<District> towns = districtService.list(query);
					request.setAttribute("towns", towns);
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("mgmr.tenant.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/mgmr/tenant/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysTenantQuery query = new SysTenantQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		List<Long> selected = new ArrayList<Long>();
		selected.add(0L);
		TreePermissionQuery query2 = new TreePermissionQuery();
		query2.userId(loginContext.getActorId());
		query2.type("district");

		List<TreePermission> perms = treePermissionService.list(query2);
		if (perms != null && !perms.isEmpty()) {
			for (TreePermission p : perms) {
				selected.add(p.getNodeId());
			}
		}

		query.areaIds(selected);

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
		int total = sysTenantService.getSysTenantCountByQueryCriteria(query);
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

			List<SysTenant> list = sysTenantService.getSysTenantsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SysTenant sysTenant : list) {
					JSONObject rowJSON = sysTenant.toJsonObject();
					rowJSON.put("id", sysTenant.getId());
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

		List<Long> nodeIds = treePermissionService.getUserTypeNodeIds(loginContext.getActorId(), "district");
		if (nodeIds == null) {
			nodeIds = new ArrayList<Long>();
		}
		nodeIds.add(0L);
		DistrictQuery query = new DistrictQuery();
		query.setNodeIds(nodeIds);
		query.parentId(0L);

		List<District> provinces = districtService.list(query);
		request.setAttribute("provinces", provinces);

		long provinceId = RequestUtils.getLong(request, "provinceId");
		if (provinceId > 0) {
			query.parentId(provinceId);
			List<District> citys = districtService.list(query);
			request.setAttribute("citys", citys);
		}

		long cityId = RequestUtils.getLong(request, "cityId");
		if (cityId > 0) {
			query.parentId(cityId);
			List<District> areas = districtService.list(query);
			request.setAttribute("areas", areas);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/mgmr/tenant/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("mgmr.tenant.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/mgmr/tenant/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveTenant")
	public byte[] saveTenant(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 必须是区域经理才有权限
		 */
		if (loginContext.hasPermission("AreaManager", "or")) {
			String actorId = loginContext.getActorId();
			String tenantId = request.getParameter("tenantId");
			SysTenant sysTenant = null;
			try {

				List<Long> nodeIds = treePermissionService.getUserTypeNodeIds(loginContext.getActorId(), "district");
				if (nodeIds == null) {
					nodeIds = new ArrayList<Long>();
				}

				if (StringUtils.isNotEmpty(tenantId)) {
					sysTenant = sysTenantService.getSysTenantByTenantId(tenantId);
				}
				if (sysTenant == null) {
					sysTenant = new SysTenant();
					sysTenant.setCreateBy(actorId);
				}
				sysTenant.setName(request.getParameter("name"));
				sysTenant.setCode(request.getParameter("code"));
				sysTenant.setLevel(RequestUtils.getInt(request, "level"));
				sysTenant.setTenantType(RequestUtils.getInt(request, "tenantType"));
				sysTenant.setProvinceId(RequestUtils.getLong(request, "provinceId"));
				if (sysTenant.getProvinceId() > 0) {
					District m = districtService.getDistrict(sysTenant.getProvinceId());
					if (!nodeIds.contains(m.getId())) {
						return ResponseUtils.responseJsonResult(false, "您没有操作权限！");
					}
					sysTenant.setProvince(m.getName());
				} else {
					return ResponseUtils.responseJsonResult(false, "必须选择省/直辖市！");
				}
				sysTenant.setCityId(RequestUtils.getLong(request, "cityId"));
				if (sysTenant.getCityId() > 0) {
					District m = districtService.getDistrict(sysTenant.getCityId());
					if (!nodeIds.contains(m.getId())) {
						return ResponseUtils.responseJsonResult(false, "您没有操作权限！");
					}
					sysTenant.setCity(m.getName());
				} else {
					return ResponseUtils.responseJsonResult(false, "必须选择市！");
				}
				sysTenant.setAreaId(RequestUtils.getLong(request, "areaId"));
				if (sysTenant.getAreaId() > 0) {
					District m = districtService.getDistrict(sysTenant.getAreaId());
					if (!nodeIds.contains(m.getId())) {
						return ResponseUtils.responseJsonResult(false, "您没有操作权限！");
					}
					sysTenant.setArea(m.getName());
				} else {
					return ResponseUtils.responseJsonResult(false, "必须选择区/县！");
				}
				sysTenant.setTownId(RequestUtils.getLong(request, "townId"));
				if (sysTenant.getTownId() > 0) {
					District m = districtService.getDistrict(sysTenant.getTownId());
					sysTenant.setTown(m.getName());
				}
				sysTenant.setPrincipal(request.getParameter("principal"));
				sysTenant.setTelephone(request.getParameter("telephone"));
				sysTenant.setAddress(request.getParameter("address"));
				sysTenant.setProperty(request.getParameter("property"));
				sysTenant.setVerify(request.getParameter("verify"));
				sysTenant.setTicketFlag(request.getParameter("ticketFlag"));
				sysTenant.setType(request.getParameter("type"));
				sysTenant.setLimit(RequestUtils.getInt(request, "limit"));
				sysTenant.setLocked(RequestUtils.getInt(request, "locked"));
				sysTenant.setDisableDataConstraint(request.getParameter("disableDataConstraint"));
				sysTenant.setUpdateBy(actorId);
				this.sysTenantService.save(sysTenant);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setTreePermissionService(TreePermissionService treePermissionService) {
		this.treePermissionService = treePermissionService;
	}

}
