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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;

@Controller("/heathcare/bookkeeping")
@RequestMapping("/heathcare/bookkeeping")
public class BookkeepingController {

	protected SysTenantService sysTenantService;

	protected TenantConfigService tenantConfigService;

	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isEmpty(tenantId)) {
			tenantId = loginContext.getTenantId();
		}

		request.setAttribute("tenantId", tenantId);
		request.setAttribute("ts", System.currentTimeMillis());

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(tenantId);
		if (tenantConfig != null) {
			params.put("tenantConfig", tenantConfig);
			params.put("sysName", tenantConfig.getSysName());
		}

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
		if (tenant != null) {
			params.put("orgName", tenant.getName());
			params.put("tenant", tenant);
		}

		return new ModelAndView("/heathcare/bookkeeping/main", modelMap);
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}
}
