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
import java.io.InputStream;
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
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.ZipUtils;
import com.glaf.heathcare.report.IReportPreprocessor;
import com.glaf.heathcare.service.ActualRepastPersonService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsActualQuantityService;
import com.glaf.heathcare.service.GoodsOutStockService;
import com.glaf.heathcare.service.GoodsPlanQuantityService;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

@Controller("/heathcare/goodsActualQuantityExport")
@RequestMapping("/heathcare/goodsActualQuantityExport")
public class GoodsActualQuantityExportController {

	protected static final Log logger = LogFactory.getLog(GoodsActualQuantityExportController.class);

	protected static final Semaphore semaphore2 = new Semaphore(20);

	protected DietaryService dietaryService;

	protected ActualRepastPersonService actualRepastPersonService;

	protected FoodCompositionService foodCompositionService;

	protected GoodsActualQuantityService goodsActualQuantityService;

	protected GoodsOutStockService goodsOutStockService;

	protected GoodsPlanQuantityService goodsPlanQuantityService;

	protected SysTreeService sysTreeService;

	protected SysTenantService sysTenantService;

	protected TenantConfigService tenantConfigService;

	public GoodsActualQuantityExportController() {

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
		params.put("startDate", startDate);
		params.put("startTime", startDate);
		params.put("endDate", endDate);
		params.put("endTime", endDate);

		params.put("tenantId", tenantId);
		params.put("tableSuffix", IdentityFactory.getTenantHash(tenantId));

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
				// ex.printStackTrace();
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.actualRepastPersonService")
	public void setActualRepastPersonService(ActualRepastPersonService actualRepastPersonService) {
		this.actualRepastPersonService = actualRepastPersonService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryService")
	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsActualQuantityService")
	public void setGoodsActualQuantityService(GoodsActualQuantityService goodsActualQuantityService) {
		this.goodsActualQuantityService = goodsActualQuantityService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsOutStockService")
	public void setGoodsOutStockService(GoodsOutStockService goodsOutStockService) {
		this.goodsOutStockService = goodsOutStockService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPlanQuantityService")
	public void setGoodsPlanQuantityService(GoodsPlanQuantityService goodsPlanQuantityService) {
		this.goodsPlanQuantityService = goodsPlanQuantityService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
