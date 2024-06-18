package com.jeesite.modules.data_collect.weeklydata.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.weeklydata.entity.DataMetrics;

/**
 * 数据指标表DAO接口
 * @author 王浩宇
 * @version 2024-06-18
 */
@MyBatisDao
public interface DataMetricsDao extends CrudDao<DataMetrics> {
	
}