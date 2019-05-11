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

import java.util.*;
import com.glaf.core.query.DataQuery;

public class MedicalExaminationEvaluateQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long batchId;
	protected String checkId;
	protected List<String> tenantIds;
	protected String gradeId;
	protected List<String> gradeIds;
	protected String personId;
	protected List<String> personIds;
	protected String nameLike;
	protected String sex;
	protected Double heightGreaterThanOrEqual;
	protected Double heightLessThanOrEqual;
	protected Integer heightLevel;
	protected Integer heightLevelGreaterThanOrEqual;
	protected Integer heightLevelLessThanOrEqual;
	protected String heightEvaluate;
	protected String heightEvaluateLike;
	protected String weightGreaterThanStd;
	protected Double weightGreaterThanOrEqual;
	protected Double weightLessThanOrEqual;
	protected Integer weightLevel;
	protected Integer weightLevelGreaterThanOrEqual;
	protected Integer weightLevelLessThanOrEqual;
	protected String weightEvaluate;
	protected String weightEvaluateLike;
	protected Double weightOffsetPercentGreaterThanOrEqual;
	protected Double weightHeightPercentGreaterThanOrEqual;
	protected Double weightHeightPercentLessThanOrEqual;
	protected Double bmiGreaterThanOrEqual;
	protected Double bmiLessThanOrEqual;
	protected String jobNo;
	protected String type;
	protected Integer year;
	protected Integer yearGreaterThanOrEqual;
	protected Integer yearLessThanOrEqual;
	protected Integer month;
	protected Long provinceId;
	protected Long cityId;
	protected Long areaId;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public MedicalExaminationEvaluateQuery() {

	}

	public MedicalExaminationEvaluateQuery areaId(Long areaId) {
		if (areaId == null) {
			throw new RuntimeException("areaId is null");
		}
		this.areaId = areaId;
		return this;
	}

	public MedicalExaminationEvaluateQuery batchId(Long batchId) {
		if (batchId == null) {
			throw new RuntimeException("batchId is null");
		}
		this.batchId = batchId;
		return this;
	}

	public MedicalExaminationEvaluateQuery bmiGreaterThanOrEqual(Double bmiGreaterThanOrEqual) {
		if (bmiGreaterThanOrEqual == null) {
			throw new RuntimeException("bmi is null");
		}
		this.bmiGreaterThanOrEqual = bmiGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery bmiLessThanOrEqual(Double bmiLessThanOrEqual) {
		if (bmiLessThanOrEqual == null) {
			throw new RuntimeException("bmi is null");
		}
		this.bmiLessThanOrEqual = bmiLessThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery checkId(String checkId) {
		if (checkId == null) {
			throw new RuntimeException("checkId is null");
		}
		this.checkId = checkId;
		return this;
	}

	public MedicalExaminationEvaluateQuery cityId(Long cityId) {
		if (cityId == null) {
			throw new RuntimeException("cityId is null");
		}
		this.cityId = cityId;
		return this;
	}

	public MedicalExaminationEvaluateQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Long getBatchId() {
		return batchId;
	}

	public Double getBmiGreaterThanOrEqual() {
		return bmiGreaterThanOrEqual;
	}

	public Double getBmiLessThanOrEqual() {
		return bmiLessThanOrEqual;
	}

	public String getCheckId() {
		return checkId;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getGradeId() {
		return gradeId;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public String getHeightEvaluate() {
		return heightEvaluate;
	}

	public String getHeightEvaluateLike() {
		if (heightEvaluateLike != null && heightEvaluateLike.trim().length() > 0) {
			if (!heightEvaluateLike.startsWith("%")) {
				heightEvaluateLike = "%" + heightEvaluateLike;
			}
			if (!heightEvaluateLike.endsWith("%")) {
				heightEvaluateLike = heightEvaluateLike + "%";
			}
		}
		return heightEvaluateLike;
	}

	public Double getHeightGreaterThanOrEqual() {
		return heightGreaterThanOrEqual;
	}

	public Double getHeightLessThanOrEqual() {
		return heightLessThanOrEqual;
	}

	public Integer getHeightLevel() {
		return heightLevel;
	}

	public Integer getHeightLevelGreaterThanOrEqual() {
		return heightLevelGreaterThanOrEqual;
	}

	public Integer getHeightLevelLessThanOrEqual() {
		return heightLevelLessThanOrEqual;
	}

	public String getJobNo() {
		return jobNo;
	}

	public Integer getMonth() {
		return month;
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

			if ("gradeId".equals(sortColumn)) {
				orderBy = "E.GRADEID_" + a_x;
			}

			if ("personId".equals(sortColumn)) {
				orderBy = "E.PERSONID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("sex".equals(sortColumn)) {
				orderBy = "E.SEX_" + a_x;
			}

			if ("height".equals(sortColumn)) {
				orderBy = "E.HEIGHT_" + a_x;
			}

			if ("heightEvaluate".equals(sortColumn)) {
				orderBy = "E.HEIGHTEVALUATE_" + a_x;
			}

			if ("weight".equals(sortColumn)) {
				orderBy = "E.WEIGHT_" + a_x;
			}

			if ("weightEvaluate".equals(sortColumn)) {
				orderBy = "E.WEIGHTEVALUATE_" + a_x;
			}

			if ("weightHeightPercent".equals(sortColumn)) {
				orderBy = "E.WEIGHTHEIGHTPERCENT_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("checkDate".equals(sortColumn)) {
				orderBy = "E.CHECKDATE_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getPersonId() {
		return personId;
	}

	public List<String> getPersonIds() {
		return personIds;
	}

	public String getSex() {
		return sex;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getType() {
		return type;
	}

	public String getWeightEvaluate() {
		return weightEvaluate;
	}

	public String getWeightEvaluateLike() {
		if (weightEvaluateLike != null && weightEvaluateLike.trim().length() > 0) {
			if (!weightEvaluateLike.startsWith("%")) {
				weightEvaluateLike = "%" + weightEvaluateLike;
			}
			if (!weightEvaluateLike.endsWith("%")) {
				weightEvaluateLike = weightEvaluateLike + "%";
			}
		}
		return weightEvaluateLike;
	}

	public Double getWeightGreaterThanOrEqual() {
		return weightGreaterThanOrEqual;
	}

	public String getWeightGreaterThanStd() {
		return weightGreaterThanStd;
	}

	public Double getWeightHeightPercentGreaterThanOrEqual() {
		return weightHeightPercentGreaterThanOrEqual;
	}

	public Double getWeightHeightPercentLessThanOrEqual() {
		return weightHeightPercentLessThanOrEqual;
	}

	public Double getWeightLessThanOrEqual() {
		return weightLessThanOrEqual;
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

	public Double getWeightOffsetPercentGreaterThanOrEqual() {
		return weightOffsetPercentGreaterThanOrEqual;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getYearGreaterThanOrEqual() {
		return yearGreaterThanOrEqual;
	}

	public Integer getYearLessThanOrEqual() {
		return yearLessThanOrEqual;
	}

	public MedicalExaminationEvaluateQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public MedicalExaminationEvaluateQuery gradeIds(List<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	public MedicalExaminationEvaluateQuery heightEvaluate(String heightEvaluate) {
		if (heightEvaluate == null) {
			throw new RuntimeException("heightEvaluate is null");
		}
		this.heightEvaluate = heightEvaluate;
		return this;
	}

	public MedicalExaminationEvaluateQuery heightEvaluateLike(String heightEvaluateLike) {
		if (heightEvaluateLike == null) {
			throw new RuntimeException("heightEvaluate is null");
		}
		this.heightEvaluateLike = heightEvaluateLike;
		return this;
	}

	public MedicalExaminationEvaluateQuery heightGreaterThanOrEqual(Double heightGreaterThanOrEqual) {
		if (heightGreaterThanOrEqual == null) {
			throw new RuntimeException("height is null");
		}
		this.heightGreaterThanOrEqual = heightGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery heightLessThanOrEqual(Double heightLessThanOrEqual) {
		if (heightLessThanOrEqual == null) {
			throw new RuntimeException("height is null");
		}
		this.heightLessThanOrEqual = heightLessThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery heightLevel(Integer heightLevel) {
		if (heightLevel == null) {
			throw new RuntimeException("heightLevel is null");
		}
		this.heightLevel = heightLevel;
		return this;
	}

	public MedicalExaminationEvaluateQuery heightLevelGreaterThanOrEqual(Integer heightLevelGreaterThanOrEqual) {
		if (heightLevelGreaterThanOrEqual == null) {
			throw new RuntimeException("heightLevel is null");
		}
		this.heightLevelGreaterThanOrEqual = heightLevelGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery heightLevelLessThanOrEqual(Integer heightLevelLessThanOrEqual) {
		if (heightLevelLessThanOrEqual == null) {
			throw new RuntimeException("heightLevel is null");
		}
		this.heightLevelLessThanOrEqual = heightLevelLessThanOrEqual;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("gradeId", "GRADEID_");
		addColumn("personId", "PERSONID_");
		addColumn("name", "NAME_");
		addColumn("sex", "SEX_");
		addColumn("height", "HEIGHT_");
		addColumn("heightEvaluate", "HEIGHTEVALUATE_");
		addColumn("weight", "WEIGHT_");
		addColumn("weightEvaluate", "WEIGHTEVALUATE_");
		addColumn("weightHeightPercent", "WEIGHTHEIGHTPERCENT_");
		addColumn("type", "TYPE_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public MedicalExaminationEvaluateQuery jobNo(String jobNo) {
		if (jobNo == null) {
			throw new RuntimeException("jobNo is null");
		}
		this.jobNo = jobNo;
		return this;
	}

	public MedicalExaminationEvaluateQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public MedicalExaminationEvaluateQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public MedicalExaminationEvaluateQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public MedicalExaminationEvaluateQuery personIds(List<String> personIds) {
		if (personIds == null) {
			throw new RuntimeException("personIds is empty ");
		}
		this.personIds = personIds;
		return this;
	}

	public MedicalExaminationEvaluateQuery provinceId(Long provinceId) {
		if (provinceId == null) {
			throw new RuntimeException("provinceId is null");
		}
		this.provinceId = provinceId;
		return this;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public void setBmiGreaterThanOrEqual(Double bmiGreaterThanOrEqual) {
		this.bmiGreaterThanOrEqual = bmiGreaterThanOrEqual;
	}

	public void setBmiLessThanOrEqual(Double bmiLessThanOrEqual) {
		this.bmiLessThanOrEqual = bmiLessThanOrEqual;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setHeightEvaluate(String heightEvaluate) {
		this.heightEvaluate = heightEvaluate;
	}

	public void setHeightEvaluateLike(String heightEvaluateLike) {
		this.heightEvaluateLike = heightEvaluateLike;
	}

	public void setHeightGreaterThanOrEqual(Double heightGreaterThanOrEqual) {
		this.heightGreaterThanOrEqual = heightGreaterThanOrEqual;
	}

	public void setHeightLessThanOrEqual(Double heightLessThanOrEqual) {
		this.heightLessThanOrEqual = heightLessThanOrEqual;
	}

	public void setHeightLevel(Integer heightLevel) {
		this.heightLevel = heightLevel;
	}

	public void setHeightLevelGreaterThanOrEqual(Integer heightLevelGreaterThanOrEqual) {
		this.heightLevelGreaterThanOrEqual = heightLevelGreaterThanOrEqual;
	}

	public void setHeightLevelLessThanOrEqual(Integer heightLevelLessThanOrEqual) {
		this.heightLevelLessThanOrEqual = heightLevelLessThanOrEqual;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setPersonIds(List<String> personIds) {
		this.personIds = personIds;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWeightEvaluate(String weightEvaluate) {
		this.weightEvaluate = weightEvaluate;
	}

	public void setWeightEvaluateLike(String weightEvaluateLike) {
		this.weightEvaluateLike = weightEvaluateLike;
	}

	public void setWeightGreaterThanOrEqual(Double weightGreaterThanOrEqual) {
		this.weightGreaterThanOrEqual = weightGreaterThanOrEqual;
	}

	public void setWeightGreaterThanStd(String weightGreaterThanStd) {
		this.weightGreaterThanStd = weightGreaterThanStd;
	}

	public void setWeightHeightPercentGreaterThanOrEqual(Double weightHeightPercentGreaterThanOrEqual) {
		this.weightHeightPercentGreaterThanOrEqual = weightHeightPercentGreaterThanOrEqual;
	}

	public void setWeightHeightPercentLessThanOrEqual(Double weightHeightPercentLessThanOrEqual) {
		this.weightHeightPercentLessThanOrEqual = weightHeightPercentLessThanOrEqual;
	}

	public void setWeightLessThanOrEqual(Double weightLessThanOrEqual) {
		this.weightLessThanOrEqual = weightLessThanOrEqual;
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

	public void setWeightOffsetPercentGreaterThanOrEqual(Double weightOffsetPercentGreaterThanOrEqual) {
		this.weightOffsetPercentGreaterThanOrEqual = weightOffsetPercentGreaterThanOrEqual;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setYearGreaterThanOrEqual(Integer yearGreaterThanOrEqual) {
		this.yearGreaterThanOrEqual = yearGreaterThanOrEqual;
	}

	public void setYearLessThanOrEqual(Integer yearLessThanOrEqual) {
		this.yearLessThanOrEqual = yearLessThanOrEqual;
	}

	public MedicalExaminationEvaluateQuery sex(String sex) {
		if (sex == null) {
			throw new RuntimeException("sex is null");
		}
		this.sex = sex;
		return this;
	}

	public MedicalExaminationEvaluateQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public MedicalExaminationEvaluateQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public MedicalExaminationEvaluateQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightEvaluate(String weightEvaluate) {
		if (weightEvaluate == null) {
			throw new RuntimeException("weightEvaluate is null");
		}
		this.weightEvaluate = weightEvaluate;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightEvaluateLike(String weightEvaluateLike) {
		if (weightEvaluateLike == null) {
			throw new RuntimeException("weightEvaluate is null");
		}
		this.weightEvaluateLike = weightEvaluateLike;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightGreaterThanOrEqual(Double weightGreaterThanOrEqual) {
		if (weightGreaterThanOrEqual == null) {
			throw new RuntimeException("weight is null");
		}
		this.weightGreaterThanOrEqual = weightGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightGreaterThanStd(String weightGreaterThanStd) {
		if (weightGreaterThanStd == null) {
			throw new RuntimeException("weightGreaterThanStd is null");
		}
		this.weightGreaterThanStd = weightGreaterThanStd;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightHeightPercentGreaterThanOrEqual(
			Double weightHeightPercentGreaterThanOrEqual) {
		if (weightHeightPercentGreaterThanOrEqual == null) {
			throw new RuntimeException("weightHeightPercent is null");
		}
		this.weightHeightPercentGreaterThanOrEqual = weightHeightPercentGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightHeightPercentLessThanOrEqual(
			Double weightHeightPercentLessThanOrEqual) {
		if (weightHeightPercentLessThanOrEqual == null) {
			throw new RuntimeException("weightHeightPercent is null");
		}
		this.weightHeightPercentLessThanOrEqual = weightHeightPercentLessThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightLessThanOrEqual(Double weightLessThanOrEqual) {
		if (weightLessThanOrEqual == null) {
			throw new RuntimeException("weight is null");
		}
		this.weightLessThanOrEqual = weightLessThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightLevel(Integer weightLevel) {
		if (weightLevel == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevel = weightLevel;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightLevelGreaterThanOrEqual(Integer weightLevelGreaterThanOrEqual) {
		if (weightLevelGreaterThanOrEqual == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevelGreaterThanOrEqual = weightLevelGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightLevelLessThanOrEqual(Integer weightLevelLessThanOrEqual) {
		if (weightLevelLessThanOrEqual == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevelLessThanOrEqual = weightLevelLessThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery weightOffsetPercentGreaterThanOrEqual(
			Double weightOffsetPercentGreaterThanOrEqual) {
		if (weightOffsetPercentGreaterThanOrEqual == null) {
			throw new RuntimeException("weightOffsetPercentGreaterThanOrEqual is null");
		}
		this.weightOffsetPercentGreaterThanOrEqual = weightOffsetPercentGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

	public MedicalExaminationEvaluateQuery yearGreaterThanOrEqual(Integer yearGreaterThanOrEqual) {
		if (yearGreaterThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearGreaterThanOrEqual = yearGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationEvaluateQuery yearLessThanOrEqual(Integer yearLessThanOrEqual) {
		if (yearLessThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearLessThanOrEqual = yearLessThanOrEqual;
		return this;
	}
}