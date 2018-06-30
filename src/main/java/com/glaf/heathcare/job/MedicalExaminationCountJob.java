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

package com.glaf.heathcare.job;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.core.job.BaseJob;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.bean.MedicalExaminationEvaluateCountBean;

public class MedicalExaminationCountJob extends BaseJob {

	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	protected static int F_STEP = 0;

	@Override
	public void runJob(JobExecutionContext context) throws JobExecutionException {
		logger.debug("-----------------------MedicalExaminationCountJob-------------------------");
		if (F_STEP > 0) {
			if ((System.currentTimeMillis() - lastExecuteTime.get()) < DateUtils.MINUTE * 60) {
				logger.info("间隔时间未到，不执行。");
				return;
			}
		}
		try {
			TimeUnit.SECONDS.sleep(1 + new Random().nextInt(30));// 随机等待，避免Job同时执行
		} catch (InterruptedException e) {
		}
		F_STEP++;
		MedicalExaminationEvaluateCountBean bean = new MedicalExaminationEvaluateCountBean();
		bean.executeAll();
		lastExecuteTime.set(System.currentTimeMillis());
	}

}
