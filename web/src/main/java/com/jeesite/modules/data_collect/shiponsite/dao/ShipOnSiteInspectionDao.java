package com.jeesite.modules.data_collect.shiponsite.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.shiponsite.entity.ShipOnSiteInspection;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 船舶现场监督检查表DAO接口
 * @author 王浩宇
 * @version 2024-06-06
 */
@MyBatisDao
public interface ShipOnSiteInspectionDao extends CrudDao<ShipOnSiteInspection> {

    List<ShipOnSiteInspection> findDistinctList(ShipOnSiteInspection query);
    
    /**
     * 按部门统计现场监督数据（通过agency_dept表关联）
     */
    List<Map<String, Object>> findOnSiteInspectionStatisticsByDepartment(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("department") String department
    );
}