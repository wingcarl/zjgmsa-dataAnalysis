package com.jeesite.modules.data_collect.misdata.web;

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
import com.jeesite.modules.data_collect.misdata.entity.MonthlyMiscellaneousData;
import com.jeesite.modules.data_collect.misdata.service.MonthlyMiscellaneousDataService;

/**
 * 月度杂项数据表Controller
 * @author 王浩宇
 * @version 2025-05-30
 */
@Controller
@RequestMapping(value = "${adminPath}/misdata/monthlyMiscellaneousData")
public class MonthlyMiscellaneousDataController extends BaseController {

	@Autowired
	private MonthlyMiscellaneousDataService monthlyMiscellaneousDataService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public MonthlyMiscellaneousData get(String id, boolean isNewRecord) {
		return monthlyMiscellaneousDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("misdata:monthlyMiscellaneousData:view")
	@RequestMapping(value = {"list", ""})
	public String list(MonthlyMiscellaneousData monthlyMiscellaneousData, Model model) {
		model.addAttribute("monthlyMiscellaneousData", monthlyMiscellaneousData);
		return "data_collect/misdata/monthlyMiscellaneousDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("misdata:monthlyMiscellaneousData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<MonthlyMiscellaneousData> listData(MonthlyMiscellaneousData monthlyMiscellaneousData, HttpServletRequest request, HttpServletResponse response) {
		monthlyMiscellaneousData.setPage(new Page<>(request, response));
		Page<MonthlyMiscellaneousData> page = monthlyMiscellaneousDataService.findPage(monthlyMiscellaneousData);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("misdata:monthlyMiscellaneousData:view")
	@RequestMapping(value = "form")
	public String form(MonthlyMiscellaneousData monthlyMiscellaneousData, Model model) {
		model.addAttribute("monthlyMiscellaneousData", monthlyMiscellaneousData);
		return "data_collect/misdata/monthlyMiscellaneousDataForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("misdata:monthlyMiscellaneousData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated MonthlyMiscellaneousData monthlyMiscellaneousData) {
		monthlyMiscellaneousDataService.save(monthlyMiscellaneousData);
		return renderResult(Global.TRUE, text("保存月度杂项数据表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("misdata:monthlyMiscellaneousData:view")
	@RequestMapping(value = "exportData")
	public void exportData(MonthlyMiscellaneousData monthlyMiscellaneousData, HttpServletResponse response) {
		List<MonthlyMiscellaneousData> list = monthlyMiscellaneousDataService.findList(monthlyMiscellaneousData);
		String fileName = "月度杂项数据表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("月度杂项数据表", MonthlyMiscellaneousData.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("misdata:monthlyMiscellaneousData:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		MonthlyMiscellaneousData monthlyMiscellaneousData = new MonthlyMiscellaneousData();
		List<MonthlyMiscellaneousData> list = ListUtils.newArrayList(monthlyMiscellaneousData);
		String fileName = "月度杂项数据表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("月度杂项数据表", MonthlyMiscellaneousData.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("misdata:monthlyMiscellaneousData:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = monthlyMiscellaneousDataService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("misdata:monthlyMiscellaneousData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(MonthlyMiscellaneousData monthlyMiscellaneousData) {
		monthlyMiscellaneousDataService.delete(monthlyMiscellaneousData);
		return renderResult(Global.TRUE, text("删除月度杂项数据表成功！"));
	}
	
}