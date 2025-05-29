package com.jeesite.modules.data_collect.weekly.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
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
import com.jeesite.modules.data_collect.weekly.dao.WeeklyReportDao;
import com.jeesite.modules.data_collect.ship.entity.ShipInspection;
import com.jeesite.modules.data_collect.ship.service.ShipInspectionService;
import com.jeesite.modules.data_collect.psc.entity.PscInspection;
import com.jeesite.modules.data_collect.psc.service.PscInspectionService;
import com.jeesite.modules.data_collect.shiponsite.entity.ShipOnSiteInspection;
import com.jeesite.modules.data_collect.shiponsite.service.ShipOnSiteInspectionService;
import com.jeesite.modules.data_collect.ship.web.ShipInspectionController;
import com.jeesite.modules.data_collect.shiponsite.web.ShipOnSiteInspectionController;

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

	@Autowired
	private ShipInspectionService shipInspectionService;
	
	@Autowired
	private PscInspectionService pscInspectionService;
	
	@Autowired
	private ShipOnSiteInspectionService shipOnSiteInspectionService;
	
	@Autowired
	private WeeklyReportDao weeklyReportDao;
	
	@Autowired
	private ShipInspectionController shipInspectionController;
	
	@Autowired
	private ShipOnSiteInspectionController shipOnSiteInspectionController;

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
	 * 跳转到综合数据通报展示页面
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "dataDashboard")
	public String dataDashboard() {
		return "data_collect/weekly/weeklyDataDashboard";
	}
	
	/**
	 * 获取仪表盘综合统计数据
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "getDashboardStatistics")
	@ResponseBody
	public Map<String, Object> getDashboardStatistics() {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 获取当前日期
			LocalDate now = LocalDate.now();
			
			// 计算本周开始日期（星期一）
			LocalDate currentWeekStartDate = now.minusDays(now.getDayOfWeek().getValue() - 1);
			
			// 计算上周开始日期
			LocalDate lastWeekStartDate = currentWeekStartDate.minusWeeks(1);
			
			// 格式化日期为字符串
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String currentWeekStart = currentWeekStartDate.format(formatter);
			String lastWeekStart = lastWeekStartDate.format(formatter);
			
			// 获取本周和上周数据
			Map<String, Object> fetchData = getFetchData(currentWeekStart, lastWeekStart);
			
			// 组装返回数据
			Map<String, Object> data = new HashMap<>();
			
			// 处理结果数据
			if (fetchData.containsKey("currentWeekTotal")) {
				Map<String, Object> currentWeekTotal = (Map<String, Object>) fetchData.get("currentWeekTotal");
				
				// 添加行政处罚数据
				data.put("penaltyCount", currentWeekTotal.getOrDefault("administrativePenalty", 0));
				
				// 添加巡航次数数据
				data.put("patrolCount", currentWeekTotal.getOrDefault("patrolCount", 0));
				
				// 添加船舶检查数据
				long inspectionCount = 
					((Number) currentWeekTotal.getOrDefault("seaShipInspectionCount", 0)).longValue() + 
					((Number) currentWeekTotal.getOrDefault("inlandShipInspectionCount", 0)).longValue();
				data.put("inspectionCount", inspectionCount);
				
				// 添加危险品船舶数据
				data.put("hazardousCount", currentWeekTotal.getOrDefault("hazardousCargoCount", 0));
				
				// 添加证书办理量数据
				long certificateCount = 
					((Number) currentWeekTotal.getOrDefault("certificateIssuanceCount", 0)).longValue() + 
					((Number) currentWeekTotal.getOrDefault("endorsementCount", 0)).longValue();
				data.put("certificateCount", certificateCount);
				
				// 添加大型船舶进港数据
				data.put("largeShipCount", currentWeekTotal.getOrDefault("largeShipEntryCount", 0));
				
				// 添加数据更新时间
				data.put("lastUpdateTime", now.format(formatter) + " " + 
					java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
			}
			
			result.put("status", "success");
			result.put("data", data);
		} catch (Exception e) {
			logger.error("获取仪表盘统计数据失败", e);
			result.put("status", "error");
			result.put("message", "获取仪表盘统计数据失败: " + e.getMessage());
		}
		
		return result;
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
	 * 跳转到月度报表图表页面
	 * @return
	 */
	@RequestMapping(value = "monthlyChart")
	public String monthlyChart(){
		return "data_collect/weekly/monthlyReportChart";
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
		// 新增PSC和船舶现场监督检查数据
		Map<String,Object> pscInspectionCounts = new HashMap<>();
		Map<String,Object> pscDefectCounts = new HashMap<>();
		Map<String,Object> pscDetentionCounts = new HashMap<>();
		Map<String,Object> onSiteCounts = new HashMap<>();
		Map<String,Object> onSiteAbnormalCounts = new HashMap<>();


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

		// 新增PSC和船舶现场监督检查数据列表
		List<Object> currentPscInspectionCountsList = new ArrayList<>();
		List<Object> lastPscInspectionCountsList = new ArrayList<>();
		List<Object> currentPscDefectCountsList = new ArrayList<>();
		List<Object> lastPscDefectCountsList = new ArrayList<>();
		List<Object> currentPscDetentionCountsList = new ArrayList<>();
		List<Object> lastPscDetentionCountsList = new ArrayList<>();
		List<Object> currentOnSiteCountsList = new ArrayList<>();
		List<Object> lastOnSiteCountsList = new ArrayList<>();
		List<Object> currentOnSiteAbnormalCountsList = new ArrayList<>();
		List<Object> lastOnSiteAbnormalCountsList = new ArrayList<>();


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
			// 新增PSC和船舶现场监督检查数据
			long currentTotalPscInspectionCount = 0;
			long currentTotalPscDefectCount = 0;
			long currentTotalPscDetentionCount = 0;
			long currentTotalOnSiteCount = 0;
			long currentTotalOnSiteAbnormalCount = 0;

			for (WeeklyReport report : currentList){
				currentTotalInspectionCount += report.getSeaShipInspectionCount() == null ? 0 : report.getSeaShipInspectionCount();
				currentTotalDefectCount += report.getSeaShipDefectCount() == null ? 0 : report.getSeaShipDefectCount();
				currentTotalDetentionCount += report.getSeaShipDetentionCount() == null ? 0 : report.getSeaShipDetentionCount();
				currentTotalRiverInspectionCount += report.getRiverShipInspectionCount() == null ? 0 : report.getRiverShipInspectionCount();
				currentTotalRiverDefectCount += report.getRiverShipDefectCount() == null ? 0 : report.getRiverShipDefectCount();
				currentTotalRiverDetentionCount += report.getRiverShipDetentionCount() == null ? 0 : report.getRiverShipDetentionCount();
				// 新增PSC和船舶现场监督检查数据
				currentTotalPscInspectionCount += report.getPscInspectionCount() == null ? 0 : report.getPscInspectionCount();
				currentTotalPscDefectCount += report.getPscDefectCount() == null ? 0 : report.getPscDefectCount();
				currentTotalPscDetentionCount += report.getPscDetentionCount() == null ? 0 : report.getPscDetentionCount();
				currentTotalOnSiteCount += report.getOnSiteCount() == null ? 0 : report.getOnSiteCount();
				currentTotalOnSiteAbnormalCount += report.getOnSiteAbnormalCount() == null ? 0 : report.getOnSiteAbnormalCount();
			}


			//查找上周是否有数据
			long lastTotalInspectionCount = 0;
			long lastTotalDefectCount = 0;
			long lastTotalDetentionCount = 0;
			long lastTotalRiverInspectionCount = 0;
			long lastTotalRiverDefectCount = 0;
			long lastTotalRiverDetentionCount = 0;
			// 新增PSC和船舶现场监督检查数据
			long lastTotalPscInspectionCount = 0;
			long lastTotalPscDefectCount = 0;
			long lastTotalPscDetentionCount = 0;
			long lastTotalOnSiteCount = 0;
			long lastTotalOnSiteAbnormalCount = 0;

			if(groupedLastData.containsKey(departmentName)){
				List<WeeklyReport> lastList =  groupedLastData.get(departmentName);
				for (WeeklyReport report : lastList){
					lastTotalInspectionCount += report.getSeaShipInspectionCount() == null ? 0 : report.getSeaShipInspectionCount();
					lastTotalDefectCount += report.getSeaShipDefectCount() == null ? 0 : report.getSeaShipDefectCount();
					lastTotalDetentionCount += report.getSeaShipDetentionCount() == null ? 0 : report.getSeaShipDetentionCount();
					lastTotalRiverInspectionCount += report.getRiverShipInspectionCount() == null ? 0 : report.getRiverShipInspectionCount();
					lastTotalRiverDefectCount += report.getRiverShipDefectCount() == null ? 0 : report.getRiverShipDefectCount();
					lastTotalRiverDetentionCount += report.getRiverShipDetentionCount() == null ? 0 : report.getRiverShipDetentionCount();
					// 新增PSC和船舶现场监督检查数据
					lastTotalPscInspectionCount += report.getPscInspectionCount() == null ? 0 : report.getPscInspectionCount();
					lastTotalPscDefectCount += report.getPscDefectCount() == null ? 0 : report.getPscDefectCount();
					lastTotalPscDetentionCount += report.getPscDetentionCount() == null ? 0 : report.getPscDetentionCount();
					lastTotalOnSiteCount += report.getOnSiteCount() == null ? 0 : report.getOnSiteCount();
					lastTotalOnSiteAbnormalCount += report.getOnSiteAbnormalCount() == null ? 0 : report.getOnSiteAbnormalCount();
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

			// 新增PSC数据处理
			double pscInspectionChangeRate = 0;
			if(lastTotalPscInspectionCount !=0){
				pscInspectionChangeRate = (double)Math.round(( (double) (currentTotalPscInspectionCount - lastTotalPscInspectionCount)/ lastTotalPscInspectionCount) * 10000 ) / 100 ;
			}
			final long finalCurrentTotalPscInspectionCount = currentTotalPscInspectionCount;
			final double finalPscInspectionChangeRate = pscInspectionChangeRate;
			currentPscInspectionCountsList.add(new HashMap<String, Object>(){{
				put("value",finalCurrentTotalPscInspectionCount);
				put("changeRate",finalPscInspectionChangeRate);
			}});
			lastPscInspectionCountsList.add(lastTotalPscInspectionCount);

			double pscDefectChangeRate = 0;
			if(lastTotalPscDefectCount !=0){
				pscDefectChangeRate = (double)Math.round(( (double) (currentTotalPscDefectCount - lastTotalPscDefectCount)/ lastTotalPscDefectCount) * 10000 ) / 100 ;
			}
			final long finalCurrentTotalPscDefectCount = currentTotalPscDefectCount;
			final double finalPscDefectChangeRate = pscDefectChangeRate;
			currentPscDefectCountsList.add(new HashMap<String, Object>(){{
				put("value",finalCurrentTotalPscDefectCount);
				put("changeRate",finalPscDefectChangeRate);
			}});
			lastPscDefectCountsList.add(lastTotalPscDefectCount);

			double pscDetentionChangeRate = 0;
			if(lastTotalPscDetentionCount !=0){
				pscDetentionChangeRate = (double)Math.round(( (double) (currentTotalPscDetentionCount - lastTotalPscDetentionCount)/ lastTotalPscDetentionCount) * 10000 ) / 100 ;
			}
			final long finalCurrentTotalPscDetentionCount = currentTotalPscDetentionCount;
			final double finalPscDetentionChangeRate = pscDetentionChangeRate;
			currentPscDetentionCountsList.add(new HashMap<String, Object>(){{
				put("value",finalCurrentTotalPscDetentionCount);
				put("changeRate",finalPscDetentionChangeRate);
			}});
			lastPscDetentionCountsList.add(lastTotalPscDetentionCount);

			// 新增船舶现场监督数据处理
			double onSiteChangeRate = 0;
			if(lastTotalOnSiteCount !=0){
				onSiteChangeRate = (double)Math.round(( (double) (currentTotalOnSiteCount - lastTotalOnSiteCount)/ lastTotalOnSiteCount) * 10000 ) / 100 ;
			}
			final long finalCurrentTotalOnSiteCount = currentTotalOnSiteCount;
			final double finalOnSiteChangeRate = onSiteChangeRate;
			currentOnSiteCountsList.add(new HashMap<String, Object>(){{
				put("value",finalCurrentTotalOnSiteCount);
				put("changeRate",finalOnSiteChangeRate);
			}});
			lastOnSiteCountsList.add(lastTotalOnSiteCount);

			double onSiteAbnormalChangeRate = 0;
			if(lastTotalOnSiteAbnormalCount !=0){
				onSiteAbnormalChangeRate = (double)Math.round(( (double) (currentTotalOnSiteAbnormalCount - lastTotalOnSiteAbnormalCount)/ lastTotalOnSiteAbnormalCount) * 10000 ) / 100 ;
			}
			final long finalCurrentTotalOnSiteAbnormalCount = currentTotalOnSiteAbnormalCount;
			final double finalOnSiteAbnormalChangeRate = onSiteAbnormalChangeRate;
			currentOnSiteAbnormalCountsList.add(new HashMap<String, Object>(){{
				put("value",finalCurrentTotalOnSiteAbnormalCount);
				put("changeRate",finalOnSiteAbnormalChangeRate);
			}});
			lastOnSiteAbnormalCountsList.add(lastTotalOnSiteAbnormalCount);
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

		// 添加PSC和船舶现场监督检查数据到结果集
		pscInspectionCounts.put("current",currentPscInspectionCountsList);
		pscInspectionCounts.put("last",lastPscInspectionCountsList);
		pscDefectCounts.put("current",currentPscDefectCountsList);
		pscDefectCounts.put("last",lastPscDefectCountsList);
		pscDetentionCounts.put("current",currentPscDetentionCountsList);
		pscDetentionCounts.put("last",lastPscDetentionCountsList);
		onSiteCounts.put("current",currentOnSiteCountsList);
		onSiteCounts.put("last",lastOnSiteCountsList);
		onSiteAbnormalCounts.put("current",currentOnSiteAbnormalCountsList);
		onSiteAbnormalCounts.put("last",lastOnSiteAbnormalCountsList);


		Map<String, Object> result = new HashMap<>();
		result.put("categories", categories);
		result.put("seaShipInspectionCounts", seaShipInspectionCounts);
		result.put("seaShipDefectCounts", seaShipDefectCounts);
		result.put("seaShipDetentionCounts", seaShipDetentionCounts);

		result.put("riverShipInspectionCounts", riverShipInspectionCounts);
		result.put("riverShipDefectCounts", riverShipDefectCounts);
		result.put("riverShipDetentionCounts", riverShipDetentionCounts);

		// 添加PSC和船舶现场监督检查数据到结果中
		result.put("pscInspectionCounts", pscInspectionCounts);
		result.put("pscDefectCounts", pscDefectCounts);
		result.put("pscDetentionCounts", pscDetentionCounts);
		result.put("onSiteCounts", onSiteCounts);
		result.put("onSiteAbnormalCounts", onSiteAbnormalCounts);

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

	/**
	 * 从原始数据表获取统计数据 - 用于周数据看板，按部门分组
	 */
	@GetMapping("getOriginalDataStatistics")
	@ResponseBody
	public Map<String, Object> getOriginalDataStatistics(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 获取当前周期的各类型数据（限制为张家港海事局的四家机构）
			Map<String, Object> shipData = getShipInspectionStatisticsForZhangjiagang(startDate, endDate);
			Map<String, Object> pscData = getPscInspectionStatistics(startDate, endDate);
			Map<String, Object> onSiteData = getOnSiteInspectionStatisticsForZhangjiagang(startDate, endDate);
			
			// 计算上一周期的日期范围
			Date startDateObj = startDate != null ? DateUtils.parseDate(startDate) : null;
			Date endDateObj = endDate != null ? DateUtils.parseDate(endDate) : null;
			
			if (startDateObj != null && endDateObj != null) {
				// 计算上一周期的日期范围（向前推7天）
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDateObj);
				cal.add(Calendar.DAY_OF_MONTH, -7);
				Date prevStartDate = cal.getTime();
				
				cal.setTime(endDateObj);
				cal.add(Calendar.DAY_OF_MONTH, -7);
				Date prevEndDate = cal.getTime();
				
				String prevStartDateStr = DateUtils.formatDate(prevStartDate);
				String prevEndDateStr = DateUtils.formatDate(prevEndDate);
				
				// 获取上一周期的数据
				Map<String, Object> prevShipData = getShipInspectionStatisticsForZhangjiagang(prevStartDateStr, prevEndDateStr);
				Map<String, Object> prevPscData = getPscInspectionStatistics(prevStartDateStr, prevEndDateStr);
				Map<String, Object> prevOnSiteData = getOnSiteInspectionStatisticsForZhangjiagang(prevStartDateStr, prevEndDateStr);
				
				// 计算环比变化率并构建部门数据列表
				List<Map<String, Object>> agencyDataList = buildDepartmentDataListForZhangjiagang(shipData, onSiteData, prevShipData, prevOnSiteData);
				result.put("agencyDataList", agencyDataList);
				
				// 添加上期数据到结果中
				result.put("prevShipData", prevShipData);
				result.put("prevPscData", prevPscData);
				result.put("prevOnSiteData", prevOnSiteData);
				
				// 计算总体环比变化率
				calculateChangeRates(result, shipData, pscData, onSiteData, prevShipData, prevPscData, prevOnSiteData);
			}
			
			// 合并所有数据到结果中
			result.putAll(shipData);
			result.putAll(pscData);
			result.putAll(onSiteData);
			
			result.put("status", "success");
			
		} catch (Exception e) {
			logger.error("获取原始数据统计失败", e);
			result.put("status", "error");
			result.put("message", "获取数据失败: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * 获取船舶安全检查统计数据
	 */
	private Map<String, Object> getShipInspectionStatistics(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date startDateObj = startDate != null ? DateUtils.parseDate(startDate) : null;
			Date endDateObj = endDate != null ? DateUtils.parseDate(endDate) : null;
			
			// 创建查询条件 - 海船安检
			ShipInspection seaShipQuery = new ShipInspection();
			seaShipQuery.setInspectionType("初查");
			seaShipQuery.setShipType("海船");
			if (startDateObj != null) {
				seaShipQuery.setInspectionDate_gte(startDateObj);
			}
			if (endDateObj != null) {
				seaShipQuery.setInspectionDate_lte(endDateObj);
			}
			
			// 创建查询条件 - 内河船安检
			ShipInspection riverShipQuery = new ShipInspection();
			riverShipQuery.setInspectionType("初查");
			riverShipQuery.setShipType("内河船");
			if (startDateObj != null) {
				riverShipQuery.setInspectionDate_gte(startDateObj);
			}
			if (endDateObj != null) {
				riverShipQuery.setInspectionDate_lte(endDateObj);
			}
			
			// 执行查询
			List<ShipInspection> seaShipList = shipInspectionService.findList(seaShipQuery);
			List<ShipInspection> riverShipList = shipInspectionService.findList(riverShipQuery);
			
			// 去重统计 - 海船安检
			Map<String, List<ShipInspection>> seaShipMap = seaShipList.stream()
				.collect(Collectors.groupingBy(ship -> 
					ship.getShipNameCn() + "_" + 
					DateUtils.formatDate(ship.getInspectionDate(), "yyyy-MM-dd") + "_" + 
					ship.getInspectionAgency()
				));
			
			// 去重统计 - 内河船安检
			Map<String, List<ShipInspection>> riverShipMap = riverShipList.stream()
				.collect(Collectors.groupingBy(ship ->
					ship.getShipNameCn() + "_" + 
					DateUtils.formatDate(ship.getInspectionDate(), "yyyy-MM-dd") + "_" + 
					ship.getInspectionAgency()
				));
			
			// 统计结果
			long seaShipCount = seaShipMap.size();
			long riverShipCount = riverShipMap.size();
			
			// 统计缺陷
			long seaShipDefectCount = seaShipMap.values().stream()
				.mapToLong(list -> {
					// 只取每组中的第一条记录的缺陷数量，避免重复计算
					if (!list.isEmpty()) {
						ShipInspection ship = list.get(0);
						return ship.getDefectCount() != null ? ship.getDefectCount() : 0;
					}
					return 0;
				})
				.sum();
			
			long riverShipDefectCount = riverShipMap.values().stream()
				.mapToLong(list -> {
					// 只取每组中的第一条记录的缺陷数量，避免重复计算
					if (!list.isEmpty()) {
						ShipInspection ship = list.get(0);
						return ship.getDefectCount() != null ? ship.getDefectCount() : 0;
					}
					return 0;
				})
				.sum();
			
			// 统计滞留
			long seaShipDetainedCount = seaShipMap.values().stream()
				.filter(list -> list.stream()
					.anyMatch(ship -> "是".equals(ship.getDetained())))
				.count();
			
			long riverShipDetainedCount = riverShipMap.values().stream()
				.filter(list -> list.stream()
					.anyMatch(ship -> "是".equals(ship.getDetained())))
				.count();
			
			// 按机构统计
			Map<String, Long> agencySeaShipCounts = new HashMap<>();
			for (List<ShipInspection> shipList : seaShipMap.values()) {
				if (!shipList.isEmpty()) {
					String agency = shipList.get(0).getInspectionAgency();
					agencySeaShipCounts.put(agency, agencySeaShipCounts.getOrDefault(agency, 0L) + 1);
				}
			}
			
			Map<String, Long> agencyRiverShipCounts = new HashMap<>();
			for (List<ShipInspection> shipList : riverShipMap.values()) {
				if (!shipList.isEmpty()) {
					String agency = shipList.get(0).getInspectionAgency();
					agencyRiverShipCounts.put(agency, agencyRiverShipCounts.getOrDefault(agency, 0L) + 1);
				}
			}
			
			// 添加按机构统计的缺陷数和滞留数
			Map<String, Long> agencySeaShipDefectCounts = new HashMap<>();
			Map<String, Long> agencySeaShipDetainedCounts = new HashMap<>();
			Map<String, Long> agencyRiverShipDefectCounts = new HashMap<>();
			Map<String, Long> agencyRiverShipDetainedCounts = new HashMap<>();
			
			// 海船按机构统计缺陷和滞留
			for (Map.Entry<String, List<ShipInspection>> entry : seaShipMap.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					String agency = entry.getValue().get(0).getInspectionAgency();
					
					// 统计缺陷数 - 只取第一条记录的缺陷数量
					ShipInspection ship = entry.getValue().get(0);
					long defectCount = ship.getDefectCount() != null ? ship.getDefectCount() : 0;
					agencySeaShipDefectCounts.put(agency, agencySeaShipDefectCounts.getOrDefault(agency, 0L) + defectCount);
					
					// 统计滞留数
					boolean hasDetained = entry.getValue().stream()
						.anyMatch(s -> "是".equals(s.getDetained()));
					if (hasDetained) {
						agencySeaShipDetainedCounts.put(agency, agencySeaShipDetainedCounts.getOrDefault(agency, 0L) + 1);
					}
				}
			}
			
			// 内河船按机构统计缺陷和滞留
			for (Map.Entry<String, List<ShipInspection>> entry : riverShipMap.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					String agency = entry.getValue().get(0).getInspectionAgency();
					
					// 统计缺陷数 - 只取第一条记录的缺陷数量
					ShipInspection ship = entry.getValue().get(0);
					long defectCount = ship.getDefectCount() != null ? ship.getDefectCount() : 0;
					agencyRiverShipDefectCounts.put(agency, agencyRiverShipDefectCounts.getOrDefault(agency, 0L) + defectCount);
					
					// 统计滞留数
					boolean hasDetained = entry.getValue().stream()
						.anyMatch(s -> "是".equals(s.getDetained()));
					if (hasDetained) {
						agencyRiverShipDetainedCounts.put(agency, agencyRiverShipDetainedCounts.getOrDefault(agency, 0L) + 1);
					}
				}
			}
			
			// 组装返回数据
			result.put("seaShipCount", seaShipCount);
			result.put("riverShipCount", riverShipCount);
			result.put("seaShipDefectCount", seaShipDefectCount);
			result.put("riverShipDefectCount", riverShipDefectCount);
			result.put("seaShipDetainedCount", seaShipDetainedCount);
			result.put("riverShipDetainedCount", riverShipDetainedCount);
			result.put("agencySeaShipCounts", agencySeaShipCounts);
			result.put("agencyRiverShipCounts", agencyRiverShipCounts);
			// 添加新的返回数据
			result.put("agencySeaShipDefectCounts", agencySeaShipDefectCounts);
			result.put("agencySeaShipDetainedCounts", agencySeaShipDetainedCounts);
			result.put("agencyRiverShipDefectCounts", agencyRiverShipDefectCounts);
			result.put("agencyRiverShipDetainedCounts", agencyRiverShipDetainedCounts);
		} catch (Exception e) {
			logger.error("获取船舶检查统计数据失败", e);
		}
		
		return result;
	}
	
	/**
	 * 获取PSC检查统计数据
	 */
	private Map<String, Object> getPscInspectionStatistics(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date startDateObj = startDate != null ? DateUtils.parseDate(startDate) : null;
			Date endDateObj = endDate != null ? DateUtils.parseDate(endDate) : null;
			
			// 创建查询条件
			PscInspection pscQuery = new PscInspection();
			pscQuery.setType("INITIAL");
			pscQuery.setPort("Zhangjiagang");
			if (startDateObj != null) {
				pscQuery.setInspectionDate_gte(startDateObj);
			}
			if (endDateObj != null) {
				pscQuery.setInspectionDate_lte(endDateObj);
			}
			
			// 执行查询
			List<PscInspection> pscList = pscInspectionService.findList(pscQuery);
			
			// 统计PSC检查数量
			long pscCount = pscList.size();
			
			// 统计PSC缺陷数量
			long pscDefectCount = pscList.stream()
				.mapToLong(psc -> {
					if (psc.getDeficiencies() != null && !psc.getDeficiencies().isEmpty()) {
						try {
							return Long.parseLong(psc.getDeficiencies());
						} catch (NumberFormatException e) {
							return 0;
						}
					}
					return 0;
				})
				.sum();
			
			// 统计PSC滞留数量
			long pscDetentionCount = pscList.stream()
				.filter(psc -> "Y".equals(psc.getDetention()))
				.count();
			
			// 组装返回数据
			result.put("pscCount", pscCount);
			result.put("pscDefectCount", pscDefectCount);
			result.put("pscDetentionCount", pscDetentionCount);
		} catch (Exception e) {
			logger.error("获取PSC检查统计数据失败", e);
		}
		
		return result;
	}
	
	/**
	 * 获取船舶现场监督检查统计数据
	 */
	private Map<String, Object> getOnSiteInspectionStatistics(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date startDateObj = startDate != null ? DateUtils.parseDate(startDate) : null;
			Date endDateObj = endDate != null ? DateUtils.parseDate(endDate) : null;
			
			// 创建查询条件
			ShipOnSiteInspection onSiteQuery = new ShipOnSiteInspection();
			onSiteQuery.setInitialOrRecheck("初查");
			if (startDateObj != null) {
				onSiteQuery.setInspectionDate_gte(startDateObj);
			}
			if (endDateObj != null) {
				onSiteQuery.setInspectionDate_lte(endDateObj);
			}
			
			// 执行查询
			List<ShipOnSiteInspection> onSiteList = shipOnSiteInspectionService.findList(onSiteQuery);
			
			// 去重统计
			Map<String, List<ShipOnSiteInspection>> onSiteMap = onSiteList.stream()
				.collect(Collectors.groupingBy(inspection -> 
					inspection.getShipNameCn() + "_" + 
					DateUtils.formatDate(inspection.getInspectionDate(), "yyyy-MM-dd") + "_" + 
					inspection.getInspectionAgency()
				));
			
			// 统计监督数量
			long onSiteCount = onSiteMap.size();
			
			// 统计异常数量 - 使用issueFound字段判断是否发现问题
			long abnormalCount = onSiteList.stream()
				.filter(inspection -> "是".equals(inspection.getIssueFound()))
				.count();
			
			// 按机构统计
			Map<String, Long> agencyOnSiteCounts = new HashMap<>();
			Map<String, Long> agencyAbnormalCounts = new HashMap<>();
			
			for (List<ShipOnSiteInspection> inspectionList : onSiteMap.values()) {
				if (!inspectionList.isEmpty()) {
					ShipOnSiteInspection inspection = inspectionList.get(0);
					String agency = inspection.getInspectionAgency();
					agencyOnSiteCounts.put(agency, agencyOnSiteCounts.getOrDefault(agency, 0L) + 1);
					
					boolean hasIssue = inspectionList.stream()
						.anyMatch(insp -> "是".equals(insp.getIssueFound()));
					
					if (hasIssue) {
						agencyAbnormalCounts.put(agency, agencyAbnormalCounts.getOrDefault(agency, 0L) + 1);
					}
				}
			}
			
			// 组装返回数据
			result.put("onSiteCount", onSiteCount);
			result.put("abnormalCount", abnormalCount);
			result.put("agencyOnSiteCounts", agencyOnSiteCounts);
			result.put("agencyAbnormalCounts", agencyAbnormalCounts);
		} catch (Exception e) {
			logger.error("获取船舶现场监督检查统计数据失败", e);
		}
		
		return result;
	}

	/**
	 * 从原始数据表获取统计数据 - 用于月度数据看板，支持部门筛选
	 */
	@GetMapping("getMonthlyDataStatistics")
	@ResponseBody
	public Map<String, Object> getMonthlyDataStatistics(String startDate, String endDate, String department) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 获取当前周期的各类型数据（使用新的DAO方法按部门分组）
			Map<String, Object> shipData = getShipInspectionStatisticsByDept(startDate, endDate, department);
			Map<String, Object> pscData = getPscInspectionStatisticsByDept(startDate, endDate, department);
			Map<String, Object> onSiteData = getOnSiteInspectionStatisticsByDept(startDate, endDate, department);
			
			// 计算上一周期的日期范围
			Date startDateObj = startDate != null ? DateUtils.parseDate(startDate) : null;
			Date endDateObj = endDate != null ? DateUtils.parseDate(endDate) : null;
			
			Map<String, Object> prevShipData = new HashMap<>();
			Map<String, Object> prevPscData = new HashMap<>();
			Map<String, Object> prevOnSiteData = new HashMap<>();
			
			if (startDateObj != null && endDateObj != null) {
				long daysDiff = (endDateObj.getTime() - startDateObj.getTime()) / (1000 * 60 * 60 * 24);
				daysDiff++; // 包含起始日期
				
				// 计算上一周期的起止日期
				Date prevStartDateObj = DateUtils.addDays(startDateObj, (int)-daysDiff);
				Date prevEndDateObj = DateUtils.addDays(endDateObj, (int)-daysDiff);
				
				String prevStartDate = DateUtils.formatDate(prevStartDateObj);
				String prevEndDate = DateUtils.formatDate(prevEndDateObj);
				
				// 获取上一周期的各类型数据（使用新的DAO方法按部门分组）
				prevShipData = getShipInspectionStatisticsByDept(prevStartDate, prevEndDate, department);
				prevPscData = getPscInspectionStatisticsByDept(prevStartDate, prevEndDate, department);
				prevOnSiteData = getOnSiteInspectionStatisticsByDept(prevStartDate, prevEndDate, department);
				
				// 计算环比变化率
				long seaShipCount = (long) shipData.getOrDefault("seaShipCount", 0L);
				long prevSeaShipCount = (long) prevShipData.getOrDefault("seaShipCount", 0L);
				double seaShipCountRate = calculateChangeRate(seaShipCount, prevSeaShipCount);
				result.put("seaShipCountRate", seaShipCountRate);
				
				long seaShipDefectCount = (long) shipData.getOrDefault("seaShipDefectCount", 0L);
				long prevSeaShipDefectCount = (long) prevShipData.getOrDefault("seaShipDefectCount", 0L);
				double seaShipDefectRate = calculateChangeRate(seaShipDefectCount, prevSeaShipDefectCount);
				result.put("seaShipDefectRate", seaShipDefectRate);
				
				long seaShipDetainedCount = (long) shipData.getOrDefault("seaShipDetainedCount", 0L);
				long prevSeaShipDetainedCount = (long) prevShipData.getOrDefault("seaShipDetainedCount", 0L);
				double seaShipDetainedRate = calculateChangeRate(seaShipDetainedCount, prevSeaShipDetainedCount);
				result.put("seaShipDetainedRate", seaShipDetainedRate);
				
				long riverShipCount = (long) shipData.getOrDefault("riverShipCount", 0L);
				long prevRiverShipCount = (long) prevShipData.getOrDefault("riverShipCount", 0L);
				double riverShipCountRate = calculateChangeRate(riverShipCount, prevRiverShipCount);
				result.put("riverShipCountRate", riverShipCountRate);
				
				long riverShipDefectCount = (long) shipData.getOrDefault("riverShipDefectCount", 0L);
				long prevRiverShipDefectCount = (long) prevShipData.getOrDefault("riverShipDefectCount", 0L);
				double riverShipDefectRate = calculateChangeRate(riverShipDefectCount, prevRiverShipDefectCount);
				result.put("riverShipDefectRate", riverShipDefectRate);
				
				long riverShipDetainedCount = (long) shipData.getOrDefault("riverShipDetainedCount", 0L);
				long prevRiverShipDetainedCount = (long) prevShipData.getOrDefault("riverShipDetainedCount", 0L);
				double riverShipDetainedRate = calculateChangeRate(riverShipDetainedCount, prevRiverShipDetainedCount);
				result.put("riverShipDetainedRate", riverShipDetainedRate);
				
				long pscCount = (long) pscData.getOrDefault("pscCount", 0L);
				long prevPscCount = (long) prevPscData.getOrDefault("pscCount", 0L);
				double pscCountRate = calculateChangeRate(pscCount, prevPscCount);
				result.put("pscCountRate", pscCountRate);
				
				long pscDefectCount = (long) pscData.getOrDefault("pscDefectCount", 0L);
				long prevPscDefectCount = (long) prevPscData.getOrDefault("pscDefectCount", 0L);
				double pscDefectRate = calculateChangeRate(pscDefectCount, prevPscDefectCount);
				result.put("pscDefectRate", pscDefectRate);
				
				long pscDetentionCount = (long) pscData.getOrDefault("pscDetentionCount", 0L);
				long prevPscDetentionCount = (long) prevPscData.getOrDefault("pscDetentionCount", 0L);
				double pscDetentionRate = calculateChangeRate(pscDetentionCount, prevPscDetentionCount);
				result.put("pscDetentionRate", pscDetentionRate);
				
				long onSiteCount = (long) onSiteData.getOrDefault("onSiteCount", 0L);
				long prevOnSiteCount = (long) prevOnSiteData.getOrDefault("onSiteCount", 0L);
				double onSiteCountRate = calculateChangeRate(onSiteCount, prevOnSiteCount);
				result.put("onSiteCountRate", onSiteCountRate);
				
				long abnormalCount = (long) onSiteData.getOrDefault("abnormalCount", 0L);
				long prevAbnormalCount = (long) prevOnSiteData.getOrDefault("abnormalCount", 0L);
				double abnormalCountRate = calculateChangeRate(abnormalCount, prevAbnormalCount);
				result.put("abnormalCountRate", abnormalCountRate);
			}
			
			// 合并当前数据到结果中
			result.putAll(shipData);
			result.putAll(pscData);
			result.putAll(onSiteData);
			
			// 构建按部门分组的数据列表
			List<Map<String, Object>> deptDataList = buildMonthlyDepartmentDataList(shipData, onSiteData, pscData, prevShipData, prevOnSiteData, prevPscData);
			result.put("deptDataList", deptDataList);
			
			// 获取部门列表（使用新的DAO方法）
			List<String> departments = weeklyReportDao.findAllDepartments();
			result.put("departments", departments);
			
			// 获取机构列表（从shipData中获取）
			if (shipData.containsKey("agencyDeptList")) {
				result.put("agencyDeptList", shipData.get("agencyDeptList"));
			}
			
			result.put("status", "success");
		} catch (Exception e) {
			logger.error("获取月度统计数据失败", e);
			result.put("status", "error");
			result.put("message", "获取月度统计数据失败: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * 获取船舶检查统计数据 - 支持部门筛选
	 */
	private Map<String, Object> getShipInspectionStatisticsWithDept(String startDate, String endDate, String department) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date startDateObj = startDate != null ? DateUtils.parseDate(startDate) : null;
			Date endDateObj = endDate != null ? DateUtils.parseDate(endDate) : null;
			
			// 创建查询条件 - 海船安检
			ShipInspection seaShipQuery = new ShipInspection();
			seaShipQuery.setInspectionType("初查");
			seaShipQuery.setShipType("海船");
			if (startDateObj != null) {
				seaShipQuery.setInspectionDate_gte(startDateObj);
			}
			if (endDateObj != null) {
				seaShipQuery.setInspectionDate_lte(endDateObj);
			}
			
			// 创建查询条件 - 内河船安检
			ShipInspection riverShipQuery = new ShipInspection();
			riverShipQuery.setInspectionType("初查");
			riverShipQuery.setShipType("内河船");
			if (startDateObj != null) {
				riverShipQuery.setInspectionDate_gte(startDateObj);
			}
			if (endDateObj != null) {
				riverShipQuery.setInspectionDate_lte(endDateObj);
			}
			
			// 执行查询
			List<ShipInspection> seaShipList = shipInspectionService.findList(seaShipQuery);
			List<ShipInspection> riverShipList = shipInspectionService.findList(riverShipQuery);
			
			// 如果指定了部门，则通过agency_dept表进行筛选
			if (com.jeesite.common.lang.StringUtils.isNotBlank(department)) {
				seaShipList = seaShipList.stream()
					.filter(ship -> isDepartmentMatch(ship.getInspectionAgency(), department))
					.collect(Collectors.toList());
				riverShipList = riverShipList.stream()
					.filter(ship -> isDepartmentMatch(ship.getInspectionAgency(), department))
					.collect(Collectors.toList());
			}
			
			// 去重统计 - 海船安检
			Map<String, List<ShipInspection>> seaShipMap = seaShipList.stream()
				.collect(Collectors.groupingBy(ship -> 
					ship.getShipNameCn() + "_" + 
					DateUtils.formatDate(ship.getInspectionDate(), "yyyy-MM-dd") + "_" + 
					ship.getInspectionAgency()
				));
			
			// 去重统计 - 内河船安检
			Map<String, List<ShipInspection>> riverShipMap = riverShipList.stream()
				.collect(Collectors.groupingBy(ship ->
					ship.getShipNameCn() + "_" + 
					DateUtils.formatDate(ship.getInspectionDate(), "yyyy-MM-dd") + "_" + 
					ship.getInspectionAgency()
				));
			
			// 统计结果
			long seaShipCount = seaShipMap.size();
			long riverShipCount = riverShipMap.size();
			
			// 统计缺陷
			long seaShipDefectCount = seaShipMap.values().stream()
				.mapToLong(list -> {
					if (!list.isEmpty()) {
						ShipInspection ship = list.get(0);
						return ship.getDefectCount() != null ? ship.getDefectCount() : 0;
					}
					return 0;
				})
				.sum();
			
			long riverShipDefectCount = riverShipMap.values().stream()
				.mapToLong(list -> {
					if (!list.isEmpty()) {
						ShipInspection ship = list.get(0);
						return ship.getDefectCount() != null ? ship.getDefectCount() : 0;
					}
					return 0;
				})
				.sum();
			
			// 统计滞留
			long seaShipDetainedCount = seaShipMap.values().stream()
				.filter(list -> list.stream()
					.anyMatch(ship -> "是".equals(ship.getDetained())))
				.count();
			
			long riverShipDetainedCount = riverShipMap.values().stream()
				.filter(list -> list.stream()
					.anyMatch(ship -> "是".equals(ship.getDetained())))
				.count();
			
			// 组装返回数据
			result.put("seaShipCount", seaShipCount);
			result.put("riverShipCount", riverShipCount);
			result.put("seaShipDefectCount", seaShipDefectCount);
			result.put("riverShipDefectCount", riverShipDefectCount);
			result.put("seaShipDetainedCount", seaShipDetainedCount);
			result.put("riverShipDetainedCount", riverShipDetainedCount);
		} catch (Exception e) {
			logger.error("获取船舶检查统计数据失败", e);
		}
		
		return result;
	}
	
	/**
	 * 获取现场监督统计数据 - 支持部门筛选
	 */
	private Map<String, Object> getOnSiteInspectionStatisticsWithDept(String startDate, String endDate, String department) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date startDateObj = startDate != null ? DateUtils.parseDate(startDate) : null;
			Date endDateObj = endDate != null ? DateUtils.parseDate(endDate) : null;
			
			// 创建查询条件
			ShipOnSiteInspection onSiteQuery = new ShipOnSiteInspection();
			if (startDateObj != null) {
				onSiteQuery.setInspectionDate_gte(startDateObj);
			}
			if (endDateObj != null) {
				onSiteQuery.setInspectionDate_lte(endDateObj);
			}
			
			// 执行查询
			List<ShipOnSiteInspection> onSiteList = shipOnSiteInspectionService.findList(onSiteQuery);
			
			// 如果指定了部门，则通过agency_dept表进行筛选
			if (com.jeesite.common.lang.StringUtils.isNotBlank(department)) {
				onSiteList = onSiteList.stream()
					.filter(inspection -> isDepartmentMatch(inspection.getInspectionAgency(), department))
					.collect(Collectors.toList());
			}
			
			// 统计现场监督数量
			long onSiteCount = onSiteList.size();
			
			// 统计异常数量 - 使用issueFound字段判断是否发现问题
			long abnormalCount = onSiteList.stream()
				.filter(inspection -> "是".equals(inspection.getIssueFound()))
				.count();
			
			// 组装返回数据
			result.put("onSiteCount", onSiteCount);
			result.put("abnormalCount", abnormalCount);
		} catch (Exception e) {
			logger.error("获取现场监督统计数据失败", e);
		}
		
		return result;
	}
	
	/**
	 * 检查机构是否属于指定部门
	 */
	private boolean isDepartmentMatch(String agency, String department) {
		// 这里需要查询agency_dept表来判断
		// 简化实现：如果部门是"张家港海事局"，则只返回张家港相关的数据
		if ("张家港海事局".equals(department)) {
			return agency != null && agency.contains("张家港");
		}
		// 其他部门的匹配逻辑可以在这里添加
		return true;
	}
	

	/**
	 * 获取船舶检查统计数据 - 按部门分组（使用DAO方法）
	 */
	private Map<String, Object> getShipInspectionStatisticsByDept(String startDate, String endDate, String department) {
		// 直接调用ShipInspectionController的新方法
		return shipInspectionController.getInspectionStatisticsByDepartment(startDate, endDate, department);
	}
	
	/**
	 * 获取现场监督统计数据 - 按部门分组（使用DAO方法）
	 */
	private Map<String, Object> getOnSiteInspectionStatisticsByDept(String startDate, String endDate, String department) {
		// 直接调用ShipOnSiteInspectionController的新方法
		return shipOnSiteInspectionController.getOnSiteInspectionStatisticsByDepartment(startDate, endDate, department);
	}
	
	/**
	 * 获取PSC检查统计数据 - 按部门分组（使用DAO方法）
	 */
	private Map<String, Object> getPscInspectionStatisticsByDept(String startDate, String endDate, String department) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 使用DAO方法获取按部门分组的PSC统计数据
			List<Map<String, Object>> pscStatsList = weeklyReportDao.findPscInspectionStatisticsByDepartment(startDate, endDate, department);
			
			// 初始化总计数据
			long totalPscCount = 0L;
			long totalPscDefectCount = 0L;
			long totalPscDetentionCount = 0L;
			
			// 按部门统计数据
			Map<String, Long> agencyPscCounts = new HashMap<>();
			Map<String, Long> agencyPscDefectCounts = new HashMap<>();
			Map<String, Long> agencyPscDetentionCounts = new HashMap<>();
			
			// 处理查询结果
			for (Map<String, Object> pscStats : pscStatsList) {
				String dept = (String) pscStats.get("department");
				Long pscCount = ((Number) pscStats.get("pscCount")).longValue();
				Long pscDefectCount = ((Number) pscStats.get("pscDefectCount")).longValue();
				Long pscDetentionCount = ((Number) pscStats.get("pscDetentionCount")).longValue();
				
				// 累计总数
				totalPscCount += pscCount;
				totalPscDefectCount += pscDefectCount;
				totalPscDetentionCount += pscDetentionCount;
				
				// 按部门统计
				agencyPscCounts.put(dept, pscCount);
				agencyPscDefectCounts.put(dept, pscDefectCount);
				agencyPscDetentionCounts.put(dept, pscDetentionCount);
			}
			
			// 组装返回数据
			result.put("pscCount", totalPscCount);
			result.put("pscDefectCount", totalPscDefectCount);
			result.put("pscDetentionCount", totalPscDetentionCount);
			result.put("agencyPscCounts", agencyPscCounts);
			result.put("agencyPscDefectCounts", agencyPscDefectCounts);
			result.put("agencyPscDetentionCounts", agencyPscDetentionCounts);
			
		} catch (Exception e) {
			logger.error("获取PSC检查统计数据失败", e);
		}
		
		return result;
	}
	
	/**
	 * 计算环比变化率
	 */
	private void calculateChangeRates(Map<String, Object> result, 
			Map<String, Object> shipData, Map<String, Object> prevShipData,
			Map<String, Object> pscData, Map<String, Object> prevPscData,
			Map<String, Object> onSiteData, Map<String, Object> prevOnSiteData) {
				
				// 计算海船安检环比变化
				long seaShipCount = (long) shipData.getOrDefault("seaShipCount", 0L);
				long prevSeaShipCount = (long) prevShipData.getOrDefault("seaShipCount", 0L);
				double seaShipCountRate = calculateChangeRate(seaShipCount, prevSeaShipCount);
				result.put("seaShipCountRate", seaShipCountRate);
				
				// 计算海船缺陷环比变化
				long seaShipDefectCount = (long) shipData.getOrDefault("seaShipDefectCount", 0L);
				long prevSeaShipDefectCount = (long) prevShipData.getOrDefault("seaShipDefectCount", 0L);
				double seaShipDefectRate = calculateChangeRate(seaShipDefectCount, prevSeaShipDefectCount);
				result.put("seaShipDefectRate", seaShipDefectRate);
				
				// 计算海船滞留环比变化
				long seaShipDetainedCount = (long) shipData.getOrDefault("seaShipDetainedCount", 0L);
				long prevSeaShipDetainedCount = (long) prevShipData.getOrDefault("seaShipDetainedCount", 0L);
				double seaShipDetainedRate = calculateChangeRate(seaShipDetainedCount, prevSeaShipDetainedCount);
				result.put("seaShipDetainedRate", seaShipDetainedRate);
				
				// 计算内河船安检环比变化
				long riverShipCount = (long) shipData.getOrDefault("riverShipCount", 0L);
				long prevRiverShipCount = (long) prevShipData.getOrDefault("riverShipCount", 0L);
				double riverShipCountRate = calculateChangeRate(riverShipCount, prevRiverShipCount);
				result.put("riverShipCountRate", riverShipCountRate);
				
				// 计算内河船缺陷环比变化
				long riverShipDefectCount = (long) shipData.getOrDefault("riverShipDefectCount", 0L);
				long prevRiverShipDefectCount = (long) prevShipData.getOrDefault("riverShipDefectCount", 0L);
				double riverShipDefectRate = calculateChangeRate(riverShipDefectCount, prevRiverShipDefectCount);
				result.put("riverShipDefectRate", riverShipDefectRate);
				
				// 计算内河船滞留环比变化
				long riverShipDetainedCount = (long) shipData.getOrDefault("riverShipDetainedCount", 0L);
				long prevRiverShipDetainedCount = (long) prevShipData.getOrDefault("riverShipDetainedCount", 0L);
				double riverShipDetainedRate = calculateChangeRate(riverShipDetainedCount, prevRiverShipDetainedCount);
				result.put("riverShipDetainedRate", riverShipDetainedRate);
				
				// 计算PSC检查环比变化
				long pscCount = (long) pscData.getOrDefault("pscCount", 0L);
				long prevPscCount = (long) prevPscData.getOrDefault("pscCount", 0L);
				double pscCountRate = calculateChangeRate(pscCount, prevPscCount);
				result.put("pscCountRate", pscCountRate);
				
				// 计算PSC缺陷环比变化
				long pscDefectCount = (long) pscData.getOrDefault("pscDefectCount", 0L);
				long prevPscDefectCount = (long) prevPscData.getOrDefault("pscDefectCount", 0L);
				double pscDefectRate = calculateChangeRate(pscDefectCount, prevPscDefectCount);
				result.put("pscDefectRate", pscDefectRate);
				
				// 计算PSC滞留环比变化
				long pscDetentionCount = (long) pscData.getOrDefault("pscDetentionCount", 0L);
				long prevPscDetentionCount = (long) prevPscData.getOrDefault("pscDetentionCount", 0L);
				double pscDetentionRate = calculateChangeRate(pscDetentionCount, prevPscDetentionCount);
				result.put("pscDetentionRate", pscDetentionRate);
				
				// 计算现场监督环比变化
				long onSiteCount = (long) onSiteData.getOrDefault("onSiteCount", 0L);
				long prevOnSiteCount = (long) prevOnSiteData.getOrDefault("onSiteCount", 0L);
				double onSiteCountRate = calculateChangeRate(onSiteCount, prevOnSiteCount);
				result.put("onSiteCountRate", onSiteCountRate);
				
				// 计算现场监督异常环比变化
				long abnormalCount = (long) onSiteData.getOrDefault("abnormalCount", 0L);
				long prevAbnormalCount = (long) prevOnSiteData.getOrDefault("abnormalCount", 0L);
				double abnormalCountRate = calculateChangeRate(abnormalCount, prevAbnormalCount);
				result.put("abnormalCountRate", abnormalCountRate);
				
				// 按部门计算环比变化率
		calculateDepartmentChangeRates(result, shipData, prevShipData, onSiteData, prevOnSiteData, pscData);
	}
	
	/**
	 * 计算各部门环比变化率
	 */
	private void calculateDepartmentChangeRates(Map<String, Object> result,
			Map<String, Object> shipData, Map<String, Object> prevShipData,
			Map<String, Object> onSiteData, Map<String, Object> prevOnSiteData,
			Map<String, Object> pscData) {
		
				Map<String, Long> agencySeaShipCounts = (Map<String, Long>) shipData.getOrDefault("agencySeaShipCounts", new HashMap<>());
				Map<String, Long> prevAgencySeaShipCounts = (Map<String, Long>) prevShipData.getOrDefault("agencySeaShipCounts", new HashMap<>());
				
				Map<String, Long> agencySeaShipDefectCounts = (Map<String, Long>) shipData.getOrDefault("agencySeaShipDefectCounts", new HashMap<>());
				Map<String, Long> prevAgencySeaShipDefectCounts = (Map<String, Long>) prevShipData.getOrDefault("agencySeaShipDefectCounts", new HashMap<>());
				
				Map<String, Long> agencySeaShipDetainedCounts = (Map<String, Long>) shipData.getOrDefault("agencySeaShipDetainedCounts", new HashMap<>());
				Map<String, Long> prevAgencySeaShipDetainedCounts = (Map<String, Long>) prevShipData.getOrDefault("agencySeaShipDetainedCounts", new HashMap<>());
				
				Map<String, Long> agencyRiverShipCounts = (Map<String, Long>) shipData.getOrDefault("agencyRiverShipCounts", new HashMap<>());
				Map<String, Long> prevAgencyRiverShipCounts = (Map<String, Long>) prevShipData.getOrDefault("agencyRiverShipCounts", new HashMap<>());
				
				Map<String, Long> agencyRiverShipDefectCounts = (Map<String, Long>) shipData.getOrDefault("agencyRiverShipDefectCounts", new HashMap<>());
				Map<String, Long> prevAgencyRiverShipDefectCounts = (Map<String, Long>) prevShipData.getOrDefault("agencyRiverShipDefectCounts", new HashMap<>());
				
				Map<String, Long> agencyRiverShipDetainedCounts = (Map<String, Long>) shipData.getOrDefault("agencyRiverShipDetainedCounts", new HashMap<>());
				Map<String, Long> prevAgencyRiverShipDetainedCounts = (Map<String, Long>) prevShipData.getOrDefault("agencyRiverShipDetainedCounts", new HashMap<>());
				
				Map<String, Long> agencyOnSiteCounts = (Map<String, Long>) onSiteData.getOrDefault("agencyOnSiteCounts", new HashMap<>());
				Map<String, Long> prevAgencyOnSiteCounts = (Map<String, Long>) prevOnSiteData.getOrDefault("agencyOnSiteCounts", new HashMap<>());
				
				Map<String, Long> agencyAbnormalCounts = (Map<String, Long>) onSiteData.getOrDefault("agencyAbnormalCounts", new HashMap<>());
				Map<String, Long> prevAgencyAbnormalCounts = (Map<String, Long>) prevOnSiteData.getOrDefault("agencyAbnormalCounts", new HashMap<>());
				
				// 计算各部门环比变化率
				Map<String, Double> agencySeaShipRates = new HashMap<>();
				Map<String, Double> agencySeaShipDefectRates = new HashMap<>();
				Map<String, Double> agencySeaShipDetainedRates = new HashMap<>();
				Map<String, Double> agencyRiverShipRates = new HashMap<>();
				Map<String, Double> agencyRiverShipDefectRates = new HashMap<>();
				Map<String, Double> agencyRiverShipDetainedRates = new HashMap<>();
				Map<String, Double> agencyOnSiteRates = new HashMap<>();
				Map<String, Double> agencyAbnormalRates = new HashMap<>();
				
				// 合并当前和上一周期的所有部门
				Set<String> allAgencies = new HashSet<>();
				allAgencies.addAll(agencySeaShipCounts.keySet());
				allAgencies.addAll(prevAgencySeaShipCounts.keySet());
				allAgencies.addAll(agencyRiverShipCounts.keySet());
				allAgencies.addAll(prevAgencyRiverShipCounts.keySet());
				allAgencies.addAll(agencyOnSiteCounts.keySet());
				allAgencies.addAll(prevAgencyOnSiteCounts.keySet());
				
				// 计算每个部门的各项数据环比变化率
				for (String agency : allAgencies) {
					// 海船安检数量环比
					long currSeaShipCount = agencySeaShipCounts.getOrDefault(agency, 0L);
					long lastSeaShipCount = prevAgencySeaShipCounts.getOrDefault(agency, 0L);
					agencySeaShipRates.put(agency, calculateChangeRate(currSeaShipCount, lastSeaShipCount));
					
					// 海船缺陷数量环比
					long currSeaShipDefectCount = agencySeaShipDefectCounts.getOrDefault(agency, 0L);
					long lastSeaShipDefectCount = prevAgencySeaShipDefectCounts.getOrDefault(agency, 0L);
					agencySeaShipDefectRates.put(agency, calculateChangeRate(currSeaShipDefectCount, lastSeaShipDefectCount));
					
					// 海船滞留数量环比
					long currSeaShipDetainedCount = agencySeaShipDetainedCounts.getOrDefault(agency, 0L);
					long lastSeaShipDetainedCount = prevAgencySeaShipDetainedCounts.getOrDefault(agency, 0L);
					agencySeaShipDetainedRates.put(agency, calculateChangeRate(currSeaShipDetainedCount, lastSeaShipDetainedCount));
					
					// 内河船安检数量环比
					long currRiverShipCount = agencyRiverShipCounts.getOrDefault(agency, 0L);
					long lastRiverShipCount = prevAgencyRiverShipCounts.getOrDefault(agency, 0L);
					agencyRiverShipRates.put(agency, calculateChangeRate(currRiverShipCount, lastRiverShipCount));
					
					// 内河船缺陷数量环比
					long currRiverShipDefectCount = agencyRiverShipDefectCounts.getOrDefault(agency, 0L);
					long lastRiverShipDefectCount = prevAgencyRiverShipDefectCounts.getOrDefault(agency, 0L);
					agencyRiverShipDefectRates.put(agency, calculateChangeRate(currRiverShipDefectCount, lastRiverShipDefectCount));
					
					// 内河船滞留数量环比
					long currRiverShipDetainedCount = agencyRiverShipDetainedCounts.getOrDefault(agency, 0L);
					long lastRiverShipDetainedCount = prevAgencyRiverShipDetainedCounts.getOrDefault(agency, 0L);
					agencyRiverShipDetainedRates.put(agency, calculateChangeRate(currRiverShipDetainedCount, lastRiverShipDetainedCount));
					
					// 现场监督数量环比
					long currOnSiteCount = agencyOnSiteCounts.getOrDefault(agency, 0L);
					long lastOnSiteCount = prevAgencyOnSiteCounts.getOrDefault(agency, 0L);
					agencyOnSiteRates.put(agency, calculateChangeRate(currOnSiteCount, lastOnSiteCount));
					
					// 现场监督异常数量环比
					long currAbnormalCount = agencyAbnormalCounts.getOrDefault(agency, 0L);
					long lastAbnormalCount = prevAgencyAbnormalCounts.getOrDefault(agency, 0L);
					agencyAbnormalRates.put(agency, calculateChangeRate(currAbnormalCount, lastAbnormalCount));
				}
				
				// 把环比数据添加到结果中
				result.put("agencySeaShipRates", agencySeaShipRates);
				result.put("agencySeaShipDefectRates", agencySeaShipDefectRates);
				result.put("agencySeaShipDetainedRates", agencySeaShipDetainedRates);
				result.put("agencyRiverShipRates", agencyRiverShipRates);
				result.put("agencyRiverShipDefectRates", agencyRiverShipDefectRates);
				result.put("agencyRiverShipDetainedRates", agencyRiverShipDetainedRates);
				result.put("agencyOnSiteRates", agencyOnSiteRates);
				result.put("agencyAbnormalRates", agencyAbnormalRates);
	}
	
	/**
	 * 构建部门数据列表
	 */
	private List<Map<String, Object>> buildDepartmentDataList(List<String> agencyList,
			Map<String, Object> shipData, Map<String, Object> onSiteData,
			Map<String, Object> pscData, Map<String, Object> result) {
		
			List<Map<String, Object>> agencyDataList = new ArrayList<>();
		
			for (String agency : agencyList) {
				Map<String, Object> agencyData = new HashMap<>();
				agencyData.put("agency", agency);
				
				// 海船安检数据
				Map<String, Long> seaShipCounts = (Map<String, Long>)shipData.getOrDefault("agencySeaShipCounts", new HashMap<>());
				agencyData.put("seaShipCount", seaShipCounts.getOrDefault(agency, 0L));
				
				// 添加海船缺陷和滞留数据
				Map<String, Long> seaShipDefectCounts = (Map<String, Long>)shipData.getOrDefault("agencySeaShipDefectCounts", new HashMap<>());
				Map<String, Long> seaShipDetainedCounts = (Map<String, Long>)shipData.getOrDefault("agencySeaShipDetainedCounts", new HashMap<>());
				agencyData.put("seaShipDefectCount", seaShipDefectCounts.getOrDefault(agency, 0L));
				agencyData.put("seaShipDetainedCount", seaShipDetainedCounts.getOrDefault(agency, 0L));
				
				// 内河船安检数据
				Map<String, Long> riverShipCounts = (Map<String, Long>)shipData.getOrDefault("agencyRiverShipCounts", new HashMap<>());
				agencyData.put("riverShipCount", riverShipCounts.getOrDefault(agency, 0L));
				
				// 添加内河船缺陷和滞留数据
				Map<String, Long> riverShipDefectCounts = (Map<String, Long>)shipData.getOrDefault("agencyRiverShipDefectCounts", new HashMap<>());
				Map<String, Long> riverShipDetainedCounts = (Map<String, Long>)shipData.getOrDefault("agencyRiverShipDetainedCounts", new HashMap<>());
				agencyData.put("riverShipDefectCount", riverShipDefectCounts.getOrDefault(agency, 0L));
				agencyData.put("riverShipDetainedCount", riverShipDetainedCounts.getOrDefault(agency, 0L));
				
				// 现场监督数据
				Map<String, Long> onSiteCounts = (Map<String, Long>)onSiteData.getOrDefault("agencyOnSiteCounts", new HashMap<>());
				agencyData.put("onSiteCount", onSiteCounts.getOrDefault(agency, 0L));
				
				// 异常监督数据
				Map<String, Long> abnormalCounts = (Map<String, Long>)onSiteData.getOrDefault("agencyAbnormalCounts", new HashMap<>());
				agencyData.put("abnormalCount", abnormalCounts.getOrDefault(agency, 0L));
				
				// PSC数据 - 仅张家港海事局有
				if (agency.contains("张家港海事局")) {
					agencyData.put("pscCount", pscData.getOrDefault("pscCount", 0L));
					agencyData.put("pscDefectCount", pscData.getOrDefault("pscDefectCount", 0L));
					agencyData.put("pscDetentionCount", pscData.getOrDefault("pscDetentionCount", 0L));
				} else {
					agencyData.put("pscCount", 0L);
					agencyData.put("pscDefectCount", 0L);
					agencyData.put("pscDetentionCount", 0L);
				}
				
				// 添加环比数据
				if (result.containsKey("agencySeaShipRates")) {
					Map<String, Double> agencySeaShipRates = (Map<String, Double>) result.get("agencySeaShipRates");
					Map<String, Double> agencySeaShipDefectRates = (Map<String, Double>) result.get("agencySeaShipDefectRates");
					Map<String, Double> agencySeaShipDetainedRates = (Map<String, Double>) result.get("agencySeaShipDetainedRates");
					Map<String, Double> agencyRiverShipRates = (Map<String, Double>) result.get("agencyRiverShipRates");
					Map<String, Double> agencyRiverShipDefectRates = (Map<String, Double>) result.get("agencyRiverShipDefectRates");
					Map<String, Double> agencyRiverShipDetainedRates = (Map<String, Double>) result.get("agencyRiverShipDetainedRates");
					Map<String, Double> agencyOnSiteRates = (Map<String, Double>) result.get("agencyOnSiteRates");
					Map<String, Double> agencyAbnormalRates = (Map<String, Double>) result.get("agencyAbnormalRates");
					
					agencyData.put("seaShipCountRate", agencySeaShipRates.getOrDefault(agency, 0.0));
					agencyData.put("seaShipDefectRate", agencySeaShipDefectRates.getOrDefault(agency, 0.0));
					agencyData.put("seaShipDetainedRate", agencySeaShipDetainedRates.getOrDefault(agency, 0.0));
					agencyData.put("riverShipCountRate", agencyRiverShipRates.getOrDefault(agency, 0.0));
					agencyData.put("riverShipDefectRate", agencyRiverShipDefectRates.getOrDefault(agency, 0.0));
					agencyData.put("riverShipDetainedRate", agencyRiverShipDetainedRates.getOrDefault(agency, 0.0));
					agencyData.put("onSiteCountRate", agencyOnSiteRates.getOrDefault(agency, 0.0));
					agencyData.put("abnormalCountRate", agencyAbnormalRates.getOrDefault(agency, 0.0));
					
					// PSC环比数据 - 仅张家港海事局有
					if (agency.contains("张家港海事局")) {
						agencyData.put("pscCountRate", result.getOrDefault("pscCountRate", 0.0));
						agencyData.put("pscDefectRate", result.getOrDefault("pscDefectRate", 0.0));
						agencyData.put("pscDetentionRate", result.getOrDefault("pscDetentionRate", 0.0));
					} else {
						agencyData.put("pscCountRate", 0.0);
						agencyData.put("pscDefectRate", 0.0);
						agencyData.put("pscDetentionRate", 0.0);
					}
				}
				
				agencyDataList.add(agencyData);
			}
			
		return agencyDataList;
	}

	/**
	 * 获取部门列表
	 */
	private List<String> getDepartmentList() {
		// 返回部门列表，这里可以从agency_dept表查询
		List<String> departments = new ArrayList<>();
		departments.add("张家港海事局");
		departments.add("江阴海事局");
		departments.add("常熟海事局");
		departments.add("太仓海事局");
		return departments;
	}

	/**
	 * 获取船舶检查统计数据 - 限制为张家港海事局四家机构
	 */
	private Map<String, Object> getShipInspectionStatisticsForZhangjiagang(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date startDateObj = startDate != null ? DateUtils.parseDate(startDate) : null;
			Date endDateObj = endDate != null ? DateUtils.parseDate(endDate) : null;
			
			// 创建查询条件
			ShipInspection shipQuery = new ShipInspection();
			shipQuery.setInspectionType("初查");
			if (startDateObj != null) {
				shipQuery.setInspectionDate_gte(startDateObj);
			}
			if (endDateObj != null) {
				shipQuery.setInspectionDate_lte(endDateObj);
			}
			
			// 执行查询
			List<ShipInspection> shipList = shipInspectionService.findDistinctList(shipQuery);
			
			// 定义张家港海事局的四家机构
			List<String> targetAgencies = Arrays.asList(
				"保税区海事处(筹)", 
				"港区海事处", 
				"锦丰海事处", 
				"张家港海事局"
			);
			
			// 筛选只包含目标机构的数据
			shipList = shipList.stream()
				.filter(ship -> targetAgencies.stream()
					.anyMatch(agency -> ship.getInspectionAgency() != null && 
						ship.getInspectionAgency().contains(agency)))
				.collect(Collectors.toList());
			
			// 初始化统计数据
			long seaShipCount = 0;
			long riverShipCount = 0;
			long seaShipDefectCount = 0;
			long riverShipDefectCount = 0;
			long seaShipDetainedCount = 0;
			long riverShipDetainedCount = 0;
			
			Map<String, Long> agencySeaShipCounts = new HashMap<>();
			Map<String, Long> agencyRiverShipCounts = new HashMap<>();
			Map<String, Long> agencySeaShipDefectCounts = new HashMap<>();
			Map<String, Long> agencyRiverShipDefectCounts = new HashMap<>();
			Map<String, Long> agencySeaShipDetainedCounts = new HashMap<>();
			Map<String, Long> agencyRiverShipDetainedCounts = new HashMap<>();
			
			// 初始化各机构的统计数据
			for (String agency : targetAgencies) {
				agencySeaShipCounts.put(agency, 0L);
				agencyRiverShipCounts.put(agency, 0L);
				agencySeaShipDefectCounts.put(agency, 0L);
				agencyRiverShipDefectCounts.put(agency, 0L);
				agencySeaShipDetainedCounts.put(agency, 0L);
				agencyRiverShipDetainedCounts.put(agency, 0L);
			}
			
			// 处理查询结果
			for (ShipInspection ship : shipList) {
				String shipType = ship.getShipType();
				String agency = ship.getInspectionAgency();
				
				// 找到匹配的机构
				String matchedAgency = targetAgencies.stream()
					.filter(targetAgency -> agency != null && agency.contains(targetAgency))
					.findFirst()
					.orElse(null);
				
				if (matchedAgency != null) {
					if ("海船".equals(shipType)) {
						seaShipCount++;
						agencySeaShipCounts.put(matchedAgency, agencySeaShipCounts.get(matchedAgency) + 1);
						
						// 缺陷数量统计
						if (ship.getDefectCount() != null && ship.getDefectCount() > 0) {
							seaShipDefectCount += ship.getDefectCount();
							agencySeaShipDefectCounts.put(matchedAgency, 
								agencySeaShipDefectCounts.get(matchedAgency) + ship.getDefectCount());
						}
						
						// 滞留数量统计
						if ("是".equals(ship.getDetained())) {
							seaShipDetainedCount++;
							agencySeaShipDetainedCounts.put(matchedAgency, 
								agencySeaShipDetainedCounts.get(matchedAgency) + 1);
						}
					} else if ("内河船".equals(shipType)) {
						riverShipCount++;
						agencyRiverShipCounts.put(matchedAgency, agencyRiverShipCounts.get(matchedAgency) + 1);
						
						// 缺陷数量统计
						if (ship.getDefectCount() != null && ship.getDefectCount() > 0) {
							riverShipDefectCount += ship.getDefectCount();
							agencyRiverShipDefectCounts.put(matchedAgency, 
								agencyRiverShipDefectCounts.get(matchedAgency) + ship.getDefectCount());
						}
						
						// 滞留数量统计
						if ("是".equals(ship.getDetained())) {
							riverShipDetainedCount++;
							agencyRiverShipDetainedCounts.put(matchedAgency, 
								agencyRiverShipDetainedCounts.get(matchedAgency) + 1);
						}
					}
				}
			}
			
			// 构建结果
			result.put("seaShipCount", seaShipCount);
			result.put("riverShipCount", riverShipCount);
			result.put("seaShipDefectCount", seaShipDefectCount);
			result.put("riverShipDefectCount", riverShipDefectCount);
			result.put("seaShipDetainedCount", seaShipDetainedCount);
			result.put("riverShipDetainedCount", riverShipDetainedCount);
			
			result.put("agencySeaShipCounts", agencySeaShipCounts);
			result.put("agencyRiverShipCounts", agencyRiverShipCounts);
			result.put("agencySeaShipDefectCounts", agencySeaShipDefectCounts);
			result.put("agencyRiverShipDefectCounts", agencyRiverShipDefectCounts);
			result.put("agencySeaShipDetainedCounts", agencySeaShipDetainedCounts);
			result.put("agencyRiverShipDetainedCounts", agencyRiverShipDetainedCounts);
			
			// 添加机构列表
			result.put("agencies", targetAgencies);
			
		} catch (Exception e) {
			logger.error("获取船舶检查统计数据失败", e);
		}
		
		return result;
	}
	
	/**
	 * 获取现场监督统计数据 - 限制为张家港海事局四家机构
	 */
	private Map<String, Object> getOnSiteInspectionStatisticsForZhangjiagang(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date startDateObj = startDate != null ? DateUtils.parseDate(startDate) : null;
			Date endDateObj = endDate != null ? DateUtils.parseDate(endDate) : null;
			
			// 创建查询条件
			ShipOnSiteInspection onSiteQuery = new ShipOnSiteInspection();
			if (startDateObj != null) {
				onSiteQuery.setInspectionDate_gte(startDateObj);
			}
			if (endDateObj != null) {
				onSiteQuery.setInspectionDate_lte(endDateObj);
			}
			
			// 执行查询
			List<ShipOnSiteInspection> onSiteList = shipOnSiteInspectionService.findDistinctList(onSiteQuery);
			
			// 定义张家港海事局的四家机构
			List<String> targetAgencies = Arrays.asList(
				"保税区海事处(筹)", 
				"港区海事处", 
				"锦丰海事处", 
				"张家港海事局"
			);
			
			// 筛选只包含目标机构的数据
			onSiteList = onSiteList.stream()
				.filter(onSite -> targetAgencies.stream()
					.anyMatch(agency -> onSite.getInspectionAgency() != null && 
						onSite.getInspectionAgency().contains(agency)))
				.collect(Collectors.toList());
			
			// 初始化统计数据
			long onSiteCount = 0;
			long abnormalCount = 0;
			Map<String, Long> agencyOnSiteCounts = new HashMap<>();
			Map<String, Long> agencyAbnormalCounts = new HashMap<>();
			
			// 初始化各机构的统计数据
			for (String agency : targetAgencies) {
				agencyOnSiteCounts.put(agency, 0L);
				agencyAbnormalCounts.put(agency, 0L);
			}
			
			// 使用去重逻辑：按船名+日期+机构分组
			Map<String, List<ShipOnSiteInspection>> groupedData = onSiteList.stream()
				.collect(Collectors.groupingBy(onSite -> 
					onSite.getShipNameCn() + "|" + 
					DateUtils.formatDate(onSite.getInspectionDate()) + "|" + 
					onSite.getInspectionAgency()));
			
			// 处理分组后的数据
			for (Map.Entry<String, List<ShipOnSiteInspection>> entry : groupedData.entrySet()) {
				List<ShipOnSiteInspection> group = entry.getValue();
				if (!group.isEmpty()) {
					ShipOnSiteInspection representative = group.get(0);
					String agency = representative.getInspectionAgency();
					
					// 找到匹配的机构
					String matchedAgency = targetAgencies.stream()
						.filter(targetAgency -> agency != null && agency.contains(targetAgency))
						.findFirst()
						.orElse(null);
					
					if (matchedAgency != null) {
						onSiteCount++;
						agencyOnSiteCounts.put(matchedAgency, agencyOnSiteCounts.get(matchedAgency) + 1);
						
						// 检查该分组是否有异常（任何一条记录有问题就算异常）
						boolean hasIssue = group.stream()
							.anyMatch(onSite -> "是".equals(onSite.getIssueFound()));
						
						if (hasIssue) {
							abnormalCount++;
							agencyAbnormalCounts.put(matchedAgency, agencyAbnormalCounts.get(matchedAgency) + 1);
						}
					}
				}
			}
			
			// 构建结果
			result.put("onSiteCount", onSiteCount);
			result.put("abnormalCount", abnormalCount);
			result.put("agencyOnSiteCounts", agencyOnSiteCounts);
			result.put("agencyAbnormalCounts", agencyAbnormalCounts);
			
		} catch (Exception e) {
			logger.error("获取现场监督统计数据失败", e);
		}
		
		return result;
	}
	
	/**
	 * 构建张家港海事局四家机构的部门数据列表
	 */
	private List<Map<String, Object>> buildDepartmentDataListForZhangjiagang(
			Map<String, Object> shipData, Map<String, Object> onSiteData,
			Map<String, Object> prevShipData, Map<String, Object> prevOnSiteData) {
		
		List<Map<String, Object>> agencyDataList = new ArrayList<>();
		
		// 定义张家港海事局的四家机构
		List<String> targetAgencies = Arrays.asList(
			"保税区海事处(筹)", 
			"港区海事处", 
			"锦丰海事处", 
			"张家港海事局"
		);
		
		// 获取当前数据
		Map<String, Long> agencySeaShipCounts = (Map<String, Long>) shipData.get("agencySeaShipCounts");
		Map<String, Long> agencyRiverShipCounts = (Map<String, Long>) shipData.get("agencyRiverShipCounts");
		Map<String, Long> agencySeaShipDefectCounts = (Map<String, Long>) shipData.get("agencySeaShipDefectCounts");
		Map<String, Long> agencyRiverShipDefectCounts = (Map<String, Long>) shipData.get("agencyRiverShipDefectCounts");
		Map<String, Long> agencySeaShipDetainedCounts = (Map<String, Long>) shipData.get("agencySeaShipDetainedCounts");
		Map<String, Long> agencyRiverShipDetainedCounts = (Map<String, Long>) shipData.get("agencyRiverShipDetainedCounts");
		Map<String, Long> agencyOnSiteCounts = (Map<String, Long>) onSiteData.get("agencyOnSiteCounts");
		Map<String, Long> agencyAbnormalCounts = (Map<String, Long>) onSiteData.get("agencyAbnormalCounts");
		
		// 获取上期数据
		Map<String, Long> prevAgencySeaShipCounts = prevShipData != null ? 
			(Map<String, Long>) prevShipData.get("agencySeaShipCounts") : new HashMap<>();
		Map<String, Long> prevAgencyRiverShipCounts = prevShipData != null ? 
			(Map<String, Long>) prevShipData.get("agencyRiverShipCounts") : new HashMap<>();
		Map<String, Long> prevAgencySeaShipDefectCounts = prevShipData != null ? 
			(Map<String, Long>) prevShipData.get("agencySeaShipDefectCounts") : new HashMap<>();
		Map<String, Long> prevAgencyRiverShipDefectCounts = prevShipData != null ? 
			(Map<String, Long>) prevShipData.get("agencyRiverShipDefectCounts") : new HashMap<>();
		Map<String, Long> prevAgencySeaShipDetainedCounts = prevShipData != null ? 
			(Map<String, Long>) prevShipData.get("agencySeaShipDetainedCounts") : new HashMap<>();
		Map<String, Long> prevAgencyRiverShipDetainedCounts = prevShipData != null ? 
			(Map<String, Long>) prevShipData.get("agencyRiverShipDetainedCounts") : new HashMap<>();
		Map<String, Long> prevAgencyOnSiteCounts = prevOnSiteData != null ? 
			(Map<String, Long>) prevOnSiteData.get("agencyOnSiteCounts") : new HashMap<>();
		Map<String, Long> prevAgencyAbnormalCounts = prevOnSiteData != null ? 
			(Map<String, Long>) prevOnSiteData.get("agencyAbnormalCounts") : new HashMap<>();
		
		// 为每个机构构建数据
		for (String agency : targetAgencies) {
			Map<String, Object> agencyData = new HashMap<>();
			agencyData.put("agency", agency);
			
			// 当前数据
			long seaShipCount = agencySeaShipCounts.getOrDefault(agency, 0L);
			long riverShipCount = agencyRiverShipCounts.getOrDefault(agency, 0L);
			long seaShipDefectCount = agencySeaShipDefectCounts.getOrDefault(agency, 0L);
			long riverShipDefectCount = agencyRiverShipDefectCounts.getOrDefault(agency, 0L);
			long seaShipDetainedCount = agencySeaShipDetainedCounts.getOrDefault(agency, 0L);
			long riverShipDetainedCount = agencyRiverShipDetainedCounts.getOrDefault(agency, 0L);
			long onSiteCount = agencyOnSiteCounts.getOrDefault(agency, 0L);
			long abnormalCount = agencyAbnormalCounts.getOrDefault(agency, 0L);
			
			// 上期数据
			long prevSeaShipCount = prevAgencySeaShipCounts.getOrDefault(agency, 0L);
			long prevRiverShipCount = prevAgencyRiverShipCounts.getOrDefault(agency, 0L);
			long prevSeaShipDefectCount = prevAgencySeaShipDefectCounts.getOrDefault(agency, 0L);
			long prevRiverShipDefectCount = prevAgencyRiverShipDefectCounts.getOrDefault(agency, 0L);
			long prevSeaShipDetainedCount = prevAgencySeaShipDetainedCounts.getOrDefault(agency, 0L);
			long prevRiverShipDetainedCount = prevAgencyRiverShipDetainedCounts.getOrDefault(agency, 0L);
			long prevOnSiteCount = prevAgencyOnSiteCounts.getOrDefault(agency, 0L);
			long prevAbnormalCount = prevAgencyAbnormalCounts.getOrDefault(agency, 0L);
			
			// 设置当前数据
			agencyData.put("seaShipCount", seaShipCount);
			agencyData.put("riverShipCount", riverShipCount);
			agencyData.put("seaShipDefectCount", seaShipDefectCount);
			agencyData.put("riverShipDefectCount", riverShipDefectCount);
			agencyData.put("seaShipDetainedCount", seaShipDetainedCount);
			agencyData.put("riverShipDetainedCount", riverShipDetainedCount);
			agencyData.put("onSiteCount", onSiteCount);
			agencyData.put("abnormalCount", abnormalCount);
			
			// 计算环比变化率
			agencyData.put("seaShipCountRate", calculateRate(seaShipCount, prevSeaShipCount));
			agencyData.put("riverShipCountRate", calculateRate(riverShipCount, prevRiverShipCount));
			agencyData.put("seaShipDefectRate", calculateRate(seaShipDefectCount, prevSeaShipDefectCount));
			agencyData.put("riverShipDefectRate", calculateRate(riverShipDefectCount, prevRiverShipDefectCount));
			agencyData.put("seaShipDetainedRate", calculateRate(seaShipDetainedCount, prevSeaShipDetainedCount));
			agencyData.put("riverShipDetainedRate", calculateRate(riverShipDetainedCount, prevRiverShipDetainedCount));
			agencyData.put("onSiteCountRate", calculateRate(onSiteCount, prevOnSiteCount));
			agencyData.put("abnormalCountRate", calculateRate(abnormalCount, prevAbnormalCount));
			
			// PSC数据（只有张家港海事局有）
			if ("张家港海事局".equals(agency)) {
				agencyData.put("pscCount", 0L); // PSC数据需要单独处理
				agencyData.put("pscDefectCount", 0L);
				agencyData.put("pscDetentionCount", 0L);
				agencyData.put("pscCountRate", 0.0);
				agencyData.put("pscDefectRate", 0.0);
				agencyData.put("pscDetentionRate", 0.0);
			} else {
				agencyData.put("pscCount", 0L);
				agencyData.put("pscDefectCount", 0L);
				agencyData.put("pscDetentionCount", 0L);
				agencyData.put("pscCountRate", 0.0);
				agencyData.put("pscDefectRate", 0.0);
				agencyData.put("pscDetentionRate", 0.0);
			}
			
			agencyDataList.add(agencyData);
		}
		
		return agencyDataList;
	}

	/**
	 * 计算环比变化率
	 */
	private double calculateRate(long current, long previous) {
		if (previous == 0) {
			return current > 0 ? 100.0 : 0.0;
		}
		return ((double) (current - previous) / previous) * 100.0;
	}
	
	/**
	 * 构建月度报表按部门分组的数据列表
	 */
	private List<Map<String, Object>> buildMonthlyDepartmentDataList(
			Map<String, Object> shipData, Map<String, Object> onSiteData, Map<String, Object> pscData,
			Map<String, Object> prevShipData, Map<String, Object> prevOnSiteData, Map<String, Object> prevPscData) {
		
		List<Map<String, Object>> deptDataList = new ArrayList<>();
		
		try {
			// 获取所有部门列表
			List<String> departments = weeklyReportDao.findAllDepartments();
			
			// 获取当前期间各部门的数据
			Map<String, Long> agencySeaShipCounts = (Map<String, Long>) shipData.getOrDefault("agencySeaShipCounts", new HashMap<>());
			Map<String, Long> agencyRiverShipCounts = (Map<String, Long>) shipData.getOrDefault("agencyRiverShipCounts", new HashMap<>());
			Map<String, Long> agencySeaShipDefectCounts = (Map<String, Long>) shipData.getOrDefault("agencySeaShipDefectCounts", new HashMap<>());
			Map<String, Long> agencyRiverShipDefectCounts = (Map<String, Long>) shipData.getOrDefault("agencyRiverShipDefectCounts", new HashMap<>());
			Map<String, Long> agencySeaShipDetainedCounts = (Map<String, Long>) shipData.getOrDefault("agencySeaShipDetainedCounts", new HashMap<>());
			Map<String, Long> agencyRiverShipDetainedCounts = (Map<String, Long>) shipData.getOrDefault("agencyRiverShipDetainedCounts", new HashMap<>());
			Map<String, Long> agencyOnSiteCounts = (Map<String, Long>) onSiteData.getOrDefault("agencyOnSiteCounts", new HashMap<>());
			Map<String, Long> agencyAbnormalCounts = (Map<String, Long>) onSiteData.getOrDefault("agencyAbnormalCounts", new HashMap<>());
			Map<String, Long> agencyPscCounts = (Map<String, Long>) pscData.getOrDefault("agencyPscCounts", new HashMap<>());
			Map<String, Long> agencyPscDefectCounts = (Map<String, Long>) pscData.getOrDefault("agencyPscDefectCounts", new HashMap<>());
			Map<String, Long> agencyPscDetentionCounts = (Map<String, Long>) pscData.getOrDefault("agencyPscDetentionCounts", new HashMap<>());
			
			// 获取上期各部门的数据
			Map<String, Long> prevAgencySeaShipCounts = (Map<String, Long>) prevShipData.getOrDefault("agencySeaShipCounts", new HashMap<>());
			Map<String, Long> prevAgencyRiverShipCounts = (Map<String, Long>) prevShipData.getOrDefault("agencyRiverShipCounts", new HashMap<>());
			Map<String, Long> prevAgencySeaShipDefectCounts = (Map<String, Long>) prevShipData.getOrDefault("agencySeaShipDefectCounts", new HashMap<>());
			Map<String, Long> prevAgencyRiverShipDefectCounts = (Map<String, Long>) prevShipData.getOrDefault("agencyRiverShipDefectCounts", new HashMap<>());
			Map<String, Long> prevAgencySeaShipDetainedCounts = (Map<String, Long>) prevShipData.getOrDefault("agencySeaShipDetainedCounts", new HashMap<>());
			Map<String, Long> prevAgencyRiverShipDetainedCounts = (Map<String, Long>) prevShipData.getOrDefault("agencyRiverShipDetainedCounts", new HashMap<>());
			Map<String, Long> prevAgencyOnSiteCounts = (Map<String, Long>) prevOnSiteData.getOrDefault("agencyOnSiteCounts", new HashMap<>());
			Map<String, Long> prevAgencyAbnormalCounts = (Map<String, Long>) prevOnSiteData.getOrDefault("agencyAbnormalCounts", new HashMap<>());
			Map<String, Long> prevAgencyPscCounts = (Map<String, Long>) prevPscData.getOrDefault("agencyPscCounts", new HashMap<>());
			Map<String, Long> prevAgencyPscDefectCounts = (Map<String, Long>) prevPscData.getOrDefault("agencyPscDefectCounts", new HashMap<>());
			Map<String, Long> prevAgencyPscDetentionCounts = (Map<String, Long>) prevPscData.getOrDefault("agencyPscDetentionCounts", new HashMap<>());
			
			for (String dept : departments) {
				Map<String, Object> deptData = new HashMap<>();
				deptData.put("dept", dept);
				
				// 海船安检数据
				long seaShipCount = agencySeaShipCounts.getOrDefault(dept, 0L);
				long prevSeaShipCount = prevAgencySeaShipCounts.getOrDefault(dept, 0L);
				long seaShipDefectCount = agencySeaShipDefectCounts.getOrDefault(dept, 0L);
				long prevSeaShipDefectCount = prevAgencySeaShipDefectCounts.getOrDefault(dept, 0L);
				long seaShipDetainedCount = agencySeaShipDetainedCounts.getOrDefault(dept, 0L);
				long prevSeaShipDetainedCount = prevAgencySeaShipDetainedCounts.getOrDefault(dept, 0L);
				
				deptData.put("seaShipCount", seaShipCount);
				deptData.put("prevSeaShipCount", prevSeaShipCount);
				
				deptData.put("seaShipCountRate", calculateRate(seaShipCount, prevSeaShipCount));
				deptData.put("seaShipDefectCount", seaShipDefectCount);
				deptData.put("prevSeaShipDefectCount", prevSeaShipDefectCount);
				
				deptData.put("seaShipDefectRate", calculateRate(seaShipDefectCount, prevSeaShipDefectCount));
				deptData.put("seaShipDetainedCount", seaShipDetainedCount);
				deptData.put("prevSeaShipDetainedCount", prevSeaShipDetainedCount);
				
				deptData.put("seaShipDetainedRate", calculateRate(seaShipDetainedCount, prevSeaShipDetainedCount));
				
				// 内河船安检数据
				long riverShipCount = agencyRiverShipCounts.getOrDefault(dept, 0L);
				long prevRiverShipCount = prevAgencyRiverShipCounts.getOrDefault(dept, 0L);
				long riverShipDefectCount = agencyRiverShipDefectCounts.getOrDefault(dept, 0L);
				long prevRiverShipDefectCount = prevAgencyRiverShipDefectCounts.getOrDefault(dept, 0L);
				long riverShipDetainedCount = agencyRiverShipDetainedCounts.getOrDefault(dept, 0L);
				long prevRiverShipDetainedCount = prevAgencyRiverShipDetainedCounts.getOrDefault(dept, 0L);
				
				deptData.put("riverShipCount", riverShipCount);
				deptData.put("prevRiverShipCount", prevRiverShipCount);
				
				deptData.put("riverShipCountRate", calculateRate(riverShipCount, prevRiverShipCount));
				deptData.put("riverShipDefectCount", riverShipDefectCount);
				deptData.put("prevRiverShipDefectCount", prevRiverShipDefectCount);
				
				deptData.put("riverShipDefectRate", calculateRate(riverShipDefectCount, prevRiverShipDefectCount));
				deptData.put("riverShipDetainedCount", riverShipDetainedCount);
				deptData.put("prevRiverShipDetainedCount", prevRiverShipDetainedCount);
				
				deptData.put("riverShipDetainedRate", calculateRate(riverShipDetainedCount, prevRiverShipDetainedCount));
				
				// PSC检查数据（从各部门的PSC数据Map中获取）
				long pscCount = agencyPscCounts.getOrDefault(dept, 0L);
				long prevPscCount = prevAgencyPscCounts.getOrDefault(dept, 0L);
				long pscDefectCount = agencyPscDefectCounts.getOrDefault(dept, 0L);
				long prevPscDefectCount = prevAgencyPscDefectCounts.getOrDefault(dept, 0L);
				long pscDetentionCount = agencyPscDetentionCounts.getOrDefault(dept, 0L);
				long prevPscDetentionCount = prevAgencyPscDetentionCounts.getOrDefault(dept, 0L);
				
				deptData.put("pscCount", pscCount);
				deptData.put("prevPscCount", prevPscCount);
				deptData.put("pscCountRate", calculateRate(pscCount, prevPscCount));
				deptData.put("pscDefectCount", pscDefectCount);
				deptData.put("prevPscDefectCount", prevPscDefectCount);
				deptData.put("pscDefectRate", calculateRate(pscDefectCount, prevPscDefectCount));
				deptData.put("pscDetentionCount", pscDetentionCount);
				deptData.put("prevPscDetentionCount", prevPscDetentionCount);
				deptData.put("pscDetentionRate", calculateRate(pscDetentionCount, prevPscDetentionCount));
				
				// 现场监督数据
				long onSiteCount = agencyOnSiteCounts.getOrDefault(dept, 0L);
				long prevOnSiteCount = prevAgencyOnSiteCounts.getOrDefault(dept, 0L);
				long abnormalCount = agencyAbnormalCounts.getOrDefault(dept, 0L);
				long prevAbnormalCount = prevAgencyAbnormalCounts.getOrDefault(dept, 0L);
				
				deptData.put("onSiteCount", onSiteCount);
				deptData.put("prevOnSiteCount", prevOnSiteCount);
				
				deptData.put("onSiteCountRate", calculateRate(onSiteCount, prevOnSiteCount));
				deptData.put("abnormalCount", abnormalCount);
				deptData.put("prevAbnormalCount", prevAbnormalCount);
				
				deptData.put("abnormalCountRate", calculateRate(abnormalCount, prevAbnormalCount));
				
				deptDataList.add(deptData);
			}
		} catch (Exception e) {
			logger.error("构建月度部门数据列表失败", e);
		}
		
		return deptDataList;
	}
}