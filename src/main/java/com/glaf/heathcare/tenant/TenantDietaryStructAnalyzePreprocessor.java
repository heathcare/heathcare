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

package com.glaf.heathcare.tenant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;

import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.ActualRepastPerson;
import com.glaf.heathcare.domain.DietaryAnalyzeModel;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.domain.FoodDRIPercent;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.query.ActualRepastPersonQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.report.IReportPreprocessor;
import com.glaf.heathcare.service.ActualRepastPersonService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.service.FoodDRIService;
import com.glaf.heathcare.service.GoodsActualQuantityService;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

public class TenantDietaryStructAnalyzePreprocessor implements IReportPreprocessor {

	protected static final Log logger = LogFactory.getLog(TenantDietaryStructAnalyzePreprocessor.class);

	private static String getCellValue(CellValue cell) {
		String cellValue = null;
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			cellValue = String.valueOf(cell.getNumberValue());
		} else {
			cellValue = cell.getStringValue();
		}
		return cellValue;
	}

	protected double getValue(FormulaEvaluator evaluator, HSSFCell cell) {
		String strValue = null;
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			return cell.getNumericCellValue();
		} else if (cell.getCellTypeEnum() == CellType.STRING) {
			strValue = cell.getStringCellValue();
		} else if (cell.getCellTypeEnum() == CellType.FORMULA) {
			CellValue cellValue = evaluator.evaluate(cell);
			strValue = getCellValue(cellValue);
		}
		if (strValue != null) {
			return Double.parseDouble(strValue);
		}
		return 0;
	}

	@Override
	public void prepare(Tenant tenant, Map<String, Object> params) {
		Date startDate = ParamUtils.getDate(params, "startDate");
		Date endDate = ParamUtils.getDate(params, "endDate");
		if (startDate != null && endDate != null) {
			String tenantId = tenant.getTenantId();
			ActualRepastPersonService actualRepastPersonService = ContextFactory
					.getBean("com.glaf.heathcare.service.actualRepastPersonService");
			FoodCompositionService foodCompositionService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodCompositionService");
			GoodsActualQuantityService goodsActualQuantityService = ContextFactory
					.getBean("com.glaf.heathcare.service.goodsActualQuantityService");
			FoodDRIService foodDRIService = ContextFactory.getBean("com.glaf.heathcare.service.foodDRIService");
			FoodDRIPercentService foodDRIPercentService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodDRIPercentService");
			TenantConfigService tenantConfigService = ContextFactory.getBean("tenantConfigService");
			TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(tenant.getTenantId());
			long typeId = tenantConfig.getTypeId();

			DietaryAnalyzeModel std = new DietaryAnalyzeModel();
			FoodDRI foodDRI = foodDRIService.getFoodDRIByAge(4);
			FoodDRIPercent foodDRIPercent = foodDRIPercentService.getFoodDRIPercent("3-6", typeId);
			logger.debug("foodDRIPercent:" + foodDRIPercent);
			if (foodDRI != null && foodDRIPercent != null) {

				std.setHeatEnergy(foodDRI.getHeatEnergy());
				std.setProtein(foodDRI.getProtein());
				std.setFat(foodDRI.getFat());
				std.setCalcium(foodDRI.getCalcium());
				std.setIron(foodDRI.getIron());
				std.setZinc(foodDRI.getZinc());
				std.setVitaminA(foodDRI.getVitaminA());
				std.setVitaminB1(foodDRI.getVitaminB1());
				std.setVitaminB2(foodDRI.getVitaminB2());
				std.setVitaminC(foodDRI.getVitaminC());
				std.setNicotinicCid(foodDRI.getNicotinicCid());

				foodDRI.setCalcium(foodDRI.getCalcium() * foodDRIPercent.getCalcium());
				foodDRI.setCarbohydrate(foodDRI.getCarbohydrate() * foodDRIPercent.getCarbohydrate());
				foodDRI.setFat(foodDRI.getFat() * foodDRIPercent.getFat());
				foodDRI.setHeatEnergy(foodDRI.getHeatEnergy() * foodDRIPercent.getHeatEnergy());
				foodDRI.setIodine(foodDRI.getIodine() * foodDRIPercent.getIodine());
				foodDRI.setIron(foodDRI.getIron() * foodDRIPercent.getIron());
				foodDRI.setNicotinicCid(foodDRI.getNicotinicCid() * foodDRIPercent.getNicotinicCid());
				foodDRI.setPhosphorus(foodDRI.getPhosphorus() * foodDRIPercent.getPhosphorus());
				foodDRI.setProtein(foodDRI.getProtein() * foodDRIPercent.getProtein());
				foodDRI.setRetinol(foodDRI.getRetinol() * foodDRIPercent.getRetinol());
				foodDRI.setVitaminA(foodDRI.getVitaminA() * foodDRIPercent.getVitaminA());
				foodDRI.setVitaminB1(foodDRI.getVitaminB1() * foodDRIPercent.getVitaminB1());
				foodDRI.setVitaminB12(foodDRI.getVitaminB12() * foodDRIPercent.getVitaminB12());
				foodDRI.setVitaminB2(foodDRI.getVitaminB2() * foodDRIPercent.getVitaminB2());
				foodDRI.setVitaminB6(foodDRI.getVitaminB6() * foodDRIPercent.getVitaminB6());
				foodDRI.setVitaminC(foodDRI.getVitaminC() * foodDRIPercent.getVitaminC());
				foodDRI.setVitaminE(foodDRI.getVitaminE() * foodDRIPercent.getVitaminE());
				foodDRI.setZinc(foodDRI.getZinc() * foodDRIPercent.getZinc());

				params.put("std", std);
				params.put("foodDRI", foodDRI);
				params.put("foodDRIPercent", foodDRIPercent);
			}

			FoodCompositionQuery q = new FoodCompositionQuery();
			List<FoodComposition> foods = foodCompositionService.list(q);
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			if (foods != null && !foods.isEmpty()) {
				for (FoodComposition food : foods) {
					foodMap.put(food.getId(), food);
				}
			}

			ActualRepastPersonQuery q2 = new ActualRepastPersonQuery();
			q2.tenantId(tenantId);
			q2.fullDayGreaterThanOrEqual(DateUtils.getYearMonthDay(startDate));
			q2.fullDayLessThanOrEqual(DateUtils.getYearMonthDay(endDate));

			List<ActualRepastPerson> persons = actualRepastPersonService.list(q2);
			int totalPerson = 0;
			int person3 = 0;
			int person4 = 0;
			int person5 = 0;
			int person6 = 0;
			DietaryAnalyzeModel avgM = new DietaryAnalyzeModel();
			if (persons != null && !persons.isEmpty()) {
				for (ActualRepastPerson p : persons) {
					totalPerson = totalPerson + p.getMale();
					totalPerson = totalPerson + p.getFemale();
					switch (p.getAge()) {
					case 3:
						person3 = person3 + p.getMale() + p.getFemale();
						break;
					case 4:
						person4 = person4 + p.getMale() + p.getFemale();
						break;
					case 5:
						person5 = person5 + p.getMale() + p.getFemale();
						break;
					case 6:
						person6 = person6 + p.getMale() + p.getFemale();
						break;
					default:
						break;
					}
				}
				params.put("person3", person3);
				params.put("person4", person4);
				params.put("person5", person5);
				params.put("person6", person6);
				params.put("total", person3 + person4 + person5 + person6);

				ReportDefinition rdf = ReportContainer.getContainer()
						.getReportDefinition("TenantDietaryNutritionAvgQuantityV1");

				if (rdf != null) {
					byte[] data = rdf.getData();
					HSSFWorkbook workbook = null;
					FormulaEvaluator evaluator = null;
					InputStream is = null;
					ByteArrayInputStream bais = null;
					ByteArrayOutputStream baos = null;
					BufferedOutputStream bos = null;
					try {

						bais = new ByteArrayInputStream(data);
						is = new BufferedInputStream(bais);
						baos = new ByteArrayOutputStream();
						bos = new BufferedOutputStream(baos);

						Context context2 = PoiTransformer.createInitialContext();

						Set<Entry<String, Object>> entrySet = params.entrySet();
						for (Entry<String, Object> entry : entrySet) {
							String key = entry.getKey();
							Object value = entry.getValue();
							context2.putVar(key, value);
						}

						org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);

						bos.flush();
						baos.flush();
						data = baos.toByteArray();

						IOUtils.closeQuietly(is);
						IOUtils.closeQuietly(bais);

						bais = new ByteArrayInputStream(data);
						is = new BufferedInputStream(bais);

						workbook = new HSSFWorkbook(is);
						evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						evaluator.evaluateAll();
						HSSFSheet sheet = workbook.getSheetAt(0);
						HSSFRow row = sheet.getRow(17);
						avgM.setHeatEnergy(getValue(evaluator, row.getCell(3)) * foodDRIPercent.getHeatEnergy());
						avgM.setProtein(getValue(evaluator, row.getCell(5)) * foodDRIPercent.getProtein());
						avgM.setNicotinicCid(getValue(evaluator, row.getCell(15)) * foodDRIPercent.getNicotinicCid());
						avgM.setCalcium(getValue(evaluator, row.getCell(7)) * foodDRIPercent.getCalcium());
						avgM.setZinc(getValue(evaluator, row.getCell(9)) * foodDRIPercent.getZinc());
						avgM.setVitaminB1(getValue(evaluator, row.getCell(13)) * foodDRIPercent.getVitaminB1());
						avgM.setVitaminC(getValue(evaluator, row.getCell(17)) * foodDRIPercent.getVitaminC());

						params.put("avg", avgM);

						IOUtils.closeQuietly(is);
						IOUtils.closeQuietly(bais);

					} catch (Exception ex) {
						throw new RuntimeException(ex);
					} finally {
						data = null;
						IOUtils.closeQuietly(is);
						IOUtils.closeQuietly(bais);
						IOUtils.closeQuietly(baos);
						IOUtils.closeQuietly(bos);
						evaluator.clearAllCachedResultValues();
						evaluator = null;
						if (workbook != null) {
							try {
								workbook.close();
							} catch (IOException e) {
							}
							workbook = null;
						}
					}
				}
			}

			GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
			query.tenantId(tenantId);
			query.businessStatus(9);
			query.usageTimeGreaterThanOrEqual(startDate);
			query.usageTimeLessThanOrEqual(endDate);
			query.setOrderBy("  E.GOODSNODEID_ asc, E.GOODSID_ asc ");

			List<GoodsActualQuantity> rows = goodsActualQuantityService.list(query);
			if (rows != null && !rows.isEmpty() && totalPerson > 0) {
				for (GoodsActualQuantity m : rows) {
					FoodComposition food = foodMap.get(m.getGoodsId());
					if (food == null) {
						continue;
					}
				}

				int days = 1;// 已经折算人数了，故系数为1
				double avg = 0.0;
				double heatEnergy = 0.0;
				double fat = 0.0;
				double protein = 0.0;
				double carbohydrate = 0.0;
				double vitaminA = 0.0;
				double vitaminB1 = 0.0;
				double vitaminB2 = 0.0;
				double vitaminC = 0.0;
				double nicotinicCid = 0.0;
				double calcium = 0.0;
				double iron = 0.0;
				double zinc = 0.0;
				double animalBeansProtein = 0.0;
				double animalProtein = 0.0;
				double plantProtein = 0.0;
				double animalFat = 0.0;
				double plantFat = 0.0;

				for (GoodsActualQuantity m : rows) {
					FoodComposition food = foodMap.get(m.getGoodsId());
					if (food == null) {
						continue;
					}
					avg = m.getQuantity() * 1000 / (days * totalPerson);// 转成均量
					avg = avg / 100D;// 转成100克标准
					// heatEnergy = heatEnergy + food.getHeatEnergy() * avg;

					fat = fat + food.getFat() * avg;
					protein = protein + food.getProtein() * avg;
					carbohydrate = carbohydrate + food.getCarbohydrate() * avg;
					vitaminA = vitaminA + food.getVitaminA() * avg;
					vitaminB1 = vitaminB1 + food.getVitaminB1() * avg;
					vitaminB2 = vitaminB2 + food.getVitaminB2() * avg;
					vitaminC = vitaminC + food.getVitaminC() * avg;
					nicotinicCid = nicotinicCid + food.getNicotinicCid() * avg;
					calcium = calcium + food.getCalcium() * avg;
					iron = iron + food.getIron() * avg;
					zinc = zinc + food.getZinc() * avg;

					switch ((int) food.getNodeId()) {
					case 4402:// 谷类及制品
						plantProtein = plantProtein + food.getProtein() * avg;
						plantFat = plantFat + food.getFat() * avg;
						break;
					case 4404:// 豆类及制品
						animalBeansProtein = animalBeansProtein + food.getProtein() * avg;
						plantFat = plantFat + food.getFat() * avg;
						break;
					case 4405:// 蔬菜
						plantProtein = plantProtein + food.getProtein() * avg;
						plantFat = plantFat + food.getFat() * avg;
						break;
					case 4407:// 水果
						plantProtein = plantProtein + food.getProtein() * avg;
						plantFat = plantFat + food.getFat() * avg;
						break;
					case 4411:// 乳类
						animalBeansProtein = animalBeansProtein + food.getProtein() * avg;
						animalProtein = animalProtein + food.getProtein() * avg;
						animalFat = animalFat + food.getFat() * avg;
						break;
					case 4412:// 蛋类
						animalBeansProtein = animalBeansProtein + food.getProtein() * avg;
						animalProtein = animalProtein + food.getProtein() * avg;
						animalFat = animalFat + food.getFat() * avg;
						break;
					case 4409:// 畜肉类及制品
					case 4410:// 禽肉类及制品
						animalBeansProtein = animalBeansProtein + food.getProtein() * avg;
						animalProtein = animalProtein + food.getProtein() * avg;
						animalFat = animalFat + food.getFat() * avg;
						break;
					case 4413:// 鱼虾蟹贝类及制品
						animalBeansProtein = animalBeansProtein + food.getProtein() * avg;
						animalProtein = animalProtein + food.getProtein() * avg;
						animalFat = animalFat + food.getFat() * avg;
						break;
					case 4418:// 油脂类
						break;
					case 4417:// 糖
						break;
					default:
						break;
					}
				}

				heatEnergy = carbohydrate * 4 + fat * 9 + protein * 4;
				DietaryAnalyzeModel real = new DietaryAnalyzeModel();
				real.setHeatEnergy(heatEnergy);
				real.setCarbohydrate(carbohydrate);
				real.setCarbohydrateHE(carbohydrate * 4);
				real.setFat(fat);
				real.setFatHE(fat * 9);
				real.setProtein(protein);
				real.setProteinHE(protein * 4);
				real.setCalcium(calcium);
				real.setIron(iron);
				real.setZinc(zinc);
				real.setVitaminA(vitaminA);
				real.setVitaminB1(vitaminB1);
				real.setVitaminB2(vitaminB2);
				real.setVitaminC(vitaminC);
				real.setNicotinicCid(nicotinicCid);
				real.setAnimalBeansProtein(animalBeansProtein);
				real.setAnimalProtein(animalProtein);
				real.setPlantProtein(protein - animalProtein);
				real.setAnimalFat(animalFat);
				real.setPlantFat(fat - plantFat);
				params.put("real", real);

				DietaryAnalyzeModel realPercent = new DietaryAnalyzeModel();
				DietaryAnalyzeModel stdPercent = new DietaryAnalyzeModel();
				DietaryAnalyzeModel evaluate = new DietaryAnalyzeModel();

				if (animalBeansProtein > 0) {
					realPercent.setAnimalBeansProtein(Math.round(animalBeansProtein / protein * 10000D) / 100D);
					stdPercent.setAnimalBeansProteinString("50%以上");
				}

				if (animalProtein > 0) {
					realPercent.setAnimalProtein(Math.round(animalProtein / protein * 10000D) / 100D);
					stdPercent.setAnimalProteinString("50%以上");
				}

				if (real.getPlantProtein() > 0) {
					realPercent.setPlantProtein(Math.round(real.getPlantProtein() / protein * 10000D) / 100D);
				}

				if (animalFat > 0) {
					realPercent.setAnimalFat(Math.round(animalFat / fat * 10000D) / 100D);
					stdPercent.setAnimalFatString("50%±3%");
				}

				if (plantFat > 0) {
					realPercent.setPlantFat(100 - Math.round(animalFat / fat * 10000D) / 100D);
					stdPercent.setPlantFatString("50%以下");
				}

				if (avgM.getHeatEnergy() > 0 && heatEnergy > 0) {
					realPercent.setHeatEnergy(heatEnergy);
					realPercent.setHeatEnergyPercent(Math.round(heatEnergy / avgM.getHeatEnergy() * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getHeatEnergyPercent() >= 120) {
							evaluate.setHeatEnergyString("偏高");
						} else if (realPercent.getHeatEnergyPercent() >= 80) {
							evaluate.setHeatEnergyString("合理");
						} else {
							evaluate.setHeatEnergyString("偏低");
						}
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getHeatEnergyPercent() >= 100) {
							evaluate.setHeatEnergyString("偏高");
						} else if (realPercent.getHeatEnergyPercent() >= 67) {
							evaluate.setHeatEnergyString("合理");
						} else {
							evaluate.setHeatEnergyString("偏低");
						}
						break;
					case 3321:// 一餐二点
						if (realPercent.getHeatEnergyPercent() >= 80) {
							evaluate.setHeatEnergyString("偏高");
						} else if (realPercent.getHeatEnergyPercent() >= 47) {
							evaluate.setHeatEnergyString("合理");
						} else {
							evaluate.setHeatEnergyString("偏低");
						}
						break;
					default:
						break;
					}
				}

				if (protein > 0) {
					stdPercent.setProteinHEString("12%±15%");
					realPercent.setProteinHE(Math.round(protein * 4 / heatEnergy * 10000D) / 100D);
					if (realPercent.getProteinHE() > 25) {
						evaluate.setProteinHEString("严重过剩");
					} else if (realPercent.getProteinHE() > 15) {
						evaluate.setProteinHEString("过剩");
					} else if (realPercent.getProteinHE() <= 15 && realPercent.getProteinHE() >= 12) {
						evaluate.setProteinHEString("合理");
					} else {
						evaluate.setProteinHEString("严重不足");
					}
				}

				if (fat > 0) {
					stdPercent.setFatHEString("30%±35%");
					realPercent.setFatHE(Math.round(fat * 9 / heatEnergy * 10000D) / 100D);
					if (realPercent.getFatHE() > 45) {
						evaluate.setFatHEString("严重过剩");
					} else if (realPercent.getFatHE() > 35) {
						evaluate.setFatHEString("过剩");
					} else if (realPercent.getFatHE() <= 35 && realPercent.getFatHE() >= 30) {
						evaluate.setFatHEString("合理");
					} else {
						evaluate.setFatHEString("偏低");
					}
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						break;
					case 3321:// 一餐二点
						break;
					default:
						break;
					}
				}

				if (carbohydrate > 0) {
					stdPercent.setCarbohydrateHEString("50%±60%");
					realPercent.setCarbohydrateHE(Math.round(carbohydrate * 4 / heatEnergy * 10000D) / 100D);
					if (realPercent.getCarbohydrateHE() > 85) {
						evaluate.setCarbohydrateHEString("严重过剩");
					} else if (realPercent.getCarbohydrateHE() > 60) {
						evaluate.setCarbohydrateHEString("过剩");
					} else if (realPercent.getCarbohydrateHE() <= 60 && realPercent.getCarbohydrateHE() >= 50) {
						evaluate.setCarbohydrateHEString("合理");
					} else {
						evaluate.setCarbohydrateHEString("偏低");
					}
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						break;
					case 3321:// 一餐二点
						break;
					default:
						break;
					}
				}

				double proteinStd = avgM.getProtein();
				if (proteinStd == 0) {
					proteinStd = std.getProtein();
				}

				if (proteinStd > 0 && protein > 0) {
					realPercent.setProtein(Math.round(protein / proteinStd * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getProtein() >= 120) {
							evaluate.setProteinString("偏高");
						} else if (realPercent.getProtein() >= 80) {
							evaluate.setProteinString("合理");
						} else {
							evaluate.setProteinString("严重不足");
						}
						stdPercent.setProteinString("80%以上");
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getProtein() >= 100) {
							evaluate.setProteinString("偏高");
						} else if (realPercent.getProtein() >= 62) {
							evaluate.setProteinString("合理");
						} else {
							evaluate.setProteinString("严重不足");
						}
						stdPercent.setProteinString("65%±3%");
						break;
					case 3321:// 一餐二点
						if (realPercent.getProtein() >= 80) {
							evaluate.setProteinString("偏高");
						} else if (realPercent.getProtein() >= 42) {
							evaluate.setProteinString("合理");
						} else {
							evaluate.setProteinString("严重不足");
						}
						stdPercent.setProteinString("45%±3%");
						break;
					default:
						break;
					}
				}

				double calciumStd = avgM.getCalcium();
				if (calciumStd == 0) {
					calciumStd = std.getCalcium();
				}

				if (calciumStd > 0 && calcium > 0) {
					realPercent.setCalcium(Math.round(calcium / calciumStd * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getCalcium() >= 150) {
							evaluate.setCalciumString("偏高");
						} else if (realPercent.getCalcium() >= 100) {
							evaluate.setCalciumString("合理");
						} else {
							evaluate.setCalciumString("严重不足");
						}
						stdPercent.setCalciumString("80%以上");
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getCalcium() >= 150) {
							evaluate.setCalciumString("偏高");
						} else if (realPercent.getCalcium() >= 100) {
							evaluate.setCalciumString("合理");
						} else {
							evaluate.setCalciumString("严重不足");
						}
						stdPercent.setCalciumString("65%±3%");
						break;
					case 3321:// 一餐二点
						if (realPercent.getCalcium() >= 150) {
							evaluate.setCalciumString("偏高");
						} else if (realPercent.getCalcium() >= 100) {
							evaluate.setCalciumString("合理");
						} else {
							evaluate.setCalciumString("严重不足");
						}
						stdPercent.setCalciumString("45%±3%");
						break;
					default:
						break;
					}
				}

				double ironStd = avgM.getIron();
				if (ironStd == 0) {
					ironStd = std.getIron();
				}

				if (ironStd > 0 && iron > 0) {
					realPercent.setIron(Math.round(iron / ironStd * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getIron() >= 120) {
							evaluate.setIronString("偏高");
						} else if (realPercent.getIron() >= 80) {
							evaluate.setIronString("合理");
						} else {
							evaluate.setIronString("严重不足");
						}
						stdPercent.setIronString("80%以上");
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getIron() >= 100) {
							evaluate.setIronString("偏高");
						} else if (realPercent.getIron() >= 57) {
							evaluate.setIronString("合理");
						} else {
							evaluate.setIronString("严重不足");
						}
						stdPercent.setIronString("65%±3%");
						break;
					case 3321:// 一餐二点
						if (realPercent.getIron() >= 80) {
							evaluate.setIronString("偏高");
						} else if (realPercent.getIron() >= 39) {
							evaluate.setIronString("合理");
						} else {
							evaluate.setIronString("严重不足");
						}
						stdPercent.setIronString("45%±3%");
						break;
					default:
						break;
					}
				}

				double zincStd = avgM.getZinc();
				if (zincStd == 0) {
					zincStd = std.getZinc();
				}

				if (zincStd > 0 && zinc > 0) {
					realPercent.setZinc(Math.round(zinc / zincStd * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getZinc() >= 120) {
							evaluate.setZincString("偏高");
						} else if (realPercent.getZinc() >= 80) {
							evaluate.setZincString("合理");
						} else {
							evaluate.setZincString("严重不足");
						}
						stdPercent.setZincString("80%以上");
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getZinc() >= 100) {
							evaluate.setZincString("偏高");
						} else if (realPercent.getZinc() >= 57) {
							evaluate.setZincString("合理");
						} else {
							evaluate.setZincString("严重不足");
						}
						stdPercent.setZincString("65%±3%");
						break;
					case 3321:// 一餐二点
						if (realPercent.getZinc() >= 80) {
							evaluate.setZincString("偏高");
						} else if (realPercent.getZinc() >= 39) {
							evaluate.setZincString("合理");
						} else {
							evaluate.setZincString("严重不足");
						}
						stdPercent.setZincString("45%±3%");
						break;
					default:
						break;
					}
				}

				double nicotinicCidStd = avgM.getNicotinicCid();
				if (nicotinicCidStd == 0) {
					nicotinicCidStd = std.getNicotinicCid();
				}

				if (nicotinicCidStd > 0 && nicotinicCid > 0) {
					realPercent.setNicotinicCid(Math.round(nicotinicCid / nicotinicCidStd * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getNicotinicCid() >= 120) {
							evaluate.setNicotinicCidString("偏高");
						} else if (realPercent.getNicotinicCid() >= 80) {
							evaluate.setNicotinicCidString("合理");
						} else {
							evaluate.setNicotinicCidString("严重不足");
						}
						stdPercent.setNicotinicCidString("80%以上");
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getNicotinicCid() >= 100) {
							evaluate.setNicotinicCidString("偏高");
						} else if (realPercent.getNicotinicCid() >= 57) {
							evaluate.setNicotinicCidString("合理");
						} else {
							evaluate.setNicotinicCidString("严重不足");
						}
						stdPercent.setNicotinicCidString("65%±3%");
						break;
					case 3321:// 一餐二点
						if (realPercent.getNicotinicCid() >= 80) {
							evaluate.setNicotinicCidString("偏高");
						} else if (realPercent.getNicotinicCid() >= 39) {
							evaluate.setNicotinicCidString("合理");
						} else {
							evaluate.setNicotinicCidString("严重不足");
						}
						stdPercent.setNicotinicCidString("45%±3%");
						break;
					default:
						break;
					}
				}

				double vitaminB1Std = avgM.getVitaminB1();
				if (vitaminB1Std == 0) {
					vitaminB1Std = std.getVitaminB1();
				}
				if (vitaminB1Std > 0 && vitaminB1 > 0) {
					realPercent.setVitaminB1(Math.round(vitaminB1 / vitaminB1Std * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getVitaminB1() >= 120) {
							evaluate.setVitaminB1String("偏高");
						} else if (realPercent.getVitaminB1() >= 80) {
							evaluate.setVitaminB1String("合理");
						} else {
							evaluate.setVitaminB1String("严重不足");
						}
						stdPercent.setVitaminB1String("80%以上");
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getVitaminB1() >= 100) {
							evaluate.setVitaminB1String("偏高");
						} else if (realPercent.getVitaminB1() >= 57) {
							evaluate.setVitaminB1String("合理");
						} else {
							evaluate.setVitaminB1String("严重不足");
						}
						stdPercent.setVitaminB1String("65%±3%");
						break;
					case 3321:// 一餐二点
						if (realPercent.getVitaminB1() >= 80) {
							evaluate.setVitaminB1String("偏高");
						} else if (realPercent.getVitaminB1() >= 39) {
							evaluate.setVitaminB1String("合理");
						} else {
							evaluate.setVitaminB1String("严重不足");
						}
						stdPercent.setVitaminB1String("45%±3%");
						break;
					default:
						break;
					}
				}

				double vitaminB2Std = avgM.getVitaminB2();
				if (vitaminB2Std == 0) {
					vitaminB2Std = std.getVitaminB2();
				}
				if (vitaminB2Std > 0 && vitaminB2 > 0) {
					realPercent.setVitaminB2(Math.round(vitaminB2 / vitaminB2Std * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getVitaminB2() >= 120) {
							evaluate.setVitaminB2String("偏高");
						} else if (realPercent.getVitaminB2() >= 80) {
							evaluate.setVitaminB2String("合理");
						} else {
							evaluate.setVitaminB2String("严重不足");
						}
						stdPercent.setVitaminB2String("80%以上");
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getVitaminB2() >= 100) {
							evaluate.setVitaminB2String("偏高");
						} else if (realPercent.getVitaminB2() >= 57) {
							evaluate.setVitaminB2String("合理");
						} else {
							evaluate.setVitaminB2String("严重不足");
						}
						stdPercent.setVitaminB2String("65%±3%");
						break;
					case 3321:// 一餐二点
						if (realPercent.getVitaminB2() >= 80) {
							evaluate.setVitaminB2String("偏高");
						} else if (realPercent.getVitaminB2() >= 39) {
							evaluate.setVitaminB2String("合理");
						} else {
							evaluate.setVitaminB2String("严重不足");
						}
						stdPercent.setVitaminB2String("45%±3%");
						break;
					default:
						break;
					}
				}

				double vitaminCStd = avgM.getVitaminC();
				if (vitaminCStd == 0) {
					vitaminCStd = std.getVitaminC();
				}
				if (vitaminCStd > 0 && vitaminC > 0) {
					realPercent.setVitaminC(Math.round(vitaminC / vitaminCStd * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getVitaminC() >= 120) {
							evaluate.setVitaminCString("偏高");
						} else if (realPercent.getVitaminC() >= 80) {
							evaluate.setVitaminCString("合理");
						} else {
							evaluate.setVitaminCString("严重不足");
						}
						stdPercent.setVitaminCString("80%以上");
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getVitaminC() >= 100) {
							evaluate.setVitaminCString("偏高");
						} else if (realPercent.getVitaminC() >= 57) {
							evaluate.setVitaminCString("合理");
						} else {
							evaluate.setVitaminCString("严重不足");
						}
						stdPercent.setVitaminCString("65%±3%");
						break;
					case 3321:// 一餐二点
						if (realPercent.getVitaminC() >= 80) {
							evaluate.setVitaminCString("偏高");
						} else if (realPercent.getVitaminC() >= 39) {
							evaluate.setVitaminCString("合理");
						} else {
							evaluate.setVitaminCString("严重不足");
						}
						stdPercent.setVitaminCString("45%±3%");
						break;
					default:
						break;
					}
				}

				double vitaminAStd = avgM.getVitaminA();
				if (vitaminAStd == 0) {
					vitaminAStd = std.getVitaminA();
				}
				if (vitaminAStd > 0 && vitaminA > 0) {
					realPercent.setVitaminA(Math.round(vitaminA / vitaminAStd * 10000D) / 100D);
					switch ((int) typeId) {
					case 3301:// 三餐一点
					case 3401:// 三餐两点
						if (realPercent.getVitaminA() >= 120) {
							evaluate.setVitaminAString("偏高");
						} else if (realPercent.getVitaminA() >= 80) {
							evaluate.setVitaminAString("合理");
						} else {
							evaluate.setVitaminAString("严重不足");
						}
						stdPercent.setVitaminAString("80%以上");
						break;
					case 3311:// 二餐一点
					case 3411:// 二餐二点
						if (realPercent.getVitaminA() >= 100) {
							evaluate.setVitaminAString("偏高");
						} else if (realPercent.getVitaminA() >= 57) {
							evaluate.setVitaminAString("合理");
						} else {
							evaluate.setVitaminAString("严重不足");
						}
						stdPercent.setVitaminAString("65%±3%");
						break;
					case 3321:// 一餐二点
						if (realPercent.getVitaminA() >= 80) {
							evaluate.setVitaminAString("偏高");
						} else if (realPercent.getVitaminA() >= 39) {
							evaluate.setVitaminAString("合理");
						} else {
							evaluate.setVitaminAString("严重不足");
						}
						stdPercent.setVitaminAString("45%±3%");
						break;
					default:
						break;
					}
				}

				params.put("evaluate", evaluate);
				params.put("stdPercent", stdPercent);
				params.put("realPercent", realPercent);
			}
		}
	}

}
