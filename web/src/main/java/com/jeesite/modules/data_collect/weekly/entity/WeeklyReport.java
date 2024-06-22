package com.jeesite.modules.data_collect.weekly.entity;

import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.modules.sys.entity.Office;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;

/**
 * 周工作数据表Entity
 * @author 王浩宇
 * @version 2024-06-22
 */
@Table(name="weekly_report", alias="a", label="周工作数据表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="report_date", attrName="reportDate", label="日期", isUpdateForce=true),
		@Column(name="department_id", attrName="departmentId.officeCode", label="部门"),
		@Column(name="sea_ship_inspection_count", attrName="seaShipInspectionCount", label="海船安检艘次", isQuery=false, isUpdateForce=true),
		@Column(name="sea_ship_defect_count", attrName="seaShipDefectCount", label="海船缺陷数量", isQuery=false, isUpdateForce=true),
		@Column(name="sea_ship_detention_count", attrName="seaShipDetentionCount", label="海船滞留艘次", isQuery=false, isUpdateForce=true),
		@Column(name="river_ship_inspection_count", attrName="riverShipInspectionCount", label="内河船安检艘次", isQuery=false, isUpdateForce=true),
		@Column(name="river_ship_defect_count", attrName="riverShipDefectCount", label="内河船缺陷数量", isQuery=false, isUpdateForce=true),
		@Column(name="river_ship_detention_count", attrName="riverShipDetentionCount", label="内河船滞留数量", isQuery=false, isUpdateForce=true),
		@Column(name="psc_inspection_count", attrName="pscInspectionCount", label="PSC艘次", isQuery=false, isUpdateForce=true),
		@Column(name="psc_defect_count", attrName="pscDefectCount", label="PSC缺陷数量", isQuery=false, isUpdateForce=true),
		@Column(name="psc_detention_count", attrName="pscDetentionCount", label="PSC滞留艘次", isQuery=false, isUpdateForce=true),
		@Column(name="bulk_liquid_hazardous_cargo_supervision", attrName="bulkLiquidHazardousCargoSupervision", label="散装液体危险货物作业现场监督检查", isQuery=false, isUpdateForce=true),
		@Column(name="fuel_quick_sampling_inspection", attrName="fuelQuickSamplingInspection", label="燃油快速抽样检测", isQuery=false, isUpdateForce=true),
		@Column(name="investigation_cases", attrName="investigationCases", label="立案调查数据", isQuery=false, isUpdateForce=true),
		@Column(name="patrol_boat_abnormal_discovery", attrName="patrolBoatAbnormalDiscovery", label="海巡艇巡航发现异常", isQuery=false, isUpdateForce=true),
		@Column(name="patrol_boat_investigation_cases", attrName="patrolBoatInvestigationCases", label="海巡艇巡航立案调查", isQuery=false, isUpdateForce=true),
		@Column(name="drone_abnormal_discovery", attrName="droneAbnormalDiscovery", label="无人机巡航发现异常", isQuery=false, isUpdateForce=true),
		@Column(name="drone_investigation_cases", attrName="droneInvestigationCases", label="无人机巡航立案调查", isQuery=false, isUpdateForce=true),
		@Column(name="electronic_patrol_abnormal_discovery", attrName="electronicPatrolAbnormalDiscovery", label="电子巡航发现异常", isQuery=false, isUpdateForce=true),
		@Column(name="electronic_patrol_investigation_cases", attrName="electronicPatrolInvestigationCases", label="电子巡航立案调查", isQuery=false, isUpdateForce=true),
		@Column(name="restricted_ship_in", attrName="restrictedShipIn", label="受限船进", isQuery=false, isUpdateForce=true),
		@Column(name="restricted_ship_out", attrName="restrictedShipOut", label="受限船出", isQuery=false, isUpdateForce=true),
		@Column(name="cape_ship_in", attrName="capeShipIn", label="cape船进", isQuery=false, isUpdateForce=true),
		@Column(name="cape_ship_out", attrName="capeShipOut", label="cape船出", isQuery=false, isUpdateForce=true),
		@Column(name="danger_count", attrName="dangerCount", label="险情数量", isQuery=false, isUpdateForce=true),
		@Column(name="hydraulic_construction_count", attrName="hydraulicConstructionCount", label="水工数量", isQuery=false, isUpdateForce=true),
		@Column(name="restricted_ship_approval_count", attrName="restrictedShipApprovalCount", label="受限船核准数量", isQuery=false, isUpdateForce=true),
		@Column(name="risk_hazard_identification_count", attrName="riskHazardIdentificationCount", label="排查风险隐患数量", isQuery=false, isUpdateForce=true),
		@Column(name="port_entry_exit_report_count", attrName="portEntryExitReportCount", label="进出港报告艘次", isQuery=false, isUpdateForce=true),
		@Column(name="penalty_decision_count", attrName="penaltyDecisionCount", label="处罚决定数量", isQuery=false, isUpdateForce=true),
		@Column(name="penalty_amount", attrName="penaltyAmount", label="处罚金额", isQuery=false, isUpdateForce=true),
		@Column(name="illegal_score", attrName="illegalScore", label="违法计分", isQuery=false, isUpdateForce=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, joinTable={		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="departmentId", alias="u3",
			on="u3.office_code = a.department_id", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
	}, orderBy="a.update_date DESC"
)
public class WeeklyReport extends DataEntity<WeeklyReport> {
	
	private static final long serialVersionUID = 1L;
	private Date reportDate;		// 日期
	private Office departmentId;		// 部门
	private Long seaShipInspectionCount;		// 海船安检艘次
	private Long seaShipDefectCount;		// 海船缺陷数量
	private Long seaShipDetentionCount;		// 海船滞留艘次
	private Long riverShipInspectionCount;		// 内河船安检艘次
	private Long riverShipDefectCount;		// 内河船缺陷数量
	private Long riverShipDetentionCount;		// 内河船滞留数量
	private Long pscInspectionCount;		// PSC艘次
	private Long pscDefectCount;		// PSC缺陷数量
	private Long pscDetentionCount;		// PSC滞留艘次
	private Long bulkLiquidHazardousCargoSupervision;		// 散装液体危险货物作业现场监督检查
	private Long fuelQuickSamplingInspection;		// 燃油快速抽样检测
	private Long investigationCases;		// 立案调查数据
	private Long patrolBoatAbnormalDiscovery;		// 海巡艇巡航发现异常
	private Long patrolBoatInvestigationCases;		// 海巡艇巡航立案调查
	private Long droneAbnormalDiscovery;		// 无人机巡航发现异常
	private Long droneInvestigationCases;		// 无人机巡航立案调查
	private Long electronicPatrolAbnormalDiscovery;		// 电子巡航发现异常
	private Long electronicPatrolInvestigationCases;		// 电子巡航立案调查
	private Long restrictedShipIn;		// 受限船进
	private Long restrictedShipOut;		// 受限船出
	private Long capeShipIn;		// cape船进
	private Long capeShipOut;		// cape船出
	private Long dangerCount;		// 险情数量
	private Long hydraulicConstructionCount;		// 水工数量
	private Long restrictedShipApprovalCount;		// 受限船核准数量
	private Long riskHazardIdentificationCount;		// 排查风险隐患数量
	private Long portEntryExitReportCount;		// 进出港报告艘次
	private Long penaltyDecisionCount;		// 处罚决定数量
	private Double penaltyAmount;		// 处罚金额
	private Long illegalScore;		// 违法计分

	@ExcelFields({
		@ExcelField(title="日期", attrName="reportDate", align=Align.CENTER, sort=20, dataFormat="yyyy-MM-dd"),
		@ExcelField(title="部门", attrName="departmentId.officeCode", align=Align.CENTER, sort=30),
		@ExcelField(title="海船安检艘次", attrName="seaShipInspectionCount", align=Align.CENTER, sort=40),
		@ExcelField(title="海船缺陷数量", attrName="seaShipDefectCount", align=Align.CENTER, sort=50),
		@ExcelField(title="海船滞留艘次", attrName="seaShipDetentionCount", align=Align.CENTER, sort=60),
		@ExcelField(title="内河船安检艘次", attrName="riverShipInspectionCount", align=Align.CENTER, sort=70),
		@ExcelField(title="内河船缺陷数量", attrName="riverShipDefectCount", align=Align.CENTER, sort=80),
		@ExcelField(title="内河船滞留数量", attrName="riverShipDetentionCount", align=Align.CENTER, sort=90),
		@ExcelField(title="PSC艘次", attrName="pscInspectionCount", align=Align.CENTER, sort=100),
		@ExcelField(title="PSC缺陷数量", attrName="pscDefectCount", align=Align.CENTER, sort=110),
		@ExcelField(title="PSC滞留艘次", attrName="pscDetentionCount", align=Align.CENTER, sort=120),
		@ExcelField(title="散装液体危险货物作业现场监督检查", attrName="bulkLiquidHazardousCargoSupervision", align=Align.CENTER, sort=130),
		@ExcelField(title="燃油快速抽样检测", attrName="fuelQuickSamplingInspection", align=Align.CENTER, sort=140),
		@ExcelField(title="立案调查数据", attrName="investigationCases", align=Align.CENTER, sort=150),
		@ExcelField(title="海巡艇巡航发现异常", attrName="patrolBoatAbnormalDiscovery", align=Align.CENTER, sort=160),
		@ExcelField(title="海巡艇巡航立案调查", attrName="patrolBoatInvestigationCases", align=Align.CENTER, sort=170),
		@ExcelField(title="无人机巡航发现异常", attrName="droneAbnormalDiscovery", align=Align.CENTER, sort=180),
		@ExcelField(title="无人机巡航立案调查", attrName="droneInvestigationCases", align=Align.CENTER, sort=190),
		@ExcelField(title="电子巡航发现异常", attrName="electronicPatrolAbnormalDiscovery", align=Align.CENTER, sort=200),
		@ExcelField(title="电子巡航立案调查", attrName="electronicPatrolInvestigationCases", align=Align.CENTER, sort=210),
		@ExcelField(title="受限船进", attrName="restrictedShipIn", align=Align.CENTER, sort=220),
		@ExcelField(title="受限船出", attrName="restrictedShipOut", align=Align.CENTER, sort=230),
		@ExcelField(title="cape船进", attrName="capeShipIn", align=Align.CENTER, sort=240),
		@ExcelField(title="cape船出", attrName="capeShipOut", align=Align.CENTER, sort=250),
		@ExcelField(title="险情数量", attrName="dangerCount", align=Align.CENTER, sort=260),
		@ExcelField(title="水工数量", attrName="hydraulicConstructionCount", align=Align.CENTER, sort=270),
		@ExcelField(title="受限船核准数量", attrName="restrictedShipApprovalCount", align=Align.CENTER, sort=280),
		@ExcelField(title="排查风险隐患数量", attrName="riskHazardIdentificationCount", align=Align.CENTER, sort=290),
		@ExcelField(title="进出港报告艘次", attrName="portEntryExitReportCount", align=Align.CENTER, sort=300),
		@ExcelField(title="处罚决定数量", attrName="penaltyDecisionCount", align=Align.CENTER, sort=310),
		@ExcelField(title="处罚金额", attrName="penaltyAmount", align=Align.CENTER, sort=320),
		@ExcelField(title="违法计分", attrName="illegalScore", align=Align.CENTER, sort=330),
	})
	public WeeklyReport() {
		this(null);
	}
	
	public WeeklyReport(String id){
		super(id);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	
	public Office getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Office departmentId) {
		this.departmentId = departmentId;
	}
	
	public Long getSeaShipInspectionCount() {
		return seaShipInspectionCount;
	}

	public void setSeaShipInspectionCount(Long seaShipInspectionCount) {
		this.seaShipInspectionCount = seaShipInspectionCount;
	}
	
	public Long getSeaShipDefectCount() {
		return seaShipDefectCount;
	}

	public void setSeaShipDefectCount(Long seaShipDefectCount) {
		this.seaShipDefectCount = seaShipDefectCount;
	}
	
	public Long getSeaShipDetentionCount() {
		return seaShipDetentionCount;
	}

	public void setSeaShipDetentionCount(Long seaShipDetentionCount) {
		this.seaShipDetentionCount = seaShipDetentionCount;
	}
	
	public Long getRiverShipInspectionCount() {
		return riverShipInspectionCount;
	}

	public void setRiverShipInspectionCount(Long riverShipInspectionCount) {
		this.riverShipInspectionCount = riverShipInspectionCount;
	}
	
	public Long getRiverShipDefectCount() {
		return riverShipDefectCount;
	}

	public void setRiverShipDefectCount(Long riverShipDefectCount) {
		this.riverShipDefectCount = riverShipDefectCount;
	}
	
	public Long getRiverShipDetentionCount() {
		return riverShipDetentionCount;
	}

	public void setRiverShipDetentionCount(Long riverShipDetentionCount) {
		this.riverShipDetentionCount = riverShipDetentionCount;
	}
	
	public Long getPscInspectionCount() {
		return pscInspectionCount;
	}

	public void setPscInspectionCount(Long pscInspectionCount) {
		this.pscInspectionCount = pscInspectionCount;
	}
	
	public Long getPscDefectCount() {
		return pscDefectCount;
	}

	public void setPscDefectCount(Long pscDefectCount) {
		this.pscDefectCount = pscDefectCount;
	}
	
	public Long getPscDetentionCount() {
		return pscDetentionCount;
	}

	public void setPscDetentionCount(Long pscDetentionCount) {
		this.pscDetentionCount = pscDetentionCount;
	}
	
	public Long getBulkLiquidHazardousCargoSupervision() {
		return bulkLiquidHazardousCargoSupervision;
	}

	public void setBulkLiquidHazardousCargoSupervision(Long bulkLiquidHazardousCargoSupervision) {
		this.bulkLiquidHazardousCargoSupervision = bulkLiquidHazardousCargoSupervision;
	}
	
	public Long getFuelQuickSamplingInspection() {
		return fuelQuickSamplingInspection;
	}

	public void setFuelQuickSamplingInspection(Long fuelQuickSamplingInspection) {
		this.fuelQuickSamplingInspection = fuelQuickSamplingInspection;
	}
	
	public Long getInvestigationCases() {
		return investigationCases;
	}

	public void setInvestigationCases(Long investigationCases) {
		this.investigationCases = investigationCases;
	}
	
	public Long getPatrolBoatAbnormalDiscovery() {
		return patrolBoatAbnormalDiscovery;
	}

	public void setPatrolBoatAbnormalDiscovery(Long patrolBoatAbnormalDiscovery) {
		this.patrolBoatAbnormalDiscovery = patrolBoatAbnormalDiscovery;
	}
	
	public Long getPatrolBoatInvestigationCases() {
		return patrolBoatInvestigationCases;
	}

	public void setPatrolBoatInvestigationCases(Long patrolBoatInvestigationCases) {
		this.patrolBoatInvestigationCases = patrolBoatInvestigationCases;
	}
	
	public Long getDroneAbnormalDiscovery() {
		return droneAbnormalDiscovery;
	}

	public void setDroneAbnormalDiscovery(Long droneAbnormalDiscovery) {
		this.droneAbnormalDiscovery = droneAbnormalDiscovery;
	}
	
	public Long getDroneInvestigationCases() {
		return droneInvestigationCases;
	}

	public void setDroneInvestigationCases(Long droneInvestigationCases) {
		this.droneInvestigationCases = droneInvestigationCases;
	}
	
	public Long getElectronicPatrolAbnormalDiscovery() {
		return electronicPatrolAbnormalDiscovery;
	}

	public void setElectronicPatrolAbnormalDiscovery(Long electronicPatrolAbnormalDiscovery) {
		this.electronicPatrolAbnormalDiscovery = electronicPatrolAbnormalDiscovery;
	}
	
	public Long getElectronicPatrolInvestigationCases() {
		return electronicPatrolInvestigationCases;
	}

	public void setElectronicPatrolInvestigationCases(Long electronicPatrolInvestigationCases) {
		this.electronicPatrolInvestigationCases = electronicPatrolInvestigationCases;
	}
	
	public Long getRestrictedShipIn() {
		return restrictedShipIn;
	}

	public void setRestrictedShipIn(Long restrictedShipIn) {
		this.restrictedShipIn = restrictedShipIn;
	}
	
	public Long getRestrictedShipOut() {
		return restrictedShipOut;
	}

	public void setRestrictedShipOut(Long restrictedShipOut) {
		this.restrictedShipOut = restrictedShipOut;
	}
	
	public Long getCapeShipIn() {
		return capeShipIn;
	}

	public void setCapeShipIn(Long capeShipIn) {
		this.capeShipIn = capeShipIn;
	}
	
	public Long getCapeShipOut() {
		return capeShipOut;
	}

	public void setCapeShipOut(Long capeShipOut) {
		this.capeShipOut = capeShipOut;
	}
	
	public Long getDangerCount() {
		return dangerCount;
	}

	public void setDangerCount(Long dangerCount) {
		this.dangerCount = dangerCount;
	}
	
	public Long getHydraulicConstructionCount() {
		return hydraulicConstructionCount;
	}

	public void setHydraulicConstructionCount(Long hydraulicConstructionCount) {
		this.hydraulicConstructionCount = hydraulicConstructionCount;
	}
	
	public Long getRestrictedShipApprovalCount() {
		return restrictedShipApprovalCount;
	}

	public void setRestrictedShipApprovalCount(Long restrictedShipApprovalCount) {
		this.restrictedShipApprovalCount = restrictedShipApprovalCount;
	}
	
	public Long getRiskHazardIdentificationCount() {
		return riskHazardIdentificationCount;
	}

	public void setRiskHazardIdentificationCount(Long riskHazardIdentificationCount) {
		this.riskHazardIdentificationCount = riskHazardIdentificationCount;
	}
	
	public Long getPortEntryExitReportCount() {
		return portEntryExitReportCount;
	}

	public void setPortEntryExitReportCount(Long portEntryExitReportCount) {
		this.portEntryExitReportCount = portEntryExitReportCount;
	}
	
	public Long getPenaltyDecisionCount() {
		return penaltyDecisionCount;
	}

	public void setPenaltyDecisionCount(Long penaltyDecisionCount) {
		this.penaltyDecisionCount = penaltyDecisionCount;
	}
	
	public Double getPenaltyAmount() {
		return penaltyAmount;
	}

	public void setPenaltyAmount(Double penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}
	
	public Long getIllegalScore() {
		return illegalScore;
	}

	public void setIllegalScore(Long illegalScore) {
		this.illegalScore = illegalScore;
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