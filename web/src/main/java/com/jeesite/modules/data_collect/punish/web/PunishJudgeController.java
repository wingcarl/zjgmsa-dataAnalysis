package com.jeesite.modules.data_collect.punish.web;

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
import com.jeesite.modules.data_collect.punish.entity.PunishJudge;
import com.jeesite.modules.data_collect.punish.service.PunishJudgeService;

/**
 * 处罚决定Controller
 * @author 王浩宇
 * @version 2025-02-07
 */
@Controller
@RequestMapping(value = "${adminPath}/punish/punishJudge")
public class PunishJudgeController extends BaseController {

	@Autowired
	private PunishJudgeService punishJudgeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public PunishJudge get(String id, boolean isNewRecord) {
		return punishJudgeService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = {"list", ""})
	public String list(PunishJudge punishJudge, Model model) {
		model.addAttribute("punishJudge", punishJudge);
		return "data_collect/punish/punishJudgeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<PunishJudge> listData(PunishJudge punishJudge, HttpServletRequest request, HttpServletResponse response) {
		punishJudge.setPage(new Page<>(request, response));
		Page<PunishJudge> page = punishJudgeService.findPage(punishJudge);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = "form")
	public String form(PunishJudge punishJudge, Model model) {
		model.addAttribute("punishJudge", punishJudge);
		return "data_collect/punish/punishJudgeForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("punish:punishJudge:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated PunishJudge punishJudge) {
		punishJudgeService.save(punishJudge);
		return renderResult(Global.TRUE, text("保存处罚决定成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = "exportData")
	public void exportData(PunishJudge punishJudge, HttpServletResponse response) {
		List<PunishJudge> list = punishJudgeService.findList(punishJudge);
		String fileName = "处罚决定" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("处罚决定", PunishJudge.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("punish:punishJudge:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		PunishJudge punishJudge = new PunishJudge();
		List<PunishJudge> list = ListUtils.newArrayList(punishJudge);
		String fileName = "处罚决定模板.xlsx";
		try(ExcelExport ee = new ExcelExport("处罚决定", PunishJudge.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("punish:punishJudge:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = punishJudgeService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("punish:punishJudge:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(PunishJudge punishJudge) {
		punishJudgeService.delete(punishJudge);
		return renderResult(Global.TRUE, text("删除处罚决定成功！"));
	}
	
}