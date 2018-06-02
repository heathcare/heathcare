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

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.heathcare.domain.PersonInfo;
import com.glaf.heathcare.report.IReportPreprocessor;
import com.glaf.heathcare.service.PersonInfoService;

public class TenantDietaryNutritionAvgQuantityPreprocessor implements IReportPreprocessor {

	protected static final Log logger = LogFactory.getLog(TenantDietaryNutritionAvgQuantityPreprocessor.class);

	@Override
	public void prepare(Tenant tenant, Map<String, Object> params) {
		PersonInfoService personInfoService = ContextFactory.getBean("com.glaf.heathcare.service.personInfoService");
		List<PersonInfo> list = personInfoService.getPersonInfos(tenant.getTenantId());
		if (list != null && !list.isEmpty()) {
			int person3 = 0;
			int person4 = 0;
			int person5 = 0;
			int person6 = 0;
			for (PersonInfo pinfo : list) {
				switch (pinfo.getAge()) {
				case 3:
					person3 = person3 + pinfo.getMale() + pinfo.getFemale();
					break;
				case 4:
					person4 = person4 + pinfo.getMale() + pinfo.getFemale();
					break;
				case 5:
					person5 = person5 + pinfo.getMale() + pinfo.getFemale();
					break;
				case 6:
					person6 = person6 + pinfo.getMale() + pinfo.getFemale();
					break;
				default:
					break;
				}
			}

			params.put("person3", person3);
			params.put("person4", person4);
			params.put("person5", person5);
			params.put("person6", person6);
			params.put("total", person3 + person4 + person5 + person6);
			logger.debug("total:" + params.get("total"));
		}
	}

}
