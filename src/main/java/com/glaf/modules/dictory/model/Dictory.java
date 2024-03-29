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

package com.glaf.modules.dictory.model;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.glaf.modules.dictory.util.*;
import com.glaf.core.base.JSONable;

@Entity(name = "TDictory")
@Table(name = "T_DICTORY")
public class Dictory implements Serializable, JSONable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	protected long id;

	/**
	 * 所属租户
	 */
	@Column(name = "TENANTID")
	protected String tenantId;

	/**
	 * 使用领域
	 */
	@Column(name = "CATEGORY", length = 50)
	protected String category;

	/**
	 * 编码
	 */
	@Column(name = "CODE", length = 50)
	protected String code;

	/**
	 * 描述
	 */
	@Column(name = "DICTDESC", length = 500)
	protected String desc;

	/**
	 * 名称
	 */
	@Column(name = "NAME", length = 50)
	protected String name;

	/**
	 * 类型编号
	 */
	@Column(name = "TYPEID")
	protected long nodeId;

	/**
	 * 序号
	 */
	@Column(name = "SORTNO")
	protected int sort;

	/**
	 * 字符串值1
	 */
	@Column(name = "EXT1", length = 200)
	protected String ext1;

	/**
	 * 日期值10
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXT10")
	protected Date ext10;

	/**
	 * null
	 */
	@Column(name = "EXT11")
	protected Long ext11;

	/**
	 * null
	 */
	@Column(name = "EXT12")
	protected Long ext12;

	/**
	 * null
	 */
	@Column(name = "EXT13")
	protected Long ext13;

	/**
	 * null
	 */
	@Column(name = "EXT14")
	protected Long ext14;

	/**
	 * null
	 */
	@Column(name = "EXT15")
	protected Long ext15;

	/**
	 * null
	 */
	@Column(name = "EXT16")
	protected Double ext16;

	/**
	 * null
	 */
	@Column(name = "EXT17")
	protected Double ext17;

	/**
	 * null
	 */
	@Column(name = "EXT18")
	protected Double ext18;

	/**
	 * null
	 */
	@Column(name = "EXT19")
	protected Double ext19;

	/**
	 * 字符串值2
	 */
	@Column(name = "EXT2", length = 200)
	protected String ext2;

	/**
	 * null
	 */
	@Column(name = "EXT20")
	protected Double ext20;

	/**
	 * 字符串值3
	 */
	@Column(name = "EXT3", length = 200)
	protected String ext3;

	/**
	 * 字符串值4
	 */
	@Column(name = "EXT4", length = 200)
	protected String ext4;

	/**
	 * 日期值5
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXT5")
	protected Date ext5;

	/**
	 * 日期值6
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXT6")
	protected Date ext6;

	/**
	 * 日期值7
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXT7")
	protected Date ext7;

	/**
	 * 日期值8
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXT8")
	protected Date ext8;

	/**
	 * 日期值9
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXT9")
	protected Date ext9;

	/**
	 * 值
	 */
	@Column(name = "VALUE_", length = 2000)
	protected String value;

	/**
	 * 是否启用
	 */
	@Column(name = "LOCKED")
	protected int locked;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE")
	protected Date createDate;

	/**
	 * 修改人
	 */
	@Column(name = "UPDATEBY", length = 50)
	protected String updateBy;

	/**
	 * 修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATEDATE")
	protected Date updateDate;

	public Dictory() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dictory other = (Dictory) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getCategory() {
		return category;
	}

	public String getCode() {
		return this.code;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getDesc() {
		return this.desc;
	}

	public String getExt1() {
		return this.ext1;
	}

	public Date getExt10() {
		return this.ext10;
	}

	public Long getExt11() {
		return this.ext11;
	}

	public Long getExt12() {
		return this.ext12;
	}

	public Long getExt13() {
		return this.ext13;
	}

	public Long getExt14() {
		return this.ext14;
	}

	public Long getExt15() {
		return this.ext15;
	}

	public Double getExt16() {
		return this.ext16;
	}

	public Double getExt17() {
		return this.ext17;
	}

	public Double getExt18() {
		return this.ext18;
	}

	public Double getExt19() {
		return this.ext19;
	}

	public String getExt2() {
		return this.ext2;
	}

	public Double getExt20() {
		return this.ext20;
	}

	public String getExt3() {
		return this.ext3;
	}

	public String getExt4() {
		return this.ext4;
	}

	public Date getExt5() {
		return this.ext5;
	}

	public Date getExt6() {
		return this.ext6;
	}

	public Date getExt7() {
		return this.ext7;
	}

	public Date getExt8() {
		return this.ext8;
	}

	public Date getExt9() {
		return this.ext9;
	}

	public long getId() {
		return this.id;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getName() {
		return this.name;
	}

	public long getNodeId() {
		return this.nodeId;
	}

	public int getSort() {
		return this.sort;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public Dictory jsonToObject(JSONObject jsonObject) {
		return DictoryJsonFactory.jsonToObject(jsonObject);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public void setExt10(Date ext10) {
		this.ext10 = ext10;
	}

	public void setExt11(Long ext11) {
		this.ext11 = ext11;
	}

	public void setExt12(Long ext12) {
		this.ext12 = ext12;
	}

	public void setExt13(Long ext13) {
		this.ext13 = ext13;
	}

	public void setExt14(Long ext14) {
		this.ext14 = ext14;
	}

	public void setExt15(Long ext15) {
		this.ext15 = ext15;
	}

	public void setExt16(Double ext16) {
		this.ext16 = ext16;
	}

	public void setExt17(Double ext17) {
		this.ext17 = ext17;
	}

	public void setExt18(Double ext18) {
		this.ext18 = ext18;
	}

	public void setExt19(Double ext19) {
		this.ext19 = ext19;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public void setExt20(Double ext20) {
		this.ext20 = ext20;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public void setExt5(Date ext5) {
		this.ext5 = ext5;
	}

	public void setExt6(Date ext6) {
		this.ext6 = ext6;
	}

	public void setExt7(Date ext7) {
		this.ext7 = ext7;
	}

	public void setExt8(Date ext8) {
		this.ext8 = ext8;
	}

	public void setExt9(Date ext9) {
		this.ext9 = ext9;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public JSONObject toJsonObject() {
		return DictoryJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DictoryJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
