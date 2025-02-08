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


		// 获取指标板数据
		Map<String, Object> indicatorData = getIndicatorData(currentWeekReports, lastWeekReports);

		// 获取图表数据
		Map<String, Object> chartData = getPunishAgencyChartData(currentWeekReports, lastWeekReports);
		chartData.put("indicatorData", indicatorData); // 将指标板数据添加到 chartData

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

		// 计算上周总数
		long lastPenaltyDecisionCount = lastWeekReports.stream().mapToLong(report -> report.getPenaltyDecisionCount() == null ? 0 : report.getPenaltyDecisionCount()).sum();
		double lastPenaltyAmount = lastWeekReports.stream().mapToDouble(report -> report.getPenaltyAmount() == null ? 0 : report.getPenaltyAmount()).sum();
		long lastIllegalScore = lastWeekReports.stream().mapToLong(report -> report.getIllegalScore() == null ? 0 : report.getIllegalScore()).sum();

		// 计算变化率
		double penaltyDecisionCountRate = calculateChangeRate(currentPenaltyDecisionCount, lastPenaltyDecisionCount);
		double penaltyAmountRate = calculateChangeRate(currentPenaltyAmount, lastPenaltyAmount);
		double illegalScoreRate = calculateChangeRate(currentIllegalScore, lastIllegalScore);

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

		return result;
	}

	private double calculateChangeRate(double current, double last) {
		if (last == 0) {
			return 0;
		}
		return (double) Math.round(((double) (current - last) / last) * 10000) / 100;
	}
}