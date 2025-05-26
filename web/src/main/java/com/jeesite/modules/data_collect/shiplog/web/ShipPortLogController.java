package com.jeesite.modules.data_collect.shiplog.web;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import org.springframework.web.multipart.MultipartFile;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.data_collect.shiplog.entity.ShipPortLog;
import com.jeesite.modules.data_collect.shiplog.service.ShipPortLogService;

/**
 * 国际航行船舶表Controller
 * @author 王浩宇
 * @version 2025-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/shiplog/shipPortLog")
public class ShipPortLogController extends BaseController {

	@Autowired
	private ShipPortLogService shipPortLogService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ShipPortLog get(String id, boolean isNewRecord) {
		return shipPortLogService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(ShipPortLog shipPortLog, Model model) {
		model.addAttribute("shipPortLog", shipPortLog);
		return "data_collect/shiplog/shipPortLogList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ShipPortLog> listData(ShipPortLog shipPortLog, HttpServletRequest request, HttpServletResponse response) {
		shipPortLog.setPage(new Page<>(request, response));
		Page<ShipPortLog> page = shipPortLogService.findPage(shipPortLog);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "form")
	public String form(ShipPortLog shipPortLog, Model model) {
		model.addAttribute("shipPortLog", shipPortLog);
		return "data_collect/shiplog/shipPortLogForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ShipPortLog shipPortLog) {
		shipPortLogService.save(shipPortLog);
		return renderResult(Global.TRUE, text("保存国际航行船舶表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "exportData")
	public void exportData(ShipPortLog shipPortLog, HttpServletResponse response) {
		List<ShipPortLog> list = shipPortLogService.findList(shipPortLog);
		String fileName = "国际航行船舶表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("国际航行船舶表", ShipPortLog.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		ShipPortLog shipPortLog = new ShipPortLog();
		List<ShipPortLog> list = ListUtils.newArrayList(shipPortLog);
		String fileName = "国际航行船舶表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("国际航行船舶表", ShipPortLog.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("shiplog:shipPortLog:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = shipPortLogService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ShipPortLog shipPortLog) {
		shipPortLogService.delete(shipPortLog);
		return renderResult(Global.TRUE, text("删除国际航行船舶表成功！"));
	}
	
	/**
	 * 数据分析页面
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "analysis")
	public String analysis(Model model) {
		return "data_collect/shiplog/shipPortLogAnalysis";
	}
	
	/**
	 * 获取数据分析统计数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "analysisData")
	@ResponseBody
	public Map<String, Object> analysisData(String startDate, String endDate) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 如果没有传入日期，默认为上周五至本周四
			if (startDate == null || endDate == null) {
				Calendar cal = Calendar.getInstance();
				// 获取本周四
				int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
				int daysToThursday = (Calendar.THURSDAY - dayOfWeek + 7) % 7;
				if (daysToThursday == 0 && dayOfWeek != Calendar.THURSDAY) {
					daysToThursday = 7;
				}
				cal.add(Calendar.DAY_OF_MONTH, daysToThursday);
				Date endDateTime = cal.getTime();
				
				// 获取上周五
				cal.add(Calendar.DAY_OF_MONTH, -6);
				Date startDateTime = cal.getTime();
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				startDate = sdf.format(startDateTime);
				endDate = sdf.format(endDateTime);
			}
			
			// 获取统计数据
			Map<String, Object> analysisResult = shipPortLogService.getAnalysisData(startDate, endDate);
			result.put("success", true);
			result.put("data", analysisResult);
			result.put("startDate", startDate);
			result.put("endDate", endDate);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取时间趋势数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "trendData")
	@ResponseBody
	public Map<String, Object> trendData(String startDate, String endDate, String timeInterval, String berthingLocation, String shipCategory) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			if (timeInterval == null || timeInterval.isEmpty()) {
				timeInterval = "day";
			}
			
			List<Map<String, Object>> trendData = shipPortLogService.getTrendData(startDate, endDate, timeInterval, berthingLocation, shipCategory);
			result.put("success", true);
			result.put("data", trendData);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取各码头船舶数量统计
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "berthingLocationStats")
	@ResponseBody
	public Map<String, Object> berthingLocationStats(String startDate, String endDate, String berthingLocation, String shipCategory) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			List<Map<String, Object>> statsData = shipPortLogService.getBerthingLocationStats(startDate, endDate, berthingLocation, shipCategory);
			result.put("success", true);
			result.put("data", statsData);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取各码头装卸货量统计
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "berthingLocationCargoStats")
	@ResponseBody
	public Map<String, Object> berthingLocationCargoStats(String startDate, String endDate, String berthingLocation, String shipCategory) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			List<Map<String, Object>> statsData = shipPortLogService.getBerthingLocationCargoStats(startDate, endDate, berthingLocation, shipCategory);
			result.put("success", true);
			result.put("data", statsData);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取船舶类型统计
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "shipCategoryStats")
	@ResponseBody
	public Map<String, Object> shipCategoryStats(String startDate, String endDate, String berthingLocation, String shipCategory) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			List<Map<String, Object>> statsData = shipPortLogService.getShipCategoryStats(startDate, endDate, berthingLocation, shipCategory);
			result.put("success", true);
			result.put("data", statsData);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取筛选选项数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "filterOptions")
	@ResponseBody
	public Map<String, Object> filterOptions() {
		Map<String, Object> result = new HashMap<>();
		
		try {
			List<String> berthingLocations = shipPortLogService.getBerthingLocationList();
			List<String> shipCategories = shipPortLogService.getShipCategoryList();
			
			result.put("success", true);
			result.put("berthingLocations", berthingLocations);
			result.put("shipCategories", shipCategories);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取来往港口统计数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "previousNextPortStats")
	@ResponseBody
	public Map<String, Object> previousNextPortStats(String startDate, String endDate, String berthingLocation, String shipCategory) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			List<Map<String, Object>> data = shipPortLogService.getPreviousNextPortStats(startDate, endDate, berthingLocation, shipCategory);
			result.put("success", true);
			result.put("data", data);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取来往港口装卸货量统计数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "previousNextPortCargoStats")
	@ResponseBody
	public Map<String, Object> previousNextPortCargoStats(String startDate, String endDate, String berthingLocation, String shipCategory) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			List<Map<String, Object>> data = shipPortLogService.getPreviousNextPortCargoStats(startDate, endDate, berthingLocation, shipCategory);
			result.put("success", true);
			result.put("data", data);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取船籍港统计数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "portOfRegistryStats")
	@ResponseBody
	public Map<String, Object> portOfRegistryStats(String startDate, String endDate, String berthingLocation, String shipCategory) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			List<Map<String, Object>> data = shipPortLogService.getPortOfRegistryStats(startDate, endDate, berthingLocation, shipCategory);
			result.put("success", true);
			result.put("data", data);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取船舶所有人统计数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "shipOwnerStats")
	@ResponseBody
	public Map<String, Object> shipOwnerStats(String startDate, String endDate, String berthingLocation, String shipCategory) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			List<Map<String, Object>> data = shipPortLogService.getShipOwnerStats(startDate, endDate, berthingLocation, shipCategory);
			result.put("success", true);
			result.put("data", data);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取船舶经营人统计数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "shipOperatorStats")
	@ResponseBody
	public Map<String, Object> shipOperatorStats(String startDate, String endDate, String berthingLocation, String shipCategory) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			List<Map<String, Object>> data = shipPortLogService.getShipOperatorStats(startDate, endDate, berthingLocation, shipCategory);
			result.put("success", true);
			result.put("data", data);
			
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
}