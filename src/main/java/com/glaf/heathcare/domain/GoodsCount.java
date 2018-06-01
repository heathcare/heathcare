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

public class GoodsCount implements Serializable {

	private static final long serialVersionUID = 1L;

	protected long goodsId;

	protected long goodsNodeId;

	protected String goodsName;

	/**
	 * 入库数量
	 */
	protected double instockQuantity;

	protected String instockQuantityString;

	protected double instockPrice;

	protected String instockPriceString;

	/**
	 * 出库数量
	 */
	protected double outstockQuantity;

	protected String outstockQuantityString;

	protected double outstockPrice;

	protected String outstockPriceString;

	/**
	 * 库存量
	 */
	protected double stockQuantity;

	protected String stockQuantityString;

	/**
	 * 实际用量
	 */
	protected double useQuantity;

	protected String useQuantityString;

	protected double usePrice;

	protected String usePriceString;

	protected String key;

	protected int fullDay;

	public GoodsCount() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsCount other = (GoodsCount) obj;
		if (goodsId != other.goodsId)
			return false;
		return true;
	}

	public int getFullDay() {
		return fullDay;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public String getGoodsName() {
		if (goodsName == null) {
			goodsName = "";
		}
		return goodsName;
	}

	public long getGoodsNodeId() {
		return goodsNodeId;
	}

	public double getInstockPrice() {
		return instockPrice;
	}

	public String getInstockPriceString() {
		instockPriceString = "";
		if (instockPrice > 0) {
			instockPriceString = String.valueOf(instockPrice);
		}
		return instockPriceString;
	}

	public double getInstockQuantity() {
		return instockQuantity;
	}

	public String getInstockQuantityString() {
		instockQuantityString = "";
		if (instockQuantity > 0) {
			instockQuantityString = String.valueOf(instockQuantity);
		}
		return instockQuantityString;
	}

	public String getKey() {
		return key;
	}

	public double getOutstockPrice() {
		return outstockPrice;
	}

	public String getOutstockPriceString() {
		outstockPriceString = "";
		if (outstockPrice > 0) {
			outstockPriceString = String.valueOf(outstockPrice);
		}
		return outstockPriceString;
	}

	public double getOutstockQuantity() {
		return outstockQuantity;
	}

	public String getOutstockQuantityString() {
		outstockQuantityString = "";
		if (outstockQuantity > 0) {
			outstockQuantityString = String.valueOf(outstockQuantity);
		}
		return outstockQuantityString;
	}

	public double getStockQuantity() {
		return stockQuantity;
	}

	public String getStockQuantityString() {
		stockQuantityString = "";
		if (stockQuantity > 0) {
			stockQuantityString = String.valueOf(stockQuantity);
		}
		return stockQuantityString;
	}

	public double getUsePrice() {
		return usePrice;
	}

	public String getUsePriceString() {
		usePriceString = "";
		if (usePrice > 0) {
			usePriceString = String.valueOf(usePrice);
		}
		return usePriceString;
	}

	public double getUseQuantity() {
		return useQuantity;
	}

	public String getUseQuantityString() {
		useQuantityString = "";
		if (useQuantity > 0) {
			useQuantityString = String.valueOf(useQuantity);
		}
		return useQuantityString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (goodsId ^ (goodsId >>> 32));
		return result;
	}

	public void setFullDay(int fullDay) {
		this.fullDay = fullDay;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setGoodsNodeId(long goodsNodeId) {
		this.goodsNodeId = goodsNodeId;
	}

	public void setInstockPrice(double instockPrice) {
		this.instockPrice = instockPrice;
	}

	public void setInstockPriceString(String instockPriceString) {
		this.instockPriceString = instockPriceString;
	}

	public void setInstockQuantity(double instockQuantity) {
		this.instockQuantity = instockQuantity;
	}

	public void setInstockQuantityString(String instockQuantityString) {
		this.instockQuantityString = instockQuantityString;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setOutstockPrice(double outstockPrice) {
		this.outstockPrice = outstockPrice;
	}

	public void setOutstockPriceString(String outstockPriceString) {
		this.outstockPriceString = outstockPriceString;
	}

	public void setOutstockQuantity(double outstockQuantity) {
		this.outstockQuantity = outstockQuantity;
	}

	public void setOutstockQuantityString(String outstockQuantityString) {
		this.outstockQuantityString = outstockQuantityString;
	}

	public void setStockQuantity(double stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public void setStockQuantityString(String stockQuantityString) {
		this.stockQuantityString = stockQuantityString;
	}

	public void setUsePrice(double usePrice) {
		this.usePrice = usePrice;
	}

	public void setUsePriceString(String usePriceString) {
		this.usePriceString = usePriceString;
	}

	public void setUseQuantity(double useQuantity) {
		this.useQuantity = useQuantity;
	}

	public void setUseQuantityString(String useQuantityString) {
		this.useQuantityString = useQuantityString;
	}

}
