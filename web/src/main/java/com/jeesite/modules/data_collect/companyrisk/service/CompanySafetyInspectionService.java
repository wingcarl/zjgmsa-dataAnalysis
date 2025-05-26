package com.jeesite.modules.data_collect.companyrisk.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.companyrisk.entity.CompanySafetyInspection;
import com.jeesite.modules.data_collect.companyrisk.dao.CompanySafetyInspectionDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 安全隐患与风险排查记录表Service
 * @author 王浩宇
 * @version 2025-05-26
 */
@Service
public class CompanySafetyInspectionService extends CrudService<CompanySafetyInspectionDao, CompanySafetyInspection> {
	
	/**
	 * 获取单条数据
	 * @param companySafetyInspection
	 * @return
	 */
	@Override
	public CompanySafetyInspection get(CompanySafetyInspection companySafetyInspection) {
		return super.get(companySafetyInspection);
	}
	
	/**
	 * 查询分页数据
	 * @param companySafetyInspection 查询条件
	 * @param companySafetyInspection page 分页对象
	 * @return
	 */
	@Override
	public Page<CompanySafetyInspection> findPage(CompanySafetyInspection companySafetyInspection) {
		return super.findPage(companySafetyInspection);
	}
	
	/**
	 * 查询列表数据
	 * @param companySafetyInspection
	 * @return
	 */
	@Override
	public List<CompanySafetyInspection> findList(CompanySafetyInspection companySafetyInspection) {
		return super.findList(companySafetyInspection);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param companySafetyInspection
	 */
	@Override
	@Transactional
	public void save(CompanySafetyInspection companySafetyInspection) {
		super.save(companySafetyInspection);
	}

	/**
	 * 导入数据
	 * @param file 导入的数据文件
	 */
	@Transactional
	public String importData(MultipartFile file) {
		if (file == null){
			throw new ServiceException(text("请选择导入的数据文件！"));
		}
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try(ExcelImport ei = new ExcelImport(file, 2, 0)){
			List<CompanySafetyInspection> list = ei.getDataList(CompanySafetyInspection.class);
			for (CompanySafetyInspection companySafetyInspection : list) {
				try{
					ValidatorUtils.validateWithException(companySafetyInspection);
					this.save(companySafetyInspection);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + companySafetyInspection.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + companySafetyInspection.getId() + " 导入失败：";
					if (e instanceof ConstraintViolationException){
						ConstraintViolationException cve = (ConstraintViolationException)e;
						for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
							msg += Global.getText(violation.getMessage()) + " ("+violation.getPropertyPath()+")";
						}
					}else{
						msg += e.getMessage();
					}
					failureMsg.append(msg);
					logger.error(msg, e);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			failureMsg.append(e.getMessage());
			return failureMsg.toString();
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new ServiceException(failureMsg.toString());
		}else{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}
	
	/**
	 * 更新状态
	 * @param companySafetyInspection
	 */
	@Override
	@Transactional
	public void updateStatus(CompanySafetyInspection companySafetyInspection) {
		super.updateStatus(companySafetyInspection);
	}
	
	/**
	 * 删除数据
	 * @param companySafetyInspection
	 */
	@Override
	@Transactional
	public void delete(CompanySafetyInspection companySafetyInspection) {
		super.delete(companySafetyInspection);
	}
	
	/**
	 * 获取风险隐患统计数据
	 */
	public Map<String, Object> getStatisticsData(String startDate, String endDate) {
		Map<String, Object> params = new HashMap<>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return dao.getStatisticsData(params);
	}
	
	/**
	 * 获取企业自查统计数据（按部门）
	 */
	public List<Map<String, Object>> getCompanySelfCheckStats(String startDate, String endDate) {
		Map<String, Object> params = new HashMap<>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return dao.getCompanySelfCheckStats(params);
	}
	
	/**
	 * 获取海事排查统计数据（按部门）
	 */
	public List<Map<String, Object>> getMaritimeInspectionStats(String startDate, String endDate) {
		Map<String, Object> params = new HashMap<>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return dao.getMaritimeInspectionStats(params);
	}
	
	/**
	 * 获取综合统计数据（按企业）
	 */
	public List<Map<String, Object>> getComprehensiveStats(String startDate, String endDate) {
		Map<String, Object> params = new HashMap<>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return dao.getComprehensiveStats(params);
	}
	
}