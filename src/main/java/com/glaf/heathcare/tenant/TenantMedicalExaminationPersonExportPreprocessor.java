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
package com.glaf.heathcare.tenant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.identity.Tenant;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.bean.MedicalExaminationEvaluateBean;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.MedicalExaminationEvaluate;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.MedicalExaminationEvaluateQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.MedicalExaminationEvaluateService;
import com.glaf.heathcare.service.PersonService;

public class TenantMedicalExaminationPersonExportPreprocessor implements ITenantReportPreprocessor {

	@Override
	public void prepare(Tenant tenant, Map<String, Object> params) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationEvaluateService medicalExaminationEvaluateService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationEvaluateService");

		int year = ParamUtils.getInt(params, "year");
		int month = ParamUtils.getInt(params, "month");

		MedicalExaminationEvaluateBean bean = new MedicalExaminationEvaluateBean();
		bean.execute(tenant.getTenantId(), year, month);

		PersonQuery query = new PersonQuery();
		query.tenantId(tenant.getTenantId());
		query.locked(0);
		query.deleteFlag(0);
		List<Person> persons = personService.list(query);

		GradeInfoQuery qx = new GradeInfoQuery();
		qx.tenantId(tenant.getTenantId());
		qx.deleteFlag(0);
		qx.locked(0);
		List<GradeInfo> grades = gradeInfoService.list(qx);

		String type = ParamUtils.getString(params, "type");

		MedicalExaminationEvaluateQuery q = new MedicalExaminationEvaluateQuery();
		q.tenantId(tenant.getTenantId());
		q.year(year);
		q.month(month);
		q.type(type);
		q.weightGreaterThanStd("true");
		q.weightLevelGreaterThanOrEqual(1);
		q.weightOffsetPercentGreaterThanOrEqual(10.0);

		if (params.get("gradeId") != null && StringUtils.isNotEmpty(ParamUtils.getString(params, "gradeId"))) {
			q.gradeId(ParamUtils.getString(params, "gradeId"));
		}

		if (params.get("checkId") != null && StringUtils.isNotEmpty(ParamUtils.getString(params, "checkId"))) {
			q.checkId(ParamUtils.getString(params, "checkId"));
		}

		if (params.get("sex") != null && StringUtils.isNotEmpty(ParamUtils.getString(params, "sex"))) {
			q.sex(ParamUtils.getString(params, "sex"));
		}

		String systemName = Environment.getCurrentSystemName();
		List<MedicalExaminationEvaluate> list = null;
		Database database = null;
		try {
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			database = databaseService.getDatabaseByMapping("etl");
			if (database != null) {
				Environment.setCurrentSystemName(database.getName());
			}
			list = medicalExaminationEvaluateService.list(q);
		} catch (Exception ex) {
			// ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			com.glaf.core.config.Environment.setCurrentSystemName(systemName);
		}

		if (list != null && !list.isEmpty()) {
			Map<String, Person> personMap = new HashMap<String, Person>();
			if (persons != null && !persons.isEmpty()) {
				for (Person p : persons) {
					personMap.put(p.getId(), p);
				}
			}

			List<MedicalExaminationEvaluate> rows1 = new ArrayList<MedicalExaminationEvaluate>();
			List<MedicalExaminationEvaluate> rows2 = new ArrayList<MedicalExaminationEvaluate>();
			List<MedicalExaminationEvaluate> rows3 = new ArrayList<MedicalExaminationEvaluate>();
			List<MedicalExaminationEvaluate> rows4 = new ArrayList<MedicalExaminationEvaluate>();

			if (grades != null && !grades.isEmpty()) {
				Date checkDate = null;
				int param0 = 0;
				int param1 = 0;
				int param2 = 0;
				int param3 = 0;
				int sortNo = 0;
				for (GradeInfo g : grades) {
					for (MedicalExaminationEvaluate me : list) {
						if (StringUtils.equals(g.getId(), me.getGradeId())) {
							me.setGradeName(g.getName());
							me.setSortNo(++sortNo);
							if (StringUtils.equals(me.getSex(), "1")) {
								me.setSex("男");
							} else {
								me.setSex("女");
							}
							switch (me.getWeightLevel()) {
							case 1:
								param1 = param1 + 1;
								break;
							case 2:
								param2 = param2 + 1;
								break;
							case 3:
								param3 = param3 + 1;
								break;
							default:
								param0 = param0 + 1;
								break;
							}
							if (rows1.size() < 20) {
								if (checkDate == null) {
									checkDate = me.getCheckDate();
								}
								rows1.add(me);
							} else {
								if (rows2.size() < 20) {
									rows2.add(me);
								} else {
									if (rows3.size() < 20) {
										rows3.add(me);
									} else {
										rows4.add(me);
									}
								}
							}
						}
					}
				}

				params.put("rows1", rows1);
				params.put("rows2", rows2);
				params.put("rows3", rows3);
				params.put("rows4", rows4);
				params.put("param0", param0);
				params.put("param1", param1);
				params.put("param2", param2);
				params.put("param3", param3);

				if (checkDate != null) {
					params.put("checkDate", DateUtils.getDate(checkDate));
				}
			}
		}
	}

}
