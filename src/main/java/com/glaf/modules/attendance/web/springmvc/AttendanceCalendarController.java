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

package com.glaf.modules.attendance.web.springmvc;

import java.io.IOException;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.modules.attendance.domain.AttendanceCalendar;
import com.glaf.modules.attendance.query.AttendanceCalendarQuery;
import com.glaf.modules.attendance.service.AttendanceCalendarService;
import com.glaf.modules.attendance.util.AttendanceCalendarJsonFactory;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/attendance/calendar")
@RequestMapping("/attendance/calendar")
public class AttendanceCalendarController {
	protected static final Log logger = LogFactory.getLog(AttendanceCalendarController.class);

	protected AttendanceCalendarService attendanceCalendarService;

	public AttendanceCalendarController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");

		AttendanceCalendar attendanceCalendar = attendanceCalendarService
				.getAttendanceCalendar(loginContext.getTenantId(), year, month);
		if (attendanceCalendar != null) {
			request.setAttribute("attendanceCalendar", attendanceCalendar);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("attendance_calendar.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/attendance/calendar/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		AttendanceCalendarQuery query = new AttendanceCalendarQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
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
		int total = attendanceCalendarService.getAttendanceCalendarCountByQueryCriteria(query);
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

			List<AttendanceCalendar> list = attendanceCalendarService.getAttendanceCalendarsByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (AttendanceCalendar attendanceCalendar : list) {
					JSONObject rowJSON = attendanceCalendar.toJsonObject();
					rowJSON.put("id", attendanceCalendar.getId());
					rowJSON.put("rowId", attendanceCalendar.getId());
					rowJSON.put("attendanceCalendarId", attendanceCalendar.getId());
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

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/attendance/calendar/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");
		String json = request.getParameter("json");
		if (year > 0 && month > 0 && StringUtils.isNotEmpty(json)) {
			try {
				JSONObject jsonObject = JSON.parseObject(json);
				AttendanceCalendar model = AttendanceCalendarJsonFactory.jsonToObject(jsonObject);
				model.setTenantId(loginContext.getTenantId());
				model.setYear(year);
				model.setMonth(month);
				attendanceCalendarService.save(loginContext.getTenantId(), year, month, model);
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.modules.attendance.service.attendanceCalendarService")
	public void setAttendanceCalendarService(AttendanceCalendarService attendanceCalendarService) {
		this.attendanceCalendarService = attendanceCalendarService;
	}

}
