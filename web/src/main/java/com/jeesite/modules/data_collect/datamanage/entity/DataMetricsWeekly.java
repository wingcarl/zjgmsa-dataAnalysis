package com.jeesite.modules.data_collect.datamanage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 数据共享清单Entity
 * @author Dylan Wang
 * @version 2024-06-21
 */
@Table(name="data_metrics_weekly", alias="a", label="数据共享清单信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="data_id", attrName="dataSharingInventory.id", label="数据项ID"),
		@Column(name="department_id", attrName="office.officeCode", label="部门ID"),
		@Column(name="start_time", attrName="startTime", label="开始时间"),
		@Column(name="end_time", attrName="endTime", label="结束时间"),
		@Column(name="current_value", attrName="currentValue", label="本期数值"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, joinTable={		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="office", alias="u3",
			on="u3.office_code = a.department_id", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
	}, orderBy="a.create_date ASC"
)
public class DataMetricsWeekly extends DataEntity<DataMetricsWeekly> {
	
	private static final long serialVersionUID = 1L;
	private DataSharingInventory dataSharingInventory;		// 数据项ID 父类
	private Office office;		// 部门ID
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private Double currentValue;		// 本期数值

	public DataMetricsWeekly() {
		this(null);
	}

	public DataMetricsWeekly(DataSharingInventory dataSharingInventory){
		this.dataSharingInventory = dataSharingInventory;
	}
	
	public DataSharingInventory getDataSharingInventory() {
		return dataSharingInventory;
	}

	public void setDataSharingInventory(DataSharingInventory dataSharingInventory) {
		this.dataSharingInventory = dataSharingInventory;
	}
	
	@NotNull(message="部门ID不能为空")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
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
	
}