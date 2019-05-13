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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.bean.MedicalExaminationTbxExcelImporter;
import com.glaf.heathcare.bean.MedicalExaminationXlsImporter;
import com.glaf.heathcare.bean.MedicalExaminationXlsxImporter;
import com.glaf.heathcare.converter.HaizgMedicalExaminationConverter;
import com.glaf.heathcare.converter.TBNMedicalExaminationConverter;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationDef;
import com.glaf.heathcare.domain.MedicalExaminationXlsArea;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.helper.MedicalExaminationHelper;
import com.glaf.heathcare.query.MedicalExaminationDefQuery;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationDefService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/medicalExamination")
@RequestMapping("/heathcare/medicalExamination")
public class MedicalExaminationController {
	protected static final Log logger = LogFactory.getLog(MedicalExaminationController.class);

	protected static final Semaphore semaphore2 = new Semaphore(20);

	protected MedicalExaminationService medicalExaminationService;

	protected MedicalExaminationDefService medicalExaminationDefService;

	protected GradeInfoService gradeInfoService;

	protected GrowthStandardService growthStandardService;

	protected PersonService personService;

	protected SysTenantService sysTenantService;

	public MedicalExaminationController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		MedicalExamination medicalExamination = medicalExaminationService.getMedicalExamination(id);
		Person person = null;
		if (medicalExamination != null) {
			if (StringUtils.isNotEmpty(medicalExamination.getPersonId())) {
				person = personService.getPerson(medicalExamination.getPersonId());
			}

			boolean havePermission = false;
			/**
			 * 机构管理员或保健医生
			 */
			if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
				if ((StringUtils.equals(loginContext.getTenantId(), medicalExamination.getTenantId()))) {
					havePermission = true;
				}
			} else {
				/**
				 * 授权的保健机构
				 */
				String tenantId = request.getParameter("tenantId");
				if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
						&& loginContext.getManagedTenantIds().contains(tenantId)) {
					havePermission = true;
				} else {
					/**
					 * 授权的保教人员
					 */
					if (person != null && loginContext.getRoles().contains("Teacher")
							&& loginContext.getGradeIds().contains(person.getGradeId())) {
						havePermission = true;
					}
				}
			}
			if (havePermission) {
				if (loginContext.getLimit() == Constants.UNLIMIT) {
					medicalExaminationService.deleteById(medicalExamination.getId());
					return ResponseUtils.responseResult(true);
				} else {
					if ((DateUtils.getDaysBetween(medicalExamination.getCreateTime(), new Date())) > 30) {
						medicalExaminationService.deleteById(medicalExamination.getId());
						return ResponseUtils.responseResult(true);
					} else {
						return ResponseUtils.responseJsonResult(false, "只能删除一月以内的数据！");
					}
				}
			} else {
				return ResponseUtils.responseJsonResult(false, "您没有该数据的操作权限！");
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 
	 * @param request
	 * @param modelMap
	 * @param mFile
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/doImport", method = RequestMethod.POST)
	public ModelAndView doImport(HttpServletRequest request, ModelMap modelMap,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		String type = request.getParameter("type");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (mFile != null && !mFile.isEmpty()) {
			String tenantId = loginContext.getTenantId();
			String gradeId = request.getParameter("gradeId");
			Date checkDate = RequestUtils.getDate(request, "checkDate");
			/**
			 * 判断体检日期是否是当天以前的日期
			 */
			if (StringUtils.isNotEmpty(type) && checkDate != null && DateUtils.beforeTime(checkDate, new Date())) {
				MedicalExaminationXlsImporter bean = new MedicalExaminationXlsImporter();
				MedicalExaminationXlsxImporter beanx = new MedicalExaminationXlsxImporter();
				List<MedicalExaminationXlsArea> areas = new ArrayList<MedicalExaminationXlsArea>();
				MedicalExaminationXlsArea area = new MedicalExaminationXlsArea();
				area.setStartRow(2);
				area.setEndRow(2000);
				area.setNameColIndex(1);
				area.setHeightColIndex(2);
				area.setWeightColIndex(3);
				area.setHemoglobinColIndex(4);
				area.setCheckDateColIndex(5);
				areas.add(area);
				String checkId = null;
				try {
					semaphore2.acquire();
					if (StringUtils.endsWithIgnoreCase(mFile.getOriginalFilename(), ".xlsx")) {
						checkId = beanx.importData(tenantId, gradeId, type, checkDate, areas, mFile.getInputStream());
					} else {
						checkId = bean.importData(tenantId, gradeId, type, checkDate, areas, mFile.getInputStream());
					}
					request.setAttribute("type", type);
					request.setAttribute("checkId", checkId);
					request.setAttribute("gradeId", gradeId);
					request.setAttribute("checkDate", DateUtils.getDate(checkDate));
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					semaphore2.release();
				}
			}
		}
		if (StringUtils.equals(type, "1")) {
			return this.enterList(request, modelMap);
		}
		return this.list(request, modelMap);
	}

	/**
	 * 
	 * @param request
	 * @param modelMap
	 * @param mFile
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/doImport2", method = RequestMethod.POST)
	public ModelAndView doImport2(HttpServletRequest request, ModelMap modelMap,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		String type = request.getParameter("type");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (mFile != null && !mFile.isEmpty()) {
			Date checkDate = RequestUtils.getDate(request, "checkDate");
			/**
			 * 判断体检日期是否是当天以前的日期
			 */
			if (StringUtils.isNotEmpty(type) && checkDate != null && DateUtils.beforeTime(checkDate, new Date())) {

				try {
					semaphore2.acquire();
					HaizgMedicalExaminationConverter converter = new HaizgMedicalExaminationConverter();
					List<MedicalExamination> exams = converter.getMedicalExaminations(mFile.getInputStream());
					if (exams != null && !exams.isEmpty()) {
						if (loginContext.isSystemAdministrator()) {
							String tenantId = request.getParameter("tenantId");
							medicalExaminationService.insertAll(tenantId, type, exams, checkDate);
						} else {
							String tenantId = loginContext.getTenantId();
							medicalExaminationService.insertAll(tenantId, type, exams, checkDate);
						}
					}
					request.setAttribute("checkDate", DateUtils.getDate(checkDate));
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					semaphore2.release();
				}
			}
		}
		if (StringUtils.equals(type, "1")) {
			return this.enterList(request, modelMap);
		}
		return this.list(request, modelMap);
	}

	/**
	 * 
	 * @param request
	 * @param modelMap
	 * @param mFile
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/doImport3", method = RequestMethod.POST)
	public ModelAndView doImport3(HttpServletRequest request, ModelMap modelMap,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		String type = request.getParameter("type");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (mFile != null && !mFile.isEmpty()) {
			Date checkDate = RequestUtils.getDate(request, "checkDate");
			/**
			 * 判断体检日期是否是当天以前的日期
			 */
			if (StringUtils.isNotEmpty(type) && checkDate != null && DateUtils.beforeTime(checkDate, new Date())) {

				try {
					semaphore2.acquire();
					TBNMedicalExaminationConverter converter = new TBNMedicalExaminationConverter();
					List<MedicalExamination> exams = converter.getMedicalExaminations(mFile.getInputStream());
					if (exams != null && !exams.isEmpty()) {
						if (loginContext.isSystemAdministrator()) {
							String tenantId = request.getParameter("tenantId");
							medicalExaminationService.insertAll(tenantId, type, exams, checkDate);
						} else {
							String tenantId = loginContext.getTenantId();
							medicalExaminationService.insertAll(tenantId, type, exams, checkDate);
						}
					}
					request.setAttribute("checkDate", DateUtils.getDate(checkDate));
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					semaphore2.release();
				}
			}
		}
		if (StringUtils.equals(type, "1")) {
			return this.enterList(request, modelMap);
		}
		return this.list(request, modelMap);
	}

	/**
	 * 
	 * @param request
	 * @param modelMap
	 * @param mFile
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/doImportTbx", method = RequestMethod.POST)
	public ModelAndView doImportTbx(HttpServletRequest request, ModelMap modelMap,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		String type = request.getParameter("type");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (mFile != null && !mFile.isEmpty()) {
			String tenantId = loginContext.getTenantId();
			Date checkDate = RequestUtils.getDate(request, "checkDate");
			/**
			 * 判断体检日期是否是当天以前的日期
			 */
			if (StringUtils.isNotEmpty(type) && checkDate != null && DateUtils.beforeTime(checkDate, new Date())) {
				MedicalExaminationTbxExcelImporter bean = new MedicalExaminationTbxExcelImporter();
				List<MedicalExaminationXlsArea> areas = new ArrayList<MedicalExaminationXlsArea>();
				MedicalExaminationXlsArea area = new MedicalExaminationXlsArea();
				area.setStartRow(2);
				area.setEndRow(2000);
				area.setNameColIndex(0);
				area.setGradeColIndex(3);
				area.setWeightColIndex(6);
				area.setHeightColIndex(7);
				area.setCheckDateColIndex(4);
				area.setHemoglobinColIndex(8);
				area.setEyesightLeftColIndex(31);
				area.setEyesightRightColIndex(32);
				areas.add(area);
				String checkId = null;
				try {
					semaphore2.acquire();
					if (StringUtils.endsWithIgnoreCase(mFile.getOriginalFilename(), ".xlsx")) {
						checkId = bean.importData(tenantId, type, checkDate, areas, mFile.getInputStream());
					} else {
						checkId = bean.importData(tenantId, type, checkDate, areas, mFile.getInputStream());
					}
					request.setAttribute("type", type);
					request.setAttribute("checkId", checkId);
					request.setAttribute("checkDate", DateUtils.getDate(checkDate));
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					semaphore2.release();
				}
			}
		}
		if (StringUtils.equals(type, "1")) {
			return this.enterList(request, modelMap);
		}
		return this.list(request, modelMap);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.setAttribute("hasWritePermission", false);
		MedicalExamination medicalExamination = medicalExaminationService
				.getMedicalExamination(RequestUtils.getLong(request, "id"));
		if (medicalExamination != null) {
			request.setAttribute("medicalExamination", medicalExamination);
			request.setAttribute("checkDate", medicalExamination.getCheckDate());
			request.setAttribute("type", medicalExamination.getType());
			String personId = medicalExamination.getPersonId();
			Person person = null;
			if (StringUtils.isNotEmpty(personId)) {
				person = personService.getPerson(personId);
				request.setAttribute("person", person);
			}

			boolean havePermission = false;
			/**
			 * 机构管理员或保健医生
			 */
			if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
				if ((StringUtils.equals(loginContext.getTenantId(), medicalExamination.getTenantId()))) {
					havePermission = true;
				}
			} else {
				/**
				 * 授权的保健机构
				 */
				String tenantId = request.getParameter("tenantId");
				if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
						&& loginContext.getManagedTenantIds().contains(tenantId)) {
					havePermission = true;
				} else {
					/**
					 * 授权的保教人员
					 */
					if (person != null && loginContext.getRoles().contains("Teacher")
							&& loginContext.getGradeIds().contains(person.getGradeId())) {
						havePermission = true;
					}
				}
			}
			if (havePermission) {
				request.setAttribute("hasWritePermission", true);
			} else {
				request.setAttribute("hasWritePermission", false);
			}
		} else {
			String personId = request.getParameter("personId");
			String gradeId = request.getParameter("gradeId");
			Person person = null;
			if (StringUtils.isNotEmpty(personId)) {
				person = personService.getPerson(personId);
				request.setAttribute("person", person);
			} else {
				if (gradeId != null && gradeId.trim().length() > 0) {
					List<Person> persons = personService.getPersons(gradeId);
					request.setAttribute("persons", persons);
				}
			}

			String checkId = request.getParameter("checkId");
			if (StringUtils.isNotEmpty(checkId)) {
				MedicalExaminationDef examDef = medicalExaminationDefService.getMedicalExaminationDefByCheckId(checkId);
				request.setAttribute("examDef", examDef);
				request.setAttribute("type", examDef.getType());
				request.setAttribute("checkDate", examDef.getCheckDate());
			}

			boolean havePermission = false;
			/**
			 * 机构管理员或保健医生
			 */
			if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
				havePermission = true;
			} else {
				/**
				 * 授权的保健机构
				 */
				String tenantId = request.getParameter("tenantId");
				if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
						&& loginContext.getManagedTenantIds().contains(tenantId)) {
					havePermission = true;
				} else {
					/**
					 * 授权的保教人员
					 */
					if (person != null && loginContext.getRoles().contains("Teacher")
							&& loginContext.getGradeIds().contains(person.getGradeId())) {
						havePermission = true;
					}
				}
			}
			if (havePermission) {
				request.setAttribute("hasWritePermission", true);
			} else {
				request.setAttribute("hasWritePermission", false);
			}
		}

		String type = request.getParameter("type");
		if (StringUtils.equals(type, "7")) {
			return new ModelAndView("/heathcare/medicalExamination/edit7", modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("medicalExamination.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/edit", modelMap);
	}

	@RequestMapping("/enterEdit")
	public ModelAndView enterEdit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		request.setAttribute("hasWritePermission", false);
		MedicalExamination medicalExamination = medicalExaminationService
				.getMedicalExamination(RequestUtils.getLong(request, "id"));
		if (medicalExamination != null) {
			request.setAttribute("medicalExamination", medicalExamination);
			request.setAttribute("checkDate", medicalExamination.getCheckDate());
			request.setAttribute("type", medicalExamination.getType());
			String personId = medicalExamination.getPersonId();
			Person person = null;
			if (StringUtils.isNotEmpty(personId)) {
				person = personService.getPerson(personId);
				request.setAttribute("person", person);
			}

			boolean havePermission = false;
			/**
			 * 机构管理员或保健医生
			 */
			if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
				if ((StringUtils.equals(loginContext.getTenantId(), medicalExamination.getTenantId()))) {
					havePermission = true;
				}
			} else {
				/**
				 * 授权的保健机构
				 */
				String tenantId = request.getParameter("tenantId");
				if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
						&& loginContext.getManagedTenantIds().contains(tenantId)) {
					havePermission = true;
				} else {
					/**
					 * 授权的保教人员
					 */
					if (person != null && loginContext.getRoles().contains("Teacher")
							&& loginContext.getGradeIds().contains(person.getGradeId())) {
						havePermission = true;
					}
				}
			}
			if (havePermission) {
				request.setAttribute("hasWritePermission", true);
			} else {
				request.setAttribute("hasWritePermission", false);
			}
		} else {
			String personId = request.getParameter("personId");
			String gradeId = request.getParameter("gradeId");
			Person person = null;
			if (StringUtils.isNotEmpty(personId)) {
				person = personService.getPerson(personId);
				request.setAttribute("person", person);
			} else {
				if (gradeId != null && gradeId.trim().length() > 0) {
					List<Person> persons = personService.getPersons(gradeId);
					request.setAttribute("persons", persons);
				}
			}

			String checkId = request.getParameter("checkId");
			if (StringUtils.isNotEmpty(checkId)) {
				MedicalExaminationDef examDef = medicalExaminationDefService.getMedicalExaminationDefByCheckId(checkId);
				request.setAttribute("examDef", examDef);
				request.setAttribute("type", examDef.getType());
				request.setAttribute("checkDate", examDef.getCheckDate());
			}

			boolean havePermission = false;
			/**
			 * 机构管理员或保健医生
			 */
			if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
				havePermission = true;
			} else {
				/**
				 * 授权的保健机构
				 */
				String tenantId = request.getParameter("tenantId");
				if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
						&& loginContext.getManagedTenantIds().contains(tenantId)) {
					havePermission = true;
				} else {
					/**
					 * 授权的保教人员
					 */
					if (person != null && loginContext.getRoles().contains("Teacher")
							&& loginContext.getGradeIds().contains(person.getGradeId())) {
						havePermission = true;
					}
				}
			}
			if (havePermission) {
				request.setAttribute("hasWritePermission", true);
			} else {
				request.setAttribute("hasWritePermission", false);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("medicalExamination.enterEdit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/enterEdit", modelMap);
	}

	@RequestMapping("enterList")
	public ModelAndView enterList(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		MedicalExaminationDefQuery query = new MedicalExaminationDefQuery();
		query.tenantId(loginContext.getTenantId());
		String type = request.getParameter("type");
		if (StringUtils.isNotEmpty(type)) {
			query.type(type);
		}

		List<MedicalExaminationDef> examList = medicalExaminationDefService.list(query);
		request.setAttribute("examList", examList);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		List<Integer> years = new ArrayList<Integer>();
		years.add(year);
		if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			years.add(year - 1);
		}
		request.setAttribute("years", years);

		List<Integer> months = new ArrayList<Integer>();

		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("months", months);
		request.setAttribute("ts", System.currentTimeMillis());

		boolean havePermission = false;
		/**
		 * 机构管理员或保健医生
		 */
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			havePermission = true;
		} else {
			/**
			 * 授权的保健机构
			 */
			String tenantId = request.getParameter("tenantId");
			if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
					&& loginContext.getManagedTenantIds().contains(tenantId)) {
				havePermission = true;
			} else {

			}
		}
		if (havePermission) {
			request.setAttribute("hasWritePermission", true);
		} else {
			request.setAttribute("hasWritePermission", false);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/enterList", modelMap);
	}

	@RequestMapping("examList")
	public ModelAndView examList(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tenantId = request.getParameter("tenantId");

		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			tenantId = loginContext.getTenantId();
		}

		MedicalExaminationDefQuery query = new MedicalExaminationDefQuery();
		query.tenantId(tenantId);

		String type = request.getParameter("type");
		if (StringUtils.isNotEmpty(type)) {
			query.type(type);
		}

		String personId = request.getParameter("personId");
		Person person = personService.getPerson(personId);
		request.setAttribute("person", person);

		List<MedicalExaminationDef> examList = medicalExaminationDefService.list(query);
		request.setAttribute("examList", examList);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		List<Integer> years = new ArrayList<Integer>();
		years.add(year);
		if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			years.add(year - 1);
		}
		request.setAttribute("years", years);

		List<Integer> months = new ArrayList<Integer>();

		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("months", months);

		request.setAttribute("ts", System.currentTimeMillis());

		boolean havePermission = false;
		/**
		 * 机构管理员或保健医生
		 */
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			havePermission = true;
		} else {
			/**
			 * 授权的保健机构
			 */
			tenantId = request.getParameter("tenantId");
			if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
					&& loginContext.getManagedTenantIds().contains(tenantId)) {
				havePermission = true;
			} else {
				/**
				 * 授权的保教人员
				 */
				if (person != null && loginContext.getRoles().contains("Teacher")
						&& loginContext.getGradeIds().contains(person.getGradeId())) {
					havePermission = true;
				}
			}
		}
		if (havePermission) {
			request.setAttribute("hasWritePermission", true);
		} else {
			request.setAttribute("hasWritePermission", false);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/examList", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		logger.debug("--------------------------json--------------------");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		MedicalExaminationQuery query = new MedicalExaminationQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		JSONObject result = new JSONObject();

		String gradeId = request.getParameter("gradeId");

		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			query.tenantId(loginContext.getTenantId());
		} else if (loginContext.getRoles().contains("Teacher")) {
			query.tenantId(loginContext.getTenantId());
			if (loginContext.getGradeIds().contains(gradeId)) {
				query.gradeId(gradeId);
			} else {
				return result.toJSONString().getBytes();
			}
		} else {
			if (!loginContext.getGradeIds().contains(gradeId)) {
				return result.toJSONString().getBytes();
			}
		}

		int heightLevel = RequestUtils.getInt(request, "heightLevel");
		if (heightLevel >= 0) {
			query.heightLevelGreaterThanOrEqual(heightLevel);
		}
		if (heightLevel < 0) {
			query.heightLevelLessThanOrEqual(heightLevel);
		}

		if (StringUtils.isNotEmpty(gradeId)) {
			query.gradeId(gradeId);
		} else {
			List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
			if (grades != null && !grades.isEmpty()) {
				List<String> gradeIds = new ArrayList<String>();
				for (GradeInfo grade : grades) {
					gradeIds.add(grade.getId());
				}
				query.gradeIds(gradeIds);
			}
		}

		int start = 0;
		int limit = 50;
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
			limit = 50;
		}

		int total = medicalExaminationService.getMedicalExaminationCountByQueryCriteria(query);
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

			List<MedicalExamination> list = medicalExaminationService.getMedicalExaminationsByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<Person> persons = null;
				if (StringUtils.isNotEmpty(gradeId)) {
					persons = personService.getPersons(gradeId);
				} else {
					persons = personService.getTenantPersons(loginContext.getTenantId());
				}

				Map<String, Person> personMap = new HashMap<String, Person>();
				if (persons != null && !persons.isEmpty()) {
					for (Person person : persons) {
						personMap.put(person.getId(), person);
					}
				}

				List<GrowthStandard> list4 = growthStandardService.getAllGrowthStandards();
				Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
				if (list4 != null && !list4.isEmpty()) {
					for (GrowthStandard gs : list4) {
						gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
					}
				}

				List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
				Map<String, String> gradeMap = new HashMap<String, String>();
				if (grades != null && !grades.isEmpty()) {
					for (GradeInfo grade : grades) {
						gradeMap.put(grade.getId(), grade.getName());
					}
				}

				Person person = null;
				MedicalExaminationHelper helper = new MedicalExaminationHelper();
				for (MedicalExamination medicalExamination : list) {
					helper.evaluate(gsMap, medicalExamination);
					JSONObject rowJSON = medicalExamination.toJsonObject();
					rowJSON.put("id", medicalExamination.getId());
					rowJSON.put("rowId", medicalExamination.getId());
					rowJSON.put("medicalExaminationId", medicalExamination.getId());
					rowJSON.put("startIndex", ++start);
					person = personMap.get(medicalExamination.getPersonId());
					if (person != null && person.getDeleteFlag() == 0) {
						if (gradeMap.get(person.getGradeId()) != null) {
							rowJSON.put("gradeName", gradeMap.get(person.getGradeId()));
						}
						rowJSON.put("sex", person.getSex());
						if (person.getBirthday() != null) {
							medicalExamination.setBirthday(person.getBirthday());
							rowJSON.put("birthday", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_date", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_datetime", DateUtils.getDateTime(person.getBirthday()));
							rowJSON.put("checkAgeOfMonth",
									medicalExamination.getAgeOfTheMoon() > 0 ? medicalExamination.getAgeOfTheMoon()
											: "-");
						}
						rowsJSON.add(rowJSON);
					}
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

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		MedicalExaminationDefQuery query = new MedicalExaminationDefQuery();
		query.tenantId(loginContext.getTenantId());
		String type = request.getParameter("type");
		if (StringUtils.isNotEmpty(type)) {
			query.type(type);
		}

		List<MedicalExaminationDef> examList = medicalExaminationDefService.list(query);
		request.setAttribute("examList", examList);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		List<Integer> years = new ArrayList<Integer>();
		years.add(year);
		if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			years.add(year - 1);
		}
		request.setAttribute("years", years);

		List<Integer> months = new ArrayList<Integer>();

		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("months", months);

		request.setAttribute("ts", System.currentTimeMillis());

		boolean havePermission = false;
		/**
		 * 机构管理员或保健医生
		 */
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			havePermission = true;
		} else {
			/**
			 * 授权的保健机构
			 */
			String tenantId = request.getParameter("tenantId");
			if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
					&& loginContext.getManagedTenantIds().contains(tenantId)) {
				havePermission = true;
			} else {

			}
		}
		if (havePermission) {
			request.setAttribute("hasWritePermission", true);
		} else {
			request.setAttribute("hasWritePermission", false);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/list", modelMap);
	}

	/**
	 * 健康监测卡
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/monitorEdit")
	public ModelAndView monitorEdit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		request.setAttribute("hasWritePermission", false);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		MedicalExamination medicalExamination = medicalExaminationService
				.getMedicalExamination(RequestUtils.getLong(request, "id"));
		if (medicalExamination != null) {
			request.setAttribute("medicalExamination", medicalExamination);
			request.setAttribute("checkDate", medicalExamination.getCheckDate());
			request.setAttribute("type", medicalExamination.getType());
			String personId = medicalExamination.getPersonId();
			Person person = null;
			if (StringUtils.isNotEmpty(personId)) {
				person = personService.getPerson(personId);
				request.setAttribute("person", person);
			}

			boolean havePermission = false;
			/**
			 * 机构管理员或保健医生
			 */
			if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
				if ((StringUtils.equals(loginContext.getTenantId(), medicalExamination.getTenantId()))) {
					havePermission = true;
				}
			} else {
				/**
				 * 授权的保健机构
				 */
				String tenantId = request.getParameter("tenantId");
				if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
						&& loginContext.getManagedTenantIds().contains(tenantId)) {
					havePermission = true;
				} else {
					/**
					 * 授权的保教人员
					 */
					if (person != null && loginContext.getRoles().contains("Teacher")
							&& loginContext.getGradeIds().contains(person.getGradeId())) {
						havePermission = true;
					}
				}
			}
			if (havePermission) {
				request.setAttribute("hasWritePermission", true);
			} else {
				request.setAttribute("hasWritePermission", false);
			}

		} else {
			String personId = request.getParameter("personId");
			String gradeId = request.getParameter("gradeId");
			Person person = null;
			if (StringUtils.isNotEmpty(personId)) {
				person = personService.getPerson(personId);
				request.setAttribute("person", person);
			} else {
				if (gradeId != null && gradeId.trim().length() > 0) {
					List<Person> persons = personService.getPersons(gradeId);
					request.setAttribute("persons", persons);
				}
			}

			String checkId = request.getParameter("checkId");
			if (StringUtils.isNotEmpty(checkId)) {
				MedicalExaminationDef examDef = medicalExaminationDefService.getMedicalExaminationDefByCheckId(checkId);
				request.setAttribute("examDef", examDef);
				request.setAttribute("type", examDef.getType());
				request.setAttribute("checkDate", examDef.getCheckDate());
			}

			boolean havePermission = false;
			/**
			 * 机构管理员或保健医生
			 */
			if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
				havePermission = true;
			} else {
				/**
				 * 授权的保健机构
				 */
				String tenantId = request.getParameter("tenantId");
				if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
						&& loginContext.getManagedTenantIds().contains(tenantId)) {
					havePermission = true;
				} else {
					/**
					 * 授权的保教人员
					 */
					if (person != null && loginContext.getRoles().contains("Teacher")
							&& loginContext.getGradeIds().contains(person.getGradeId())) {
						havePermission = true;
					}
				}
			}
			if (havePermission) {
				request.setAttribute("hasWritePermission", true);
			} else {
				request.setAttribute("hasWritePermission", false);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("medicalExamination.monitorEdit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/monitorEdit", modelMap);
	}

	/**
	 * 健康监测列表
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("monitorList")
	public ModelAndView monitorList(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		MedicalExaminationDefQuery query = new MedicalExaminationDefQuery();
		query.tenantId(loginContext.getTenantId());
		String type = request.getParameter("type");
		if (StringUtils.isNotEmpty(type)) {
			query.type(type);
		}

		List<MedicalExaminationDef> examList = medicalExaminationDefService.list(query);
		request.setAttribute("examList", examList);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		List<Integer> years = new ArrayList<Integer>();
		years.add(year);
		if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			years.add(year - 1);
		}
		request.setAttribute("years", years);

		List<Integer> months = new ArrayList<Integer>();

		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("months", months);

		request.setAttribute("ts", System.currentTimeMillis());

		boolean havePermission = false;
		/**
		 * 机构管理员或保健医生
		 */
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			havePermission = true;
		} else {
			/**
			 * 授权的保健机构
			 */
			String tenantId = request.getParameter("tenantId");
			if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
					&& loginContext.getManagedTenantIds().contains(tenantId)) {
				havePermission = true;
			} else {

			}
		}
		if (havePermission) {
			request.setAttribute("hasWritePermission", true);
		} else {
			request.setAttribute("hasWritePermission", false);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/monitorList", modelMap);
	}

	@RequestMapping("/personJson")
	@ResponseBody
	public byte[] personJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		MedicalExaminationQuery query = new MedicalExaminationQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);

		JSONObject result = new JSONObject();

		String personId = request.getParameter("personId");
		query.personId(personId);

		int start = 0;
		int limit = 50;
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
			limit = 50;
		}

		int total = medicalExaminationService.getMedicalExaminationCountByQueryCriteria(query);
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

			List<MedicalExamination> list = medicalExaminationService.getMedicalExaminationsByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<GrowthStandard> list4 = growthStandardService.getAllGrowthStandards();
				Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
				if (list4 != null && !list4.isEmpty()) {
					for (GrowthStandard gs : list4) {
						gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
					}
				}

				MedicalExaminationHelper helper = new MedicalExaminationHelper();
				Person person = personService.getPerson(personId);
				for (MedicalExamination medicalExamination : list) {
					helper.evaluate(gsMap, medicalExamination);
					JSONObject rowJSON = medicalExamination.toJsonObject();
					rowJSON.put("id", medicalExamination.getId());
					rowJSON.put("rowId", medicalExamination.getId());
					rowJSON.put("medicalExaminationId", medicalExamination.getId());
					rowJSON.put("startIndex", ++start);
					if (person != null) {
						rowJSON.put("sex", person.getSex());
						if (person.getBirthday() != null) {
							medicalExamination.setBirthday(person.getBirthday());
							rowJSON.put("birthday", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_date", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_datetime", DateUtils.getDateTime(person.getBirthday()));
							rowJSON.put("checkAgeOfMonth",
									medicalExamination.getAgeOfTheMoon() > 0 ? medicalExamination.getAgeOfTheMoon()
											: "-");
						}
					}
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

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("medicalExamination.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/medicalExamination/query", modelMap);
	}

	@RequestMapping("/reviewJson")
	@ResponseBody
	public byte[] reviewJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		MedicalExaminationQuery query = new MedicalExaminationQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		JSONObject result = new JSONObject();

		String gradeId = request.getParameter("gradeId");
		String tenantId = request.getParameter("tenantId");
		if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
				&& loginContext.getManagedTenantIds().contains(tenantId)) {
			query.tenantId(tenantId);
			if (StringUtils.isNotEmpty(gradeId)) {
				query.gradeId(gradeId);
			} else {
				List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(tenantId);
				if (grades != null && !grades.isEmpty()) {
					List<String> gradeIds = new ArrayList<String>();
					for (GradeInfo grade : grades) {
						gradeIds.add(grade.getId());
					}
					query.gradeIds(gradeIds);
				}
			}
		} else {
			return result.toJSONString().getBytes();
		}

		int heightLevel = RequestUtils.getInt(request, "heightLevel");
		if (heightLevel >= 0) {
			query.heightLevelGreaterThanOrEqual(heightLevel);
		}
		if (heightLevel < 0) {
			query.heightLevelLessThanOrEqual(heightLevel);
		}

		int start = 0;
		int limit = 50;
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
			limit = 50;
		}

		int total = medicalExaminationService.getMedicalExaminationCountByQueryCriteria(query);
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

			List<MedicalExamination> list = medicalExaminationService.getMedicalExaminationsByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<Person> persons = personService.getTenantPersons(tenantId);

				Map<String, Person> personMap = new HashMap<String, Person>();
				if (persons != null && !persons.isEmpty()) {
					for (Person person : persons) {
						personMap.put(person.getId(), person);
					}
				}

				List<GrowthStandard> list4 = growthStandardService.getAllGrowthStandards();
				Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
				if (list4 != null && !list4.isEmpty()) {
					for (GrowthStandard gs : list4) {
						gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
					}
				}

				List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(tenantId);
				Map<String, String> gradeMap = new HashMap<String, String>();
				if (grades != null && !grades.isEmpty()) {
					for (GradeInfo grade : grades) {
						gradeMap.put(grade.getId(), grade.getName());
					}
				}

				Person person = null;
				MedicalExaminationHelper helper = new MedicalExaminationHelper();
				for (MedicalExamination medicalExamination : list) {
					helper.evaluate(gsMap, medicalExamination);
					JSONObject rowJSON = medicalExamination.toJsonObject();
					rowJSON.put("id", medicalExamination.getId());
					rowJSON.put("rowId", medicalExamination.getId());
					rowJSON.put("medicalExaminationId", medicalExamination.getId());
					rowJSON.put("startIndex", ++start);
					person = personMap.get(medicalExamination.getPersonId());
					if (person != null && person.getDeleteFlag() == 0) {
						if (gradeMap.get(person.getGradeId()) != null) {
							rowJSON.put("gradeName", gradeMap.get(person.getGradeId()));
						}
						rowJSON.put("sex", person.getSex());
						if (person.getBirthday() != null) {
							medicalExamination.setBirthday(person.getBirthday());
							rowJSON.put("birthday", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_date", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_datetime", DateUtils.getDateTime(person.getBirthday()));
							rowJSON.put("checkAgeOfMonth",
									medicalExamination.getAgeOfTheMoon() > 0 ? medicalExamination.getAgeOfTheMoon()
											: "-");
						}
						rowsJSON.add(rowJSON);
					}
				}
			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/reviewlist")
	public ModelAndView reviewlist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String tenantId = request.getParameter("tenantId");

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(tenantId);
		request.setAttribute("gradeInfos", list);

		MedicalExaminationDefQuery query = new MedicalExaminationDefQuery();
		query.tenantId(tenantId);
		String type = request.getParameter("type");
		if (StringUtils.isNotEmpty(type)) {
			query.type(type);
		}

		List<MedicalExaminationDef> examList = medicalExaminationDefService.list(query);
		request.setAttribute("examList", examList);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		List<Integer> years = new ArrayList<Integer>();
		years.add(year);
		if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			years.add(year - 1);
		}
		request.setAttribute("years", years);

		List<Integer> months = new ArrayList<Integer>();

		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("months", months);

		boolean havePermission = false;

		/**
		 * 授权的保健机构
		 */
		if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
				&& loginContext.getManagedTenantIds().contains(tenantId)) {
			havePermission = true;
		}

		if (havePermission) {
			request.setAttribute("hasWritePermission", true);
		} else {
			request.setAttribute("hasWritePermission", false);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/review_list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		long id = RequestUtils.getLong(request, "id");
		String gradeId = request.getParameter("gradeId");
		String personId = request.getParameter("personId");
		String type = request.getParameter("type");
		if (RequestUtils.getDouble(request, "height") < 50) {
			return ResponseUtils.responseJsonResult(false, "身高不能低于50厘米。");
		}
		if (RequestUtils.getDouble(request, "height") > 160) {
			return ResponseUtils.responseJsonResult(false, "身高不能超过160厘米。");
		}
		if (RequestUtils.getDouble(request, "weight") < 5) {
			return ResponseUtils.responseJsonResult(false, "体重不能低于5千克。");
		}
		if (RequestUtils.getDouble(request, "weight") > 80) {
			return ResponseUtils.responseJsonResult(false, "体重不能超过80千克。");
		}
		if (RequestUtils.getDate(request, "checkDate") == null) {
			return ResponseUtils.responseJsonResult(false, "检查日期不能为空。");
		}
		if (DateUtils.getYearMonthDay(RequestUtils.getDate(request, "checkDate")) > DateUtils.getNowYearMonthDay()) {
			return ResponseUtils.responseJsonResult(false, "检查日期不能是未来的日期。");
		}
		boolean havePermission = false;
		MedicalExamination medicalExamination = null;
		try {
			if (id > 0) {
				medicalExamination = medicalExaminationService.getMedicalExamination(id);
			}

			Person person = personService.getPerson(personId);

			if (person.getBirthday() == null) {
				return ResponseUtils.responseJsonResult(false, "该儿童的出生日期为空，不能计算BMI，请先完善儿童基本信息。");
			}

			if (person.getJoinDate() == null) {
				return ResponseUtils.responseJsonResult(false, "该儿童的入园日期为空，请先完善儿童基本信息。");
			}

			if (DateUtils.getYearMonthDay(RequestUtils.getDate(request, "checkDate")) < DateUtils
					.getYearMonthDay(person.getJoinDate())) {
				// return ResponseUtils.responseJsonResult(false, "检查日期不能是入园之前的日期。");
			}

			if (medicalExamination == null) {
				if (StringUtils.isEmpty(type)) {
					return ResponseUtils.responseJsonResult(false, "检查类型不能为空。");
				}
				medicalExamination = new MedicalExamination();
				medicalExamination.setTenantId(person.getTenantId());
				medicalExamination.setGradeId(person.getGradeId());
				medicalExamination.setPersonId(person.getId());
				medicalExamination.setName(person.getName().trim());
				medicalExamination.setSex(person.getSex());
				medicalExamination.setBirthday(person.getBirthday());
				medicalExamination.setCreateTime(new Date());

				/**
				 * 机构管理员或保健医生
				 */
				if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
					if ((StringUtils.equals(loginContext.getTenantId(), medicalExamination.getTenantId()))) {
						havePermission = true;
					}
				} else {
					/**
					 * 授权的保健机构
					 */
					String tenantId = request.getParameter("tenantId");
					if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
							&& loginContext.getManagedTenantIds().contains(tenantId)) {
						havePermission = true;
					} else {
						/**
						 * 授权的保教人员
						 */
						if (loginContext.getRoles().contains("Teacher")
								&& loginContext.getGradeIds().contains(gradeId)) {
							havePermission = true;
						}
					}
				}

			} else {

				/**
				 * 机构管理员或保健医生
				 */
				if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
					if ((StringUtils.equals(loginContext.getTenantId(), medicalExamination.getTenantId()))) {
						havePermission = true;
					}
				} else {
					/**
					 * 授权的保健机构
					 */
					String tenantId = request.getParameter("tenantId");
					if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
							&& loginContext.getManagedTenantIds().contains(tenantId)) {
						havePermission = true;
					} else {
						/**
						 * 授权的保教人员
						 */
						if (loginContext.getRoles().contains("Teacher")
								&& loginContext.getGradeIds().contains(gradeId)) {
							havePermission = true;
						}
					}
				}
			}

			if (!havePermission) {
				return ResponseUtils.responseJsonResult(false, "您没有该数据的操作权限!");
			} else if ((DateUtils.getDaysBetween(medicalExamination.getCreateTime(), new Date())) > 30) {
				if (loginContext.getLimit() != Constants.UNLIMIT) {
					return ResponseUtils.responseJsonResult(false, "只能修改一月以内的数据!");
				}
			}

			if (!StringUtils.equals(type, "1")) {// 排除入园体检
				MedicalExamination latest = medicalExaminationService.getLatestMedicalExamination(personId);
				if (latest != null && latest.getId() != medicalExamination.getId()) {
					if (latest.getHeight() > RequestUtils.getDouble(request, "height")) {
						SysTenant tenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
						if (!StringUtils.equals(tenant.getDisableDataConstraint(), "Y")) {
							return ResponseUtils.responseJsonResult(false,
									"身高数据不正确，最近一次体检数据为" + latest.getHeight() + "厘米。");
						}
					}
					medicalExamination
							.setHeightIncrement(RequestUtils.getDouble(request, "height") - latest.getHeight());
					medicalExamination
							.setWeightIncrement(RequestUtils.getDouble(request, "weight") - latest.getWeight());
				}
			}

			medicalExamination.setBirthday(person.getBirthday());
			medicalExamination.setBatchId(RequestUtils.getLong(request, "batchId"));
			medicalExamination.setHeight(RequestUtils.getDouble(request, "height"));
			medicalExamination.setHeightEvaluate(request.getParameter("heightEvaluate"));
			medicalExamination.setWeight(RequestUtils.getDouble(request, "weight"));
			medicalExamination.setWeightEvaluate(request.getParameter("weightEvaluate"));
			medicalExamination.setWeightHeightPercent(RequestUtils.getDouble(request, "weightHeightPercent"));
			medicalExamination.setAllergy(request.getParameter("allergy"));
			medicalExamination.setEyeLeft(request.getParameter("eyeLeft"));
			medicalExamination.setEyeLeftRemark(request.getParameter("eyeLeftRemark"));
			medicalExamination.setEyeRight(request.getParameter("eyeRight"));
			medicalExamination.setEyeRightRemark(request.getParameter("eyeRightRemark"));
			medicalExamination.setEyesightLeft(RequestUtils.getDouble(request, "eyesightLeft"));
			medicalExamination.setEyesightRight(RequestUtils.getDouble(request, "eyesightRight"));
			medicalExamination.setEarLeft(request.getParameter("earLeft"));
			medicalExamination.setEarLeftRemark(request.getParameter("earLeftRemark"));
			medicalExamination.setEarRight(request.getParameter("earRight"));
			medicalExamination.setEarRightRemark(request.getParameter("earRightRemark"));
			medicalExamination.setTooth(RequestUtils.getInt(request, "tooth"));
			medicalExamination.setSaprodontia(RequestUtils.getInt(request, "saprodontia"));
			medicalExamination.setHead(request.getParameter("head"));
			medicalExamination.setHeadRemark(request.getParameter("headRemark"));
			medicalExamination.setThorax(request.getParameter("thorax"));
			medicalExamination.setThoraxRemark(request.getParameter("thoraxRemark"));
			medicalExamination.setSpine(request.getParameter("spine"));
			medicalExamination.setSpineRemark(request.getParameter("spineRemark"));
			medicalExamination.setPharyngeal(request.getParameter("pharyngeal"));
			medicalExamination.setPharyngealRemark(request.getParameter("pharyngealRemark"));
			medicalExamination.setCardiopulmonary(request.getParameter("cardiopulmonary"));
			medicalExamination.setCardiopulmonaryRemark(request.getParameter("cardiopulmonaryRemark"));
			medicalExamination.setHepatolienal(request.getParameter("hepatolienal"));
			medicalExamination.setHepatolienalRemark(request.getParameter("hepatolienalRemark"));
			medicalExamination.setPudendum(request.getParameter("pudendum"));
			medicalExamination.setPudendumRemark(request.getParameter("pudendumRemark"));
			medicalExamination.setSkin(request.getParameter("skin"));
			medicalExamination.setSkinRemark(request.getParameter("skinRemark"));
			medicalExamination.setLymphoid(request.getParameter("lymphoid"));
			medicalExamination.setLymphoidRemark(request.getParameter("lymphoidRemark"));
			medicalExamination.setBregma(request.getParameter("bregma"));
			medicalExamination.setBregmaRemark(request.getParameter("bregmaRemark"));
			medicalExamination.setOralogy(request.getParameter("oralogy"));
			medicalExamination.setOralogyRemark(request.getParameter("oralogyRemark"));
			medicalExamination.setTonsil(request.getParameter("tonsil"));
			medicalExamination.setTonsilRemark(request.getParameter("tonsilRemark"));
			medicalExamination.setBone(request.getParameter("bone"));
			medicalExamination.setBoneRemark(request.getParameter("boneRemark"));
			medicalExamination.setBirthDefect(request.getParameter("birthDefect"));
			medicalExamination.setPreviousHistory(request.getParameter("previousHistory"));
			medicalExamination.setHemoglobin(request.getParameter("hemoglobin"));
			medicalExamination.setHemoglobinValue(RequestUtils.getDouble(request, "hemoglobinValue"));
			medicalExamination.setAlt(request.getParameter("alt"));
			medicalExamination.setAltValue(RequestUtils.getDouble(request, "altValue"));
			medicalExamination.setHbsab(request.getParameter("hbsab"));
			medicalExamination.setHbsabValue(RequestUtils.getDouble(request, "hbsabValue"));
			medicalExamination.setSgpt(request.getParameter("sgpt"));
			medicalExamination.setHvaigm(request.getParameter("hvaigm"));
			medicalExamination.setLymphoid(request.getParameter("lymphoid"));
			medicalExamination.setBregma(request.getParameter("bregma"));
			medicalExamination.setOralogy(request.getParameter("oralogy"));
			medicalExamination.setTonsil(request.getParameter("tonsil"));
			medicalExamination.setBone(request.getParameter("bone"));
			medicalExamination.setBirthDefect(request.getParameter("birthDefect"));
			medicalExamination.setHealthEvaluate(request.getParameter("healthEvaluate"));
			medicalExamination.setType(request.getParameter("type"));
			medicalExamination.setCheckDate(RequestUtils.getDate(request, "checkDate"));
			medicalExamination.setCheckDoctor(request.getParameter("checkDoctor"));
			medicalExamination.setCheckDoctorId(request.getParameter("checkDoctorId"));
			medicalExamination.setCheckResult(request.getParameter("checkResult"));
			medicalExamination.setCheckOrganization(request.getParameter("checkOrganization"));
			medicalExamination.setCheckOrganizationId(RequestUtils.getLong(request, "checkOrganizationId"));
			medicalExamination.setDoctorSuggest(request.getParameter("doctorSuggest"));
			medicalExamination.setRemark(request.getParameter("remark"));
			medicalExamination.setCreateBy(actorId);

			String checkId = request.getParameter("checkId");
			if (StringUtils.isNotEmpty(checkId)) {
				MedicalExaminationDef examDef = medicalExaminationDefService.getMedicalExaminationDefByCheckId(checkId);
				if (examDef != null) {
					medicalExamination.setCheckId(checkId);
					medicalExamination.setCheckDate(examDef.getCheckDate());
					medicalExamination.setType(examDef.getType());
				}
			}

			this.medicalExaminationService.save(medicalExamination);

			return ResponseUtils.responseJsonResult(true);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.growthStandardService")
	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalExaminationDefService")
	public void setMedicalExaminationDefService(MedicalExaminationDefService medicalExaminationDefService) {
		this.medicalExaminationDefService = medicalExaminationDefService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalExaminationService")
	public void setMedicalExaminationService(MedicalExaminationService medicalExaminationService) {
		this.medicalExaminationService = medicalExaminationService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@RequestMapping("/showImport")
	public ModelAndView showImport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String tenantId = request.getParameter("tenantId");
			List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(tenantId);
			request.setAttribute("gradeInfos", list);
		} else {
			List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
			request.setAttribute("gradeInfos", list);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("medicalExamination.showImport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/medicalExamination/showImport", modelMap);
	}

	@RequestMapping("/showReport")
	public ModelAndView showReport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.setAttribute("hasReportPermission", false);
		boolean havePermission = false;
		/**
		 * 机构管理员或保健医生
		 */
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			havePermission = true;
		} else {
			/**
			 * 授权的保健机构
			 */
			String tenantId = request.getParameter("tenantId");
			if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
					&& loginContext.getManagedTenantIds().contains(tenantId)) {
				havePermission = true;
			}
		}

		if (havePermission) {
			request.setAttribute("hasReportPermission", true);
		} else {
			request.setAttribute("hasReportPermission", false);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("medicalExamination.showReport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/showReport", modelMap);
	}

	@RequestMapping("/showTenantReport")
	public ModelAndView showTenantReport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.setAttribute("hasReportPermission", false);
		boolean havePermission = false;
		/**
		 * 机构管理员或保健医生
		 */
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			havePermission = true;
		} else {
			/**
			 * 授权的保健机构
			 */
			String tenantId = request.getParameter("tenantId");
			if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
					&& loginContext.getManagedTenantIds().contains(tenantId)) {
				havePermission = true;
			}
		}

		if (havePermission) {
			request.setAttribute("hasReportPermission", true);
		} else {
			request.setAttribute("hasReportPermission", false);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("medicalExamination.showTenantReport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExamination/showTenantReport", modelMap);
	}

}
