package com.jeesite.modules.data_collect.weekly.service;

import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.weekly.dao.WeeklyReportDangerDefenseDao;
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReportDangerDefense;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 危防数据统计Service实现
 */
@Service
@Transactional(readOnly = true)
public class WeeklyReportDangerDefenseServiceImpl extends CrudService<WeeklyReportDangerDefenseDao, WeeklyReportDangerDefense> implements WeeklyReportDangerDefenseService {

    public Map<String, Object> getWeeklyDangerDefenseChartData(String currentWeekStartDate, String lastWeekStartDate) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取指标数据
        Map<String, Object> indicatorData = getIndicatorData(currentWeekStartDate, lastWeekStartDate);
        result.put("indicatorData", indicatorData);
        
        // 获取部门数据
        List<String> categories = getCategories();
        result.put("categories", categories);
        
        // 获取危防类处罚数据
        Map<String, Object> dangerDefenseData = new HashMap<>();
        dangerDefenseData.put("current", convertToMapList(getDangerDefenseDataByDate(currentWeekStartDate, currentWeekStartDate)));
        dangerDefenseData.put("last", convertToMapList(getDangerDefenseDataByDate(lastWeekStartDate, lastWeekStartDate)));
        result.put("dangerDefenseData", dangerDefenseData);
        
        // 获取危险品检查数据
        Map<String, Object> dangerousGoodsData = new HashMap<>();
        dangerousGoodsData.put("current", convertToMapList(getDangerousGoodsDataByDate(currentWeekStartDate, currentWeekStartDate)));
        dangerousGoodsData.put("last", convertToMapList(getDangerousGoodsDataByDate(lastWeekStartDate, lastWeekStartDate)));
        result.put("dangerousGoodsData", dangerousGoodsData);
        
        // 获取防污染检查数据
        Map<String, Object> pollutionData = new HashMap<>();
        pollutionData.put("current", convertToMapList(getPollutionDataByDate(currentWeekStartDate, currentWeekStartDate)));
        pollutionData.put("last", convertToMapList(getPollutionDataByDate(lastWeekStartDate, lastWeekStartDate)));
        result.put("pollutionData", pollutionData);
        
        // 获取案由对比数据
        Map<String, Object> caseReasonData = new HashMap<>();
        caseReasonData.put("dangerDefense", getDangerDefenseCaseReasonData(currentWeekStartDate, currentWeekStartDate));
        caseReasonData.put("dangerousGoods", getDangerousGoodsCaseReasonData(currentWeekStartDate, currentWeekStartDate));
        caseReasonData.put("pollution", getPollutionCaseReasonData(currentWeekStartDate, currentWeekStartDate));
        result.put("caseReasonData", caseReasonData);
        
        return result;
    }

    public Map<String, Object> fetchDangerDefenseData(String currentWeekStartDate, String lastWeekStartDate) {
        return getWeeklyDangerDefenseChartData(currentWeekStartDate, lastWeekStartDate);
    }

    @Override
    public List<String> getCategories() {
        return dao.getCategories();
    }

    @Override
    public List<WeeklyReportDangerDefense> getDangerDefenseDataByDate(String startDate, String endDate) {
        return dao.getDangerDefenseDataByDate(startDate, endDate);
    }

    @Override
    public List<WeeklyReportDangerDefense> getDangerousGoodsDataByDate(String startDate, String endDate) {
        return dao.getDangerousGoodsDataByDate(startDate, endDate);
    }

    @Override
    public List<WeeklyReportDangerDefense> getPollutionDataByDate(String startDate, String endDate) {
        return dao.getPollutionDataByDate(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getDangerDefenseCaseReasonData(String startDate, String endDate) {
        return dao.getDangerDefenseCaseReasonData(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getDangerousGoodsCaseReasonData(String startDate, String endDate) {
        return dao.getDangerousGoodsCaseReasonData(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getPollutionCaseReasonData(String startDate, String endDate) {
        return dao.getPollutionCaseReasonData(startDate, endDate);
    }

    private Map<String, Object> getIndicatorData(String currentWeekStartDate, String lastWeekStartDate) {
        Map<String, Object> indicatorData = new HashMap<>();
        
        // 危防类处罚数据
        Map<String, Object> dangerDefense = new HashMap<>();
        int currentDangerDefenseCount = sumDangerDefenseCount(getDangerDefenseDataByDate(currentWeekStartDate, currentWeekStartDate));
        int lastDangerDefenseCount = sumDangerDefenseCount(getDangerDefenseDataByDate(lastWeekStartDate, lastWeekStartDate));
        dangerDefense.put("value", currentDangerDefenseCount);
        dangerDefense.put("lastValue", lastDangerDefenseCount);
        dangerDefense.put("rate", calculateRate(lastDangerDefenseCount, currentDangerDefenseCount));
        indicatorData.put("dangerDefense", dangerDefense);
        
        // 危险品检查数据
        Map<String, Object> dangerousGoods = new HashMap<>();
        int currentDangerousGoodsCount = sumDangerousGoodsCount(getDangerousGoodsDataByDate(currentWeekStartDate, currentWeekStartDate));
        int lastDangerousGoodsCount = sumDangerousGoodsCount(getDangerousGoodsDataByDate(lastWeekStartDate, lastWeekStartDate));
        dangerousGoods.put("value", currentDangerousGoodsCount);
        dangerousGoods.put("lastValue", lastDangerousGoodsCount);
        dangerousGoods.put("rate", calculateRate(lastDangerousGoodsCount, currentDangerousGoodsCount));
        indicatorData.put("dangerousGoods", dangerousGoods);
        
        // 防污染检查数据
        Map<String, Object> pollution = new HashMap<>();
        int currentPollutionCount = sumPollutionCount(getPollutionDataByDate(currentWeekStartDate, currentWeekStartDate));
        int lastPollutionCount = sumPollutionCount(getPollutionDataByDate(lastWeekStartDate, lastWeekStartDate));
        pollution.put("value", currentPollutionCount);
        pollution.put("lastValue", lastPollutionCount);
        pollution.put("rate", calculateRate(lastPollutionCount, currentPollutionCount));
        indicatorData.put("pollution", pollution);
        
        return indicatorData;
    }

    private List<Map<String, Object>> convertToMapList(List<WeeklyReportDangerDefense> dataList) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (WeeklyReportDangerDefense data : dataList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", data.getAgencyName());
            map.put("value", data.getDangerDefenseCount());
            result.add(map);
        }
        return result;
    }

    private int sumDangerDefenseCount(List<WeeklyReportDangerDefense> dataList) {
        return dataList.stream().mapToInt(WeeklyReportDangerDefense::getDangerDefenseCount).sum();
    }

    private int sumDangerousGoodsCount(List<WeeklyReportDangerDefense> dataList) {
        return dataList.stream().mapToInt(WeeklyReportDangerDefense::getDangerousGoodsCount).sum();
    }

    private int sumPollutionCount(List<WeeklyReportDangerDefense> dataList) {
        return dataList.stream().mapToInt(WeeklyReportDangerDefense::getPollutionCount).sum();
    }

    private double calculateRate(int lastValue, int currentValue) {
        if (lastValue == 0) {
            return currentValue > 0 ? 100 : 0;
        }
        return ((double) (currentValue - lastValue) / lastValue) * 100;
    }
} 