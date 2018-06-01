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

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;

import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.domain.GoodsCount;
import com.glaf.heathcare.domain.GoodsCountList;
import com.glaf.heathcare.domain.GoodsInStock;
import com.glaf.heathcare.domain.GoodsOutStock;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.query.GoodsInStockQuery;
import com.glaf.heathcare.query.GoodsOutStockQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsActualQuantityService;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsOutStockService;

public class GoodsCountPreprocessor implements IReportPreprocessor {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void prepare(Tenant tenant, int year, int month, Map<String, Object> params) {
		params.put("year", year);
		params.put("month", month);
		String tenantId = tenant.getTenantId();
		Date startTime = ParamUtils.getDate(params, "startTime");
		Date endTime = ParamUtils.getDate(params, "endTime");

		if (startTime != null && endTime != null) {
			FoodCompositionService foodCompositionService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodCompositionService");
			GoodsInStockService goodsInStockService = ContextFactory
					.getBean("com.glaf.heathcare.service.goodsInStockService");
			GoodsOutStockService goodsOutStockService = ContextFactory
					.getBean("com.glaf.heathcare.service.goodsOutStockService");
			GoodsActualQuantityService goodsActualQuantityService = ContextFactory
					.getBean("com.glaf.heathcare.service.goodsActualQuantityService");
			FoodCompositionQuery query = new FoodCompositionQuery();
			query.locked(0);
			List<FoodComposition> foods = foodCompositionService.list(query);
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food);
			}

			Map<String, GoodsCountList> goodsMap = new TreeMap<String, GoodsCountList>();

			GoodsInStockQuery q1 = new GoodsInStockQuery();
			q1.tenantId(tenantId);
			q1.businessStatus(9);
			if (startTime != null) {
				q1.inStockTimeGreaterThanOrEqual(startTime);
			}
			if (endTime != null) {
				q1.inStockTimeLessThanOrEqual(endTime);
			}
			q1.setOrderBy(" E.GOODSID_ asc ");
			List<GoodsInStock> rows1 = goodsInStockService.list(q1);
			if (rows1 != null && !rows1.isEmpty()) {
				FoodComposition food = null;
				for (GoodsInStock m : rows1) {
					food = foodMap.get(m.getGoodsId());
					if (food != null) {
						GoodsCountList cntList = goodsMap.get(DateUtils.getDate(m.getInStockTime()));
						if (cntList == null) {
							cntList = new GoodsCountList();
							cntList.setKey(DateUtils.getDate(m.getInStockTime()));
							goodsMap.put(cntList.getKey(), cntList);
						}
						GoodsCount cnt = cntList.getCountMap().get(m.getGoodsId());
						if (cnt == null) {
							cnt = new GoodsCount();
							cnt.setGoodsId(m.getGoodsId());
							cnt.setGoodsNodeId(food.getNodeId());
							cnt.setGoodsName(food.getName());
							cntList.getCountMap().put(m.getGoodsId(), cnt);
						}
						cnt.setInstockPrice(m.getPrice());
						cnt.setInstockQuantity(cnt.getInstockQuantity() + m.getQuantity());
					}
				}
			}

			GoodsOutStockQuery q2 = new GoodsOutStockQuery();
			q2.tenantId(tenantId);
			q2.businessStatus(9);
			if (startTime != null) {
				q2.outStockTimeGreaterThanOrEqual(startTime);
			}
			if (endTime != null) {
				q2.outStockTimeLessThanOrEqual(endTime);
			}
			q2.setOrderBy(" E.GOODSID_ asc ");
			List<GoodsOutStock> rows2 = goodsOutStockService.list(q2);
			if (rows2 != null && !rows2.isEmpty()) {
				FoodComposition food = null;
				for (GoodsOutStock m : rows2) {
					food = foodMap.get(m.getGoodsId());
					if (food != null) {
						GoodsCountList cntList = goodsMap.get(DateUtils.getDate(m.getOutStockTime()));
						if (cntList == null) {
							cntList = new GoodsCountList();
							cntList.setKey(DateUtils.getDate(m.getOutStockTime()));
							goodsMap.put(cntList.getKey(), cntList);
						}
						GoodsCount cnt = cntList.getCountMap().get(m.getGoodsId());
						if (cnt == null) {
							cnt = new GoodsCount();
							cnt.setGoodsId(m.getGoodsId());
							cnt.setGoodsNodeId(food.getNodeId());
							cnt.setGoodsName(food.getName());
							cntList.getCountMap().put(m.getGoodsId(), cnt);
						}
						cnt.setOutstockQuantity(cnt.getOutstockQuantity() + m.getQuantity());
					}
				}
			}

			GoodsActualQuantityQuery q4 = new GoodsActualQuantityQuery();
			q4.tenantId(tenantId);
			q4.businessStatus(9);
			q4.setOrderBy(" E.GOODSID_ asc ");
			if (startTime != null) {
				q4.usageTimeGreaterThanOrEqual(startTime);
			}
			if (endTime != null) {
				q4.usageTimeLessThanOrEqual(endTime);
			}
			List<GoodsActualQuantity> rows4 = goodsActualQuantityService.list(q4);
			if (rows4 != null && !rows4.isEmpty()) {
				FoodComposition food = null;
				for (GoodsActualQuantity m : rows4) {
					food = foodMap.get(m.getGoodsId());
					if (food != null) {
						GoodsCountList cntList = goodsMap.get(DateUtils.getDate(m.getUsageTime()));
						if (cntList == null) {
							cntList = new GoodsCountList();
							cntList.setKey(DateUtils.getDate(m.getUsageTime()));
							goodsMap.put(cntList.getKey(), cntList);
						}
						GoodsCount cnt = cntList.getCountMap().get(m.getGoodsId());
						if (cnt == null) {
							cnt = new GoodsCount();
							cnt.setGoodsId(m.getGoodsId());
							cnt.setGoodsNodeId(food.getNodeId());
							cnt.setGoodsName(food.getName());

							cntList.getCountMap().put(m.getGoodsId(), cnt);
						}
						cnt.setUsePrice(m.getPrice());
						cnt.setUseQuantity(cnt.getUseQuantity() + m.getQuantity());
					}
				}
			}

			logger.debug("days:" + goodsMap.values().size());
			params.put("rows", goodsMap.values());
			params.put("startDate", DateUtils.getDateTime("yyyy年MM月dd日", startTime));
			params.put("endDate", DateUtils.getDateTime("yyyy年MM月dd日", endTime));
		}
	}

}
