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

package com.glaf.modules.employee.web.springmvc;

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
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.modules.employee.domain.Employee;
import com.glaf.modules.employee.query.EmployeeQuery;
import com.glaf.modules.employee.service.EmployeeService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/employee")
@RequestMapping("/employee")
public class EmployeeController {
	protected static final Log logger = LogFactory.getLog(EmployeeController.class);

	protected DictoryService dictoryService;

	protected EmployeeService employeeService;

	public EmployeeController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (!loginContext.getRoles().contains("TenantAdmin")) {
			return ResponseUtils.responseJsonResult(false, "您没有操作权限!");
		}
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Employee employee = employeeService.getEmployee(String.valueOf(x));
					if (employee != null && StringUtils.equals(loginContext.getTenantId(), employee.getTenantId())) {
						employee.setDeleteFlag(1);
						employeeService.save(employee);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Employee employee = employeeService.getEmployee(String.valueOf(id));
			if (employee != null && StringUtils.equals(loginContext.getTenantId(), employee.getTenantId())) {
				employee.setDeleteFlag(1);
				employeeService.save(employee);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		request.removeAttribute("privilege_write");
		if (loginContext.getRoles().contains("TenantAdmin")) {
			request.setAttribute("privilege_write", true);
		}

		Employee employee = employeeService.getEmployee(request.getParameter("id"));
		if (employee != null) {
			request.setAttribute("employee", employee);
		}

		List<Dictory> nations = dictoryService.getDictoryList("employee_nation");
		List<Dictory> educations = dictoryService.getDictoryList("employee_education");
		List<Dictory> senioritys = dictoryService.getDictoryList("employee_seniority");
		List<Dictory> positions = dictoryService.getDictoryList("employee_position");

		request.setAttribute("nations", nations);
		request.setAttribute("educations", educations);
		request.setAttribute("senioritys", senioritys);
		request.setAttribute("positions", positions);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("employee.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/employee/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		EmployeeQuery query = new EmployeeQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.tenantId(loginContext.getTenantId());
		/**
		 * 此处业务逻辑需自行调整
		 */
		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
		}

		String nameLike = request.getParameter("nameLike_enc");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = RequestUtils.decodeString(nameLike);
			query.setNameLike(nameLike);
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
		int total = employeeService.getEmployeeCountByQueryCriteria(query);
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

			List<Employee> list = employeeService.getEmployeesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Employee employee : list) {
					JSONObject rowJSON = employee.toJsonObject();
					rowJSON.put("id", employee.getId());
					rowJSON.put("rowId", employee.getId());
					rowJSON.put("employeeId", employee.getId());
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
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		request.removeAttribute("privilege_write");
		if (loginContext.getRoles().contains("TenantAdmin")) {
			request.setAttribute("privilege_write", true);
		}

		List<Dictory> nations = dictoryService.getDictoryList("employee_nation");
		List<Dictory> educations = dictoryService.getDictoryList("employee_education");
		List<Dictory> senioritys = dictoryService.getDictoryList("employee_seniority");
		List<Dictory> positions = dictoryService.getDictoryList("employee_position");

		request.setAttribute("nations", nations);
		request.setAttribute("educations", educations);
		request.setAttribute("senioritys", senioritys);
		request.setAttribute("positions", positions);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/modules/employee/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("employee.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/modules/employee/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveEmployee")
	public byte[] saveEmployee(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (!loginContext.getRoles().contains("TenantAdmin")) {
			return ResponseUtils.responseJsonResult(false, "您没有操作权限!");
		}
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String id = request.getParameter("id");
		Employee employee = null;
		try {
			if (StringUtils.isNotEmpty(id)) {
				employee = employeeService.getEmployee(id);
			}
			if (employee == null) {
				employee = new Employee();
				Tools.populate(employee, params);
				employee.setCreateBy(actorId);
				employee.setTenantId(loginContext.getTenantId());
			} else {
				if (!StringUtils.equals(loginContext.getTenantId(), employee.getTenantId())) {
					return ResponseUtils.responseJsonResult(false, "您没有操作权限!");
				}
				Tools.populate(employee, params);
				employee.setUpdateBy(actorId);
			}

			employee.setName(request.getParameter("name"));
			employee.setSex(request.getParameter("sex"));
			employee.setBirthday(RequestUtils.getDate(request, "birthday"));
			employee.setIdCardNo(request.getParameter("idCardNo"));
			employee.setEmployeeID(request.getParameter("employeeID"));
			employee.setMobile(request.getParameter("mobile"));
			employee.setTelephone(request.getParameter("telephone"));
			employee.setNationality(request.getParameter("nationality"));
			employee.setNation(request.getParameter("nation"));
			employee.setBirthPlace(request.getParameter("birthPlace"));
			employee.setHomeAddress(request.getParameter("homeAddress"));
			employee.setMarryStatus(request.getParameter("marryStatus"));
			employee.setNatureAccount(request.getParameter("natureAccount"));
			employee.setNatureType(request.getParameter("natureType"));
			employee.setEducation(request.getParameter("education"));
			employee.setSeniority(request.getParameter("seniority"));
			employee.setPosition(request.getParameter("position"));
			employee.setType(request.getParameter("type"));
			employee.setCategory(request.getParameter("category"));
			employee.setJoinDate(RequestUtils.getDate(request, "joinDate"));
			employee.setRemark(request.getParameter("remark"));
			employee.setDeleteFlag(RequestUtils.getInt(request, "deleteFlag"));

			this.employeeService.save(employee);

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

	@javax.annotation.Resource(name = "com.glaf.modules.employee.service.employeeService")
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
