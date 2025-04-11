package com.jeesite.modules.data_collect.largeship.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.largeship.entity.LargeRestrictedVessels;

/**
 * 大型受限船舶DAO接口
 * @author 王浩宇
 * @version 2025-04-11
 */
@MyBatisDao
public interface LargeRestrictedVesselsDao extends CrudDao<LargeRestrictedVessels> {
	
}