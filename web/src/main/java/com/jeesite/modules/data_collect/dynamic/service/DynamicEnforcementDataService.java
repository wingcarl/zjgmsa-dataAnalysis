package com.jeesite.modules.data_collect.dynamic.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.dynamic.entity.DynamicEnforcementData;
import com.jeesite.modules.data_collect.dynamic.dao.DynamicEnforcementDataDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 动态执法数据管理Service
 * @author 王浩宇
 * @version 2025-02-07
 */
@Service
public class DynamicEnforcementDataService extends CrudService<DynamicEnforcementDataDao, DynamicEnforcementData> {
	
	/**
	 * 获取单条数据
	 * @param dynamicEnforcementData
	 * @return
	 */
	@Override
	public DynamicEnforcementData get(DynamicEnforcementData dynamicEnforcementData) {
		return super.get(dynamicEnforcementData);
	}
	
	/**
	 * 查询分页数据
	 * @param dynamicEnforcementData 查询条件
	 * @param dynamicEnforcementData page 分页对象
	 * @return
	 */
	@Override
	public Page<DynamicEnforcementData> findPage(DynamicEnforcementData dynamicEnforcementData) {
		return super.findPage(dynamicEnforcementData);
	}
	
	/**
	 * 查询列表数据
	 * @param dynamicEnforcementData
	 * @return
	 */
	@Override
	public List<DynamicEnforcementData> findList(DynamicEnforcementData dynamicEnforcementData) {
		return super.findList(dynamicEnforcementData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param dynamicEnforcementData
	 */
	@Override
	@Transactional
	public void save(DynamicEnforcementData dynamicEnforcementData) {
		super.save(dynamicEnforcementData);
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
			List<DynamicEnforcementData> list = ei.getDataList(DynamicEnforcementData.class);
			for (DynamicEnforcementData dynamicEnforcementData : list) {
				try{
					ValidatorUtils.validateWithException(dynamicEnforcementData);
					this.save(dynamicEnforcementData);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + dynamicEnforcementData.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + dynamicEnforcementData.getId() + " 导入失败：";
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
	 * @param dynamicEnforcementData
	 */
	@Override
	@Transactional
	public void updateStatus(DynamicEnforcementData dynamicEnforcementData) {
		super.updateStatus(dynamicEnforcementData);
	}
	
	/**
	 * 删除数据
	 * @param dynamicEnforcementData
	 */
	@Override
	@Transactional
	public void delete(DynamicEnforcementData dynamicEnforcementData) {
		super.delete(dynamicEnforcementData);
	}
	
}