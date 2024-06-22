package com.jeesite.modules.data_collect.weekly.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReport;

/**
 * 周工作数据表DAO接口
 * @author 王浩宇
 * @version 2024-06-22
 */
@MyBatisDao
public interface WeeklyReportDao extends CrudDao<WeeklyReport> {
	
}