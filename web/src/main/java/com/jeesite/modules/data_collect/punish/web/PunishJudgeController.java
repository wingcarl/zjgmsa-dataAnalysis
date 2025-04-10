package com.jeesite.modules.data_collect.punish.web;

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
import com.jeesite.modules.data_collect.punish.entity.PunishJudge;
import com.jeesite.modules.data_collect.punish.service.PunishJudgeService;

/**
 * 处罚决定Controller
 * @author 王浩宇
 * @version 2025-02-07
 */
@Controller
@RequestMapping(value = "${adminPath}/punish/punishJudge")
public class PunishJudgeController extends BaseController {

	@Autowired
	private PunishJudgeService punishJudgeService;
	@Autowired
	private WeeklyReportService weeklyReportService;  // Changed autowired service

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public PunishJudge get(String id, boolean isNewRecord) {
		return punishJudgeService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = {"list", ""})
	public String list(PunishJudge punishJudge, Model model) {
		model.addAttribute("punishJudge", punishJudge);
		return "data_collect/punish/punishJudgeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<PunishJudge> listData(PunishJudge punishJudge, HttpServletRequest request, HttpServletResponse response) {
		punishJudge.setPage(new Page<>(request, response));
		Page<PunishJudge> page = punishJudgeService.findPage(punishJudge);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = "form")
	public String form(PunishJudge punishJudge, Model model) {
		model.addAttribute("punishJudge", punishJudge);
		return "data_collect/punish/punishJudgeForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("punish:punishJudge:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated PunishJudge punishJudge) {
		punishJudgeService.save(punishJudge);
		return renderResult(Global.TRUE, text("保存处罚决定成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = "exportData")
	public void exportData(PunishJudge punishJudge, HttpServletResponse response) {
		List<PunishJudge> list = punishJudgeService.findList(punishJudge);
		String fileName = "处罚决定" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("处罚决定", PunishJudge.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		PunishJudge punishJudge = new PunishJudge();
		List<PunishJudge> list = ListUtils.newArrayList(punishJudge);
		String fileName = "处罚决定模板.xlsx";
		try(ExcelExport ee = new ExcelExport("处罚决定", PunishJudge.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("punish:punishJudge:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = punishJudgeService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("punish:punishJudge:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(PunishJudge punishJudge) {
		punishJudgeService.delete(punishJudge);
		return renderResult(Global.TRUE, text("删除处罚决定成功！"));
	}

	@RequestMapping(value = "chart")
	public String chart(){
		return "data_collect/weekly/weeklyReportPenatyChart";
	}

	@GetMapping("weeklyChartDataWithDate")
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

		// 首先获取所有部门列表，确保所有数据使用相同的顺序
		List<String> categories = getAllDepartments(currentWeekReports, lastWeekReports);

		// 获取图表数据 - 使用统一的部门列表
		Map<String, Object> chartData = getPunishAgencyChartData(categories, currentWeekReports, lastWeekReports);

		// 获取指标板数据
		Map<String, Object> indicatorData = getIndicatorData(currentWeekReports, lastWeekReports);
		chartData.put("indicatorData", indicatorData); // 将指标板数据添加到 chartData

		// 获取各部门处罚金额数据 - 使用统一的部门列表
		Map<String, Object> penaltyAmountByAgency = getPenaltyAmountByAgency(categories, currentWeekReports, lastWeekReports);
		chartData.put("penaltyAmountByAgency", penaltyAmountByAgency);

		// 获取进出港、无线电、防污染处罚数量图表数据 - 使用统一的部门列表
		Map<String, Object> reportIllegalChartData = getReportIllegalChartData(categories, currentWeekReports, lastWeekReports);
		chartData.put("reportIllegalCounts", reportIllegalChartData);
		Map<String, Object> wirelessIllegalChartData = getWirelessIllegalChartData(categories, currentWeekReports, lastWeekReports);
		chartData.put("wirelessIllegalCounts", wirelessIllegalChartData);
		Map<String, Object> polluteIllegalChartData = getPolluteIllegalChartData(categories, currentWeekReports, lastWeekReports);
		chartData.put("polluteIllegalCounts", polluteIllegalChartData);

		return chartData;
	}

	/**
	 * 获取所有部门列表，确保所有数据使用统一的部门顺序
	 */
	private List<String> getAllDepartments(List<WeeklyReport> currentWeekReports, List<WeeklyReport> lastWeekReports) {
		Set<String> departmentSet = new LinkedHashSet<>(); // 使用LinkedHashSet保持插入顺序

		// 收集所有部门名称
		for (WeeklyReport report : currentWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				departmentSet.add(report.getDepartmentId().getOfficeName());
			}
		}

		for (WeeklyReport report : lastWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				departmentSet.add(report.getDepartmentId().getOfficeName());
			}
		}

		return new ArrayList<>(departmentSet);
	}

	/**
	 * 获取各部门处罚金额数据 - 修改后使用统一的部门列表
	 */
	private Map<String, Object> getPenaltyAmountByAgency(List<String> categories, List<WeeklyReport> currentWeekReports, List<WeeklyReport> lastWeekReports) {
		Map<String, Object> result = new HashMap<>();

		// 收集本周各部门处罚金额
		Map<String, Double> currentAgencyAmounts = new HashMap<>();
		for (WeeklyReport report : currentWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				currentAgencyAmounts.put(agency, currentAgencyAmounts.getOrDefault(agency, 0.0) +
						(report.getPenaltyAmount() == null ? 0.0 : report.getPenaltyAmount()));
			}
		}

		// 收集上周各部门处罚金额
		Map<String, Double> lastAgencyAmounts = new HashMap<>();
		for (WeeklyReport report : lastWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				lastAgencyAmounts.put(agency, lastAgencyAmounts.getOrDefault(agency, 0.0) +
						(report.getPenaltyAmount() == null ? 0.0 : report.getPenaltyAmount()));
			}
		}

		// 基于统一的部门列表构建数据
		List<Double> lastAmounts = new ArrayList<>();
		List<Map<String, Object>> currentAmounts = new ArrayList<>();

		for (String agency : categories) {
			double lastAmount = lastAgencyAmounts.getOrDefault(agency, 0.0);
			double currentAmount = currentAgencyAmounts.getOrDefault(agency, 0.0);

			// 计算环比变化率
			double changeRate = calculateChangeRate(currentAmount, lastAmount);

			// 构建本周数据对象（包含值和环比）
			Map<String, Object> currentData = new HashMap<>();
			currentData.put("value", currentAmount);
			currentData.put("changeRate", changeRate);

			lastAmounts.add(lastAmount);
			currentAmounts.add(currentData);
		}

		result.put("last", lastAmounts);
		result.put("current", currentAmounts);

		return result;
	}

	/**
	 * 获取处罚机构图表数据 - 修改后使用统一的部门列表
	 */
	private Map<String, Object> getPunishAgencyChartData(List<String> categories, List<WeeklyReport> currentWeekReports, List<WeeklyReport> lastWeekReports) {
		Map<String, Object> chartData = new HashMap<>();

		// 收集本周各部门处罚数量
		Map<String, Long> currentAgencyCounts = new HashMap<>();
		for (WeeklyReport report : currentWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				currentAgencyCounts.put(agency, currentAgencyCounts.getOrDefault(agency, 0L) +
						(report.getPenaltyDecisionCount() == null ? 0 : report.getPenaltyDecisionCount()));
			}
		}

		// 收集上周各部门处罚数量
		Map<String, Long> lastAgencyCounts = new HashMap<>();
		for (WeeklyReport report : lastWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				lastAgencyCounts.put(agency, lastAgencyCounts.getOrDefault(agency, 0L) +
						(report.getPenaltyDecisionCount() == null ? 0 : report.getPenaltyDecisionCount()));
			}
		}

		// 基于统一的部门列表构建数据
		List<Long> currentCounts = new ArrayList<>();
		List<Long> lastCounts = new ArrayList<>();

		for (String agency : categories) {
			lastCounts.add(lastAgencyCounts.getOrDefault(agency, 0L));

			long currentCount = currentAgencyCounts.getOrDefault(agency, 0L);
			long lastCount = lastAgencyCounts.getOrDefault(agency, 0L);
			double changeRate = calculateChangeRate(currentCount, lastCount);

			Map<String, Object> currentData = new HashMap<>();
			currentData.put("value", currentCount);
			currentData.put("changeRate", changeRate);

			currentCounts.add(currentCount);
		}

		// 添加数据到chartData
		chartData.put("categories", categories);
		Map<String, Object> penaltyAgencyCounts = new HashMap<>();
		penaltyAgencyCounts.put("current", currentCounts);
		penaltyAgencyCounts.put("last", lastCounts);
		chartData.put("penaltyAgencyCounts", penaltyAgencyCounts);

		return chartData;
	}

	/**
	 * 获取进出港处罚数据 - 修改后使用统一的部门列表
	 */
	private Map<String, Object> getReportIllegalChartData(List<String> categories, List<WeeklyReport> currentWeekReports, List<WeeklyReport> lastWeekReports) {
		Map<String, Object> chartData = new HashMap<>();

		// 收集本周各部门进出港处罚数量
		Map<String, Long> currentAgencyCounts = new HashMap<>();
		for (WeeklyReport report : currentWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				currentAgencyCounts.put(agency, currentAgencyCounts.getOrDefault(agency, 0L) +
						(report.getReportIllegalCount() == null ? 0 : report.getReportIllegalCount()));
			}
		}

		// 收集上周各部门进出港处罚数量
		Map<String, Long> lastAgencyCounts = new HashMap<>();
		for (WeeklyReport report : lastWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				lastAgencyCounts.put(agency, lastAgencyCounts.getOrDefault(agency, 0L) +
						(report.getReportIllegalCount() == null ? 0 : report.getReportIllegalCount()));
			}
		}

		// 基于统一的部门列表构建数据
		List<Long> currentCounts = new ArrayList<>();
		List<Long> lastCounts = new ArrayList<>();

		for (String agency : categories) {
			currentCounts.add(currentAgencyCounts.getOrDefault(agency, 0L));
			lastCounts.add(lastAgencyCounts.getOrDefault(agency, 0L));
		}

		// 添加数据到chartData
		chartData.put("categories", categories);
		Map<String, Object> reportIllegalCounts = new HashMap<>();
		reportIllegalCounts.put("current", currentCounts);
		reportIllegalCounts.put("last", lastCounts);
		chartData.put("reportIllegalCounts", reportIllegalCounts);

		return chartData;
	}

	/**
	 * 获取无线电处罚数据 - 修改后使用统一的部门列表
	 */
	private Map<String, Object> getWirelessIllegalChartData(List<String> categories, List<WeeklyReport> currentWeekReports, List<WeeklyReport> lastWeekReports) {
		Map<String, Object> chartData = new HashMap<>();

		// 收集本周各部门无线电处罚数量
		Map<String, Long> currentAgencyCounts = new HashMap<>();
		for (WeeklyReport report : currentWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				currentAgencyCounts.put(agency, currentAgencyCounts.getOrDefault(agency, 0L) +
						(report.getWirelessIllegalCount() == null ? 0 : report.getWirelessIllegalCount()));
			}
		}

		// 收集上周各部门无线电处罚数量 - 注意这里原来的代码有一个bug，应该使用WirelessIllegalCount而不是PolluteIllegalCount
		Map<String, Long> lastAgencyCounts = new HashMap<>();
		for (WeeklyReport report : lastWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				lastAgencyCounts.put(agency, lastAgencyCounts.getOrDefault(agency, 0L) +
						(report.getWirelessIllegalCount() == null ? 0 : report.getWirelessIllegalCount())); // 修正了bug
			}
		}

		// 基于统一的部门列表构建数据
		List<Long> currentCounts = new ArrayList<>();
		List<Long> lastCounts = new ArrayList<>();

		for (String agency : categories) {
			currentCounts.add(currentAgencyCounts.getOrDefault(agency, 0L));
			lastCounts.add(lastAgencyCounts.getOrDefault(agency, 0L));
		}

		// 添加数据到chartData
		chartData.put("categories", categories);
		Map<String, Object> wirelessIllegalCounts = new HashMap<>();
		wirelessIllegalCounts.put("current", currentCounts);
		wirelessIllegalCounts.put("last", lastCounts);
		chartData.put("wirelessIllegalCounts", wirelessIllegalCounts);

		return chartData;
	}

	/**
	 * 获取防污染处罚数据 - 修改后使用统一的部门列表
	 */
	private Map<String, Object> getPolluteIllegalChartData(List<String> categories, List<WeeklyReport> currentWeekReports, List<WeeklyReport> lastWeekReports) {
		Map<String, Object> chartData = new HashMap<>();

		// 收集本周各部门防污染处罚数量
		Map<String, Long> currentAgencyCounts = new HashMap<>();
		for (WeeklyReport report : currentWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				currentAgencyCounts.put(agency, currentAgencyCounts.getOrDefault(agency, 0L) +
						(report.getPolluteIllegalCount() == null ? 0 : report.getPolluteIllegalCount()));
			}
		}

		// 收集上周各部门防污染处罚数量
		Map<String, Long> lastAgencyCounts = new HashMap<>();
		for (WeeklyReport report : lastWeekReports) {
			if (report.getDepartmentId() != null && report.getDepartmentId().getOfficeName() != null) {
				String agency = report.getDepartmentId().getOfficeName();
				lastAgencyCounts.put(agency, lastAgencyCounts.getOrDefault(agency, 0L) +
						(report.getPolluteIllegalCount() == null ? 0 : report.getPolluteIllegalCount()));
			}
		}

		// 基于统一的部门列表构建数据
		List<Long> currentCounts = new ArrayList<>();
		List<Long> lastCounts = new ArrayList<>();

		for (String agency : categories) {
			currentCounts.add(currentAgencyCounts.getOrDefault(agency, 0L));
			lastCounts.add(lastAgencyCounts.getOrDefault(agency, 0L));
		}

		// 添加数据到chartData
		chartData.put("categories", categories);
		Map<String, Object> polluteIllegalCounts = new HashMap<>();
		polluteIllegalCounts.put("current", currentCounts);
		polluteIllegalCounts.put("last", lastCounts);
		chartData.put("polluteIllegalCounts", polluteIllegalCounts);

		return chartData;
	}

	private Map<String, Object> getPunishAgencyChartData(List<WeeklyReport> currentWeekReports, List<WeeklyReport> lastWeekReports) {
		Map<String, Object> chartData = new HashMap<>();

		// Prepare categories and data lists
		List<String> categories = new ArrayList<>();
		List<Long> currentCounts = new ArrayList<>();
		List<Long> lastCounts = new ArrayList<>();

		// Collect current week counts
		Map<String, Long> currentAgencyCounts = new HashMap<>();
		for (WeeklyReport report : currentWeekReports) {
			String agency = report.getDepartmentId().getOfficeName();
			currentAgencyCounts.put(agency, currentAgencyCounts.getOrDefault(agency, 0L) + (report.getPenaltyDecisionCount() == null ? 0 : report.getPenaltyDecisionCount()));
		}

		// Collect last week counts
		Map<String, Long> lastAgencyCounts = new HashMap<>();
		for (WeeklyReport report : lastWeekReports) {
			String agency = report.getDepartmentId().getOfficeName();
			lastAgencyCounts.put(agency, lastAgencyCounts.getOrDefault(agency, 0L) + (report.getPenaltyDecisionCount() == null ? 0 : report.getPenaltyDecisionCount()));
		}

		// Combine categories and data for chart (including agencies from both weeks)
		for (String agency : currentAgencyCounts.keySet()) {
			if (!categories.contains(agency)) {
				categories.add(agency);
			}
		}
		for (String agency : lastAgencyCounts.keySet()) {
			if (!categories.contains(agency)) {
				categories.add(agency);
			}
		}

		// Populate data lists based on categories
		for (String agency : categories) {
			currentCounts.add(currentAgencyCounts.getOrDefault(agency, 0L));
			lastCounts.add(lastAgencyCounts.getOrDefault(agency, 0L));
		}

		// Add data to the chartData map
		chartData.put("categories", categories);
		Map<String, Object> penaltyAgencyCounts = new HashMap<>();
		penaltyAgencyCounts.put("current", currentCounts);
		penaltyAgencyCounts.put("last", lastCounts);
		chartData.put("penaltyAgencyCounts", penaltyAgencyCounts);

		return chartData;
	}

	private Map<String, Object> getIndicatorData(List<WeeklyReport> currentWeekReports, List<WeeklyReport> lastWeekReports) {
		Map<String, Object> result = new HashMap<>();

		// 计算本周总数
		long currentPenaltyDecisionCount = currentWeekReports.stream().mapToLong(report -> report.getPenaltyDecisionCount() == null ? 0 : report.getPenaltyDecisionCount()).sum();
		double currentPenaltyAmount = currentWeekReports.stream().mapToDouble(report -> report.getPenaltyAmount() == null ? 0 : report.getPenaltyAmount()).sum();
		long currentIllegalScore = currentWeekReports.stream().mapToLong(report -> report.getIllegalScore() == null ? 0 : report.getIllegalScore()).sum();
		long currentReportIllegalCount = currentWeekReports.stream().mapToLong(report -> report.getReportIllegalCount()==null ? 0:report.getReportIllegalCount()).sum();
		long currentWirelessIllegalCount = currentWeekReports.stream().mapToLong(report -> report.getWirelessIllegalCount()==null ? 0:report.getWirelessIllegalCount()).sum();
		long currentPolluteIllegalCount = currentWeekReports.stream().mapToLong(report -> report.getPolluteIllegalCount()==null ? 0:report.getPolluteIllegalCount()).sum();

		// 计算上周总数
		long lastPenaltyDecisionCount = lastWeekReports.stream().mapToLong(report -> report.getPenaltyDecisionCount() == null ? 0 : report.getPenaltyDecisionCount()).sum();
		double lastPenaltyAmount = lastWeekReports.stream().mapToDouble(report -> report.getPenaltyAmount() == null ? 0 : report.getPenaltyAmount()).sum();
		long lastIllegalScore = lastWeekReports.stream().mapToLong(report -> report.getIllegalScore() == null ? 0 : report.getIllegalScore()).sum();
		long lastReportIllegalCount = lastWeekReports.stream().mapToLong(report -> report.getReportIllegalCount()==null ? 0:report.getReportIllegalCount()).sum();
		long lastWirelessIllegalCount = lastWeekReports.stream().mapToLong(report -> report.getWirelessIllegalCount()==null ? 0:report.getWirelessIllegalCount()).sum();
		long lastPolluteIllegalCount = lastWeekReports.stream().mapToLong(report -> report.getPolluteIllegalCount()==null ? 0:report.getPolluteIllegalCount()).sum();

		// 计算变化率
		double penaltyDecisionCountRate = calculateChangeRate(currentPenaltyDecisionCount, lastPenaltyDecisionCount);
		double penaltyAmountRate = calculateChangeRate(currentPenaltyAmount, lastPenaltyAmount);
		double illegalScoreRate = calculateChangeRate(currentIllegalScore, lastIllegalScore);
		double reportIllegalRate = calculateChangeRate(currentReportIllegalCount,lastReportIllegalCount);
		double wirelessIllegalRate = calculateChangeRate(currentWirelessIllegalCount,lastWirelessIllegalCount);
		double polluteIllegalRate = calculateChangeRate(currentPolluteIllegalCount,lastPolluteIllegalCount);

		result.put("penaltyDecisionCount", new HashMap<String, Object>() {{
			put("value", currentPenaltyDecisionCount);
			put("rate", penaltyDecisionCountRate);
		}});

		result.put("penaltyAmount", new HashMap<String, Object>() {{
			put("value", currentPenaltyAmount);
			put("rate", penaltyAmountRate);
		}});

		result.put("illegalScore", new HashMap<String, Object>() {{
			put("value", currentIllegalScore);
			put("rate", illegalScoreRate);
		}});
		result.put("illegalReport",new HashMap<String,Object>(){{
			put("value",currentReportIllegalCount);
			put("rate",reportIllegalRate);
		}});
		result.put("illegalWireless",new HashMap<String,Object>(){{
			put("value",currentWirelessIllegalCount);
			put("rate",wirelessIllegalRate);
		}});
		result.put("illegalPollute",new HashMap<String,Object>(){{
			put("value",currentPolluteIllegalCount);
			put("rate",polluteIllegalRate);
		}});
		return result;
	}

	private double calculateChangeRate(double current, double last) {
		if (last == 0) {
			return 0;
		}
		return (double) Math.round(((double) (current - last) / last) * 10000) / 100;
	}
}