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

package com.glaf.modules.employee.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class EmployeeQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String nameLike;
	protected String namePinyinLike;
	protected String sex;
	protected String mobileLike;
	protected String nation;
	protected String marryStatus;
	protected String education;
	protected String seniority;
	protected String type;
	protected String category;
	protected Date joinDateGreaterThanOrEqual;
	protected Date joinDateLessThanOrEqual;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public EmployeeQuery() {

	}

	public EmployeeQuery category(String category) {
		if (category == null) {
			throw new RuntimeException("category is null");
		}
		this.category = category;
		return this;
	}

	public EmployeeQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public EmployeeQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public EmployeeQuery deleteFlag(Integer deleteFlag) {
		if (deleteFlag == null) {
			throw new RuntimeException("deleteFlag is null");
		}
		this.deleteFlag = deleteFlag;
		return this;
	}

	public EmployeeQuery education(String education) {
		if (education == null) {
			throw new RuntimeException("education is null");
		}
		this.education = education;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getEducation() {
		return education;
	}

	public Date getJoinDateGreaterThanOrEqual() {
		return joinDateGreaterThanOrEqual;
	}

	public Date getJoinDateLessThanOrEqual() {
		return joinDateLessThanOrEqual;
	}

	public String getMarryStatus() {
		return marryStatus;
	}

	public String getMobileLike() {
		if (mobileLike != null && mobileLike.trim().length() > 0) {
			if (!mobileLike.startsWith("%")) {
				mobileLike = "%" + mobileLike;
			}
			if (!mobileLike.endsWith("%")) {
				mobileLike = mobileLike + "%";
			}
		}
		return mobileLike;
	}

	public String getNameLike() {
		if (nameLike != null && nameLike.trim().length() > 0) {
			if (!nameLike.startsWith("%")) {
				nameLike = "%" + nameLike;
			}
			if (!nameLike.endsWith("%")) {
				nameLike = nameLike + "%";
			}
		}
		return nameLike;
	}

	public String getNamePinyinLike() {
		if (namePinyinLike != null && namePinyinLike.trim().length() > 0) {
			if (!namePinyinLike.endsWith("%")) {
				namePinyinLike = namePinyinLike + "%";
			}
		}
		return namePinyinLike;
	}

	public String getNation() {
		return nation;
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

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("sex".equals(sortColumn)) {
				orderBy = "E.SEX_" + a_x;
			}

			if ("birthday".equals(sortColumn)) {
				orderBy = "E.BIRTHDAY_" + a_x;
			}

			if ("idCardNo".equals(sortColumn)) {
				orderBy = "E.IDCARDNO_" + a_x;
			}

			if ("employeeID".equals(sortColumn)) {
				orderBy = "E.EMPLOYEEID_" + a_x;
			}

			if ("mobile".equals(sortColumn)) {
				orderBy = "E.MOBILE_" + a_x;
			}

			if ("telephone".equals(sortColumn)) {
				orderBy = "E.TELEPHONE_" + a_x;
			}

			if ("nationality".equals(sortColumn)) {
				orderBy = "E.NATIONALITY_" + a_x;
			}

			if ("nation".equals(sortColumn)) {
				orderBy = "E.NATION_" + a_x;
			}

			if ("birthPlace".equals(sortColumn)) {
				orderBy = "E.BIRTHPLACE_" + a_x;
			}

			if ("homeAddress".equals(sortColumn)) {
				orderBy = "E.HOMEADDRESS_" + a_x;
			}

			if ("marryStatus".equals(sortColumn)) {
				orderBy = "E.MARRYSTATUS_" + a_x;
			}

			if ("natureAccount".equals(sortColumn)) {
				orderBy = "E.NATUREACCOUNT_" + a_x;
			}

			if ("natureType".equals(sortColumn)) {
				orderBy = "E.NATURETYPE_" + a_x;
			}

			if ("education".equals(sortColumn)) {
				orderBy = "E.EDUCATION_" + a_x;
			}

			if ("seniority".equals(sortColumn)) {
				orderBy = "E.SENIORITY_" + a_x;
			}

			if ("position".equals(sortColumn)) {
				orderBy = "E.POSITION_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("category".equals(sortColumn)) {
				orderBy = "E.CATEGORY_" + a_x;
			}

			if ("joinDate".equals(sortColumn)) {
				orderBy = "E.JOINDATE_" + a_x;
			}

			if ("remark".equals(sortColumn)) {
				orderBy = "E.REMARK_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

			if ("deleteFlag".equals(sortColumn)) {
				orderBy = "E.DELETEFLAG_" + a_x;
			}

		}
		return orderBy;
	}

	public String getSeniority() {
		return seniority;
	}

	public String getSex() {
		return sex;
	}

	public String getType() {
		return type;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("name", "NAME_");
		addColumn("sex", "SEX_");
		addColumn("birthday", "BIRTHDAY_");
		addColumn("idCardNo", "IDCARDNO_");
		addColumn("employeeID", "EMPLOYEEID_");
		addColumn("mobile", "MOBILE_");
		addColumn("telephone", "TELEPHONE_");
		addColumn("nationality", "NATIONALITY_");
		addColumn("nation", "NATION_");
		addColumn("birthPlace", "BIRTHPLACE_");
		addColumn("homeAddress", "HOMEADDRESS_");
		addColumn("marryStatus", "MARRYSTATUS_");
		addColumn("natureAccount", "NATUREACCOUNT_");
		addColumn("natureType", "NATURETYPE_");
		addColumn("education", "EDUCATION_");
		addColumn("seniority", "SENIORITY_");
		addColumn("position", "POSITION_");
		addColumn("type", "TYPE_");
		addColumn("category", "CATEGORY_");
		addColumn("joinDate", "JOINDATE_");
		addColumn("remark", "REMARK_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
		addColumn("deleteFlag", "DELETEFLAG_");
	}

	public EmployeeQuery joinDateGreaterThanOrEqual(Date joinDateGreaterThanOrEqual) {
		if (joinDateGreaterThanOrEqual == null) {
			throw new RuntimeException("joinDate is null");
		}
		this.joinDateGreaterThanOrEqual = joinDateGreaterThanOrEqual;
		return this;
	}

	public EmployeeQuery joinDateLessThanOrEqual(Date joinDateLessThanOrEqual) {
		if (joinDateLessThanOrEqual == null) {
			throw new RuntimeException("joinDate is null");
		}
		this.joinDateLessThanOrEqual = joinDateLessThanOrEqual;
		return this;
	}

	public EmployeeQuery marryStatus(String marryStatus) {
		if (marryStatus == null) {
			throw new RuntimeException("marryStatus is null");
		}
		this.marryStatus = marryStatus;
		return this;
	}

	public EmployeeQuery mobileLike(String mobileLike) {
		if (mobileLike == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobileLike = mobileLike;
		return this;
	}

	public EmployeeQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public EmployeeQuery namePinyinLike(String namePinyinLike) {
		if (namePinyinLike == null) {
			throw new RuntimeException("namePinyinLike is null");
		}
		this.namePinyinLike = namePinyinLike;
		return this;
	}

	public EmployeeQuery nation(String nation) {
		if (nation == null) {
			throw new RuntimeException("nation is null");
		}
		this.nation = nation;
		return this;
	}

	public EmployeeQuery seniority(String seniority) {
		if (seniority == null) {
			throw new RuntimeException("seniority is null");
		}
		this.seniority = seniority;
		return this;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public void setJoinDateGreaterThanOrEqual(Date joinDateGreaterThanOrEqual) {
		this.joinDateGreaterThanOrEqual = joinDateGreaterThanOrEqual;
	}

	public void setJoinDateLessThanOrEqual(Date joinDateLessThanOrEqual) {
		this.joinDateLessThanOrEqual = joinDateLessThanOrEqual;
	}

	public void setMarryStatus(String marryStatus) {
		this.marryStatus = marryStatus;
	}

	public void setMobileLike(String mobileLike) {
		this.mobileLike = mobileLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setNamePinyinLike(String namePinyinLike) {
		this.namePinyinLike = namePinyinLike;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public void setSeniority(String seniority) {
		this.seniority = seniority;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setType(String type) {
		this.type = type;
	}

	public EmployeeQuery sex(String sex) {
		if (sex == null) {
			throw new RuntimeException("sex is null");
		}
		this.sex = sex;
		return this;
	}

	public EmployeeQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public EmployeeQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}