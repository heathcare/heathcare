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

package com.glaf.heathcare.web.springmvc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;

import com.glaf.heathcare.bean.GrowthSplineBean;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;

@Controller("/heathcare/growthSpline")
@RequestMapping("/heathcare/growthSpline")
public class GrowthSplineController {
	protected static final Log logger = LogFactory.getLog(GrowthSplineController.class);

	protected MedicalExaminationService medicalExaminationService;

	protected PersonService personService;

	public GrowthSplineController() {

	}

	@RequestMapping("/line")
	public ModelAndView line(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		GrowthSplineBean bean = new GrowthSplineBean();
		String standardType = request.getParameter("standardType");
		String type = request.getParameter("type");
		String sex = request.getParameter("sex");

		String personId = request.getParameter("personId");
		Person person = null;
		if (StringUtils.isNotEmpty(personId)) {
			person = personService.getPerson(personId);
			request.setAttribute("person", person);
			sex = person.getSex();
			logger.debug("person:" + person.toJsonObject().toJSONString());
		}

		String title = "2-6岁";
		String xAxisTitle = "";
		String yAxisTitle = "";

		if (StringUtils.equals(sex, "1")) {
			title = title + "男童";
		} else {
			title = title + "女童";
		}

		if (StringUtils.equals(type, "2")) {
			title = title + "年龄别身高（厘米、立位）标准差图表";
			xAxisTitle = "年龄（月或年）";
			yAxisTitle = "身高（厘米、立位）";
		} else if (StringUtils.equals(type, "3")) {
			title = title + "年龄别体重（千克）标准差图表";
			xAxisTitle = "年龄（月或年）";
			yAxisTitle = "体重（千克）";
		} else if (StringUtils.equals(type, "4")) {
			title = title + "身高别体重图表";
			xAxisTitle = "身高(cm)";
			yAxisTitle = "体重（千克）";
		} else if (StringUtils.equals(type, "5")) {
			title = title + "体质指数（BMI）图表";
			yAxisTitle = "BMI";
		}

		request.setAttribute("chartTitle", title);
		request.setAttribute("xAxisTitle", xAxisTitle);
		request.setAttribute("yAxisTitle", yAxisTitle);

		Map<String, List<Double>> myData = null;
		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(sex) && StringUtils.isNotEmpty(standardType)) {
			JSONArray categories = bean.buildJsonArrayCategories(type, sex, standardType);
			request.setAttribute("categories", categories.toJSONString());
			logger.debug("categories json:" + categories.toJSONString());
			List<Integer> categoriesList = bean.getCategories(type, sex, standardType);
			String mtype = request.getParameter("mtype");
			if (StringUtils.isNotEmpty(personId)) {
				MedicalExaminationQuery query = new MedicalExaminationQuery();
				query.personId(personId);
				if (StringUtils.isNotEmpty(mtype)) {
					query.type(mtype);
				}
				query.setOrderBy(" E.HEIGHT_ asc ");

				List<MedicalExamination> list = medicalExaminationService.list(query);
				if (list != null && !list.isEmpty()) {
					logger.debug("list size:" + list.size());

					logger.debug("categories: " + categoriesList);
					logger.debug("categories size: " + categoriesList.size());
					myData = new HashMap<String, List<Double>>();
					List<Double> valList = new ArrayList<Double>();
					double val = 0.0D;
					double prevVal = 0.0D;
					int joinAgeOfMonth = 36;
					int ageOfMonth = 0;
					int maxCheckMonth = 0;
					if (person != null) {
						ageOfMonth = person.getAgeOfTheMoon();
						double minHeight = person.getHeight();
						double minWeight = person.getWeight();
						double maxHeight = 0;

						for (MedicalExamination me : list) {
							if (minHeight == 0) {
								minHeight = me.getHeight();
							}
							if (minWeight == 0) {
								minWeight = me.getWeight();
							}
							if (me.getHeight() > maxHeight) {
								maxHeight = me.getHeight();// 取最大身高
							}
							if (me.getHeight() < minHeight) {
								minHeight = me.getHeight();// 取最小身高
							}
							if (me.getWeight() < minWeight) {
								minWeight = me.getWeight();// 取最小体重
							}
							ageOfMonth = getAgeOfTheMoon(person.getBirthday(), me.getCheckDate());
							if (maxCheckMonth < ageOfMonth) {
								maxCheckMonth = ageOfMonth;
							}
						}

						if (StringUtils.equals(type, "2")) {
							prevVal = person.getHeight();
						} else if (StringUtils.equals(type, "3")) {
							prevVal = person.getWeight();
						} else if (StringUtils.equals(type, "4")) {
							prevVal = person.getWeight();
						}
						joinAgeOfMonth = person.getJoinAgeOfTheMoon();

						for (Integer category : categoriesList) {
							for (MedicalExamination me : list) {
								val = 0.0D;
								if (category == me.getAgeOfTheMoon()) {
									if (StringUtils.equals(type, "2")) {
										val = me.getHeight();// 身高
									} else if (StringUtils.equals(type, "3")) {
										val = me.getWeight();// 体重
									}
									break;
								}
								if (Math.ceil(me.getHeight()) == category) {
									if (StringUtils.equals(type, "4")) {
										val = me.getWeight();// 体重
										logger.debug(me.getHeight() + "=" + val);
										break;
									}
								}
							}
							if (StringUtils.equals(type, "2")) {
								if (category < joinAgeOfMonth || category > maxCheckMonth) {
									valList.add(null);
								} else {
									if (val > prevVal) {
										prevVal = val;
									}
									if (prevVal > minHeight) {
										valList.add(prevVal);
									} else {
										valList.add(minHeight);
									}
								}
							} else if (StringUtils.equals(type, "3")) {
								if (category < joinAgeOfMonth || category > maxCheckMonth) {
									valList.add(null);
								} else {
									if (val < person.getWeight()) {
										val = person.getWeight();
									}
									if (val > prevVal) {
										prevVal = val;
									}
									if (prevVal > minWeight) {
										valList.add(prevVal);
									} else {
										valList.add(minWeight);
									}
								}
							} else if (StringUtils.equals(type, "4")) {
								if (category < minHeight || category > maxHeight) {
									valList.add(null);
								} else {
									if (val < person.getWeight()) {
										val = person.getWeight();
									}
									if (val > prevVal) {
										prevVal = val;
									}
									if (prevVal > minWeight) {
										valList.add(prevVal);
									} else {
										valList.add(minWeight);
									}
								}
							}
						}
						logger.debug("valList:" + valList);
						myData.put("Z", valList);
					}
				}
			}

			JSONArray result = bean.buildJsonArrayData(type, sex, standardType, categoriesList, myData);
			logger.debug("json data:" + result.toJSONString());
			request.setAttribute("seriesData", result.toJSONString());

		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("growthSpline.line");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/growthSpline/line");
	}

	public int getAgeOfTheMoon(Date birthday, Date checkDate) {
		int ageOfTheMoon = 0;
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalExaminationService")
	public void setMedicalExaminationService(MedicalExaminationService medicalExaminationService) {
		this.medicalExaminationService = medicalExaminationService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
