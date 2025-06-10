package com.jeesite.modules.data_collect.danger.service;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.danger.entity.DangerCargoDeclaration;
import com.jeesite.modules.data_collect.danger.dao.DangerCargoDeclarationDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.HashMap;

/**
 * 危险货物申报表Service
 * @author 王浩宇
 * @version 2025-06-08
 */
@Service
public class DangerCargoDeclarationService extends CrudService<DangerCargoDeclarationDao, DangerCargoDeclaration> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取单条数据
	 * @param dangerCargoDeclaration
	 * @return
	 */
	@Override
	public DangerCargoDeclaration get(DangerCargoDeclaration dangerCargoDeclaration) {
		return super.get(dangerCargoDeclaration);
	}
	
	/**
	 * 查询分页数据
	 * @param dangerCargoDeclaration 查询条件
	 * @param dangerCargoDeclaration page 分页对象
	 * @return
	 */
	@Override
	public Page<DangerCargoDeclaration> findPage(DangerCargoDeclaration dangerCargoDeclaration) {
		return super.findPage(dangerCargoDeclaration);
	}
	
	/**
	 * 查询列表数据
	 * @param dangerCargoDeclaration
	 * @return
	 */
	@Override
	public List<DangerCargoDeclaration> findList(DangerCargoDeclaration dangerCargoDeclaration) {
		return super.findList(dangerCargoDeclaration);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param dangerCargoDeclaration
	 */
	@Override
	@Transactional
	public void save(DangerCargoDeclaration dangerCargoDeclaration) {
		super.save(dangerCargoDeclaration);
	}

	/**
	 * 导入数据
	 * @param file 导入的数据文件
	 * @param remarkType 备注类型
	 */
	@Transactional
	public String importData(MultipartFile file, String remarkType) {
		if (file == null){
			throw new ServiceException(text("请选择导入的数据文件！"));
		}
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try(ExcelImport ei = new ExcelImport(file, 2, 0)){
			List<DangerCargoDeclaration> list = ei.getDataList(DangerCargoDeclaration.class);
			for (DangerCargoDeclaration dangerCargoDeclaration : list) {
				try{
					ValidatorUtils.validateWithException(dangerCargoDeclaration);
					// 设置remark类型
					dangerCargoDeclaration.setRemarks(remarkType);
					this.save(dangerCargoDeclaration);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + dangerCargoDeclaration.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + dangerCargoDeclaration.getId() + " 导入失败：";
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
	 * @param dangerCargoDeclaration
	 */
	@Override
	@Transactional
	public void updateStatus(DangerCargoDeclaration dangerCargoDeclaration) {
		super.updateStatus(dangerCargoDeclaration);
	}
	
	/**
	 * 获取指定时间范围内的危险货物申报表数据
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 危险货物申报表列表
	 */
	public List<DangerCargoDeclaration> getDangerCargoDeclarationData(String startDate, String endDate) {
		DangerCargoDeclaration query = new DangerCargoDeclaration();
		try {
			if (startDate != null && !startDate.isEmpty()) {
				query.setDeclarationDate_gte(java.sql.Date.valueOf(startDate));
			}
			if (endDate != null && !endDate.isEmpty()) {
				query.setDeclarationDate_lte(java.sql.Date.valueOf(endDate));
			}
		} catch (Exception e) {
			logger.error("日期格式错误: " + e.getMessage(), e);
			return new ArrayList<>();
		}
		return findList(query);
	}
	
	/**
	 * 根据备注类型和时间范围获取总重量
	 * @param remarkType 备注类型
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 总重量
	 */
	public Double getTotalWeightByRemarkType(String remarkType, String startDate, String endDate) {
		DangerCargoDeclaration query = new DangerCargoDeclaration();
		query.setRemarks(remarkType);
		try {
			if (startDate != null && !startDate.isEmpty()) {
				query.setDeclarationDate_gte(java.sql.Date.valueOf(startDate));
			}
			if (endDate != null && !endDate.isEmpty()) {
				query.setDeclarationDate_lte(java.sql.Date.valueOf(endDate));
			}
		} catch (Exception e) {
			logger.error("日期格式错误: " + e.getMessage(), e);
			return 0.0;
		}
		
		List<DangerCargoDeclaration> list = findList(query);
		return list.stream()
				.filter(item -> item.getTotalWeight() != null)
				.mapToDouble(DangerCargoDeclaration::getTotalWeight)
				.sum();
	}
	
	/**
	 * 删除数据
	 * @param dangerCargoDeclaration
	 */
	@Override
	@Transactional
	public void delete(DangerCargoDeclaration dangerCargoDeclaration) {
		super.delete(dangerCargoDeclaration);
	}
	
	/**
	 * 根据备注类型和时间范围获取各agency的总重量统计
	 * @param remarkType 备注类型
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return agency重量统计列表
	 */
	public List<Map<String, Object>> getWeightStatsByAgency(String remarkType, String startDate, String endDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT bh.agency, SUM(dcd.total_weight) as totalWeight ");
		sql.append("FROM danger_cargo_declaration dcd ");
		sql.append("LEFT JOIN berth_habor bh ON dcd.berth = bh.berth ");
		sql.append("WHERE dcd.remarks = ? ");
		
		List<Object> params = new ArrayList<>();
		params.add(remarkType);
		
		if (startDate != null && !startDate.isEmpty()) {
			sql.append("AND dcd.declaration_date >= ? ");
			params.add(startDate);
		}
		if (endDate != null && !endDate.isEmpty()) {
			sql.append("AND dcd.declaration_date <= ? ");
			params.add(endDate);
		}
		
		sql.append("AND bh.agency IS NOT NULL ");
		sql.append("GROUP BY bh.agency ");
		sql.append("ORDER BY totalWeight DESC");
		
		return jdbcTemplate.queryForList(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据备注类型、agency和时间范围获取各habor的总重量统计
	 * @param remarkType 备注类型
	 * @param agency 机构
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return habor重量统计列表
	 */
	public List<Map<String, Object>> getWeightStatsByHabor(String remarkType, String agency, String startDate, String endDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT bh.habor, SUM(dcd.total_weight) as totalWeight ");
		sql.append("FROM danger_cargo_declaration dcd ");
		sql.append("LEFT JOIN berth_habor bh ON dcd.berth = bh.berth ");
		sql.append("WHERE dcd.remarks = ? ");
		sql.append("AND bh.agency = ? ");
		
		List<Object> params = new ArrayList<>();
		params.add(remarkType);
		params.add(agency);
		
		if (startDate != null && !startDate.isEmpty()) {
			sql.append("AND dcd.declaration_date >= ? ");
			params.add(startDate);
		}
		if (endDate != null && !endDate.isEmpty()) {
			sql.append("AND dcd.declaration_date <= ? ");
			params.add(endDate);
		}
		
		sql.append("AND bh.habor IS NOT NULL ");
		sql.append("GROUP BY bh.habor ");
		sql.append("ORDER BY totalWeight DESC");
		
		return jdbcTemplate.queryForList(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据备注类型和筛选条件获取各货物名称的总重量统计
	 * @param remarkType 备注类型
	 * @param cargoFlowDirection 货物流向
	 * @param habor 港口
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 货物名称重量统计列表
	 */
	public List<Map<String, Object>> getWeightStatsByCargoName(String remarkType, String cargoFlowDirection, String habor, String startDate, String endDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT dcd.cargo_name, SUM(dcd.total_weight) as totalWeight ");
		sql.append("FROM danger_cargo_declaration dcd ");
		sql.append("LEFT JOIN berth_habor bh ON dcd.berth = bh.berth ");
		sql.append("WHERE dcd.remarks = ? ");
		
		List<Object> params = new ArrayList<>();
		params.add(remarkType);
		
		if (cargoFlowDirection != null && !cargoFlowDirection.isEmpty() && !"all".equals(cargoFlowDirection)) {
			sql.append("AND dcd.cargo_flow_direction = ? ");
			params.add(cargoFlowDirection);
		}
		
		if (habor != null && !habor.isEmpty() && !"all".equals(habor)) {
			sql.append("AND bh.habor = ? ");
			params.add(habor);
		}
		
		if (startDate != null && !startDate.isEmpty()) {
			sql.append("AND dcd.declaration_date >= ? ");
			params.add(startDate);
		}
		if (endDate != null && !endDate.isEmpty()) {
			sql.append("AND dcd.declaration_date <= ? ");
			params.add(endDate);
		}
		
		sql.append("AND dcd.cargo_name IS NOT NULL ");
		sql.append("AND dcd.cargo_name != '' ");
		sql.append("GROUP BY dcd.cargo_name ");
		sql.append("ORDER BY totalWeight DESC");
		
		return jdbcTemplate.queryForList(sql.toString(), params.toArray());
	}
	
	/**
	 * 获取所有港口列表
	 * @return 港口列表
	 */
	public List<Map<String, Object>> getAllHabors() {
		String sql = "SELECT DISTINCT habor FROM berth_habor WHERE habor IS NOT NULL AND habor != '' ORDER BY habor";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 获取所有货物流向列表
	 * @return 货物流向列表
	 */
	public List<Map<String, Object>> getAllCargoFlowDirections() {
		String sql = "SELECT DISTINCT cargo_flow_direction FROM danger_cargo_declaration WHERE cargo_flow_direction IS NOT NULL AND cargo_flow_direction != '' ORDER BY cargo_flow_direction";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 获取港口吞吐量统计数据
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 港口吞吐量统计数据
	 */
	public List<Map<String, Object>> getPortThroughputStats(String startDate, String endDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("    bh.agency, ");
		sql.append("    bh.habor, ");
		sql.append("    COALESCE(SUM(CASE WHEN dcd.remarks = '散装液体' THEN dcd.total_weight ELSE 0 END), 0) as bulkLiquid, ");
		sql.append("    COALESCE(SUM(CASE WHEN dcd.remarks = '散装固体' THEN dcd.total_weight ELSE 0 END), 0) as bulkSolid, ");
		sql.append("    COALESCE(SUM(CASE WHEN dcd.remarks = '包装货物' THEN dcd.total_weight ELSE 0 END), 0) as packagedCargo ");
		sql.append("FROM berth_habor bh ");
		sql.append("LEFT JOIN danger_cargo_declaration dcd ON bh.berth = dcd.berth ");
		sql.append("    AND dcd.declaration_date >= ? AND dcd.declaration_date <= ? ");
		sql.append("WHERE bh.agency IS NOT NULL AND bh.habor IS NOT NULL ");
		sql.append("GROUP BY bh.agency, bh.habor ");
		sql.append("ORDER BY bh.agency, bh.habor");
		
		return jdbcTemplate.queryForList(sql.toString(), startDate, endDate);
	}
	
	/**
	 * 获取港口吞吐量对比统计数据（包含环比和同比）
	 * @param currentStartDate 当前期间开始日期
	 * @param currentEndDate 当前期间结束日期
	 * @param lastStartDate 上期开始日期
	 * @param lastEndDate 上期结束日期
	 * @param lastYearStartDate 去年同期开始日期
	 * @param lastYearEndDate 去年同期结束日期
	 * @return 港口吞吐量对比统计数据
	 */
	public List<Map<String, Object>> getPortThroughputCompareStats(
			String currentStartDate, String currentEndDate,
			String lastStartDate, String lastEndDate,
			String lastYearStartDate, String lastYearEndDate) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("    bh.agency, ");
		sql.append("    bh.habor, ");
		// 当前期间数据
		sql.append("    COALESCE(SUM(CASE WHEN dcd1.remarks = '散装液体' THEN dcd1.total_weight ELSE 0 END), 0) as currentBulkLiquid, ");
		sql.append("    COALESCE(SUM(CASE WHEN dcd1.remarks = '散装固体' THEN dcd1.total_weight ELSE 0 END), 0) as currentBulkSolid, ");
		sql.append("    COALESCE(SUM(CASE WHEN dcd1.remarks = '包装货物' THEN dcd1.total_weight ELSE 0 END), 0) as currentPackagedCargo, ");
		// 上期数据
		sql.append("    COALESCE(SUM(CASE WHEN dcd2.remarks = '散装液体' THEN dcd2.total_weight ELSE 0 END), 0) as lastBulkLiquid, ");
		sql.append("    COALESCE(SUM(CASE WHEN dcd2.remarks = '散装固体' THEN dcd2.total_weight ELSE 0 END), 0) as lastBulkSolid, ");
		sql.append("    COALESCE(SUM(CASE WHEN dcd2.remarks = '包装货物' THEN dcd2.total_weight ELSE 0 END), 0) as lastPackagedCargo, ");
		// 去年同期数据
		sql.append("    COALESCE(SUM(CASE WHEN dcd3.remarks = '散装液体' THEN dcd3.total_weight ELSE 0 END), 0) as lastYearBulkLiquid, ");
		sql.append("    COALESCE(SUM(CASE WHEN dcd3.remarks = '散装固体' THEN dcd3.total_weight ELSE 0 END), 0) as lastYearBulkSolid, ");
		sql.append("    COALESCE(SUM(CASE WHEN dcd3.remarks = '包装货物' THEN dcd3.total_weight ELSE 0 END), 0) as lastYearPackagedCargo ");
		
		sql.append("FROM berth_habor bh ");
		// 当前期间关联
		sql.append("LEFT JOIN danger_cargo_declaration dcd1 ON bh.berth = dcd1.berth ");
		sql.append("    AND dcd1.declaration_date >= ? AND dcd1.declaration_date <= ? ");
		// 上期关联
		sql.append("LEFT JOIN danger_cargo_declaration dcd2 ON bh.berth = dcd2.berth ");
		sql.append("    AND dcd2.declaration_date >= ? AND dcd2.declaration_date <= ? ");
		// 去年同期关联
		sql.append("LEFT JOIN danger_cargo_declaration dcd3 ON bh.berth = dcd3.berth ");
		sql.append("    AND dcd3.declaration_date >= ? AND dcd3.declaration_date <= ? ");
		
		sql.append("WHERE bh.agency IS NOT NULL AND bh.habor IS NOT NULL ");
		sql.append("GROUP BY bh.agency, bh.habor ");
		sql.append("ORDER BY bh.agency, bh.habor");
		
		return jdbcTemplate.queryForList(sql.toString(), 
			currentStartDate, currentEndDate,
			lastStartDate, lastEndDate,
			lastYearStartDate, lastYearEndDate);
	}
	
}