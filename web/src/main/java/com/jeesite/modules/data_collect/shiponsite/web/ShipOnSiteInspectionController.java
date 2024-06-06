package com.jeesite.modules.data_collect.shiponsite.web;

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
	
}