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
@Table(name = "HEALTH_DIETARY_CATEGORY")
public class DietaryCategory implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION_", length = 4000)
	protected String description;

	/**
	 * 季节编号
	 */
	@Column(name = "SEASON_")
	protected int season;

	/**
	 * 省份
	 */
	@Column(name = "PROVINCE_", length = 50)
	protected String province;

	/**
	 * 地域
	 */
	@Column(name = "REGION_", length = 50)
	protected String region;

	/**
	 * 餐点类别编号
	 */
	@Column(name = "TYPEID_")
	protected long typeId;

	/**
	 * 人群类型
	 */
	@Column(name = "PEOPLETYPE_", length = 50)
	protected String peopleType;

	/**
	 * 模板套编号
	 */
	@Column(name = "SUITNO_")
	protected int suitNo;

	/**
	 * 排序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * 系统内置
	 */
	@Column(name = "SYSFLAG_", length = 1)
	protected String sysFlag;

	/**
	 * 是否有效
	 */
	@Column(name = "ENABLEFLAG_", length = 1)
	protected String enableFlag;

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

	public DietaryCategory() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DietaryCategory other = (DietaryCategory) obj;
		if (id != other.id)
			return false;
		return true;
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

	public String getDescription() {
		return this.description;
	}

	public String getEnableFlag() {
		return this.enableFlag;
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getPeopleType() {
		return this.peopleType;
	}

	public String getProvince() {
		return this.province;
	}

	public String getRegion() {
		return this.region;
	}

	public int getSeason() {
		return this.season;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public int getSuitNo() {
		return this.suitNo;
	}

	public String getSysFlag() {
		return this.sysFlag;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public long getTypeId() {
		return this.typeId;
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
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public DietaryCategory jsonToObject(JSONObject jsonObject) {
		return DietaryCategoryJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPeopleType(String peopleType) {
		this.peopleType = peopleType;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSuitNo(int suitNo) {
		this.suitNo = suitNo;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public JSONObject toJsonObject() {
		return DietaryCategoryJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DietaryCategoryJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
