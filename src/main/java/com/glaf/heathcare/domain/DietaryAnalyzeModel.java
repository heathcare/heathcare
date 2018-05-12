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

public class DietaryAnalyzeModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 热能
	 */
	protected double heatEnergy;

	protected double heatEnergyPercent;

	/**
	 * 蛋白质
	 */
	protected double protein;

	/**
	 * 脂肪
	 */
	protected double fat;

	/**
	 * 碳水化合物
	 */
	protected double carbohydrate;

	/**
	 * 蛋白质
	 */
	protected double proteinHE;

	/**
	 * 脂肪
	 */
	protected double fatHE;

	/**
	 * 碳水化合物
	 */
	protected double carbohydrateHE;

	/**
	 * 微生素A
	 */
	protected double vitaminA;

	/**
	 * 微生素B1
	 */
	protected double vitaminB1;

	/**
	 * 微生素B2
	 */
	protected double vitaminB2;

	/**
	 * 微生素C
	 */
	protected double vitaminC;

	/**
	 * 尼克酸
	 */
	protected double nicotinicCid;

	/**
	 * 钙
	 */
	protected double calcium;

	/**
	 * 铁
	 */
	protected double iron;

	/**
	 * 锌
	 */
	protected double zinc;

	/**
	 * 动豆蛋白
	 */
	protected double animalBeansProtein;

	/**
	 * 动物性蛋白
	 */
	protected double animalProtein;

	/**
	 * 植物性蛋白
	 */
	protected double plantProtein;

	/**
	 * 动物性脂肪
	 */
	protected double animalFat;

	/**
	 * 植物性脂肪
	 */
	protected double plantFat;

	/**
	 * 热能
	 */
	protected String heatEnergyString;

	/**
	 * 蛋白质
	 */
	protected String proteinString;

	/**
	 * 脂肪
	 */
	protected String fatString;

	/**
	 * 碳水化合物
	 */
	protected String carbohydrateString;

	/**
	 * 蛋白质
	 */
	protected String proteinHEString;

	/**
	 * 脂肪
	 */
	protected String fatHEString;

	/**
	 * 碳水化合物
	 */
	protected String carbohydrateHEString;

	/**
	 * 微生素A
	 */
	protected String vitaminAString;

	/**
	 * 微生素B1
	 */
	protected String vitaminB1String;

	/**
	 * 微生素B2
	 */
	protected String vitaminB2String;

	/**
	 * 微生素C
	 */
	protected String vitaminCString;

	/**
	 * 尼克酸
	 */
	protected String nicotinicCidString;

	/**
	 * 钙
	 */
	protected String calciumString;

	/**
	 * 铁
	 */
	protected String ironString;

	/**
	 * 锌
	 */
	protected String zincString;

	/**
	 * 动豆蛋白
	 */
	protected String animalBeansProteinString;

	/**
	 * 动物性蛋白
	 */
	protected String animalProteinString;

	/**
	 * 植物性蛋白
	 */
	protected String plantProteinString;

	/**
	 * 动物性脂肪
	 */
	protected String animalFatString;

	/**
	 * 植物性脂肪
	 */
	protected String plantFatString;

	public DietaryAnalyzeModel() {

	}

	public double getAnimalBeansProtein() {
		if (animalBeansProtein > 0) {
			animalBeansProtein = Math.round(animalBeansProtein * 1000D) / 1000D;
		}
		return animalBeansProtein;
	}

	public String getAnimalBeansProteinString() {
		return animalBeansProteinString;
	}

	public double getAnimalFat() {
		if (animalFat > 0) {
			animalFat = Math.round(animalFat * 1000D) / 1000D;
		}
		return animalFat;
	}

	public String getAnimalFatString() {
		return animalFatString;
	}

	public double getAnimalProtein() {
		if (animalProtein > 0) {
			animalProtein = Math.round(animalProtein * 1000D) / 1000D;
		}
		return animalProtein;
	}

	public String getAnimalProteinString() {
		return animalProteinString;
	}

	public double getCalcium() {
		if (calcium > 0) {
			calcium = Math.round(calcium * 1000D) / 1000D;
		}
		return calcium;
	}

	public String getCalciumString() {
		return calciumString;
	}

	public double getCarbohydrate() {
		if (carbohydrate > 0) {
			carbohydrate = Math.round(carbohydrate * 1000D) / 1000D;
		}
		return carbohydrate;
	}

	public double getCarbohydrateHE() {
		if (carbohydrateHE > 0) {
			carbohydrateHE = Math.round(carbohydrateHE * 1000D) / 1000D;
		}
		return carbohydrateHE;
	}

	public String getCarbohydrateHEString() {
		return carbohydrateHEString;
	}

	public String getCarbohydrateString() {
		return carbohydrateString;
	}

	public double getFat() {
		if (fat > 0) {
			fat = Math.round(fat * 1000D) / 1000D;
		}
		return fat;
	}

	public double getFatHE() {
		if (fatHE > 0) {
			fatHE = Math.round(fatHE * 1000D) / 1000D;
		}
		return fatHE;
	}

	public String getFatHEString() {
		return fatHEString;
	}

	public String getFatString() {
		return fatString;
	}

	public double getHeatEnergy() {
		if (heatEnergy > 0) {
			heatEnergy = Math.round(heatEnergy * 10D) / 10D;
		}
		return heatEnergy;
	}

	public double getHeatEnergyPercent() {
		if (heatEnergyPercent > 0) {
			heatEnergyPercent = Math.round(heatEnergyPercent * 100D) / 100D;
		}
		return heatEnergyPercent;
	}

	public String getHeatEnergyString() {
		return heatEnergyString;
	}

	public double getIron() {
		if (iron > 0) {
			iron = Math.round(iron * 1000D) / 1000D;
		}
		return iron;
	}

	public String getIronString() {
		return ironString;
	}

	public double getNicotinicCid() {
		if (nicotinicCid > 0) {
			nicotinicCid = Math.round(nicotinicCid * 1000D) / 1000D;
		}
		return nicotinicCid;
	}

	public String getNicotinicCidString() {
		return nicotinicCidString;
	}

	public double getPlantFat() {
		if (plantFat > 0) {
			plantFat = Math.round(plantFat * 1000D) / 1000D;
		}
		return plantFat;
	}

	public String getPlantFatString() {
		return plantFatString;
	}

	public double getPlantProtein() {
		if (plantProtein > 0) {
			plantProtein = Math.round(plantProtein * 1000D) / 1000D;
		}
		return plantProtein;
	}

	public String getPlantProteinString() {
		return plantProteinString;
	}

	public double getProtein() {
		if (protein > 0) {
			protein = Math.round(protein * 1000D) / 1000D;
		}
		return protein;
	}

	public double getProteinHE() {
		if (proteinHE > 0) {
			proteinHE = Math.round(proteinHE * 1000D) / 1000D;
		}
		return proteinHE;
	}

	public String getProteinHEString() {
		return proteinHEString;
	}

	public String getProteinString() {
		return proteinString;
	}

	public double getVitaminA() {
		if (vitaminA > 0) {
			vitaminA = Math.round(vitaminA * 1000D) / 1000D;
		}
		return vitaminA;
	}

	public String getVitaminAString() {
		return vitaminAString;
	}

	public double getVitaminB1() {
		if (vitaminB1 > 0) {
			vitaminB1 = Math.round(vitaminB1 * 1000D) / 1000D;
		}
		return vitaminB1;
	}

	public String getVitaminB1String() {
		return vitaminB1String;
	}

	public double getVitaminB2() {
		if (vitaminB2 > 0) {
			vitaminB2 = Math.round(vitaminB2 * 1000D) / 1000D;
		}
		return vitaminB2;
	}

	public String getVitaminB2String() {
		return vitaminB2String;
	}

	public double getVitaminC() {
		if (vitaminC > 0) {
			vitaminC = Math.round(vitaminC * 1000D) / 1000D;
		}
		return vitaminC;
	}

	public String getVitaminCString() {
		return vitaminCString;
	}

	public double getZinc() {
		if (zinc > 0) {
			zinc = Math.round(zinc * 1000D) / 1000D;
		}
		return zinc;
	}

	public String getZincString() {
		return zincString;
	}

	public void setAnimalBeansProtein(double animalBeansProtein) {
		this.animalBeansProtein = animalBeansProtein;
	}

	public void setAnimalBeansProteinString(String animalBeansProteinString) {
		this.animalBeansProteinString = animalBeansProteinString;
	}

	public void setAnimalFat(double animalFat) {
		this.animalFat = animalFat;
	}

	public void setAnimalFatString(String animalFatString) {
		this.animalFatString = animalFatString;
	}

	public void setAnimalProtein(double animalProtein) {
		this.animalProtein = animalProtein;
	}

	public void setAnimalProteinString(String animalProteinString) {
		this.animalProteinString = animalProteinString;
	}

	public void setCalcium(double calcium) {
		this.calcium = calcium;
	}

	public void setCalciumString(String calciumString) {
		this.calciumString = calciumString;
	}

	public void setCarbohydrate(double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	public void setCarbohydrateHE(double carbohydrateHE) {
		this.carbohydrateHE = carbohydrateHE;
	}

	public void setCarbohydrateHEString(String carbohydrateHEString) {
		this.carbohydrateHEString = carbohydrateHEString;
	}

	public void setCarbohydrateString(String carbohydrateString) {
		this.carbohydrateString = carbohydrateString;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public void setFatHE(double fatHE) {
		this.fatHE = fatHE;
	}

	public void setFatHEString(String fatHEString) {
		this.fatHEString = fatHEString;
	}

	public void setFatString(String fatString) {
		this.fatString = fatString;
	}

	public void setHeatEnergy(double heatEnergy) {
		this.heatEnergy = heatEnergy;
	}

	public void setHeatEnergyPercent(double heatEnergyPercent) {
		this.heatEnergyPercent = heatEnergyPercent;
	}

	public void setHeatEnergyString(String heatEnergyString) {
		this.heatEnergyString = heatEnergyString;
	}

	public void setIron(double iron) {
		this.iron = iron;
	}

	public void setIronString(String ironString) {
		this.ironString = ironString;
	}

	public void setNicotinicCid(double nicotinicCid) {
		this.nicotinicCid = nicotinicCid;
	}

	public void setNicotinicCidString(String nicotinicCidString) {
		this.nicotinicCidString = nicotinicCidString;
	}

	public void setPlantFat(double plantFat) {
		this.plantFat = plantFat;
	}

	public void setPlantFatString(String plantFatString) {
		this.plantFatString = plantFatString;
	}

	public void setPlantProtein(double plantProtein) {
		this.plantProtein = plantProtein;
	}

	public void setPlantProteinString(String plantProteinString) {
		this.plantProteinString = plantProteinString;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setProteinHE(double proteinHE) {
		this.proteinHE = proteinHE;
	}

	public void setProteinHEString(String proteinHEString) {
		this.proteinHEString = proteinHEString;
	}

	public void setProteinString(String proteinString) {
		this.proteinString = proteinString;
	}

	public void setVitaminA(double vitaminA) {
		this.vitaminA = vitaminA;
	}

	public void setVitaminAString(String vitaminAString) {
		this.vitaminAString = vitaminAString;
	}

	public void setVitaminB1(double vitaminB1) {
		this.vitaminB1 = vitaminB1;
	}

	public void setVitaminB1String(String vitaminB1String) {
		this.vitaminB1String = vitaminB1String;
	}

	public void setVitaminB2(double vitaminB2) {
		this.vitaminB2 = vitaminB2;
	}

	public void setVitaminB2String(String vitaminB2String) {
		this.vitaminB2String = vitaminB2String;
	}

	public void setVitaminC(double vitaminC) {
		this.vitaminC = vitaminC;
	}

	public void setVitaminCString(String vitaminCString) {
		this.vitaminCString = vitaminCString;
	}

	public void setZinc(double zinc) {
		this.zinc = zinc;
	}

	public void setZincString(String zincString) {
		this.zincString = zincString;
	}

}
