package com.jeesite.modules.shipreport.entity;

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
 * 船舶报告表Entity
 * @author 王浩宇
 * @version 2024-06-21
 */
@Table(name="ship_report", alias="a", label="船舶报告表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="report_id", attrName="reportId", label="报告编号", isQuery=false),
		@Column(name="ship_name_cn", attrName="shipNameCn", label="中文船名", queryType=QueryType.LIKE),
		@Column(name="ship_identification_number", attrName="shipIdentificationNumber", label="船舶识别号", queryType=QueryType.LIKE),
		@Column(name="gross_tonnage", attrName="grossTonnage", label="总吨", isUpdateForce=true),
		@Column(name="deadweight_tonnage", attrName="deadweightTonnage", label="载重吨", isUpdateForce=true),
		@Column(name="main_engine_power", attrName="mainEnginePower", label="主机功率", isUpdateForce=true),
		@Column(name="ship_type", attrName="shipType", label="船舶种类"),
		@Column(name="port_of_registry", attrName="portOfRegistry", label="船籍港"),
		@Column(name="ship_length", attrName="shipLength", label="船舶长度", isUpdateForce=true),
		@Column(name="ship_width", attrName="shipWidth", label="船舶宽度", isUpdateForce=true),
		@Column(name="in_out_port", attrName="inOutPort", label="进出港"),
		@Column(name="sea_river_ship", attrName="seaRiverShip", label="海/河船"),
		@Column(name="reporting_agency", attrName="reportingAgency", label="报告机构"),
		@Column(name="estimated_arrival_departure_time", attrName="estimatedArrivalDepartureTime", label="预抵离时间", isUpdateForce=true),
		@Column(name="report_time", attrName="reportTime", label="报告时间", isUpdateForce=true),
		@Column(name="port", attrName="port", label="港口"),
		@Column(name="berth", attrName="berth", label="泊位"),
		@Column(name="up_down_port", attrName="upDownPort", label="上下港"),
		@Column(name="voyage_daily_report", attrName="voyageDailyReport", label="航次日报"),
		@Column(name="voyage_count", attrName="voyageCount", label="航次数量", isQuery=false, isUpdateForce=true),
		@Column(name="actual_cargo_volume", attrName="actualCargoVolume", label="实载货量", isUpdateForce=true),
		@Column(name="loading_unloading_cargo_volume", attrName="loadingUnloadingCargoVolume", label="装卸货量", isUpdateForce=true),
		@Column(name="actual_vehicle_count", attrName="actualVehicleCount", label="实载车辆", isUpdateForce=true),
		@Column(name="loading_unloading_vehicle_count", attrName="loadingUnloadingVehicleCount", label="装卸车辆", isUpdateForce=true),
		@Column(name="actual_passenger_count", attrName="actualPassengerCount", label="实载客量", isUpdateForce=true),
		@Column(name="up_down_passenger_count", attrName="upDownPassengerCount", label="上下客量", isUpdateForce=true),
		@Column(name="cargo_type", attrName="cargoType", label="货物种类", queryType=QueryType.LIKE),
		@Column(name="cargo_name", attrName="cargoName", label="品名", queryType=QueryType.LIKE),
		@Column(name="mmsi", attrName="mmsi", label="MMSI"),
		@Column(name="draft_fore", attrName="draftFore", label="前吃水", isUpdateForce=true),
		@Column(name="draft_aft", attrName="draftAft", label="后吃水", isUpdateForce=true),
		@Column(name="ship_owner", attrName="shipOwner", label="船舶所有人", queryType=QueryType.LIKE),
		@Column(name="applicant", attrName="applicant", label="申请人", queryType=QueryType.LIKE),
		@Column(name="applicant_contact", attrName="applicantContact", label="申请人联系方式", isQuery=false),
		@Column(name="is_hazardous_cargo", attrName="isHazardousCargo", label="是否危险货物"),
		@Column(name="hazardous_cargo_quantity", attrName="hazardousCargoQuantity", label="危险货物数量", isUpdateForce=true),
		@Column(name="barge_count", attrName="bargeCount", label="驳船数量", isQuery=false, isUpdateForce=true),
		@Column(name="local_barge_operations_count", attrName="localBargeOperationsCount", label="本港加/解驳船数量", isQuery=false, isUpdateForce=true),
		@Column(name="actual_container_volume", attrName="actualContainerVolume", label="实载集装箱运量", isUpdateForce=true),
		@Column(name="local_container_operations_volume", attrName="localContainerOperationsVolume", label="本港装/卸集装箱运量", isUpdateForce=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ShipReport extends DataEntity<ShipReport> {
	
	private static final long serialVersionUID = 1L;
	private String reportId;		// 报告编号
	private String shipNameCn;		// 中文船名
	private String shipIdentificationNumber;		// 船舶识别号
	private Double grossTonnage;		// 总吨
	private Double deadweightTonnage;		// 载重吨
	private Double mainEnginePower;		// 主机功率
	private String shipType;		// 船舶种类
	private String portOfRegistry;		// 船籍港
	private Double shipLength;		// 船舶长度
	private Double shipWidth;		// 船舶宽度
	private String inOutPort;		// 进出港
	private String seaRiverShip;		// 海/河船
	private String reportingAgency;		// 报告机构
	private Date estimatedArrivalDepartureTime;		// 预抵离时间
	private Date reportTime;		// 报告时间
	private String port;		// 港口
	private String berth;		// 泊位
	private String upDownPort;		// 上下港
	private String voyageDailyReport;		// 航次日报
	private Long voyageCount;		// 航次数量
	private Double actualCargoVolume;		// 实载货量
	private Double loadingUnloadingCargoVolume;		// 装卸货量
	private Long actualVehicleCount;		// 实载车辆
	private Long loadingUnloadingVehicleCount;		// 装卸车辆
	private Long actualPassengerCount;		// 实载客量
	private Long upDownPassengerCount;		// 上下客量
	private String cargoType;		// 货物种类
	private String cargoName;		// 品名
	private String mmsi;		// MMSI
	private Double draftFore;		// 前吃水
	private Double draftAft;		// 后吃水
	private String shipOwner;		// 船舶所有人
	private String applicant;		// 申请人
	private String applicantContact;		// 申请人联系方式
	private String isHazardousCargo;		// 是否危险货物
	private Double hazardousCargoQuantity;		// 危险货物数量
	private Long bargeCount;		// 驳船数量
	private Long localBargeOperationsCount;		// 本港加/解驳船数量
	private Double actualContainerVolume;		// 实载集装箱运量
	private Double localContainerOperationsVolume;		// 本港装/卸集装箱运量

	@ExcelFields({
		@ExcelField(title="报告编号", attrName="reportId", align=Align.CENTER, sort=20),
		@ExcelField(title="中文船名", attrName="shipNameCn", align=Align.CENTER, sort=30),
		@ExcelField(title="船舶识别号", attrName="shipIdentificationNumber", align=Align.CENTER, sort=40),
		@ExcelField(title="总吨", attrName="grossTonnage", align=Align.CENTER, sort=50),
		@ExcelField(title="载重吨", attrName="deadweightTonnage", align=Align.CENTER, sort=60),
		@ExcelField(title="主机功率", attrName="mainEnginePower", align=Align.CENTER, sort=70),
		@ExcelField(title="船舶种类", attrName="shipType", align=Align.CENTER, sort=80),
		@ExcelField(title="船籍港", attrName="portOfRegistry", align=Align.CENTER, sort=90),
		@ExcelField(title="船舶长度", attrName="shipLength", align=Align.CENTER, sort=100),
		@ExcelField(title="船舶宽度", attrName="shipWidth", align=Align.CENTER, sort=110),
		@ExcelField(title="进出港", attrName="inOutPort", align=Align.CENTER, sort=120),
		@ExcelField(title="海/河船", attrName="seaRiverShip", align=Align.CENTER, sort=130),
		@ExcelField(title="报告机构", attrName="reportingAgency", align=Align.CENTER, sort=140),
		@ExcelField(title="预抵离时间", attrName="estimatedArrivalDepartureTime", align=Align.CENTER, sort=150, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="报告时间", attrName="reportTime", align=Align.CENTER, sort=160, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="港口", attrName="port", align=Align.CENTER, sort=170),
		@ExcelField(title="泊位", attrName="berth", align=Align.CENTER, sort=180),
		@ExcelField(title="上下港", attrName="upDownPort", align=Align.CENTER, sort=190),
		@ExcelField(title="航次日报", attrName="voyageDailyReport", align=Align.CENTER, sort=200),
		@ExcelField(title="航次数量", attrName="voyageCount", align=Align.CENTER, sort=210),
		@ExcelField(title="实载货量", attrName="actualCargoVolume", align=Align.CENTER, sort=220),
		@ExcelField(title="装卸货量", attrName="loadingUnloadingCargoVolume", align=Align.CENTER, sort=230),
		@ExcelField(title="实载车辆", attrName="actualVehicleCount", align=Align.CENTER, sort=240),
		@ExcelField(title="装卸车辆", attrName="loadingUnloadingVehicleCount", align=Align.CENTER, sort=250),
		@ExcelField(title="实载客量", attrName="actualPassengerCount", align=Align.CENTER, sort=260),
		@ExcelField(title="上下客量", attrName="upDownPassengerCount", align=Align.CENTER, sort=270),
		@ExcelField(title="货物种类", attrName="cargoType", align=Align.CENTER, sort=280),
		@ExcelField(title="品名", attrName="cargoName", align=Align.CENTER, sort=290),
		@ExcelField(title="MMSI", attrName="mmsi", align=Align.CENTER, sort=300),
		@ExcelField(title="前吃水", attrName="draftFore", align=Align.CENTER, sort=310),
		@ExcelField(title="后吃水", attrName="draftAft", align=Align.CENTER, sort=320),
		@ExcelField(title="船舶所有人", attrName="shipOwner", align=Align.CENTER, sort=330),
		@ExcelField(title="申请人", attrName="applicant", align=Align.CENTER, sort=340),
		@ExcelField(title="申请人联系方式", attrName="applicantContact", align=Align.CENTER, sort=350),
		@ExcelField(title="是否危险货物", attrName="isHazardousCargo", align=Align.CENTER, sort=360),
		@ExcelField(title="危险货物数量", attrName="hazardousCargoQuantity", align=Align.CENTER, sort=370),
		@ExcelField(title="驳船数量", attrName="bargeCount", align=Align.CENTER, sort=380),
		@ExcelField(title="本港加/解驳船数量", attrName="localBargeOperationsCount", align=Align.CENTER, sort=390),
		@ExcelField(title="实载集装箱运量", attrName="actualContainerVolume", align=Align.CENTER, sort=400),
		@ExcelField(title="本港装/卸集装箱运量", attrName="localContainerOperationsVolume", align=Align.CENTER, sort=410),
	})
	public ShipReport() {
		this(null);
	}
	
	public ShipReport(String id){
		super(id);
	}
	
	@Size(min=0, max=64, message="报告编号长度不能超过 64 个字符")
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	@Size(min=0, max=32, message="中文船名长度不能超过 32 个字符")
	public String getShipNameCn() {
		return shipNameCn;
	}

	public void setShipNameCn(String shipNameCn) {
		this.shipNameCn = shipNameCn;
	}
	
	@Size(min=0, max=64, message="船舶识别号长度不能超过 64 个字符")
	public String getShipIdentificationNumber() {
		return shipIdentificationNumber;
	}

	public void setShipIdentificationNumber(String shipIdentificationNumber) {
		this.shipIdentificationNumber = shipIdentificationNumber;
	}
	
	public Double getGrossTonnage() {
		return grossTonnage;
	}

	public void setGrossTonnage(Double grossTonnage) {
		this.grossTonnage = grossTonnage;
	}
	
	public Double getDeadweightTonnage() {
		return deadweightTonnage;
	}

	public void setDeadweightTonnage(Double deadweightTonnage) {
		this.deadweightTonnage = deadweightTonnage;
	}
	
	public Double getMainEnginePower() {
		return mainEnginePower;
	}

	public void setMainEnginePower(Double mainEnginePower) {
		this.mainEnginePower = mainEnginePower;
	}
	
	@Size(min=0, max=32, message="船舶种类长度不能超过 32 个字符")
	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	
	@Size(min=0, max=32, message="船籍港长度不能超过 32 个字符")
	public String getPortOfRegistry() {
		return portOfRegistry;
	}

	public void setPortOfRegistry(String portOfRegistry) {
		this.portOfRegistry = portOfRegistry;
	}
	
	public Double getShipLength() {
		return shipLength;
	}

	public void setShipLength(Double shipLength) {
		this.shipLength = shipLength;
	}
	
	public Double getShipWidth() {
		return shipWidth;
	}

	public void setShipWidth(Double shipWidth) {
		this.shipWidth = shipWidth;
	}
	
	@Size(min=0, max=16, message="进出港长度不能超过 16 个字符")
	public String getInOutPort() {
		return inOutPort;
	}

	public void setInOutPort(String inOutPort) {
		this.inOutPort = inOutPort;
	}
	
	@Size(min=0, max=16, message="海/河船长度不能超过 16 个字符")
	public String getSeaRiverShip() {
		return seaRiverShip;
	}

	public void setSeaRiverShip(String seaRiverShip) {
		this.seaRiverShip = seaRiverShip;
	}
	
	@Size(min=0, max=32, message="报告机构长度不能超过 32 个字符")
	public String getReportingAgency() {
		return reportingAgency;
	}

	public void setReportingAgency(String reportingAgency) {
		this.reportingAgency = reportingAgency;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEstimatedArrivalDepartureTime() {
		return estimatedArrivalDepartureTime;
	}

	public void setEstimatedArrivalDepartureTime(Date estimatedArrivalDepartureTime) {
		this.estimatedArrivalDepartureTime = estimatedArrivalDepartureTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	
	@Size(min=0, max=32, message="港口长度不能超过 32 个字符")
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	@Size(min=0, max=64, message="泊位长度不能超过 64 个字符")
	public String getBerth() {
		return berth;
	}

	public void setBerth(String berth) {
		this.berth = berth;
	}
	
	@Size(min=0, max=32, message="上下港长度不能超过 32 个字符")
	public String getUpDownPort() {
		return upDownPort;
	}

	public void setUpDownPort(String upDownPort) {
		this.upDownPort = upDownPort;
	}
	
	@Size(min=0, max=32, message="航次日报长度不能超过 32 个字符")
	public String getVoyageDailyReport() {
		return voyageDailyReport;
	}

	public void setVoyageDailyReport(String voyageDailyReport) {
		this.voyageDailyReport = voyageDailyReport;
	}
	
	public Long getVoyageCount() {
		return voyageCount;
	}

	public void setVoyageCount(Long voyageCount) {
		this.voyageCount = voyageCount;
	}
	
	public Double getActualCargoVolume() {
		return actualCargoVolume;
	}

	public void setActualCargoVolume(Double actualCargoVolume) {
		this.actualCargoVolume = actualCargoVolume;
	}
	
	public Double getLoadingUnloadingCargoVolume() {
		return loadingUnloadingCargoVolume;
	}

	public void setLoadingUnloadingCargoVolume(Double loadingUnloadingCargoVolume) {
		this.loadingUnloadingCargoVolume = loadingUnloadingCargoVolume;
	}
	
	public Long getActualVehicleCount() {
		return actualVehicleCount;
	}

	public void setActualVehicleCount(Long actualVehicleCount) {
		this.actualVehicleCount = actualVehicleCount;
	}
	
	public Long getLoadingUnloadingVehicleCount() {
		return loadingUnloadingVehicleCount;
	}

	public void setLoadingUnloadingVehicleCount(Long loadingUnloadingVehicleCount) {
		this.loadingUnloadingVehicleCount = loadingUnloadingVehicleCount;
	}
	
	public Long getActualPassengerCount() {
		return actualPassengerCount;
	}

	public void setActualPassengerCount(Long actualPassengerCount) {
		this.actualPassengerCount = actualPassengerCount;
	}
	
	public Long getUpDownPassengerCount() {
		return upDownPassengerCount;
	}

	public void setUpDownPassengerCount(Long upDownPassengerCount) {
		this.upDownPassengerCount = upDownPassengerCount;
	}
	
	@Size(min=0, max=64, message="货物种类长度不能超过 64 个字符")
	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}
	
	@Size(min=0, max=128, message="品名长度不能超过 64 个字符")
	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	
	@Size(min=0, max=24, message="MMSI长度不能超过 24 个字符")
	public String getMmsi() {
		return mmsi;
	}

	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}
	
	public Double getDraftFore() {
		return draftFore;
	}

	public void setDraftFore(Double draftFore) {
		this.draftFore = draftFore;
	}
	
	public Double getDraftAft() {
		return draftAft;
	}

	public void setDraftAft(Double draftAft) {
		this.draftAft = draftAft;
	}
	
	@Size(min=0, max=64, message="船舶所有人长度不能超过 64 个字符")
	public String getShipOwner() {
		return shipOwner;
	}

	public void setShipOwner(String shipOwner) {
		this.shipOwner = shipOwner;
	}
	
	@Size(min=0, max=32, message="申请人长度不能超过 32 个字符")
	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	
	@Size(min=0, max=32, message="申请人联系方式长度不能超过 32 个字符")
	public String getApplicantContact() {
		return applicantContact;
	}

	public void setApplicantContact(String applicantContact) {
		this.applicantContact = applicantContact;
	}
	
	@Size(min=0, max=10, message="是否危险货物长度不能超过 10 个字符")
	public String getIsHazardousCargo() {
		return isHazardousCargo;
	}

	public void setIsHazardousCargo(String isHazardousCargo) {
		this.isHazardousCargo = isHazardousCargo;
	}
	
	public Double getHazardousCargoQuantity() {
		return hazardousCargoQuantity;
	}

	public void setHazardousCargoQuantity(Double hazardousCargoQuantity) {
		this.hazardousCargoQuantity = hazardousCargoQuantity;
	}
	
	public Long getBargeCount() {
		return bargeCount;
	}

	public void setBargeCount(Long bargeCount) {
		this.bargeCount = bargeCount;
	}
	
	public Long getLocalBargeOperationsCount() {
		return localBargeOperationsCount;
	}

	public void setLocalBargeOperationsCount(Long localBargeOperationsCount) {
		this.localBargeOperationsCount = localBargeOperationsCount;
	}
	
	public Double getActualContainerVolume() {
		return actualContainerVolume;
	}

	public void setActualContainerVolume(Double actualContainerVolume) {
		this.actualContainerVolume = actualContainerVolume;
	}
	
	public Double getLocalContainerOperationsVolume() {
		return localContainerOperationsVolume;
	}

	public void setLocalContainerOperationsVolume(Double localContainerOperationsVolume) {
		this.localContainerOperationsVolume = localContainerOperationsVolume;
	}
	
	public Double getGrossTonnage_gte() {
		return sqlMap.getWhere().getValue("gross_tonnage", QueryType.GTE);
	}

	public void setGrossTonnage_gte(Double grossTonnage) {
		sqlMap.getWhere().and("gross_tonnage", QueryType.GTE, grossTonnage);
	}
	
	public Double getGrossTonnage_lte() {
		return sqlMap.getWhere().getValue("gross_tonnage", QueryType.LTE);
	}

	public void setGrossTonnage_lte(Double grossTonnage) {
		sqlMap.getWhere().and("gross_tonnage", QueryType.LTE, grossTonnage);
	}
	
	public Double getDeadweightTonnage_gte() {
		return sqlMap.getWhere().getValue("deadweight_tonnage", QueryType.GTE);
	}

	public void setDeadweightTonnage_gte(Double deadweightTonnage) {
		sqlMap.getWhere().and("deadweight_tonnage", QueryType.GTE, deadweightTonnage);
	}
	
	public Double getDeadweightTonnage_lte() {
		return sqlMap.getWhere().getValue("deadweight_tonnage", QueryType.LTE);
	}

	public void setDeadweightTonnage_lte(Double deadweightTonnage) {
		sqlMap.getWhere().and("deadweight_tonnage", QueryType.LTE, deadweightTonnage);
	}
	
	public Double getMainEnginePower_gte() {
		return sqlMap.getWhere().getValue("main_engine_power", QueryType.GTE);
	}

	public void setMainEnginePower_gte(Double mainEnginePower) {
		sqlMap.getWhere().and("main_engine_power", QueryType.GTE, mainEnginePower);
	}
	
	public Double getMainEnginePower_lte() {
		return sqlMap.getWhere().getValue("main_engine_power", QueryType.LTE);
	}

	public void setMainEnginePower_lte(Double mainEnginePower) {
		sqlMap.getWhere().and("main_engine_power", QueryType.LTE, mainEnginePower);
	}
	
	public Double getShipLength_gte() {
		return sqlMap.getWhere().getValue("ship_length", QueryType.GTE);
	}

	public void setShipLength_gte(Double shipLength) {
		sqlMap.getWhere().and("ship_length", QueryType.GTE, shipLength);
	}
	
	public Double getShipLength_lte() {
		return sqlMap.getWhere().getValue("ship_length", QueryType.LTE);
	}

	public void setShipLength_lte(Double shipLength) {
		sqlMap.getWhere().and("ship_length", QueryType.LTE, shipLength);
	}
	
	public Double getShipWidth_gte() {
		return sqlMap.getWhere().getValue("ship_width", QueryType.GTE);
	}

	public void setShipWidth_gte(Double shipWidth) {
		sqlMap.getWhere().and("ship_width", QueryType.GTE, shipWidth);
	}
	
	public Double getShipWidth_lte() {
		return sqlMap.getWhere().getValue("ship_width", QueryType.LTE);
	}

	public void setShipWidth_lte(Double shipWidth) {
		sqlMap.getWhere().and("ship_width", QueryType.LTE, shipWidth);
	}
	
	public Date getEstimatedArrivalDepartureTime_gte() {
		return sqlMap.getWhere().getValue("estimated_arrival_departure_time", QueryType.GTE);
	}

	public void setEstimatedArrivalDepartureTime_gte(Date estimatedArrivalDepartureTime) {
		sqlMap.getWhere().and("estimated_arrival_departure_time", QueryType.GTE, estimatedArrivalDepartureTime);
	}
	
	public Date getEstimatedArrivalDepartureTime_lte() {
		return sqlMap.getWhere().getValue("estimated_arrival_departure_time", QueryType.LTE);
	}

	public void setEstimatedArrivalDepartureTime_lte(Date estimatedArrivalDepartureTime) {
		sqlMap.getWhere().and("estimated_arrival_departure_time", QueryType.LTE, estimatedArrivalDepartureTime);
	}
	
	public Date getReportTime_gte() {
		return sqlMap.getWhere().getValue("report_time", QueryType.GTE);
	}

	public void setReportTime_gte(Date reportTime) {
		sqlMap.getWhere().and("report_time", QueryType.GTE, reportTime);
	}
	
	public Date getReportTime_lte() {
		return sqlMap.getWhere().getValue("report_time", QueryType.LTE);
	}

	public void setReportTime_lte(Date reportTime) {
		sqlMap.getWhere().and("report_time", QueryType.LTE, reportTime);
	}
	
	public Double getActualCargoVolume_gte() {
		return sqlMap.getWhere().getValue("actual_cargo_volume", QueryType.GTE);
	}

	public void setActualCargoVolume_gte(Double actualCargoVolume) {
		sqlMap.getWhere().and("actual_cargo_volume", QueryType.GTE, actualCargoVolume);
	}
	
	public Double getActualCargoVolume_lte() {
		return sqlMap.getWhere().getValue("actual_cargo_volume", QueryType.LTE);
	}

	public void setActualCargoVolume_lte(Double actualCargoVolume) {
		sqlMap.getWhere().and("actual_cargo_volume", QueryType.LTE, actualCargoVolume);
	}
	
	public Double getLoadingUnloadingCargoVolume_gte() {
		return sqlMap.getWhere().getValue("loading_unloading_cargo_volume", QueryType.GTE);
	}

	public void setLoadingUnloadingCargoVolume_gte(Double loadingUnloadingCargoVolume) {
		sqlMap.getWhere().and("loading_unloading_cargo_volume", QueryType.GTE, loadingUnloadingCargoVolume);
	}
	
	public Double getLoadingUnloadingCargoVolume_lte() {
		return sqlMap.getWhere().getValue("loading_unloading_cargo_volume", QueryType.LTE);
	}

	public void setLoadingUnloadingCargoVolume_lte(Double loadingUnloadingCargoVolume) {
		sqlMap.getWhere().and("loading_unloading_cargo_volume", QueryType.LTE, loadingUnloadingCargoVolume);
	}
	
	public Long getActualVehicleCount_gte() {
		return sqlMap.getWhere().getValue("actual_vehicle_count", QueryType.GTE);
	}

	public void setActualVehicleCount_gte(Long actualVehicleCount) {
		sqlMap.getWhere().and("actual_vehicle_count", QueryType.GTE, actualVehicleCount);
	}
	
	public Long getActualVehicleCount_lte() {
		return sqlMap.getWhere().getValue("actual_vehicle_count", QueryType.LTE);
	}

	public void setActualVehicleCount_lte(Long actualVehicleCount) {
		sqlMap.getWhere().and("actual_vehicle_count", QueryType.LTE, actualVehicleCount);
	}
	
	public Long getLoadingUnloadingVehicleCount_gte() {
		return sqlMap.getWhere().getValue("loading_unloading_vehicle_count", QueryType.GTE);
	}

	public void setLoadingUnloadingVehicleCount_gte(Long loadingUnloadingVehicleCount) {
		sqlMap.getWhere().and("loading_unloading_vehicle_count", QueryType.GTE, loadingUnloadingVehicleCount);
	}
	
	public Long getLoadingUnloadingVehicleCount_lte() {
		return sqlMap.getWhere().getValue("loading_unloading_vehicle_count", QueryType.LTE);
	}

	public void setLoadingUnloadingVehicleCount_lte(Long loadingUnloadingVehicleCount) {
		sqlMap.getWhere().and("loading_unloading_vehicle_count", QueryType.LTE, loadingUnloadingVehicleCount);
	}
	
	public Long getActualPassengerCount_gte() {
		return sqlMap.getWhere().getValue("actual_passenger_count", QueryType.GTE);
	}

	public void setActualPassengerCount_gte(Long actualPassengerCount) {
		sqlMap.getWhere().and("actual_passenger_count", QueryType.GTE, actualPassengerCount);
	}
	
	public Long getActualPassengerCount_lte() {
		return sqlMap.getWhere().getValue("actual_passenger_count", QueryType.LTE);
	}

	public void setActualPassengerCount_lte(Long actualPassengerCount) {
		sqlMap.getWhere().and("actual_passenger_count", QueryType.LTE, actualPassengerCount);
	}
	
	public Long getUpDownPassengerCount_gte() {
		return sqlMap.getWhere().getValue("up_down_passenger_count", QueryType.GTE);
	}

	public void setUpDownPassengerCount_gte(Long upDownPassengerCount) {
		sqlMap.getWhere().and("up_down_passenger_count", QueryType.GTE, upDownPassengerCount);
	}
	
	public Long getUpDownPassengerCount_lte() {
		return sqlMap.getWhere().getValue("up_down_passenger_count", QueryType.LTE);
	}

	public void setUpDownPassengerCount_lte(Long upDownPassengerCount) {
		sqlMap.getWhere().and("up_down_passenger_count", QueryType.LTE, upDownPassengerCount);
	}
	
	public Double getDraftFore_gte() {
		return sqlMap.getWhere().getValue("draft_fore", QueryType.GTE);
	}

	public void setDraftFore_gte(Double draftFore) {
		sqlMap.getWhere().and("draft_fore", QueryType.GTE, draftFore);
	}
	
	public Double getDraftFore_lte() {
		return sqlMap.getWhere().getValue("draft_fore", QueryType.LTE);
	}

	public void setDraftFore_lte(Double draftFore) {
		sqlMap.getWhere().and("draft_fore", QueryType.LTE, draftFore);
	}
	
	public Double getDraftAft_gte() {
		return sqlMap.getWhere().getValue("draft_aft", QueryType.GTE);
	}

	public void setDraftAft_gte(Double draftAft) {
		sqlMap.getWhere().and("draft_aft", QueryType.GTE, draftAft);
	}
	
	public Double getDraftAft_lte() {
		return sqlMap.getWhere().getValue("draft_aft", QueryType.LTE);
	}

	public void setDraftAft_lte(Double draftAft) {
		sqlMap.getWhere().and("draft_aft", QueryType.LTE, draftAft);
	}
	
	public Double getHazardousCargoQuantity_gte() {
		return sqlMap.getWhere().getValue("hazardous_cargo_quantity", QueryType.GTE);
	}

	public void setHazardousCargoQuantity_gte(Double hazardousCargoQuantity) {
		sqlMap.getWhere().and("hazardous_cargo_quantity", QueryType.GTE, hazardousCargoQuantity);
	}
	
	public Double getHazardousCargoQuantity_lte() {
		return sqlMap.getWhere().getValue("hazardous_cargo_quantity", QueryType.LTE);
	}

	public void setHazardousCargoQuantity_lte(Double hazardousCargoQuantity) {
		sqlMap.getWhere().and("hazardous_cargo_quantity", QueryType.LTE, hazardousCargoQuantity);
	}
	
	public Double getActualContainerVolume_gte() {
		return sqlMap.getWhere().getValue("actual_container_volume", QueryType.GTE);
	}

	public void setActualContainerVolume_gte(Double actualContainerVolume) {
		sqlMap.getWhere().and("actual_container_volume", QueryType.GTE, actualContainerVolume);
	}
	
	public Double getActualContainerVolume_lte() {
		return sqlMap.getWhere().getValue("actual_container_volume", QueryType.LTE);
	}

	public void setActualContainerVolume_lte(Double actualContainerVolume) {
		sqlMap.getWhere().and("actual_container_volume", QueryType.LTE, actualContainerVolume);
	}
	
	public Double getLocalContainerOperationsVolume_gte() {
		return sqlMap.getWhere().getValue("local_container_operations_volume", QueryType.GTE);
	}

	public void setLocalContainerOperationsVolume_gte(Double localContainerOperationsVolume) {
		sqlMap.getWhere().and("local_container_operations_volume", QueryType.GTE, localContainerOperationsVolume);
	}
	
	public Double getLocalContainerOperationsVolume_lte() {
		return sqlMap.getWhere().getValue("local_container_operations_volume", QueryType.LTE);
	}

	public void setLocalContainerOperationsVolume_lte(Double localContainerOperationsVolume) {
		sqlMap.getWhere().and("local_container_operations_volume", QueryType.LTE, localContainerOperationsVolume);
	}
	
}