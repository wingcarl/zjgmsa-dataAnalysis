package com.jeesite.modules.data_collect.weekly.controller;

import com.jeesite.common.web.BaseController;
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReportDangerDefense;
import com.jeesite.modules.data_collect.weekly.service.WeeklyReportDangerDefenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 危防数据统计Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/weekly/weeklyReportDanger")
public class WeeklyReportDangerDefenseController extends BaseController {

    @Autowired
    private WeeklyReportDangerDefenseService weeklyReportDangerDefenseService;

    /**
     * 危防数据图表页面
     */
    @RequestMapping(value = "dangerDefenseChart")
    public String dangerDefenseChart() {
        return "data_collect/weekly/weeklyReportDangerDefenseChart";
    }

    /**
     * 获取危防数据图表数据
     */
    @RequestMapping(value = "weeklyDangerDefenseChartData")
    @ResponseBody
    public Map<String, Object> weeklyDangerDefenseChartData(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 计算上周同期时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            
            // 计算日期差，确定取上周同期的日期
            long diff = end.getTime() - start.getTime();
            long days = diff / (24 * 60 * 60 * 1000) + 1; // 包括开始和结束日期
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.add(Calendar.DAY_OF_YEAR, -7); // 上周同期开始日期
            String lastWeekStartDate = sdf.format(cal.getTime());
            
            cal.setTime(end);
            cal.add(Calendar.DAY_OF_YEAR, -7); // 上周同期结束日期
            String lastWeekEndDate = sdf.format(cal.getTime());
            
            // 获取部门列表
            List<String> categories = weeklyReportDangerDefenseService.getCategories();
            result.put("categories", categories);
            
            // 获取本周和上周的危防数据
            List<WeeklyReportDangerDefense> currentDangerDefenseList = weeklyReportDangerDefenseService.getDangerDefenseDataByDate(startDate, endDate);
            List<WeeklyReportDangerDefense> lastDangerDefenseList = weeklyReportDangerDefenseService.getDangerDefenseDataByDate(lastWeekStartDate, lastWeekEndDate);
            
            // 构造危防数据
            Map<String, Object> dangerDefenseData = new HashMap<>();
            dangerDefenseData.put("current", extractValuesByAgency(currentDangerDefenseList, categories, "dangerDefense"));
            dangerDefenseData.put("last", extractValuesByAgency(lastDangerDefenseList, categories, "dangerDefense"));
            result.put("dangerDefenseData", dangerDefenseData);
            
            // 获取本周和上周的危险品数据
            List<WeeklyReportDangerDefense> currentDangerousGoodsList = weeklyReportDangerDefenseService.getDangerousGoodsDataByDate(startDate, endDate);
            List<WeeklyReportDangerDefense> lastDangerousGoodsList = weeklyReportDangerDefenseService.getDangerousGoodsDataByDate(lastWeekStartDate, lastWeekEndDate);
            
            // 构造危险品数据
            Map<String, Object> dangerousGoodsData = new HashMap<>();
            dangerousGoodsData.put("current", extractValuesByAgency(currentDangerousGoodsList, categories, "dangerousGoods"));
            dangerousGoodsData.put("last", extractValuesByAgency(lastDangerousGoodsList, categories, "dangerousGoods"));
            result.put("dangerousGoodsData", dangerousGoodsData);
            
            // 获取本周和上周的防污染数据
            List<WeeklyReportDangerDefense> currentPollutionList = weeklyReportDangerDefenseService.getPollutionDataByDate(startDate, endDate);
            List<WeeklyReportDangerDefense> lastPollutionList = weeklyReportDangerDefenseService.getPollutionDataByDate(lastWeekStartDate, lastWeekEndDate);
            
            // 构造防污染数据
            Map<String, Object> pollutionData = new HashMap<>();
            pollutionData.put("current", extractValuesByAgency(currentPollutionList, categories, "pollution"));
            pollutionData.put("last", extractValuesByAgency(lastPollutionList, categories, "pollution"));
            result.put("pollutionData", pollutionData);
            
            // 获取案由数据
            Map<String, Object> caseReasonData = new HashMap<>();
            caseReasonData.put("dangerDefense", weeklyReportDangerDefenseService.getDangerDefenseCaseReasonData(startDate, endDate));
            caseReasonData.put("dangerousGoods", weeklyReportDangerDefenseService.getDangerousGoodsCaseReasonData(startDate, endDate));
            caseReasonData.put("pollution", weeklyReportDangerDefenseService.getPollutionCaseReasonData(startDate, endDate));
            result.put("caseReasonData", caseReasonData);
            
            // 计算指标数据
            Map<String, Object> indicatorData = calculateIndicatorData(
                currentDangerDefenseList, lastDangerDefenseList,
                currentDangerousGoodsList, lastDangerousGoodsList,
                currentPollutionList, lastPollutionList
            );
            result.put("indicatorData", indicatorData);
            
        } catch (ParseException e) {
            logger.error("日期解析错误", e);
        }
        
        return result;
    }

    /**
     * 获取危防数据
     */
    @RequestMapping(value = "fetchDangerDefenseData")
    @ResponseBody
    public Map<String, Object> fetchDangerDefenseData(String startDate, String endDate) {
        return weeklyDangerDefenseChartData(startDate, endDate);
    }
    
    /**
     * 获取指定机构的危险品检查详细数据
     */
    @RequestMapping(value = "getDangerousGoodsDetailData")
    @ResponseBody
    public List<Map<String, Object>> getDangerousGoodsDetailData(String agency, String startDate, String endDate) {
        return weeklyReportDangerDefenseService.getDangerousGoodsDetailData(agency, startDate, endDate);
    }
    
    /**
     * 获取指定机构的防污染检查详细数据
     */
    @RequestMapping(value = "getPollutionDetailData")
    @ResponseBody
    public List<Map<String, Object>> getPollutionDetailData(String agency, String startDate, String endDate) {
        return weeklyReportDangerDefenseService.getPollutionDetailData(agency, startDate, endDate);
    }
    
    /**
     * 获取指定机构的危防类处罚详细数据
     */
    @RequestMapping(value = "getDangerDefenseDetailData")
    @ResponseBody
    public List<Map<String, Object>> getDangerDefenseDetailData(String agency, String startDate, String endDate) {
        return weeklyReportDangerDefenseService.getDangerDefenseDetailData(agency, startDate, endDate);
    }
    
    /**
     * 根据机构提取对应的指标值
     */
    private List<Integer> extractValuesByAgency(List<WeeklyReportDangerDefense> dataList, List<String> categories, String type) {
        List<Integer> result = new ArrayList<>();
        Map<String, Integer> agencyMap = new HashMap<>();
        
        // 将数据转换为机构名称到数据的映射
        for (WeeklyReportDangerDefense data : dataList) {
            String agency = data.getAgencyName();
            int value = 0;
            
            if ("dangerDefense".equals(type)) {
                value = data.getDangerDefenseCount();
            } else if ("dangerousGoods".equals(type)) {
                value = data.getDangerousGoodsCount();
            } else if ("pollution".equals(type)) {
                value = data.getPollutionCount();
            }
            
            agencyMap.put(agency, value);
        }
        
        // 按照categories的顺序提取数据
        for (String agency : categories) {
            result.add(agencyMap.getOrDefault(agency, 0));
        }
        
        return result;
    }
    
    /**
     * 计算指标数据
     */
    private Map<String, Object> calculateIndicatorData(
            List<WeeklyReportDangerDefense> currentDangerDefenseList, List<WeeklyReportDangerDefense> lastDangerDefenseList,
            List<WeeklyReportDangerDefense> currentDangerousGoodsList, List<WeeklyReportDangerDefense> lastDangerousGoodsList,
            List<WeeklyReportDangerDefense> currentPollutionList, List<WeeklyReportDangerDefense> lastPollutionList) {
        
        Map<String, Object> indicatorData = new HashMap<>();
        
        // 危防类处罚数据
        int currentDangerDefenseCount = sumField(currentDangerDefenseList, "dangerDefense");
        int lastDangerDefenseCount = sumField(lastDangerDefenseList, "dangerDefense");
        Map<String, Object> dangerDefense = new HashMap<>();
        dangerDefense.put("value", currentDangerDefenseCount);
        dangerDefense.put("lastValue", lastDangerDefenseCount);
        dangerDefense.put("rate", calculateRate(lastDangerDefenseCount, currentDangerDefenseCount));
        indicatorData.put("dangerDefense", dangerDefense);
        
        // 危险品类检查数据
        int currentDangerousGoodsCount = sumField(currentDangerousGoodsList, "dangerousGoods");
        int lastDangerousGoodsCount = sumField(lastDangerousGoodsList, "dangerousGoods");
        Map<String, Object> dangerousGoods = new HashMap<>();
        dangerousGoods.put("value", currentDangerousGoodsCount);
        dangerousGoods.put("lastValue", lastDangerousGoodsCount);
        dangerousGoods.put("rate", calculateRate(lastDangerousGoodsCount, currentDangerousGoodsCount));
        indicatorData.put("dangerousGoods", dangerousGoods);
        
        // 防污染类检查数据
        int currentPollutionCount = sumField(currentPollutionList, "pollution");
        int lastPollutionCount = sumField(lastPollutionList, "pollution");
        Map<String, Object> pollution = new HashMap<>();
        pollution.put("value", currentPollutionCount);
        pollution.put("lastValue", lastPollutionCount);
        pollution.put("rate", calculateRate(lastPollutionCount, currentPollutionCount));
        indicatorData.put("pollution", pollution);
        
        return indicatorData;
    }
    
    /**
     * 计算指定字段的总和
     */
    private int sumField(List<WeeklyReportDangerDefense> dataList, String fieldName) {
        int sum = 0;
        for (WeeklyReportDangerDefense data : dataList) {
            if ("dangerDefense".equals(fieldName)) {
                sum += data.getDangerDefenseCount();
            } else if ("dangerousGoods".equals(fieldName)) {
                sum += data.getDangerousGoodsCount();
            } else if ("pollution".equals(fieldName)) {
                sum += data.getPollutionCount();
            }
        }
        return sum;
    }
    
    /**
     * 计算变化率
     */
    private double calculateRate(int lastValue, int currentValue) {
        if (lastValue == 0) {
            return currentValue > 0 ? 100 : 0;
        }
        return ((double) (currentValue - lastValue) / lastValue) * 100;
    }
} 