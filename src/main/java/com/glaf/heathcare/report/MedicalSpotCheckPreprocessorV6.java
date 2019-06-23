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
import com.glaf.heathcare.domain.MedicalSpotCheckAggregationModel;
import com.glaf.heathcare.domain.MedicalSpotCheckTotalModel;
import com.glaf.heathcare.helper.MedicalExaminationEvaluateHelper;
import com.glaf.heathcare.query.MedicalSpotCheckQuery;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalSpotCheckService;

public class MedicalSpotCheckPreprocessorV6 implements IReportPreprocessor {
	protected final static Log logger = LogFactory.getLog(MedicalSpotCheckPreprocessorV6.class);

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
		logger.debug("--------------------MedicalSpotCheckPreprocessorV6--------");
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
						// logger.debug(gs.getHeight());
						gsMap.put(gs.getHeight() + "_" + gs.getSex() + "_" + gs.getType(), gs);
					} else {
						gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
					}
				}
			}
			String useMethod = ParamUtils.getString(parameter, "useMethod");
			MedicalExaminationEvaluateHelper helper = new MedicalExaminationEvaluateHelper();
			int total = 0;
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
					total++;
				}
			}

			logger.debug("合法记录总数:" + total);

			String sex = ParamUtils.getString(parameter, "sex");
			if (StringUtils.equals(sex, "1")) {
				parameter.put("sex", "男生");
			} else {
				parameter.put("sex", "女生");
			}

			Map<String, MedicalSpotCheckAggregationModel> totalMap = new TreeMap<String, MedicalSpotCheckAggregationModel>();
			for (MedicalSpotCheck exam : list) {
				if (StringUtils.isEmpty(exam.getNation())) {
					continue;
				}
				MedicalSpotCheckAggregationModel model = totalMap.get(exam.getNation());
				if (model == null) {
					model = new MedicalSpotCheckAggregationModel();
					model.setKey(exam.getNation());
					totalMap.put(exam.getNation(), model);
				}

				MedicalSpotCheckTotalModel m1 = model.getModel1();
				this.populate(exam, m1, "W/A");// W/A年龄别体重

				MedicalSpotCheckTotalModel m2 = model.getModel2();
				this.populate(exam, m2, "H/A");// H/A年龄别身高

				MedicalSpotCheckTotalModel m3 = model.getModel3();
				this.populate(exam, m3, "W/H");// W/H身高别体重

				totalMap.put(exam.getNation(), model);
			}

			List<MedicalSpotCheckAggregationModel> dataList = new ArrayList<MedicalSpotCheckAggregationModel>();
			Collection<MedicalSpotCheckAggregationModel> rows = totalMap.values();
			for (MedicalSpotCheckAggregationModel model : rows) {
				if (model.getModel1().getRecordTotal() + model.getModel2().getRecordTotal()
						+ model.getModel3().getRecordTotal() > 0) {
					model.setSubTotal1(model.getModel1().getRecordTotal());
					model.setSubTotal2(model.getModel2().getRecordTotal());
					model.setSubTotal3(model.getModel3().getRecordTotal());
					dataList.add(model);
				}
			}
			parameter.put("dataList", dataList);
		}
	}

	protected void populate(MedicalSpotCheck exam, MedicalSpotCheckTotalModel model, String checkType) {
		if (StringUtils.equals(checkType, "W/A")) {// W/A年龄别体重
			switch (exam.getWeightLevel()) {
			case -3:
				model.addRecord();
				model.addTotla(exam.getWeight());
				model.addNegative3D(exam.getWeight());
				model.setNegative3DQty(model.getNegative3DQty() + 1);
				break;
			case -2:
				model.addRecord();
				model.addTotla(exam.getWeight());
				model.addNegative2D(exam.getWeight());
				model.setNegative2DQty(model.getNegative2DQty() + 1);
				break;
			case -1:
				model.addRecord();
				model.addTotla(exam.getWeight());
				model.addNegative1D(exam.getWeight());
				model.setNegative1DQty(model.getNegative1DQty() + 1);
				break;
			case 0:
				model.addRecord();
				model.addTotla(exam.getWeight());
				model.addNormal(exam.getWeight());
				model.setNormalQty(model.getNormalQty() + 1);
				break;
			case 1:
				model.addRecord();
				model.addTotla(exam.getWeight());
				model.addPositive1D(exam.getWeight());
				model.setPositive1DQty(model.getPositive1DQty() + 1);
				break;
			case 2:
				model.addRecord();
				model.addTotla(exam.getWeight());
				model.addPositive2D(exam.getWeight());
				model.setPositive2DQty(model.getPositive2DQty() + 1);
				break;
			case 3:
				model.addRecord();
				model.addTotla(exam.getWeight());
				model.addPositive3D(exam.getWeight());
				model.setPositive3DQty(model.getPositive3DQty() + 1);
				break;
			default:
				model.addRecord();
				model.setRecordTotal(model.getRecordTotal() + 1);
				model.addTotla(exam.getWeight());
				// model.setNormalQty(model.getNormalQty() + 1);
				break;
			}
		} else if (StringUtils.equals(checkType, "H/A")) {// H/A年龄别身高
			switch (exam.getHeightLevel()) {
			case -3:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addNegative3D(exam.getHeight());
				model.setNegative3DQty(model.getNegative3DQty() + 1);
				break;
			case -2:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addNegative2D(exam.getHeight());
				model.setNegative2DQty(model.getNegative2DQty() + 1);
				break;
			case -1:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addNegative1D(exam.getHeight());
				model.setNegative1DQty(model.getNegative1DQty() + 1);
				break;
			case 0:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addNormal(exam.getHeight());
				model.setNormalQty(model.getNormalQty() + 1);
				break;
			case 1:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addPositive1D(exam.getHeight());
				model.setPositive1DQty(model.getPositive1DQty() + 1);
				break;
			case 2:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addPositive2D(exam.getHeight());
				model.setPositive2DQty(model.getPositive2DQty() + 1);
				break;
			case 3:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addPositive3D(exam.getHeight());
				model.setPositive3DQty(model.getPositive3DQty() + 1);
				break;
			default:
				model.addRecord();
				model.addTotla(exam.getHeight());
				// model.setNormalQty(model.getNormalQty() + 1);
				break;
			}
		} else if (StringUtils.equals(checkType, "W/H")) {// W/H身高别体重
			switch (exam.getWeightHeightLevel()) {
			case -3:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addNegative3D(exam.getHeight());
				model.setNegative3DQty(model.getNegative3DQty() + 1);
				break;
			case -2:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addNegative2D(exam.getHeight());
				model.setNegative2DQty(model.getNegative2DQty() + 1);
				// logger.debug("************************************");
				break;
			case -1:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addNegative1D(exam.getHeight());
				model.setNegative1DQty(model.getNegative1DQty() + 1);
				break;
			case 0:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addNormal(exam.getHeight());
				model.setNormalQty(model.getNormalQty() + 1);
				break;
			case 1:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addPositive1D(exam.getHeight());
				model.setPositive1DQty(model.getPositive1DQty() + 1);
				// logger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				break;
			case 2:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addPositive2D(exam.getHeight());
				model.setPositive2DQty(model.getPositive2DQty() + 1);
				break;
			case 3:
				model.addRecord();
				model.addTotla(exam.getHeight());
				model.addPositive3D(exam.getHeight());
				model.setPositive3DQty(model.getPositive3DQty() + 1);
				break;
			default:
				model.addRecord();
				model.addTotla(exam.getHeight());
				// model.setNormalQty(model.getNormalQty() + 1);
				break;
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
