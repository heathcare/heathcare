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
@Table(name = "HEALTH_MEDICAL_EXAM_EVAL")
public class MedicalExaminationEvaluate implements Serializable, JSONable, IMedicalEvaluate {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 检查编号
	 */
	@Column(name = "BATCHID_")
	protected long batchId;

	/**
	 * CheckId
	 */
	@Column(name = "CHECKID_", length = 50)
	protected String checkId;

	/**
	 * 作业号
	 */
	@Column(name = "JOBNO_", length = 50)
	protected String jobNo;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 省/直辖市编号
	 */
	@Column(name = "PROVINCEID_")
	protected long provinceId;

	/**
	 * 市编号
	 */
	@Column(name = "CITYID_")
	protected long cityId;

	/**
	 * 区/县编号
	 */
	@Column(name = "AREAID_")
	protected long areaId;

	/**
	 * 班级编号
	 */
	@Column(name = "GRADEID_", length = 50)
	protected String gradeId;

	@javax.persistence.Transient
	protected String gradeName;

	/**
	 * 学生编号
	 */
	@Column(name = "PERSONID_", length = 50)
	protected String personId;

	/**
	 * 姓名
	 */
	@Column(name = "NAME_", length = 100)
	protected String name;

	/**
	 * 性别
	 */
	@Column(name = "SEX_", length = 2)
	protected String sex;

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
	 * 理论标准体重
	 */
	@Column(name = "STDWEIGHT_")
	protected double stdWeight;

	/**
	 * 理论标准体重偏差
	 */
	@Column(name = "WEIGHTOFFSETPERCENT_")
	protected double weightOffsetPercent;

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
	 * 身高别体重
	 */
	@Column(name = "WEIGHTHEIGHTPERCENT_")
	protected double weightHeightPercent;

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
	 * 健康评价
	 */
	@Column(name = "HEALTHEVALUATE_", length = 500)
	protected String healthEvaluate;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

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

	@javax.persistence.Transient
	protected Date birthday;

	/**
	 * 体检时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKDATE_")
	protected Date checkDate;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	@javax.persistence.Transient
	protected int sortNo;

	public MedicalExaminationEvaluate() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicalExaminationEvaluate other = (MedicalExaminationEvaluate) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getAgeOfTheMoon() {
		if (checkDate == null) {
			checkDate = new java.util.Date();
		}
		if (birthday != null) {
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(birthday);
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(checkDate);
			int days = DateUtils.getDaysBetween(startDate, endDate);
			ageOfTheMoon = (int) Math.floor(days / 30.0D);
		}
		return ageOfTheMoon;
	}

	public long getAreaId() {
		return areaId;
	}

	public long getBatchId() {
		return this.batchId;
	}

	public Date getBirthday() {
		return birthday;
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

	public String getCheckId() {
		return this.checkId;
	}

	public long getCityId() {
		return cityId;
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

	public String getGradeId() {
		return this.gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public String getHealthEvaluate() {
		return this.healthEvaluate;
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

	public long getId() {
		return this.id;
	}

	public String getJobNo() {
		return jobNo;
	}

	public int getMonth() {
		return this.month;
	}

	public String getName() {
		return this.name;
	}

	public String getPersonId() {
		return this.personId;
	}

	public long getProvinceId() {
		return provinceId;
	}

	public String getSex() {
		return this.sex;
	}

	public int getSortNo() {
		return sortNo;
	}

	public double getStdWeight() {
		if (stdWeight > 0) {
			stdWeight = Math.round(stdWeight * 10D) / 10D;
		}
		return stdWeight;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getType() {
		return this.type;
	}

	public double getWeight() {
		if (weight > 0) {
			weight = Math.round(weight * 10D) / 10D;
		}
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
		weightHeightPercent = Math.round(weightHeightPercent * 100D) / 100D;
		return this.weightHeightPercent;
	}

	public int getWeightLevel() {
		return this.weightLevel;
	}

	public double getWeightOffsetPercent() {
		weightOffsetPercent = Math.round(weightOffsetPercent * 100D) / 100D;
		return weightOffsetPercent;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public MedicalExaminationEvaluate jsonToObject(JSONObject jsonObject) {
		return MedicalExaminationEvaluateJsonFactory.jsonToObject(jsonObject);
	}

	public void setAgeOfTheMoon(int ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
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

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setHealthEvaluate(String healthEvaluate) {
		this.healthEvaluate = healthEvaluate;
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

	public void setId(long id) {
		this.id = id;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setStdWeight(double stdWeight) {
		this.stdWeight = stdWeight;
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

	public void setWeightOffsetPercent(double weightOffsetPercent) {
		this.weightOffsetPercent = weightOffsetPercent;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return MedicalExaminationEvaluateJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return MedicalExaminationEvaluateJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
