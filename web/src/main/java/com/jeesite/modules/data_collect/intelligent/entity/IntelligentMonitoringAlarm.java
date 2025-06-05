package com.jeesite.modules.data_collect.intelligent.entity;

import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.Size;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;

/**
 * 智能卡口预警记录表Entity
 * @author 王浩宇
 * @version 2025-06-05
 */
@Table(name="intelligent_monitoring_alarm", alias="a", label="智能卡口预警记录表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="capture_time", attrName="captureTime", label="抓拍时间", isUpdateForce=true),
		@Column(name="capture_location", attrName="captureLocation", label="抓拍地点"),
		@Column(name="ship_name", attrName="shipName", label="船名", queryType=QueryType.LIKE),
		@Column(name="mmsi", attrName="mmsi", label="MMSI码"),
		@Column(name="alarm_type", attrName="alarmType", label="预警类型"),
		@Column(name="alarm_flag", attrName="alarmFlag", label="预警标识"),
		@Column(name="branch_office", attrName="branchOffice", label="所属分支局"),
		@Column(name="disposal_unit", attrName="disposalUnit", label="处置单位"),
		@Column(name="verification_status", attrName="verificationStatus", label="核查情况"),
		@Column(name="disposal_method", attrName="disposalMethod", label="处置方式"),
		@Column(name="verification_remarks", attrName="verificationRemarks", label="核查备注", queryType=QueryType.LIKE),
		@Column(name="disposal_remarks", attrName="disposalRemarks", label="处置备注", queryType=QueryType.LIKE),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class IntelligentMonitoringAlarm extends DataEntity<IntelligentMonitoringAlarm> {
	
	private static final long serialVersionUID = 1L;
	private Date captureTime;		// 抓拍时间
	private String captureLocation;		// 抓拍地点
	private String shipName;		// 船名
	private String mmsi;		// MMSI码
	private String alarmType;		// 预警类型
	private String alarmFlag;		// 预警标识
	private String branchOffice;		// 所属分支局
	private String disposalUnit;		// 处置单位
	private String verificationStatus;		// 核查情况
	private String disposalMethod;		// 处置方式
	private String verificationRemarks;		// 核查备注
	private String disposalRemarks;		// 处置备注

	@ExcelFields({
		@ExcelField(title="抓拍时间", attrName="captureTime", align=Align.CENTER, sort=20, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="抓拍地点", attrName="captureLocation", align=Align.CENTER, sort=30),
		@ExcelField(title="船名", attrName="shipName", align=Align.CENTER, sort=40),
		@ExcelField(title="MMSI码", attrName="mmsi", align=Align.CENTER, sort=50),
		@ExcelField(title="预警类型", attrName="alarmType", align=Align.CENTER, sort=60),
		@ExcelField(title="预警标识", attrName="alarmFlag", align=Align.CENTER, sort=70),
		@ExcelField(title="所属分支局", attrName="branchOffice", align=Align.CENTER, sort=80),
		@ExcelField(title="处置单位", attrName="disposalUnit", align=Align.CENTER, sort=90),
		@ExcelField(title="核查情况", attrName="verificationStatus", align=Align.CENTER, sort=100),
		@ExcelField(title="处置方式", attrName="disposalMethod", align=Align.CENTER, sort=110),
		@ExcelField(title="核查备注", attrName="verificationRemarks", align=Align.CENTER, sort=120),
		@ExcelField(title="处置备注", attrName="disposalRemarks", align=Align.CENTER, sort=130),
	})
	public IntelligentMonitoringAlarm() {
		this(null);
	}
	
	public IntelligentMonitoringAlarm(String id){
		super(id);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaptureTime() {
		return captureTime;
	}

	public void setCaptureTime(Date captureTime) {
		this.captureTime = captureTime;
	}
	
	@Size(min=0, max=255, message="抓拍地点长度不能超过 255 个字符")
	public String getCaptureLocation() {
		return captureLocation;
	}

	public void setCaptureLocation(String captureLocation) {
		this.captureLocation = captureLocation;
	}
	
	@Size(min=0, max=100, message="船名长度不能超过 100 个字符")
	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	
	@Size(min=0, max=20, message="MMSI码长度不能超过 20 个字符")
	public String getMmsi() {
		return mmsi;
	}

	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}
	
	@Size(min=0, max=100, message="预警类型长度不能超过 100 个字符")
	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
	@Size(min=0, max=100, message="预警标识长度不能超过 100 个字符")
	public String getAlarmFlag() {
		return alarmFlag;
	}

	public void setAlarmFlag(String alarmFlag) {
		this.alarmFlag = alarmFlag;
	}
	
	@Size(min=0, max=100, message="所属分支局长度不能超过 100 个字符")
	public String getBranchOffice() {
		return branchOffice;
	}

	public void setBranchOffice(String branchOffice) {
		this.branchOffice = branchOffice;
	}
	
	@Size(min=0, max=100, message="处置单位长度不能超过 100 个字符")
	public String getDisposalUnit() {
		return disposalUnit;
	}

	public void setDisposalUnit(String disposalUnit) {
		this.disposalUnit = disposalUnit;
	}
	
	@Size(min=0, max=50, message="核查情况长度不能超过 50 个字符")
	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}
	
	@Size(min=0, max=100, message="处置方式长度不能超过 100 个字符")
	public String getDisposalMethod() {
		return disposalMethod;
	}

	public void setDisposalMethod(String disposalMethod) {
		this.disposalMethod = disposalMethod;
	}
	
	public String getVerificationRemarks() {
		return verificationRemarks;
	}

	public void setVerificationRemarks(String verificationRemarks) {
		this.verificationRemarks = verificationRemarks;
	}
	
	public String getDisposalRemarks() {
		return disposalRemarks;
	}

	public void setDisposalRemarks(String disposalRemarks) {
		this.disposalRemarks = disposalRemarks;
	}
	
}