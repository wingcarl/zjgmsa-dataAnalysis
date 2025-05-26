package com.jeesite.modules.data_collect.companyrisk.entity;

import javax.validation.constraints.NotBlank;
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
 * 安全隐患与风险排查记录表Entity
 * @author 王浩宇
 * @version 2025-05-26
 */
@Table(name="company_safety_inspection", alias="a", label="安全隐患与风险排查记录表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="company_name", attrName="companyName", label="单位名称", queryType=QueryType.LIKE),
		@Column(name="inspection_date", attrName="inspectionDate", label="数据记录日期", isUpdateForce=true),
		@Column(name="company_self_check_general_hazards", attrName="companySelfCheckGeneralHazards", label="企业自查一般隐患数量", isQuery=false, isUpdateForce=true),
		@Column(name="company_self_check_major_hazards", attrName="companySelfCheckMajorHazards", label="企业自查重大隐患数量", isQuery=false, isUpdateForce=true),
		@Column(name="company_self_check_risks", attrName="companySelfCheckRisks", label="企业自查风险数量", isQuery=false, isUpdateForce=true),
		@Column(name="maritime_inspection_general_hazards", attrName="maritimeInspectionGeneralHazards", label="海事排查一般隐患数量", isQuery=false, isUpdateForce=true),
		@Column(name="maritime_inspection_major_hazards", attrName="maritimeInspectionMajorHazards", label="海事排查重大隐患数量", isQuery=false, isUpdateForce=true),
		@Column(name="maritime_inspection_risks", attrName="maritimeInspectionRisks", label="海事排查风险数量", isQuery=false, isUpdateForce=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class CompanySafetyInspection extends DataEntity<CompanySafetyInspection> {
	
	private static final long serialVersionUID = 1L;
	private String companyName;		// 单位名称
	private Date inspectionDate;		// 数据记录日期
	private Long companySelfCheckGeneralHazards;		// 企业自查一般隐患数量
	private Long companySelfCheckMajorHazards;		// 企业自查重大隐患数量
	private Long companySelfCheckRisks;		// 企业自查风险数量
	private Long maritimeInspectionGeneralHazards;		// 海事排查一般隐患数量
	private Long maritimeInspectionMajorHazards;		// 海事排查重大隐患数量
	private Long maritimeInspectionRisks;		// 海事排查风险数量

	@ExcelFields({
		@ExcelField(title="单位名称", attrName="companyName", align=Align.CENTER, sort=20),
		@ExcelField(title="数据记录日期", attrName="inspectionDate", align=Align.CENTER, sort=30, dataFormat="yyyy-MM-dd"),
		@ExcelField(title="企业自查一般隐患数量", attrName="companySelfCheckGeneralHazards", align=Align.CENTER, sort=40),
		@ExcelField(title="企业自查重大隐患数量", attrName="companySelfCheckMajorHazards", align=Align.CENTER, sort=50),
		@ExcelField(title="企业自查风险数量", attrName="companySelfCheckRisks", align=Align.CENTER, sort=60),
		@ExcelField(title="海事排查一般隐患数量", attrName="maritimeInspectionGeneralHazards", align=Align.CENTER, sort=70),
		@ExcelField(title="海事排查重大隐患数量", attrName="maritimeInspectionMajorHazards", align=Align.CENTER, sort=80),
		@ExcelField(title="海事排查风险数量", attrName="maritimeInspectionRisks", align=Align.CENTER, sort=90),
	})
	public CompanySafetyInspection() {
		this(null);
	}
	
	public CompanySafetyInspection(String id){
		super(id);
	}
	
	@NotBlank(message="单位名称不能为空")
	@Size(min=0, max=255, message="单位名称长度不能超过 255 个字符")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	
	public Long getCompanySelfCheckGeneralHazards() {
		return companySelfCheckGeneralHazards;
	}

	public void setCompanySelfCheckGeneralHazards(Long companySelfCheckGeneralHazards) {
		this.companySelfCheckGeneralHazards = companySelfCheckGeneralHazards;
	}
	
	public Long getCompanySelfCheckMajorHazards() {
		return companySelfCheckMajorHazards;
	}

	public void setCompanySelfCheckMajorHazards(Long companySelfCheckMajorHazards) {
		this.companySelfCheckMajorHazards = companySelfCheckMajorHazards;
	}
	
	public Long getCompanySelfCheckRisks() {
		return companySelfCheckRisks;
	}

	public void setCompanySelfCheckRisks(Long companySelfCheckRisks) {
		this.companySelfCheckRisks = companySelfCheckRisks;
	}
	
	public Long getMaritimeInspectionGeneralHazards() {
		return maritimeInspectionGeneralHazards;
	}

	public void setMaritimeInspectionGeneralHazards(Long maritimeInspectionGeneralHazards) {
		this.maritimeInspectionGeneralHazards = maritimeInspectionGeneralHazards;
	}
	
	public Long getMaritimeInspectionMajorHazards() {
		return maritimeInspectionMajorHazards;
	}

	public void setMaritimeInspectionMajorHazards(Long maritimeInspectionMajorHazards) {
		this.maritimeInspectionMajorHazards = maritimeInspectionMajorHazards;
	}
	
	public Long getMaritimeInspectionRisks() {
		return maritimeInspectionRisks;
	}

	public void setMaritimeInspectionRisks(Long maritimeInspectionRisks) {
		this.maritimeInspectionRisks = maritimeInspectionRisks;
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