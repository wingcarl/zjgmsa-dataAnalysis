package com.jeesite.modules.data_collect.intelligent.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.intelligent.entity.IntelligentMonitoringAlarm;

/**
 * 智能卡口预警记录表DAO接口
 * @author 王浩宇
 * @version 2025-06-05
 */
@MyBatisDao
public interface IntelligentMonitoringAlarmDao extends CrudDao<IntelligentMonitoringAlarm> {
	
}