package com.jeesite.modules.data_collect.weekly.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReportDangerDefense;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 危防数据统计DAO接口
 */
@MyBatisDao
public interface WeeklyReportDangerDefenseDao extends CrudDao<WeeklyReportDangerDefense> {
    
    /**
     * 获取部门列表
     */
    List<String> getCategories();

    /**
     * 获取危防数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    List<WeeklyReportDangerDefense> getDangerDefenseDataByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取危险品数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    List<WeeklyReportDangerDefense> getDangerousGoodsDataByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取防污染数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    List<WeeklyReportDangerDefense> getPollutionDataByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取危防案由数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    List<Map<String, Object>> getDangerDefenseCaseReasonData(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取危险品案由数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    List<Map<String, Object>> getDangerousGoodsCaseReasonData(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取防污染案由数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    List<Map<String, Object>> getPollutionCaseReasonData(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取危防类处罚总数
     */
    int getDangerDefenseCount(@Param("date") String date);

    /**
     * 获取危险品检查总数
     */
    int getDangerousGoodsCount(@Param("date") String date);

    /**
     * 获取防污染检查总数
     */
    int getPollutionCount(@Param("date") String date);
} 