package com.jeesite.modules.data_collect.weeklydata.web;

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
import com.jeesite.modules.data_collect.weeklydata.entity.DataMetrics;
import com.jeesite.modules.data_collect.weeklydata.service.DataMetricsService;

/**
 * 数据指标表Controller
 * @author 王浩宇
 * @version 2024-06-18
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklydata/dataMetrics")
public class DataMetricsController extends BaseController {

	@Autowired
	private DataMetricsService dataMetricsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DataMetrics get(String id, boolean isNewRecord) {
		return dataMetricsService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("weeklydata:dataMetrics:view")
	@RequestMapping(value = {"list", ""})
	public String list(DataMetrics dataMetrics, Model model) {
		model.addAttribute("dataMetrics", dataMetrics);
		return "data_collect/weeklydata/dataMetricsList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("weeklydata:dataMetrics:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DataMetrics> listData(DataMetrics dataMetrics, HttpServletRequest request, HttpServletResponse response) {
		dataMetrics.setPage(new Page<>(request, response));
		Page<DataMetrics> page = dataMetricsService.findPage(dataMetrics);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("weeklydata:dataMetrics:view")
	@RequestMapping(value = "form")
	public String form(DataMetrics dataMetrics, Model model) {
		model.addAttribute("dataMetrics", dataMetrics);
		return "data_collect/weeklydata/dataMetricsForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("weeklydata:dataMetrics:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DataMetrics dataMetrics) {
		dataMetricsService.save(dataMetrics);
		return renderResult(Global.TRUE, text("保存数据指标表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("weeklydata:dataMetrics:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DataMetrics dataMetrics) {
		dataMetricsService.delete(dataMetrics);
		return renderResult(Global.TRUE, text("删除数据指标表成功！"));
	}
	
}