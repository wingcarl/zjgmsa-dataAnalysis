package com.jeesite.modules.data_collect.shiponsite.entity;

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
 * 船舶现场监督检查表Entity
 * @author 王浩宇
 * @version 2024-06-06
 */
@Table(name="ship_on_site_inspection", alias="a", label="船舶现场监督检查表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="ship_id", attrName="shipId", label="船舶识别号", queryType=QueryType.LIKE),
		@Column(name="ship_name_cn", attrName="shipNameCn", label="中文船名", queryType=QueryType.LIKE),
		@Column(name="inspection_name", attrName="inspectionName", label="专项检查名称", queryType=QueryType.LIKE),
		@Column(name="inspection_type", attrName="inspectionType", label="专项检查类型", queryType=QueryType.LIKE),
		@Column(name="initial_or_recheck", attrName="initialOrRecheck", label="初查/复查", queryType=QueryType.LIKE),
		@Column(name="inspection_date", attrName="inspectionDate", label="检查日期"),
		@Column(name="inspection_location", attrName="inspectionLocation", label="检查地点", queryType=QueryType.LIKE),
		@Column(name="inspection_agency", attrName="inspectionAgency", label="检查机构", queryType=QueryType.LIKE),
		@Column(name="inspector", attrName="inspector", label="检查员", queryType=QueryType.LIKE),
		@Column(name="issue_found", attrName="issueFound", label="发现问题或违章", queryType=QueryType.LIKE),
		@Column(name="inspection_content_code", attrName="inspectionContentCode", label="检查内容代码", queryType=QueryType.LIKE),
		@Column(name="inspection_content", attrName="inspectionContent", label="检查内容", queryType=QueryType.LIKE),
		@Column(name="issue_description", attrName="issueDescription", label="违章或问题说明", queryType=QueryType.LIKE),
		@Column(name="handling_code", attrName="handlingCode", label="处理意见代码", queryType=QueryType.LIKE),
		@Column(name="handling_comments", attrName="handlingComments", label="处理意见说明", queryType=QueryType.LIKE),
		@Column(name="corrected", attrName="corrected", label="是否纠正", queryType=QueryType.LIKE),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
	}, orderBy="a.update_date DESC"
)
public class ShipOnSiteInspection extends DataEntity<ShipOnSiteInspection> {
	
	private static final long serialVersionUID = 1L;
	private String shipId;		// 船舶识别号
	private String shipNameCn;		// 中文船名
	private String inspectionName;		// 专项检查名称
	private String inspectionType;		// 专项检查类型
	private String initialOrRecheck;		// 初查/复查
	private Date inspectionDate;		// 检查日期
	private String inspectionLocation;		// 检查地点
	private String inspectionAgency;		// 检查机构
	private String inspector;		// 检查员
	private String issueFound;		// 发现问题或违章
	private String inspectionContentCode;		// 检查内容代码
	private String inspectionContent;		// 检查内容
	private String issueDescription;		// 违章或问题说明
	private String handlingCode;		// 处理意见代码
	private String handlingComments;		// 处理意见说明
	private String corrected;		// 是否纠正

	@ExcelFields({
		@ExcelField(title="船舶识别号", attrName="shipId", align=Align.CENTER, sort=20),
		@ExcelField(title="中文船名", attrName="shipNameCn", align=Align.CENTER, sort=30),
		@ExcelField(title="专项检查名称", attrName="inspectionName", align=Align.CENTER, sort=40),
		@ExcelField(title="专项检查类型", attrName="inspectionType", align=Align.CENTER, sort=50),
		@ExcelField(title="初查/复查", attrName="initialOrRecheck", align=Align.CENTER, sort=60),
		@ExcelField(title="检查日期", attrName="inspectionDate", align=Align.CENTER, sort=70, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="检查地点", attrName="inspectionLocation", align=Align.CENTER, sort=80),
		@ExcelField(title="检查机构", attrName="inspectionAgency", align=Align.CENTER, sort=90),
		@ExcelField(title="检查员", attrName="inspector", align=Align.CENTER, sort=100),
		@ExcelField(title="发现问题或违章", attrName="issueFound", align=Align.CENTER, sort=110),
		@ExcelField(title="检查内容代码", attrName="inspectionContentCode", align=Align.CENTER, sort=120),
		@ExcelField(title="检查内容", attrName="inspectionContent", align=Align.CENTER, sort=130),
		@ExcelField(title="违章或问题说明", attrName="issueDescription", align=Align.CENTER, sort=140),
		@ExcelField(title="处理意见代码", attrName="handlingCode", align=Align.CENTER, sort=150),
		@ExcelField(title="处理意见说明", attrName="handlingComments", align=Align.CENTER, sort=160),
		@ExcelField(title="是否纠正", attrName="corrected", align=Align.CENTER, sort=170),
	})
	public ShipOnSiteInspection() {
		this(null);
	}
	
	public ShipOnSiteInspection(String id){
		super(id);
	}
	
	@Size(min=0, max=50, message="船舶识别号长度不能超过 50 个字符")
	public String getShipId() {
		return shipId;
	}

	public void setShipId(String shipId) {
		this.shipId = shipId;
	}
	
	@Size(min=0, max=100, message="中文船名长度不能超过 100 个字符")
	public String getShipNameCn() {
		return shipNameCn;
	}

	public void setShipNameCn(String shipNameCn) {
		this.shipNameCn = shipNameCn;
	}
	
	@Size(min=0, max=100, message="专项检查名称长度不能超过 100 个字符")
	public String getInspectionName() {
		return inspectionName;
	}

	public void setInspectionName(String inspectionName) {
		this.inspectionName = inspectionName;
	}
	
	@Size(min=0, max=100, message="专项检查类型长度不能超过 100 个字符")
	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	
	@Size(min=0, max=10, message="初查/复查长度不能超过 10 个字符")
	public String getInitialOrRecheck() {
		return initialOrRecheck;
	}

	public void setInitialOrRecheck(String initialOrRecheck) {
		this.initialOrRecheck = initialOrRecheck;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="检查日期不能为空")
	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	
	@Size(min=0, max=100, message="检查地点长度不能超过 100 个字符")
	public String getInspectionLocation() {
		return inspectionLocation;
	}

	public void setInspectionLocation(String inspectionLocation) {
		this.inspectionLocation = inspectionLocation;
	}
	
	@Size(min=0, max=100, message="检查机构长度不能超过 100 个字符")
	public String getInspectionAgency() {
		return inspectionAgency;
	}

	public void setInspectionAgency(String inspectionAgency) {
		this.inspectionAgency = inspectionAgency;
	}
	
	@Size(min=0, max=100, message="检查员长度不能超过 100 个字符")
	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}
	
	@Size(min=0, max=100, message="发现问题或违章长度不能超过 100 个字符")
	public String getIssueFound() {
		return issueFound;
	}

	public void setIssueFound(String issueFound) {
		this.issueFound = issueFound;
	}
	
	@Size(min=0, max=50, message="检查内容代码长度不能超过 50 个字符")
	public String getInspectionContentCode() {
		return inspectionContentCode;
	}

	public void setInspectionContentCode(String inspectionContentCode) {
		this.inspectionContentCode = inspectionContentCode;
	}
	
	public String getInspectionContent() {
		return inspectionContent;
	}

	public void setInspectionContent(String inspectionContent) {
		this.inspectionContent = inspectionContent;
	}
	
	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}
	
	@Size(min=0, max=50, message="处理意见代码长度不能超过 50 个字符")
	public String getHandlingCode() {
		return handlingCode;
	}

	public void setHandlingCode(String handlingCode) {
		this.handlingCode = handlingCode;
	}
	
	public String getHandlingComments() {
		return handlingComments;
	}

	public void setHandlingComments(String handlingComments) {
		this.handlingComments = handlingComments;
	}
	
	@Size(min=0, max=10, message="是否纠正长度不能超过 10 个字符")
	public String getCorrected() {
		return corrected;
	}

	public void setCorrected(String corrected) {
		this.corrected = corrected;
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