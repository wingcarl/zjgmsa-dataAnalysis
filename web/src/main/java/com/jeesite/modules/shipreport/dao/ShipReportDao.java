package com.jeesite.modules.shipreport.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.shipreport.entity.ShipReport;

/**
 * 船舶报告表DAO接口
 * @author 王浩宇
 * @version 2024-06-21
 */
@MyBatisDao
public interface ShipReportDao extends CrudDao<ShipReport> {
	
}