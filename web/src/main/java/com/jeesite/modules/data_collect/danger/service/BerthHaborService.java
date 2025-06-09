package com.jeesite.modules.data_collect.danger.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.danger.entity.BerthHabor;
import com.jeesite.modules.data_collect.danger.dao.BerthHaborDao;

/**
 * 泊位港口对应表Service
 * @author 系统自动生成
 * @version 2025-01-20
 */
@Service
public class BerthHaborService extends CrudService<BerthHaborDao, BerthHabor> {
    
    /**
     * 获取单条数据
     * @param berthHabor
     * @return
     */
    @Override
    public BerthHabor get(BerthHabor berthHabor) {
        return super.get(berthHabor);
    }
    
    /**
     * 查询分页数据
     * @param berthHabor 查询条件
     * @param berthHabor page 分页对象
     * @return
     */
    @Override
    public Page<BerthHabor> findPage(BerthHabor berthHabor) {
        return super.findPage(berthHabor);
    }
    
    /**
     * 查询列表数据
     * @param berthHabor
     * @return
     */
    @Override
    public List<BerthHabor> findList(BerthHabor berthHabor) {
        return super.findList(berthHabor);
    }
    
    /**
     * 保存数据（插入或更新）
     * @param berthHabor
     */
    @Override
    @Transactional
    public void save(BerthHabor berthHabor) {
        super.save(berthHabor);
    }
    
    /**
     * 更新状态
     * @param berthHabor
     */
    @Override
    @Transactional
    public void updateStatus(BerthHabor berthHabor) {
        super.updateStatus(berthHabor);
    }
    
    /**
     * 删除数据
     * @param berthHabor
     */
    @Override
    @Transactional
    public void delete(BerthHabor berthHabor) {
        super.delete(berthHabor);
    }
} 