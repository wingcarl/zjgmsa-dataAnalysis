package com.jeesite.modules.data_collect.dynamic.service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
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
	 * 执行自定义SQL查询，返回结果列表
	 * @param sql SQL语句
	 * @param params 查询参数
	 * @param clazz 返回对象类型
	 * @return 查询结果列表
	 */
	public <E> List<E> findListBySql(String sql, Map<String, ?> params, Class<E> clazz) {
		if (String.class.equals(clazz)) {
			// 处理String类型的特殊情况
			if (params != null) {
				return (List<E>) jdbcTemplate.queryForList(sql, params.values().toArray(), String.class);
			} else {
				return (List<E>) jdbcTemplate.queryForList(sql, String.class);
			}
		} else {
			// 其他类型暂时返回空列表，可以根据需要扩展
			logger.warn("Unsupported return type for findListBySql: " + clazz.getName());
			return new ArrayList<>();
		}
	}


	/**
	 * 导入数据
	 * @param file 导入的数据文件
	 */
	@Transactional
	public String importData(MultipartFile file, String importType) {
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

					// 只有在特定导入类型时才检查现有记录
					if ("abnormal".equals(importType) || "case".equals(importType)) {
					// 检查是否存在相同的记录
					DynamicEnforcementData existingData = findExistingData(dynamicEnforcementData);

					if (existingData != null) {
						// 更新记录
						if ("abnormal".equals(importType)) {
							existingData.setInspectionResult("异常");
						} else if ("case".equals(importType)) {
							existingData.setInspectionResult("立案调查");
						}
						super.save(existingData); // 使用 save 方法更新
						successNum++;
						successMsg.append("<br/>" + successNum + "、编号 " + existingData.getId() + " 更新成功");
					} else {
							this.save(dynamicEnforcementData);
							successNum++;
							successMsg.append("<br/>" + successNum + "、编号 " + dynamicEnforcementData.getId() + " 导入成功");
						}
					} else {
						// 其他导入类型直接保存新记录
						this.save(dynamicEnforcementData);
						successNum++;
						successMsg.append("<br/>" + successNum + "、编号 " + dynamicEnforcementData.getId() + " 导入成功");
					}
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
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确或未找到匹配记录，错误如下：");
			throw new ServiceException(failureMsg.toString());
		}else{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}

	/**
	 * 查找是否存在相同的记录
	 * @param dynamicEnforcementData
	 * @return
	 */
	private DynamicEnforcementData findExistingData(DynamicEnforcementData dynamicEnforcementData) {
		DynamicEnforcementData params = new DynamicEnforcementData();
		params.setInspectionUnit(dynamicEnforcementData.getInspectionUnit());
		params.setInspectionTime(dynamicEnforcementData.getInspectionTime());
		params.setInspectionLocation(dynamicEnforcementData.getInspectionLocation());
		params.setInspectionTarget(dynamicEnforcementData.getInspectionTarget());
		params.setCruiseTaskName(dynamicEnforcementData.getCruiseTaskName());
		params.setMajorItemName(dynamicEnforcementData.getMajorItemName());
		params.setShipGrossTonnage(dynamicEnforcementData.getShipGrossTonnage());

		List<DynamicEnforcementData> list = super.findList(params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 更新数据状态
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