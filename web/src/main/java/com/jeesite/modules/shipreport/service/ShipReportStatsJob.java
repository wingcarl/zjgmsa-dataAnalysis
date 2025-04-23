package com.jeesite.modules.shipreport.service;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.modules.shipreport.entity.ShipReport;
import com.jeesite.modules.shipreport.entity.ShipReportStats;
import com.jeesite.modules.shipreport.service.ShipReportService;
import com.jeesite.modules.shipreport.service.ShipReportStatsService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 船舶报告数据统计定时任务
 */
@DisallowConcurrentExecution
@Component
public class ShipReportStatsJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(ShipReportStatsJob.class);

    @Autowired
    private ShipReportService shipReportService;

    @Autowired
    private ShipReportStatsService shipReportStatsService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("===== 船舶报告数据统计定时任务开始执行 =====");

        try {
            // 获取配置参数
            String statsType = Global.getConfig("ship.report.stats.type", "recent");
            int dayCount = Integer.parseInt(Global.getConfig("ship.report.stats.days", "30"));

            Date endDate = new Date();
            Date startDate;

            if ("all".equals(statsType)) {
                // 计算全量数据，获取最早的记录时间作为起始时间
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -10); // 设置一个足够早的时间
                startDate = cal.getTime();
            } else {
                // 只计算最近N天的数据
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -dayCount);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = cal.getTime();
            }

            // 删除已有的统计数据（针对计算时间范围内的）
            deleteExistingStats(startDate, endDate);

            // 按天计算统计数据
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            endCal.set(Calendar.HOUR_OF_DAY, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);
            endCal.set(Calendar.MILLISECOND, 999);

            // 获取所有报告机构
            List<String> allAgencies = shipReportService.findAllReportingAgencies();
            // 获取所有泊位
            List<String> allBerths = shipReportService.findAllBerths();

            // 每天的全局统计
            while (cal.getTime().before(endCal.getTime())) {
                Date currentStartDate = cal.getTime();

                Calendar dayEndCal = Calendar.getInstance();
                dayEndCal.setTime(currentStartDate);
                dayEndCal.set(Calendar.HOUR_OF_DAY, 23);
                dayEndCal.set(Calendar.MINUTE, 59);
                dayEndCal.set(Calendar.SECOND, 59);
                dayEndCal.set(Calendar.MILLISECOND, 999);
                Date currentEndDate = dayEndCal.getTime();

                // 计算全局统计数据
                ShipReportStats globalStats = new ShipReportStats();
                globalStats.setStatDate(currentStartDate);
                globalStats.setReportingAgency("ALL");
                globalStats.setBerth("ALL");

                calculateStats(globalStats, currentStartDate, currentEndDate, null, null);
                shipReportStatsService.save(globalStats);

                // 按报告机构统计
                for (String agency : allAgencies) {
                    if (agency != null && !agency.trim().isEmpty()) {
                        ShipReportStats agencyStats = new ShipReportStats();
                        agencyStats.setStatDate(currentStartDate);
                        agencyStats.setReportingAgency(agency);
                        agencyStats.setBerth("ALL");

                        calculateStats(agencyStats, currentStartDate, currentEndDate, agency, null);
                        shipReportStatsService.save(agencyStats);
                    }
                }

                // 按泊位统计
                for (String berth : allBerths) {
                    if (berth != null && !berth.trim().isEmpty()) {
                        ShipReportStats berthStats = new ShipReportStats();
                        berthStats.setStatDate(currentStartDate);
                        berthStats.setReportingAgency("ALL");
                        berthStats.setBerth(berth);

                        calculateStats(berthStats, currentStartDate, currentEndDate, null, berth);
                        shipReportStatsService.save(berthStats);
                    }
                }

                // 进入下一天
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }

            logger.info("船舶报告数据统计定时任务执行完成");
        } catch (Exception e) {
            logger.error("船舶报告数据统计任务异常", e);
            throw new JobExecutionException(e);
        }
    }

    /**
     * 删除指定日期范围内的已有统计数据
     */
    private void deleteExistingStats(Date startDate, Date endDate) {
        ShipReportStats stats = new ShipReportStats();
        stats.setStatDate_gte(startDate);
        stats.setStatDate_lte(endDate);
        shipReportStatsService.deleteByEntity(stats);
    }

    /**
     * 计算统计数据
     */
    private void calculateStats(ShipReportStats stats, Date startDate, Date endDate, String reportingAgency, String berth) {
        // 创建查询条件
        ShipReport query = new ShipReport();
        query.setEstimatedArrivalDepartureTime_gte(startDate);
        query.setEstimatedArrivalDepartureTime_lte(endDate);

        if (reportingAgency != null && !"ALL".equals(reportingAgency)) {
            query.setReportingAgency(reportingAgency);
        }

        if (berth != null && !"ALL".equals(berth)) {
            query.setBerth(berth);
        }

        // 查询符合条件的所有船舶报告
        List<ShipReport> reports = shipReportService.findList(query);

        // 初始化计数
        int totalShips = reports.size();
        int inboundShips = 0;
        int outboundShips = 0;
        int riverShips = 0;
        int seaShips = 0;
        int hazardousShips = 0;
        int lengthBelow80 = 0;
        int length80To150 = 0;
        int lengthAbove150 = 0;

        // 统计各类数据
        for (ShipReport report : reports) {
            // 进港/出港统计
            if (report.getInOutPort() != null) {
                if (report.getInOutPort().contains("进")) {
                    inboundShips++;
                } else if (report.getInOutPort().contains("出")) {
                    outboundShips++;
                }
            }

            // 内河船/海船统计
            if (report.getSeaRiverShip() != null) {
                if (report.getSeaRiverShip().contains("内河船")) {
                    riverShips++;
                } else if (report.getSeaRiverShip().contains("海船")) {
                    seaShips++;
                }
            }

            // 危化品船舶统计
            if ("是".equals(report.getIsHazardousCargo())) {
                hazardousShips++;
            }

            // 船舶长度统计
            if (report.getShipLength() != null) {
                if (report.getShipLength() < 80) {
                    lengthBelow80++;
                } else if (report.getShipLength() >= 80 && report.getShipLength() <= 150) {
                    length80To150++;
                } else if (report.getShipLength() > 150) {
                    lengthAbove150++;
                }
            }
        }

        // 设置统计结果
        stats.setTotalShips(totalShips);
        stats.setInboundShips(inboundShips);
        stats.setOutboundShips(outboundShips);
        stats.setRiverShips(riverShips);
        stats.setSeaShips(seaShips);
        stats.setHazardousShips(hazardousShips);
        stats.setLengthBelow80(lengthBelow80);
        stats.setLength80To150(length80To150);
        stats.setLengthAbove150(lengthAbove150);
    }
}