package com.jeesite.modules.data_collect.dynamic.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.data_collect.weekly.entity.WeeklyReport;
import com.jeesite.modules.data_collect.weekly.service.WeeklyReportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import org.springframework.web.multipart.MultipartFile;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.data_collect.dynamic.entity.DynamicEnforcementData;
import com.jeesite.modules.data_collect.dynamic.service.DynamicEnforcementDataService;

/**
 * 动态执法数据管理Controller
 * @author 王浩宇
 * @version 2025-02-07
 */
@Controller
@RequestMapping(value = "${adminPath}/dynamic/dynamicEnforcementData")
public class DynamicEnforcementDataController extends BaseController {

	@Autowired
	private DynamicEnforcementDataService dynamicEnforcementDataService;
	@Autowired
	private WeeklyReportService weeklyReportService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DynamicEnforcementData get(String id, boolean isNewRecord) {
		return dynamicEnforcementDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("dynamic:dynamicEnforcementData:view")
	@RequestMapping(value = {"list", ""})
	public String list(DynamicEnforcementData dynamicEnforcementData, Model model) {
		model.addAttribute("dynamicEnforcementData", dynamicEnforcementData);
		return "data_collect/dynamic/dynamicEnforcementDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("dynamic:dynamicEnforcementData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DynamicEnforcementData> listData(DynamicEnforcementData dynamicEnforcementData, HttpServletRequest request, HttpServletResponse response) {
		dynamicEnforcementData.setPage(new Page<>(request, response));
		Page<DynamicEnforcementData> page = dynamicEnforcementDataService.findPage(dynamicEnforcementData);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("dynamic:dynamicEnforcementData:view")
	@RequestMapping(value = "form")
	public String form(DynamicEnforcementData dynamicEnforcementData, Model model) {
		model.addAttribute("dynamicEnforcementData", dynamicEnforcementData);
		return "data_collect/dynamic/dynamicEnforcementDataForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("dynamic:dynamicEnforcementData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DynamicEnforcementData dynamicEnforcementData) {
		dynamicEnforcementDataService.save(dynamicEnforcementData);
		return renderResult(Global.TRUE, text("保存动态执法数据成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("dynamic:dynamicEnforcementData:view")
	@RequestMapping(value = "exportData")
	public void exportData(DynamicEnforcementData dynamicEnforcementData, HttpServletResponse response) {
		List<DynamicEnforcementData> list = dynamicEnforcementDataService.findList(dynamicEnforcementData);
		String fileName = "动态执法数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("动态执法数据", DynamicEnforcementData.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("dynamic:dynamicEnforcementData:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		DynamicEnforcementData dynamicEnforcementData = new DynamicEnforcementData();
		List<DynamicEnforcementData> list = ListUtils.newArrayList(dynamicEnforcementData);
		String fileName = "动态执法数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("动态执法数据", DynamicEnforcementData.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("dynamic:dynamicEnforcementData:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String importType) {
		try {
			String message = dynamicEnforcementDataService.importData(file, importType);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	/**
	 * 删除数据
	 */
	@RequiresPermissions("dynamic:dynamicEnforcementData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DynamicEnforcementData dynamicEnforcementData) {
		dynamicEnforcementDataService.delete(dynamicEnforcementData);
		return renderResult(Global.TRUE, text("删除动态执法数据成功！"));
	}
	@RequestMapping(value = "chart")
	public String chart(){
		return "data_collect/dynamic/dynamicChart";
	}
	@GetMapping("chartDataWithDate")
	@ResponseBody
	public Map<String, Object> getChartDataWithDate(String currentWeekStartDate, String lastWeekStartDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);

		// 查询本周的数据
		WeeklyReport weeklyReportCurrent = new WeeklyReport();
		LocalDate currentStartLocalDate = LocalDate.parse(currentWeekStartDate, formatter);
		weeklyReportCurrent.setReportDate_gte(DateUtils.asDate(currentStartLocalDate));
		weeklyReportCurrent.setReportDate_lte(DateUtils.asDate(currentStartLocalDate.plusDays(6)));
		List<WeeklyReport> currentWeekReports = weeklyReportService.findList(weeklyReportCurrent);

		// 查询上周的数据
		WeeklyReport weeklyReportLast = new WeeklyReport();
		LocalDate lastStartLocalDate = LocalDate.parse(lastWeekStartDate, formatter);
		weeklyReportLast.setReportDate_gte(DateUtils.asDate(lastStartLocalDate));
		weeklyReportLast.setReportDate_lte(DateUtils.asDate(lastStartLocalDate.plusDays(6)));
		List<WeeklyReport> lastWeekReports = weeklyReportService.findList(weeklyReportLast);

		// 将数据分组
		Map<String, Long> currentPatrolBoatAbnormalDiscovery = new HashMap<>();
		Map<String, Long> currentPatrolBoatInvestigationCases = new HashMap<>();
		Map<String, Long> currentDroneAbnormalDiscovery = new HashMap<>();
		Map<String, Long> currentDroneInvestigationCases = new HashMap<>();
		Map<String, Long> currentElectronicPatrolAbnormalDiscovery = new HashMap<>();
		Map<String, Long> currentElectronicPatrolInvestigationCases = new HashMap<>();

		Map<String, Long> lastPatrolBoatAbnormalDiscovery = new HashMap<>();
		Map<String, Long> lastPatrolBoatInvestigationCases = new HashMap<>();
		Map<String, Long> lastDroneAbnormalDiscovery = new HashMap<>();
		Map<String, Long> lastDroneInvestigationCases = new HashMap<>();
		Map<String, Long> lastElectronicPatrolAbnormalDiscovery = new HashMap<>();
		Map<String, Long> lastElectronicPatrolInvestigationCases = new HashMap<>();


		List<String> categories = new ArrayList<>();

		processData(currentWeekReports, currentPatrolBoatAbnormalDiscovery, currentPatrolBoatInvestigationCases,
				currentDroneAbnormalDiscovery, currentDroneInvestigationCases, currentElectronicPatrolAbnormalDiscovery,
				currentElectronicPatrolInvestigationCases, categories);
		processData(lastWeekReports, lastPatrolBoatAbnormalDiscovery, lastPatrolBoatInvestigationCases,
				lastDroneAbnormalDiscovery, lastDroneInvestigationCases, lastElectronicPatrolAbnormalDiscovery,
				lastElectronicPatrolInvestigationCases, categories);

		// 处理图表数据
		Map<String, Object> result = new HashMap<>();
		result.put("categories", new ArrayList<>(new HashSet<>(categories)));

		addChartData(result, "patrolBoatAbnormalDiscovery", currentPatrolBoatAbnormalDiscovery, lastPatrolBoatAbnormalDiscovery, categories);
		addChartData(result, "patrolBoatInvestigationCases", currentPatrolBoatInvestigationCases, lastPatrolBoatInvestigationCases, categories);
		addChartData(result, "droneAbnormalDiscovery", currentDroneAbnormalDiscovery, lastDroneAbnormalDiscovery, categories);
		addChartData(result, "droneInvestigationCases", currentDroneInvestigationCases, lastDroneInvestigationCases, categories);
		addChartData(result, "electronicPatrolAbnormalDiscovery", currentElectronicPatrolAbnormalDiscovery, lastElectronicPatrolAbnormalDiscovery, categories);
		addChartData(result, "electronicPatrolInvestigationCases", currentElectronicPatrolInvestigationCases, lastElectronicPatrolInvestigationCases, categories);

		return result;
	}

	private void processData(List<WeeklyReport> reports, Map<String, Long> patrolBoatAbnormalDiscovery,
							 Map<String, Long> patrolBoatInvestigationCases, Map<String, Long> droneAbnormalDiscovery,
							 Map<String, Long> droneInvestigationCases, Map<String, Long> electronicPatrolAbnormalDiscovery,
							 Map<String, Long> electronicPatrolInvestigationCases, List<String> categories) {
		for (WeeklyReport report : reports) {
			String departmentName = report.getDepartmentId().getOfficeName();
			categories.add(departmentName);

			patrolBoatAbnormalDiscovery.put(departmentName, patrolBoatAbnormalDiscovery.getOrDefault(departmentName, 0L) +
					(report.getPatrolBoatAbnormalDiscovery() == null ? 0 : report.getPatrolBoatAbnormalDiscovery()));
			patrolBoatInvestigationCases.put(departmentName, patrolBoatInvestigationCases.getOrDefault(departmentName, 0L) +
					(report.getPatrolBoatInvestigationCases() == null ? 0 : report.getPatrolBoatInvestigationCases()));
			droneAbnormalDiscovery.put(departmentName, droneAbnormalDiscovery.getOrDefault(departmentName, 0L) +
					(report.getDroneAbnormalDiscovery() == null ? 0 : report.getDroneAbnormalDiscovery()));
			droneInvestigationCases.put(departmentName, droneInvestigationCases.getOrDefault(departmentName, 0L) +
					(report.getDroneInvestigationCases() == null ? 0 : report.getDroneInvestigationCases()));
			electronicPatrolAbnormalDiscovery.put(departmentName, electronicPatrolAbnormalDiscovery.getOrDefault(departmentName, 0L) +
					(report.getElectronicPatrolAbnormalDiscovery() == null ? 0 : report.getElectronicPatrolAbnormalDiscovery()));
			electronicPatrolInvestigationCases.put(departmentName, electronicPatrolInvestigationCases.getOrDefault(departmentName, 0L) +
					(report.getElectronicPatrolInvestigationCases() == null ? 0 : report.getElectronicPatrolInvestigationCases()));
		}
	}

	private void addChartData(Map<String, Object> result, String baseName, Map<String, Long> currentData,
							  Map<String, Long> lastData, List<String> categories) {
		List<Object> currentList = new ArrayList<>();
		List<Object> lastList = new ArrayList<>();

		Set<String> allCats = new HashSet<>(categories);
		for (String cat : allCats) {
			Long currentVal = currentData.getOrDefault(cat, 0L);
			Long lastVal = lastData.getOrDefault(cat, 0L);

			double changeRate = 0;
			if (lastVal != 0) {
				changeRate = (double) Math.round(((double) (currentVal - lastVal) / lastVal) * 10000) / 100;
			}

			final double finalChangeRate = changeRate;
			final Long finalCurrentVal = currentVal;
			currentList.add(new HashMap<String, Object>() {{
				put("value", finalCurrentVal);
				put("changeRate", finalChangeRate);
			}});
			lastList.add(lastVal);
		}

		result.put(baseName + "Current", currentList);
		result.put(baseName + "Last", lastList);
	}
	
}