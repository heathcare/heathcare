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

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.heathcare.service.GradeAdjustService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/gradeAdjust")
@RequestMapping("/heathcare/gradeAdjust")
public class GradeAdjustController {
	protected static final Log logger = LogFactory.getLog(GradeAdjustController.class);

	protected GradeAdjustService gradeAdjustService;

	public GradeAdjustController() {

	}

	@ResponseBody
	@RequestMapping("/upgrade")
	public byte[] upgrade(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("HealthPhysician")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			logger.debug("month:"+calendar.get(Calendar.MONTH));
			if (calendar.get(Calendar.MONTH) == Calendar.AUGUST) {
				try {
					logger.info("升班进行中......");
					gradeAdjustService.upgrade(loginContext.getTenantId());
					return ResponseUtils.responseJsonResult(true);
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeAdjustService")
	public void setGradeAdjustService(GradeAdjustService gradeAdjustService) {
		this.gradeAdjustService = gradeAdjustService;
	}

}
