package com.jeesite.modules.data_collect.danger.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.danger.entity.DangerCargoDeclaration;

/**
 * 危险货物申报表DAO接口
 * @author 王浩宇
 * @version 2025-06-08
 */
@MyBatisDao
public interface DangerCargoDeclarationDao extends CrudDao<DangerCargoDeclaration> {
	
}