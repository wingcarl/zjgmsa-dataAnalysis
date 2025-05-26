package com.jeesite.modules.data_collect.shiplog.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.shiplog.entity.ShipPortLog;
import com.jeesite.modules.data_collect.shiplog.dao.ShipPortLogDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 国际航行船舶表Service
 * @author 王浩宇
 * @version 2025-05-26
 */
@Service
public class ShipPortLogService extends CrudService<ShipPortLogDao, ShipPortLog> {
	
	/**
	 * 获取单条数据
	 * @param shipPortLog
	 * @return
	 */
	@Override
	public ShipPortLog get(ShipPortLog shipPortLog) {
		return super.get(shipPortLog);
	}
	
	/**
	 * 查询分页数据
	 * @param shipPortLog 查询条件
	 * @param shipPortLog page 分页对象
	 * @return
	 */
	@Override
	public Page<ShipPortLog> findPage(ShipPortLog shipPortLog) {
		return super.findPage(shipPortLog);
	}
	
	/**
	 * 查询列表数据
	 * @param shipPortLog
	 * @return
	 */
	@Override
	public List<ShipPortLog> findList(ShipPortLog shipPortLog) {
		return super.findList(shipPortLog);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param shipPortLog
	 */
	@Override
	@Transactional
	public void save(ShipPortLog shipPortLog) {
		super.save(shipPortLog);
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
			List<ShipPortLog> list = ei.getDataList(ShipPortLog.class);
			for (ShipPortLog shipPortLog : list) {
				try{
					ValidatorUtils.validateWithException(shipPortLog);
					this.save(shipPortLog);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + shipPortLog.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + shipPortLog.getId() + " 导入失败：";
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
	 * @param shipPortLog
	 */
	@Override
	@Transactional
	public void updateStatus(ShipPortLog shipPortLog) {
		super.updateStatus(shipPortLog);
	}
	
	/**
	 * 删除数据
	 * @param shipPortLog
	 */
	@Override
	@Transactional
	public void delete(ShipPortLog shipPortLog) {
		super.delete(shipPortLog);
	}
	
	/**
	 * 获取数据分析统计数据
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 统计数据
	 */
	public Map<String, Object> getAnalysisData(String startDate, String endDate) {
		Map<String, Object> params = new HashMap<>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return dao.getAnalysisData(params);
	}
	
}