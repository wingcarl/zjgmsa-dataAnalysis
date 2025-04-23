package com.jeesite.modules.shipreport.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 船舶报告数据统计表Entity
 * @author 系统自动生成
 * @version 2024-06-21
 */
@Table(name="ship_report_stats", alias="a", label="船舶报告数据统计信息", columns={
        @Column(name="id", attrName="id", label="编号", isPK=true),
        @Column(name="stat_date", attrName="statDate", label="统计日期"),
        @Column(name="total_ships", attrName="totalShips", label="进出船舶总数"),
        @Column(name="inbound_ships", attrName="inboundShips", label="进港船舶数"),
        @Column(name="outbound_ships", attrName="outboundShips", label="出港船舶数"),
        @Column(name="river_ships", attrName="riverShips", label="内河船数量"),
        @Column(name="sea_ships", attrName="seaShips", label="海船数量"),
        @Column(name="hazardous_ships", attrName="hazardousShips", label="危化品船舶数量"),
        @Column(name="length_below_80", attrName="lengthBelow80", label="80米以下船舶数量"),
        @Column(name="length_80_to_150", attrName="length80To150", label="80-150米船舶数量"),
        @Column(name="length_above_150", attrName="lengthAbove150", label="150米以上船舶数量"),
        @Column(name="reporting_agency", attrName="reportingAgency", label="报告机构", queryType=QueryType.LIKE),
        @Column(name="berth", attrName="berth", label="泊位", queryType=QueryType.LIKE),
        @Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
        @Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
        @Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
        @Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
}, orderBy="a.stat_date DESC"
)
public class ShipReportStats extends DataEntity<ShipReportStats> {

    private static final long serialVersionUID = 1L;
    private Date statDate;       // 统计日期
    private Integer totalShips;  // 进出船舶总数
    private Integer inboundShips; // 进港船舶数
    private Integer outboundShips; // 出港船舶数
    private Integer riverShips;   // 内河船数量
    private Integer seaShips;     // 海船数量
    private Integer hazardousShips; // 危化品船舶数量
    private Integer lengthBelow80;  // 80米以下船舶数量
    private Integer length80To150;  // 80-150米船舶数量
    private Integer lengthAbove150; // 150米以上船舶数量
    private String reportingAgency; // 报告机构
    private String berth;          // 泊位

    public ShipReportStats() {
        this(null);
    }

    public ShipReportStats(String id) {
        super(id);
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Integer getTotalShips() {
        return totalShips;
    }

    public void setTotalShips(Integer totalShips) {
        this.totalShips = totalShips;
    }

    public Integer getInboundShips() {
        return inboundShips;
    }

    public void setInboundShips(Integer inboundShips) {
        this.inboundShips = inboundShips;
    }

    public Integer getOutboundShips() {
        return outboundShips;
    }

    public void setOutboundShips(Integer outboundShips) {
        this.outboundShips = outboundShips;
    }

    public Integer getRiverShips() {
        return riverShips;
    }

    public void setRiverShips(Integer riverShips) {
        this.riverShips = riverShips;
    }

    public Integer getSeaShips() {
        return seaShips;
    }

    public void setSeaShips(Integer seaShips) {
        this.seaShips = seaShips;
    }

    public Integer getHazardousShips() {
        return hazardousShips;
    }

    public void setHazardousShips(Integer hazardousShips) {
        this.hazardousShips = hazardousShips;
    }

    public Integer getLengthBelow80() {
        return lengthBelow80;
    }

    public void setLengthBelow80(Integer lengthBelow80) {
        this.lengthBelow80 = lengthBelow80;
    }

    public Integer getLength80To150() {
        return length80To150;
    }

    public void setLength80To150(Integer length80To150) {
        this.length80To150 = length80To150;
    }

    public Integer getLengthAbove150() {
        return lengthAbove150;
    }

    public void setLengthAbove150(Integer lengthAbove150) {
        this.lengthAbove150 = lengthAbove150;
    }

    public String getReportingAgency() {
        return reportingAgency;
    }

    public void setReportingAgency(String reportingAgency) {
        this.reportingAgency = reportingAgency;
    }

    public String getBerth() {
        return berth;
    }

    public void setBerth(String berth) {
        this.berth = berth;
    }

    public void setStatDate_gte(Date startDate) {
    }

    public void setStatDate_lte(Date endDate) {

    }
}