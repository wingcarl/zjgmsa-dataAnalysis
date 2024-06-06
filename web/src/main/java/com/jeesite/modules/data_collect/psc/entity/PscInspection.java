package com.jeesite.modules.data_collect.psc.entity;

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
 * PSC Inspection TableEntity
 * @author Dylan Wang
 * @version 2024-06-06
 */
@Table(name="psc_inspection", alias="a", label="PSC Inspection Table信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="type", attrName="type", label="Type of inspection"),
		@Column(name="submitted", attrName="submitted", label="submitted time", isUpdateForce=true),
		@Column(name="inspection_date", attrName="inspectionDate", label="inspection date", isUpdateForce=true),
		@Column(name="port", attrName="port", label="Port"),
		@Column(name="imo_number", attrName="imoNumber", label="IMO number"),
		@Column(name="name", attrName="name", label="Name", queryType=QueryType.LIKE),
		@Column(name="callsign", attrName="callsign", label="Callsign"),
		@Column(name="mmsi", attrName="mmsi", label="MMSI"),
		@Column(name="flag", attrName="flag", label="Flag"),
		@Column(name="ship_type", attrName="shipType", label="Ship Type"),
		@Column(name="psco", attrName="psco", label="PSCO"),
		@Column(name="date_keel_laid", attrName="dateKeelLaid", label="keel laid"),
		@Column(name="class_society", attrName="classSociety", label="Classification society"),
		@Column(name="gross_tonnage", attrName="grossTonnage", label="Gross tonnage"),
		@Column(name="deficiencies", attrName="deficiencies", label="deficiencies found"),
		@Column(name="detention", attrName="detention", label="Detention status"),
		@Column(name="detention_deficiencies", attrName="detentionDeficiencies", label="detention deficiencies"),
		@Column(name="ship_risk_profile", attrName="shipRiskProfile", label="Risk profile"),
		@Column(name="priority", attrName="priority", label="Inspection priority"),
		@Column(name="window_inspection_range", attrName="windowInspectionRange", label="Window inspection range"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class PscInspection extends DataEntity<PscInspection> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// Type of inspection
	private Date submitted;		// submitted time
	private Date inspectionDate;		// inspection date
	private String port;		// Port
	private String imoNumber;		// IMO number
	private String name;		// Name
	private String callsign;		// Callsign
	private String mmsi;		// MMSI
	private String flag;		// Flag
	private String shipType;		// Ship Type
	private String psco;		// PSCO
	private String dateKeelLaid;		// keel laid
	private String classSociety;		// Classification society
	private String grossTonnage;		// Gross tonnage
	private String deficiencies;		// deficiencies found
	private String detention;		// Detention status
	private String detentionDeficiencies;		// detention deficiencies
	private String shipRiskProfile;		// Risk profile
	private String priority;		// Inspection priority
	private String windowInspectionRange;		// Window inspection range

	@ExcelFields({
		@ExcelField(title="Type of inspection", attrName="type", align=Align.CENTER, sort=20),
		@ExcelField(title="submitted time", attrName="submitted", align=Align.CENTER, sort=30, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="inspection date", attrName="inspectionDate", align=Align.CENTER, sort=40, dataFormat="yyyyMMdd"),
		@ExcelField(title="Port", attrName="port", align=Align.CENTER, sort=50),
		@ExcelField(title="IMO number", attrName="imoNumber", align=Align.CENTER, sort=60),
		@ExcelField(title="Name", attrName="name", align=Align.CENTER, sort=70),
		@ExcelField(title="Callsign", attrName="callsign", align=Align.CENTER, sort=80),
		@ExcelField(title="MMSI", attrName="mmsi", align=Align.CENTER, sort=90),
		@ExcelField(title="Flag", attrName="flag", align=Align.CENTER, sort=100),
		@ExcelField(title="Ship Type", attrName="shipType", align=Align.CENTER, sort=110),
		@ExcelField(title="PSCO", attrName="psco", align=Align.CENTER, sort=120),
		@ExcelField(title="keel laid", attrName="dateKeelLaid", align=Align.CENTER, sort=130),
		@ExcelField(title="Classification society", attrName="classSociety", align=Align.CENTER, sort=140),
		@ExcelField(title="Gross tonnage", attrName="grossTonnage", align=Align.CENTER, sort=150),
		@ExcelField(title="deficiencies found", attrName="deficiencies", align=Align.CENTER, sort=160),
		@ExcelField(title="Detention status", attrName="detention", align=Align.CENTER, sort=170),
		@ExcelField(title="detention deficiencies", attrName="detentionDeficiencies", align=Align.CENTER, sort=180),
		@ExcelField(title="Risk profile", attrName="shipRiskProfile", align=Align.CENTER, sort=190),
		@ExcelField(title="Inspection priority", attrName="priority", align=Align.CENTER, sort=200),
		@ExcelField(title="Window inspection range", attrName="windowInspectionRange", align=Align.CENTER, sort=210),
	})
	public PscInspection() {
		this(null);
	}
	
	public PscInspection(String id){
		super(id);
	}
	
	@Size(min=0, max=50, message="Type of inspection长度不能超过 50 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}
	
	@JsonFormat(pattern = "yyyyMMdd")
	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	
	@Size(min=0, max=100, message="Port长度不能超过 100 个字符")
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	@Size(min=0, max=50, message="IMO number长度不能超过 50 个字符")
	public String getImoNumber() {
		return imoNumber;
	}

	public void setImoNumber(String imoNumber) {
		this.imoNumber = imoNumber;
	}
	
	@Size(min=0, max=100, message="Name长度不能超过 100 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Size(min=0, max=50, message="Callsign长度不能超过 50 个字符")
	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	
	@Size(min=0, max=50, message="MMSI长度不能超过 50 个字符")
	public String getMmsi() {
		return mmsi;
	}

	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}
	
	@Size(min=0, max=100, message="Flag长度不能超过 100 个字符")
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@Size(min=0, max=100, message="Ship Type长度不能超过 100 个字符")
	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	
	@Size(min=0, max=100, message="PSCO长度不能超过 100 个字符")
	public String getPsco() {
		return psco;
	}

	public void setPsco(String psco) {
		this.psco = psco;
	}
	
	@Size(min=0, max=100, message="keel laid长度不能超过 100 个字符")
	public String getDateKeelLaid() {
		return dateKeelLaid;
	}

	public void setDateKeelLaid(String dateKeelLaid) {
		this.dateKeelLaid = dateKeelLaid;
	}
	
	@Size(min=0, max=100, message="Classification society长度不能超过 100 个字符")
	public String getClassSociety() {
		return classSociety;
	}

	public void setClassSociety(String classSociety) {
		this.classSociety = classSociety;
	}
	
	@Size(min=0, max=10, message="Gross tonnage长度不能超过 10 个字符")
	public String getGrossTonnage() {
		return grossTonnage;
	}

	public void setGrossTonnage(String grossTonnage) {
		this.grossTonnage = grossTonnage;
	}
	
	@Size(min=0, max=10, message="deficiencies found长度不能超过 10 个字符")
	public String getDeficiencies() {
		return deficiencies;
	}

	public void setDeficiencies(String deficiencies) {
		this.deficiencies = deficiencies;
	}
	
	@Size(min=0, max=10, message="Detention status长度不能超过 10 个字符")
	public String getDetention() {
		return detention;
	}

	public void setDetention(String detention) {
		this.detention = detention;
	}
	
	@Size(min=0, max=10, message="detention deficiencies长度不能超过 10 个字符")
	public String getDetentionDeficiencies() {
		return detentionDeficiencies;
	}

	public void setDetentionDeficiencies(String detentionDeficiencies) {
		this.detentionDeficiencies = detentionDeficiencies;
	}
	
	@Size(min=0, max=100, message="Risk profile长度不能超过 100 个字符")
	public String getShipRiskProfile() {
		return shipRiskProfile;
	}

	public void setShipRiskProfile(String shipRiskProfile) {
		this.shipRiskProfile = shipRiskProfile;
	}
	
	@Size(min=0, max=100, message="Inspection priority长度不能超过 100 个字符")
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	@Size(min=0, max=100, message="Window inspection range长度不能超过 100 个字符")
	public String getWindowInspectionRange() {
		return windowInspectionRange;
	}

	public void setWindowInspectionRange(String windowInspectionRange) {
		this.windowInspectionRange = windowInspectionRange;
	}
	
	public Date getSubmitted_gte() {
		return sqlMap.getWhere().getValue("submitted", QueryType.GTE);
	}

	public void setSubmitted_gte(Date submitted) {
		sqlMap.getWhere().and("submitted", QueryType.GTE, submitted);
	}
	
	public Date getSubmitted_lte() {
		return sqlMap.getWhere().getValue("submitted", QueryType.LTE);
	}

	public void setSubmitted_lte(Date submitted) {
		sqlMap.getWhere().and("submitted", QueryType.LTE, submitted);
	}
	
	public Date getInspectionDate_gte() {
		return sqlMap.getWhere().getValue("inspection_date", QueryType.GTE);
	}

	public void setInspectionDate_gte(Date inspectionDate) {
		sqlMap.getWhere().and("inspection_date", QueryType.GTE, inspectionDate);
	}
	
	public Date getInspectionDate_lte() {
		return sqlMap.getWhere().getValue("inspection_date", QueryType.LTE);
	}

	public void setInspectionDate_lte(Date inspectionDate) {
		sqlMap.getWhere().and("inspection_date", QueryType.LTE, inspectionDate);
	}
	
}