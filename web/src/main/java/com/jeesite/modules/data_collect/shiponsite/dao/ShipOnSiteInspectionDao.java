package com.jeesite.modules.data_collect.shiponsite.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.shiponsite.entity.ShipOnSiteInspection;

import java.util.List;

/**
 * 船舶现场监督检查表DAO接口
 * @author 王浩宇
 * @version 2024-06-06
 */
@MyBatisDao
public interface ShipOnSiteInspectionDao extends CrudDao<ShipOnSiteInspection> {

    List<ShipOnSiteInspection> findDistinctList(ShipOnSiteInspection query);
}