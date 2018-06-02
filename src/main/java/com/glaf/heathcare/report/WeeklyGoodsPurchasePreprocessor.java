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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.FoodCompositionService;

public class WeeklyGoodsPurchasePreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(Tenant tenant, Map<String, Object> params) {
		String tenantId = tenant.getTenantId();
		Date startDate = ParamUtils.getDate(params, "startDate");
		Date endDate = ParamUtils.getDate(params, "endDate");
		if (startDate != null && endDate != null) {
			int startFullDay = DateUtils.getYearMonthDay(startDate);
			int endFullDay = DateUtils.getYearMonthDay(endDate);

			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(
					" select E.GOODSID_ as goodsid, E.QUANTITY_ as quantity, E.PRICE_ as price, E.TOTALPRICE_ as totalprice, E.PURCHASE_TIME_ as purchasetime from GOODS_PURCHASE")
					.append(String.valueOf(IdentityFactory.getTenantHash(tenantId))).append(" E ")
					.append(" where ( E.TENANTID_ = '").append(tenantId).append("' ) ").append(" and ( E.FULLDAY_ >=")
					.append(startFullDay).append(" ) and ( E.FULLDAY_ <= ").append(endFullDay)
					.append(" ) and ( E.BUSINESSSTATUS_ = 9 ) and E.PURCHASE_TIME_ is not null ")
					.append(" order by E.FULLDAY_ asc ");
			ITablePageService tablePageService = ContextFactory.getBean("tablePageService");
			FoodCompositionService foodCompositionService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodCompositionService");
			FoodCompositionQuery query = new FoodCompositionQuery();
			query.locked(0);
			List<FoodComposition> foods = foodCompositionService.list(query);
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food);
			}
			List<Map<String, Object>> list = tablePageService.getListData(sqlBuffer.toString(), params);
			List<GoodsPurchase> rows = new ArrayList<GoodsPurchase>();
			if (list != null && !list.isEmpty()) {
				FoodComposition food = null;
				for (Map<String, Object> dataMap : list) {
					GoodsPurchase m = new GoodsPurchase();
					m.setGoodsId(ParamUtils.getLong(dataMap, "goodsid"));
					m.setGoodsName(ParamUtils.getString(dataMap, "goodsname"));
					m.setQuantity(ParamUtils.getDouble(dataMap, "quantity"));
					m.setPrice(ParamUtils.getDouble(dataMap, "price"));
					m.setTotalPrice(ParamUtils.getDouble(dataMap, "totalprice"));
					m.setPurchaseTime(ParamUtils.getDate(dataMap, "purchasetime"));
					if (foodMap.get(m.getGoodsId()) != null) {
						food = foodMap.get(m.getGoodsId());
						m.setGoodsName(food.getName());
						m.setGoodsNodeId(food.getNodeId());
						rows.add(m);
					}
				}
			}

			if (!rows.isEmpty()) {
				double total1 = 0;
				double total2 = 0;
				double total3 = 0;
				double total4 = 0;
				double total5 = 0;

				double totalPrice1 = 0;
				double totalPrice2 = 0;
				double totalPrice3 = 0;
				double totalPrice4 = 0;
				double totalPrice5 = 0;

				String day1 = null;
				String day2 = null;
				String day3 = null;
				String day4 = null;
				String day5 = null;

				int week = 0;

				List<GoodsPurchase> rows1 = new ArrayList<GoodsPurchase>();
				List<GoodsPurchase> rows2 = new ArrayList<GoodsPurchase>();
				List<GoodsPurchase> rows3 = new ArrayList<GoodsPurchase>();
				List<GoodsPurchase> rows4 = new ArrayList<GoodsPurchase>();
				List<GoodsPurchase> rows5 = new ArrayList<GoodsPurchase>();
				List<GoodsPurchase> rows7 = new ArrayList<GoodsPurchase>();

				java.util.Calendar calendar = java.util.Calendar.getInstance();
				java.util.Map<Long, Double> day1Map = new HashMap<Long, Double>();
				java.util.Map<Long, Double> day2Map = new HashMap<Long, Double>();
				java.util.Map<Long, Double> day3Map = new HashMap<Long, Double>();
				java.util.Map<Long, Double> day4Map = new HashMap<Long, Double>();
				java.util.Map<Long, Double> day5Map = new HashMap<Long, Double>();

				for (GoodsPurchase m : rows) {
					if (week == 0) {
						week = m.getWeek();
					}

					calendar.setTime(m.getPurchaseTime());
					int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
					switch (dayOfWeek) {
					case Calendar.MONDAY:
						rows1.add(m);
						total1 = total1 + m.getQuantity();
						totalPrice1 = totalPrice1 + m.getTotalPrice();
						if (day1 == null) {
							day1 = DateUtils.getDate(m.getPurchaseTime());
						}
						day1Map.put(m.getGoodsId(), m.getQuantity());
						break;
					case Calendar.TUESDAY:
						rows2.add(m);
						total2 = total2 + m.getQuantity();
						totalPrice2 = totalPrice2 + m.getTotalPrice();
						if (day2 == null) {
							day2 = DateUtils.getDate(m.getPurchaseTime());
						}
						day2Map.put(m.getGoodsId(), m.getQuantity());
						break;
					case Calendar.WEDNESDAY:
						rows3.add(m);
						total3 = total3 + m.getQuantity();
						totalPrice3 = totalPrice3 + m.getTotalPrice();
						if (day3 == null) {
							day3 = DateUtils.getDate(m.getPurchaseTime());
						}
						day3Map.put(m.getGoodsId(), m.getQuantity());
						break;
					case Calendar.THURSDAY:
						rows4.add(m);
						total4 = total4 + m.getQuantity();
						totalPrice4 = totalPrice4 + m.getTotalPrice();
						if (day4 == null) {
							day4 = DateUtils.getDate(m.getPurchaseTime());
						}
						day4Map.put(m.getGoodsId(), m.getQuantity());
						break;
					case Calendar.FRIDAY:
						rows5.add(m);
						total5 = total5 + m.getQuantity();
						totalPrice5 = totalPrice5 + m.getTotalPrice();
						if (day5 == null) {
							day5 = DateUtils.getDate(m.getPurchaseTime());
						}
						day5Map.put(m.getGoodsId(), m.getQuantity());
						break;
					default:
						break;
					}
				}

				List<Integer> sortNos = new ArrayList<Integer>();
				for (int i = 1; i <= 30; i++) {
					sortNos.add(i);
				}

				params.put("sortNos", sortNos);
				params.put("totalQty", rows.size());
				params.put("week", week);

				params.put("day1", day1);
				params.put("day2", day2);
				params.put("day3", day3);
				params.put("day4", day4);
				params.put("day5", day5);

				params.put("qty1", day1Map.keySet().size());
				params.put("qty2", day2Map.keySet().size());
				params.put("qty3", day3Map.keySet().size());
				params.put("qty4", day4Map.keySet().size());
				params.put("qty5", day5Map.keySet().size());

				params.put("weight1", total1);
				params.put("weight2", total2);
				params.put("weight3", total3);
				params.put("weight4", total4);
				params.put("weight5", total5);

				double totalPrice = totalPrice1 + totalPrice2 + totalPrice3 + totalPrice4 + totalPrice5;

				params.put("totalPrice1", totalPrice1);
				params.put("totalPrice2", totalPrice2);
				params.put("totalPrice3", totalPrice3);
				params.put("totalPrice4", totalPrice4);
				params.put("totalPrice5", totalPrice5);
				params.put("totalPrice", totalPrice);

				params.put("totalPriceString1", totalPrice1);
				params.put("totalPriceString2", totalPrice2);
				params.put("totalPriceString3", totalPrice3);
				params.put("totalPriceString4", totalPrice4);
				params.put("totalPriceString5", totalPrice5);
				params.put("totalPriceString", totalPrice);

				int size = rows1.size();
				for (int i = 0; i < 30 - size; i++) {
					GoodsPurchase m = new GoodsPurchase();
					rows1.add(m);
				}

				size = rows2.size();
				for (int i = 0; i < 30 - size; i++) {
					GoodsPurchase m = new GoodsPurchase();
					rows2.add(m);
				}

				size = rows3.size();
				for (int i = 0; i < 30 - size; i++) {
					GoodsPurchase m = new GoodsPurchase();
					rows3.add(m);
				}

				size = rows4.size();
				for (int i = 0; i < 30 - size; i++) {
					GoodsPurchase m = new GoodsPurchase();
					rows4.add(m);
				}

				size = rows5.size();
				for (int i = 0; i < 30 - size; i++) {
					GoodsPurchase m = new GoodsPurchase();
					rows5.add(m);
				}

				for (int i = 0; i < 30; i++) {
					GoodsPurchase m = new GoodsPurchase();
					rows7.add(m);
				}

				params.put("rows1", rows1);
				params.put("rows2", rows2);
				params.put("rows3", rows3);
				params.put("rows4", rows4);
				params.put("rows5", rows5);
				params.put("rows7", rows7);
			}
		}
	}

}
