package com.jeesite.modules.data_collect.largeship.web;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.data_collect.largeship.entity.LargeRestrictedVessels;
import com.jeesite.modules.data_collect.largeship.service.LargeRestrictedVesselsService;

/**
 * 大型受限船舶数据分析Controller
 * @author 王浩宇
 * @version 2025-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/largeship/analysis")
public class LargeShipAnalysisController extends BaseController {

    @Autowired
    private LargeRestrictedVesselsService largeRestrictedVesselsService;

    /**
     * 数据分析页面
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @RequestMapping(value = {"", "index"})
    public String index(Model model) {
        // 默认展示最近一周的数据
        LocalDate today = LocalDate.now();
        // 计算本周的上周五到本周四
        LocalDate thisThursday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY));
        LocalDate prevFriday = thisThursday.minusDays(6);

        // 计算上一周期的上周五到本周四
        LocalDate lastThursday = thisThursday.minusDays(7);
        LocalDate lastPrevFriday = prevFriday.minusDays(7);

        model.addAttribute("startDate", prevFriday.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("endDate", thisThursday.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("lastStartDate", lastPrevFriday.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("lastEndDate", lastThursday.format(DateTimeFormatter.ISO_DATE));

        return "data_collect/largeship/largeShipAnalysis";
    }

    /**
     * 获取大型受限船舶数据卡片信息
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @GetMapping(value = "getCardData")
    @ResponseBody
    public Map<String, Object> getCardData(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String lastStartDate,
            @RequestParam String lastEndDate) {

        // 解析日期
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDate lastStart = LocalDate.parse(lastStartDate);
        LocalDate lastEnd = LocalDate.parse(lastEndDate);

        // 查询当前周期的数据
        LargeRestrictedVessels queryParams = new LargeRestrictedVessels();
        queryParams.setOperationDate_gte(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryParams.setOperationDate_lte(Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
        List<LargeRestrictedVessels> currentPeriodData = largeRestrictedVesselsService.findList(queryParams);

        // 查询上一周期的数据
        queryParams = new LargeRestrictedVessels();
        queryParams.setOperationDate_gte(Date.from(lastStart.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryParams.setOperationDate_lte(Date.from(lastEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
        List<LargeRestrictedVessels> lastPeriodData = largeRestrictedVesselsService.findList(queryParams);

        // 计算卡片数据
        Map<String, Object> result = new HashMap<>();

        // 1.大型受限船舶数量
        int totalShips = currentPeriodData.size();
        int lastTotalShips = lastPeriodData.size();
        double totalShipsRate = calculateRate(totalShips, lastTotalShips);

        // 2.大型受限船舶进港数量
        int entryShips = (int) currentPeriodData.stream()
                .filter(v -> v.getEntryExitDirection() != null && v.getEntryExitDirection().contains("进"))
                .count();
        int lastEntryShips = (int) lastPeriodData.stream()
                .filter(v -> v.getEntryExitDirection() != null && v.getEntryExitDirection().contains("进"))
                .count();
        double entryShipsRate = calculateRate(entryShips, lastEntryShips);

        // 3.大型受限船舶出港数量
        int exitShips = (int) currentPeriodData.stream()
                .filter(v -> v.getEntryExitDirection() != null && v.getEntryExitDirection().contains("出"))
                .count();
        int lastExitShips = (int) lastPeriodData.stream()
                .filter(v -> v.getEntryExitDirection() != null && v.getEntryExitDirection().contains("出"))
                .count();
        double exitShipsRate = calculateRate(exitShips, lastExitShips);

        // 4.CAPE船舶进港数量 (>=250米)
        int capeEntryShips = (int) currentPeriodData.stream()
                .filter(v -> v.getEntryExitDirection() != null && v.getEntryExitDirection().contains("进"))
                .filter(v -> v.getShipLength() != null && v.getShipLength() >= 250)
                .count();
        int lastCapeEntryShips = (int) lastPeriodData.stream()
                .filter(v -> v.getEntryExitDirection() != null && v.getEntryExitDirection().contains("进"))
                .filter(v -> v.getShipLength() != null && v.getShipLength() >= 250)
                .count();
        double capeEntryShipsRate = calculateRate(capeEntryShips, lastCapeEntryShips);

        // 5.CAPE船舶出港数量 (>=250米)
        int capeExitShips = (int) currentPeriodData.stream()
                .filter(v -> v.getEntryExitDirection() != null && v.getEntryExitDirection().contains("出"))
                .filter(v -> v.getShipLength() != null && v.getShipLength() >= 250)
                .count();
        int lastCapeExitShips = (int) lastPeriodData.stream()
                .filter(v -> v.getEntryExitDirection() != null && v.getEntryExitDirection().contains("出"))
                .filter(v -> v.getShipLength() != null && v.getShipLength() >= 250)
                .count();
        double capeExitShipsRate = calculateRate(capeExitShips, lastCapeExitShips);

        // 组装结果
        result.put("totalShips", createCardData(totalShips, totalShipsRate));
        result.put("entryShips", createCardData(entryShips, entryShipsRate));
        result.put("exitShips", createCardData(exitShips, exitShipsRate));
        result.put("capeEntryShips", createCardData(capeEntryShips, capeEntryShipsRate));
        result.put("capeExitShips", createCardData(capeExitShips, capeExitShipsRate));

        return result;
    }

    /**
     * 获取船舶趋势数据
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @GetMapping(value = "getTrendData")
    @ResponseBody
    public Map<String, Object> getTrendData(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String timeUnit,
            @RequestParam(required = false) Double minLength,
            @RequestParam(required = false) Double maxLength,
            @RequestParam(required = false) String berthName,
            @RequestParam(required = false) String cargoName,
            @RequestParam(required = false) String agentName,
            @RequestParam(required = false) String portOfRegistry) {

        // 解析日期
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // 查询数据
        LargeRestrictedVessels queryParams = new LargeRestrictedVessels();
        queryParams.setOperationDate_gte(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryParams.setOperationDate_lte(Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));

        // 应用过滤条件
        if (minLength != null) {
            queryParams.setShipLength_gte(minLength);
        }
        if (maxLength != null) {
            queryParams.setShipLength_lte(maxLength);
        }
        if (StringUtils.isNotBlank(berthName)) {
            queryParams.setBerthName(berthName);
        }
        if (StringUtils.isNotBlank(cargoName)) {
            queryParams.setCargoName(cargoName);
        }
        if (StringUtils.isNotBlank(agentName)) {
            queryParams.setAgentName(agentName);
        }
        if (StringUtils.isNotBlank(portOfRegistry)) {
            queryParams.setPortOfRegistry(portOfRegistry);
        }

        List<LargeRestrictedVessels> data = largeRestrictedVesselsService.findList(queryParams);

        // 根据时间单位生成统计数据
        Map<String, Object> result = new HashMap<>();

        if ("week".equals(timeUnit)) {
            result = generateWeeklyTrend(data, start, end);
        } else if ("month".equals(timeUnit)) {
            result = generateMonthlyTrend(data, start, end);
        } else if ("year".equals(timeUnit)) {
            result = generateYearlyTrend(data, start, end);
        } else {
            // 默认按天
            result = generateDailyTrend(data, start, end);
        }

        return result;
    }

    /**
     * 获取船舶长度分布数据
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @GetMapping(value = "getLengthDistribution")
    @ResponseBody
    public Map<String, Object> getLengthDistribution(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // 解析日期
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // 查询数据
        LargeRestrictedVessels queryParams = new LargeRestrictedVessels();
        queryParams.setOperationDate_gte(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryParams.setOperationDate_lte(Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));

        List<LargeRestrictedVessels> data = largeRestrictedVesselsService.findList(queryParams);

        // 统计船舶长度分布
        int lessThan200 = 0;
        int between200And250 = 0;
        int greaterThan250 = 0;

        for (LargeRestrictedVessels vessel : data) {
            Double length = vessel.getShipLength();
            if (length != null) {
                if (length < 200) {
                    lessThan200++;
                } else if (length >= 200 && length < 250) {
                    between200And250++;
                } else if (length >= 250) {
                    greaterThan250++;
                }
            }
        }

        Map<String, Object> result = new HashMap<>();

        result.put("categories", Arrays.asList("<200米", "200-250米", "≥250米"));
        result.put("data", Arrays.asList(lessThan200, between200And250, greaterThan250));

        return result;
    }

    /**
     * 计算环比增长率
     */
    private double calculateRate(int current, int last) {
        if (last == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return Math.round(((double)(current - last) / last) * 10000) / 100.0;
    }

    /**
     * 创建卡片数据结构
     */
    private Map<String, Object> createCardData(int value, double rate) {
        Map<String, Object> cardData = new HashMap<>();
        cardData.put("value", value);
        cardData.put("rate", rate);
        return cardData;
    }

    /**
     * 生成按天的趋势数据
     */
    private Map<String, Object> generateDailyTrend(List<LargeRestrictedVessels> data, LocalDate start, LocalDate end) {
        Map<LocalDate, Integer> dailyCounts = new HashMap<>();

        // 初始化日期范围内的每一天
        long daysBetween = ChronoUnit.DAYS.between(start, end) + 1;
        for (int i = 0; i < daysBetween; i++) {
            LocalDate date = start.plusDays(i);
            dailyCounts.put(date, 0);
        }

        // 统计每天的船舶数量
        for (LargeRestrictedVessels vessel : data) {
            if (vessel.getOperationDate() != null) {
                LocalDate operationDate = vessel.getOperationDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                if (dailyCounts.containsKey(operationDate)) {
                    dailyCounts.put(operationDate, dailyCounts.get(operationDate) + 1);
                }
            }
        }

        // 构建结果
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        dailyCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
                    dates.add(entry.getKey().format(formatter));
                    counts.add(entry.getValue());
                });

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", dates);
        result.put("series", counts);
        result.put("unit", "天");

        return result;
    }

    /**
     * 生成按周的趋势数据
     */
    private Map<String, Object> generateWeeklyTrend(List<LargeRestrictedVessels> data, LocalDate start, LocalDate end) {
        // 调整开始日期到上一个周五
        LocalDate adjustedStart = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));

        Map<String, Integer> weeklyCounts = new HashMap<>();

        // 按周分组初始化
        LocalDate currentWeekStart = adjustedStart;
        while (currentWeekStart.isBefore(end) || currentWeekStart.isEqual(end)) {
            LocalDate weekEnd = currentWeekStart.plusDays(6);
            String weekKey = currentWeekStart.format(DateTimeFormatter.ISO_DATE) + " ~ " +
                    weekEnd.format(DateTimeFormatter.ISO_DATE);
            weeklyCounts.put(weekKey, 0);
            currentWeekStart = currentWeekStart.plusDays(7);
        }

        // 统计每周的船舶数量
        for (LargeRestrictedVessels vessel : data) {
            if (vessel.getOperationDate() != null) {
                LocalDate operationDate = vessel.getOperationDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                // 找到对应的周
                LocalDate weekStart = operationDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));
                LocalDate weekEnd = weekStart.plusDays(6);

                String weekKey = weekStart.format(DateTimeFormatter.ISO_DATE) + " ~ " +
                        weekEnd.format(DateTimeFormatter.ISO_DATE);

                if (weeklyCounts.containsKey(weekKey)) {
                    weeklyCounts.put(weekKey, weeklyCounts.get(weekKey) + 1);
                }
            }
        }

        // 构建结果
        List<String> weeks = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        weeklyCounts.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .forEach(entry -> {
                    weeks.add(entry.getKey());
                    counts.add(entry.getValue());
                });

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", weeks);
        result.put("series", counts);
        result.put("unit", "周");

        return result;
    }

    /**
     * 生成按月的趋势数据
     */
    private Map<String, Object> generateMonthlyTrend(List<LargeRestrictedVessels> data, LocalDate start, LocalDate end) {
        Map<String, Integer> monthlyCounts = new HashMap<>();

        // 初始化月份范围
        LocalDate currentMonth = start.withDayOfMonth(1);
        while (currentMonth.isBefore(end) || currentMonth.isEqual(end)) {
            String monthKey = currentMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyCounts.put(monthKey, 0);
            currentMonth = currentMonth.plusMonths(1);
        }

        // 统计每月的船舶数量
        for (LargeRestrictedVessels vessel : data) {
            if (vessel.getOperationDate() != null) {
                LocalDate operationDate = vessel.getOperationDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                String monthKey = operationDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));

                if (monthlyCounts.containsKey(monthKey)) {
                    monthlyCounts.put(monthKey, monthlyCounts.get(monthKey) + 1);
                }
            }
        }

        // 构建结果
        List<String> months = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        monthlyCounts.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .forEach(entry -> {
                    months.add(entry.getKey());
                    counts.add(entry.getValue());
                });

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", months);
        result.put("series", counts);
        result.put("unit", "月");

        return result;
    }

    /**
     * 生成按年的趋势数据
     */
    private Map<String, Object> generateYearlyTrend(List<LargeRestrictedVessels> data, LocalDate start, LocalDate end) {
        Map<Integer, Integer> yearlyCounts = new HashMap<>();

        // 初始化年份范围
        int startYear = start.getYear();
        int endYear = end.getYear();

        for (int year = startYear; year <= endYear; year++) {
            yearlyCounts.put(year, 0);
        }

        // 统计每年的船舶数量
        for (LargeRestrictedVessels vessel : data) {
            if (vessel.getOperationDate() != null) {
                LocalDate operationDate = vessel.getOperationDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                int year = operationDate.getYear();

                if (yearlyCounts.containsKey(year)) {
                    yearlyCounts.put(year, yearlyCounts.get(year) + 1);
                }
            }
        }

        // 构建结果
        List<String> years = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        yearlyCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    years.add(String.valueOf(entry.getKey()));
                    counts.add(entry.getValue());
                });

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", years);
        result.put("series", counts);
        result.put("unit", "年");

        return result;
    }
    /**
     * 数据分析页面
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @RequestMapping(value = {"chart", "index"})
    public String chart() {
        return "data_collect/largeship/largeShipAnalysis";
    }

    /**
     * 获取代理船舶吃水范围数据
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @GetMapping(value = "getAgentDraftData")
    @ResponseBody
    public Map<String, Object> getAgentDraftData(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // 解析日期
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // 查询数据
        LargeRestrictedVessels queryParams = new LargeRestrictedVessels();
        queryParams.setOperationDate_gte(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryParams.setOperationDate_lte(Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));

        List<LargeRestrictedVessels> data = largeRestrictedVesselsService.findList(queryParams);

        // 统计每个代理的船舶最大淡水吃水范围
        Map<String, Double> agentMinDraftMap = new HashMap<>();
        Map<String, Double> agentMaxDraftMap = new HashMap<>();
        Map<String, Integer> agentVesselCountMap = new HashMap<>();

        for (LargeRestrictedVessels vessel : data) {
            if (vessel.getAgentName() != null && !vessel.getAgentName().trim().isEmpty()
                    && vessel.getMaxFreshwaterDraft() != null) {
                String agentName = vessel.getAgentName();
                Double draft = vessel.getMaxFreshwaterDraft();

                // 更新最小吃水
                if (!agentMinDraftMap.containsKey(agentName) || draft < agentMinDraftMap.get(agentName)) {
                    agentMinDraftMap.put(agentName, draft);
                }

                // 更新最大吃水
                if (!agentMaxDraftMap.containsKey(agentName) || draft > agentMaxDraftMap.get(agentName)) {
                    agentMaxDraftMap.put(agentName, draft);
                }

                // 更新船舶数量
                agentVesselCountMap.put(agentName, agentVesselCountMap.getOrDefault(agentName, 0) + 1);
            }
        }

        // 过滤至少有2艘船的代理
        Set<String> validAgents = agentVesselCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // 按照最大吃水降序排序，返回所有数据
        List<String> filteredAgents = validAgents.stream()
                .sorted((a, b) -> Double.compare(agentMaxDraftMap.get(b), agentMaxDraftMap.get(a)))
                .collect(Collectors.toList());

        // 组装结果
        List<Double> minDrafts = new ArrayList<>();
        List<Double> maxDrafts = new ArrayList<>();

        for (String agent : filteredAgents) {
            minDrafts.add(agentMinDraftMap.get(agent));
            maxDrafts.add(agentMaxDraftMap.get(agent));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("agents", filteredAgents);
        result.put("minDrafts", minDrafts);
        result.put("maxDrafts", maxDrafts);

        return result;
    }

    /**
     * 获取码头船舶吃水分布数据
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @GetMapping(value = "getBerthDraftData")
    @ResponseBody
    public Map<String, Object> getBerthDraftData(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // 解析日期
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // 查询数据
        LargeRestrictedVessels queryParams = new LargeRestrictedVessels();
        queryParams.setOperationDate_gte(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryParams.setOperationDate_lte(Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));

        List<LargeRestrictedVessels> data = largeRestrictedVesselsService.findList(queryParams);

        // 收集每个码头的船舶吃水数据
        Map<String, List<Double>> berthDraftMap = new HashMap<>();
        Map<String, Integer> berthCountMap = new HashMap<>();

        for (LargeRestrictedVessels vessel : data) {
            if (vessel.getBerthName() != null && !vessel.getBerthName().trim().isEmpty()
                    && vessel.getMaxFreshwaterDraft() != null) {
                // 只保留中文，去掉符号和数字
                String berthName = vessel.getBerthName().replaceAll("[^\\u4e00-\\u9fa5]", "");
                if (!berthName.isEmpty()) {
                    // 初始化该码头的数据列表（如果尚未存在）
                    if (!berthDraftMap.containsKey(berthName)) {
                        berthDraftMap.put(berthName, new ArrayList<>());
                    }

                    // 添加吃水数据
                    berthDraftMap.get(berthName).add(vessel.getMaxFreshwaterDraft());

                    // 更新码头船舶计数
                    berthCountMap.put(berthName, berthCountMap.getOrDefault(berthName, 0) + 1);
                }
            }
        }

        // 过滤至少有5艘船的码头
        Set<String> validBerths = berthCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // 按船舶数量排序，返回所有数据
        List<String> sortedBerths = validBerths.stream()
                .sorted((a, b) -> berthCountMap.get(b) - berthCountMap.get(a))
                .collect(Collectors.toList());

        // 为每个码头计算箱线图数据: [最小值, 下四分位数, 中位数, 上四分位数, 最大值]
        List<double[]> boxplotData = new ArrayList<>();

        for (String berth : sortedBerths) {
            List<Double> drafts = berthDraftMap.get(berth);

            // 对吃水数据进行排序
            Collections.sort(drafts);

            int size = drafts.size();
            double min = drafts.get(0);
            double max = drafts.get(size - 1);
            double median = size % 2 == 0 ?
                    (drafts.get(size / 2 - 1) + drafts.get(size / 2)) / 2 :
                    drafts.get(size / 2);

            // 计算四分位数
            double q1, q3;
            int q1Idx = size / 4;
            int q3Idx = size * 3 / 4;

            if (size % 4 == 0) {
                q1 = (drafts.get(q1Idx - 1) + drafts.get(q1Idx)) / 2;
                q3 = (drafts.get(q3Idx - 1) + drafts.get(q3Idx)) / 2;
            } else {
                q1 = drafts.get(q1Idx);
                q3 = drafts.get(q3Idx);
            }

            boxplotData.add(new double[]{min, q1, median, q3, max});
        }

        Map<String, Object> result = new HashMap<>();
        result.put("berths", sortedBerths);
        result.put("draftData", boxplotData);

        return result;
    }

    /**
     * 获取码头船舶长度分布数据
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @GetMapping(value = "getBerthLengthData")
    @ResponseBody
    public Map<String, Object> getBerthLengthData(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // 解析日期
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // 查询数据
        LargeRestrictedVessels queryParams = new LargeRestrictedVessels();
        queryParams.setOperationDate_gte(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryParams.setOperationDate_lte(Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));

        List<LargeRestrictedVessels> data = largeRestrictedVesselsService.findList(queryParams);

        // 收集每个码头的船舶长度数据
        Map<String, List<Double>> berthLengthMap = new HashMap<>();
        Map<String, Integer> berthCountMap = new HashMap<>();

        for (LargeRestrictedVessels vessel : data) {
            if (vessel.getBerthName() != null && !vessel.getBerthName().trim().isEmpty()
                    && vessel.getShipLength() != null) {
                // 只保留中文，去掉符号和数字
                String berthName = vessel.getBerthName().replaceAll("[^\\u4e00-\\u9fa5]", "");
                if (!berthName.isEmpty()) {
                    // 初始化该码头的数据列表（如果尚未存在）
                    if (!berthLengthMap.containsKey(berthName)) {
                        berthLengthMap.put(berthName, new ArrayList<>());
                    }

                    // 添加长度数据
                    berthLengthMap.get(berthName).add(vessel.getShipLength());

                    // 更新码头船舶计数
                    berthCountMap.put(berthName, berthCountMap.getOrDefault(berthName, 0) + 1);
                }
            }
        }

        // 过滤至少有5艘船的码头
        Set<String> validBerths = berthCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // 按船舶数量排序，返回所有数据
        List<String> sortedBerths = validBerths.stream()
                .sorted((a, b) -> berthCountMap.get(b) - berthCountMap.get(a))
                .collect(Collectors.toList());

        // 为每个码头计算箱线图数据: [最小值, 下四分位数, 中位数, 上四分位数, 最大值]
        List<double[]> boxplotData = new ArrayList<>();

        for (String berth : sortedBerths) {
            List<Double> lengths = berthLengthMap.get(berth);

            // 对长度数据进行排序
            Collections.sort(lengths);

            int size = lengths.size();
            double min = lengths.get(0);
            double max = lengths.get(size - 1);
            double median = size % 2 == 0 ?
                    (lengths.get(size / 2 - 1) + lengths.get(size / 2)) / 2 :
                    lengths.get(size / 2);

            // 计算四分位数
            double q1, q3;
            int q1Idx = size / 4;
            int q3Idx = size * 3 / 4;

            if (size % 4 == 0) {
                q1 = (lengths.get(q1Idx - 1) + lengths.get(q1Idx)) / 2;
                q3 = (lengths.get(q3Idx - 1) + lengths.get(q3Idx)) / 2;
            } else {
                q1 = lengths.get(q1Idx);
                q3 = lengths.get(q3Idx);
            }

            boxplotData.add(new double[]{min, q1, median, q3, max});
        }

        Map<String, Object> result = new HashMap<>();
        result.put("berths", sortedBerths);
        result.put("lengthData", boxplotData);

        return result;
    }
    /**
     * 获取码头船舶数量统计数据
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @GetMapping(value = "getBerthCountData")
    @ResponseBody
    public Map<String, Object> getBerthCountData(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // 解析日期
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // 查询数据
        LargeRestrictedVessels queryParams = new LargeRestrictedVessels();
        queryParams.setOperationDate_gte(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryParams.setOperationDate_lte(Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));

        List<LargeRestrictedVessels> data = largeRestrictedVesselsService.findList(queryParams);

        // 统计每个码头的船舶数量
        Map<String, Integer> berthCountMap = new HashMap<>();

        for (LargeRestrictedVessels vessel : data) {
            if (vessel.getBerthName() != null && !vessel.getBerthName().trim().isEmpty()) {
                // 只保留中文，去掉符号和数字
                String berthName = vessel.getBerthName().replaceAll("[^\\u4e00-\\u9fa5]", "");
                if (!berthName.isEmpty()) {
                    berthCountMap.put(berthName, berthCountMap.getOrDefault(berthName, 0) + 1);
                }
            }
        }

        // 按数量降序排序
        List<Map.Entry<String, Integer>> sortedEntries = berthCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                
                .collect(Collectors.toList());

        // 组装结果
        List<String> berths = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            berths.add(entry.getKey());
            counts.add(entry.getValue());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("berths", berths);
        result.put("counts", counts);

        return result;
    }


    /**
     * 获取代理船舶数量统计数据
     */
    @RequiresPermissions("largeship:largeRestrictedVessels:view")
    @GetMapping(value = "getAgentCountData")
    @ResponseBody
    public Map<String, Object> getAgentCountData(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // 解析日期
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // 查询数据
        LargeRestrictedVessels queryParams = new LargeRestrictedVessels();
        queryParams.setOperationDate_gte(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryParams.setOperationDate_lte(Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));

        List<LargeRestrictedVessels> data = largeRestrictedVesselsService.findList(queryParams);

        // 统计每个代理的船舶数量
        Map<String, Integer> agentCountMap = new HashMap<>();

        for (LargeRestrictedVessels vessel : data) {
            if (vessel.getAgentName() != null && !vessel.getAgentName().trim().isEmpty()) {
                String agentName = vessel.getAgentName();
                agentCountMap.put(agentName, agentCountMap.getOrDefault(agentName, 0) + 1);
            }
        }

        // 按数量降序排序
        List<Map.Entry<String, Integer>> sortedEntries = agentCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(15) // 取前15个
                .collect(Collectors.toList());

        // 组装结果
        List<String> agents = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            agents.add(entry.getKey());
            counts.add(entry.getValue());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("agents", agents);
        result.put("counts", counts);

        return result;
    }


}