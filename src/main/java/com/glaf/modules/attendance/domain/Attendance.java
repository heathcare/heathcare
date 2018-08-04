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

package com.glaf.modules.attendance.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.modules.attendance.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "T_ATTENDANCE")
public class Attendance implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
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
	 * 学生编号
	 */
	@Column(name = "PERSONID_", length = 50)
	protected String personId;

	/**
	 * 姓名
	 */
	@Column(name = "NAME_", length = 100)
	protected String name;

	/**
	 * 1日出勤情况
	 */
	@Column(name = "STATUS1_")
	protected int status1;

	/**
	 * 2日出勤情况
	 */
	@Column(name = "STATUS2_")
	protected int status2;

	/**
	 * 3日出勤情况
	 */
	@Column(name = "STATUS3_")
	protected int status3;

	/**
	 * 4日出勤情况
	 */
	@Column(name = "STATUS4_")
	protected int status4;

	/**
	 * 5日出勤情况
	 */
	@Column(name = "STATUS5_")
	protected int status5;

	/**
	 * 6日出勤情况
	 */
	@Column(name = "STATUS6_")
	protected int status6;

	/**
	 * 7日出勤情况
	 */
	@Column(name = "STATUS7_")
	protected int status7;

	/**
	 * 8日出勤情况
	 */
	@Column(name = "STATUS8_")
	protected int status8;

	/**
	 * 9日出勤情况
	 */
	@Column(name = "STATUS9_")
	protected int status9;

	/**
	 * 10日出勤情况
	 */
	@Column(name = "STATUS10_")
	protected int status10;

	/**
	 * 11日出勤情况
	 */
	@Column(name = "STATUS11_")
	protected int status11;

	/**
	 * 12日出勤情况
	 */
	@Column(name = "STATUS12_")
	protected int status12;

	/**
	 * 13日出勤情况
	 */
	@Column(name = "STATUS13_")
	protected int status13;

	/**
	 * 14日出勤情况
	 */
	@Column(name = "STATUS14_")
	protected int status14;

	/**
	 * 15日出勤情况
	 */
	@Column(name = "STATUS15_")
	protected int status15;

	/**
	 * 16日出勤情况
	 */
	@Column(name = "STATUS16_")
	protected int status16;

	/**
	 * 17日出勤情况
	 */
	@Column(name = "STATUS17_")
	protected int status17;

	/**
	 * 18日出勤情况
	 */
	@Column(name = "STATUS18_")
	protected int status18;

	/**
	 * 19日出勤情况
	 */
	@Column(name = "STATUS19_")
	protected int status19;

	/**
	 * 20日出勤情况
	 */
	@Column(name = "STATUS20_")
	protected int status20;

	/**
	 * 21日出勤情况
	 */
	@Column(name = "STATUS21_")
	protected int status21;

	/**
	 * 22日出勤情况
	 */
	@Column(name = "STATUS22_")
	protected int status22;

	/**
	 * 23日出勤情况
	 */
	@Column(name = "STATUS23_")
	protected int status23;

	/**
	 * 24日出勤情况
	 */
	@Column(name = "STATUS24_")
	protected int status24;

	/**
	 * 25日出勤情况
	 */
	@Column(name = "STATUS25_")
	protected int status25;

	/**
	 * 26日出勤情况
	 */
	@Column(name = "STATUS26_")
	protected int status26;

	/**
	 * 27日出勤情况
	 */
	@Column(name = "STATUS27_")
	protected int status27;

	/**
	 * 28日出勤情况
	 */
	@Column(name = "STATUS28_")
	protected int status28;

	/**
	 * 29日出勤情况
	 */
	@Column(name = "STATUS29_")
	protected int status29;

	/**
	 * 30日出勤情况
	 */
	@Column(name = "STATUS30_")
	protected int status30;

	/**
	 * 31日出勤情况
	 */
	@Column(name = "STATUS31_")
	protected int status31;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

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

	public Attendance() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public String getPersonId() {
		return this.personId;
	}

	public String getName() {
		return this.name;
	}

	public int getStatus1() {
		return this.status1;
	}

	public int getStatus2() {
		return this.status2;
	}

	public int getStatus3() {
		return this.status3;
	}

	public int getStatus4() {
		return this.status4;
	}

	public int getStatus5() {
		return this.status5;
	}

	public int getStatus6() {
		return this.status6;
	}

	public int getStatus7() {
		return this.status7;
	}

	public int getStatus8() {
		return this.status8;
	}

	public int getStatus9() {
		return this.status9;
	}

	public int getStatus10() {
		return this.status10;
	}

	public int getStatus11() {
		return this.status11;
	}

	public int getStatus12() {
		return this.status12;
	}

	public int getStatus13() {
		return this.status13;
	}

	public int getStatus14() {
		return this.status14;
	}

	public int getStatus15() {
		return this.status15;
	}

	public int getStatus16() {
		return this.status16;
	}

	public int getStatus17() {
		return this.status17;
	}

	public int getStatus18() {
		return this.status18;
	}

	public int getStatus19() {
		return this.status19;
	}

	public int getStatus20() {
		return this.status20;
	}

	public int getStatus21() {
		return this.status21;
	}

	public int getStatus22() {
		return this.status22;
	}

	public int getStatus23() {
		return this.status23;
	}

	public int getStatus24() {
		return this.status24;
	}

	public int getStatus25() {
		return this.status25;
	}

	public int getStatus26() {
		return this.status26;
	}

	public int getStatus27() {
		return this.status27;
	}

	public int getStatus28() {
		return this.status28;
	}

	public int getStatus29() {
		return this.status29;
	}

	public int getStatus30() {
		return this.status30;
	}

	public int getStatus31() {
		return this.status31;
	}

	public String getType() {
		return this.type;
	}

	public int getYear() {
		return this.year;
	}

	public int getMonth() {
		return this.month;
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

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus1(int status1) {
		this.status1 = status1;
	}

	public void setStatus2(int status2) {
		this.status2 = status2;
	}

	public void setStatus3(int status3) {
		this.status3 = status3;
	}

	public void setStatus4(int status4) {
		this.status4 = status4;
	}

	public void setStatus5(int status5) {
		this.status5 = status5;
	}

	public void setStatus6(int status6) {
		this.status6 = status6;
	}

	public void setStatus7(int status7) {
		this.status7 = status7;
	}

	public void setStatus8(int status8) {
		this.status8 = status8;
	}

	public void setStatus9(int status9) {
		this.status9 = status9;
	}

	public void setStatus10(int status10) {
		this.status10 = status10;
	}

	public void setStatus11(int status11) {
		this.status11 = status11;
	}

	public void setStatus12(int status12) {
		this.status12 = status12;
	}

	public void setStatus13(int status13) {
		this.status13 = status13;
	}

	public void setStatus14(int status14) {
		this.status14 = status14;
	}

	public void setStatus15(int status15) {
		this.status15 = status15;
	}

	public void setStatus16(int status16) {
		this.status16 = status16;
	}

	public void setStatus17(int status17) {
		this.status17 = status17;
	}

	public void setStatus18(int status18) {
		this.status18 = status18;
	}

	public void setStatus19(int status19) {
		this.status19 = status19;
	}

	public void setStatus20(int status20) {
		this.status20 = status20;
	}

	public void setStatus21(int status21) {
		this.status21 = status21;
	}

	public void setStatus22(int status22) {
		this.status22 = status22;
	}

	public void setStatus23(int status23) {
		this.status23 = status23;
	}

	public void setStatus24(int status24) {
		this.status24 = status24;
	}

	public void setStatus25(int status25) {
		this.status25 = status25;
	}

	public void setStatus26(int status26) {
		this.status26 = status26;
	}

	public void setStatus27(int status27) {
		this.status27 = status27;
	}

	public void setStatus28(int status28) {
		this.status28 = status28;
	}

	public void setStatus29(int status29) {
		this.status29 = status29;
	}

	public void setStatus30(int status30) {
		this.status30 = status30;
	}

	public void setStatus31(int status31) {
		this.status31 = status31;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attendance other = (Attendance) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Attendance jsonToObject(JSONObject jsonObject) {
		return AttendanceJsonFactory.jsonToObject(jsonObject);
	}

	public JSONObject toJsonObject() {
		return AttendanceJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return AttendanceJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
