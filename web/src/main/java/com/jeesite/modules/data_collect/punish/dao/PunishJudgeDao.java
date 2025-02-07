package com.jeesite.modules.data_collect.punish.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.punish.entity.PunishJudge;

/**
 * 处罚决定DAO接口
 * @author 王浩宇
 * @version 2025-02-07
 */
@MyBatisDao
public interface PunishJudgeDao extends CrudDao<PunishJudge> {
	
}