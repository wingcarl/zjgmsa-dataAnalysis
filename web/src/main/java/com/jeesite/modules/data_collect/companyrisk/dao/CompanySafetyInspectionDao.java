package com.jeesite.modules.data_collect.companyrisk.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.companyrisk.entity.CompanySafetyInspection;

/**
 * 安全隐患与风险排查记录表DAO接口
 * @author 王浩宇
 * @version 2025-05-26
 */
@MyBatisDao
public interface CompanySafetyInspectionDao extends CrudDao<CompanySafetyInspection> {
	
}