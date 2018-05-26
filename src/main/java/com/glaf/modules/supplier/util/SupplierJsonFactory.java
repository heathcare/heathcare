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

package com.glaf.modules.supplier.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.modules.supplier.domain.Supplier;

/**
 * 
 * JSON工厂类
 *
 */
public class SupplierJsonFactory {

	public static Supplier jsonToObject(JSONObject jsonObject) {
		Supplier model = new Supplier();
		if (jsonObject.containsKey("supplierId")) {
			model.setSupplierId(jsonObject.getString("supplierId"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("shortName")) {
			model.setShortName(jsonObject.getString("shortName"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
		}
		if (jsonObject.containsKey("material")) {
			model.setMaterial(jsonObject.getString("material"));
		}
		if (jsonObject.containsKey("level")) {
			model.setLevel(jsonObject.getInteger("level"));
		}
		if (jsonObject.containsKey("province")) {
			model.setProvince(jsonObject.getString("province"));
		}
		if (jsonObject.containsKey("provinceId")) {
			model.setProvinceId(jsonObject.getLong("provinceId"));
		}
		if (jsonObject.containsKey("city")) {
			model.setCity(jsonObject.getString("city"));
		}
		if (jsonObject.containsKey("cityId")) {
			model.setCityId(jsonObject.getLong("cityId"));
		}
		if (jsonObject.containsKey("area")) {
			model.setArea(jsonObject.getString("area"));
		}
		if (jsonObject.containsKey("areaId")) {
			model.setAreaId(jsonObject.getLong("areaId"));
		}
		if (jsonObject.containsKey("town")) {
			model.setTown(jsonObject.getString("town"));
		}
		if (jsonObject.containsKey("townId")) {
			model.setTownId(jsonObject.getLong("townId"));
		}
		if (jsonObject.containsKey("principal")) {
			model.setPrincipal(jsonObject.getString("principal"));
		}
		if (jsonObject.containsKey("telephone")) {
			model.setTelephone(jsonObject.getString("telephone"));
		}
		if (jsonObject.containsKey("address")) {
			model.setAddress(jsonObject.getString("address"));
		}
		if (jsonObject.containsKey("property")) {
			model.setProperty(jsonObject.getString("property"));
		}
		if (jsonObject.containsKey("mail")) {
			model.setMail(jsonObject.getString("mail"));
		}
		if (jsonObject.containsKey("legalPerson")) {
			model.setLegalPerson(jsonObject.getString("legalPerson"));
		}
		if (jsonObject.containsKey("taxpayerID")) {
			model.setTaxpayerID(jsonObject.getString("taxpayerID"));
		}
		if (jsonObject.containsKey("taxpayerIdentity")) {
			model.setTaxpayerIdentity(jsonObject.getString("taxpayerIdentity"));
		}
		if (jsonObject.containsKey("bankOfDeposit")) {
			model.setBankOfDeposit(jsonObject.getString("bankOfDeposit"));
		}
		if (jsonObject.containsKey("bankAccount")) {
			model.setBankAccount(jsonObject.getString("bankAccount"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("verify")) {
			model.setVerify(jsonObject.getString("verify"));
		}
		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}
		if (jsonObject.containsKey("updateBy")) {
			model.setUpdateBy(jsonObject.getString("updateBy"));
		}
		if (jsonObject.containsKey("updateTime")) {
			model.setUpdateTime(jsonObject.getDate("updateTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(Supplier model) {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("id", model.getSupplierId());
		jsonObject.put("_id_", model.getSupplierId());
		jsonObject.put("_oid_", model.getSupplierId());
		jsonObject.put("supplierId", model.getSupplierId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getShortName() != null) {
			jsonObject.put("shortName", model.getShortName());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getMaterial() != null) {
			jsonObject.put("material", model.getMaterial());
		}
		jsonObject.put("level", model.getLevel());
		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		jsonObject.put("provinceId", model.getProvinceId());
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
		}
		jsonObject.put("cityId", model.getCityId());
		if (model.getArea() != null) {
			jsonObject.put("area", model.getArea());
		}
		jsonObject.put("areaId", model.getAreaId());
		if (model.getTown() != null) {
			jsonObject.put("town", model.getTown());
		}
		jsonObject.put("townId", model.getTownId());
		if (model.getPrincipal() != null) {
			jsonObject.put("principal", model.getPrincipal());
		}
		if (model.getTelephone() != null) {
			jsonObject.put("telephone", model.getTelephone());
		}
		if (model.getAddress() != null) {
			jsonObject.put("address", model.getAddress());
		}
		if (model.getProperty() != null) {
			jsonObject.put("property", model.getProperty());
		}
		if (model.getMail() != null) {
			jsonObject.put("mail", model.getMail());
		}
		if (model.getLegalPerson() != null) {
			jsonObject.put("legalPerson", model.getLegalPerson());
		}
		if (model.getTaxpayerID() != null) {
			jsonObject.put("taxpayerID", model.getTaxpayerID());
		}
		if (model.getTaxpayerIdentity() != null) {
			jsonObject.put("taxpayerIdentity", model.getTaxpayerIdentity());
		}
		if (model.getBankOfDeposit() != null) {
			jsonObject.put("bankOfDeposit", model.getBankOfDeposit());
		}
		if (model.getBankAccount() != null) {
			jsonObject.put("bankAccount", model.getBankAccount());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getVerify() != null) {
			jsonObject.put("verify", model.getVerify());
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		jsonObject.put("locked", model.getLocked());
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(Supplier model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getSupplierId());
		jsonObject.put("_id_", model.getSupplierId());
		jsonObject.put("_oid_", model.getSupplierId());
		jsonObject.put("supplierId", model.getSupplierId());

		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getShortName() != null) {
			jsonObject.put("shortName", model.getShortName());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getMaterial() != null) {
			jsonObject.put("material", model.getMaterial());
		}
		jsonObject.put("level", model.getLevel());
		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		jsonObject.put("provinceId", model.getProvinceId());
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
		}
		jsonObject.put("cityId", model.getCityId());
		if (model.getArea() != null) {
			jsonObject.put("area", model.getArea());
		}
		jsonObject.put("areaId", model.getAreaId());
		if (model.getTown() != null) {
			jsonObject.put("town", model.getTown());
		}
		jsonObject.put("townId", model.getTownId());
		if (model.getPrincipal() != null) {
			jsonObject.put("principal", model.getPrincipal());
		}
		if (model.getTelephone() != null) {
			jsonObject.put("telephone", model.getTelephone());
		}
		if (model.getAddress() != null) {
			jsonObject.put("address", model.getAddress());
		}
		if (model.getProperty() != null) {
			jsonObject.put("property", model.getProperty());
		}
		if (model.getMail() != null) {
			jsonObject.put("mail", model.getMail());
		}
		if (model.getLegalPerson() != null) {
			jsonObject.put("legalPerson", model.getLegalPerson());
		}
		if (model.getTaxpayerID() != null) {
			jsonObject.put("taxpayerID", model.getTaxpayerID());
		}
		if (model.getTaxpayerIdentity() != null) {
			jsonObject.put("taxpayerIdentity", model.getTaxpayerIdentity());
		}
		if (model.getBankOfDeposit() != null) {
			jsonObject.put("bankOfDeposit", model.getBankOfDeposit());
		}
		if (model.getBankAccount() != null) {
			jsonObject.put("bankAccount", model.getBankAccount());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getVerify() != null) {
			jsonObject.put("verify", model.getVerify());
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		jsonObject.put("locked", model.getLocked());
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<Supplier> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (Supplier model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<Supplier> arrayToList(JSONArray array) {
		java.util.List<Supplier> list = new java.util.ArrayList<Supplier>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Supplier model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private SupplierJsonFactory() {

	}

}
