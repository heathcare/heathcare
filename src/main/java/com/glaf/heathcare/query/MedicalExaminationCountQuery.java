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

public class MedicalExaminationCountQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tenantIds;
	protected String gradeId;
	protected List<String> gradeIds;
	protected String gradeNotNull;
	protected String checkId;
	protected List<String> checkIds;
	protected Integer checkPersonGreaterThanOrEqual;
	protected String type;
	protected Integer year;
	protected Integer yearGreaterThanOrEqual;
	protected Integer yearLessThanOrEqual;
	protected List<Integer> years;
	protected Integer month;
	protected Integer monthGreaterThanOrEqual;
	protected Integer monthLessThanOrEqual;
	protected List<Integer> months;
	protected Integer gradeYear;
	protected String targetType;

	public MedicalExaminationCountQuery() {

	}

	public String getGradeNotNull() {
		return gradeNotNull;
	}

	public void setGradeNotNull(String gradeNotNull) {
		this.gradeNotNull = gradeNotNull;
	}

	public MedicalExaminationCountQuery gradeNotNull(String gradeNotNull) {
		if (gradeNotNull == null) {
			throw new RuntimeException("gradeNotNull is null");
		}
		this.gradeNotNull = gradeNotNull;
		return this;
	}

	public MedicalExaminationCountQuery checkId(String checkId) {
		if (checkId == null) {
			throw new RuntimeException("checkId is null");
		}
		this.checkId = checkId;
		return this;
	}

	public MedicalExaminationCountQuery checkIds(List<String> checkIds) {
		if (checkIds == null) {
			throw new RuntimeException("checkIds is empty ");
		}
		this.checkIds = checkIds;
		return this;
	}

	public MedicalExaminationCountQuery checkPersonGreaterThanOrEqual(Integer checkPersonGreaterThanOrEqual) {
		if (checkPersonGreaterThanOrEqual == null) {
			throw new RuntimeException("checkPerson is null");
		}
		this.checkPersonGreaterThanOrEqual = checkPersonGreaterThanOrEqual;
		return this;
	}

	public String getCheckId() {
		return checkId;
	}

	public List<String> getCheckIds() {
		return checkIds;
	}

	public Integer getCheckPersonGreaterThanOrEqual() {
		return checkPersonGreaterThanOrEqual;
	}

	public String getGradeId() {
		return gradeId;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public Integer getGradeYear() {
		return gradeYear;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getMonthGreaterThanOrEqual() {
		return monthGreaterThanOrEqual;
	}

	public Integer getMonthLessThanOrEqual() {
		return monthLessThanOrEqual;
	}

	public List<Integer> getMonths() {
		return months;
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

			if ("gradeName".equals(sortColumn)) {
				orderBy = "E.GRADENAME_" + a_x;
			}

			if ("checkId".equals(sortColumn)) {
				orderBy = "E.CHECKID_" + a_x;
			}

			if ("totalPerson".equals(sortColumn)) {
				orderBy = "E.TOTALPERSON_" + a_x;
			}

			if ("checkPerson".equals(sortColumn)) {
				orderBy = "E.CHECKPERSON_" + a_x;
			}

			if ("checkPercent".equals(sortColumn)) {
				orderBy = "E.CHECKPCT_" + a_x;
			}

			if ("meanWeightNormal".equals(sortColumn)) {
				orderBy = "E.MEANWEIGHTNORMAL_" + a_x;
			}

			if ("meanWeightNormalPercent".equals(sortColumn)) {
				orderBy = "E.MEANWEIGHTNORMAL_PCT_" + a_x;
			}

			if ("meanHeightNormal".equals(sortColumn)) {
				orderBy = "E.MEANHEIGHTNORMAL_" + a_x;
			}

			if ("meanHeightNormalPercent".equals(sortColumn)) {
				orderBy = "E.MEANHEIGHTNORMAL_PCT_" + a_x;
			}

			if ("meanWeightLow".equals(sortColumn)) {
				orderBy = "E.MEANWEIGHTLOW_" + a_x;
			}

			if ("meanWeightLowPercent".equals(sortColumn)) {
				orderBy = "E.MEANWEIGHTLOWPCT_" + a_x;
			}

			if ("meanHeightLow".equals(sortColumn)) {
				orderBy = "E.MEANHEIGHTLOW_" + a_x;
			}

			if ("meanHeightLowPercent".equals(sortColumn)) {
				orderBy = "E.MEANHEIGHTLOWPCT_" + a_x;
			}

			if ("meanWeightSkinny".equals(sortColumn)) {
				orderBy = "E.MEANWEIGHTSKINNY_" + a_x;
			}

			if ("meanWeightSkinnyPercent".equals(sortColumn)) {
				orderBy = "E.MEANWEIGHTSKINNYPCT_" + a_x;
			}

			if ("meanOverWeight".equals(sortColumn)) {
				orderBy = "E.MEANOVERWEIGHT_" + a_x;
			}

			if ("meanOverWeightPercent".equals(sortColumn)) {
				orderBy = "E.MEANOVERWEIGHTPCT_" + a_x;
			}

			if ("meanWeightObesity".equals(sortColumn)) {
				orderBy = "E.MEANWEIGHTOBESITY_" + a_x;
			}

			if ("meanWeightObesityPercent".equals(sortColumn)) {
				orderBy = "E.MEANWEIGHTOBESITYPCT_" + a_x;
			}

			if ("prctileWeightHeightNormal".equals(sortColumn)) {
				orderBy = "E.PRCTILE_WH_NORMAL_" + a_x;
			}

			if ("prctileWeightHeightNormalPercent".equals(sortColumn)) {
				orderBy = "E.PRCTILE_WH_NORMAL_PCT_" + a_x;
			}

			if ("prctileWeightAgeNormal".equals(sortColumn)) {
				orderBy = "E.PRCTILE_WA_NORMAL_" + a_x;
			}

			if ("prctileWeightAgeNormalPercent".equals(sortColumn)) {
				orderBy = "E.PRCTILE_WA_NORMAL_PCT_" + a_x;
			}

			if ("prctileHeightAgeNormal".equals(sortColumn)) {
				orderBy = "E.PRCTILE_HA_NORMAL_" + a_x;
			}

			if ("prctileHeightAgeNormalPercent".equals(sortColumn)) {
				orderBy = "E.PRCTILE_HA_NORMAL_PCT_" + a_x;
			}

			if ("prctileHeightAgeLow".equals(sortColumn)) {
				orderBy = "E.PRCTILE_HA_LOW_" + a_x;
			}

			if ("prctileHeightAgeLowPercent".equals(sortColumn)) {
				orderBy = "E.PRCTILE_HA_LOWPCT_" + a_x;
			}

			if ("prctileWeightHeightLow".equals(sortColumn)) {
				orderBy = "E.PRCTILE_WH_LOW_" + a_x;
			}

			if ("prctileWeightHeightLowPercent".equals(sortColumn)) {
				orderBy = "E.PRCTILE_WH_LOWPCT_" + a_x;
			}

			if ("prctileWeightAgeLow".equals(sortColumn)) {
				orderBy = "E.PRCTILE_WA_LOW_" + a_x;
			}

			if ("prctileWeightAgeLowPercent".equals(sortColumn)) {
				orderBy = "E.PRCTILE_WA_LOWPCT_" + a_x;
			}

			if ("prctileOverWeight".equals(sortColumn)) {
				orderBy = "E.PRCTILEOVERWEIGHT_" + a_x;
			}

			if ("prctileOverWeightPercent".equals(sortColumn)) {
				orderBy = "E.PRCTILEOVERWEIGHTPCT_" + a_x;
			}

			if ("prctileWeightObesity".equals(sortColumn)) {
				orderBy = "E.PRCTILEWEIGHTOBESITY_" + a_x;
			}

			if ("prctileWeightObesityPercent".equals(sortColumn)) {
				orderBy = "E.PRCTILEWEIGHTOBESITYPCT_" + a_x;
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

	public String getTargetType() {
		return targetType;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getType() {
		return type;
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

	public List<Integer> getYears() {
		return years;
	}

	public MedicalExaminationCountQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public MedicalExaminationCountQuery gradeIds(List<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	public MedicalExaminationCountQuery gradeYear(Integer gradeYear) {
		if (gradeYear == null) {
			throw new RuntimeException("gradeYear is null");
		}
		this.gradeYear = gradeYear;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("gradeId", "GRADEID_");
		addColumn("gradeName", "GRADENAME_");
		addColumn("checkId", "CHECKID_");
		addColumn("totalPerson", "TOTALPERSON_");
		addColumn("checkPerson", "CHECKPERSON_");
		addColumn("checkPercent", "CHECKPCT_");
		addColumn("meanWeightNormal", "MEANWEIGHTNORMAL_");
		addColumn("meanWeightNormalPercent", "MEANWEIGHTNORMAL_PCT_");
		addColumn("meanHeightNormal", "MEANHEIGHTNORMAL_");
		addColumn("meanHeightNormalPercent", "MEANHEIGHTNORMAL_PCT_");
		addColumn("meanWeightLow", "MEANWEIGHTLOW_");
		addColumn("meanWeightLowPercent", "MEANWEIGHTLOWPCT_");
		addColumn("meanHeightLow", "MEANHEIGHTLOW_");
		addColumn("meanHeightLowPercent", "MEANHEIGHTLOWPCT_");
		addColumn("meanWeightSkinny", "MEANWEIGHTSKINNY_");
		addColumn("meanWeightSkinnyPercent", "MEANWEIGHTSKINNYPCT_");
		addColumn("meanOverWeight", "MEANOVERWEIGHT_");
		addColumn("meanOverWeightPercent", "MEANOVERWEIGHTPCT_");
		addColumn("meanWeightObesity", "MEANWEIGHTOBESITY_");
		addColumn("meanWeightObesityPercent", "MEANWEIGHTOBESITYPCT_");
		addColumn("prctileWeightHeightNormal", "PRCTILE_WH_NORMAL_");
		addColumn("prctileWeightHeightNormalPercent", "PRCTILE_WH_NORMAL_PCT_");
		addColumn("prctileWeightAgeNormal", "PRCTILE_WA_NORMAL_");
		addColumn("prctileWeightAgeNormalPercent", "PRCTILE_WA_NORMAL_PCT_");
		addColumn("prctileHeightAgeNormal", "PRCTILE_HA_NORMAL_");
		addColumn("prctileHeightAgeNormalPercent", "PRCTILE_HA_NORMAL_PCT_");
		addColumn("prctileHeightAgeLow", "PRCTILE_HA_LOW_");
		addColumn("prctileHeightAgeLowPercent", "PRCTILE_HA_LOWPCT_");
		addColumn("prctileWeightHeightLow", "PRCTILE_WH_LOW_");
		addColumn("prctileWeightHeightLowPercent", "PRCTILE_WH_LOWPCT_");
		addColumn("prctileWeightAgeLow", "PRCTILE_WA_LOW_");
		addColumn("prctileWeightAgeLowPercent", "PRCTILE_WA_LOWPCT_");
		addColumn("prctileOverWeight", "PRCTILEOVERWEIGHT_");
		addColumn("prctileOverWeightPercent", "PRCTILEOVERWEIGHTPCT_");
		addColumn("prctileWeightObesity", "PRCTILEWEIGHTOBESITY_");
		addColumn("prctileWeightObesityPercent", "PRCTILEWEIGHTOBESITYPCT_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("createTime", "CREATETIME_");
	}

	public MedicalExaminationCountQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public MedicalExaminationCountQuery monthGreaterThanOrEqual(Integer monthGreaterThanOrEqual) {
		if (monthGreaterThanOrEqual == null) {
			throw new RuntimeException("month is null");
		}
		this.monthGreaterThanOrEqual = monthGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationCountQuery monthLessThanOrEqual(Integer monthLessThanOrEqual) {
		if (monthLessThanOrEqual == null) {
			throw new RuntimeException("month is null");
		}
		this.monthLessThanOrEqual = monthLessThanOrEqual;
		return this;
	}

	public MedicalExaminationCountQuery months(List<Integer> months) {
		if (months == null) {
			throw new RuntimeException("months is empty ");
		}
		this.months = months;
		return this;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCheckIds(List<String> checkIds) {
		this.checkIds = checkIds;
	}

	public void setCheckPersonGreaterThanOrEqual(Integer checkPersonGreaterThanOrEqual) {
		this.checkPersonGreaterThanOrEqual = checkPersonGreaterThanOrEqual;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setGradeYear(Integer gradeYear) {
		this.gradeYear = gradeYear;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setMonthGreaterThanOrEqual(Integer monthGreaterThanOrEqual) {
		this.monthGreaterThanOrEqual = monthGreaterThanOrEqual;
	}

	public void setMonthLessThanOrEqual(Integer monthLessThanOrEqual) {
		this.monthLessThanOrEqual = monthLessThanOrEqual;
	}

	public void setMonths(List<Integer> months) {
		this.months = months;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setType(String type) {
		this.type = type;
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

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public MedicalExaminationCountQuery targetType(String targetType) {
		if (targetType == null) {
			throw new RuntimeException("targetType is null");
		}
		this.targetType = targetType;
		return this;
	}

	public MedicalExaminationCountQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public MedicalExaminationCountQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public MedicalExaminationCountQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

	public MedicalExaminationCountQuery yearGreaterThanOrEqual(Integer yearGreaterThanOrEqual) {
		if (yearGreaterThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearGreaterThanOrEqual = yearGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationCountQuery yearLessThanOrEqual(Integer yearLessThanOrEqual) {
		if (yearLessThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearLessThanOrEqual = yearLessThanOrEqual;
		return this;
	}

	public MedicalExaminationCountQuery years(List<Integer> years) {
		if (years == null) {
			throw new RuntimeException("years is empty ");
		}
		this.years = years;
		return this;
	}

}