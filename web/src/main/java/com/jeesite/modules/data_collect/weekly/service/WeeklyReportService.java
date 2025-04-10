package com.jeesite.modules.data_collect.weekly.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.modules.data_collect.dynamic.entity.DynamicEnforcementData;
import com.jeesite.modules.data_collect.dynamic.service.DynamicEnforcementDataService;
import com.jeesite.modules.data_collect.psc.entity.PscInspection;
import com.jeesite.modules.data_collect.psc.service.PscInspectionService;
import com.jeesite.modules.data_collect.punish.entity.PunishJudge;
import com.jeesite.modules.data_collect.punish.service.PunishJudgeService;
import com.jeesite.modules.data_collect.ship.entity.ShipInspection;
import com.jeesite.modules.data_collect.ship.service.ShipInspectionService;
import com.jeesite.modules.data_collect.shiponsite.entity.ShipOnSiteInspection;
import com.jeesite.modules.data_collect.shiponsite.service.ShipOnSiteInspectionService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReport;
import com.jeesite.modules.data_collect.weekly.dao.WeeklyReportDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 周工作数据表Service
 * @author 王浩宇
 * @version 2024-06-22
 */
@Service
public class WeeklyReportService extends CrudService<WeeklyReportDao, WeeklyReport> {
	@Autowired
	ShipInspectionService shipInspectionService;
	@Autowired
	PscInspectionService pscInspectionService;
	@Autowired
	ShipOnSiteInspectionService onsiteInspectionService;
	@Autowired
	PunishJudgeService punishJudgeService;
	@Autowired
	DynamicEnforcementDataService dynamicEnforcementDataService;
	@Autowired
	OfficeService officeService;
	/**
	 * 获取单条数据
	 * @param weeklyReport
	 * @return
	 */
	@Override
	public WeeklyReport get(WeeklyReport weeklyReport) {
		return super.get(weeklyReport);
	}
	
	/**
	 * 查询分页数据
	 * @param weeklyReport 查询条件
	 * @param weeklyReport page 分页对象
	 * @return
	 */
	@Override
	public Page<WeeklyReport> findPage(WeeklyReport weeklyReport) {
		return super.findPage(weeklyReport);
	}
	
	/**
	 * 查询列表数据
	 * @param weeklyReport
	 * @return
	 */
	@Override
	public List<WeeklyReport> findList(WeeklyReport weeklyReport) {
		return super.findList(weeklyReport);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param weeklyReport
	 */
	@Override
	@Transactional
	public void save(WeeklyReport weeklyReport) {
		super.save(weeklyReport);
	}

	/**
	 * 导入数据
	 * @param file 导入的数据文件
	 */
	@Transactional
	public String importData(MultipartFile file) {
		if (file == null){
			throw new ServiceException(text("请选择导入的数据文件！"));
		}
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try(ExcelImport ei = new ExcelImport(file, 2, 0)){
			List<WeeklyReport> list = ei.getDataList(WeeklyReport.class);
			for (WeeklyReport weeklyReport : list) {
				try{
					ValidatorUtils.validateWithException(weeklyReport);
					this.save(weeklyReport);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + weeklyReport.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + weeklyReport.getId() + " 导入失败：";
					if (e instanceof ConstraintViolationException){
						ConstraintViolationException cve = (ConstraintViolationException)e;
						for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
							msg += Global.getText(violation.getMessage()) + " ("+violation.getPropertyPath()+")";
						}
					}else{
						msg += e.getMessage();
					}
					failureMsg.append(msg);
					logger.error(msg, e);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			failureMsg.append(e.getMessage());
			return failureMsg.toString();
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new ServiceException(failureMsg.toString());
		}else{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}
	
	/**
	 * 更新状态
	 * @param weeklyReport
	 */
	@Override
	@Transactional
	public void updateStatus(WeeklyReport weeklyReport) {
		super.updateStatus(weeklyReport);
	}
	
	/**
	 * 删除数据
	 * @param weeklyReport
	 */
	@Override
	@Transactional
	public void delete(WeeklyReport weeklyReport) {
		super.delete(weeklyReport);
	}
	public static Map<String,String> agencyMap = new HashMap<>() ;
	static {
		agencyMap.put("张家港海事局","SDJN");
		agencyMap.put("港区海事处","SDJN01");
		agencyMap.put("锦丰海事处","SDJN02");
		agencyMap.put("保税区海事处(筹)","SDJN03");
		//agencyMap.put("海巡执法支队","SDJN04");
	}
	public static Map<String,String> penatyAgencyMap = new HashMap<>() ;
	static {
		//penatyAgencyMap.put("张家港海事局","SDJN");
		penatyAgencyMap.put("张家港港区海事处","SDJN01");
		penatyAgencyMap.put("张家港锦丰海事处","SDJN02");
		penatyAgencyMap.put("张家港港区海事处保税区海巡执法大队","SDJN03");
		penatyAgencyMap.put("海巡执法支队","SDJN04");
	}
	public static Map<String,String> dynamicAgencyMap = new HashMap<>() ;
	static {
		dynamicAgencyMap.put("张家港海事局","SDJN");
		dynamicAgencyMap.put("西港区快反处置中心","SDJN01");
        dynamicAgencyMap.put("五号快反执法单元","SDJN01");
        dynamicAgencyMap.put("六号快反执法单元","SDJN01");

        dynamicAgencyMap.put("东港区快反处置中心","SDJN02");
        dynamicAgencyMap.put("一号快反执法单元","SDJN02");
        dynamicAgencyMap.put("二号快反执法单元","SDJN02");
        dynamicAgencyMap.put("三号快反执法单元","SDJN02");

        dynamicAgencyMap.put("保税区快反处置中心","SDJN03");
        dynamicAgencyMap.put("四号快反执法单元","SDJN03");

        dynamicAgencyMap.put("福姜沙快反处置中心","SDJN04");
        dynamicAgencyMap.put("七号快反执法单元","SDJN04");

    }
	// 查询是否存在相同部门和日期的 WeeklyReport 数据
	private WeeklyReport findExistingReport(java.util.Date startDate, java.util.Date endDate, Office office) {
		WeeklyReport query = new WeeklyReport();
		query.setReportDate_gte(startDate);
		query.setReportDate_lte(endDate);
		query.setDepartmentId(office);

		List<WeeklyReport> existingReports = findList(query); // 使用 dao 查询

		if (existingReports != null && !existingReports.isEmpty()) {
			return existingReports.get(0); // 返回第一个结果
		}

		return null; // 如果不存在，返回 null
	}

	@Transactional
	public void fetchData(LocalDate currentStartLocalDate) {
		// 查询日期范围
		WeeklyReport weeklyReport ;

		java.util.Date startDate = DateUtils.asDate(currentStartLocalDate);
		java.util.Date endDate = DateUtils.asDate(currentStartLocalDate.plusDays(6));
		Set<String> processedDynamicAgencies = new HashSet<>();
		for (Map.Entry<String, String> dynamicAgencyEntry : dynamicAgencyMap.entrySet()) {
			String agencyName = dynamicAgencyEntry.getKey();
			String agencyCode = dynamicAgencyEntry.getValue();

			// 查找部门
			Office office = officeService.get(agencyCode);
			if (office == null) {
				logger.warn("找不到部门，代码: {}, 名称: {}", agencyCode, agencyName);
				continue; // 如果找不到对应的部门，跳过保存操作
			}
			DynamicQueryResults dynamicResults= getDynamicData(startDate,endDate,agencyName);
			weeklyReport = new WeeklyReport();
			weeklyReport.setDepartmentId(office);
			weeklyReport.setReportDate(startDate);
			long patrolBoatAbnormalDiscovery;
			long patrolBoatInvestigationCases;
			long droneAbnormalDiscovery;
			long droneInvestigationCases;
			long electronicPatrolAbnormalDiscovery;
			long electronicPatrolInvestigationCases;
			long bulkLiquidHazardousCargoSupervision;
			long fuelQuickSamplingInspection;
			weeklyReport.setPatrolBoatAbnormalDiscovery(dynamicResults.patrolBoatAbnormalDiscovery);
			weeklyReport.setPatrolBoatInvestigationCases(dynamicResults.patrolBoatInvestigationCases);
			weeklyReport.setDroneAbnormalDiscovery(dynamicResults.droneAbnormalDiscovery);
			weeklyReport.setDroneInvestigationCases(dynamicResults.droneInvestigationCases);
			weeklyReport.setElectronicPatrolAbnormalDiscovery(dynamicResults.electronicPatrolAbnormalDiscovery);
			weeklyReport.setElectronicPatrolInvestigationCases(dynamicResults.electronicPatrolInvestigationCases);
			weeklyReport.setBulkLiquidHazardousCargoSupervision(dynamicResults.bulkLiquidHazardousCargoSupervision);
			weeklyReport.setFuelQuickSamplingInspection(dynamicResults.fuelQuickSamplingInspection);
			// 查询是否存在相同部门和日期的 WeeklyReport 数据
			WeeklyReport existingReport = findExistingReport(startDate, endDate, office);
			if (existingReport != null) {
				if (processedDynamicAgencies.contains(agencyCode)) {
					existingReport.setPatrolBoatAbnormalDiscovery(dynamicResults.patrolBoatAbnormalDiscovery+ (existingReport.getPatrolBoatAbnormalDiscovery()==null?0:existingReport.getPatrolBoatAbnormalDiscovery()));
					existingReport.setPatrolBoatInvestigationCases(dynamicResults.patrolBoatInvestigationCases+ (existingReport.getPatrolBoatInvestigationCases()==null?0:existingReport.getPatrolBoatInvestigationCases()));
					existingReport.setDroneAbnormalDiscovery(dynamicResults.droneAbnormalDiscovery+ (existingReport.getDroneAbnormalDiscovery()==null?0:existingReport.getDroneAbnormalDiscovery()));
					existingReport.setDroneInvestigationCases(dynamicResults.droneInvestigationCases+ (existingReport.getDroneInvestigationCases()==null?0:existingReport.getDroneInvestigationCases()));
					existingReport.setElectronicPatrolAbnormalDiscovery(dynamicResults.electronicPatrolAbnormalDiscovery+ (existingReport.getElectronicPatrolAbnormalDiscovery()==null?0:existingReport.getElectronicPatrolAbnormalDiscovery()));
					existingReport.setElectronicPatrolInvestigationCases(dynamicResults.electronicPatrolInvestigationCases+ (existingReport.getElectronicPatrolInvestigationCases()==null?0:existingReport.getElectronicPatrolInvestigationCases()));
					existingReport.setBulkLiquidHazardousCargoSupervision(dynamicResults.bulkLiquidHazardousCargoSupervision+ (existingReport.getBulkLiquidHazardousCargoSupervision()==null?0:existingReport.getBulkLiquidHazardousCargoSupervision()));
					existingReport.setFuelQuickSamplingInspection(dynamicResults.fuelQuickSamplingInspection+ (existingReport.getFuelQuickSamplingInspection()==null?0:existingReport.getFuelQuickSamplingInspection()));
					existingReport.preUpdate();
					update(existingReport);
				}else{
					existingReport.setPatrolBoatAbnormalDiscovery(dynamicResults.patrolBoatAbnormalDiscovery);
					existingReport.setPatrolBoatInvestigationCases(dynamicResults.patrolBoatInvestigationCases);
					existingReport.setDroneAbnormalDiscovery(dynamicResults.droneAbnormalDiscovery);
					existingReport.setDroneInvestigationCases(dynamicResults.droneInvestigationCases);
					existingReport.setElectronicPatrolAbnormalDiscovery(dynamicResults.electronicPatrolAbnormalDiscovery);
					existingReport.setElectronicPatrolInvestigationCases(dynamicResults.electronicPatrolInvestigationCases);
					existingReport.setBulkLiquidHazardousCargoSupervision(dynamicResults.bulkLiquidHazardousCargoSupervision);
					existingReport.setFuelQuickSamplingInspection(dynamicResults.fuelQuickSamplingInspection);
					existingReport.preUpdate();
					update(existingReport);
					processedDynamicAgencies.add(agencyCode);
				}

			}else{
				// 如果不存在，则保存数据
				// 设置创建者信息
				weeklyReport.preInsert();
				weeklyReport.setIsNewRecord(true);
				save(weeklyReport); // 调用保存方法
			}

			}
		for (Map.Entry<String, String> agencyEntry : penatyAgencyMap.entrySet()) {
			String agencyName = agencyEntry.getKey();
			String agencyCode = agencyEntry.getValue();
			// 查找部门
			Office office = officeService.get(agencyCode);
			if (office == null) {
				logger.warn("找不到部门，代码: {}, 名称: {}", agencyCode, agencyName);
				continue; // 如果找不到对应的部门，跳过保存操作
			}
			PenatyQueryResults penaltyResults = getPenatyData(startDate,endDate,agencyName);
			weeklyReport = new WeeklyReport();
			weeklyReport.setDepartmentId(office);
			weeklyReport.setReportDate(startDate);
			weeklyReport.setPenaltyDecisionCount(penaltyResults.penatyCount);
			weeklyReport.setPenaltyAmount(penaltyResults.penatyMoneyCount);
			weeklyReport.setReportIllegalCount(penaltyResults.penatyReportCount);
			weeklyReport.setWirelessIllegalCount(penaltyResults.penatyWirelessCount);
			weeklyReport.setPolluteIllegalCount(penaltyResults.penatyPolluteCount);
			// 查询是否存在相同部门和日期的 WeeklyReport 数据
			WeeklyReport existingReport = findExistingReport(startDate, endDate, office);

			if (existingReport != null) {
				// 如果存在，则更新数据
				existingReport.setPenaltyDecisionCount(penaltyResults.penatyCount);
				existingReport.setPenaltyAmount(penaltyResults.penatyMoneyCount);
				existingReport.setReportIllegalCount(penaltyResults.penatyReportCount);
				existingReport.setWirelessIllegalCount(penaltyResults.penatyWirelessCount);
				existingReport.setPolluteIllegalCount(penaltyResults.penatyPolluteCount);
				existingReport.preUpdate();
				update(existingReport); // 调用更新方法
			}else {
				// 如果不存在，则保存数据
				// 设置创建者信息
				weeklyReport.preInsert();
				weeklyReport.setIsNewRecord(true);
				save(weeklyReport); // 调用保存方法
			}
		}
		// 遍历不同的机构
		for (Map.Entry<String, String> agencyEntry : agencyMap.entrySet()) {
			String agencyName = agencyEntry.getKey();
			String agencyCode = agencyEntry.getValue();

			// 查找部门
			Office office = officeService.get(agencyCode);
			if (office == null) {
				logger.warn("找不到部门，代码: {}, 名称: {}", agencyCode, agencyName);
				continue; // 如果找不到对应的部门，跳过保存操作
			}

			// 获取内河船数据
			ShipInspectionQueryResults riverResults = getShipInspectionData("内河船", startDate, endDate, agencyName);
			// 获取海船数据
			ShipInspectionQueryResults seaResults = getShipInspectionData("海船", startDate, endDate, agencyName);
			//获取现场监督数据
			ShipInspectionQueryResults onsiteResults = getOnsiteInsepctionData(startDate,endDate,agencyName);
			weeklyReport = new WeeklyReport();
			// 创建 WeeklyReport 对象
			weeklyReport.setDepartmentId(office);
			weeklyReport.setReportDate(startDate);

			// 设置内河船舶数据
			weeklyReport.setRiverShipInspectionCount(riverResults.inspectionCount);
			weeklyReport.setRiverShipDetentionCount(riverResults.detentionCount);
			weeklyReport.setRiverShipDefectCount(riverResults.defectCount);
			ShipInspectionQueryResults pscResults = null;

			if("张家港海事局".equals(agencyName)){
				//获取PSC数据
				pscResults = getPscInsepctionData(startDate,endDate,agencyName);
				//设置PSC数据
				weeklyReport.setPscInspectionCount(pscResults.inspectionCount);
				weeklyReport.setPscDefectCount(pscResults.defectCount);
				weeklyReport.setPscDetentionCount(pscResults.detentionCount);
			}
			// 设置海船数据
			weeklyReport.setSeaShipInspectionCount(seaResults.inspectionCount);
			weeklyReport.setSeaShipDetentionCount(seaResults.detentionCount);
			weeklyReport.setSeaShipDefectCount(seaResults.defectCount);

			//设置现场监督数据
			weeklyReport.setOnSiteCount(onsiteResults.inspectionCount);
			weeklyReport.setOnSiteAbnormalCount(onsiteResults.detentionCount);
			// 查询是否存在相同部门和日期的 WeeklyReport 数据
			WeeklyReport existingReport = findExistingReport(startDate, endDate, office);

			if (existingReport != null) {
				// 如果存在，则更新数据
				existingReport.setRiverShipInspectionCount(riverResults.inspectionCount);
				existingReport.setRiverShipDetentionCount(riverResults.detentionCount);
				existingReport.setRiverShipDefectCount(riverResults.defectCount);
				existingReport.setSeaShipInspectionCount(seaResults.inspectionCount);
				existingReport.setSeaShipDetentionCount(seaResults.detentionCount);
				existingReport.setSeaShipDefectCount(seaResults.defectCount);
				existingReport.setPscDefectCount(pscResults==null?0:pscResults.defectCount);
				existingReport.setPscInspectionCount(pscResults==null?0: pscResults.inspectionCount);
				existingReport.setPscDetentionCount(pscResults==null?0:pscResults.detentionCount);
				existingReport.setOnSiteCount(onsiteResults.inspectionCount);
				existingReport.setOnSiteAbnormalCount(onsiteResults.detentionCount);
				// 设置更新者信息
				existingReport.preUpdate();
				update(existingReport); // 调用更新方法
			} else {
				// 如果不存在，则保存数据
				// 设置创建者信息
				weeklyReport.preInsert();
				weeklyReport.setIsNewRecord(true);
				save(weeklyReport); // 调用保存方法
			}
		}
	}

    private DynamicQueryResults getDynamicData(Date startDate, Date endDate, String agencyName) {
		DynamicEnforcementData query = new DynamicEnforcementData();
		query.setInspectionTime_gte(startDate);
		query.setInspectionTime_lte(endDate);
		query.setInspectionUnit(agencyName);

		List<DynamicEnforcementData> enforcementDataList = dynamicEnforcementDataService.findList(query);

		DynamicQueryResults results = new DynamicQueryResults();
		results.patrolBoatAbnormalDiscovery = 0L;
		results.patrolBoatInvestigationCases = 0L;
		results.droneAbnormalDiscovery = 0L;
		results.droneInvestigationCases = 0L;
		results.electronicPatrolAbnormalDiscovery = 0L;
		results.electronicPatrolInvestigationCases = 0L;
		results.bulkLiquidHazardousCargoSupervision = 0L;
		results.fuelQuickSamplingInspection = 0L;

		for (DynamicEnforcementData data : enforcementDataList) {
			String cruiseTask = data.getCruiseTaskName();
			String inspectionResult = data.getInspectionResult();
			String majorItem = data.getMajorItemName();

			// 判断是否为异常或立案调查
			boolean isAbnormalOrCase = "异常".equals(inspectionResult) || "立案调查".equals(inspectionResult);
			boolean isCase = "立案调查".equals(inspectionResult);

			// 海巡艇巡航
			if (cruiseTask != null && !cruiseTask.contains("电子巡航") && !cruiseTask.contains("无人机巡航")) {
				if (isAbnormalOrCase) {
					results.patrolBoatAbnormalDiscovery++;
				}
				if (isCase) {
					results.patrolBoatInvestigationCases++;
				}
			}

			// 无人机巡航
			if (cruiseTask != null && cruiseTask.contains("无人机巡航")) {
				if (isAbnormalOrCase) {
					results.droneAbnormalDiscovery++;
				}
				if (isCase) {
					results.droneInvestigationCases++;
				}
			}

			// 电子巡航
			if (cruiseTask != null && cruiseTask.contains("电子巡航")) {
				if (isAbnormalOrCase) {
					results.electronicPatrolAbnormalDiscovery++;
				}
				if (isCase) {
					results.electronicPatrolInvestigationCases++;
				}
			}

			// 散装液体危险货物作业现场监督检查
			if (majorItem != null && majorItem.equals("船载散装液体危险货物/污染危害性货物现场监督检查")) {
				results.bulkLiquidHazardousCargoSupervision++;
			}

			// 燃油快速抽样检测
			if (majorItem != null && majorItem.equals("船舶燃油质量监督检查")) {
				results.fuelQuickSamplingInspection++;
			}
		}

		return results;
    }
	private PenatyQueryResults getPenatyData(Date startDate, Date endDate, String agencyName) {
		PunishJudge query = new PunishJudge();
		query.setPenaltyDecisionTime_gte(startDate);
		query.setPenaltyDecisionTime_lte(endDate);
		query.setPenaltyAgency(agencyName);

		List<PunishJudge> penatyList = punishJudgeService.findList(query);

		PenatyQueryResults results = new PenatyQueryResults();
		results.penatyCount = penatyList.size();
		results.penatyMoneyCount = penatyList.stream().map(PunishJudge::getPenaltyAmount).mapToDouble(
				penaltyAmount-> {
						return penaltyAmount;
				}).sum();
		// 初始化计数器
		long reportCount = 0;
		long wirelessCount = 0;
		long polluteCount = 0;

		for (PunishJudge punishJudge : penatyList) {
			String caseReason = punishJudge.getCaseReason();
			String managementCategory = punishJudge.getManagementCategory();

			// 计算 penatyReportCount
			if ("船舶进出内河港口，未按照规定向海事管理机构报告船舶进出港信息的".equals(caseReason)) {
				reportCount++;
			}

			// 计算 penatyWirelessCount
			if (caseReason != null && (
					caseReason.contains("不按照无线电台执照规定的许可事项和要求设置、使用无线电台（站）") ||
							caseReason.contains("不按照规定在船舶自动识别设备中输入准确信息") ||
							caseReason.contains("未经许可擅自使用无线电频率，或者擅自设置、使用无线电台（站）的") ||
							caseReason.contains("其他违反海上无线电通信规则的行为") ||
							caseReason.contains("承担无线电通信任务的船员和岸基无线电台（站）的工作人员未保持海上交通安全通信频道的值守和畅通，或者使用海上交通安全通信频率交流与海上交通安全无关的内容") ||
							caseReason.contains("不按照规定保持船舶自动识别系统处于正常工作状态") ||
							caseReason.contains("未按照有关规定开启船舶自动识别、船舶航行数据记录、船舶远程识别与跟踪、通信等与航行安全、保安、船舶防污染相关的装置，并持续进行显示和记录"))) {
				wirelessCount++;
			}

			// 计算 penatyPolluteCount
			if ("危防管理".equals(managementCategory)) {
				polluteCount++;
			}
		}

		results.penatyReportCount = reportCount;
		results.penatyWirelessCount = wirelessCount;
		results.penatyPolluteCount = polluteCount;
		return results;
	}

	// 内部类用于存储查询结果
	private static class ShipInspectionQueryResults {
		long inspectionCount;
		long detentionCount;
		long defectCount;
	}
	private static class PenatyQueryResults {
		long penatyCount;
		double penatyMoneyCount;
		long penatyPointCount;
		long penatyReportCount;
		long penatyWirelessCount;
		long penatyPolluteCount;
	}

    private static class DynamicQueryResults {
		long patrolBoatAbnormalDiscovery;
		long patrolBoatInvestigationCases;
		long droneAbnormalDiscovery;
		long droneInvestigationCases;
		long electronicPatrolAbnormalDiscovery;
		long electronicPatrolInvestigationCases;
		long bulkLiquidHazardousCargoSupervision;
		long fuelQuickSamplingInspection;
    }
	private ShipInspectionQueryResults getOnsiteInsepctionData(Date startDate, Date endDate, String agencyName) {
		// 准备查询条件
		ShipOnSiteInspection query = new ShipOnSiteInspection();
		query.setInspectionDate_gte(startDate);
		query.setInspectionDate_lte(endDate);
		query.setInitialOrRecheck("初查");
		query.setInspectionAgency(agencyName);
		// 查询所有符合条件的检查记录
		List<ShipOnSiteInspection> inspections = onsiteInspectionService.findDistinctList(query);
		List<ShipOnSiteInspection> inspections1 = onsiteInspectionService.findList(query);

		// 统计总检查数
		long inspectionCount = inspections.size();

		// 统计滞留数
		long detentionCount = inspections.stream()
				.filter(inspection -> "是".equals(inspection.getIssueFound()))
				.count();
		ShipInspectionQueryResults results = new ShipInspectionQueryResults();
		results.inspectionCount = inspectionCount;
		results.detentionCount = detentionCount;
		return results;
	}

	private ShipInspectionQueryResults getPscInsepctionData(Date startDate, Date endDate, String agencyName) {
		PscInspection query = new PscInspection();
		query.setInspectionDate_gte(startDate);
		query.setInspectionDate_lte(endDate);
		query.setPort("Zhangjiagang");
		query.setType("INITIAL");

		List<PscInspection> pscList = pscInspectionService.findList(query);

		// 统计总检查数
		long inspectionCount = pscList.size();

		// 统计滞留数
		long detentionCount = pscList.stream()
				.filter(inspection -> "Y".equals(inspection.getDetention()))
				.count();

		// 统计缺陷总数
		long defectCount = pscList.stream()
				.map(PscInspection::getDeficiencies)
				.mapToLong(deficiencies -> {
					try {
						return Long.parseLong(deficiencies); // 使用 Long.parseLong() 转换
					} catch (NumberFormatException e) {
						// 处理转换异常
						logger.warn("无法将缺陷数量转换为 Long 类型: {}", deficiencies, e);
						return 0L; // 如果转换失败，返回 0
					}
				})
				.sum();

		// 封装结果
		ShipInspectionQueryResults results = new ShipInspectionQueryResults();
		results.inspectionCount = inspectionCount;
		results.detentionCount = detentionCount;
		results.defectCount = defectCount;
		return results;
	}



	// 获取船舶检查数据
	private ShipInspectionQueryResults getShipInspectionData(String shipType, java.util.Date startDate, java.util.Date endDate, String agencyName) {
		// 准备查询条件
		ShipInspection query = new ShipInspection();
		query.setInspectionDate_gte(startDate);
		query.setInspectionDate_lte(endDate);
		query.setShipType(shipType);
		query.setInspectionType("初查");
		query.setInspectionAgency(agencyName);

		// 查询所有符合条件的检查记录
		List<ShipInspection> inspections = shipInspectionService.findDistinctList(query);

		// 统计总检查数
		long inspectionCount = inspections.size();

		// 统计滞留数
		long detentionCount = inspections.stream()
				.filter(inspection -> "是".equals(inspection.getDetained()))
				.count();

		// 统计缺陷总数
		long defectCount = inspections.stream()
				.mapToLong(ShipInspection::getDefectCount)
				.sum();

		// 封装结果
		ShipInspectionQueryResults results = new ShipInspectionQueryResults();
		results.inspectionCount = inspectionCount;
		results.detentionCount = detentionCount;
		results.defectCount = defectCount;

		return results;
	}

	/**
	 * 每天凌晨 2 点执行两次数据拉取任务：
	 * - 一次是上周五 -> 本周四
	 * - 一次是上上周五 -> 上周四
	 */
	/**
	 * 定时执行任务：
	 * - 周五：00:00 ~ 17:00 每小时整点运行
	 * - 其他日子：每天凌晨2:00执行一次
	 */
	@Scheduled(cron = "0 0 0-19 * * ?")
	public void scheduledHourlyTask() {
		LocalDate today = LocalDate.now();
		DayOfWeek dayOfWeek = today.getDayOfWeek();

		if (dayOfWeek == DayOfWeek.FRIDAY) {
			logger.info("【周五每小时任务】当前时间：{}，执行 fetchData", LocalTime.now());
			runWeeklyFetch(today);
		}
	}

	@Scheduled(cron = "0 0 2 * * ?")
	public void scheduledDailyTask() {
		LocalDate today = LocalDate.now();
		DayOfWeek dayOfWeek = today.getDayOfWeek();

		if (dayOfWeek != DayOfWeek.FRIDAY) {
			logger.info("【非周五凌晨2点任务】当前时间：{}，执行 fetchData", LocalTime.now());
			runWeeklyFetch(today);
		}
	}

	/**
	 * 调用 fetchData 方法，分别处理两个周期：
	 * - 当前周期：上周五 ~ 本周四
	 * - 上一周期：上上周五 ~ 上周四
	 */
	private void runWeeklyFetch(LocalDate today) {
		// 找到“最近的周四”
		LocalDate currentThursday = today.with(DayOfWeek.THURSDAY);
		if (today.getDayOfWeek().getValue() < DayOfWeek.THURSDAY.getValue()) {
			currentThursday = currentThursday.minusWeeks(1);
		}

		LocalDate currentStartDate = currentThursday.minusDays(6); // 当前周期：上周五
		LocalDate previousStartDate = currentStartDate.minusWeeks(1); // 上一周期：上上周五

		logger.info("执行 fetchData：当前周期 {} ~ {}", currentStartDate, currentStartDate.plusDays(6));
		fetchData(currentStartDate);

		logger.info("执行 fetchData：上一周期 {} ~ {}", previousStartDate, previousStartDate.plusDays(6));
		fetchData(previousStartDate);
	}
}