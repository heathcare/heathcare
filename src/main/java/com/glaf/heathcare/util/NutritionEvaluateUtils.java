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
package com.glaf.heathcare.util;

import com.glaf.heathcare.domain.DietaryCount;
import com.glaf.heathcare.domain.DietaryDayRptModel;
import com.glaf.heathcare.domain.DietaryTemplateCount;
import com.glaf.heathcare.domain.DietaryWeeklyRptModel;
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.domain.FoodDRIPercent;

public class NutritionEvaluateUtils {

	public static void evaluate(DietaryDayRptModel m, DietaryCount c, FoodDRI foodDRI, FoodDRIPercent foodDRIPercent) {
		m.setCalcium(c.getCalcium());
		m.setHeatEnergy(c.getHeatEnergy());
		m.setProtein(c.getProtein());
		if (foodDRI != null && foodDRIPercent != null) {

			double d2 = c.getHeatEnergy() / foodDRI.getHeatEnergy();
			if (d2 > foodDRIPercent.getHeatEnergy()) {
				m.setHeatEnergyEvaluate("OK!");
			} else {
				m.setHeatEnergyEvaluate("<span class='red'>低!</span>");
			}

			double d3 = c.getProtein() / foodDRI.getProtein();
			if (d3 > foodDRIPercent.getProtein()) {
				m.setProteinEvaluate("OK!");
			} else {
				m.setProteinEvaluate("<span class='red'>少!</span>");
			}

			double d4 = c.getFat() / foodDRI.getFat();
			if (d4 > foodDRIPercent.getFat()) {
				m.setFatEvaluate("OK!");
			} else {
				m.setFatEvaluate("<span class='red'>少!</span>");
			}

			double d5 = c.getCarbohydrate() / foodDRI.getCarbohydrate();
			if (d5 - foodDRIPercent.getCarbohydrate() > 0.2) {
				m.setCarbohydrateEvaluate("多!");
			} else if (d5 > foodDRIPercent.getCarbohydrate()) {
				m.setCarbohydrateEvaluate("OK!");
			} else {
				m.setCarbohydrateEvaluate("<span class='red'>少!</span>");
			}

			double d6 = c.getVitaminC() / foodDRI.getVitaminC();
			if (d6 > foodDRIPercent.getVitaminC()) {
				m.setVitaminCEvaluate("OK!");
			} else {
				m.setVitaminCEvaluate("<span class='red'>少!</span>");
			}

			double d7 = c.getCalcium() / foodDRI.getCalcium();
			if (d7 > foodDRIPercent.getCalcium()) {
				m.setCalciumEvaluate("OK!");
			} else {
				m.setCalciumEvaluate("<span class='red'>少!</span>");
			}

			double d8 = c.getIron() / foodDRI.getIron();
			if (d8 > foodDRIPercent.getIron()) {
				m.setIronEvaluate("OK!");
			} else {
				m.setIronEvaluate("<span class='red'>少!</span>");
			}

			double d9 = c.getZinc() / foodDRI.getZinc();
			if (d9 > foodDRIPercent.getZinc()) {
				m.setZincEvaluate("OK!");
			} else {
				m.setZincEvaluate("<span class='red'>少!</span>");
			}

		}
	}

	public static void evaluate(DietaryDayRptModel m, DietaryTemplateCount c, FoodDRI foodDRI,
			FoodDRIPercent foodDRIPercent) {
		m.setCalcium(c.getCalcium());
		m.setHeatEnergy(c.getHeatEnergy());
		m.setProtein(c.getProtein());
		if (foodDRI != null && foodDRIPercent != null) {

			double d2 = c.getHeatEnergy() / foodDRI.getHeatEnergy();
			if (d2 > foodDRIPercent.getHeatEnergy()) {
				m.setHeatEnergyEvaluate("OK!");
			} else {
				m.setHeatEnergyEvaluate("<span class='red'>低!</span>");
			}

			double d3 = c.getProtein() / foodDRI.getProtein();
			if (d3 > foodDRIPercent.getProtein()) {
				m.setProteinEvaluate("OK!");
			} else {
				m.setProteinEvaluate("<span class='red'>少!</span>");
			}

			double d4 = c.getFat() / foodDRI.getFat();
			if (d4 > foodDRIPercent.getFat()) {
				m.setFatEvaluate("OK!");
			} else {
				m.setFatEvaluate("<span class='red'>少!</span>");
			}

			double d5 = c.getCarbohydrate() / foodDRI.getCarbohydrate();
			if (d5 - foodDRIPercent.getCarbohydrate() > 0.2) {
				m.setCarbohydrateEvaluate("多!");
			} else if (d5 > foodDRIPercent.getCarbohydrate()) {
				m.setCarbohydrateEvaluate("OK!");
			} else {
				m.setCarbohydrateEvaluate("<span class='red'>少!</span>");
			}

			double d6 = c.getVitaminC() / foodDRI.getVitaminC();
			if (d6 > foodDRIPercent.getVitaminC()) {
				m.setVitaminCEvaluate("OK!");
			} else {
				m.setVitaminCEvaluate("<span class='red'>少!</span>");
			}

			double d7 = c.getCalcium() / foodDRI.getCalcium();
			if (d7 > foodDRIPercent.getCalcium()) {
				m.setCalciumEvaluate("OK!");
			} else {
				m.setCalciumEvaluate("<span class='red'>少!</span>");
			}

			double d8 = c.getIron() / foodDRI.getIron();
			if (d8 > foodDRIPercent.getIron()) {
				m.setIronEvaluate("OK!");
			} else {
				m.setIronEvaluate("<span class='red'>少!</span>");
			}

			double d9 = c.getZinc() / foodDRI.getZinc();
			if (d9 > foodDRIPercent.getZinc()) {
				m.setZincEvaluate("OK!");
			} else {
				m.setZincEvaluate("<span class='red'>少!</span>");
			}

		}
	}

	public static void evaluate(DietaryWeeklyRptModel m, FoodDRI foodDRI, FoodDRIPercent foodDRIPercent) {
		if (foodDRI != null && foodDRIPercent != null) {
			double d1 = m.getCalcium() / foodDRI.getCalcium();
			m.setCalciumPercent(Math.round(d1 * 100));
			if (d1 > foodDRIPercent.getCalcium()) {
				m.setCalciumEvaluate("OK!");
			} else {
				m.setCalciumEvaluate("<span class='red'>少!</span>");
			}

			double d2 = m.getHeatEnergy() / foodDRI.getHeatEnergy();
			m.setHeatEnergyPercent(Math.round(d2 * 100));
			if (d2 > foodDRIPercent.getHeatEnergy()) {
				m.setHeatEnergyEvaluate("OK!");
			} else {
				m.setHeatEnergyEvaluate("<span class='red'>低!<span>");
			}

			if (m.getHeatEnergy() > 0) {
				double d22 = m.getHeatEnergyCarbohydrate() / m.getHeatEnergy();
				m.setHeatEnergyCarbohydratePercent(Math.round(d22 * 100));
				if (d22 >= 0.65) {
					m.setHeatEnergyCarbohydrateEvaluate("<span class='blue'>高!<span>");
				} else if (d22 < 0.5) {
					m.setHeatEnergyCarbohydrateEvaluate("<span class='red'>低!<span>");
				} else {
					m.setHeatEnergyCarbohydrateEvaluate("OK!");
				}

				d22 = m.getHeatEnergyFat() / m.getHeatEnergy();
				m.setHeatEnergyFatPercent(Math.round(d22 * 100));
				if (d22 >= 0.35) {
					m.setHeatEnergyFatEvaluate("<span class='blue'>高!<span>");
				} else if (d22 < 0.2) {
					m.setHeatEnergyFatEvaluate("<span class='red'>低!<span>");
				} else {
					m.setHeatEnergyFatEvaluate("OK!");
				}
			}

			double d3 = m.getProtein() / foodDRI.getProtein();
			m.setProteinPercent(Math.round(d3 * 100));
			if (d3 > foodDRIPercent.getProtein()) {
				m.setProteinEvaluate("OK!");
			} else {
				m.setProteinEvaluate("<span class='red'>少!</span>");
			}

			if (m.getProtein() > 0) {
				double d32 = m.getProteinAnimal() / m.getProtein();
				m.setProteinAnimalPercent(Math.round(d32 * 100));
				if (d32 >= 0.3) {
					m.setProteinAnimalEvaluate("OK!");
				} else {
					m.setProteinAnimalEvaluate("<span class='red'>少!</span>");
				}

				d32 = m.getProteinAnimalBeans() / m.getProtein();
				m.setProteinAnimalBeansPercent(Math.round(d32 * 100));
				if (d32 >= 0.5) {
					m.setProteinAnimalBeansEvaluate("OK!");
				} else {
					m.setProteinAnimalBeansEvaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getVitaminA() > 0) {
				double d4 = m.getVitaminA() / foodDRI.getVitaminA();
				m.setVitaminAPercent(Math.round(d4 * 100));
				if (d4 > foodDRIPercent.getVitaminA()) {
					m.setVitaminAEvaluate("OK!");
				} else {
					m.setVitaminAEvaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getVitaminB1() > 0) {
				double d5 = m.getVitaminB1() / foodDRI.getVitaminB1();
				m.setVitaminB1Percent(Math.round(d5 * 100));
				if (d5 > foodDRIPercent.getVitaminB1()) {
					m.setVitaminB1Evaluate("OK!");
				} else {
					m.setVitaminB1Evaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getVitaminB2() > 0) {
				double d5 = m.getVitaminB2() / foodDRI.getVitaminB2();
				m.setVitaminB2Percent(Math.round(d5 * 100));
				if (d5 > foodDRIPercent.getVitaminB2()) {
					m.setVitaminB2Evaluate("OK!");
				} else {
					m.setVitaminB2Evaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getVitaminC() > 0) {
				double d5 = m.getVitaminC() / foodDRI.getVitaminC();
				m.setVitaminCPercent(Math.round(d5 * 100));
				if (d5 > foodDRIPercent.getVitaminC()) {
					m.setVitaminCEvaluate("OK!");
				} else {
					m.setVitaminCEvaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getVitaminE() > 0) {
				double d5 = m.getVitaminE() / foodDRI.getVitaminE();
				m.setVitaminEPercent(Math.round(d5 * 100));
				if (d5 > foodDRIPercent.getVitaminE()) {
					m.setVitaminEEvaluate("OK!");
				} else {
					m.setVitaminEEvaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getCalcium() > 0) {
				double d5 = m.getCalcium() / foodDRI.getCalcium();
				m.setCalciumPercent(Math.round(d5 * 100));
				if (d5 > foodDRIPercent.getCalcium()) {
					m.setCalciumEvaluate("OK!");
				} else {
					m.setCalciumEvaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getIron() > 0) {
				double d5 = m.getIron() / foodDRI.getIron();
				m.setIronPercent(Math.round(d5 * 100));
				if (d5 > foodDRIPercent.getIron()) {
					m.setIronEvaluate("OK!");
				} else {
					m.setIronEvaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getZinc() > 0) {
				double d5 = m.getZinc() / foodDRI.getZinc();
				m.setZincPercent(Math.round(d5 * 100));
				if (d5 > foodDRIPercent.getZinc()) {
					m.setZincEvaluate("OK!");
				} else {
					m.setZincEvaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getPhosphorus() > 0) {
				double d5 = m.getPhosphorus() / foodDRI.getPhosphorus();
				m.setPhosphorusPercent(Math.round(d5 * 100));
				if (d5 > foodDRIPercent.getPhosphorus()) {
					if (d5 > 3.0) {
						m.setPhosphorusEvaluate("高!");
					} else {
						m.setPhosphorusEvaluate("OK!");
					}
				} else {
					m.setPhosphorusEvaluate("<span class='red'>少!</span>");
				}
			}

			if (foodDRI.getNicotinicCid() > 0) {
				double d5 = m.getNicotinicCid() / foodDRI.getNicotinicCid();
				m.setNicotinicCidPercent(Math.round(d5 * 100));
				if (d5 > foodDRIPercent.getNicotinicCid()) {
					m.setNicotinicCidEvaluate("OK!");
				} else {
					m.setNicotinicCidEvaluate("<span class='red'>少!</span>");
				}
			}
		}
	}

	private NutritionEvaluateUtils() {

	}

}
