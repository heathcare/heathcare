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

package com.glaf.modules.supplier.web.springmvc;

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

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.modules.supplier.domain.Supplier;
import com.glaf.modules.supplier.query.SupplierQuery;
import com.glaf.modules.supplier.service.SupplierService;

import net.iharder.Base64;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/supplier")
@RequestMapping("/supplier")
public class SupplierController {
	protected static final Log logger = LogFactory.getLog(SupplierController.class);

	protected DistrictService districtService;

	protected SupplierService supplierService;

	public SupplierController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Supplier supplier = supplierService.getSupplier(x);
					if (supplier != null && (StringUtils.equals(loginContext.getTenantId(), supplier.getTenantId()))
							&& (loginContext.getRoles().contains("TenantAdmin"))) {
						supplierService.deleteById(supplier.getSupplierId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Supplier supplier = supplierService.getSupplier(id);
			if (supplier != null && (StringUtils.equals(loginContext.getTenantId(), supplier.getTenantId()))
					&& (loginContext.getRoles().contains("TenantAdmin"))) {
				supplierService.deleteById(supplier.getSupplierId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Supplier supplier = supplierService.getSupplier(RequestUtils.getString(request, "supplierId"));
		if (supplier != null) {
			request.setAttribute("supplier", supplier);

			if (supplier.getProvinceId() > 0) {
				List<District> citys = districtService.getDistrictList(supplier.getProvinceId());
				request.setAttribute("citys", citys);
			}
			if (supplier.getCityId() > 0) {
				List<District> areas = districtService.getDistrictList(supplier.getCityId());
				request.setAttribute("areas", areas);
			}
			if (supplier.getAreaId() > 0) {
				List<District> towns = districtService.getDistrictList(supplier.getAreaId());
				request.setAttribute("towns", towns);
			}

		}

		List<District> provinces = districtService.getDistrictList(0);
		request.setAttribute("provinces", provinces);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("supplier.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/supplier/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SupplierQuery query = new SupplierQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
		}

		String keywordLike = request.getParameter("keywordLike_enc");
		if (StringUtils.isNotEmpty(keywordLike)) {
			query.setKeywordLike(RequestUtils.decodeString(keywordLike));
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
		int total = supplierService.getSupplierCountByQueryCriteria(query);
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

			List<Supplier> list = supplierService.getSuppliersByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Supplier supplier : list) {
					JSONObject rowJSON = supplier.toJsonObject();
					rowJSON.put("id", supplier.getSupplierId());
					rowJSON.put("supplierId", supplier.getSupplierId());
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

		String keywordLike = request.getParameter("keywordLike");
		if (StringUtils.isNotEmpty(keywordLike)) {
			keywordLike = keywordLike.trim();
			request.setAttribute("keywordLike_enc", RequestUtils.encodeString(keywordLike));
			request.setAttribute("keywordLike_base64", Base64.encodeBytes(keywordLike.getBytes()));
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/modules/supplier/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("supplier.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/modules/supplier/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveSupplier")
	public byte[] saveSupplier(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String supplierId = RequestUtils.getString(request, "supplierId");
		Supplier supplier = null;
		try {
			if (supplierId != null) {
				supplier = supplierService.getSupplier(supplierId);
			}
			if (supplier != null) {
				if (!(StringUtils.equals(loginContext.getTenantId(), supplier.getTenantId()))
						&& (loginContext.getRoles().contains("TenantAdmin"))) {
					return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
				}
			} else {
				supplier = new Supplier();
				Tools.populate(supplier, params);
				supplier.setCreateBy(actorId);
				supplier.setTenantId(loginContext.getTenantId());
			}

			supplier.setName(request.getParameter("name"));
			supplier.setShortName(request.getParameter("shortName"));
			supplier.setCode(request.getParameter("code"));
			supplier.setMaterial(request.getParameter("material"));
			supplier.setLevel(RequestUtils.getInt(request, "level"));
			supplier.setProvince(request.getParameter("province"));
			supplier.setProvinceId(RequestUtils.getLong(request, "provinceId"));
			supplier.setCity(request.getParameter("city"));
			supplier.setCityId(RequestUtils.getLong(request, "cityId"));
			supplier.setArea(request.getParameter("area"));
			supplier.setAreaId(RequestUtils.getLong(request, "areaId"));
			supplier.setTown(request.getParameter("town"));
			supplier.setTownId(RequestUtils.getLong(request, "townId"));
			supplier.setPrincipal(request.getParameter("principal"));
			supplier.setTelephone(request.getParameter("telephone"));
			supplier.setAddress(request.getParameter("address"));
			supplier.setProperty(request.getParameter("property"));
			supplier.setMail(request.getParameter("mail"));
			supplier.setLegalPerson(request.getParameter("legalPerson"));
			supplier.setTaxpayerID(request.getParameter("taxpayerID"));
			supplier.setTaxpayerIdentity(request.getParameter("taxpayerIdentity"));
			supplier.setBankOfDeposit(request.getParameter("bankOfDeposit"));
			supplier.setBankAccount(request.getParameter("bankAccount"));
			supplier.setType(request.getParameter("type"));
			supplier.setVerify(request.getParameter("verify"));
			supplier.setRemark(request.getParameter("remark"));
			supplier.setLocked(RequestUtils.getInt(request, "locked"));
			supplier.setUpdateBy(actorId);

			this.supplierService.save(supplier);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("selectlist")
	public ModelAndView selectlist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String keywordLike = request.getParameter("keywordLike");
		if (StringUtils.isNotEmpty(keywordLike)) {
			keywordLike = keywordLike.trim();
			request.setAttribute("keywordLike_enc", RequestUtils.encodeString(keywordLike));
			request.setAttribute("keywordLike_base64", Base64.encodeBytes(keywordLike.getBytes()));
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/modules/supplier/selectlist", modelMap);
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource(name = "com.glaf.modules.supplier.service.supplierService")
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

}
