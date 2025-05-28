package com.jeesite.modules.data_collect.punish.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.punish.entity.PunishJudge;
import java.util.List;
import java.util.Map;

/**
 * 处罚决定DAO接口
 * @author 王浩宇
 * @version 2025-02-07
 */
@MyBatisDao
public interface PunishJudgeDao extends CrudDao<PunishJudge> {
	
	/**
	 * 获取所有违法情节选项
	 */
	List<String> findViolationCircumstances();
	
	/**
	 * 获取所有船舶种类选项
	 */
	List<String> findShipTypes();
	
	/**
	 * 获取所有处罚机构选项
	 */
	List<String> findPenaltyAgencies();
	
	/**
	 * 获取所有案由选项
	 */
	List<String> findCaseReasons();
	
	/**
	 * 获取所有业务类型选项
	 */
	List<String> findManagementCategories();
	
	/**
	 * 获取所有部门列表（通过agency_dept表）
	 */
	List<String> findAllDepartments();
	
	/**
	 * 按部门统计处罚数量（通过agency_dept表关联）
	 */
	List<Map<String, Object>> findPenaltyCountsByDepartment(PunishJudge punishJudge);
	
	/**
	 * 按部门统计处罚金额（通过agency_dept表关联）
	 */
	List<Map<String, Object>> findPenaltyAmountsByDepartment(PunishJudge punishJudge);
	
	/**
	 * 按部门统计平均处罚金额（通过agency_dept表关联）
	 */
	List<Map<String, Object>> findAveragePenaltyAmountsByDepartment(PunishJudge punishJudge);
	
	/**
	 * 按部门统计重点违法行为占比（通过agency_dept和case_key表关联）
	 */
	List<Map<String, Object>> findKeyViolationRatioByDepartment(PunishJudge punishJudge);
	
	/**
	 * 获取案由统计数据（支持部门筛选）
	 */
	List<Map<String, Object>> findCaseReasonStatistics(PunishJudge punishJudge);
	
	/**
	 * 获取管理分类统计数据（支持部门筛选）
	 */
	List<Map<String, Object>> findManagementCategoryStatistics(PunishJudge punishJudge);
	
	/**
	 * 获取违法情节统计数据（支持部门筛选）
	 */
	List<Map<String, Object>> findViolationCircumstancesStatistics(PunishJudge punishJudge);
	
	/**
	 * 获取当事人类型统计数据（支持部门筛选）
	 */
	List<Map<String, Object>> findPartyTypeStatistics(PunishJudge punishJudge);
	
	/**
	 * 获取船籍港统计数据（支持部门筛选）
	 */
	List<Map<String, Object>> findPortRegistryStatistics(PunishJudge punishJudge);
}