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

package com.glaf.heathcare.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "HEALTH_GRADE_PERSON_RELATION")
public class GradePersonRelation implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 64, nullable = false)
	protected String id;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 班级编号
	 */
	@Column(name = "GRADEID_", length = 50)
	protected String gradeId;

	/**
	 * 人员编号
	 */
	@Column(name = "PERSONID_", length = 50)
	protected String personId;

	/**
	 * 姓名
	 */
	@Column(name = "NAME_", length = 100)
	protected String name;

	/**
	 * 性别(0代表女生，1代表男生)
	 */
	@Column(name = "SEX_", length = 2)
	protected String sex;

	/**
	 * 年
	 */
	@Column(name = "YEAR_")
	protected int year;

	/**
	 * 月
	 */
	@Column(name = "MONTH_")
	protected int month;

	/**
	 * 学期
	 */
	@Column(name = "SEMESTER_")
	protected int semester;

	/**
	 * 排序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	@javax.persistence.Transient
	protected int count;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public GradePersonRelation() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GradePersonRelation other = (GradePersonRelation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int getCount() {
		return count;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public String getCreateTimeString() {
		if (this.createTime != null) {
			return DateUtils.getDateTime(this.createTime);
		}
		return "";
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public String getId() {
		return this.id;
	}

	public int getMonth() {
		return this.month;
	}

	public String getName() {
		return this.name;
	}

	public String getPersonId() {
		return this.personId;
	}

	public int getSemester() {
		return this.semester;
	}

	public String getSex() {
		return sex;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public GradePersonRelation jsonToObject(JSONObject jsonObject) {
		return GradePersonRelationJsonFactory.jsonToObject(jsonObject);
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return GradePersonRelationJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return GradePersonRelationJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
