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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;

import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsPurchasePlan;
import com.glaf.heathcare.domain.WeeklyDataModel;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsPurchasePlanQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsPurchasePlanService;

public class GoodsPurchasePlanWeeklyPreprocessor implements IReportPreprocessor {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void prepare(Tenant tenant, Map<String, Object> params) {
		int year = ParamUtils.getInt(params, "year");
		int month = ParamUtils.getInt(params, "month");
		params.put("year", year);
		params.put("month", month);
		String tenantId = tenant.getTenantId();

		FoodCompositionService foodCompositionService = ContextFactory
				.getBean("com.glaf.heathcare.service.foodCompositionService");
		GoodsPurchasePlanService goodsPurchasePlanService = ContextFactory
				.getBean("com.glaf.heathcare.service.goodsPurchasePlanService");
		FoodCompositionQuery query = new FoodCompositionQuery();
		query.locked(0);
		List<FoodComposition> foods = foodCompositionService.list(query);
		Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
		for (FoodComposition food : foods) {
			foodMap.put(food.getId(), food);
		}

		GoodsPurchasePlanQuery q = new GoodsPurchasePlanQuery();
		q.tenantId(tenantId);
		q.businessStatus(9);
		Date startTime = ParamUtils.getDate(params, "startTime");
		Date endTime = ParamUtils.getDate(params, "endTime");
		if (startTime != null) {
			q.purchaseTimeGreaterThanOrEqual(startTime);
		}
		if (endTime != null) {
			q.purchaseTimeLessThanOrEqual(endTime);
		}

		List<GoodsPurchasePlan> rows = goodsPurchasePlanService.list(q);

		if (rows != null && !rows.isEmpty()) {
			Date date = null;
			String wkName = "";
			FoodComposition food = null;
			java.util.Calendar cal = Calendar.getInstance();
			Map<Integer, WeeklyDataModel> dataMap = new TreeMap<Integer, WeeklyDataModel>();
			for (GoodsPurchasePlan m : rows) {
				WeeklyDataModel dm = dataMap.get(m.getFullDay());
				if (dm == null) {
					dm = new WeeklyDataModel();
					date = DateUtils.toDate(String.valueOf(m.getFullDay()));
					cal.setTime(date);
					int wk = cal.get(Calendar.DAY_OF_WEEK);
					switch (wk) {
					case Calendar.SUNDAY:
						wkName = "星期日";
						break;
					case Calendar.MONDAY:
						wkName = "星期一";
						break;
					case Calendar.TUESDAY:
						wkName = "星期二";
						break;
					case Calendar.WEDNESDAY:
						wkName = "星期三";
						break;
					case Calendar.THURSDAY:
						wkName = "星期四";
						break;
					case Calendar.FRIDAY:
						wkName = "星期五";
						break;
					case Calendar.SATURDAY:
						wkName = "星期六";
						break;
					}
					dm.setName(wkName + "(" + DateUtils.getDate(date) + ")");
					dataMap.put(m.getFullDay(), dm);
				}
				food = foodMap.get(m.getGoodsId());
				if (food != null) {
					m.setGoodsName(food.getName());
					dm.getDataList().add(m);
				}
			}

			int maxLength = 0;
			Iterator<Entry<Integer, WeeklyDataModel>> iterator = dataMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, WeeklyDataModel> entry = iterator.next();
				WeeklyDataModel value = entry.getValue();
				if (value.getDataList().size() > maxLength) {
					maxLength = value.getDataList().size();
				}
			}

			int len = 0;
			iterator = dataMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, WeeklyDataModel> entry = iterator.next();
				WeeklyDataModel value = entry.getValue();
				len = maxLength - value.getDataList().size();
				for (int i = 0; i < len; i++) {
					GoodsPurchasePlan plan = new GoodsPurchasePlan();
					value.getDataList().add(plan);
				}
			}

			params.put("weekList", dataMap.values());
			params.put("startDate", DateUtils.getDateTime("yyyy年MM月dd日", startTime));
			params.put("endDate", DateUtils.getDateTime("yyyy年MM月dd日", endTime));
		}

	}

}
