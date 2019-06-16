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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.el.ExpressionTools;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExcelToHtml;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;
import com.glaf.core.util.ZipUtils;
import com.glaf.heathcare.bean.MedicalExaminationEvaluateCountBean;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.report.IReportPreprocessor;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.jxls.ext.JxlsBuilder;
import com.glaf.matrix.export.handler.CellMergeHandler;
import com.glaf.matrix.export.handler.RemoveCommentHandler;
import com.glaf.matrix.export.handler.RowHeightAdjustHandler;
import com.glaf.matrix.export.handler.TextToDecimalHandler;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

@Controller("/heathcare/reportMain")
@RequestMapping("/heathcare/reportMain")
public class ReportMainController {

	protected static final Log logger = LogFactory.getLog(ReportMainController.class);

	protected static final Semaphore semaphore2 = new Semaphore(20);

	protected GradeInfoService gradeInfoService;

	protected SysTenantService sysTenantService;

	protected TenantConfigService tenantConfigService;

	@RequestMapping("/confirm")
	public ModelAndView confirm(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		return new ModelAndView("/heathcare/statistics/confirm", modelMap);
	}

	@ResponseBody
	@RequestMapping("/exportXls")
	public void exportXls(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");

		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isEmpty(tenantId)) {
			tenantId = loginContext.getTenantId();
		}

		params.put("year", year);
		params.put("month", month);

		params.put("tenantId", tenantId);
		params.put("tableSuffix", IdentityFactory.getTenantHash(tenantId));

		if (loginContext.getUser() != null) {
			params.put("username", loginContext.getUser().getName());
		}

		byte[] data = null;
		String reportId = request.getParameter("reportId");
		ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition(reportId);
		logger.debug("reportId:" + reportId);
		if (rdf != null) {
			if (StringUtils.isNotEmpty(rdf.getPerms())) {
				logger.debug("检查权限点:" + rdf.getPerms());
				boolean hasPermission = false;
				if (!loginContext.isSystemAdministrator()) {
					List<String> perms = StringTools.split(rdf.getPerms(), "|");
					Collection<String> roles = loginContext.getRoles();
					if (roles != null && !roles.isEmpty()) {
						for (String role : roles) {
							if (perms.contains(role)) {
								hasPermission = true;
							}
						}
					}
					Collection<String> permissions = loginContext.getPermissions();
					if (permissions != null && !permissions.isEmpty()) {
						for (String permission : permissions) {
							if (perms.contains(permission)) {
								hasPermission = true;
							}
						}
					}
					if (!hasPermission) {
						logger.warn(loginContext.getActorId() + "没有权限,不能操作!");
						try {
							response.sendRedirect(request.getContextPath() + "/static/html/unauthorized.html");
						} catch (IOException e) {
						}
						return;
					}
				}
			}
			data = rdf.getData();
			String useExt = request.getParameter("useExt");
			String megerFlag = request.getParameter("megerFlag");
			String outputFormat = request.getParameter("outputFormat");
			int precision = RequestUtils.getInt(request, "precision", 2);
			IReportPreprocessor reportPreprocessor = null;
			Workbook workbook = null;
			InputStream is = null;
			BufferedInputStream bis = null;
			PrintWriter writer = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			try {

				TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(tenantId);
				if (tenantConfig != null) {
					params.put("tenantConfig", tenantConfig);
					params.put("sysName", tenantConfig.getSysName());
				}

				SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
				if (tenant != null) {
					params.put("orgName", tenant.getName());
					params.put("tenant", tenant);
					
					MedicalExaminationEvaluateCountBean bean = new MedicalExaminationEvaluateCountBean();
					bean.execute(tenantId);
				}

				String gradeId = request.getParameter("gradeId");
				if (StringUtils.isNotEmpty(gradeId)) {
					GradeInfo gradeInfo = gradeInfoService.getGradeInfo(gradeId);
					if (gradeInfo != null) {
						params.put("gradeInfo", gradeInfo);
						params.put("gradeName", gradeInfo.getName());
					}
				}

				if (StringUtils.isNotEmpty(rdf.getPrepareClass())) {
					reportPreprocessor = (IReportPreprocessor) com.glaf.core.util.ReflectUtils
							.instantiate(rdf.getPrepareClass());
					if (loginContext.isSystemAdministrator()) {
						tenant.setSystemAdministrator(loginContext.isSystemAdministrator());
					} else {
						tenant.setSystemAdministrator(false);
					}
					reportPreprocessor.prepare(tenant, params);
				}

				bais = new ByteArrayInputStream(data);
				is = new BufferedInputStream(bais);
				baos = new ByteArrayOutputStream();
				bos = new BufferedOutputStream(baos);

				Context context2 = PoiTransformer.createInitialContext();

				Set<Entry<String, Object>> entrySet = params.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String key = entry.getKey();
					Object value = entry.getValue();
					context2.putVar(key, value);
				}

				ZipSecureFile.setMinInflateRatio(-1.0d);// 延迟解析比率

				if (StringUtils.equals(useExt, "Y")) {
					JxlsBuilder jxlsBuilder = JxlsBuilder.getBuilder(is).out(bos).putAll(params);
					jxlsBuilder.putVar("_ignoreImageMiss", Boolean.valueOf(true));
					jxlsBuilder.build();
				} else {
					org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);
				}
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(bais);

				bos.flush();
				baos.flush();
				data = baos.toByteArray();

				if (StringUtils.equals(megerFlag, "Y")) {
					bais = new ByteArrayInputStream(data);
					bis = new BufferedInputStream(bais);

					workbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(bis);

					CellMergeHandler cellMergeHandler = new CellMergeHandler();
					cellMergeHandler.mergeVertical(workbook);

					RowHeightAdjustHandler rowHeightAdjustHandler = new RowHeightAdjustHandler();
					rowHeightAdjustHandler.processWorkbook(workbook);

					TextToDecimalHandler textToDecimalHandler = new TextToDecimalHandler();
					textToDecimalHandler.processWorkbook(workbook);

					RemoveCommentHandler removeCommentHandler = new RemoveCommentHandler();
					removeCommentHandler.removeComment(workbook);

					FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
					evaluator.evaluateAll();
					
					baos = new ByteArrayOutputStream();
					bos = new BufferedOutputStream(baos);
					workbook.write(bos);
					bos.flush();
					baos.flush();
					data = baos.toByteArray();
				}

				if (StringUtils.equals(outputFormat, "html")) {
					request.setCharacterEncoding("UTF-8");
					response.setCharacterEncoding("UTF-8");
					response.setContentType("text/html; charset=UTF-8");
					bais = new ByteArrayInputStream(data);
					is = new BufferedInputStream(bais);
					if (precision == 0) {
						precision = 2;
					}
					if (precision == -1) {
						precision = 0;
					}
					String content = new String(ExcelToHtml.toHtml(is, precision), "UTF-8");
					writer = response.getWriter();
					writer.write(content);
					writer.flush();
				} else {
					String filename = "export" + DateUtils.getNowYearMonthDayHHmmss() + ".xls";
					if (rdf.getTemplateFile().endsWith(".xlsx")) {
						filename = "export" + DateUtils.getNowYearMonthDayHHmmss() + ".xlsx";
					}
					if (StringUtils.isNotEmpty(rdf.getExportFilename())) {
						filename = rdf.getExportFilename();
						params.put("yyyyMMdd", DateUtils.getDateTime("yyyyMMdd", new Date()));
						params.put("yyyyMMddHHmm", DateUtils.getDateTime("yyyyMMddHHmm", new Date()));
						params.put("yyyyMMddHHmmss", DateUtils.getDateTime("yyyyMMddHHmmss", new Date()));

						filename = ExpressionTools.evaluate(filename, params);
					}
					ResponseUtils.download(request, response, data, filename);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			} finally {
				data = null;
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(bis);
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

	@ResponseBody
	@RequestMapping("/exportZip")
	public byte[] exportZip(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("启用并发访问控制,可用许可数:" + semaphore2.availablePermits());
		if (semaphore2.availablePermits() == 0) {
			return ResponseUtils.responseJsonResult(false, "导出任务繁忙，请稍候再试。");
		}
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");
		Date startDate = ParamUtils.getDate(params, "startDate");
		Date endDate = ParamUtils.getDate(params, "endDate");

		if (startDate == null) {
			startDate = ParamUtils.getDate(params, "startTime");
		}

		if (endDate == null) {
			endDate = ParamUtils.getDate(params, "endTime");
		}

		String tenantId = loginContext.getTenantId();

		params.put("year", year);
		params.put("month", month);
		params.put("startdate", DateUtils.getDate(startDate));
		params.put("startDate", DateUtils.getDate(startDate));
		params.put("starttime", DateUtils.getDateTime(startDate));
		params.put("startTime", DateUtils.getDateTime(startDate));
		params.put("enddate", DateUtils.getDate(endDate));
		params.put("endDate", DateUtils.getDate(endDate));
		params.put("endtime", DateUtils.getDateTime(endDate));
		params.put("endTime", DateUtils.getDateTime(endDate));

		params.put("tenantId", tenantId);
		params.put("tableSuffix", IdentityFactory.getTenantHash(tenantId));

		logger.debug(params);

		String reportIds = request.getParameter("reportIds");
		if (StringUtils.isNotEmpty(reportIds)) {
			List<String> rptIds = StringTools.split(reportIds);

			byte[] data = null;
			byte[] bytes = null;
			InputStream is = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			ReportDefinition rdf = null;
			IReportPreprocessor reportPreprocessor = null;
			Map<String, byte[]> bytesMap = new HashMap<String, byte[]>();
			try {
				semaphore2.acquire();

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

				for (String reportId : rptIds) {
					rdf = ReportContainer.getContainer().getReportDefinition(reportId);
					if (rdf != null) {

						if (StringUtils.isNotEmpty(rdf.getPrepareClass())) {
							reportPreprocessor = (IReportPreprocessor) com.glaf.core.util.ReflectUtils
									.instantiate(rdf.getPrepareClass());
							reportPreprocessor.prepare(tenant, params);
						}

						data = rdf.getData();
						bais = new ByteArrayInputStream(data);
						is = new BufferedInputStream(bais);
						baos = new ByteArrayOutputStream();
						bos = new BufferedOutputStream(baos);

						Context context2 = PoiTransformer.createInitialContext();

						Set<Entry<String, Object>> entrySet = params.entrySet();
						for (Entry<String, Object> entry : entrySet) {
							String key = entry.getKey();
							Object value = entry.getValue();
							context2.putVar(key, value);
						}

						org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);
						IOUtils.closeQuietly(is);
						IOUtils.closeQuietly(bais);

						bos.flush();
						baos.flush();
						bytes = baos.toByteArray();

						bytesMap.put(rdf.getTitle() + ".xls", bytes);

						IOUtils.closeQuietly(is);
						IOUtils.closeQuietly(bais);
						IOUtils.closeQuietly(baos);
						IOUtils.closeQuietly(bos);
					}
				}

				data = ZipUtils.genZipBytes(bytesMap);
				ResponseUtils.download(request, response, data,
						"export" + DateUtils.getNowYearMonthDayHHmmss() + ".zip");

			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			} finally {
				semaphore2.release();
				data = null;
				bytes = null;
				bytesMap = null;
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(bais);
				IOUtils.closeQuietly(baos);
				IOUtils.closeQuietly(bos);
			}
		}
		return null;
	}

	@RequestMapping
	public ModelAndView main(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		request.setAttribute("year", year);
		request.setAttribute("month", calendar.get(Calendar.MONTH));

		List<Integer> years = new ArrayList<Integer>();
		if (year >= 2019) {
			years.add(year - 1);
		}

		years.add(year);

		List<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		GradeInfoQuery query = new GradeInfoQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.locked(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			logger.debug("perms:" + loginContext.getPermissions());
			query.tenantId(loginContext.getTenantId());
			if (!(loginContext.hasPermission("HealthPhysician", "or")
					|| loginContext.hasPermission("TenantAdmin", "or"))) {
				logger.debug("gradeIds:" + loginContext.getGradeIds());
				query.gradeIds(loginContext.getGradeIds());
			}
		}

		List<GradeInfo> list = gradeInfoService.list(query);
		request.setAttribute("grades", list);

		return new ModelAndView("/heathcare/reportMain/main", modelMap);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
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
