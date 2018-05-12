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

package com.glaf.heathcare.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationEvaluate;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.helper.MedicalExaminationHelper;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationEvaluateService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.util.MedicalExaminationEvaluateDomainFactory;

public class MedicalExaminationEvaluateBean {
	protected static final Log logger = LogFactory.getLog(MedicalExaminationEvaluateBean.class);

	public void execute(String tenantId, int year, int month) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		GrowthStandardService growthStandardService = ContextFactory
				.getBean("com.glaf.heathcare.service.growthStandardService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
		MedicalExaminationEvaluateService medicalExaminationEvaluateService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationEvaluateService");
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");

		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		List<MedicalExaminationEvaluate> rows = new ArrayList<MedicalExaminationEvaluate>();
		MedicalExaminationHelper helper = new MedicalExaminationHelper();
		String systemName = Environment.getCurrentSystemName();
		List<MedicalExamination> exams = null;
		List<Person> persons = null;
		Database database = null;
		try {
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			database = databaseService.getDatabaseByMapping("etl");
			if (database != null) {
				if (!DBUtils.tableExists(database.getName(), "HEALTH_MEDICAL_EXAM_EVAL")) {
					TableDefinition tableDefinition = MedicalExaminationEvaluateDomainFactory
							.getTableDefinition("HEALTH_MEDICAL_EXAM_EVAL");
					DBUtils.createTable(database.getName(), tableDefinition);
				}
			}

			GradeInfoQuery query0 = new GradeInfoQuery();
			query0.tenantId(tenantId);
			query0.locked(0);
			query0.deleteFlag(0);
			List<GradeInfo> grades = gradeInfoService.list(query0);

			if (grades != null && !grades.isEmpty()) {
				List<String> gradeIds = new ArrayList<String>();
				Map<String, GradeInfo> gradeMap = new HashMap<String, GradeInfo>();
				for (GradeInfo grade : grades) {
					if (!gradeIds.contains(grade.getId())) {
						gradeIds.add(grade.getId());
						gradeMap.put(grade.getId(), grade);
					}
				}

				PersonQuery query = new PersonQuery();
				query.tenantId(tenantId);
				query.gradeIds(gradeIds);
				query.locked(0);
				query.deleteFlag(0);
				persons = personService.list(query);
				if (persons != null && !persons.isEmpty()) {
					Map<String, Person> personMap = new HashMap<String, Person>();
					for (Person person : persons) {
						if (person.getBirthday() != null && StringUtils.isNotEmpty(person.getSex())) {
							personMap.put(person.getId(), person);
						}
					}

					MedicalExaminationQuery query2 = new MedicalExaminationQuery();
					query2.tenantId(tenantId);
					query2.gradeIds(gradeIds);
					query2.year(year);
					query2.month(month);
					query2.checkDateLessThanOrEqual(new Date());
					exams = medicalExaminationService.list(query2);
					if (exams != null && !exams.isEmpty()) {
						List<GrowthStandard> standards = growthStandardService.getAllGrowthStandards();
						Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
						if (standards != null && !standards.isEmpty()) {
							for (GrowthStandard gs : standards) {
								if (StringUtils.equals(gs.getType(), "3")) {
									gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex(), gs);
								}
								if (StringUtils.equals(gs.getType(), "4")) {
									// int height = (int) Math.round(gs.getHeight());
									// gsMap.put(height + "_" + gs.getSex() + "_" + gs.getType(), gs);
								}
							}
						}
						
						//logger.debug(gsMap);

						for (MedicalExamination exam : exams) {
							if (exam.getCheckDate() != null && exam.getHeight() > 0 && exam.getWeight() > 0) {
								if (personMap.get(exam.getPersonId()) == null) {
									continue;
								}
								Person person = personMap.get(exam.getPersonId());
								exam.setBirthday(person.getBirthday());
								exam.setSex(person.getSex());
								helper.evaluate(gsMap, exam);

								MedicalExaminationEvaluate model = new MedicalExaminationEvaluate();
								org.apache.commons.beanutils.PropertyUtils.copyProperties(model, exam);
								String key = exam.getAgeOfTheMoon() + "_" + exam.getSex();
								model.setTenantId(tenantId);
								model.setGradeId(person.getGradeId());
								model.setBirthday(person.getBirthday());
								model.setSex(person.getSex());
								//logger.debug("key:"+key);
								if (gsMap.get(key) != null) {
									//logger.debug("Median:"+gsMap.get(key).getMedian());
									model.setStdWeight(gsMap.get(key).getMedian());
									model.setWeightOffsetPercent(
											(model.getWeight() - model.getStdWeight()) * 100D / model.getStdWeight());
								}

								rows.add(model);
							}
						}

						if (database != null) {
							Environment.setCurrentSystemName(database.getName());
						}
						medicalExaminationEvaluateService.saveAll(tenantId, year, month, rows);// 保存体检评价数据
						rows.clear();
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			com.glaf.core.config.Environment.setCurrentSystemName(systemName);
			if (rows != null) {
				rows.clear();
				rows = null;
			}
			if (exams != null) {
				exams.clear();
				exams = null;
			}

			if (persons != null) {
				persons.clear();
				persons = null;
			}
		}
	}

}
