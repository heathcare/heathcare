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

package com.glaf.modules.supplier.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.modules.supplier.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_SUPPLIER")
public class Supplier implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SUPPLIERID_", length = 50, nullable = false)
	protected String supplierId;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 供应商名称
	 */
	@Column(name = "NAME_", length = 500)
	protected String name;

	/**
	 * 供应商简称
	 */
	@Column(name = "SHORTNAME_", length = 200)
	protected String shortName;

	/**
	 * 供应商代码
	 */
	@Column(name = "CODE_", length = 50)
	protected String code;

	/**
	 * 供应材料
	 */
	@Column(name = "MATERIAL_", length = 500)
	protected String material;

	/**
	 * 等级
	 */
	@Column(name = "LEVEL_")
	protected int level;

	/**
	 * 省/直辖市
	 */
	@Column(name = "PROVINCE_", length = 100)
	protected String province;

	/**
	 * 省/直辖市编号
	 */
	@Column(name = "PROVINCEID_")
	protected long provinceId;

	/**
	 * 市
	 */
	@Column(name = "CITY_", length = 100)
	protected String city;

	/**
	 * 市编号
	 */
	@Column(name = "CITYID_")
	protected long cityId;

	/**
	 * 区/县
	 */
	@Column(name = "AREA_", length = 100)
	protected String area;

	/**
	 * 区/县编号
	 */
	@Column(name = "AREAID_")
	protected long areaId;

	/**
	 * 镇/街道
	 */
	@Column(name = "TOWN_", length = 200)
	protected String town;

	/**
	 * 镇/街道编号
	 */
	@Column(name = "TOWNID_")
	protected long townId;

	/**
	 * 负责人
	 */
	@Column(name = "PRINCIPAL_", length = 250)
	protected String principal;

	/**
	 * 电话
	 */
	@Column(name = "TELEPHONE_", length = 200)
	protected String telephone;

	/**
	 * 地址
	 */
	@Column(name = "ADDRESS_", length = 200)
	protected String address;

	/**
	 * 性质
	 */
	@Column(name = "PROPERTY_", length = 50)
	protected String property;

	/**
	 * 电子邮件
	 */
	@Column(name = "MAIL_", length = 100)
	protected String mail;

	/**
	 * 企业法人姓名
	 */
	@Column(name = "LEGALPERSON_", length = 50)
	protected String legalPerson;

	/**
	 * 纳税人识别号
	 */
	@Column(name = "TAXPAYERID_", length = 50)
	protected String taxpayerID;

	/**
	 * 纳税人身份
	 */
	@Column(name = "TAXPAYERIDENTITY_", length = 50)
	protected String taxpayerIdentity;

	/**
	 * 收款开户银行全称
	 */
	@Column(name = "BANKOFDEPOSIT_", length = 250)
	protected String bankOfDeposit;

	/**
	 * 收款银行账号
	 */
	@Column(name = "BANKACCOUNT_", length = 50)
	protected String bankAccount;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 认证标识
	 */
	@Column(name = "VERIFY_", length = 1)
	protected String verify;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 500)
	protected String remark;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED_")
	protected int locked;

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

	public Supplier() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Supplier other = (Supplier) obj;
		if (supplierId == null) {
			if (other.supplierId != null)
				return false;
		} else if (!supplierId.equals(other.supplierId))
			return false;
		return true;
	}

	public String getAddress() {
		return this.address;
	}

	public String getArea() {
		return this.area;
	}

	public long getAreaId() {
		return this.areaId;
	}

	public String getBankAccount() {
		return this.bankAccount;
	}

	public String getBankOfDeposit() {
		return this.bankOfDeposit;
	}

	public String getCity() {
		return this.city;
	}

	public long getCityId() {
		return this.cityId;
	}

	public String getCode() {
		return this.code;
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

	public String getLegalPerson() {
		return this.legalPerson;
	}

	public int getLevel() {
		return this.level;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getMail() {
		return this.mail;
	}

	public String getMaterial() {
		return this.material;
	}

	public String getName() {
		return this.name;
	}

	public String getPrincipal() {
		return this.principal;
	}

	public String getProperty() {
		return this.property;
	}

	public String getProvince() {
		return this.province;
	}

	public long getProvinceId() {
		return this.provinceId;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getShortName() {
		return this.shortName;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public String getTaxpayerID() {
		return this.taxpayerID;
	}

	public String getTaxpayerIdentity() {
		return this.taxpayerIdentity;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getTown() {
		return this.town;
	}

	public long getTownId() {
		return this.townId;
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

	public String getVerify() {
		return this.verify;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((supplierId == null) ? 0 : supplierId.hashCode());
		return result;
	}

	public Supplier jsonToObject(JSONObject jsonObject) {
		return SupplierJsonFactory.jsonToObject(jsonObject);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public void setBankOfDeposit(String bankOfDeposit) {
		this.bankOfDeposit = bankOfDeposit;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public void setTaxpayerID(String taxpayerID) {
		this.taxpayerID = taxpayerID;
	}

	public void setTaxpayerIdentity(String taxpayerIdentity) {
		this.taxpayerIdentity = taxpayerIdentity;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public void setTownId(long townId) {
		this.townId = townId;
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

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public JSONObject toJsonObject() {
		return SupplierJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SupplierJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
