package com.jeesite.modules.data_collect.misdata.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.misdata.entity.MonthlyMiscellaneousData;

/**
 * 月度杂项数据表DAO接口
 * @author 王浩宇
 * @version 2025-05-30
 */
@MyBatisDao
public interface MonthlyMiscellaneousDataDao extends CrudDao<MonthlyMiscellaneousData> {
	
}