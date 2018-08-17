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

package com.glaf.heathcare.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;

@Transactional(readOnly = true)
public interface GoodsPurchaseService {

	@Transactional
	void bulkInsert(String tenantId, List<GoodsPurchase> list);

	/**
	 * 复制某天的采购计划
	 * 
	 * @param tenantId
	 * @param fullDay
	 */
	@Transactional
	void copyPurchasePlan(String tenantId, int fullDay, String userId);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(String tenantId, long id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(String tenantId, List<Long> ids);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	GoodsPurchase getGoodsPurchase(String tenantId, long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getGoodsPurchaseCountByQueryCriteria(GoodsPurchaseQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<GoodsPurchase> getGoodsPurchasesByQueryCriteria(int start, int pageSize, GoodsPurchaseQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<GoodsPurchase> list(GoodsPurchaseQuery query);

	/**
	 * 获取某天采购的食品单价
	 * 
	 * @param tenantId
	 * @param fullDay
	 * @return
	 */
	Map<Long, Double> getPriceMap(String tenantId, int fullDay);

	/**
	 * 获取某天采购的食品总价
	 * 
	 * @param tenantId
	 * @param fullDay
	 * @return
	 */
	Map<Long, Double> getTotalPriceMap(String tenantId, int fullDay);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(GoodsPurchase goodsPurchase);

	@Transactional
	void updateGoodsPurchaseStatus(GoodsPurchase model);

	@Transactional
	void updateGoodsPurchaseStatus(List<GoodsPurchase> list);
	
	@Transactional
	void updateAll(String tenantId, Map<Long, GoodsPurchase> dataMap);

}
