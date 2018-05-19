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
import java.util.LinkedHashMap;
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

					if (person != null) {
						int key = 0;
						List<Integer> keys = new ArrayList<Integer>();
						Map<Integer, Double> valMap = new LinkedHashMap<Integer, Double>();
						for (MedicalExamination me : list) {
							me.setBirthday(person.getBirthday());
							key = me.getAgeOfTheMoon();
							if (StringUtils.equals(type, "2")) {
								keys.add(key);
								valMap.put(key, me.getHeight());// 年龄别身高
							} else if (StringUtils.equals(type, "3")) {
								keys.add(key);
								valMap.put(key, me.getWeight());// 年龄别体重
							} else if (StringUtils.equals(type, "4")) {
								key = (int) Math.ceil(me.getHeight());
								keys.add(key);
								valMap.put(key, me.getWeight());// 身高别体重
							}
						}

						if (StringUtils.equals(type, "2") || StringUtils.equals(type, "3")) {
							int key1 = 0;
							int key2 = 0;
							double value1 = 0;
							double value2 = 0;
							double avg = 0;
							double startX = 0;
							int start = keys.get(0);
							int end = keys.get(keys.size() - 1);
							//logger.debug("start:" + start);
							for (int i = 24; i < start; i++) {
								valList.add(null);
							}
							for (int i = 0, len = keys.size() - 1; i < len; i++) {
								key1 = keys.get(i);
								key2 = keys.get(i + 1);
								value1 = valMap.get(key1);
								value2 = valMap.get(key2);
								if (key2 - key1 > 0) {
									avg = (value2 - value1) / (key2 - key1);
									avg = Math.round(avg * 10D) / 10D;
									startX = value1;
									for (int k = key1; k < key2; k++) {
										startX = startX + avg;
										startX = Math.round(startX * 10D) / 10D;
										//logger.debug("month:" + k);
										valList.add(startX);
									}
								}
							}
							// valList.add(valMap.get(end));
							for (int i = end; i < 72; i++) {
								valList.add(null);
							}
						} else if (StringUtils.equals(type, "4")) {
							int key1 = 0;
							int key2 = 0;
							double value1 = 0;
							double value2 = 0;
							double avg = 0;
							double startX = 0;
							int start = keys.get(0);
							int end = keys.get(keys.size() - 1);
							//logger.debug("start:" + start);
							for (int i = 80; i < start; i++) {
								valList.add(null);
							}
							for (int i = 0, len = keys.size() - 1; i < len; i++) {
								key1 = keys.get(i);
								key2 = keys.get(i + 1);
								value1 = valMap.get(key1);
								value2 = valMap.get(key2);
								if (key2 - key1 > 0) {
									avg = (value2 - value1) / (key2 - key1);
									avg = Math.round(avg * 10D) / 10D;
									startX = value1;
									for (int k = key1; k < key2; k++) {
										startX = startX + avg;
										startX = Math.round(startX * 10D) / 10D;
										//logger.debug("height:" + k);
										valList.add(startX);
									}
								}
							}
							// valList.add(valMap.get(end));
							for (int i = end; i <= 120; i++) {
								valList.add(null);
							}
						}
						//logger.debug("valList:" + valList);
						logger.debug("valList size:" + valList.size());
						myData.put("Z", valList);
					}
				}
			}

			logger.debug("categoriesList size:" + categoriesList.size());
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
