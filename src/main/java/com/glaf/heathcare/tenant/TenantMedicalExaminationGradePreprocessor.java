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

import org.apache.commons.lang.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.helper.MedicalExaminationHelper;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.report.IReportPreprocessor;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;

public class TenantMedicalExaminationGradePreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(Tenant tenant, Map<String, Object> params) {
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
		GrowthStandardService growthStandardService = ContextFactory
				.getBean("com.glaf.heathcare.service.growthStandardService");
		String tenantId = tenant.getTenantId();
		int year = ParamUtils.getInt(params, "year");
		int month = ParamUtils.getInt(params, "month");
		String type = ParamUtils.getString(params, "type");
		String gradeId = ParamUtils.getString(params, "gradeId");
		GradeInfo gradeInfo = gradeInfoService.getGradeInfo(gradeId);
		if (gradeInfo != null && gradeInfo.getLocked() == 0) {
			PersonQuery query = new PersonQuery();
			query.tenantId(tenantId);
			query.gradeId(gradeId);
			query.locked(0);
			query.deleteFlag(0);
			List<Person> persons = personService.list(query);
			if (persons != null && !persons.isEmpty()) {
				Map<String, Person> personMap = new HashMap<String, Person>();
				List<String> personIds = new ArrayList<String>();
				for (Person person : persons) {
					if (person.getBirthday() != null && StringUtils.isNotEmpty(person.getSex())) {
						personMap.put(person.getId(), person);
						personIds.add(person.getId());
					}
				}

				MedicalExaminationQuery query2 = new MedicalExaminationQuery();
				query2.tenantId(tenantId);
				query2.gradeId(gradeId);
				query2.personIds(personIds);
				query2.year(year);
				query2.month(month);
				query2.type(type);
				query2.setOrderBy(" E.SEX_ asc ");

				List<MedicalExamination> list = medicalExaminationService.list(query2);
				if (list != null && !list.isEmpty()) {
					MedicalExaminationQuery query3 = new MedicalExaminationQuery();
					query3.tenantId(tenantId);
					query3.gradeId(gradeId);
					query3.personIds(personIds);
					query3.year(year - 1);
					query3.month(month);
					query3.type(type);

					Person person = null;
					Date checkDate = null;
					Date preCheckDate = null;
					List<MedicalExamination> list2 = medicalExaminationService.list(query3);
					Map<String, MedicalExamination> examMap = new HashMap<String, MedicalExamination>();
					if (list2 != null && !list2.isEmpty()) {
						for (MedicalExamination exam : list2) {
							examMap.put(exam.getPersonId(), exam);
							if (exam.getCheckDate() != null) {
								preCheckDate = exam.getCheckDate();
							}
						}
					}

					List<GrowthStandard> standards = growthStandardService.getAllGrowthStandards();
					Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
					MedicalExaminationHelper helper = new MedicalExaminationHelper();
					if (standards != null && !standards.isEmpty()) {
						for (GrowthStandard gs : standards) {
							gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
							if (StringUtils.equals(gs.getType(), "4")) {
								int height = (int) Math.round(gs.getHeight());
								gsMap.put(height + "_" + gs.getSex() + "_" + gs.getType(), gs);
							}
						}
					}

					GrowthStandard gs = null;

					int sortNo = 0;

					for (MedicalExamination exam : list) {
						exam.setSortNo(++sortNo);
						MedicalExamination pre = examMap.get(exam.getPersonId());
						if (pre != null) {
							exam.setPreviousHeight(pre.getHeight());
							exam.setPreviousWeight(pre.getWeight());
							if (pre.getCheckDate() != null && exam.getCheckDate() != null) {
								int days = DateUtils.getDaysBetween(pre.getCheckDate(), exam.getCheckDate());
								exam.setCheckIntervalMoon((int) Math.floor(days / 30.0D));
							}
						}
						if (exam.getCheckDate() != null) {
							checkDate = exam.getCheckDate();
						}
						person = personMap.get(exam.getPersonId());
						exam.setName(person.getName());
						if (person.getBirthday() != null) {
							exam.setBirthday(person.getBirthday());
							exam.setBirthdayString(DateUtils.getDate(person.getBirthday()));
						}

						if (StringUtils.equals(exam.getHvaigm(), "X")) {
							exam.setHvaigmText("阳性");
						} else if (StringUtils.equals(exam.getHvaigm(), "Y")) {
							exam.setHvaigmText("阴性");
						}

						if (StringUtils.equals(person.getSex(), "1")) {
							exam.setSex("男");
						} else {
							exam.setSex("女");
						}

						helper.evaluate(gsMap, exam);

						gs = gsMap.get(exam.getAgeOfTheMoon() + "_" + person.getSex() + "_3");// 年龄别体重
						if (gs != null && gs.getMedian() > 0) {
							exam.setStdWeight(gs.getMedian());// 体重中位数
							exam.setWeightOffsetPercent((exam.getWeight() - gs.getMedian()) / gs.getMedian() * 100D);
						}

					}

					params.put("gradeInfo", gradeInfo);
					params.put("gradeName", gradeInfo.getName());
					if (checkDate != null) {
						params.put("checkDate", DateUtils.getDate(checkDate));
					}
					if (preCheckDate != null) {
						params.put("preCheckDate", DateUtils.getDate(preCheckDate));
					}
					params.put("rows", list);
				}
			}
		}
	}

}
