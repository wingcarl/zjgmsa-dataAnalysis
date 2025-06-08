package com.jeesite.modules.data_collect.danger.entity;

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
 * 危险货物申报表Entity
 * @author 王浩宇
 * @version 2025-06-08
 */
@Table(name="danger_cargo_declaration", alias="a", label="危险货物申报表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="declaration_number", attrName="declarationNumber", label="申报单号"),
		@Column(name="declaring_unit", attrName="declaringUnit", label="申报单位"),
		@Column(name="declarer_name", attrName="declarerName", label="申报员", queryType=QueryType.LIKE),
		@Column(name="declaration_date", attrName="declarationDate", label="申报日期", isUpdateForce=true),
		@Column(name="voyage_number", attrName="voyageNumber", label="航次"),
		@Column(name="berth", attrName="berth", label="泊位"),
		@Column(name="approval_unit", attrName="approvalUnit", label="审批单位"),
		@Column(name="approver_name", attrName="approverName", label="审批人员", queryType=QueryType.LIKE),
		@Column(name="trade_type", attrName="tradeType", label="内外贸 ", comment="内外贸 (例如: 内贸, 外贸)"),
		@Column(name="port_movement", attrName="portMovement", label="进出港 ", comment="进出港 (例如: 进港, 出港)"),
		@Column(name="ship_name_cn", attrName="shipNameCn", label="中文船名", queryType=QueryType.LIKE),
		@Column(name="ship_name_en", attrName="shipNameEn", label="英文船名", queryType=QueryType.LIKE),
		@Column(name="port_of_registry", attrName="portOfRegistry", label="船籍港"),
		@Column(name="nationality", attrName="nationality", label="国籍"),
		@Column(name="ship_class", attrName="shipClass", label="船舶类型 ", comment="船舶类型 (例如: 内河船)"),
		@Column(name="ship_type", attrName="shipType", label="船舶种类 ", comment="船舶种类 (例如: 油船, 散货船)"),
		@Column(name="gross_tonnage", attrName="grossTonnage", label="总吨", isUpdateForce=true),
		@Column(name="net_tonnage", attrName="netTonnage", label="净吨", isUpdateForce=true),
		@Column(name="deadweight_tonnage", attrName="deadweightTonnage", label="总载重吨", isUpdateForce=true),
		@Column(name="cargo_name", attrName="cargoName", label="货物名称", queryType=QueryType.LIKE),
		@Column(name="cargo_flow_direction", attrName="cargoFlowDirection", label="货物流向"),
		@Column(name="dangerous_goods_code", attrName="dangerousGoodsCode", label="危规编号"),
		@Column(name="total_weight", attrName="totalWeight", label="总重量 ", comment="总重量 (吨)", isUpdateForce=true),
		@Column(name="consignor", attrName="consignor", label="托运人"),
		@Column(name="cargo_owner", attrName="cargoOwner", label="货主"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
	}, orderBy="a.update_date DESC"
)
public class DangerCargoDeclaration extends DataEntity<DangerCargoDeclaration> {
	
	private static final long serialVersionUID = 1L;
	private String declarationNumber;		// 申报单号
	private String declaringUnit;		// 申报单位
	private String declarerName;		// 申报员
	private Date declarationDate;		// 申报日期
	private String voyageNumber;		// 航次
	private String berth;		// 泊位
	private String approvalUnit;		// 审批单位
	private String approverName;		// 审批人员
	private String tradeType;		// 内外贸 (例如: 内贸, 外贸)
	private String portMovement;		// 进出港 (例如: 进港, 出港)
	private String shipNameCn;		// 中文船名
	private String shipNameEn;		// 英文船名
	private String portOfRegistry;		// 船籍港
	private String nationality;		// 国籍
	private String shipClass;		// 船舶类型 (例如: 内河船)
	private String shipType;		// 船舶种类 (例如: 油船, 散货船)
	private Long grossTonnage;		// 总吨
	private Long netTonnage;		// 净吨
	private Long deadweightTonnage;		// 总载重吨
	private String cargoName;		// 货物名称
	private String cargoFlowDirection;		// 货物流向
	private String dangerousGoodsCode;		// 危规编号
	private Double totalWeight;		// 总重量 (吨)
	private String consignor;		// 托运人
	private String cargoOwner;		// 货主

	@ExcelFields({
		@ExcelField(title="申报单号", attrName="declarationNumber", align=Align.CENTER, sort=20),
		@ExcelField(title="申报单位", attrName="declaringUnit", align=Align.CENTER, sort=30),
		@ExcelField(title="申报员", attrName="declarerName", align=Align.CENTER, sort=40),
		@ExcelField(title="申报日期", attrName="declarationDate", align=Align.CENTER, sort=50, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="航次", attrName="voyageNumber", align=Align.CENTER, sort=60),
		@ExcelField(title="泊位", attrName="berth", align=Align.CENTER, sort=70),
		@ExcelField(title="审批单位", attrName="approvalUnit", align=Align.CENTER, sort=80),
		@ExcelField(title="审批人员", attrName="approverName", align=Align.CENTER, sort=90),
		@ExcelField(title="内外贸 ", attrName="tradeType", align=Align.CENTER, sort=100),
		@ExcelField(title="进出港 ", attrName="portMovement", align=Align.CENTER, sort=110),
		@ExcelField(title="中文船名", attrName="shipNameCn", align=Align.CENTER, sort=120),
		@ExcelField(title="英文船名", attrName="shipNameEn", align=Align.CENTER, sort=130),
		@ExcelField(title="船籍港", attrName="portOfRegistry", align=Align.CENTER, sort=140),
		@ExcelField(title="国籍", attrName="nationality", align=Align.CENTER, sort=150),
		@ExcelField(title="船舶类型 ", attrName="shipClass", align=Align.CENTER, sort=160),
		@ExcelField(title="船舶种类 ", attrName="shipType", align=Align.CENTER, sort=170),
		@ExcelField(title="总吨", attrName="grossTonnage", align=Align.CENTER, sort=180),
		@ExcelField(title="净吨", attrName="netTonnage", align=Align.CENTER, sort=190),
		@ExcelField(title="总载重吨", attrName="deadweightTonnage", align=Align.CENTER, sort=200),
		@ExcelField(title="货物名称", attrName="cargoName", align=Align.CENTER, sort=210),
		@ExcelField(title="货物流向", attrName="cargoFlowDirection", align=Align.CENTER, sort=220),
		@ExcelField(title="危规编号", attrName="dangerousGoodsCode", align=Align.CENTER, sort=230),
		@ExcelField(title="总重量 ", attrName="totalWeight", align=Align.CENTER, sort=240),
		@ExcelField(title="托运人", attrName="consignor", align=Align.CENTER, sort=250),
		@ExcelField(title="货主", attrName="cargoOwner", align=Align.CENTER, sort=260),
	})
	public DangerCargoDeclaration() {
		this(null);
	}
	
	public DangerCargoDeclaration(String id){
		super(id);
	}
	
	@Size(min=0, max=50, message="申报单号长度不能超过 50 个字符")
	public String getDeclarationNumber() {
		return declarationNumber;
	}

	public void setDeclarationNumber(String declarationNumber) {
		this.declarationNumber = declarationNumber;
	}
	
	@Size(min=0, max=255, message="申报单位长度不能超过 255 个字符")
	public String getDeclaringUnit() {
		return declaringUnit;
	}

	public void setDeclaringUnit(String declaringUnit) {
		this.declaringUnit = declaringUnit;
	}
	
	@Size(min=0, max=100, message="申报员长度不能超过 100 个字符")
	public String getDeclarerName() {
		return declarerName;
	}

	public void setDeclarerName(String declarerName) {
		this.declarerName = declarerName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDeclarationDate() {
		return declarationDate;
	}

	public void setDeclarationDate(Date declarationDate) {
		this.declarationDate = declarationDate;
	}
	
	@Size(min=0, max=100, message="航次长度不能超过 100 个字符")
	public String getVoyageNumber() {
		return voyageNumber;
	}

	public void setVoyageNumber(String voyageNumber) {
		this.voyageNumber = voyageNumber;
	}
	
	@Size(min=0, max=255, message="泊位长度不能超过 255 个字符")
	public String getBerth() {
		return berth;
	}

	public void setBerth(String berth) {
		this.berth = berth;
	}
	
	@Size(min=0, max=255, message="审批单位长度不能超过 255 个字符")
	public String getApprovalUnit() {
		return approvalUnit;
	}

	public void setApprovalUnit(String approvalUnit) {
		this.approvalUnit = approvalUnit;
	}
	
	@Size(min=0, max=100, message="审批人员长度不能超过 100 个字符")
	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	
	@Size(min=0, max=20, message="内外贸 长度不能超过 20 个字符")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	@Size(min=0, max=20, message="进出港 长度不能超过 20 个字符")
	public String getPortMovement() {
		return portMovement;
	}

	public void setPortMovement(String portMovement) {
		this.portMovement = portMovement;
	}
	
	@Size(min=0, max=100, message="中文船名长度不能超过 100 个字符")
	public String getShipNameCn() {
		return shipNameCn;
	}

	public void setShipNameCn(String shipNameCn) {
		this.shipNameCn = shipNameCn;
	}
	
	@Size(min=0, max=100, message="英文船名长度不能超过 100 个字符")
	public String getShipNameEn() {
		return shipNameEn;
	}

	public void setShipNameEn(String shipNameEn) {
		this.shipNameEn = shipNameEn;
	}
	
	@Size(min=0, max=100, message="船籍港长度不能超过 100 个字符")
	public String getPortOfRegistry() {
		return portOfRegistry;
	}

	public void setPortOfRegistry(String portOfRegistry) {
		this.portOfRegistry = portOfRegistry;
	}
	
	@Size(min=0, max=50, message="国籍长度不能超过 50 个字符")
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	@Size(min=0, max=50, message="船舶类型 长度不能超过 50 个字符")
	public String getShipClass() {
		return shipClass;
	}

	public void setShipClass(String shipClass) {
		this.shipClass = shipClass;
	}
	
	@Size(min=0, max=50, message="船舶种类 长度不能超过 50 个字符")
	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
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
	
	@Size(min=0, max=255, message="货物名称长度不能超过 255 个字符")
	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	
	@Size(min=0, max=50, message="货物流向长度不能超过 50 个字符")
	public String getCargoFlowDirection() {
		return cargoFlowDirection;
	}

	public void setCargoFlowDirection(String cargoFlowDirection) {
		this.cargoFlowDirection = cargoFlowDirection;
	}
	
	@Size(min=0, max=50, message="危规编号长度不能超过 50 个字符")
	public String getDangerousGoodsCode() {
		return dangerousGoodsCode;
	}

	public void setDangerousGoodsCode(String dangerousGoodsCode) {
		this.dangerousGoodsCode = dangerousGoodsCode;
	}
	
	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	@Size(min=0, max=255, message="托运人长度不能超过 255 个字符")
	public String getConsignor() {
		return consignor;
	}

	public void setConsignor(String consignor) {
		this.consignor = consignor;
	}
	
	@Size(min=0, max=255, message="货主长度不能超过 255 个字符")
	public String getCargoOwner() {
		return cargoOwner;
	}

	public void setCargoOwner(String cargoOwner) {
		this.cargoOwner = cargoOwner;
	}
	
	public Date getDeclarationDate_gte() {
		return sqlMap.getWhere().getValue("declaration_date", QueryType.GTE);
	}

	public void setDeclarationDate_gte(Date declarationDate) {
		sqlMap.getWhere().and("declaration_date", QueryType.GTE, declarationDate);
	}
	
	public Date getDeclarationDate_lte() {
		return sqlMap.getWhere().getValue("declaration_date", QueryType.LTE);
	}

	public void setDeclarationDate_lte(Date declarationDate) {
		sqlMap.getWhere().and("declaration_date", QueryType.LTE, declarationDate);
	}
	
}