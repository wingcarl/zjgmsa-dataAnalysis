package com.jeesite.modules.data_collect.largeship.web;

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
import com.jeesite.modules.data_collect.largeship.entity.LargeRestrictedVessels;
import com.jeesite.modules.data_collect.largeship.service.LargeRestrictedVesselsService;

/**
 * 大型受限船舶Controller
 * @author 王浩宇
 * @version 2025-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/largeship/largeRestrictedVessels")
public class LargeRestrictedVesselsController extends BaseController {

	@Autowired
	private LargeRestrictedVesselsService largeRestrictedVesselsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public LargeRestrictedVessels get(String id, boolean isNewRecord) {
		return largeRestrictedVesselsService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("largeship:largeRestrictedVessels:view")
	@RequestMapping(value = {"list", ""})
	public String list(LargeRestrictedVessels largeRestrictedVessels, Model model) {
		model.addAttribute("largeRestrictedVessels", largeRestrictedVessels);
		return "data_collect/largeship/largeRestrictedVesselsList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("largeship:largeRestrictedVessels:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<LargeRestrictedVessels> listData(LargeRestrictedVessels largeRestrictedVessels, HttpServletRequest request, HttpServletResponse response) {
		largeRestrictedVessels.setPage(new Page<>(request, response));
		Page<LargeRestrictedVessels> page = largeRestrictedVesselsService.findPage(largeRestrictedVessels);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("largeship:largeRestrictedVessels:view")
	@RequestMapping(value = "form")
	public String form(LargeRestrictedVessels largeRestrictedVessels, Model model) {
		model.addAttribute("largeRestrictedVessels", largeRestrictedVessels);
		return "data_collect/largeship/largeRestrictedVesselsForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("largeship:largeRestrictedVessels:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated LargeRestrictedVessels largeRestrictedVessels) {
		largeRestrictedVesselsService.save(largeRestrictedVessels);
		return renderResult(Global.TRUE, text("保存大型受限船舶成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("largeship:largeRestrictedVessels:view")
	@RequestMapping(value = "exportData")
	public void exportData(LargeRestrictedVessels largeRestrictedVessels, HttpServletResponse response) {
		List<LargeRestrictedVessels> list = largeRestrictedVesselsService.findList(largeRestrictedVessels);
		String fileName = "大型受限船舶" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("大型受限船舶", LargeRestrictedVessels.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("largeship:largeRestrictedVessels:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		LargeRestrictedVessels largeRestrictedVessels = new LargeRestrictedVessels();
		List<LargeRestrictedVessels> list = ListUtils.newArrayList(largeRestrictedVessels);
		String fileName = "大型受限船舶模板.xlsx";
		try(ExcelExport ee = new ExcelExport("大型受限船舶", LargeRestrictedVessels.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("largeship:largeRestrictedVessels:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = largeRestrictedVesselsService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("largeship:largeRestrictedVessels:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(LargeRestrictedVessels largeRestrictedVessels) {
		largeRestrictedVesselsService.delete(largeRestrictedVessels);
		return renderResult(Global.TRUE, text("删除大型受限船舶成功！"));
	}
	
}