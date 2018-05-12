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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.base.JSONable;
import com.glaf.heathcare.util.FoodFavoriteJsonFactory;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "HEALTH_FOOD_FAVORITE")
public class FoodFavorite implements Serializable, JSONable {
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
	 * 食物编号
	 */
	@Column(name = "FOODID_")
	protected long foodId;

	@javax.persistence.Transient
	protected String name;

	/**
	 * 分类编号
	 */
	@Column(name = "NODEID_")
	protected long nodeId;

	/**
	 * 子分类编号
	 */
	@Column(name = "SUBNODEID_")
	protected long subNodeId;

	/**
	 * 排序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	public FoodFavorite() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodFavorite other = (FoodFavorite) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public long getFoodId() {
		return this.foodId;
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public long getNodeId() {
		return this.nodeId;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public long getSubNodeId() {
		return this.subNodeId;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public FoodFavorite jsonToObject(JSONObject jsonObject) {
		return FoodFavoriteJsonFactory.jsonToObject(jsonObject);
	}

	public void setFoodId(long foodId) {
		this.foodId = foodId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSubNodeId(long subNodeId) {
		this.subNodeId = subNodeId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public JSONObject toJsonObject() {
		return FoodFavoriteJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return FoodFavoriteJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
