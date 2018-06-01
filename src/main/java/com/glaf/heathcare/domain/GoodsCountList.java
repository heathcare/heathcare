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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GoodsCountList implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String key;

	protected int fullDay;

	protected List<GoodsCount> countList = new ArrayList<GoodsCount>();

	protected Map<Long, GoodsCount> countMap = new LinkedHashMap<Long, GoodsCount>();

	public GoodsCountList() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsCountList other = (GoodsCountList) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	public List<GoodsCount> getCountList() {
		if (countList == null) {
			countList = new ArrayList<GoodsCount>();
		}
		if (countList.isEmpty()) {
			Collection<GoodsCount> values = countMap.values();
			for (GoodsCount cnt : values) {
				countList.add(cnt);
			}
		}
		return countList;
	}

	public Map<Long, GoodsCount> getCountMap() {
		if (countMap == null) {
			countMap = new LinkedHashMap<Long, GoodsCount>();
		}
		return countMap;
	}

	public int getFullDay() {
		return fullDay;
	}

	public String getKey() {
		return key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	public void setCountList(List<GoodsCount> countList) {
		this.countList = countList;
	}

	public void setCountMap(Map<Long, GoodsCount> countMap) {
		this.countMap = countMap;
	}

	public void setFullDay(int fullDay) {
		this.fullDay = fullDay;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
