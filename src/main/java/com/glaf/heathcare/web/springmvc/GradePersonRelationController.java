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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.glaf.core.security.LoginContext;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GradePersonRelation;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.GradePersonRelationQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GradePersonRelationService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/gradePersonRelation")
@RequestMapping("/heathcare/gradePersonRelation")
public class GradePersonRelationController {
	protected static final Log logger = LogFactory.getLog(GradePersonRelationController.class);

	protected GradeInfoService gradeInfoService;

	protected GradePersonRelationService gradePersonRelationService;

	protected PersonService personService;

	public GradePersonRelationController() {

	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			request.setAttribute("save_permission", true);
		}
		String gradeId = request.getParameter("gradeId");
		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");
		int semester = RequestUtils.getInt(request, "semester");
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug(params);

		GradeInfo gradeInfo = null;
		if (StringUtils.isNotEmpty(gradeId)) {
			try {
				gradeInfo = gradeInfoService.getGradeInfo(gradeId);
				request.setAttribute("gradeInfo", gradeInfo);

				if (year > 0 && month > 0) {
					List<Person> persons = personService.getPersons(gradeId);
					GradePersonRelationQuery query = new GradePersonRelationQuery();
					query.tenantId(loginContext.getTenantId());
					query.gradeId(gradeId);
					query.year(year);
					if (semester > 0) {
						query.semester(semester);
					}
					if (month > 0) {
						query.month(month);
					}
					List<Person> selected = new ArrayList<Person>();
					List<Person> unselected = new ArrayList<Person>();
					List<GradePersonRelation> rows = gradePersonRelationService.list(query);
					List<String> exists = new ArrayList<String>();
					if (rows != null && !rows.isEmpty()) {
						for (GradePersonRelation rel : rows) {
							exists.add(rel.getPersonId());
						}
					}

					StringBuilder bufferx = new StringBuilder();
					StringBuilder buffery = new StringBuilder();

					for (Person p : persons) {
						if (exists.contains(p.getId())) {
							selected.add(p);
							buffery.append("<option value=\"").append(p.getId()).append("\">").append(p.getName())
									.append("");
							if (StringUtils.equals(p.getSex(), "1")) {
								buffery.append("[男]");
							} else {
								buffery.append("[女]");
							}
							buffery.append("</option>");
							buffery.append(FileUtils.newline);
						} else {
							unselected.add(p);
							bufferx.append("<option value=\"").append(p.getId()).append("\">").append(p.getName())
									.append("");
							if (StringUtils.equals(p.getSex(), "1")) {
								bufferx.append("[男]");
							} else {
								bufferx.append("[女]");
							}
							bufferx.append("</option>");
							bufferx.append(FileUtils.newline);
						}
					}
					request.setAttribute("selected", selected);
					request.setAttribute("unselected", unselected);
					request.setAttribute("bufferx", bufferx.toString());
					request.setAttribute("buffery", buffery.toString());

				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int yearx = calendar.get(Calendar.YEAR);

		years.add(yearx);
		years.add(yearx - 1);
		years.add(yearx - 2);
		years.add(yearx - 3);

		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}
		request.setAttribute("years", years);
		request.setAttribute("months", months);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/gradePersonRelation/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAll")
	public byte[] saveAll(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		try {
			if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
				String gradeId = request.getParameter("gradeId");
				int year = RequestUtils.getInt(request, "year");
				int month = RequestUtils.getInt(request, "month");
				int semester = RequestUtils.getInt(request, "semester");
				List<String> items = StringTools.split(request.getParameter("items"));
				List<Person> persons = personService.getPersons(gradeId);
				List<GradePersonRelation> rows = new ArrayList<GradePersonRelation>();
				int sortNo = 0;
				for (Person p : persons) {
					if (items.contains(p.getId())) {
						GradePersonRelation rel = new GradePersonRelation();
						rel.setTenantId(loginContext.getTenantId());
						rel.setGradeId(gradeId);
						rel.setPersonId(p.getId());
						rel.setName(p.getName());
						rel.setSex(p.getSex());
						rel.setYear(year);
						rel.setMonth(month);
						rel.setSemester(semester);
						rel.setSortNo(sortNo++);
						rel.setCreateBy(loginContext.getActorId());
						rows.add(rel);
					}
				}

				gradePersonRelationService.saveAll(loginContext.getTenantId(), gradeId, year, semester, month, rows);
				return ResponseUtils.responseJsonResult(true);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradePersonRelationService")
	public void setGradePersonRelationService(GradePersonRelationService gradePersonRelationService) {
		this.gradePersonRelationService = gradePersonRelationService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
