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
import java.util.Collection;
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
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PhysicalGrowthCount;
import com.glaf.heathcare.helper.MedicalExaminationEvaluateHelper;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.service.PhysicalGrowthCountService;
import com.glaf.heathcare.util.PhysicalGrowthCountDomainFactory;

public class PhysicalGrowthCountBean {
	protected static final Log logger = LogFactory.getLog(PhysicalGrowthCountBean.class);

	protected static boolean checkTable = false;

	public void execute(String tenantId, String type, int year, int month) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		GrowthStandardService growthStandardService = ContextFactory
				.getBean("com.glaf.heathcare.service.growthStandardService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		PhysicalGrowthCountService physicalGrowthCountService = ContextFactory
				.getBean("com.glaf.heathcare.service.physicalGrowthCountService");
		MedicalExaminationEvaluateHelper helper = new MedicalExaminationEvaluateHelper();
		String systemName = Environment.getCurrentSystemName();
		List<MedicalExamination> exams = null;
		List<Person> persons = null;
		Database database = null;
		try {
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			database = databaseService.getDatabaseByMapping("etl");
			if (database != null) {
				if (!checkTable) {
					if (!DBUtils.tableExists(database.getName(), "HEALTH_PHYSICAL_GROWTH_COUNT")) {
						TableDefinition tableDefinition = PhysicalGrowthCountDomainFactory
								.getTableDefinition("HEALTH_PHYSICAL_GROWTH_COUNT");
						DBUtils.createTable(database.getName(), tableDefinition);
					} else {
						TableDefinition tableDefinition = PhysicalGrowthCountDomainFactory
								.getTableDefinition("HEALTH_PHYSICAL_GROWTH_COUNT");
						DBUtils.alterTable(database.getName(), tableDefinition);
					}
					checkTable = true;
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
					query2.type(type);
					query2.checkDateLessThanOrEqual(new Date());
					exams = medicalExaminationService.list(query2);
					if (exams != null && !exams.isEmpty()) {
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

						Map<String, PhysicalGrowthCount> cntMap = new HashMap<String, PhysicalGrowthCount>();
						for (MedicalExamination exam : exams) {
							if (exam.getCheckDate() != null && exam.getHeight() > 0 && exam.getWeight() > 0) {
								if (personMap.get(exam.getPersonId()) == null) {
									continue;
								}
								Person person = personMap.get(exam.getPersonId());
								exam.setBirthday(person.getBirthday());
								exam.setSex(person.getSex());
								helper.evaluate(gsMap, exam);
								PhysicalGrowthCount cntHA = cntMap.get(person.getGradeId() + "_ha");
								if (cntHA == null) {
									cntHA = new PhysicalGrowthCount();
									cntHA.setGradeId(person.getGradeId());
									cntHA.setTenantId(tenantId);
									cntHA.setYear(year);
									cntHA.setMonth(month);
									cntHA.setItem("H/A");// 身高/年龄
									cntHA.setType(type);
									cntHA.setStandard("mean");
									cntMap.put(person.getGradeId() + "_ha", cntHA);
								}
								cntHA.setTotal(cntHA.getTotal() + 1);
								switch (exam.getHeightLevel()) {// 身高/年龄
								case 3:
									cntHA.setLevel9(cntHA.getLevel9() + 1);
									cntHA.setNormal(cntHA.getNormal() + 1);
									break;
								case 2:
									cntHA.setLevel8(cntHA.getLevel8() + 1);
									cntHA.setNormal(cntHA.getNormal() + 1);
									break;
								case 1:
									cntHA.setLevel7(cntHA.getLevel7() + 1);
									cntHA.setNormal(cntHA.getNormal() + 1);
									break;
								case -1:
									cntHA.setLevel3(cntHA.getLevel3() + 1);
									break;
								case -2:
									cntHA.setLevel2(cntHA.getLevel2() + 1);
									break;
								case -3:
									cntHA.setLevel1(cntHA.getLevel1() + 1);
									break;
								default:
									cntHA.setNormal(cntHA.getNormal() + 1);
									break;
								}

								PhysicalGrowthCount cntWA = cntMap.get(person.getGradeId() + "_wa");
								if (cntWA == null) {
									cntWA = new PhysicalGrowthCount();
									cntWA.setGradeId(person.getGradeId());
									cntWA.setTenantId(tenantId);
									cntWA.setYear(year);
									cntWA.setMonth(month);
									cntWA.setItem("W/A");// 体重/年龄
									cntWA.setType(type);
									cntWA.setStandard("mean");
									cntMap.put(person.getGradeId() + "_wa", cntWA);
								}
								cntWA.setTotal(cntWA.getTotal() + 1);
								switch (exam.getWeightLevel()) {// 体重/年龄
								case 3:
									cntWA.setLevel9(cntWA.getLevel9() + 1);
									break;
								case 2:
									cntWA.setLevel8(cntWA.getLevel8() + 1);
									break;
								case 1:
									cntWA.setLevel7(cntWA.getLevel7() + 1);
									break;
								case -1:
									cntWA.setLevel3(cntWA.getLevel3() + 1);
									break;
								case -2:
									cntWA.setLevel2(cntWA.getLevel2() + 1);
									break;
								case -3:
									cntWA.setLevel1(cntWA.getLevel1() + 1);
									break;
								default:
									cntWA.setNormal(cntWA.getNormal() + 1);
									break;
								}

								PhysicalGrowthCount cntWH = cntMap.get(person.getGradeId() + "_wh");
								if (cntWH == null) {
									cntWH = new PhysicalGrowthCount();
									cntWH.setGradeId(person.getGradeId());
									cntWH.setTenantId(tenantId);
									cntWH.setYear(year);
									cntWH.setMonth(month);
									cntWH.setItem("W/H");// 体重/身高
									cntWH.setType(type);
									cntWH.setStandard("mean");
									cntMap.put(person.getGradeId() + "_wh", cntWH);
								}
								cntWH.setTotal(cntWH.getTotal() + 1);
								switch (exam.getWeightHeightLevel()) {// 体重/身高
								case 3:
									cntWH.setLevel9(cntWH.getLevel9() + 1);
									break;
								case 2:
									cntWH.setLevel8(cntWH.getLevel8() + 1);
									break;
								case 1:
									cntWH.setLevel7(cntWH.getLevel7() + 1);
									break;
								case -1:
									cntWH.setLevel3(cntWH.getLevel3() + 1);
									break;
								case -2:
									cntWH.setLevel2(cntWH.getLevel2() + 1);
									break;
								case -3:
									cntWH.setLevel1(cntWH.getLevel1() + 1);
									break;
								default:
									cntWH.setNormal(cntWH.getNormal() + 1);
									break;
								}

							}
						}

						Collection<PhysicalGrowthCount> rows = cntMap.values();

						if (!rows.isEmpty()) {

							for (PhysicalGrowthCount cnt : rows) {
								if (cnt.getTotal() > 0) {
									cnt.setLevel1Percent(cnt.getLevel1() * 100.0D / cnt.getTotal());
									cnt.setLevel2Percent(cnt.getLevel2() * 100.0D / cnt.getTotal());
									cnt.setLevel3Percent(cnt.getLevel3() * 100.0D / cnt.getTotal());
									cnt.setLevel5Percent(cnt.getLevel5() * 100.0D / cnt.getTotal());
									cnt.setLevel7Percent(cnt.getLevel7() * 100.0D / cnt.getTotal());
									cnt.setLevel8Percent(cnt.getLevel8() * 100.0D / cnt.getTotal());
									cnt.setLevel9Percent(cnt.getLevel9() * 100.0D / cnt.getTotal());
									cnt.setNormalPercent(cnt.getNormal() * 100.0D / cnt.getTotal());
								}
							}

							if (database != null) {
								Environment.setCurrentSystemName(database.getName());
							}

							physicalGrowthCountService.saveAll(tenantId, type, year, month, rows);
						}
					}
				}
			}

		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			com.glaf.core.config.Environment.setCurrentSystemName(systemName);
		}
	}

}
