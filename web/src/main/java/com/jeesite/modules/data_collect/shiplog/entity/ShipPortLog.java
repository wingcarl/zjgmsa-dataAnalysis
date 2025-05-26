package com.jeesite.modules.data_collect.shiplog.entity;

import javax.validation.constraints.Size;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 国际航行船舶表Entity
 * @author 王浩宇
 * @version 2025-05-26
 */
@Table(name="ship_port_log", alias="a", label="国际航行船舶表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="chinese_ship_name", attrName="chineseShipName", label="中文船名", queryType=QueryType.LIKE),
		@Column(name="ship_sea_river_flag", attrName="shipSeaRiverFlag", label="海船内河船标志", isQuery=false),
		@Column(name="ship_category", attrName="shipCategory", label="船舶种类"),
		@Column(name="port_operation_status", attrName="portOperationStatus", label="进", comment="进(出)港标志"),
		@Column(name="port_operation_time", attrName="portOperationTime", label="进", comment="进(出)港时间", isUpdateForce=true),
		@Column(name="inspection_agency", attrName="inspectionAgency", label="查验机构"),
		@Column(name="english_ship_name", attrName="englishShipName", label="英文船名", queryType=QueryType.LIKE),
		@Column(name="initial_registration_no", attrName="initialRegistrationNo", label="初次登记号", isQuery=false),
		@Column(name="imo_number", attrName="imoNumber", label="IMO编号"),
		@Column(name="ship_registration_no", attrName="shipRegistrationNo", label="船舶登记号", isQuery=false),
		@Column(name="port_of_registry", attrName="portOfRegistry", label="船籍港"),
		@Column(name="gross_tonnage", attrName="grossTonnage", label="总吨", isQuery=false, isUpdateForce=true),
		@Column(name="net_tonnage", attrName="netTonnage", label="净吨", isQuery=false, isUpdateForce=true),
		@Column(name="deadweight_tonnage", attrName="deadweightTonnage", label="载重吨", isQuery=false, isUpdateForce=true),
		@Column(name="ship_owner", attrName="shipOwner", label="船舶所有人"),
		@Column(name="ship_operator", attrName="shipOperator", label="船舶经营人"),
		@Column(name="origin_destination_port", attrName="originDestinationPort", label="始发/目的港"),
		@Column(name="previous_next_port", attrName="previousNextPort", label="上/下一港"),
		@Column(name="berthing_location", attrName="berthingLocation", label="停泊地点"),
		@Column(name="voyage_number", attrName="voyageNumber", label="航次", isQuery=false),
		@Column(name="actual_cargo_tonnage", attrName="actualCargoTonnage", label="实载货量", isQuery=false, isUpdateForce=true),
		@Column(name="loaded_unloaded_cargo_tonnage", attrName="loadedUnloadedCargoTonnage", label="载/卸货量", isQuery=false, isUpdateForce=true),
		@Column(name="actual_passenger_count", attrName="actualPassengerCount", label="实载客量", isQuery=false, isUpdateForce=true),
		@Column(name="embarked_disembarked_passenger_count", attrName="embarkedDisembarkedPassengerCount", label="上/下客量", isQuery=false, isUpdateForce=true),
		@Column(name="barge_count", attrName="bargeCount", label="驳船数量", isQuery=false, isUpdateForce=true),
		@Column(name="dangerous_goods_details", attrName="dangerousGoodsDetails", label="危险货物", isQuery=false),
		@Column(name="crew_count", attrName="crewCount", label="船员人数", isQuery=false, isUpdateForce=true),
		@Column(name="forward_draft", attrName="forwardDraft", label="前吃水", isQuery=false, isUpdateForce=true),
		@Column(name="aft_draft", attrName="aftDraft", label="后吃水", isQuery=false, isUpdateForce=true),
		@Column(name="applicant_name", attrName="applicantName", label="申办人", queryType=QueryType.LIKE),
		@Column(name="agent_company", attrName="agentCompany", label="代理单位"),
		@Column(name="inspection_datetime", attrName="inspectionDatetime", label="查验时间", isUpdateForce=true),
		@Column(name="handling_officer_name", attrName="handlingOfficerName", label="受理人员", isQuery=false),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ShipPortLog extends DataEntity<ShipPortLog> {
	
	private static final long serialVersionUID = 1L;
	private String chineseShipName;		// 中文船名
	private String shipSeaRiverFlag;		// 海船内河船标志
	private String shipCategory;		// 船舶种类
	private String portOperationStatus;		// 进(出)港标志
	private Date portOperationTime;		// 进(出)港时间
	private String inspectionAgency;		// 查验机构
	private String englishShipName;		// 英文船名
	private String initialRegistrationNo;		// 初次登记号
	private String imoNumber;		// IMO编号
	private String shipRegistrationNo;		// 船舶登记号
	private String portOfRegistry;		// 船籍港
	private Long grossTonnage;		// 总吨
	private Long netTonnage;		// 净吨
	private Long deadweightTonnage;		// 载重吨
	private String shipOwner;		// 船舶所有人
	private String shipOperator;		// 船舶经营人
	private String originDestinationPort;		// 始发/目的港
	private String previousNextPort;		// 上/下一港
	private String berthingLocation;		// 停泊地点
	private String voyageNumber;		// 航次
	private Double actualCargoTonnage;		// 实载货量
	private Double loadedUnloadedCargoTonnage;		// 载/卸货量
	private Long actualPassengerCount;		// 实载客量
	private Long embarkedDisembarkedPassengerCount;		// 上/下客量
	private Long bargeCount;		// 驳船数量
	private String dangerousGoodsDetails;		// 危险货物
	private Long crewCount;		// 船员人数
	private Double forwardDraft;		// 前吃水
	private Double aftDraft;		// 后吃水
	private String applicantName;		// 申办人
	private String agentCompany;		// 代理单位
	private Date inspectionDatetime;		// 查验时间
	private String handlingOfficerName;		// 受理人员

	public ShipPortLog() {
		this(null);
	}
	
	public ShipPortLog(String id){
		super(id);
	}
	
	@Size(min=0, max=255, message="中文船名长度不能超过 255 个字符")
	public String getChineseShipName() {
		return chineseShipName;
	}

	public void setChineseShipName(String chineseShipName) {
		this.chineseShipName = chineseShipName;
	}
	
	@Size(min=0, max=50, message="海船内河船标志长度不能超过 50 个字符")
	public String getShipSeaRiverFlag() {
		return shipSeaRiverFlag;
	}

	public void setShipSeaRiverFlag(String shipSeaRiverFlag) {
		this.shipSeaRiverFlag = shipSeaRiverFlag;
	}
	
	@Size(min=0, max=100, message="船舶种类长度不能超过 100 个字符")
	public String getShipCategory() {
		return shipCategory;
	}

	public void setShipCategory(String shipCategory) {
		this.shipCategory = shipCategory;
	}
	
	@Size(min=0, max=20, message="进长度不能超过 20 个字符")
	public String getPortOperationStatus() {
		return portOperationStatus;
	}

	public void setPortOperationStatus(String portOperationStatus) {
		this.portOperationStatus = portOperationStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPortOperationTime() {
		return portOperationTime;
	}

	public void setPortOperationTime(Date portOperationTime) {
		this.portOperationTime = portOperationTime;
	}
	
	@Size(min=0, max=100, message="查验机构长度不能超过 100 个字符")
	public String getInspectionAgency() {
		return inspectionAgency;
	}

	public void setInspectionAgency(String inspectionAgency) {
		this.inspectionAgency = inspectionAgency;
	}
	
	@Size(min=0, max=255, message="英文船名长度不能超过 255 个字符")
	public String getEnglishShipName() {
		return englishShipName;
	}

	public void setEnglishShipName(String englishShipName) {
		this.englishShipName = englishShipName;
	}
	
	@Size(min=0, max=50, message="初次登记号长度不能超过 50 个字符")
	public String getInitialRegistrationNo() {
		return initialRegistrationNo;
	}

	public void setInitialRegistrationNo(String initialRegistrationNo) {
		this.initialRegistrationNo = initialRegistrationNo;
	}
	
	@Size(min=0, max=20, message="IMO编号长度不能超过 20 个字符")
	public String getImoNumber() {
		return imoNumber;
	}

	public void setImoNumber(String imoNumber) {
		this.imoNumber = imoNumber;
	}
	
	@Size(min=0, max=50, message="船舶登记号长度不能超过 50 个字符")
	public String getShipRegistrationNo() {
		return shipRegistrationNo;
	}

	public void setShipRegistrationNo(String shipRegistrationNo) {
		this.shipRegistrationNo = shipRegistrationNo;
	}
	
	@Size(min=0, max=100, message="船籍港长度不能超过 100 个字符")
	public String getPortOfRegistry() {
		return portOfRegistry;
	}

	public void setPortOfRegistry(String portOfRegistry) {
		this.portOfRegistry = portOfRegistry;
	}
	
	public Long getGrossTonnage() {
		return grossTonnage;
	}

	public void setGrossTonnage(Long grossTonnage) {
		this.grossTonnage = grossTonnage;
	}
	
	public Long getNetTonnage() {
		return netTonnage;
	}

	public void setNetTonnage(Long netTonnage) {
		this.netTonnage = netTonnage;
	}
	
	public Long getDeadweightTonnage() {
		return deadweightTonnage;
	}

	public void setDeadweightTonnage(Long deadweightTonnage) {
		this.deadweightTonnage = deadweightTonnage;
	}
	
	public String getShipOwner() {
		return shipOwner;
	}

	public void setShipOwner(String shipOwner) {
		this.shipOwner = shipOwner;
	}
	
	public String getShipOperator() {
		return shipOperator;
	}

	public void setShipOperator(String shipOperator) {
		this.shipOperator = shipOperator;
	}
	
	@Size(min=0, max=100, message="始发/目的港长度不能超过 100 个字符")
	public String getOriginDestinationPort() {
		return originDestinationPort;
	}

	public void setOriginDestinationPort(String originDestinationPort) {
		this.originDestinationPort = originDestinationPort;
	}
	
	@Size(min=0, max=100, message="上/下一港长度不能超过 100 个字符")
	public String getPreviousNextPort() {
		return previousNextPort;
	}

	public void setPreviousNextPort(String previousNextPort) {
		this.previousNextPort = previousNextPort;
	}
	
	@Size(min=0, max=255, message="停泊地点长度不能超过 255 个字符")
	public String getBerthingLocation() {
		return berthingLocation;
	}

	public void setBerthingLocation(String berthingLocation) {
		this.berthingLocation = berthingLocation;
	}
	
	@Size(min=0, max=50, message="航次长度不能超过 50 个字符")
	public String getVoyageNumber() {
		return voyageNumber;
	}

	public void setVoyageNumber(String voyageNumber) {
		this.voyageNumber = voyageNumber;
	}
	
	public Double getActualCargoTonnage() {
		return actualCargoTonnage;
	}

	public void setActualCargoTonnage(Double actualCargoTonnage) {
		this.actualCargoTonnage = actualCargoTonnage;
	}
	
	public Double getLoadedUnloadedCargoTonnage() {
		return loadedUnloadedCargoTonnage;
	}

	public void setLoadedUnloadedCargoTonnage(Double loadedUnloadedCargoTonnage) {
		this.loadedUnloadedCargoTonnage = loadedUnloadedCargoTonnage;
	}
	
	public Long getActualPassengerCount() {
		return actualPassengerCount;
	}

	public void setActualPassengerCount(Long actualPassengerCount) {
		this.actualPassengerCount = actualPassengerCount;
	}
	
	public Long getEmbarkedDisembarkedPassengerCount() {
		return embarkedDisembarkedPassengerCount;
	}

	public void setEmbarkedDisembarkedPassengerCount(Long embarkedDisembarkedPassengerCount) {
		this.embarkedDisembarkedPassengerCount = embarkedDisembarkedPassengerCount;
	}
	
	public Long getBargeCount() {
		return bargeCount;
	}

	public void setBargeCount(Long bargeCount) {
		this.bargeCount = bargeCount;
	}
	
	public String getDangerousGoodsDetails() {
		return dangerousGoodsDetails;
	}

	public void setDangerousGoodsDetails(String dangerousGoodsDetails) {
		this.dangerousGoodsDetails = dangerousGoodsDetails;
	}
	
	public Long getCrewCount() {
		return crewCount;
	}

	public void setCrewCount(Long crewCount) {
		this.crewCount = crewCount;
	}
	
	public Double getForwardDraft() {
		return forwardDraft;
	}

	public void setForwardDraft(Double forwardDraft) {
		this.forwardDraft = forwardDraft;
	}
	
	public Double getAftDraft() {
		return aftDraft;
	}

	public void setAftDraft(Double aftDraft) {
		this.aftDraft = aftDraft;
	}
	
	@Size(min=0, max=100, message="申办人长度不能超过 100 个字符")
	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	
	@Size(min=0, max=255, message="代理单位长度不能超过 255 个字符")
	public String getAgentCompany() {
		return agentCompany;
	}

	public void setAgentCompany(String agentCompany) {
		this.agentCompany = agentCompany;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInspectionDatetime() {
		return inspectionDatetime;
	}

	public void setInspectionDatetime(Date inspectionDatetime) {
		this.inspectionDatetime = inspectionDatetime;
	}
	
	@Size(min=0, max=100, message="受理人员长度不能超过 100 个字符")
	public String getHandlingOfficerName() {
		return handlingOfficerName;
	}

	public void setHandlingOfficerName(String handlingOfficerName) {
		this.handlingOfficerName = handlingOfficerName;
	}
	
	public Date getPortOperationTime_gte() {
		return sqlMap.getWhere().getValue("port_operation_time", QueryType.GTE);
	}

	public void setPortOperationTime_gte(Date portOperationTime) {
		sqlMap.getWhere().and("port_operation_time", QueryType.GTE, portOperationTime);
	}
	
	public Date getPortOperationTime_lte() {
		return sqlMap.getWhere().getValue("port_operation_time", QueryType.LTE);
	}

	public void setPortOperationTime_lte(Date portOperationTime) {
		sqlMap.getWhere().and("port_operation_time", QueryType.LTE, portOperationTime);
	}
	
}