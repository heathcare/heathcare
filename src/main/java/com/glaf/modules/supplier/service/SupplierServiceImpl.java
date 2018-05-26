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

package com.glaf.modules.supplier.service;

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.*;

import com.glaf.modules.supplier.mapper.*;
import com.glaf.modules.supplier.domain.*;
import com.glaf.modules.supplier.query.*;

@Service("com.glaf.modules.supplier.service.supplierService")
@Transactional(readOnly = true)
public class SupplierServiceImpl implements SupplierService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SupplierMapper supplierMapper;

	public SupplierServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<Supplier> list) {
		for (Supplier supplier : list) {
			if (supplier.getSupplierId() == null) {
				supplier.setSupplierId(UUID32.getUUID());
				supplier.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<Supplier> rows = new ArrayList<Supplier>(batch_size);

		for (Supplier bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					supplierMapper.bulkInsertSupplier_oracle(rows);
				} else {
					supplierMapper.bulkInsertSupplier(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				supplierMapper.bulkInsertSupplier_oracle(rows);
			} else {
				supplierMapper.bulkInsertSupplier(rows);
			}
			rows.clear();
		}
	}

	public int count(SupplierQuery query) {
		return supplierMapper.getSupplierCount(query);
	}

	@Transactional
	public void deleteById(String supplierId) {
		if (supplierId != null) {
			supplierMapper.deleteSupplierById(supplierId);
		}
	}

	@Transactional
	public void deleteByIds(List<String> supplierIds) {
		if (supplierIds != null && !supplierIds.isEmpty()) {
			for (String supplierId : supplierIds) {
				supplierMapper.deleteSupplierById(supplierId);
			}
		}
	}

	public Supplier getSupplier(String supplierId) {
		if (supplierId == null) {
			return null;
		}
		Supplier supplier = supplierMapper.getSupplierById(supplierId);
		return supplier;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSupplierCountByQueryCriteria(SupplierQuery query) {
		return supplierMapper.getSupplierCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Supplier> getSuppliersByQueryCriteria(int start, int pageSize, SupplierQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Supplier> rows = sqlSessionTemplate.selectList("getSuppliers", query, rowBounds);
		return rows;
	}

	public List<Supplier> list(SupplierQuery query) {
		List<Supplier> list = supplierMapper.getSuppliers(query);
		return list;
	}

	@Transactional
	public void save(Supplier supplier) {
		if (supplier.getSupplierId() == null) {
			supplier.setSupplierId(UUID32.getUUID());
			supplier.setCreateTime(new Date());

			supplierMapper.insertSupplier(supplier);
		} else {
			supplierMapper.updateSupplier(supplier);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.modules.supplier.mapper.SupplierMapper")
	public void setSupplierMapper(SupplierMapper supplierMapper) {
		this.supplierMapper = supplierMapper;
	}

}
