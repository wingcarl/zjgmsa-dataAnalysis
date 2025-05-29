package com.jeesite.modules.data_collect.ship.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.ship.entity.ShipInspection;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 海船安全检查信息表DAO接口
 * @author 王浩宇
 * @version 2024-06-06
 */
@MyBatisDao
public interface ShipInspectionDao extends CrudDao<ShipInspection> {

    List<ShipInspection> findDistinctList(ShipInspection shipInspection);
    
    /**
     * 按部门统计船舶检查数据（通过agency_dept表关联）
     */
    List<Map<String, Object>> findShipInspectionStatisticsByDepartment(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("department") String department
    );
}