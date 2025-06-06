package com.jeesite.modules.data_collect.intelligent.web;

import java.util.List;
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
import com.jeesite.modules.data_collect.intelligent.entity.IntelligentMonitoringAlarm;
import com.jeesite.modules.data_collect.intelligent.service.IntelligentMonitoringAlarmService;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.jeesite.common.lang.StringUtils;
import java.util.Set;
import java.util.HashSet;

/**
 * 智能卡口预警记录表Controller
 * @author 王浩宇
 * @version 2025-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/intelligent/intelligentMonitoringAlarm")
public class IntelligentMonitoringAlarmController extends BaseController {

	@Autowired
	private IntelligentMonitoringAlarmService intelligentMonitoringAlarmService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public IntelligentMonitoringAlarm get(String id, boolean isNewRecord) {
		return intelligentMonitoringAlarmService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = {"list", ""})
	public String list(IntelligentMonitoringAlarm intelligentMonitoringAlarm, Model model) {
		model.addAttribute("intelligentMonitoringAlarm", intelligentMonitoringAlarm);
		return "data_collect/intelligent/intelligentMonitoringAlarmList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<IntelligentMonitoringAlarm> listData(IntelligentMonitoringAlarm intelligentMonitoringAlarm, HttpServletRequest request, HttpServletResponse response) {
		intelligentMonitoringAlarm.setPage(new Page<>(request, response));
		Page<IntelligentMonitoringAlarm> page = intelligentMonitoringAlarmService.findPage(intelligentMonitoringAlarm);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "form")
	public String form(IntelligentMonitoringAlarm intelligentMonitoringAlarm, Model model) {
		model.addAttribute("intelligentMonitoringAlarm", intelligentMonitoringAlarm);
		return "data_collect/intelligent/intelligentMonitoringAlarmForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		intelligentMonitoringAlarmService.save(intelligentMonitoringAlarm);
		return renderResult(Global.TRUE, text("保存智能卡口预警记录表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "exportData")
	public void exportData(IntelligentMonitoringAlarm intelligentMonitoringAlarm, HttpServletResponse response) {
		List<IntelligentMonitoringAlarm> list = intelligentMonitoringAlarmService.findList(intelligentMonitoringAlarm);
		String fileName = "智能卡口预警记录表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("智能卡口预警记录表", IntelligentMonitoringAlarm.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		IntelligentMonitoringAlarm intelligentMonitoringAlarm = new IntelligentMonitoringAlarm();
		List<IntelligentMonitoringAlarm> list = ListUtils.newArrayList(intelligentMonitoringAlarm);
		String fileName = "智能卡口预警记录表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("智能卡口预警记录表", IntelligentMonitoringAlarm.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = intelligentMonitoringAlarmService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		intelligentMonitoringAlarmService.delete(intelligentMonitoringAlarm);
		return renderResult(Global.TRUE, text("删除智能卡口预警记录表成功！"));
	}
	
	/**
	 * 获取智能卡口数据统计（按分支局分组）
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "getIntelligentMonitoringData")
	@ResponseBody
	public Map<String, Object> getIntelligentMonitoringData(String startDate, String endDate, String level) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 获取所有数据
			List<IntelligentMonitoringAlarm> allData;
			
			// 如果指定了时间范围，通过Service层查询
			if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
				allData = intelligentMonitoringAlarmService.getIntelligentMonitoringDataByDateRange(startDate, endDate);
			} else {
				// 否则获取所有数据
				IntelligentMonitoringAlarm queryParam = new IntelligentMonitoringAlarm();
				allData = intelligentMonitoringAlarmService.findList(queryParam);
			}
			
			// 按分支局分组统计
			Map<String, Map<String, Integer>> branchStats = new HashMap<>();
			
			for (IntelligentMonitoringAlarm item : allData) {
				String branchOffice = item.getBranchOffice();
				if (StringUtils.isBlank(branchOffice)) {
					branchOffice = "未知分支局";
				}
				
				String verificationStatus = item.getVerificationStatus();
				if (StringUtils.isBlank(verificationStatus)) {
					verificationStatus = "未核查";
				}
				
				// 初始化分支局统计
				if (!branchStats.containsKey(branchOffice)) {
					Map<String, Integer> statusCount = new HashMap<>();
					statusCount.put("真实报警", 0);
					statusCount.put("误报警", 0);
					statusCount.put("无效报警", 0);
					statusCount.put("未核查", 0);
					statusCount.put("总计", 0);
					branchStats.put(branchOffice, statusCount);
				}
				
				Map<String, Integer> statusCount = branchStats.get(branchOffice);
				statusCount.put("总计", statusCount.get("总计") + 1);
				
				// 根据核查情况分类
				if (verificationStatus.contains("真实") || verificationStatus.contains("实报")) {
					statusCount.put("真实报警", statusCount.get("真实报警") + 1);
				} else if (verificationStatus.contains("误报") || verificationStatus.contains("虚报")) {
					statusCount.put("误报警", statusCount.get("误报警") + 1);
				} else if (verificationStatus.contains("无效") || verificationStatus.contains("失效")) {
					statusCount.put("无效报警", statusCount.get("无效报警") + 1);
				} else {
					statusCount.put("未核查", statusCount.get("未核查") + 1);
				}
			}
			
			// 构建返回数据
			List<String> organizations = new ArrayList<>();
			List<Integer> realAlarmCounts = new ArrayList<>();
			List<Integer> falseAlarmCounts = new ArrayList<>();
			List<Integer> invalidAlarmCounts = new ArrayList<>();
			List<Integer> uncheckAlarmCounts = new ArrayList<>();
			List<Double> realAlarmRates = new ArrayList<>();
			
			for (Map.Entry<String, Map<String, Integer>> entry : branchStats.entrySet()) {
				String branchOffice = entry.getKey();
				Map<String, Integer> counts = entry.getValue();
				
				organizations.add(branchOffice);
				realAlarmCounts.add(counts.get("真实报警"));
				falseAlarmCounts.add(counts.get("误报警"));
				invalidAlarmCounts.add(counts.get("无效报警"));
				uncheckAlarmCounts.add(counts.get("未核查"));
				
				// 计算真实报警率
				int totalCount = counts.get("总计");
				int realCount = counts.get("真实报警");
				double rate = totalCount > 0 ? (double) realCount / totalCount * 100 : 0.0;
				realAlarmRates.add(Math.round(rate * 100) / 100.0); // 保留两位小数
			}
			
			result.put("organizations", organizations);
			result.put("realAlarmCounts", realAlarmCounts);
			result.put("falseAlarmCounts", falseAlarmCounts);
			result.put("invalidAlarmCounts", invalidAlarmCounts);
			result.put("uncheckAlarmCounts", uncheckAlarmCounts);
			result.put("realAlarmRates", realAlarmRates);
			result.put("startDate", startDate);
			result.put("endDate", endDate);
			
		} catch (Exception e) {
			logger.error("获取智能卡口数据统计失败", e);
			result.put("error", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取alarm_flag分类统计数据（按alarm_flag分组，只统计数量大于10的）
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "getAlarmFlagData")
	@ResponseBody
	public Map<String, Object> getAlarmFlagData(String startDate, String endDate, String level, String branchOffice) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 获取所有数据
			List<IntelligentMonitoringAlarm> allData;
			
			// 如果指定了时间范围，通过Service层查询
			if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
				allData = intelligentMonitoringAlarmService.getIntelligentMonitoringDataByDateRange(startDate, endDate);
			} else {
				// 否则获取所有数据
				IntelligentMonitoringAlarm queryParam = new IntelligentMonitoringAlarm();
				allData = intelligentMonitoringAlarmService.findList(queryParam);
			}
			
			// 如果指定了分支局筛选，进行过滤
			if (StringUtils.isNotBlank(branchOffice)) {
				allData = allData.stream()
					.filter(item -> branchOffice.equals(item.getBranchOffice()))
					.collect(java.util.stream.Collectors.toList());
			}
			
			// 按alarm_flag分组统计
			Map<String, Map<String, Integer>> alarmFlagStats = new HashMap<>();
			
			for (IntelligentMonitoringAlarm item : allData) {
				String alarmFlag = item.getAlarmFlag();
				if (StringUtils.isBlank(alarmFlag)) {
					alarmFlag = "未知告警类型";
				}
				
				String verificationStatus = item.getVerificationStatus();
				if (StringUtils.isBlank(verificationStatus)) {
					verificationStatus = "未核查";
				}
				
				// 初始化alarm_flag统计
				if (!alarmFlagStats.containsKey(alarmFlag)) {
					Map<String, Integer> statusCount = new HashMap<>();
					statusCount.put("真实报警", 0);
					statusCount.put("误报警", 0);
					statusCount.put("无效报警", 0);
					statusCount.put("未核查", 0);
					statusCount.put("总计", 0);
					alarmFlagStats.put(alarmFlag, statusCount);
				}
				
				Map<String, Integer> statusCount = alarmFlagStats.get(alarmFlag);
				statusCount.put("总计", statusCount.get("总计") + 1);
				
				// 根据核查情况分类
				if (verificationStatus.contains("真实") || verificationStatus.contains("实报")) {
					statusCount.put("真实报警", statusCount.get("真实报警") + 1);
				} else if (verificationStatus.contains("误报") || verificationStatus.contains("虚报")) {
					statusCount.put("误报警", statusCount.get("误报警") + 1);
				} else if (verificationStatus.contains("无效") || verificationStatus.contains("失效")) {
					statusCount.put("无效报警", statusCount.get("无效报警") + 1);
				} else {
					statusCount.put("未核查", statusCount.get("未核查") + 1);
				}
			}
			
			// 过滤出数量大于10的alarm_flag，并按总数排序
			List<Map.Entry<String, Map<String, Integer>>> filteredEntries = alarmFlagStats.entrySet().stream()
				.filter(entry -> entry.getValue().get("总计") > 10)
				.sorted((a, b) -> b.getValue().get("总计").compareTo(a.getValue().get("总计")))
				.collect(java.util.stream.Collectors.toList());
			
			// 构建返回数据
			List<String> alarmFlags = new ArrayList<>();
			List<Integer> realAlarmCounts = new ArrayList<>();
			List<Integer> falseAlarmCounts = new ArrayList<>();
			List<Integer> invalidAlarmCounts = new ArrayList<>();
			List<Integer> uncheckAlarmCounts = new ArrayList<>();
			List<Double> realAlarmRates = new ArrayList<>();
			
			for (Map.Entry<String, Map<String, Integer>> entry : filteredEntries) {
				String alarmFlag = entry.getKey();
				Map<String, Integer> counts = entry.getValue();
				
				alarmFlags.add(alarmFlag);
				realAlarmCounts.add(counts.get("真实报警"));
				falseAlarmCounts.add(counts.get("误报警"));
				invalidAlarmCounts.add(counts.get("无效报警"));
				uncheckAlarmCounts.add(counts.get("未核查"));
				
				// 计算真实报警率
				int totalCount = counts.get("总计");
				int realCount = counts.get("真实报警");
				double rate = totalCount > 0 ? (double) realCount / totalCount * 100 : 0.0;
				realAlarmRates.add(Math.round(rate * 100) / 100.0); // 保留两位小数
			}
			
			result.put("alarmFlags", alarmFlags);
			result.put("realAlarmCounts", realAlarmCounts);
			result.put("falseAlarmCounts", falseAlarmCounts);
			result.put("invalidAlarmCounts", invalidAlarmCounts);
			result.put("uncheckAlarmCounts", uncheckAlarmCounts);
			result.put("realAlarmRates", realAlarmRates);
			result.put("startDate", startDate);
			result.put("endDate", endDate);
			result.put("branchOffice", branchOffice);
			
		} catch (Exception e) {
			logger.error("获取alarm_flag分类统计数据失败", e);
			result.put("error", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取分支局列表（用于筛选）
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "getBranchOffices")
	@ResponseBody
	public List<String> getBranchOffices(String startDate, String endDate) {
		List<String> branchOffices = new ArrayList<>();
		
		try {
			// 获取所有数据
			List<IntelligentMonitoringAlarm> allData;
			
			// 如果指定了时间范围，通过Service层查询
			if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
				allData = intelligentMonitoringAlarmService.getIntelligentMonitoringDataByDateRange(startDate, endDate);
			} else {
				// 否则获取所有数据
				IntelligentMonitoringAlarm queryParam = new IntelligentMonitoringAlarm();
				allData = intelligentMonitoringAlarmService.findList(queryParam);
			}
			
			// 收集去重的分支局列表
			Set<String> branchOfficeSet = new HashSet<>();
			for (IntelligentMonitoringAlarm item : allData) {
				String branchOffice = item.getBranchOffice();
				if (StringUtils.isNotBlank(branchOffice)) {
					branchOfficeSet.add(branchOffice);
				}
			}
			
			// 转换为列表并排序
			branchOffices = branchOfficeSet.stream()
				.sorted()
				.collect(java.util.stream.Collectors.toList());
			
		} catch (Exception e) {
			logger.error("获取分支局列表失败", e);
		}
		
		return branchOffices;
	}
	
}