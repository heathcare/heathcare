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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;

import com.glaf.core.config.Environment;
import com.glaf.core.domain.Database;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.RequestUtils;

import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.IMedicalEvaluate;
import com.glaf.heathcare.domain.MedicalExaminationCount;
import com.glaf.heathcare.domain.MedicalExaminationEvaluate;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.helper.MedicalExaminationEvaluateHelper;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.MedicalExaminationCountQuery;
import com.glaf.heathcare.query.MedicalExaminationEvaluateQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GradePersonRelationService;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationCountService;
import com.glaf.heathcare.service.MedicalExaminationEvaluateService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/medicalExaminationChart")
@RequestMapping("/heathcare/medicalExaminationChart")
public class MedicalExaminationChartController {
	protected static final Log logger = LogFactory.getLog(MedicalExaminationChartController.class);

	protected static final Semaphore semaphore2 = new Semaphore(20);

	protected MedicalExaminationService medicalExaminationService;

	protected MedicalExaminationCountService medicalExaminationCountService;

	protected MedicalExaminationEvaluateService medicalExaminationEvaluateService;

	protected GradeInfoService gradeInfoService;

	protected GradePersonRelationService gradePersonRelationService;

	protected GrowthStandardService growthStandardService;

	protected PersonService personService;

	protected IDatabaseService databaseService;

	protected SysTenantService sysTenantService;

	public MedicalExaminationChartController() {

	}

	@RequestMapping("/columnLine")
	public ModelAndView columnLine(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		String tenantId = request.getParameter("tenantId");

		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			tenantId = loginContext.getTenantId();
		}

		String type = request.getParameter("type");
		String index = request.getParameter("index");
		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");
		if (year > 0 && StringUtils.isNotEmpty(tenantId) && StringUtils.isNotEmpty(type)) {
			SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
			request.setAttribute("tenant", tenant);
			request.setAttribute("tenantId", tenant.getTenantId());

			List<Integer> months = new ArrayList<Integer>();

			for (int i = 1; i <= 12; i++) {
				months.add(i);
			}

			request.setAttribute("months", months);
			request.setAttribute("ts", System.currentTimeMillis());

			String chartType = request.getParameter("chartType");
			if (StringUtils.isEmpty(chartType)) {
				chartType = "column";
			}

			if (index == null) {
				index = "B";
			}

			request.setAttribute("index", index);
			request.setAttribute("chartType", chartType);

			MedicalExaminationEvaluateHelper helper = new MedicalExaminationEvaluateHelper();
			String systemName = Environment.getCurrentSystemName();
			Collection<MedicalExaminationCount> rows = null;
			List<MedicalExaminationEvaluate> exams = null;
			List<Person> persons = null;
			Database database = null;
			try {
				List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(tenantId);
				if (grades != null && !grades.isEmpty()) {
					persons = personService.getTenantPersons(tenantId);
					if (persons != null && !persons.isEmpty()) {
						Map<String, Person> personMap = new HashMap<String, Person>();
						for (Person person : persons) {
							if (person.getBirthday() != null && StringUtils.isNotEmpty(person.getSex())) {
								personMap.put(person.getId(), person);
							}
						}

						List<String> gradeIds = new ArrayList<String>();
						for (GradeInfo grade : grades) {
							if (!gradeIds.contains(grade.getId())) {
								gradeIds.add(grade.getId());
							}
						}

						List<GrowthStandard> standards = growthStandardService.getAllGrowthStandards();

						Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
						if (standards != null && !standards.isEmpty()) {
							for (GrowthStandard gs : standards) {
								gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
							}
						}

						MedicalExaminationEvaluateQuery query2 = new MedicalExaminationEvaluateQuery();
						query2.tenantId(tenantId);
						query2.gradeIds(gradeIds);
						query2.year(year);
						query2.month(month);
						try {
							Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
							database = databaseService.getDatabaseByMapping("etl");
							if (database != null) {
								Environment.setCurrentSystemName(database.getName());
							}
							exams = medicalExaminationEvaluateService.list(query2);
						} catch (Exception ex) {
							// ex.printStackTrace();
							throw new RuntimeException(ex);
						} finally {
							com.glaf.core.config.Environment.setCurrentSystemName(systemName);
						}
						if (exams != null && !exams.isEmpty()) {
							// Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
							Map<String, Integer> personCountMap = gradePersonRelationService.getPersonCountMap(tenantId,
									year, month);
							List<IMedicalEvaluate> rowsx = new ArrayList<IMedicalEvaluate>();
							for (MedicalExaminationEvaluate exam : exams) {
								rowsx.add(exam);
							}
							rows = helper.populate(tenantId, year, month, type, grades, persons, rowsx, standards,
									personCountMap);
							if (rows != null && !rows.isEmpty()) {
								String title = "";
								String xAxisTitle = "班级";
								String yAxisTitle = "";
								if (StringUtils.equals(type, "1")) {
									title = year + "年入园体检";
								} else if (StringUtils.equals(type, "5")) {
									title = year + "年" + month + "月定期体检";
								} else if (StringUtils.equals(type, "7")) {
									title = year + "年" + month + "月专项体检";
								}

								title = tenant.getName() + title + "统计图";

								if (StringUtils.equals(index, "A")) {
									yAxisTitle = "体重正常人数";
								} else if (StringUtils.equals(index, "B")) {
									yAxisTitle = "身高正常人数";
								} else if (StringUtils.equals(index, "C")) {
									yAxisTitle = "体重低于2SD人数";
								} else if (StringUtils.equals(index, "D")) {
									yAxisTitle = "身高低于2SD人数";
								} else if (StringUtils.equals(index, "E")) {
									yAxisTitle = "消瘦人数";
								} else if (StringUtils.equals(index, "F")) {
									yAxisTitle = "超重人数";
								} else if (StringUtils.equals(index, "G")) {
									yAxisTitle = "肥胖人数";
								}

								request.setAttribute("chartTitle", title);
								request.setAttribute("xAxisTitle", xAxisTitle);
								request.setAttribute("yAxisTitle", yAxisTitle);

								JSONArray categories = new JSONArray();
								for (MedicalExaminationCount cnt : rows) {
									categories.add(cnt.getGradeName());
								}
								categories.add("全园");
								request.setAttribute("categories", categories.toJSONString());

								JSONArray result = new JSONArray();
								JSONObject json0 = new JSONObject();
								JSONObject json1 = new JSONObject();
								json0.put("name", "应检人数");
								json1.put("name", "实检人数");

								JSONObject json2 = new JSONObject();
								if (StringUtils.equals(index, "A")) {
									json2.put("name", "体重正常人数");
								} else if (StringUtils.equals(index, "B")) {
									json2.put("name", "身高正常人数");
								} else if (StringUtils.equals(index, "C")) {
									json2.put("name", "体重低于2SD人数");
								} else if (StringUtils.equals(index, "D")) {
									json2.put("name", "身高低于2SD人数");
								} else if (StringUtils.equals(index, "E")) {
									json2.put("name", "消瘦人数");
								} else if (StringUtils.equals(index, "F")) {
									json2.put("name", "超重人数");
								} else if (StringUtils.equals(index, "G")) {
									json2.put("name", "肥胖人数");
								}

								JSONArray array0 = new JSONArray();
								JSONArray array1 = new JSONArray();
								JSONArray array2 = new JSONArray();

								int totalPerson = 0;
								int checkPerson = 0;
								int evalPerson = 0;

								for (MedicalExaminationCount cnt : rows) {
									totalPerson = totalPerson + cnt.getTotalPerson();
									checkPerson = checkPerson + cnt.getCheckPerson();
									if (StringUtils.equals(index, "A")) {
										evalPerson = evalPerson + cnt.getMeanWeightNormal();
									} else if (StringUtils.equals(index, "B")) {
										evalPerson = evalPerson + cnt.getMeanHeightNormal();
									} else if (StringUtils.equals(index, "C")) {
										evalPerson = evalPerson + cnt.getMeanWeightLow();
									} else if (StringUtils.equals(index, "D")) {
										evalPerson = evalPerson + cnt.getMeanHeightLow();
									} else if (StringUtils.equals(index, "E")) {
										evalPerson = evalPerson + cnt.getMeanWeightSkinny();
									} else if (StringUtils.equals(index, "F")) {
										evalPerson = evalPerson + cnt.getMeanOverWeight();
									} else if (StringUtils.equals(index, "G")) {
										evalPerson = evalPerson + cnt.getMeanWeightObesity();
									}
								}

								for (MedicalExaminationCount cnt : rows) {
									array0.add(cnt.getTotalPerson());
									array1.add(cnt.getCheckPerson());

									if (StringUtils.equals(index, "A")) {
										array2.add(cnt.getMeanWeightNormal());
									} else if (StringUtils.equals(index, "B")) {
										array2.add(cnt.getMeanHeightNormal());
									} else if (StringUtils.equals(index, "C")) {
										array2.add(cnt.getMeanWeightLow());
									} else if (StringUtils.equals(index, "D")) {
										array2.add(cnt.getMeanHeightLow());
									} else if (StringUtils.equals(index, "E")) {
										array2.add(cnt.getMeanWeightSkinny());
									} else if (StringUtils.equals(index, "F")) {
										array2.add(cnt.getMeanOverWeight());
									} else if (StringUtils.equals(index, "G")) {
										array2.add(cnt.getMeanWeightObesity());
									}
								}

								if ("Y".equals(request.getParameter("all"))) {
									array0.add(totalPerson);
									array1.add(checkPerson);
									array2.add(evalPerson);
								}

								if (StringUtils.equals(chartType, "bar")) {
									json0.put("data", array2);
									json1.put("data", array1);
									json2.put("data", array0);

									json0.put("name", json2.get("name"));
									json1.put("name", "实检人数");
									json2.put("name", "应检人数");
								} else {
									json0.put("data", array0);
									json1.put("data", array1);
									json2.put("data", array2);
								}

								result.add(json0);
								result.add(json1);
								result.add(json2);

								request.setAttribute("seriesData", result.toJSONString());
							}
						}
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
			} finally {
				com.glaf.core.config.Environment.setCurrentSystemName(systemName);
				if (exams != null) {
					exams.clear();
					exams = null;
				}
				if (rows != null) {
					rows.clear();
					rows = null;
				}
				if (persons != null) {
					persons.clear();
					persons = null;
				}
			}
		}
		return new ModelAndView("/heathcare/medicalExamination/columnLine", modelMap);
	}

	@RequestMapping("/gradeChart")
	public ModelAndView gradeChart(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		String tenantId = request.getParameter("tenantId");
		String gradeId = request.getParameter("gradeId");

		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			tenantId = loginContext.getTenantId();
		}

		if (StringUtils.isNotEmpty(tenantId)) {
			SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
			request.setAttribute("tenant", tenant);
			request.setAttribute("tenantId", tenant.getTenantId());
			request.setAttribute("ts", System.currentTimeMillis());

			String chartType = request.getParameter("chartType");
			if (StringUtils.isEmpty(chartType)) {
				chartType = "column";
			}

			String index = request.getParameter("index");
			if (index == null) {
				index = "B";
			}

			String type = request.getParameter("type");
			int gradeYear = RequestUtils.getInt(request, "gradeYear");

			request.setAttribute("index", index);
			request.setAttribute("chartType", chartType);

			java.util.Calendar calendar = Calendar.getInstance();
			calendar.setTime(new java.util.Date());
			int year = calendar.get(Calendar.YEAR);

			List<Integer> years = new ArrayList<Integer>();
			years.add(year - 3);
			years.add(year - 2);
			years.add(year - 1);
			years.add(year);

			request.setAttribute("years", years);

			GradeInfoQuery queryx = new GradeInfoQuery();
			queryx.tenantId(tenantId);
			queryx.locked(0);
			queryx.deleteFlag(0);
			List<GradeInfo> grades = gradeInfoService.list(queryx);
			request.setAttribute("grades", grades);

			GradeInfo gradeInfo = gradeInfoService.getGradeInfo(gradeId);
			if (gradeInfo != null) {
				request.setAttribute("gradeInfo", gradeInfo);
			}

			MedicalExaminationCountQuery query = new MedicalExaminationCountQuery();
			query.tenantId(tenantId);
			if (gradeInfo != null) {
				query.gradeId(gradeId);
				query.targetType("C");
				query.yearGreaterThanOrEqual(gradeInfo.getYear());
			} else {
				if (gradeYear > 0) {
					query.gradeYear(gradeYear);
					query.yearGreaterThanOrEqual(gradeYear);
					query.targetType("G");
				}
			}
			query.checkPersonGreaterThanOrEqual(3);
			if (StringUtils.isNotEmpty(type)) {
				query.type(type);
			}
			query.setOrderBy(" E.YEAR_ asc, E.MONTH_ asc ");

			String systemName = Environment.getCurrentSystemName();
			Collection<MedicalExaminationCount> rows = null;
			Database database = null;
			try {

				Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
				database = databaseService.getDatabaseByMapping("etl");
				if (database != null) {
					Environment.setCurrentSystemName(database.getName());
				}

				rows = medicalExaminationCountService.list(query);
				if (rows != null && !rows.isEmpty()) {

					String title = "";
					String xAxisTitle = "年度";
					String yAxisTitle = "";

					if (gradeInfo != null) {
						title = gradeInfo.getName();
					} else {
						title = String.valueOf(gradeYear) + "年级";
					}

					title = tenant.getName() + title + "逐年体检统计图";

					if (StringUtils.equals(index, "A")) {
						yAxisTitle = "体重正常人数";
					} else if (StringUtils.equals(index, "B")) {
						yAxisTitle = "身高正常人数";
					} else if (StringUtils.equals(index, "C")) {
						yAxisTitle = "体重低于2SD人数";
					} else if (StringUtils.equals(index, "D")) {
						yAxisTitle = "身高低于2SD人数";
					} else if (StringUtils.equals(index, "E")) {
						yAxisTitle = "消瘦人数";
					} else if (StringUtils.equals(index, "F")) {
						yAxisTitle = "超重人数";
					} else if (StringUtils.equals(index, "G")) {
						yAxisTitle = "肥胖人数";
					}

					request.setAttribute("chartTitle", title);
					request.setAttribute("xAxisTitle", xAxisTitle);
					request.setAttribute("yAxisTitle", yAxisTitle);

					JSONArray categories = new JSONArray();
					Set<String> exists = new HashSet<String>();
					for (MedicalExaminationCount cnt : rows) {
						if (cnt.getCheckPerson() > 0 && cnt.getYear() > 0
								&& (cnt.getMonth() > 0 && cnt.getMonth() <= 12)) {
							String category = cnt.getYear() + "年" + cnt.getMonth() + "月";
							if (!exists.contains(category)) {
								exists.add(category);
								categories.add(category);
							}
						}
					}

					request.setAttribute("categories", categories.toJSONString());
					logger.debug("categories:" + categories.toJSONString());

					JSONArray result = new JSONArray();
					JSONObject json0 = new JSONObject();
					JSONObject json1 = new JSONObject();
					json0.put("name", "应检人数");
					json1.put("name", "实检人数");

					JSONObject json2 = new JSONObject();
					if (StringUtils.equals(index, "A")) {
						json2.put("name", "体重正常人数");
					} else if (StringUtils.equals(index, "B")) {
						json2.put("name", "身高正常人数");
					} else if (StringUtils.equals(index, "C")) {
						json2.put("name", "体重低于2SD人数");
					} else if (StringUtils.equals(index, "D")) {
						json2.put("name", "身高低于2SD人数");
					} else if (StringUtils.equals(index, "E")) {
						json2.put("name", "消瘦人数");
					} else if (StringUtils.equals(index, "F")) {
						json2.put("name", "超重人数");
					} else if (StringUtils.equals(index, "G")) {
						json2.put("name", "肥胖人数");
					}

					exists.clear();

					JSONArray array0 = new JSONArray();
					JSONArray array1 = new JSONArray();
					JSONArray array2 = new JSONArray();

					for (MedicalExaminationCount cnt : rows) {
						if (cnt.getCheckPerson() > 0 && cnt.getYear() > 0
								&& (cnt.getMonth() > 0 && cnt.getMonth() <= 12)) {
							String category = cnt.getYear() + "年" + cnt.getMonth() + "月";
							if (!exists.contains(category)) {
								exists.add(category);
								array0.add(cnt.getTotalPerson());
								array1.add(cnt.getCheckPerson());

								if (StringUtils.equals(index, "A")) {
									array2.add(cnt.getMeanWeightNormal());
								} else if (StringUtils.equals(index, "B")) {
									array2.add(cnt.getMeanHeightNormal());
								} else if (StringUtils.equals(index, "C")) {
									array2.add(cnt.getMeanWeightLow());
								} else if (StringUtils.equals(index, "D")) {
									array2.add(cnt.getMeanHeightLow());
								} else if (StringUtils.equals(index, "E")) {
									array2.add(cnt.getMeanWeightSkinny());
								} else if (StringUtils.equals(index, "F")) {
									array2.add(cnt.getMeanOverWeight());
								} else if (StringUtils.equals(index, "G")) {
									array2.add(cnt.getMeanWeightObesity());
								}
							}
						}
					}

					if (StringUtils.equals(chartType, "bar")) {
						json0.put("data", array2);
						json1.put("data", array1);
						json2.put("data", array0);

						json0.put("name", json2.get("name"));
						json1.put("name", "实检人数");
						json2.put("name", "应检人数");
					} else {
						json0.put("data", array0);
						json1.put("data", array1);
						json2.put("data", array2);
					}

					result.add(json0);
					result.add(json1);
					result.add(json2);

					request.setAttribute("seriesData", result.toJSONString());
				}
			} catch (Exception ex) {
				logger.error(ex);
			} finally {
				com.glaf.core.config.Environment.setCurrentSystemName(systemName);
				if (rows != null) {
					rows.clear();
					rows = null;
				}
			}
		}

		return new ModelAndView("/heathcare/medicalExamination/gradeChart", modelMap);
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradePersonRelationService")
	public void setGradePersonRelationService(GradePersonRelationService gradePersonRelationService) {
		this.gradePersonRelationService = gradePersonRelationService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.growthStandardService")
	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalExaminationCountService")
	public void setMedicalExaminationCountService(MedicalExaminationCountService medicalExaminationCountService) {
		this.medicalExaminationCountService = medicalExaminationCountService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalExaminationEvaluateService")
	public void setMedicalExaminationEvaluateService(
			MedicalExaminationEvaluateService medicalExaminationEvaluateService) {
		this.medicalExaminationEvaluateService = medicalExaminationEvaluateService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalExaminationService")
	public void setMedicalExaminationService(MedicalExaminationService medicalExaminationService) {
		this.medicalExaminationService = medicalExaminationService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

}
