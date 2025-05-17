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
		// 部门映射配置
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
		
		// 遍历数据并统计
		for (DynamicEnforcementData data : dataList) {
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
}