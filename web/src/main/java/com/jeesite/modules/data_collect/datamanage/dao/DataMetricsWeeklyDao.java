package com.jeesite.modules.data_collect.datamanage.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.datamanage.entity.DataMetricsWeekly;

/**
 * 数据共享清单DAO接口
 * @author Dylan Wang
 * @version 2024-06-21
 */
@MyBatisDao
public interface DataMetricsWeeklyDao extends CrudDao<DataMetricsWeekly> {
	
}