package com.jeesite.modules.data_collect.shiplog.dao;

import java.util.Map;
import java.util.List;
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
	
	/**
	 * 获取时间趋势数据
	 * @param params 参数Map，包含startDate、endDate、timeInterval等
	 * @return 时间趋势数据列表
	 */
	List<Map<String, Object>> getTrendData(Map<String, Object> params);
	
	/**
	 * 获取各码头船舶数量统计
	 * @param params 参数Map，包含startDate、endDate等筛选条件
	 * @return 码头统计数据列表
	 */
	List<Map<String, Object>> getBerthingLocationStats(Map<String, Object> params);
	
	/**
	 * 获取各码头装卸货量统计
	 * @param params 参数Map，包含startDate、endDate等筛选条件
	 * @return 码头货量统计数据列表
	 */
	List<Map<String, Object>> getBerthingLocationCargoStats(Map<String, Object> params);
	
	/**
	 * 获取船舶类型统计
	 * @param params 参数Map，包含startDate、endDate等筛选条件
	 * @return 船舶类型统计数据列表
	 */
	List<Map<String, Object>> getShipCategoryStats(Map<String, Object> params);
	
	/**
	 * 获取所有码头列表（去重并处理）
	 * @return 码头列表
	 */
	List<String> getBerthingLocationList();
	
	/**
	 * 获取所有船舶类型列表（去重）
	 * @return 船舶类型列表
	 */
	List<String> getShipCategoryList();
	
	/**
	 * 获取来往港口统计（数量大于2的）
	 * @param params 参数Map，包含startDate、endDate等筛选条件
	 * @return 来往港口统计数据列表
	 */
	List<Map<String, Object>> getPreviousNextPortStats(Map<String, Object> params);
	
	/**
	 * 获取来往港口装卸货量统计
	 * @param params 参数Map，包含startDate、endDate等筛选条件
	 * @return 来往港口货量统计数据列表
	 */
	List<Map<String, Object>> getPreviousNextPortCargoStats(Map<String, Object> params);
	
	/**
	 * 获取船籍港统计（数量大于等于2的）
	 * @param params 参数Map，包含startDate、endDate等筛选条件
	 * @return 船籍港统计数据列表
	 */
	List<Map<String, Object>> getPortOfRegistryStats(Map<String, Object> params);
	
	/**
	 * 获取船舶所有人统计（数量大于等于2的）
	 * @param params 参数Map，包含startDate、endDate等筛选条件
	 * @return 船舶所有人统计数据列表
	 */
	List<Map<String, Object>> getShipOwnerStats(Map<String, Object> params);
	
	/**
	 * 获取船舶经营人统计（数量大于等于2的）
	 * @param params 参数Map，包含startDate、endDate等筛选条件
	 * @return 船舶经营人统计数据列表
	 */
	List<Map<String, Object>> getShipOperatorStats(Map<String, Object> params);
	
}