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

package com.glaf.core.jdbc.datasource;

import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.SystemConfig;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.jdbc.connection.ConnectionProviderFactory;

public class DataSourceUtils {
	private static final Logger logger = LoggerFactory.getLogger(DataSourceUtils.class);

	private DataSourceUtils() {

	}

	public static void closeAndReloadDataSource() {
		logger.warn("----------------------closeAndReloadDataSource-----------");
		/**
		 * 系统参数，是否允许重建数据源
		 */
		if (SystemConfig.getBoolean("allow_datasource_rebuild")) {
			Map<Object, Object> targetDataSources = MultiRoutingDataSource.getTargetDataSources();
			if (targetDataSources != null && !targetDataSources.isEmpty()) {
				Set<Entry<Object, Object>> entrySet = targetDataSources.entrySet();
				for (Entry<Object, Object> entry : entrySet) {
					Object val = entry.getValue();
					if (val != null && val instanceof DataSource) {
						DataSource ds = (DataSource) val;
						if (ds instanceof com.zaxxer.hikari.HikariDataSource) {
							com.zaxxer.hikari.HikariDataSource xds = (com.zaxxer.hikari.HikariDataSource) ds;
							xds.close();
							logger.warn("关闭HikariCP数据源。");
						} else if (ds instanceof com.alibaba.druid.pool.DruidDataSource) {
							com.alibaba.druid.pool.DruidDataSource xds = (com.alibaba.druid.pool.DruidDataSource) ds;
							xds.close();
							logger.warn("关闭Druid数据源。");
						}
					}
				}
				targetDataSources.clear();
				logger.warn("----------------------准备重建数据源-----------");
				ConnectionProviderFactory.close();// 先关闭连接数据源
				MultiRoutingDataSource dataSource = ContextFactory.getBean("dataSource");
				dataSource.afterPropertiesSet();// 重建数据源
			}
		}
	}

}
