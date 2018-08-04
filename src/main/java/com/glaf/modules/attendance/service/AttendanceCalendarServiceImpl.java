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
import com.glaf.core.util.UUID32;
import com.glaf.modules.attendance.domain.AttendanceCalendar;
import com.glaf.modules.attendance.mapper.AttendanceCalendarMapper;
import com.glaf.modules.attendance.query.AttendanceCalendarQuery;

@Service("com.glaf.modules.attendance.service.attendanceCalendarService")
@Transactional(readOnly = true)
public class AttendanceCalendarServiceImpl implements AttendanceCalendarService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected AttendanceCalendarMapper attendanceCalendarMapper;

	public AttendanceCalendarServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<AttendanceCalendar> list) {
		for (AttendanceCalendar attendanceCalendar : list) {
			if (StringUtils.isEmpty(attendanceCalendar.getId())) {
				attendanceCalendar.setId(idGenerator.getNextId("T_ATTENDANCE_CALENDAR"));
			}
		}

		int batch_size = 50;
		List<AttendanceCalendar> rows = new ArrayList<AttendanceCalendar>(batch_size);

		for (AttendanceCalendar bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					attendanceCalendarMapper.bulkInsertAttendanceCalendar_oracle(rows);
				} else {
					attendanceCalendarMapper.bulkInsertAttendanceCalendar(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				attendanceCalendarMapper.bulkInsertAttendanceCalendar_oracle(rows);
			} else {
				attendanceCalendarMapper.bulkInsertAttendanceCalendar(rows);
			}
			rows.clear();
		}
	}

	public int count(AttendanceCalendarQuery query) {
		return attendanceCalendarMapper.getAttendanceCalendarCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			attendanceCalendarMapper.deleteAttendanceCalendarById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				attendanceCalendarMapper.deleteAttendanceCalendarById(id);
			}
		}
	}

	public AttendanceCalendar getAttendanceCalendar(String id) {
		if (id == null) {
			return null;
		}
		AttendanceCalendar attendanceCalendar = attendanceCalendarMapper.getAttendanceCalendarById(id);
		return attendanceCalendar;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getAttendanceCalendarCountByQueryCriteria(AttendanceCalendarQuery query) {
		return attendanceCalendarMapper.getAttendanceCalendarCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<AttendanceCalendar> getAttendanceCalendarsByQueryCriteria(int start, int pageSize,
			AttendanceCalendarQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<AttendanceCalendar> rows = sqlSessionTemplate.selectList("getAttendanceCalendars", query, rowBounds);
		return rows;
	}

	public List<AttendanceCalendar> list(AttendanceCalendarQuery query) {
		List<AttendanceCalendar> list = attendanceCalendarMapper.getAttendanceCalendars(query);
		return list;
	}

	@Transactional
	public void save(String tenantId, int year, int month, AttendanceCalendar attendanceCalendar) {
		AttendanceCalendarQuery query = new AttendanceCalendarQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.month(month);
		attendanceCalendarMapper.deleteAttendanceCalendars(query);

		attendanceCalendar.setId(UUID32.getUUID());
		attendanceCalendar.setCreateTime(new java.util.Date());
		attendanceCalendarMapper.insertAttendanceCalendar(attendanceCalendar);
	}

	@javax.annotation.Resource(name = "com.glaf.modules.attendance.mapper.AttendanceCalendarMapper")
	public void setAttendanceCalendarMapper(AttendanceCalendarMapper attendanceCalendarMapper) {
		this.attendanceCalendarMapper = attendanceCalendarMapper;
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
