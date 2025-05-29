package com.jeesite.modules.data_collect.shiponsite.web;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Date;
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
import org.springframework.web.bind.annotation.GetMapping;

import com.jeesite.common.config.Global;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import org.springframework.web.multipart.MultipartFile;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.data_collect.shiponsite.entity.ShipOnSiteInspection;
import com.jeesite.modules.data_collect.shiponsite.service.ShipOnSiteInspectionService;

/**
 * 船舶现场监督检查表Controller
 * @author 王浩宇
 * @version 2024-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/shiponsite/shipOnSiteInspection")
public class ShipOnSiteInspectionController extends BaseController {

	@Autowired
	private ShipOnSiteInspectionService shipOnSiteInspectionService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ShipOnSiteInspection get(String id, boolean isNewRecord) {
		return shipOnSiteInspectionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("shiponsite:shipOnSiteInspection:view")
	@RequestMapping(value = {"list", ""})
	public String list(ShipOnSiteInspection shipOnSiteInspection, Model model) {
		model.addAttribute("shipOnSiteInspection", shipOnSiteInspection);
		return "data_collect/shiponsite/shipOnSiteInspectionList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("shiponsite:shipOnSiteInspection:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ShipOnSiteInspection> listData(ShipOnSiteInspection shipOnSiteInspection, HttpServletRequest request, HttpServletResponse response) {
		shipOnSiteInspection.setPage(new Page<>(request, response));
		Page<ShipOnSiteInspection> page = shipOnSiteInspectionService.findPage(shipOnSiteInspection);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("shiponsite:shipOnSiteInspection:view")
	@RequestMapping(value = "form")
	public String form(ShipOnSiteInspection shipOnSiteInspection, Model model) {
		model.addAttribute("shipOnSiteInspection", shipOnSiteInspection);
		return "data_collect/shiponsite/shipOnSiteInspectionForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("shiponsite:shipOnSiteInspection:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ShipOnSiteInspection shipOnSiteInspection) {
		shipOnSiteInspectionService.save(shipOnSiteInspection);
		return renderResult(Global.TRUE, text("保存船舶现场监督检查表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("shiponsite:shipOnSiteInspection:view")
	@RequestMapping(value = "exportData")
	public void exportData(ShipOnSiteInspection shipOnSiteInspection, HttpServletResponse response) {
		List<ShipOnSiteInspection> list = shipOnSiteInspectionService.findList(shipOnSiteInspection);
		String fileName = "船舶现场监督检查表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("船舶现场监督检查表", ShipOnSiteInspection.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("shiponsite:shipOnSiteInspection:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		ShipOnSiteInspection shipOnSiteInspection = new ShipOnSiteInspection();
		List<ShipOnSiteInspection> list = ListUtils.newArrayList(shipOnSiteInspection);
		String fileName = "船舶现场监督检查表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("船舶现场监督检查表", ShipOnSiteInspection.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("shiponsite:shipOnSiteInspection:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = shipOnSiteInspectionService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("shiponsite:shipOnSiteInspection:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ShipOnSiteInspection shipOnSiteInspection) {
		shipOnSiteInspectionService.delete(shipOnSiteInspection);
		return renderResult(Global.TRUE, text("删除船舶现场监督检查表成功！"));
	}
	
	/**
	 * 获取船舶现场监督检查数据统计 - 用于周数据看板
	 */
	@GetMapping("getOnSiteInspectionStatisticsData")
	@ResponseBody
	public Map<String, Object> getOnSiteInspectionStatisticsData(String startDate, String endDate) {
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
			
			// 统计异常数量
			long abnormalCount = onSiteMap.values().stream()
				.filter(list -> list.stream()
					.anyMatch(inspection -> "是".equals(inspection.getIssueFound())))
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
			result.put("status", "success");
		} catch (Exception e) {
			logger.error("获取船舶现场监督检查统计数据失败", e);
			result.put("status", "error");
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取船舶现场监督检查数据统计 - 按部门分组（通过agency_dept表关联）
	 */
	@GetMapping("getOnSiteInspectionStatisticsByDepartment")
	@ResponseBody
	public Map<String, Object> getOnSiteInspectionStatisticsByDepartment(String startDate, String endDate, String department) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 使用DAO方法获取按部门分组的现场监督数据
			List<Map<String, Object>> onSiteStatistics = shipOnSiteInspectionService.findOnSiteInspectionStatisticsByDepartment(startDate, endDate, department);
			
			// 初始化统计数据
			long onSiteCount = 0;
			long abnormalCount = 0;
			Map<String, Long> agencyOnSiteCounts = new HashMap<>();
			Map<String, Long> agencyAbnormalCounts = new HashMap<>();
			
			// 处理查询结果
			for (Map<String, Object> stat : onSiteStatistics) {
				String dept = (String) stat.get("department");
				Long onSiteCountForDept = ((Number) stat.get("onSiteCount")).longValue();
				Long abnormalCountForDept = ((Number) stat.get("abnormalCount")).longValue();
				
				onSiteCount += onSiteCountForDept;
				abnormalCount += abnormalCountForDept;
				
				agencyOnSiteCounts.put(dept, onSiteCountForDept);
				agencyAbnormalCounts.put(dept, abnormalCountForDept);
			}
			
			// 组装返回数据
			result.put("onSiteCount", onSiteCount);
			result.put("abnormalCount", abnormalCount);
			result.put("agencyOnSiteCounts", agencyOnSiteCounts);
			result.put("agencyAbnormalCounts", agencyAbnormalCounts);
			result.put("status", "success");
		} catch (Exception e) {
			logger.error("获取现场监督统计数据失败", e);
			result.put("status", "error");
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
}