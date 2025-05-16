package com.jeesite.modules.data_collect.weekly.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 危防数据统计实体
 */
@Table(name = "weekly_report_danger_defense", columns = {
        @Column(name = "id", attrName = "id", label = "主键", isPK = true),
        @Column(name = "agency_Caused by: java.lang.ClassNotFoundException: Cannot find class: WeeklyReportDangerDefense\nname", attrName = "agencyName", label = "部门名称"),
        @Column(name = "danger_defense_count", attrName = "dangerDefenseCount", label = "危防类处罚数量"),
        @Column(name = "dangerous_goods_count", attrName = "dangerousGoodsCount", label = "危险品类检查数量"),
        @Column(name = "pollution_count", attrName = "pollutionCount", label = "防污染类检查数量"),
        @Column(name = "statistics_date", attrName = "statisticsDate", label = "统计日期")
})
public class WeeklyReportDangerDefense extends DataEntity<WeeklyReportDangerDefense> {

    private static final long serialVersionUID = 1L;

    private String agencyName;        // 部门名称
    private Integer dangerDefenseCount;    // 危防类处罚数量
    private Integer dangerousGoodsCount;   // 危险品类检查数量
    private Integer pollutionCount;        // 防污染类检查数量
    private String statisticsDate;         // 统计日期

    public WeeklyReportDangerDefense() {
        this(null);
    }

    public WeeklyReportDangerDefense(String id) {
        super(id);
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public Integer getDangerDefenseCount() {
        return dangerDefenseCount;
    }

    public void setDangerDefenseCount(Integer dangerDefenseCount) {
        this.dangerDefenseCount = dangerDefenseCount;
    }

    public Integer getDangerousGoodsCount() {
        return dangerousGoodsCount;
    }

    public void setDangerousGoodsCount(Integer dangerousGoodsCount) {
        this.dangerousGoodsCount = dangerousGoodsCount;
    }

    public Integer getPollutionCount() {
        return pollutionCount;
    }

    public void setPollutionCount(Integer pollutionCount) {
        this.pollutionCount = pollutionCount;
    }

    public String getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(String statisticsDate) {
        this.statisticsDate = statisticsDate;
    }
} 