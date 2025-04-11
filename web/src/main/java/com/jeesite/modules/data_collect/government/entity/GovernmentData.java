package com.jeesite.modules.data_collect.government.entity;

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
 * 政务服务数据Entity
 * @author 王浩宇
 * @version 2025-04-10
 */
@Table(name="government_data", alias="a", label="政务服务数据信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="data_time", attrName="dataTime", label="数据时间", isUpdateForce=true),
		@Column(name="crew_certificate_issuance", attrName="crewCertificateIssuance", label="船员证书签发", comment="船员证书签发（本）受理", isQuery=false, isUpdateForce=true),
		@Column(name="ship_registration_preliminary_review", attrName="shipRegistrationPreliminaryReview", label="初审船舶登记", comment="初审船舶登记（艘次）", isQuery=false, isUpdateForce=true),
		@Column(name="ship_radio_license_issuance", attrName="shipRadioLicenseIssuance", label="船舶制式无线电台执照", isQuery=false, isUpdateForce=true),
		@Column(name="ship_dangerous_cargo_approval", attrName="shipDangerousCargoApproval", label="船载危险货物申报审批", comment="船载危险货物申报审批（件）", isQuery=false, isUpdateForce=true),
		@Column(name="ship_dangerous_cargo_port_entry_exit_approval", attrName="shipDangerousCargoPortEntryExitApproval", label="船载危险货物进出港口审批", comment="船载危险货物进出港口审批（件）", isQuery=false, isUpdateForce=true),
		@Column(name="international_ship_certificate_online_filing", attrName="internationalShipCertificateOnlineFiling", label="国际航行船舶证书网上备案", comment="国际航行船舶证书网上备案（件）", isQuery=false, isUpdateForce=true),
		@Column(name="international_ship_inspection", attrName="internationalShipInspection", label="国际航行船舶进出口岸查验", comment="国际航行船舶进出口岸查验（艘次）", isQuery=false, isUpdateForce=true),
		@Column(name="international_ship_port_entry_approval", attrName="internationalShipPortEntryApproval", label="国际航行船舶进口岸审批", comment="国际航行船舶进口岸审批（艘）", isQuery=false, isUpdateForce=true),
		@Column(name="water_area_construction_permit", attrName="waterAreaConstructionPermit", label="海域或者内河通航水域、岸线施工作业许可", comment="海域或者内河通航水域、岸线施工作业许可（件）", isQuery=false, isUpdateForce=true),
		@Column(name="inland_ship_tank_cleaning_verification", attrName="inlandShipTankCleaningVerification", label="内河船舶洗舱作业核查", comment="内河船舶洗舱作业核查（艘次）", isQuery=false, isUpdateForce=true),
		@Column(name="tank_cleaning_declaration", attrName="tankCleaningDeclaration", label="洗舱作业申报", comment="洗舱作业申报（艘次）", isQuery=false, isUpdateForce=true),
		@Column(name="cargo_exchange_declaration", attrName="cargoExchangeDeclaration", label="换货申报", comment="换货申报（艘次）", isQuery=false, isUpdateForce=true),
		@Column(name="tank_cleaning_declaration_issues", attrName="tankCleaningDeclarationIssues", label="发现洗舱申报问题", comment="发现洗舱申报问题（艘次）", isQuery=false, isUpdateForce=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class GovernmentData extends DataEntity<GovernmentData> {
	
	private static final long serialVersionUID = 1L;
	private Date dataTime;		// 数据时间
	private Long crewCertificateIssuance;		// 船员证书签发（本）受理
	private Long shipRegistrationPreliminaryReview;		// 初审船舶登记（艘次）
	private Long shipRadioLicenseIssuance;		// 船舶制式无线电台执照
	//private Long shipIdentificationCodeIssuance;		// 船舶标识码核发
	private Long shipDangerousCargoApproval;		// 船载危险货物申报审批（件）
	private Long shipDangerousCargoPortEntryExitApproval;		// 船载危险货物进出港口审批（件）
	private Long internationalShipCertificateOnlineFiling;		// 国际航行船舶证书网上备案（件）
	private Long internationalShipInspection;		// 国际航行船舶进出口岸查验（艘次）
	private Long internationalShipPortEntryApproval;		// 国际航行船舶进口岸审批（艘）
	private Long waterAreaConstructionPermit;		// 海域或者内河通航水域、岸线施工作业许可（件）
	private Long inlandShipTankCleaningVerification;		// 内河船舶洗舱作业核查（艘次）
	private Long tankCleaningDeclaration;		// 洗舱作业申报（艘次）
	private Long cargoExchangeDeclaration;		// 换货申报（艘次）
	private Long tankCleaningDeclarationIssues;		// 发现洗舱申报问题（艘次）

	@ExcelFields({
		@ExcelField(title="数据时间", attrName="dataTime", align=Align.CENTER, sort=20, dataFormat="yyyy-MM-dd"),
		@ExcelField(title="船员证书签发", attrName="crewCertificateIssuance", align=Align.CENTER, sort=30),
		@ExcelField(title="初审船舶登记", attrName="shipRegistrationPreliminaryReview", align=Align.CENTER, sort=40),
		@ExcelField(title="船舶制式无线电台执照船舶标识码核发", attrName="shipRadioLicenseIssuance", align=Align.CENTER, sort=50),
		@ExcelField(title="船载危险货物申报审批", attrName="shipDangerousCargoApproval", align=Align.CENTER, sort=70),
		@ExcelField(title="船载危险货物进出港口审批", attrName="shipDangerousCargoPortEntryExitApproval", align=Align.CENTER, sort=80),
		@ExcelField(title="国际航行船舶证书网上备案", attrName="internationalShipCertificateOnlineFiling", align=Align.CENTER, sort=90),
		@ExcelField(title="国际航行船舶进出口岸查验", attrName="internationalShipInspection", align=Align.CENTER, sort=100),
		@ExcelField(title="国际航行船舶进口岸审批", attrName="internationalShipPortEntryApproval", align=Align.CENTER, sort=110),
		@ExcelField(title="海域或者内河通航水域、岸线施工作业许可", attrName="waterAreaConstructionPermit", align=Align.CENTER, sort=120),
		@ExcelField(title="内河船舶洗舱作业核查", attrName="inlandShipTankCleaningVerification", align=Align.CENTER, sort=130),
		@ExcelField(title="洗舱作业申报", attrName="tankCleaningDeclaration", align=Align.CENTER, sort=140),
		@ExcelField(title="换货申报", attrName="cargoExchangeDeclaration", align=Align.CENTER, sort=150),
		@ExcelField(title="发现洗舱申报问题", attrName="tankCleaningDeclarationIssues", align=Align.CENTER, sort=160),
	})
	public GovernmentData() {
		this(null);
	}
	
	public GovernmentData(String id){
		super(id);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}
	
	public Long getCrewCertificateIssuance() {
		return crewCertificateIssuance;
	}

	public void setCrewCertificateIssuance(Long crewCertificateIssuance) {
		this.crewCertificateIssuance = crewCertificateIssuance;
	}
	
	public Long getShipRegistrationPreliminaryReview() {
		return shipRegistrationPreliminaryReview;
	}

	public void setShipRegistrationPreliminaryReview(Long shipRegistrationPreliminaryReview) {
		this.shipRegistrationPreliminaryReview = shipRegistrationPreliminaryReview;
	}
	
	public Long getShipRadioLicenseIssuance() {
		return shipRadioLicenseIssuance;
	}

	public void setShipRadioLicenseIssuance(Long shipRadioLicenseIssuance) {
		this.shipRadioLicenseIssuance = shipRadioLicenseIssuance;
	}
	

	public Long getShipDangerousCargoApproval() {
		return shipDangerousCargoApproval;
	}

	public void setShipDangerousCargoApproval(Long shipDangerousCargoApproval) {
		this.shipDangerousCargoApproval = shipDangerousCargoApproval;
	}
	
	public Long getShipDangerousCargoPortEntryExitApproval() {
		return shipDangerousCargoPortEntryExitApproval;
	}

	public void setShipDangerousCargoPortEntryExitApproval(Long shipDangerousCargoPortEntryExitApproval) {
		this.shipDangerousCargoPortEntryExitApproval = shipDangerousCargoPortEntryExitApproval;
	}
	
	public Long getInternationalShipCertificateOnlineFiling() {
		return internationalShipCertificateOnlineFiling;
	}

	public void setInternationalShipCertificateOnlineFiling(Long internationalShipCertificateOnlineFiling) {
		this.internationalShipCertificateOnlineFiling = internationalShipCertificateOnlineFiling;
	}
	
	public Long getInternationalShipInspection() {
		return internationalShipInspection;
	}

	public void setInternationalShipInspection(Long internationalShipInspection) {
		this.internationalShipInspection = internationalShipInspection;
	}
	
	public Long getInternationalShipPortEntryApproval() {
		return internationalShipPortEntryApproval;
	}

	public void setInternationalShipPortEntryApproval(Long internationalShipPortEntryApproval) {
		this.internationalShipPortEntryApproval = internationalShipPortEntryApproval;
	}
	
	public Long getWaterAreaConstructionPermit() {
		return waterAreaConstructionPermit;
	}

	public void setWaterAreaConstructionPermit(Long waterAreaConstructionPermit) {
		this.waterAreaConstructionPermit = waterAreaConstructionPermit;
	}
	
	public Long getInlandShipTankCleaningVerification() {
		return inlandShipTankCleaningVerification;
	}

	public void setInlandShipTankCleaningVerification(Long inlandShipTankCleaningVerification) {
		this.inlandShipTankCleaningVerification = inlandShipTankCleaningVerification;
	}
	
	public Long getTankCleaningDeclaration() {
		return tankCleaningDeclaration;
	}

	public void setTankCleaningDeclaration(Long tankCleaningDeclaration) {
		this.tankCleaningDeclaration = tankCleaningDeclaration;
	}
	
	public Long getCargoExchangeDeclaration() {
		return cargoExchangeDeclaration;
	}

	public void setCargoExchangeDeclaration(Long cargoExchangeDeclaration) {
		this.cargoExchangeDeclaration = cargoExchangeDeclaration;
	}
	
	public Long getTankCleaningDeclarationIssues() {
		return tankCleaningDeclarationIssues;
	}

	public void setTankCleaningDeclarationIssues(Long tankCleaningDeclarationIssues) {
		this.tankCleaningDeclarationIssues = tankCleaningDeclarationIssues;
	}
	
	public Date getDataTime_gte() {
		return sqlMap.getWhere().getValue("data_time", QueryType.GTE);
	}

	public void setDataTime_gte(Date dataTime) {
		sqlMap.getWhere().and("data_time", QueryType.GTE, dataTime);
	}
	
	public Date getDataTime_lte() {
		return sqlMap.getWhere().getValue("data_time", QueryType.LTE);
	}

	public void setDataTime_lte(Date dataTime) {
		sqlMap.getWhere().and("data_time", QueryType.LTE, dataTime);
	}
	
}