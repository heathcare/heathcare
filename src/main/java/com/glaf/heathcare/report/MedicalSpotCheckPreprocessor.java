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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.security.Authentication;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalSpotCheck;
import com.glaf.heathcare.domain.MedicalSpotCheckTotalModel;
import com.glaf.heathcare.helper.MedicalExaminationEvaluateHelper;
import com.glaf.heathcare.query.MedicalSpotCheckQuery;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalSpotCheckService;

public class MedicalSpotCheckPreprocessor implements IReportPreprocessor {
	protected final static Log logger = LogFactory.getLog(MedicalSpotCheckPreprocessor.class);

	protected GrowthStandardService growthStandardService;

	protected MedicalSpotCheckService medicalSpotCheckService;

	public GrowthStandardService getGrowthStandardService() {
		if (growthStandardService == null) {
			growthStandardService = ContextFactory.getBean("com.glaf.heathcare.service.growthStandardService");
		}
		return growthStandardService;
	}

	public MedicalSpotCheckService getMedicalSpotCheckService() {
		if (medicalSpotCheckService == null) {
			medicalSpotCheckService = ContextFactory.getBean("com.glaf.heathcare.service.medicalSpotCheckService");
		}
		return medicalSpotCheckService;
	}

	/**
	 * 报表预处理
	 * 
	 * @param tenant 租户信息
	 * @param params 参数
	 */
	public void prepare(Tenant tenant, Map<String, Object> parameter) {
		logger.debug("parameter:" + parameter);
		MedicalSpotCheckQuery query = new MedicalSpotCheckQuery();
		Tools.populate(query, parameter);
		query.actorId(Authentication.getAuthenticatedActorId());

		List<MedicalSpotCheck> list = getMedicalSpotCheckService().list(query);
		logger.debug("list:" + list);
		if (list != null && !list.isEmpty()) {
			List<GrowthStandard> standards = getGrowthStandardService().getAllGrowthStandards();
			Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
			if (standards != null && !standards.isEmpty()) {
				for (GrowthStandard gs : standards) {
					if (StringUtils.equals(gs.getType(), "4")) {
						//int height = (int) Math.round(gs.getHeight());
						gsMap.put(gs.getHeight() + "_" + gs.getSex() + "_" + gs.getType(), gs);
					} else {
						gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
					}
				}
			}
			String useMethod = ParamUtils.getString(parameter, "useMethod");
			MedicalExaminationEvaluateHelper helper = new MedicalExaminationEvaluateHelper();
			for (MedicalSpotCheck exam : list) {
				if (exam.getAgeOfTheMoon() > 0 && exam.getHeight() > 0 && exam.getWeight() > 0) {
					if (StringUtils.contains(exam.getSex(), "1")) {
						exam.setSex("1");
					} else {
						exam.setSex("0");
					}
					if (StringUtils.equals(useMethod, "prctile")) {
						helper.evaluateByPrctile(gsMap, exam);
					} else {
						helper.evaluate(gsMap, exam);
					}
				}
			}

			String checkType = ParamUtils.getString(parameter, "checkType");

			if (StringUtils.equals(checkType, "H/A")) {
				parameter.put("typeName", "身高");
			} else if (StringUtils.equals(checkType, "W/A")) {
				parameter.put("typeName", "体重");
			} else if (StringUtils.equals(checkType, "W/H")) {
				parameter.put("typeName", "身高别体重");
			}

			String sex = ParamUtils.getString(parameter, "sex");
			if (StringUtils.equals(sex, "1")) {
				parameter.put("sex", "男生");
			} else {
				parameter.put("sex", "女生");
			}

			Map<Integer, MedicalSpotCheckTotalModel> totalMap = new TreeMap<Integer, MedicalSpotCheckTotalModel>();
			for (MedicalSpotCheck exam : list) {
				MedicalSpotCheckTotalModel model = totalMap.get(exam.getAgeOfTheMoon());
				if (model == null) {
					model = new MedicalSpotCheckTotalModel();
					model.setAgeOfTheMoon(exam.getAgeOfTheMoon());
					totalMap.put(exam.getAgeOfTheMoon(), model);
					if (StringUtils.equals(checkType, "H/A")) {// H/A年龄别身高
						String key = exam.getAgeOfTheMoon() + "_" + exam.getSex() + "_2";
						GrowthStandard gs = gsMap.get(key);
						model.setIsoStdValue(gs.getMedian());// 中位数
					} else if (StringUtils.equals(checkType, "W/A")) {// W/A年龄别体重
						String key = exam.getAgeOfTheMoon() + "_" + exam.getSex() + "_3";
						GrowthStandard gs = gsMap.get(key);
						model.setIsoStdValue(gs.getMedian());// 中位数
					} else if (StringUtils.equals(checkType, "W/H")) {// W/H身高别体重
						String key = exam.getAgeOfTheMoon() + "_" + exam.getSex() + "_4";
						GrowthStandard gs = gsMap.get(key);
						model.setIsoStdValue(gs.getMedian());// 中位数
					}
				}

				if (StringUtils.equals(checkType, "W/A")) {// W/A年龄别体重
					switch (exam.getWeightLevel()) {
					case -3:
						model.addRecord();
						model.addTotla(exam.getWeight());
						model.addNegative3D(exam.getWeight());
						break;
					case -2:
						model.addRecord();
						model.addTotla(exam.getWeight());
						model.addNegative2D(exam.getWeight());
						break;
					case -1:
						model.addRecord();
						model.addTotla(exam.getWeight());
						model.addNegative1D(exam.getWeight());
						break;
					case 1:
						model.addRecord();
						model.addTotla(exam.getWeight());
						model.addPositive1D(exam.getWeight());
						break;
					case 2:
						model.addRecord();
						model.addTotla(exam.getWeight());
						model.addPositive2D(exam.getWeight());
						break;
					case 3:
						model.addRecord();
						model.addTotla(exam.getWeight());
						model.addPositive3D(exam.getWeight());
						break;
					default:
						model.addRecord();
						model.addTotla(exam.getWeight());
						model.addNormal(exam.getWeight());
						break;
					}
				} else if (StringUtils.equals(checkType, "H/A")) {// H/A年龄别身高
					switch (exam.getHeightLevel()) {
					case -3:
						model.addRecord();
						model.addTotla(exam.getHeight());
						model.addNegative3D(exam.getHeight());
						break;
					case -2:
						model.addRecord();
						model.addTotla(exam.getHeight());
						model.addNegative2D(exam.getHeight());
						break;
					case -1:
						model.addRecord();
						model.addTotla(exam.getHeight());
						model.addNegative1D(exam.getHeight());
						break;
					case 1:
						model.addRecord();
						model.addTotla(exam.getHeight());
						model.addPositive1D(exam.getHeight());
						break;
					case 2:
						model.addRecord();
						model.addTotla(exam.getHeight());
						model.addPositive2D(exam.getHeight());
						break;
					case 3:
						model.addRecord();
						model.addTotla(exam.getHeight());
						model.addPositive3D(exam.getHeight());
						break;
					default:
						model.addRecord();
						model.addTotla(exam.getHeight());
						model.addNormal(exam.getHeight());
						break;
					}
				} else if (StringUtils.equals(checkType, "W/H")) {// W/H身高别体重
					switch (exam.getWeightHeightLevel()) {
					case -3:
						model.addRecord();
						model.addTotla(exam.getWeightHeightPercent());
						model.addNegative3D(exam.getWeightHeightPercent());
						break;
					case -2:
						model.addRecord();
						model.addTotla(exam.getWeightHeightPercent());
						model.addNegative2D(exam.getWeightHeightPercent());
						break;
					case -1:
						model.addRecord();
						model.addTotla(exam.getWeightHeightPercent());
						model.addNegative1D(exam.getWeightHeightPercent());
						break;
					case 1:
						model.addRecord();
						model.addTotla(exam.getWeightHeightPercent());
						model.addPositive1D(exam.getWeightHeightPercent());
						break;
					case 2:
						model.addRecord();
						model.addTotla(exam.getWeightHeightPercent());
						model.addPositive2D(exam.getWeightHeightPercent());
						break;
					case 3:
						model.addRecord();
						model.addTotla(exam.getWeightHeightPercent());
						model.addPositive3D(exam.getWeightHeightPercent());
						break;
					default:
						model.addRecord();
						model.addTotla(exam.getWeightHeightPercent());
						model.addNormal(exam.getWeightHeightPercent());
						break;
					}
				}
				totalMap.put(exam.getAgeOfTheMoon(), model);
			}

			Collection<MedicalSpotCheckTotalModel> coll = totalMap.values();
			if (!coll.isEmpty()) {
				List<MedicalSpotCheckTotalModel> rows = new ArrayList<MedicalSpotCheckTotalModel>();
				for (MedicalSpotCheckTotalModel model : coll) {
					if (model.getRecordTotal() > 0) {
						model.setAvg(model.getTotalValue() / model.getRecordTotal());// 计算平均值
						model.setWhoDiffValue(model.getAvg() - model.getIsoStdValue());// 与WHO的标准值偏差
						rows.add(model);
					}
				}
				parameter.put("dataList", rows);
				logger.debug("dataList size:" + rows.size());
			}
		}
	}

	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
	}

	public void setMedicalSpotCheckService(MedicalSpotCheckService medicalSpotCheckService) {
		this.medicalSpotCheckService = medicalSpotCheckService;
	}

}
