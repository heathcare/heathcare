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

package com.glaf.heathcare.web.springmvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.SysTreeService;

import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.FoodFavorite;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.query.FoodFavoriteQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodFavoriteService;
import com.glaf.heathcare.service.GoodsActualQuantityService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/foodFavorite")
@RequestMapping("/heathcare/foodFavorite")
public class FoodFavoriteController {
	protected static final Log logger = LogFactory.getLog(FoodFavoriteController.class);

	protected FoodCompositionService foodCompositionService;

	protected FoodFavoriteService foodFavoriteService;

	protected GoodsActualQuantityService goodsActualQuantityService;

	protected SysTreeService sysTreeService;

	public FoodFavoriteController() {

	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		try {
			SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
			if (root != null) {
				List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
				request.setAttribute("foodCategories", foodCategories);
			}

			long nodeId = RequestUtils.getLong(request, "nodeId");
			FoodFavoriteQuery query = new FoodFavoriteQuery();
			if (loginContext.isSystemAdministrator()) {
				query.tenantId("SYS");
			} else {
				query.tenantId(loginContext.getTenantId());
			}

			if (nodeId > 0) {
				query.nodeId(nodeId);

				List<FoodComposition> foods = foodCompositionService.getFoodCompositions(nodeId);
				List<FoodComposition> rows = new ArrayList<FoodComposition>();
				Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
				for (FoodComposition food : foods) {
					if (StringUtils.equals(food.getEnableFlag(), "N")) {
						continue;
					}
					rows.add(food);
					foodMap.put(food.getId(), food);
				}

				request.setAttribute("foods", rows);

				List<FoodFavorite> foodFavorites = foodFavoriteService.list(query);
				if (foodFavorites != null && !foodFavorites.isEmpty()) {
					for (FoodFavorite f : foodFavorites) {
						if (foodMap.get(f.getFoodId()) != null) {
							f.setName(foodMap.get(f.getFoodId()).getName());
						}
					}
				} else {
					GoodsActualQuantityQuery query2 = new GoodsActualQuantityQuery();
					if (loginContext.isSystemAdministrator()) {
						query2.tenantId("SYS");
					} else {
						query2.tenantId(loginContext.getTenantId());
					}
					if (nodeId > 0) {
						query2.goodsNodeId(nodeId);
					}
					List<GoodsActualQuantity> goods = goodsActualQuantityService.list(query2);
					if (goods != null && !goods.isEmpty()) {
						Set<Long> exists = new HashSet<Long>();
						foodFavorites = new ArrayList<FoodFavorite>();
						for (GoodsActualQuantity model : goods) {
							if (!exists.contains(model.getGoodsId())) {
								FoodFavorite f = new FoodFavorite();
								f.setFoodId(model.getGoodsId());
								if (foodMap.get(f.getFoodId()) != null) {
									f.setName(foodMap.get(f.getFoodId()).getName());
									f.setSortNo(foodMap.get(f.getFoodId()).getSortNo());
									exists.add(model.getGoodsId());
									foodFavorites.add(f);
								}
							}
						}
					}
				}

				request.setAttribute("foodFavorites", foodFavorites);

				List<Long> selected = new ArrayList<Long>();
				StringBuilder buffery = new StringBuilder();
				for (FoodFavorite food : foodFavorites) {
					if (StringUtils.isNotEmpty(food.getName())) {
						selected.add(food.getFoodId());
						buffery.append("<option value=\"").append(food.getFoodId()).append("\">").append(food.getName())
								.append("</option>");
					}
				}
				request.setAttribute("buffery", buffery.toString());

				StringBuilder bufferx = new StringBuilder();
				for (FoodComposition food : rows) {
					if (selected.contains(food.getId())) {
						continue;
					}
					bufferx.append("<option value=\"").append(food.getId()).append("\">").append(food.getName())
							.append("</option>");
				}
				request.setAttribute("bufferx", bufferx.toString());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}

		request.setAttribute("save_permission", "false");
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
				|| loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("TenantAdmin")) {
			request.setAttribute("save_permission", "true");
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/foodFavorite/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAll")
	public byte[] saveAll(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long nodeId = RequestUtils.getLong(request, "nodeId");
		try {
			if ((loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
					|| loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("TenantAdmin"))
					&& nodeId > 0) {
				List<FoodComposition> foods = foodCompositionService.getFoodCompositions(nodeId);
				Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
				for (FoodComposition food : foods) {
					foodMap.put(food.getId(), food);
				}

				List<FoodFavorite> foodFavorites = new ArrayList<FoodFavorite>();
				String items = request.getParameter("items");
				if (StringUtils.isNotEmpty(items)) {
					int sort = 0;
					StringTokenizer token = new StringTokenizer(items, ",");
					while (token.hasMoreTokens()) {
						String item = token.nextToken();
						if (StringUtils.isNotEmpty(item)) {
							sort++;
						}
						FoodFavorite f = new FoodFavorite();
						f.setFoodId(Long.parseLong(item));
						if (foodMap.get(f.getFoodId()) != null) {
							f.setNodeId(foodMap.get(f.getFoodId()).getNodeId());
							f.setSortNo(sort);
							foodFavorites.add(f);
						}
					}
				}

				if (loginContext.isSystemAdministrator()) {
					foodFavoriteService.saveAll("SYS", nodeId, foodFavorites);
				} else {
					foodFavoriteService.saveAll(loginContext.getTenantId(), nodeId, foodFavorites);
				}
			}

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodFavoriteService")
	public void setFoodFavoriteService(FoodFavoriteService foodFavoriteService) {
		this.foodFavoriteService = foodFavoriteService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsActualQuantityService")
	public void setGoodsActualQuantityService(GoodsActualQuantityService goodsActualQuantityService) {
		this.goodsActualQuantityService = goodsActualQuantityService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}
}
