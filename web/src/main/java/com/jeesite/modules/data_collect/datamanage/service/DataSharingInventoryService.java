package com.jeesite.modules.data_collect.datamanage.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.data_collect.datamanage.entity.DataSharingInventory;
import com.jeesite.modules.data_collect.datamanage.dao.DataSharingInventoryDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 数据共享清单Service
 * @author Dylan Wang
 * @version 2024-06-07
 */
@Service
public class DataSharingInventoryService extends CrudService<DataSharingInventoryDao, DataSharingInventory> {
	
	/**
	 * 获取单条数据
	 * @param dataSharingInventory
	 * @return
	 */
	@Override
	public DataSharingInventory get(DataSharingInventory dataSharingInventory) {
		return super.get(dataSharingInventory);
	}
	
	/**
	 * 查询分页数据
	 * @param dataSharingInventory 查询条件
	 * @param dataSharingInventory page 分页对象
	 * @return
	 */
	@Override
	public Page<DataSharingInventory> findPage(DataSharingInventory dataSharingInventory) {
		return super.findPage(dataSharingInventory);
	}
	
	/**
	 * 查询列表数据
	 * @param dataSharingInventory
	 * @return
	 */
	@Override
	public List<DataSharingInventory> findList(DataSharingInventory dataSharingInventory) {
		return super.findList(dataSharingInventory);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param dataSharingInventory
	 */
	@Override
	@Transactional
	public void save(DataSharingInventory dataSharingInventory) {
		super.save(dataSharingInventory);
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
			List<DataSharingInventory> list = ei.getDataList(DataSharingInventory.class);
			for (DataSharingInventory dataSharingInventory : list) {
				try{
					ValidatorUtils.validateWithException(dataSharingInventory);
					this.save(dataSharingInventory);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + dataSharingInventory.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + dataSharingInventory.getId() + " 导入失败：";
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
	 * @param dataSharingInventory
	 */
	@Override
	@Transactional
	public void updateStatus(DataSharingInventory dataSharingInventory) {
		super.updateStatus(dataSharingInventory);
	}
	
	/**
	 * 删除数据
	 * @param dataSharingInventory
	 */
	@Override
	@Transactional
	public void delete(DataSharingInventory dataSharingInventory) {
		super.delete(dataSharingInventory);
	}
	
}