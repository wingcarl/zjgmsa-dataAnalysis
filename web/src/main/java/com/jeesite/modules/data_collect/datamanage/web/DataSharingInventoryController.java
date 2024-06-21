package com.jeesite.modules.data_collect.datamanage.web;

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
import com.jeesite.modules.data_collect.datamanage.entity.DataSharingInventory;
import com.jeesite.modules.data_collect.datamanage.entity.DataMetricsWeekly;
import com.jeesite.modules.data_collect.datamanage.service.DataSharingInventoryService;

/**
 * 数据共享清单Controller
 * @author Dylan Wang
 * @version 2024-06-21
 */
@Controller
@RequestMapping(value = "${adminPath}/datamanage/dataSharingInventory")
public class DataSharingInventoryController extends BaseController {

	@Autowired
	private DataSharingInventoryService dataSharingInventoryService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DataSharingInventory get(String id, boolean isNewRecord) {
		return dataSharingInventoryService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("datamanage:dataSharingInventory:view")
	@RequestMapping(value = {"list", ""})
	public String list(DataSharingInventory dataSharingInventory, Model model) {
		model.addAttribute("dataSharingInventory", dataSharingInventory);
		return "data_collect/datamanage/dataSharingInventoryList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("datamanage:dataSharingInventory:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DataSharingInventory> listData(DataSharingInventory dataSharingInventory, HttpServletRequest request, HttpServletResponse response) {
		dataSharingInventory.setPage(new Page<>(request, response));
		Page<DataSharingInventory> page = dataSharingInventoryService.findPage(dataSharingInventory);
		return page;
	}
	
	/**
	 * 查询子表数据
	 */
	@RequiresPermissions("datamanage:dataSharingInventory:view")
	@RequestMapping(value = "dataMetricsWeeklyListData")
	@ResponseBody
	public Page<DataMetricsWeekly> subListData(DataMetricsWeekly dataMetricsWeekly, HttpServletRequest request, HttpServletResponse response) {
		dataMetricsWeekly.setPage(new Page<>(request, response));
		Page<DataMetricsWeekly> page = dataSharingInventoryService.findSubPage(dataMetricsWeekly);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("datamanage:dataSharingInventory:view")
	@RequestMapping(value = "form")
	public String form(DataSharingInventory dataSharingInventory, Model model) {
		model.addAttribute("dataSharingInventory", dataSharingInventory);
		return "data_collect/datamanage/dataSharingInventoryForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("datamanage:dataSharingInventory:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DataSharingInventory dataSharingInventory) {
		dataSharingInventoryService.save(dataSharingInventory);
		return renderResult(Global.TRUE, text("保存数据共享清单成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("datamanage:dataSharingInventory:view")
	@RequestMapping(value = "exportData")
	public void exportData(DataSharingInventory dataSharingInventory, HttpServletResponse response) {
		List<DataSharingInventory> list = dataSharingInventoryService.findList(dataSharingInventory);
		String fileName = "数据共享清单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("数据共享清单", DataSharingInventory.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("datamanage:dataSharingInventory:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		DataSharingInventory dataSharingInventory = new DataSharingInventory();
		List<DataSharingInventory> list = ListUtils.newArrayList(dataSharingInventory);
		String fileName = "数据共享清单模板.xlsx";
		try(ExcelExport ee = new ExcelExport("数据共享清单", DataSharingInventory.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("datamanage:dataSharingInventory:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = dataSharingInventoryService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("datamanage:dataSharingInventory:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DataSharingInventory dataSharingInventory) {
		dataSharingInventoryService.delete(dataSharingInventory);
		return renderResult(Global.TRUE, text("删除数据共享清单成功！"));
	}
	
}