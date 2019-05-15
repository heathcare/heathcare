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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalSpotCheck;
import com.glaf.heathcare.helper.MedicalExaminationEvaluateHelper;
import com.glaf.heathcare.query.MedicalSpotCheckQuery;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalSpotCheckService;

public class MedicalSpotCheckEvaluateBean {
	protected static final Log logger = LogFactory.getLog(MedicalSpotCheckEvaluateBean.class);

	public void execute(String userId, String method) {
		GrowthStandardService growthStandardService = ContextFactory
				.getBean("com.glaf.heathcare.service.growthStandardService");
		MedicalSpotCheckService medicalSpotCheckService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalSpotCheckService");

		MedicalExaminationEvaluateHelper helper = new MedicalExaminationEvaluateHelper();
		List<MedicalSpotCheck> exams = null;
		try {
			MedicalSpotCheckQuery query2 = new MedicalSpotCheckQuery();
			query2.actorId(userId);
			exams = medicalSpotCheckService.list(query2);
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
				logger.debug("GrowthStandard map:" + gsMap.size());
				for (MedicalSpotCheck exam : exams) {
					if (exam.getAgeOfTheMoon() > 0 && exam.getHeight() > 0 && exam.getWeight() > 0) {
						if (StringUtils.contains(exam.getSex(), "男")) {
							exam.setSex("1");
						} else {
							exam.setSex("0");
						}
						if (StringUtils.equals(method, "prctile")) {
							helper.evaluateByPrctile(gsMap, exam);
						} else {
							helper.evaluate(gsMap, exam);
							// logger.debug(""+exam.toJsonObject().toJSONString());
						}
					}
				}
				medicalSpotCheckService.updateAll(exams);
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			if (exams != null) {
				exams.clear();
				exams = null;
			}
		}
	}

}
