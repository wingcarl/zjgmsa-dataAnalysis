package com.jeesite.modules.data_collect.government.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.government.entity.GovernmentData;

/**
 * 政务服务数据DAO接口
 * @author 王浩宇
 * @version 2025-04-10
 */
@MyBatisDao
public interface GovernmentDataDao extends CrudDao<GovernmentData> {
	
}