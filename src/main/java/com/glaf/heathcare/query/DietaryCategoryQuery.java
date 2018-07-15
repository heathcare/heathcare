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

public class DietaryCategoryQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tenantIds;
	protected String nameLike;
	protected Integer season;
	protected String province;
	protected String provinceLike;
	protected String region;
	protected Long typeId;
	protected List<Long> typeIds;
	protected String peopleType;
	protected String sysFlag;
	protected String enableFlag;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public DietaryCategoryQuery() {

	}

	public DietaryCategoryQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public DietaryCategoryQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public DietaryCategoryQuery enableFlag(String enableFlag) {
		if (enableFlag == null) {
			throw new RuntimeException("enableFlag is null");
		}
		this.enableFlag = enableFlag;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getEnableFlag() {
		return enableFlag;
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

			if ("description".equals(sortColumn)) {
				orderBy = "E.DESCRIPTION_" + a_x;
			}

			if ("season".equals(sortColumn)) {
				orderBy = "E.SEASON_" + a_x;
			}

			if ("province".equals(sortColumn)) {
				orderBy = "E.PROVINCE_" + a_x;
			}

			if ("region".equals(sortColumn)) {
				orderBy = "E.REGION_" + a_x;
			}

			if ("typeId".equals(sortColumn)) {
				orderBy = "E.TYPEID_" + a_x;
			}

			if ("peopleType".equals(sortColumn)) {
				orderBy = "E.PEOPLETYPE_" + a_x;
			}

			if ("suitNo".equals(sortColumn)) {
				orderBy = "E.SUITNO_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

			if ("sysFlag".equals(sortColumn)) {
				orderBy = "E.SYSFLAG_" + a_x;
			}

			if ("enableFlag".equals(sortColumn)) {
				orderBy = "E.ENABLEFLAG_" + a_x;
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

		}
		return orderBy;
	}

	public String getPeopleType() {
		return peopleType;
	}

	public String getProvince() {
		return province;
	}

	public String getProvinceLike() {
		if (provinceLike != null && provinceLike.trim().length() > 0) {
			if (!provinceLike.startsWith("%")) {
				provinceLike = "%" + provinceLike;
			}
			if (!provinceLike.endsWith("%")) {
				provinceLike = provinceLike + "%";
			}
		}
		return provinceLike;
	}

	public String getRegion() {
		return region;
	}

	public Integer getSeason() {
		return season;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public String getTenantId() {
		return tenantId;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public Long getTypeId() {
		return typeId;
	}

	public List<Long> getTypeIds() {
		return typeIds;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("name", "NAME_");
		addColumn("description", "DESCRIPTION_");
		addColumn("season", "SEASON_");
		addColumn("province", "PROVINCE_");
		addColumn("region", "REGION_");
		addColumn("typeId", "TYPEID_");
		addColumn("peopleType", "PEOPLETYPE_");
		addColumn("suitNo", "SUITNO_");
		addColumn("sortNo", "SORTNO_");
		addColumn("sysFlag", "SYSFLAG_");
		addColumn("enableFlag", "ENABLEFLAG_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public DietaryCategoryQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public DietaryCategoryQuery peopleType(String peopleType) {
		if (peopleType == null) {
			throw new RuntimeException("peopleType is null");
		}
		this.peopleType = peopleType;
		return this;
	}

	public DietaryCategoryQuery province(String province) {
		if (province == null) {
			throw new RuntimeException("province is null");
		}
		this.province = province;
		return this;
	}

	public DietaryCategoryQuery provinceLike(String provinceLike) {
		if (provinceLike == null) {
			throw new RuntimeException("province is null");
		}
		this.provinceLike = provinceLike;
		return this;
	}

	public DietaryCategoryQuery region(String region) {
		if (region == null) {
			throw new RuntimeException("region is null");
		}
		this.region = region;
		return this;
	}

	public DietaryCategoryQuery season(Integer season) {
		if (season == null) {
			throw new RuntimeException("season is null");
		}
		this.season = season;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setPeopleType(String peopleType) {
		this.peopleType = peopleType;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setProvinceLike(String provinceLike) {
		this.provinceLike = provinceLike;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public void setTypeIds(List<Long> typeIds) {
		this.typeIds = typeIds;
	}

	public DietaryCategoryQuery sysFlag(String sysFlag) {
		if (sysFlag == null) {
			throw new RuntimeException("sysFlag is null");
		}
		this.sysFlag = sysFlag;
		return this;
	}

	public DietaryCategoryQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public DietaryCategoryQuery typeId(Long typeId) {
		if (typeId == null) {
			throw new RuntimeException("typeId is null");
		}
		this.typeId = typeId;
		return this;
	}

	public DietaryCategoryQuery typeIds(List<Long> typeIds) {
		if (typeIds == null) {
			throw new RuntimeException("typeIds is empty ");
		}
		this.typeIds = typeIds;
		return this;
	}

}