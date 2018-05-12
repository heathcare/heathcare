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
import com.glaf.core.domain.util.DbidDomainFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;

import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationGradeCount;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.helper.MedicalExaminationHelper;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GradePersonRelationService;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationGradeCountService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.util.MedicalExaminationCountDomainFactory;
import com.glaf.heathcare.util.MedicalExaminationEvaluateDomainFactory;
import com.glaf.heathcare.util.MedicalExaminationGradeCountDomainFactory;
import com.glaf.heathcare.util.PhysicalGrowthCountDomainFactory;

public class MedicalExaminationGradeCountBean {
	protected static final Log logger = LogFactory.getLog(MedicalExaminationGradeCountBean.class);

	public void execute(String tenantId, String type, int year, int month) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		GrowthStandardService growthStandardService = ContextFactory
				.getBean("com.glaf.heathcare.service.growthStandardService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		GradePersonRelationService gradePersonRelationService = ContextFactory
				.getBean("com.glaf.heathcare.service.gradePersonRelationService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationGradeCountService medicalExaminationGradeCountService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationGradeCountService");
		MedicalExaminationHelper helper = new MedicalExaminationHelper();
		String systemName = Environment.getCurrentSystemName();
		List<MedicalExamination> exams = null;
		List<Person> persons = null;
		Database database = null;
		try {
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			database = databaseService.getDatabaseByMapping("etl");
			if (database != null) {
				if (!DBUtils.tableExists(database.getName(), "SYS_DBID")) {
					TableDefinition tableDefinition = DbidDomainFactory.getTableDefinition("SYS_DBID");
					DBUtils.createTable(database.getName(), tableDefinition);
				}
				if (!DBUtils.tableExists(database.getName(), "HEALTH_PHYSICAL_GROWTH_COUNT")) {
					TableDefinition tableDefinition = PhysicalGrowthCountDomainFactory
							.getTableDefinition("HEALTH_PHYSICAL_GROWTH_COUNT");
					DBUtils.createTable(database.getName(), tableDefinition);
				}
				if (!DBUtils.tableExists(database.getName(), "HEALTH_MEDICAL_EXAM_EVAL")) {
					TableDefinition tableDefinition = MedicalExaminationEvaluateDomainFactory
							.getTableDefinition("HEALTH_MEDICAL_EXAM_EVAL");
					DBUtils.createTable(database.getName(), tableDefinition);
				}
				if (!DBUtils.tableExists(database.getName(), "HEALTH_MEDICAL_EXAM_COUNT")) {
					TableDefinition tableDefinition = MedicalExaminationCountDomainFactory
							.getTableDefinition("HEALTH_MEDICAL_EXAM_COUNT");
					DBUtils.createTable(database.getName(), tableDefinition);
				}
				if (!DBUtils.tableExists(database.getName(), "HEALTH_MEDICAL_EXAM_GRADE_CNT")) {
					TableDefinition tableDefinition = MedicalExaminationGradeCountDomainFactory
							.getTableDefinition("HEALTH_MEDICAL_EXAM_GRADE_CNT");
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
				Map<String, Integer> gradePersonMap = new HashMap<String, Integer>();
				Map<String, Integer> gradeSortMap = new HashMap<String, Integer>();
				int sortNo = 0;
				for (GradeInfo grade : grades) {
					if (!gradeIds.contains(grade.getId())) {
						gradeIds.add(grade.getId());
						gradeMap.put(grade.getId(), grade);
						gradeSortMap.put(grade.getId(), ++sortNo);
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
						Integer ix = gradePersonMap.get(person.getGradeId());
						if (ix == null) {
							ix = new Integer(0);
						}
						ix = new Integer(ix.intValue() + 1);
						gradePersonMap.put(person.getGradeId(), ix);
					}

					Map<String, Integer> cntMap0 = gradePersonRelationService.getPersonCountMap(tenantId, year, month);
					gradePersonMap.putAll(cntMap0);// 替换为实际在册人数

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
								gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
								if (StringUtils.equals(gs.getType(), "4")) {
									int height = (int) Math.round(gs.getHeight());
									gsMap.put(height + "_" + gs.getSex() + "_" + gs.getType(), gs);
								}
							}
						}

						Map<String, MedicalExaminationGradeCount> cntMap = new HashMap<String, MedicalExaminationGradeCount>();
						for (MedicalExamination exam : exams) {
							if (exam.getCheckDate() != null && exam.getHeight() > 0 && exam.getWeight() > 0) {
								if (personMap.get(exam.getPersonId()) == null) {
									continue;
								}
								Person person = personMap.get(exam.getPersonId());
								exam.setBirthday(person.getBirthday());
								exam.setSex(person.getSex());
								helper.evaluate(gsMap, exam);
								MedicalExaminationGradeCount cnt = cntMap.get(person.getGradeId());
								if (cnt == null) {
									cnt = new MedicalExaminationGradeCount();
									cnt.setGradeId(person.getGradeId());
									cnt.setTenantId(tenantId);
									cnt.setYear(year);
									cnt.setMonth(month);
									cnt.setType(type);
									cnt.setPersonCount(gradePersonMap.get(person.getGradeId()));
									cnt.setSortNo(gradeSortMap.get(person.getGradeId()));
									if (cntMap0.get(cnt.getGradeId()) != null) {
										cnt.setPersonCount(cntMap0.get(cnt.getGradeId()));// 替换为实际在册人数
									}
									cntMap.put(person.getGradeId(), cnt);
								}
								cnt.setCheckPerson(cnt.getCheckPerson() + 1);
								if (StringUtils.equals(person.getSex(), "1")) {
									cnt.setMale(cnt.getMale() + 1);
								} else {
									cnt.setFemale(cnt.getFemale() + 1);
								}

								switch ((int) exam.getBmiIndex()) {// 体重/身高
								case 3:
									cnt.setWeightObesityMid(cnt.getWeightObesityMid() + 1);// 中度肥胖
									break;
								case 2:
									cnt.setWeightObesityLow(cnt.getWeightObesityLow() + 1);// 轻度肥胖
									break;
								case 1:
									cnt.setOverWeight(cnt.getOverWeight() + 1);// 超重
									break;
								case -1:
									cnt.setWeightLow(cnt.getWeightLow() + 1);// 低体重
									break;
								case -2:
									cnt.setWeightSkinny(cnt.getWeightSkinny() + 1);// 消瘦
									break;
								case -3:
									cnt.setWeightSevereMalnutrition(cnt.getWeightSevereMalnutrition() + 1);// 严重营养不良
									break;
								default:
									break;
								}

								switch (exam.getHeightLevel()) {
								case -3:
								case -2:
								case -1:
									cnt.setWeightRetardation(cnt.getWeightRetardation() + 1);// 发育迟缓
									break;
								default:
									break;
								}

								switch (exam.getAnemiaLevel()) {
								case -4:
								case -3:
									cnt.setAnemiaHigh(cnt.getAnemiaHigh() + 1);// 重度贫血人数
									break;
								case -2:
									cnt.setAnemiaMid(cnt.getAnemiaMid() + 1);// 中度贫血人数
									break;
								case -1:
									cnt.setAnemiaLow(cnt.getAnemiaLow() + 1);// 轻度贫血人数
									break;
								default:
									cnt.setAnemiaCheckNormal(cnt.getAnemiaCheckNormal() + 1);
									break;
								}

								if (exam.getSaprodontia() > 0) {// 龋齿
									cnt.setSaprodontia(cnt.getSaprodontia() + 1);
								}

								if (StringUtils.equals(exam.getEyeLeft(), "T")
										|| StringUtils.equals(exam.getEyeRight(), "T")) {// 沙眼
									cnt.setTrachoma(cnt.getTrachoma() + 1);
								}

								if (StringUtils.equals(exam.getEyeLeft(), "A")
										|| StringUtils.equals(exam.getEyeRight(), "A")) {// 弱视
									cnt.setAmblyopia(cnt.getAmblyopia() + 1);
								}

								if (exam.getHemoglobinValue() >= 90 && exam.getHemoglobinValue() < 110) {// 血红蛋白Hb
									cnt.setHemoglobin110(cnt.getHemoglobin110() + 1);
								}

								if (exam.getHemoglobinValue() > 0 && exam.getHemoglobinValue() < 90) {
									cnt.setHemoglobin90(cnt.getHemoglobin90() + 1);
								}

								if (exam.getHbsabValue() > 10) {// 乙肝表面抗体
									cnt.setHbsab(cnt.getHbsab() + 1);
								}

								if (StringUtils.equals(exam.getSgpt(), "N")) {// 肝功能异常
									cnt.setSgpt(cnt.getSgpt() + 1);
								}

								if (StringUtils.equals(exam.getHvaigm(), "X")) {// HVAIgM异常 X阳性
									cnt.setHvaigm(cnt.getHvaigm() + 1);
								}

							}
						}

						Collection<MedicalExaminationGradeCount> rows = cntMap.values();

						if (!rows.isEmpty()) {
							for (MedicalExaminationGradeCount cnt : rows) {
								if (cnt.getPersonCount() > 0) {
									cnt.setCheckPercent(cnt.getCheckPerson() * 1.0D / cnt.getPersonCount());
								}
								if (cnt.getCheckPerson() > 0) {
									cnt.setAnemiaCheck(cnt.getCheckPerson());
									cnt.setAnemiaCheckNormalPercent(
											cnt.getAnemiaCheckNormal() * 1.0D / cnt.getCheckPerson());
								}
							}

							if (database != null) {
								Environment.setCurrentSystemName(database.getName());
							}

							medicalExaminationGradeCountService.saveAll(tenantId, type, year, month, rows);
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
