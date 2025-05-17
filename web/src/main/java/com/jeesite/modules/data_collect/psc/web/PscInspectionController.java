package com.jeesite.modules.data_collect.psc.web;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
import com.jeesite.modules.data_collect.psc.entity.PscInspection;
import com.jeesite.modules.data_collect.psc.service.PscInspectionService;

/**
 * PSC Inspection TableController
 * @author Dylan Wang
 * @version 2024-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/psc/pscInspection")
public class PscInspectionController extends BaseController {

	@Autowired
	private PscInspectionService pscInspectionService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public PscInspection get(String id, boolean isNewRecord) {
		return pscInspectionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("psc:pscInspection:view")
	@RequestMapping(value = {"list", ""})
	public String list(PscInspection pscInspection, Model model) {
		model.addAttribute("pscInspection", pscInspection);
		return "data_collect/psc/pscInspectionList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("psc:pscInspection:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<PscInspection> listData(PscInspection pscInspection, HttpServletRequest request, HttpServletResponse response) {
		pscInspection.setPage(new Page<>(request, response));
		Page<PscInspection> page = pscInspectionService.findPage(pscInspection);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("psc:pscInspection:view")
	@RequestMapping(value = "form")
	public String form(PscInspection pscInspection, Model model) {
		model.addAttribute("pscInspection", pscInspection);
		return "data_collect/psc/pscInspectionForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("psc:pscInspection:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated PscInspection pscInspection) {
		pscInspectionService.save(pscInspection);
		return renderResult(Global.TRUE, text("保存PSC Inspection Table成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("psc:pscInspection:view")
	@RequestMapping(value = "exportData")
	public void exportData(PscInspection pscInspection, HttpServletResponse response) {
		List<PscInspection> list = pscInspectionService.findList(pscInspection);
		String fileName = "PSC Inspection Table" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("PSC Inspection Table", PscInspection.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("psc:pscInspection:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		PscInspection pscInspection = new PscInspection();
		List<PscInspection> list = ListUtils.newArrayList(pscInspection);
		String fileName = "PSC Inspection Table模板.xlsx";
		try(ExcelExport ee = new ExcelExport("PSC Inspection Table", PscInspection.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("psc:pscInspection:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = pscInspectionService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("psc:pscInspection:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(PscInspection pscInspection) {
		pscInspectionService.delete(pscInspection);
		return renderResult(Global.TRUE, text("删除PSC Inspection Table成功！"));
	}
	
	/**
	 * 获取PSC检查数据统计 - 用于周数据看板
	 */
	@GetMapping("getPscStatisticsData")
	@ResponseBody
	public Map<String, Object> getPscStatisticsData(String startDate, String endDate) {
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
			result.put("status", "success");
		} catch (Exception e) {
			logger.error("获取PSC检查统计数据失败", e);
			result.put("status", "error");
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
}