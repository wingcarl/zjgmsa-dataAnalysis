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
	 * @param importType 导入的类型
	 * @return
	 */
	@Transactional
	public String importData(MultipartFile file, String importType) {
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		
		try(ExcelImport ei = new ExcelImport(file, 2, 0)){
			List<DynamicEnforcementData> list = ei.getDataList(DynamicEnforcementData.class);
			for (DynamicEnforcementData ded : list) {
				try{
					save(ded);
					successNum++;
				}catch(Exception ex){
					failureNum++;
					String msg = "<br/>导入第 " + (successNum + failureNum) + " 条数据:"
					+ (ex.getMessage() != null ? ex.getMessage() : "未知错误");
					failureMsg.append(msg);
					logger.error(msg, ex);
				}
			}
		} catch (Exception e) {
			failureMsg.append(e.getMessage());
		}
		
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据导入失败，导入信息如下：");
			return failureMsg.toString();
		} else {
			successMsg.append("恭喜您，数据已全部导入成功！共 " + successNum + " 条");
			return successMsg.toString();
		}
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