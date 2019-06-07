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

public class SevenLevelMethodQuantity implements Serializable {
	private static final long serialVersionUID = 1L;

	protected String key;

	protected int negative3D;// <M-3SD

	protected int negative2D;// <M-2SD

	protected int negative1D;// <M-1SD

	protected int normal;// ≥M-1SD ～ M+1SD

	protected int positive1D;// ≥M+1SD

	protected int positive2D;// ≥M+2SD

	protected int positive3D;// ≥M+3SD

	public SevenLevelMethodQuantity() {

	}

	public String getKey() {
		return key;
	}

	public int getNegative1D() {
		return negative1D;
	}

	public int getNegative2D() {
		return negative2D;
	}

	public int getNegative3D() {
		return negative3D;
	}

	public int getNormal() {
		return normal;
	}

	public int getPositive1D() {
		return positive1D;
	}

	public int getPositive2D() {
		return positive2D;
	}

	public int getPositive3D() {
		return positive3D;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNegative1D(int negative1d) {
		negative1D = negative1d;
	}

	public void setNegative2D(int negative2d) {
		negative2D = negative2d;
	}

	public void setNegative3D(int negative3d) {
		negative3D = negative3d;
	}

	public void setNormal(int normal) {
		this.normal = normal;
	}

	public void setPositive1D(int positive1d) {
		positive1D = positive1d;
	}

	public void setPositive2D(int positive2d) {
		positive2D = positive2d;
	}

	public void setPositive3D(int positive3d) {
		positive3D = positive3d;
	}

}
