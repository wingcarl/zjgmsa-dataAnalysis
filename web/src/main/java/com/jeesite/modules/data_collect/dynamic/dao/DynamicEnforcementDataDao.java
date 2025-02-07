package com.jeesite.modules.data_collect.dynamic.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.dynamic.entity.DynamicEnforcementData;

/**
 * 动态执法数据管理DAO接口
 * @author 王浩宇
 * @version 2025-02-07
 */
@MyBatisDao
public interface DynamicEnforcementDataDao extends CrudDao<DynamicEnforcementData> {
	
}