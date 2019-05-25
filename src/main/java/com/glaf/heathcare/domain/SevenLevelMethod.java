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

public class SevenLevelMethod implements Serializable {
	private static final long serialVersionUID = 1L;

	protected double negative3D;

	protected double negative2D;

	protected double negative1D;

	protected double normal;

	protected double positive1D;

	protected double positive2D;

	protected double positive3D;

	public SevenLevelMethod() {

	}

	public double getNegative1D() {
		return negative1D;
	}

	public String getNegative1DString() {
		if (negative1D > 0) {
			return String.valueOf(negative1D);
		}
		return "";
	}

	public double getNegative2D() {
		return negative2D;
	}

	public String getNegative2DString() {
		if (negative2D > 0) {
			return String.valueOf(negative2D);
		}
		return "";
	}

	public double getNegative3D() {
		return negative3D;
	}

	public String getNegative3DString() {
		if (negative3D > 0) {
			return String.valueOf(negative3D);
		}
		return "";
	}

	public double getNormal() {
		return normal;
	}

	public String getNormalString() {
		if (normal > 0) {
			return String.valueOf(normal);
		}
		return "";
	}

	public double getPositive1D() {
		return positive1D;
	}

	public String getPositive1DString() {
		if (positive1D > 0) {
			return String.valueOf(positive1D);
		}
		return "";
	}

	public double getPositive2D() {
		return positive2D;
	}

	public String getPositive2DString() {
		if (positive2D > 0) {
			return String.valueOf(positive2D);
		}
		return "";
	}

	public double getPositive3D() {
		return positive3D;
	}

	public String getPositive3DString() {
		if (positive3D > 0) {
			return String.valueOf(positive3D);
		}
		return "";
	}

	public void setNegative1D(double negative1d) {
		negative1D = negative1d;
	}

	public void setNegative2D(double negative2d) {
		negative2D = negative2d;
	}

	public void setNegative3D(double negative3d) {
		negative3D = negative3d;
	}

	public void setNormal(double normal) {
		this.normal = normal;
	}

	public void setPositive1D(double positive1d) {
		positive1D = positive1d;
	}

	public void setPositive2D(double positive2d) {
		positive2D = positive2d;
	}

	public void setPositive3D(double positive3d) {
		positive3D = positive3d;
	}

}
