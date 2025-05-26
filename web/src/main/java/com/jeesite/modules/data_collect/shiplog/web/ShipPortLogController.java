package com.jeesite.modules.data_collect.shiplog.web;

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
import com.jeesite.modules.data_collect.shiplog.entity.ShipPortLog;
import com.jeesite.modules.data_collect.shiplog.service.ShipPortLogService;

/**
 * 国际航行船舶表Controller
 * @author 王浩宇
 * @version 2025-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/shiplog/shipPortLog")
public class ShipPortLogController extends BaseController {

	@Autowired
	private ShipPortLogService shipPortLogService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ShipPortLog get(String id, boolean isNewRecord) {
		return shipPortLogService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(ShipPortLog shipPortLog, Model model) {
		model.addAttribute("shipPortLog", shipPortLog);
		return "data_collect/shiplog/shipPortLogList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ShipPortLog> listData(ShipPortLog shipPortLog, HttpServletRequest request, HttpServletResponse response) {
		shipPortLog.setPage(new Page<>(request, response));
		Page<ShipPortLog> page = shipPortLogService.findPage(shipPortLog);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("shiplog:shipPortLog:view")
	@RequestMapping(value = "form")
	public String form(ShipPortLog shipPortLog, Model model) {
		model.addAttribute("shipPortLog", shipPortLog);
		return "data_collect/shiplog/shipPortLogForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ShipPortLog shipPortLog) {
		shipPortLogService.save(shipPortLog);
		return renderResult(Global.TRUE, text("保存国际航行船舶表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("shiplog:shipPortLog:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ShipPortLog shipPortLog) {
		shipPortLogService.delete(shipPortLog);
		return renderResult(Global.TRUE, text("删除国际航行船舶表成功！"));
	}
	
}