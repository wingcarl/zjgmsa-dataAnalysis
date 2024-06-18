package com.jeesite.modules.data_collect.weeklydata.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.jeesite.modules.data_collect.datamanage.entity.DataSharingInventory;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 数据指标表Entity
 * @author 王浩宇
 * @version 2024-06-18
 */
@Table(name="data_metrics", alias="a", label="数据指标表信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="data_id", attrName="dataSharing.id", label="数据项ID"),
		@Column(name="department_id", attrName="departmentId.officeCode", label="部门ID"),
		@Column(name="start_time", attrName="startTime", label="开始时间"),
		@Column(name="end_time", attrName="endTime", label="结束时间"),
		@Column(name="current_value", attrName="currentValue", label="本期数值"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, joinTable={		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="departmentId", alias="u3",
			on="u3.office_code = a.department_id", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),@JoinTable(type=Type.LEFT_JOIN,entity = DataSharingInventory.class,attrName = "dataSharing",alias = "u4",
			on="u4.id=a.data_id",columns={
		@Column(name="id", label="数据项编码", isPK=true),
		@Column(name="data_category", label="数据大类", isQuery=false),
		@Column(name="business_segment", label="业务板块", isQuery=false),
		@Column(name="data_item_name", label="数据项目", isQuery=false),
		@Column(name="unit_of_measurement", label="技术单位", isQuery=false),
}),
	}, orderBy="a.update_date DESC"
)
public class DataMetrics extends DataEntity<DataMetrics> {
	
	private static final long serialVersionUID = 1L;
	private DataSharingInventory dataSharing;		// 数据项ID
	private Office departmentId;		// 部门ID
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private Double currentValue;		// 本期数值

	public DataMetrics() {
		this(null);
	}
	
	public DataMetrics(String id){
		super(id);
	}

	public DataSharingInventory getDataSharing() {
		return dataSharing;
	}

	public void setDataSharing(DataSharingInventory dataSharing) {
		this.dataSharing = dataSharing;
	}

	@NotNull(message="部门ID不能为空")
	public Office getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Office departmentId) {
		this.departmentId = departmentId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="开始时间不能为空")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="结束时间不能为空")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@NotNull(message="本期数值不能为空")
	public Double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}
	
	public Double getCurrentValue_gte() {
		return sqlMap.getWhere().getValue("current_value", QueryType.GTE);
	}

	public void setCurrentValue_gte(Double currentValue) {
		sqlMap.getWhere().and("current_value", QueryType.GTE, currentValue);
	}
	
	public Double getCurrentValue_lte() {
		return sqlMap.getWhere().getValue("current_value", QueryType.LTE);
	}

	public void setCurrentValue_lte(Double currentValue) {
		sqlMap.getWhere().and("current_value", QueryType.LTE, currentValue);
	}
	
}