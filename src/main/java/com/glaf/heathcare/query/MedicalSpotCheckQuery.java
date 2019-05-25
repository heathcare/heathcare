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

import java.util.List;

import com.glaf.core.query.DataQuery;

public class MedicalSpotCheckQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> ids;
	protected String checkId;
	protected String gradeNameLike;
	protected String personId;
	protected String sex;
	protected String nation;
	protected String nationLike;
	protected String nationNotLike;
	protected Integer year;
	protected Integer month;
	protected Integer ageOfTheMoon;
	protected Integer ageOfTheMoonGreaterThanOrEqual;
	protected Integer ageOfTheMoonLessThanOrEqual;
	protected Integer heightLevelGreaterThanOrEqual;
	protected Integer heightLevelLessThanOrEqual;
	protected Integer weightLevelGreaterThanOrEqual;
	protected Integer weightLevelLessThanOrEqual;
	protected Integer weightLevel;
	protected String provinceLike;
	protected String cityLike;
	protected String areaLike;
	protected String organizationLike;
	protected String organizationLevelLike;
	protected String organizationPropertyLike;
	protected String organizationTerritoryLike;
	protected String type;
	protected String tableSuffix;

	public MedicalSpotCheckQuery() {

	}

	public MedicalSpotCheckQuery ageOfTheMoon(Integer ageOfTheMoon) {
		if (ageOfTheMoon == null) {
			throw new RuntimeException("ageOfTheMoon is null");
		}
		this.ageOfTheMoon = ageOfTheMoon;
		return this;
	}

	public MedicalSpotCheckQuery ageOfTheMoonGreaterThanOrEqual(Integer ageOfTheMoonGreaterThanOrEqual) {
		if (ageOfTheMoonGreaterThanOrEqual == null) {
			throw new RuntimeException("ageOfTheMoon is null");
		}
		this.ageOfTheMoonGreaterThanOrEqual = ageOfTheMoonGreaterThanOrEqual;
		return this;
	}

	public MedicalSpotCheckQuery ageOfTheMoonLessThanOrEqual(Integer ageOfTheMoonLessThanOrEqual) {
		if (ageOfTheMoonLessThanOrEqual == null) {
			throw new RuntimeException("ageOfTheMoon is null");
		}
		this.ageOfTheMoonLessThanOrEqual = ageOfTheMoonLessThanOrEqual;
		return this;
	}

	public MedicalSpotCheckQuery areaLike(String areaLike) {
		if (areaLike == null) {
			throw new RuntimeException("areaLike is null");
		}
		this.areaLike = areaLike;
		return this;
	}

	public MedicalSpotCheckQuery checkId(String checkId) {
		if (checkId == null) {
			throw new RuntimeException("checkId is null");
		}
		this.checkId = checkId;
		return this;
	}

	public MedicalSpotCheckQuery cityLike(String cityLike) {
		if (cityLike == null) {
			throw new RuntimeException("cityLike is null");
		}
		this.cityLike = cityLike;
		return this;
	}

	public Integer getAgeOfTheMoon() {
		return ageOfTheMoon;
	}

	public Integer getAgeOfTheMoonGreaterThanOrEqual() {
		return ageOfTheMoonGreaterThanOrEqual;
	}

	public Integer getAgeOfTheMoonLessThanOrEqual() {
		return ageOfTheMoonLessThanOrEqual;
	}

	public String getAreaLike() {
		if (areaLike != null && areaLike.trim().length() > 0) {
			if (!areaLike.startsWith("%")) {
				areaLike = "%" + areaLike;
			}
			if (!areaLike.endsWith("%")) {
				areaLike = areaLike + "%";
			}
		}
		return areaLike;
	}

	public String getCheckId() {
		return checkId;
	}

	public String getCityLike() {
		if (cityLike != null && cityLike.trim().length() > 0) {
			if (!cityLike.startsWith("%")) {
				cityLike = "%" + cityLike;
			}
			if (!cityLike.endsWith("%")) {
				cityLike = cityLike + "%";
			}
		}
		return cityLike;
	}

	public String getGradeNameLike() {
		if (gradeNameLike != null && gradeNameLike.trim().length() > 0) {
			if (!gradeNameLike.startsWith("%")) {
				gradeNameLike = "%" + gradeNameLike;
			}
			if (!gradeNameLike.endsWith("%")) {
				gradeNameLike = gradeNameLike + "%";
			}
		}
		return gradeNameLike;
	}

	public Integer getHeightLevelGreaterThanOrEqual() {
		return heightLevelGreaterThanOrEqual;
	}

	public Integer getHeightLevelLessThanOrEqual() {
		return heightLevelLessThanOrEqual;
	}

	public List<String> getIds() {
		return ids;
	}

	public Integer getMonth() {
		return month;
	}

	public String getNation() {
		return nation;
	}

	public String getNationLike() {
		if (nationLike != null && nationLike.trim().length() > 0) {
			if (!nationLike.startsWith("%")) {
				nationLike = "%" + nationLike;
			}
			if (!nationLike.endsWith("%")) {
				nationLike = nationLike + "%";
			}
		}
		return nationLike;
	}

	public String getNationNotLike() {
		return nationNotLike;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
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

			if ("nation".equals(sortColumn)) {
				orderBy = "E.NATION_" + a_x;
			}

			if ("height".equals(sortColumn)) {
				orderBy = "E.HEIGHT_" + a_x;
			}

			if ("heightLevel".equals(sortColumn)) {
				orderBy = "E.HEIGHTLEVEL_" + a_x;
			}

			if ("heightEvaluate".equals(sortColumn)) {
				orderBy = "E.HEIGHTEVALUATE_" + a_x;
			}

			if ("weight".equals(sortColumn)) {
				orderBy = "E.WEIGHT_" + a_x;
			}

			if ("weightLevel".equals(sortColumn)) {
				orderBy = "E.WEIGHTLEVEL_" + a_x;
			}

			if ("weightEvaluate".equals(sortColumn)) {
				orderBy = "E.WEIGHTEVALUATE_" + a_x;
			}

			if ("weightHeightPercent".equals(sortColumn)) {
				orderBy = "E.WEIGHTHEIGHTPERCENT_" + a_x;
			}

			if ("bmi".equals(sortColumn)) {
				orderBy = "E.BMI_" + a_x;
			}

			if ("bmiIndex".equals(sortColumn)) {
				orderBy = "E.BMIINDEX_" + a_x;
			}

			if ("bmiEvaluate".equals(sortColumn)) {
				orderBy = "E.BMIEVALUATE_" + a_x;
			}

			if ("eyesightLeft".equals(sortColumn)) {
				orderBy = "E.EYESIGHTLEFT_" + a_x;
			}

			if ("eyesightRight".equals(sortColumn)) {
				orderBy = "E.EYESIGHTRIGHT_" + a_x;
			}

			if ("hemoglobin".equals(sortColumn)) {
				orderBy = "E.HEMOGLOBIN_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("ageOfTheMoon".equals(sortColumn)) {
				orderBy = "E.AGEOFTHEMOON_" + a_x;
			}

			if ("ageOfTheMoonString".equals(sortColumn)) {
				orderBy = "E.AGEOFTHEMOONSTRING_" + a_x;
			}

			if ("province".equals(sortColumn)) {
				orderBy = "E.PROVINCE_" + a_x;
			}

			if ("city".equals(sortColumn)) {
				orderBy = "E.CITY_" + a_x;
			}

			if ("area".equals(sortColumn)) {
				orderBy = "E.AREA_" + a_x;
			}

			if ("organization".equals(sortColumn)) {
				orderBy = "E.ORGANIZATION_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("checkDate".equals(sortColumn)) {
				orderBy = "E.CHECKDATE_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getOrganizationLevelLike() {
		if (organizationLevelLike != null && organizationLevelLike.trim().length() > 0) {
			if (!organizationLevelLike.startsWith("%")) {
				organizationLevelLike = "%" + organizationLevelLike;
			}
			if (!organizationLevelLike.endsWith("%")) {
				organizationLevelLike = organizationLevelLike + "%";
			}
		}
		return organizationLevelLike;
	}

	public String getOrganizationLike() {
		if (organizationLike != null && organizationLike.trim().length() > 0) {
			if (!organizationLike.startsWith("%")) {
				organizationLike = "%" + organizationLike;
			}
			if (!organizationLike.endsWith("%")) {
				organizationLike = organizationLike + "%";
			}
		}
		return organizationLike;
	}

	public String getOrganizationPropertyLike() {
		if (organizationPropertyLike != null && organizationPropertyLike.trim().length() > 0) {
			if (!organizationPropertyLike.startsWith("%")) {
				organizationPropertyLike = "%" + organizationPropertyLike;
			}
			if (!organizationPropertyLike.endsWith("%")) {
				organizationPropertyLike = organizationPropertyLike + "%";
			}
		}
		return organizationPropertyLike;
	}

	public String getOrganizationTerritoryLike() {
		if (organizationTerritoryLike != null && organizationTerritoryLike.trim().length() > 0) {
			if (!organizationTerritoryLike.startsWith("%")) {
				organizationTerritoryLike = "%" + organizationTerritoryLike;
			}
			if (!organizationTerritoryLike.endsWith("%")) {
				organizationTerritoryLike = organizationTerritoryLike + "%";
			}
		}
		return organizationTerritoryLike;
	}

	public String getPersonId() {
		return personId;
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

	public String getSex() {
		return sex;
	}

	public String getTableSuffix() {
		if (tableSuffix == null) {
			tableSuffix = "";
		}
		return tableSuffix;
	}

	public String getType() {
		return type;
	}

	public Integer getWeightLevel() {
		return weightLevel;
	}

	public Integer getWeightLevelGreaterThanOrEqual() {
		return weightLevelGreaterThanOrEqual;
	}

	public Integer getWeightLevelLessThanOrEqual() {
		return weightLevelLessThanOrEqual;
	}

	public Integer getYear() {
		return year;
	}

	public MedicalSpotCheckQuery gradeNameLike(String gradeNameLike) {
		if (gradeNameLike == null) {
			throw new RuntimeException("gradeName is null");
		}
		this.gradeNameLike = gradeNameLike;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("name", "NAME_");
		addColumn("sex", "SEX_");
		addColumn("birthday", "BIRTHDAY_");
		addColumn("nation", "NATION_");
		addColumn("height", "HEIGHT_");
		addColumn("heightLevel", "HEIGHTLEVEL_");
		addColumn("heightEvaluate", "HEIGHTEVALUATE_");
		addColumn("weight", "WEIGHT_");
		addColumn("weightLevel", "WEIGHTLEVEL_");
		addColumn("weightEvaluate", "WEIGHTEVALUATE_");
		addColumn("weightHeightPercent", "WEIGHTHEIGHTPERCENT_");
		addColumn("bmi", "BMI_");
		addColumn("bmiIndex", "BMIINDEX_");
		addColumn("bmiEvaluate", "BMIEVALUATE_");
		addColumn("eyesightLeft", "EYESIGHTLEFT_");
		addColumn("eyesightRight", "EYESIGHTRIGHT_");
		addColumn("hemoglobin", "HEMOGLOBIN_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("ageOfTheMoon", "AGEOFTHEMOON_");
		addColumn("ageOfTheMoonString", "AGEOFTHEMOONSTRING_");
		addColumn("province", "PROVINCE_");
		addColumn("city", "CITY_");
		addColumn("area", "AREA_");
		addColumn("organization", "ORGANIZATION_");
		addColumn("type", "TYPE_");
		addColumn("checkDate", "CHECKDATE_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public MedicalSpotCheckQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public MedicalSpotCheckQuery nation(String nation) {
		if (nation == null) {
			throw new RuntimeException("nation is null");
		}
		this.nation = nation;
		return this;
	}

	public MedicalSpotCheckQuery nationLike(String nationLike) {
		if (nationLike == null) {
			throw new RuntimeException("nation is null");
		}
		this.nationLike = nationLike;
		return this;
	}

	public MedicalSpotCheckQuery organizationLevelLike(String organizationLevelLike) {
		if (organizationLevelLike == null) {
			throw new RuntimeException("organizationLevel is null");
		}
		this.organizationLevelLike = organizationLevelLike;
		return this;
	}

	public MedicalSpotCheckQuery organizationLike(String organizationLike) {
		if (organizationLike == null) {
			throw new RuntimeException("organization is null");
		}
		this.organizationLike = organizationLike;
		return this;
	}

	public MedicalSpotCheckQuery organizationPropertyLike(String organizationPropertyLike) {
		if (organizationPropertyLike == null) {
			throw new RuntimeException("organizationProperty is null");
		}
		this.organizationPropertyLike = organizationPropertyLike;
		return this;
	}

	public MedicalSpotCheckQuery organizationTerritoryLike(String organizationTerritoryLike) {
		if (organizationTerritoryLike == null) {
			throw new RuntimeException("organizationTerritory is null");
		}
		this.organizationTerritoryLike = organizationTerritoryLike;
		return this;
	}

	public MedicalSpotCheckQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public MedicalSpotCheckQuery provinceLike(String provinceLike) {
		if (provinceLike == null) {
			throw new RuntimeException("provinceLike is null");
		}
		this.provinceLike = provinceLike;
		return this;
	}

	public void setAgeOfTheMoon(Integer ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setAgeOfTheMoonGreaterThanOrEqual(Integer ageOfTheMoonGreaterThanOrEqual) {
		this.ageOfTheMoonGreaterThanOrEqual = ageOfTheMoonGreaterThanOrEqual;
	}

	public void setAgeOfTheMoonLessThanOrEqual(Integer ageOfTheMoonLessThanOrEqual) {
		this.ageOfTheMoonLessThanOrEqual = ageOfTheMoonLessThanOrEqual;
	}

	public void setAreaLike(String areaLike) {
		this.areaLike = areaLike;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCityLike(String cityLike) {
		this.cityLike = cityLike;
	}

	public void setGradeNameLike(String gradeNameLike) {
		this.gradeNameLike = gradeNameLike;
	}

	public void setHeightLevelGreaterThanOrEqual(Integer heightLevelGreaterThanOrEqual) {
		this.heightLevelGreaterThanOrEqual = heightLevelGreaterThanOrEqual;
	}

	public void setHeightLevelLessThanOrEqual(Integer heightLevelLessThanOrEqual) {
		this.heightLevelLessThanOrEqual = heightLevelLessThanOrEqual;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public void setNationLike(String nationLike) {
		this.nationLike = nationLike;
	}

	public void setNationNotLike(String nationNotLike) {
		this.nationNotLike = nationNotLike;
	}

	public void setOrganizationLevelLike(String organizationLevelLike) {
		this.organizationLevelLike = organizationLevelLike;
	}

	public void setOrganizationLike(String organizationLike) {
		this.organizationLike = organizationLike;
	}

	public void setOrganizationPropertyLike(String organizationPropertyLike) {
		this.organizationPropertyLike = organizationPropertyLike;
	}

	public void setOrganizationTerritoryLike(String organizationTerritoryLike) {
		this.organizationTerritoryLike = organizationTerritoryLike;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setProvinceLike(String provinceLike) {
		this.provinceLike = provinceLike;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWeightLevel(Integer weightLevel) {
		this.weightLevel = weightLevel;
	}

	public void setWeightLevelGreaterThanOrEqual(Integer weightLevelGreaterThanOrEqual) {
		this.weightLevelGreaterThanOrEqual = weightLevelGreaterThanOrEqual;
	}

	public void setWeightLevelLessThanOrEqual(Integer weightLevelLessThanOrEqual) {
		this.weightLevelLessThanOrEqual = weightLevelLessThanOrEqual;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public MedicalSpotCheckQuery sex(String sex) {
		if (sex == null) {
			throw new RuntimeException("sex is null");
		}
		this.sex = sex;
		return this;
	}

	public MedicalSpotCheckQuery tableSuffix(String tableSuffix) {
		if (tableSuffix == null) {
			throw new RuntimeException("tableSuffix is null");
		}
		this.tableSuffix = tableSuffix;
		return this;
	}

	public MedicalSpotCheckQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public MedicalSpotCheckQuery weightLevel(Integer weightLevel) {
		if (weightLevel == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevel = weightLevel;
		return this;
	}

	public MedicalSpotCheckQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}