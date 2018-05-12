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

import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonTransferSchool;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.service.PersonTransferSchoolService;

public class PersonTransferSchoolPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(Tenant tenant, int year, int month, Map<String, Object> params) {
		PersonTransferSchoolService personTransferSchoolService = ContextFactory
				.getBean("com.glaf.heathcare.service.personTransferSchoolService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		String id = ParamUtils.getString(params, "pid");
		if (StringUtils.isNotEmpty(id)) {
			PersonTransferSchool model = personTransferSchoolService.getPersonTransferSchool(id);
			if (model != null) {
				Person person = personService.getPerson(model.getPersonId());
				if (person != null) {
					params.put("name", person.getName());
					params.put("previousHistory", person.getPreviousHistory());
					params.put("fromSchool", model.getFromSchool());
					params.put("toSchool", model.getToSchool());
					params.put("checkResult", model.getCheckResult());

					if ("1".equals(person.getSex())) {
						params.put("sex", "男");
					} else {
						params.put("sex", "女");
					}
					if (person.getBirthday() != null) {
						SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
						params.put("birthday", f.format(person.getBirthday()));
					}
					if (model.getLeaveDate() != null) {
						SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
						params.put("leaveDate", f.format(model.getLeaveDate()));
						params.put("leavedate", f.format(model.getLeaveDate()));
					}

				}
			}
		}
	}

}
