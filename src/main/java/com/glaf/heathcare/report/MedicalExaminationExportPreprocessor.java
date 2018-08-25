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
package com.glaf.heathcare.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.report.IReportPreprocessor;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;

public class MedicalExaminationExportPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(Tenant tenant, Map<String, Object> params) {
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");

		MedicalExaminationQuery query = new MedicalExaminationQuery();
		query.tenantId(tenant.getTenantId());

		Map<String, String> gradeMap = new HashMap<String, String>();
		List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(tenant.getTenantId());
		if (grades != null && !grades.isEmpty()) {
			List<String> gradeIds = new ArrayList<String>();
			for (GradeInfo grade : grades) {
				gradeIds.add(grade.getId());
				gradeMap.put(grade.getId(), grade.getName());
			}
			query.gradeIds(gradeIds);
		}

		List<Person> persons = personService.getTenantPersons(tenant.getTenantId());
		Map<String, Person> personMap = new HashMap<String, Person>();
		if (persons != null && !persons.isEmpty()) {
			for (Person person : persons) {
				personMap.put(person.getId(), person);
			}
		}

		List<MedicalExamination> exams = medicalExaminationService.list(query);
		if (exams != null && !exams.isEmpty()) {
			Person person = null;
			for (MedicalExamination exam : exams) {

				person = personMap.get(exam.getPersonId());
				if (person == null) {
					continue;
				}

				if (gradeMap.get(person.getGradeId()) != null) {
					exam.setGradeName(gradeMap.get(person.getGradeId()));
				}

				exam.setName(person.getName());
				if (person.getBirthday() != null) {
					exam.setBirthdayString(DateUtils.getDate(person.getBirthday()));
				}
				if (StringUtils.equals(person.getSex(), "1")) {
					exam.setSex("男");
				} else {
					exam.setSex("女");
				}

				if (StringUtils.isNotEmpty(exam.getEyeLeftRemark())) {
					exam.setEyeLeft(exam.getEyeLeftRemark());
				} else {
					if (StringUtils.equals(exam.getEyeLeft(), "Y")) {
						exam.setEyeLeft("正常");
					} else if (StringUtils.equals(exam.getEyeLeft(), "T")) {
						exam.setEyeLeft("沙眼");
					} else if (StringUtils.equals(exam.getEyeLeft(), "T")) {
						exam.setEyeLeft("弱视");
					}
				}

				if (StringUtils.isNotEmpty(exam.getEyeRightRemark())) {
					exam.setEyeRight(exam.getEyeRightRemark());
				} else {
					if (StringUtils.equals(exam.getEyeRight(), "Y")) {
						exam.setEyeRight("正常");
					} else if (StringUtils.equals(exam.getEyeRight(), "T")) {
						exam.setEyeRight("沙眼");
					} else if (StringUtils.equals(exam.getEyeRight(), "T")) {
						exam.setEyeRight("弱视");
					}
				}

				if (StringUtils.isNotEmpty(exam.getEarLeftRemark())) {
					exam.setEarLeft(exam.getEarLeftRemark());
				} else {
					if (StringUtils.equals(exam.getEarLeft(), "Y")) {
						exam.setEarLeft("正常");
					} else if (StringUtils.equals(exam.getEarLeft(), "N")) {
						exam.setEarLeft("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getEarRightRemark())) {
					exam.setEarRight(exam.getEarRightRemark());
				} else {
					if (StringUtils.equals(exam.getEarRight(), "Y")) {
						exam.setEarRight("正常");
					} else if (StringUtils.equals(exam.getEarRight(), "N")) {
						exam.setEarRight("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getHeadRemark())) {
					exam.setHead(exam.getHeadRemark());
				} else {
					if (StringUtils.equals(exam.getHead(), "Y")) {
						exam.setHead("正常");
					} else if (StringUtils.equals(exam.getHead(), "N")) {
						exam.setHead("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getThoraxRemark())) {
					exam.setThorax(exam.getThoraxRemark());
				} else {
					if (StringUtils.equals(exam.getThorax(), "Y")) {
						exam.setThorax("正常");
					} else if (StringUtils.equals(exam.getThorax(), "N")) {
						exam.setThorax("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getSpineRemark())) {
					exam.setSpine(exam.getSpineRemark());
				} else {
					if (StringUtils.equals(exam.getSpine(), "Y")) {
						exam.setSpine("正常");
					} else if (StringUtils.equals(exam.getSpine(), "N")) {
						exam.setSpine("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getPharyngealRemark())) {
					exam.setPharyngeal(exam.getPharyngealRemark());
				} else {
					if (StringUtils.equals(exam.getPharyngeal(), "Y")) {
						exam.setPharyngeal("正常");
					} else if (StringUtils.equals(exam.getPharyngeal(), "N")) {
						exam.setPharyngeal("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getCardiopulmonaryRemark())) {
					exam.setCardiopulmonary(exam.getCardiopulmonaryRemark());
				} else {
					if (StringUtils.equals(exam.getCardiopulmonary(), "Y")) {
						exam.setCardiopulmonary("正常");
					} else if (StringUtils.equals(exam.getCardiopulmonary(), "N")) {
						exam.setCardiopulmonary("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getHepatolienalRemark())) {
					exam.setHepatolienal(exam.getHepatolienalRemark());
				} else {
					if (StringUtils.equals(exam.getHepatolienal(), "Y")) {
						exam.setHepatolienal("正常");
					} else if (StringUtils.equals(exam.getHepatolienal(), "N")) {
						exam.setHepatolienal("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getPharyngealRemark())) {
					exam.setPharyngeal(exam.getPharyngealRemark());
				} else {
					if (StringUtils.equals(exam.getPharyngeal(), "Y")) {
						exam.setPharyngeal("正常");
					} else if (StringUtils.equals(exam.getPharyngeal(), "N")) {
						exam.setPharyngeal("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getPudendumRemark())) {
					exam.setPudendum(exam.getPudendumRemark());
				} else {
					if (StringUtils.equals(exam.getPudendum(), "Y")) {
						exam.setPudendum("正常");
					} else if (StringUtils.equals(exam.getPudendum(), "N")) {
						exam.setPudendum("异常");
					}
				}

				if (StringUtils.isNotEmpty(exam.getSkinRemark())) {
					exam.setSkin(exam.getSkinRemark());
				} else {
					if (StringUtils.equals(exam.getSkin(), "Y")) {
						exam.setSkin("正常");
					} else if (StringUtils.equals(exam.getSkin(), "N")) {
						exam.setSkin("异常");
					}
				}
			}
			params.put("rows", exams);
		}
	}

}
