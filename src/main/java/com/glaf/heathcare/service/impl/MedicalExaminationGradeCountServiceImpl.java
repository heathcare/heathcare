package com.glaf.heathcare.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;

import com.glaf.heathcare.domain.MedicalExaminationGradeCount;
import com.glaf.heathcare.domain.GrowthRateCount;
import com.glaf.heathcare.domain.PhysicalGrowthCount;
import com.glaf.heathcare.mapper.MedicalExaminationGradeCountMapper;
import com.glaf.heathcare.query.MedicalExaminationGradeCountQuery;
import com.glaf.heathcare.query.GrowthRateCountQuery;
import com.glaf.heathcare.query.PhysicalGrowthCountQuery;
import com.glaf.heathcare.service.MedicalExaminationGradeCountService;
import com.glaf.heathcare.service.GrowthRateCountService;
import com.glaf.heathcare.service.PhysicalGrowthCountService;

@Service("com.glaf.heathcare.service.medicalExaminationGradeCountService")
@Transactional(readOnly = true)
public class MedicalExaminationGradeCountServiceImpl implements MedicalExaminationGradeCountService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected MedicalExaminationGradeCountMapper medicalExaminationGradeCountMapper;

	protected GrowthRateCountService growthRateCountService;

	protected PhysicalGrowthCountService physicalGrowthCountService;

	protected SysTenantService sysTenantService;

	public MedicalExaminationGradeCountServiceImpl() {

	}

	@Transactional
	public void bulkInsert(Collection<MedicalExaminationGradeCount> list) {
		for (MedicalExaminationGradeCount medicalExaminationGradeCount : list) {
			if (medicalExaminationGradeCount.getId() == 0) {
				medicalExaminationGradeCount.setId(idGenerator.nextId("HEALTH_MEDICAL_EXAM_GRADE_CNT"));
			}
		}

		int batch_size = 50;
		List<MedicalExaminationGradeCount> rows = new ArrayList<MedicalExaminationGradeCount>(batch_size);

		for (MedicalExaminationGradeCount bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					medicalExaminationGradeCountMapper.bulkInsertMedicalExaminationGradeCount_oracle(rows);
				} else {
					medicalExaminationGradeCountMapper.bulkInsertMedicalExaminationGradeCount(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				medicalExaminationGradeCountMapper.bulkInsertMedicalExaminationGradeCount_oracle(rows);
			} else {
				medicalExaminationGradeCountMapper.bulkInsertMedicalExaminationGradeCount(rows);
			}
			rows.clear();
		}
	}

	public int count(MedicalExaminationGradeCountQuery query) {
		return medicalExaminationGradeCountMapper.getMedicalExaminationGradeCountCount(query);
	}

	/**
	 * 根据checkId删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	public void deleteByCheckId(String checkId) {
		medicalExaminationGradeCountMapper.deleteMedicalExaminationGradeCountByCheckId(checkId);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getMedicalExaminationGradeCountCountByQueryCriteria(MedicalExaminationGradeCountQuery query) {
		return medicalExaminationGradeCountMapper.getMedicalExaminationGradeCountCount(query);
	}

	public List<MedicalExaminationGradeCount> getMedicalExaminationGradeCountList(String tenantId, String checkId) {
		MedicalExaminationGradeCountQuery query = new MedicalExaminationGradeCountQuery();
		query.tenantId(tenantId);
		query.checkId(checkId);
		List<MedicalExaminationGradeCount> list = medicalExaminationGradeCountMapper
				.getMedicalExaminationGradeCounts(query);
		if (list != null && !list.isEmpty()) {
			GrowthRateCountQuery q1 = new GrowthRateCountQuery();
			q1.tenantId(tenantId);
			q1.checkId(checkId);
			List<GrowthRateCount> list1 = growthRateCountService.list(q1);

			PhysicalGrowthCountQuery q2 = new PhysicalGrowthCountQuery();
			q2.tenantId(tenantId);
			q2.checkId(checkId);
			List<PhysicalGrowthCount> list2 = physicalGrowthCountService.list(q2);

			for (MedicalExaminationGradeCount cnt : list) {
				if (list1 != null && !list1.isEmpty()) {
					for (GrowthRateCount c1 : list1) {
						if (StringUtils.equals(cnt.getGradeId(), c1.getGradeId())) {
							if (StringUtils.equals(c1.getType(), "both")) {
								cnt.setBothRate(c1);
							} else if (StringUtils.equals(c1.getType(), "height")) {
								cnt.setHeightRate(c1);
							} else if (StringUtils.equals(c1.getType(), "weight")) {
								cnt.setWeightRate(c1);
							}
						}
					}
				}

				if (list2 != null && !list2.isEmpty()) {
					for (PhysicalGrowthCount c2 : list2) {
						if (StringUtils.equals(cnt.getGradeId(), c2.getGradeId())) {
							if (StringUtils.equals(c2.getType(), "H/A")) {
								cnt.setHaCount(c2);
							} else if (StringUtils.equals(c2.getType(), "W/A")) {
								cnt.setWaCount(c2);
							} else if (StringUtils.equals(c2.getType(), "W/H")) {
								cnt.setWhCount(c2);
							}
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<MedicalExaminationGradeCount> getMedicalExaminationGradeCountsByQueryCriteria(int start, int pageSize,
			MedicalExaminationGradeCountQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<MedicalExaminationGradeCount> rows = sqlSessionTemplate.selectList("getMedicalExaminationGradeCounts",
				query, rowBounds);
		return rows;
	}

	public List<MedicalExaminationGradeCount> list(MedicalExaminationGradeCountQuery query) {
		List<MedicalExaminationGradeCount> list = medicalExaminationGradeCountMapper
				.getMedicalExaminationGradeCounts(query);
		return list;
	}

	@Transactional
	public void saveAll(String tenantId, String type, int year, int month,
			Collection<MedicalExaminationGradeCount> list) {
		MedicalExaminationGradeCountQuery query = new MedicalExaminationGradeCountQuery();
		query.tenantId(tenantId);
		query.type(type);
		query.year(year);
		query.month(month);
		medicalExaminationGradeCountMapper.deleteMedicalExaminationGradeCount(query);

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
		for (MedicalExaminationGradeCount model : list) {
			model.setAreaId(tenant.getAreaId());
			model.setCityId(tenant.getCityId());
			model.setProvinceId(tenant.getProvinceId());
		}

		this.bulkInsert(list);
	}

	@Transactional
	public void saveAll(String tenantId, String checkId, List<MedicalExaminationGradeCount> list) {
		growthRateCountService.deleteByCheckId(checkId);
		physicalGrowthCountService.deleteByCheckId(checkId);
		medicalExaminationGradeCountMapper.deleteMedicalExaminationGradeCountByCheckId(checkId);
		if (list != null && !list.isEmpty()) {
			List<GrowthRateCount> rows1 = new ArrayList<GrowthRateCount>();
			List<PhysicalGrowthCount> rows2 = new ArrayList<PhysicalGrowthCount>();

			for (MedicalExaminationGradeCount cnt : list) {
				cnt.setCheckId(checkId);
				cnt.setTenantId(tenantId);
				if (cnt.getBothRate() != null) {
					cnt.getBothRate().setCheckId(checkId);
					cnt.getBothRate().setTenantId(tenantId);
					cnt.getBothRate().setType("both");
					rows1.add(cnt.getBothRate());
				}
				if (cnt.getHeightRate() != null) {
					cnt.getHeightRate().setCheckId(checkId);
					cnt.getHeightRate().setTenantId(tenantId);
					cnt.getHeightRate().setType("height");
					rows1.add(cnt.getHeightRate());
				}
				if (cnt.getWeightRate() != null) {
					cnt.getWeightRate().setCheckId(checkId);
					cnt.getWeightRate().setTenantId(tenantId);
					cnt.getWeightRate().setType("weight");
					rows1.add(cnt.getWeightRate());
				}
				if (cnt.getHaCount() != null) {
					cnt.getHaCount().setCheckId(checkId);
					cnt.getHaCount().setTenantId(tenantId);
					cnt.getHaCount().setType("H/A");
					rows2.add(cnt.getHaCount());
				}
				if (cnt.getWaCount() != null) {
					cnt.getWaCount().setCheckId(checkId);
					cnt.getWaCount().setTenantId(tenantId);
					cnt.getWaCount().setType("W/A");
					rows2.add(cnt.getWaCount());
				}
				if (cnt.getWhCount() != null) {
					cnt.getWhCount().setCheckId(checkId);
					cnt.getWhCount().setTenantId(tenantId);
					cnt.getWhCount().setType("W/H");
					rows2.add(cnt.getWhCount());
				}
			}

			growthRateCountService.bulkInsert(rows1);
			physicalGrowthCountService.bulkInsert(rows2);

			SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
			for (MedicalExaminationGradeCount model : list) {
				model.setAreaId(tenant.getAreaId());
				model.setCityId(tenant.getCityId());
				model.setProvinceId(tenant.getProvinceId());
			}

			this.bulkInsert(list);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.growthRateCountService")
	public void setGrowthRateCountService(GrowthRateCountService growthRateCountService) {
		this.growthRateCountService = growthRateCountService;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.MedicalExaminationGradeCountMapper")
	public void setMedicalExaminationGradeCountMapper(
			MedicalExaminationGradeCountMapper medicalExaminationGradeCountMapper) {
		this.medicalExaminationGradeCountMapper = medicalExaminationGradeCountMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.physicalGrowthCountService")
	public void setPhysicalGrowthCountService(PhysicalGrowthCountService physicalGrowthCountService) {
		this.physicalGrowthCountService = physicalGrowthCountService;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

}
