package com.jeesite.modules.data_collect.ship.web;

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