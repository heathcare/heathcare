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

import java.util.ArrayList;
import java.util.List;

import com.glaf.core.util.FileUtils;

public class MedicalSpotCheckTotalModel {

	/**
	 * 月龄
	 */
	protected int ageOfTheMoon;

	/**
	 * 月龄
	 */
	protected String ageOfTheMoonString;

	/**
	 * 最大值
	 */
	protected double max;

	protected String maxString;

	/**
	 * 最小值
	 */
	protected double min;

	protected String minString;

	/**
	 * 平均值
	 */
	protected double avg;

	protected String avgString;

	/**
	 * 与WHO标准的偏差
	 */
	protected double whoDiffValue;

	protected String whoDiffValueString;

	/**
	 * 与中国标准的偏差
	 */
	protected double cnDiffValue;

	protected String cnDiffValueString;

	protected int negative3DQty;

	protected int negative2DQty;

	protected int negative1DQty;

	protected int normalQty;

	protected int positive1DQty;

	protected int positive2DQty;

	protected int positive3DQty;

	/**
	 * 样本个数
	 */
	protected int recordTotal;

	protected double isoStdValue;

	protected double cnStdValue;

	protected double totalValue;

	protected String sample;

	protected List<ItemValue> negative3DList = new ArrayList<ItemValue>();

	protected List<ItemValue> negative2DList = new ArrayList<ItemValue>();

	protected List<ItemValue> negative1DList = new ArrayList<ItemValue>();

	protected List<ItemValue> normalList = new ArrayList<ItemValue>();

	protected List<ItemValue> positive1DList = new ArrayList<ItemValue>();

	protected List<ItemValue> positive2DList = new ArrayList<ItemValue>();

	protected List<ItemValue> positive3DList = new ArrayList<ItemValue>();

	protected List<SevenLevelMethod> childrenList = new ArrayList<SevenLevelMethod>();

	public MedicalSpotCheckTotalModel() {

	}

	public void addNegative1D(double value) {
		ItemValue m = new ItemValue();
		m.setValue(value);
		negative1DList.add(m);
	}

	public void addNegative2D(double value) {
		ItemValue m = new ItemValue();
		m.setValue(value);
		negative2DList.add(m);
	}

	public void addNegative3D(double value) {
		ItemValue m = new ItemValue();
		m.setValue(value);
		negative3DList.add(m);
	}

	public void addNormal(double value) {
		ItemValue m = new ItemValue();
		m.setValue(value);
		normalList.add(m);
	}

	public void addPositive1D(double value) {
		ItemValue m = new ItemValue();
		m.setValue(value);
		positive1DList.add(m);
	}

	public void addPositive2D(double value) {
		ItemValue m = new ItemValue();
		m.setValue(value);
		positive2DList.add(m);
	}

	public void addPositive3D(double value) {
		ItemValue m = new ItemValue();
		m.setValue(value);
		positive3DList.add(m);
	}

	public synchronized void addRecord() {
		++recordTotal;
	}

	public void addTotla(double value) {
		totalValue += value;
	}

	public int getAgeOfTheMoon() {
		return ageOfTheMoon;
	}

	public String getAgeOfTheMoonString() {
		if (ageOfTheMoon > 0) {
			int year = ageOfTheMoon / 12;
			int month = ageOfTheMoon % 12;
			ageOfTheMoonString = year + "岁" + month + "月";
			return ageOfTheMoonString;
		}
		return "";
	}

	public double getAvg() {
		avg = Math.round(avg * 10D) / 10D;
		return avg;
	}

	public String getAvgString() {
		if (avg > 0) {
			avg = Math.round(avg * 10D) / 10D;
		} else {
			double total = 0;
			int size = negative3DList.size() + negative2DList.size() + negative1DList.size() + normalList.size()
					+ positive1DList.size() + positive2DList.size() + positive3DList.size();
			if (size > 0) {
				for (ItemValue val : negative3DList) {
					total += val.getValue();
				}
				for (ItemValue val : negative2DList) {
					total += val.getValue();
				}
				for (ItemValue val : negative1DList) {
					total += val.getValue();
				}
				for (ItemValue val : normalList) {
					total += val.getValue();
				}
				for (ItemValue val : positive1DList) {
					total += val.getValue();
				}
				for (ItemValue val : positive2DList) {
					total += val.getValue();
				}
				for (ItemValue val : positive3DList) {
					total += val.getValue();
				}
				avg = total / size;
				avg = Math.round(avg * 10D) / 10D;
			}
		}
		avgString = String.valueOf(avg);
		return avgString;
	}

	public List<SevenLevelMethod> getChildrenList() {
		return childrenList;
	}

	public double getCnDiffValue() {
		return cnDiffValue;
	}

	public String getCnDiffValueString() {
		if (cnDiffValue != 0) {
			cnDiffValue = Math.round(cnDiffValue * 10D) / 10D;
			cnDiffValueString = String.valueOf(cnDiffValue);
			return cnDiffValueString;
		}
		return "";
	}

	public double getIsoStdValue() {
		return isoStdValue;
	}

	public String getIsoStdValueString() {
		isoStdValue = Math.round(isoStdValue * 10D) / 10D;
		return String.valueOf(isoStdValue);
	}

	public double getMax() {
		return max;
	}

	public String getMaxString() {
		if (max > 0) {
			return String.valueOf(max);
		}
		return "";
	}

	public double getMin() {
		return min;
	}

	public String getMinString() {
		if (min > 0) {
			return String.valueOf(min);
		}
		return "";
	}

	public List<ItemValue> getNegative1DList() {
		return negative1DList;
	}

	public int getNegative1DQty() {
		if (negative1DQty == 0) {
			if (negative1DList.size() > 0) {
				negative1DQty = negative1DList.size();
			}
		}
		return negative1DQty;
	}

	public String getNegative1DQtyString() {
		if (getNegative1DQty() > 0) {
			return String.valueOf(getNegative1DQty());
		}
		return "";
	}

	public List<ItemValue> getNegative2DList() {
		return negative2DList;
	}

	public int getNegative2DQty() {
		if (negative2DQty == 0) {
			if (negative2DList.size() > 0) {
				negative2DQty = negative2DList.size();
			}
		}
		return negative2DQty;
	}

	public String getNegative2DQtyString() {
		if (getNegative2DQty() > 0) {
			return String.valueOf(getNegative2DQty());
		}
		return "";
	}

	public List<ItemValue> getNegative3DList() {
		return negative3DList;
	}

	public int getNegative3DQty() {
		if (negative3DQty == 0) {
			if (negative3DList.size() > 0) {
				negative3DQty = negative3DList.size();
			}
		}
		return negative3DQty;
	}

	public String getNegative3DQtyString() {
		if (getNegative3DQty() > 0) {
			return String.valueOf(getNegative3DQty());
		}
		return "";
	}

	public List<ItemValue> getNormalList() {
		return normalList;
	}

	public int getNormalQty() {
		if (normalQty == 0) {
			if (normalList.size() > 0) {
				normalQty = normalList.size();
			}
		}
		return normalQty;
	}

	public String getNormalQtyString() {
		if (getNormalQty() > 0) {
			return String.valueOf(getNormalQty());
		}
		return "";
	}

	public List<ItemValue> getPositive1DList() {
		return positive1DList;
	}

	public int getPositive1DQty() {
		if (positive1DQty == 0) {
			if (positive1DList.size() > 0) {
				positive1DQty = positive1DList.size();
			}
		}
		return positive1DQty;
	}

	public String getPositive1DQtyString() {
		if (getPositive1DQty() > 0) {
			return String.valueOf(getPositive1DQty());
		}
		return "";
	}

	public List<ItemValue> getPositive2DList() {
		return positive2DList;
	}

	public int getPositive2DQty() {
		if (positive2DQty == 0) {
			if (positive2DList.size() > 0) {
				positive2DQty = positive2DList.size();
			}
		}
		return positive2DQty;
	}

	public String getPositive2DQtyString() {
		if (getPositive2DQty() > 0) {
			return String.valueOf(getPositive2DQty());
		}
		return "";
	}

	public List<ItemValue> getPositive3DList() {
		return positive3DList;
	}

	public int getPositive3DQty() {
		if (positive3DQty == 0) {
			if (positive3DList.size() > 0) {
				positive3DQty = positive3DList.size();
			}
		}
		return positive3DQty;
	}

	public String getPositive3DQtyString() {
		if (getPositive3DQty() > 0) {
			return String.valueOf(getPositive3DQty());
		}
		return "";
	}

	public int getRecordTotal() {
		return recordTotal;
	}

	public String getSample() {
		StringBuilder buffer = new StringBuilder();
		int size = negative3DList.size() + negative2DList.size() + negative1DList.size() + normalList.size()
				+ positive1DList.size() + positive2DList.size() + positive3DList.size();
		if (size > 0) {
			int index = 0;
			for (ItemValue val : negative3DList) {
				if (index > 0 && index % 5 == 0) {
					buffer.append(FileUtils.newline);
				}
				buffer.append(val.getValue()).append(" | ");
				index++;
			}
			for (ItemValue val : negative2DList) {
				if (index > 0 && index % 5 == 0) {
					buffer.append(FileUtils.newline);
				}
				buffer.append(val.getValue()).append(" | ");
				index++;
			}
			for (ItemValue val : negative1DList) {
				if (index > 0 && index % 5 == 0) {
					buffer.append(FileUtils.newline);
				}
				buffer.append(val.getValue()).append(" | ");
				index++;
			}
			for (ItemValue val : normalList) {
				if (index > 0 && index % 5 == 0) {
					buffer.append(FileUtils.newline);
				}
				buffer.append(val.getValue()).append(" | ");
				index++;
			}
			for (ItemValue val : positive1DList) {
				if (index > 0 && index % 5 == 0) {
					buffer.append(FileUtils.newline);
				}
				buffer.append(val.getValue()).append(" | ");
				index++;
			}
			for (ItemValue val : positive2DList) {
				if (index > 0 && index % 5 == 0) {
					buffer.append(FileUtils.newline);
				}
				buffer.append(val.getValue()).append(" | ");
				index++;
			}
			for (ItemValue val : positive3DList) {
				if (index > 0 && index % 5 == 0) {
					buffer.append(FileUtils.newline);
				}
				buffer.append(val.getValue()).append(" | ");
				index++;
			}
		}
		return buffer.toString();
	}

	public double getTotalValue() {
		return totalValue;
	}

	public double getWhoDiffValue() {
		return whoDiffValue;
	}

	public String getWhoDiffValueString() {
		if (whoDiffValue != 0) {
			whoDiffValue = Math.round(whoDiffValue * 10D) / 10D;
			if (whoDiffValue > 0) {
				whoDiffValueString = "+" + String.valueOf(whoDiffValue);
			} else {
				whoDiffValueString = String.valueOf(whoDiffValue);
			}
			return whoDiffValueString;
		}
		return "";
	}

	public void setAgeOfTheMoon(int ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public void setChildrenList(List<SevenLevelMethod> childrenList) {
		this.childrenList = childrenList;
	}

	public void setCnDiffValue(double cnDiffValue) {
		this.cnDiffValue = cnDiffValue;
	}

	public void setIsoStdValue(double isoStdValue) {
		this.isoStdValue = isoStdValue;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public void setNegative1DList(List<ItemValue> negative1dList) {
		negative1DList = negative1dList;
	}

	public void setNegative1DQty(int negative1dQty) {
		negative1DQty = negative1dQty;
	}

	public void setNegative2DList(List<ItemValue> negative2dList) {
		negative2DList = negative2dList;
	}

	public void setNegative2DQty(int negative2dQty) {
		negative2DQty = negative2dQty;
	}

	public void setNegative3DList(List<ItemValue> negative3dList) {
		negative3DList = negative3dList;
	}

	public void setNegative3DQty(int negative3dQty) {
		negative3DQty = negative3dQty;
	}

	public void setNormalList(List<ItemValue> normalList) {
		this.normalList = normalList;
	}

	public void setNormalQty(int normalQty) {
		this.normalQty = normalQty;
	}

	public void setPositive1DList(List<ItemValue> positive1dList) {
		positive1DList = positive1dList;
	}

	public void setPositive1DQty(int positive1dQty) {
		positive1DQty = positive1dQty;
	}

	public void setPositive2DList(List<ItemValue> positive2dList) {
		positive2DList = positive2dList;
	}

	public void setPositive2DQty(int positive2dQty) {
		positive2DQty = positive2dQty;
	}

	public void setPositive3DList(List<ItemValue> positive3dList) {
		positive3DList = positive3dList;
	}

	public void setPositive3DQty(int positive3dQty) {
		positive3DQty = positive3dQty;
	}

	public void setRecordTotal(int recordTotal) {
		this.recordTotal = recordTotal;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public void setWhoDiffValue(double whoDiffValue) {
		this.whoDiffValue = whoDiffValue;
	}

	public void setWhoDiffValueString(String whoDiffValueString) {
		this.whoDiffValueString = whoDiffValueString;
	}

}
