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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.ZipUtils;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationDefService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.tenant.ITenantReportPreprocessor;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/medicalExaminationExport")
@RequestMapping("/heathcare/medicalExaminationExport")
public class MedicalExaminationExportController {
	protected static final Log logger = LogFactory.getLog(MedicalExaminationExportController.class);

	protected static final Semaphore semaphore2 = new Semaphore(20);

	protected MedicalExaminationService medicalExaminationService;

	protected MedicalExaminationDefService medicalExaminationDefService;

	protected GradeInfoService gradeInfoService;

	protected GrowthStandardService growthStandardService;

	protected PersonService personService;

	protected SysTenantService sysTenantService;

	protected TenantConfigService tenantConfigService;

	public MedicalExaminationExportController() {

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
		String tenantId = loginContext.getTenantId();

		params.put("year", year);
		params.put("month", month);
		params.put("tenantId", tenantId);
		params.put("tableSuffix", IdentityFactory.getTenantHash(tenantId));

		String reportId = request.getParameter("reportId");
		ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition(reportId);

		if (rdf != null) {
			byte[] data = null;
			byte[] bytes = null;
			InputStream is = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			ITenantReportPreprocessor reportPreprocessor = null;
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

				String gradeId = request.getParameter("gradeId");

				MedicalExaminationQuery query = new MedicalExaminationQuery();
				query.type(request.getParameter("type"));
				query.tenantId(tenantId);
				query.gradeId(gradeId);
				query.year(year);
				query.month(month);
				query.locked(0);
				query.deleteFlag(0);

				List<MedicalExamination> exams = medicalExaminationService.list(query);
				if (exams != null && !exams.isEmpty()) {

					for (MedicalExamination exam : exams) {
						params.put("examId", exam.getId());
						params.put("personId", exam.getPersonId());

						if (StringUtils.isNotEmpty(rdf.getPrepareClass())) {
							reportPreprocessor = (ITenantReportPreprocessor) com.glaf.core.util.ReflectUtils
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
						bytesMap.put(exam.getName() + ".xls", bytes);

						IOUtils.closeQuietly(is);
						IOUtils.closeQuietly(bais);
						IOUtils.closeQuietly(baos);
						IOUtils.closeQuietly(bos);
					}
					data = ZipUtils.genZipBytes(bytesMap);
					ResponseUtils.download(request, response, data,
							"export" + DateUtils.getNowYearMonthDayHHmmss() + ".zip");
				}
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

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
