package com.jeesite.modules.data_collect.shiplog.dao;

import java.util.Map;
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
	
	/**
	 * 获取数据分析统计数据
	 * @param params 参数Map，包含startDate和endDate
	 * @return 统计数据
	 */
	Map<String, Object> getAnalysisData(Map<String, Object> params);
	
}