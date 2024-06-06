package com.jeesite.modules.data_collect.ship.entity;

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
 * 海船安全检查信息表Entity
 * @author 王浩宇
 * @version 2024-06-06
 */
@Table(name="ship_inspection", alias="a", label="海船安全检查信息表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="ship_id", attrName="shipId", label="船舶识别号"),
		@Column(name="ship_name_cn", attrName="shipNameCn", label="中文船名", queryType=QueryType.LIKE),
		@Column(name="port_of_registry", attrName="portOfRegistry", label="船籍港", queryType=QueryType.LIKE),
		@Column(name="inspection_type", attrName="inspectionType", label="初查/复查"),
		@Column(name="safety_inspection_type", attrName="safetyInspectionType", label="安检类型"),
		@Column(name="inspection_date", attrName="inspectionDate", label="检查日期"),
		@Column(name="inspection_port", attrName="inspectionPort", label="检查港", queryType=QueryType.LIKE),
		@Column(name="inspection_agency", attrName="inspectionAgency", label="检查机构", queryType=QueryType.LIKE),
		@Column(name="inspector", attrName="inspector", label="检查员", queryType=QueryType.LIKE),
		@Column(name="defect_count", attrName="defectCount", label="缺陷数量", isQuery=false),
		@Column(name="detained", attrName="detained", label="是否被滞留"),
		@Column(name="defect_code", attrName="defectCode", label="缺陷代码"),
		@Column(name="defect_description", attrName="defectDescription", label="缺陷描述", queryType=QueryType.LIKE),
		@Column(name="handling_comments", attrName="handlingComments", label="处理意见说明", queryType=QueryType.LIKE),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ShipInspection extends DataEntity<ShipInspection> {
	
	private static final long serialVersionUID = 1L;
	private String shipId;		// 船舶识别号
	private String shipNameCn;		// 中文船名
	private String portOfRegistry;		// 船籍港
	private String inspectionType;		// 初查/复查
	private String safetyInspectionType;		// 安检类型
	private Date inspectionDate;		// 检查日期
	private String inspectionPort;		// 检查港
	private String inspectionAgency;		// 检查机构
	private String inspector;		// 检查员
	private Long defectCount;		// 缺陷数量
	private String detained;		// 是否被滞留
	private String defectCode;		// 缺陷代码
	private String defectDescription;		// 缺陷描述
	private String handlingComments;		// 处理意见说明

	@ExcelFields({
		@ExcelField(title="船舶识别号", attrName="shipId", align=Align.CENTER, sort=20),
		@ExcelField(title="中文船名", attrName="shipNameCn", align=Align.CENTER, sort=30),
		@ExcelField(title="船籍港", attrName="portOfRegistry", align=Align.CENTER, sort=40),
		@ExcelField(title="初查/复查", attrName="inspectionType", align=Align.CENTER, sort=50),
		@ExcelField(title="安检类型", attrName="safetyInspectionType", align=Align.CENTER, sort=60),
		@ExcelField(title="检查日期", attrName="inspectionDate", align=Align.CENTER, sort=70, dataFormat="yyyy-MM-dd"),
		@ExcelField(title="检查港", attrName="inspectionPort", align=Align.CENTER, sort=80),
		@ExcelField(title="检查机构", attrName="inspectionAgency", align=Align.CENTER, sort=90),
		@ExcelField(title="检查员", attrName="inspector", align=Align.CENTER, sort=100),
		@ExcelField(title="缺陷数量", attrName="defectCount", align=Align.CENTER, sort=110),
		@ExcelField(title="是否被滞留", attrName="detained", align=Align.CENTER, sort=120),
		@ExcelField(title="缺陷代码", attrName="defectCode", align=Align.CENTER, sort=130),
		@ExcelField(title="缺陷描述", attrName="defectDescription", align=Align.CENTER, sort=140),
		@ExcelField(title="处理意见说明", attrName="handlingComments", align=Align.CENTER, sort=150),
	})
	public ShipInspection() {
		this(null);
	}
	
	public ShipInspection(String id){
		super(id);
	}
	
	@NotBlank(message="船舶识别号不能为空")
	@Size(min=0, max=50, message="船舶识别号长度不能超过 50 个字符")
	public String getShipId() {
		return shipId;
	}

	public void setShipId(String shipId) {
		this.shipId = shipId;
	}
	
	@NotBlank(message="中文船名不能为空")
	@Size(min=0, max=100, message="中文船名长度不能超过 100 个字符")
	public String getShipNameCn() {
		return shipNameCn;
	}

	public void setShipNameCn(String shipNameCn) {
		this.shipNameCn = shipNameCn;
	}
	
	@NotBlank(message="船籍港不能为空")
	@Size(min=0, max=100, message="船籍港长度不能超过 100 个字符")
	public String getPortOfRegistry() {
		return portOfRegistry;
	}

	public void setPortOfRegistry(String portOfRegistry) {
		this.portOfRegistry = portOfRegistry;
	}
	
	@NotBlank(message="初查/复查不能为空")
	@Size(min=0, max=50, message="初查/复查长度不能超过 50 个字符")
	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	
	@Size(min=0, max=100, message="安检类型长度不能超过 100 个字符")
	public String getSafetyInspectionType() {
		return safetyInspectionType;
	}

	public void setSafetyInspectionType(String safetyInspectionType) {
		this.safetyInspectionType = safetyInspectionType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="检查日期不能为空")
	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	
	@Size(min=0, max=100, message="检查港长度不能超过 100 个字符")
	public String getInspectionPort() {
		return inspectionPort;
	}

	public void setInspectionPort(String inspectionPort) {
		this.inspectionPort = inspectionPort;
	}
	
	@NotBlank(message="检查机构不能为空")
	@Size(min=0, max=100, message="检查机构长度不能超过 100 个字符")
	public String getInspectionAgency() {
		return inspectionAgency;
	}

	public void setInspectionAgency(String inspectionAgency) {
		this.inspectionAgency = inspectionAgency;
	}
	
	@NotBlank(message="检查员不能为空")
	@Size(min=0, max=100, message="检查员长度不能超过 100 个字符")
	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}
	
	@NotNull(message="缺陷数量不能为空")
	public Long getDefectCount() {
		return defectCount;
	}

	public void setDefectCount(Long defectCount) {
		this.defectCount = defectCount;
	}
	
	@NotBlank(message="是否被滞留不能为空")
	@Size(min=0, max=50, message="是否被滞留长度不能超过 50 个字符")
	public String getDetained() {
		return detained;
	}

	public void setDetained(String detained) {
		this.detained = detained;
	}
	
	@Size(min=0, max=50, message="缺陷代码长度不能超过 50 个字符")
	public String getDefectCode() {
		return defectCode;
	}

	public void setDefectCode(String defectCode) {
		this.defectCode = defectCode;
	}
	
	public String getDefectDescription() {
		return defectDescription;
	}

	public void setDefectDescription(String defectDescription) {
		this.defectDescription = defectDescription;
	}
	
	public String getHandlingComments() {
		return handlingComments;
	}

	public void setHandlingComments(String handlingComments) {
		this.handlingComments = handlingComments;
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