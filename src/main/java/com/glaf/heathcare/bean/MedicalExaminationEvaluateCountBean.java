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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.query.SysTenantQuery;
import com.glaf.base.modules.sys.service.SysTenantService;

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
import com.glaf.heathcare.domain.MedicalExaminationCount;
import com.glaf.heathcare.domain.MedicalExaminationEvaluate;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.helper.MedicalExaminationHelper;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GradePersonRelationService;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationCountService;
import com.glaf.heathcare.service.MedicalExaminationEvaluateService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.util.MedicalExaminationCountDomainFactory;
import com.glaf.heathcare.util.MedicalExaminationEvaluateDomainFactory;
import com.glaf.heathcare.util.MedicalExaminationGradeCountDomainFactory;

public class MedicalExaminationEvaluateCountBean {
	protected static final Log logger = LogFactory.getLog(MedicalExaminationEvaluateCountBean.class);

	public void execute(String tenantId) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		SysTenantService sysTenantService = ContextFactory.getBean("sysTenantService");
		GrowthStandardService growthStandardService = ContextFactory
				.getBean("com.glaf.heathcare.service.growthStandardService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
		MedicalExaminationEvaluateService medicalExaminationEvaluateService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationEvaluateService");
		MedicalExaminationCountService medicalExaminationCountService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationCountService");
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		GradePersonRelationService gradePersonRelationService = ContextFactory
				.getBean("com.glaf.heathcare.service.gradePersonRelationService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		List<MedicalExaminationCount> tenantList = new ArrayList<MedicalExaminationCount>();
		List<MedicalExaminationEvaluate> rows = new ArrayList<MedicalExaminationEvaluate>();
		MedicalExaminationHelper helper = new MedicalExaminationHelper();
		String systemName = Environment.getCurrentSystemName();
		Collection<MedicalExaminationCount> list = null;
		Collection<MedicalExaminationCount> values = null;
		List<MedicalExaminationCount> indexList = null;
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

			SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);

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

								model.setTenantId(tenantId);
								model.setProvinceId(tenant.getProvinceId());
								model.setCityId(tenant.getCityId());
								model.setAreaId(tenant.getAreaId());
								model.setGradeId(person.getGradeId());
								model.setBirthday(person.getBirthday());
								model.setSex(person.getSex());

								rows.add(model);
							}
						}

						if (database != null) {
							Environment.setCurrentSystemName(database.getName());
						}
						medicalExaminationEvaluateService.saveAll(tenantId, rows);// 保存体检评价数据
						rows.clear();

						Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);

						MedicalExaminationQuery query3 = new MedicalExaminationQuery();
						query3.tenantId(tenantId);
						query3.gradeIds(gradeIds);
						query3.checkDateLessThanOrEqual(new Date());// 排除错误的数据
						indexList = medicalExaminationService.getMedicalExaminationIndexs(query3);// 统计维度
						if (indexList != null && !indexList.isEmpty()) {
							for (MedicalExaminationCount cnt : indexList) {
								if (cnt != null && StringUtils.isNotEmpty(cnt.getType())) {
									/**
									 * 遍历全部体检数据，按年度、类型进行归类
									 */
									Map<String, Integer> personCountMap = gradePersonRelationService
											.getPersonCountMap(tenantId, cnt.getYear(), cnt.getMonth());
									list = helper.populate(tenantId, cnt.getYear(), cnt.getMonth(), cnt.getType(),
											grades, persons, exams, standards, personCountMap);
									if (list != null && !list.isEmpty()) {
										for (MedicalExaminationCount model : list) {
											model.setTenantId(tenantId);
											model.setYear(cnt.getYear());
											model.setMonth(cnt.getMonth());
											model.setType(cnt.getType());
											model.setTargetType("T");
											if (personCountMap.get(cnt.getGradeId()) != null) {
												model.setTotalPerson(personCountMap.get(cnt.getGradeId()));// 替换为实际在册人数
											}
											tenantList.add(model);
										}
									}
								}
							}
							if (database != null) {
								Environment.setCurrentSystemName(database.getName());
							}
							if (tenantList.size() > 0) {
								logger.debug(tenantId + "体检汇总数:" + tenantList.size());
								medicalExaminationCountService.saveAll(tenantId, "T", tenantList);

								/**
								 * 汇总每个年级的数据
								 */
								Map<String, MedicalExaminationCount> gradeCntMap = new HashMap<String, MedicalExaminationCount>();
								for (MedicalExaminationCount cnt : tenantList) {
									if (cnt.getCheckPerson() <= 5) {
										continue;
									}
									String iKey = cnt.getGradeYear() + "_" + cnt.getType() + "_" + cnt.getYear() + "_"
											+ cnt.getMonth();
									MedicalExaminationCount model = gradeCntMap.get(iKey);
									if (model == null) {
										model = new MedicalExaminationCount();
										model.setTenantId(tenantId);
										model.setGradeYear(cnt.getGradeYear());
										model.setYear(cnt.getYear());
										model.setMonth(cnt.getMonth());
										model.setTargetType("G");
										model.setType(cnt.getType());
										gradeCntMap.put(iKey, model);
									}
									model.setCheckPerson(cnt.getCheckPerson() + model.getCheckPerson());
									model.setTotalPerson(cnt.getTotalPerson() + model.getTotalPerson());
									model.setTotalMale(cnt.getTotalMale() + model.getTotalMale());
									model.setTotalFemale(cnt.getTotalFemale() + model.getTotalFemale());

									model.setMeanHeightLow(cnt.getMeanHeightLow() + model.getMeanHeightLow());
									model.setMeanHeightNormal(cnt.getMeanHeightNormal() + model.getMeanHeightNormal());
									model.setMeanOverWeight(cnt.getMeanOverWeight() + model.getMeanOverWeight());
									model.setMeanWeightLow(cnt.getMeanWeightLow() + model.getMeanWeightLow());
									model.setMeanWeightNormal(cnt.getMeanWeightNormal() + model.getMeanWeightNormal());
									model.setMeanWeightObesity(
											cnt.getMeanWeightObesity() + model.getMeanWeightObesity());
									model.setMeanWeightSkinny(cnt.getMeanWeightSkinny() + model.getMeanWeightSkinny());

									model.setPrctileHeightAgeLow(
											cnt.getPrctileHeightAgeLow() + model.getPrctileHeightAgeLow());
									model.setPrctileHeightAgeNormal(
											cnt.getPrctileHeightAgeNormal() + model.getPrctileHeightAgeNormal());
									model.setPrctileOverWeight(
											cnt.getPrctileOverWeight() + model.getPrctileOverWeight());
									model.setPrctileWeightAgeLow(
											cnt.getPrctileWeightAgeLow() + model.getPrctileWeightAgeLow());
									model.setPrctileWeightAgeNormal(
											cnt.getPrctileWeightAgeNormal() + model.getPrctileWeightAgeNormal());
									model.setPrctileWeightHeightLow(
											cnt.getPrctileWeightHeightLow() + model.getPrctileWeightHeightLow());
									model.setPrctileWeightHeightNormal(
											cnt.getPrctileWeightHeightNormal() + model.getPrctileWeightHeightNormal());
									model.setPrctileWeightObesity(
											cnt.getPrctileWeightObesity() + model.getPrctileWeightObesity());
								}

								values = gradeCntMap.values();
								for (MedicalExaminationCount cnt : values) {
									if (cnt.getTotalPerson() > 0 && cnt.getCheckPerson() > 0) {
										cnt.setCheckPercent(cnt.getCheckPerson() * 1.0D / cnt.getTotalPerson());
										cnt.setMeanHeightLowPercent(
												cnt.getMeanHeightLow() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanHeightNormalPercent(
												cnt.getMeanHeightNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanOverWeightPercent(
												cnt.getMeanOverWeight() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanWeightLowPercent(
												cnt.getMeanWeightLow() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanWeightNormalPercent(
												cnt.getMeanWeightNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanWeightObesityPercent(
												cnt.getMeanWeightObesity() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanWeightSkinnyPercent(
												cnt.getMeanWeightSkinny() * 1.0D / cnt.getCheckPerson());

										cnt.setPrctileHeightAgeLowPercent(
												cnt.getPrctileHeightAgeLow() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileHeightAgeNormalPercent(
												cnt.getPrctileHeightAgeNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileOverWeightPercent(
												cnt.getPrctileOverWeight() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightAgeLowPercent(
												cnt.getPrctileWeightAgeLow() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightAgeNormalPercent(
												cnt.getPrctileWeightAgeNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightHeightLowPercent(
												cnt.getPrctileWeightHeightLow() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightHeightNormalPercent(
												cnt.getPrctileWeightHeightNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightObesityPercent(
												cnt.getPrctileWeightObesity() * 1.0D / cnt.getCheckPerson());
									}
								}

								if (database != null) {
									Environment.setCurrentSystemName(database.getName());
								}
								medicalExaminationCountService.saveAll(tenantId, "G", values);

								values.clear();
								gradeCntMap.clear();

								/**
								 * 汇总班级级的数据
								 */

								for (MedicalExaminationCount cnt : tenantList) {
									if (cnt.getCheckPerson() <= 3) {
										continue;
									}
									if (cnt.getYear() < cnt.getGradeYear()) {
										continue;
									}
									String iKey = cnt.getGradeId() + "_" + cnt.getType() + "_" + cnt.getYear() + "_"
											+ cnt.getMonth();
									MedicalExaminationCount model = gradeCntMap.get(iKey);
									if (model == null) {
										model = new MedicalExaminationCount();
										model.setTenantId(tenantId);
										model.setGradeId(cnt.getGradeId());
										model.setGradeYear(cnt.getGradeYear());
										model.setYear(cnt.getYear());
										model.setMonth(cnt.getMonth());
										model.setTargetType("C");
										model.setType(cnt.getType());
										gradeCntMap.put(iKey, model);
									}
									model.setCheckPerson(cnt.getCheckPerson() + model.getCheckPerson());
									model.setTotalPerson(cnt.getTotalPerson() + model.getTotalPerson());
									model.setTotalMale(cnt.getTotalMale() + model.getTotalMale());
									model.setTotalFemale(cnt.getTotalFemale() + model.getTotalFemale());

									model.setMeanHeightLow(cnt.getMeanHeightLow() + model.getMeanHeightLow());
									model.setMeanHeightNormal(cnt.getMeanHeightNormal() + model.getMeanHeightNormal());
									model.setMeanOverWeight(cnt.getMeanOverWeight() + model.getMeanOverWeight());
									model.setMeanWeightLow(cnt.getMeanWeightLow() + model.getMeanWeightLow());
									model.setMeanWeightNormal(cnt.getMeanWeightNormal() + model.getMeanWeightNormal());
									model.setMeanWeightObesity(
											cnt.getMeanWeightObesity() + model.getMeanWeightObesity());
									model.setMeanWeightSkinny(cnt.getMeanWeightSkinny() + model.getMeanWeightSkinny());

									model.setPrctileHeightAgeLow(
											cnt.getPrctileHeightAgeLow() + model.getPrctileHeightAgeLow());
									model.setPrctileHeightAgeNormal(
											cnt.getPrctileHeightAgeNormal() + model.getPrctileHeightAgeNormal());
									model.setPrctileOverWeight(
											cnt.getPrctileOverWeight() + model.getPrctileOverWeight());
									model.setPrctileWeightAgeLow(
											cnt.getPrctileWeightAgeLow() + model.getPrctileWeightAgeLow());
									model.setPrctileWeightAgeNormal(
											cnt.getPrctileWeightAgeNormal() + model.getPrctileWeightAgeNormal());
									model.setPrctileWeightHeightLow(
											cnt.getPrctileWeightHeightLow() + model.getPrctileWeightHeightLow());
									model.setPrctileWeightHeightNormal(
											cnt.getPrctileWeightHeightNormal() + model.getPrctileWeightHeightNormal());
									model.setPrctileWeightObesity(
											cnt.getPrctileWeightObesity() + model.getPrctileWeightObesity());
								}

								values = gradeCntMap.values();
								for (MedicalExaminationCount cnt : values) {
									if (cnt.getTotalPerson() > 0 && cnt.getCheckPerson() > 0) {
										cnt.setCheckPercent(cnt.getCheckPerson() * 1.0D / cnt.getTotalPerson());
										cnt.setMeanHeightLowPercent(
												cnt.getMeanHeightLow() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanHeightNormalPercent(
												cnt.getMeanHeightNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanOverWeightPercent(
												cnt.getMeanOverWeight() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanWeightLowPercent(
												cnt.getMeanWeightLow() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanWeightNormalPercent(
												cnt.getMeanWeightNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanWeightObesityPercent(
												cnt.getMeanWeightObesity() * 1.0D / cnt.getCheckPerson());
										cnt.setMeanWeightSkinnyPercent(
												cnt.getMeanWeightSkinny() * 1.0D / cnt.getCheckPerson());

										cnt.setPrctileHeightAgeLowPercent(
												cnt.getPrctileHeightAgeLow() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileHeightAgeNormalPercent(
												cnt.getPrctileHeightAgeNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileOverWeightPercent(
												cnt.getPrctileOverWeight() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightAgeLowPercent(
												cnt.getPrctileWeightAgeLow() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightAgeNormalPercent(
												cnt.getPrctileWeightAgeNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightHeightLowPercent(
												cnt.getPrctileWeightHeightLow() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightHeightNormalPercent(
												cnt.getPrctileWeightHeightNormal() * 1.0D / cnt.getCheckPerson());
										cnt.setPrctileWeightObesityPercent(
												cnt.getPrctileWeightObesity() * 1.0D / cnt.getCheckPerson());
									}
									cnt.setProvinceId(tenant.getProvinceId());
									cnt.setCityId(tenant.getCityId());
									cnt.setAreaId(tenant.getAreaId());
								}

								if (database != null) {
									Environment.setCurrentSystemName(database.getName());
								}
								medicalExaminationCountService.saveAll(tenantId, "C", values);

								values.clear();
								gradeCntMap.clear();
								tenantList.clear();
							}
						}

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
			if (list != null) {
				list.clear();
				list = null;
			}
			if (values != null) {
				values.clear();
				values = null;
			}
			if (tenantList != null) {
				tenantList.clear();
				tenantList = null;
			}
			if (persons != null) {
				persons.clear();
				persons = null;
			}
		}
	}

	public void executeAll() {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		SysTenantService sysTenantService = ContextFactory.getBean("sysTenantService");
		ForkJoinPool pool = ForkJoinPool.commonPool();
		MedicalExaminationEvaluateAction action = null;
		Database database = null;
		try {
			database = databaseService.getDatabaseByMapping("etl");
			if (database != null) {
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
			}

			SysTenantQuery query = new SysTenantQuery();
			query.locked(0);
			List<SysTenant> tenants = sysTenantService.list(query);
			if (tenants != null && !tenants.isEmpty()) {
				logger.info("开始并行任务......");
				for (SysTenant tenant : tenants) {
					action = new MedicalExaminationEvaluateAction(tenant.getTenantId());
					pool.submit(action);
				}
			}

			// 线程阻塞，等待所有任务完成
			try {
				pool.awaitTermination(2, TimeUnit.SECONDS);
			} catch (InterruptedException ex) {
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			pool.shutdown();
			logger.info("并行任务已经结束。");
		}
	}

}
