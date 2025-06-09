package com.jeesite.modules.data_collect.danger.web;

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
import com.jeesite.modules.data_collect.danger.entity.DangerCargoDeclaration;
import com.jeesite.modules.data_collect.danger.service.DangerCargoDeclarationService;

/**
 * 危险货物申报表Controller
 * @author 王浩宇
 * @version 2025-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/danger/dangerCargoDeclaration")
public class DangerCargoDeclarationController extends BaseController {

	@Autowired
	private DangerCargoDeclarationService dangerCargoDeclarationService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DangerCargoDeclaration get(String id, boolean isNewRecord) {
		return dangerCargoDeclarationService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("danger:dangerCargoDeclaration:view")
	@RequestMapping(value = {"list", ""})
	public String list(DangerCargoDeclaration dangerCargoDeclaration, Model model) {
		model.addAttribute("dangerCargoDeclaration", dangerCargoDeclaration);
		return "data_collect/danger/dangerCargoDeclarationList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("danger:dangerCargoDeclaration:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DangerCargoDeclaration> listData(DangerCargoDeclaration dangerCargoDeclaration, HttpServletRequest request, HttpServletResponse response) {
		dangerCargoDeclaration.setPage(new Page<>(request, response));
		Page<DangerCargoDeclaration> page = dangerCargoDeclarationService.findPage(dangerCargoDeclaration);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("danger:dangerCargoDeclaration:view")
	@RequestMapping(value = "form")
	public String form(DangerCargoDeclaration dangerCargoDeclaration, Model model) {
		model.addAttribute("dangerCargoDeclaration", dangerCargoDeclaration);
		return "data_collect/danger/dangerCargoDeclarationForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("danger:dangerCargoDeclaration:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DangerCargoDeclaration dangerCargoDeclaration) {
		dangerCargoDeclarationService.save(dangerCargoDeclaration);
		return renderResult(Global.TRUE, text("保存危险货物申报表成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("danger:dangerCargoDeclaration:view")
	@RequestMapping(value = "exportData")
	public void exportData(DangerCargoDeclaration dangerCargoDeclaration, HttpServletResponse response) {
		List<DangerCargoDeclaration> list = dangerCargoDeclarationService.findList(dangerCargoDeclaration);
		String fileName = "危险货物申报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("危险货物申报表", DangerCargoDeclaration.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("danger:dangerCargoDeclaration:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		DangerCargoDeclaration dangerCargoDeclaration = new DangerCargoDeclaration();
		List<DangerCargoDeclaration> list = ListUtils.newArrayList(dangerCargoDeclaration);
		String fileName = "危险货物申报表模板.xlsx";
		try(ExcelExport ee = new ExcelExport("危险货物申报表", DangerCargoDeclaration.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("danger:dangerCargoDeclaration:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String remarkType) {
		try {
			String message = dangerCargoDeclarationService.importData(file, remarkType);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("danger:dangerCargoDeclaration:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DangerCargoDeclaration dangerCargoDeclaration) {
		dangerCargoDeclarationService.delete(dangerCargoDeclaration);
		return renderResult(Global.TRUE, text("删除危险货物申报表成功！"));
	}
	
}