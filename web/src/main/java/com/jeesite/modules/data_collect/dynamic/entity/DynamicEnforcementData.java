package com.jeesite.modules.data_collect.dynamic.entity;

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
 * 动态执法数据管理Entity
 * @author 王浩宇
 * @version 2025-02-07
 */
@Table(name="dynamic_enforcement_data", alias="a", label="动态执法数据信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="inspection_unit", attrName="inspectionUnit", label="检查单位"),
		@Column(name="inspection_time", attrName="inspectionTime", label="检查时间", isUpdateForce=true),
		@Column(name="inspection_location", attrName="inspectionLocation", label="检查地点", comment="检查地点(网格)"),
		@Column(name="inspection_target", attrName="inspectionTarget", label="检查对象"),
		@Column(name="cruise_task_name", attrName="cruiseTaskName", label="巡航任务名称", queryType=QueryType.LIKE),
		@Column(name="major_item_name", attrName="majorItemName", label="大项名称", queryType=QueryType.LIKE),
		@Column(name="ship_gross_tonnage", attrName="shipGrossTonnage", label="船舶总吨"),
		@Column(name="inspection_result", attrName="inspectionResult", label="检查结果"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
	}, orderBy="a.update_date DESC"
)
public class DynamicEnforcementData extends DataEntity<DynamicEnforcementData> {
	
	private static final long serialVersionUID = 1L;
	private String inspectionUnit;		// 检查单位
	private Date inspectionTime;		// 检查时间
	private String inspectionLocation;		// 检查地点(网格)
	private String inspectionTarget;		// 检查对象
	private String cruiseTaskName;		// 巡航任务名称
	private String majorItemName;		// 大项名称
	private String shipGrossTonnage;		// 船舶总吨
	private String inspectionResult;		// 检查结果

	@ExcelFields({
		@ExcelField(title="检查单位", attrName="inspectionUnit", align=Align.CENTER, sort=20),
		@ExcelField(title="检查时间", attrName="inspectionTime", align=Align.CENTER, sort=30, dataFormat="yyyy-MM-dd hh:mm"),
		@ExcelField(title="检查地点", attrName="inspectionLocation", align=Align.CENTER, sort=40),
		@ExcelField(title="检查对象", attrName="inspectionTarget", align=Align.CENTER, sort=50),
		@ExcelField(title="巡航任务名称", attrName="cruiseTaskName", align=Align.CENTER, sort=60),
		@ExcelField(title="大项名称", attrName="majorItemName", align=Align.CENTER, sort=70),
		@ExcelField(title="船舶总吨", attrName="shipGrossTonnage", align=Align.CENTER, sort=80),
		@ExcelField(title="检查结果", attrName="inspectionResult", align=Align.CENTER, sort=90),
	})
	public DynamicEnforcementData() {
		this(null);
	}
	
	public DynamicEnforcementData(String id){
		super(id);
	}
	
	@Size(min=0, max=255, message="检查单位长度不能超过 255 个字符")
	public String getInspectionUnit() {
		return inspectionUnit;
	}

	public void setInspectionUnit(String inspectionUnit) {
		this.inspectionUnit = inspectionUnit;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInspectionTime() {
		return inspectionTime;
	}

	public void setInspectionTime(Date inspectionTime) {
		this.inspectionTime = inspectionTime;
	}
	
	@Size(min=0, max=255, message="检查地点长度不能超过 255 个字符")
	public String getInspectionLocation() {
		return inspectionLocation;
	}

	public void setInspectionLocation(String inspectionLocation) {
		this.inspectionLocation = inspectionLocation;
	}
	
	@Size(min=0, max=255, message="检查对象长度不能超过 255 个字符")
	public String getInspectionTarget() {
		return inspectionTarget;
	}

	public void setInspectionTarget(String inspectionTarget) {
		this.inspectionTarget = inspectionTarget;
	}
	
	@Size(min=0, max=255, message="巡航任务名称长度不能超过 255 个字符")
	public String getCruiseTaskName() {
		return cruiseTaskName;
	}

	public void setCruiseTaskName(String cruiseTaskName) {
		this.cruiseTaskName = cruiseTaskName;
	}
	
	@Size(min=0, max=255, message="大项名称长度不能超过 255 个字符")
	public String getMajorItemName() {
		return majorItemName;
	}

	public void setMajorItemName(String majorItemName) {
		this.majorItemName = majorItemName;
	}
	
	@Size(min=0, max=255, message="船舶总吨长度不能超过 255 个字符")
	public String getShipGrossTonnage() {
		return shipGrossTonnage;
	}

	public void setShipGrossTonnage(String shipGrossTonnage) {
		this.shipGrossTonnage = shipGrossTonnage;
	}
	
	@Size(min=0, max=255, message="检查结果长度不能超过 255 个字符")
	public String getInspectionResult() {
		return inspectionResult;
	}

	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}
	
	public Date getInspectionTime_gte() {
		return sqlMap.getWhere().getValue("inspection_time", QueryType.GTE);
	}

	public void setInspectionTime_gte(Date inspectionTime) {
		sqlMap.getWhere().and("inspection_time", QueryType.GTE, inspectionTime);
	}
	
	public Date getInspectionTime_lte() {
		return sqlMap.getWhere().getValue("inspection_time", QueryType.LTE);
	}

	public void setInspectionTime_lte(Date inspectionTime) {
		sqlMap.getWhere().and("inspection_time", QueryType.LTE, inspectionTime);
	}
	
}