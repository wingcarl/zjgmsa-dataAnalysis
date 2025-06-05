package com.jeesite.modules.data_collect.intelligent.web;

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
import com.jeesite.modules.data_collect.intelligent.entity.IntelligentMonitoringAlarm;
import com.jeesite.modules.data_collect.intelligent.service.IntelligentMonitoringAlarmService;

/**
 * 智能卡口预警记录表Controller
 * @author 王浩宇
 * @version 2025-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/intelligent/intelligentMonitoringAlarm")
public class IntelligentMonitoringAlarmController extends BaseController {

	@Autowired
	private IntelligentMonitoringAlarmService intelligentMonitoringAlarmService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public IntelligentMonitoringAlarm get(String id, boolean isNewRecord) {
		return intelligentMonitoringAlarmService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = {"list", ""})
	public String list(IntelligentMonitoringAlarm intelligentMonitoringAlarm, Model model) {
		model.addAttribute("intelligentMonitoringAlarm", intelligentMonitoringAlarm);
		return "data_collect/intelligent/intelligentMonitoringAlarmList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<IntelligentMonitoringAlarm> listData(IntelligentMonitoringAlarm intelligentMonitoringAlarm, HttpServletRequest request, HttpServletResponse response) {
		intelligentMonitoringAlarm.setPage(new Page<>(request, response));
		Page<IntelligentMonitoringAlarm> page = intelligentMonitoringAlarmService.findPage(intelligentMonitoringAlarm);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "form")
	public String form(IntelligentMonitoringAlarm intelligentMonitoringAlarm, Model model) {
		model.addAttribute("intelligentMonitoringAlarm", intelligentMonitoringAlarm);
		return "data_collect/intelligent/intelligentMonitoringAlarmForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		intelligentMonitoringAlarmService.save(intelligentMonitoringAlarm);
		return renderResult(Global.TRUE, text("保存智能卡口预警记录表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "exportData")
	public void exportData(IntelligentMonitoringAlarm intelligentMonitoringAlarm, HttpServletResponse response) {
		List<IntelligentMonitoringAlarm> list = intelligentMonitoringAlarmService.findList(intelligentMonitoringAlarm);
		String fileName = "智能卡口预警记录表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("智能卡口预警记录表", IntelligentMonitoringAlarm.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		IntelligentMonitoringAlarm intelligentMonitoringAlarm = new IntelligentMonitoringAlarm();
		List<IntelligentMonitoringAlarm> list = ListUtils.newArrayList(intelligentMonitoringAlarm);
		String fileName = "智能卡口预警记录表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("智能卡口预警记录表", IntelligentMonitoringAlarm.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = intelligentMonitoringAlarmService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("intelligent:intelligentMonitoringAlarm:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		intelligentMonitoringAlarmService.delete(intelligentMonitoringAlarm);
		return renderResult(Global.TRUE, text("删除智能卡口预警记录表成功！"));
	}
	
}