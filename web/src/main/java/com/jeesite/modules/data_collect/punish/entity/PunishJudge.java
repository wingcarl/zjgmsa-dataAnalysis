package com.jeesite.modules.data_collect.punish.entity;

import javax.validation.constraints.Size;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;

/**
 * 处罚决定Entity
 * @author 王浩宇
 * @version 2025-02-07
 */
@Table(name="punish_judge", alias="a", label="处罚决定信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="penalty_type", attrName="penaltyType", label="处罚类型"),
		@Column(name="water_category", attrName="waterCategory", label="水域类别"),
		@Column(name="case_number", attrName="caseNumber", label="案号"),
		@Column(name="case_reason", attrName="caseReason", label="案由"),
		@Column(name="management_category", attrName="managementCategory", label="管理分类"),
		@Column(name="violation_discovery_location", attrName="violationDiscoveryLocation", label="违法发现地点"),
		@Column(name="violation_discovery_time", attrName="violationDiscoveryTime", label="违法发现时间", isUpdateForce=true),
		@Column(name="case_source", attrName="caseSource", label="案件来源"),
		@Column(name="task_source", attrName="taskSource", label="任务来源"),
		@Column(name="case_status", attrName="caseStatus", label="案件状态"),
		@Column(name="filing_officer", attrName="filingOfficer", label="立案执法人员"),
		@Column(name="filing_time", attrName="filingTime", label="立案时间", isUpdateForce=true),
		@Column(name="penalty_decision_time", attrName="penaltyDecisionTime", label="处罚决定时间", isUpdateForce=true),
		@Column(name="case_closing_time", attrName="caseClosingTime", label="结案时间", isUpdateForce=true),
		@Column(name="penalty_type_applied", attrName="penaltyTypeApplied", label="处罚种类"),
		@Column(name="violation_circumstances", attrName="violationCircumstances", label="违法情节"),
		@Column(name="party_type", attrName="partyType", label="当事人类别"),
		@Column(name="penalty_object_name", attrName="penaltyObjectName", label="处罚对象名称", queryType=QueryType.LIKE),
		@Column(name="penalty_amount", attrName="penaltyAmount", label="处罚金额", isUpdateForce=true),
		@Column(name="penalty_agency", attrName="penaltyAgency", label="处罚机构"),
		@Column(name="investigation_officer", attrName="investigationOfficer", label="调查执法人员"),
		@Column(name="receipt_delivery_time", attrName="receiptDeliveryTime", label="决定书送达回执时间", isUpdateForce=true),
		@Column(name="ship_name", attrName="shipName", label="船舶名称", queryType=QueryType.LIKE),
		@Column(name="port_registry", attrName="portRegistry", label="船籍港"),
		@Column(name="ship_gross_tonnage", attrName="shipGrossTonnage", label="船舶总吨", comment="船舶总吨(GT)", isUpdateForce=true),
		@Column(name="ship_type", attrName="shipType", label="船舶种类"),
		@Column(name="engine_power", attrName="enginePower", label="主机功率", comment="主机功率(KW)", isUpdateForce=true),
		@Column(name="sea_river_ship", attrName="seaRiverShip", label="海内河船"),
		@Column(name="investigation_time", attrName="investigationTime", label="调查时间", isUpdateForce=true),
		@Column(name="notification_time", attrName="notificationTime", label="通知时间", isUpdateForce=true),
		@Column(name="penalty_payment_number", attrName="penaltyPaymentNumber", label="缴纳罚款单号"),
		@Column(name="ship_identification_number", attrName="shipIdentificationNumber", label="船舶识别号"),
		@Column(name="party_social_credit_code", attrName="partySocialCreditCode", label="当事人社会信用代码"),
		@Column(name="ship_name_english", attrName="shipNameEnglish", label="英文船名", queryType=QueryType.LIKE),
		@Column(name="mmsi_number", attrName="mmsiNumber", label="MMSI编号"),
		@Column(name="imo_number", attrName="imoNumber", label="IMO编号"),
		@Column(name="party_id", attrName="partyId", label="当事人身份证"),
		@Column(name="current_handler", attrName="currentHandler", label="当前处理人"),
		@Column(name="special_circumstances", attrName="specialCircumstances", label="特殊情况"),
		@Column(name="filing_submission_time", attrName="filingSubmissionTime", label="立案提交时间", isUpdateForce=true),
		@Column(name="illegal_income", attrName="illegalIncome", label="没收违法所得", isUpdateForce=true),
		@Column(name="no_penalty_type", attrName="noPenaltyType", label="不予处罚类型"),
		@Column(name="actual_penalty", attrName="actualPenalty", label="实收罚款", isUpdateForce=true),
		@Column(name="actual_illegal_income", attrName="actualIllegalIncome", label="实收没收违法所得", isUpdateForce=true),
		@Column(name="subject_category", attrName="subjectCategory", label="主体类别"),
		@Column(name="violation_location", attrName="violationLocation", label="违法发生地点"),
		@Column(name="penalty_object_location", attrName="penaltyObjectLocation", label="处罚对象所在地"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC", extWhereKeys="departmentWhere"
)
public class PunishJudge extends DataEntity<PunishJudge> {
	
	private static final long serialVersionUID = 1L;
	private String penaltyType;		// 处罚类型
	private String waterCategory;		// 水域类别
	private String caseNumber;		// 案号
	private String caseReason;		// 案由
	private String managementCategory;		// 管理分类
	private String violationDiscoveryLocation;		// 违法发现地点
	private Date violationDiscoveryTime;		// 违法发现时间
	private String caseSource;		// 案件来源
	private String taskSource;		// 任务来源
	private String caseStatus;		// 案件状态
	private String filingOfficer;		// 立案执法人员
	private Date filingTime;		// 立案时间
	private Date penaltyDecisionTime;		// 处罚决定时间
	private Date caseClosingTime;		// 结案时间
	private String penaltyTypeApplied;		// 处罚种类
	private String violationCircumstances;		// 违法情节
	private String partyType;		// 当事人类别
	private String penaltyObjectName;		// 处罚对象名称
	private Double penaltyAmount;		// 处罚金额
	private String penaltyAgency;		// 处罚机构
	private String investigationOfficer;		// 调查执法人员
	private Date receiptDeliveryTime;		// 决定书送达回执时间
	private String shipName;		// 船舶名称
	private String portRegistry;		// 船籍港
	private Long shipGrossTonnage;		// 船舶总吨(GT)
	private String shipType;		// 船舶种类
	private Long enginePower;		// 主机功率(KW)
	private String seaRiverShip;		// 海内河船
	private Date investigationTime;		// 调查时间
	private Date notificationTime;		// 通知时间
	private String penaltyPaymentNumber;		// 缴纳罚款单号
	private String shipIdentificationNumber;		// 船舶识别号
	private String partySocialCreditCode;		// 当事人社会信用代码
	private String shipNameEnglish;		// 英文船名
	private String mmsiNumber;		// MMSI编号
	private String imoNumber;		// IMO编号
	private String partyId;		// 当事人身份证
	private String currentHandler;		// 当前处理人
	private String specialCircumstances;		// 特殊情况
	private Date filingSubmissionTime;		// 立案提交时间
	private Double illegalIncome;		// 没收违法所得
	private String noPenaltyType;		// 不予处罚类型
	private Double actualPenalty;		// 实收罚款
	private Double actualIllegalIncome;		// 实收没收违法所得
	private String subjectCategory;		// 主体类别
	private String violationLocation;		// 违法发生地点
	private String penaltyObjectLocation;		// 处罚对象所在地

	// 查询范围条件
	private Date penaltyDecisionTime_gte;		// 处罚决定时间-起始
	private Date penaltyDecisionTime_lte;		// 处罚决定时间-结束
	private Double penaltyAmount_gte;		// 处罚金额-最小值
	private Double penaltyAmount_lte;		// 处罚金额-最大值
	
	// 部门筛选条件（用于关联agency_dept表）
	private String department;		// 部门筛选

	@ExcelFields({
		@ExcelField(title="处罚类型", attrName="penaltyType", align=Align.CENTER, sort=20),
		@ExcelField(title="水域类别", attrName="waterCategory", align=Align.CENTER, sort=30),
		@ExcelField(title="案号", attrName="caseNumber", align=Align.CENTER, sort=40),
		@ExcelField(title="案由", attrName="caseReason", align=Align.CENTER, sort=50),
		@ExcelField(title="管理分类", attrName="managementCategory", align=Align.CENTER, sort=60),
		@ExcelField(title="违法发现地点", attrName="violationDiscoveryLocation", align=Align.CENTER, sort=70),
		@ExcelField(title="违法发现时间", attrName="violationDiscoveryTime", align=Align.CENTER, sort=80, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="案件来源", attrName="caseSource", align=Align.CENTER, sort=90),
		@ExcelField(title="任务来源", attrName="taskSource", align=Align.CENTER, sort=100),
		@ExcelField(title="案件状态", attrName="caseStatus", align=Align.CENTER, sort=110),
		@ExcelField(title="立案执法人员", attrName="filingOfficer", align=Align.CENTER, sort=120),
		@ExcelField(title="立案时间", attrName="filingTime", align=Align.CENTER, sort=130, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="处罚决定时间", attrName="penaltyDecisionTime", align=Align.CENTER, sort=140, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="结案时间", attrName="caseClosingTime", align=Align.CENTER, sort=150, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="处罚种类", attrName="penaltyTypeApplied", align=Align.CENTER, sort=160),
		@ExcelField(title="违法情节", attrName="violationCircumstances", align=Align.CENTER, sort=170),
		@ExcelField(title="当事人类别", attrName="partyType", align=Align.CENTER, sort=180),
		@ExcelField(title="处罚对象名称", attrName="penaltyObjectName", align=Align.CENTER, sort=190),
		@ExcelField(title="处罚金额", attrName="penaltyAmount", align=Align.CENTER, sort=200),
		@ExcelField(title="处罚机构", attrName="penaltyAgency", align=Align.CENTER, sort=210),
		@ExcelField(title="调查执法人员", attrName="investigationOfficer", align=Align.CENTER, sort=220),
		@ExcelField(title="决定书送达回执时间", attrName="receiptDeliveryTime", align=Align.CENTER, sort=230, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="船舶名称", attrName="shipName", align=Align.CENTER, sort=240),
		@ExcelField(title="船籍港", attrName="portRegistry", align=Align.CENTER, sort=250),
		@ExcelField(title="船舶总吨", attrName="shipGrossTonnage", align=Align.CENTER, sort=260),
		@ExcelField(title="船舶种类", attrName="shipType", align=Align.CENTER, sort=270),
		@ExcelField(title="主机功率", attrName="enginePower", align=Align.CENTER, sort=280),
		@ExcelField(title="海内河船", attrName="seaRiverShip", align=Align.CENTER, sort=290),
		@ExcelField(title="调查时间", attrName="investigationTime", align=Align.CENTER, sort=300, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="通知时间", attrName="notificationTime", align=Align.CENTER, sort=310, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="缴纳罚款单号", attrName="penaltyPaymentNumber", align=Align.CENTER, sort=320),
		@ExcelField(title="船舶识别号", attrName="shipIdentificationNumber", align=Align.CENTER, sort=330),
		@ExcelField(title="当事人社会信用代码", attrName="partySocialCreditCode", align=Align.CENTER, sort=340),
		@ExcelField(title="英文船名", attrName="shipNameEnglish", align=Align.CENTER, sort=350),
		@ExcelField(title="MMSI编号", attrName="mmsiNumber", align=Align.CENTER, sort=360),
		@ExcelField(title="IMO编号", attrName="imoNumber", align=Align.CENTER, sort=370),
		@ExcelField(title="当事人身份证", attrName="partyId", align=Align.CENTER, sort=380),
		@ExcelField(title="当前处理人", attrName="currentHandler", align=Align.CENTER, sort=390),
		@ExcelField(title="特殊情况", attrName="specialCircumstances", align=Align.CENTER, sort=400),
		@ExcelField(title="立案提交时间", attrName="filingSubmissionTime", align=Align.CENTER, sort=410, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="没收违法所得", attrName="illegalIncome", align=Align.CENTER, sort=420),
		@ExcelField(title="不予处罚类型", attrName="noPenaltyType", align=Align.CENTER, sort=430),
		@ExcelField(title="实收罚款", attrName="actualPenalty", align=Align.CENTER, sort=440),
		@ExcelField(title="实收没收违法所得", attrName="actualIllegalIncome", align=Align.CENTER, sort=450),
		@ExcelField(title="主体类别", attrName="subjectCategory", align=Align.CENTER, sort=460),
		@ExcelField(title="违法发生地点", attrName="violationLocation", align=Align.CENTER, sort=470),
		@ExcelField(title="处罚对象所在地", attrName="penaltyObjectLocation", align=Align.CENTER, sort=480),
	})
	public PunishJudge() {
		this(null);
	}
	
	public PunishJudge(String id){
		super(id);
	}
	
	@Size(min=0, max=255, message="处罚类型长度不能超过 255 个字符")
	public String getPenaltyType() {
		return penaltyType;
	}

	public void setPenaltyType(String penaltyType) {
		this.penaltyType = penaltyType;
	}
	
	@Size(min=0, max=255, message="水域类别长度不能超过 255 个字符")
	public String getWaterCategory() {
		return waterCategory;
	}

	public void setWaterCategory(String waterCategory) {
		this.waterCategory = waterCategory;
	}
	
	@Size(min=0, max=255, message="案号长度不能超过 255 个字符")
	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	
	@Size(min=0, max=255, message="案由长度不能超过 255 个字符")
	public String getCaseReason() {
		return caseReason;
	}

	public void setCaseReason(String caseReason) {
		this.caseReason = caseReason;
	}
	
	@Size(min=0, max=255, message="管理分类长度不能超过 255 个字符")
	public String getManagementCategory() {
		return managementCategory;
	}

	public void setManagementCategory(String managementCategory) {
		this.managementCategory = managementCategory;
	}
	
	@Size(min=0, max=255, message="违法发现地点长度不能超过 255 个字符")
	public String getViolationDiscoveryLocation() {
		return violationDiscoveryLocation;
	}

	public void setViolationDiscoveryLocation(String violationDiscoveryLocation) {
		this.violationDiscoveryLocation = violationDiscoveryLocation;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getViolationDiscoveryTime() {
		return violationDiscoveryTime;
	}

	public void setViolationDiscoveryTime(Date violationDiscoveryTime) {
		this.violationDiscoveryTime = violationDiscoveryTime;
	}
	
	@Size(min=0, max=255, message="案件来源长度不能超过 255 个字符")
	public String getCaseSource() {
		return caseSource;
	}

	public void setCaseSource(String caseSource) {
		this.caseSource = caseSource;
	}
	
	@Size(min=0, max=255, message="任务来源长度不能超过 255 个字符")
	public String getTaskSource() {
		return taskSource;
	}

	public void setTaskSource(String taskSource) {
		this.taskSource = taskSource;
	}
	
	@Size(min=0, max=255, message="案件状态长度不能超过 255 个字符")
	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	
	@Size(min=0, max=255, message="立案执法人员长度不能超过 255 个字符")
	public String getFilingOfficer() {
		return filingOfficer;
	}

	public void setFilingOfficer(String filingOfficer) {
		this.filingOfficer = filingOfficer;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFilingTime() {
		return filingTime;
	}

	public void setFilingTime(Date filingTime) {
		this.filingTime = filingTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPenaltyDecisionTime() {
		return penaltyDecisionTime;
	}

	public void setPenaltyDecisionTime(Date penaltyDecisionTime) {
		this.penaltyDecisionTime = penaltyDecisionTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseClosingTime() {
		return caseClosingTime;
	}

	public void setCaseClosingTime(Date caseClosingTime) {
		this.caseClosingTime = caseClosingTime;
	}
	
	@Size(min=0, max=255, message="处罚种类长度不能超过 255 个字符")
	public String getPenaltyTypeApplied() {
		return penaltyTypeApplied;
	}

	public void setPenaltyTypeApplied(String penaltyTypeApplied) {
		this.penaltyTypeApplied = penaltyTypeApplied;
	}
	
	@Size(min=0, max=255, message="违法情节长度不能超过 255 个字符")
	public String getViolationCircumstances() {
		return violationCircumstances;
	}

	public void setViolationCircumstances(String violationCircumstances) {
		this.violationCircumstances = violationCircumstances;
	}
	
	@Size(min=0, max=255, message="当事人类别长度不能超过 255 个字符")
	public String getPartyType() {
		return partyType;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}
	
	@Size(min=0, max=255, message="处罚对象名称长度不能超过 255 个字符")
	public String getPenaltyObjectName() {
		return penaltyObjectName;
	}

	public void setPenaltyObjectName(String penaltyObjectName) {
		this.penaltyObjectName = penaltyObjectName;
	}
	
	public Double getPenaltyAmount() {
		return penaltyAmount;
	}

	public void setPenaltyAmount(Double penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}
	
	@Size(min=0, max=255, message="处罚机构长度不能超过 255 个字符")
	public String getPenaltyAgency() {
		return penaltyAgency;
	}

	public void setPenaltyAgency(String penaltyAgency) {
		this.penaltyAgency = penaltyAgency;
	}
	
	@Size(min=0, max=255, message="调查执法人员长度不能超过 255 个字符")
	public String getInvestigationOfficer() {
		return investigationOfficer;
	}

	public void setInvestigationOfficer(String investigationOfficer) {
		this.investigationOfficer = investigationOfficer;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReceiptDeliveryTime() {
		return receiptDeliveryTime;
	}

	public void setReceiptDeliveryTime(Date receiptDeliveryTime) {
		this.receiptDeliveryTime = receiptDeliveryTime;
	}
	
	@Size(min=0, max=255, message="船舶名称长度不能超过 255 个字符")
	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	
	@Size(min=0, max=255, message="船籍港长度不能超过 255 个字符")
	public String getPortRegistry() {
		return portRegistry;
	}

	public void setPortRegistry(String portRegistry) {
		this.portRegistry = portRegistry;
	}
	
	public Long getShipGrossTonnage() {
		return shipGrossTonnage;
	}

	public void setShipGrossTonnage(Long shipGrossTonnage) {
		this.shipGrossTonnage = shipGrossTonnage;
	}
	
	@Size(min=0, max=255, message="船舶种类长度不能超过 255 个字符")
	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	
	public Long getEnginePower() {
		return enginePower;
	}

	public void setEnginePower(Long enginePower) {
		this.enginePower = enginePower;
	}
	
	@Size(min=0, max=255, message="海内河船长度不能超过 255 个字符")
	public String getSeaRiverShip() {
		return seaRiverShip;
	}

	public void setSeaRiverShip(String seaRiverShip) {
		this.seaRiverShip = seaRiverShip;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInvestigationTime() {
		return investigationTime;
	}

	public void setInvestigationTime(Date investigationTime) {
		this.investigationTime = investigationTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getNotificationTime() {
		return notificationTime;
	}

	public void setNotificationTime(Date notificationTime) {
		this.notificationTime = notificationTime;
	}
	
	@Size(min=0, max=255, message="缴纳罚款单号长度不能超过 255 个字符")
	public String getPenaltyPaymentNumber() {
		return penaltyPaymentNumber;
	}

	public void setPenaltyPaymentNumber(String penaltyPaymentNumber) {
		this.penaltyPaymentNumber = penaltyPaymentNumber;
	}
	
	@Size(min=0, max=255, message="船舶识别号长度不能超过 255 个字符")
	public String getShipIdentificationNumber() {
		return shipIdentificationNumber;
	}

	public void setShipIdentificationNumber(String shipIdentificationNumber) {
		this.shipIdentificationNumber = shipIdentificationNumber;
	}
	
	@Size(min=0, max=255, message="当事人社会信用代码长度不能超过 255 个字符")
	public String getPartySocialCreditCode() {
		return partySocialCreditCode;
	}

	public void setPartySocialCreditCode(String partySocialCreditCode) {
		this.partySocialCreditCode = partySocialCreditCode;
	}
	
	@Size(min=0, max=255, message="英文船名长度不能超过 255 个字符")
	public String getShipNameEnglish() {
		return shipNameEnglish;
	}

	public void setShipNameEnglish(String shipNameEnglish) {
		this.shipNameEnglish = shipNameEnglish;
	}
	
	@Size(min=0, max=255, message="MMSI编号长度不能超过 255 个字符")
	public String getMmsiNumber() {
		return mmsiNumber;
	}

	public void setMmsiNumber(String mmsiNumber) {
		this.mmsiNumber = mmsiNumber;
	}
	
	@Size(min=0, max=255, message="IMO编号长度不能超过 255 个字符")
	public String getImoNumber() {
		return imoNumber;
	}

	public void setImoNumber(String imoNumber) {
		this.imoNumber = imoNumber;
	}
	
	@Size(min=0, max=255, message="当事人身份证长度不能超过 255 个字符")
	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	
	@Size(min=0, max=255, message="当前处理人长度不能超过 255 个字符")
	public String getCurrentHandler() {
		return currentHandler;
	}

	public void setCurrentHandler(String currentHandler) {
		this.currentHandler = currentHandler;
	}
	
	public String getSpecialCircumstances() {
		return specialCircumstances;
	}

	public void setSpecialCircumstances(String specialCircumstances) {
		this.specialCircumstances = specialCircumstances;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFilingSubmissionTime() {
		return filingSubmissionTime;
	}

	public void setFilingSubmissionTime(Date filingSubmissionTime) {
		this.filingSubmissionTime = filingSubmissionTime;
	}
	
	public Double getIllegalIncome() {
		return illegalIncome;
	}

	public void setIllegalIncome(Double illegalIncome) {
		this.illegalIncome = illegalIncome;
	}
	
	@Size(min=0, max=255, message="不予处罚类型长度不能超过 255 个字符")
	public String getNoPenaltyType() {
		return noPenaltyType;
	}

	public void setNoPenaltyType(String noPenaltyType) {
		this.noPenaltyType = noPenaltyType;
	}
	
	public Double getActualPenalty() {
		return actualPenalty;
	}

	public void setActualPenalty(Double actualPenalty) {
		this.actualPenalty = actualPenalty;
	}
	
	public Double getActualIllegalIncome() {
		return actualIllegalIncome;
	}

	public void setActualIllegalIncome(Double actualIllegalIncome) {
		this.actualIllegalIncome = actualIllegalIncome;
	}
	
	@Size(min=0, max=255, message="主体类别长度不能超过 255 个字符")
	public String getSubjectCategory() {
		return subjectCategory;
	}

	public void setSubjectCategory(String subjectCategory) {
		this.subjectCategory = subjectCategory;
	}
	
	@Size(min=0, max=255, message="违法发生地点长度不能超过 255 个字符")
	public String getViolationLocation() {
		return violationLocation;
	}

	public void setViolationLocation(String violationLocation) {
		this.violationLocation = violationLocation;
	}
	
	@Size(min=0, max=255, message="处罚对象所在地长度不能超过 255 个字符")
	public String getPenaltyObjectLocation() {
		return penaltyObjectLocation;
	}

	public void setPenaltyObjectLocation(String penaltyObjectLocation) {
		this.penaltyObjectLocation = penaltyObjectLocation;
	}
	
	public Date getPenaltyDecisionTime_gte() {
		return sqlMap.getWhere().getValue("penalty_decision_time", QueryType.GTE);
	}

	public void setPenaltyDecisionTime_gte(Date penaltyDecisionTime) {
		this.penaltyDecisionTime_gte = penaltyDecisionTime;
		sqlMap.getWhere().and("penalty_decision_time", QueryType.GTE, penaltyDecisionTime);
	}
	
	public Date getPenaltyDecisionTime_lte() {
		return sqlMap.getWhere().getValue("penalty_decision_time", QueryType.LTE);
	}

	public void setPenaltyDecisionTime_lte(Date penaltyDecisionTime) {
		this.penaltyDecisionTime_lte = penaltyDecisionTime;
		sqlMap.getWhere().and("penalty_decision_time", QueryType.LTE, penaltyDecisionTime);
	}
	
	public Double getPenaltyAmount_gte() {
		return penaltyAmount_gte;
	}

	public void setPenaltyAmount_gte(Double penaltyAmount_gte) {
		this.penaltyAmount_gte = penaltyAmount_gte;
		sqlMap.getWhere().and("penalty_amount", QueryType.GTE, penaltyAmount_gte);
	}

	public Double getPenaltyAmount_lte() {
		return penaltyAmount_lte;
	}

	public void setPenaltyAmount_lte(Double penaltyAmount_lte) {
		this.penaltyAmount_lte = penaltyAmount_lte;
		sqlMap.getWhere().and("penalty_amount", QueryType.LTE, penaltyAmount_lte);
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
		// 添加自定义查询条件，通过agency_dept表关联查询
		if (com.jeesite.common.lang.StringUtils.isNotBlank(department)) {
			String extWhere = "AND EXISTS (SELECT 1 FROM agency_dept ad WHERE ad.agency = a.penalty_agency AND ad.dept = '" + department + "')";
			sqlMap().add("departmentWhere", extWhere);
		}
	}
	
}