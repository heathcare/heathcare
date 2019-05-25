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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.*;
import com.glaf.heathcare.bean.MedicalSpotCheckEvaluateBean;
import com.glaf.heathcare.bean.MedicalSpotCheckExcelImporter;
import com.glaf.heathcare.bean.MedicalSpotCheckExcelImporterV2;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalExaminationDef;
import com.glaf.heathcare.domain.MedicalSpotCheck;
import com.glaf.heathcare.domain.MedicalSpotCheckXlsArea;
import com.glaf.heathcare.helper.MedicalExaminationEvaluateHelper;
import com.glaf.heathcare.query.MedicalExaminationDefQuery;
import com.glaf.heathcare.query.MedicalSpotCheckQuery;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationDefService;
import com.glaf.heathcare.service.MedicalSpotCheckService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SpringMVC控制器
 */

@Controller("/heathcare/medicalSpotCheck")
@RequestMapping("/heathcare/medicalSpotCheck")
public class MedicalSpotCheckController {
	protected static final Log logger = LogFactory.getLog(MedicalSpotCheckController.class);

	protected GrowthStandardService growthStandardService;

	protected MedicalSpotCheckService medicalSpotCheckService;

	protected MedicalExaminationDefService medicalExaminationDefService;

	public MedicalSpotCheckController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			List<String> xids = StringTools.split(ids);
			try {
				medicalSpotCheckService.deleteByIds(loginContext.getTenantId(), xids);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * @param request
	 * @param response
	 * @param mFile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/doImport", method = RequestMethod.POST)
	public void doImport(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		String checkId = request.getParameter("checkId");
		String type = request.getParameter("type");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (mFile != null && !mFile.isEmpty()) {
			if (StringUtils.isNotEmpty(type)) {
				MedicalSpotCheckExcelImporter bean = new MedicalSpotCheckExcelImporter();
				List<MedicalSpotCheckXlsArea> areas = new ArrayList<MedicalSpotCheckXlsArea>();
				MedicalSpotCheckXlsArea area = new MedicalSpotCheckXlsArea();
				int index = 0;
				area.setStartRow(1);
				area.setEndRow(5000);
				area.setOrganizationColIndex(index++);
				area.setGradeColIndex(index++);
				area.setNameColIndex(index++);
				area.setSexColIndex(index++);
				area.setAgeOfTheMoonColIndex(index++);
				area.setNationColIndex(index++);
				area.setWeightColIndex(index++);
				area.setHeightColIndex(index++);
				area.setHemoglobinColIndex(index++);
				area.setCityColIndex(index++);
				area.setAreaColIndex(index++);
				area.setOrganizationLevelColIndex(index++);
				area.setOrganizationPropertyColIndex(index++);
				area.setOrganizationTerritoryColIndex(index++);
				areas.add(area);
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter writer = null;
				try {
					if (StringUtils.isEmpty(checkId)) {
						checkId = UUID32.generateShortUuid();
						MedicalExaminationDef medicalExaminationDef = new MedicalExaminationDef();
						medicalExaminationDef.setTenantId(loginContext.getTenantId());
						medicalExaminationDef.setTitle(request.getParameter("title"));
						if (StringUtils.isEmpty(medicalExaminationDef.getTitle())) {
							medicalExaminationDef.setTitle("无主题" + DateUtils.getDate(new java.util.Date()));
						}
						medicalExaminationDef.setType(request.getParameter("type"));
						medicalExaminationDef.setCheckDate(RequestUtils.getDate(request, "checkDate"));
						medicalExaminationDef.setRemark(request.getParameter("remark"));
						medicalExaminationDef.setEnableFlag("Y");

						if (medicalExaminationDef.getCheckDate() != null) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(medicalExaminationDef.getCheckDate());
							int year = calendar.get(Calendar.YEAR);
							int month = calendar.get(Calendar.MONTH) + 1;
							medicalExaminationDef.setYear(year);
							medicalExaminationDef.setMonth(month);
							medicalExaminationDef.setDay(calendar.get(Calendar.DAY_OF_MONTH));
						}
						medicalExaminationDef.setCreateBy(loginContext.getActorId());
						this.medicalExaminationDefService.save(medicalExaminationDef);
					}

					String str = null;
					if (StringUtils.endsWithIgnoreCase(mFile.getOriginalFilename(), ".xlsx")) {
						str = bean.importData(loginContext.getTenantId(), loginContext.getActorId(), type, checkId,
								areas, mFile.getInputStream());
					} else {
						str = bean.importData(loginContext.getTenantId(), loginContext.getActorId(), type, checkId,
								areas, mFile.getInputStream());
					}

					if (str != null && str.length() > 0) {
						writer = response.getWriter();
						writer.write(str);
						writer.flush();
					} else {
						writer = response.getWriter();
						writer.write("<br>没有记录导入。");
						writer.flush();
					}
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			}
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param mFile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/doImport2", method = RequestMethod.POST)
	public void doImport2(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		String checkId = request.getParameter("checkId");
		String type = request.getParameter("type");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (mFile != null && !mFile.isEmpty()) {
			if (StringUtils.isNotEmpty(type)) {
				MedicalSpotCheckExcelImporterV2 bean = new MedicalSpotCheckExcelImporterV2();
				List<MedicalSpotCheckXlsArea> areas = new ArrayList<MedicalSpotCheckXlsArea>();
				MedicalSpotCheckXlsArea area = new MedicalSpotCheckXlsArea();
				int index = 0;
				area.setStartRow(1);
				area.setEndRow(5000);
				area.setOrganizationColIndex(index++);
				area.setGradeColIndex(index++);
				area.setNameColIndex(index++);
				area.setSexColIndex(index++);
				area.setBirthdayColIndex(index++);
				area.setCheckDateColIndex(index++);
				area.setNationColIndex(index++);
				area.setWeightColIndex(index++);
				area.setHeightColIndex(index++);
				area.setHemoglobinColIndex(index++);
				area.setCityColIndex(index++);
				area.setAreaColIndex(index++);
				area.setOrganizationLevelColIndex(index++);
				area.setOrganizationPropertyColIndex(index++);
				area.setOrganizationTerritoryColIndex(index++);
				areas.add(area);
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter writer = null;
				try {
					if (StringUtils.isEmpty(checkId)) {
						checkId = UUID32.generateShortUuid();
						MedicalExaminationDef medicalExaminationDef = new MedicalExaminationDef();
						medicalExaminationDef.setTenantId(loginContext.getTenantId());
						medicalExaminationDef.setTitle(request.getParameter("title"));
						if (StringUtils.isEmpty(medicalExaminationDef.getTitle())) {
							medicalExaminationDef.setTitle("无主题" + DateUtils.getDate(new java.util.Date()));
						}
						medicalExaminationDef.setType(request.getParameter("type"));
						medicalExaminationDef.setCheckDate(RequestUtils.getDate(request, "checkDate"));
						medicalExaminationDef.setRemark(request.getParameter("remark"));
						medicalExaminationDef.setEnableFlag("Y");

						if (medicalExaminationDef.getCheckDate() != null) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(medicalExaminationDef.getCheckDate());
							int year = calendar.get(Calendar.YEAR);
							int month = calendar.get(Calendar.MONTH) + 1;
							medicalExaminationDef.setYear(year);
							medicalExaminationDef.setMonth(month);
							medicalExaminationDef.setDay(calendar.get(Calendar.DAY_OF_MONTH));
						}
						medicalExaminationDef.setCreateBy(loginContext.getActorId());
						this.medicalExaminationDefService.save(medicalExaminationDef);
					}

					String str = null;
					if (StringUtils.endsWithIgnoreCase(mFile.getOriginalFilename(), ".xlsx")) {
						str = bean.importData(loginContext.getTenantId(), loginContext.getActorId(), type, checkId,
								areas, mFile.getInputStream());
					} else {
						str = bean.importData(loginContext.getTenantId(), loginContext.getActorId(), type, checkId,
								areas, mFile.getInputStream());
					}

					if (str != null && str.length() > 0) {
						writer = response.getWriter();
						writer.write(str);
						writer.flush();
					} else {
						writer = response.getWriter();
						writer.write("<br>没有记录导入。");
						writer.flush();
					}
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			}
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param mFile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/doImport3", method = RequestMethod.POST)
	public void doImport3(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		String checkId = request.getParameter("checkId");
		String type = request.getParameter("type");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (mFile != null && !mFile.isEmpty()) {
			if (StringUtils.isNotEmpty(type)) {
				MedicalSpotCheckExcelImporterV2 bean = new MedicalSpotCheckExcelImporterV2();
				List<MedicalSpotCheckXlsArea> areas = new ArrayList<MedicalSpotCheckXlsArea>();
				MedicalSpotCheckXlsArea area = new MedicalSpotCheckXlsArea();
				int index = 0;
				area.setStartRow(1);
				area.setEndRow(5000);
				area.setGradeColIndex(index++);
				area.setNameColIndex(index++);
				area.setSexColIndex(index++);
				area.setBirthdayColIndex(index++);
				area.setCheckDateColIndex(index++);
				area.setWeightColIndex(index++);
				area.setHeightColIndex(index++);
				area.setHemoglobinColIndex(index++);
				area.setEyesightLeftColIndex(index++);
				area.setEyesightRightColIndex(index++);
				areas.add(area);
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter writer = null;
				try {
					if (StringUtils.isEmpty(checkId)) {
						checkId = UUID32.generateShortUuid();
						MedicalExaminationDef medicalExaminationDef = new MedicalExaminationDef();
						medicalExaminationDef.setTenantId(loginContext.getTenantId());
						medicalExaminationDef.setTitle(request.getParameter("title"));
						if (StringUtils.isEmpty(medicalExaminationDef.getTitle())) {
							medicalExaminationDef.setTitle("无主题" + DateUtils.getDate(new java.util.Date()));
						}
						medicalExaminationDef.setType(request.getParameter("type"));
						medicalExaminationDef.setCheckDate(RequestUtils.getDate(request, "checkDate"));
						medicalExaminationDef.setRemark(request.getParameter("remark"));
						medicalExaminationDef.setEnableFlag("Y");

						if (medicalExaminationDef.getCheckDate() != null) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(medicalExaminationDef.getCheckDate());
							int year = calendar.get(Calendar.YEAR);
							int month = calendar.get(Calendar.MONTH) + 1;
							medicalExaminationDef.setYear(year);
							medicalExaminationDef.setMonth(month);
							medicalExaminationDef.setDay(calendar.get(Calendar.DAY_OF_MONTH));
						}
						medicalExaminationDef.setCreateBy(loginContext.getActorId());
						this.medicalExaminationDefService.save(medicalExaminationDef);
					}

					String str = null;
					if (StringUtils.endsWithIgnoreCase(mFile.getOriginalFilename(), ".xlsx")) {
						str = bean.importData(loginContext.getTenantId(), loginContext.getActorId(), type, checkId,
								areas, mFile.getInputStream());
					} else {
						str = bean.importData(loginContext.getTenantId(), loginContext.getActorId(), type, checkId,
								areas, mFile.getInputStream());
					}

					if (str != null && str.length() > 0) {
						writer = response.getWriter();
						writer.write(str);
						writer.flush();
					} else {
						writer = response.getWriter();
						writer.write("<br>没有记录导入。");
						writer.flush();
					}
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			}
		}
	}

	@RequestMapping("/execute")
	@ResponseBody
	public byte[] execute(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		MedicalSpotCheckEvaluateBean bean = new MedicalSpotCheckEvaluateBean();
		String checkId = request.getParameter("checkId");
		String useMethod = request.getParameter("useMethod");
		try {
			bean.execute(loginContext.getTenantId(), loginContext.getActorId(), checkId, useMethod);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		MedicalSpotCheckQuery query = new MedicalSpotCheckQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.actorId(loginContext.getActorId());
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		if (loginContext.getTenantId() != null) {
			query.tenantId(loginContext.getTenantId());
		}

		String nationLike = request.getParameter("nationLike");
		if (StringUtils.isNotEmpty(nationLike)) {
			if (StringUtils.equals("少数民族", nationLike)) {
				query.setNationNotLike("%汉族%");
				query.setNationLike(null);
			} else {
				query.nationLike(nationLike);
			}
		}

		int heightLevel = RequestUtils.getInt(request, "heightLevel", -9);
		if (heightLevel >= 0) {
			query.setHeightLevelGreaterThanOrEqual(heightLevel);
		} else {
			if (heightLevel != -9) {
				query.setHeightLevelLessThanOrEqual(heightLevel);
			}
		}

		int weightLevel = RequestUtils.getInt(request, "weightLevel", -9);
		if (weightLevel != -9) {
			query.setWeightLevel(weightLevel);
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
		try {
			int total = medicalSpotCheckService.getMedicalSpotCheckCountByQueryCriteria(query);
			if (total > 0) {
				result.put("code", 0);
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

				List<MedicalSpotCheck> list = medicalSpotCheckService.getMedicalSpotChecksByQueryCriteria(start, limit,
						query);
				if (list != null && !list.isEmpty()) {
					List<GrowthStandard> standards = growthStandardService.getAllGrowthStandards();
					Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
					if (standards != null && !standards.isEmpty()) {
						for (GrowthStandard gs : standards) {
							if (StringUtils.equals(gs.getType(), "4")) {
								// int height = (int) Math.round(gs.getHeight());
								String key = gs.getHeight() + "_" + gs.getSex() + "_" + gs.getType();
								// logger.debug(key);
								gsMap.put(key, gs);
							} else {
								gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
							}
						}
					}

					String useMethod = request.getParameter("useMethod");
					MedicalExaminationEvaluateHelper helper = new MedicalExaminationEvaluateHelper();
					JSONArray rowsJSON = new JSONArray();
					for (MedicalSpotCheck exam : list) {
						if (exam.getAgeOfTheMoon() > 0 && exam.getHeight() > 0 && exam.getWeight() > 0) {
							if (StringUtils.equals(useMethod, "prctile")) {
								helper.evaluateByPrctile(gsMap, exam);
							} else {
								helper.evaluate(gsMap, exam);
							}
						}
						JSONObject rowJSON = exam.toJsonObject();
						rowJSON.put("id", exam.getId());
						rowJSON.put("startIndex", ++start);
						rowsJSON.add(rowJSON);
					}
					result.put("rows", rowsJSON);
				}
			} else {
				JSONArray rowsJSON = new JSONArray();
				result.put("rows", rowsJSON);
				result.put("total", total);
				result.put("code", 0);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		MedicalExaminationDefQuery query = new MedicalExaminationDefQuery();
		if (loginContext.isSystemAdministrator()) {
			query.tenantId("sys");
		} else {
			query.tenantId(loginContext.getTenantId());
		}
		List<MedicalExaminationDef> list = medicalExaminationDefService.list(query);
		request.setAttribute("examDefs", list);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalSpotCheck/list", modelMap);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.growthStandardService")
	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalExaminationDefService")
	public void setMedicalExaminationDefService(MedicalExaminationDefService medicalExaminationDefService) {
		this.medicalExaminationDefService = medicalExaminationDefService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalSpotCheckService")
	public void setMedicalSpotCheckService(MedicalSpotCheckService medicalSpotCheckService) {
		this.medicalSpotCheckService = medicalSpotCheckService;
	}

	@RequestMapping("/showImport")
	public ModelAndView showImport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		MedicalExaminationDefQuery query = new MedicalExaminationDefQuery();
		if (loginContext.isSystemAdministrator()) {
			query.tenantId("sys");
		} else {
			query.tenantId(loginContext.getTenantId());
		}
		List<MedicalExaminationDef> list = medicalExaminationDefService.list(query);
		request.setAttribute("examDefs", list);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalSpotCheck/showImport", modelMap);
	}

}
