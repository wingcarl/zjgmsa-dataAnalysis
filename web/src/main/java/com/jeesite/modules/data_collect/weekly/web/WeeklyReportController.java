package com.jeesite.modules.data_collect.weekly.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReport;
import com.jeesite.modules.data_collect.weekly.service.WeeklyReportService;

/**
 * 周工作数据表Controller
 * @author 王浩宇
 * @version 2024-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/weekly/weeklyReport")
public class WeeklyReportController extends BaseController {

	@Autowired
	private WeeklyReportService weeklyReportService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public WeeklyReport get(String id, boolean isNewRecord) {
		return weeklyReportService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(WeeklyReport weeklyReport, Model model) {
		model.addAttribute("weeklyReport", weeklyReport);
		return "data_collect/weekly/weeklyReportList";
	}

	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = {"dash"})
	public String dashboard(){
		return "data_collect/weekly/weeklyDashboard";
	}
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<WeeklyReport> listData(WeeklyReport weeklyReport, HttpServletRequest request, HttpServletResponse response) {
		weeklyReport.setPage(new Page<>(request, response));
		Page<WeeklyReport> page = weeklyReportService.findPage(weeklyReport);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "form")
	public String form(WeeklyReport weeklyReport, Model model) {
		model.addAttribute("weeklyReport", weeklyReport);
		return "data_collect/weekly/weeklyReportForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("weekly:weeklyReport:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated WeeklyReport weeklyReport) {
		weeklyReportService.save(weeklyReport);
		return renderResult(Global.TRUE, text("保存周工作数据表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "exportData")
	public void exportData(WeeklyReport weeklyReport, HttpServletResponse response) {
		List<WeeklyReport> list = weeklyReportService.findList(weeklyReport);
		String fileName = "周工作数据表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("周工作数据表", WeeklyReport.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		WeeklyReport weeklyReport = new WeeklyReport();
		List<WeeklyReport> list = ListUtils.newArrayList(weeklyReport);
		String fileName = "周工作数据表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("周工作数据表", WeeklyReport.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("weekly:weeklyReport:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = weeklyReportService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("weekly:weeklyReport:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(WeeklyReport weeklyReport) {
		weeklyReportService.delete(weeklyReport);
		return renderResult(Global.TRUE, text("删除周工作数据表成功！"));
	}

	/**
	 * 跳转到图表页面
	 * @return
	 */
	@RequestMapping(value = "chart")
	public String chart(){
		return "data_collect/weekly/weeklyReportChart";
	}


	/**
	 * 获取Echarts图表数据
	 * @return
	 */
	@RequestMapping(value = "chartData")
	@ResponseBody
	public Map<String, Object> getChartData() {
		List<WeeklyReport> weeklyReports = weeklyReportService.findList(new WeeklyReport());

		// 将数据分组
		Map<String, List<WeeklyReport>> groupedData = new HashMap<>();
		for(WeeklyReport report : weeklyReports){
			String departmentName = report.getDepartmentId().getOfficeName();
			if(!groupedData.containsKey(departmentName)){
				groupedData.put(departmentName,new ArrayList<>());
			}
			groupedData.get(departmentName).add(report);
		}

		//准备echarts需要的数据格式
		List<String> categories = new ArrayList<>();
		List<Long> seaShipInspectionCounts = new ArrayList<>();
		List<Long> seaShipDefectCounts = new ArrayList<>();
		List<Long> seaShipDetentionCounts = new ArrayList<>();

		for(Map.Entry<String,List<WeeklyReport>> entry : groupedData.entrySet()){
			String departmentName = entry.getKey();
			List<WeeklyReport> list = entry.getValue();
			long totalInspectionCount = 0;
			long totalDefectCount = 0;
			long totalDetentionCount = 0;
			for (WeeklyReport report : list){
				totalInspectionCount += report.getSeaShipInspectionCount() == null ? 0 : report.getSeaShipInspectionCount();
				totalDefectCount += report.getSeaShipDefectCount() == null ? 0 : report.getSeaShipDefectCount();
				totalDetentionCount += report.getSeaShipDetentionCount() == null ? 0 : report.getSeaShipDetentionCount();
			}
			categories.add(departmentName);
			seaShipInspectionCounts.add(totalInspectionCount);
			seaShipDefectCounts.add(totalDefectCount);
			seaShipDetentionCounts.add(totalDetentionCount);
		}


		Map<String, Object> result = new HashMap<>();
		result.put("categories", categories);
		result.put("seaShipInspectionCounts", seaShipInspectionCounts);
		result.put("seaShipDefectCounts", seaShipDefectCounts);
		result.put("seaShipDetentionCounts", seaShipDetentionCounts);
		return result;
	}
	@GetMapping("fetchData")
	@ResponseBody
	public Map<String, Object> getFetchData(String currentWeekStartDate, String lastWeekStartDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);
		LocalDate currentStartLocalDate = LocalDate.parse(currentWeekStartDate, formatter);
		weeklyReportService.fetchData(currentStartLocalDate);
		// 查询本周的数据
		WeeklyReport weeklyReportCurrent = new WeeklyReport();
		Map<String,Object> re = getChartDataWithDate(currentWeekStartDate,lastWeekStartDate);
		return re;
	}
	/**
	 * 获取Echarts图表数据
	 * @return
	 */
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

		// 获取指标板数据
		Map<String, Object> indicatorData = getIndicatorData(currentWeekReports, lastWeekReports);

		// 将数据分组
		Map<String, List<WeeklyReport>> groupedCurrentData = new HashMap<>();
		for(WeeklyReport report : currentWeekReports){
			String departmentName = report.getDepartmentId().getOfficeName();
			if(!groupedCurrentData.containsKey(departmentName)){
				groupedCurrentData.put(departmentName,new ArrayList<>());
			}
			groupedCurrentData.get(departmentName).add(report);
		}

		Map<String, List<WeeklyReport>> groupedLastData = new HashMap<>();
		for(WeeklyReport report : lastWeekReports){
			String departmentName = report.getDepartmentId().getOfficeName();
			if(!groupedLastData.containsKey(departmentName)){
				groupedLastData.put(departmentName,new ArrayList<>());
			}
			groupedLastData.get(departmentName).add(report);
		}


		//准备echarts需要的数据格式
		List<String> categories = new ArrayList<>();
		Map<String,Object> seaShipInspectionCounts = new HashMap<>();
		Map<String,Object> seaShipDefectCounts = new HashMap<>();
		Map<String,Object> seaShipDetentionCounts = new HashMap<>();
		Map<String,Object> riverShipInspectionCounts = new HashMap<>();
		Map<String,Object> riverShipDefectCounts = new HashMap<>();
		Map<String,Object> riverShipDetentionCounts = new HashMap<>();


		List<Object> currentSeaShipInspectionCountsList = new ArrayList<>();
		List<Object> lastSeaShipInspectionCountsList = new ArrayList<>();
		List<Object> currentSeaShipDefectCountsList = new ArrayList<>();
		List<Object> lastSeaShipDefectCountsList = new ArrayList<>();
		List<Object> currentSeaShipDetentionCountsList = new ArrayList<>();
		List<Object> lastSeaShipDetentionCountsList = new ArrayList<>();

		List<Object> currentRiverShipInspectionCountsList = new ArrayList<>();
		List<Object> lastRiverShipInspectionCountsList = new ArrayList<>();
		List<Object> currentRiverShipDefectCountsList = new ArrayList<>();
		List<Object> lastRiverShipDefectCountsList = new ArrayList<>();
		List<Object> currentRiverShipDetentionCountsList = new ArrayList<>();
		List<Object> lastRiverShipDetentionCountsList = new ArrayList<>();




		for(Map.Entry<String,List<WeeklyReport>> currentEntry : groupedCurrentData.entrySet()){
			String departmentName = currentEntry.getKey();
			categories.add(departmentName);
			List<WeeklyReport> currentList = currentEntry.getValue();
			long currentTotalInspectionCount = 0;
			long currentTotalDefectCount = 0;
			long currentTotalDetentionCount = 0;
			long currentTotalRiverInspectionCount = 0;
			long currentTotalRiverDefectCount = 0;
			long currentTotalRiverDetentionCount = 0;
			for (WeeklyReport report : currentList){
				currentTotalInspectionCount += report.getSeaShipInspectionCount() == null ? 0 : report.getSeaShipInspectionCount();
				currentTotalDefectCount += report.getSeaShipDefectCount() == null ? 0 : report.getSeaShipDefectCount();
				currentTotalDetentionCount += report.getSeaShipDetentionCount() == null ? 0 : report.getSeaShipDetentionCount();
				currentTotalRiverInspectionCount += report.getRiverShipInspectionCount() == null ? 0 : report.getRiverShipInspectionCount();
				currentTotalRiverDefectCount += report.getRiverShipDefectCount() == null ? 0 : report.getRiverShipDefectCount();
				currentTotalRiverDetentionCount += report.getRiverShipDetentionCount() == null ? 0 : report.getRiverShipDetentionCount();
			}


			//查找上周是否有数据
			long lastTotalInspectionCount = 0;
			long lastTotalDefectCount = 0;
			long lastTotalDetentionCount = 0;
			long lastTotalRiverInspectionCount = 0;
			long lastTotalRiverDefectCount = 0;
			long lastTotalRiverDetentionCount = 0;
			if(groupedLastData.containsKey(departmentName)){
				List<WeeklyReport> lastList =  groupedLastData.get(departmentName);
				for (WeeklyReport report : lastList){
					lastTotalInspectionCount += report.getSeaShipInspectionCount() == null ? 0 : report.getSeaShipInspectionCount();
					lastTotalDefectCount += report.getSeaShipDefectCount() == null ? 0 : report.getSeaShipDefectCount();
					lastTotalDetentionCount += report.getSeaShipDetentionCount() == null ? 0 : report.getSeaShipDetentionCount();
					lastTotalRiverInspectionCount += report.getRiverShipInspectionCount() == null ? 0 : report.getRiverShipInspectionCount();
					lastTotalRiverDefectCount += report.getRiverShipDefectCount() == null ? 0 : report.getRiverShipDefectCount();
					lastTotalRiverDetentionCount += report.getRiverShipDetentionCount() == null ? 0 : report.getRiverShipDetentionCount();
				}
			}

			// 计算变化率并添加到本周数据中
			double inspectionChangeRate = 0;
			if(lastTotalInspectionCount !=0){
				inspectionChangeRate =   (double)Math.round(( (double) (currentTotalInspectionCount - lastTotalInspectionCount)/ lastTotalInspectionCount) * 10000 ) / 100 ;
			}

			final long finalCurrentTotalInspectionCount = currentTotalInspectionCount;
			final double finalInspectionChangeRate = inspectionChangeRate;
			currentSeaShipInspectionCountsList.add(new  HashMap<String, Object>(){{
				put("value",finalCurrentTotalInspectionCount);
				put("changeRate",finalInspectionChangeRate);
			}});
			lastSeaShipInspectionCountsList.add(lastTotalInspectionCount);


			double defectChangeRate = 0;
			if(lastTotalDefectCount !=0){
				defectChangeRate = (double)Math.round(( (double) (currentTotalDefectCount - lastTotalDefectCount)/ lastTotalDefectCount) * 10000 ) / 100 ;
			}
			final long finalCurrentTotalDefectCount = currentTotalDefectCount;
			final double finalDefectChangeRate = defectChangeRate;
			currentSeaShipDefectCountsList.add(new  HashMap<String, Object>(){{
				put("value",finalCurrentTotalDefectCount);
				put("changeRate",finalDefectChangeRate);
			}});
			lastSeaShipDefectCountsList.add(lastTotalDefectCount);


			double detentionChangeRate = 0;
			if(lastTotalDetentionCount!=0){
				detentionChangeRate =  (double)Math.round(( (double) (currentTotalDetentionCount - lastTotalDetentionCount)/ lastTotalDetentionCount) * 10000 ) / 100 ;
			}
			final long finalCurrentTotalDetentionCount = currentTotalDetentionCount;
			final double finalDetentionChangeRate = detentionChangeRate;
			currentSeaShipDetentionCountsList.add(new HashMap<String, Object>(){{
				put("value",finalCurrentTotalDetentionCount);
				put("changeRate",finalDetentionChangeRate);
			}});
			lastSeaShipDetentionCountsList.add(lastTotalDetentionCount);

			double riverInspectionChangeRate = 0;
			if(lastTotalRiverInspectionCount !=0){
				riverInspectionChangeRate =   (double)Math.round(( (double) (currentTotalRiverInspectionCount - lastTotalRiverInspectionCount)/ lastTotalRiverInspectionCount) * 10000 ) / 100 ;
			}

			final long finalCurrentTotalRiverInspectionCount = currentTotalRiverInspectionCount;
			final double finalRiverInspectionChangeRate = riverInspectionChangeRate;
			currentRiverShipInspectionCountsList.add(new  HashMap<String, Object>(){{
				put("value",finalCurrentTotalRiverInspectionCount);
				put("changeRate",finalRiverInspectionChangeRate);
			}});
			lastRiverShipInspectionCountsList.add(lastTotalRiverInspectionCount);



			double riverDefectChangeRate = 0;
			if(lastTotalRiverDefectCount !=0){
				riverDefectChangeRate = (double)Math.round(( (double) (currentTotalRiverDefectCount - lastTotalRiverDefectCount)/ lastTotalRiverDefectCount) * 10000 ) / 100 ;
			}
			final long finalCurrentTotalRiverDefectCount = currentTotalRiverDefectCount;
			final double finalRiverDefectChangeRate = riverDefectChangeRate;
			currentRiverShipDefectCountsList.add(new  HashMap<String, Object>(){{
				put("value",finalCurrentTotalRiverDefectCount);
				put("changeRate",finalRiverDefectChangeRate);
			}});
			lastRiverShipDefectCountsList.add(lastTotalRiverDefectCount);


			double riverDetentionChangeRate = 0;
			if(lastTotalRiverDetentionCount!=0){
				riverDetentionChangeRate =  (double)Math.round(( (double) (currentTotalRiverDetentionCount - lastTotalRiverDetentionCount)/ lastTotalRiverDetentionCount) * 10000 ) / 100 ;
			}
			final long finalCurrentTotalRiverDetentionCount = currentTotalRiverDetentionCount;
			final double finalRiverDetentionChangeRate = riverDetentionChangeRate;
			currentRiverShipDetentionCountsList.add(new HashMap<String, Object>(){{
				put("value",finalCurrentTotalRiverDetentionCount);
				put("changeRate",finalRiverDetentionChangeRate);
			}});
			lastRiverShipDetentionCountsList.add(lastTotalRiverDetentionCount);


		}


		seaShipInspectionCounts.put("current",currentSeaShipInspectionCountsList);
		seaShipInspectionCounts.put("last",lastSeaShipInspectionCountsList);
		seaShipDefectCounts.put("current",currentSeaShipDefectCountsList);
		seaShipDefectCounts.put("last",lastSeaShipDefectCountsList);
		seaShipDetentionCounts.put("current",currentSeaShipDetentionCountsList);
		seaShipDetentionCounts.put("last",lastSeaShipDetentionCountsList);


		riverShipInspectionCounts.put("current",currentRiverShipInspectionCountsList);
		riverShipInspectionCounts.put("last",lastRiverShipInspectionCountsList);
		riverShipDefectCounts.put("current",currentRiverShipDefectCountsList);
		riverShipDefectCounts.put("last",lastRiverShipDefectCountsList);
		riverShipDetentionCounts.put("current",currentRiverShipDetentionCountsList);
		riverShipDetentionCounts.put("last",lastRiverShipDetentionCountsList);


		Map<String, Object> result = new HashMap<>();
		result.put("categories", categories);
		result.put("seaShipInspectionCounts", seaShipInspectionCounts);
		result.put("seaShipDefectCounts", seaShipDefectCounts);
		result.put("seaShipDetentionCounts", seaShipDetentionCounts);

		result.put("riverShipInspectionCounts", riverShipInspectionCounts);
		result.put("riverShipDefectCounts", riverShipDefectCounts);
		result.put("riverShipDetentionCounts", riverShipDetentionCounts);
		result.put("indicatorData", indicatorData);
		return result;
	}

	private Map<String, Object> getIndicatorData(List<WeeklyReport> currentWeekReports, List<WeeklyReport> lastWeekReports) {
		Map<String, Object> result = new HashMap<>();

		// 计算本周总数
		long currentSeaShipInspectionCount = currentWeekReports.stream().mapToLong(report -> report.getSeaShipInspectionCount() == null ? 0 : report.getSeaShipInspectionCount()).sum();
		long currentSeaShipDefectCount = currentWeekReports.stream().mapToLong(report -> report.getSeaShipDefectCount() == null ? 0 : report.getSeaShipDefectCount()).sum();
		long currentSeaShipDetentionCount = currentWeekReports.stream().mapToLong(report -> report.getSeaShipDetentionCount() == null ? 0 : report.getSeaShipDetentionCount()).sum();
		long currentRiverShipInspectionCount = currentWeekReports.stream().mapToLong(report -> report.getRiverShipInspectionCount() == null ? 0 : report.getRiverShipInspectionCount()).sum();
		long currentRiverShipDefectCount = currentWeekReports.stream().mapToLong(report -> report.getRiverShipDefectCount() == null ? 0 : report.getRiverShipDefectCount()).sum();
		long currentRiverShipDetentionCount = currentWeekReports.stream().mapToLong(report -> report.getRiverShipDetentionCount() == null ? 0 : report.getRiverShipDetentionCount()).sum();
		long currentPscInspectionCount = currentWeekReports.stream().mapToLong(report -> report.getPscInspectionCount() == null ? 0 : report.getPscInspectionCount()).sum();
		long currentPscDefectCount = currentWeekReports.stream().mapToLong(report -> report.getPscDefectCount() == null ? 0 : report.getPscDefectCount()).sum();
		long currentPscDetentionCount = currentWeekReports.stream().mapToLong(report -> report.getPscDetentionCount() == null ? 0 : report.getPscDetentionCount()).sum();
		long currentOnSiteCount = currentWeekReports.stream().mapToLong(report -> report.getOnSiteCount()== null ? 0 : report.getOnSiteCount()).sum();
		long currentOnSiteAbnormalCount = currentWeekReports.stream().mapToLong(report -> report.getOnSiteAbnormalCount()== null ? 0 : report.getOnSiteAbnormalCount()).sum();

		// 计算上周总数
		long lastSeaShipInspectionCount = lastWeekReports.stream().mapToLong(report -> report.getSeaShipInspectionCount() == null ? 0 : report.getSeaShipInspectionCount()).sum();
		long lastSeaShipDefectCount = lastWeekReports.stream().mapToLong(report -> report.getSeaShipDefectCount() == null ? 0 : report.getSeaShipDefectCount()).sum();
		long lastSeaShipDetentionCount = lastWeekReports.stream().mapToLong(report -> report.getSeaShipDetentionCount() == null ? 0 : report.getSeaShipDetentionCount()).sum();
		long lastRiverShipInspectionCount = lastWeekReports.stream().mapToLong(report -> report.getRiverShipInspectionCount() == null ? 0 : report.getRiverShipInspectionCount()).sum();
		long lastRiverShipDefectCount = lastWeekReports.stream().mapToLong(report -> report.getRiverShipDefectCount() == null ? 0 : report.getRiverShipDefectCount()).sum();
		long lastRiverShipDetentionCount = lastWeekReports.stream().mapToLong(report -> report.getRiverShipDetentionCount() == null ? 0 : report.getRiverShipDetentionCount()).sum();
		long lastPscInspectionCount = lastWeekReports.stream().mapToLong(report -> report.getPscInspectionCount() == null ? 0 : report.getPscInspectionCount()).sum();
		long lastPscDefectCount = lastWeekReports.stream().mapToLong(report -> report.getPscDefectCount() == null ? 0 : report.getPscDefectCount()).sum();
		long lastPscDetentionCount = lastWeekReports.stream().mapToLong(report -> report.getPscDetentionCount() == null ? 0 : report.getPscDetentionCount()).sum();
		long lastOnSiteCount = lastWeekReports.stream().mapToLong(report -> report.getOnSiteCount()== null ? 0 : report.getOnSiteCount()).sum();
		long lastOnSiteAbnormalCount = lastWeekReports.stream().mapToLong(report -> report.getOnSiteAbnormalCount()== null ? 0 : report.getOnSiteAbnormalCount()).sum();

		// 计算变化率
		double seaShipInspectionRate = calculateChangeRate(currentSeaShipInspectionCount, lastSeaShipInspectionCount);
		double seaShipDefectRate = calculateChangeRate(currentSeaShipDefectCount, lastSeaShipDefectCount);
		double seaShipDetentionRate = calculateChangeRate(currentSeaShipDetentionCount, lastSeaShipDetentionCount);
		double riverShipInspectionRate = calculateChangeRate(currentRiverShipInspectionCount, lastRiverShipInspectionCount);
		double riverShipDefectRate = calculateChangeRate(currentRiverShipDefectCount, lastRiverShipDefectCount);
		double riverShipDetentionRate = calculateChangeRate(currentRiverShipDetentionCount, lastRiverShipDetentionCount);
		double pscInspectionRate = calculateChangeRate(currentPscInspectionCount, lastPscInspectionCount);
		double pscDefectRate = calculateChangeRate(currentPscDefectCount, lastPscDefectCount);
		double pscDetentionRate = calculateChangeRate(currentPscDetentionCount, lastPscDetentionCount);
		double onSiteRate = calculateChangeRate(currentOnSiteCount,lastOnSiteCount);
		double onSiteAbnormalRate = calculateChangeRate(currentOnSiteAbnormalCount,lastOnSiteAbnormalCount);

		result.put("seaShipInspection", new HashMap<String,Object>(){{
			put("value",currentSeaShipInspectionCount);
			put("rate",seaShipInspectionRate);
		}});
		result.put("seaShipDefect", new HashMap<String,Object>(){{
			put("value",currentSeaShipDefectCount);
			put("rate",seaShipDefectRate);
		}});
		result.put("seaShipDetention", new HashMap<String,Object>(){{
			put("value",currentSeaShipDetentionCount);
			put("rate",seaShipDetentionRate);
		}});

		result.put("riverShipInspection",new HashMap<String,Object>(){{
			put("value",currentRiverShipInspectionCount);
			put("rate",riverShipInspectionRate);
		}});
		result.put("riverShipDefect",new HashMap<String,Object>(){{
			put("value",currentRiverShipDefectCount);
			put("rate",riverShipDefectRate);
		}});
		result.put("riverShipDetention",new HashMap<String,Object>(){{
			put("value",currentRiverShipDetentionCount);
			put("rate",riverShipDetentionRate);
		}});


		result.put("pscInspection",new HashMap<String,Object>(){{
			put("value",currentPscInspectionCount);
			put("rate",pscInspectionRate);
		}});
		result.put("pscDefect",new HashMap<String,Object>(){{
			put("value",currentPscDefectCount);
			put("rate",pscDefectRate);
		}});
		result.put("pscDetention",new HashMap<String,Object>(){{
			put("value",currentPscDetentionCount);
			put("rate",pscDetentionRate);
		}});
		result.put("onSiteCount",new HashMap<String,Object>(){{
			put("value",currentOnSiteCount);
			put("rate",onSiteRate);
		}});
		result.put("onSiteAbnormalCount",new HashMap<String,Object>(){{
			put("value",currentOnSiteAbnormalCount);
			put("rate",onSiteAbnormalRate);
		}});

		return result;
	}

	private double calculateChangeRate(long current, long last) {
		if (last == 0) {
			return 0; // 或者你可以返回其他表示没有变化的数值
		}
		return (double) Math.round(((double) (current - last) / last) * 10000) / 100;
	}

}