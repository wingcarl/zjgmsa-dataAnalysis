package com.jeesite.modules.shipreport.web;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.modules.data_collect.dynamic.entity.DynamicEnforcementData;
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
import com.jeesite.modules.shipreport.entity.ShipReport;
import com.jeesite.modules.shipreport.service.ShipReportService;

/**
 * 船舶报告表Controller
 * @author 王浩宇
 * @version 2024-06-21
 */
@Controller
@RequestMapping(value = "${adminPath}/shipreport/shipReport")
public class ShipReportController extends BaseController {

	@Autowired
	private ShipReportService shipReportService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ShipReport get(String id, boolean isNew) {
		return shipReportService.get(id, isNew);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(ShipReport shipReport, Model model) {
		model.addAttribute("shipReport", shipReport);
		return "modules/shipreport/shipReportList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ShipReport> listDatalistData(ShipReport shipReport, HttpServletRequest request, HttpServletResponse response) {
		shipReport.setPage(new Page<>(request, response));
		Page<ShipReport> page = shipReportService.findPage(shipReport);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "form")
	public String form(ShipReport shipReport, Model model) {
		model.addAttribute("shipReport", shipReport);
		return "modules/shipreport/shipReportForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("shipreport:shipReport:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ShipReport shipReport) {
		shipReportService.save(shipReport);
		return renderResult(Global.TRUE, text("保存船舶报告表成功！"));
	}
	/**
	 * 导出数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "exportData")
	public void exportData(ShipReport shipReport, HttpServletResponse response) {
		List<ShipReport> list = shipReportService.findList(shipReport);
		String fileName = "船舶报告表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("船舶报告表", ShipReport.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		ShipReport shipReport = new ShipReport();
		List<ShipReport> list = ListUtils.newArrayList(shipReport);
		String fileName = "船舶报告表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("船舶报告表", ShipReport.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("shipreport:shipReport:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = shipReportService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	/**
	 * 删除数据
	 */
	@RequiresPermissions("shipreport:shipReport:edit")
	@PostMapping(value = "delete")
	@ResponseBody
	public String delete(ShipReport shipReport) {
		shipReportService.delete(shipReport);
		return renderResult(Global.TRUE, text("删除船舶报告表成功！"));
	}

	/**
	 * 数据分析页面
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "analysis")
	public String analysis(Model model) {
		return "modules/shipreport/shipReportAnalysis";
	}

	/**
	 * 获取统计数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "statisticsData")
	@ResponseBody
	public Map<String, Object> getStatisticsData(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);

			// 获取统计数据
			Map<String, Object> stats = shipReportService.getStatisticsData(start, end);
			
			result.put("status", "success");
			result.put("data", stats);
		} catch (Exception e) {
			logger.error("获取统计数据失败", e);
			result.put("status", "error");
			result.put("message", "获取统计数据失败: " + e.getMessage());
		}

		return result;
	}

	/**
	 * 获取筛选后的数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "filteredData")
	@ResponseBody
	public Map<String, Object> getFilteredData(String startDate, String endDate,
											  String portDirection, String shipType, String hazardous, String lengthRange,
											  String agency, String berth) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);
			
			// 获取筛选后的数据
			Map<String, Object> filteredStats = shipReportService.getFilteredStatistics(
					start, end, portDirection, shipType, hazardous, lengthRange, agency, berth);
			
			result.put("status", "success");
			result.put("data", filteredStats);
		} catch (Exception e) {
			logger.error("获取筛选数据失败", e);
			result.put("status", "error");
			result.put("message", "获取筛选数据失败: " + e.getMessage());
		}

		return result;
	}

	/**
	 * 获取趋势数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "trendData")
	@ResponseBody
	public Map<String, Object> getTrendData(String startDate, String endDate, String interval,
											String portDirection, String shipType, String hazardous, String lengthRange,
										   String agency, String berth) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);
			
			// 获取趋势数据
			List<Map<String, Object>> trendData = shipReportService.getTrendData(
					start, end, interval, portDirection, shipType, hazardous, lengthRange, agency, berth);
			
			result.put("status", "success");
			result.put("data", trendData);
		} catch (Exception e) {
			logger.error("获取趋势数据失败", e);
			result.put("status", "error");
			result.put("message", "获取趋势数据失败: " + e.getMessage());
		}

		return result;
	}

	/**
	 * 获取独立的海事机构列表
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "agencies")
	@ResponseBody
	public Map<String, Object> getAgencies(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);
			
			// 获取海事机构列表
			List<String> agencies = shipReportService.getDistinctAgencies(start, end);
			
			result.put("status", "success");
			result.put("data", agencies);
		} catch (Exception e) {
			logger.error("获取海事机构列表失败", e);
			result.put("status", "error");
			result.put("message", "获取海事机构列表失败: " + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取独立的泊位列表
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "berths")
	@ResponseBody
	public Map<String, Object> getBerths(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);
			
			// 获取泊位列表
			List<String> berths = shipReportService.getDistinctBerths(start, end);
			
			result.put("status", "success");
			result.put("data", berths);
		} catch (Exception e) {
			logger.error("获取泊位列表失败", e);
				result.put("status", "error");
			result.put("message", "获取泊位列表失败: " + e.getMessage());
		}
		
				return result;
			}

	/**
	 * 获取船舶类型分布数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "shipTypeDistribution")
	@ResponseBody
	public Map<String, Object> getShipTypeDistribution(String startDate, String endDate,
													  String portDirection, String shipType, String hazardous, String lengthRange,
													  String agency, String berth) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);

			// 获取船舶类型分布数据
			List<Map<String, Object>> typeData = shipReportService.getShipTypeDistribution(
					start, end, portDirection, shipType, hazardous, lengthRange, agency, berth);
			
			result.put("status", "success");
			result.put("data", processTopNAndOthers(typeData, 9)); // 处理前9名和"其他"
		} catch (Exception e) {
			logger.error("获取船舶类型分布数据失败", e);
			result.put("status", "error");
			result.put("message", "获取船舶类型分布数据失败: " + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取货物类型分布数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "cargoTypeDistribution")
	@ResponseBody
	public Map<String, Object> getCargoTypeDistribution(String startDate, String endDate,
													   String portDirection, String shipType, String hazardous, String lengthRange,
													   String agency, String berth) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);
			
			// 获取货物类型分布数据
			List<Map<String, Object>> cargoData = shipReportService.getCargoTypeDistribution(
					start, end, portDirection, shipType, hazardous, lengthRange, agency, berth);
			
			result.put("status", "success");
			result.put("data", processTopNAndOthers(cargoData, 9)); // 处理前9名和"其他"
		} catch (Exception e) {
			logger.error("获取货物类型分布数据失败", e);
			result.put("status", "error");
			result.put("message", "获取货物类型分布数据失败: " + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 处理数据，保留前N名，其余归为"其他"
	 */
	private List<Map<String, Object>> processTopNAndOthers(List<Map<String, Object>> data, int topN) {
		if (data == null || data.isEmpty() || data.size() <= topN) {
			return data;
		}
		
		List<Map<String, Object>> result = new ArrayList<>();
		// 添加前N个元素
		for (int i = 0; i < topN; i++) {
			result.add(data.get(i));
		}
		
		// 合并其余数据为"其他"
		double otherValue = 0;
		for (int i = topN; i < data.size(); i++) {
			Object value = data.get(i).get("value");
			if (value instanceof Number) {
				otherValue += ((Number) value).doubleValue();
			}
		}
		
		if (otherValue > 0) {
			Map<String, Object> otherMap = new HashMap<>();
			otherMap.put("name", "其他");
			otherMap.put("value", otherValue);
			result.add(otherMap);
		}

		return result;
	}

	/**
	 * 获取船舶长度分布数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "shipLengthDistribution")
	@ResponseBody
	public Map<String, Object> getShipLengthDistribution(String startDate, String endDate,
													String portDirection, String shipType, String hazardous, String lengthRange,
													String agency, String berth) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);
			
			// 获取船舶长度分布数据
			List<Map<String, Object>> lengthData = shipReportService.getShipLengthDistribution(
					start, end, portDirection, shipType, hazardous, lengthRange, agency, berth);
			
			result.put("status", "success");
			result.put("data", lengthData);
		} catch (Exception e) {
			logger.error("获取船舶长度分布数据失败", e);
			result.put("status", "error");
			result.put("message", "获取船舶长度分布数据失败: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * 获取船舶载重吨分布数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "deadweightDistribution")
	@ResponseBody
	public Map<String, Object> getDeadweightDistribution(String startDate, String endDate,
													String portDirection, String shipType, String hazardous, String lengthRange,
													String agency, String berth) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);
			
			// 获取船舶载重吨分布数据
			List<Map<String, Object>> deadweightData = shipReportService.getDeadweightDistribution(
					start, end, portDirection, shipType, hazardous, lengthRange, agency, berth);
			
			result.put("status", "success");
			result.put("data", deadweightData);
		} catch (Exception e) {
			logger.error("获取船舶载重吨分布数据失败", e);
			result.put("status", "error");
			result.put("message", "获取船舶载重吨分布数据失败: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * 获取航线分布数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "routeDistribution")
	@ResponseBody
	public Map<String, Object> getRouteDistribution(String startDate, String endDate,
												 String portDirection, String shipType, String hazardous, String lengthRange,
												 String agency, String berth) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 解析日期
			Date start = DateUtils.parseDate(startDate);
			Date end = DateUtils.parseDate(endDate);
			
			// 获取航线分布数据
			List<Map<String, Object>> routeData = shipReportService.getRouteDistribution(
					start, end, portDirection, shipType, hazardous, lengthRange, agency, berth);
			
			result.put("status", "success");
			result.put("data", routeData);
		} catch (Exception e) {
			logger.error("获取航线分布数据失败", e);
			result.put("status", "error");
			result.put("message", "获取航线分布数据失败: " + e.getMessage());
		}
		
		return result;
	}
}