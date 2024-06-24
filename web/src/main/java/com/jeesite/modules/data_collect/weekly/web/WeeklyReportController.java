package com.jeesite.modules.data_collect.weekly.web;

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
import com.jeesite.modules.data_collect.weekly.entity.WeeklyReport;
import com.jeesite.modules.data_collect.weekly.service.WeeklyReportService;

/**
 * 周工作数据表Controller
 * @author 王浩宇
 * @version 2024-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/weekly/weeklyReport")
public class WeeklyReportController extends BaseController {

	@Autowired
	private WeeklyReportService weeklyReportService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public WeeklyReport get(String id, boolean isNewRecord) {
		return weeklyReportService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(WeeklyReport weeklyReport, Model model) {
		model.addAttribute("weeklyReport", weeklyReport);
		return "data_collect/weekly/weeklyReportList";
	}

	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = {"dash"})
	public String dashboard(){
		return "data_collect/weekly/weeklyDashboard";
	}
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<WeeklyReport> listData(WeeklyReport weeklyReport, HttpServletRequest request, HttpServletResponse response) {
		weeklyReport.setPage(new Page<>(request, response));
		Page<WeeklyReport> page = weeklyReportService.findPage(weeklyReport);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "form")
	public String form(WeeklyReport weeklyReport, Model model) {
		model.addAttribute("weeklyReport", weeklyReport);
		return "data_collect/weekly/weeklyReportForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("weekly:weeklyReport:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated WeeklyReport weeklyReport) {
		weeklyReportService.save(weeklyReport);
		return renderResult(Global.TRUE, text("保存周工作数据表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "exportData")
	public void exportData(WeeklyReport weeklyReport, HttpServletResponse response) {
		List<WeeklyReport> list = weeklyReportService.findList(weeklyReport);
		String fileName = "周工作数据表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("周工作数据表", WeeklyReport.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("weekly:weeklyReport:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		WeeklyReport weeklyReport = new WeeklyReport();
		List<WeeklyReport> list = ListUtils.newArrayList(weeklyReport);
		String fileName = "周工作数据表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("周工作数据表", WeeklyReport.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("weekly:weeklyReport:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = weeklyReportService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("weekly:weeklyReport:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(WeeklyReport weeklyReport) {
		weeklyReportService.delete(weeklyReport);
		return renderResult(Global.TRUE, text("删除周工作数据表成功！"));
	}
	
}