package com.jeesite.modules.data_collect.shiponsite.service;

import java.util.List;

import com.jeesite.modules.data_collect.ship.dao.ShipInspectionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.shiponsite.entity.ShipOnSiteInspection;
import com.jeesite.modules.data_collect.shiponsite.dao.ShipOnSiteInspectionDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 船舶现场监督检查表Service
 * @author 王浩宇
 * @version 2024-06-06
 */
@Service
public class ShipOnSiteInspectionService extends CrudService<ShipOnSiteInspectionDao, ShipOnSiteInspection> {

	@Autowired
	ShipOnSiteInspectionDao shipOnsiteInspectionDao;
    public  List<ShipOnSiteInspection> findDistinctList(ShipOnSiteInspection query) {
    	return shipOnsiteInspectionDao.findDistinctList(query);
	}

    /**
	 * 按部门统计现场监督数据（通过agency_dept表关联）
	 */
	public List<java.util.Map<String, Object>> findOnSiteInspectionStatisticsByDepartment(String startDate, String endDate, String department) {
		return shipOnsiteInspectionDao.findOnSiteInspectionStatisticsByDepartment(startDate, endDate, department);
	}

    /**
	 * 获取单条数据
	 * @param shipOnSiteInspection
	 * @return
	 */
	@Override
	public ShipOnSiteInspection get(ShipOnSiteInspection shipOnSiteInspection) {
		return super.get(shipOnSiteInspection);
	}
	
	/**
	 * 查询分页数据
	 * @param shipOnSiteInspection 查询条件
	 * @param shipOnSiteInspection page 分页对象
	 * @return
	 */
	@Override
	public Page<ShipOnSiteInspection> findPage(ShipOnSiteInspection shipOnSiteInspection) {
		return super.findPage(shipOnSiteInspection);
	}
	
	/**
	 * 查询列表数据
	 * @param shipOnSiteInspection
	 * @return
	 */
	@Override
	public List<ShipOnSiteInspection> findList(ShipOnSiteInspection shipOnSiteInspection) {
		return super.findList(shipOnSiteInspection);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param shipOnSiteInspection
	 */
	@Override
	@Transactional
	public void save(ShipOnSiteInspection shipOnSiteInspection) {
		super.save(shipOnSiteInspection);
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
			List<ShipOnSiteInspection> list = ei.getDataList(ShipOnSiteInspection.class);
			for (ShipOnSiteInspection shipOnSiteInspection : list) {
				try{
					ValidatorUtils.validateWithException(shipOnSiteInspection);
					this.save(shipOnSiteInspection);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + shipOnSiteInspection.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + shipOnSiteInspection.getId() + " 导入失败：";
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
	 * @param shipOnSiteInspection
	 */
	@Override
	@Transactional
	public void updateStatus(ShipOnSiteInspection shipOnSiteInspection) {
		super.updateStatus(shipOnSiteInspection);
	}
	
	/**
	 * 删除数据
	 * @param shipOnSiteInspection
	 */
	@Override
	@Transactional
	public void delete(ShipOnSiteInspection shipOnSiteInspection) {
		super.delete(shipOnSiteInspection);
	}
	
}