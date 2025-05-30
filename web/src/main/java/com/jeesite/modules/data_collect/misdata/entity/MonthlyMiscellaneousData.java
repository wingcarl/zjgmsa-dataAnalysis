package com.jeesite.modules.data_collect.misdata.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;

/**
 * 月度杂项数据表Entity
 * @author 王浩宇
 * @version 2025-05-30
 */
@Table(name="monthly_miscellaneous_data", alias="a", label="月度杂项数据表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="organization_name", attrName="organizationName", label="单位名称", queryType=QueryType.LIKE),
		@Column(name="level", attrName="level", label="层级"),
		@Column(name="report_date", attrName="reportDate", label="数据时间 ", comment="数据时间 (通常指月份，例如YYYY-MM-01)"),
		@Column(name="patrol_boat_cruise_hours", attrName="patrolBoatCruiseHours", label="海巡艇巡航时间 ", comment="海巡艇巡航时间 (小时)", isQuery=false, isUpdateForce=true),
		@Column(name="patrol_boat_cruise_count", attrName="patrolBoatCruiseCount", label="海巡艇巡航次数", isQuery=false, isUpdateForce=true),
		@Column(name="electronic_cruise_count", attrName="electronicCruiseCount", label="电子巡航次数", isQuery=false, isUpdateForce=true),
		@Column(name="uav_soldier_cruise_count", attrName="uavSoldierCruiseCount", label="无人机单兵巡航次数", isQuery=false, isUpdateForce=true),
		@Column(name="uav_hangar_cruise_count", attrName="uavHangarCruiseCount", label="无人机机库巡航次数", isQuery=false, isUpdateForce=true),
		@Column(name="uav_penalty_count", attrName="uavPenaltyCount", label="无人机处罚数量", isQuery=false, isUpdateForce=true),
		@Column(name="dispatched_task_count", attrName="dispatchedTaskCount", label="派发任务数量", isQuery=false, isUpdateForce=true),
		@Column(name="cruise_task_dispatched_count", attrName="cruiseTaskDispatchedCount", label="巡航任务派发数量", isQuery=false, isUpdateForce=true),
		@Column(name="anchor_rate", attrName="anchorRate", label="锚泊申请率", isQuery=false, isUpdateForce=true),
		@Column(name="port_shipping_integration_closure_rate", attrName="portShippingIntegrationClosureRate", label="港航一体化全过程闭环率 ", comment="港航一体化全过程闭环率 (%)，存储百分比数值，如98.75", isQuery=false, isUpdateForce=true),
		@Column(name="port_shipping_integration_arrival_op_rate", attrName="portShippingIntegrationArrivalOpRate", label="港航一体化到港作业率 ", comment="港航一体化到港作业率 (%)，存储百分比数值，如98.75", isQuery=false, isUpdateForce=true),
		@Column(name="red_code_ship_disposal_rate", attrName="redCodeShipDisposalRate", label="红码船处置率 ", comment="红码船处置率 (%)，存储百分比数值，如98.75", isQuery=false, isUpdateForce=true),
		@Column(name="red_code_ship_onboard_inspection_count", attrName="redCodeShipOnboardInspectionCount", label="红码船登轮检查数量", isQuery=false, isUpdateForce=true),
		@Column(name="red_code_ship_remote_verification_count", attrName="redCodeShipRemoteVerificationCount", label="红码船远程核查数量", isQuery=false, isUpdateForce=true),
		@Column(name="red_code_ship_onboard_inspection_ratio", attrName="redCodeShipOnboardInspectionRatio", label="红码船登轮检查比例 ", comment="红码船登轮检查比例 (%)，存储百分比数值，如98.75", isQuery=false, isUpdateForce=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class MonthlyMiscellaneousData extends DataEntity<MonthlyMiscellaneousData> {
	
	private static final long serialVersionUID = 1L;
	private String organizationName;		// 单位名称
	private String level;		// 层级
	private Date reportDate;		// 数据时间 (通常指月份，例如YYYY-MM-01)
	private Double patrolBoatCruiseHours;		// 海巡艇巡航时间 (小时)
	private Long patrolBoatCruiseCount;		// 海巡艇巡航次数
	private Long electronicCruiseCount;		// 电子巡航次数
	private Long uavSoldierCruiseCount;		// 无人机单兵巡航次数
	private Long uavHangarCruiseCount;		// 无人机机库巡航次数
	private Long uavPenaltyCount;		// 无人机处罚数量
	private Long dispatchedTaskCount;		// 派发任务数量
	private Long cruiseTaskDispatchedCount;		// 巡航任务派发数量
	private Double anchorRate;		// 锚泊申请率
	private Double portShippingIntegrationClosureRate;		// 港航一体化全过程闭环率 (%)，存储百分比数值，如98.75
	private Double portShippingIntegrationArrivalOpRate;		// 港航一体化到港作业率 (%)，存储百分比数值，如98.75
	private Double redCodeShipDisposalRate;		// 红码船处置率 (%)，存储百分比数值，如98.75
	private Long redCodeShipOnboardInspectionCount;		// 红码船登轮检查数量
	private Long redCodeShipRemoteVerificationCount;		// 红码船远程核查数量
	private Double redCodeShipOnboardInspectionRatio;		// 红码船登轮检查比例 (%)，存储百分比数值，如98.75

	@ExcelFields({
		@ExcelField(title="单位名称", attrName="organizationName", align=Align.CENTER, sort=20),
		@ExcelField(title="层级", attrName="level", align=Align.CENTER, sort=30),
		@ExcelField(title="数据时间 ", attrName="reportDate", align=Align.CENTER, sort=40, dataFormat="yyyy-MM-dd"),
		@ExcelField(title="海巡艇巡航时间 ", attrName="patrolBoatCruiseHours", align=Align.CENTER, sort=50),
		@ExcelField(title="海巡艇巡航次数", attrName="patrolBoatCruiseCount", align=Align.CENTER, sort=60),
		@ExcelField(title="电子巡航次数", attrName="electronicCruiseCount", align=Align.CENTER, sort=70),
		@ExcelField(title="无人机单兵巡航次数", attrName="uavSoldierCruiseCount", align=Align.CENTER, sort=80),
		@ExcelField(title="无人机机库巡航次数", attrName="uavHangarCruiseCount", align=Align.CENTER, sort=90),
		@ExcelField(title="无人机处罚数量", attrName="uavPenaltyCount", align=Align.CENTER, sort=100),
		@ExcelField(title="派发任务数量", attrName="dispatchedTaskCount", align=Align.CENTER, sort=110),
		@ExcelField(title="巡航任务派发数量", attrName="cruiseTaskDispatchedCount", align=Align.CENTER, sort=120),
		@ExcelField(title="锚泊申请率", attrName="anchorRate", align=Align.CENTER, sort=130),
		@ExcelField(title="港航一体化全过程闭环率 ", attrName="portShippingIntegrationClosureRate", align=Align.CENTER, sort=140),
		@ExcelField(title="港航一体化到港作业率 ", attrName="portShippingIntegrationArrivalOpRate", align=Align.CENTER, sort=150),
		@ExcelField(title="红码船处置率 ", attrName="redCodeShipDisposalRate", align=Align.CENTER, sort=160),
		@ExcelField(title="红码船登轮检查数量", attrName="redCodeShipOnboardInspectionCount", align=Align.CENTER, sort=170),
		@ExcelField(title="红码船远程核查数量", attrName="redCodeShipRemoteVerificationCount", align=Align.CENTER, sort=180),
		@ExcelField(title="红码船登轮检查比例 ", attrName="redCodeShipOnboardInspectionRatio", align=Align.CENTER, sort=190),
	})
	public MonthlyMiscellaneousData() {
		this(null);
	}
	
	public MonthlyMiscellaneousData(String id){
		super(id);
	}
	
	@NotBlank(message="单位名称不能为空")
	@Size(min=0, max=255, message="单位名称长度不能超过 255 个字符")
	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	@Size(min=0, max=100, message="层级长度不能超过 100 个字符")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="数据时间 不能为空")
	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	
	public Double getPatrolBoatCruiseHours() {
		return patrolBoatCruiseHours;
	}

	public void setPatrolBoatCruiseHours(Double patrolBoatCruiseHours) {
		this.patrolBoatCruiseHours = patrolBoatCruiseHours;
	}
	
	public Long getPatrolBoatCruiseCount() {
		return patrolBoatCruiseCount;
	}

	public void setPatrolBoatCruiseCount(Long patrolBoatCruiseCount) {
		this.patrolBoatCruiseCount = patrolBoatCruiseCount;
	}
	
	public Long getElectronicCruiseCount() {
		return electronicCruiseCount;
	}

	public void setElectronicCruiseCount(Long electronicCruiseCount) {
		this.electronicCruiseCount = electronicCruiseCount;
	}
	
	public Long getUavSoldierCruiseCount() {
		return uavSoldierCruiseCount;
	}

	public void setUavSoldierCruiseCount(Long uavSoldierCruiseCount) {
		this.uavSoldierCruiseCount = uavSoldierCruiseCount;
	}
	
	public Long getUavHangarCruiseCount() {
		return uavHangarCruiseCount;
	}

	public void setUavHangarCruiseCount(Long uavHangarCruiseCount) {
		this.uavHangarCruiseCount = uavHangarCruiseCount;
	}
	
	public Long getUavPenaltyCount() {
		return uavPenaltyCount;
	}

	public void setUavPenaltyCount(Long uavPenaltyCount) {
		this.uavPenaltyCount = uavPenaltyCount;
	}
	
	public Long getDispatchedTaskCount() {
		return dispatchedTaskCount;
	}

	public void setDispatchedTaskCount(Long dispatchedTaskCount) {
		this.dispatchedTaskCount = dispatchedTaskCount;
	}
	
	public Long getCruiseTaskDispatchedCount() {
		return cruiseTaskDispatchedCount;
	}

	public void setCruiseTaskDispatchedCount(Long cruiseTaskDispatchedCount) {
		this.cruiseTaskDispatchedCount = cruiseTaskDispatchedCount;
	}
	
	public Double getAnchorRate() {
		return anchorRate;
	}

	public void setAnchorRate(Double anchorRate) {
		this.anchorRate = anchorRate;
	}
	
	public Double getPortShippingIntegrationClosureRate() {
		return portShippingIntegrationClosureRate;
	}

	public void setPortShippingIntegrationClosureRate(Double portShippingIntegrationClosureRate) {
		this.portShippingIntegrationClosureRate = portShippingIntegrationClosureRate;
	}
	
	public Double getPortShippingIntegrationArrivalOpRate() {
		return portShippingIntegrationArrivalOpRate;
	}

	public void setPortShippingIntegrationArrivalOpRate(Double portShippingIntegrationArrivalOpRate) {
		this.portShippingIntegrationArrivalOpRate = portShippingIntegrationArrivalOpRate;
	}
	
	public Double getRedCodeShipDisposalRate() {
		return redCodeShipDisposalRate;
	}

	public void setRedCodeShipDisposalRate(Double redCodeShipDisposalRate) {
		this.redCodeShipDisposalRate = redCodeShipDisposalRate;
	}
	
	public Long getRedCodeShipOnboardInspectionCount() {
		return redCodeShipOnboardInspectionCount;
	}

	public void setRedCodeShipOnboardInspectionCount(Long redCodeShipOnboardInspectionCount) {
		this.redCodeShipOnboardInspectionCount = redCodeShipOnboardInspectionCount;
	}
	
	public Long getRedCodeShipRemoteVerificationCount() {
		return redCodeShipRemoteVerificationCount;
	}

	public void setRedCodeShipRemoteVerificationCount(Long redCodeShipRemoteVerificationCount) {
		this.redCodeShipRemoteVerificationCount = redCodeShipRemoteVerificationCount;
	}
	
	public Double getRedCodeShipOnboardInspectionRatio() {
		return redCodeShipOnboardInspectionRatio;
	}

	public void setRedCodeShipOnboardInspectionRatio(Double redCodeShipOnboardInspectionRatio) {
		this.redCodeShipOnboardInspectionRatio = redCodeShipOnboardInspectionRatio;
	}
	
	public Date getReportDate_gte() {
		return sqlMap.getWhere().getValue("report_date", QueryType.GTE);
	}

	public void setReportDate_gte(Date reportDate) {
		sqlMap.getWhere().and("report_date", QueryType.GTE, reportDate);
	}
	
	public Date getReportDate_lte() {
		return sqlMap.getWhere().getValue("report_date", QueryType.LTE);
	}

	public void setReportDate_lte(Date reportDate) {
		sqlMap.getWhere().and("report_date", QueryType.LTE, reportDate);
	}
	
}