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

package com.glaf.modules.supplier.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.glaf.modules.supplier.domain.*;
import com.glaf.modules.supplier.query.*;

/**
 * 
 * Mapper接口
 *
 */

@Component("com.glaf.modules.supplier.mapper.SupplierMapper")
public interface SupplierMapper {

	void bulkInsertSupplier(List<Supplier> list);

	void bulkInsertSupplier_oracle(List<Supplier> list);

	void deleteSuppliers(SupplierQuery query);

	void deleteSupplierById(String supplierId);

	Supplier getSupplierById(String supplierId);

	int getSupplierCount(SupplierQuery query);

	List<Supplier> getSuppliers(SupplierQuery query);

	void insertSupplier(Supplier model);

	void updateSupplier(Supplier model);

}
