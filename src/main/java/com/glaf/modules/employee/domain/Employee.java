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

package com.glaf.modules.employee.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.modules.employee.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "T_EMPLOYEE")
public class Employee implements Serializable, JSONable {
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
	 * 姓名
	 */
	@Column(name = "NAME_", length = 100)
	protected String name;

	/**
	 * 性别
	 */
	@Column(name = "SEX_", length = 1)
	protected String sex;

	/**
	 * 出生日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BIRTHDAY_")
	protected Date birthday;

	/**
	 * 身份证编号
	 */
	@Column(name = "IDCARDNO_", length = 20)
	protected String idCardNo;

	/**
	 * 工号
	 */
	@Column(name = "EMPLOYEEID_", length = 50)
	protected String employeeID;

	/**
	 * 手机号码
	 */
	@Column(name = "MOBILE_", length = 200)
	protected String mobile;

	/**
	 * 固定电话
	 */
	@Column(name = "TELEPHONE_", length = 200)
	protected String telephone;

	/**
	 * 国籍
	 */
	@Column(name = "NATIONALITY_", length = 200)
	protected String nationality;

	/**
	 * 民族
	 */
	@Column(name = "NATION_", length = 200)
	protected String nation;

	/**
	 * 籍贯
	 */
	@Column(name = "BIRTHPLACE_", length = 200)
	protected String birthPlace;

	/**
	 * 家庭住址
	 */
	@Column(name = "HOMEADDRESS_", length = 250)
	protected String homeAddress;

	/**
	 * 婚否
	 */
	@Column(name = "MARRYSTATUS_", length = 1)
	protected String marryStatus;

	/**
	 * 户口性质
	 */
	@Column(name = "NATUREACCOUNT_", length = 200)
	protected String natureAccount;

	/**
	 * 户口类型
	 */
	@Column(name = "NATURETYPE_", length = 200)
	protected String natureType;

	/**
	 * 学历
	 */
	@Column(name = "EDUCATION_", length = 200)
	protected String education;

	/**
	 * 资历
	 */
	@Column(name = "SENIORITY_", length = 200)
	protected String seniority;

	/**
	 * 职务
	 */
	@Column(name = "POSITION_", length = 200)
	protected String position;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 分类
	 */
	@Column(name = "CATEGORY_", length = 50)
	protected String category;

	/**
	 * 入职日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "JOINDATE_")
	protected Date joinDate;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 4000)
	protected String remark;

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

	/**
	 * 修改人
	 */
	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	/**
	 * 修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	/**
	 * 删除标识
	 */
	@Column(name = "DELETEFLAG_")
	protected int deleteFlag;

	public Employee() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public String getBirthdayString() {
		if (this.birthday != null) {
			return DateUtils.getDateTime(this.birthday);
		}
		return "";
	}

	public String getBirthPlace() {
		return this.birthPlace;
	}

	public String getCategory() {
		return this.category;
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

	public int getDeleteFlag() {
		return this.deleteFlag;
	}

	public String getEducation() {
		return this.education;
	}

	public String getEmployeeID() {
		return this.employeeID;
	}

	public String getHomeAddress() {
		return this.homeAddress;
	}

	public String getId() {
		return this.id;
	}

	public String getIdCardNo() {
		return this.idCardNo;
	}

	public Date getJoinDate() {
		return this.joinDate;
	}

	public String getJoinDateString() {
		if (this.joinDate != null) {
			return DateUtils.getDateTime(this.joinDate);
		}
		return "";
	}

	public String getMarryStatus() {
		return this.marryStatus;
	}

	public String getMobile() {
		return this.mobile;
	}

	public String getName() {
		return this.name;
	}

	public String getNation() {
		return this.nation;
	}

	public String getNationality() {
		return this.nationality;
	}

	public String getNatureAccount() {
		return this.natureAccount;
	}

	public String getNatureType() {
		return this.natureType;
	}

	public String getPosition() {
		return this.position;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getSeniority() {
		return this.seniority;
	}

	public String getSex() {
		return this.sex;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getType() {
		return this.type;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String getUpdateTimeString() {
		if (this.updateTime != null) {
			return DateUtils.getDateTime(this.updateTime);
		}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Employee jsonToObject(JSONObject jsonObject) {
		return EmployeeJsonFactory.jsonToObject(jsonObject);
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public void setMarryStatus(String marryStatus) {
		this.marryStatus = marryStatus;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public void setNatureAccount(String natureAccount) {
		this.natureAccount = natureAccount;
	}

	public void setNatureType(String natureType) {
		this.natureType = natureType;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSeniority(String seniority) {
		this.seniority = seniority;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public JSONObject toJsonObject() {
		return EmployeeJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return EmployeeJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
