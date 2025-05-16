package com.jeesite.modules.data_collect.punish.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.punish.entity.PunishJudge;
import java.util.List;

/**
 * 处罚决定DAO接口
 * @author 王浩宇
 * @version 2025-02-07
 */
@MyBatisDao
public interface PunishJudgeDao extends CrudDao<PunishJudge> {
	
	/**
	 * 获取所有违法情节选项
	 */
	List<String> findViolationCircumstances();
	
	/**
	 * 获取所有船舶种类选项
	 */
	List<String> findShipTypes();
	
	/**
	 * 获取所有处罚机构选项
	 */
	List<String> findPenaltyAgencies();
	
	/**
	 * 获取所有案由选项
	 */
	List<String> findCaseReasons();
	
	/**
	 * 获取所有业务类型选项
	 */
	List<String> findManagementCategories();
}