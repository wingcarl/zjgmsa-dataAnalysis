package com.jeesite.modules.data_collect.companyrisk.web;

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
import com.jeesite.modules.data_collect.companyrisk.entity.CompanySafetyInspection;
import com.jeesite.modules.data_collect.companyrisk.service.CompanySafetyInspectionService;

/**
 * 安全隐患与风险排查记录表Controller
 * @author 王浩宇
 * @version 2025-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/companyrisk/companySafetyInspection")
public class CompanySafetyInspectionController extends BaseController {

	@Autowired
	private CompanySafetyInspectionService companySafetyInspectionService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CompanySafetyInspection get(String id, boolean isNewRecord) {
		return companySafetyInspectionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("companyrisk:companySafetyInspection:view")
	@RequestMapping(value = {"list", ""})
	public String list(CompanySafetyInspection companySafetyInspection, Model model) {
		model.addAttribute("companySafetyInspection", companySafetyInspection);
		return "data_collect/companyrisk/companySafetyInspectionList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("companyrisk:companySafetyInspection:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CompanySafetyInspection> listData(CompanySafetyInspection companySafetyInspection, HttpServletRequest request, HttpServletResponse response) {
		companySafetyInspection.setPage(new Page<>(request, response));
		Page<CompanySafetyInspection> page = companySafetyInspectionService.findPage(companySafetyInspection);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("companyrisk:companySafetyInspection:view")
	@RequestMapping(value = "form")
	public String form(CompanySafetyInspection companySafetyInspection, Model model) {
		model.addAttribute("companySafetyInspection", companySafetyInspection);
		return "data_collect/companyrisk/companySafetyInspectionForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("companyrisk:companySafetyInspection:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CompanySafetyInspection companySafetyInspection) {
		companySafetyInspectionService.save(companySafetyInspection);
		return renderResult(Global.TRUE, text("保存安全隐患与风险排查记录表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("companyrisk:companySafetyInspection:view")
	@RequestMapping(value = "exportData")
	public void exportData(CompanySafetyInspection companySafetyInspection, HttpServletResponse response) {
		List<CompanySafetyInspection> list = companySafetyInspectionService.findList(companySafetyInspection);
		String fileName = "安全隐患与风险排查记录表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("安全隐患与风险排查记录表", CompanySafetyInspection.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("companyrisk:companySafetyInspection:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		CompanySafetyInspection companySafetyInspection = new CompanySafetyInspection();
		List<CompanySafetyInspection> list = ListUtils.newArrayList(companySafetyInspection);
		String fileName = "安全隐患与风险排查记录表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("安全隐患与风险排查记录表", CompanySafetyInspection.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("companyrisk:companySafetyInspection:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = companySafetyInspectionService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("companyrisk:companySafetyInspection:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CompanySafetyInspection companySafetyInspection) {
		companySafetyInspectionService.delete(companySafetyInspection);
		return renderResult(Global.TRUE, text("删除安全隐患与风险排查记录表成功！"));
	}
	
}