package com.jeesite.modules.data_collect.companyrisk.dao;

import java.util.List;
import java.util.Map;
import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.companyrisk.entity.CompanySafetyInspection;

/**
 * 安全隐患与风险排查记录表DAO接口
 * @author 王浩宇
 * @version 2025-05-26
 */
@MyBatisDao
public interface CompanySafetyInspectionDao extends CrudDao<CompanySafetyInspection> {
	
	/**
	 * 获取风险隐患统计数据
	 */
	Map<String, Object> getStatisticsData(Map<String, Object> params);
	
	/**
	 * 获取企业自查统计数据（按部门）
	 */
	List<Map<String, Object>> getCompanySelfCheckStats(Map<String, Object> params);
	
	/**
	 * 获取海事排查统计数据（按部门）
	 */
	List<Map<String, Object>> getMaritimeInspectionStats(Map<String, Object> params);
	
	/**
	 * 获取综合统计数据（按企业）
	 */
	List<Map<String, Object>> getComprehensiveStats(Map<String, Object> params);
	
}