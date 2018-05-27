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

package com.glaf.heathcare.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationCount;
import com.glaf.heathcare.domain.Person;

public class MedicalExaminationHelper {

	/**
	 * 均值离差法
	 * 
	 * @param gsMap
	 * @param medicalExamination
	 */
	public void evaluate(Map<String, GrowthStandard> gsMap, MedicalExamination medicalExamination) {
		if (medicalExamination.getHeight() > 0 && medicalExamination.getWeight() > 0) {
			double BMI = medicalExamination.getWeight()
					/ (medicalExamination.getHeight() * medicalExamination.getHeight() / 10000D);
			medicalExamination.setBmi(BMI);
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_5");// BMI
			if (gs != null) {
				if (BMI > gs.getThreeDSDeviation()) {
					medicalExamination.setBmiIndex(3);
					medicalExamination.setBmiEvaluate("严重肥胖");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff0000; font:bold 12px 微软雅黑;'>严重肥胖</span>");
				} else if (BMI > gs.getTwoDSDeviation() && BMI <= gs.getThreeDSDeviation()) {
					medicalExamination.setBmiIndex(2);
					medicalExamination.setBmiEvaluate("肥胖");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>肥胖</span>");
				} else if (BMI > gs.getOneDSDeviation() && BMI <= gs.getTwoDSDeviation()) {
					medicalExamination.setBmiIndex(1);
					medicalExamination.setBmiEvaluate("超重");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>超重</span>");
				} else if (BMI < gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setBmiIndex(-3);
					medicalExamination.setBmiEvaluate("消瘦");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff0000; font:bold 12px 微软雅黑;'>消瘦</span>");
				} else if (BMI < gs.getNegativeTwoDSDeviation() && BMI >= gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setBmiIndex(-2);
					medicalExamination.setBmiEvaluate("低体重");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>低体重</span>");
				} else if (BMI < gs.getNegativeOneDSDeviation() && BMI >= gs.getNegativeTwoDSDeviation()) {
					medicalExamination.setBmiIndex(-1);
					medicalExamination.setBmiEvaluate("低体重");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>低体重</span>");
				} else {
					medicalExamination.setBmiIndex(0);
					medicalExamination.setBmiEvaluate("正常");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>正常</span>");
				}
			} else {
				// throw new RuntimeException("growth standard not config");
			}
		}

		if (medicalExamination.getHeight() > 0) {
			double height = medicalExamination.getHeight();
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_2");// H/A年龄别身高
			if (gs != null) {
				if (height > gs.getThreeDSDeviation()) {
					medicalExamination.setHeightLevel(3);
					medicalExamination.setHeightEvaluate("上");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>上</span>");
				} else if (height > gs.getTwoDSDeviation() && height <= gs.getThreeDSDeviation()) {
					medicalExamination.setHeightLevel(2);
					medicalExamination.setHeightEvaluate("中上");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>中上</span>");
				} else if (height > gs.getOneDSDeviation() && height <= gs.getTwoDSDeviation()) {
					medicalExamination.setHeightLevel(1);
					medicalExamination.setHeightEvaluate("中+");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>中+</span>");
				} else if (height < gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setHeightLevel(-3);
					medicalExamination.setHeightEvaluate("重度生长迟缓");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#ff0000; font:bold 12px 微软雅黑;'>重度生长迟缓</span>");
				} else if (height < gs.getNegativeTwoDSDeviation() && height >= gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setHeightLevel(-2);
					medicalExamination.setHeightEvaluate("中度生长迟缓");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>中度生长迟缓</span>");
				} else if (height < gs.getNegativeOneDSDeviation() && height >= gs.getNegativeTwoDSDeviation()) {
					medicalExamination.setHeightLevel(-1);
					medicalExamination.setHeightEvaluate("生长迟缓");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>生长迟缓</span>");
				} else {
					medicalExamination.setHeightLevel(0);
					medicalExamination.setHeightEvaluate("正常");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>正常</span>");
				}
			}
		}

		if (medicalExamination.getWeight() > 0) {
			double weight = medicalExamination.getWeight();
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_3");// W/A年龄别体重
			if (gs != null) {
				if (weight > gs.getThreeDSDeviation()) {
					medicalExamination.setWeightLevel(3);
					medicalExamination.setWeightEvaluate("严重肥胖");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff0000; font:bold 12px 微软雅黑;'>严重肥胖</span>");
				} else if (weight > gs.getTwoDSDeviation() && weight <= gs.getThreeDSDeviation()) {
					medicalExamination.setWeightLevel(2);
					medicalExamination.setWeightEvaluate("肥胖");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>肥胖</span>");
				} else if (weight > gs.getOneDSDeviation() && weight <= gs.getTwoDSDeviation()) {
					medicalExamination.setWeightLevel(1);
					medicalExamination.setWeightEvaluate("超重");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>超重</span>");
				} else if (weight < gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setWeightLevel(-3);
					medicalExamination.setWeightEvaluate("消瘦");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>消瘦</span>");
				} else if (weight < gs.getNegativeTwoDSDeviation() && weight >= gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setWeightLevel(-2);
					medicalExamination.setWeightEvaluate("低体重");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>低体重</span>");
				} else if (weight < gs.getNegativeOneDSDeviation() && weight >= gs.getNegativeTwoDSDeviation()) {
					medicalExamination.setWeightLevel(-1);
					medicalExamination.setWeightEvaluate("低体重");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>低体重</span>");
				} else {
					medicalExamination.setWeightLevel(0);
					medicalExamination.setWeightEvaluate("正常");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>正常</span>");
				}
			}
		}

		if (medicalExamination.getWeight() > 0 && medicalExamination.getHeight() > 0) {
			int height = (int) Math.round(medicalExamination.getHeight());
			double weight = medicalExamination.getWeight();
			GrowthStandard gs = gsMap.get(height + "_" + medicalExamination.getSex() + "_4");// W/H身高别体重
			if (gs != null) {
				if (weight > gs.getThreeDSDeviation()) {
					medicalExamination.setWeightHeightLevel(3);
					medicalExamination.setWeightHeightEvaluate("重度肥胖");
					medicalExamination.setWeightHeightEvaluateHtml(
							"<span style='color:#ff0000; font:bold 12px 微软雅黑;'>重度肥胖</span>");
				} else if (weight > gs.getTwoDSDeviation() && weight <= gs.getThreeDSDeviation()) {
					medicalExamination.setWeightHeightLevel(2);
					medicalExamination.setWeightHeightEvaluate("中度肥胖");
					medicalExamination.setWeightHeightEvaluateHtml(
							"<span style='color:#ff6666; font:bold 12px 微软雅黑;'>中度肥胖</span>");
				} else if (weight > gs.getOneDSDeviation() && weight <= gs.getTwoDSDeviation()) {
					medicalExamination.setWeightLevel(1);
					medicalExamination.setWeightEvaluate("轻度肥胖");
					medicalExamination.setWeightHeightEvaluateHtml(
							"<span style='color:#ff9900; font:bold 12px 微软雅黑;'>轻度肥胖</span>");
				} else if (weight > gs.getMedian() && weight <= gs.getOneDSDeviation()) {
					medicalExamination.setWeightLevel(1);
					medicalExamination.setWeightEvaluate("超重");
					medicalExamination
							.setWeightHeightEvaluateHtml("<span style='color:#ffff00; font:bold 12px 微软雅黑;'>超重</span>");
				} else if (weight < gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setWeightHeightLevel(-3);
					medicalExamination.setWeightHeightEvaluate("消瘦");
					medicalExamination
							.setWeightHeightEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>消瘦</span>");
				} else if (weight < gs.getNegativeTwoDSDeviation() && weight >= gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setWeightLevel(-2);
					medicalExamination.setWeightEvaluate("体重低下");
					medicalExamination.setWeightHeightEvaluateHtml(
							"<span style='color:#ff9900; font:bold 12px 微软雅黑;'>体重低下</span>");
				} else if (weight < gs.getNegativeOneDSDeviation() && weight >= gs.getNegativeTwoDSDeviation()) {
					medicalExamination.setWeightHeightLevel(-1);
					medicalExamination.setWeightHeightEvaluate("偏瘦");
					medicalExamination
							.setWeightHeightEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>偏瘦</span>");
				} else {
					medicalExamination.setWeightHeightLevel(0);
					medicalExamination.setWeightHeightEvaluate("正常");
					medicalExamination
							.setWeightHeightEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>正常</span>");
				}
			}
		}
	}

	/**
	 * 百分位数评价法
	 * 
	 * @param gsMap
	 * @param medicalExamination
	 */
	public void evaluateByPrctile(Map<String, GrowthStandard> gsMap, MedicalExamination medicalExamination) {
		if (medicalExamination.getHeight() > 0 && medicalExamination.getWeight() > 0) {
			double BMI = medicalExamination.getWeight()
					/ (medicalExamination.getHeight() * medicalExamination.getHeight() / 10000D);
			medicalExamination.setBmi(BMI);
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_4");// W/H身高别体重
			if (gs != null) {
				if (BMI > gs.getPercent97()) {
					medicalExamination.setBmiIndex(3);
					medicalExamination.setBmiEvaluate("肥胖");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>肥胖</span>");
				} else if (BMI > gs.getPercent85() && BMI <= gs.getPercent97()) {
					medicalExamination.setBmiIndex(2);
					medicalExamination.setBmiEvaluate("超重");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>超重</span>");
				} else if (BMI < gs.getPercent3()) {
					medicalExamination.setBmiIndex(-3);
					medicalExamination.setBmiEvaluate("消瘦");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>消瘦</span>");
				} else if (BMI < gs.getPercent15() && BMI >= gs.getPercent3()) {
					medicalExamination.setBmiIndex(-2);
					medicalExamination.setBmiEvaluate("低体重");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>低体重</span>");
				} else {
					medicalExamination.setBmiIndex(0);
					medicalExamination.setBmiEvaluate("正常");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>正常</span>");
				}
			}
		}

		if (medicalExamination.getHeight() > 0) {
			double index = medicalExamination.getHeight();
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_2");// H/A年龄别身高
			if (gs != null) {
				if (index > gs.getPercent97()) {
					medicalExamination.setHeightLevel(3);
					medicalExamination.setHeightEvaluate("上");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#ff0000; font:bold 12px 微软雅黑;'>上</span>");
				} else if (index > gs.getPercent85() && index <= gs.getPercent97()) {
					medicalExamination.setHeightLevel(2);
					medicalExamination.setHeightEvaluate("中上");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#ff0000; font:bold 12px 微软雅黑;'>中上</span>");
				} else if (index < gs.getPercent3()) {
					medicalExamination.setHeightLevel(-3);
					medicalExamination.setHeightEvaluate("下");
					medicalExamination.setBmiEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>下</span>");
				} else if (index < gs.getPercent15() && index >= gs.getPercent3()) {
					medicalExamination.setHeightLevel(-2);
					medicalExamination.setHeightEvaluate("生长迟缓");
					medicalExamination
							.setBmiEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>生长迟缓</span>");
				} else {
					medicalExamination.setHeightLevel(0);
					medicalExamination.setHeightEvaluate("正常");
					medicalExamination
							.setHeightEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>正常</span>");
				}
			}
		}

		if (medicalExamination.getWeight() > 0) {
			double index = medicalExamination.getWeight();
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_3");// W/A年龄别体重
			if (gs != null) {
				if (index > gs.getPercent97()) {
					medicalExamination.setWeightLevel(3);
					medicalExamination.setWeightEvaluate("肥胖");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>肥胖</span>");
				} else if (index > gs.getPercent85() && index <= gs.getPercent97()) {
					medicalExamination.setWeightLevel(2);
					medicalExamination.setWeightEvaluate("超重");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>超重</span>");
				} else if (index < gs.getPercent3()) {
					medicalExamination.setWeightLevel(-3);
					medicalExamination.setWeightEvaluate("消瘦");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>消瘦</span>");
				} else if (index < gs.getPercent15() && index >= gs.getPercent3()) {
					medicalExamination.setWeightLevel(-2);
					medicalExamination.setWeightEvaluate("低体重");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#ff9900; font:bold 12px 微软雅黑;'>低体重</span>");
				} else {
					medicalExamination.setWeightLevel(0);
					medicalExamination.setWeightEvaluate("正常");
					medicalExamination
							.setWeightEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>正常</span>");
				}
			}
		}

		if (medicalExamination.getWeight() > 0 && medicalExamination.getHeight() > 0) {
			int height = (int) Math.round(medicalExamination.getHeight());
			double index = medicalExamination.getWeight();
			GrowthStandard gs = gsMap.get(height + "_" + medicalExamination.getSex() + "_4");// W/A身高别体重
			if (gs != null) {
				if (index > gs.getPercent97()) {
					medicalExamination.setWeightHeightLevel(3);
					medicalExamination.setWeightHeightEvaluate("重度肥胖");
					medicalExamination.setWeightHeightEvaluateHtml(
							"<span style='color:#ff9900; font:bold 12px 微软雅黑;'>重度肥胖</span>");
				} else if (index > gs.getPercent85() && index <= gs.getPercent97()) {
					medicalExamination.setWeightHeightLevel(2);
					medicalExamination.setWeightHeightEvaluate("超重");
					medicalExamination
							.setWeightHeightEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>超重</span>");
				} else if (index < gs.getPercent3()) {
					medicalExamination.setWeightHeightLevel(-3);
					medicalExamination.setWeightHeightEvaluate("消瘦");
					medicalExamination
							.setWeightHeightEvaluateHtml("<span style='color:#ff6666; font:bold 12px 微软雅黑;'>消瘦</span>");
				} else if (index < gs.getPercent15() && index >= gs.getPercent3()) {
					medicalExamination.setWeightLevel(-2);
					medicalExamination.setWeightEvaluate("低体重");
					medicalExamination.setWeightHeightEvaluateHtml(
							"<span style='color:#ff9900; font:bold 12px 微软雅黑;'>低体重</span>");
				} else {
					medicalExamination.setWeightHeightLevel(0);
					medicalExamination.setWeightHeightEvaluate("正常");
					medicalExamination
							.setWeightHeightEvaluateHtml("<span style='color:#339933; font:bold 12px 微软雅黑;'>正常</span>");
				}
			}
		}
	}

	public Collection<MedicalExaminationCount> populate(String tenantId, int year, int month, String type,
			List<GradeInfo> grades, List<Person> persons, List<MedicalExamination> list, List<GrowthStandard> standards,
			Map<String, Integer> personCountMap) {
		Map<String, MedicalExaminationCount> examMap = new LinkedHashMap<String, MedicalExaminationCount>();
		Collection<MedicalExaminationCount> result = new ArrayList<MedicalExaminationCount>();

		Map<String, Person> personMap = new HashMap<String, Person>();
		if (persons != null && !persons.isEmpty()) {
			for (Person person : persons) {
				if (person.getDeleteFlag() == 0) {
					personMap.put(person.getId(), person);
				}
			}
		}

		if (grades != null && !grades.isEmpty()) {
			for (GradeInfo gradeInfo : grades) {
				MedicalExaminationCount cnt = examMap.get(gradeInfo.getId());
				if (cnt == null) {
					cnt = new MedicalExaminationCount();
					cnt.setTenantId(tenantId);
					cnt.setGradeId(gradeInfo.getId());
					cnt.setGradeName(gradeInfo.getName());
					cnt.setGradeYear(gradeInfo.getYear());

					if (persons != null && !persons.isEmpty()) {
						for (Person p : persons) {
							if (StringUtils.equals(p.getGradeId(), gradeInfo.getId())) {
								cnt.setTotalPerson(cnt.getTotalPerson() + 1);
							}
							if (StringUtils.equals(p.getSex(), "1")) {
								cnt.setTotalMale(cnt.getTotalMale() + 1);
							} else {
								cnt.setTotalFemale(cnt.getTotalFemale() + 1);
							}
						}
					}
					examMap.put(gradeInfo.getId(), cnt);
				}
			}
		}

		if (list != null && !list.isEmpty()) {
			Person person = null;

			Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
			if (standards != null && !standards.isEmpty()) {
				for (GrowthStandard gs : standards) {
					gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
				}
			}

			for (MedicalExamination exam : list) {
				if (exam.getYear() != year) {
					continue;
				}
				if (exam.getMonth() != month) {
					continue;
				}
				if (!StringUtils.equals(exam.getType(), type)) {
					continue;
				}
				person = personMap.get(exam.getPersonId());
				if (person != null) {
					MedicalExaminationCount cnt = examMap.get(person.getGradeId());
					if (cnt != null) {
						cnt.setCheckPerson(cnt.getCheckPerson() + 1);// 实检人数

						this.evaluate(gsMap, exam);// 均值离差法

						if (exam.getBmiIndex() >= 2) {
							cnt.setMeanWeightObesity(cnt.getMeanWeightObesity() + 1);// 肥胖人数
						} else if (exam.getBmiIndex() == 1.0) {
							cnt.setMeanOverWeight(cnt.getMeanOverWeight() + 1);// 超重人数
						} else if (exam.getBmiIndex() <= -1) {
							cnt.setMeanWeightSkinny(cnt.getMeanWeightSkinny() + 1);// 消瘦人数
						} else {
							cnt.setMeanWeightNormal(cnt.getMeanWeightNormal() + 1);// 体重正常人数
						}

						if (exam.getWeightLevel() <= -2) {
							cnt.setMeanWeightLow(cnt.getMeanWeightLow() + 1);// 体重低于2SD人数
						}

						if (exam.getHeightLevel() <= -2) {
							cnt.setMeanHeightLow(cnt.getMeanHeightLow() + 1);// 身高低于2SD人数
						}

						if (exam.getHeightLevel() >= 0) {
							cnt.setMeanHeightNormal(cnt.getMeanHeightNormal() + 1);// 身高正常人数
						}

						this.evaluateByPrctile(gsMap, exam);// 百分位数评价法

						if (exam.getBmiIndex() > 2) {
							cnt.setPrctileWeightObesity(cnt.getPrctileWeightObesity() + 1);// 肥胖人数
						} else if (exam.getBmiIndex() == 2) {
							cnt.setPrctileOverWeight(cnt.getPrctileOverWeight() + 1);// 超重人数
						} else if (exam.getBmiIndex() == -3) {
							cnt.setPrctileWeightHeightLow(cnt.getPrctileWeightHeightLow() + 1);// W/H小于P3人数(身高别体重)
						} else if (exam.getBmiIndex() == 0) {
							cnt.setPrctileWeightHeightNormal(cnt.getPrctileWeightHeightNormal() + 1);// W/H正常人数(身高别体重)
						}

						if (exam.getWeightLevel() <= -2) {
							cnt.setPrctileWeightAgeLow(cnt.getPrctileWeightAgeLow() + 1); // W/A小于P3人数(年龄别体重)
						}

						if (exam.getWeightLevel() == 0) {
							cnt.setPrctileWeightAgeNormal(cnt.getPrctileWeightAgeNormal() + 1); // W/A正常人数(年龄别体重)
						}

						if (exam.getHeightLevel() <= -2) {
							cnt.setPrctileHeightAgeLow(cnt.getPrctileHeightAgeLow() + 1); // H/A小于P3人数(年龄别身高)
						}

						if (exam.getHeightLevel() >= 0) {
							cnt.setPrctileHeightAgeNormal(cnt.getPrctileHeightAgeNormal() + 1);// H/A正常人数(年龄别身高)
						}
					}
				}
			}

			Collection<MedicalExaminationCount> countList = examMap.values();

			for (MedicalExaminationCount cnt : countList) {
				if (cnt.getTotalPerson() > 0 && cnt.getCheckPerson() > 0) {
					if (personCountMap.get(cnt.getGradeId()) != null) {
						cnt.setTotalPerson(personCountMap.get(cnt.getGradeId()));// 替换为实际在册人数
					}
					cnt.setCheckPercent(cnt.getCheckPerson() * 1.0D / cnt.getTotalPerson());
					cnt.setMeanHeightLowPercent(cnt.getMeanHeightLow() * 1.0D / cnt.getCheckPerson());
					cnt.setMeanHeightNormalPercent(cnt.getMeanHeightNormal() * 1.0D / cnt.getCheckPerson());
					cnt.setMeanOverWeightPercent(cnt.getMeanOverWeight() * 1.0D / cnt.getCheckPerson());
					cnt.setMeanWeightLowPercent(cnt.getMeanWeightLow() * 1.0D / cnt.getCheckPerson());
					cnt.setMeanWeightNormalPercent(cnt.getMeanWeightNormal() * 1.0D / cnt.getCheckPerson());
					cnt.setMeanWeightObesityPercent(cnt.getMeanWeightObesity() * 1.0D / cnt.getCheckPerson());
					cnt.setMeanWeightSkinnyPercent(cnt.getMeanWeightSkinny() * 1.0D / cnt.getCheckPerson());

					cnt.setPrctileHeightAgeLowPercent(cnt.getPrctileHeightAgeLow() * 1.0D / cnt.getCheckPerson());
					cnt.setPrctileHeightAgeNormalPercent(cnt.getPrctileHeightAgeNormal() * 1.0D / cnt.getCheckPerson());
					cnt.setPrctileOverWeightPercent(cnt.getPrctileOverWeight() * 1.0D / cnt.getCheckPerson());
					cnt.setPrctileWeightAgeLowPercent(cnt.getPrctileWeightAgeLow() * 1.0D / cnt.getCheckPerson());
					cnt.setPrctileWeightAgeNormalPercent(cnt.getPrctileWeightAgeNormal() * 1.0D / cnt.getCheckPerson());
					cnt.setPrctileWeightHeightLowPercent(cnt.getPrctileWeightHeightLow() * 1.0D / cnt.getCheckPerson());
					cnt.setPrctileWeightHeightNormalPercent(
							cnt.getPrctileWeightHeightNormal() * 1.0D / cnt.getCheckPerson());
					cnt.setPrctileWeightObesityPercent(cnt.getPrctileWeightObesity() * 1.0D / cnt.getCheckPerson());
					result.add(cnt);
				}
			}

		}

		return result;
	}
}
