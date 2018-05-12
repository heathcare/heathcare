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

package com.glaf.heathcare.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class GradePersonRelationQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String gradeId;
	protected List<String> gradeIds;
	protected String sex;
	protected Integer year;
	protected Integer yearGreaterThanOrEqual;
	protected Integer yearLessThanOrEqual;
	protected Integer month;
	protected Integer monthGreaterThanOrEqual;
	protected Integer monthLessThanOrEqual;
	protected Integer semester;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public GradePersonRelationQuery() {

	}

	public GradePersonRelationQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public GradePersonRelationQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getGradeId() {
		return gradeId;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getMonthGreaterThanOrEqual() {
		return monthGreaterThanOrEqual;
	}

	public Integer getMonthLessThanOrEqual() {
		return monthLessThanOrEqual;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("gradeId".equals(sortColumn)) {
				orderBy = "E.GRADEID_" + a_x;
			}

			if ("personId".equals(sortColumn)) {
				orderBy = "E.PERSONID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("semester".equals(sortColumn)) {
				orderBy = "E.SEMESTER_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Integer getSemester() {
		return semester;
	}

	public String getSex() {
		return sex;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getYearGreaterThanOrEqual() {
		return yearGreaterThanOrEqual;
	}

	public Integer getYearLessThanOrEqual() {
		return yearLessThanOrEqual;
	}

	public GradePersonRelationQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public GradePersonRelationQuery gradeIds(List<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("gradeId", "GRADEID_");
		addColumn("personId", "PERSONID_");
		addColumn("name", "NAME_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("semester", "SEMESTER_");
		addColumn("sortNo", "SORTNO_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public GradePersonRelationQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public GradePersonRelationQuery monthGreaterThanOrEqual(Integer monthGreaterThanOrEqual) {
		if (monthGreaterThanOrEqual == null) {
			throw new RuntimeException("month is null");
		}
		this.monthGreaterThanOrEqual = monthGreaterThanOrEqual;
		return this;
	}

	public GradePersonRelationQuery monthLessThanOrEqual(Integer monthLessThanOrEqual) {
		if (monthLessThanOrEqual == null) {
			throw new RuntimeException("month is null");
		}
		this.monthLessThanOrEqual = monthLessThanOrEqual;
		return this;
	}

	public GradePersonRelationQuery semester(Integer semester) {
		if (semester == null) {
			throw new RuntimeException("semester is null");
		}
		this.semester = semester;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setMonthGreaterThanOrEqual(Integer monthGreaterThanOrEqual) {
		this.monthGreaterThanOrEqual = monthGreaterThanOrEqual;
	}

	public void setMonthLessThanOrEqual(Integer monthLessThanOrEqual) {
		this.monthLessThanOrEqual = monthLessThanOrEqual;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setYearGreaterThanOrEqual(Integer yearGreaterThanOrEqual) {
		this.yearGreaterThanOrEqual = yearGreaterThanOrEqual;
	}

	public void setYearLessThanOrEqual(Integer yearLessThanOrEqual) {
		this.yearLessThanOrEqual = yearLessThanOrEqual;
	}

	public GradePersonRelationQuery sex(String sex) {
		if (sex == null) {
			throw new RuntimeException("sex is null");
		}
		this.sex = sex;
		return this;
	}

	public GradePersonRelationQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public GradePersonRelationQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

	public GradePersonRelationQuery yearGreaterThanOrEqual(Integer yearGreaterThanOrEqual) {
		if (yearGreaterThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearGreaterThanOrEqual = yearGreaterThanOrEqual;
		return this;
	}

	public GradePersonRelationQuery yearLessThanOrEqual(Integer yearLessThanOrEqual) {
		if (yearLessThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearLessThanOrEqual = yearLessThanOrEqual;
		return this;
	}

}