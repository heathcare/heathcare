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
import com.glaf.heathcare.util.MedicalExaminationCountJsonFactory;

@Entity
@Table(name = "HEALTH_MEDICAL_EXAM_COUNT")
public class MedicalExaminationCount implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 64, nullable = false)
	protected String id;

	@Column(name = "CHECKID_", length = 50)
	protected String checkId;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 班级编号
	 */
	@Column(name = "GRADEID_", length = 50)
	protected String gradeId;

	@Column(name = "GRADENAME_", length = 200)
	protected String gradeName;

	@javax.persistence.Transient
	protected Date checkDate;

	@Column(name = "TOTALPERSON_")
	protected int totalPerson;

	@Column(name = "TOTALMALE_")
	protected int totalMale;

	@Column(name = "TOTALFEMALE_")
	protected int totalFemale;

	@Column(name = "CHECKPERSON_")
	protected int checkPerson;

	@Column(name = "CHECKPCT_")
	protected double checkPercent;

	/**
	 * 体重正常人数
	 */
	@Column(name = "MEANWEIGHTNORMAL_")
	protected int meanWeightNormal;

	@Column(name = "MEANWEIGHTNORMAL_PCT_")
	protected double meanWeightNormalPercent;

	/**
	 * 身高正常人数
	 */
	@Column(name = "MEANHEIGHTNORMAL_")
	protected int meanHeightNormal;

	@Column(name = "MEANHEIGHTNORMAL_PCT_")
	protected double meanHeightNormalPercent;

	/**
	 * 体重低于2SD人数
	 */
	@Column(name = "MEANWEIGHTLOW_")
	protected int meanWeightLow;

	@Column(name = "MEANWEIGHTLOWPCT_")
	protected double meanWeightLowPercent;

	/**
	 * 体重低于3SD人数
	 */
	@Column(name = "MEANWEIGHTLOW3_")
	protected int meanWeightLow3;

	@Column(name = "MEANWEIGHTLOWPCT3_")
	protected double meanWeightLow3Percent;

	/**
	 * 身高低于2SD人数
	 */
	@Column(name = "MEANHEIGHTLOW_")
	protected int meanHeightLow;

	@Column(name = "MEANHEIGHTLOWPCT_")
	protected double meanHeightLowPercent;

	/**
	 * 身高低于3SD人数
	 */
	@Column(name = "MEANHEIGHTLOW3_")
	protected int meanHeightLow3;

	@Column(name = "MEANHEIGHTLOWPCT3_")
	protected double meanHeightLow3Percent;

	/**
	 * 消瘦人数
	 */
	@Column(name = "MEANWEIGHTSKINNY_")
	protected int meanWeightSkinny;

	@Column(name = "MEANWEIGHTSKINNYPCT_")
	protected double meanWeightSkinnyPercent;

	/**
	 * 重度消瘦人数
	 */
	@Column(name = "MEANWEIGHTSERIOUSSKINNY_")
	protected int meanWeightSeriousSkinny;

	@Column(name = "MEANWEIGHTSERIOUSSKINNYPCT_")
	protected double meanWeightSeriousSkinnyPercent;

	/**
	 * 超重人数
	 */
	@Column(name = "MEANOVERWEIGHT_")
	protected int meanOverWeight;

	@Column(name = "MEANOVERWEIGHTPCT_")
	protected double meanOverWeightPercent;

	/**
	 * 肥胖人数
	 */
	@Column(name = "MEANWEIGHTOBESITY_")
	protected int meanWeightObesity;

	@Column(name = "MEANWEIGHTOBESITYPCT_")
	protected double meanWeightObesityPercent;

	/**
	 * W/H正常人数(身高别体重)
	 */
	@Column(name = "PRCTILE_WH_NORMAL_")
	protected int prctileWeightHeightNormal;

	@Column(name = "PRCTILE_WH_NORMAL_PCT_")
	protected double prctileWeightHeightNormalPercent;

	/**
	 * W/A正常人数(年龄别体重)
	 */
	@Column(name = "PRCTILE_WA_NORMAL_")
	protected int prctileWeightAgeNormal;

	@Column(name = "PRCTILE_WA_NORMAL_PCT_")
	protected double prctileWeightAgeNormalPercent;

	/**
	 * H/A正常人数(年龄别身高)
	 */
	@Column(name = "PRCTILE_HA_NORMAL_")
	protected int prctileHeightAgeNormal;

	@Column(name = "PRCTILE_HA_NORMAL_PCT_")
	protected double prctileHeightAgeNormalPercent;

	/**
	 * H/A小于P3人数(年龄别身高)
	 */
	@Column(name = "PRCTILE_HA_LOW_")
	protected int prctileHeightAgeLow;

	@Column(name = "PRCTILE_HA_LOWPCT_")
	protected double prctileHeightAgeLowPercent;

	/**
	 * W/H小于P3人数(身高别体重)
	 */
	@Column(name = "PRCTILE_WH_LOW_")
	protected int prctileWeightHeightLow;

	@Column(name = "PRCTILE_WH_LOWPCT_")
	protected double prctileWeightHeightLowPercent;

	/**
	 * W/A小于P3人数(年龄别体重)
	 */
	@Column(name = "PRCTILE_WA_LOW_")
	protected int prctileWeightAgeLow;

	@Column(name = "PRCTILE_WA_LOWPCT_")
	protected double prctileWeightAgeLowPercent;

	/**
	 * 超重人数
	 */
	@Column(name = "PRCTILEOVERWEIGHT_")
	protected int prctileOverWeight;

	@Column(name = "PRCTILEOVERWEIGHTPCT_")
	protected double prctileOverWeightPercent;

	/**
	 * 肥胖人数
	 */
	@Column(name = "PRCTILEWEIGHTOBESITY_")
	protected int prctileWeightObesity;

	@Column(name = "PRCTILEWEIGHTOBESITYPCT_")
	protected double prctileWeightObesityPercent;

	/**
	 * 学年
	 */
	@Column(name = "GRADEYEAR_")
	protected int gradeYear;

	/**
	 * 类型
	 */
	@Column(name = "TARGETTYPE_", length = 50)
	protected String targetType;

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

	@javax.persistence.Transient
	protected int sortNo;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public MedicalExaminationCount() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicalExaminationCount other = (MedicalExaminationCount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public String getCheckId() {
		return checkId;
	}

	public double getCheckPercent() {
		if (checkPercent > 0) {
			checkPercent = Math.round(checkPercent * 100D) / 100D;
		}
		return checkPercent;
	}

	public int getCheckPerson() {
		return checkPerson;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public int getGradeYear() {
		return gradeYear;
	}

	public String getId() {
		return id;
	}

	public int getMeanHeightLow() {
		return meanHeightLow;
	}

	public int getMeanHeightLow3() {
		return meanHeightLow3;
	}

	public double getMeanHeightLow3Percent() {
		return meanHeightLow3Percent;
	}

	public double getMeanHeightLowPercent() {
		if (meanHeightLowPercent > 0) {
			meanHeightLowPercent = Math.round(meanHeightLowPercent * 100D) / 100D;
		}
		return meanHeightLowPercent;
	}

	public int getMeanHeightNormal() {
		return meanHeightNormal;
	}

	public double getMeanHeightNormalPercent() {
		if (meanHeightNormalPercent > 0) {
			meanHeightNormalPercent = Math.round(meanHeightNormalPercent * 100D) / 100D;
		}
		return meanHeightNormalPercent;
	}

	public int getMeanOverWeight() {
		return meanOverWeight;
	}

	public double getMeanOverWeightPercent() {
		if (meanOverWeightPercent > 0) {
			meanOverWeightPercent = Math.round(meanOverWeightPercent * 100D) / 100D;
		}
		return meanOverWeightPercent;
	}

	public int getMeanWeightLow() {
		return meanWeightLow;
	}

	public int getMeanWeightLow3() {
		return meanWeightLow3;
	}

	public double getMeanWeightLow3Percent() {
		return meanWeightLow3Percent;
	}

	public double getMeanWeightLowPercent() {
		if (meanWeightLowPercent > 0) {
			meanWeightLowPercent = Math.round(meanWeightLowPercent * 100D) / 100D;
		}
		return meanWeightLowPercent;
	}

	public int getMeanWeightNormal() {
		return meanWeightNormal;
	}

	public double getMeanWeightNormalPercent() {
		if (meanWeightNormalPercent > 0) {
			meanWeightNormalPercent = Math.round(meanWeightNormalPercent * 100D) / 100D;
		}
		return meanWeightNormalPercent;
	}

	public int getMeanWeightObesity() {
		return meanWeightObesity;
	}

	public double getMeanWeightObesityPercent() {
		if (meanWeightObesityPercent > 0) {
			meanWeightObesityPercent = Math.round(meanWeightObesityPercent * 100D) / 100D;
		}
		return meanWeightObesityPercent;
	}

	public int getMeanWeightSeriousSkinny() {
		return meanWeightSeriousSkinny;
	}

	public double getMeanWeightSeriousSkinnyPercent() {
		return meanWeightSeriousSkinnyPercent;
	}

	public int getMeanWeightSkinny() {
		return meanWeightSkinny;
	}

	public double getMeanWeightSkinnyPercent() {
		if (meanWeightSkinnyPercent > 0) {
			meanWeightSkinnyPercent = Math.round(meanWeightSkinnyPercent * 100D) / 100D;
		}
		return meanWeightSkinnyPercent;
	}

	public int getMonth() {
		return month;
	}

	public int getPrctileHeightAgeLow() {
		return prctileHeightAgeLow;
	}

	public double getPrctileHeightAgeLowPercent() {
		if (prctileHeightAgeLowPercent > 0) {
			prctileHeightAgeLowPercent = Math.round(prctileHeightAgeLowPercent * 100D) / 100D;
		}
		return prctileHeightAgeLowPercent;
	}

	public int getPrctileHeightAgeNormal() {
		return prctileHeightAgeNormal;
	}

	public double getPrctileHeightAgeNormalPercent() {
		if (prctileHeightAgeNormalPercent > 0) {
			prctileHeightAgeNormalPercent = Math.round(prctileHeightAgeNormalPercent * 100D) / 100D;
		}
		return prctileHeightAgeNormalPercent;
	}

	public int getPrctileOverWeight() {
		return prctileOverWeight;
	}

	public double getPrctileOverWeightPercent() {
		if (prctileOverWeightPercent > 0) {
			prctileOverWeightPercent = Math.round(prctileOverWeightPercent * 100D) / 100D;
		}
		return prctileOverWeightPercent;
	}

	public int getPrctileWeightAgeLow() {
		return prctileWeightAgeLow;
	}

	public double getPrctileWeightAgeLowPercent() {
		if (prctileWeightAgeLowPercent > 0) {
			prctileWeightAgeLowPercent = Math.round(prctileWeightAgeLowPercent * 100D) / 100D;
		}
		return prctileWeightAgeLowPercent;
	}

	public int getPrctileWeightAgeNormal() {
		return prctileWeightAgeNormal;
	}

	public double getPrctileWeightAgeNormalPercent() {
		if (prctileWeightAgeNormalPercent > 0) {
			prctileWeightAgeNormalPercent = Math.round(prctileWeightAgeNormalPercent * 100D) / 100D;
		}
		return prctileWeightAgeNormalPercent;
	}

	public int getPrctileWeightHeightLow() {
		return prctileWeightHeightLow;
	}

	public double getPrctileWeightHeightLowPercent() {
		if (prctileWeightHeightLowPercent > 0) {
			prctileWeightHeightLowPercent = Math.round(prctileWeightHeightLowPercent * 100D) / 100D;
		}
		return prctileWeightHeightLowPercent;
	}

	public int getPrctileWeightHeightNormal() {
		return prctileWeightHeightNormal;
	}

	public double getPrctileWeightHeightNormalPercent() {
		if (prctileWeightHeightNormalPercent > 0) {
			prctileWeightHeightNormalPercent = Math.round(prctileWeightHeightNormalPercent * 100D) / 100D;
		}
		return prctileWeightHeightNormalPercent;
	}

	public int getPrctileWeightObesity() {
		return prctileWeightObesity;
	}

	public double getPrctileWeightObesityPercent() {
		if (prctileWeightObesityPercent > 0) {
			prctileWeightObesityPercent = Math.round(prctileWeightObesityPercent * 100D) / 100D;
		}
		return prctileWeightObesityPercent;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getTargetType() {
		return targetType;
	}

	public String getTenantId() {
		return tenantId;
	}

	public int getTotalFemale() {
		return totalFemale;
	}

	public int getTotalMale() {
		return totalMale;
	}

	public int getTotalPerson() {
		return totalPerson;
	}

	public String getType() {
		return type;
	}

	public int getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public MedicalExaminationCount jsonToObject(JSONObject jsonObject) {
		return MedicalExaminationCountJsonFactory.jsonToObject(jsonObject);
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

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setGradeYear(int gradeYear) {
		this.gradeYear = gradeYear;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMeanHeightLow(int meanHeightLow) {
		this.meanHeightLow = meanHeightLow;
	}

	public void setMeanHeightLow3(int meanHeightLow3) {
		this.meanHeightLow3 = meanHeightLow3;
	}

	public void setMeanHeightLow3Percent(double meanHeightLow3Percent) {
		this.meanHeightLow3Percent = meanHeightLow3Percent;
	}

	public void setMeanHeightLowPercent(double meanHeightLowPercent) {
		this.meanHeightLowPercent = meanHeightLowPercent;
	}

	public void setMeanHeightNormal(int meanHeightNormal) {
		this.meanHeightNormal = meanHeightNormal;
	}

	public void setMeanHeightNormalPercent(double meanHeightNormalPercent) {
		this.meanHeightNormalPercent = meanHeightNormalPercent;
	}

	public void setMeanOverWeight(int meanOverWeight) {
		this.meanOverWeight = meanOverWeight;
	}

	public void setMeanOverWeightPercent(double meanOverWeightPercent) {
		this.meanOverWeightPercent = meanOverWeightPercent;
	}

	public void setMeanWeightLow(int meanWeightLow) {
		this.meanWeightLow = meanWeightLow;
	}

	public void setMeanWeightLow3(int meanWeightLow3) {
		this.meanWeightLow3 = meanWeightLow3;
	}

	public void setMeanWeightLow3Percent(double meanWeightLow3Percent) {
		this.meanWeightLow3Percent = meanWeightLow3Percent;
	}

	public void setMeanWeightLowPercent(double meanWeightLowPercent) {
		this.meanWeightLowPercent = meanWeightLowPercent;
	}

	public void setMeanWeightNormal(int meanWeightNormal) {
		this.meanWeightNormal = meanWeightNormal;
	}

	public void setMeanWeightNormalPercent(double meanWeightNormalPercent) {
		this.meanWeightNormalPercent = meanWeightNormalPercent;
	}

	public void setMeanWeightObesity(int meanWeightObesity) {
		this.meanWeightObesity = meanWeightObesity;
	}

	public void setMeanWeightObesityPercent(double meanWeightObesityPercent) {
		this.meanWeightObesityPercent = meanWeightObesityPercent;
	}

	public void setMeanWeightSeriousSkinny(int meanWeightSeriousSkinny) {
		this.meanWeightSeriousSkinny = meanWeightSeriousSkinny;
	}

	public void setMeanWeightSeriousSkinnyPercent(double meanWeightSeriousSkinnyPercent) {
		this.meanWeightSeriousSkinnyPercent = meanWeightSeriousSkinnyPercent;
	}

	public void setMeanWeightSkinny(int meanWeightSkinny) {
		this.meanWeightSkinny = meanWeightSkinny;
	}

	public void setMeanWeightSkinnyPercent(double meanWeightSkinnyPercent) {
		this.meanWeightSkinnyPercent = meanWeightSkinnyPercent;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setPrctileHeightAgeLow(int prctileHeightAgeLow) {
		this.prctileHeightAgeLow = prctileHeightAgeLow;
	}

	public void setPrctileHeightAgeLowPercent(double prctileHeightAgeLowPercent) {
		this.prctileHeightAgeLowPercent = prctileHeightAgeLowPercent;
	}

	public void setPrctileHeightAgeNormal(int prctileHeightAgeNormal) {
		this.prctileHeightAgeNormal = prctileHeightAgeNormal;
	}

	public void setPrctileHeightAgeNormalPercent(double prctileHeightAgeNormalPercent) {
		this.prctileHeightAgeNormalPercent = prctileHeightAgeNormalPercent;
	}

	public void setPrctileOverWeight(int prctileOverWeight) {
		this.prctileOverWeight = prctileOverWeight;
	}

	public void setPrctileOverWeightPercent(double prctileOverWeightPercent) {
		this.prctileOverWeightPercent = prctileOverWeightPercent;
	}

	public void setPrctileWeightAgeLow(int prctileWeightAgeLow) {
		this.prctileWeightAgeLow = prctileWeightAgeLow;
	}

	public void setPrctileWeightAgeLowPercent(double prctileWeightAgeLowPercent) {
		this.prctileWeightAgeLowPercent = prctileWeightAgeLowPercent;
	}

	public void setPrctileWeightAgeNormal(int prctileWeightAgeNormal) {
		this.prctileWeightAgeNormal = prctileWeightAgeNormal;
	}

	public void setPrctileWeightAgeNormalPercent(double prctileWeightAgeNormalPercent) {
		this.prctileWeightAgeNormalPercent = prctileWeightAgeNormalPercent;
	}

	public void setPrctileWeightHeightLow(int prctileWeightHeightLow) {
		this.prctileWeightHeightLow = prctileWeightHeightLow;
	}

	public void setPrctileWeightHeightLowPercent(double prctileWeightHeightLowPercent) {
		this.prctileWeightHeightLowPercent = prctileWeightHeightLowPercent;
	}

	public void setPrctileWeightHeightNormal(int prctileWeightHeightNormal) {
		this.prctileWeightHeightNormal = prctileWeightHeightNormal;
	}

	public void setPrctileWeightHeightNormalPercent(double prctileWeightHeightNormalPercent) {
		this.prctileWeightHeightNormalPercent = prctileWeightHeightNormalPercent;
	}

	public void setPrctileWeightObesity(int prctileWeightObesity) {
		this.prctileWeightObesity = prctileWeightObesity;
	}

	public void setPrctileWeightObesityPercent(double prctileWeightObesityPercent) {
		this.prctileWeightObesityPercent = prctileWeightObesityPercent;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTotalFemale(int totalFemale) {
		this.totalFemale = totalFemale;
	}

	public void setTotalMale(int totalMale) {
		this.totalMale = totalMale;
	}

	public void setTotalPerson(int totalPerson) {
		this.totalPerson = totalPerson;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return MedicalExaminationCountJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return MedicalExaminationCountJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
