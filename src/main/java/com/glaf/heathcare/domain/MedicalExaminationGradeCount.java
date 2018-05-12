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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.base.JSONable;
import com.glaf.heathcare.util.MedicalExaminationGradeCountJsonFactory;

@Entity
@Table(name = "HEALTH_MEDICAL_EXAM_GRADE_CNT")
public class MedicalExaminationGradeCount implements Serializable, JSONable {

	private static final long serialVersionUID = 1L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "CHECKID_", length = 50)
	protected String checkId;

	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 班级编号
	 */
	@Column(name = "GRADEID_", length = 50)
	protected String gradeId;

	/**
	 * 班级名称
	 */
	@javax.persistence.Transient
	protected String gradeName;

	/**
	 * 女生人数
	 */
	@Column(name = "FEMALE_")
	protected int female;

	/**
	 * 男生人数
	 */
	@Column(name = "MALE_")
	protected int male;

	/**
	 * 人数合计
	 */
	@Column(name = "PERSONCOUNT_")
	protected int personCount;

	/**
	 * 体检人数
	 */
	@Column(name = "CHECKPERSON_")
	protected int checkPerson;

	/**
	 * 体检率
	 */
	@Column(name = "CHECKPERCENT_")
	protected double checkPercent;

	/**
	 * 体重/年龄
	 */
	@javax.persistence.Transient
	protected PhysicalGrowthCount waCount;

	/**
	 * 身高/年龄
	 */
	@javax.persistence.Transient
	protected PhysicalGrowthCount haCount;

	/**
	 * 体重/身高
	 */
	@javax.persistence.Transient
	protected PhysicalGrowthCount whCount;

	/**
	 * 生长速度 实比人数
	 */
	@Column(name = "GROWTHRATEPERSON_")
	protected int growthRatePerson;

	/**
	 * 生长速度 实比率
	 */
	@Column(name = "GROWTHRATEPERSONPERCENT_")
	protected double growthRatePersonPercent;

	/**
	 * 体重
	 */
	@javax.persistence.Transient
	protected GrowthRateCount weightRate;

	/**
	 * 身高
	 */
	@javax.persistence.Transient
	protected GrowthRateCount heightRate;

	/**
	 * 双增
	 */
	@javax.persistence.Transient
	protected GrowthRateCount bothRate;

	/**
	 * 低体重人数
	 */
	@Column(name = "WEIGHTLOW_")
	protected int weightLow;

	/**
	 * 消瘦人数
	 */
	@Column(name = "WEIGHTSKINNY_")
	protected int weightSkinny;

	/**
	 * 发育迟缓人数
	 */
	@Column(name = "WEIGHTRETARDATION_")
	protected int weightRetardation;

	/**
	 * 严重营养不良
	 */
	@Column(name = "WEIGHTSEVEREMALNUTRITION_")
	protected int weightSevereMalnutrition;

	/**
	 * 超重人数
	 */
	@Column(name = "OVERWEIGHT_")
	protected int overWeight;

	/**
	 * 轻度肥胖人数
	 */
	@Column(name = "WEIGHTOBESITYLOW_")
	protected int weightObesityLow;

	/**
	 * 中度肥胖人数
	 */
	@Column(name = "WEIGHTOBESITYMID_")
	protected int weightObesityMid;

	/**
	 * 重度肥胖人数
	 */
	@Column(name = "WEIGHTOBESITYHIGH_")
	protected int weightObesityHigh;

	/**
	 * 贫血人数检查人数
	 */
	@Column(name = "ANEMIACHECK_")
	protected int anemiaCheck;

	/**
	 * 贫血人数检查正常人数
	 */
	@Column(name = "ANEMIACHECKNORMAL_")
	protected int anemiaCheckNormal;

	/**
	 * 贫血人数检查正常率
	 */
	@Column(name = "ANEMIACHECKNORMALPERCENT_")
	protected double anemiaCheckNormalPercent;

	/**
	 * 轻度贫血人数
	 */
	@Column(name = "ANEMIALOW_")
	protected int anemiaLow;

	/**
	 * 中度贫血人数
	 */
	@Column(name = "ANEMIAMID_")
	protected int anemiaMid;

	/**
	 * 重度贫血人数
	 */
	@Column(name = "ANEMIAHIGH_")
	protected int anemiaHigh;

	/**
	 * 血铅人数
	 */
	@Column(name = "BLOODLEAD_")
	protected int bloodLead;

	/**
	 * 内科疾病人数
	 */
	@Column(name = "INTERNALDISEASE_")
	protected int internalDisease;

	@javax.persistence.Transient
	protected double internalDiseasePercent;

	/**
	 * 外科疾病人数
	 */
	@Column(name = "SURGICALDISEASE_")
	protected int surgicalDisease;

	@javax.persistence.Transient
	protected double surgicalDiseasePercent;

	/**
	 * 龋齿
	 */
	@Column(name = "SAPRODONTIA_")
	protected int saprodontia;

	@javax.persistence.Transient
	protected double saprodontiaPercent;

	/**
	 * 沙眼
	 */
	@Column(name = "TRACHOMA_")
	protected int trachoma;

	@javax.persistence.Transient
	protected double trachomaPercent;

	/**
	 * 弱视
	 */
	@Column(name = "AMBLYOPIA_")
	protected int amblyopia;

	@javax.persistence.Transient
	protected double amblyopiaPercent;

	/**
	 * Hb≤110克人数
	 */
	@Column(name = "HEMOGLOBIN110_")
	protected int hemoglobin110;

	@javax.persistence.Transient
	protected double hemoglobin110Percent;

	/**
	 * Hb≤90克人数
	 */
	@Column(name = "HEMOGLOBIN90_")
	protected int hemoglobin90;

	@javax.persistence.Transient
	protected double hemoglobin90Percent;

	/**
	 * 乙肝表面抗体阳性人数
	 */
	@Column(name = "HBSAB_")
	protected int hbsab;

	@javax.persistence.Transient
	protected double hbsabPercent;

	/**
	 * 肝功超标人数
	 */
	@Column(name = "SGPT_")
	protected int sgpt;

	@javax.persistence.Transient
	protected double sgptPercent;

	/**
	 * HVAIgM阳性人数
	 */
	@Column(name = "HVAIGM_")
	protected int hvaigm;

	@javax.persistence.Transient
	protected double hvaigmPercent;

	/**
	 * 体检时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKDATE_")
	protected Date checkDate;

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
	 * 序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	@Column(name = "TYPE_", length = 50)
	protected String type;

	public MedicalExaminationGradeCount() {

	}

	public int getAmblyopia() {
		return amblyopia;
	}

	public double getAmblyopiaPercent() {
		if (checkPerson > 0) {
			amblyopiaPercent = Math.round(amblyopia * 100.D / checkPerson);
		}
		return amblyopiaPercent;
	}

	public int getAnemiaCheck() {
		return anemiaCheck;
	}

	public int getAnemiaCheckNormal() {
		return anemiaCheckNormal;
	}

	public double getAnemiaCheckNormalPercent() {
		if (anemiaCheckNormalPercent > 0) {
			anemiaCheckNormalPercent = Math.round(anemiaCheckNormalPercent * 100D) / 100D;
		}
		return anemiaCheckNormalPercent;
	}

	public int getAnemiaHigh() {
		return anemiaHigh;
	}

	public int getAnemiaLow() {
		return anemiaLow;
	}

	public int getAnemiaMid() {
		return anemiaMid;
	}

	public int getBloodLead() {
		return bloodLead;
	}

	public GrowthRateCount getBothRate() {
		return bothRate;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public String getCheckId() {
		return checkId;
	}

	public double getCheckPercent() {
		if (personCount > 0) {
			checkPercent = checkPerson * 1.0D / personCount;
		}
		if (checkPercent > 0) {
			checkPercent = Math.round(checkPercent * 100D) / 100D;
		}
		return checkPercent;
	}

	public int getCheckPerson() {
		return checkPerson;
	}

	public int getFemale() {
		return female;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public int getGrowthRatePerson() {
		return growthRatePerson;
	}

	public double getGrowthRatePersonPercent() {
		if (growthRatePersonPercent > 0) {
			growthRatePersonPercent = Math.round(growthRatePersonPercent * 100D) / 100D;
		}
		return growthRatePersonPercent;
	}

	public PhysicalGrowthCount getHaCount() {
		return haCount;
	}

	public int getHbsab() {
		return hbsab;
	}

	public double getHbsabPercent() {
		if (checkPerson > 0) {
			hbsabPercent = Math.round(hbsab * 100.D / checkPerson);
		}
		return hbsabPercent;
	}

	public GrowthRateCount getHeightRate() {
		return heightRate;
	}

	public int getHemoglobin110() {
		return hemoglobin110;
	}

	public double getHemoglobin110Percent() {
		if (checkPerson > 0) {
			hemoglobin110Percent = Math.round(hemoglobin110 * 100.D / checkPerson);
		}
		return hemoglobin110Percent;
	}

	public int getHemoglobin90() {
		return hemoglobin90;
	}

	public double getHemoglobin90Percent() {
		if (checkPerson > 0) {
			hemoglobin90Percent = Math.round(hemoglobin90 * 100.D / checkPerson);
		}
		return hemoglobin90Percent;
	}

	public int getHvaigm() {
		return hvaigm;
	}

	public double getHvaigmPercent() {
		if (checkPerson > 0) {
			hvaigmPercent = Math.round(hvaigm * 100.D / checkPerson);
		}
		return hvaigmPercent;
	}

	public long getId() {
		return id;
	}

	public int getInternalDisease() {
		return internalDisease;
	}

	public double getInternalDiseasePercent() {
		if (checkPerson > 0) {
			internalDiseasePercent = Math.round(internalDisease * 100.D / checkPerson);
		}
		return internalDiseasePercent;
	}

	public int getMale() {
		return male;
	}

	public int getMonth() {
		return month;
	}

	public int getOverWeight() {
		return overWeight;
	}

	public int getPersonCount() {
		return personCount;
	}

	public int getSaprodontia() {
		return saprodontia;
	}

	public double getSaprodontiaPercent() {
		if (checkPerson > 0) {
			saprodontiaPercent = Math.round(saprodontia * 100.D / checkPerson);
		}
		return saprodontiaPercent;
	}

	public int getSgpt() {
		return sgpt;
	}

	public double getSgptPercent() {
		if (checkPerson > 0) {
			sgptPercent = Math.round(sgpt * 100.D / checkPerson);
		}
		return sgptPercent;
	}

	public int getSortNo() {
		return sortNo;
	}

	public int getSurgicalDisease() {
		return surgicalDisease;
	}

	public double getSurgicalDiseasePercent() {
		if (checkPerson > 0) {
			surgicalDiseasePercent = Math.round(surgicalDisease * 100.D / checkPerson);
		}
		return surgicalDiseasePercent;
	}

	public String getTenantId() {
		return tenantId;
	}

	public int getTrachoma() {
		return trachoma;
	}

	public double getTrachomaPercent() {
		if (checkPerson > 0) {
			trachomaPercent = Math.round(trachoma * 100.D / checkPerson);
		}
		return trachomaPercent;
	}

	public String getType() {
		return type;
	}

	public PhysicalGrowthCount getWaCount() {
		return waCount;
	}

	public int getWeightLow() {
		return weightLow;
	}

	public int getWeightObesityHigh() {
		return weightObesityHigh;
	}

	public int getWeightObesityLow() {
		return weightObesityLow;
	}

	public int getWeightObesityMid() {
		return weightObesityMid;
	}

	public GrowthRateCount getWeightRate() {
		return weightRate;
	}

	public int getWeightRetardation() {
		return weightRetardation;
	}

	public int getWeightSevereMalnutrition() {
		return weightSevereMalnutrition;
	}

	public int getWeightSkinny() {
		return weightSkinny;
	}

	public PhysicalGrowthCount getWhCount() {
		return whCount;
	}

	public int getYear() {
		return year;
	}

	public MedicalExaminationGradeCount jsonToObject(JSONObject jsonObject) {
		return MedicalExaminationGradeCountJsonFactory.jsonToObject(jsonObject);
	}

	public void setAmblyopia(int amblyopia) {
		this.amblyopia = amblyopia;
	}

	public void setAmblyopiaPercent(double amblyopiaPercent) {
		this.amblyopiaPercent = amblyopiaPercent;
	}

	public void setAnemiaCheck(int anemiaCheck) {
		this.anemiaCheck = anemiaCheck;
	}

	public void setAnemiaCheckNormal(int anemiaCheckNormal) {
		this.anemiaCheckNormal = anemiaCheckNormal;
	}

	public void setAnemiaCheckNormalPercent(double anemiaCheckNormalPercent) {
		this.anemiaCheckNormalPercent = anemiaCheckNormalPercent;
	}

	public void setAnemiaHigh(int anemiaHigh) {
		this.anemiaHigh = anemiaHigh;
	}

	public void setAnemiaLow(int anemiaLow) {
		this.anemiaLow = anemiaLow;
	}

	public void setAnemiaMid(int anemiaMid) {
		this.anemiaMid = anemiaMid;
	}

	public void setBloodLead(int bloodLead) {
		this.bloodLead = bloodLead;
	}

	public void setBothRate(GrowthRateCount bothRate) {
		this.bothRate = bothRate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCheckPercent(double checkPercent) {
		this.checkPercent = checkPercent;
	}

	public void setCheckPerson(int checkPerson) {
		this.checkPerson = checkPerson;
	}

	public void setFemale(int female) {
		this.female = female;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setGrowthRatePerson(int growthRatePerson) {
		this.growthRatePerson = growthRatePerson;
	}

	public void setGrowthRatePersonPercent(double growthRatePersonPercent) {
		this.growthRatePersonPercent = growthRatePersonPercent;
	}

	public void setHaCount(PhysicalGrowthCount haCount) {
		this.haCount = haCount;
	}

	public void setHbsab(int hbsab) {
		this.hbsab = hbsab;
	}

	public void setHbsabPercent(double hbsabPercent) {
		this.hbsabPercent = hbsabPercent;
	}

	public void setHeightRate(GrowthRateCount heightRate) {
		this.heightRate = heightRate;
	}

	public void setHemoglobin110(int hemoglobin110) {
		this.hemoglobin110 = hemoglobin110;
	}

	public void setHemoglobin110Percent(double hemoglobin110Percent) {
		this.hemoglobin110Percent = hemoglobin110Percent;
	}

	public void setHemoglobin90(int hemoglobin90) {
		this.hemoglobin90 = hemoglobin90;
	}

	public void setHemoglobin90Percent(double hemoglobin90Percent) {
		this.hemoglobin90Percent = hemoglobin90Percent;
	}

	public void setHvaigm(int hvaigm) {
		this.hvaigm = hvaigm;
	}

	public void setHvaigmPercent(double hvaigmPercent) {
		this.hvaigmPercent = hvaigmPercent;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setInternalDisease(int internalDisease) {
		this.internalDisease = internalDisease;
	}

	public void setInternalDiseasePercent(double internalDiseasePercent) {
		this.internalDiseasePercent = internalDiseasePercent;
	}

	public void setMale(int male) {
		this.male = male;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setOverWeight(int overWeight) {
		this.overWeight = overWeight;
	}

	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}

	public void setSaprodontia(int saprodontia) {
		this.saprodontia = saprodontia;
	}

	public void setSaprodontiaPercent(double saprodontiaPercent) {
		this.saprodontiaPercent = saprodontiaPercent;
	}

	public void setSgpt(int sgpt) {
		this.sgpt = sgpt;
	}

	public void setSgptPercent(double sgptPercent) {
		this.sgptPercent = sgptPercent;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSurgicalDisease(int surgicalDisease) {
		this.surgicalDisease = surgicalDisease;
	}

	public void setSurgicalDiseasePercent(double surgicalDiseasePercent) {
		this.surgicalDiseasePercent = surgicalDiseasePercent;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTrachoma(int trachoma) {
		this.trachoma = trachoma;
	}

	public void setTrachomaPercent(double trachomaPercent) {
		this.trachomaPercent = trachomaPercent;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWaCount(PhysicalGrowthCount waCount) {
		this.waCount = waCount;
	}

	public void setWeightLow(int weightLow) {
		this.weightLow = weightLow;
	}

	public void setWeightObesityHigh(int weightObesityHigh) {
		this.weightObesityHigh = weightObesityHigh;
	}

	public void setWeightObesityLow(int weightObesityLow) {
		this.weightObesityLow = weightObesityLow;
	}

	public void setWeightObesityMid(int weightObesityMid) {
		this.weightObesityMid = weightObesityMid;
	}

	public void setWeightRate(GrowthRateCount weightRate) {
		this.weightRate = weightRate;
	}

	public void setWeightRetardation(int weightRetardation) {
		this.weightRetardation = weightRetardation;
	}

	public void setWeightSevereMalnutrition(int weightSevereMalnutrition) {
		this.weightSevereMalnutrition = weightSevereMalnutrition;
	}

	public void setWeightSkinny(int weightSkinny) {
		this.weightSkinny = weightSkinny;
	}

	public void setWhCount(PhysicalGrowthCount whCount) {
		this.whCount = whCount;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return MedicalExaminationGradeCountJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return MedicalExaminationGradeCountJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
