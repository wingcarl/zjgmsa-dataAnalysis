package com.jeesite.modules.data_collect.weekly.service;

import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.weekly.dao.WeeklyReportDangerDefenseDao;
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReportDangerDefense;
import java.util.List;
import java.util.Map;

/**
 * 危防数据统计Service接口
 */
public interface WeeklyReportDangerDefenseService {

    /**
     * 获取部门列表
     */
    List<String> getCategories();

    /**
     * 获取危防数据
     */
    List<WeeklyReportDangerDefense> getDangerDefenseDataByDate(String startDate, String endDate);

    /**
     * 获取危险品数据
     */
    List<WeeklyReportDangerDefense> getDangerousGoodsDataByDate(String startDate, String endDate);

    /**
     * 获取防污染数据
     */
    List<WeeklyReportDangerDefense> getPollutionDataByDate(String startDate, String endDate);

    /**
     * 获取危防案由数据
     */
    List<Map<String, Object>> getDangerDefenseCaseReasonData(String startDate, String endDate);

    /**
     * 获取危险品案由数据
     */
    List<Map<String, Object>> getDangerousGoodsCaseReasonData(String startDate, String endDate);

    /**
     * 获取防污染案由数据
     */
    List<Map<String, Object>> getPollutionCaseReasonData(String startDate, String endDate);
    
    /**
     * 获取指定机构的危险品检查详细数据
     */
    List<Map<String, Object>> getDangerousGoodsDetailData(String agency, String startDate, String endDate);
    
    /**
     * 获取指定机构的防污染检查详细数据
     */
    List<Map<String, Object>> getPollutionDetailData(String agency, String startDate, String endDate);
    
    /**
     * 获取指定机构的危防类处罚详细数据
     */
    List<Map<String, Object>> getDangerDefenseDetailData(String agency, String startDate, String endDate);
} 