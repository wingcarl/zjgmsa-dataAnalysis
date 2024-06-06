package com.jeesite.modules.data_collect.psc.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.psc.entity.PscInspection;

/**
 * PSC Inspection TableDAO接口
 * @author Dylan Wang
 * @version 2024-06-06
 */
@MyBatisDao
public interface PscInspectionDao extends CrudDao<PscInspection> {
	
}