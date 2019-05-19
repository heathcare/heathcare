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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
		MedicalSpotCheckQuery query = new MedicalSpotCheckQuery();
		Tools.populate(query, parameter);
		query.deleteFlag(0);
		query.actorId(Authentication.getAuthenticatedActorId());

		List<MedicalSpotCheck> list = getMedicalSpotCheckService().list(query);
		if (list != null && !list.isEmpty()) {
			List<GrowthStandard> standards = getGrowthStandardService().getAllGrowthStandards();
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
			String useMethod = ParamUtils.getString(parameter, "useMethod");
			MedicalExaminationEvaluateHelper helper = new MedicalExaminationEvaluateHelper();
			for (MedicalSpotCheck exam : list) {
				if (exam.getAgeOfTheMoon() > 0 && exam.getHeight() > 0 && exam.getWeight() > 0) {
					if (StringUtils.contains(exam.getSex(), "男")) {
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

			Map<Integer, MedicalSpotCheckTotalModel> totalMap = new HashMap<Integer, MedicalSpotCheckTotalModel>();
			for (MedicalSpotCheck exam : list) {
				MedicalSpotCheckTotalModel model = totalMap.get(exam.getAgeOfTheMoon());
				if (model == null) {
					model = new MedicalSpotCheckTotalModel();
					model.setAgeOfTheMoon(exam.getAgeOfTheMoon());
					totalMap.put(exam.getAgeOfTheMoon(), model);
				}

				if (StringUtils.equals(checkType, "W/A")) {// W/A年龄别体重
					switch (exam.getWeightLevel()) {
					case -3:
						model.addNegative3D(exam.getWeight());
						break;
					case -2:
						model.addNegative2D(exam.getWeight());
						break;
					case -1:
						model.addNegative1D(exam.getWeight());
						break;
					case 1:
						model.addPositive1D(exam.getWeight());
						break;
					case 2:
						model.addPositive2D(exam.getWeight());
						break;
					case 3:
						model.addPositive3D(exam.getWeight());
						break;
					default:
						model.addNormal(exam.getWeight());
						break;
					}
				} else if (StringUtils.equals(checkType, "H/A")) {// H/A年龄别身高
					switch (exam.getHeightLevel()) {
					case -3:
						model.addNegative3D(exam.getHeight());
						break;
					case -2:
						model.addNegative2D(exam.getHeight());
						break;
					case -1:
						model.addNegative1D(exam.getHeight());
						break;
					case 1:
						model.addPositive1D(exam.getHeight());
						break;
					case 2:
						model.addPositive2D(exam.getHeight());
						break;
					case 3:
						model.addPositive3D(exam.getHeight());
						break;
					default:
						model.addNormal(exam.getHeight());
						break;
					}
				} else if (StringUtils.equals(checkType, "W/H")) {// W/H身高别体重
					switch (exam.getWeightHeightLevel()) {
					case -3:
						model.addNegative3D(exam.getWeightHeightPercent());
						break;
					case -2:
						model.addNegative2D(exam.getWeightHeightPercent());
						break;
					case -1:
						model.addNegative1D(exam.getWeightHeightPercent());
						break;
					case 1:
						model.addPositive1D(exam.getWeightHeightPercent());
						break;
					case 2:
						model.addPositive2D(exam.getWeightHeightPercent());
						break;
					case 3:
						model.addPositive3D(exam.getWeightHeightPercent());
						break;
					default:
						model.addNormal(exam.getWeightHeightPercent());
						break;
					}
				}
			}

			parameter.put("dataList", totalMap.values());

		}
	}

	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
	}

	public void setMedicalSpotCheckService(MedicalSpotCheckService medicalSpotCheckService) {
		this.medicalSpotCheckService = medicalSpotCheckService;
	}

}
