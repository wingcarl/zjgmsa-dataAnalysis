package com.jeesite.modules.data_collect.shiplog.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.shiplog.entity.ShipPortLog;
import com.jeesite.modules.data_collect.shiplog.dao.ShipPortLogDao;

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
	
}