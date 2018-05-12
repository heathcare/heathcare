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

public class WeeklyFee implements Serializable {

	private static final long serialVersionUID = 1L;

	protected int year;

	protected int month;

	protected int week;

	protected int day;

	protected int fullday;

	protected String dateString;

	protected long nodeId;

	protected String name;

	protected double cost;

	public WeeklyFee() {

	}

	public double getCost() {
		if (cost > 0) {
			cost = Math.round(cost * 100D) / 100D;
		}
		return cost;
	}

	public String getDateString() {
		return dateString;
	}

	public int getDay() {
		return day;
	}

	public int getFullday() {
		return fullday;
	}

	public int getMonth() {
		return month;
	}

	public String getName() {
		return name;
	}

	public long getNodeId() {
		return nodeId;
	}

	public int getWeek() {
		return week;
	}

	public int getYear() {
		return year;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setFullday(int fullday) {
		this.fullday = fullday;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
