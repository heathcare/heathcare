package com.glaf.heathcare.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;

@Transactional(readOnly = true)
public interface MedicalExaminationGradeCountService {

	/**
	 * 根据checkId删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByCheckId(String checkId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getMedicalExaminationGradeCountCountByQueryCriteria(MedicalExaminationGradeCountQuery query);

	/**
	 * 获取某个学校某次体检的汇总结果
	 * 
	 * @param tenantId
	 * @param checkId
	 * @return
	 */
	List<MedicalExaminationGradeCount> getMedicalExaminationGradeCountList(String tenantId, String checkId);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<MedicalExaminationGradeCount> getMedicalExaminationGradeCountsByQueryCriteria(int start, int pageSize,
			MedicalExaminationGradeCountQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<MedicalExaminationGradeCount> list(MedicalExaminationGradeCountQuery query);

	@Transactional
	void saveAll(String tenantId, String type, int year, int month, Collection<MedicalExaminationGradeCount> list);
	
	@Transactional
	void saveAll(String tenantId, String checkId, List<MedicalExaminationGradeCount> list);

}
