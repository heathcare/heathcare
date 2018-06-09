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

package com.glaf.modules.employee.service;

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

import com.glaf.modules.employee.mapper.*;
import com.glaf.modules.employee.domain.*;
import com.glaf.modules.employee.query.*;

@Service("com.glaf.modules.employee.service.employeeService")
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected EmployeeMapper employeeMapper;

	public EmployeeServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<Employee> list) {
		for (Employee employee : list) {
			if (StringUtils.isEmpty(employee.getId())) {
				employee.setId(UUID32.getUUID());
				employee.setCreateTime(new Date());
				employee.setDeleteFlag(0);
			}
		}

		int batch_size = 50;
		List<Employee> rows = new ArrayList<Employee>(batch_size);

		for (Employee bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					employeeMapper.bulkInsertEmployee_oracle(rows);
				} else {
					employeeMapper.bulkInsertEmployee(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				employeeMapper.bulkInsertEmployee_oracle(rows);
			} else {
				employeeMapper.bulkInsertEmployee(rows);
			}
			rows.clear();
		}
	}

	public int count(EmployeeQuery query) {
		return employeeMapper.getEmployeeCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			employeeMapper.deleteEmployeeById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				employeeMapper.deleteEmployeeById(id);
			}
		}
	}

	public Employee getEmployee(String id) {
		if (id == null) {
			return null;
		}
		Employee employee = employeeMapper.getEmployeeById(id);
		return employee;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getEmployeeCountByQueryCriteria(EmployeeQuery query) {
		return employeeMapper.getEmployeeCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Employee> getEmployeesByQueryCriteria(int start, int pageSize, EmployeeQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Employee> rows = sqlSessionTemplate.selectList("getEmployees", query, rowBounds);
		return rows;
	}

	public List<Employee> list(EmployeeQuery query) {
		List<Employee> list = employeeMapper.getEmployees(query);
		return list;
	}

	@Transactional
	public void save(Employee employee) {
		if (StringUtils.isEmpty(employee.getId())) {
			employee.setId(UUID32.getUUID());
			employee.setCreateTime(new Date());
			employee.setDeleteFlag(0);
			employeeMapper.insertEmployee(employee);
		} else {
			employeeMapper.updateEmployee(employee);
		}
	}

	@javax.annotation.Resource(name = "com.glaf.modules.employee.mapper.EmployeeMapper")
	public void setEmployeeMapper(EmployeeMapper employeeMapper) {
		this.employeeMapper = employeeMapper;
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

}
