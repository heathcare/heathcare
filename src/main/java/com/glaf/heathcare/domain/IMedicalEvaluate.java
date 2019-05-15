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

public interface IMedicalEvaluate {

	int getAgeOfTheMoon();

	double getBmi();

	String getBmiEvaluate();

	String getBmiEvaluateHtml();

	double getBmiIndex();

	double getHeight();

	String getHeightEvaluate();

	String getHeightEvaluateHtml();

	int getHeightLevel();

	int getMonth();

	String getPersonId();

	String getSex();

	String getType();

	double getWeight();

	String getWeightEvaluate();

	String getWeightEvaluateHtml();

	String getWeightHeightEvaluate();

	String getWeightHeightEvaluateHtml();

	int getWeightHeightLevel();

	int getWeightLevel();

	int getYear();

	void setAgeOfTheMoon(int ageOfTheMoon);

	void setBmi(double bmi);

	void setBmiEvaluate(String bmiEvaluate);

	void setBmiEvaluateHtml(String bmiEvaluateHtml);

	void setBmiIndex(double bmiIndex);

	void setHeight(double height);

	void setHeightEvaluate(String heightEvaluate);

	void setHeightEvaluateHtml(String heightEvaluateHtml);

	void setHeightLevel(int heightLevel);

	void setMonth(int month);

	void setPersonId(String personId);

	void setSex(String sex);

	void setType(String type);

	void setWeight(double weight);

	void setWeightEvaluate(String weightEvaluate);

	void setWeightEvaluateHtml(String weightEvaluateHtml);

	void setWeightHeightEvaluate(String weightHeightEvaluate);

	void setWeightHeightEvaluateHtml(String weightHeightEvaluateHtml);

	void setWeightHeightLevel(int weightHeightLevel);

	void setWeightLevel(int weightLevel);

	void setYear(int year);

}
