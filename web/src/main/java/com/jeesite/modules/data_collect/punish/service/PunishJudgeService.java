package com.jeesite.modules.data_collect.punish.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.punish.entity.PunishJudge;
import com.jeesite.modules.data_collect.punish.dao.PunishJudgeDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 处罚决定Service
 * @author 王浩宇
 * @version 2025-02-07
 */
@Service
public class PunishJudgeService extends CrudService<PunishJudgeDao, PunishJudge> {
	
	/**
	 * 获取单条数据
	 * @param punishJudge
	 * @return
	 */
	@Override
	public PunishJudge get(PunishJudge punishJudge) {
		return super.get(punishJudge);
	}
	
	/**
	 * 查询分页数据
	 * @param punishJudge 查询条件
	 * @param punishJudge page 分页对象
	 * @return
	 */
	@Override
	public Page<PunishJudge> findPage(PunishJudge punishJudge) {
		return super.findPage(punishJudge);
	}
	
	/**
	 * 查询列表数据
	 * @param punishJudge
	 * @return
	 */
	@Override
	public List<PunishJudge> findList(PunishJudge punishJudge) {
		return super.findList(punishJudge);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param punishJudge
	 */
	@Override
	@Transactional
	public void save(PunishJudge punishJudge) {
		super.save(punishJudge);
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
			List<PunishJudge> list = ei.getDataList(PunishJudge.class);
			for (PunishJudge punishJudge : list) {
				try{
					ValidatorUtils.validateWithException(punishJudge);
					this.save(punishJudge);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + punishJudge.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + punishJudge.getId() + " 导入失败：";
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
	 * @param punishJudge
	 */
	@Override
	@Transactional
	public void updateStatus(PunishJudge punishJudge) {
		super.updateStatus(punishJudge);
	}
	
	/**
	 * 删除数据
	 * @param punishJudge
	 */
	@Override
	@Transactional
	public void delete(PunishJudge punishJudge) {
		super.delete(punishJudge);
	}
	
	/**
	 * 获取所有违法情节选项
	 */
	public List<String> findViolationCircumstances() {
		return dao.findViolationCircumstances();
	}
	
	/**
	 * 获取所有船舶种类选项
	 */
	public List<String> findShipTypes() {
		return dao.findShipTypes();
	}
	
	/**
	 * 获取所有处罚机构选项
	 */
	public List<String> findPenaltyAgencies() {
		return dao.findPenaltyAgencies();
	}
	
	/**
	 * 获取所有案由选项
	 */
	public List<String> findCaseReasons() {
		return dao.findCaseReasons();
	}
	
	/**
	 * 获取所有业务类型选项
	 */
	public List<String> findManagementCategories() {
		return dao.findManagementCategories();
	}
	
	/**
	 * 获取所有部门列表（通过agency_dept表）
	 */
	public List<String> findAllDepartments() {
		return dao.findAllDepartments();
	}
	
	/**
	 * 按部门统计处罚数量（通过agency_dept表关联）
	 */
	public List<java.util.Map<String, Object>> findPenaltyCountsByDepartment(PunishJudge punishJudge) {
		return dao.findPenaltyCountsByDepartment(punishJudge);
	}
	
	/**
	 * 按部门统计处罚金额（通过agency_dept表关联）
	 */
	public List<java.util.Map<String, Object>> findPenaltyAmountsByDepartment(PunishJudge punishJudge) {
		return dao.findPenaltyAmountsByDepartment(punishJudge);
	}
	
	/**
	 * 按部门统计平均处罚金额（通过agency_dept表关联）
	 */
	public List<java.util.Map<String, Object>> findAveragePenaltyAmountsByDepartment(PunishJudge punishJudge) {
		return dao.findAveragePenaltyAmountsByDepartment(punishJudge);
	}
	
	/**
	 * 按部门统计重点违法行为占比（通过agency_dept和case_key表关联）
	 */
	public List<java.util.Map<String, Object>> findKeyViolationRatioByDepartment(PunishJudge punishJudge) {
		return dao.findKeyViolationRatioByDepartment(punishJudge);
	}
}