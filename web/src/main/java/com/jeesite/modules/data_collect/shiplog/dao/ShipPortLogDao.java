package com.jeesite.modules.data_collect.shiplog.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.shiplog.entity.ShipPortLog;

/**
 * 国际航行船舶表DAO接口
 * @author 王浩宇
 * @version 2025-05-26
 */
@MyBatisDao
public interface ShipPortLogDao extends CrudDao<ShipPortLog> {
	
}