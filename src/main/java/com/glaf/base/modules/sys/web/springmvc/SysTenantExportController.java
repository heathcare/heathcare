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

package com.glaf.base.modules.sys.web.springmvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.glaf.base.district.service.DistrictService;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.jdbc.QueryHelper;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/tenantExport")
@RequestMapping("/sys/tenantExport")
public class SysTenantExportController {
	protected static final Log logger = LogFactory.getLog(SysTenantExportController.class);

	public final static String newline = System.getProperty("line.separator");

	protected DistrictService districtService;

	protected SysTenantService sysTenantService;

	public SysTenantExportController() {

	}

	@ResponseBody
	@RequestMapping("/export")
	public byte[] export(HttpServletRequest request, HttpServletResponse response) {
		String dbType = request.getParameter("dbType");
		String tenantId = request.getParameter("tenantId");
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysTenant sysTenant = null;
		java.sql.Connection conn = null;
		try {
			if (StringUtils.isNotEmpty(tenantId)) {
				sysTenant = sysTenantService.getSysTenantByTenantId(tenantId);
				if (sysTenant != null) {
					QueryHelper helper = new QueryHelper();
					StringBuilder buffer = new StringBuilder();
					String suffix = String.valueOf(IdentityFactory.getTenantHash(tenantId));
					conn = DBConnectionFactory.getConnection();

					buffer.append("--delete from SYS_USER where TENANTID = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "SYS_USER", dbType,
							" select * from SYS_USER where TENANTID = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from SYS_USER_ROLE where TENANTID = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "SYS_USER_ROLE", dbType,
							" select * from SYS_USER_ROLE where TENANTID = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from SYS_TENANT where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "SYS_TENANT", dbType,
							" select * from SYS_TENANT where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from SYS_TENANT_CONFIG where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "SYS_TENANT_CONFIG", dbType,
							" select * from SYS_TENANT_CONFIG where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_GRADE_INFO where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_GRADE_INFO", dbType,
							" select * from HEALTH_GRADE_INFO where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_PERSON where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_PERSON", dbType,
							" select * from HEALTH_PERSON where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_PERSON_LINKMAN where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_PERSON_LINKMAN", dbType,
							" select * from HEALTH_PERSON_LINKMAN where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_PERSON_INFO where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_PERSON_INFO", dbType,
							" select * from HEALTH_PERSON_INFO where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_PERSON_SICKNESS where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_PERSON_SICKNESS", dbType,
							" select * from HEALTH_PERSON_SICKNESS where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_REPAST_PERSON where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_REPAST_PERSON", dbType,
							" select * from HEALTH_REPAST_PERSON where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_MEDICAL_EXAMINATION where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_MEDICAL_EXAMINATION", dbType,
							" select * from HEALTH_MEDICAL_EXAMINATION where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append(
							"--delete from HEALTH_MEDICAL_EXAMINATION_DEF where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_MEDICAL_EXAMINATION_DEF", dbType,
							" select * from HEALTH_MEDICAL_EXAMINATION_DEF where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_MONTHLY_FEE where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_MONTHLY_FEE", dbType,
							" select * from HEALTH_MONTHLY_FEE where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_MONTHLY_MEAL_FEE where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_MONTHLY_MEAL_FEE", dbType,
							" select * from HEALTH_MONTHLY_MEAL_FEE where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_FOOD_FAVORITE where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_FOOD_FAVORITE", dbType,
							" select * from HEALTH_FOOD_FAVORITE where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_DIETARY_TEMPLATE where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_DIETARY_TEMPLATE", dbType,
							" select * from HEALTH_DIETARY_TEMPLATE where TENANTID_ = '" + tenantId + "' ", params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from HEALTH_DIETARY" + suffix + " where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_DIETARY" + suffix, dbType,
							" select * from HEALTH_DIETARY" + suffix + " where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append(
							"--delete from HEALTH_DIETARY_ITEM" + suffix + " where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "HEALTH_DIETARY_ITEM" + suffix, dbType,
							" select * from HEALTH_DIETARY_ITEM" + suffix + " where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append(
							"--delete from GOODS_ACCEPTANCE" + suffix + " where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "GOODS_ACCEPTANCE" + suffix, dbType,
							" select * from GOODS_ACCEPTANCE" + suffix + " where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append(
							"--delete from GOODS_ACTUAL_QUANTITY" + suffix + " where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "GOODS_ACTUAL_QUANTITY" + suffix, dbType,
							" select * from GOODS_ACTUAL_QUANTITY" + suffix + " where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from GOODS_IN_STOCK" + suffix + " where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "GOODS_IN_STOCK" + suffix, dbType,
							" select * from GOODS_IN_STOCK" + suffix + " where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from GOODS_OUT_STOCK" + suffix + " where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "GOODS_OUT_STOCK" + suffix, dbType,
							" select * from GOODS_OUT_STOCK" + suffix + " where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append(
							"--delete from GOODS_PLAN_QUANTITY" + suffix + " where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "GOODS_PLAN_QUANTITY" + suffix, dbType,
							" select * from GOODS_PLAN_QUANTITY" + suffix + " where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append("--delete from GOODS_PURCHASE" + suffix + " where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "GOODS_PURCHASE" + suffix, dbType,
							" select * from GOODS_PURCHASE" + suffix + " where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					buffer.append(
							"--delete from GOODS_PURCHASE_PLAN" + suffix + " where TENANTID_ = '" + tenantId + "'; ");
					buffer.append(newline);
					buffer.append(helper.getInsertScript(conn, "GOODS_PURCHASE_PLAN" + suffix, dbType,
							" select * from GOODS_PURCHASE_PLAN" + suffix + " where TENANTID_ = '" + tenantId + "' ",
							params));
					buffer.append(newline);
					buffer.append(newline);

					ResponseUtils.download(request, response, buffer.toString().getBytes("UTF-8"), "export.sql");
					return null;
				}
			}
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			JdbcUtils.close(conn);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

}
