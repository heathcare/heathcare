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
@Table(name = "HEALTH_MEDICAL_SPOT_CHECK")
public class MedicalSpotCheck implements Serializable, JSONable, IMedicalEvaluate {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 64, nullable = false)
	protected String id;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 班级名称
	 */
	@Column(name = "GRADENAME_", length = 100)
	protected String gradeName;

	/**
	 * 儿童编号
	 */
	@Column(name = "PERSONID_", length = 50)
	protected String personId;

	/**
	 * 姓名
	 */
	@Column(name = "NAME_", length = 50)
	protected String name;

	/**
	 * 性别
	 */
	@Column(name = "SEX_", length = 2)
	protected String sex;

	/**
	 * 出生日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BIRTHDAY_")
	protected Date birthday;

	/**
	 * 民族
	 */
	@Column(name = "NATION_", length = 50)
	protected String nation;

	/**
	 * 身高
	 */
	@Column(name = "HEIGHT_")
	protected double height;

	/**
	 * 身高等级
	 */
	@Column(name = "HEIGHTLEVEL_")
	protected int heightLevel;

	/**
	 * 身高评价
	 */
	@Column(name = "HEIGHTEVALUATE_", length = 200)
	protected String heightEvaluate;

	@javax.persistence.Transient
	protected String heightEvaluateHtml;

	/**
	 * 体重
	 */
	@Column(name = "WEIGHT_")
	protected double weight;

	/**
	 * 体重等级
	 */
	@Column(name = "WEIGHTLEVEL_")
	protected int weightLevel;

	/**
	 * 体重评价
	 */
	@Column(name = "WEIGHTEVALUATE_", length = 200)
	protected String weightEvaluate;

	@javax.persistence.Transient
	protected String weightEvaluateHtml;

	/**
	 * 身高别体重
	 */
	@Column(name = "WEIGHTHEIGHTPERCENT_")
	protected double weightHeightPercent;

	/**
	 * 身高别体重等级
	 */
	@Column(name = "WEIGHTHEIGHTLEVEL_")
	protected int weightHeightLevel;

	/**
	 * 身高体重评价
	 */
	@Column(name = "WEIGHTHEIGHTEVALUATE_", length = 200)
	protected String weightHeightEvaluate;

	@javax.persistence.Transient
	protected String weightHeightEvaluateHtml;

	/**
	 * BMI
	 */
	@Column(name = "BMI_")
	protected double bmi;

	/**
	 * 体重指数
	 */
	@Column(name = "BMIINDEX_")
	protected double bmiIndex;

	/**
	 * 综合评价
	 */
	@Column(name = "BMIEVALUATE_", length = 200)
	protected String bmiEvaluate;

	@javax.persistence.Transient
	protected String bmiEvaluateHtml;

	/**
	 * 左视力
	 */
	@Column(name = "EYESIGHTLEFT_")
	protected double eyesightLeft;

	/**
	 * 右视力
	 */
	@Column(name = "EYESIGHTRIGHT_")
	protected double eyesightRight;

	/**
	 * 血红蛋白Hb
	 */
	@Column(name = "HEMOGLOBIN_")
	protected double hemoglobin;

	/**
	 * 年
	 */
	@Column(name = "YEAR_")
	protected int year;

	/**
	 * 月
	 */
	@Column(name = "MONTH_")
	protected int month;

	/**
	 * 月龄
	 */
	@Column(name = "AGEOFTHEMOON_")
	protected int ageOfTheMoon;

	/**
	 * 月龄
	 */
	@Column(name = "AGEOFTHEMOONSTRING_", length = 20)
	protected String ageOfTheMoonString;

	/**
	 * 省/直辖市
	 */
	@Column(name = "PROVINCE_", length = 50)
	protected String province;

	/**
	 * 市
	 */
	@Column(name = "CITY_", length = 250)
	protected String city;

	/**
	 * 区/县
	 */
	@Column(name = "AREA_", length = 250)
	protected String area;

	/**
	 * 机构名称
	 */
	@Column(name = "ORGANIZATION_", length = 250)
	protected String organization;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 顺序号
	 */
	@Column(name = "ORDINAL_")
	protected int ordinal;

	/**
	 * 体检时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKDATE_")
	protected Date checkDate;

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

	public MedicalSpotCheck() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicalSpotCheck other = (MedicalSpotCheck) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int getAgeOfTheMoon() {
		return this.ageOfTheMoon;
	}

	public String getAgeOfTheMoonString() {
		return this.ageOfTheMoonString;
	}

	public String getArea() {
		return this.area;
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

	public double getBmi() {
		if (bmi > 0) {
			bmi = Math.round(bmi * 10D) / 10D;
		}
		return this.bmi;
	}

	public String getBmiEvaluate() {
		return this.bmiEvaluate;
	}

	public String getBmiEvaluateHtml() {
		return bmiEvaluateHtml;
	}

	public double getBmiIndex() {
		return this.bmiIndex;
	}

	public Date getCheckDate() {
		return this.checkDate;
	}

	public String getCheckDateString() {
		if (this.checkDate != null) {
			return DateUtils.getDateTime(this.checkDate);
		}
		return "";
	}

	public String getCity() {
		return this.city;
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

	public double getEyesightLeft() {
		return this.eyesightLeft;
	}

	public double getEyesightRight() {
		return this.eyesightRight;
	}

	public String getGradeName() {
		return gradeName;
	}

	public double getHeight() {
		return this.height;
	}

	public String getHeightEvaluate() {
		return this.heightEvaluate;
	}

	public String getHeightEvaluateHtml() {
		return heightEvaluateHtml;
	}

	public int getHeightLevel() {
		return this.heightLevel;
	}

	public double getHemoglobin() {
		return this.hemoglobin;
	}

	public String getId() {
		return this.id;
	}

	public int getMonth() {
		return this.month;
	}

	public String getName() {
		return this.name;
	}

	public String getNation() {
		return this.nation;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public String getOrganization() {
		return this.organization;
	}

	public String getPersonId() {
		return personId;
	}

	public String getProvince() {
		return this.province;
	}

	public String getSex() {
		return this.sex;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return this.type;
	}

	public double getWeight() {
		return this.weight;
	}

	public String getWeightEvaluate() {
		return this.weightEvaluate;
	}

	public String getWeightEvaluateHtml() {
		return weightEvaluateHtml;
	}

	public String getWeightHeightEvaluate() {
		return weightHeightEvaluate;
	}

	public String getWeightHeightEvaluateHtml() {
		return weightHeightEvaluateHtml;
	}

	public int getWeightHeightLevel() {
		return weightHeightLevel;
	}

	public double getWeightHeightPercent() {
		return this.weightHeightPercent;
	}

	public int getWeightLevel() {
		return this.weightLevel;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public MedicalSpotCheck jsonToObject(JSONObject jsonObject) {
		return MedicalSpotCheckJsonFactory.jsonToObject(jsonObject);
	}

	public void setAgeOfTheMoon(int ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setAgeOfTheMoonString(String ageOfTheMoonString) {
		this.ageOfTheMoonString = ageOfTheMoonString;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setBmi(double bmi) {
		this.bmi = bmi;
	}

	public void setBmiEvaluate(String bmiEvaluate) {
		this.bmiEvaluate = bmiEvaluate;
	}

	public void setBmiEvaluateHtml(String bmiEvaluateHtml) {
		this.bmiEvaluateHtml = bmiEvaluateHtml;
	}

	public void setBmiIndex(double bmiIndex) {
		this.bmiIndex = bmiIndex;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setEyesightLeft(double eyesightLeft) {
		this.eyesightLeft = eyesightLeft;
	}

	public void setEyesightRight(double eyesightRight) {
		this.eyesightRight = eyesightRight;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setHeightEvaluate(String heightEvaluate) {
		this.heightEvaluate = heightEvaluate;
	}

	public void setHeightEvaluateHtml(String heightEvaluateHtml) {
		this.heightEvaluateHtml = heightEvaluateHtml;
	}

	public void setHeightLevel(int heightLevel) {
		this.heightLevel = heightLevel;
	}

	public void setHemoglobin(double hemoglobin) {
		this.hemoglobin = hemoglobin;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setWeightEvaluate(String weightEvaluate) {
		this.weightEvaluate = weightEvaluate;
	}

	public void setWeightEvaluateHtml(String weightEvaluateHtml) {
		this.weightEvaluateHtml = weightEvaluateHtml;
	}

	public void setWeightHeightEvaluate(String weightHeightEvaluate) {
		this.weightHeightEvaluate = weightHeightEvaluate;
	}

	public void setWeightHeightEvaluateHtml(String weightHeightEvaluateHtml) {
		this.weightHeightEvaluateHtml = weightHeightEvaluateHtml;
	}

	public void setWeightHeightLevel(int weightHeightLevel) {
		this.weightHeightLevel = weightHeightLevel;
	}

	public void setWeightHeightPercent(double weightHeightPercent) {
		this.weightHeightPercent = weightHeightPercent;
	}

	public void setWeightLevel(int weightLevel) {
		this.weightLevel = weightLevel;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return MedicalSpotCheckJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return MedicalSpotCheckJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
