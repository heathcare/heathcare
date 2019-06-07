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
import com.glaf.heathcare.domain.SevenLevelMethodQuantity;
import com.glaf.heathcare.helper.MedicalExaminationEvaluateHelper;
import com.glaf.heathcare.query.MedicalSpotCheckQuery;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalSpotCheckService;

public class MedicalSpotCheckPreprocessorV4 implements IReportPreprocessor {
	protected final static Log logger = LogFactory.getLog(MedicalSpotCheckPreprocessorV4.class);

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

	protected void populate(MedicalSpotCheck exam, SevenLevelMethodQuantity model) {
		switch (exam.getWeightHeightLevel()) {
		case -3:
			model.setNegative3D(model.getNegative3D() + 1);
			break;
		case -2:
			model.setNegative2D(model.getNegative2D() + 1);
			break;
		case -1:
			model.setNegative1D(model.getNegative1D() + 1);
			break;
		case 0:
			model.setNormal(model.getNormal() + 1);
			break;
		case 1:
			model.setPositive1D(model.getPositive1D() + 1);
			break;
		case 2:
			model.setPositive2D(model.getPositive2D() + 1);
			break;
		case 3:
			model.setPositive3D(model.getPositive3D() + 1);
			break;
		default:
			model.setNormal(model.getNormal() + 1);
			break;
		}
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
		// logger.debug("list:" + list);
		if (list != null && !list.isEmpty()) {
			logger.debug("list size:" + list.size());
			List<GrowthStandard> standards = getGrowthStandardService().getAllGrowthStandards();
			Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
			if (standards != null && !standards.isEmpty()) {
				for (GrowthStandard gs : standards) {
					if (StringUtils.equals(gs.getType(), "4")) {
						// int height = (int) Math.round(gs.getHeight());
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

			String sex = ParamUtils.getString(parameter, "sex");
			if (StringUtils.equals(sex, "1")) {
				parameter.put("sex", "男生");
			} else {
				parameter.put("sex", "女生");
			}

			Map<Integer, SevenLevelMethodQuantity> totalMap = new TreeMap<Integer, SevenLevelMethodQuantity>();
			for (MedicalSpotCheck exam : list) {
				int key = (int) Math.round(exam.getHeight());
				SevenLevelMethodQuantity model = totalMap.get(key);
				if (model == null) {
					model = new SevenLevelMethodQuantity();
					model.setKey(String.valueOf(key));
					totalMap.put(key, model);
				}
				this.populate(exam, model);
				totalMap.put(key, model);
			}

			List<SevenLevelMethodQuantity> dataList = new ArrayList<SevenLevelMethodQuantity>();
			Collection<SevenLevelMethodQuantity> rows = totalMap.values();
			for (SevenLevelMethodQuantity model : rows) {
				dataList.add(model);
			}
			parameter.put("dataList", dataList);
		}
	}

	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
	}

	public void setMedicalSpotCheckService(MedicalSpotCheckService medicalSpotCheckService) {
		this.medicalSpotCheckService = medicalSpotCheckService;
	}

}
