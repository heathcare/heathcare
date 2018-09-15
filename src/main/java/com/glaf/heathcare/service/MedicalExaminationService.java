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

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationCount;
import com.glaf.heathcare.query.MedicalExaminationQuery;

@Transactional(readOnly = true)
public interface MedicalExaminationService {

	@Transactional
	void bulkInsert(List<MedicalExamination> list);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(long id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<Long> ids);

	/**
	 * 获取最近一次检查记录
	 * 
	 * @param personId
	 * @return
	 */
	MedicalExamination getLatestMedicalExamination(String personId);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	MedicalExamination getMedicalExamination(long id);

	int getMedicalExaminationCount(String tenantId, String checkId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getMedicalExaminationCountByQueryCriteria(MedicalExaminationQuery query);

	List<MedicalExaminationCount> getMedicalExaminationIndexs(MedicalExaminationQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<MedicalExamination> getMedicalExaminationsByQueryCriteria(int start, int pageSize,
			MedicalExaminationQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<MedicalExamination> list(MedicalExaminationQuery query);

	/**
	 * 批量插入体检信息
	 * 
	 * @param tenantId
	 * @param type
	 * @param rows
	 * @param checkDate
	 */
	@Transactional
	void insertAll(String tenantId, String type, List<MedicalExamination> rows, Date checkDate);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(MedicalExamination medicalExamination);

	@Transactional
	void updateAllEvaluate(List<MedicalExamination> medicalExaminations);

}
