package com.jeesite.modules.data_collect.intelligent.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.intelligent.entity.IntelligentMonitoringAlarm;
import com.jeesite.modules.data_collect.intelligent.dao.IntelligentMonitoringAlarmDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

// 新增导入
import java.text.SimpleDateFormat;
import java.util.Date;
import com.jeesite.common.lang.StringUtils;

/**
 * 智能卡口预警记录表Service
 * @author 王浩宇
 * @version 2025-06-05
 */
@Service
public class IntelligentMonitoringAlarmService extends CrudService<IntelligentMonitoringAlarmDao, IntelligentMonitoringAlarm> {
	
	/**
	 * 获取单条数据
	 * @param intelligentMonitoringAlarm
	 * @return
	 */
	@Override
	public IntelligentMonitoringAlarm get(IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		return super.get(intelligentMonitoringAlarm);
	}
	
	/**
	 * 查询分页数据
	 * @param intelligentMonitoringAlarm 查询条件
	 * @param intelligentMonitoringAlarm page 分页对象
	 * @return
	 */
	@Override
	public Page<IntelligentMonitoringAlarm> findPage(IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		return super.findPage(intelligentMonitoringAlarm);
	}
	
	/**
	 * 查询列表数据
	 * @param intelligentMonitoringAlarm
	 * @return
	 */
	@Override
	public List<IntelligentMonitoringAlarm> findList(IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		return super.findList(intelligentMonitoringAlarm);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param intelligentMonitoringAlarm
	 */
	@Override
	@Transactional
	public void save(IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		super.save(intelligentMonitoringAlarm);
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
			List<IntelligentMonitoringAlarm> list = ei.getDataList(IntelligentMonitoringAlarm.class);
			for (IntelligentMonitoringAlarm intelligentMonitoringAlarm : list) {
				try{
					ValidatorUtils.validateWithException(intelligentMonitoringAlarm);
					this.save(intelligentMonitoringAlarm);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + intelligentMonitoringAlarm.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + intelligentMonitoringAlarm.getId() + " 导入失败：";
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
	 * @param intelligentMonitoringAlarm
	 */
	@Override
	@Transactional
	public void updateStatus(IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		super.updateStatus(intelligentMonitoringAlarm);
	}
	
	/**
	 * 删除数据
	 * @param intelligentMonitoringAlarm
	 */
	@Override
	@Transactional
	public void delete(IntelligentMonitoringAlarm intelligentMonitoringAlarm) {
		super.delete(intelligentMonitoringAlarm);
	}
	
	/**
	 * 按时间范围查询智能卡口数据
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 */
	public List<IntelligentMonitoringAlarm> getIntelligentMonitoringDataByDateRange(String startDate, String endDate) {
		try {
			// 直接获取所有数据，然后在应用层进行过滤
			IntelligentMonitoringAlarm queryParam = new IntelligentMonitoringAlarm();
			List<IntelligentMonitoringAlarm> allData = this.findList(queryParam);
			
			if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
				return allData;
			}
			
			// 在应用层进行时间过滤
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);
			end = new Date(end.getTime() + 24 * 60 * 60 * 1000 - 1); // 结束日期到当天23:59:59
			
			List<IntelligentMonitoringAlarm> filteredData = new java.util.ArrayList<>();
			for (IntelligentMonitoringAlarm item : allData) {
				if (item.getCaptureTime() != null) {
					Date captureTime = item.getCaptureTime();
					if (captureTime.compareTo(start) >= 0 && captureTime.compareTo(end) <= 0) {
						filteredData.add(item);
					}
				}
			}
			
			return filteredData;
		} catch (Exception e) {
			logger.error("按时间范围查询智能卡口数据失败", e);
			return new java.util.ArrayList<>();
		}
	}
	
}