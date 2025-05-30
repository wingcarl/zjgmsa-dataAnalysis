package com.jeesite.modules.data_collect.dynamic.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReport;
import com.jeesite.modules.data_collect.weekly.service.WeeklyReportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

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
	@Autowired
	private JdbcTemplate jdbcTemplate;
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

	/**
	 * 动态执法图表页面
	 */
	@RequestMapping(value = "chart")
	public String chart() {
		return "data_collect/dynamic/dynamicChart";
	}

	/**
	 * 获取动态执法数据
	 */
	@RequestMapping(value = "getDynamicEnforcementData")
	@ResponseBody
	public Map<String, Object> getDynamicEnforcementData(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		// 参数处理，默认为上周五到本周四
		LocalDate today = LocalDate.now();
		LocalDate lastFriday = today.minusDays(today.getDayOfWeek().getValue() + 2) // 回到上周五
				.plusDays(today.getDayOfWeek().getValue() <= 5 ? 0 : 7); // 如果今天是周末，则取上上周五
		LocalDate thisThursday = lastFriday.plusDays(6); // 本周四
		
		Date start = startDate != null && !startDate.isEmpty() ? 
				DateUtils.parseDate(startDate) : 
				DateUtils.parseDate(lastFriday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		Date end = endDate != null && !endDate.isEmpty() ? 
				DateUtils.parseDate(endDate) : 
				DateUtils.parseDate(thisThursday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		
		// 计算上一个周期
		long periodDays = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000) + 1;
		Date lastStart = new Date(start.getTime() - periodDays * 24 * 60 * 60 * 1000);
		Date lastEnd = new Date(start.getTime() - 24 * 60 * 60 * 1000);
		
		// 获取当前周期数据
		Map<String, Map<String, Object>> currentData = calculateEnforcementData(start, end);
		// 获取上一周期数据
		Map<String, Map<String, Object>> previousData = calculateEnforcementData(lastStart, lastEnd);
		
		result.put("currentData", currentData);
		result.put("previousData", previousData);
		result.put("startDate", DateUtils.formatDate(start, "yyyy-MM-dd"));
		result.put("endDate", DateUtils.formatDate(end, "yyyy-MM-dd"));

		return result;
	}

	/**
	 * 计算动态执法数据统计
	 */
	private Map<String, Map<String, Object>> calculateEnforcementData(Date startDate, Date endDate) {
		// 部门映射配置 - 将快反执法单元映射到部门
		Map<String, String> departmentMapping = new HashMap<>();
		departmentMapping.put("西港区快反处置中心", "港区海事处");
		departmentMapping.put("六号快反执法单元", "港区海事处");
		departmentMapping.put("五号快反执法单元", "港区海事处");
		departmentMapping.put("东港区快反处置中心", "锦丰海事处");
		departmentMapping.put("一号快反执法单元", "锦丰海事处");
		departmentMapping.put("二号快反执法单元", "锦丰海事处");
		departmentMapping.put("三号快反执法单元", "锦丰海事处");
		departmentMapping.put("保税区快反处置中心", "保税区海巡执法大队");
		departmentMapping.put("四号快反执法单元", "保税区海巡执法大队");
		departmentMapping.put("福姜沙快反处置中心", "海巡执法支队");
		departmentMapping.put("七号快反执法单元", "海巡执法支队");
		
		// 初始化结果集
		Map<String, Map<String, Object>> results = new HashMap<>();
		results.put("张家港海事局", createEmptyResultMap());
		results.put("港区海事处", createEmptyResultMap());
		results.put("锦丰海事处", createEmptyResultMap());
		results.put("保税区海巡执法大队", createEmptyResultMap());
		results.put("海巡执法支队", createEmptyResultMap());
		
		// 获取查询时间范围内的所有数据
		DynamicEnforcementData query = new DynamicEnforcementData();
		query.setInspectionTime_gte(startDate);
		query.setInspectionTime_lte(endDate);
		List<DynamicEnforcementData> dataList = dynamicEnforcementDataService.findList(query);
		
		// 对数据进行去重处理
		Set<String> uniqueKeys = new HashSet<>();
		List<DynamicEnforcementData> dedupDataList = new ArrayList<>();
		
		for (DynamicEnforcementData data : dataList) {
			// 构建去重键：检查单位+检查时间(日期部分)+检查对象+大项名称+巡航任务名称
			String unit = data.getInspectionUnit() != null ? data.getInspectionUnit() : "";
			String time = data.getInspectionTime() != null ? 
					DateUtils.formatDate(data.getInspectionTime(), "yyyy-MM-dd") : "";
			String target = data.getInspectionTarget() != null ? data.getInspectionTarget() : "";
			String item = data.getMajorItemName() != null ? data.getMajorItemName() : "";
			String task = data.getCruiseTaskName() != null ? data.getCruiseTaskName() : "";
			
			String uniqueKey = unit + "|" + time + "|" + target + "|" + item + "|" + task;
			
			// 如果这个组合没出现过，则加入去重后的列表
			if (!uniqueKeys.contains(uniqueKey)) {
				uniqueKeys.add(uniqueKey);
				dedupDataList.add(data);
			}
		}
		
		// 使用去重后的数据进行统计
		for (DynamicEnforcementData data : dedupDataList) {
			// 获取部门，应用映射规则
			String department = data.getInspectionUnit();
			String mappedDepartment = departmentMapping.containsKey(department) ? 
					departmentMapping.get(department) : department;
			
			// 如果不是指定的五个部门之一，则跳过
			if (!results.containsKey(mappedDepartment)) {
				continue;
			}
			
			// 获取该部门的统计结果
			Map<String, Object> deptResult = results.get(mappedDepartment);
			Map<String, Object> totalResult = results.get("张家港海事局");
			
			// 判断巡航类型和结果
			String cruiseTaskName = data.getCruiseTaskName();
			String inspectionResult = data.getInspectionResult();
			
			// 现场巡航（海巡艇或执法车）
			if (cruiseTaskName != null && (cruiseTaskName.contains("海巡艇") || cruiseTaskName.contains("执法车"))) {
				// 发现异常
				if ("异常".equals(inspectionResult) || "立案调查".equals(inspectionResult)) {
					deptResult.put("fieldPatrolAbnormal", ((Long)deptResult.get("fieldPatrolAbnormal")) + 1);
					totalResult.put("fieldPatrolAbnormal", ((Long)totalResult.get("fieldPatrolAbnormal")) + 1);
				}
				// 立案调查
				if ("立案调查".equals(inspectionResult)) {
					deptResult.put("fieldPatrolCase", ((Long)deptResult.get("fieldPatrolCase")) + 1);
					totalResult.put("fieldPatrolCase", ((Long)totalResult.get("fieldPatrolCase")) + 1);
				}
			}
			
			// 电子巡航
			if (cruiseTaskName != null && cruiseTaskName.contains("电子")) {
				// 发现异常
				if ("异常".equals(inspectionResult) || "立案调查".equals(inspectionResult)) {
					deptResult.put("electronicPatrolAbnormal", ((Long)deptResult.get("electronicPatrolAbnormal")) + 1);
					totalResult.put("electronicPatrolAbnormal", ((Long)totalResult.get("electronicPatrolAbnormal")) + 1);
				}
				// 立案调查
				if ("立案调查".equals(inspectionResult)) {
					deptResult.put("electronicPatrolCase", ((Long)deptResult.get("electronicPatrolCase")) + 1);
					totalResult.put("electronicPatrolCase", ((Long)totalResult.get("electronicPatrolCase")) + 1);
				}
			}
			
			// 无人机巡航
			if (cruiseTaskName != null && cruiseTaskName.contains("无人机")) {
				// 发现异常
				if ("异常".equals(inspectionResult) || "立案调查".equals(inspectionResult)) {
					deptResult.put("dronePatrolAbnormal", ((Long)deptResult.get("dronePatrolAbnormal")) + 1);
					totalResult.put("dronePatrolAbnormal", ((Long)totalResult.get("dronePatrolAbnormal")) + 1);
				}
				// 立案调查
				if ("立案调查".equals(inspectionResult)) {
					deptResult.put("dronePatrolCase", ((Long)deptResult.get("dronePatrolCase")) + 1);
					totalResult.put("dronePatrolCase", ((Long)totalResult.get("dronePatrolCase")) + 1);
				}
			}
		}
		
		return results;
	}

	/**
	 * 创建空的结果Map
	 */
	private Map<String, Object> createEmptyResultMap() {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("fieldPatrolAbnormal", 0L);     // 现场巡航发现异常
		resultMap.put("fieldPatrolCase", 0L);         // 现场巡航立案调查
		resultMap.put("electronicPatrolAbnormal", 0L); // 电子巡航发现异常
		resultMap.put("electronicPatrolCase", 0L);     // 电子巡航立案调查
		resultMap.put("dronePatrolAbnormal", 0L);      // 无人机巡航发现异常
		resultMap.put("dronePatrolCase", 0L);          // 无人机巡航立案调查
		return resultMap;
	}

	/**
	 * 日期类型转换工具方法
	 */
	private Date convertToDate(Object dateObj) {
		if (dateObj == null) {
			return null;
		}
		if (dateObj instanceof Date) {
			return (Date) dateObj;
		}
		if (dateObj instanceof LocalDateTime) {
			LocalDateTime ldt = (LocalDateTime) dateObj;
			return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		}
		if (dateObj instanceof java.sql.Timestamp) {
			return new Date(((java.sql.Timestamp) dateObj).getTime());
		}
		return null;
	}

	/**
	 * 安全获取字符串值
	 */
	private String safeGetString(Map<String, Object> data, String key) {
		Object value = data.get(key);
		return value != null ? value.toString() : "";
	}
	
	/**
	 * 获取检查项目列表（周报表，带部门筛选）
	 */
	@RequestMapping(value = "getMajorItemNames")
	@ResponseBody
	public List<String> getMajorItemNames() {
		// 创建查询条件，使用硬编码的部门映射筛选
		DynamicEnforcementData query = new DynamicEnforcementData();
		
		// 只查询属于指定部门的数据
		List<String> allowedUnits = Arrays.asList(
			"西港区快反处置中心", "六号快反执法单元", "五号快反执法单元",
			"东港区快反处置中心", "一号快反执法单元", "二号快反执法单元", "三号快反执法单元",
			"保税区快反处置中心", "四号快反执法单元",
			"福姜沙快反处置中心", "七号快反执法单元"
		);
		query.sqlMap().getWhere().and("a.inspection_unit", QueryType.IN, allowedUnits);
		
		// 查询数据
		List<DynamicEnforcementData> dataList = dynamicEnforcementDataService.findList(query);
		
		// 提取不重复的检查项目名称
		Set<String> itemNames = new TreeSet<>();
		for (DynamicEnforcementData data : dataList) {
			if (data.getMajorItemName() != null && !data.getMajorItemName().trim().isEmpty()) {
				itemNames.add(data.getMajorItemName().trim());
			}
		}
		
		return new ArrayList<>(itemNames);
	}
	
	/**
	 * 获取动态执法数据详情（用于高级筛选和图表展示）
	 */
	@RequestMapping(value = "getDetailedEnforcementData")
	@ResponseBody
	public Map<String, Object> getDetailedEnforcementData(
			String startDate, 
			String endDate, 
			String inspectionUnit, 
			String inspectionResult, 
			String cruiseTaskType, 
			String majorItemName) {
		
		Map<String, Object> result = new HashMap<>();
		
		// 参数处理，默认为上周五到本周四
		LocalDate today = LocalDate.now();
		LocalDate lastFriday = today.minusDays(today.getDayOfWeek().getValue() + 2) // 回到上周五
				.plusDays(today.getDayOfWeek().getValue() <= 5 ? 0 : 7); // 如果今天是周末，则取上上周五
		LocalDate thisThursday = lastFriday.plusDays(6); // 本周四
		
		Date start = startDate != null && !startDate.isEmpty() ? 
				DateUtils.parseDate(startDate) : 
				DateUtils.parseDate(lastFriday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		Date end = endDate != null && !endDate.isEmpty() ? 
				DateUtils.parseDate(endDate) : 
				DateUtils.parseDate(thisThursday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		
		// 构建查询条件
		DynamicEnforcementData query = new DynamicEnforcementData();
		
		// 直接设置时间范围条件，确保基本条件正确
		query.setInspectionTime_gte(start);
		query.setInspectionTime_lte(end);
		
		// 部门筛选
		if (inspectionUnit != null && !inspectionUnit.isEmpty() && !"全部".equals(inspectionUnit)) {
			// 创建反向映射，用于查找每个部门对应的快反执法单元
			Map<String, List<String>> reverseDeptMapping = new HashMap<>();
			reverseDeptMapping.put("港区海事处", Arrays.asList("西港区快反处置中心", "六号快反执法单元", "五号快反执法单元"));
			reverseDeptMapping.put("锦丰海事处", Arrays.asList("东港区快反处置中心", "一号快反执法单元", "二号快反执法单元", "三号快反执法单元"));
			reverseDeptMapping.put("保税区海巡执法大队", Arrays.asList("保税区快反处置中心", "四号快反执法单元"));
			reverseDeptMapping.put("海巡执法支队", Arrays.asList("福姜沙快反处置中心", "七号快反执法单元"));
			
			// 获取选择部门对应的快反执法单元列表
			List<String> enforcementUnits = reverseDeptMapping.get(inspectionUnit);
			
			if (enforcementUnits != null && !enforcementUnits.isEmpty()) {
				// 使用指定部门的执法单元
				query.sqlMap().getWhere().and("a.inspection_unit", QueryType.IN, enforcementUnits);
			}
		} else {
			// 基础部门筛选：只查询属于指定部门的数据
			List<String> allowedUnits = Arrays.asList(
				"西港区快反处置中心", "六号快反执法单元", "五号快反执法单元",
				"东港区快反处置中心", "一号快反执法单元", "二号快反执法单元", "三号快反执法单元",
				"保税区快反处置中心", "四号快反执法单元",
				"福姜沙快反处置中心", "七号快反执法单元"
			);
			query.sqlMap().getWhere().and("a.inspection_unit", QueryType.IN, allowedUnits);
		}
		
		// 处理意见筛选
		if (inspectionResult != null && !inspectionResult.isEmpty() && !"全部".equals(inspectionResult)) {
			query.setInspectionResult(inspectionResult);
		}
		
		// 巡航方式筛选
		if (cruiseTaskType != null && !cruiseTaskType.isEmpty() && !"全部".equals(cruiseTaskType)) {
			if ("现场巡查".equals(cruiseTaskType)) {
				// 使用正确的链式调用方式
				query.sqlMap().getWhere()
					.andBracket("a.cruise_task_name", QueryType.LIKE, "%海巡艇%",1)
					.or("a.cruise_task_name", QueryType.LIKE, "%执法车%",2)
					.endBracket();
			} else if ("电子巡航".equals(cruiseTaskType)) {
				query.sqlMap().getWhere().and("a.cruise_task_name", QueryType.LIKE, "%电子%");
			} else if ("无人机巡航".equals(cruiseTaskType)) {
				query.sqlMap().getWhere().and("a.cruise_task_name", QueryType.LIKE, "%无人机%");
			}
		}
		
		// 检查项目筛选
		if (majorItemName != null && !majorItemName.isEmpty() && !"全部".equals(majorItemName)) {
			query.setMajorItemName(majorItemName);
		}
		
		// 查询数据
		List<DynamicEnforcementData> dataList = dynamicEnforcementDataService.findList(query);
		
		// 对数据进行去重处理
		Set<String> uniqueKeys = new HashSet<>();
		List<DynamicEnforcementData> dedupDataList = new ArrayList<>();
		
		for (DynamicEnforcementData data : dataList) {
			// 构建去重键：检查单位+检查时间(日期部分)+检查对象+大项名称+巡航任务名称
			String unit = data.getInspectionUnit() != null ? data.getInspectionUnit() : "";
			String time = data.getInspectionTime() != null ? 
					DateUtils.formatDate(data.getInspectionTime(), "yyyy-MM-dd") : "";
			String target = data.getInspectionTarget() != null ? data.getInspectionTarget() : "";
			String item = data.getMajorItemName() != null ? data.getMajorItemName() : "";
			String task = data.getCruiseTaskName() != null ? data.getCruiseTaskName() : "";
			
			String uniqueKey = unit + "|" + time + "|" + target + "|" + item + "|" + task;
			
			// 如果这个组合没出现过，则加入去重后的列表
			if (!uniqueKeys.contains(uniqueKey)) {
				uniqueKeys.add(uniqueKey);
				dedupDataList.add(data);
			}
		}
		
		// 1. 统计检查项目分布
		Map<String, Integer> majorItemStats = new HashMap<>();
		for (DynamicEnforcementData data : dedupDataList) {
			String majorItem = data.getMajorItemName();
			if (majorItem != null && !majorItem.isEmpty()) {
				majorItemStats.put(majorItem, majorItemStats.getOrDefault(majorItem, 0) + 1);
			}
		}
		
		// 排序取前15个
		List<Map.Entry<String, Integer>> sortedMajorItems = new ArrayList<>(majorItemStats.entrySet());
		sortedMajorItems.sort((a, b) -> b.getValue() - a.getValue());
		
		List<String> topMajorItems = new ArrayList<>();
		List<Integer> topMajorItemCounts = new ArrayList<>();
		
		int count = 0;
		for (Map.Entry<String, Integer> entry : sortedMajorItems) {
			if (count++ < 15) {
				topMajorItems.add(entry.getKey());
				topMajorItemCounts.add(entry.getValue());
			} else {
				break;
			}
		}
		
		// 2. 统计检查数量趋势
		Map<String, Integer> dailyStats = new HashMap<>();
		Map<String, Integer> weeklyStats = new HashMap<>();
		Map<String, Integer> monthlyStats = new HashMap<>();
		Map<String, Integer> quarterlyStats = new HashMap<>();
		
		for (DynamicEnforcementData data : dedupDataList) {
			Date inspectionTime = data.getInspectionTime();
			if (inspectionTime != null) {
				// 日统计
				String dailyKey = DateUtils.formatDate(inspectionTime, "yyyy-MM-dd");
				dailyStats.put(dailyKey, dailyStats.getOrDefault(dailyKey, 0) + 1);
				
				// 周统计 (以周一为开始)
				Calendar cal = Calendar.getInstance();
				cal.setTime(inspectionTime);
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
				String weeklyKey = cal.get(Calendar.YEAR) + "-W" + weekOfYear;
				weeklyStats.put(weeklyKey, weeklyStats.getOrDefault(weeklyKey, 0) + 1);
				
				// 月统计
				String monthlyKey = DateUtils.formatDate(inspectionTime, "yyyy-MM");
				monthlyStats.put(monthlyKey, monthlyStats.getOrDefault(monthlyKey, 0) + 1);
				
				// 季度统计
				int month = cal.get(Calendar.MONTH);
				int quarter = month / 3 + 1;
				String quarterlyKey = cal.get(Calendar.YEAR) + "-Q" + quarter;
				quarterlyStats.put(quarterlyKey, quarterlyStats.getOrDefault(quarterlyKey, 0) + 1);
			}
		}
		
		// 对各个时间段的统计结果进行排序
		List<Map<String, Object>> dailyTrend = convertToSortedList(dailyStats);
		List<Map<String, Object>> weeklyTrend = convertToSortedList(weeklyStats);
		List<Map<String, Object>> monthlyTrend = convertToSortedList(monthlyStats);
		List<Map<String, Object>> quarterlyTrend = convertToSortedList(quarterlyStats);
		
		// 3. 统计各部门检查量
		Map<String, Integer> deptStats = new HashMap<>();
		Map<String, String> departmentMapping = new HashMap<>();
		departmentMapping.put("西港区快反处置中心", "港区海事处");
		departmentMapping.put("六号快反执法单元", "港区海事处");
		departmentMapping.put("五号快反执法单元", "港区海事处");
		departmentMapping.put("东港区快反处置中心", "锦丰海事处");
		departmentMapping.put("一号快反执法单元", "锦丰海事处");
		departmentMapping.put("二号快反执法单元", "锦丰海事处");
		departmentMapping.put("三号快反执法单元", "锦丰海事处");
		departmentMapping.put("保税区快反处置中心", "保税区海巡执法大队");
		departmentMapping.put("四号快反执法单元", "保税区海巡执法大队");
		departmentMapping.put("福姜沙快反处置中心", "海巡执法支队");
		departmentMapping.put("七号快反执法单元", "海巡执法支队");

		for (DynamicEnforcementData data : dedupDataList) {
			String unit = data.getInspectionUnit();
			if (unit != null && !unit.isEmpty()) {
				// 应用部门映射
				String mappedDept = departmentMapping.getOrDefault(unit, unit);
				deptStats.put(mappedDept, deptStats.getOrDefault(mappedDept, 0) + 1);
			}
		}
		
		// 构建返回结果
		result.put("majorItemNames", topMajorItems);
		result.put("majorItemCounts", topMajorItemCounts);
		result.put("dailyTrend", dailyTrend);
		result.put("weeklyTrend", weeklyTrend);
		result.put("monthlyTrend", monthlyTrend);
		result.put("quarterlyTrend", quarterlyTrend);
		result.put("departmentStats", deptStats);
		result.put("totalCount", dedupDataList.size());
		result.put("startDate", DateUtils.formatDate(start, "yyyy-MM-dd"));
		result.put("endDate", DateUtils.formatDate(end, "yyyy-MM-dd"));

		return result;
	}

	// 辅助方法：将Map转换为排序的List
	private List<Map<String, Object>> convertToSortedList(Map<String, Integer> statsMap) {
		List<Map<String, Object>> result = new ArrayList<>();
		List<String> sortedKeys = new ArrayList<>(statsMap.keySet());
		Collections.sort(sortedKeys);
		
		for (String key : sortedKeys) {
			Map<String, Object> item = new HashMap<>();
			item.put("time", key);
			item.put("count", statsMap.get(key));
			result.add(item);
		}

		return result;
	}

	/**
	 * 获取详细记录数据（用于图表点击时显示）
	 */
	@RequestMapping(value = "getDetailRecords")
	@ResponseBody
	public List<DynamicEnforcementData> getDetailRecords(
			String startDate, 
			String endDate, 
			String patrolType, 
			String department,
			String inspectionResult,
			String isPeriod,
			String majorItemName) {
		
		// 参数处理
		Date start = startDate != null && !startDate.isEmpty() ? 
				DateUtils.parseDate(startDate) : null;
		Date end = endDate != null && !endDate.isEmpty() ? 
				DateUtils.parseDate(endDate) : null;
		
		// 如果是上期数据，需要计算上期的日期范围
		if ("previous".equals(isPeriod) && start != null && end != null) {
			long periodDays = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000) + 1;
			end = new Date(start.getTime() - 24 * 60 * 60 * 1000);
			start = new Date(end.getTime() - periodDays * 24 * 60 * 60 * 1000 + 24 * 60 * 60 * 1000);
		}
		
		// 构建查询条件
		DynamicEnforcementData query = new DynamicEnforcementData();
		
		// 设置日期范围
		if (start != null) {
			query.setInspectionTime_gte(start);
		}
		if (end != null) {
			query.setInspectionTime_lte(end);
		}
		
		// 部门筛选
		if (department != null && !department.isEmpty() && !"全部".equals(department)) {
			// 创建反向映射，用于查找每个部门对应的快反执法单元
			Map<String, List<String>> reverseDeptMapping = new HashMap<>();
			reverseDeptMapping.put("港区海事处", Arrays.asList("西港区快反处置中心", "六号快反执法单元", "五号快反执法单元"));
			reverseDeptMapping.put("锦丰海事处", Arrays.asList("东港区快反处置中心", "一号快反执法单元", "二号快反执法单元", "三号快反执法单元"));
			reverseDeptMapping.put("保税区海巡执法大队", Arrays.asList("保税区快反处置中心", "四号快反执法单元"));
			reverseDeptMapping.put("海巡执法支队", Arrays.asList("福姜沙快反处置中心", "七号快反执法单元"));
			
			// 获取选择部门对应的快反执法单元列表
			List<String> enforcementUnits = reverseDeptMapping.get(department);
			
			if (enforcementUnits != null && !enforcementUnits.isEmpty()) {
				// 使用WHERE IN查询方式
				query.sqlMap().getWhere().and("a.inspection_unit", QueryType.IN, enforcementUnits);
			}
		}
		
		// 巡航方式筛选
		if (patrolType != null && !patrolType.isEmpty()) {
			if ("现场巡查".equals(patrolType)) {
				// 使用有参数的andBracket方法
				query.sqlMap().getWhere().andBracket("a.cruise_task_name", QueryType.LIKE, "%海巡艇%",1);
				query.sqlMap().getWhere().or("a.cruise_task_name", QueryType.LIKE, "%执法车%",2);
				query.sqlMap().getWhere().endBracket();
			} else if ("电子巡航".equals(patrolType)) {
				query.sqlMap().getWhere().and("a.cruise_task_name", QueryType.LIKE, "%电子%");
			} else if ("无人机巡航".equals(patrolType)) {
				query.sqlMap().getWhere().and("a.cruise_task_name", QueryType.LIKE, "%无人机%");
			}
		}
		
		// 检查结果筛选
		if (inspectionResult != null && !inspectionResult.isEmpty()) {
			query.setInspectionResult(inspectionResult);
		}
		
		// 检查项目筛选
		if (majorItemName != null && !majorItemName.isEmpty() && !"全部".equals(majorItemName)) {
			query.setMajorItemName(majorItemName);
		}
		
		// 查询数据并返回
		List<DynamicEnforcementData> resultList = dynamicEnforcementDataService.findList(query);
		
		// 处理检查结果为空的情况
		for (DynamicEnforcementData data : resultList) {
			if (data.getInspectionResult() == null || data.getInspectionResult().isEmpty()) {
				data.setInspectionResult("正常");
			}
		}
		
		return resultList;
	}

	/**
	 * 月度动态执法图表页面
	 */
	@RequestMapping(value = "monthlyChart")
	public String monthlyChart() {
		return "data_collect/dynamic/monthlyDynamicChart";
	}

	/**
	 * 获取月度动态执法数据（与agency_dept表关联）
	 */
	@RequestMapping(value = "getMonthlyDynamicEnforcementData")
	@ResponseBody
	public Map<String, Object> getMonthlyDynamicEnforcementData(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		// 参数处理，默认为月度报表逻辑（25号为分界点）
		LocalDate today = LocalDate.now();
		int currentDay = today.getDayOfMonth();
		
		LocalDate endDate_calc, startDate_calc;
		if (currentDay <= 25) {
			// 当前日期为本月25日之前，则为上上月的26至上月的25日
			endDate_calc = today.withDayOfMonth(1).minusMonths(1).withDayOfMonth(25);
			startDate_calc = today.withDayOfMonth(1).minusMonths(2).withDayOfMonth(26);
		} else {
			// 当前日期为本月25日之后，则为上月的26至本月的25日
			endDate_calc = today.withDayOfMonth(25);
			startDate_calc = today.withDayOfMonth(1).minusMonths(1).withDayOfMonth(26);
		}
		
		Date start = startDate != null && !startDate.isEmpty() ? 
				DateUtils.parseDate(startDate) : 
				DateUtils.parseDate(startDate_calc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		Date end = endDate != null && !endDate.isEmpty() ? 
				DateUtils.parseDate(endDate) : 
				DateUtils.parseDate(endDate_calc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		
		// 计算上一个周期
		long periodDays = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000) + 1;
		Date lastStart = new Date(start.getTime() - periodDays * 24 * 60 * 60 * 1000);
		Date lastEnd = new Date(start.getTime() - 24 * 60 * 60 * 1000);
		
		// 获取当前周期数据
		Map<String, Map<String, Object>> currentData = calculateMonthlyEnforcementData(start, end);
		// 获取上一周期数据
		Map<String, Map<String, Object>> previousData = calculateMonthlyEnforcementData(lastStart, lastEnd);
		
		result.put("currentData", currentData);
		result.put("previousData", previousData);
		result.put("startDate", DateUtils.formatDate(start, "yyyy-MM-dd"));
		result.put("endDate", DateUtils.formatDate(end, "yyyy-MM-dd"));

		return result;
	}

	/**
	 * 计算月度动态执法数据统计（与agency_dept表关联）
	 */
	private Map<String, Map<String, Object>> calculateMonthlyEnforcementData(Date startDate, Date endDate) {
		// 初始化结果集 - 使用agency_dept表获取部门列表
		Map<String, Map<String, Object>> results = new HashMap<>();
		
		// 查询所有部门，排除江苏海事局
		String deptSql = "SELECT DISTINCT dept FROM agency_dept WHERE dept IS NOT NULL AND dept != '' AND dept != '江苏海事局' ORDER BY dept";
		List<String> departments = dynamicEnforcementDataService.findListBySql(deptSql, null, String.class);
		
		// 初始化所有部门的结果（张家港海事局也在departments列表中，作为普通部门）
		for (String dept : departments) {
			results.put(dept, createEmptyResultMap());
		}
		
		// 获取查询时间范围内的所有数据，与agency_dept表关联
		String sql = "SELECT ded.*, ad.dept " +
					"FROM dynamic_enforcement_data ded " +
					"LEFT JOIN agency_dept ad ON ded.inspection_unit = ad.agency " +
					"WHERE ded.inspection_time >= ? AND ded.inspection_time <= ? " +
					"AND ad.dept IS NOT NULL AND ad.dept != '江苏海事局' " +
					"ORDER BY ded.inspection_time";
		
		// 执行原生SQL查询
		List<Map<String, Object>> rawDataList = jdbcTemplate.queryForList(sql, startDate, endDate);
		
		// 对数据进行去重处理
		Set<String> uniqueKeys = new HashSet<>();
		List<Map<String, Object>> dedupDataList = new ArrayList<>();
		
		for (Map<String, Object> data : rawDataList) {
			// 构建去重键：检查单位+检查时间(日期部分)+检查对象+大项名称+巡航任务名称
			String unit = safeGetString(data, "inspection_unit");
			String time = "";
			Date inspectionTime = convertToDate(data.get("inspection_time"));
			if (inspectionTime != null) {
				time = DateUtils.formatDate(inspectionTime, "yyyy-MM-dd");
			}
			String target = safeGetString(data, "inspection_target");
			String item = safeGetString(data, "major_item_name");
			String task = safeGetString(data, "cruise_task_name");
			
			String uniqueKey = unit + "|" + time + "|" + target + "|" + item + "|" + task;
			
			// 如果这个组合没出现过，则加入去重后的列表
			if (!uniqueKeys.contains(uniqueKey)) {
				uniqueKeys.add(uniqueKey);
				dedupDataList.add(data);
			}
		}
		
		// 使用去重后的数据进行统计
		for (Map<String, Object> data : dedupDataList) {
			String department = safeGetString(data, "dept");
			
			// 如果部门不在结果集中，则跳过
			if (!results.containsKey(department)) {
				continue;
			}
			
			// 获取该部门的统计结果（只累加到具体部门，不再累加到总计项）
			Map<String, Object> deptResult = results.get(department);
			
			// 判断巡航类型和结果
			String cruiseTaskName = safeGetString(data, "cruise_task_name");
			String inspectionResult = safeGetString(data, "inspection_result");
			
			// 现场巡航（海巡艇或执法车）
			if (cruiseTaskName != null && (cruiseTaskName.contains("海巡艇") || cruiseTaskName.contains("执法车"))) {
				// 发现异常
				if ("异常".equals(inspectionResult) || "立案调查".equals(inspectionResult)) {
					deptResult.put("fieldPatrolAbnormal", ((Long)deptResult.get("fieldPatrolAbnormal")) + 1);
				}
				// 立案调查
				if ("立案调查".equals(inspectionResult)) {
					deptResult.put("fieldPatrolCase", ((Long)deptResult.get("fieldPatrolCase")) + 1);
				}
			}
			
			// 电子巡航
			if (cruiseTaskName != null && cruiseTaskName.contains("电子")) {
				// 发现异常
				if ("异常".equals(inspectionResult) || "立案调查".equals(inspectionResult)) {
					deptResult.put("electronicPatrolAbnormal", ((Long)deptResult.get("electronicPatrolAbnormal")) + 1);
				}
				// 立案调查
				if ("立案调查".equals(inspectionResult)) {
					deptResult.put("electronicPatrolCase", ((Long)deptResult.get("electronicPatrolCase")) + 1);
				}
			}
			
			// 无人机巡航
			if (cruiseTaskName != null && cruiseTaskName.contains("无人机")) {
				// 发现异常
				if ("异常".equals(inspectionResult) || "立案调查".equals(inspectionResult)) {
					deptResult.put("dronePatrolAbnormal", ((Long)deptResult.get("dronePatrolAbnormal")) + 1);
				}
				// 立案调查
				if ("立案调查".equals(inspectionResult)) {
					deptResult.put("dronePatrolCase", ((Long)deptResult.get("dronePatrolCase")) + 1);
				}
			}
		}
		
		return results;
	}
	
	/**
	 * 获取月度部门列表
	 */
	@RequestMapping(value = "getMonthlyDepartments")
	@ResponseBody
	public List<String> getMonthlyDepartments() {
		// 查询所有部门，排除江苏海事局
		String sql = "SELECT DISTINCT dept FROM agency_dept WHERE dept IS NOT NULL AND dept != '' AND dept != '江苏海事局' ORDER BY dept";
		return dynamicEnforcementDataService.findListBySql(sql, null, String.class);
	}
	
	/**
	 * 获取月度检查项目列表
	 */
	@RequestMapping(value = "getMonthlyMajorItemNames")
	@ResponseBody
	public List<String> getMonthlyMajorItemNames() {
		// 创建SQL查询，与agency_dept表关联，排除江苏海事局
		String sql = "SELECT DISTINCT ded.major_item_name " +
					"FROM dynamic_enforcement_data ded " +
					"LEFT JOIN agency_dept ad ON ded.inspection_unit = ad.agency " +
					"WHERE ded.major_item_name IS NOT NULL AND ded.major_item_name != '' " +
					"AND ad.dept IS NOT NULL AND ad.dept != '江苏海事局' " +
					"ORDER BY ded.major_item_name";
		return dynamicEnforcementDataService.findListBySql(sql, null, String.class);
	}
	
	/**
	 * 获取月度动态执法数据详情（用于高级筛选和图表展示）
	 */
	@RequestMapping(value = "getMonthlyDetailedEnforcementData")
	@ResponseBody
	public Map<String, Object> getMonthlyDetailedEnforcementData(
			String startDate, 
			String endDate, 
			String inspectionUnit, 
			String inspectionResult, 
			String cruiseTaskType, 
			String majorItemName) {
		
		Map<String, Object> result = new HashMap<>();
		
		// 参数处理，默认为月度报表逻辑
		LocalDate today = LocalDate.now();
		int currentDay = today.getDayOfMonth();
		
		LocalDate endDate_calc, startDate_calc;
		if (currentDay <= 25) {
			endDate_calc = today.withDayOfMonth(1).minusMonths(1).withDayOfMonth(25);
			startDate_calc = today.withDayOfMonth(1).minusMonths(2).withDayOfMonth(26);
		} else {
			endDate_calc = today.withDayOfMonth(25);
			startDate_calc = today.withDayOfMonth(1).minusMonths(1).withDayOfMonth(26);
		}
		
		Date start = startDate != null && !startDate.isEmpty() ? 
				DateUtils.parseDate(startDate) : 
				DateUtils.parseDate(startDate_calc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		Date end = endDate != null && !endDate.isEmpty() ? 
				DateUtils.parseDate(endDate) : 
				DateUtils.parseDate(endDate_calc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		
		// 构建SQL查询条件
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ded.*, ad.dept ");
		sql.append("FROM dynamic_enforcement_data ded ");
		sql.append("LEFT JOIN agency_dept ad ON ded.inspection_unit = ad.agency ");
		sql.append("WHERE ded.inspection_time >= ? AND ded.inspection_time <= ? ");
		sql.append("AND ad.dept IS NOT NULL AND ad.dept != '江苏海事局' ");
		
		List<Object> params = new ArrayList<>();
		params.add(start);
		params.add(end);
		
		// 部门筛选
		if (inspectionUnit != null && !inspectionUnit.isEmpty() && !"全部".equals(inspectionUnit)) {
			sql.append("AND ad.dept = ? ");
			params.add(inspectionUnit);
		}
		
		// 处理意见筛选
		if (inspectionResult != null && !inspectionResult.isEmpty() && !"全部".equals(inspectionResult)) {
			sql.append("AND ded.inspection_result = ? ");
			params.add(inspectionResult);
		}
		
		// 巡航方式筛选
		if (cruiseTaskType != null && !cruiseTaskType.isEmpty() && !"全部".equals(cruiseTaskType)) {
			if ("现场巡查".equals(cruiseTaskType)) {
				sql.append("AND (ded.cruise_task_name LIKE ? OR ded.cruise_task_name LIKE ?) ");
				params.add("%海巡艇%");
				params.add("%执法车%");
			} else if ("电子巡航".equals(cruiseTaskType)) {
				sql.append("AND ded.cruise_task_name LIKE ? ");
				params.add("%电子%");
			} else if ("无人机巡航".equals(cruiseTaskType)) {
				sql.append("AND ded.cruise_task_name LIKE ? ");
				params.add("%无人机%");
			}
		}
		
		// 检查项目筛选
		if (majorItemName != null && !majorItemName.isEmpty() && !"全部".equals(majorItemName)) {
			sql.append("AND ded.major_item_name = ? ");
			params.add(majorItemName);
		}
		
		sql.append("ORDER BY ded.inspection_time");
		
		// 执行查询
		List<Map<String, Object>> rawDataList = jdbcTemplate.queryForList(sql.toString(), params.toArray());
		
		// 对数据进行去重处理
		Set<String> uniqueKeys = new HashSet<>();
		List<Map<String, Object>> dedupDataList = new ArrayList<>();
		
		for (Map<String, Object> data : rawDataList) {
			String unit = safeGetString(data, "inspection_unit");
			String time = "";
			Date inspectionTime = convertToDate(data.get("inspection_time"));
			if (inspectionTime != null) {
				time = DateUtils.formatDate(inspectionTime, "yyyy-MM-dd");
			}
			String target = safeGetString(data, "inspection_target");
			String item = safeGetString(data, "major_item_name");
			String task = safeGetString(data, "cruise_task_name");
			
			String uniqueKey = unit + "|" + time + "|" + target + "|" + item + "|" + task;
			
			if (!uniqueKeys.contains(uniqueKey)) {
				uniqueKeys.add(uniqueKey);
				dedupDataList.add(data);
			}
		}
		
		// 1. 统计检查项目分布
		Map<String, Integer> majorItemStats = new HashMap<>();
		for (Map<String, Object> data : dedupDataList) {
			String itemName = safeGetString(data, "major_item_name");
			if (!itemName.isEmpty()) {
				majorItemStats.put(itemName, majorItemStats.getOrDefault(itemName, 0) + 1);
			}
		}
		
		// 按检查次数排序并取前15项
		List<Map.Entry<String, Integer>> sortedMajorItems = new ArrayList<>(majorItemStats.entrySet());
		sortedMajorItems.sort((a, b) -> b.getValue().compareTo(a.getValue()));
		
		List<String> topMajorItems = new ArrayList<>();
		List<Integer> topMajorItemCounts = new ArrayList<>();
		
		int count = 0;
		for (Map.Entry<String, Integer> entry : sortedMajorItems) {
			if (count++ < 15) {
				topMajorItems.add(entry.getKey());
				topMajorItemCounts.add(entry.getValue());
			} else {
				break;
			}
		}
		
		// 2. 统计检查数量趋势
		Map<String, Integer> dailyStats = new HashMap<>();
		Map<String, Integer> weeklyStats = new HashMap<>();
		Map<String, Integer> monthlyStats = new HashMap<>();
		Map<String, Integer> quarterlyStats = new HashMap<>();
		
		for (Map<String, Object> data : dedupDataList) {
			Date inspectionTime = convertToDate(data.get("inspection_time"));
			if (inspectionTime != null) {
				// 日统计
				String dailyKey = DateUtils.formatDate(inspectionTime, "yyyy-MM-dd");
				dailyStats.put(dailyKey, dailyStats.getOrDefault(dailyKey, 0) + 1);
				
				// 周统计 (以周一为开始)
				Calendar cal = Calendar.getInstance();
				cal.setTime(inspectionTime);
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
				String weeklyKey = cal.get(Calendar.YEAR) + "-W" + weekOfYear;
				weeklyStats.put(weeklyKey, weeklyStats.getOrDefault(weeklyKey, 0) + 1);
				
				// 月统计
				String monthlyKey = DateUtils.formatDate(inspectionTime, "yyyy-MM");
				monthlyStats.put(monthlyKey, monthlyStats.getOrDefault(monthlyKey, 0) + 1);
				
				// 季度统计
				int month = cal.get(Calendar.MONTH);
				int quarter = month / 3 + 1;
				String quarterlyKey = cal.get(Calendar.YEAR) + "-Q" + quarter;
				quarterlyStats.put(quarterlyKey, quarterlyStats.getOrDefault(quarterlyKey, 0) + 1);
			}
		}
		
		// 对各个时间段的统计结果进行排序
		List<Map<String, Object>> dailyTrend = convertToSortedList(dailyStats);
		List<Map<String, Object>> weeklyTrend = convertToSortedList(weeklyStats);
		List<Map<String, Object>> monthlyTrend = convertToSortedList(monthlyStats);
		List<Map<String, Object>> quarterlyTrend = convertToSortedList(quarterlyStats);
		
		// 3. 统计各部门检查量
		Map<String, Integer> deptStats = new HashMap<>();
		for (Map<String, Object> data : dedupDataList) {
			String dept = safeGetString(data, "dept");
			if (!dept.isEmpty()) {
				deptStats.put(dept, deptStats.getOrDefault(dept, 0) + 1);
			}
		}
		
		// 构建返回结果
		result.put("majorItemNames", topMajorItems);
		result.put("majorItemCounts", topMajorItemCounts);
		result.put("dailyTrend", dailyTrend);
		result.put("weeklyTrend", weeklyTrend);
		result.put("monthlyTrend", monthlyTrend);
		result.put("quarterlyTrend", quarterlyTrend);
		result.put("departmentStats", deptStats);
		result.put("totalCount", dedupDataList.size());
		result.put("startDate", DateUtils.formatDate(start, "yyyy-MM-dd"));
		result.put("endDate", DateUtils.formatDate(end, "yyyy-MM-dd"));

		return result;
	}

	/**
	 * 获取月度详细记录数据（用于图表点击时显示）
	 */
	@RequestMapping(value = "getMonthlyDetailRecords")
	@ResponseBody
	public Map<String, Object> getMonthlyDetailRecords(
			String startDate, 
			String endDate, 
			String patrolType, 
			String department,
			String inspectionResult,
			String isPeriod,
			String majorItemName,
			Integer page,
			Integer size) {
		
		// 分页参数处理
		int pageNumber = page != null && page > 0 ? page : 1;
		int pageSize = size != null && size > 0 ? size : 20;
		int offset = (pageNumber - 1) * pageSize;
		
		// 参数处理
		Date start = startDate != null && !startDate.isEmpty() ? 
				DateUtils.parseDate(startDate) : null;
		Date end = endDate != null && !endDate.isEmpty() ? 
				DateUtils.parseDate(endDate) : null;
		
		// 如果是上期数据，需要计算上期的日期范围
		if ("previous".equals(isPeriod) && start != null && end != null) {
			long periodDays = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000) + 1;
			end = new Date(start.getTime() - 24 * 60 * 60 * 1000);
			start = new Date(end.getTime() - periodDays * 24 * 60 * 60 * 1000 + 24 * 60 * 60 * 1000);
		}
		
		// 构建基础SQL查询条件（用于计算总数）
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("WHERE ad.dept IS NOT NULL AND ad.dept != '江苏海事局' ");
		
		List<Object> params = new ArrayList<>();
		
		// 设置日期范围
		if (start != null) {
			whereClause.append("AND ded.inspection_time >= ? ");
			params.add(start);
		}
		if (end != null) {
			whereClause.append("AND ded.inspection_time <= ? ");
			params.add(end);
		}
		
		// 部门筛选
		if (department != null && !department.isEmpty() && !"全部".equals(department)) {
			whereClause.append("AND ad.dept = ? ");
			params.add(department);
		}
		
		// 巡航方式筛选
		if (patrolType != null && !patrolType.isEmpty()) {
			if ("现场巡查".equals(patrolType)) {
				whereClause.append("AND (ded.cruise_task_name LIKE ? OR ded.cruise_task_name LIKE ?) ");
				params.add("%海巡艇%");
				params.add("%执法车%");
			} else if ("电子巡航".equals(patrolType)) {
				whereClause.append("AND ded.cruise_task_name LIKE ? ");
				params.add("%电子%");
			} else if ("无人机巡航".equals(patrolType)) {
				whereClause.append("AND ded.cruise_task_name LIKE ? ");
				params.add("%无人机%");
			}
		}
		
		// 检查结果筛选
		if (inspectionResult != null && !inspectionResult.isEmpty()) {
			whereClause.append("AND ded.inspection_result = ? ");
			params.add(inspectionResult);
		}
		
		// 检查项目筛选
		if (majorItemName != null && !majorItemName.isEmpty() && !"全部".equals(majorItemName)) {
			whereClause.append("AND ded.major_item_name = ? ");
			params.add(majorItemName);
		}
		
		// 1. 先查询总记录数
		StringBuilder countSql = new StringBuilder();
		countSql.append("SELECT COUNT(*) ");
		countSql.append("FROM dynamic_enforcement_data ded ");
		countSql.append("LEFT JOIN agency_dept ad ON ded.inspection_unit = ad.agency ");
		countSql.append(whereClause.toString());
		
		Integer totalCount = jdbcTemplate.queryForObject(countSql.toString(), params.toArray(), Integer.class);
		
		// 2. 查询分页数据
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("SELECT ded.inspection_unit as inspectionUnit, ");
		dataSql.append("DATE_FORMAT(ded.inspection_time, '%Y-%m-%d %H:%i:%s') as inspectionTime, ");
		dataSql.append("ded.inspection_location as inspectionLocation, ");
		dataSql.append("ded.inspection_target as inspectionTarget, ");
		dataSql.append("ded.major_item_name as majorItemName, ");
		dataSql.append("ded.cruise_task_name as cruiseTaskName, ");
		dataSql.append("COALESCE(ded.inspection_result, '正常') as inspectionResult, ");
		dataSql.append("ad.dept ");
		dataSql.append("FROM dynamic_enforcement_data ded ");
		dataSql.append("LEFT JOIN agency_dept ad ON ded.inspection_unit = ad.agency ");
		dataSql.append(whereClause.toString());
		dataSql.append("ORDER BY ded.inspection_time DESC ");
		dataSql.append("LIMIT ? OFFSET ?");
		
		// 添加分页参数
		List<Object> dataParams = new ArrayList<>(params);
		dataParams.add(pageSize);
		dataParams.add(offset);
		
		// 执行查询
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(dataSql.toString(), dataParams.toArray());
		
		// 计算总页数
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		
		// 构建返回结果
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", resultList);
		resultMap.put("totalCount", totalCount);
		resultMap.put("totalPages", totalPages);
		resultMap.put("currentPage", pageNumber);
		resultMap.put("pageSize", pageSize);
		resultMap.put("hasNext", pageNumber < totalPages);
		resultMap.put("hasPrevious", pageNumber > 1);
		
		return resultMap;
	}
}