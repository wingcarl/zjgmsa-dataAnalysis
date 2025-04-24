package com.jeesite.modules.shipreport.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.shipreport.entity.ShipReport;
import java.util.List;
import java.util.Map;

/**
 * 船舶报告表DAO接口
 * @author 王浩宇
 * @version 2024-06-21
 */
@MyBatisDao
public interface ShipReportDao extends CrudDao<ShipReport> {
	
	/**
	 * 获取船舶类型分布数据
	 */
	List<Map<String, Object>> getShipTypeDistribution(Map<String, Object> params);
	
	/**
	 * 获取货物类型分布数据
	 */
	List<Map<String, Object>> getCargoTypeDistribution(Map<String, Object> params);
}