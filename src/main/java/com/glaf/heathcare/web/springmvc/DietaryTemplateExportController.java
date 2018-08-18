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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;

import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

import com.glaf.heathcare.bean.DayDietaryTemplateExportBean;
import com.glaf.heathcare.bean.DietaryStatisticsBean;
import com.glaf.heathcare.bean.DietaryTemplateCountBean;
import com.glaf.heathcare.bean.DietaryTemplateCountExportBean;
import com.glaf.heathcare.bean.DietaryTemplateExportBean;
import com.glaf.heathcare.domain.DietaryCategory;
import com.glaf.heathcare.query.DietaryCategoryQuery;
import com.glaf.heathcare.service.DietaryCategoryService;
import com.glaf.heathcare.service.DietaryCountService;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

@Controller("/heathcare/dietaryTemplateExport")
@RequestMapping("/heathcare/dietaryTemplateExport")
public class DietaryTemplateExportController {
	protected static final Log logger = LogFactory.getLog(DietaryTemplateExportController.class);

	protected DictoryService dictoryService;

	protected DietaryCategoryService dietaryCategoryService;

	protected DietaryCountService dietaryCountService;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateService dietaryTemplateService;

	protected FoodCompositionService foodCompositionService;

	protected SysTreeService sysTreeService;

	protected TenantConfigService tenantConfigService;

	public DietaryTemplateExportController() {

	}

	@RequestMapping("/copyTemplates")
	public ModelAndView copyTemplates(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);

		int suitNo = RequestUtils.getInt(request, "suitNo");
		String sysFlag = request.getParameter("sysFlag");

		boolean result = false;
		if (suitNo > 0) {
			try {
				int targetSuitNo = dietaryCategoryService.getMaxSuitNo(loginContext);
				dietaryTemplateService.copyTemplates(loginContext, suitNo, sysFlag, targetSuitNo);

				DietaryTemplateExportBean exportBean = new DietaryTemplateExportBean();
				Map<String, Object> dataMap = exportBean.prepareData(loginContext, targetSuitNo, sysFlag, params);
				Set<Entry<String, Object>> entrySet = dataMap.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String key = entry.getKey();
					Object value = entry.getValue();
					request.setAttribute(key, value);
					// logger.debug("key=" + key);
				}
				result = true;
				request.setAttribute("suitNo", targetSuitNo);
			} catch (Exception ex) {
				logger.error(ex);
				result = false;
			}
			if (result) {
				sysFlag = "N";
			}
		}

		if (loginContext.isSystemAdministrator()) {
			List<DietaryCategory> categories = dietaryCategoryService.getSysDietaryCategories();
			request.setAttribute("categories", categories);
		} else {
			if (StringUtils.equals(sysFlag, "Y")) {
				List<DietaryCategory> categories = dietaryCategoryService.getSysDietaryCategories();
				request.setAttribute("categories", categories);
			} else {
				List<DietaryCategory> categories = dietaryCategoryService.getDietaryCategories(loginContext, false);
				request.setAttribute("categories", categories);
			}
		}

		if (result) {
			request.setAttribute("sysFlag", "N");
			request.setAttribute("copy_msg", "模板已经复制成功。");
		} else {
			request.setAttribute("copy_msg", "复制失败，请稍候再试。");
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryTemplateExport.showExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryTemplate/showExport", modelMap);
	}

	@ResponseBody
	@RequestMapping("/export")
	public void export(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
				|| loginContext.getRoles().contains("TenantAdmin")) {
			Map<String, Object> context = RequestUtils.getParameterMap(request);
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			logger.debug("params:" + params);
			int suitNo = RequestUtils.getInt(request, "suitNo");
			String sysFlag = request.getParameter("sysFlag");
			if (suitNo > 0) {
				DietaryTemplateExportBean exportBean = new DietaryTemplateExportBean();
				Map<String, Object> dataMap = exportBean.prepareData(loginContext, suitNo, sysFlag, params);
				Set<Entry<String, Object>> entrySet = dataMap.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String key = entry.getKey();
					Object value = entry.getValue();
					context.put(key, value);
				}
			}

			String exportType = request.getParameter("exportType");
			if (StringUtils.equals(exportType, "xls")) {
				byte[] data = null;
				ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition("rpt_dietary_template");
				data = rdf.getData();
				if (data != null) {
					Workbook workbook = null;
					InputStream is = null;
					ByteArrayInputStream bais = null;
					ByteArrayOutputStream baos = null;
					BufferedOutputStream bos = null;
					try {
						bais = new ByteArrayInputStream(data);
						is = new BufferedInputStream(bais);
						baos = new ByteArrayOutputStream();
						bos = new BufferedOutputStream(baos);

						Context context2 = PoiTransformer.createInitialContext();

						Set<Entry<String, Object>> entrySet = context.entrySet();
						for (Entry<String, Object> entry : entrySet) {
							String key = entry.getKey();
							Object value = entry.getValue();
							context2.putVar(key, value);
							// logger.debug(key);
						}
						org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);

						bos.flush();
						baos.flush();
						data = baos.toByteArray();

						ResponseUtils.download(request, response, data,
								"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xls");
					} catch (Exception ex) {
						// ex.printStackTrace();
						logger.error(ex);
					} finally {
						data = null;
						IOUtils.closeQuietly(is);
						IOUtils.closeQuietly(bais);
						IOUtils.closeQuietly(baos);
						IOUtils.closeQuietly(bos);
						if (workbook != null) {
							try {
								workbook.close();
							} catch (IOException e) {
							}
							workbook = null;
						}
					}
				}
			}
		}
	}

	@ResponseBody
	@RequestMapping("/genAllSysJS")
	public byte[] genAllSysJS(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			try {
				DietaryCategoryQuery query = new DietaryCategoryQuery();
				query.locked(0);
				query.sysFlag("Y");
				List<DietaryCategory> categories = dietaryCategoryService.list(query);
				if (categories != null && !categories.isEmpty()) {
					for (DietaryCategory category : categories)
						if (category != null) {
							JSONArray array = dietaryTemplateService.getSuiteData(loginContext, "Y",
									category.getSuitNo(), category.getTypeId());
							if (array != null && array.size() > 0) {
								String filename = SystemProperties.getAppPath() + "/static/generate/js/dietaryTemplate"
										+ category.getSuitNo() + ".js";
								StringBuilder buff = new StringBuilder();
								buff.append(" var dietaryTemplates = ").append(array.toJSONString()).append("; ");
								FileUtils.save(filename, buff.toString().getBytes("UTF-8"));
							}
						}
				}
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/genAllSysJSON")
	public byte[] genAllSysJSON(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			try {
				DietaryCategoryQuery query = new DietaryCategoryQuery();
				query.locked(0);
				query.sysFlag("Y");
				List<DietaryCategory> categories = dietaryCategoryService.list(query);
				if (categories != null && !categories.isEmpty()) {
					for (DietaryCategory category : categories)
						if (category != null) {
							JSONArray array = dietaryTemplateService.getSuiteData(loginContext, "Y",
									category.getSuitNo(), category.getTypeId());
							if (array != null && array.size() > 0) {
								String filename = SystemProperties.getAppPath()
										+ "/static/generate/json/dietaryTemplate" + category.getSuitNo() + ".json";
								FileUtils.save(filename, array.toJSONString().getBytes("UTF-8"));
							}
						}
				}
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/genJS")
	public byte[] genJS(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		int suitNo = RequestUtils.getInt(request, "suitNo");
		if (suitNo > 0 && suitNo <= 1000 && loginContext.isSystemAdministrator()) {
			try {
				DietaryCategory category = dietaryCategoryService.getDietaryCategory(loginContext, suitNo);
				if (category != null) {
					JSONArray array = dietaryTemplateService.getSuiteData(loginContext, "Y", suitNo,
							category.getTypeId());
					if (array != null && array.size() > 0) {
						String filename = SystemProperties.getAppPath() + "/static/generate/js/dietaryTemplate" + suitNo
								+ ".js";
						StringBuilder buff = new StringBuilder();
						buff.append(" var dietaryTemplates = ").append(array.toJSONString()).append("; ");
						FileUtils.save(filename, buff.toString().getBytes("UTF-8"));
						return ResponseUtils.responseJsonResult(true);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/genJSON")
	public byte[] genJSON(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		int suitNo = RequestUtils.getInt(request, "suitNo");
		if (suitNo > 0 && suitNo <= 1000 && loginContext.isSystemAdministrator()) {
			try {
				DietaryCategory category = dietaryCategoryService.getDietaryCategory(loginContext, suitNo);
				if (category != null) {
					JSONArray array = dietaryTemplateService.getSuiteData(loginContext, "Y", suitNo,
							category.getTypeId());
					if (array != null && array.size() > 0) {
						String filename = SystemProperties.getAppPath() + "/static/generate/json/dietaryTemplate"
								+ suitNo + ".json";
						FileUtils.save(filename, array.toJSONString().getBytes("UTF-8"));
						return ResponseUtils.responseJsonResult(true);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryCategoryService")
	public void setDietaryCategoryService(DietaryCategoryService dietaryCategoryService) {
		this.dietaryCategoryService = dietaryCategoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryCountService")
	public void setDietaryCountService(DietaryCountService dietaryCountService) {
		this.dietaryCountService = dietaryCountService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryItemService")
	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryTemplateService")
	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

	@RequestMapping("/showCopy")
	public ModelAndView showCopy(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);

		if (loginContext.isSystemAdministrator()) {

		} else {

		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryTemplateExport.showCopy");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryTemplate/showCopy", modelMap);
	}

	@RequestMapping("/showDayExport")
	public ModelAndView showDayExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);

		List<Dictory> dictoryList = dictoryService.getDictoryListByCategory("CAT_MEAL");
		request.setAttribute("dictoryList", dictoryList);

		if (!loginContext.isSystemAdministrator()) {
			TenantConfig cfg = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			request.setAttribute("tenantConfig", cfg);
			for (Dictory dict : dictoryList) {
				if (cfg.getTypeId() == dict.getId()) {
					request.setAttribute("typeDict", dict);
					request.setAttribute("typeId", dict.getId());
				}
			}
		}

		int dayOfWeek = RequestUtils.getInt(request, "dayOfWeek");
		int suitNo = RequestUtils.getInt(request, "suitNo");
		long typeId = RequestUtils.getLong(request, "typeId");
		String sysFlag = request.getParameter("sysFlag");
		if (suitNo > 0 && dayOfWeek > 0) {
			DayDietaryTemplateExportBean exportBean = new DayDietaryTemplateExportBean();
			if (typeId == 0) {
				DietaryCategory category = dietaryCategoryService.getDietaryCategory(loginContext, suitNo);
				if (category != null) {
					typeId = category.getTypeId();
				}
			}
			Map<String, Object> dataMap = exportBean.prepareData(loginContext, suitNo, dayOfWeek, typeId, sysFlag,
					params);
			Set<Entry<String, Object>> entrySet = dataMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				request.setAttribute(key, value);
				// logger.debug("key=" + key);
			}
		}

		request.removeAttribute("dietary_execute_perm");
		request.removeAttribute("dietary_copy_add_perm");

		if (StringUtils.equals(sysFlag, "Y")) {
			if (loginContext.isSystemAdministrator()) {
				request.setAttribute("tenantCorrelation", "false");
				request.setAttribute("dietary_execute_perm", true);
				request.setAttribute("dietary_copy_add_perm", true);
			}
		} else {
			if (!loginContext.isSystemAdministrator()) {
				request.setAttribute("tenantCorrelation", "true");
			}
			if (loginContext.getRoles().contains("TenantAdmin")
					|| loginContext.getRoles().contains("HealthPhysician")) {
				request.setAttribute("dietary_execute_perm", true);
				request.setAttribute("dietary_copy_add_perm", true);
			}
		}

		if (loginContext.isSystemAdministrator()) {
			List<DietaryCategory> categories = dietaryCategoryService.getSysDietaryCategories();
			request.setAttribute("categories", categories);
		} else {
			if (StringUtils.equals(sysFlag, "Y")) {
				List<DietaryCategory> categories = dietaryCategoryService.getSysDietaryCategories();
				request.setAttribute("categories", categories);
			} else {
				List<DietaryCategory> categories = dietaryCategoryService.getDietaryCategories(loginContext, false);
				request.setAttribute("categories", categories);
			}
		}

		request.setAttribute("canChangeDishes", false);

		if (loginContext.isSystemAdministrator() && StringUtils.equals(sysFlag, "Y")) {
			request.setAttribute("canChangeDishes", true);
		} else if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
				&& StringUtils.equals(sysFlag, "N")) {
			request.setAttribute("canChangeDishes", true);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryTemplateExport.showDayExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryTemplate/showDayExport", modelMap);
	}

	@RequestMapping("/showExport")
	public ModelAndView showExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		int suitNo = RequestUtils.getInt(request, "suitNo");
		String sysFlag = request.getParameter("sysFlag");
		long typeId = 0;
		if (suitNo > 0) {
			DietaryTemplateExportBean exportBean = new DietaryTemplateExportBean();
			Map<String, Object> dataMap = exportBean.prepareData(loginContext, suitNo, sysFlag, params);
			Set<Entry<String, Object>> entrySet = dataMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				request.setAttribute(key, value);
				// logger.debug("key=" + key);
			}
			DietaryCategory dietaryCategory = null;
			if (StringUtils.equals(sysFlag, "Y")) {
				dietaryCategory = dietaryCategoryService.getSysDietaryCategory(suitNo);
			} else {
				dietaryCategory = dietaryCategoryService.getDietaryCategory(loginContext, suitNo);
			}
			if (dietaryCategory != null) {
				typeId = dietaryCategory.getTypeId();
				sysFlag = dietaryCategory.getSysFlag();
				request.setAttribute("dietaryCategory", dietaryCategory);
				request.setAttribute("dietary_template_subject", dietaryCategory.getName());
			}
		}

		if (StringUtils.equals(sysFlag, "Y") && loginContext.isSystemAdministrator()) {
			DietaryTemplateCountBean countBean = new DietaryTemplateCountBean();
			try {
				countBean.executeCountAll(loginContext, sysFlag, suitNo);
				countBean.executeCountItems(loginContext, sysFlag, suitNo);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

		/**
		 * 角色HealthPhysician和TenantAdmin可以执行汇总
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("TenantAdmin")) {
			if (StringUtils.equals(sysFlag, "N")) {
				DietaryTemplateCountBean countBean = new DietaryTemplateCountBean();
				try {
					countBean.executeCountAll(loginContext, sysFlag, suitNo);
					countBean.executeCountItems(loginContext, sysFlag, suitNo);
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}

		request.removeAttribute("dietary_nutrient");

		DietaryTemplateCountExportBean exportBean = new DietaryTemplateCountExportBean();
		try {
			boolean dietary_nutrient = false;
			Map<String, Object> dataMap = exportBean.prepareData(loginContext, sysFlag, suitNo, typeId);
			Set<Entry<String, Object>> entrySet = dataMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				request.setAttribute("cnt_" + key, value);
				// logger.debug("key:" + key);
			}
			if (!dataMap.isEmpty()) {
				dietary_nutrient = true;
			}
			request.setAttribute("dietary_nutrient", dietary_nutrient);
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}

		if (loginContext.isSystemAdministrator()) {
			List<DietaryCategory> categories = dietaryCategoryService.getSysDietaryCategories();
			request.setAttribute("categories", categories);
		} else {
			if (StringUtils.equals(sysFlag, "Y")) {
				List<DietaryCategory> categories = dietaryCategoryService.getSysDietaryCategories();
				request.setAttribute("categories", categories);
			} else {
				List<DietaryCategory> categories = dietaryCategoryService.getDietaryCategories(loginContext, false);
				request.setAttribute("categories", categories);
			}
		}

		request.removeAttribute("dietary_execute_perm");
		request.removeAttribute("dietary_copy_add_perm");

		if (StringUtils.equals(sysFlag, "Y")) {
			if (loginContext.isSystemAdministrator()) {
				request.setAttribute("tenantCorrelation", "false");
				request.setAttribute("dietary_execute_perm", true);
				request.setAttribute("dietary_copy_add_perm", true);
			}
			if (loginContext.getRoles().contains("TenantAdmin")
					|| loginContext.getRoles().contains("HealthPhysician")) {
				request.setAttribute("dietary_copy_add_perm", true);
			}
		} else {
			if (!loginContext.isSystemAdministrator()) {
				request.setAttribute("tenantCorrelation", "true");
			}
			if (loginContext.getRoles().contains("TenantAdmin")
					|| loginContext.getRoles().contains("HealthPhysician")) {
				request.setAttribute("dietary_execute_perm", true);
				request.setAttribute("dietary_copy_add_perm", true);
				try {
					DietaryStatisticsBean bean = new DietaryStatisticsBean();
					bean.execute(loginContext, suitNo, sysFlag);
				} catch (Exception ex) {
				}
			}
		}

		request.setAttribute("canChangeDishes", false);

		if (loginContext.isSystemAdministrator() && StringUtils.equals(sysFlag, "Y")) {
			request.setAttribute("canChangeDishes", true);
		} else if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
				&& StringUtils.equals(sysFlag, "N")) {
			request.setAttribute("canChangeDishes", true);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryTemplateExport.showExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryTemplate/showExport", modelMap);
	}
}
