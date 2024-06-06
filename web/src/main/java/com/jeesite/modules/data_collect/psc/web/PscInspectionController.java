package com.jeesite.modules.data_collect.psc.web;

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
	
}