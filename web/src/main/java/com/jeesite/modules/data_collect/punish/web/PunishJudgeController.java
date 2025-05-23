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
import org.apache.commons.lang3.StringUtils;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处罚决定Controller
 * @author 王浩宇
 * @version 2025-02-07
 */
@Controller
@RequestMapping(value = "${adminPath}/punish/punishJudge")
public class PunishJudgeController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PunishJudgeController.class);

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
	public Map<String, Object> getChartDataWithDate(String startDate, String endDate, String penaltyAgency, 
												  String seaRiverShip, String caseReason, 
												  String managementCategory, String violationCircumstances, 
												  String minAmount, String maxAmount, String shipType) {
		Map<String, Object> chartData = new HashMap<>();
		
		// 构建查询条件
		PunishJudge punishJudge = new PunishJudge();
		
		// 设置时间范围
		if (StringUtils.isNotBlank(startDate)) {
			punishJudge.setPenaltyDecisionTime_gte(DateUtils.parseDate(startDate));
		}
		if (StringUtils.isNotBlank(endDate)) {
			punishJudge.setPenaltyDecisionTime_lte(DateUtils.parseDate(endDate));
		}
		
		// 设置筛选条件
		if (StringUtils.isNotBlank(penaltyAgency)) {
			punishJudge.setPenaltyAgency(penaltyAgency);
		}
		if (StringUtils.isNotBlank(seaRiverShip)) {
			punishJudge.setSeaRiverShip(seaRiverShip);
		}
		if (StringUtils.isNotBlank(caseReason)) {
			punishJudge.setCaseReason(caseReason);
		}
		if (StringUtils.isNotBlank(managementCategory)) {
			punishJudge.setManagementCategory(managementCategory);
		}
		if (StringUtils.isNotBlank(violationCircumstances)) {
			punishJudge.setViolationCircumstances(violationCircumstances);
		}
		if (StringUtils.isNotBlank(shipType)) {
			punishJudge.setShipType(shipType);
		}
		
		// 设置金额范围
		if (StringUtils.isNotBlank(minAmount)) {
			try {
				punishJudge.setPenaltyAmount_gte(Double.parseDouble(minAmount));
			} catch (NumberFormatException e) {
				logger.warn("Invalid minAmount format: {}", minAmount);
			}
		}
		if (StringUtils.isNotBlank(maxAmount)) {
			try {
				punishJudge.setPenaltyAmount_lte(Double.parseDouble(maxAmount));
			} catch (NumberFormatException e) {
				logger.warn("Invalid maxAmount format: {}", maxAmount);
			}
		}
		
		// 查询当前时间范围的数据
		List<PunishJudge> currentPunishList = punishJudgeService.findList(punishJudge);
		
		// 计算上周同期的时间范围
		PunishJudge lastPunishJudge = new PunishJudge();
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);
			LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
			LocalDate endLocalDate = LocalDate.parse(endDate, formatter);
			LocalDate lastStartLocalDate = startLocalDate.minusDays(7);
			LocalDate lastEndLocalDate = endLocalDate.minusDays(7);
			
			lastPunishJudge.setPenaltyDecisionTime_gte(DateUtils.asDate(lastStartLocalDate));
			lastPunishJudge.setPenaltyDecisionTime_lte(DateUtils.asDate(lastEndLocalDate));
			
			// 复制其他筛选条件
			if (StringUtils.isNotBlank(penaltyAgency)) {
				lastPunishJudge.setPenaltyAgency(penaltyAgency);
			}
			if (StringUtils.isNotBlank(seaRiverShip)) {
				lastPunishJudge.setSeaRiverShip(seaRiverShip);
			}
			if (StringUtils.isNotBlank(caseReason)) {
				lastPunishJudge.setCaseReason(caseReason);
			}
			if (StringUtils.isNotBlank(managementCategory)) {
				lastPunishJudge.setManagementCategory(managementCategory);
			}
			if (StringUtils.isNotBlank(violationCircumstances)) {
				lastPunishJudge.setViolationCircumstances(violationCircumstances);
			}
			if (StringUtils.isNotBlank(shipType)) {
				lastPunishJudge.setShipType(shipType);
			}
			if (StringUtils.isNotBlank(minAmount)) {
				lastPunishJudge.setPenaltyAmount_gte(Double.parseDouble(minAmount));
			}
			if (StringUtils.isNotBlank(maxAmount)) {
				lastPunishJudge.setPenaltyAmount_lte(Double.parseDouble(maxAmount));
			}
		}
		
		// 查询上周同期的数据
		List<PunishJudge> lastPunishList = punishJudgeService.findList(lastPunishJudge);
		
		// 获取所有机构列表
		List<String> categories = getAllPenaltyAgencies(currentPunishList, lastPunishList);
		chartData.put("categories", categories);
		
		// 计算指标数据
		Map<String, Object> indicatorData = getIndicatorDataFromPunish(currentPunishList, lastPunishList);
		chartData.put("indicatorData", indicatorData);
		
		// 获取各机构处罚决定数量数据
		Map<String, Object> penaltyAgencyCounts = getPenaltyAgencyCountsFromPunish(categories, currentPunishList, lastPunishList);
		chartData.put("penaltyAgencyCounts", penaltyAgencyCounts);
		
		// 获取各机构处罚金额数据
		Map<String, Object> penaltyAmountByAgency = getPenaltyAmountByAgencyFromPunish(categories, currentPunishList, lastPunishList);
		chartData.put("penaltyAmountByAgency", penaltyAmountByAgency);
		
		// 获取案由对比数据
		Map<String, Object> caseReasonData = getCaseReasonDataFromPunish(currentPunishList);
		chartData.put("caseReasonData", caseReasonData);
		
		// 获取处罚类别对比数据
		Map<String, Object> managementCategoryData = getManagementCategoryDataFromPunish(currentPunishList);
		chartData.put("managementCategoryData", managementCategoryData);
		
		return chartData;
	}

	/**
	 * 获取所有处罚机构列表
	 */
	private List<String> getAllPenaltyAgencies(List<PunishJudge> currentPunishList, List<PunishJudge> lastPunishList) {
		Set<String> agencySet = new LinkedHashSet<>(); // 使用LinkedHashSet保持插入顺序
		
		// 收集所有机构名称
		for (PunishJudge punish : currentPunishList) {
			if (StringUtils.isNotBlank(punish.getPenaltyAgency())) {
				agencySet.add(punish.getPenaltyAgency());
			}
		}
		
		for (PunishJudge punish : lastPunishList) {
			if (StringUtils.isNotBlank(punish.getPenaltyAgency())) {
				agencySet.add(punish.getPenaltyAgency());
			}
		}
		
		return new ArrayList<>(agencySet);
	}

	/**
	 * 计算指标数据
	 */
	private Map<String, Object> getIndicatorDataFromPunish(List<PunishJudge> currentPunishList, List<PunishJudge> lastPunishList) {
		Map<String, Object> result = new HashMap<>();
		
		// 计算当前处罚决定数量
		long currentPenaltyDecisionCount = currentPunishList.size();
		
		// 计算当前处罚总金额
		double currentPenaltyAmount = currentPunishList.stream()
				.mapToDouble(punish -> punish.getPenaltyAmount() == null ? 0 : punish.getPenaltyAmount())
				.sum();
		
		// 计算上周处罚决定数量
		long lastPenaltyDecisionCount = lastPunishList.size();
		
		// 计算上周处罚总金额
		double lastPenaltyAmount = lastPunishList.stream()
				.mapToDouble(punish -> punish.getPenaltyAmount() == null ? 0 : punish.getPenaltyAmount())
				.sum();
		
		// 计算变化率
		double penaltyDecisionCountRate = calculateChangeRate(currentPenaltyDecisionCount, lastPenaltyDecisionCount);
		double penaltyAmountRate = calculateChangeRate(currentPenaltyAmount, lastPenaltyAmount);
		
		// 处罚决定数量
		result.put("penaltyDecisionCount", new HashMap<String, Object>() {{
			put("value", currentPenaltyDecisionCount);
			put("rate", penaltyDecisionCountRate);
		}});
		
		// 处罚总金额
		result.put("penaltyAmount", new HashMap<String, Object>() {{
			put("value", currentPenaltyAmount);
			put("rate", penaltyAmountRate);
		}});
		
		return result;
	}

	/**
	 * 获取各机构处罚决定数量数据
	 */
	private Map<String, Object> getPenaltyAgencyCountsFromPunish(List<String> categories, List<PunishJudge> currentPunishList, List<PunishJudge> lastPunishList) {
		Map<String, Object> result = new HashMap<>();
		
		// 统计当前各机构处罚数量
		Map<String, Long> currentAgencyCounts = currentPunishList.stream()
				.filter(punish -> StringUtils.isNotBlank(punish.getPenaltyAgency()))
				.collect(Collectors.groupingBy(PunishJudge::getPenaltyAgency, Collectors.counting()));
		
		// 统计上周各机构处罚数量
		Map<String, Long> lastAgencyCounts = lastPunishList.stream()
				.filter(punish -> StringUtils.isNotBlank(punish.getPenaltyAgency()))
				.collect(Collectors.groupingBy(PunishJudge::getPenaltyAgency, Collectors.counting()));
		
		// 基于统一的机构列表构建数据
		List<Map<String, Object>> currentCounts = new ArrayList<>();
		List<Long> lastCounts = new ArrayList<>();
		
		for (String agency : categories) {
			long currentCount = currentAgencyCounts.getOrDefault(agency, 0L);
			long lastCount = lastAgencyCounts.getOrDefault(agency, 0L);
			
			// 计算环比变化率
			double changeRate = calculateChangeRate(currentCount, lastCount);
			
			// 构建当前数据对象（包含值和环比）
			Map<String, Object> currentData = new HashMap<>();
			currentData.put("value", currentCount);
			currentData.put("changeRate", changeRate);
			
			currentCounts.add(currentData);
			lastCounts.add(lastCount);
		}
		
		result.put("current", currentCounts);
		result.put("last", lastCounts);
		
		return result;
	}

	/**
	 * 获取各机构处罚金额数据
	 */
	private Map<String, Object> getPenaltyAmountByAgencyFromPunish(List<String> categories, List<PunishJudge> currentPunishList, List<PunishJudge> lastPunishList) {
		Map<String, Object> result = new HashMap<>();
		
		// 统计当前各机构处罚金额
		Map<String, Double> currentAgencyAmounts = currentPunishList.stream()
				.filter(punish -> StringUtils.isNotBlank(punish.getPenaltyAgency()))
				.collect(Collectors.groupingBy(
						PunishJudge::getPenaltyAgency,
						Collectors.summingDouble(punish -> punish.getPenaltyAmount() == null ? 0 : punish.getPenaltyAmount())
				));
		
		// 统计上周各机构处罚金额
		Map<String, Double> lastAgencyAmounts = lastPunishList.stream()
				.filter(punish -> StringUtils.isNotBlank(punish.getPenaltyAgency()))
				.collect(Collectors.groupingBy(
						PunishJudge::getPenaltyAgency,
						Collectors.summingDouble(punish -> punish.getPenaltyAmount() == null ? 0 : punish.getPenaltyAmount())
				));
		
		// 基于统一的机构列表构建数据
		List<Map<String, Object>> currentAmounts = new ArrayList<>();
		List<Double> lastAmounts = new ArrayList<>();
		
		for (String agency : categories) {
			double currentAmount = currentAgencyAmounts.getOrDefault(agency, 0.0);
			double lastAmount = lastAgencyAmounts.getOrDefault(agency, 0.0);
			
			// 计算环比变化率
			double changeRate = calculateChangeRate(currentAmount, lastAmount);
			
			// 构建当前数据对象（包含值和环比）
			Map<String, Object> currentData = new HashMap<>();
			currentData.put("value", currentAmount);
			currentData.put("changeRate", changeRate);
			
			currentAmounts.add(currentData);
			lastAmounts.add(lastAmount);
		}
		
		result.put("current", currentAmounts);
		result.put("last", lastAmounts);
		
		return result;
	}

	/**
	 * 获取案由对比数据
	 */
	private Map<String, Object> getCaseReasonDataFromPunish(List<PunishJudge> punishList) {
		Map<String, Object> result = new HashMap<>();
		
		// 按案由分组并计数
		Map<String, Long> caseReasonCounts = punishList.stream()
				.filter(punish -> StringUtils.isNotBlank(punish.getCaseReason()))
				.collect(Collectors.groupingBy(PunishJudge::getCaseReason, Collectors.counting()));
		
		// 转换为图表所需格式
		List<Map<String, Object>> caseReasonData = new ArrayList<>();
		for (Map.Entry<String, Long> entry : caseReasonCounts.entrySet()) {
			Map<String, Object> item = new HashMap<>();
			item.put("name", entry.getKey());
			item.put("value", entry.getValue());
			caseReasonData.add(item);
		}
		
		// 按数量降序排序
		caseReasonData.sort((a, b) -> Long.compare((Long)b.get("value"), (Long)a.get("value")));
		
		result.put("data", caseReasonData);
		
		return result;
	}

	/**
	 * 获取处罚类别对比数据
	 */
	private Map<String, Object> getManagementCategoryDataFromPunish(List<PunishJudge> punishList) {
		Map<String, Object> result = new HashMap<>();
		
		// 按管理类别分组并计数
		Map<String, Long> categoryCounts = punishList.stream()
				.filter(punish -> StringUtils.isNotBlank(punish.getManagementCategory()))
				.collect(Collectors.groupingBy(PunishJudge::getManagementCategory, Collectors.counting()));
		
		// 转换为图表所需格式
		List<Map<String, Object>> categoryData = new ArrayList<>();
		for (Map.Entry<String, Long> entry : categoryCounts.entrySet()) {
			Map<String, Object> item = new HashMap<>();
			item.put("name", entry.getKey());
			item.put("value", entry.getValue());
			categoryData.add(item);
		}
		
		result.put("data", categoryData);
		
		return result;
	}

	/**
	 * 获取下拉列表选项
	 */
	@GetMapping("getFilterOptions")
	@ResponseBody
	public Map<String, Object> getFilterOptions() {
		Map<String, Object> result = new HashMap<>();
		
		// 获取所有违法情节选项
		List<String> violationCircumstances = punishJudgeService.findViolationCircumstances();
		result.put("violationCircumstances", violationCircumstances);
		
		// 获取所有船舶种类选项
		List<String> shipTypes = punishJudgeService.findShipTypes();
		result.put("shipTypes", shipTypes);
		
		// 获取所有处罚机构选项
		List<String> penaltyAgencies = punishJudgeService.findPenaltyAgencies();
		result.put("penaltyAgencies", penaltyAgencies);
		
		// 获取所有案由选项
		List<String> caseReasons = punishJudgeService.findCaseReasons();
		result.put("caseReasons", caseReasons);
		
		// 获取所有业务类型选项
		List<String> managementCategories = punishJudgeService.findManagementCategories();
		result.put("managementCategories", managementCategories);
		
		return result;
	}

	/**
	 * 获取机构处罚详情
	 */
	@GetMapping("getPenaltyDetails")
	@ResponseBody
	public List<Map<String, Object>> getPenaltyDetails(String startDate, String endDate, String penaltyAgency, 
                                     String seaRiverShip, String caseReason, 
                                     String managementCategory, String violationCircumstances, 
                                     String minAmount, String maxAmount, String shipType) {
		// 构建查询条件
		PunishJudge punishJudge = new PunishJudge();
		
		// 设置时间范围
		if (StringUtils.isNotBlank(startDate)) {
			punishJudge.setPenaltyDecisionTime_gte(DateUtils.parseDate(startDate));
		}
		if (StringUtils.isNotBlank(endDate)) {
			punishJudge.setPenaltyDecisionTime_lte(DateUtils.parseDate(endDate));
		}
		
		// 设置机构
		if (StringUtils.isNotBlank(penaltyAgency)) {
			punishJudge.setPenaltyAgency(penaltyAgency);
		}
		
		// 设置其他筛选条件
		if (StringUtils.isNotBlank(seaRiverShip)) {
			punishJudge.setSeaRiverShip(seaRiverShip);
		}
		if (StringUtils.isNotBlank(caseReason)) {
			punishJudge.setCaseReason(caseReason);
		}
		if (StringUtils.isNotBlank(managementCategory)) {
			punishJudge.setManagementCategory(managementCategory);
		}
		if (StringUtils.isNotBlank(violationCircumstances)) {
			punishJudge.setViolationCircumstances(violationCircumstances);
		}
		if (StringUtils.isNotBlank(shipType)) {
			punishJudge.setShipType(shipType);
		}
		
		// 设置金额范围
		if (StringUtils.isNotBlank(minAmount)) {
			try {
				punishJudge.setPenaltyAmount_gte(Double.parseDouble(minAmount));
			} catch (NumberFormatException e) {
				logger.warn("Invalid minAmount format: {}", minAmount);
			}
		}
		if (StringUtils.isNotBlank(maxAmount)) {
			try {
				punishJudge.setPenaltyAmount_lte(Double.parseDouble(maxAmount));
			} catch (NumberFormatException e) {
				logger.warn("Invalid maxAmount format: {}", maxAmount);
			}
		}
		
		// 查询数据
		List<PunishJudge> punishList = punishJudgeService.findList(punishJudge);
		
		// 转换为前端所需格式
		List<Map<String, Object>> result = new ArrayList<>();
		for (PunishJudge punish : punishList) {
			Map<String, Object> item = new HashMap<>();
			item.put("caseReason", punish.getCaseReason());
			item.put("shipName", punish.getShipName());
			item.put("shipType", punish.getShipType());
			item.put("seaRiverShip", punish.getSeaRiverShip());
			item.put("penaltyAgency", punish.getPenaltyAgency());
			item.put("penaltyAmount", punish.getPenaltyAmount());
			item.put("penaltyDecisionTime", DateUtils.formatDate(punish.getPenaltyDecisionTime(), "yyyy-MM-dd"));
			item.put("managementCategory", punish.getManagementCategory());
			item.put("violationCircumstances", punish.getViolationCircumstances());
			result.add(item);
		}
		
		return result;
	}

	private double calculateChangeRate(double current, double last) {
		if (last == 0) {
			return 0;
		}
		return (double) Math.round(((double) (current - last) / last) * 10000) / 100;
	}

	@GetMapping("fetchData")
	@ResponseBody
	public Map<String, Object> fetchData(String startDate, String endDate, String penaltyAgency, 
                                     String seaRiverShip, String caseReason, 
                                     String managementCategory, String violationCircumstances, 
                                     String minAmount, String maxAmount, String shipType) {
		return getChartDataWithDate(startDate, endDate, penaltyAgency, seaRiverShip, caseReason,
                               managementCategory, violationCircumstances, minAmount, maxAmount, shipType);
	}
}