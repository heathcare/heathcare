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

package com.glaf.modules.attendance.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.modules.attendance.domain.Attendance;
import com.glaf.modules.attendance.mapper.AttendanceMapper;
import com.glaf.modules.attendance.query.AttendanceQuery;

@Service("com.glaf.modules.attendance.service.attendanceService")
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected AttendanceMapper attendanceMapper;

	public AttendanceServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<Attendance> list) {
		for (Attendance attendance : list) {
			if (StringUtils.isEmpty(attendance.getId())) {
				attendance.setId(idGenerator.getNextId("T_ATTENDANCE"));
			}
		}

		int batch_size = 50;
		List<Attendance> rows = new ArrayList<Attendance>(batch_size);

		for (Attendance bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					attendanceMapper.bulkInsertAttendance_oracle(rows);
				} else {
					attendanceMapper.bulkInsertAttendance(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				attendanceMapper.bulkInsertAttendance_oracle(rows);
			} else {
				attendanceMapper.bulkInsertAttendance(rows);
			}
			rows.clear();
		}
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			attendanceMapper.deleteAttendanceById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				attendanceMapper.deleteAttendanceById(id);
			}
		}
	}

	public int count(AttendanceQuery query) {
		return attendanceMapper.getAttendanceCount(query);
	}

	public List<Attendance> list(AttendanceQuery query) {
		List<Attendance> list = attendanceMapper.getAttendances(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getAttendanceCountByQueryCriteria(AttendanceQuery query) {
		return attendanceMapper.getAttendanceCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Attendance> getAttendancesByQueryCriteria(int start, int pageSize, AttendanceQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Attendance> rows = sqlSessionTemplate.selectList("getAttendances", query, rowBounds);
		return rows;
	}

	public Attendance getAttendance(String id) {
		if (id == null) {
			return null;
		}
		Attendance attendance = attendanceMapper.getAttendanceById(id);
		return attendance;
	}

	/**
	 * 保存多条记录
	 * 
	 * @return
	 */
	@Transactional
	public void saveAll(String tenantId, int year, int month, List<Attendance> attendances) {
		AttendanceQuery query = new AttendanceQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.month(month);
		attendanceMapper.deleteAttendances(query);

		this.bulkInsert(attendances);
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource(name = "com.glaf.modules.attendance.mapper.AttendanceMapper")
	public void setAttendanceMapper(AttendanceMapper attendanceMapper) {
		this.attendanceMapper = attendanceMapper;
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
