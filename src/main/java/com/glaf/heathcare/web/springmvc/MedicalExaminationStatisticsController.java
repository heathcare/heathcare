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
import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aliyuncs.utils.StringUtils;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.heathcare.bean.MedicalExaminationEvaluateCountBean;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.service.GradeInfoService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/medicalExaminationStatistics")
@RequestMapping("/heathcare/medicalExaminationStatistics")
public class MedicalExaminationStatisticsController {
	protected static final Log logger = LogFactory.getLog(MedicalExaminationStatisticsController.class);

	protected static final Semaphore semaphore1 = new Semaphore(5);

	protected GradeInfoService gradeInfoService;

	public MedicalExaminationStatisticsController() {

	}

	@RequestMapping("/execute")
	@ResponseBody
	public byte[] execute(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin()) {
			logger.debug("启用并发访问控制,可用许可数:" + semaphore1.availablePermits());
			if (semaphore1.availablePermits() == 0) {
				return ResponseUtils.responseJsonResult(false, "任务执行中，请等待执行完成。");
			}
			try {
				semaphore1.acquire();
				// 高级租户，开启实时统计功能。
				MedicalExaminationEvaluateCountBean bean = new MedicalExaminationEvaluateCountBean();
				bean.execute(loginContext.getTenantId());

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			} finally {
				semaphore1.release();
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/executeAll")
	@ResponseBody
	public byte[] executeAll(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			logger.debug("启用并发访问控制,可用许可数:" + semaphore1.availablePermits());
			if (semaphore1.availablePermits() == 0) {
				return ResponseUtils.responseJsonResult(false, "任务执行中，请等待执行完成。");
			}
			try {
				semaphore1.acquire();
				MedicalExaminationEvaluateCountBean bean = new MedicalExaminationEvaluateCountBean();
				bean.executeAll();
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			} finally {
				semaphore1.release();
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/list1")
	public ModelAndView list1(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		List<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}
		request.setAttribute("months", months);
		request.setAttribute("ts", System.currentTimeMillis());

		return new ModelAndView("/heathcare/medicalExaminationStatistics/list1", modelMap);
	}

	@RequestMapping("/list5")
	public ModelAndView list5(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		List<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}
		request.setAttribute("months", months);
		request.setAttribute("ts", System.currentTimeMillis());

		return new ModelAndView("/heathcare/medicalExaminationStatistics/list5", modelMap);
	}

	@RequestMapping("/list7")
	public ModelAndView list7(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		List<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}
		request.setAttribute("months", months);
		request.setAttribute("ts", System.currentTimeMillis());

		return new ModelAndView("/heathcare/medicalExaminationStatistics/list7", modelMap);
	}

	@RequestMapping
	public ModelAndView main(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isEmpty(tenantId)) {
			tenantId = loginContext.getTenantId();
		}
		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(tenantId);
		request.setAttribute("gradeInfos", list);

		List<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}
		request.setAttribute("months", months);
		request.setAttribute("ts", System.currentTimeMillis());

		return new ModelAndView("/heathcare/medicalExaminationStatistics/main", modelMap);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

}
