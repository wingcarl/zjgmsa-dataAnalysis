package com.jeesite.modules.data_collect.largeship.entity;

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
 * 大型受限船舶Entity
 * @author 王浩宇
 * @version 2025-04-11
 */
@Table(name="large_restricted_vessels", alias="a", label="大型受限船舶信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="no", attrName="no", label="序号", isUpdateForce=true),
		@Column(name="operation_date", attrName="operationDate", label="日期", isUpdateForce=true),
		@Column(name="chinese_ship_name", attrName="chineseShipName", label="中文船名", queryType=QueryType.LIKE),
		@Column(name="english_ship_name", attrName="englishShipName", label="英文船名", queryType=QueryType.LIKE),
		@Column(name="mmsi", attrName="mmsi", label="海上移动识别码", comment="海上移动识别码（MMSI）"),
		@Column(name="port_of_registry", attrName="portOfRegistry", label="船籍港", queryType=QueryType.LIKE),
		@Column(name="owner_or_operator", attrName="ownerOrOperator", label="所有人或经营人", queryType=QueryType.LIKE),
		@Column(name="agent_name", attrName="agentName", label="代理名称", queryType=QueryType.LIKE),
		@Column(name="agent_phone", attrName="agentPhone", label="代理联系电话", isQuery=false),
		@Column(name="ship_length", attrName="shipLength", label="船长", comment="船长（米）", isUpdateForce=true),
		@Column(name="ship_width", attrName="shipWidth", label="船宽", comment="船宽（米）", isUpdateForce=true),
		@Column(name="max_height_above_water", attrName="maxHeightAboveWater", label="船舶水面以上最大高度", comment="船舶水面以上最大高度（米）", isUpdateForce=true),
		@Column(name="max_freshwater_draft", attrName="maxFreshwaterDraft", label="船舶最大淡水吃水", comment="船舶最大淡水吃水（米）", isUpdateForce=true),
		@Column(name="gross_tonnage", attrName="grossTonnage", label="总吨", isUpdateForce=true),
		@Column(name="deadweight_tonnage", attrName="deadweightTonnage", label="核定载重吨", isUpdateForce=true),
		@Column(name="actual_cargo_status", attrName="actualCargoStatus", label="实际载货情况"),
		@Column(name="cargo_name", attrName="cargoName", label="货物名称", queryType=QueryType.LIKE),
		@Column(name="berth_name", attrName="berthName", label="靠泊码头或泊位名称", queryType=QueryType.LIKE),
		@Column(name="berth_tonnage_grade", attrName="berthTonnageGrade", label="靠泊码头或泊位等级", comment="靠泊码头或泊位等级（吨级）"),
		@Column(name="entry_exit_direction", attrName="entryExitDirection", label="进/出", queryType=QueryType.LIKE),
		@Column(name="expected_entry_exit_date", attrName="expectedEntryExitDate", label="预计进/出港日期"),
		@Column(name="safety_measures", attrName="safetyMeasures", label="安全措施", queryType=QueryType.LIKE),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
	}, orderBy="a.update_date DESC"
)
public class LargeRestrictedVessels extends DataEntity<LargeRestrictedVessels> {
	
	private static final long serialVersionUID = 1L;
	private Long no;		// 序号
	private Date operationDate;		// 日期
	private String chineseShipName;		// 中文船名
	private String englishShipName;		// 英文船名
	private String mmsi;		// 海上移动识别码（MMSI）
	private String portOfRegistry;		// 船籍港
	private String ownerOrOperator;		// 所有人或经营人
	private String agentName;		// 代理名称
	private String agentPhone;		// 代理联系电话
	private Double shipLength;		// 船长（米）
	private Double shipWidth;		// 船宽（米）
	private Double maxHeightAboveWater;		// 船舶水面以上最大高度（米）
	private Double maxFreshwaterDraft;		// 船舶最大淡水吃水（米）
	private Double grossTonnage;		// 总吨
	private Double deadweightTonnage;		// 核定载重吨
	private String actualCargoStatus;		// 实际载货情况
	private String cargoName;		// 货物名称
	private String berthName;		// 靠泊码头或泊位名称
	private String berthTonnageGrade;		// 靠泊码头或泊位等级（吨级）
	private String entryExitDirection;		// 进/出
	private String expectedEntryExitDate;		// 预计进/出港日期
	private String safetyMeasures;		// 安全措施

	@ExcelFields({
		@ExcelField(title="序号", attrName="no", align=Align.CENTER, sort=20),
		@ExcelField(title="日期", attrName="operationDate", align=Align.CENTER, sort=30, dataFormat="yyyy-MM-dd"),
		@ExcelField(title="中文船名", attrName="chineseShipName", align=Align.CENTER, sort=40),
		@ExcelField(title="英文船名", attrName="englishShipName", align=Align.CENTER, sort=50),
		@ExcelField(title="海上移动识别码", attrName="mmsi", align=Align.CENTER, sort=60),
		@ExcelField(title="船籍港", attrName="portOfRegistry", align=Align.CENTER, sort=70),
		@ExcelField(title="所有人或经营人", attrName="ownerOrOperator", align=Align.CENTER, sort=80),
		@ExcelField(title="代理名称", attrName="agentName", align=Align.CENTER, sort=90),
		@ExcelField(title="代理联系电话", attrName="agentPhone", align=Align.CENTER, sort=100),
		@ExcelField(title="船长", attrName="shipLength", align=Align.CENTER, sort=110),
		@ExcelField(title="船宽", attrName="shipWidth", align=Align.CENTER, sort=120),
		@ExcelField(title="船舶水面以上最大高度", attrName="maxHeightAboveWater", align=Align.CENTER, sort=130),
		@ExcelField(title="船舶最大淡水吃水", attrName="maxFreshwaterDraft", align=Align.CENTER, sort=140),
		@ExcelField(title="总吨", attrName="grossTonnage", align=Align.CENTER, sort=150),
		@ExcelField(title="核定载重吨", attrName="deadweightTonnage", align=Align.CENTER, sort=160),
		@ExcelField(title="实际载货情况", attrName="actualCargoStatus", align=Align.CENTER, sort=170),
		@ExcelField(title="货物名称", attrName="cargoName", align=Align.CENTER, sort=180),
		@ExcelField(title="靠泊码头或泊位名称", attrName="berthName", align=Align.CENTER, sort=190),
		@ExcelField(title="靠泊码头或泊位等级", attrName="berthTonnageGrade", align=Align.CENTER, sort=200),
		@ExcelField(title="进/出", attrName="entryExitDirection", align=Align.CENTER, sort=210),
		@ExcelField(title="预计进/出港日期", attrName="expectedEntryExitDate", align=Align.CENTER, sort=220),
		@ExcelField(title="安全措施", attrName="safetyMeasures", align=Align.CENTER, sort=230),
		@ExcelField(title="备注信息", attrName="remarks", align=Align.CENTER, sort=280),
	})
	public LargeRestrictedVessels() {
		this(null);
	}
	
	public LargeRestrictedVessels(String id){
		super(id);
	}
	
	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}
	
	@Size(min=0, max=100, message="中文船名长度不能超过 100 个字符")
	public String getChineseShipName() {
		return chineseShipName;
	}

	public void setChineseShipName(String chineseShipName) {
		this.chineseShipName = chineseShipName;
	}
	
	@Size(min=0, max=100, message="英文船名长度不能超过 100 个字符")
	public String getEnglishShipName() {
		return englishShipName;
	}

	public void setEnglishShipName(String englishShipName) {
		this.englishShipName = englishShipName;
	}
	
	@Size(min=0, max=20, message="海上移动识别码长度不能超过 20 个字符")
	public String getMmsi() {
		return mmsi;
	}

	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}
	
	@Size(min=0, max=100, message="船籍港长度不能超过 100 个字符")
	public String getPortOfRegistry() {
		return portOfRegistry;
	}

	public void setPortOfRegistry(String portOfRegistry) {
		this.portOfRegistry = portOfRegistry;
	}
	
	@Size(min=0, max=100, message="所有人或经营人长度不能超过 100 个字符")
	public String getOwnerOrOperator() {
		return ownerOrOperator;
	}

	public void setOwnerOrOperator(String ownerOrOperator) {
		this.ownerOrOperator = ownerOrOperator;
	}
	
	@Size(min=0, max=100, message="代理名称长度不能超过 100 个字符")
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	@Size(min=0, max=20, message="代理联系电话长度不能超过 20 个字符")
	public String getAgentPhone() {
		return agentPhone;
	}

	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
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
	
	public Double getMaxHeightAboveWater() {
		return maxHeightAboveWater;
	}

	public void setMaxHeightAboveWater(Double maxHeightAboveWater) {
		this.maxHeightAboveWater = maxHeightAboveWater;
	}
	
	public Double getMaxFreshwaterDraft() {
		return maxFreshwaterDraft;
	}

	public void setMaxFreshwaterDraft(Double maxFreshwaterDraft) {
		this.maxFreshwaterDraft = maxFreshwaterDraft;
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
	
	@Size(min=0, max=200, message="实际载货情况长度不能超过 200 个字符")
	public String getActualCargoStatus() {
		return actualCargoStatus;
	}

	public void setActualCargoStatus(String actualCargoStatus) {
		this.actualCargoStatus = actualCargoStatus;
	}
	
	@Size(min=0, max=200, message="货物名称长度不能超过 200 个字符")
	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	
	@Size(min=0, max=200, message="靠泊码头或泊位名称长度不能超过 200 个字符")
	public String getBerthName() {
		return berthName;
	}

	public void setBerthName(String berthName) {
		this.berthName = berthName;
	}
	
	@Size(min=0, max=100, message="靠泊码头或泊位等级长度不能超过 100 个字符")
	public String getBerthTonnageGrade() {
		return berthTonnageGrade;
	}

	public void setBerthTonnageGrade(String berthTonnageGrade) {
		this.berthTonnageGrade = berthTonnageGrade;
	}
	
	public String getEntryExitDirection() {
		return entryExitDirection;
	}

	public void setEntryExitDirection(String entryExitDirection) {
		this.entryExitDirection = entryExitDirection;
	}
	
	public String getExpectedEntryExitDate() {
		return expectedEntryExitDate;
	}

	public void setExpectedEntryExitDate(String expectedEntryExitDate) {
		this.expectedEntryExitDate = expectedEntryExitDate;
	}
	
	public String getSafetyMeasures() {
		return safetyMeasures;
	}

	public void setSafetyMeasures(String safetyMeasures) {
		this.safetyMeasures = safetyMeasures;
	}
	
	public Date getOperationDate_gte() {
		return sqlMap.getWhere().getValue("operation_date", QueryType.GTE);
	}

	public void setOperationDate_gte(Date operationDate) {
		sqlMap.getWhere().and("operation_date", QueryType.GTE, operationDate);
	}
	
	public Date getOperationDate_lte() {
		return sqlMap.getWhere().getValue("operation_date", QueryType.LTE);
	}

	public void setOperationDate_lte(Date operationDate) {
		sqlMap.getWhere().and("operation_date", QueryType.LTE, operationDate);
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
	
	public Double getMaxHeightAboveWater_gte() {
		return sqlMap.getWhere().getValue("max_height_above_water", QueryType.GTE);
	}

	public void setMaxHeightAboveWater_gte(Double maxHeightAboveWater) {
		sqlMap.getWhere().and("max_height_above_water", QueryType.GTE, maxHeightAboveWater);
	}
	
	public Double getMaxHeightAboveWater_lte() {
		return sqlMap.getWhere().getValue("max_height_above_water", QueryType.LTE);
	}

	public void setMaxHeightAboveWater_lte(Double maxHeightAboveWater) {
		sqlMap.getWhere().and("max_height_above_water", QueryType.LTE, maxHeightAboveWater);
	}
	
	public Double getMaxFreshwaterDraft_gte() {
		return sqlMap.getWhere().getValue("max_freshwater_draft", QueryType.GTE);
	}

	public void setMaxFreshwaterDraft_gte(Double maxFreshwaterDraft) {
		sqlMap.getWhere().and("max_freshwater_draft", QueryType.GTE, maxFreshwaterDraft);
	}
	
	public Double getMaxFreshwaterDraft_lte() {
		return sqlMap.getWhere().getValue("max_freshwater_draft", QueryType.LTE);
	}

	public void setMaxFreshwaterDraft_lte(Double maxFreshwaterDraft) {
		sqlMap.getWhere().and("max_freshwater_draft", QueryType.LTE, maxFreshwaterDraft);
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
	
	public String getBerthTonnageGrade_gte() {
		return sqlMap.getWhere().getValue("berth_tonnage_grade", QueryType.GTE);
	}

	public void setBerthTonnageGrade_gte(String berthTonnageGrade) {
		sqlMap.getWhere().and("berth_tonnage_grade", QueryType.GTE, berthTonnageGrade);
	}
	
	public String getBerthTonnageGrade_lte() {
		return sqlMap.getWhere().getValue("berth_tonnage_grade", QueryType.LTE);
	}

	public void setBerthTonnageGrade_lte(String berthTonnageGrade) {
		sqlMap.getWhere().and("berth_tonnage_grade", QueryType.LTE, berthTonnageGrade);
	}
	
	public String getExpectedEntryExitDate_gte() {
		return sqlMap.getWhere().getValue("expected_entry_exit_date", QueryType.GTE);
	}

	public void setExpectedEntryExitDate_gte(String expectedEntryExitDate) {
		sqlMap.getWhere().and("expected_entry_exit_date", QueryType.GTE, expectedEntryExitDate);
	}
	
	public String getExpectedEntryExitDate_lte() {
		return sqlMap.getWhere().getValue("expected_entry_exit_date", QueryType.LTE);
	}

	public void setExpectedEntryExitDate_lte(String expectedEntryExitDate) {
		sqlMap.getWhere().and("expected_entry_exit_date", QueryType.LTE, expectedEntryExitDate);
	}
	
}