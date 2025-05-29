package com.jeesite.modules.data_collect.weekly.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 周工作数据表DAO接口
 * @author 王浩宇
 * @version 2024-06-22
 */
@MyBatisDao
public interface WeeklyReportDao extends CrudDao<WeeklyReport> {
	
	/**
	 * 获取所有部门列表（通过agency_dept表）
	 */
	List<String> findAllDepartments();
	
	/**
	 * 按部门统计船舶检查数据（通过agency_dept表关联）
	 */
	List<Map<String, Object>> findShipInspectionStatisticsByDepartment(
		@Param("startDate") String startDate,
		@Param("endDate") String endDate,
		@Param("department") String department
	);
	
	/**
	 * 按部门统计现场监督数据（通过agency_dept表关联）
	 */
	List<Map<String, Object>> findOnSiteInspectionStatisticsByDepartment(
		@Param("startDate") String startDate,
		@Param("endDate") String endDate,
		@Param("department") String department
	);
}