package com.jeesite.modules.data_collect.government.web;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
import com.jeesite.modules.data_collect.government.entity.GovernmentData;
import com.jeesite.modules.data_collect.government.service.GovernmentDataService;

/**
 * 政务服务数据Controller
 * @author 王浩宇
 * @version 2025-04-10
 */
@Controller
@RequestMapping(value = "${adminPath}/government/governmentData")
public class GovernmentDataController extends BaseController {

	@Autowired
	private GovernmentDataService governmentDataService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public GovernmentData get(String id, boolean isNewRecord) {
		return governmentDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("government:governmentData:view")
	@RequestMapping(value = {"list", ""})
	public String list(GovernmentData governmentData, Model model) {
		model.addAttribute("governmentData", governmentData);
		return "data_collect/government/governmentDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("government:governmentData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<GovernmentData> listData(GovernmentData governmentData, HttpServletRequest request, HttpServletResponse response) {
		governmentData.setPage(new Page<>(request, response));
		Page<GovernmentData> page = governmentDataService.findPage(governmentData);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("government:governmentData:view")
	@RequestMapping(value = "form")
	public String form(GovernmentData governmentData, Model model) {
		model.addAttribute("governmentData", governmentData);
		return "data_collect/government/governmentDataForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("government:governmentData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated GovernmentData governmentData) {
		governmentDataService.save(governmentData);
		return renderResult(Global.TRUE, text("保存政务服务数据成功！"));
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("government:governmentData:view")
	@RequestMapping(value = "exportData")
	public void exportData(GovernmentData governmentData, HttpServletResponse response) {
		List<GovernmentData> list = governmentDataService.findList(governmentData);
		String fileName = "政务服务数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("政务服务数据", GovernmentData.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载模板
	 */
	@RequiresPermissions("government:governmentData:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		GovernmentData governmentData = new GovernmentData();
		List<GovernmentData> list = ListUtils.newArrayList(governmentData);
		String fileName = "政务服务数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("政务服务数据", GovernmentData.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入数据
	 */
	@ResponseBody
	@RequiresPermissions("government:governmentData:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file) {
		try {
			String message = governmentDataService.importData(file);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("government:governmentData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(GovernmentData governmentData) {
		governmentDataService.delete(governmentData);
		return renderResult(Global.TRUE, text("删除政务服务数据成功！"));
	}

	/**
	 * 数据大屏页面
	 */
	@RequiresPermissions("government:governmentData:view")
	@RequestMapping(value = "chart")
	public String chart() {
		return "data_collect/government/governmentChart";
	}

	/**
	 * 根据日期获取政务服务数据
	 */
	@RequestMapping("fetchGovernmentData")
	@ResponseBody
	public Map<String, Object> fetchGovernmentData(String currentWeekStartDate, String lastWeekStartDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);

		// 查询本周的数据
		GovernmentData govDataCurrent = new GovernmentData();
		LocalDate currentStartLocalDate = LocalDate.parse(currentWeekStartDate, formatter);
		govDataCurrent.setDataTime_gte(DateUtils.asDate(currentStartLocalDate));
		govDataCurrent.setDataTime_lte(DateUtils.asDate(currentStartLocalDate.plusDays(6)));
		List<GovernmentData> currentWeekData = governmentDataService.findList(govDataCurrent);

		// 查询上周的数据
		GovernmentData govDataLast = new GovernmentData();
		LocalDate lastStartLocalDate = LocalDate.parse(lastWeekStartDate, formatter);
		govDataLast.setDataTime_gte(DateUtils.asDate(lastStartLocalDate));
		govDataLast.setDataTime_lte(DateUtils.asDate(lastStartLocalDate.plusDays(6)));
		List<GovernmentData> lastWeekData = governmentDataService.findList(govDataLast);

		// 处理数据并返回结果
		return processGovernmentData(currentWeekData, lastWeekData);
	}

	/**
	 * 处理政务服务数据，整合为前端所需格式
	 */
	private Map<String, Object> processGovernmentData(List<GovernmentData> currentWeekData, List<GovernmentData> lastWeekData) {
		Map<String, Object> result = new HashMap<>();

		// 定义服务项目类别
		List<String> categories = Arrays.asList(
				"船员证书签发",
				"初审船舶登记",
				"船舶制式无线电台执照",
				"船载危险货物申报审批",
				"船载危险货物进出港口审批",
				"国际航行船舶证书网上备案",
				"国际航行船舶进出口岸查验",
				"国际航行船舶进口岸审批",
				"海域或者内河通航水域、岸线施工作业许可",
				"内河船舶洗舱作业核查",
				"洗舱作业申报",
				"换货申报",
				"发现洗舱申报问题"
		);
		result.put("categories", categories);

		// 汇总本周数据和上周数据
		long[] lastWeekSums = new long[categories.size()];
		long[] currentWeekValues = new long[categories.size()];

		// 处理上周数据
		for (GovernmentData data : lastWeekData) {
			lastWeekSums[0] += data.getCrewCertificateIssuance() != null ? data.getCrewCertificateIssuance() : 0;
			lastWeekSums[1] += data.getShipRegistrationPreliminaryReview() != null ? data.getShipRegistrationPreliminaryReview() : 0;
			lastWeekSums[2] += data.getShipRadioLicenseIssuance() != null ? data.getShipRadioLicenseIssuance() : 0;
			lastWeekSums[3] += data.getShipDangerousCargoApproval() != null ? data.getShipDangerousCargoApproval() : 0;
			lastWeekSums[4] += data.getShipDangerousCargoPortEntryExitApproval() != null ? data.getShipDangerousCargoPortEntryExitApproval() : 0;
			lastWeekSums[5] += data.getInternationalShipCertificateOnlineFiling() != null ? data.getInternationalShipCertificateOnlineFiling() : 0;
			lastWeekSums[6] += data.getInternationalShipInspection() != null ? data.getInternationalShipInspection() : 0;
			lastWeekSums[7] += data.getInternationalShipPortEntryApproval() != null ? data.getInternationalShipPortEntryApproval() : 0;
			lastWeekSums[8] += data.getWaterAreaConstructionPermit() != null ? data.getWaterAreaConstructionPermit() : 0;
			lastWeekSums[9] += data.getInlandShipTankCleaningVerification() != null ? data.getInlandShipTankCleaningVerification() : 0;
			lastWeekSums[10] += data.getTankCleaningDeclaration() != null ? data.getTankCleaningDeclaration() : 0;
			lastWeekSums[11] += data.getCargoExchangeDeclaration() != null ? data.getCargoExchangeDeclaration() : 0;
			lastWeekSums[12] += data.getTankCleaningDeclarationIssues() != null ? data.getTankCleaningDeclarationIssues() : 0;
		}

		// 处理本周数据
		for (GovernmentData data : currentWeekData) {
			currentWeekValues[0] += data.getCrewCertificateIssuance() != null ? data.getCrewCertificateIssuance() : 0;
			currentWeekValues[1] += data.getShipRegistrationPreliminaryReview() != null ? data.getShipRegistrationPreliminaryReview() : 0;
			currentWeekValues[2] += data.getShipRadioLicenseIssuance() != null ? data.getShipRadioLicenseIssuance() : 0;
			currentWeekValues[3] += data.getShipDangerousCargoApproval() != null ? data.getShipDangerousCargoApproval() : 0;
			currentWeekValues[4] += data.getShipDangerousCargoPortEntryExitApproval() != null ? data.getShipDangerousCargoPortEntryExitApproval() : 0;
			currentWeekValues[5] += data.getInternationalShipCertificateOnlineFiling() != null ? data.getInternationalShipCertificateOnlineFiling() : 0;
			currentWeekValues[6] += data.getInternationalShipInspection() != null ? data.getInternationalShipInspection() : 0;
			currentWeekValues[7] += data.getInternationalShipPortEntryApproval() != null ? data.getInternationalShipPortEntryApproval() : 0;
			currentWeekValues[8] += data.getWaterAreaConstructionPermit() != null ? data.getWaterAreaConstructionPermit() : 0;
			currentWeekValues[9] += data.getInlandShipTankCleaningVerification() != null ? data.getInlandShipTankCleaningVerification() : 0;
			currentWeekValues[10] += data.getTankCleaningDeclaration() != null ? data.getTankCleaningDeclaration() : 0;
			currentWeekValues[11] += data.getCargoExchangeDeclaration() != null ? data.getCargoExchangeDeclaration() : 0;
			currentWeekValues[12] += data.getTankCleaningDeclarationIssues() != null ? data.getTankCleaningDeclarationIssues() : 0;
		}

		// 按照参考代码中的方式格式化返回数据
		List<Object> currentWeekDataWithRate = new ArrayList<>();
		List<Long> lastWeekDataList = new ArrayList<>();

		// 遍历各个类别
		for (int i = 0; i < categories.size(); i++) {
			Long currentVal = currentWeekValues[i];
			Long lastVal = lastWeekSums[i];

			// 计算环比变化率（精确到小数点后2位）
			double changeRate = 0;
			if (lastVal != 0) {
				changeRate = (double) Math.round(((double) (currentVal - lastVal) / lastVal) * 10000) / 100;
			} else if (currentVal > 0) {
				changeRate = 100.00;
			}

			// 添加带环比变化率的本周数据
			final double finalChangeRate = changeRate;
			final Long finalCurrentVal = currentVal;
			currentWeekDataWithRate.add(new HashMap<String, Object>() {{
				put("value", finalCurrentVal);
				put("changeRate", finalChangeRate);
			}});

			// 添加上周数据
			lastWeekDataList.add(lastVal);
		}

		// 设置结果
		result.put("currentWeekData", currentWeekDataWithRate);
		result.put("lastWeekData", lastWeekDataList);

		return result;
	}
	
}