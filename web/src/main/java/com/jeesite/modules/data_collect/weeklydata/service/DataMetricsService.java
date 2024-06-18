package com.jeesite.modules.data_collect.weeklydata.service;

import java.util.*;
import java.util.stream.Collectors;

import com.jeesite.modules.data_collect.datamanage.entity.DataSharingInventory;
import com.jeesite.modules.data_collect.datamanage.service.DataSharingInventoryService;
import com.jeesite.modules.data_collect.ship.entity.ShipInspection;
import com.jeesite.modules.data_collect.ship.service.ShipInspectionService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.weeklydata.entity.DataMetrics;
import com.jeesite.modules.data_collect.weeklydata.dao.DataMetricsDao;

/**
 * 数据指标表Service
 * @author 王浩宇
 * @version 2024-06-18
 */
@Service
public class DataMetricsService extends CrudService<DataMetricsDao, DataMetrics> {
	@Autowired
	ShipInspectionService shipInspectionService;
	@Autowired
	OfficeService officeService;
	@Autowired
	DataSharingInventoryService dataSharingInventoryService;
	/**
	 * 获取单条数据
	 * @param dataMetrics
	 * @return
	 */
	@Override
	public DataMetrics get(DataMetrics dataMetrics) {
		return super.get(dataMetrics);
	}
	
	/**
	 * 查询分页数据
	 * @param dataMetrics 查询条件
	 * @param dataMetrics page 分页对象
	 * @return
	 */
	@Override
	public Page<DataMetrics> findPage(DataMetrics dataMetrics) {
		return super.findPage(dataMetrics);
	}
	
	/**
	 * 查询列表数据
	 * 自行查询，按照周工作数据的方式显示。
	 * @param dataMetrics
	 * @return
	 */
	@Override
	public List<DataMetrics> findList(DataMetrics dataMetrics) {
		//查询FSC列表
		ShipInspection shipInspection = new ShipInspection();
		shipInspection.setInspectionDate_gte(dataMetrics.getStartTime());
		shipInspection.setInspectionDate_lte(dataMetrics.getEndTime());
		shipInspection.setInspectionType("初查");
		String[] agency = {"张家港海事局","港区海事处","锦丰海事处","保税区海事处（筹）"};
		shipInspection.setInspectionAgency_in(agency);
		List<ShipInspection> fscList = shipInspectionService.findDistinctList(shipInspection);
		List<DataMetrics> dataList = new ArrayList<>();
		// 使用Map统计每个inspectionAgency的数量
		Map<String, Integer> agencyCountMap = new HashMap<>();
		for(ShipInspection fsc : fscList){
			String agencyName = fsc.getInspectionAgency();
			if("张家港海事局".equals(agencyName) || "保税区海事处（筹）".equals(agencyName)){
				agencyCountMap.put("保税区海巡执法大队", agencyCountMap.getOrDefault("保税区海巡执法大队", 0) + 1);
			}else{
				agencyCountMap.put(agencyName, agencyCountMap.getOrDefault(agencyName, 0) + 1);

			}
		}
		for (Map.Entry<String, Integer> entry : agencyCountMap.entrySet()) {
			DataMetrics dataMetric = new DataMetrics();
			DataSharingInventory sharing = new DataSharingInventory();
			sharing.setDataItemName("FSC检查");
			DataSharingInventory dataSharing = dataSharingInventoryService.findList(sharing).get(0);
			dataMetric.setDataSharing(dataSharing);
			dataMetric.setCurrentValue((double)entry.getValue());
			Office o1 = new Office();
			o1.setOfficeName(entry.getKey());
			Office office = officeService.findList(o1).get(0);
			dataMetric.setDepartmentId(office);
			dataList.add(dataMetric);
		}
		//设置报表
		return dataList;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param dataMetrics
	 */
	@Override
	@Transactional
	public void save(DataMetrics dataMetrics) {
		super.save(dataMetrics);
	}
	
	/**
	 * 更新状态
	 * @param dataMetrics
	 */
	@Override
	@Transactional
	public void updateStatus(DataMetrics dataMetrics) {
		super.updateStatus(dataMetrics);
	}
	
	/**
	 * 删除数据
	 * @param dataMetrics
	 */
	@Override
	@Transactional
	public void delete(DataMetrics dataMetrics) {
		super.delete(dataMetrics);
	}
	
}