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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.identity.Tenant;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.bean.MedicalExaminationGradeCountBean;
import com.glaf.heathcare.bean.PhysicalGrowthCountBean;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GradePersonRelation;
import com.glaf.heathcare.domain.MedicalExaminationGradeCount;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PhysicalGrowthCount;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.GradePersonRelationQuery;
import com.glaf.heathcare.query.MedicalExaminationGradeCountQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.query.PhysicalGrowthCountQuery;
import com.glaf.heathcare.report.IReportPreprocessor;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GradePersonRelationService;
import com.glaf.heathcare.service.MedicalExaminationGradeCountService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.service.PhysicalGrowthCountService;

public class TenantMedicalExaminationGradeCountPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(Tenant tenant, Map<String, Object> params) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		GradePersonRelationService gradePersonRelationService = ContextFactory
				.getBean("com.glaf.heathcare.service.gradePersonRelationService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationGradeCountService medicalExaminationGradeCountService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationGradeCountService");
		PhysicalGrowthCountService physicalGrowthCountService = ContextFactory
				.getBean("com.glaf.heathcare.service.physicalGrowthCountService");
		String tenantId = tenant.getTenantId();
		GradeInfoQuery query0 = new GradeInfoQuery();
		query0.tenantId(tenantId);
		query0.locked(0);
		query0.deleteFlag(0);
		List<GradeInfo> grades = gradeInfoService.list(query0);

		if (grades != null && !grades.isEmpty()) {
			List<String> gradeIds = new ArrayList<String>();
			Map<String, GradeInfo> gradeMap = new HashMap<String, GradeInfo>();
			Map<String, Integer> gradePersonMap = new HashMap<String, Integer>();
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
			List<Person> persons = personService.list(query);
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

				int year = ParamUtils.getInt(params, "year");
				int month = ParamUtils.getInt(params, "month");
				String type = ParamUtils.getString(params, "type");

				Map<String, Integer> cntMap = gradePersonRelationService.getPersonCountMap(tenantId, year, month);
				gradePersonMap.putAll(cntMap);// 替换为实际在册人数

				GradePersonRelationQuery qx = new GradePersonRelationQuery();
				qx.tenantId(tenantId);
				qx.year(year);
				qx.month(month);
				List<GradePersonRelation> list0 = gradePersonRelationService
						.selectGradePersonRelationCountGroupBySex(qx);
				Map<String, Integer> maleCntMap = new HashMap<String, Integer>();
				Map<String, Integer> femaleCntMap = new HashMap<String, Integer>();
				for (GradePersonRelation rel : list0) {
					if (rel.getCount() > 0) {
						if ("1".equals(rel.getSex())) {
							maleCntMap.put(rel.getGradeId(), rel.getCount());
						} else {
							femaleCntMap.put(rel.getGradeId(), rel.getCount());
						}
					}
				}

				String systemName = Environment.getCurrentSystemName();
				List<MedicalExaminationGradeCount> list = null;
				Database database = null;
				try {

					MedicalExaminationGradeCountBean bean = new MedicalExaminationGradeCountBean();
					bean.execute(tenantId, type, year, month);

					PhysicalGrowthCountBean bean2 = new PhysicalGrowthCountBean();
					bean2.execute(tenantId, type, year, month);

					MedicalExaminationGradeCountQuery query2 = new MedicalExaminationGradeCountQuery();
					query2.tenantId(tenantId);
					query2.type(type);
					query2.year(year);
					query2.month(month);

					Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
					database = databaseService.getDatabaseByMapping("etl");
					if (database != null) {
						Environment.setCurrentSystemName(database.getName());
					}
					list = medicalExaminationGradeCountService.list(query2);
				} catch (Exception ex) {
					// ex.printStackTrace();
					throw new RuntimeException(ex);
				} finally {
					com.glaf.core.config.Environment.setCurrentSystemName(systemName);
				}

				if (list != null && !list.isEmpty()) {
					MedicalExaminationGradeCount total = new MedicalExaminationGradeCount();

					for (MedicalExaminationGradeCount cnt : list) {
						cnt.setGradeName(gradeMap.get(cnt.getGradeId()).getName());

						if (maleCntMap.get(cnt.getGradeId()) != null) {
							cnt.setMale(maleCntMap.get(cnt.getGradeId()));
						}
						if (femaleCntMap.get(cnt.getGradeId()) != null) {
							cnt.setFemale(femaleCntMap.get(cnt.getGradeId()));
						}

						if (cnt.getMale() > 0 && cnt.getFemale() > 0) {
							cnt.setPersonCount(cnt.getMale() + cnt.getFemale());
						} else {
							cnt.setPersonCount(gradePersonMap.get(cnt.getGradeId()));
						}

						cnt.setCheckPercent(cnt.getCheckPerson() * 100D / cnt.getPersonCount());
						cnt.setAnemiaCheckNormalPercent(cnt.getAnemiaCheckNormal() * 100.0D / cnt.getCheckPerson());

						total.setPersonCount(total.getPersonCount() + cnt.getPersonCount());
						total.setCheckPerson(total.getCheckPerson() + cnt.getCheckPerson());
						total.setMale(total.getMale() + cnt.getMale());
						total.setFemale(total.getFemale() + cnt.getFemale());
						total.setWeightLow(total.getWeightLow() + cnt.getWeightLow());
						total.setWeightSkinny(total.getWeightSkinny() + cnt.getWeightSkinny());
						total.setWeightRetardation(total.getWeightRetardation() + cnt.getWeightRetardation());
						total.setWeightSevereMalnutrition(
								total.getWeightSevereMalnutrition() + cnt.getWeightSevereMalnutrition());
						total.setOverWeight(total.getOverWeight() + cnt.getOverWeight());
						total.setWeightObesityLow(total.getWeightObesityLow() + cnt.getWeightObesityLow());
						total.setWeightObesityMid(total.getWeightObesityMid() + cnt.getWeightObesityMid());
						total.setWeightObesityHigh(total.getWeightObesityHigh() + cnt.getWeightObesityHigh());
						total.setAnemiaCheckNormal(total.getAnemiaCheckNormal() + cnt.getAnemiaCheckNormal());
						total.setAnemiaLow(total.getAnemiaLow() + cnt.getAnemiaLow());
						total.setAnemiaHigh(total.getAnemiaHigh() + cnt.getAnemiaHigh());
						total.setAnemiaCheck(total.getCheckPerson());
					}

					PhysicalGrowthCountQuery query3 = new PhysicalGrowthCountQuery();
					query3.tenantId(tenantId);
					query3.type(type);
					query3.year(year);
					query3.month(month);

					if (database != null) {
						Environment.setCurrentSystemName(database.getName());
					}
					List<PhysicalGrowthCount> list3 = physicalGrowthCountService.list(query3);

					if (list3 != null && !list3.isEmpty()) {
						PhysicalGrowthCount haCount = new PhysicalGrowthCount();
						PhysicalGrowthCount waCount = new PhysicalGrowthCount();
						PhysicalGrowthCount whCount = new PhysicalGrowthCount();

						for (MedicalExaminationGradeCount master : list) {
							for (PhysicalGrowthCount child : list3) {
								if (StringUtils.equals(master.getGradeId(), child.getGradeId())) {
									if (StringUtils.equals(child.getItem(), "H/A")) {
										master.setHaCount(child);
										haCount.setLevel1(haCount.getLevel1() + child.getLevel1());
										haCount.setLevel2(haCount.getLevel2() + child.getLevel2());
										haCount.setLevel3(haCount.getLevel3() + child.getLevel3());
										haCount.setLevel5(haCount.getLevel5() + child.getLevel5());
										haCount.setLevel7(haCount.getLevel7() + child.getLevel7());
										haCount.setLevel8(haCount.getLevel8() + child.getLevel8());
										haCount.setLevel9(haCount.getLevel9() + child.getLevel9());
										haCount.setNormal(haCount.getNormal() + child.getNormal());
									} else if (StringUtils.equals(child.getItem(), "W/A")) {
										master.setWaCount(child);
										waCount.setLevel1(waCount.getLevel1() + child.getLevel1());
										waCount.setLevel2(waCount.getLevel2() + child.getLevel2());
										waCount.setLevel3(waCount.getLevel3() + child.getLevel3());
										waCount.setLevel5(waCount.getLevel5() + child.getLevel5());
										waCount.setLevel7(waCount.getLevel7() + child.getLevel7());
										waCount.setLevel8(waCount.getLevel8() + child.getLevel8());
										waCount.setLevel9(waCount.getLevel9() + child.getLevel9());
										waCount.setNormal(haCount.getNormal() + child.getNormal());
									} else if (StringUtils.equals(child.getItem(), "W/H")) {
										master.setWhCount(child);
										whCount.setLevel1(whCount.getLevel1() + child.getLevel1());
										whCount.setLevel2(whCount.getLevel2() + child.getLevel2());
										whCount.setLevel3(whCount.getLevel3() + child.getLevel3());
										whCount.setLevel5(whCount.getLevel5() + child.getLevel5());
										whCount.setLevel7(whCount.getLevel7() + child.getLevel7());
										whCount.setLevel8(whCount.getLevel8() + child.getLevel8());
										whCount.setLevel9(whCount.getLevel9() + child.getLevel9());
										whCount.setNormal(whCount.getNormal() + child.getNormal());
									}
								}
							}
						}

						waCount.setLevel1Percent(waCount.getLevel1() * 100D / total.getCheckPerson());
						waCount.setLevel2Percent(waCount.getLevel2() * 100D / total.getCheckPerson());
						waCount.setLevel3Percent(waCount.getLevel3() * 100D / total.getCheckPerson());
						waCount.setLevel5Percent(waCount.getLevel5() * 100D / total.getCheckPerson());
						waCount.setLevel7Percent(waCount.getLevel7() * 100D / total.getCheckPerson());
						waCount.setLevel8Percent(waCount.getLevel8() * 100D / total.getCheckPerson());
						waCount.setLevel9Percent(waCount.getLevel9() * 100D / total.getCheckPerson());
						waCount.setNormalPercent(waCount.getNormal() * 100D / total.getCheckPerson());

						haCount.setLevel1Percent(haCount.getLevel1() * 100D / total.getCheckPerson());
						haCount.setLevel2Percent(haCount.getLevel2() * 100D / total.getCheckPerson());
						haCount.setLevel3Percent(haCount.getLevel3() * 100D / total.getCheckPerson());
						haCount.setLevel5Percent(haCount.getLevel5() * 100D / total.getCheckPerson());
						haCount.setLevel7Percent(haCount.getLevel7() * 100D / total.getCheckPerson());
						haCount.setLevel8Percent(haCount.getLevel8() * 100D / total.getCheckPerson());
						haCount.setLevel9Percent(haCount.getLevel9() * 100D / total.getCheckPerson());
						haCount.setNormalPercent(haCount.getNormal() * 100D / total.getCheckPerson());

						whCount.setLevel1Percent(whCount.getLevel1() * 100D / total.getCheckPerson());
						whCount.setLevel2Percent(whCount.getLevel2() * 100D / total.getCheckPerson());
						whCount.setLevel3Percent(whCount.getLevel3() * 100D / total.getCheckPerson());
						whCount.setLevel5Percent(whCount.getLevel5() * 100D / total.getCheckPerson());
						whCount.setLevel7Percent(whCount.getLevel7() * 100D / total.getCheckPerson());
						whCount.setLevel8Percent(whCount.getLevel8() * 100D / total.getCheckPerson());
						whCount.setLevel9Percent(whCount.getLevel9() * 100D / total.getCheckPerson());
						whCount.setNormalPercent(whCount.getNormal() * 100D / total.getCheckPerson());

						total.setWaCount(waCount);
						total.setHaCount(haCount);
						total.setWhCount(whCount);
					}

					total.setCheckPercent(total.getCheckPerson() * 100.0D / total.getPersonCount());
					total.setAnemiaCheckNormalPercent(total.getAnemiaCheckNormal() * 100.0D / total.getCheckPerson());

					params.put("total", total);
					params.put("rows", list);
				}
			}
		}
	}

}
