package com.jeesite.modules.data_collect.ship.service;

import java.util.List;

import com.jeesite.common.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.ship.entity.ShipInspection;
import com.jeesite.modules.data_collect.ship.dao.ShipInspectionDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 海船安全检查信息表Service
 * @author 王浩宇
 * @version 2024-06-06
 */
@Service
public class ShipInspectionService extends CrudService<ShipInspectionDao, ShipInspection> {

	/**
	 * 获取单条数据
	 * @param shipInspection
	 * @return
	 */

	@Autowired
	ShipInspectionDao shipInspectionDao;
	@Override
	public ShipInspection get(ShipInspection shipInspection) {
		return super.get(shipInspection);
	}
	
	/**
	 * 查询分页数据
	 * @param shipInspection 查询条件
	 * @param shipInspection page 分页对象
	 * @return
	 */
	@Override
	public Page<ShipInspection> findPage(ShipInspection shipInspection) {
		return super.findPage(shipInspection);
	}
	
	/**
	 * 查询列表数据
	 * @param shipInspection
	 * @return
	 */
	@Override
	public List<ShipInspection> findList(ShipInspection shipInspection) {
		return super.findList(shipInspection);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param shipInspection
	 */
	@Override
	@Transactional
	public void save(ShipInspection shipInspection) {
		super.save(shipInspection);
	}

	/**
	 * 导入数据
	 * @param file 导入的数据文件
	 */
	@Transactional
	public String importData(MultipartFile file, String shipType) {
		if (file == null) {
			throw new ServiceException(text("请选择导入的数据文件！"));
		}

		int successNum = 0;
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();

		try (ExcelImport ei = new ExcelImport(file, 2, 0)) {
			List<ShipInspection> list = ei.getDataList(ShipInspection.class);

			for (ShipInspection shipInspection : list) {
				try {
					ValidatorUtils.validateWithException(shipInspection);
					if (StringUtils.isBlank(shipInspection.getShipId())){
						failureNum++;
						String msg = "<br/>" + failureNum + "、船舶识别号不能为空,导入失败。";
						failureMsg.append(msg);
						continue;
					}
					shipInspection.setShipType(shipType); // Set the ship type here!
					this.save(shipInspection);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + shipInspection.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + shipInspection.getId() + " 导入失败：";
					if (e instanceof ConstraintViolationException) {
						ConstraintViolationException cve = (ConstraintViolationException) e;
						for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
							msg += Global.getText(violation.getMessage()) + " (" + violation.getPropertyPath() + ")";
						}
					} else {
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
		} else {
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}

		return successMsg.toString();
	}


	/**
	 * 更新状态
	 * @param shipInspection
	 */
	@Override
	@Transactional
	public void updateStatus(ShipInspection shipInspection) {
		super.updateStatus(shipInspection);
	}
	
	/**
	 * 删除数据
	 * @param shipInspection
	 */
	@Override
	@Transactional
	public void delete(ShipInspection shipInspection) {
		super.delete(shipInspection);
	}

	public List<ShipInspection> findDistinctList(ShipInspection shipInspection) {
		return shipInspectionDao.findDistinctList(shipInspection);
	}

	/**
	 * 按部门统计船舶检查数据（通过agency_dept表关联）
	 */
	public List<java.util.Map<String, Object>> findShipInspectionStatisticsByDepartment(String startDate, String endDate, String department) {
		return shipInspectionDao.findShipInspectionStatisticsByDepartment(startDate, endDate, department);
	}
}