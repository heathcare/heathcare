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
import com.glaf.base.district.domain.District;
import com.glaf.base.district.service.DistrictService;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.service.TenantConfigService;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.DietaryCategory;
import com.glaf.heathcare.query.DietaryCategoryQuery;
import com.glaf.heathcare.service.DietaryCategoryService;

import net.iharder.Base64;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dietaryCategory")
@RequestMapping("/heathcare/dietaryCategory")
public class DietaryCategoryController {
	protected static final Log logger = LogFactory.getLog(DietaryCategoryController.class);

	protected DictoryService dictoryService;

	protected DistrictService districtService;

	protected SysTreeService sysTreeService;

	protected DietaryCategoryService dietaryCategoryService;

	protected TenantConfigService tenantConfigService;

	public DietaryCategoryController() {

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
					DietaryCategory dietaryCategory = dietaryCategoryService.getDietaryCategory(Long.valueOf(x));
					if (dietaryCategory != null
							&& (StringUtils.equals(dietaryCategory.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {

					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			DietaryCategory dietaryCategory = dietaryCategoryService.getDietaryCategory(Long.valueOf(id));
			if (dietaryCategory != null && (StringUtils.equals(dietaryCategory.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {

				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		RequestUtils.setRequestParameterToAttribute(request);

		request.setAttribute("canEdit", true);

		DietaryCategory dietaryCategory = dietaryCategoryService
				.getDietaryCategory(RequestUtils.getLong(request, "id"));
		if (dietaryCategory != null) {
			request.setAttribute("dietaryCategory", dietaryCategory);

			if (!loginContext.isSystemAdministrator()) {
				if (StringUtils.equals(dietaryCategory.getTenantId(), loginContext.getTenantId())) {
					request.setAttribute("canEdit", false);
				}
			}
		}

		List<Dictory> dictoryList = dictoryService.getDictoryListByCategory("CAT_MEAL");
		request.setAttribute("dictoryList", dictoryList);

		List<District> districts = districtService.getDistrictList(0);
		request.setAttribute("districts", districts);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryCategory.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryCategory/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DietaryCategoryQuery query = new DietaryCategoryQuery();
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
		int total = dietaryCategoryService.getDietaryCategoryCountByQueryCriteria(query);
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

			List<DietaryCategory> list = dietaryCategoryService.getDietaryCategorysByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (DietaryCategory dietaryCategory : list) {
					JSONObject rowJSON = dietaryCategory.toJsonObject();
					rowJSON.put("id", dietaryCategory.getId());
					rowJSON.put("rowId", dietaryCategory.getId());
					rowJSON.put("dietaryCategoryId", dietaryCategory.getId());
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

		List<Dictory> dictoryList = dictoryService.getDictoryListByCategory("CAT_MEAL");
		request.setAttribute("dictoryList", dictoryList);

		if (!loginContext.isSystemAdministrator()) {
			TenantConfig cfg = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			if (cfg != null) {
				request.setAttribute("tenantConfig", cfg);
				for (Dictory dict : dictoryList) {
					if (cfg.getTypeId() == dict.getId()) {
						request.setAttribute("typeDict", dict);
						// request.setAttribute("typeId", dict.getId());
					}
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryCategory/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		DietaryCategory dietaryCategory = null;
		long categoryId = RequestUtils.getLong(request, "id");
		try {
			if (categoryId > 0) {
				dietaryCategory = dietaryCategoryService.getDietaryCategory(categoryId);
			}

			if (dietaryCategory == null) {
				dietaryCategory = new DietaryCategory();
				if (loginContext.isSystemAdministrator()) {
					dietaryCategory.setSysFlag("Y");
				} else {
					dietaryCategory.setTenantId(loginContext.getTenantId());
					dietaryCategory.setSysFlag("N");
				}
			} else {
				if (!loginContext.isSystemAdministrator()) {
					if (StringUtils.equals(loginContext.getTenantId(), dietaryCategory.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "您没有修改该数据的权限。");
					}
				}
			}

			dietaryCategory.setName(request.getParameter("name"));
			dietaryCategory.setDescription(request.getParameter("description"));
			dietaryCategory.setSeason(RequestUtils.getInt(request, "season"));
			dietaryCategory.setProvince(request.getParameter("province"));
			dietaryCategory.setRegion(request.getParameter("region"));
			dietaryCategory.setTypeId(RequestUtils.getLong(request, "typeId"));
			dietaryCategory.setPeopleType(request.getParameter("peopleType"));
			dietaryCategory.setSortNo(RequestUtils.getInt(request, "sortNo"));
			dietaryCategory.setEnableFlag(request.getParameter("enableFlag"));
			dietaryCategory.setCreateBy(actorId);
			dietaryCategory.setUpdateBy(actorId);
			if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
					|| loginContext.getRoles().contains("TenantAdmin")) {
				this.dietaryCategoryService.save(dietaryCategory);
			}

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryCategoryService")
	public void setDietaryCategoryService(DietaryCategoryService dietaryCategoryService) {
		this.dietaryCategoryService = dietaryCategoryService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
