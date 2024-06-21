package com.jeesite.modules.shipreport.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.shipreport.entity.ShipReport;
import com.jeesite.modules.shipreport.dao.ShipReportDao;

/**
 * 船舶报告表Service
 * @author 王浩宇
 * @version 2024-06-21
 */
@Service
public class ShipReportService extends CrudService<ShipReportDao, ShipReport> {
	
	/**
	 * 获取单条数据
	 * @param shipReport
	 * @return
	 */
	@Override
	public ShipReport get(ShipReport shipReport) {
		return super.get(shipReport);
	}
	
	/**
	 * 查询分页数据
	 * @param shipReport 查询条件
	 * @param shipReport page 分页对象
	 * @return
	 */
	@Override
	public Page<ShipReport> findPage(ShipReport shipReport) {
		return super.findPage(shipReport);
	}
	
	/**
	 * 查询列表数据
	 * @param shipReport
	 * @return
	 */
	@Override
	public List<ShipReport> findList(ShipReport shipReport) {
		return super.findList(shipReport);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param shipReport
	 */
	@Override
	@Transactional
	public void save(ShipReport shipReport) {
		super.save(shipReport);
	}
	
	/**
	 * 更新状态
	 * @param shipReport
	 */
	@Override
	@Transactional
	public void updateStatus(ShipReport shipReport) {
		super.updateStatus(shipReport);
	}
	
	/**
	 * 删除数据
	 * @param shipReport
	 */
	@Override
	@Transactional
	public void delete(ShipReport shipReport) {
		super.delete(shipReport);
	}
	
}