package com.jeesite.modules.data_collect.ship.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.ship.entity.ShipInspection;

/**
 * 海船安全检查信息表DAO接口
 * @author 王浩宇
 * @version 2024-06-06
 */
@MyBatisDao
public interface ShipInspectionDao extends CrudDao<ShipInspection> {
	
}