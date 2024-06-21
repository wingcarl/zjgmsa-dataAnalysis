package com.jeesite.modules.shipreport.web;

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
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.shipreport.entity.ShipReport;
import com.jeesite.modules.shipreport.service.ShipReportService;

/**
 * 船舶报告表Controller
 * @author 王浩宇
 * @version 2024-06-21
 */
@Controller
@RequestMapping(value = "${adminPath}/shipreport/shipReport")
public class ShipReportController extends BaseController {

	@Autowired
	private ShipReportService shipReportService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ShipReport get(String id, boolean isNewRecord) {
		return shipReportService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(ShipReport shipReport, Model model) {
		model.addAttribute("shipReport", shipReport);
		return "modules/shipreport/shipReportList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ShipReport> listData(ShipReport shipReport, HttpServletRequest request, HttpServletResponse response) {
		shipReport.setPage(new Page<>(request, response));
		Page<ShipReport> page = shipReportService.findPage(shipReport);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("shipreport:shipReport:view")
	@RequestMapping(value = "form")
	public String form(ShipReport shipReport, Model model) {
		model.addAttribute("shipReport", shipReport);
		return "modules/shipreport/shipReportForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("shipreport:shipReport:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ShipReport shipReport) {
		shipReportService.save(shipReport);
		return renderResult(Global.TRUE, text("保存船舶报告表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("shipreport:shipReport:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ShipReport shipReport) {
		shipReportService.delete(shipReport);
		return renderResult(Global.TRUE, text("删除船舶报告表成功！"));
	}
	
}