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

package com.glaf.modules.supplier.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class SupplierQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String nameLike;
	protected String shortName;
	protected String shortNameLike;
	protected String code;
	protected String codeLike;
	protected String materialLike;
	protected String keywordLike;
	protected Integer level;
	protected Integer levelGreaterThanOrEqual;
	protected Integer levelLessThanOrEqual;
	protected Long provinceId;
	protected Long cityId;
	protected Long areaId;
	protected Long townId;
	protected String principalLike;
	protected String telephoneLike;
	protected String addressLike;
	protected String property;
	protected String propertyLike;
	protected String mailLike;
	protected String legalPerson;
	protected String legalPersonLike;
	protected String taxpayerID;
	protected String taxpayerIDLike;
	protected String taxpayerIdentity;
	protected String taxpayerIdentityLike;
	protected String bankOfDeposit;
	protected String bankOfDepositLike;
	protected String bankAccount;
	protected String bankAccountLike;
	protected String type;
	protected String verify;
	protected String remarkLike;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public SupplierQuery() {

	}

	public SupplierQuery addressLike(String addressLike) {
		if (addressLike == null) {
			throw new RuntimeException("address is null");
		}
		this.addressLike = addressLike;
		return this;
	}

	public SupplierQuery areaId(Long areaId) {
		if (areaId == null) {
			throw new RuntimeException("areaId is null");
		}
		this.areaId = areaId;
		return this;
	}

	public SupplierQuery bankAccount(String bankAccount) {
		if (bankAccount == null) {
			throw new RuntimeException("bankAccount is null");
		}
		this.bankAccount = bankAccount;
		return this;
	}

	public SupplierQuery bankAccountLike(String bankAccountLike) {
		if (bankAccountLike == null) {
			throw new RuntimeException("bankAccount is null");
		}
		this.bankAccountLike = bankAccountLike;
		return this;
	}

	public SupplierQuery bankOfDeposit(String bankOfDeposit) {
		if (bankOfDeposit == null) {
			throw new RuntimeException("bankOfDeposit is null");
		}
		this.bankOfDeposit = bankOfDeposit;
		return this;
	}

	public SupplierQuery bankOfDepositLike(String bankOfDepositLike) {
		if (bankOfDepositLike == null) {
			throw new RuntimeException("bankOfDeposit is null");
		}
		this.bankOfDepositLike = bankOfDepositLike;
		return this;
	}

	public SupplierQuery cityId(Long cityId) {
		if (cityId == null) {
			throw new RuntimeException("cityId is null");
		}
		this.cityId = cityId;
		return this;
	}

	public SupplierQuery code(String code) {
		if (code == null) {
			throw new RuntimeException("code is null");
		}
		this.code = code;
		return this;
	}

	public SupplierQuery codeLike(String codeLike) {
		if (codeLike == null) {
			throw new RuntimeException("code is null");
		}
		this.codeLike = codeLike;
		return this;
	}

	public SupplierQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SupplierQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public String getAddressLike() {
		if (addressLike != null && addressLike.trim().length() > 0) {
			if (!addressLike.startsWith("%")) {
				addressLike = "%" + addressLike;
			}
			if (!addressLike.endsWith("%")) {
				addressLike = addressLike + "%";
			}
		}
		return addressLike;
	}

	public Long getAreaId() {
		return areaId;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public String getBankAccountLike() {
		if (bankAccountLike != null && bankAccountLike.trim().length() > 0) {
			if (!bankAccountLike.startsWith("%")) {
				bankAccountLike = "%" + bankAccountLike;
			}
			if (!bankAccountLike.endsWith("%")) {
				bankAccountLike = bankAccountLike + "%";
			}
		}
		return bankAccountLike;
	}

	public String getBankOfDeposit() {
		return bankOfDeposit;
	}

	public String getBankOfDepositLike() {
		if (bankOfDepositLike != null && bankOfDepositLike.trim().length() > 0) {
			if (!bankOfDepositLike.startsWith("%")) {
				bankOfDepositLike = "%" + bankOfDepositLike;
			}
			if (!bankOfDepositLike.endsWith("%")) {
				bankOfDepositLike = bankOfDepositLike + "%";
			}
		}
		return bankOfDepositLike;
	}

	public Long getCityId() {
		return cityId;
	}

	public String getCode() {
		return code;
	}

	public String getCodeLike() {
		if (codeLike != null && codeLike.trim().length() > 0) {
			if (!codeLike.startsWith("%")) {
				codeLike = "%" + codeLike;
			}
			if (!codeLike.endsWith("%")) {
				codeLike = codeLike + "%";
			}
		}
		return codeLike;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getKeywordLike() {
		if (keywordLike != null && keywordLike.trim().length() > 0) {
			if (!keywordLike.startsWith("%")) {
				keywordLike = "%" + keywordLike;
			}
			if (!keywordLike.endsWith("%")) {
				keywordLike = keywordLike + "%";
			}
		}
		return keywordLike;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public String getLegalPersonLike() {
		if (legalPersonLike != null && legalPersonLike.trim().length() > 0) {
			if (!legalPersonLike.startsWith("%")) {
				legalPersonLike = "%" + legalPersonLike;
			}
			if (!legalPersonLike.endsWith("%")) {
				legalPersonLike = legalPersonLike + "%";
			}
		}
		return legalPersonLike;
	}

	public Integer getLevel() {
		return level;
	}

	public Integer getLevelGreaterThanOrEqual() {
		return levelGreaterThanOrEqual;
	}

	public Integer getLevelLessThanOrEqual() {
		return levelLessThanOrEqual;
	}

	public String getMailLike() {
		if (mailLike != null && mailLike.trim().length() > 0) {
			if (!mailLike.startsWith("%")) {
				mailLike = "%" + mailLike;
			}
			if (!mailLike.endsWith("%")) {
				mailLike = mailLike + "%";
			}
		}
		return mailLike;
	}

	public String getMaterialLike() {
		if (materialLike != null && materialLike.trim().length() > 0) {
			if (!materialLike.startsWith("%")) {
				materialLike = "%" + materialLike;
			}
			if (!materialLike.endsWith("%")) {
				materialLike = materialLike + "%";
			}
		}
		return materialLike;
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

			if ("shortName".equals(sortColumn)) {
				orderBy = "E.SHORTNAME_" + a_x;
			}

			if ("code".equals(sortColumn)) {
				orderBy = "E.CODE_" + a_x;
			}

			if ("material".equals(sortColumn)) {
				orderBy = "E.MATERIAL_" + a_x;
			}

			if ("level".equals(sortColumn)) {
				orderBy = "E.LEVEL_" + a_x;
			}

			if ("province".equals(sortColumn)) {
				orderBy = "E.PROVINCE_" + a_x;
			}

			if ("provinceId".equals(sortColumn)) {
				orderBy = "E.PROVINCEID_" + a_x;
			}

			if ("city".equals(sortColumn)) {
				orderBy = "E.CITY_" + a_x;
			}

			if ("cityId".equals(sortColumn)) {
				orderBy = "E.CITYID_" + a_x;
			}

			if ("area".equals(sortColumn)) {
				orderBy = "E.AREA_" + a_x;
			}

			if ("areaId".equals(sortColumn)) {
				orderBy = "E.AREAID_" + a_x;
			}

			if ("town".equals(sortColumn)) {
				orderBy = "E.TOWN_" + a_x;
			}

			if ("townId".equals(sortColumn)) {
				orderBy = "E.TOWNID_" + a_x;
			}

			if ("principal".equals(sortColumn)) {
				orderBy = "E.PRINCIPAL_" + a_x;
			}

			if ("telephone".equals(sortColumn)) {
				orderBy = "E.TELEPHONE_" + a_x;
			}

			if ("address".equals(sortColumn)) {
				orderBy = "E.ADDRESS_" + a_x;
			}

			if ("property".equals(sortColumn)) {
				orderBy = "E.PROPERTY_" + a_x;
			}

			if ("mail".equals(sortColumn)) {
				orderBy = "E.MAIL_" + a_x;
			}

			if ("legalPerson".equals(sortColumn)) {
				orderBy = "E.LEGALPERSON_" + a_x;
			}

			if ("taxpayerID".equals(sortColumn)) {
				orderBy = "E.TAXPAYERID_" + a_x;
			}

			if ("taxpayerIdentity".equals(sortColumn)) {
				orderBy = "E.TAXPAYERIDENTITY_" + a_x;
			}

			if ("bankOfDeposit".equals(sortColumn)) {
				orderBy = "E.BANKOFDEPOSIT_" + a_x;
			}

			if ("bankAccount".equals(sortColumn)) {
				orderBy = "E.BANKACCOUNT_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("verify".equals(sortColumn)) {
				orderBy = "E.VERIFY_" + a_x;
			}

			if ("remark".equals(sortColumn)) {
				orderBy = "E.REMARK_" + a_x;
			}

			if ("locked".equals(sortColumn)) {
				orderBy = "E.LOCKED_" + a_x;
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

	public String getPrincipalLike() {
		if (principalLike != null && principalLike.trim().length() > 0) {
			if (!principalLike.startsWith("%")) {
				principalLike = "%" + principalLike;
			}
			if (!principalLike.endsWith("%")) {
				principalLike = principalLike + "%";
			}
		}
		return principalLike;
	}

	public String getProperty() {
		return property;
	}

	public String getPropertyLike() {
		if (propertyLike != null && propertyLike.trim().length() > 0) {
			if (!propertyLike.startsWith("%")) {
				propertyLike = "%" + propertyLike;
			}
			if (!propertyLike.endsWith("%")) {
				propertyLike = propertyLike + "%";
			}
		}
		return propertyLike;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public String getRemarkLike() {
		if (remarkLike != null && remarkLike.trim().length() > 0) {
			if (!remarkLike.startsWith("%")) {
				remarkLike = "%" + remarkLike;
			}
			if (!remarkLike.endsWith("%")) {
				remarkLike = remarkLike + "%";
			}
		}
		return remarkLike;
	}

	public String getShortName() {
		return shortName;
	}

	public String getShortNameLike() {
		if (shortNameLike != null && shortNameLike.trim().length() > 0) {
			if (!shortNameLike.startsWith("%")) {
				shortNameLike = "%" + shortNameLike;
			}
			if (!shortNameLike.endsWith("%")) {
				shortNameLike = shortNameLike + "%";
			}
		}
		return shortNameLike;
	}

	public String getTaxpayerID() {
		return taxpayerID;
	}

	public String getTaxpayerIdentity() {
		return taxpayerIdentity;
	}

	public String getTaxpayerIdentityLike() {
		if (taxpayerIdentityLike != null && taxpayerIdentityLike.trim().length() > 0) {
			if (!taxpayerIdentityLike.startsWith("%")) {
				taxpayerIdentityLike = "%" + taxpayerIdentityLike;
			}
			if (!taxpayerIdentityLike.endsWith("%")) {
				taxpayerIdentityLike = taxpayerIdentityLike + "%";
			}
		}
		return taxpayerIdentityLike;
	}

	public String getTaxpayerIDLike() {
		if (taxpayerIDLike != null && taxpayerIDLike.trim().length() > 0) {
			if (!taxpayerIDLike.startsWith("%")) {
				taxpayerIDLike = "%" + taxpayerIDLike;
			}
			if (!taxpayerIDLike.endsWith("%")) {
				taxpayerIDLike = taxpayerIDLike + "%";
			}
		}
		return taxpayerIDLike;
	}

	public String getTelephoneLike() {
		if (telephoneLike != null && telephoneLike.trim().length() > 0) {
			if (!telephoneLike.startsWith("%")) {
				telephoneLike = "%" + telephoneLike;
			}
			if (!telephoneLike.endsWith("%")) {
				telephoneLike = telephoneLike + "%";
			}
		}
		return telephoneLike;
	}

	public Long getTownId() {
		return townId;
	}

	public String getType() {
		return type;
	}

	public String getVerify() {
		return verify;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("name", "NAME_");
		addColumn("shortName", "SHORTNAME_");
		addColumn("code", "CODE_");
		addColumn("material", "MATERIAL_");
		addColumn("level", "LEVEL_");
		addColumn("province", "PROVINCE_");
		addColumn("provinceId", "PROVINCEID_");
		addColumn("city", "CITY_");
		addColumn("cityId", "CITYID_");
		addColumn("area", "AREA_");
		addColumn("areaId", "AREAID_");
		addColumn("town", "TOWN_");
		addColumn("townId", "TOWNID_");
		addColumn("principal", "PRINCIPAL_");
		addColumn("telephone", "TELEPHONE_");
		addColumn("address", "ADDRESS_");
		addColumn("property", "PROPERTY_");
		addColumn("mail", "MAIL_");
		addColumn("legalPerson", "LEGALPERSON_");
		addColumn("taxpayerID", "TAXPAYERID_");
		addColumn("taxpayerIdentity", "TAXPAYERIDENTITY_");
		addColumn("bankOfDeposit", "BANKOFDEPOSIT_");
		addColumn("bankAccount", "BANKACCOUNT_");
		addColumn("type", "TYPE_");
		addColumn("verify", "VERIFY_");
		addColumn("remark", "REMARK_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public SupplierQuery keywordLike(String keywordLike) {
		if (keywordLike == null) {
			throw new RuntimeException("keyword is null");
		}
		this.keywordLike = keywordLike;
		return this;
	}

	public SupplierQuery legalPerson(String legalPerson) {
		if (legalPerson == null) {
			throw new RuntimeException("legalPerson is null");
		}
		this.legalPerson = legalPerson;
		return this;
	}

	public SupplierQuery legalPersonLike(String legalPersonLike) {
		if (legalPersonLike == null) {
			throw new RuntimeException("legalPerson is null");
		}
		this.legalPersonLike = legalPersonLike;
		return this;
	}

	public SupplierQuery level(Integer level) {
		if (level == null) {
			throw new RuntimeException("level is null");
		}
		this.level = level;
		return this;
	}

	public SupplierQuery levelGreaterThanOrEqual(Integer levelGreaterThanOrEqual) {
		if (levelGreaterThanOrEqual == null) {
			throw new RuntimeException("level is null");
		}
		this.levelGreaterThanOrEqual = levelGreaterThanOrEqual;
		return this;
	}

	public SupplierQuery levelLessThanOrEqual(Integer levelLessThanOrEqual) {
		if (levelLessThanOrEqual == null) {
			throw new RuntimeException("level is null");
		}
		this.levelLessThanOrEqual = levelLessThanOrEqual;
		return this;
	}

	public SupplierQuery locked(Integer locked) {
		if (locked == null) {
			throw new RuntimeException("locked is null");
		}
		this.locked = locked;
		return this;
	}

	public SupplierQuery mailLike(String mailLike) {
		if (mailLike == null) {
			throw new RuntimeException("mail is null");
		}
		this.mailLike = mailLike;
		return this;
	}

	public SupplierQuery materialLike(String materialLike) {
		if (materialLike == null) {
			throw new RuntimeException("material is null");
		}
		this.materialLike = materialLike;
		return this;
	}

	public SupplierQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public SupplierQuery principalLike(String principalLike) {
		if (principalLike == null) {
			throw new RuntimeException("principal is null");
		}
		this.principalLike = principalLike;
		return this;
	}

	public SupplierQuery property(String property) {
		if (property == null) {
			throw new RuntimeException("property is null");
		}
		this.property = property;
		return this;
	}

	public SupplierQuery propertyLike(String propertyLike) {
		if (propertyLike == null) {
			throw new RuntimeException("property is null");
		}
		this.propertyLike = propertyLike;
		return this;
	}

	public SupplierQuery provinceId(Long provinceId) {
		if (provinceId == null) {
			throw new RuntimeException("provinceId is null");
		}
		this.provinceId = provinceId;
		return this;
	}

	public SupplierQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public void setAddressLike(String addressLike) {
		this.addressLike = addressLike;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public void setBankAccountLike(String bankAccountLike) {
		this.bankAccountLike = bankAccountLike;
	}

	public void setBankOfDeposit(String bankOfDeposit) {
		this.bankOfDeposit = bankOfDeposit;
	}

	public void setBankOfDepositLike(String bankOfDepositLike) {
		this.bankOfDepositLike = bankOfDepositLike;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCodeLike(String codeLike) {
		this.codeLike = codeLike;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setKeywordLike(String keywordLike) {
		this.keywordLike = keywordLike;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public void setLegalPersonLike(String legalPersonLike) {
		this.legalPersonLike = legalPersonLike;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setLevelGreaterThanOrEqual(Integer levelGreaterThanOrEqual) {
		this.levelGreaterThanOrEqual = levelGreaterThanOrEqual;
	}

	public void setLevelLessThanOrEqual(Integer levelLessThanOrEqual) {
		this.levelLessThanOrEqual = levelLessThanOrEqual;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	public void setMailLike(String mailLike) {
		this.mailLike = mailLike;
	}

	public void setMaterialLike(String materialLike) {
		this.materialLike = materialLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setPrincipalLike(String principalLike) {
		this.principalLike = principalLike;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setPropertyLike(String propertyLike) {
		this.propertyLike = propertyLike;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setShortNameLike(String shortNameLike) {
		this.shortNameLike = shortNameLike;
	}

	public void setTaxpayerID(String taxpayerID) {
		this.taxpayerID = taxpayerID;
	}

	public void setTaxpayerIdentity(String taxpayerIdentity) {
		this.taxpayerIdentity = taxpayerIdentity;
	}

	public void setTaxpayerIdentityLike(String taxpayerIdentityLike) {
		this.taxpayerIdentityLike = taxpayerIdentityLike;
	}

	public void setTaxpayerIDLike(String taxpayerIDLike) {
		this.taxpayerIDLike = taxpayerIDLike;
	}

	public void setTelephoneLike(String telephoneLike) {
		this.telephoneLike = telephoneLike;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public SupplierQuery shortName(String shortName) {
		if (shortName == null) {
			throw new RuntimeException("shortName is null");
		}
		this.shortName = shortName;
		return this;
	}

	public SupplierQuery shortNameLike(String shortNameLike) {
		if (shortNameLike == null) {
			throw new RuntimeException("shortName is null");
		}
		this.shortNameLike = shortNameLike;
		return this;
	}

	public SupplierQuery taxpayerID(String taxpayerID) {
		if (taxpayerID == null) {
			throw new RuntimeException("taxpayerID is null");
		}
		this.taxpayerID = taxpayerID;
		return this;
	}

	public SupplierQuery taxpayerIdentity(String taxpayerIdentity) {
		if (taxpayerIdentity == null) {
			throw new RuntimeException("taxpayerIdentity is null");
		}
		this.taxpayerIdentity = taxpayerIdentity;
		return this;
	}

	public SupplierQuery taxpayerIdentityLike(String taxpayerIdentityLike) {
		if (taxpayerIdentityLike == null) {
			throw new RuntimeException("taxpayerIdentity is null");
		}
		this.taxpayerIdentityLike = taxpayerIdentityLike;
		return this;
	}

	public SupplierQuery taxpayerIDLike(String taxpayerIDLike) {
		if (taxpayerIDLike == null) {
			throw new RuntimeException("taxpayerID is null");
		}
		this.taxpayerIDLike = taxpayerIDLike;
		return this;
	}

	public SupplierQuery telephoneLike(String telephoneLike) {
		if (telephoneLike == null) {
			throw new RuntimeException("telephone is null");
		}
		this.telephoneLike = telephoneLike;
		return this;
	}

	public SupplierQuery townId(Long townId) {
		if (townId == null) {
			throw new RuntimeException("townId is null");
		}
		this.townId = townId;
		return this;
	}

	public SupplierQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public SupplierQuery verify(String verify) {
		if (verify == null) {
			throw new RuntimeException("verify is null");
		}
		this.verify = verify;
		return this;
	}

}