package com.jeesite.modules.data_collect.dynamic.service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

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
			
			// 性能优化：如果需要检查重复记录，先批量查询相关数据
			Map<String, DynamicEnforcementData> existingDataMap = new HashMap<>();
			if ("abnormal".equals(importType) || "case".equals(importType)) {
				existingDataMap = buildExistingDataMap(list);
			}
			
			for (DynamicEnforcementData dynamicEnforcementData : list) {
				try{
					ValidatorUtils.validateWithException(dynamicEnforcementData);

					// 只有在特定导入类型时才检查现有记录
					if ("abnormal".equals(importType) || "case".equals(importType)) {
						// 使用优化后的查找方法
						String key = buildDataKey(dynamicEnforcementData);
						DynamicEnforcementData existingData = existingDataMap.get(key);

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
	 * 优化后的批量查询现有数据方法
	 * 根据导入数据的时间范围，批量查询相关数据，然后在内存中建立索引
	 * @param importDataList 导入的数据列表
	 * @return 现有数据的映射表，key为数据的唯一标识，value为现有数据
	 */
	private Map<String, DynamicEnforcementData> buildExistingDataMap(List<DynamicEnforcementData> importDataList) {
		Map<String, DynamicEnforcementData> resultMap = new HashMap<>();
		
		if (importDataList == null || importDataList.isEmpty()) {
			return resultMap;
		}
		
		// 计算时间范围
		Date minTime = null;
		Date maxTime = null;
		for (DynamicEnforcementData data : importDataList) {
			Date inspectionTime = data.getInspectionTime();
			if (inspectionTime != null) {
				if (minTime == null || inspectionTime.before(minTime)) {
					minTime = inspectionTime;
				}
				if (maxTime == null || inspectionTime.after(maxTime)) {
					maxTime = inspectionTime;
				}
			}
		}
		
		// 如果没有有效的时间数据，返回空Map
		if (minTime == null || maxTime == null) {
			return resultMap;
		}
		
		// 扩展时间范围，避免边界问题（前后各扩展1天）
		Calendar cal = Calendar.getInstance();
		cal.setTime(minTime);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		minTime = cal.getTime();
		
		cal.setTime(maxTime);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		maxTime = cal.getTime();
		
		// 构建查询条件，按时间范围批量查询
		DynamicEnforcementData queryParams = new DynamicEnforcementData();
		queryParams.setInspectionTime_gte(minTime);
		queryParams.setInspectionTime_lte(maxTime);
		
		// 批量查询指定时间范围内的所有数据
		List<DynamicEnforcementData> existingDataList = super.findList(queryParams);
		
		// 在内存中建立索引，以便快速查找
		for (DynamicEnforcementData existingData : existingDataList) {
			String key = buildDataKey(existingData);
			resultMap.put(key, existingData);
		}
		
		return resultMap;
	}
	
	/**
	 * 构建数据的唯一标识key
	 * 基于检查单位、检查时间、检查地点、检查对象、巡航任务名称、大项名称、船舶总吨生成唯一key
	 * @param data 数据对象
	 * @return 唯一标识key
	 */
	private String buildDataKey(DynamicEnforcementData data) {
		if (data == null) {
			return "";
		}
		
		StringBuilder keyBuilder = new StringBuilder();
		keyBuilder.append(data.getInspectionUnit() != null ? data.getInspectionUnit() : "").append("|");
		
		// 格式化检查时间为字符串，精确到分钟
		if (data.getInspectionTime() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			keyBuilder.append(sdf.format(data.getInspectionTime()));
		}
		keyBuilder.append("|");
		
		keyBuilder.append(data.getInspectionLocation() != null ? data.getInspectionLocation() : "").append("|");
		keyBuilder.append(data.getInspectionTarget() != null ? data.getInspectionTarget() : "").append("|");
		keyBuilder.append(data.getCruiseTaskName() != null ? data.getCruiseTaskName() : "").append("|");
		keyBuilder.append(data.getMajorItemName() != null ? data.getMajorItemName() : "").append("|");
		keyBuilder.append(data.getShipGrossTonnage() != null ? data.getShipGrossTonnage() : "");
		
		return keyBuilder.toString();
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
	
	/**
	 * 查找是否存在相同的记录（已废弃，保留用于兼容性）
	 * @param dynamicEnforcementData
	 * @return
	 * @deprecated 此方法性能较差，建议使用 buildExistingDataMap 方法批量处理
	 */
	@Deprecated
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
	
}