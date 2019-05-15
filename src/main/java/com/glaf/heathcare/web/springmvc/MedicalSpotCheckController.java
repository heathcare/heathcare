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
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalSpotCheck;
import com.glaf.heathcare.domain.MedicalSpotCheckXlsArea;
import com.glaf.heathcare.helper.MedicalExaminationEvaluateHelper;
import com.glaf.heathcare.query.MedicalSpotCheckQuery;
import com.glaf.heathcare.service.GrowthStandardService;
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
import java.util.ArrayList;
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

	public MedicalSpotCheckController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			List<String> xids = StringTools.split(ids);
			try {
				medicalSpotCheckService.deleteByIds(xids);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * @param request
	 * @param modelMap
	 * @param mFile
	 * @return
	 */
	@RequestMapping(value = "/doImport", method = RequestMethod.POST)
	public ModelAndView doImport(HttpServletRequest request, ModelMap modelMap,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		String type = request.getParameter("type");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (mFile != null && !mFile.isEmpty()) {
			if (StringUtils.isNotEmpty(type)) {
				MedicalSpotCheckExcelImporter bean = new MedicalSpotCheckExcelImporter();
				List<MedicalSpotCheckXlsArea> areas = new ArrayList<MedicalSpotCheckXlsArea>();
				MedicalSpotCheckXlsArea area = new MedicalSpotCheckXlsArea();
				area.setStartRow(1);
				area.setEndRow(50000);
				area.setAgeOfTheMoonColIndex(1);
				area.setNationColIndex(2);
				area.setHemoglobinColIndex(3);
				area.setWeightColIndex(4);
				area.setHeightColIndex(5);
				area.setCityColIndex(6);
				area.setAreaColIndex(7);
				area.setOrganizationColIndex(8);
				area.setSexColIndex(9);
				areas.add(area);
				String checkId = null;
				try {
					// semaphore2.acquire();
					if (StringUtils.endsWithIgnoreCase(mFile.getOriginalFilename(), ".xlsx")) {
						checkId = bean.importData(loginContext.getActorId(), type, areas, mFile.getInputStream());
					} else {
						checkId = bean.importData(loginContext.getActorId(), type, areas, mFile.getInputStream());
					}
					request.setAttribute("type", type);
					request.setAttribute("checkId", checkId);
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					// semaphore2.release();
				}
			}
		}

		return this.list(request, modelMap);
	}

	@RequestMapping("/execute")
	@ResponseBody
	public byte[] execute(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		MedicalSpotCheckEvaluateBean bean = new MedicalSpotCheckEvaluateBean();
		String useMethod = request.getParameter("useMethod");
		try {
			bean.execute(loginContext.getActorId(), useMethod);
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

		int heightLevel = RequestUtils.getInt(request, "heightLevel");
		if (heightLevel > 0) {
			query.setHeightLevelGreaterThanOrEqual(0);
		} else {
			query.setHeightLevelLessThanOrEqual(heightLevel);
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
							int height = (int) Math.round(gs.getHeight());
							gsMap.put(height + "_" + gs.getSex() + "_" + gs.getType(), gs);
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
						if (StringUtils.contains(exam.getSex(), "男")) {
							exam.setSex("1");
						} else {
							exam.setSex("0");
						}
						if (StringUtils.equals(useMethod, "prctile")) {
							helper.evaluateByPrctile(gsMap, exam);
						} else {
							helper.evaluate(gsMap, exam);
						}
					}
					JSONObject rowJSON = exam.toJsonObject();
					if (StringUtils.contains(exam.getSex(), "1")) {
						rowJSON.put("sex", "男");
					} else {
						rowJSON.put("sex", "女");
					}
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
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalSpotCheckService")
	public void setMedicalSpotCheckService(MedicalSpotCheckService medicalSpotCheckService) {
		this.medicalSpotCheckService = medicalSpotCheckService;
	}

	@RequestMapping("/showImport")
	public ModelAndView showImport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {

		}
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalSpotCheck/showImport", modelMap);
	}

}
