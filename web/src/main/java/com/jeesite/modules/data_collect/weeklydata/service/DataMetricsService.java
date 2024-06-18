package com.jeesite.modules.data_collect.weeklydata.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.weeklydata.entity.DataMetrics;
import com.jeesite.modules.data_collect.weeklydata.dao.DataMetricsDao;

/**
 * 数据指标表Service
 * @author 王浩宇
 * @version 2024-06-18
 */
@Service
public class DataMetricsService extends CrudService<DataMetricsDao, DataMetrics> {
	
	/**
	 * 获取单条数据
	 * @param dataMetrics
	 * @return
	 */
	@Override
	public DataMetrics get(DataMetrics dataMetrics) {
		return super.get(dataMetrics);
	}
	
	/**
	 * 查询分页数据
	 * @param dataMetrics 查询条件
	 * @param dataMetrics page 分页对象
	 * @return
	 */
	@Override
	public Page<DataMetrics> findPage(DataMetrics dataMetrics) {
		return super.findPage(dataMetrics);
	}
	
	/**
	 * 查询列表数据
	 * @param dataMetrics
	 * @return
	 */
	@Override
	public List<DataMetrics> findList(DataMetrics dataMetrics) {
		return super.findList(dataMetrics);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param dataMetrics
	 */
	@Override
	@Transactional
	public void save(DataMetrics dataMetrics) {
		super.save(dataMetrics);
	}
	
	/**
	 * 更新状态
	 * @param dataMetrics
	 */
	@Override
	@Transactional
	public void updateStatus(DataMetrics dataMetrics) {
		super.updateStatus(dataMetrics);
	}
	
	/**
	 * 删除数据
	 * @param dataMetrics
	 */
	@Override
	@Transactional
	public void delete(DataMetrics dataMetrics) {
		super.delete(dataMetrics);
	}
	
}