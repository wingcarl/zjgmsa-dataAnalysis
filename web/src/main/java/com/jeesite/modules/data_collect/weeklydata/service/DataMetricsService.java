package com.jeesite.modules.data_collect.weeklydata.service;

import java.util.*;
import java.util.stream.Collectors;

import com.jeesite.modules.data_collect.datamanage.entity.DataSharingInventory;
import com.jeesite.modules.data_collect.datamanage.service.DataSharingInventoryService;
import com.jeesite.modules.data_collect.psc.entity.PscInspection;
import com.jeesite.modules.data_collect.psc.service.PscInspectionService;
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
	PscInspectionService pscInspectionService;
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
		shipInspection.setShipType("海船");
		String[] agency = {"张家港海事局","港区海事处","锦丰海事处","保税区海事处（筹）"};
		shipInspection.setInspectionAgency_in(agency);
		List<DataMetrics> dataList = findDataMetricsList(shipInspection);
		shipInspection.setShipType("内河船");
		dataList.addAll(findDataMetricsList(shipInspection));
		//查询PSC列表
		PscInspection pscInspection = new PscInspection();
		pscInspection.setInspectionDate_gte(dataMetrics.getStartTime());
		pscInspection.setInspectionDate_lte(dataMetrics.getEndTime());
		pscInspection.setType("INITIAL");
		pscInspection.setPort("Zhangjiagang");
		dataList.addAll(findPscDataMetrics(pscInspection));
		//设置报表
		return dataList;
	}
	private List<DataMetrics> findPscDataMetrics(PscInspection pscInspection){
		List<PscInspection> pscList = pscInspectionService.findList(pscInspection);
		List<DataMetrics> dataList;
		// 使用Map统计每个inspectionAgency的数量
		Map<String, Integer> agencyCountMap = new HashMap<>();
		Map<String, Integer> detainedCountMap = new HashMap<>();
		Map<String, Integer> defectCountMap = new HashMap<>();
		for(PscInspection psc : pscList) {
			String portName = psc.getPort();
			String detained = psc.getDetention();
			int deficienciesNum = Integer.parseInt(psc.getDeficiencies());
			agencyCountMap.put("张家港海事局", agencyCountMap.getOrDefault("张家港海事局", 0) + 1);
			if("Y".equals(detained)){
				detainedCountMap.put("张家港海事局",detainedCountMap.getOrDefault("张家港海事局",0) +1);
			}
			defectCountMap.put("张家港海事局", defectCountMap.getOrDefault("张家港海事局", 0) + deficienciesNum);
		}
		dataList = loadDataMetrics(agencyCountMap,"PSC检查");
		dataList.addAll(loadDataMetrics(detainedCountMap, "PSC滞留"));
		dataList.addAll(loadDataMetrics(defectCountMap,"PSC缺陷数量"));
		return dataList;
	}
	private List<DataMetrics> findDataMetricsList(ShipInspection shipInspection){
		List<ShipInspection> fscList = shipInspectionService.findDistinctList(shipInspection);
		List<DataMetrics> dataList;
		// 使用Map统计每个inspectionAgency的数量
		Map<String, Integer> agencyCountMap = new HashMap<>();
		Map<String, Integer> detainedCountMap = new HashMap<>();
		Map<String, Integer> defectCountMap = new HashMap<>();
		for(ShipInspection fsc : fscList){
			String agencyName = fsc.getInspectionAgency();
			String detained = fsc.getDetained();
			int defectCount = Math.toIntExact(fsc.getDefectCount());
			if("张家港海事局".equals(agencyName) || "保税区海事处（筹）".equals(agencyName)){
				agencyCountMap.put("保税区海巡执法大队", agencyCountMap.getOrDefault("保税区海巡执法大队", 0) + 1);
				if("是".equals(detained)){
					detainedCountMap.put("保税区海巡执法大队", detainedCountMap.getOrDefault("保税区海巡执法大队", 0) + 1);
				}
				defectCountMap.put("保税区海巡执法大队", defectCountMap.getOrDefault("保税区海巡执法大队", 0) + defectCount);

			}else{
				agencyCountMap.put(agencyName, agencyCountMap.getOrDefault(agencyName, 0) + 1);
				if("是".equals(detained)){
					detainedCountMap.put(agencyName, detainedCountMap.getOrDefault(agencyName, 0) + 1);
				}
				defectCountMap.put(agencyName, defectCountMap.getOrDefault(agencyName, 0) + defectCount);

			}
		}
		dataList = loadDataMetrics(agencyCountMap,shipInspection.getShipType()+"FSC检查");
		dataList.addAll(loadDataMetrics(detainedCountMap, shipInspection.getShipType()+"FSC滞留"));
		dataList.addAll(loadDataMetrics(defectCountMap,shipInspection.getShipType()+"FSC缺陷数量"));
		//设置报表
		return dataList;
	}
	private List<DataMetrics> loadDataMetrics(Map<String, Integer> agencyCountMap,String dataItemName) {
		List<DataMetrics> dataList = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : agencyCountMap.entrySet()) {
			DataMetrics dataMetric = new DataMetrics();
			DataSharingInventory sharing = new DataSharingInventory();
			sharing.setDataItemName(dataItemName);
			DataSharingInventory dataSharing = dataSharingInventoryService.findList(sharing).get(0);
			dataMetric.setDataSharing(dataSharing);
			dataMetric.setCurrentValue((double) entry.getValue());
			Office o1 = new Office();
			o1.setOfficeName(entry.getKey());
			Office office = officeService.findList(o1).get(0);
			dataMetric.setDepartmentId(office);
			dataList.add(dataMetric);
		}
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