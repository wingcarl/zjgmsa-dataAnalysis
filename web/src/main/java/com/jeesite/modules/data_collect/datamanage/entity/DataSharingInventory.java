package com.jeesite.modules.data_collect.datamanage.entity;

import javax.validation.constraints.Size;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;

/**
 * 数据共享清单Entity
 * @author Dylan Wang
 * @version 2024-06-07
 */
@Table(name="data_sharing_inventory", alias="a", label="数据共享清单信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="management_department", attrName="managementDepartment", label="归口管理部门"),
		@Column(name="data_category", attrName="dataCategory", label="数据大类"),
		@Column(name="business_segment", attrName="businessSegment", label="业务板块"),
		@Column(name="serial_number", attrName="serialNumber", label="编号"),
		@Column(name="data_item_name", attrName="dataItemName", label="数据项目名称", queryType=QueryType.LIKE),
		@Column(name="unit_of_measurement", attrName="unitOfMeasurement", label="计数单位"),
		@Column(name="statistical_frequency", attrName="statisticalFrequency", label="统计频次"),
		@Column(name="data_granularity", attrName="dataGranularity", label="数据粒度"),
		@Column(name="data_collection_method", attrName="dataCollectionMethod", label="数据采集方式"),
		@Column(name="data_definition", attrName="dataDefinition", label="数据定义"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
	}, orderBy="a.update_date DESC"
)
public class DataSharingInventory extends DataEntity<DataSharingInventory> {
	
	private static final long serialVersionUID = 1L;
	private String managementDepartment;		// 归口管理部门
	private String dataCategory;		// 数据大类
	private String businessSegment;		// 业务板块
	private String serialNumber;		// 编号
	private String dataItemName;		// 数据项目名称
	private String unitOfMeasurement;		// 计数单位
	private String statisticalFrequency;		// 统计频次
	private String dataGranularity;		// 数据粒度
	private String dataCollectionMethod;		// 数据采集方式
	private String dataDefinition;		// 数据定义

	@ExcelFields({
		@ExcelField(title="归口管理部门", attrName="managementDepartment", align=Align.CENTER, sort=20),
		@ExcelField(title="数据大类", attrName="dataCategory", align=Align.CENTER, sort=30),
		@ExcelField(title="业务板块", attrName="businessSegment", align=Align.CENTER, sort=40),
		@ExcelField(title="编号", attrName="serialNumber", align=Align.CENTER, sort=50),
		@ExcelField(title="数据项目名称", attrName="dataItemName", align=Align.CENTER, sort=60),
		@ExcelField(title="计数单位", attrName="unitOfMeasurement", align=Align.CENTER, sort=70),
		@ExcelField(title="统计频次", attrName="statisticalFrequency", dictType="statistical_frequency", align=Align.CENTER, sort=80),
		@ExcelField(title="数据粒度", attrName="dataGranularity", dictType="data_granularity", align=Align.CENTER, sort=90),
		@ExcelField(title="数据采集方式", attrName="dataCollectionMethod", align=Align.CENTER, sort=100),
		@ExcelField(title="数据定义", attrName="dataDefinition", align=Align.CENTER, sort=110),
		@ExcelField(title="备注信息", attrName="remarks", align=Align.CENTER, sort=160),
	})
	public DataSharingInventory() {
		this(null);
	}
	
	public DataSharingInventory(String id){
		super(id);
	}
	
	@Size(min=0, max=64, message="归口管理部门长度不能超过 64 个字符")
	public String getManagementDepartment() {
		return managementDepartment;
	}

	public void setManagementDepartment(String managementDepartment) {
		this.managementDepartment = managementDepartment;
	}
	
	@Size(min=0, max=255, message="数据大类长度不能超过 255 个字符")
	public String getDataCategory() {
		return dataCategory;
	}

	public void setDataCategory(String dataCategory) {
		this.dataCategory = dataCategory;
	}
	
	@Size(min=0, max=255, message="业务板块长度不能超过 255 个字符")
	public String getBusinessSegment() {
		return businessSegment;
	}

	public void setBusinessSegment(String businessSegment) {
		this.businessSegment = businessSegment;
	}
	
	@Size(min=0, max=255, message="编号长度不能超过 255 个字符")
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	@Size(min=0, max=255, message="数据项目名称长度不能超过 255 个字符")
	public String getDataItemName() {
		return dataItemName;
	}

	public void setDataItemName(String dataItemName) {
		this.dataItemName = dataItemName;
	}
	
	@Size(min=0, max=30, message="计数单位长度不能超过 30 个字符")
	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}
	
	@Size(min=0, max=255, message="统计频次长度不能超过 255 个字符")
	public String getStatisticalFrequency() {
		return statisticalFrequency;
	}

	public void setStatisticalFrequency(String statisticalFrequency) {
		this.statisticalFrequency = statisticalFrequency;
	}
	
	@Size(min=0, max=255, message="数据粒度长度不能超过 255 个字符")
	public String getDataGranularity() {
		return dataGranularity;
	}

	public void setDataGranularity(String dataGranularity) {
		this.dataGranularity = dataGranularity;
	}
	
	@Size(min=0, max=255, message="数据采集方式长度不能超过 255 个字符")
	public String getDataCollectionMethod() {
		return dataCollectionMethod;
	}

	public void setDataCollectionMethod(String dataCollectionMethod) {
		this.dataCollectionMethod = dataCollectionMethod;
	}
	
	public String getDataDefinition() {
		return dataDefinition;
	}

	public void setDataDefinition(String dataDefinition) {
		this.dataDefinition = dataDefinition;
	}
	
}