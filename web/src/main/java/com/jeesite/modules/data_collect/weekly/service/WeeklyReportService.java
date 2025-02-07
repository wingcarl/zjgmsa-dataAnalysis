package com.jeesite.modules.data_collect.weekly.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeesite.common.lang.DateUtils;
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
		java.util.Date startDate = DateUtils.asDate(currentStartLocalDate);
		java.util.Date endDate = DateUtils.asDate(currentStartLocalDate.plusDays(6));

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

			// 创建 WeeklyReport 对象
			WeeklyReport weeklyReport = new WeeklyReport();
			weeklyReport.setDepartmentId(office);
			weeklyReport.setReportDate(startDate);

			// 设置内河船舶数据
			weeklyReport.setRiverShipInspectionCount(riverResults.inspectionCount);
			weeklyReport.setRiverShipDetentionCount(riverResults.detentionCount);
			weeklyReport.setRiverShipDefectCount(riverResults.defectCount);

			// 设置海船数据
			weeklyReport.setSeaShipInspectionCount(seaResults.inspectionCount);
			weeklyReport.setSeaShipDetentionCount(seaResults.detentionCount);
			weeklyReport.setSeaShipDefectCount(seaResults.defectCount);

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

	// 内部类用于存储查询结果
	private static class ShipInspectionQueryResults {
		long inspectionCount;
		long detentionCount;
		long defectCount;
	}

	// 获取船舶检查数据
	private ShipInspectionQueryResults getShipInspectionData(String shipType, java.util.Date startDate, java.util.Date endDate, String agencyName) {
		// 准备查询条件
		ShipInspection query = new ShipInspection();
		query.setInspectionDate_gte(startDate);
		query.setInspectionDate_lte(endDate);
		query.setShipType(shipType);
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
}