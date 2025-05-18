package com.jeesite.modules.data_collect.ship.web;

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
import com.jeesite.modules.data_collect.ship.entity.ShipInspection;
import com.jeesite.modules.data_collect.ship.service.ShipInspectionService;

/**
 * 海船安全检查信息表Controller
 * @author 王浩宇
 * @version 2024-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/ship/shipInspection")
public class ShipInspectionController extends BaseController {

	@Autowired
	private ShipInspectionService shipInspectionService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ShipInspection get(String id, boolean isNewRecord) {
		return shipInspectionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ship:shipInspection:view")
	@RequestMapping(value = {"list", ""})
	public String list(ShipInspection shipInspection, Model model) {
		model.addAttribute("shipInspection", shipInspection);
		return "data_collect/ship/shipInspectionList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ship:shipInspection:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ShipInspection> listData(ShipInspection shipInspection, HttpServletRequest request, HttpServletResponse response) {
		shipInspection.setPage(new Page<>(request, response));
		Page<ShipInspection> page = shipInspectionService.findPage(shipInspection);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ship:shipInspection:view")
	@RequestMapping(value = "form")
	public String form(ShipInspection shipInspection, Model model) {
		model.addAttribute("shipInspection", shipInspection);
		return "data_collect/ship/shipInspectionForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ship:shipInspection:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ShipInspection shipInspection) {
		shipInspectionService.save(shipInspection);
		return renderResult(Global.TRUE, text("保存海船安全检查信息表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("ship:shipInspection:view")
	@RequestMapping(value = "exportData")
	public void exportData(ShipInspection shipInspection, HttpServletResponse response) {
		List<ShipInspection> list = shipInspectionService.findList(shipInspection);
		String fileName = "海船安全检查信息表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("海船安全检查信息表", ShipInspection.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("ship:shipInspection:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		ShipInspection shipInspection = new ShipInspection();
		List<ShipInspection> list = ListUtils.newArrayList(shipInspection);
		String fileName = "海船安全检查信息表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("海船安全检查信息表", ShipInspection.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("ship:shipInspection:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String shipType) {
		try {
			String message = shipInspectionService.importData(file,shipType);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}

	/**
	 * 获取船舶检查数据统计 - 用于周数据看板
	 */
	@GetMapping("getInspectionStatisticsData")
	@ResponseBody
	public Map<String, Object> getInspectionStatisticsData(String startDate, String endDate) {
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
			
			// 组装返回数据
			result.put("seaShipCount", seaShipCount);
			result.put("riverShipCount", riverShipCount);
			result.put("seaShipDefectCount", seaShipDefectCount);
			result.put("riverShipDefectCount", riverShipDefectCount);
			result.put("seaShipDetainedCount", seaShipDetainedCount);
			result.put("riverShipDetainedCount", riverShipDetainedCount);
			result.put("agencySeaShipCounts", agencySeaShipCounts);
			result.put("agencyRiverShipCounts", agencyRiverShipCounts);
			
			result.put("status", "success");
		} catch (Exception e) {
			logger.error("获取船舶检查统计数据失败", e);
			result.put("status", "error");
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ship:shipInspection:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ShipInspection shipInspection) {
		shipInspectionService.delete(shipInspection);
		return renderResult(Global.TRUE, text("删除海船安全检查信息表成功！"));
	}
	
}