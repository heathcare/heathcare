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

package com.glaf.base.online.web.springmvc;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.*;

import com.glaf.core.service.*;
import com.glaf.core.util.*;

import com.glaf.base.online.domain.*;
import com.glaf.base.online.query.*;
import com.glaf.base.online.service.*;

@Controller("/user/onlinelog")
@RequestMapping("/user/onlinelog")
public class UserOnlineLogController {
	protected static final Log logger = LogFactory.getLog(UserOnlineLogController.class);

	protected UserOnlineLogService userOnlineLogService;

	protected ISystemPropertyService systemPropertyService;

	public UserOnlineLogController() {

	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug(params);
		UserOnlineLogQuery query = new UserOnlineLogQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);

		String searchWord = request.getParameter("searchWord");
		if (StringUtils.isNotEmpty(searchWord)) {
			query.setSearchWord(searchWord);
		}

		if (RequestUtils.getDateTime(request, "loginDateGreaterThanOrEqual") != null) {
			query.setLoginDateGreaterThanOrEqual(RequestUtils.getDateTime(request, "loginDateGreaterThanOrEqual"));
		}
		if (RequestUtils.getDateTime(request, "loginDateLessThanOrEqual") != null) {
			query.setLoginDateLessThanOrEqual(RequestUtils.getDateTime(request, "loginDateLessThanOrEqual"));
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
		int total = userOnlineLogService.getUserOnlineLogCountByQueryCriteria(query);
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

			List<UserOnlineLog> list = userOnlineLogService.getUserOnlineLogsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (UserOnlineLog userOnlineLog : list) {
					JSONObject rowJSON = userOnlineLog.toJsonObject();
					rowJSON.put("id", userOnlineLog.getId());
					rowJSON.put("userOnlineLogId", userOnlineLog.getId());
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

		return new ModelAndView("/user/online/loglist", modelMap);
	}

	@javax.annotation.Resource
	public void setSystemPropertyService(ISystemPropertyService systemPropertyService) {
		this.systemPropertyService = systemPropertyService;
	}

	@javax.annotation.Resource
	public void setUserOnlineLogService(UserOnlineLogService userOnlineLogService) {
		this.userOnlineLogService = userOnlineLogService;
	}

}
